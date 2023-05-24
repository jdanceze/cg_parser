package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.InvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/InvokeExprBox.class */
public class InvokeExprBox extends AbstractValueBox {
    public InvokeExprBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof InvokeExpr;
    }
}
