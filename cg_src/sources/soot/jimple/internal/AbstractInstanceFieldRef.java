package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.SootField;
import soot.SootFieldRef;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.grimp.PrecedenceTest;
import soot.jimple.ConvertToBaf;
import soot.jimple.InstanceFieldRef;
import soot.jimple.JimpleToBafContext;
import soot.jimple.RefSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractInstanceFieldRef.class */
public abstract class AbstractInstanceFieldRef implements InstanceFieldRef, ConvertToBaf {
    protected SootFieldRef fieldRef;
    final ValueBox baseBox;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInstanceFieldRef(ValueBox baseBox, SootFieldRef fieldRef) {
        if (fieldRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.baseBox = baseBox;
        this.fieldRef = fieldRef;
    }

    public String toString() {
        return String.valueOf(this.baseBox.getValue().toString()) + "." + this.fieldRef.getSignature();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
            up.literal("(");
        }
        this.baseBox.toString(up);
        if (PrecedenceTest.needsBrackets(this.baseBox, this)) {
            up.literal(")");
        }
        up.literal(".");
        up.fieldRef(this.fieldRef);
    }

    @Override // soot.jimple.InstanceFieldRef
    public Value getBase() {
        return this.baseBox.getValue();
    }

    @Override // soot.jimple.InstanceFieldRef
    public ValueBox getBaseBox() {
        return this.baseBox;
    }

    @Override // soot.jimple.InstanceFieldRef
    public void setBase(Value base) {
        this.baseBox.setValue(base);
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
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> useBoxes = new ArrayList<>();
        useBoxes.addAll(this.baseBox.getValue().getUseBoxes());
        useBoxes.add(this.baseBox);
        return useBoxes;
    }

    @Override // soot.Value
    public Type getType() {
        return this.fieldRef.type();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseInstanceFieldRef(this);
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractInstanceFieldRef) {
            AbstractInstanceFieldRef fr = (AbstractInstanceFieldRef) o;
            return fr.getField().equals(getField()) && fr.baseBox.getValue().equivTo(this.baseBox.getValue());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (getField().equivHashCode() * 101) + this.baseBox.getValue().equivHashCode() + 17;
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getBase()).convertToBaf(context, out);
        Unit u = Baf.v().newFieldGetInst(this.fieldRef);
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
