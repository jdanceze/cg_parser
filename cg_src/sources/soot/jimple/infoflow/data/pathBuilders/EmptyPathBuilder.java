package soot.jimple.infoflow.data.pathBuilders;

import java.util.Set;
import soot.jimple.infoflow.data.AbstractionAtSink;
import soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.ISolverTerminationReason;
import soot.jimple.infoflow.results.InfoflowResults;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/data/pathBuilders/EmptyPathBuilder.class */
public class EmptyPathBuilder implements IAbstractionPathBuilder {
    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void computeTaintPaths(Set<AbstractionAtSink> res) {
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public InfoflowResults getResults() {
        return new InfoflowResults();
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void addResultAvailableHandler(IAbstractionPathBuilder.OnPathBuilderResultAvailable handler) {
    }

    @Override // soot.jimple.infoflow.data.pathBuilders.IAbstractionPathBuilder
    public void runIncrementalPathCompuation() {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isTerminated() {
        return false;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public boolean isKilled() {
        return false;
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void reset() {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void addStatusListener(IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification listener) {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public void forceTerminate(ISolverTerminationReason reason) {
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver
    public ISolverTerminationReason getTerminationReason() {
        return null;
    }
}
