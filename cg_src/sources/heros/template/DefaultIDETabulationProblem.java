package heros.template;

import heros.EdgeFunction;
import heros.EdgeFunctions;
import heros.IDETabulationProblem;
import heros.InterproceduralCFG;
import heros.MeetLattice;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/template/DefaultIDETabulationProblem.class */
public abstract class DefaultIDETabulationProblem<N, D, M, V, I extends InterproceduralCFG<N, M>> extends DefaultIFDSTabulationProblem<N, D, M, I> implements IDETabulationProblem<N, D, M, V, I> {
    private final EdgeFunction<V> allTopFunction;
    private final MeetLattice<V> joinLattice;
    private final EdgeFunctions<N, D, M, V> edgeFunctions;

    protected abstract EdgeFunction<V> createAllTopFunction();

    protected abstract MeetLattice<V> createMeetLattice();

    protected abstract EdgeFunctions<N, D, M, V> createEdgeFunctionsFactory();

    public DefaultIDETabulationProblem(I icfg) {
        super(icfg);
        this.allTopFunction = createAllTopFunction();
        this.joinLattice = createMeetLattice();
        this.edgeFunctions = createEdgeFunctionsFactory();
    }

    @Override // heros.IDETabulationProblem
    public final EdgeFunction<V> allTopFunction() {
        return this.allTopFunction;
    }

    @Override // heros.IDETabulationProblem
    public final MeetLattice<V> meetLattice() {
        return this.joinLattice;
    }

    @Override // heros.IDETabulationProblem
    public final EdgeFunctions<N, D, M, V> edgeFunctions() {
        return this.edgeFunctions;
    }
}
