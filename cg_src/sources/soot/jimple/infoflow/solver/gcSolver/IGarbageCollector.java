package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IGarbageCollector.class */
public interface IGarbageCollector<N, D> {
    void notifyEdgeSchedule(PathEdge<N, D> pathEdge);

    void notifyTaskProcessed(PathEdge<N, D> pathEdge);

    void gc();

    int getGcedMethods();

    int getGcedEdges();

    void notifySolverTerminated();
}
