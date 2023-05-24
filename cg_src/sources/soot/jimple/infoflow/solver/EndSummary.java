package soot.jimple.infoflow.solver;

import java.util.Objects;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/EndSummary.class */
public class EndSummary<N, D extends FastSolverLinkedNode<D, N>> {
    public N eP;
    public D d4;
    public D calleeD1;

    public EndSummary(N eP, D d4, D calleeD1) {
        this.eP = eP;
        this.d4 = d4;
        this.calleeD1 = calleeD1;
    }

    public int hashCode() {
        return Objects.hash(this.calleeD1, this.d4, this.eP);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        EndSummary other = (EndSummary) obj;
        return Objects.equals(this.calleeD1, other.calleeD1) && Objects.equals(this.d4, other.d4) && Objects.equals(this.eP, other.eP);
    }
}
