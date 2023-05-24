package soot;

import java.io.Serializable;
import java.util.List;
import soot.util.Switchable;
/* loaded from: gencallgraphv3.jar:soot/Value.class */
public interface Value extends Switchable, EquivTo, Serializable {
    List<ValueBox> getUseBoxes();

    Type getType();

    Object clone();

    void toString(UnitPrinter unitPrinter);
}
