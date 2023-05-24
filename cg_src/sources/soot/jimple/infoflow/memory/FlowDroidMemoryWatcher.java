package soot.jimple.infoflow.memory;

import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.jimple.infoflow.collect.ConcurrentHashSet;
import soot.jimple.infoflow.memory.MemoryWarningSystem;
import soot.jimple.infoflow.memory.reasons.OutOfMemoryReason;
import soot.jimple.infoflow.results.InfoflowResults;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/FlowDroidMemoryWatcher.class */
public class FlowDroidMemoryWatcher {
    private final Logger logger;
    private final MemoryWarningSystem warningSystem;
    private final Set<IMemoryBoundedSolver> solvers;
    private final InfoflowResults results;
    private ISolversTerminatedCallback terminationCallback;

    public FlowDroidMemoryWatcher() {
        this((InfoflowResults) null);
    }

    public FlowDroidMemoryWatcher(double threshold) {
        this(null, threshold);
    }

    public FlowDroidMemoryWatcher(InfoflowResults res) {
        this(res, 0.9d);
    }

    public FlowDroidMemoryWatcher(InfoflowResults res, double threshold) {
        this.logger = LoggerFactory.getLogger(getClass());
        this.warningSystem = new MemoryWarningSystem();
        this.solvers = new ConcurrentHashSet();
        this.terminationCallback = null;
        this.warningSystem.addListener(new MemoryWarningSystem.OnMemoryThresholdReached() { // from class: soot.jimple.infoflow.memory.FlowDroidMemoryWatcher.1
            @Override // soot.jimple.infoflow.memory.MemoryWarningSystem.OnMemoryThresholdReached
            public void onThresholdReached(long usedMemory, long maxMemory) {
                if (FlowDroidMemoryWatcher.this.results != null) {
                    FlowDroidMemoryWatcher.this.results.addException("Memory threshold reached");
                }
                FlowDroidMemoryWatcher.this.forceTerminate();
                FlowDroidMemoryWatcher.this.logger.warn("Running out of memory, solvers terminated");
                if (FlowDroidMemoryWatcher.this.terminationCallback != null) {
                    FlowDroidMemoryWatcher.this.terminationCallback.onSolversTerminated();
                }
            }
        });
        this.warningSystem.setWarningThreshold(threshold);
        this.results = res;
    }

    public void addSolver(IMemoryBoundedSolver solver) {
        this.solvers.add(solver);
    }

    public boolean removeSolver(IMemoryBoundedSolver solver) {
        return this.solvers.remove(solver);
    }

    public void clearSolvers() {
        this.solvers.clear();
    }

    public void close() {
        clearSolvers();
        this.warningSystem.close();
    }

    public void forceTerminate() {
        Runtime runtime = Runtime.getRuntime();
        long usedMem = runtime.totalMemory() - runtime.freeMemory();
        forceTerminate(new OutOfMemoryReason(usedMem));
    }

    public void forceTerminate(ISolverTerminationReason reason) {
        for (IMemoryBoundedSolver solver : this.solvers) {
            solver.forceTerminate(reason);
        }
    }

    public void setTerminationCallback(ISolversTerminatedCallback terminationCallback) {
        this.terminationCallback = terminationCallback;
    }
}
