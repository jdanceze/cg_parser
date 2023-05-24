package soot.jimple.infoflow.aliasing;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import heros.solver.PathEdge;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import soot.Local;
import soot.PointsToSet;
import soot.Scene;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.DefinitionStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.AccessPathFactory;
import soot.jimple.infoflow.data.AccessPathFragment;
import soot.jimple.infoflow.solver.IInfoflowSolver;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/aliasing/PtsBasedAliasStrategy.class */
public class PtsBasedAliasStrategy extends AbstractBulkAliasStrategy {
    private final Table<SootMethod, Abstraction, Set<Abstraction>> aliases;

    public PtsBasedAliasStrategy(InfoflowManager manager) {
        super(manager);
        this.aliases = HashBasedTable.create();
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void computeAliasTaints(Abstraction d1, Stmt src, Value targetValue, Set<Abstraction> taintSet, SootMethod method, Abstraction newAbs) {
        computeAliasTaintsInternal(d1, method, newAbs, Collections.emptyList(), newAbs.getAccessPath().getTaintSubFields(), src);
    }

    /* JADX WARN: Type inference failed for: r0v7, types: [java.lang.Throwable, com.google.common.collect.Table<soot.SootMethod, soot.jimple.infoflow.data.Abstraction, java.util.Set<soot.jimple.infoflow.data.Abstraction>>] */
    public void computeAliasTaintsInternal(Abstraction d1, SootMethod method, Abstraction newAbs, List<AccessPathFragment> appendFragments, boolean taintSubFields, Stmt actStmt) {
        Abstraction aliasAbsRight;
        Abstraction aliasAbsRight2;
        Abstraction aliasAbsLeft;
        Abstraction aliasAbsLeft2;
        Abstraction absCallee;
        Stmt actStmt2 = newAbs.getActivationUnit() == null ? actStmt : (Stmt) newAbs.getActivationUnit();
        synchronized (this.aliases) {
            if (this.aliases.contains(method, newAbs)) {
                Set<Abstraction> d1s = this.aliases.get(method, newAbs);
                if (d1s.contains(d1)) {
                    return;
                }
                d1s.add(d1);
            } else {
                Set<Abstraction> d1s2 = Sets.newIdentityHashSet();
                d1s2.add(d1);
                this.aliases.put(method, newAbs, d1s2);
            }
            AccessPath ap = newAbs.getAccessPath();
            if ((ap.isInstanceFieldRef() && ap.getFirstField() != null) || (ap.isStaticFieldRef() && ap.getFragmentCount() > 1)) {
                List<AccessPathFragment> appendList = new LinkedList<>(appendFragments);
                appendList.add(0, newAbs.getAccessPath().getLastFragment());
                computeAliasTaintsInternal(d1, method, newAbs.deriveNewAbstraction(newAbs.getAccessPath().dropLastField(), null), appendList, taintSubFields, actStmt2);
            }
            if (ap.getFragmentCount() > 1) {
                return;
            }
            PointsToSet ptsTaint = getPointsToSet(newAbs.getAccessPath());
            AccessPathFragment[] appendFragmentsA = (AccessPathFragment[]) appendFragments.toArray(new AccessPathFragment[appendFragments.size()]);
            boolean beforeActUnit = method.getActiveBody().getUnits().contains(actStmt2);
            AccessPathFactory apFactory = this.manager.getAccessPathFactory();
            Iterator<Unit> it = method.getActiveBody().getUnits().iterator();
            while (it.hasNext()) {
                Unit u = it.next();
                Stmt stmt = (Stmt) u;
                if (stmt == actStmt2) {
                    beforeActUnit = false;
                }
                PointsToSet ptsBaseOrg = getPointsToSet(newAbs.getAccessPath().getPlainValue());
                Iterator<ValueBox> it2 = stmt.getUseAndDefBoxes().iterator();
                while (true) {
                    if (!it2.hasNext()) {
                        break;
                    }
                    ValueBox vb = it2.next();
                    PointsToSet ptsBase = getPointsToSet(vb.getValue());
                    if (ptsBase != null && ptsBase.hasNonEmptyIntersection(ptsBaseOrg)) {
                        AccessPath newAP = apFactory.appendFields(apFactory.copyWithNewValue(newAbs.getAccessPath(), vb.getValue()), appendFragmentsA, taintSubFields);
                        Abstraction absCallee2 = newAbs.deriveNewAbstraction(newAP, stmt);
                        if (beforeActUnit) {
                            absCallee = absCallee2.deriveInactiveAbstraction(actStmt2);
                        } else {
                            absCallee = absCallee2.getActiveCopy();
                        }
                        this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u, absCallee));
                    }
                }
                if (u instanceof DefinitionStmt) {
                    DefinitionStmt assign = (DefinitionStmt) u;
                    Value rop = assign.getRightOp();
                    Value lop = assign.getLeftOp();
                    if (isAliasedAtStmt(ptsTaint, rop) && appendFragments != null && appendFragments.size() > 0 && (aliasAbsLeft = newAbs.deriveNewAbstraction(this.manager.getAccessPathFactory().createAccessPath(lop, appendFragmentsA, taintSubFields), stmt)) != null) {
                        if (beforeActUnit) {
                            aliasAbsLeft2 = aliasAbsLeft.deriveInactiveAbstraction(actStmt2);
                        } else {
                            aliasAbsLeft2 = aliasAbsLeft.getActiveCopy();
                        }
                        computeAliasTaints(d1, stmt, lop, Collections.emptySet(), method, aliasAbsLeft2);
                    }
                    if (isAliasedAtStmt(ptsTaint, lop) && isValidAccessPathRoot(rop) && (aliasAbsRight = newAbs.deriveNewAbstraction(this.manager.getAccessPathFactory().createAccessPath(rop, appendFragmentsA, taintSubFields), stmt)) != null) {
                        if (beforeActUnit) {
                            aliasAbsRight2 = aliasAbsRight.deriveInactiveAbstraction(actStmt2);
                        } else {
                            aliasAbsRight2 = aliasAbsRight.getActiveCopy();
                        }
                        this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, u, aliasAbsRight2));
                    }
                }
            }
        }
    }

    private boolean isValidAccessPathRoot(Value op) {
        return (op instanceof FieldRef) || (op instanceof Local) || (op instanceof ArrayRef);
    }

    private boolean isAliasedAtStmt(PointsToSet ptsTaint, Value val) {
        PointsToSet ptsRight;
        return (ptsTaint == null || (ptsRight = getPointsToSet(val)) == null || !ptsTaint.hasNonEmptyIntersection(ptsRight)) ? false : true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Throwable, soot.PointsToAnalysis] */
    private PointsToSet getPointsToSet(Value targetValue) {
        ?? pointsToAnalysis = Scene.v().getPointsToAnalysis();
        synchronized (pointsToAnalysis) {
            if (targetValue instanceof Local) {
                return pointsToAnalysis.reachingObjects((Local) targetValue);
            } else if (targetValue instanceof InstanceFieldRef) {
                InstanceFieldRef iref = (InstanceFieldRef) targetValue;
                return pointsToAnalysis.reachingObjects((Local) iref.getBase(), iref.getField());
            } else if (targetValue instanceof StaticFieldRef) {
                StaticFieldRef sref = (StaticFieldRef) targetValue;
                return pointsToAnalysis.reachingObjects(sref.getField());
            } else if (targetValue instanceof ArrayRef) {
                ArrayRef aref = (ArrayRef) targetValue;
                return pointsToAnalysis.reachingObjects((Local) aref.getBase());
            } else {
                return null;
            }
        }
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
        return false;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public boolean requiresAnalysisOnReturn() {
        return true;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public IInfoflowSolver getSolver() {
        return null;
    }

    @Override // soot.jimple.infoflow.aliasing.IAliasingStrategy
    public void cleanup() {
        this.aliases.clear();
    }
}
