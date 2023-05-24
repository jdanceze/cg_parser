package soot.jimple.infoflow.aliasing;

import heros.solver.PathEdge;
import java.util.Set;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.solver.IInfoflowSolver;
import soot.jimple.infoflow.solver.cfg.BackwardsInfoflowCFG;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/BackwardsFlowSensitiveAliasStrategy.class */
public class BackwardsFlowSensitiveAliasStrategy extends AbstractBulkAliasStrategy {
    private final IInfoflowSolver bSolver;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BackwardsFlowSensitiveAliasStrategy.class.desiredAssertionStatus();
    }

    public BackwardsFlowSensitiveAliasStrategy(InfoflowManager manager, IInfoflowSolver backwardsSolver) {
        super(manager);
        this.bSolver = backwardsSolver;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void computeAliasTaints(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
        if (!$assertionsDisabled && !(this.manager.getICFG() instanceof BackwardsInfoflowCFG)) {
            throw new AssertionError();
        }
        this.bSolver.processEdge(new PathEdge<>(d1, src, newAbs));
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void injectCallingContext(Abstraction d3, IInfoflowSolver fSolver, SootMethod callee, Unit callSite, Abstraction source, Abstraction d1) {
        this.bSolver.injectContext(fSolver, callee, d3, callSite, source, d1);
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isFlowSensitive() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean requiresAnalysisOnReturn() {
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public IInfoflowSolver getSolver() {
        return this.bSolver;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void cleanup() {
        this.bSolver.cleanup();
    }
}
