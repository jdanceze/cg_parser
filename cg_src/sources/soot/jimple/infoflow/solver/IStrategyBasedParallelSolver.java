package soot.jimple.infoflow.solver;

import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.infoflow.solver.fastSolver.ISchedulingStrategy;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/IStrategyBasedParallelSolver.class */
public interface IStrategyBasedParallelSolver<N, D extends FastSolverLinkedNode<D, N>> {
    void setSchedulingStrategy(ISchedulingStrategy<N, D> iSchedulingStrategy);
}
