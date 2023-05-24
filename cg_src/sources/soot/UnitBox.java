package soot;

import java.io.Serializable;
/* loaded from: gencallgraphv3.jar:soot/UnitBox.class */
public interface UnitBox extends Serializable {
    void setUnit(Unit unit);

    Unit getUnit();

    boolean canContainUnit(Unit unit);

    boolean isBranchTarget();

    void toString(UnitPrinter unitPrinter);
}
