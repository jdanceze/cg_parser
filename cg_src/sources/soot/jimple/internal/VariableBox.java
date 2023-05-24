package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Local;
import soot.Value;
import soot.jimple.ConcreteRef;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/VariableBox.class */
public class VariableBox extends AbstractValueBox {
    public VariableBox(Value value) {
        setValue(value);
    }

    public boolean canContainValue(Value value) {
        return (value instanceof Local) || (value instanceof ConcreteRef);
    }
}
