package heros.solver;

import com.google.common.base.Predicate;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import heros.DontSynchronize;
import heros.EdgeFunction;
import heros.EdgeFunctionCache;
import heros.EdgeFunctions;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.MeetLattice;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.edgefunc.EdgeIdentity;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IDESolver.class */
public class IDESolver<N, D, M, V, I extends InterproceduralCFG<N, M>> {
    @SynchronizedBy("consistent lock on field")
    protected Table<N, N, Map<D, Set<D>>> computedIntraPEdges;
    @SynchronizedBy("consistent lock on field")
    protected Table<N, N, Map<D, Set<D>>> computedInterPEdges;
    protected CountingThreadPoolExecutor executor;
    @DontSynchronize("only used by single thread")
    protected int numThreads;
    @SynchronizedBy("thread safe data structure, consistent locking when used")
    protected final JumpFunctions<N, D, V> jumpFn;
    @SynchronizedBy("thread safe data structure, only modified internally")
    protected final I icfg;
    @SynchronizedBy("consistent lock on 'incoming'")
    protected final Table<N, D, Table<N, D, EdgeFunction<V>>> endSummary;
    @SynchronizedBy("consistent lock on field")
    protected final Table<N, D, Map<N, Set<D>>> incoming;
    @SynchronizedBy("use of ConcurrentHashMap")
    protected final Set<N> unbalancedRetSites;
    @DontSynchronize("stateless")
    protected final FlowFunctions<N, D, M> flowFunctions;
    @DontSynchronize("stateless")
    protected final EdgeFunctions<N, D, M, V> edgeFunctions;
    @DontSynchronize("only used by single thread")
    protected final Map<N, Set<D>> initialSeeds;
    @DontSynchronize("stateless")
    protected final MeetLattice<V> valueLattice;
    @DontSynchronize("stateless")
    protected final EdgeFunction<V> allTop;
    @SynchronizedBy("consistent lock on field")
    protected final Table<N, D, V> val;
    @DontSynchronize("benign races")
    public long flowFunctionApplicationCount;
    @DontSynchronize("benign races")
    public long flowFunctionConstructionCount;
    @DontSynchronize("benign races")
    public long propagationCount;
    @DontSynchronize("benign races")
    public long durationFlowFunctionConstruction;
    @DontSynchronize("benign races")
    public long durationFlowFunctionApplication;
    @DontSynchronize("stateless")
    protected final D zeroValue;
    @DontSynchronize("readOnly")
    protected final FlowFunctionCache<N, D, M> ffCache;
    @DontSynchronize("readOnly")
    protected final EdgeFunctionCache<N, D, M, V> efCache;
    @DontSynchronize("readOnly")
    protected final boolean followReturnsPastSeeds;
    @DontSynchronize("readOnly")
    protected final boolean computeValues;
    private boolean recordEdges;
    public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
    protected static final Logger logger = LoggerFactory.getLogger(IDESolver.class);
    public static boolean DEBUG = logger.isDebugEnabled();

    public IDESolver(IDETabulationProblem<N, D, M, V, I> tabulationProblem) {
        this(tabulationProblem, DEFAULT_CACHE_BUILDER, DEFAULT_CACHE_BUILDER);
    }

