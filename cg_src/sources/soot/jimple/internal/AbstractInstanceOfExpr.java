package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.BooleanType;
import soot.Type;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.coffi.Instruction;
import soot.jimple.ExprSwitch;
import soot.jimple.InstanceOfExpr;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractInstanceOfExpr.class */
public abstract class AbstractInstanceOfExpr implements InstanceOfExpr {
    protected final ValueBox opBox;
    protected Type checkType;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractInstanceOfExpr(ValueBox opBox, Type checkType) {
        this.opBox = opBox;
        this.checkType = checkType;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractInstanceOfExpr) {
            AbstractInstanceOfExpr aie = (AbstractInstanceOfExpr) o;
            return this.opBox.getValue().equivTo(aie.opBox.getValue()) && this.checkType.equals(aie.checkType);
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.opBox.getValue().equivHashCode() * 101) + (this.checkType.hashCode() * 17);
    }

    public String toString() {
        return String.valueOf(this.opBox.getValue().toString()) + Instruction.argsep + Jimple.INSTANCEOF + Instruction.argsep + this.checkType.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        this.opBox.toString(up);
        up.literal(" instanceof ");
        up.type(this.checkType);
    }

    @Override // soot.jimple.InstanceOfExpr
    public Value getOp() {
        return this.opBox.getValue();
    }

    @Override // soot.jimple.InstanceOfExpr
    public void setOp(Value op) {
        this.opBox.setValue(op);
    }

    @Override // soot.jimple.InstanceOfExpr
    public ValueBox getOpBox() {
        return this.opBox;
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.opBox.getValue().getUseBoxes());
        list.add(this.opBox);
        return list;
    }

    @Override // soot.Value
    public Type getType() {
        return BooleanType.v();
    }

    @Override // soot.jimple.InstanceOfExpr
    public Type getCheckType() {
        return this.checkType;
    }

    @Override // soot.jimple.InstanceOfExpr
    public void setCheckType(Type checkType) {
        this.checkType = checkType;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseInstanceOfExpr(this);
    }
}
