package soot.jimple.infoflow.problems.rules.backward;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.LengthExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.aliasing.Aliasing;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.typing.TypeUtils;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/backward/BackwardsArrayPropagationRule.class */
public class BackwardsArrayPropagationRule extends AbstractTaintPropagationRule {
    public BackwardsArrayPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        if (!(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        Aliasing aliasing = this.manager.getAliasing();
        if (aliasing == null) {
            return null;
        }
        Abstraction newAbs = null;
        Value leftVal = assignStmt.getLeftOp();
        Value rightVal = assignStmt.getRightOp();
        Set<Abstraction> res = new HashSet<>();
        if (rightVal instanceof LengthExpr) {
            LengthExpr lengthExpr = (LengthExpr) rightVal;
            if (aliasing.mayAlias(leftVal, source.getAccessPath().getPlainValue())) {
                AccessPath ap = getManager().getAccessPathFactory().createAccessPath(lengthExpr.getOp(), lengthExpr.getOp().getType(), true, AccessPath.ArrayTaintType.Length);
                newAbs = source.deriveNewAbstraction(ap, assignStmt);
            }
        } else if ((rightVal instanceof NewArrayExpr) && getManager().getConfig().getEnableArraySizeTainting()) {
            NewArrayExpr newArrayExpr = (NewArrayExpr) rightVal;
            if (!(newArrayExpr.getSize() instanceof Constant) && source.getAccessPath().getArrayTaintType() != AccessPath.ArrayTaintType.Contents && aliasing.mayAlias(source.getAccessPath().getPlainValue(), leftVal)) {
                AccessPath ap2 = getManager().getAccessPathFactory().createAccessPath(newArrayExpr.getSize(), true);
                newAbs = source.deriveNewAbstraction(ap2, assignStmt);
            }
        } else if (rightVal instanceof ArrayRef) {
            Value rightBase = ((ArrayRef) rightVal).getBase();
            Value rightIndex = ((ArrayRef) rightVal).getIndex();
            if (source.getAccessPath().getArrayTaintType() != AccessPath.ArrayTaintType.Length && aliasing.mayAlias(leftVal, source.getAccessPath().getPlainValue())) {
                if (getManager().getConfig().getImplicitFlowMode().trackArrayAccesses()) {
                    AccessPath ap3 = getManager().getAccessPathFactory().createAccessPath(rightIndex, false);
                    res.add(source.deriveNewAbstraction(ap3, assignStmt));
                }
                Type baseType = source.getAccessPath().getBaseType();
                Type targetType = TypeUtils.buildArrayOrAddDimension(baseType, baseType.getArrayType());
                AccessPath ap4 = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), rightBase, targetType, false, true, AccessPath.ArrayTaintType.Contents);
                newAbs = source.deriveNewAbstraction(ap4, assignStmt);
            }
        }
        if (newAbs == null) {
            return null;
        }
        killSource.value = !(leftVal instanceof ArrayRef);
        res.add(newAbs);
        if (aliasing.canHaveAliases(assignStmt, leftVal, newAbs)) {
            aliasing.computeAliases(d1, assignStmt, leftVal, res, this.manager.getICFG().getMethodOf(assignStmt), newAbs);
        }
        return res;
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateCallFlow(Abstraction d1, Abstraction source, Stmt stmt, SootMethod dest, ByReferenceBoolean killAll) {
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
