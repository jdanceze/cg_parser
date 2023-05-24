package soot.jimple.infoflow.problems.rules.forward;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import soot.ArrayType;
import soot.IntType;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.LengthExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.Stmt;
import soot.jimple.infoflow.InfoflowManager;
import soot.jimple.infoflow.data.Abstraction;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.problems.TaintPropagationResults;
import soot.jimple.infoflow.problems.rules.AbstractTaintPropagationRule;
import soot.jimple.infoflow.util.ByReferenceBoolean;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/problems/rules/forward/ArrayPropagationRule.class */
public class ArrayPropagationRule extends AbstractTaintPropagationRule {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ArrayPropagationRule.class.desiredAssertionStatus();
    }

    public ArrayPropagationRule(InfoflowManager manager, Abstraction zeroValue, TaintPropagationResults results) {
        super(manager, zeroValue, results);
    }

    @Override // soot.jimple.infoflow.problems.rules.forward.ITaintPropagationRule
    public Collection<Abstraction> propagateNormalFlow(Abstraction d1, Abstraction source, Stmt stmt, Stmt destStmt, ByReferenceBoolean killSource, ByReferenceBoolean killAll) {
        Type targetType;
        if (!(stmt instanceof AssignStmt)) {
            return null;
        }
        AssignStmt assignStmt = (AssignStmt) stmt;
        Abstraction newAbs = null;
        Value leftVal = assignStmt.getLeftOp();
        Value rightVal = assignStmt.getRightOp();
        if (rightVal instanceof LengthExpr) {
            LengthExpr lengthExpr = (LengthExpr) rightVal;
            if (getAliasing().mayAlias(source.getAccessPath().getPlainValue(), lengthExpr.getOp())) {
                if (source.getAccessPath().getArrayTaintType() == AccessPath.ArrayTaintType.Contents) {
                    return null;
                }
                AccessPath ap = getManager().getAccessPathFactory().createAccessPath(leftVal, IntType.v(), null, true, false, true, AccessPath.ArrayTaintType.ContentsAndLength);
                newAbs = source.deriveNewAbstraction(ap, assignStmt);
            }
        } else if (rightVal instanceof ArrayRef) {
            Value rightBase = ((ArrayRef) rightVal).getBase();
            Value rightIndex = ((ArrayRef) rightVal).getIndex();
            if (source.getAccessPath().getArrayTaintType() != AccessPath.ArrayTaintType.Length && getAliasing().mayAlias(rightBase, source.getAccessPath().getPlainValue())) {
                Type targetType2 = source.getAccessPath().getBaseType();
                if (!$assertionsDisabled && !(targetType2 instanceof ArrayType)) {
                    throw new AssertionError();
                }
                if (targetType2 instanceof ArrayType) {
                    targetType = ((ArrayType) targetType2).getElementType();
                } else {
                    targetType = null;
                }
                AccessPath.ArrayTaintType arrayTaintType = source.getAccessPath().getArrayTaintType();
                AccessPath ap2 = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftVal, targetType, false, true, arrayTaintType);
                newAbs = source.deriveNewAbstraction(ap2, assignStmt);
            } else if (source.getAccessPath().getArrayTaintType() != AccessPath.ArrayTaintType.Length && rightIndex == source.getAccessPath().getPlainValue() && getManager().getConfig().getImplicitFlowMode().trackArrayAccesses()) {
                AccessPath.ArrayTaintType arrayTaintType2 = AccessPath.ArrayTaintType.ContentsAndLength;
                AccessPath ap3 = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftVal, null, false, true, arrayTaintType2);
                newAbs = source.deriveNewAbstraction(ap3, assignStmt);
            }
        } else if ((rightVal instanceof NewArrayExpr) && getManager().getConfig().getEnableArraySizeTainting()) {
            NewArrayExpr newArrayExpr = (NewArrayExpr) rightVal;
            if (getAliasing().mayAlias(source.getAccessPath().getPlainValue(), newArrayExpr.getSize())) {
                AccessPath ap4 = getManager().getAccessPathFactory().copyWithNewValue(source.getAccessPath(), leftVal, null, false, true, AccessPath.ArrayTaintType.Length);
                newAbs = source.deriveNewAbstraction(ap4, assignStmt);
            }
        }
        if (newAbs == null) {
            return null;
        }
        Set<Abstraction> res = new HashSet<>();
        res.add(newAbs);
        if (this.manager.getAliasing().canHaveAliases(assignStmt, leftVal, newAbs)) {
            getAliasing().computeAliases(d1, assignStmt, leftVal, res, getManager().getICFG().getMethodOf(assignStmt), newAbs);
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
