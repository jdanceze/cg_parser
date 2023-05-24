package soot.jimple.infoflow.solver.gcSolver;

import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import heros.SynchronizedBy;
import heros.solver.IDESolver;
import java.util.Set;
import soot.SootMethod;
import soot.jimple.infoflow.solver.fastSolver.FastSolverLinkedNode;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/OnDemandReferenceProvider.class */
public class OnDemandReferenceProvider<D, N> extends AbstractReferenceProvider<D, N> {
    @SynchronizedBy("by use of synchronized LoadingCache class")
    protected final LoadingCache<SootMethod, Set<SootMethod>> methodToReferences;

    public OnDemandReferenceProvider(BiDiInterproceduralCFG<N, SootMethod> icfg) {
        super(icfg);
        this.methodToReferences = IDESolver.DEFAULT_CACHE_BUILDER.build(new CacheLoader<SootMethod, Set<SootMethod>>() { // from class: soot.jimple.infoflow.solver.gcSolver.OnDemandReferenceProvider.1
            @Override // com.google.common.cache.CacheLoader
            public Set<SootMethod> load(SootMethod key) throws Exception {
                return OnDemandReferenceProvider.this.getTransitiveCallees(key);
            }
        });
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGCReferenceProvider
    public Set<SootMethod> getMethodReferences(SootMethod method, FastSolverLinkedNode<D, N> context) {
        return this.methodToReferences.getUnchecked(method);
    }
}
