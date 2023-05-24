package soot.jimple.infoflow.solver.fastSolver.flowInsensitive;

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
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.solver.EndSummary;
import soot.jimple.infoflow.solver.PredecessorShorteningMode;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.executors.SetPoolExecutor;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/flowInsensitive/FlowInsensitiveSolver.class */
public class FlowInsensitiveSolver<N extends Unit, D extends FastSolverLinkedNode<D, N>, I extends BiDiInterproceduralCFG<Unit, SootMethod>> implements IMemoryBoundedSolver {
    public static CacheBuilder<Object, Object> DEFAULT_CACHE_BUILDER = CacheBuilder.newBuilder().concurrencyLevel(Runtime.getRuntime().availableProcessors()).initialCapacity(10000).softValues();
    protected static final Logger logger = LoggerFactory.getLogger(FlowInsensitiveSolver.class);
    public static final boolean DEBUG = logger.isDebugEnabled();
    protected InterruptableExecutor executor;
    @DontSynchronize("only used by single thread")
    protected int numThreads;
    @SynchronizedBy("thread safe data structure, consistent locking when used")
    protected MyConcurrentHashMap<PathEdge<SootMethod, D>, D> jumpFunctions;
    @SynchronizedBy("thread safe data structure, only modified internally")
    protected final I icfg;
    @SynchronizedBy("consistent lock on 'incoming'")
    protected final MyConcurrentHashMap<Pair<SootMethod, D>, Set<EndSummary<N, D>>> endSummary;
    @SynchronizedBy("consistent lock on field")
    protected final MyConcurrentHashMap<Pair<SootMethod, D>, MyConcurrentHashMap<Unit, Map<D, D>>> incoming;
    @DontSynchronize("stateless")
    protected final FlowFunctions<Unit, D, SootMethod> flowFunctions;
    @DontSynchronize("only used by single thread")
    protected final Map<Unit, Set<D>> initialSeeds;
    @DontSynchronize("benign races")
    public long propagationCount;
    @DontSynchronize("stateless")
    protected final D zeroValue;
    @DontSynchronize("readOnly")
    protected final FlowFunctionCache<Unit, D, SootMethod> ffCache;
    @DontSynchronize("readOnly")
    protected final boolean followReturnsPastSeeds;
    @DontSynchronize("readOnly")
    protected PredecessorShorteningMode shorteningMode;
    @DontSynchronize("readOnly")
    private int maxJoinPointAbstractions;
    @DontSynchronize("readOnly")
    protected IMemoryManager<D, N> memoryManager;
    private boolean solverId;
    private Set<IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification> notificationListeners;
    private ISolverTerminationReason killFlag;
    private int maxCalleesPerCallSite;
    private int maxAbstractionPathLength;
    private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$solver$PredecessorShorteningMode;

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

    public FlowInsensitiveSolver(IFDSTabulationProblem<Unit, D, SootMethod, I> tabulationProblem) {
        this(tabulationProblem, DEFAULT_CACHE_BUILDER);
    }

