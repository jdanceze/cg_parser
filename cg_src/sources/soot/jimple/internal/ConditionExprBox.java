package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.ConditionExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/ConditionExprBox.class */
public class ConditionExprBox extends AbstractValueBox {
    public ConditionExprBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof ConditionExpr;
    }
}
