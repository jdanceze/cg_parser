package soot.grimp.internal;

import soot.AbstractValueBox;
import soot.Local;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.Constant;
import soot.jimple.Expr;
/* loaded from: gencallgraphv3.jar:soot/grimp/internal/ExprBox.class */
public class ExprBox extends AbstractValueBox {
    public ExprBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return (value instanceof Local) || (value instanceof Constant) || (value instanceof Expr) || (value instanceof ConcreteRef);
    }
}
