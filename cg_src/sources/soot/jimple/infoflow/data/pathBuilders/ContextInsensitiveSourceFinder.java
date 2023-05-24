package soot.jimple.infoflow.data.pathBuilders;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextInsensitiveSourceFinder.class */
public class ContextInsensitiveSourceFinder extends ConcurrentAbstractionPathBuilder {
    private int lastTaskId;
    private int numTasks;

    public ContextInsensitiveSourceFinder(InfoflowManager manager, InterruptableExecutor executor) {
        this(manager, executor, -1);
    }

    public ContextInsensitiveSourceFinder(InfoflowManager manager, InterruptableExecutor executor, int numTasks) {
        super(manager, executor);
        this.lastTaskId = 0;
        this.numTasks = 0;
        this.numTasks = numTasks;
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ContextInsensitiveSourceFinder$SourceFindingTask.class */
    private class SourceFindingTask implements Runnable {
        private final int taskId;
        private final AbstractionAtSink flagAbs;
        private final List<Abstraction> abstractionQueue = new LinkedList();
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !ContextInsensitiveSourceFinder.class.desiredAssertionStatus();
        }

        public SourceFindingTask(int taskId, AbstractionAtSink flagAbs, Abstraction abstraction) {
            this.taskId = taskId;
            this.flagAbs = flagAbs;
            this.abstractionQueue.add(abstraction);
            abstraction.registerPathFlag(taskId, ContextInsensitiveSourceFinder.this.numTasks);
        }

        @Override // java.lang.Runnable
        public void run() {
            while (!this.abstractionQueue.isEmpty()) {
                if (ContextInsensitiveSourceFinder.this.isKilled()) {
                    this.abstractionQueue.clear();
                    return;
                }
                Abstraction abstraction = this.abstractionQueue.remove(0);
                if (abstraction.getSourceContext() == null) {
                    if (abstraction.getPredecessor().registerPathFlag(this.taskId, ContextInsensitiveSourceFinder.this.numTasks)) {
                        this.abstractionQueue.add(abstraction.getPredecessor());
                    }
                } else {
                    ContextInsensitiveSourceFinder.this.results.addResult(this.flagAbs.getSinkDefinition(), this.flagAbs.getAbstraction().getAccessPath(), this.flagAbs.getSinkStmt(), abstraction.getSourceContext().getDefinition(), abstraction.getSourceContext().getAccessPath(), abstraction.getSourceContext().getStmt(), abstraction.getSourceContext().getUserData(), null, ContextInsensitiveSourceFinder.this.manager);
                    if (!$assertionsDisabled && abstraction.getPredecessor() != null) {
                        throw new AssertionError();
                    }
                }
                if (abstraction.getNeighbors() != null) {
                    for (Abstraction nb : abstraction.getNeighbors()) {
                        if (nb.registerPathFlag(this.taskId, ContextInsensitiveSourceFinder.this.numTasks)) {
                            this.abstractionQueue.add(nb);
                        }
                    }
                }
            }
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected boolean triggerComputationForNeighbors() {
        return false;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder
    protected Runnable getTaintPathTask(AbstractionAtSink abs) {
        int i = this.lastTaskId;
        this.lastTaskId = i + 1;
        return new SourceFindingTask(i, abs, abs.getAbstraction());
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void runIncrementalPathCompuation() {
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.ConcurrentAbstractionPathBuilder, soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
        if (this.numTasks < 0) {
            this.numTasks = res.size();
        } else {
            this.numTasks += res.size();
        }
        super.computeTaintPaths(res);
    }
}
