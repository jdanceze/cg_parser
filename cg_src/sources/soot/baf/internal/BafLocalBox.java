package soot.baf.internal;

import soot.AbstractValueBox;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BafLocalBox.class */
public class BafLocalBox extends AbstractValueBox {
    public BafLocalBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof BafLocal;
    }
}
