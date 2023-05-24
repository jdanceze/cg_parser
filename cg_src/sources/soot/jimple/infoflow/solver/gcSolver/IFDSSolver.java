package soot.jimple.infoflow.solver.gcSolver;

import com.google.common.cache.CacheBuilder;
import heros.DontSynchronize;
import heros.FlowFunction;
import heros.FlowFunctionCache;
import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.SynchronizedBy;
import heros.ZeroedFlowFunctions;
import heros.solver.Pair;
import heros.solver.PathEdge;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.solver.EndSummary;
import soot.jimple.infoflow.solver.PredecessorShorteningMode;
import soot.jimple.infoflow.solver.SolverPeerGroup;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.executors.SetPoolExecutor;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IFDSSolver.class */
public class IFDSSolver<N, D extends FastSolverLinkedNode<D, N>, I extends BiDiInterproceduralCFG<N, SootMethod>> implements IMemoryBoundedSolver {
    public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER;
    protected static final Logger logger;
    public static final boolean DEBUG;
    protected InterruptableExecutor executor;
    @DontSynchronize("only used by single thread")
    protected int numThreads;
    @SynchronizedBy("thread safe data structure, consistent locking when used")
    protected ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions;
    @SynchronizedBy("thread safe data structure")
    protected volatile IGarbageCollector<N, D> garbageCollector;
    @SynchronizedBy("thread safe data structure, only modified internally")
    protected final I icfg;
    @SynchronizedBy("consistent lock on 'incoming'")
    protected final MyConcurrentHashMap<Pair<SootMethod, D>, Map<EndSummary<N, D>, EndSummary<N, D>>> endSummary;
    @SynchronizedBy("consistent lock on field")
    protected final ConcurrentHashMultiMap<Pair<SootMethod, D>, IncomingRecord<N, D>> incoming;
    @DontSynchronize("stateless")
    protected final FlowFunctions<N, D, SootMethod> flowFunctions;
    @DontSynchronize("only used by single thread")
    protected final Map<N, Set<D>> initialSeeds;
    @DontSynchronize("benign races")
    public long propagationCount;
    @DontSynchronize("stateless")
    protected final D zeroValue;
    @DontSynchronize("readOnly")
    protected final FlowFunctionCache<N, D, SootMethod> ffCache;
    @DontSynchronize("readOnly")
    protected final boolean followReturnsPastSeeds;
    @DontSynchronize("readOnly")
    protected PredecessorShorteningMode shorteningMode;
    @DontSynchronize("readOnly")
    private int maxJoinPointAbstractions;
    @DontSynchronize("readOnly")
    protected IMemoryManager<D, N> memoryManager;
    protected boolean solverId;
    private Set<IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification> notificationListeners;
    private ISolverTerminationReason killFlag;
    private int maxCalleesPerCallSite;
    private int maxAbstractionPathLength;
    protected SolverPeerGroup solverPeerGroup;
    static final /* synthetic */ boolean $assertionsDisabled;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode;

    static {
        $assertionsDisabled = !IFDSSolver.class.desiredAssertionStatus();
        DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
        logger = LoggerFactory.getLogger(IFDSSolver.class);
        DEBUG = logger.isDebugEnabled();
    }

    static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode() {
        int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode;
        if (iArr != null) {
            return iArr;
        }
        int[] iArr2 = new int[PredecessorShorteningMode.valuesCustom().length];
        try {
            iArr2[PredecessorShorteningMode.AlwaysShorten.ordinal()] = 3;
        } catch (NoSuchFieldError unused) {
        }
        try {
            iArr2[PredecessorShorteningMode.NeverShorten.ordinal()] = 1;
        } catch (NoSuchFieldError unused2) {
        }
        try {
            iArr2[PredecessorShorteningMode.ShortenIfEqual.ordinal()] = 2;
        } catch (NoSuchFieldError unused3) {
        }
        $SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode = iArr2;
        return iArr2;
    }

    public IFDSSolver(IFDSTabulationProblem<N, D, SootMethod, I> tabulationProblem) {
        this(tabulationProblem, DEFAULT_CACHE_BUILDER);
    }

