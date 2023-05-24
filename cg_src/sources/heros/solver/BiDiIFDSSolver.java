package heros.solver;

import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
import heros.solver.IFDSSolver;
import heros.solver.JoinHandlingNode;
import java.util.HashSet;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/solver/BiDiIFDSSolver.class */
public class BiDiIFDSSolver<N, D extends JoinHandlingNode<D>, M, I extends InterproceduralCFG<N, M>> extends BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I> {
    public BiDiIFDSSolver(IFDSTabulationProblem<N, D, M, I> forwardProblem, IFDSTabulationProblem<N, D, M, I> backwardProblem) {
        super(IFDSSolver.createIDETabulationProblem(forwardProblem), IFDSSolver.createIDETabulationProblem(backwardProblem));
    }

    public Set<D> fwIFDSResultAt(N stmt) {
        return extractResults(this.fwSolver.resultsAt(stmt).keySet());
    }

    public Set<D> bwIFDSResultAt(N stmt) {
        return extractResults(this.bwSolver.resultsAt(stmt).keySet());
    }

    private Set<D> extractResults(Set<BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I>.AbstractionWithSourceStmt> annotatedResults) {
        HashSet hashSet = new HashSet();
        for (BiDiIDESolver<N, D, M, IFDSSolver.BinaryDomain, I>.AbstractionWithSourceStmt abstractionWithSourceStmt : annotatedResults) {
            hashSet.add(abstractionWithSourceStmt.getAbstraction());
        }
        return hashSet;
    }
}
