package soot.jimple.internal;

import soot.IntType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.jimple.ExprSwitch;
import soot.jimple.LengthExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractLengthExpr.class */
public abstract class AbstractLengthExpr extends AbstractUnopExpr implements LengthExpr {
    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractLengthExpr(ValueBox opBox) {
        super(opBox);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractLengthExpr) {
            return this.opBox.getValue().equivTo(((AbstractLengthExpr) o).opBox.getValue());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.opBox.getValue().equivHashCode();
    }

    public String toString() {
        return "lengthof " + this.opBox.getValue().toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("lengthof ");
        this.opBox.toString(up);
    }

    @Override // soot.Value
    public Type getType() {
        return IntType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseLengthExpr(this);
    }
}
