package soot.jimple.infoflow.data.pathBuilders;

import heros.solver.Pair;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.collect.ConcurrentIdentityHashMultiMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.SourceContext;
import soot.jimple.infoflow.data.SourceContextAndPath;
import soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.results.ResultSinkInfo;
import soot.jimple.infoflow.results.ResultSourceInfo;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextSensitivePathBuilder.class */
public class ContextSensitivePathBuilder extends ConcurrentAbstractionPathBuilder {
    protected ConcurrentIdentityHashMultiMap<Abstraction, SourceContextAndPath> pathCache;
    protected ConcurrentHashSet<SourceContextAndPath> deferredPaths;
    protected ConcurrentHashSet<SourceContextAndPath> sourceReachingScaps;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ContextSensitivePathBuilder.class.desiredAssertionStatus();
    }

    public ContextSensitivePathBuilder(InfoflowManager manager) {
        super(manager, createExecutor(manager));
        this.pathCache = new ConcurrentIdentityHashMultiMap<>();
        this.deferredPaths = new ConcurrentHashSet<>();
        this.sourceReachingScaps = new ConcurrentHashSet<>();
    }

    private static InterruptableExecutor createExecutor(InfoflowManager manager) {
        int numThreads = Runtime.getRuntime().availableProcessors();
        int mtn = manager.getConfig().getMaxThreadNum();
        InterruptableExecutor executor = new InterruptableExecutor(mtn == -1 ? numThreads : Math.min(mtn, numThreads), Integer.MAX_VALUE, 30L, TimeUnit.SECONDS, new PriorityBlockingQueue());
        executor.setThreadFactory(new ThreadFactory() { // from class: soot.jimple.infoflow.data.pathBuilders.ContextSensitivePathBuilder.1
            @Override // java.util.concurrent.ThreadFactory
            public Thread newThread(Runnable r) {
                return new Thread(r, "Path reconstruction");
            }
        });
        return executor;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextSensitivePathBuilder$SourceFindingTask.class */
    protected class SourceFindingTask implements Runnable, Comparable<SourceFindingTask> {
        private final Abstraction abstraction;
        static final /* synthetic */ boolean $assertionsDisabled;
        private static volatile /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$data$pathBuilders$ResultStatus;

        static {
            $assertionsDisabled = !ContextSensitivePathBuilder.class.desiredAssertionStatus();
        }

        static /* synthetic */ int[] $SWITCH_TABLE$soot$jimple$infoflow$data$pathBuilders$ResultStatus() {
            int[] iArr = $SWITCH_TABLE$soot$jimple$infoflow$data$pathBuilders$ResultStatus;
            if (iArr != null) {
                return iArr;
            }
            int[] iArr2 = new int[ResultStatus.valuesCustom().length];
            try {
                iArr2[ResultStatus.CACHED.ordinal()] = 2;
            } catch (NoSuchFieldError unused) {
            }
            try {
                iArr2[ResultStatus.INFEASIBLE_OR_MAX_PATHS_REACHED.ordinal()] = 3;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                iArr2[ResultStatus.NEW.ordinal()] = 1;
            } catch (NoSuchFieldError unused3) {
            }
            $SWITCH_TABLE$soot$jimple$infoflow$data$pathBuilders$ResultStatus = iArr2;
            return iArr2;
        }

        public SourceFindingTask(Abstraction abstraction) {
            this.abstraction = abstraction;
        }

        @Override // java.lang.Runnable
        public void run() {
            Set<SourceContextAndPath> paths = ContextSensitivePathBuilder.this.pathCache.get(this.abstraction);
            Abstraction pred = this.abstraction.getPredecessor();
            if (pred != null && paths != null) {
                for (SourceContextAndPath scap : paths) {
                    processAndQueue(pred, scap);
                    if (pred.getNeighbors() != null) {
                        for (Abstraction neighbor : pred.getNeighbors()) {
                            processAndQueue(neighbor, scap);
                        }
                    }
                }
            }
        }

        private void processAndQueue(Abstraction pred, SourceContextAndPath scap) {
            ProcessingResult p = processPredecessor(scap, pred);
            switch ($SWITCH_TABLE$soot$jimple$infoflow$data$pathBuilders$ResultStatus()[p.getResult().ordinal()]) {
                case 1:
                    if (!$assertionsDisabled && !ContextSensitivePathBuilder.this.pathCache.containsKey(pred)) {
                        throw new AssertionError();
                    }
                    ContextSensitivePathBuilder.this.scheduleDependentTask(new SourceFindingTask(pred));
                    return;
                case 2:
                    ContextSensitivePathBuilder.this.deferredPaths.add(scap);
                    return;
                case 3:
                    return;
                default:
                    if (!$assertionsDisabled) {
                        throw new AssertionError();
                    }
                    return;
            }
        }

        private ProcessingResult processPredecessor(SourceContextAndPath scap, Abstraction pred) {
            Set<SourceContextAndPath> existingPaths;
            Pair<SourceContextAndPath, Stmt> pathAndItem;
            if (pred.getCurrentStmt() != null && pred.getCurrentStmt() == pred.getCorrespondingCallSite()) {
                SourceContextAndPath extendedScap = scap.extendPath(pred, ContextSensitivePathBuilder.this.config);
                if (extendedScap == null) {
                    return ProcessingResult.INFEASIBLE_OR_MAX_PATHS_REACHED();
                }
                if (ContextSensitivePathBuilder.this.checkForSource(pred, extendedScap)) {
                    ContextSensitivePathBuilder.this.sourceReachingScaps.add(extendedScap);
                }
                return ContextSensitivePathBuilder.this.pathCache.put(pred, extendedScap) ? ProcessingResult.NEW() : ProcessingResult.CACHED(extendedScap);
            }
            SourceContextAndPath extendedScap2 = scap.extendPath(pred, ContextSensitivePathBuilder.this.config);
            if (extendedScap2 == null) {
                return ProcessingResult.INFEASIBLE_OR_MAX_PATHS_REACHED();
            }
            if (pred.getCurrentStmt() != null && pred.getCurrentStmt().containsInvokeExpr() && (pathAndItem = extendedScap2.popTopCallStackItem()) != null) {
                Stmt topCallStackItem = pathAndItem.getO2();
                if (topCallStackItem != pred.getCurrentStmt()) {
                    return ProcessingResult.INFEASIBLE_OR_MAX_PATHS_REACHED();
                }
                extendedScap2 = pathAndItem.getO1();
            }
            if (ContextSensitivePathBuilder.this.checkForSource(pred, extendedScap2)) {
                ContextSensitivePathBuilder.this.sourceReachingScaps.add(extendedScap2);
            }
            int maxPaths = ContextSensitivePathBuilder.this.config.getPathConfiguration().getMaxPathsPerAbstraction();
            if (maxPaths <= 0 || (existingPaths = ContextSensitivePathBuilder.this.pathCache.get(pred)) == null || existingPaths.size() <= maxPaths) {
                return ContextSensitivePathBuilder.this.pathCache.put(pred, extendedScap2) ? ProcessingResult.NEW() : ProcessingResult.CACHED(extendedScap2);
            }
            return ProcessingResult.INFEASIBLE_OR_MAX_PATHS_REACHED();
        }

        public int hashCode() {
            int result = (31 * 1) + (this.abstraction == null ? 0 : this.abstraction.hashCode());
            return result;
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            SourceFindingTask other = (SourceFindingTask) obj;
            return this.abstraction == other.abstraction;
        }

        @Override // java.lang.Comparable
        public int compareTo(SourceFindingTask arg0) {
            return Integer.compare(this.abstraction.getPathLength(), arg0.abstraction.getPathLength());
        }
    }

    protected boolean checkForSource(Abstraction abs, SourceContextAndPath scap) {
        if (abs.getPredecessor() != null) {
            return false;
        }
        if ($assertionsDisabled || abs.getSourceContext() != null) {
            abs.getNeighbors();
            SourceContext sourceContext = abs.getSourceContext();
            Pair<ResultSourceInfo, ResultSinkInfo> newResult = this.results.addResult(scap.getDefinition(), scap.getAccessPath(), scap.getStmt(), sourceContext.getDefinition(), sourceContext.getAccessPath(), sourceContext.getStmt(), sourceContext.getUserData(), scap.getAbstractionPath(), this.manager);
            if (this.resultAvailableHandlers != null) {
                for (IAbstractionPathBuilder.OnPathBuilderResultAvailable handler : this.resultAvailableHandlers) {
                    handler.onResultAvailable(newResult.getO1(), newResult.getO2());
                }
                return true;
            }
            return true;
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void runIncrementalPathCompuation() {
        Set<AbstractionAtSink> incrementalAbs = new HashSet<>();
        for (Abstraction abs : this.pathCache.keySet()) {
            for (SourceContextAndPath scap : this.pathCache.get(abs)) {
                if (abs.getNeighbors() != null && abs.getNeighbors().size() != scap.getNeighborCounter()) {
                    scap.setNeighborCounter(abs.getNeighbors().size());
                    for (Abstraction neighbor : abs.getNeighbors()) {
                        incrementalAbs.add(new AbstractionAtSink(scap.getDefinition(), neighbor, scap.getStmt()));
                    }
                }
            }
        }
        if (!incrementalAbs.isEmpty()) {
            computeTaintPaths(incrementalAbs);
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder, soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
        try {
            super.computeTaintPaths(res);
            long pathTimeout = this.manager.getConfig().getPathConfiguration().getPathReconstructionTimeout();
            if (pathTimeout > 0) {
                this.executor.awaitCompletion(pathTimeout + 20, TimeUnit.SECONDS);
            } else {
                this.executor.awaitCompletion();
            }
        } catch (InterruptedException e) {
            this.logger.error("Could not wait for executor termination", (Throwable) e);
        } finally {
            onTaintPathsComputed();
            cleanupExecutor();
        }
    }

    protected void buildPathsFromCache() {
        Iterator<SourceContextAndPath> it = this.deferredPaths.iterator();
        while (it.hasNext()) {
            SourceContextAndPath deferredScap = it.next();
            Iterator<SourceContextAndPath> it2 = this.sourceReachingScaps.iterator();
            while (it2.hasNext()) {
                SourceContextAndPath sourceScap = it2.next();
                SourceContextAndPath fullScap = deferredScap.extendPath(sourceScap);
                if (fullScap != null) {
                    checkForSource(fullScap.getLastAbstraction(), fullScap);
                }
            }
        }
    }

    protected void onTaintPathsComputed() {
        buildPathsFromCache();
    }

    protected void cleanupExecutor() {
        shutdown();
    }

    public void shutdown() {
        this.executor.shutdown();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected Runnable getTaintPathTask(AbstractionAtSink abs) {
        SourceContextAndPath scap = new SourceContextAndPath(this.config, abs.getSinkDefinition(), abs.getAbstraction().getAccessPath(), abs.getSinkStmt()).extendPath(abs.getAbstraction(), this.config);
        if (this.pathCache.put(abs.getAbstraction(), scap) && !checkForSource(abs.getAbstraction(), scap)) {
            return new SourceFindingTask(abs.getAbstraction());
        }
        return null;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder, soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public InfoflowResults getResults() {
        return this.results;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected boolean triggerComputationForNeighbors() {
        return true;
    }
}
