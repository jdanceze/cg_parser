package soot.jimple;

import java.util.Collections;
import java.util.List;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.Baf;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/StaticFieldRef.class */
public class StaticFieldRef implements FieldRef, ConvertToBaf {
    protected SootFieldRef fieldRef;

    /* JADX INFO: Access modifiers changed from: protected */
    public StaticFieldRef(SootFieldRef fieldRef) {
        if (!fieldRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.fieldRef = fieldRef;
    }

    @Override // soot.Value
    public Object clone() {
        return new StaticFieldRef(this.fieldRef);
    }

    public String toString() {
        return this.fieldRef.getSignature();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.fieldRef(this.fieldRef);
    }

    @Override // soot.jimple.FieldRef
    public SootFieldRef getFieldRef() {
        return this.fieldRef;
    }

    @Override // soot.jimple.FieldRef
    public void setFieldRef(SootFieldRef fieldRef) {
        this.fieldRef = fieldRef;
    }

    @Override // soot.jimple.FieldRef
    public SootField getField() {
        return this.fieldRef.resolve();
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Value
    public Type getType() {
        return this.fieldRef.type();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseStaticFieldRef(this);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof StaticFieldRef) {
            return ((StaticFieldRef) o).getField().equals(getField());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return getField().equivHashCode();
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        Unit u = Baf.v().newStaticGetInst(this.fieldRef);
        u.addAllTagsOf(context.getCurrentUnit());
        out.add(u);
    }
}
