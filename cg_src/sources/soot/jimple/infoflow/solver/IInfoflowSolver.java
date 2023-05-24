package soot.jimple.infoflow.solver;

import heros.solver.PathEdge;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.AbstractInfoflowProblem;
import soot.jimple.infoflow.solver.memory.IMemoryManager;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/IInfoflowSolver.class */
public interface IInfoflowSolver {
    boolean processEdge(PathEdge<Unit, Abstraction> pathEdge);

    Set<EndSummary<Unit, Abstraction>> endSummary(SootMethod sootMethod, Abstraction abstraction);

    void injectContext(IInfoflowSolver iInfoflowSolver, SootMethod sootMethod, Abstraction abstraction, Unit unit, Abstraction abstraction2, Abstraction abstraction3);

    void cleanup();

    void setFollowReturnsPastSeedsHandler(IFollowReturnsPastSeedsHandler iFollowReturnsPastSeedsHandler);

    void setMemoryManager(IMemoryManager<Abstraction, Unit> iMemoryManager);

    IMemoryManager<Abstraction, Unit> getMemoryManager();

    void setPredecessorShorteningMode(PredecessorShorteningMode predecessorShorteningMode);

    long getPropagationCount();

    void solve();

    void setSolverId(boolean z);

    AbstractInfoflowProblem getTabulationProblem();

    void setMaxJoinPointAbstractions(int i);

    void setMaxCalleesPerCallSite(int i);

    void setMaxAbstractionPathLength(int i);

    void setPeerGroup(SolverPeerGroup solverPeerGroup);

    void terminate();
}
