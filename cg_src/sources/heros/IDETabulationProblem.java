package heros;

import heros.InterproceduralCFG;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/IDETabulationProblem.class */
public interface IDETabulationProblem<N, D, M, V, I extends InterproceduralCFG<N, M>> extends IFDSTabulationProblem<N, D, M, I> {
    EdgeFunctions<N, D, M, V> edgeFunctions();

    MeetLattice<V> meetLattice();

    EdgeFunction<V> allTopFunction();
}
