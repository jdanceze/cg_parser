package soot.jimple.infoflow.solver.functions;

import heros.FlowFunction;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/functions/SolverReturnFlowFunction.class */
public abstract class SolverReturnFlowFunction implements FlowFunction<Abstraction> {
    public abstract Set<Abstraction> computeTargets(Abstraction abstraction, Abstraction abstraction2, Collection<Abstraction> collection);

    @Override // heros.FlowFunction
    public Set<Abstraction> computeTargets(Abstraction source) {
        return computeTargets(source, null, Collections.emptySet());
    }
}
