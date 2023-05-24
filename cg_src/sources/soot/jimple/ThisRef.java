package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/ThisRef.class */
public class ThisRef implements IdentityRef {
    RefType thisType;

    public ThisRef(RefType thisType) {
        this.thisType = thisType;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        return (o instanceof ThisRef) && this.thisType.equals(((ThisRef) o).thisType);
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.thisType.hashCode();
    }

    public String toString() {
        return "@this: " + this.thisType;
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.identityRef(this);
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Type getType() {
        return this.thisType;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseThisRef(this);
    }

    @Override // soot.Value
    public Object clone() {
        return new ThisRef(this.thisType);
    }
}