    public IDESolver(IDETabulationProblem<N, D, M, V, I> tabulationProblem, CacheBuilder flowFunctionCacheBuilder, CacheBuilder edgeFunctionCacheBuilder) {
        this.computedIntraPEdges = HashBasedTable.create();
        this.computedInterPEdges = HashBasedTable.create();
        this.endSummary = HashBasedTable.create();
        this.incoming = HashBasedTable.create();
        this.val = HashBasedTable.create();
        if (logger.isDebugEnabled()) {
            flowFunctionCacheBuilder = flowFunctionCacheBuilder != null ? flowFunctionCacheBuilder.recordStats() : flowFunctionCacheBuilder;
            if (edgeFunctionCacheBuilder != null) {
                edgeFunctionCacheBuilder = edgeFunctionCacheBuilder.recordStats();
            }
        }
        this.zeroValue = tabulationProblem.zeroValue();
        this.icfg = tabulationProblem.interproceduralCFG();
        FlowFunctions<N, D, M> flowFunctions = tabulationProblem.autoAddZero() ? new ZeroedFlowFunctions<>(tabulationProblem.flowFunctions(), tabulationProblem.zeroValue()) : tabulationProblem.flowFunctions();
        EdgeFunctions<N, D, M, V> edgeFunctions = tabulationProblem.edgeFunctions();
        if (flowFunctionCacheBuilder != null) {
            this.ffCache = new FlowFunctionCache<>(flowFunctions, flowFunctionCacheBuilder);
            flowFunctions = this.ffCache;
        } else {
            this.ffCache = null;
        }
        if (edgeFunctionCacheBuilder != null) {
            this.efCache = new EdgeFunctionCache<>(edgeFunctions, edgeFunctionCacheBuilder);
            edgeFunctions = this.efCache;
        } else {
            this.efCache = null;
        }
        this.flowFunctions = flowFunctions;
        this.edgeFunctions = edgeFunctions;
        this.initialSeeds = tabulationProblem.initialSeeds();
        this.unbalancedRetSites = Collections.synchronizedSet(new LinkedHashSet());
        this.valueLattice = tabulationProblem.meetLattice();
        this.allTop = tabulationProblem.allTopFunction();
        this.jumpFn = new JumpFunctions<>(this.allTop);
        this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();
        this.numThreads = Math.max(1, tabulationProblem.numThreads());
        this.computeValues = tabulationProblem.computeValues();
        this.executor = getExecutor();
        this.recordEdges = tabulationProblem.recordEdges();
    }

