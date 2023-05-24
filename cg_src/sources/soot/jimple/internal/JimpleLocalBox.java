package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JimpleLocalBox.class */
public class JimpleLocalBox extends AbstractValueBox {
    public JimpleLocalBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof JimpleLocal;
    }
}
