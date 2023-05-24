package soot.jimple.infoflow.aliasing;

import soot.PointsToSet;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.solver.IInfoflowSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/LazyAliasingStrategy.class */
public class LazyAliasingStrategy extends AbstractInteractiveAliasStrategy {
    public LazyAliasingStrategy(InfoflowManager manager) {
        super(manager);
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isInteractive() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean mayAlias(AccessPath ap1, AccessPath ap2) {
        if (ap1 == ap2 || ap1.equals(ap2)) {
            return true;
        }
        PointsToSet ptaAP1 = getPointsToSet(ap1);
        PointsToSet ptaAP2 = getPointsToSet(ap2);
        if (ptaAP1.hasNonEmptyIntersection(ptaAP2)) {
            return true;
        }
        return false;
    }

    private PointsToSet getPointsToSet(AccessPath accessPath) {
        if (accessPath.isLocal()) {
            return Scene.v().getPointsToAnalysis().reachingObjects(accessPath.getPlainValue());
        }
        if (accessPath.isInstanceFieldRef()) {
            return Scene.v().getPointsToAnalysis().reachingObjects(accessPath.getPlainValue(), accessPath.getFirstField());
        }
        if (accessPath.isStaticFieldRef()) {
            return Scene.v().getPointsToAnalysis().reachingObjects(accessPath.getFirstField());
        }
        throw new RuntimeException("Unexepected access path type");
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void injectCallingContext(Abstraction abs, IInfoflowSolver fSolver, SootMethod callee, Unit callSite, Abstraction source, Abstraction d1) {
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isFlowSensitive() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean requiresAnalysisOnReturn() {
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.AbstractAliasStrategy, soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean hasProcessedMethod(SootMethod method) {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean isLazyAnalysis() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public IInfoflowSolver getSolver() {
        return null;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void cleanup() {
    }
}
