package soot.dexpler;

import soot.RefLikeType;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.ConditionExpr;
import soot.jimple.EqExpr;
import soot.jimple.IfStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.IntConstant;
import soot.jimple.LongConstant;
import soot.jimple.NeExpr;
import soot.jimple.NullConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/AbstractNullTransformer.class */
public abstract class AbstractNullTransformer extends DexTransformer {
    /* JADX INFO: Access modifiers changed from: protected */
    public boolean isZeroComparison(ConditionExpr expr) {
        if ((expr instanceof EqExpr) || (expr instanceof NeExpr)) {
            if (!(expr.getOp2() instanceof IntConstant) || ((IntConstant) expr.getOp2()).value != 0) {
                if ((expr.getOp2() instanceof LongConstant) && ((LongConstant) expr.getOp2()).value == 0) {
                    return true;
                }
                return false;
            }
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void replaceWithNull(Unit u) {
        if (u instanceof IfStmt) {
            ConditionExpr expr = (ConditionExpr) ((IfStmt) u).getCondition();
            if (isZeroComparison(expr)) {
                expr.setOp2(NullConstant.v());
            }
        } else if (u instanceof AssignStmt) {
            AssignStmt s = (AssignStmt) u;
            Value v = s.getRightOp();
            if (((v instanceof IntConstant) && ((IntConstant) v).value == 0) || ((v instanceof LongConstant) && ((LongConstant) v).value == 0)) {
                if (!(s.getLeftOp() instanceof InstanceFieldRef) || (((InstanceFieldRef) s.getLeftOp()).getFieldRef().type() instanceof RefLikeType)) {
                    s.setRightOp(NullConstant.v());
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isObject(Type t) {
        return t instanceof RefLikeType;
    }
}