    public FlowInsensitiveSolver(IFDSTabulationProblem<Unit, D, SootMethod, I> tabulationProblem, CacheBuilder flowFunctionCacheBuilder) {
        FlowFunctions<Unit, D, SootMethod> flowFunctions;
        this.jumpFunctions = new MyConcurrentHashMap<>();
        this.endSummary = new MyConcurrentHashMap<>();
        this.incoming = new MyConcurrentHashMap<>();
        this.shorteningMode = PredecessorShorteningMode.NeverShorten;
        this.maxJoinPointAbstractions = -1;
        this.memoryManager = null;
        this.solverId = true;
        this.notificationListeners = new HashSet();
        this.killFlag = null;
        this.maxCalleesPerCallSite = 10;
        this.maxAbstractionPathLength = 100;
        flowFunctionCacheBuilder = logger.isDebugEnabled() ? flowFunctionCacheBuilder.recordStats() : flowFunctionCacheBuilder;
        this.zeroValue = tabulationProblem.zeroValue();
        this.icfg = tabulationProblem.interproceduralCFG();
        if (tabulationProblem.autoAddZero()) {
            flowFunctions = new ZeroedFlowFunctions<>(tabulationProblem.flowFunctions(), this.zeroValue);
        } else {
            flowFunctions = tabulationProblem.flowFunctions();
        }
        FlowFunctions<Unit, D, SootMethod> flowFunctions2 = flowFunctions;
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

    public void solve() {
        reset();
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener : this.notificationListeners) {
            listener.notifySolverStarted(this);
        }
        submitInitialSeeds();
        awaitCompletionComputeValuesAndShutdown();
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener2 : this.notificationListeners) {
            listener2.notifySolverTerminated(this);
        }
    }

    protected void submitInitialSeeds() {
        for (Map.Entry<Unit, Set<D>> seed : this.initialSeeds.entrySet()) {
            Unit startPoint = seed.getKey();
            SootMethod mp = (SootMethod) this.icfg.getMethodOf(startPoint);
            for (D val : seed.getValue()) {
                if (this.icfg.isCallStmt(startPoint)) {
                    processCall(this.zeroValue, startPoint, val);
                } else if (this.icfg.isExitStmt(startPoint)) {
                    processExit(this.zeroValue, startPoint, val);
                } else {
                    processNormalFlow(this.zeroValue, startPoint, val, mp);
                }
            }
            addFunction(new PathEdge<>(this.zeroValue, mp, this.zeroValue));
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
                e.printStackTrace();
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

    protected boolean getSolverId() {
        return this.solverId;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void setSolverId(boolean solverId) {
        this.solverId = solverId;
    }

    protected void scheduleEdgeProcessing(PathEdge<SootMethod, D> edge) {
        if (this.killFlag != null || this.executor.isTerminating()) {
            return;
        }
        this.executor.execute(new PathEdgeProcessingTask(edge, getSolverId()));
        this.propagationCount++;
    }

    private void processCall(D d1, Unit n, D d2) {
        Collection<Unit> returnSiteNs = this.icfg.getReturnSitesOfCallAt(n);
        Collection<SootMethod> callees = this.icfg.getCalleesOfCallAt(n);
        if (this.maxCalleesPerCallSite < 0 || callees.size() <= this.maxCalleesPerCallSite) {
            for (SootMethod sCalledProcN : callees) {
                if (this.killFlag != null) {
                    return;
                }
                if (sCalledProcN.isConcrete()) {
                    FlowFunction<D> function = this.flowFunctions.getCallFlowFunction(n, sCalledProcN);
                    Set<D> res = computeCallFlowFunction(function, d1, d2);
                    if (res != null && !res.isEmpty()) {
                        Iterator<D> it = res.iterator();
                        while (it.hasNext()) {
                            D d3 = it.next();
                            if (this.memoryManager != null) {
                                d3 = this.memoryManager.handleGeneratedMemoryObject(d2, d3);
                            }
                            if (d3 != null) {
                                propagate(d3, sCalledProcN, d3, n, false);
                                if (addIncoming(sCalledProcN, d3, n, d1, d2)) {
                                    applyEndSummaryOnCall(d1, n, d2, returnSiteNs, sCalledProcN, d3);
                                }
                            }
                        }
                    }
                }
            }
        }
        for (Unit returnSiteN : returnSiteNs) {
            SootMethod retMeth = (SootMethod) this.icfg.getMethodOf(returnSiteN);
            FlowFunction<D> callToReturnFlowFunction = this.flowFunctions.getCallToReturnFlowFunction(n, returnSiteN);
            Set<D> res2 = computeCallToReturnFlowFunction(callToReturnFlowFunction, d1, d2);
            if (res2 != null && !res2.isEmpty()) {
                Iterator<D> it2 = res2.iterator();
                while (it2.hasNext()) {
                    D d32 = it2.next();
                    if (this.memoryManager != null) {
                        d32 = this.memoryManager.handleGeneratedMemoryObject(d2, d32);
                    }
                    if (d32 != null) {
                        propagate(d1, retMeth, d32, n, false);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v55, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    public void applyEndSummaryOnCall(D d1, Unit n, D d2, Collection<Unit> returnSiteNs, SootMethod sCalledProcN, D d3) {
        Set<EndSummary<N, D>> endSumm = endSummary(sCalledProcN, d3);
        if (endSumm != null && !endSumm.isEmpty()) {
            for (EndSummary<N, D> entry : endSumm) {
                Unit eP = entry.eP;
                D d4 = entry.d4;
                for (Unit retSiteN : returnSiteNs) {
                    SootMethod retMeth = (SootMethod) this.icfg.getMethodOf(retSiteN);
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
                            propagate(d1, retMeth, d5p, n, false, true);
                        }
                    }
                }
            }
        }
    }

    protected Set<D> computeCallFlowFunction(FlowFunction<D> callFlowFunction, D d1, D d2) {
        return callFlowFunction.computeTargets(d2);
    }

    protected Set<D> computeCallToReturnFlowFunction(FlowFunction<D> callToReturnFlowFunction, D d1, D d2) {
        return callToReturnFlowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v139, types: [soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode] */
    public void processExit(D d1, Unit n, D d2) {
        SootMethod methodThatNeedsSummary = (SootMethod) this.icfg.getMethodOf(n);
        if (!addEndSummary(methodThatNeedsSummary, d1, n, d2)) {
            return;
        }
        Map<Unit, Map<D, D>> inc = incoming(d1, methodThatNeedsSummary);
        if (inc != null && !inc.isEmpty()) {
            for (Map.Entry<Unit, Map<D, D>> entry : inc.entrySet()) {
                Unit c = entry.getKey();
                Set<D> callerSideDs = entry.getValue().keySet();
                for (Unit retSiteC : this.icfg.getReturnSitesOfCallAt(c)) {
                    SootMethod returnMeth = (SootMethod) this.icfg.getMethodOf(retSiteC);
                    FlowFunction<D> retFunction = this.flowFunctions.getReturnFlowFunction(c, methodThatNeedsSummary, n, retSiteC);
                    Set<D> targets = computeReturnFlowFunction(retFunction, d1, d2, c, callerSideDs);
                    for (Map.Entry<D, D> d1d2entry : entry.getValue().entrySet()) {
                        D d4 = d1d2entry.getKey();
                        D predVal = d1d2entry.getValue();
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
                                propagate(d4, returnMeth, d5p, c, false);
                            }
                        }
                    }
                }
            }
        }
        if (this.followReturnsPastSeeds && d1 == this.zeroValue) {
            if (inc == null || inc.isEmpty()) {
                Collection<Unit> callers = this.icfg.getCallersOf(methodThatNeedsSummary);
                for (Unit c2 : callers) {
                    SootMethod callerMethod = (SootMethod) this.icfg.getMethodOf(c2);
                    for (Unit retSiteC2 : this.icfg.getReturnSitesOfCallAt(c2)) {
                        FlowFunction<D> retFunction2 = this.flowFunctions.getReturnFlowFunction(c2, methodThatNeedsSummary, n, retSiteC2);
                        Set<D> targets2 = computeReturnFlowFunction(retFunction2, d1, d2, c2, Collections.singleton(this.zeroValue));
                        if (targets2 != null && !targets2.isEmpty()) {
                            Iterator<D> it2 = targets2.iterator();
                            while (it2.hasNext()) {
                                D d52 = it2.next();
                                if (this.memoryManager != null) {
                                    d52 = this.memoryManager.handleGeneratedMemoryObject(d2, d52);
                                }
                                if (d52 != null) {
                                    propagate(this.zeroValue, callerMethod, d52, c2, true);
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

    protected Set<D> computeReturnFlowFunction(FlowFunction<D> retFunction, D d1, D d2, Unit callSite, Collection<D> callerSideDs) {
        return retFunction.computeTargets(d2);
    }

    private void processNormalFlow(D d1, Unit n, D d2, SootMethod method) {
        for (Unit m : this.icfg.getSuccsOf(n)) {
            FlowFunction<D> flowFunction = this.flowFunctions.getNormalFlowFunction(n, m);
            Set<D> res = computeNormalFlowFunction(flowFunction, d1, d2);
            if (res != null && !res.isEmpty()) {
                Iterator<D> it = res.iterator();
                while (it.hasNext()) {
                    D d3 = it.next();
                    if (this.memoryManager != null && d2 != d3) {
                        d3 = this.memoryManager.handleGeneratedMemoryObject(d2, d3);
                    }
                    if (d3 != null && d3 != d2) {
                        propagate(d1, method, d3, null, false);
                    }
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void processMethod(PathEdge<SootMethod, D> edge) {
        D d1 = edge.factAtSource();
        SootMethod target = edge.getTarget();
        D d2 = edge.factAtTarget();
        Iterator<Unit> it = target.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (this.icfg.isCallStmt(u)) {
                processCall(d1, u, d2);
            } else {
                if (this.icfg.isExitStmt(u)) {
                    processExit(d1, u, d2);
                }
                if (!this.icfg.getSuccsOf(u).isEmpty()) {
                    processNormalFlow(d1, u, d2, target);
                }
            }
        }
    }

    protected Set<D> computeNormalFlowFunction(FlowFunction<D> flowFunction, D d1, D d2) {
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void propagate(D sourceVal, SootMethod target, D targetVal, Unit relatedCallSite, boolean isUnbalancedReturn) {
        propagate(sourceVal, target, targetVal, relatedCallSite, isUnbalancedReturn, true);
    }

    protected void propagate(D sourceVal, SootMethod target, D targetVal, Unit relatedCallSite, boolean isUnbalancedReturn, boolean schedule) {
        boolean isEssential;
        if (this.memoryManager != null) {
            sourceVal = this.memoryManager.handleMemoryObject(sourceVal);
            targetVal = this.memoryManager.handleMemoryObject(targetVal);
            if (sourceVal == null || targetVal == null) {
                return;
            }
        }
        if (this.maxAbstractionPathLength >= 0 && targetVal.getPathLength() > this.maxAbstractionPathLength) {
            return;
        }
        PathEdge<SootMethod, D> edge = new PathEdge<>(sourceVal, target, targetVal);
        D existingVal = addFunction(edge);
        if (existingVal == null) {
            if (schedule) {
                scheduleEdgeProcessing(edge);
                return;
            }
            return;
        }
        if (this.memoryManager == null) {
            isEssential = relatedCallSite != null && this.icfg.isCallStmt(relatedCallSite);
        } else {
            isEssential = this.memoryManager.isEssentialJoinPoint(targetVal, relatedCallSite);
        }
        if (this.maxJoinPointAbstractions < 0 || existingVal.getNeighborCount() < this.maxJoinPointAbstractions || isEssential) {
            existingVal.addNeighbor(targetVal);
        }
    }

    public D addFunction(PathEdge<SootMethod, D> edge) {
        return this.jumpFunctions.putIfAbsent(edge, edge.factAtTarget());
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Set<EndSummary<N, D>> endSummary(SootMethod m, D d3) {
        return this.endSummary.get(new Pair(m, d3));
    }

    private boolean addEndSummary(SootMethod m, D d1, Unit eP, D d2) {
        if (d1 == this.zeroValue) {
            return true;
        }
        Set<EndSummary<N, D>> summaries = this.endSummary.computeIfAbsent(new Pair<>(m, d1), pair -> {
            return new ConcurrentHashSet();
        });
        return summaries.add(new EndSummary<>(eP, d2, d1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<Unit, Map<D, D>> incoming(D d1, SootMethod m) {
        return this.incoming.get(new Pair(m, d1));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean addIncoming(SootMethod m, D d3, Unit n, D d1, D d2) {
        MyConcurrentHashMap<Unit, Map<D, D>> summaries = this.incoming.putIfAbsentElseGet((MyConcurrentHashMap<Pair<SootMethod, D>, MyConcurrentHashMap<Unit, Map<D, D>>>) new Pair<>(m, d3), (Pair<SootMethod, D>) new MyConcurrentHashMap<>());
        Map<D, D> set = summaries.putIfAbsentElseGet((MyConcurrentHashMap<Unit, Map<D, D>>) n, (Unit) new ConcurrentHashMap());
        return set.put(d1, d2) == null;
    }

    protected InterruptableExecutor getExecutor() {
        return new SetPoolExecutor(1, this.numThreads, 30L, TimeUnit.SECONDS, new LinkedBlockingQueue());
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
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/flowInsensitive/FlowInsensitiveSolver$PathEdgeProcessingTask.class */
    public class PathEdgeProcessingTask implements Runnable {
        private final PathEdge<SootMethod, D> edge;
        private final boolean solverId;

        public PathEdgeProcessingTask(PathEdge<SootMethod, D> edge, boolean solverId) {
            this.edge = edge;
            this.solverId = solverId;
        }

        @Override // java.lang.Runnable
        public void run() {
            FlowInsensitiveSolver.this.processMethod(this.edge);
        }

        public int hashCode() {
            int result = (31 * 1) + (this.edge == null ? 0 : this.edge.hashCode());
            return result + (this.solverId ? 1337 : 13);
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            FlowInsensitiveSolver<N, D, I>.PathEdgeProcessingTask other = (PathEdgeProcessingTask) obj;
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

        public String toString() {
            return this.edge.toString();
        }
    }

    public void setPredecessorShorteningMode(PredecessorShorteningMode mode) {
        this.shorteningMode = mode;
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
}
