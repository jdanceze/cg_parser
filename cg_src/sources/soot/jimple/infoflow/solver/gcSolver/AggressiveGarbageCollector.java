package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.Pair;
import heros.solver.PathEdge;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import soot.SootMethod;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/AggressiveGarbageCollector.class */
public class AggressiveGarbageCollector<N, D> extends AbstractGarbageCollector<N, D> {
    private final AtomicInteger gcedMethods;
    private int methodThreshold;

    public AggressiveGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions) {
        super(icfg, jumpFunctions);
        this.gcedMethods = new AtomicInteger();
        this.methodThreshold = 0;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyEdgeSchedule(PathEdge<N, D> edge) {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyTaskProcessed(PathEdge<N, D> edge) {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void gc() {
        Iterator<Pair<SootMethod, PathEdge<N, D>>> it = this.jumpFunctions.iterator();
        while (this.jumpFunctions.size() > this.methodThreshold) {
            it.next();
            it.remove();
            this.gcedMethods.incrementAndGet();
        }
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedMethods() {
        return this.gcedMethods.get();
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedEdges() {
        return 0;
    }

    public void setMethodThreshold(int threshold) {
        this.methodThreshold = threshold;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifySolverTerminated() {
    }
}
