package soot.jimple.infoflow.solver;

import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/SolverPeerGroup.class */
public abstract class SolverPeerGroup {
    protected Set<IInfoflowSolver> solvers = new HashSet();

    public void addSolver(IInfoflowSolver solver) {
        this.solvers.add(solver);
    }
}
