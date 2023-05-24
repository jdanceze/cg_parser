package soot.toolkits.scalar;

import soot.AbstractValueBox;
import soot.EquivTo;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/toolkits/scalar/ValueUnitPair.class */
public class ValueUnitPair extends AbstractValueBox implements UnitBox, EquivTo {
    protected Unit unit;

    public ValueUnitPair(Value value, Unit unit) {
        setValue(value);
        setUnit(unit);
    }

    @Override // soot.ValueBox
    public boolean canContainValue(Value value) {
        return true;
    }

    public void setUnit(Unit unit) {
        if (!canContainUnit(unit)) {
            throw new RuntimeException("Cannot put " + unit + " in this box");
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
    public boolean canContainUnit(Unit u) {
        return true;
    }

    public boolean isBranchTarget() {
        return true;
    }

    @Override // soot.AbstractValueBox
    public String toString() {
        return "Value = " + getValue() + ", Unit = " + getUnit();
    }

    @Override // soot.AbstractValueBox, soot.ValueBox
    public void toString(UnitPrinter up) {
        super.toString(up);
        up.literal(isBranchTarget() ? ", " : " #");
        up.startUnitBox(this);
        up.unitRef(this.unit, isBranchTarget());
        up.endUnitBox(this);
    }

    public int hashCode() {
        return super.hashCode();
    }

    public boolean equals(Object other) {
        return super.equals(other);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object other) {
        return (other instanceof ValueUnitPair) && ((ValueUnitPair) other).getValue().equivTo(getValue()) && ((ValueUnitPair) other).getUnit().equals(getUnit());
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (getUnit().hashCode() * 17) + (getValue().equivHashCode() * 101);
    }

    public Object clone() {
        Value cv = Jimple.cloneIfNecessary(getValue());
        Unit cu = getUnit();
        return new ValueUnitPair(cv, cu);
    }
}
