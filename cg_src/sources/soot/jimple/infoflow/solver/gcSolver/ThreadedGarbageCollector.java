package soot.jimple.infoflow.solver.gcSolver;

import heros.solver.PathEdge;
import soot.SootMethod;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/ThreadedGarbageCollector.class */
public class ThreadedGarbageCollector<N, D> extends AbstractReferenceCountingGarbageCollector<N, D> {
    private int sleepTimeSeconds;
    private ThreadedGarbageCollector<N, D>.GCThread gcThread;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/ThreadedGarbageCollector$GCThread.class */
    private class GCThread extends Thread {
        private boolean finished = false;

        public GCThread() {
            setName("IFDS Garbage Collector");
        }

        @Override // java.lang.Thread, java.lang.Runnable
        public void run() {
            while (!this.finished) {
                ThreadedGarbageCollector.this.gcImmediate();
                if (ThreadedGarbageCollector.this.sleepTimeSeconds > 0) {
                    try {
                        Thread.sleep(ThreadedGarbageCollector.this.sleepTimeSeconds * 1000);
                    } catch (InterruptedException e) {
                        return;
                    }
                }
            }
        }

        public void finish() {
            this.finished = true;
            interrupt();
        }
    }

    public ThreadedGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions, IGCReferenceProvider<D, N> referenceProvider) {
        super(icfg, jumpFunctions, referenceProvider);
        this.sleepTimeSeconds = 10;
    }

    public ThreadedGarbageCollector(BiDiInterproceduralCFG<N, SootMethod> icfg, ConcurrentHashMultiMap<SootMethod, PathEdge<N, D>> jumpFunctions) {
        super(icfg, jumpFunctions);
        this.sleepTimeSeconds = 10;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.gcSolver.AbstractGarbageCollector
    public void initialize() {
        super.initialize();
        this.gcThread = new GCThread();
        this.gcThread.start();
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void gc() {
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IGarbageCollector
    public void notifySolverTerminated() {
        this.gcThread.finish();
    }

    public void setSleepTimeSeconds(int sleepTimeSeconds) {
        this.sleepTimeSeconds = sleepTimeSeconds;
    }
}
