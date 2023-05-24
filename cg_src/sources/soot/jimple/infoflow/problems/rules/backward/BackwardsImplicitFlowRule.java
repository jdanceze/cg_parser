package soot.jimple.infoflow.problems.rules.backward;

import heros.solver.PathEdge;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.DefinitionStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.ReturnStmt;
import soot.jimple.Stmt;
import soot.jimple.SwitchStmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.collect.MyConcurrentHashMap;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.solver.cfg.IInfoflowCFG;
import soot.jimple.infoflow.sourcesSinks.manager.IReversibleSourceSinkManager;
import soot.jimple.infoflow.sourcesSinks.manager.SourceInfo;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsImplicitFlowRule.class */
public class BackwardsImplicitFlowRule extends AbstractTaintPropagationRule {
    private final MyConcurrentHashMap<Unit, Set<Abstraction>> implicitTargets;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !BackwardsImplicitFlowRule.class.desiredAssertionStatus();
    }

    public BackwardsImplicitFlowRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
        this.implicitTargets = new MyConcurrentHashMap<>();
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Value condition;
        if (!$assertionsDisabled && source.getAccessPath().isEmpty() && source.getDominator() == null) {
            throw new AssertionError();
        }
        if (source == getZeroValue()) {
            return null;
        }
        if (source.isDominator(stmt)) {
            if (!source.getAccessPath().isEmpty()) {
                Abstraction abs = source.removeDominator(stmt);
                if (abs != null) {
                    this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, stmt, abs));
                }
                killAll.value = true;
                return null;
            }
            killSource.value = true;
            if (stmt instanceof IfStmt) {
                condition = ((IfStmt) stmt).getCondition();
            } else if (stmt instanceof SwitchStmt) {
                condition = ((SwitchStmt) stmt).getKey();
            } else {
                return null;
            }
            IInfoflowCFG.UnitContainer condUnit = this.manager.getICFG().getDominatorOf(stmt);
            Set<Abstraction> res = new HashSet<>();
            if (condition instanceof Local) {
                AccessPath ap = this.manager.getAccessPathFactory().createAccessPath(condition, false);
                Abstraction abs2 = source.deriveCondition(ap, stmt);
                res.add(abs2);
                if (condUnit.getUnit() != null) {
                    res.add(abs2.deriveNewAbstractionWithDominator(condUnit.getUnit()));
                }
                return res;
            }
            for (ValueBox box : condition.getUseBoxes()) {
                if (!(box.getValue() instanceof Constant)) {
                    AccessPath ap2 = this.manager.getAccessPathFactory().createAccessPath(box.getValue(), false);
                    Abstraction abs3 = source.deriveCondition(ap2, stmt);
                    res.add(abs3);
                    if (condUnit.getUnit() != null) {
                        res.add(abs3.deriveNewAbstractionWithDominator(condUnit.getUnit()));
                    }
                }
            }
            return res;
        } else if (source.getAccessPath().isEmpty() && this.manager.getICFG().isExceptionalEdgeBetween(stmt, destStmt)) {
            if (destStmt instanceof AssignStmt) {
                AccessPath ap3 = this.manager.getAccessPathFactory().createAccessPath(((AssignStmt) destStmt).getLeftOp(), false);
                Abstraction abs4 = source.deriveNewAbstraction(ap3, stmt);
                return Collections.singleton(abs4);
            }
            return null;
        } else if (source.getAccessPath().isEmpty()) {
            return null;
        } else {
            IInfoflowCFG.UnitContainer dominator = this.manager.getICFG().getDominatorOf(stmt);
            boolean taintAffectedByStatement = (stmt instanceof DefinitionStmt) && getAliasing().mayAlias(((DefinitionStmt) stmt).getLeftOp(), source.getAccessPath().getPlainValue());
            if (dominator.getUnit() != null && dominator.getUnit() != destStmt && !taintAffectedByStatement) {
                killSource.value = true;
                Abstraction abs5 = source.deriveNewAbstractionWithDominator(dominator.getUnit(), stmt);
                return Collections.singleton(abs5);
            }
            return null;
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        Aliasing aliasing;
        Abstraction abs;
        if (!$assertionsDisabled && source.getAccessPath().isEmpty() && source.getDominator() == null) {
            throw new AssertionError();
        }
        if (source == getZeroValue() || (aliasing = getAliasing()) == null) {
            return null;
        }
        if (source.isDominator(stmt)) {
            killAll.value = true;
            if (!source.getAccessPath().isEmpty() && (abs = source.removeDominator(stmt)) != null) {
                this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, stmt, abs));
                return null;
            }
            return null;
        } else if (this.implicitTargets.containsKey(stmt) && (d1 == null || this.implicitTargets.get(stmt).contains(d1))) {
            if (killAll != null) {
                killAll.value = true;
                return null;
            }
            return null;
        } else if (source.getAccessPath().isEmpty()) {
            killAll.value = true;
            return null;
        } else if (stmt instanceof AssignStmt) {
            AssignStmt assignStmt = (AssignStmt) stmt;
            Value left = assignStmt.getLeftOp();
            boolean isImplicit = source.getDominator() != null;
            if (aliasing.mayAlias(left, source.getAccessPath().getPlainValue()) && !isImplicit) {
                Set<Abstraction> res = new HashSet<>();
                Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
                while (it.hasNext()) {
                    Unit unit = it.next();
                    if (unit instanceof ReturnStmt) {
                        ReturnStmt returnStmt = (ReturnStmt) unit;
                        Value retVal = returnStmt.getOp();
                        if (retVal instanceof Constant) {
                            Abstraction abs2 = source.deriveConditionalUpdate(stmt);
                            Abstraction abs3 = abs2.deriveNewAbstractionWithTurnUnit(stmt);
                            List<Unit> condUnits = this.manager.getICFG().getConditionalBranchIntraprocedural(returnStmt);
                            for (Unit condUnit : condUnits) {
                                Abstraction intraRet = abs3.deriveNewAbstractionWithDominator(condUnit);
                                res.add(intraRet);
                            }
                        }
                    }
                }
                return res;
            }
            return null;
        } else {
            return null;
        }
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Abstraction abs;
        if (source == getZeroValue() && (this.manager.getSourceSinkManager() instanceof IReversibleSourceSinkManager)) {
            killSource.value = true;
            IReversibleSourceSinkManager ssm = (IReversibleSourceSinkManager) this.manager.getSourceSinkManager();
            SourceInfo sink = ssm.getInverseSinkInfo(stmt, this.manager);
            if (sink != null) {
                HashSet<Abstraction> res = new HashSet<>();
                SootMethod sm = this.manager.getICFG().getMethodOf(stmt);
                List<Unit> condUnits = this.manager.getICFG().getConditionalBranchesInterprocedural(stmt);
                for (Unit condUnit : condUnits) {
                    Abstraction abs2 = new Abstraction(sink.getDefinition(), AccessPath.getEmptyAccessPath(), stmt, sink.getUserData(), false, false);
                    abs2.setCorrespondingCallSite(stmt);
                    abs2.setDominator(condUnit);
                    res.add(abs2);
                }
                if (!sm.isStatic()) {
                    AccessPath thisAp = this.manager.getAccessPathFactory().createAccessPath(sm.getActiveBody().getThisLocal(), false);
                    Abstraction thisTaint = new Abstraction(sink.getDefinition(), thisAp, stmt, sink.getUserData(), false, false);
                    thisTaint.setCorrespondingCallSite(stmt);
                    res.add(thisTaint);
                }
                return res;
            }
        }
        if (source == getZeroValue()) {
            return null;
        }
        if (source.isDominator(stmt)) {
            killAll.value = true;
            if (!source.getAccessPath().isEmpty() && (abs = source.removeDominator(stmt)) != null) {
                this.manager.getForwardSolver().processEdge(new PathEdge<>(d1, stmt, abs));
                return null;
            }
            return null;
        }
        if ((stmt instanceof AssignStmt) && getAliasing().mayAlias(((AssignStmt) stmt).getLeftOp(), source.getAccessPath().getPlainValue())) {
            boolean isImplicit = source.getDominator() != null;
            if (isImplicit) {
                killSource.value = true;
                return Collections.singleton(source.deriveConditionalUpdate(stmt));
            }
        }
        if (source.getAccessPath().isEmpty()) {
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        Abstraction abs;
        if (!$assertionsDisabled && source.getAccessPath().isEmpty() && source.getDominator() == null) {
            throw new AssertionError();
        }
        if (source == getZeroValue()) {
            return null;
        }
        if (source.isDominator(stmt)) {
            killAll.value = true;
            if (!source.getAccessPath().isEmpty() && (abs = source.removeDominator(stmt)) != null) {
                this.manager.getForwardSolver().processEdge(new PathEdge<>(calleeD1, stmt, abs));
                return null;
            }
            return null;
        } else if (source.getAccessPath().isEmpty()) {
            return Collections.singleton(source.deriveNewAbstraction(source.getAccessPath(), stmt));
        } else {
            SootMethod callee = this.manager.getICFG().getMethodOf(stmt);
            List<Local> params = callee.getActiveBody().getParameterLocals();
            InvokeExpr ie = callSite.containsInvokeExpr() ? callSite.getInvokeExpr() : null;
            if (ie != null) {
                for (int i = 0; i < params.size() && i < ie.getArgCount(); i++) {
                    if (getAliasing().mayAlias(source.getAccessPath().getPlainValue(), params.get(i)) && (ie.getArg(i) instanceof Constant)) {
                        List<Unit> condUnits = this.manager.getICFG().getConditionalBranchIntraprocedural(callSite);
                        HashSet<Abstraction> res = new HashSet<>();
                        for (Unit condUnit : condUnits) {
                            Abstraction intraRet = source.deriveNewAbstractionWithDominator(condUnit, stmt);
                            intraRet.setCorrespondingCallSite(callSite);
                            res.add(intraRet);
                        }
                        return res;
                    }
                }
                return null;
            }
            return null;
        }
    }
}
