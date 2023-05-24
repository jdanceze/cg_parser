package soot.jimple.infoflow.solver.fastSolver;

import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/ISchedulingStrategy.class */
public interface ISchedulingStrategy<N, D extends FastSolverLinkedNode<D, N>> {
    void propagateInitialSeeds(D d, N n, D d2, N n2, boolean z);

    void propagateNormalFlow(D d, N n, D d2, N n2, boolean z);

    void propagateCallFlow(D d, N n, D d2, N n2, boolean z);

    void propagateCallToReturnFlow(D d, N n, D d2, N n2, boolean z);

    void propagateReturnFlow(D d, N n, D d2, N n2, boolean z);
}
