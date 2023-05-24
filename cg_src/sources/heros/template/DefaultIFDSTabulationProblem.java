package heros.template;

import heros.FlowFunctions;
import heros.IFDSTabulationProblem;
import heros.InterproceduralCFG;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/template/DefaultIFDSTabulationProblem.class */
public abstract class DefaultIFDSTabulationProblem<N, D, M, I extends InterproceduralCFG<N, M>> implements IFDSTabulationProblem<N, D, M, I> {
    private final I icfg;
    private FlowFunctions<N, D, M> flowFunctions;
    private D zeroValue;

    protected abstract FlowFunctions<N, D, M> createFlowFunctionsFactory();

    protected abstract D createZeroValue();

    public DefaultIFDSTabulationProblem(I icfg) {
        this.icfg = icfg;
    }

    @Override // heros.IFDSTabulationProblem
    public final FlowFunctions<N, D, M> flowFunctions() {
        if (this.flowFunctions == null) {
            this.flowFunctions = createFlowFunctionsFactory();
        }
        return this.flowFunctions;
    }

    @Override // heros.IFDSTabulationProblem
    public I interproceduralCFG() {
        return this.icfg;
    }

    @Override // heros.IFDSTabulationProblem
    public final D zeroValue() {
        if (this.zeroValue == null) {
            this.zeroValue = createZeroValue();
        }
        return this.zeroValue;
    }

    @Override // heros.SolverConfiguration
    public boolean followReturnsPastSeeds() {
        return false;
    }

    @Override // heros.SolverConfiguration
    public boolean autoAddZero() {
        return true;
    }

    @Override // heros.SolverConfiguration
    public int numThreads() {
        return Runtime.getRuntime().availableProcessors();
    }

    @Override // heros.SolverConfiguration
    public boolean computeValues() {
        return true;
    }

    @Override // heros.SolverConfiguration
    public boolean recordEdges() {
        return false;
    }
}
