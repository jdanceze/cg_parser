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
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/FlowSensitiveAliasStrategy.class */
public class FlowSensitiveAliasStrategy extends AbstractBulkAliasStrategy {
    private final IInfoflowSolver bSolver;

    public FlowSensitiveAliasStrategy(InfoflowManager manager, IInfoflowSolver backwardsSolver) {
        super(manager);
        this.bSolver = backwardsSolver;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void computeAliasTaints(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
        Abstraction bwAbs = newAbs.deriveInactiveAbstraction(src);
        for (Unit predUnit : this.manager.getICFG().getPredsOf(src)) {
            this.bSolver.processEdge(new PathEdge<>(d1, predUnit, bwAbs));
        }
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
