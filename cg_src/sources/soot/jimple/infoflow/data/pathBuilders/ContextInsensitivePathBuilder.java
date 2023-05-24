package soot.jimple.infoflow.data.pathBuilders;

import java.util.HashSet;
import java.util.Set;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.collect.ConcurrentIdentityHashMultiMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.SourceContext;
import soot.jimple.infoflow.data.SourceContextAndPath;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextInsensitivePathBuilder.class */
public class ContextInsensitivePathBuilder extends ConcurrentAbstractionPathBuilder {
    private ConcurrentIdentityHashMultiMap<Abstraction, SourceContextAndPath> pathCache;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ContextInsensitivePathBuilder.class.desiredAssertionStatus();
    }

    public ContextInsensitivePathBuilder(InfoflowManager manager, InterruptableExecutor executor) {
        super(manager, executor);
        this.pathCache = new ConcurrentIdentityHashMultiMap<>();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextInsensitivePathBuilder$SourceFindingTask.class */
    private class SourceFindingTask implements Runnable {
        private final Abstraction abstraction;

        public SourceFindingTask(Abstraction abstraction) {
            this.abstraction = abstraction;
        }

        @Override // java.lang.Runnable
        public void run() {
            Set<SourceContextAndPath> paths = ContextInsensitivePathBuilder.this.pathCache.get(this.abstraction);
            Abstraction pred = this.abstraction.getPredecessor();
            if (pred != null) {
                for (SourceContextAndPath scap : paths) {
                    if (processPredecessor(scap, pred)) {
                        ContextInsensitivePathBuilder.this.scheduleDependentTask(new SourceFindingTask(pred));
                    }
                    if (pred.getNeighbors() != null) {
                        for (Abstraction neighbor : pred.getNeighbors()) {
                            if (processPredecessor(scap, neighbor)) {
                                ContextInsensitivePathBuilder.this.scheduleDependentTask(new SourceFindingTask(neighbor));
                            }
                        }
                    }
                }
            }
        }

        private boolean processPredecessor(SourceContextAndPath scap, Abstraction pred) {
            Set<SourceContextAndPath> existingPaths;
            SourceContextAndPath extendedScap = scap.extendPath(pred, ContextInsensitivePathBuilder.this.config);
            if (extendedScap == null) {
                return false;
            }
            ContextInsensitivePathBuilder.this.checkForSource(pred, extendedScap);
            int maxPaths = ContextInsensitivePathBuilder.this.config.getPathConfiguration().getMaxPathsPerAbstraction();
            if (maxPaths <= 0 || (existingPaths = ContextInsensitivePathBuilder.this.pathCache.get(pred)) == null || existingPaths.size() <= maxPaths) {
                return ContextInsensitivePathBuilder.this.pathCache.put(pred, extendedScap);
            }
            return false;
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
            if (this.abstraction != other.abstraction) {
                return false;
            }
            return true;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean checkForSource(Abstraction abs, SourceContextAndPath scap) {
        if (abs.getPredecessor() != null) {
            return false;
        }
        if ($assertionsDisabled || abs.getSourceContext() != null) {
            if ($assertionsDisabled || abs.getNeighbors() == null) {
                SourceContext sourceContext = abs.getSourceContext();
                this.results.addResult(scap.getDefinition(), scap.getAccessPath(), scap.getStmt(), sourceContext.getDefinition(), sourceContext.getAccessPath(), sourceContext.getStmt(), sourceContext.getUserData(), scap.getAbstractionPath(), this.manager);
                return true;
            }
            throw new AssertionError();
        }
        throw new AssertionError();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected Runnable getTaintPathTask(AbstractionAtSink abs) {
        SourceContextAndPath scap = new SourceContextAndPath(this.config, abs.getSinkDefinition(), abs.getAbstraction().getAccessPath(), abs.getSinkStmt()).extendPath(abs.getAbstraction(), this.config);
        if (this.pathCache.put(abs.getAbstraction(), scap) && !checkForSource(abs.getAbstraction(), scap)) {
            return new SourceFindingTask(abs.getAbstraction());
        }
        return null;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected boolean triggerComputationForNeighbors() {
        return true;
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
}
