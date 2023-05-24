package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
import soot.SootMethod;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/AbstractGarbageCollector.class */
public abstract class AbstractGarbageCollector<N, D> implements IGarbageCollector<N, D> {
    protected final BiDiInterproceduralCFG<N, SootMethod> icfg;
    protected final IGCReferenceProvider<D, N> referenceProvider;
    protected final ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions;

    public AbstractGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions, IGCReferenceProvider<D, N> referenceProvider) {
        this.icfg = icfg;
        this.referenceProvider = referenceProvider;
        this.jumpFunctions = jumpFunctions;
        initialize();
    }

    public AbstractGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions) {
        this.icfg = icfg;
        this.referenceProvider = createReferenceProvider();
        this.jumpFunctions = jumpFunctions;
        initialize();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void initialize() {
    }

    protected IGCReferenceProvider<D, N> createReferenceProvider() {
        return new OnDemandReferenceProvider(this.icfg);
    }
}