    public IFDSSolver(IFDSTabulationProblem<N, D, SootMethod, I> tabulationProblem, CacheBuilder flowFunctionCacheBuilder) {
        FlowFunctions<N, D, SootMethod> flowFunctions;
        this.jumpFunctions = new ConcurrentHashMultiMap<>();
        this.endSummary = new MyConcurrentHashMap<>();
        this.incoming = new ConcurrentHashMultiMap<>();
        this.shorteningMode = PredecessorShorteningMode.NeverShorten;
        this.maxJoinPointAbstractions = -1;
        this.memoryManager = null;
        this.notificationListeners = new HashSet();
        this.killFlag = null;
        this.maxCalleesPerCallSite = 75;
        this.maxAbstractionPathLength = 100;
        flowFunctionCacheBuilder = logger.isDebugEnabled() ? flowFunctionCacheBuilder.recordStats() : flowFunctionCacheBuilder;
        this.zeroValue = tabulationProblem.zeroValue();
        this.icfg = tabulationProblem.interproceduralCFG();
        if (tabulationProblem.autoAddZero()) {
            flowFunctions = new ZeroedFlowFunctions<>(tabulationProblem.flowFunctions(), this.zeroValue);
        } else {
            flowFunctions = tabulationProblem.flowFunctions();
        }
        FlowFunctions<N, D, SootMethod> flowFunctions2 = flowFunctions;
        if (flowFunctionCacheBuilder != null) {
            this.ffCache = new FlowFunctionCache<>(flowFunctions2, flowFunctionCacheBuilder);
            flowFunctions2 = this.ffCache;
        } else {
            this.ffCache = null;
        }
        this.flowFunctions = flowFunctions2;
        this.initialSeeds = tabulationProblem.initialSeeds();
        this.followReturnsPastSeeds = tabulationProblem.followReturnsPastSeeds();
        this.numThreads = Math.max(1, tabulationProblem.numThreads());
        this.executor = getExecutor();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public IGarbageCollector<N, D> createGarbageCollector() {
        if (this.garbageCollector != null) {
            return this.garbageCollector;
        }
        ThreadedGarbageCollector<N, D> gc = new ThreadedGarbageCollector<>(this.icfg, this.jumpFunctions);
        GCSolverPeerGroup gcSolverGroup = (GCSolverPeerGroup) this.solverPeerGroup;
        gc.setPeerGroup(gcSolverGroup.getGCPeerGroup());
        this.garbageCollector = gc;
        return gc;
    }

    public void setSolverId(boolean solverId) {
        this.solverId = solverId;
    }

    public void solve() {
        reset();
        if (this.garbageCollector == null) {
            this.garbageCollector = createGarbageCollector();
        }
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener : this.notificationListeners) {
            listener.notifySolverStarted(this);
        }
        submitInitialSeeds();
        awaitCompletionComputeValuesAndShutdown();
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener2 : this.notificationListeners) {
            listener2.notifySolverTerminated(this);
        }
        logger.info(String.format("GC removed abstractions for %d methods", Integer.valueOf(this.garbageCollector.getGcedMethods())));
        this.garbageCollector.notifySolverTerminated();
    }

    protected void submitInitialSeeds() {
        for (Map.Entry<N, Set<D>> seed : this.initialSeeds.entrySet()) {
            N startPoint = seed.getKey();
            for (D val : seed.getValue()) {
                propagate(this.zeroValue, startPoint, val, null, false);
            }
            addFunction(new PathEdge<>(this.zeroValue, startPoint, this.zeroValue));
        }
    }

    protected void awaitCompletionComputeValuesAndShutdown() {
        runExecutorAndAwaitCompletion();
        if (logger.isDebugEnabled()) {
            printStats();
        }
        this.executor.shutdown();
        while (!this.executor.isTerminated()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
            }
        }
    }

    private void runExecutorAndAwaitCompletion() {
        try {
            this.executor.awaitCompletion();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Throwable exception = this.executor.getException();
        if (exception != null) {
            throw new RuntimeException("There were exceptions during IFDS analysis. Exiting.", exception);
        }
    }

    protected void scheduleEdgeProcessing(PathEdge<N, D> edge) {
        if (this.killFlag != null || this.executor.isTerminating() || this.executor.isTerminated()) {
            return;
        }
        this.garbageCollector.notifyEdgeSchedule(edge);
        this.executor.execute(new PathEdgeProcessingTask(edge, this.solverId));
        this.propagationCount++;
        this.garbageCollector.gc();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void processCall(PathEdge<N, D> edge) {
        final D d1 = edge.factAtSource();
        final N n = edge.getTarget();
        final D d2 = edge.factAtTarget();
        if (!$assertionsDisabled && d2 == null) {
            throw new AssertionError();
        }
        final Collection<N> returnSiteNs = this.icfg.getReturnSitesOfCallAt(n);
        Collection<SootMethod> callees = this.icfg.getCalleesOfCallAt(n);
        if (this.maxCalleesPerCallSite < 0 || callees.size() <= this.maxCalleesPerCallSite) {
            callees.stream().filter(m -> {
                return m.isConcrete();
            }).forEach(new Consumer<SootMethod>() { // from class: soot.jimple.infoflow.solver.gcSolver.IFDSSolver.1
                /* JADX WARN: Multi-variable type inference failed */
                @Override // java.util.function.Consumer
                public void accept(SootMethod sCalledProcN) {
                    if (IFDSSolver.this.killFlag != null) {
                        return;
                    }
                    FlowFunction<D> function = IFDSSolver.this.flowFunctions.getCallFlowFunction(n, sCalledProcN);
                    Set<D> res = IFDSSolver.this.computeCallFlowFunction(function, d1, d2);
                    if (res != null && !res.isEmpty()) {
                        Collection<N> startPointsOf = IFDSSolver.this.icfg.getStartPointsOf(sCalledProcN);
                        Iterator<D> it = res.iterator();
                        while (it.hasNext()) {
                            FastSolverLinkedNode next = it.next();
                            if (IFDSSolver.this.memoryManager != null) {
                                next = (FastSolverLinkedNode) IFDSSolver.this.memoryManager.handleGeneratedMemoryObject(d2, next);
                            }
                            if (next != null && IFDSSolver.this.addIncoming(sCalledProcN, next, n, d1, d2) && !IFDSSolver.this.applyEndSummaryOnCall(d1, n, d2, returnSiteNs, sCalledProcN, next)) {
                                for (N sP : startPointsOf) {
                                    IFDSSolver.this.propagate(next, sP, next, n, false);
                                }
                            }
                        }
                    }
                }
            });
        }
        for (N returnSiteN : returnSiteNs) {
            FlowFunction<D> callToReturnFlowFunction = this.flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
            Set<D> res = computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2);
            if (res != null && !res.isEmpty()) {
                Iterator<D> it = res.iterator();
                while (it.hasNext()) {
                    FastSolverLinkedNode next = it.next();
                    if (this.memoryManager != null) {
                        next = (FastSolverLinkedNode) this.memoryManager.handleGeneratedMemoryObject(d2, next);
                    }
                    if (next != null) {
                        propagate(d1, returnSiteN, next, n, false);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v56, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    public boolean applyEndSummaryOnCall(D d1, N n, D d2, Collection<N> returnSiteNs, SootMethod sCalledProcN, D d3) {
        Set<EndSummary<N, D>> endSumm = endSummary(sCalledProcN, d3);
        if (endSumm != null && !endSumm.isEmpty()) {
            for (EndSummary<N, D> entry : endSumm) {
                N eP = entry.eP;
                D d4 = entry.d4;
                entry.calleeD1.addNeighbor(d3);
                for (N retSiteN : returnSiteNs) {
                    FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(n, sCalledProcN, eP, retSiteN);
                    Set<D> retFlowRes = computeReturnFlowFunction(retFunction, d3, d4, n, Collections.singleton(d1));
                    if (retFlowRes != null && !retFlowRes.isEmpty()) {
                        for (D d5 : retFlowRes) {
                            if (this.memoryManager != null) {
                                d5 = this.memoryManager.handleGeneratedMemoryObject(d4, d5);
                            }
                            D d5p = d5;
                            switch ($SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode()[this.shorteningMode.ordinal()]) {
                                case 2:
                                    if (d5.equals(d2)) {
                                        d5p = d2;
                                        break;
                                    } else {
                                        break;
                                    }
                                case 3:
                                    if (d5p != d2) {
                                        d5p = (FastSolverLinkedNode) d5p.clone();
                                        d5p.setPredecessor(d2);
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                            propagate(d1, retSiteN, d5p, n, false);
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    protected Set<D> computeCallFlowFunction(FlowFunction<D> callFlowFunction, D d1, D d2) {
        return callFlowFunction.computeTargets(d2);
    }

    protected Set<D> computeCallToReturnFlowFunction(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
        return callToReturnFlowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v115, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    /* JADX WARN: Type inference failed for: r0v131, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    /* JADX WARN: Type inference failed for: r7v0, types: [soot.jimple.infoflow.solver.gcSolver.IFDSSolver<N, D extends soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode<D, N>, I extends soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG<N, soot.SootMethod>>, soot.jimple.infoflow.solver.gcSolver.IFDSSolver] */
    public void processExit(PathEdge<N, D> edge) {
        N n = edge.getTarget();
        SootMethod methodThatNeedsSummary = (SootMethod) this.icfg.getMethodOf(n);
        D d1 = edge.factAtSource();
        D d2 = edge.factAtTarget();
        if (!addEndSummary(methodThatNeedsSummary, d1, n, d2)) {
            return;
        }
        Set<IncomingRecord<N, D>> inc = incoming(d1, methodThatNeedsSummary);
        if (inc != null && !inc.isEmpty()) {
            for (IncomingRecord<N, D> entry : inc) {
                if (this.killFlag != null) {
                    return;
                }
                N c = entry.n;
                Set<D> callerSideDs = Collections.singleton(entry.d1);
                for (N retSiteC : this.icfg.getReturnSitesOfCallAt(c)) {
                    FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary, n, retSiteC);
                    Set<D> targets = computeReturnFlowFunction(retFunction, d1, d2, c, callerSideDs);
                    if (targets != null && !targets.isEmpty()) {
                        D d4 = entry.d1;
                        D predVal = entry.d2;
                        Iterator<D> it = targets.iterator();
                        while (it.hasNext()) {
                            D d5 = it.next();
                            if (this.memoryManager != null) {
                                d5 = this.memoryManager.handleGeneratedMemoryObject(d2, d5);
                            }
                            if (d5 != null) {
                                D d5p = d5;
                                switch ($SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode()[this.shorteningMode.ordinal()]) {
                                    case 2:
                                        if (d5.equals(predVal)) {
                                            d5p = predVal;
                                            break;
                                        }
                                        break;
                                    case 3:
                                        if (d5p != predVal) {
                                            d5p = (FastSolverLinkedNode) d5p.clone();
                                            d5p.setPredecessor(predVal);
                                            break;
                                        }
                                        break;
                                }
                                propagate(d4, retSiteC, d5p, c, false);
                                d1.addNeighbor(entry.d3);
                            }
                        }
                    }
                }
            }
        }
        if (this.followReturnsPastSeeds && d1 == this.zeroValue) {
            if (inc == null || inc.isEmpty()) {
                Collection<N> callers = this.icfg.getCallersOf(methodThatNeedsSummary);
                for (N c2 : callers) {
                    for (N retSiteC2 : this.icfg.getReturnSitesOfCallAt(c2)) {
                        FlowFunction<D> retFunction2 = this.flowFunctions.getReturnFlowFunction(c2, methodThatNeedsSummary, n, retSiteC2);
                        Set<D> targets2 = computeReturnFlowFunction(retFunction2, d1, d2, c2, Collections.singleton(this.zeroValue));
                        if (targets2 != null && !targets2.isEmpty()) {
                            Iterator<D> it2 = targets2.iterator();
                            while (it2.hasNext()) {
                                FastSolverLinkedNode next = it2.next();
                                if (this.memoryManager != null) {
                                    next = (FastSolverLinkedNode) ((IMemoryManager<D, N>) this.memoryManager).handleGeneratedMemoryObject(d2, next);
                                }
                                if (next != null) {
                                    propagate(this.zeroValue, retSiteC2, next, c2, true);
                                }
                            }
                        }
                    }
                }
                if (callers.isEmpty()) {
                    FlowFunction<D> retFunction3 = this.flowFunctions.getReturnFlowFunction(null, methodThatNeedsSummary, n, null);
                    retFunction3.computeTargets(d2);
                }
            }
        }
    }

    protected Set<D> computeReturnFlowFunction(FlowFunction<D> retFunction, D d1, D d2, N callSite, Collection<D> callerSideDs) {
        return retFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v33, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    public void processNormalFlow(PathEdge<N, D> edge) {
        D d1 = edge.factAtSource();
        N n = edge.getTarget();
        D d2 = edge.factAtTarget();
        for (N m : this.icfg.getSuccsOf(n)) {
            if (this.killFlag != null) {
                return;
            }
            FlowFunction<D> flowFunction = this.flowFunctions.getNormalFlowFunction(n, m);
            Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);
            if (res != null && !res.isEmpty()) {
                Iterator<D> it = res.iterator();
                while (it.hasNext()) {
                    D d3 = it.next();
                    if (this.memoryManager != null && d2 != d3) {
                        d3 = this.memoryManager.handleGeneratedMemoryObject(d2, d3);
                    }
                    if (d3 != null) {
                        propagate(d1, m, d3, null, false);
                    }
                }
            }
        }
    }

    protected Set<D> computeNormalFlowFunction(FlowFunction<D> flowFunction, D d1, D d2) {
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void propagate(D sourceVal, N target, D targetVal, N relatedCallSite, boolean isUnbalancedReturn) {
        boolean isEssential;
        if (this.memoryManager != null) {
            sourceVal = this.memoryManager.handleMemoryObject(sourceVal);
            targetVal = this.memoryManager.handleMemoryObject(targetVal);
            if (targetVal == null) {
                return;
            }
        }
        if (this.maxAbstractionPathLength >= 0 && targetVal.getPathLength() > this.maxAbstractionPathLength) {
            return;
        }
        PathEdge<N, D> activeEdge = new PathEdge<>(sourceVal, target, (FastSolverLinkedNode) targetVal.getActiveCopy());
        PathEdge<N, D> edge = new PathEdge<>(sourceVal, target, targetVal);
        D existingVal = addFunction(activeEdge);
        if (existingVal != null) {
            if (existingVal != targetVal) {
                if (this.memoryManager == null) {
                    isEssential = relatedCallSite != null && this.icfg.isCallStmt(relatedCallSite);
                } else {
                    isEssential = this.memoryManager.isEssentialJoinPoint(targetVal, relatedCallSite);
                }
                if (this.maxJoinPointAbstractions < 0 || existingVal.getNeighborCount() < this.maxJoinPointAbstractions || isEssential) {
                    existingVal.addNeighbor(targetVal);
                    return;
                }
                return;
            }
            return;
        }
        scheduleEdgeProcessing(edge);
    }

    public D addFunction(PathEdge<N, D> edge) {
        PathEdge<N, D> oldEdge = this.jumpFunctions.putIfAbsent((SootMethod) this.icfg.getMethodOf(edge.getTarget()), edge);
        if (oldEdge == null) {
            return null;
        }
        return oldEdge.factAtTarget();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<EndSummary<N, D>> endSummary(SootMethod m, D d3) {
        Map<EndSummary<N, D>, EndSummary<N, D>> map = this.endSummary.get(new Pair(m, d3));
        if (map == null) {
            return null;
        }
        return map.keySet();
    }

    private boolean addEndSummary(SootMethod m, D d1, N eP, D d2) {
        if (d1 == this.zeroValue) {
            return true;
        }
        Map<EndSummary<N, D>, EndSummary<N, D>> summaries = this.endSummary.putIfAbsentElseGet((MyConcurrentHashMap<Pair<SootMethod, D>, Map<EndSummary<N, D>, EndSummary<N, D>>>) new Pair<>(m, d1), () -> {
            return new ConcurrentHashMap();
        });
        EndSummary<N, D> newSummary = new EndSummary<>(eP, d2, d1);
        EndSummary<N, D> existingSummary = summaries.putIfAbsent(newSummary, newSummary);
        if (existingSummary != null) {
            existingSummary.calleeD1.addNeighbor(d2);
            return false;
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IFDSSolver$IncomingRecord.class */
    public static class IncomingRecord<N, D extends FastSolverLinkedNode<D, N>> {
        public final N n;
        public final D d1;
        public final D d2;
        public final D d3;

        public IncomingRecord(N n, D d1, D d2, D d3) {
            this.n = n;
            this.d1 = d1;
            this.d2 = d2;
            this.d3 = d3;
        }

        public int hashCode() {
            int result = (31 * 1) + (this.d1 == null ? 0 : this.d1.hashCode());
            return (31 * ((31 * ((31 * result) + (this.d2 == null ? 0 : this.d2.hashCode()))) + (this.d3 == null ? 0 : this.d3.hashCode()))) + (this.n == null ? 0 : this.n.hashCode());
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IncomingRecord other = (IncomingRecord) obj;
            if (this.d1 == null) {
                if (other.d1 != null) {
                    return false;
                }
            } else if (!this.d1.equals(other.d1)) {
                return false;
            }
            if (this.d2 == null) {
                if (other.d2 != null) {
                    return false;
                }
            } else if (!this.d2.equals(other.d2)) {
                return false;
            }
            if (this.d3 == null) {
                if (other.d3 != null) {
                    return false;
                }
            } else if (!this.d3.equals(other.d3)) {
                return false;
            }
            if (this.n == null) {
                if (other.n != null) {
                    return false;
                }
                return true;
            } else if (!this.n.equals(other.n)) {
                return false;
            } else {
                return true;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<IncomingRecord<N, D>> incoming(D d1, SootMethod m) {
        Set<IncomingRecord<N, D>> inc = this.incoming.get(new Pair<>(m, d1));
        return inc;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addIncoming(SootMethod m, D d3, N n, D d1, D d2) {
        IncomingRecord<N, D> newRecord = new IncomingRecord<>(n, d1, d2, d3);
        IncomingRecord<N, D> rec = this.incoming.putIfAbsent(new Pair<>(m, d3), newRecord);
        return rec == null;
    }

    protected InterruptableExecutor getExecutor() {
        SetPoolExecutor executor = new SetPoolExecutor(1, this.numThreads, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
        executor.setThreadFactory(new ThreadFactory() { // from class: soot.jimple.infoflow.solver.gcSolver.IFDSSolver.2
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                Thread thrIFDS = new Thread(r);
                thrIFDS.setDaemon(true);
                thrIFDS.setName("IFDS Solver");
                return thrIFDS;
            }
        });
        return executor;
    }

    protected String getDebugName() {
        return "FAST IFDS SOLVER";
    }

    public void printStats() {
        if (logger.isDebugEnabled()) {
            if (this.ffCache != null) {
                this.ffCache.printStats();
                return;
            }
            return;
        }
        logger.info("No statistics were collected, as DEBUG is disabled.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IFDSSolver$PathEdgeProcessingTask.class */
    public class PathEdgeProcessingTask implements Runnable {
        private final PathEdge<N, D> edge;
        private final boolean solverId;

        public PathEdgeProcessingTask(PathEdge<N, D> edge, boolean solverId) {
            this.edge = edge;
            this.solverId = solverId;
        }

        @Override // java.lang.Runnable
        public void run() {
            if (IFDSSolver.this.icfg.isCallStmt(this.edge.getTarget())) {
                IFDSSolver.this.processCall(this.edge);
            } else {
                if (IFDSSolver.this.icfg.isExitStmt(this.edge.getTarget())) {
                    IFDSSolver.this.processExit(this.edge);
                }
                if (!IFDSSolver.this.icfg.getSuccsOf(this.edge.getTarget()).isEmpty()) {
                    IFDSSolver.this.processNormalFlow(this.edge);
                }
            }
            IFDSSolver.this.garbageCollector.notifyTaskProcessed(this.edge);
        }

        public int hashCode() {
            int result = (31 * 1) + (this.edge == null ? 0 : this.edge.hashCode());
            return (31 * result) + (this.solverId ? 1231 : 1237);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            IFDSSolver<N, D, I>.PathEdgeProcessingTask other = (PathEdgeProcessingTask) obj;
            if (this.edge == null) {
                if (other.edge != null) {
                    return false;
                }
            } else if (!this.edge.equals(other.edge)) {
                return false;
            }
            if (this.solverId != other.solverId) {
                return false;
            }
            return true;
        }
    }

    public void setPredecessorShorteningMode(PredecessorShorteningMode mode) {
    }

    public void setMaxJoinPointAbstractions(int maxJoinPointAbstractions) {
        this.maxJoinPointAbstractions = maxJoinPointAbstractions;
    }

    public void setMemoryManager(IMemoryManager<D, N> memoryManager) {
        this.memoryManager = memoryManager;
    }

    public IMemoryManager<D, N> getMemoryManager() {
        return this.memoryManager;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
        this.killFlag = reason;
        this.executor.interrupt();
        this.executor.shutdown();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isTerminated() {
        return this.killFlag != null || this.executor.isFinished();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isKilled() {
        return this.killFlag != null;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void reset() {
        this.killFlag = null;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
        this.notificationListeners.add(listener);
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public ISolverTerminationReason getTerminationReason() {
        return this.killFlag;
    }

    public void setMaxCalleesPerCallSite(int maxCalleesPerCallSite) {
        this.maxCalleesPerCallSite = maxCalleesPerCallSite;
    }

    public void setMaxAbstractionPathLength(int maxAbstractionPathLength) {
        this.maxAbstractionPathLength = maxAbstractionPathLength;
    }

    public void setPeerGroup(SolverPeerGroup solverPeerGroup) {
        this.solverPeerGroup = solverPeerGroup;
    }

    public void terminate() {
        if (this.garbageCollector != null) {
            this.garbageCollector.notifySolverTerminated();
        }
    }
}
