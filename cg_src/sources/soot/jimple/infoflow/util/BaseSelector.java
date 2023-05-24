package soot.jimple.infoflow.util;

import soot.Value;
import soot.jimple.ArrayRef;
import soot.jimple.BinopExpr;
import soot.jimple.CastExpr;
import soot.jimple.InstanceOfExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.UnopExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/BaseSelector.class */
public class BaseSelector {
    public static Value selectBase(Value val, boolean keepArrayRef) {
        if ((val instanceof ArrayRef) && !keepArrayRef) {
            return ((ArrayRef) val).getBase();
        }
        if (val instanceof CastExpr) {
            return ((CastExpr) val).getOp();
        }
        if (val instanceof NewArrayExpr) {
            return ((NewArrayExpr) val).getSize();
        }
        if (val instanceof InstanceOfExpr) {
            return ((InstanceOfExpr) val).getOp();
        }
        if (val instanceof UnopExpr) {
            return ((UnopExpr) val).getOp();
        }
        return val;
    }

    public static Value[] selectBaseList(Value val, boolean keepArrayRef) {
        if (val instanceof BinopExpr) {
            BinopExpr expr = (BinopExpr) val;
            Value[] set = {expr.getOp1(), expr.getOp2()};
            return set;
        }
        return new Value[]{selectBase(val, keepArrayRef)};
    }
}
