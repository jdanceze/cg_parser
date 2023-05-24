package soot;

import java.util.List;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/EquivalentValue.class */
public class EquivalentValue implements Value {
    private final Value e;

    public EquivalentValue(Value v) {
        this.e = v instanceof EquivalentValue ? ((EquivalentValue) v).e : v;
    }

    public boolean equals(Object o) {
        return this.e.equivTo(o instanceof EquivalentValue ? ((EquivalentValue) o).e : o);
    }

    public boolean equivToValue(Value v) {
        return this.e.equivTo(v);
    }

    public boolean equalsToValue(Value v) {
        return this.e.equals(v);
    }

    @Deprecated
    public Value getDeepestValue() {
        return getValue();
    }

    public int hashCode() {
        return this.e.equivHashCode();
    }

    public String toString() {
        return this.e.toString();
    }

    public Value getValue() {
        return this.e;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return this.e.getUseBoxes();
    }

    @Override // soot.Value
    public Type getType() {
        return this.e.getType();
    }

    @Override // soot.Value
    public Object clone() {
        return new EquivalentValue((Value) this.e.clone());
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return this.e.equivTo(o);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.e.equivHashCode();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        this.e.apply(sw);
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        this.e.toString(up);
    }
}
