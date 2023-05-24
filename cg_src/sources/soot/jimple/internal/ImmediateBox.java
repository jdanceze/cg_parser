package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Immediate;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/ImmediateBox.class */
public class ImmediateBox extends AbstractValueBox {
    public ImmediateBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof Immediate;
    }
}
