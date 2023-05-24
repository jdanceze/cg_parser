package soot.jimple.infoflow.solver.gcSolver;

import java.util.Set;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/AheadOfTimeReferenceProvider.class */
public class AheadOfTimeReferenceProvider<D, N> extends AbstractReferenceProvider<D, N> {
    private final MultiMap<SootMethod, SootMethod> methodToCallees;

    public AheadOfTimeReferenceProvider(BiDiInterproceduralCFG<N, SootMethod> icfg) {
        super(icfg);
        this.methodToCallees = new HashMultiMap();
        for (SootClass sc : Scene.v().getClasses()) {
            for (SootMethod sm : sc.getMethods()) {
                this.methodToCallees.putAll(sm, getTransitiveCallees(sm));
            }
        }
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGCReferenceProvider
    public Set<SootMethod> getMethodReferences(SootMethod method, FastSolverLinkedNode<D, N> context) {
        return this.methodToCallees.get(method);
    }
}
