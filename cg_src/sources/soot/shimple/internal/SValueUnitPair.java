package soot.shimple.internal;

import soot.Unit;
import soot.Value;
import soot.toolkits.scalar.ValueUnitPair;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/SValueUnitPair.class */
public class SValueUnitPair extends ValueUnitPair implements SUnitBox {
    protected boolean unitChanged;

    public SValueUnitPair(Value value, Unit unit) {
        super(value, unit);
        setUnitChanged(true);
    }

    @Override // soot.toolkits.scalar.ValueUnitPair, soot.UnitBox
    public boolean isBranchTarget() {
        return false;
    }

    @Override // soot.toolkits.scalar.ValueUnitPair, soot.UnitBox
    public void setUnit(Unit u) {
        super.setUnit(u);
        setUnitChanged(true);
    }

    @Override // soot.shimple.internal.SUnitBox
    public boolean isUnitChanged() {
        return this.unitChanged;
    }

    @Override // soot.shimple.internal.SUnitBox
    public void setUnitChanged(boolean unitChanged) {
        this.unitChanged = unitChanged;
    }
}
