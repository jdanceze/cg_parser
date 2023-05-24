package soot.jimple.infoflow.memory;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/IMemoryBoundedSolver.class */
public interface IMemoryBoundedSolver {

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/memory/IMemoryBoundedSolver$IMemoryBoundedSolverStatusNotification.class */
    public interface IMemoryBoundedSolverStatusNotification {
        void notifySolverStarted(IMemoryBoundedSolver iMemoryBoundedSolver);

        void notifySolverTerminated(IMemoryBoundedSolver iMemoryBoundedSolver);
    }

    void forceTerminate(ISolverTerminationReason iSolverTerminationReason);

    boolean isTerminated();

    boolean isKilled();

    ISolverTerminationReason getTerminationReason();

    void reset();

    void addStatusListener(IMemoryBoundedSolverStatusNotification iMemoryBoundedSolverStatusNotification);
}
