package heros;

import java.util.Collection;
import java.util.List;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/InterproceduralCFG.class */
public interface InterproceduralCFG<N, M> {
    M getMethodOf(N n);

    List<N> getPredsOf(N n);

    List<N> getSuccsOf(N n);

    Collection<M> getCalleesOfCallAt(N n);

    Collection<N> getCallersOf(M m);

    Set<N> getCallsFromWithin(M m);

    Collection<N> getStartPointsOf(M m);

    Collection<N> getReturnSitesOfCallAt(N n);

    boolean isCallStmt(N n);

    boolean isExitStmt(N n);

    boolean isStartPoint(N n);

    Set<N> allNonCallStartNodes();

    boolean isFallThroughSuccessor(N n, N n2);

    boolean isBranchTarget(N n, N n2);
}
