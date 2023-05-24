package soot.jimple.infoflow.solver.gcSolver;

import heros.FlowFunction;
import heros.solver.PathEdge;
import java.util.Collection;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.problems.AbstractInfoflowProblem;
import soot.jimple.infoflow.solver.EndSummary;
import soot.jimple.infoflow.solver.IFollowReturnsPastSeedsHandler;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.executors.InterruptableExecutor;
import soot.jimple.infoflow.solver.functions.SolverCallFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverCallToReturnFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverNormalFlowFunction;
import soot.jimple.infoflow.solver.functions.SolverReturnFlowFunction;
import soot.jimple.infoflow.solver.gcSolver.IFDSSolver;
import soot.jimple.toolkits.ide.icfg.BiDiInterproceduralCFG;
import soot.util.ConcurrentHashMultiMap;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/solver/gcSolver/InfoflowSolver.class */
public class InfoflowSolver extends IFDSSolver<Unit, Abstraction, BiDiInterproceduralCFG<Unit, SootMethod>> implements IInfoflowSolver {
    private IFollowReturnsPastSeedsHandler followReturnsPastSeedsHandler;
    private final AbstractInfoflowProblem problem;

    public InfoflowSolver(AbstractInfoflowProblem problem, InterruptableExecutor executor) {
        super(problem);
        this.followReturnsPastSeedsHandler = null;
        this.problem = problem;
        this.executor = executor;
        problem.setSolver(this);
    }

    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    protected InterruptableExecutor getExecutor() {
        return this.executor;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4 */
    /* JADX WARN: Type inference failed for: r0v5, types: [java.lang.Throwable] */
    /* JADX WARN: Type inference failed for: r0v8 */
    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public boolean processEdge(PathEdge<Unit, Abstraction> edge) {
        if (this.garbageCollector == null) {
            ?? r0 = this;
            synchronized (r0) {
                if (this.garbageCollector == null) {
                    this.garbageCollector = createGarbageCollector();
                }
                r0 = r0;
            }
        }
        propagate(edge.factAtSource(), edge.getTarget(), edge.factAtTarget(), null, false);
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
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public Set<Abstraction> computeReturnFlowFunction(FlowFunction<Abstraction> retFunction, Abstraction d1, Abstraction d2, Unit callSite, Collection<Abstraction> callerSideDs) {
        if (retFunction instanceof SolverReturnFlowFunction) {
            return ((SolverReturnFlowFunction) retFunction).computeTargets(d2, d1, callerSideDs);
        }
        return retFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public Set<Abstraction> computeNormalFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverNormalFlowFunction) {
            return ((SolverNormalFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public Set<Abstraction> computeCallToReturnFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverCallToReturnFlowFunction) {
            return ((SolverCallToReturnFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public Set<Abstraction> computeCallFlowFunction(FlowFunction<Abstraction> flowFunction, Abstraction d1, Abstraction d2) {
        if (flowFunction instanceof SolverCallFlowFunction) {
            return ((SolverCallFlowFunction) flowFunction).computeTargets(d1, d2);
        }
        return flowFunction.computeTargets(d2);
    }

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public void cleanup() {
        this.jumpFunctions = new ConcurrentHashMultiMap<>();
        this.incoming.clear();
        this.endSummary.clear();
        if (this.ffCache != null) {
            this.ffCache.invalidate();
        }
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public Set<EndSummary<Unit, Abstraction>> endSummary(SootMethod m, Abstraction d3) {
        return super.endSummary(m, (SootMethod) d3);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.jimple.infoflow.solver.gcSolver.IFDSSolver
    public void processExit(PathEdge<Unit, Abstraction> edge) {
        super.processExit(edge);
        if (this.followReturnsPastSeeds && this.followReturnsPastSeedsHandler != null) {
            Abstraction d1 = edge.factAtSource();
            Unit u = edge.getTarget();
            Abstraction d2 = edge.factAtTarget();
            SootMethod methodThatNeedsSummary = (SootMethod) this.icfg.getMethodOf(u);
            Set<IFDSSolver.IncomingRecord<Unit, Abstraction>> inc = incoming(d1, methodThatNeedsSummary);
            if (inc == null || inc.isEmpty()) {
                this.followReturnsPastSeedsHandler.handleFollowReturnsPastSeeds(d1, u, d2);
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

    @Override // soot.jimple.infoflow.solver.IInfoflowSolver
    public AbstractInfoflowProblem getTabulationProblem() {
        return this.problem;
    }
}
