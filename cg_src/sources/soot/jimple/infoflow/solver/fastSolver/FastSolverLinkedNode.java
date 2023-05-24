package soot.jimple.infoflow.solver.fastSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/FastSolverLinkedNode.class */
public interface FastSolverLinkedNode<D, N> extends Cloneable {
    boolean addNeighbor(D d);

    int getNeighborCount();

    void setPredecessor(D d);

    D getPredecessor();

    D clone();

    D getActiveCopy();

    int getPathLength();
}
