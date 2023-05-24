package soot.grimp.internal;

import soot.Local;
import soot.Value;
import soot.jimple.CastExpr;
import soot.jimple.ClassConstant;
import soot.jimple.ConcreteRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewMultiArrayExpr;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/ObjExprBox.class */
public class ObjExprBox extends ExprBox {
    public ObjExprBox(Value value) {
        super(value);
    }

    @Override // soot.grimp.internal.ExprBox, soot.ValueBox
    public boolean canContainValue(Value value) {
        if ((value instanceof ConcreteRef) || (value instanceof InvokeExpr) || (value instanceof NewArrayExpr) || (value instanceof NewMultiArrayExpr) || (value instanceof Local) || (value instanceof NullConstant) || (value instanceof StringConstant) || (value instanceof ClassConstant)) {
            return true;
        }
        return (value instanceof CastExpr) && canContainValue(((CastExpr) value).getOp());
    }
}
