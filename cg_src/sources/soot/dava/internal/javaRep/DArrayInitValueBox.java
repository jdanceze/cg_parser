package soot.dava.internal.javaRep;

import soot.AbstractValueBox;
import soot.Value;
/* loaded from: gencallgraphv3.jar:soot/dava/internal/javaRep/DArrayInitValueBox.class */
public class DArrayInitValueBox extends AbstractValueBox {
    public DArrayInitValueBox(Value value) {
        setValue(value);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return value instanceof DArrayInitExpr;
    }
}
