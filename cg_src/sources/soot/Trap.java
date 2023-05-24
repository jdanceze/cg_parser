package soot;

import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/Trap.class */
public interface Trap extends UnitBoxOwner {
    Unit getBeginUnit();

    Unit getEndUnit();

    Unit getHandlerUnit();

    UnitBox getBeginUnitBox();

    UnitBox getEndUnitBox();

    UnitBox getHandlerUnitBox();

    @Override // soot.UnitBoxOwner
    List<UnitBox> getUnitBoxes();

    SootClass getException();

    void setBeginUnit(Unit unit);

    void setEndUnit(Unit unit);

    void setHandlerUnit(Unit unit);

    void setException(SootClass sootClass);

    Object clone();
}
