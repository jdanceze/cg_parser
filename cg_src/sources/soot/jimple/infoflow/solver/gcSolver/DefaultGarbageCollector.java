package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
import soot.SootMethod;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/DefaultGarbageCollector.class */
public class DefaultGarbageCollector<N, D> extends AbstractReferenceCountingGarbageCollector<N, D> {
    public DefaultGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions) {
        super(icfg, jumpFunctions);
    }

    public DefaultGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions, IGCReferenceProvider<D, N> referenceProvider) {
        super(icfg, jumpFunctions, referenceProvider);
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void gc() {
        gcImmediate();
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifySolverTerminated() {
    }
}
