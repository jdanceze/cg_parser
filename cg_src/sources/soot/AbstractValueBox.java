package soot;

import soot.tagkit.AbstractHost;
/* loaded from: gencallgraphv3.jar:soot/AbstractValueBox.class */
public abstract class AbstractValueBox extends AbstractHost implements ValueBox {
    protected Value value;

    @Override // soot.ValueBox
    public void setValue(Value value) {
        if (value == null) {
            throw new IllegalArgumentException("value may not be null");
        }
        if (canContainValue(value)) {
            this.value = value;
            return;
        }
        throw new RuntimeException("Box " + this + " cannot contain value: " + value + "(" + value.getClass() + ")");
    }

    @Override // soot.ValueBox
    public Value getValue() {
        return this.value;
    }

    @Override // soot.ValueBox
    public void toString(UnitPrinter up) {
        up.startValueBox(this);
        this.value.toString(up);
        up.endValueBox(this);
    }

    public String toString() {
        return String.valueOf(getClass().getSimpleName()) + "(" + this.value + ")";
    }
}
