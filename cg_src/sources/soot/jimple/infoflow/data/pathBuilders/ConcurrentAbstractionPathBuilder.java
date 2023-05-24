package soot.jimple.infoflow.data.pathBuilders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.jimple.infoflow.InfoflowConfiguration;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.results.BackwardsInfoflowResults;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/ConcurrentAbstractionPathBuilder.class */
public abstract class ConcurrentAbstractionPathBuilder extends AbstractAbstractionPathBuilder {
    protected final InfoflowResults results;
    protected final InterruptableExecutor executor;
    private Set<IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification> notificationListeners;
    private ISolverTerminationReason killFlag;

    protected abstract Runnable getTaintPathTask(AbstractionAtSink abstractionAtSink);

    protected abstract boolean triggerComputationForNeighbors();

    public ConcurrentAbstractionPathBuilder(InfoflowManager manager, InterruptableExecutor executor) {
        super(manager);
        this.notificationListeners = new HashSet();
        this.killFlag = null;
        this.executor = executor;
        boolean pathAgnostic = manager.getConfig().getPathAgnosticResults();
        InfoflowConfiguration.DataFlowDirection direction = manager.getConfig().getDataFlowDirection();
        if (direction == InfoflowConfiguration.DataFlowDirection.Backwards) {
            this.results = new BackwardsInfoflowResults(pathAgnostic);
        } else {
            this.results = new InfoflowResults(pathAgnostic);
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
        if (res == null || res.isEmpty()) {
            return;
        }
        this.logger.info("Obtainted {} connections between sources and sinks", Integer.valueOf(res.size()));
        if (!this.manager.getConfig().getPathAgnosticResults()) {
            this.logger.info("Path-agnostic results are disabled, i.e., there will be one result per path");
        }
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener : this.notificationListeners) {
            listener.notifySolverStarted(this);
        }
        int curResIdx = 0;
        Iterator<AbstractionAtSink> it = res.iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            AbstractionAtSink abs = it.next();
            this.executor.reset();
            if (this.killFlag != null) {
                res.clear();
                break;
            }
            curResIdx++;
            this.logger.info(String.format("Building path %d...", Integer.valueOf(curResIdx)));
            Runnable task = getTaintPathTask(abs);
            if (task != null) {
                this.executor.execute(task);
            }
            if (triggerComputationForNeighbors() && abs.getAbstraction().getNeighbors() != null) {
                for (Abstraction neighbor : abs.getAbstraction().getNeighbors()) {
                    AbstractionAtSink neighborAtSink = new AbstractionAtSink(abs.getSinkDefinition(), neighbor, abs.getSinkStmt());
                    Runnable task2 = getTaintPathTask(neighborAtSink);
                    if (task2 != null) {
                        this.executor.execute(task2);
                    }
                }
            }
            if (this.config.getPathConfiguration().getSequentialPathProcessing()) {
                try {
                    this.executor.awaitCompletion();
                    this.executor.reset();
                } catch (InterruptedException ex) {
                    this.logger.error("Could not wait for path executor completion", (Throwable) ex);
                }
            }
        }
        for (IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener2 : this.notificationListeners) {
            listener2.notifySolverTerminated(this);
        }
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
        this.killFlag = reason;
        this.executor.interrupt();
        this.logger.warn("Path reconstruction terminated due to low memory");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void scheduleDependentTask(Runnable task) {
        if (!isKilled()) {
            this.executor.execute(task);
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public InfoflowResults getResults() {
        return this.results;
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
    public ISolverTerminationReason getTerminationReason() {
        return this.killFlag;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
        this.notificationListeners.add(listener);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public InterruptableExecutor getExecutor() {
        return this.executor;
    }
}
