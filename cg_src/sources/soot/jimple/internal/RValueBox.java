package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Immediate;
import soot.Value;
import soot.jimple.ConcreteRef;
import soot.jimple.Expr;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/RValueBox.class */
public class RValueBox extends AbstractValueBox {
    public RValueBox(Value value) {
        setValue(value);
    }

    public boolean canContainValue(Value value) {
        return (value instanceof Immediate) || (value instanceof ConcreteRef) || (value instanceof Expr);
    }
}
