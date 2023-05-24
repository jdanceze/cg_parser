package soot.jimple.infoflow.solver.gcSolver;

import java.util.Set;
import soot.SootMethod;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/IGCReferenceProvider.class */
public interface IGCReferenceProvider<D, N> {
    Set<SootMethod> getMethodReferences(SootMethod sootMethod, FastSolverLinkedNode<D, N> fastSolverLinkedNode);
}
