package heros;

import heros.InterproceduralCFG;
import java.util.Map;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/IFDSTabulationProblem.class */
public interface IFDSTabulationProblem<N, D, M, I extends InterproceduralCFG<N, M>> extends SolverConfiguration {
    FlowFunctions<N, D, M> flowFunctions();

    I interproceduralCFG();

    Map<N, Set<D>> initialSeeds();

    D zeroValue();
}
