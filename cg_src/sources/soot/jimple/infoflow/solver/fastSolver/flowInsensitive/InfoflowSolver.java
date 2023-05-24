package soot.jimple.infoflow.solver.fastSolver.flowInsensitive;

import heros.FlowFunction;
import heros.solver.PathEdge;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.AbstractInfoflowProblem;
import soot.jimple.infoflow.solver.EndSummary;
import soot.jimple.infoflow.solver.IFollowReturnsPastSeedsHandler;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.SolverPeerGroup;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.functions.SolverCallFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/fastSolver/flowInsensitive/InfoflowSolver.class */
public class InfoflowSolver extends FlowInsensitiveSolver<Unit, Abstraction, BiDiInterproceduralCFG<Unit, SootMethod>> implements IInfoflowSolver {
    private IFollowReturnsPastSeedsHandler followReturnsPastSeedsHandler;
    private final AbstractInfoflowProblem problem;

    public InfoflowSolver(AbstractInfoflowProblem problem, InterruptableExecutor executor) {
        super(problem);
        this.followReturnsPastSeedsHandler = null;
        this.problem = problem;
        this.executor = executor;
        problem.setSolver(this);
    }

    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    protected InterruptableExecutor getExecutor() {
        return this.executor;
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public boolean processEdge(PathEdge<Unit, Abstraction> edge) {
        propagate(edge.factAtSource(), (SootMethod) this.icfg.getMethodOf(edge.getTarget()), edge.factAtTarget(), null, false);
        return true;
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void injectContext(IInfoflowSolver otherSolver, SootMethod callee, Abstraction d3, Unit callSite, Abstraction d2, Abstraction d1) {
        if (!addIncoming(callee, d3, callSite, d1, d2)) {
            return;
        }
        Collection<Unit> returnSiteNs = this.icfg.getReturnSitesOfCallAt(callSite);
        applyEndSummaryOnCall(d1, callSite, d2, returnSiteNs, callee, d3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public Set<Abstraction> computeReturnFlowFunction(FlowFunction<Abstraction> retFunction, Abstraction d1, Abstraction d2, Unit callSite, Collection<Abstraction> callerSideDs) {
        if (retFunction instanceof SolverReturnFlowFunction) {
            return ((SolverReturnFlowFunction) retFunction).computeTargets(d2, d1, callerSideDs);
        }
        return retFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public Set<Abstraction> computeNormalFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverNormalFlowFunction) {
            return ((SolverNormalFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public Set<Abstraction> computeCallToReturnFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverCallToReturnFlowFunction) {
            return ((SolverCallToReturnFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public Set<Abstraction> computeCallFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverCallFlowFunction) {
            return ((SolverCallFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void cleanup() {
        this.jumpFunctions = new MyConcurrentHashMap<>();
        this.incoming.clear();
        this.endSummary.clear();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public Set<EndSummary<Unit, Abstraction>> endSummary(SootMethod m, Abstraction d3) {
        return super.endSummary(m, (SootMethod) d3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver
    public void processExit(Abstraction d1, Unit n, Abstraction d2) {
        super.processExit((Unit) d1, n, (Unit) d2);
        if (this.followReturnsPastSeeds && this.followReturnsPastSeedsHandler != null) {
            SootMethod methodThatNeedsSummary = (SootMethod) this.icfg.getMethodOf(n);
            Map<Unit, Map<Abstraction, Abstraction>> inc = incoming(d1, methodThatNeedsSummary);
            if (inc == null || inc.isEmpty()) {
                this.followReturnsPastSeedsHandler.handleFollowReturnsPastSeeds(d1, n, d2);
            }
        }
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void setFollowReturnsPastSeedsHandler(IFollowReturnsPastSeedsHandler handler) {
        this.followReturnsPastSeedsHandler = handler;
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public long getPropagationCount() {
        return this.propagationCount;
    }

    @Override // soot.jimple.infoflow.solver.fastSolver.flowInsensitive.FlowInsensitiveSolver, soot.jimple.infoflow.solver.IInfoflowSolver
    public void setSolverId(boolean solverId) {
        super.setSolverId(solverId);
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public AbstractInfoflowProblem getTabulationProblem() {
        return this.problem;
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void setPeerGroup(SolverPeerGroup solverPeerGroup) {
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void terminate() {
    }
}