    public void solve() {
        submitInitialSeeds();
        awaitCompletionComputeValuesAndShutdown();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void submitInitialSeeds() {
        for (Map.Entry<N, Set<D>> seed : this.initialSeeds.entrySet()) {
            N startPoint = seed.getKey();
            for (D val : seed.getValue()) {
                propagate(this.zeroValue, startPoint, val, EdgeIdentity.v(), null, false);
            }
            this.jumpFn.addFunction(this.zeroValue, startPoint, this.zeroValue, EdgeIdentity.v());
        }
    }

    protected void awaitCompletionComputeValuesAndShutdown() {
        long before = System.currentTimeMillis();
        runExecutorAndAwaitCompletion();
        this.durationFlowFunctionConstruction = System.currentTimeMillis() - before;
        if (this.computeValues) {
            long before2 = System.currentTimeMillis();
            computeValues();
            this.durationFlowFunctionApplication = System.currentTimeMillis() - before2;
        }
        if (logger.isDebugEnabled()) {
            printStats();
        }
        this.executor.shutdown();
        runExecutorAndAwaitCompletion();
    }

    private void runExecutorAndAwaitCompletion() {
        try {
            this.executor.awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Throwable exception = this.executor.getException();
        if (exception != null) {
            throw new RuntimeException("There were exceptions during IDE analysis. Exiting.", exception);
        }
    }

    protected void scheduleEdgeProcessing(PathEdge<N, D> edge) {
        if (this.executor.isTerminating()) {
            return;
        }
        this.executor.execute(new PathEdgeProcessingTask(edge));
        this.propagationCount++;
    }

    private void scheduleValueProcessing(IDESolver<N, D, M, V, I>.ValuePropagationTask vpt) {
        if (this.executor.isTerminating()) {
            return;
        }
        this.executor.execute(vpt);
    }

    private void scheduleValueComputationTask(IDESolver<N, D, M, V, I>.ValueComputationTask task) {
        if (this.executor.isTerminating()) {
            return;
        }
        this.executor.execute(task);
    }

    private void saveEdges(N sourceNode, N sinkStmt, D sourceVal, Set<D> destVals, boolean interP) {
        if (!this.recordEdges) {
            return;
        }
        Table<N, N, Map<D, Set<D>>> tgtMap = interP ? this.computedInterPEdges : this.computedIntraPEdges;
        synchronized (tgtMap) {
            Map<D, Set<D>> map = tgtMap.get(sourceNode, sinkStmt);
            if (map == null) {
                map = new LinkedHashMap<>();
                tgtMap.put(sourceNode, sinkStmt, map);
            }
            map.put(sourceVal, new LinkedHashSet(destVals));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void processCall(PathEdge<N, D> edge) {
        Set<Table.Cell<N, D, EdgeFunction<V>>> endSumm;
        D d1 = edge.factAtSource();
        N n = edge.getTarget();
        logger.trace("Processing call to {}", n);
        D d2 = edge.factAtTarget();
        EdgeFunction<V> f = jumpFunction(edge);
        Collection<N> returnSiteNs = this.icfg.getReturnSitesOfCallAt(n);
        Collection<M> callees = this.icfg.getCalleesOfCallAt(n);
        for (M sCalledProcN : callees) {
            FlowFunction<D> function = this.flowFunctions.getCallFlowFunction(n, sCalledProcN);
            this.flowFunctionConstructionCount++;
            Set<D> res = computeCallFlowFunction(function, d1, d2);
            Collection<N> startPointsOf = this.icfg.getStartPointsOf(sCalledProcN);
            for (N sP : startPointsOf) {
                saveEdges(n, sP, d2, res, true);
                for (D d3 : res) {
                    propagate(d3, sP, d3, EdgeIdentity.v(), n, false);
                    synchronized (this.incoming) {
                        addIncoming(sP, d3, n, d2);
                        endSumm = new LinkedHashSet<>(endSummary(sP, d3));
                    }
                    for (Table.Cell<N, D, EdgeFunction<V>> entry : endSumm) {
                        N eP = entry.getRowKey();
                        D d4 = entry.getColumnKey();
                        EdgeFunction<V> fCalleeSummary = entry.getValue();
                        for (N retSiteN : returnSiteNs) {
                            FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
                            this.flowFunctionConstructionCount++;
                            Set<D> returnedFacts = computeReturnFlowFunction(retFunction, d3, d4, n, Collections.singleton(d2));
                            saveEdges(eP, retSiteN, d4, returnedFacts, true);
                            for (D d5 : returnedFacts) {
                                EdgeFunction<V> f4 = this.edgeFunctions.getCallEdgeFunction(n, d2, sCalledProcN, d3);
                                EdgeFunction<V> f5 = this.edgeFunctions.getReturnEdgeFunction(n, sCalledProcN, eP, d4, retSiteN, d5);
                                EdgeFunction<V> fPrime = f4.composeWith(fCalleeSummary).composeWith(f5);
                                D d5_restoredCtx = restoreContextOnReturnedFact(n, d2, d5);
                                propagate(d1, retSiteN, d5_restoredCtx, f.composeWith(fPrime), n, false);
                            }
                        }
                    }
                }
            }
        }
        for (N returnSiteN : returnSiteNs) {
            FlowFunction<D> callToReturnFlowFunction = this.flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
            this.flowFunctionConstructionCount++;
            Set<D> returnFacts = computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2);
            saveEdges(n, returnSiteN, d2, returnFacts, false);
            for (D d32 : returnFacts) {
                EdgeFunction<V> edgeFnE = this.edgeFunctions.getCallToReturnEdgeFunction(n, d2, returnSiteN, d32);
                propagate(d1, returnSiteN, d32, f.composeWith(edgeFnE), n, false);
            }
        }
    }

    protected Set<D> computeCallFlowFunction(FlowFunction<D> callFlowFunction, D d1, D d2) {
        return callFlowFunction.computeTargets(d2);
    }

    protected Set<D> computeCallToReturnFlowFunction(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
        return callToReturnFlowFunction.computeTargets(d2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected void processExit(PathEdge<N, D> edge) {
        N n = edge.getTarget();
        EdgeFunction<V> f = jumpFunction(edge);
        Object methodOf = this.icfg.getMethodOf(n);
        D d1 = edge.factAtSource();
        D d2 = edge.factAtTarget();
        Collection<N> startPointsOf = this.icfg.getStartPointsOf(methodOf);
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (N sP : startPointsOf) {
            synchronized (this.incoming) {
                addEndSummary(sP, d1, n, d2, f);
                for (Map.Entry<N, Set<D>> entry : incoming(d1, sP).entrySet()) {
                    linkedHashMap.put(entry.getKey(), new LinkedHashSet(entry.getValue()));
                }
            }
        }
        for (Map.Entry<N, Set<D>> entry2 : linkedHashMap.entrySet()) {
            N c = entry2.getKey();
            for (Object obj : this.icfg.getReturnSitesOfCallAt(c)) {
                FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(c, methodOf, n, obj);
                this.flowFunctionConstructionCount++;
                for (D d4 : entry2.getValue()) {
                    Set<D> targets = computeReturnFlowFunction(retFunction, d1, d2, c, entry2.getValue());
                    saveEdges(n, obj, d2, targets, true);
                    for (D d5 : targets) {
                        EdgeFunction<V> f4 = this.edgeFunctions.getCallEdgeFunction(c, d4, this.icfg.getMethodOf(n), d1);
                        EdgeFunction<V> f5 = this.edgeFunctions.getReturnEdgeFunction(c, this.icfg.getMethodOf(n), n, d2, obj, d5);
                        EdgeFunction<V> fPrime = f4.composeWith(f).composeWith(f5);
                        synchronized (this.jumpFn) {
                            for (Map.Entry<D, EdgeFunction<V>> valAndFunc : this.jumpFn.reverseLookup(c, d4).entrySet()) {
                                EdgeFunction<V> f3 = valAndFunc.getValue();
                                if (!f3.equalTo(this.allTop)) {
                                    D d3 = valAndFunc.getKey();
                                    D d5_restoredCtx = restoreContextOnReturnedFact(c, d4, d5);
                                    propagate(d3, obj, d5_restoredCtx, f3.composeWith(fPrime), c, false);
                                }
                            }
                        }
                    }
                }
            }
        }
        if (this.followReturnsPastSeeds && linkedHashMap.isEmpty() && d1.equals(this.zeroValue)) {
            Collection<N> callers = this.icfg.getCallersOf(methodOf);
            for (N c2 : callers) {
                for (Object obj2 : this.icfg.getReturnSitesOfCallAt(c2)) {
                    FlowFunction<D> retFunction2 = this.flowFunctions.getReturnFlowFunction(c2, methodOf, n, obj2);
                    this.flowFunctionConstructionCount++;
                    Set<D> targets2 = computeReturnFlowFunction(retFunction2, d1, d2, c2, Collections.singleton(this.zeroValue));
                    saveEdges(n, obj2, d2, targets2, true);
                    for (D d52 : targets2) {
                        EdgeFunction<V> f52 = this.edgeFunctions.getReturnEdgeFunction(c2, this.icfg.getMethodOf(n), n, d2, obj2, d52);
                        propagateUnbalancedReturnFlow(obj2, d52, f.composeWith(f52), c2);
                        this.unbalancedRetSites.add(obj2);
                    }
                }
            }
            if (callers.isEmpty()) {
                FlowFunction<D> retFunction3 = this.flowFunctions.getReturnFlowFunction(null, methodOf, n, null);
                this.flowFunctionConstructionCount++;
                retFunction3.computeTargets(d2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void propagateUnbalancedReturnFlow(N retSiteC, D targetVal, EdgeFunction<V> edgeFunction, N relatedCallSite) {
        propagate(this.zeroValue, retSiteC, targetVal, edgeFunction, relatedCallSite, true);
    }

    protected D restoreContextOnReturnedFact(N callSite, D d4, D d5) {
        if (d5 instanceof LinkedNode) {
            ((LinkedNode) d5).setCallingContext(d4);
        }
        if (d5 instanceof JoinHandlingNode) {
            ((JoinHandlingNode) d5).setCallingContext(d4);
        }
        return d5;
    }

    protected Set<D> computeReturnFlowFunction(FlowFunction<D> retFunction, D d1, D d2, N callSite, Set<D> callerSideDs) {
        return retFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processNormalFlow(PathEdge<N, D> edge) {
        D d1 = edge.factAtSource();
        N n = edge.getTarget();
        D d2 = edge.factAtTarget();
        EdgeFunction<V> f = jumpFunction(edge);
        for (N m : this.icfg.getSuccsOf(n)) {
            FlowFunction<D> flowFunction = this.flowFunctions.getNormalFlowFunction(n, m);
            this.flowFunctionConstructionCount++;
            Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);
            saveEdges(n, m, d2, res, false);
            for (D d3 : res) {
                EdgeFunction<V> fprime = f.composeWith(this.edgeFunctions.getNormalEdgeFunction(n, d2, m, d3));
                propagate(d1, m, d3, fprime, null, false);
            }
        }
    }

    protected Set<D> computeNormalFlowFunction(FlowFunction<D> flowFunction, D d1, D d2) {
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void propagate(D sourceVal, N target, D targetVal, EdgeFunction<V> f, N relatedCallSite, boolean isUnbalancedReturn) {
        EdgeFunction<V> fPrime;
        boolean newFunction;
        synchronized (this.jumpFn) {
            EdgeFunction<V> jumpFnE = this.jumpFn.reverseLookup(target, targetVal).get(sourceVal);
            if (jumpFnE == null) {
                jumpFnE = this.allTop;
            }
            fPrime = jumpFnE.meetWith(f);
            newFunction = !fPrime.equalTo(jumpFnE);
            if (newFunction) {
                this.jumpFn.addFunction(sourceVal, target, targetVal, fPrime);
            }
        }
        if (newFunction) {
            PathEdge<N, D> edge = new PathEdge<>(sourceVal, target, targetVal);
            scheduleEdgeProcessing(edge);
            if (targetVal != this.zeroValue) {
                logger.trace("{} - EDGE: <{},{}> -> <{},{}> - {}", getDebugName(), this.icfg.getMethodOf(target), sourceVal, target, targetVal, fPrime);
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r6v0, types: [heros.solver.IDESolver, heros.solver.IDESolver<N, D, M, V, I extends heros.InterproceduralCFG<N, M>>] */
    private void computeValues() {
        logger.debug("Computing the final values for the edge functions");
        Map<N, Set<D>> allSeeds = new LinkedHashMap<>((Map<? extends N, ? extends Set<D>>) this.initialSeeds);
        for (N unbalancedRetSite : this.unbalancedRetSites) {
            Set<D> seeds = allSeeds.get(unbalancedRetSite);
            if (seeds == null) {
                seeds = new LinkedHashSet<>();
                allSeeds.put(unbalancedRetSite, seeds);
            }
            seeds.add(this.zeroValue);
        }
        for (Map.Entry<N, Set<D>> seed : allSeeds.entrySet()) {
            N startPoint = seed.getKey();
            for (D val : seed.getValue()) {
                setVal(startPoint, val, this.valueLattice.bottomElement());
                Pair<N, D> superGraphNode = new Pair<>(startPoint, val);
                scheduleValueProcessing(new ValuePropagationTask(superGraphNode));
            }
        }
        logger.debug("Computed the final values of the edge functions");
        try {
            this.executor.awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Set<N> allNonCallStartNodes = this.icfg.allNonCallStartNodes();
        Object[] objArr = new Object[allNonCallStartNodes.size()];
        int i = 0;
        for (N n : allNonCallStartNodes) {
            objArr[i] = n;
            i++;
        }
        for (int t = 0; t < this.numThreads; t++) {
            IDESolver<N, D, M, V, I>.ValueComputationTask task = new ValueComputationTask(objArr, t);
            scheduleValueComputationTask(task);
        }
        try {
            this.executor.awaitCompletion();
        } catch (InterruptedException e2) {
            e2.printStackTrace();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void propagateValueAtStart(Pair<N, D> nAndD, N n) {
        D d = nAndD.getO2();
        for (Object obj : this.icfg.getCallsFromWithin(this.icfg.getMethodOf(n))) {
            synchronized (this.jumpFn) {
                Set<Map.Entry<D, EdgeFunction<V>>> entries = this.jumpFn.forwardLookup(d, obj).entrySet();
                for (Map.Entry<D, EdgeFunction<V>> dPAndFP : entries) {
                    D dPrime = dPAndFP.getKey();
                    EdgeFunction<V> fPrime = dPAndFP.getValue();
                    propagateValue(obj, dPrime, fPrime.computeTarget(val(n, d)));
                    this.flowFunctionApplicationCount++;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void propagateValueAtCall(Pair<N, D> nAndD, N n) {
        D d = nAndD.getO2();
        for (M q : this.icfg.getCalleesOfCallAt(n)) {
            FlowFunction<D> callFlowFunction = this.flowFunctions.getCallFlowFunction(n, q);
            this.flowFunctionConstructionCount++;
            for (D dPrime : callFlowFunction.computeTargets(d)) {
                EdgeFunction<V> edgeFn = this.edgeFunctions.getCallEdgeFunction(n, d, q, dPrime);
                for (N startPoint : this.icfg.getStartPointsOf(q)) {
                    propagateValue(startPoint, dPrime, edgeFn.computeTarget(val(n, d)));
                    this.flowFunctionApplicationCount++;
                }
            }
        }
    }

    protected V meetValueAt(N unit, D fact, V curr, V newVal) {
        return this.valueLattice.meet(curr, newVal);
    }

    private void propagateValue(N nHashN, D nHashD, V v) {
        synchronized (this.val) {
            V valNHash = val(nHashN, nHashD);
            V vPrime = meetValueAt(nHashN, nHashD, valNHash, v);
            if (!vPrime.equals(valNHash)) {
                setVal(nHashN, nHashD, vPrime);
                scheduleValueProcessing(new ValuePropagationTask(new Pair(nHashN, nHashD)));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public V val(N nHashN, D nHashD) {
        V l;
        synchronized (this.val) {
            l = this.val.get(nHashN, nHashD);
        }
        return l == null ? this.valueLattice.topElement() : l;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setVal(N nHashN, D nHashD, V l) {
        synchronized (this.val) {
            if (l == this.valueLattice.topElement()) {
                this.val.remove(nHashN, nHashD);
            } else {
                this.val.put(nHashN, nHashD, l);
            }
        }
        logger.debug("VALUE: {} {} {} {}", this.icfg.getMethodOf(nHashN), nHashN, nHashD, l);
    }

    private EdgeFunction<V> jumpFunction(PathEdge<N, D> edge) {
        synchronized (this.jumpFn) {
            EdgeFunction<V> function = this.jumpFn.forwardLookup(edge.factAtSource(), edge.getTarget()).get(edge.factAtTarget());
            if (function == null) {
                return this.allTop;
            }
            return function;
        }
    }

    protected Set<Table.Cell<N, D, EdgeFunction<V>>> endSummary(N sP, D d3) {
        Table<N, D, EdgeFunction<V>> map = this.endSummary.get(sP, d3);
        return map == null ? Collections.emptySet() : map.cellSet();
    }

    private void addEndSummary(N sP, D d1, N eP, D d2, EdgeFunction<V> f) {
        Table<N, D, EdgeFunction<V>> summaries = this.endSummary.get(sP, d1);
        if (summaries == null) {
            summaries = HashBasedTable.create();
            this.endSummary.put(sP, d1, summaries);
        }
        summaries.put(eP, d2, f);
    }

    protected Map<N, Set<D>> incoming(D d1, N sP) {
        synchronized (this.incoming) {
            Map<N, Set<D>> map = this.incoming.get(sP, d1);
            if (map == null) {
                return Collections.emptyMap();
            }
            return map;
        }
    }

    protected void addIncoming(N sP, D d3, N n, D d2) {
        synchronized (this.incoming) {
            Map<N, Set<D>> summaries = this.incoming.get(sP, d3);
            if (summaries == null) {
                summaries = new LinkedHashMap<>();
                this.incoming.put(sP, d3, summaries);
            }
            Set<D> set = summaries.get(n);
            if (set == null) {
                set = new LinkedHashSet<>();
                summaries.put(n, set);
            }
            set.add(d2);
        }
    }

    public V resultAt(N stmt, D value) {
        return this.val.get(stmt, value);
    }

    public Map<D, V> resultsAt(N stmt) {
        return Maps.filterKeys(this.val.row(stmt), new Predicate<D>() { // from class: heros.solver.IDESolver.1
            @Override // com.google.common.base.Predicate
            public boolean apply(D val) {
                return val != IDESolver.this.zeroValue;
            }
        });
    }

    protected CountingThreadPoolExecutor getExecutor() {
        return new CountingThreadPoolExecutor(this.numThreads, Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
    }

    protected String getDebugName() {
        return "";
    }

    public void printStats() {
        if (logger.isDebugEnabled()) {
            if (this.ffCache != null) {
                this.ffCache.printStats();
            }
            if (this.efCache != null) {
                this.efCache.printStats();
                return;
            }
            return;
        }
        logger.info("No statistics were collected, as DEBUG is disabled.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IDESolver$PathEdgeProcessingTask.class */
    public class PathEdgeProcessingTask implements Runnable {
        private final PathEdge<N, D> edge;

        public PathEdgeProcessingTask(PathEdge<N, D> edge) {
            this.edge = edge;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (IDESolver.this.icfg.isCallStmt(this.edge.getTarget())) {
                IDESolver.this.processCall(this.edge);
                return;
            }
            if (IDESolver.this.icfg.isExitStmt(this.edge.getTarget())) {
                IDESolver.this.processExit(this.edge);
            }
            if (!IDESolver.this.icfg.getSuccsOf(this.edge.getTarget()).isEmpty()) {
                IDESolver.this.processNormalFlow(this.edge);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IDESolver$ValuePropagationTask.class */
    public class ValuePropagationTask implements Runnable {
        private final Pair<N, D> nAndD;

        public ValuePropagationTask(Pair<N, D> nAndD) {
            this.nAndD = nAndD;
        }

        @Override // java.lang.Runnable
        public void run() {
            N n = this.nAndD.getO1();
            if (IDESolver.this.icfg.isStartPoint(n) || IDESolver.this.initialSeeds.containsKey(n) || IDESolver.this.unbalancedRetSites.contains(n)) {
                IDESolver.this.propagateValueAtStart(this.nAndD, n);
            }
            if (IDESolver.this.icfg.isCallStmt(n)) {
                IDESolver.this.propagateValueAtCall(this.nAndD, n);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/IDESolver$ValueComputationTask.class */
    public class ValueComputationTask implements Runnable {
        private final N[] values;
        final int num;

        public ValueComputationTask(N[] values, int num) {
            this.values = values;
            this.num = num;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.lang.Runnable
        public void run() {
            int sectionSize = ((int) Math.floor(this.values.length / IDESolver.this.numThreads)) + IDESolver.this.numThreads;
            for (int i = sectionSize * this.num; i < Math.min(sectionSize * (this.num + 1), this.values.length); i++) {
                N n = this.values[i];
                for (Object obj : IDESolver.this.icfg.getStartPointsOf(IDESolver.this.icfg.getMethodOf(n))) {
                    Set<Table.Cell<D, D, EdgeFunction<V>>> lookupByTarget = IDESolver.this.jumpFn.lookupByTarget(n);
                    for (Table.Cell<D, D, EdgeFunction<V>> sourceValTargetValAndFunction : lookupByTarget) {
                        D dPrime = sourceValTargetValAndFunction.getRowKey();
                        D d = sourceValTargetValAndFunction.getColumnKey();
                        EdgeFunction<V> fPrime = sourceValTargetValAndFunction.getValue();
                        synchronized (IDESolver.this.val) {
                            IDESolver.this.setVal(n, d, IDESolver.this.valueLattice.meet(IDESolver.this.val(n, d), fPrime.computeTarget((V) IDESolver.this.val(obj, dPrime))));
                        }
                        IDESolver.this.flowFunctionApplicationCount++;
                    }
                }
            }
        }
    }
}
