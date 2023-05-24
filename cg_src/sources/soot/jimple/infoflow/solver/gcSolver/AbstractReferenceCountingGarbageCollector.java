package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import soot.SootMethod;
import soot.jimple.infoflow.collect.ConcurrentCountingMap;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.util.ExtendedAtomicInteger;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/AbstractReferenceCountingGarbageCollector.class */
public abstract class AbstractReferenceCountingGarbageCollector<N, D> extends AbstractGarbageCollector<N, D> implements IGarbageCollectorPeer {
    private ConcurrentCountingMap<SootMethod> jumpFnCounter;
    private final Set<SootMethod> gcScheduleSet;
    private final AtomicInteger gcedMethods;
    private final AtomicInteger gcedEdges;
    private final ExtendedAtomicInteger edgeCounterForThreshold;
    private GarbageCollectionTrigger trigger;
    private GarbageCollectorPeerGroup peerGroup;
    private boolean checkChangeCounter;
    protected boolean validateEdges;
    protected Set<PathEdge<N, D>> oldEdges;
    protected int methodThreshold;
    protected int edgeThreshold;

    public AbstractReferenceCountingGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions, IGCReferenceProvider<D, N> referenceProvider) {
        super(icfg, jumpFunctions, referenceProvider);
        this.jumpFnCounter = new ConcurrentCountingMap<>();
        this.gcScheduleSet = new ConcurrentHashSet();
        this.gcedMethods = new AtomicInteger();
        this.gcedEdges = new AtomicInteger();
        this.edgeCounterForThreshold = new ExtendedAtomicInteger();
        this.trigger = GarbageCollectionTrigger.Immediate;
        this.peerGroup = null;
        this.checkChangeCounter = false;
        this.validateEdges = false;
        this.oldEdges = new HashSet();
        this.methodThreshold = 0;
        this.edgeThreshold = 0;
    }

    public AbstractReferenceCountingGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions) {
        super(icfg, jumpFunctions);
        this.jumpFnCounter = new ConcurrentCountingMap<>();
        this.gcScheduleSet = new ConcurrentHashSet();
        this.gcedMethods = new AtomicInteger();
        this.gcedEdges = new AtomicInteger();
        this.edgeCounterForThreshold = new ExtendedAtomicInteger();
        this.trigger = GarbageCollectionTrigger.Immediate;
        this.peerGroup = null;
        this.checkChangeCounter = false;
        this.validateEdges = false;
        this.oldEdges = new HashSet();
        this.methodThreshold = 0;
        this.edgeThreshold = 0;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyEdgeSchedule(PathEdge<N, D> edge) {
        SootMethod sm = this.icfg.getMethodOf(edge.getTarget());
        this.jumpFnCounter.increment(sm);
        this.gcScheduleSet.add(sm);
        if (this.trigger == GarbageCollectionTrigger.EdgeThreshold) {
            this.edgeCounterForThreshold.incrementAndGet();
        }
        if (this.validateEdges && this.oldEdges.contains(edge)) {
            System.out.println("Edge re-scheduled");
        }
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifyTaskProcessed(PathEdge<N, D> edge) {
        this.jumpFnCounter.decrement(this.icfg.getMethodOf(edge.getTarget()));
    }

    private boolean hasActiveDependencies(SootMethod method, ConcurrentCountingMap<SootMethod> referenceCounter) {
        int changeCounter;
        do {
            changeCounter = referenceCounter.getChangeCounter();
            if (referenceCounter.get((Object) method).intValue() > 0) {
                return true;
            }
            Set<SootMethod> references = this.referenceProvider.getMethodReferences(method, null);
            for (SootMethod ref : references) {
                if (referenceCounter.get((Object) ref).intValue() > 0) {
                    return true;
                }
            }
            if (!this.checkChangeCounter) {
                return false;
            }
        } while (changeCounter != referenceCounter.getChangeCounter());
        return false;
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollectorPeer
    public boolean hasActiveDependencies(SootMethod method) {
        return hasActiveDependencies(method, this.jumpFnCounter);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* JADX WARN: Removed duplicated region for block: B:35:0x00b7  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00f3 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:56:0x0111 A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public void gcImmediate() {
        /*
            Method dump skipped, instructions count: 289
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.solver.gcSolver.AbstractReferenceCountingGarbageCollector.gcImmediate():void");
    }

    protected void onBeforeRemoveEdges() {
    }

    protected void onAfterRemoveEdges(int gcedMethods) {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedMethods() {
        return this.gcedMethods.get();
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public int getGcedEdges() {
        return this.gcedEdges.get();
    }

    public void setMethodThreshold(int threshold) {
        this.methodThreshold = threshold;
    }

    public void setEdgeThreshold(int threshold) {
        this.edgeThreshold = threshold;
    }

    public void setTrigger(GarbageCollectionTrigger trigger) {
        this.trigger = trigger;
    }

    public void setPeerGroup(GarbageCollectorPeerGroup peerGroup) {
        this.peerGroup = peerGroup;
        peerGroup.addGarbageCollector(this);
    }

    public void setCheckChangeCounter(boolean checkChangeCounter) {
        this.checkChangeCounter = checkChangeCounter;
    }
}
