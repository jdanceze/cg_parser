package soot;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/AbstractTrap.class */
public class AbstractTrap implements Trap, Serializable {
    protected transient SootClass exception;
    protected UnitBox beginUnitBox;
    protected UnitBox endUnitBox;
    protected UnitBox handlerUnitBox;
    protected List<UnitBox> unitBoxes;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractTrap(SootClass exception, UnitBox beginUnitBox, UnitBox endUnitBox, UnitBox handlerUnitBox) {
        this.exception = exception;
        this.beginUnitBox = beginUnitBox;
        this.endUnitBox = endUnitBox;
        this.handlerUnitBox = handlerUnitBox;
        this.unitBoxes = Collections.unmodifiableList(Arrays.asList(beginUnitBox, endUnitBox, handlerUnitBox));
    }

    @Override // soot.Trap
    public Unit getBeginUnit() {
        return this.beginUnitBox.getUnit();
    }

    @Override // soot.Trap
    public Unit getEndUnit() {
        return this.endUnitBox.getUnit();
    }

    @Override // soot.Trap
    public Unit getHandlerUnit() {
        return this.handlerUnitBox.getUnit();
    }

    @Override // soot.Trap
    public UnitBox getHandlerUnitBox() {
        return this.handlerUnitBox;
    }

    @Override // soot.Trap
    public UnitBox getBeginUnitBox() {
        return this.beginUnitBox;
    }

    @Override // soot.Trap
    public UnitBox getEndUnitBox() {
        return this.endUnitBox;
    }

    @Override // soot.Trap, soot.UnitBoxOwner
    public List<UnitBox> getUnitBoxes() {
        return this.unitBoxes;
    }

    @Override // soot.UnitBoxOwner
    public void clearUnitBoxes() {
        for (UnitBox box : getUnitBoxes()) {
            box.setUnit(null);
        }
    }

    @Override // soot.Trap
    public SootClass getException() {
        return this.exception;
    }

    @Override // soot.Trap
    public void setBeginUnit(Unit beginUnit) {
        this.beginUnitBox.setUnit(beginUnit);
    }

    @Override // soot.Trap
    public void setEndUnit(Unit endUnit) {
        this.endUnitBox.setUnit(endUnit);
    }

    @Override // soot.Trap
    public void setHandlerUnit(Unit handlerUnit) {
        this.handlerUnitBox.setUnit(handlerUnit);
    }

    @Override // soot.Trap
    public void setException(SootClass exception) {
        this.exception = exception;
    }

    @Override // soot.Trap
    public Object clone() {
        throw new RuntimeException();
    }

    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        this.exception = Scene.v().getSootClass((String) in.readObject());
    }

    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();
        out.writeObject(this.exception.getName());
    }
}
