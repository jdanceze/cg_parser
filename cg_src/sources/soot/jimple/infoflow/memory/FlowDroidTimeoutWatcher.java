package soot.jimple.infoflow.memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.memory.IMemoryBoundedSolver;
import soot.jimple.infoflow.memory.reasons.TimeoutReason;
import soot.jimple.infoflow.results.InfoflowResults;
import soot.jimple.infoflow.util.ThreadUtils;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/FlowDroidTimeoutWatcher.class */
public class FlowDroidTimeoutWatcher implements IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification {
    private final Logger logger;
    private final long timeout;
    private final InfoflowResults results;
    private final Map<IMemoryBoundedSolver, SolverState> solvers;
    private volatile boolean stopped;
    private ISolversTerminatedCallback terminationCallback;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/FlowDroidTimeoutWatcher$SolverState.class */
    public enum SolverState {
        IDLE,
        RUNNING,
        DONE;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static SolverState[] valuesCustom() {
            SolverState[] valuesCustom = values();
            int length = valuesCustom.length;
            SolverState[] solverStateArr = new SolverState[length];
            System.arraycopy(valuesCustom, 0, solverStateArr, 0, length);
            return solverStateArr;
        }
    }

    public FlowDroidTimeoutWatcher(long timeout) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.solvers = new ConcurrentHashMap();
        this.stopped = false;
        this.terminationCallback = null;
        this.timeout = timeout;
        this.results = null;
    }

    public FlowDroidTimeoutWatcher(long timeout, InfoflowResults res) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.solvers = new ConcurrentHashMap();
        this.stopped = false;
        this.terminationCallback = null;
        this.timeout = timeout;
        this.results = res;
    }

    public long getTimeout() {
        return this.timeout;
    }

    public void addSolver(IMemoryBoundedSolver solver) {
        this.solvers.put(solver, SolverState.IDLE);
        solver.addStatusListener(this);
    }

    public void start() {
        final long startTime = System.nanoTime();
        this.logger.info("FlowDroid timeout watcher started");
        this.stopped = false;
        ThreadUtils.createGenericThread(new Runnable() { // from class: soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.1
            @Override // java.lang.Runnable
            public void run() {
                boolean allTerminated = isTerminated();
                long timeoutNano = TimeUnit.SECONDS.toNanos(FlowDroidTimeoutWatcher.this.timeout);
                while (!FlowDroidTimeoutWatcher.this.stopped && System.nanoTime() - startTime < timeoutNano) {
                    allTerminated = isTerminated();
                    if (allTerminated) {
                        break;
                    }
                    try {
                        Thread.sleep(1000L);
                    } catch (InterruptedException e) {
                    }
                }
                long timeElapsed = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime() - startTime);
                if (!FlowDroidTimeoutWatcher.this.stopped && !allTerminated) {
                    FlowDroidTimeoutWatcher.this.logger.warn("Timeout reached, stopping the solvers...");
                    if (FlowDroidTimeoutWatcher.this.results != null) {
                        FlowDroidTimeoutWatcher.this.results.addException("Timeout reached");
                    }
                    TimeoutReason reason = new TimeoutReason(timeElapsed, FlowDroidTimeoutWatcher.this.timeout);
                    for (IMemoryBoundedSolver solver : FlowDroidTimeoutWatcher.this.solvers.keySet()) {
                        solver.forceTerminate(reason);
                    }
                    if (FlowDroidTimeoutWatcher.this.terminationCallback != null) {
                        FlowDroidTimeoutWatcher.this.terminationCallback.onSolversTerminated();
                    }
                }
                FlowDroidTimeoutWatcher.this.logger.info("FlowDroid timeout watcher terminated");
            }

            /* JADX WARN: Removed duplicated region for block: B:3:0x0015  */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct add '--show-bad-code' argument
            */
            private boolean isTerminated() {
                /*
                    r3 = this;
                    r0 = r3
                    soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher r0 = soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.this
                    java.util.Map r0 = soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.access$4(r0)
                    java.util.Set r0 = r0.keySet()
                    java.util.Iterator r0 = r0.iterator()
                    r5 = r0
                    goto L3d
                L15:
                    r0 = r5
                    java.lang.Object r0 = r0.next()
                    soot.jimple.infoflow.memory.IMemoryBoundedSolver r0 = (soot.jimple.infoflow.memory.IMemoryBoundedSolver) r0
                    r4 = r0
                    r0 = r3
                    soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher r0 = soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.this
                    java.util.Map r0 = soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.access$4(r0)
                    r1 = r4
                    java.lang.Object r0 = r0.get(r1)
                    soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher$SolverState r1 = soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.SolverState.DONE
                    if (r0 != r1) goto L3b
                    r0 = r4
                    boolean r0 = r0.isTerminated()
                    if (r0 != 0) goto L3d
                L3b:
                    r0 = 0
                    return r0
                L3d:
                    r0 = r5
                    boolean r0 = r0.hasNext()
                    if (r0 != 0) goto L15
                    r0 = 1
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: soot.jimple.infoflow.memory.FlowDroidTimeoutWatcher.AnonymousClass1.isTerminated():boolean");
            }
        }, "FlowDroid Timeout Watcher", true).start();
    }

    public void stop() {
        this.stopped = true;
    }

    public void reset() {
        this.stopped = false;
        for (IMemoryBoundedSolver solver : this.solvers.keySet()) {
            this.solvers.put(solver, SolverState.IDLE);
        }
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification
    public void notifySolverStarted(IMemoryBoundedSolver solver) {
        this.solvers.put(solver, SolverState.RUNNING);
    }

    @Override // soot.jimple.infoflow.memory.IMemoryBoundedSolver.IMemoryBoundedSolverStatusNotification
    public void notifySolverTerminated(IMemoryBoundedSolver solver) {
        this.solvers.put(solver, SolverState.DONE);
    }

    public void setTerminationCallback(ISolversTerminatedCallback terminationCallback) {
        this.terminationCallback = terminationCallback;
    }
}
