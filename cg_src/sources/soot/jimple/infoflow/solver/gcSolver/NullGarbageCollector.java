package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/NullGarbageCollector.class */
public class NullGarbageCollector<N, D> implements IGarbageCollector<N, D> {
    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyEdgeSchedule(PathEdge<N, D> edge) {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyTaskProcessed(PathEdge<N, D> edge) {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void gc() {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedMethods() {
        return 0;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedEdges() {
        return 0;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifySolverTerminated() {
    }
}
