package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.ArrayType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ConvertToBaf;
import soot.jimple.ExprSwitch;
import soot.jimple.JimpleToBafContext;
import soot.jimple.NewMultiArrayExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractNewMultiArrayExpr.class */
public abstract class AbstractNewMultiArrayExpr implements NewMultiArrayExpr, ConvertToBaf {
    protected ArrayType baseType;
    protected final ValueBox[] sizeBoxes;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNewMultiArrayExpr(ArrayType type, ValueBox[] sizeBoxes) {
        this.baseType = type;
        this.sizeBoxes = sizeBoxes;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractNewMultiArrayExpr) {
            AbstractNewMultiArrayExpr ae = (AbstractNewMultiArrayExpr) o;
            return this.baseType.equals(ae.baseType) && this.sizeBoxes.length == ae.sizeBoxes.length;
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return this.baseType.hashCode();
    }

    public String toString() {
        ValueBox[] valueBoxArr;
        StringBuilder buf = new StringBuilder("newmultiarray (");
        buf.append(this.baseType.baseType.toString()).append(')');
        for (ValueBox element : this.sizeBoxes) {
            buf.append('[').append(element.getValue().toString()).append(']');
        }
        int e = this.baseType.numDimensions - this.sizeBoxes.length;
        for (int i = 0; i < e; i++) {
            buf.append("[]");
        }
        return buf.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        ValueBox[] valueBoxArr;
        up.literal("newmultiarray (");
        up.type(this.baseType.baseType);
        up.literal(")");
        for (ValueBox element : this.sizeBoxes) {
            up.literal("[");
            element.toString(up);
            up.literal("]");
        }
        int e = this.baseType.numDimensions - this.sizeBoxes.length;
        for (int i = 0; i < e; i++) {
            up.literal("[]");
        }
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public ArrayType getBaseType() {
        return this.baseType;
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public void setBaseType(ArrayType baseType) {
        this.baseType = baseType;
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public ValueBox getSizeBox(int index) {
        return this.sizeBoxes[index];
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public int getSizeCount() {
        return this.sizeBoxes.length;
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public Value getSize(int index) {
        return this.sizeBoxes[index].getValue();
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public List<Value> getSizes() {
        ValueBox[] boxes = this.sizeBoxes;
        List<Value> toReturn = new ArrayList<>(boxes.length);
        for (ValueBox element : boxes) {
            toReturn.add(element.getValue());
        }
        return toReturn;
    }

    @Override // soot.jimple.NewMultiArrayExpr
    public void setSize(int index, Value size) {
        this.sizeBoxes[index].setValue(size);
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        ValueBox[] valueBoxArr;
        List<ValueBox> list = new ArrayList<>();
        Collections.addAll(list, this.sizeBoxes);
        for (ValueBox element : this.sizeBoxes) {
            list.addAll(element.getValue().getUseBoxes());
        }
        return list;
    }

    @Override // soot.jimple.NewMultiArrayExpr, soot.Value
    public Type getType() {
        return this.baseType;
    }

    @Override // soot.jimple.NewMultiArrayExpr, soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseNewMultiArrayExpr(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        List<Value> sizes = getSizes();
        for (Value s : sizes) {
            ((ConvertToBaf) s).convertToBaf(context, out);
        }
        Unit u = Baf.v().newNewMultiArrayInst(getBaseType(), sizes.size());
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
