package soot.jimple.infoflow.solver.functions;

import heros.FlowFunction;
import java.util.Set;
import soot.jimple.infoflow.data.Abstraction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/functions/SolverCallToReturnFlowFunction.class */
public abstract class SolverCallToReturnFlowFunction implements FlowFunction<Abstraction> {
    public abstract Set<Abstraction> computeTargets(Abstraction abstraction, Abstraction abstraction2);

    @Override // heros.FlowFunction
    public Set<Abstraction> computeTargets(Abstraction source) {
        return computeTargets(null, source);
    }
}
