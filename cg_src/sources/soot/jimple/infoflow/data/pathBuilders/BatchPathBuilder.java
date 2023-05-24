package soot.jimple.infoflow.data.pathBuilders;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/BatchPathBuilder.class */
public class BatchPathBuilder extends AbstractAbstractionPathBuilder {
    protected final IAbstractionPathBuilder innerBuilder;
    protected int batchSize;
    protected ISolverTerminationReason terminationReason;

    public BatchPathBuilder(InfoflowManager manager, IAbstractionPathBuilder innerBuilder) {
        super(manager);
        this.batchSize = 5;
        this.terminationReason = null;
        this.innerBuilder = innerBuilder;
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
        Set<AbstractionAtSink> batch = new HashSet<>();
        Iterator<AbstractionAtSink> resIt = res.iterator();
        int batchId = 1;
        while (resIt.hasNext()) {
            while (batch.size() < this.batchSize && resIt.hasNext()) {
                batch.add(resIt.next());
            }
            int i = batchId;
            batchId++;
            this.logger.info("Running path reconstruction batch {} with {} elements", Integer.valueOf(i), Integer.valueOf(batch.size()));
            this.innerBuilder.reset();
            this.innerBuilder.computeTaintPaths(batch);
            if (this.terminationReason == null) {
                this.terminationReason = this.innerBuilder.getTerminationReason();
            } else {
                this.terminationReason = this.terminationReason.combine(this.innerBuilder.getTerminationReason());
            }
            if (this.innerBuilder instanceof ConcurrentAbstractionPathBuilder) {
                ConcurrentAbstractionPathBuilder concurrentBuilder = (ConcurrentAbstractionPathBuilder) this.innerBuilder;
                InterruptableExecutor resultExecutor = concurrentBuilder.getExecutor();
                try {
                    long pathTimeout = this.manager.getConfig().getPathConfiguration().getPathReconstructionTimeout();
                    if (pathTimeout > 0) {
                        resultExecutor.awaitCompletion(pathTimeout + 20, TimeUnit.SECONDS);
                    } else {
                        resultExecutor.awaitCompletion();
                    }
                } catch (InterruptedException e) {
                    this.logger.error("Could not wait for executor termination", (Throwable) e);
                }
                resultExecutor.reset();
            }
            batch.clear();
        }
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public InfoflowResults getResults() {
        return this.innerBuilder.getResults();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void runIncrementalPathCompuation() {
        this.innerBuilder.runIncrementalPathCompuation();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
        this.innerBuilder.forceTerminate(reason);
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isTerminated() {
        return this.innerBuilder.isTerminated();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isKilled() {
        return this.innerBuilder.isKilled();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public ISolverTerminationReason getTerminationReason() {
        return this.terminationReason;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void reset() {
        this.innerBuilder.reset();
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
        this.innerBuilder.addStatusListener(listener);
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
