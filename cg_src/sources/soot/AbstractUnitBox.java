package soot;
/* loaded from: gencallgraphv3.jar:soot/AbstractUnitBox.class */
public abstract class AbstractUnitBox implements UnitBox {
    protected Unit unit;

    @Override // soot.UnitBox
    public boolean isBranchTarget() {
        return true;
    }

    @Override // soot.UnitBox
    public void setUnit(Unit unit) {
        if (!canContainUnit(unit)) {
            throw new RuntimeException("attempting to put invalid unit in UnitBox");
        }
        if (this.unit != null) {
            this.unit.removeBoxPointingToThis(this);
        }
        this.unit = unit;
        if (this.unit != null) {
            this.unit.addBoxPointingToThis(this);
        }
    }

    @Override // soot.UnitBox
    public Unit getUnit() {
        return this.unit;
    }

    @Override // soot.UnitBox
    public void toString(UnitPrinter up) {
        up.startUnitBox(this);
        up.unitRef(this.unit, isBranchTarget());
        up.endUnitBox(this);
    }
}
