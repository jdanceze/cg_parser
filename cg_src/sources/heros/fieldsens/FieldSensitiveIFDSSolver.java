package heros.fieldsens;

import heros.InterproceduralCFG;
import heros.fieldsens.Debugger;
import heros.utilities.DefaultValueMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:heros-1.2.3-SNAPSHOT.jar:heros/fieldsens/FieldSensitiveIFDSSolver.class */
public class FieldSensitiveIFDSSolver<FieldRef, D, N, M, I extends InterproceduralCFG<N, M>> {
    protected static final Logger logger = LoggerFactory.getLogger(FieldSensitiveIFDSSolver.class);
    private DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>> methodAnalyzers = new DefaultValueMap<M, MethodAnalyzer<FieldRef, D, N, M>>() { // from class: heros.fieldsens.FieldSensitiveIFDSSolver.1
        @Override // heros.utilities.DefaultValueMap
        protected /* bridge */ /* synthetic */ Object createItem(Object obj) {
            return createItem((AnonymousClass1) obj);
        }

        @Override // heros.utilities.DefaultValueMap
        protected MethodAnalyzer<FieldRef, D, N, M> createItem(M key) {
            return FieldSensitiveIFDSSolver.this.createMethodAnalyzer(key);
        }
    };
    private IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem;
    protected Context<FieldRef, D, N, M> context;
    protected Debugger<FieldRef, D, N, M> debugger;
    private Scheduler scheduler;

    public FieldSensitiveIFDSSolver(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler, Debugger<FieldRef, D, N, M> debugger, Scheduler scheduler) {
        this.tabulationProblem = tabulationProblem;
        this.scheduler = scheduler;
        this.debugger = debugger == null ? new Debugger.NullDebugger<>() : debugger;
        this.debugger.setICFG(tabulationProblem.interproceduralCFG());
        this.context = initContext(tabulationProblem, factHandler);
        submitInitialSeeds();
    }

    private Context<FieldRef, D, N, M> initContext(IFDSTabulationProblem<N, FieldRef, D, M, I> tabulationProblem, FactMergeHandler<D> factHandler) {
        return new Context<FieldRef, D, N, M>(tabulationProblem, this.scheduler, factHandler) { // from class: heros.fieldsens.FieldSensitiveIFDSSolver.2
            @Override // heros.fieldsens.Context
            public MethodAnalyzer<FieldRef, D, N, M> getAnalyzer(M method) {
                if (method != null) {
                    return (MethodAnalyzer) FieldSensitiveIFDSSolver.this.methodAnalyzers.getOrCreate(method);
                }
                throw new IllegalArgumentException("Method must be not null");
            }
        };
    }

    protected MethodAnalyzer<FieldRef, D, N, M> createMethodAnalyzer(M method) {
        return new MethodAnalyzerImpl(method, this.context, this.debugger);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private void submitInitialSeeds() {
        for (Map.Entry<N, Set<D>> seed : this.tabulationProblem.initialSeeds().entrySet()) {
            N startPoint = seed.getKey();
            MethodAnalyzer<FieldRef, D, N, M> analyzer = this.methodAnalyzers.getOrCreate(this.tabulationProblem.interproceduralCFG().getMethodOf(startPoint));
            for (D val : seed.getValue()) {
                analyzer.addInitialSeed(startPoint, val);
                this.debugger.initialSeed(startPoint);
            }
        }
    }
}
