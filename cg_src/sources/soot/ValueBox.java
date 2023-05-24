package soot;

import java.io.Serializable;
import soot.tagkit.Host;
/* loaded from: gencallgraphv3.jar:soot/ValueBox.class */
public interface ValueBox extends Host, Serializable {
    void setValue(Value value);

    Value getValue();

    boolean canContainValue(Value value);

    void toString(UnitPrinter unitPrinter);
}
