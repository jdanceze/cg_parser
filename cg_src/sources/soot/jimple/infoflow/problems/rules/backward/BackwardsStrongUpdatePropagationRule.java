package soot.jimple.infoflow.problems.rules.backward;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import soot.ArrayType;
import soot.Local;
import soot.PrimType;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AnyNewExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.FieldRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.ReturnStmt;
import soot.jimple.StaticFieldRef;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.BaseSelector;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsStrongUpdatePropagationRule.class */
public class BackwardsStrongUpdatePropagationRule extends AbstractTaintPropagationRule {
    public BackwardsStrongUpdatePropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (!(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        Value leftOp = assignStmt.getLeftOp();
        Aliasing aliasing = getAliasing();
        if (aliasing == null || source.getCurrentStmt() == stmt) {
            return null;
        }
        if ((source.getPredecessor() != null && !source.getPredecessor().isAbstractionActive() && source.isAbstractionActive() && source.getAccessPath().equals(source.getPredecessor().getAccessPath())) || aliasing.mayAlias(BaseSelector.selectBase(leftOp, false), source.getAccessPath().getPlainValue())) {
            return null;
        }
        boolean addRightValue = false;
        boolean cutFirstField = false;
        Type type = null;
        if (source.getAccessPath().isInstanceFieldRef()) {
            if (leftOp instanceof InstanceFieldRef) {
                InstanceFieldRef leftRef = (InstanceFieldRef) leftOp;
                if (aliasing.mustAlias((Local) leftRef.getBase(), source.getAccessPath().getPlainValue(), assignStmt) && aliasing.mustAlias(leftRef.getField(), source.getAccessPath().getFirstField())) {
                    addRightValue = true;
                    cutFirstField = true;
                    type = leftRef.getField().getType();
                }
            } else if ((leftOp instanceof Local) && aliasing.mustAlias((Local) leftOp, source.getAccessPath().getPlainValue(), stmt)) {
                addRightValue = true;
            }
        } else if (source.getAccessPath().isStaticFieldRef() && (leftOp instanceof StaticFieldRef)) {
            StaticFieldRef leftRef2 = (StaticFieldRef) leftOp;
            if (aliasing.mustAlias(leftRef2.getField(), source.getAccessPath().getFirstField())) {
                addRightValue = true;
                cutFirstField = true;
                type = leftRef2.getField().getType();
            }
        } else if (source.getAccessPath().isLocal() && (leftOp instanceof Local)) {
            if ((leftOp instanceof ArrayRef) && source.getAccessPath().getArrayTaintType() != AccessPath.ArrayTaintType.Length) {
                Value base = ((ArrayRef) leftOp).getBase();
                if ((base instanceof Local) && aliasing.mustAlias((Local) base, source.getAccessPath().getPlainValue(), stmt)) {
                    addRightValue = true;
                    type = ((ArrayType) ((ArrayRef) leftOp).getBase().getType()).getElementType();
                }
            } else if (aliasing.mustAlias((Local) leftOp, source.getAccessPath().getPlainValue(), stmt)) {
                addRightValue = true;
            }
        }
        if (addRightValue) {
            killSource.value = !(leftOp instanceof ArrayRef);
            Value rightOp = assignStmt.getRightOp();
            if ((rightOp instanceof Constant) || (rightOp instanceof AnyNewExpr)) {
                return null;
            }
            Value rightVal = BaseSelector.selectBase(assignStmt.getRightOp(), true);
            AccessPath newAp = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), rightVal, type, cutFirstField);
            Abstraction newAbs = source.deriveNewAbstraction(newAp, assignStmt);
            if (newAbs != null) {
                if ((type instanceof PrimType) || TypeUtils.isStringType(type)) {
                    newAbs = newAbs.deriveNewAbstractionWithTurnUnit(assignStmt);
                } else if (getAliasing().canHaveAliasesRightSide(assignStmt, rightVal, newAbs)) {
                    for (Unit pred : this.manager.getICFG().getPredsOf(assignStmt)) {
                        getAliasing().computeAliases(d1, (Stmt) pred, rightVal, Collections.singleton(newAbs), this.manager.getICFG().getMethodOf(pred), newAbs);
                    }
                }
                return Collections.singleton(newAbs);
            }
            return null;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
        if (source.getAccessPath().isEmpty() || !(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        Value leftOp = assignStmt.getLeftOp();
        Aliasing aliasing = getAliasing();
        if (aliasing != null && !source.getAccessPath().isStaticFieldRef() && !aliasing.mayAlias(BaseSelector.selectBase(leftOp, false), source.getAccessPath().getPlainValue()) && aliasing.mustAlias((Local) leftOp, source.getAccessPath().getPlainValue(), stmt)) {
            HashSet<Abstraction> res = new HashSet<>();
            Iterator<Unit> it = dest.getActiveBody().getUnits().iterator();
            while (it.hasNext()) {
                Unit unit = it.next();
                if (unit instanceof ReturnStmt) {
                    ReturnStmt returnStmt = (ReturnStmt) unit;
                    Value retVal = returnStmt.getOp();
                    if ((retVal instanceof Local) || (retVal instanceof FieldRef)) {
                        if (this.manager.getTypeUtils().checkCast(source.getAccessPath().getBaseType(), retVal.getType())) {
                            Type type = returnStmt.getOp().getType();
                            AccessPath ap = this.manager.getAccessPathFactory().copyWithNewValue(source.getAccessPath(), retVal, type, false);
                            Abstraction abs = source.deriveNewAbstraction(ap, stmt);
                            if (abs != null) {
                                if ((type instanceof PrimType) || TypeUtils.isStringType(type)) {
                                    abs = abs.deriveNewAbstractionWithTurnUnit(stmt);
                                }
                                abs.setCorrespondingCallSite(stmt);
                                res.add(abs);
                            }
                        }
                    }
                }
            }
            return res;
        }
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallToReturnFlow(Abstraction d1, Abstraction source, Stmt stmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        return null;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateReturnFlow(Collection<Abstraction> callerD1s, Abstraction calleeD1, Abstraction source, Stmt stmt, Stmt retSite, Stmt callSite, ByReferenceBoolean killAll) {
        return null;
    }
}
