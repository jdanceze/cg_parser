package soot.shimple.internal;

import java.util.Collections;
import java.util.List;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.shimple.PiExpr;
import soot.shimple.Shimple;
import soot.toolkits.scalar.ValueUnitPair;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/shimple/internal/SPiExpr.class */
public class SPiExpr implements PiExpr {
    protected ValueUnitPair argBox;
    protected Object targetKey;

    public SPiExpr(Value v, Unit u, Object o) {
        this.argBox = new SValueUnitPair(v, u);
        this.targetKey = o;
    }

    @Override // soot.shimple.PiExpr
    public ValueUnitPair getArgBox() {
        return this.argBox;
    }

    @Override // soot.shimple.PiExpr
    public Value getValue() {
        return this.argBox.getValue();
    }

    @Override // soot.shimple.PiExpr
    public Unit getCondStmt() {
        return this.argBox.getUnit();
    }

    @Override // soot.shimple.PiExpr
    public Object getTargetKey() {
        return this.targetKey;
    }

    @Override // soot.shimple.PiExpr
    public void setValue(Value value) {
        this.argBox.setValue(value);
    }

    @Override // soot.shimple.PiExpr
    public void setCondStmt(Unit pred) {
        this.argBox.setUnit(pred);
    }

    @Override // soot.shimple.PiExpr
    public void setTargetKey(Object targetKey) {
        this.targetKey = targetKey;
    }

    @Override // soot.UnitBoxOwner
    public List<UnitBox> getUnitBoxes() {
        return Collections.singletonList(this.argBox);
    }

    @Override // soot.UnitBoxOwner
    public void clearUnitBoxes() {
        this.argBox.setUnit(null);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof SPiExpr) {
            return getArgBox().equivTo(((SPiExpr) o).getArgBox());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return getArgBox().equivHashCode() * 17;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        throw new RuntimeException("Not Yet Implemented.");
    }

    @Override // soot.Value
    public Object clone() {
        return new SPiExpr(getValue(), getCondStmt(), getTargetKey());
    }

    public String toString() {
        return "Pi(" + getValue() + ")";
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal(Shimple.PI);
        up.literal("(");
        this.argBox.toString(up);
        up.literal(" [");
        up.literal(this.targetKey.toString());
        up.literal("])");
    }

    @Override // soot.Value
    public Type getType() {
        return getValue().getType();
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.singletonList(this.argBox);
    }
}
