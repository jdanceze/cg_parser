package soot;

import java.io.Serializable;
import java.util.List;
import soot.tagkit.Host;
import soot.util.Switchable;
/* loaded from: gencallgraphv3.jar:soot/Unit.class */
public interface Unit extends Switchable, Host, Serializable, Context {
    List<ValueBox> getUseBoxes();

    List<ValueBox> getDefBoxes();

    List<UnitBox> getUnitBoxes();

    List<UnitBox> getBoxesPointingToThis();

    void addBoxPointingToThis(UnitBox unitBox);

    void removeBoxPointingToThis(UnitBox unitBox);

    void clearUnitBoxes();

    List<ValueBox> getUseAndDefBoxes();

    Object clone();

    boolean fallsThrough();

    boolean branches();

    void toString(UnitPrinter unitPrinter);

    void redirectJumpsToThisTo(Unit unit);
}
