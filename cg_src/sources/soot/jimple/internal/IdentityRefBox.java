package soot.jimple.internal;

import soot.AbstractValueBox;
import soot.Value;
import soot.jimple.IdentityRef;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/IdentityRefBox.class */
public class IdentityRefBox extends AbstractValueBox {
    public IdentityRefBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof IdentityRef;
    }
}
