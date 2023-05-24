package soot.jimple.internal;

import java.util.ArrayList;
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
import soot.jimple.NewArrayExpr;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractNewArrayExpr.class */
public abstract class AbstractNewArrayExpr implements NewArrayExpr, ConvertToBaf {
    protected Type baseType;
    protected final ValueBox sizeBox;

    @Override // soot.Value
    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractNewArrayExpr(Type type, ValueBox sizeBox) {
        this.baseType = type;
        this.sizeBox = sizeBox;
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof AbstractNewArrayExpr) {
            AbstractNewArrayExpr ae = (AbstractNewArrayExpr) o;
            return this.sizeBox.getValue().equivTo(ae.sizeBox.getValue()) && this.baseType.equals(ae.baseType);
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (this.sizeBox.getValue().equivHashCode() * 101) + (this.baseType.hashCode() * 17);
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("newarray (");
        buf.append(getBaseTypeString()).append(")[").append(this.sizeBox.getValue().toString()).append(']');
        return buf.toString();
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        up.literal("newarray (");
        up.type(this.baseType);
        up.literal(")[");
        this.sizeBox.toString(up);
        up.literal("]");
    }

    private String getBaseTypeString() {
        return this.baseType.toString();
    }

    @Override // soot.jimple.NewArrayExpr
    public Type getBaseType() {
        return this.baseType;
    }

    @Override // soot.jimple.NewArrayExpr
    public void setBaseType(Type type) {
        this.baseType = type;
    }

    @Override // soot.jimple.NewArrayExpr
    public ValueBox getSizeBox() {
        return this.sizeBox;
    }

    @Override // soot.jimple.NewArrayExpr
    public Value getSize() {
        return this.sizeBox.getValue();
    }

    @Override // soot.jimple.NewArrayExpr
    public void setSize(Value size) {
        this.sizeBox.setValue(size);
    }

    @Override // soot.Value
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> useBoxes = new ArrayList<>(this.sizeBox.getValue().getUseBoxes());
        useBoxes.add(this.sizeBox);
        return useBoxes;
    }

    @Override // soot.jimple.NewArrayExpr, soot.Value
    public Type getType() {
        if (this.baseType instanceof ArrayType) {
            ArrayType base = (ArrayType) this.baseType;
            return ArrayType.v(base.baseType, base.numDimensions + 1);
        }
        return ArrayType.v(this.baseType, 1);
    }

    @Override // soot.jimple.NewArrayExpr, soot.util.Switchable
    public void apply(Switch sw) {
        ((ExprSwitch) sw).caseNewArrayExpr(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getSize()).convertToBaf(context, out);
        Unit u = Baf.v().newNewArrayInst(getBaseType());
        out.add(u);
        u.addAllTagsOf(context.getCurrentUnit());
    }
}
