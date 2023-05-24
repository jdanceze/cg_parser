package soot.jimple.internal;

import java.util.ArrayList;
import java.util.List;
import soot.ArrayType;
import soot.Local;
import soot.NullType;
import soot.Type;
import soot.Unit;
import soot.UnitPrinter;
import soot.UnknownType;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.jimple.ArrayRef;
import soot.jimple.ConvertToBaf;
import soot.jimple.Jimple;
import soot.jimple.JimpleToBafContext;
import soot.jimple.RefSwitch;
import soot.tagkit.Tag;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JArrayRef.class */
public class JArrayRef implements ArrayRef, ConvertToBaf {
    protected final ValueBox baseBox;
    protected final ValueBox indexBox;

    public JArrayRef(Value base, Value index) {
        this(Jimple.v().newLocalBox(base), Jimple.v().newImmediateBox(index));
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public JArrayRef(ValueBox baseBox, ValueBox indexBox) {
        this.baseBox = baseBox;
        this.indexBox = indexBox;
    }

    @Override // soot.Value
    public Object clone() {
        return new JArrayRef(Jimple.cloneIfNecessary(getBase()), Jimple.cloneIfNecessary(getIndex()));
    }

    @Override // soot.EquivTo
    public boolean equivTo(Object o) {
        if (o instanceof ArrayRef) {
            ArrayRef oArrayRef = (ArrayRef) o;
            return getBase().equivTo(oArrayRef.getBase()) && getIndex().equivTo(oArrayRef.getIndex());
        }
        return false;
    }

    @Override // soot.EquivTo
    public int equivHashCode() {
        return (getBase().equivHashCode() * 101) + getIndex().equivHashCode() + 17;
    }

    public String toString() {
        return String.valueOf(this.baseBox.getValue().toString()) + "[" + this.indexBox.getValue().toString() + "]";
    }

    @Override // soot.Value
    public void toString(UnitPrinter up) {
        this.baseBox.toString(up);
        up.literal("[");
        this.indexBox.toString(up);
        up.literal("]");
    }

    @Override // soot.jimple.ArrayRef
    public Value getBase() {
        return this.baseBox.getValue();
    }

    @Override // soot.jimple.ArrayRef
    public void setBase(Local base) {
        this.baseBox.setValue(base);
    }

    @Override // soot.jimple.ArrayRef
    public ValueBox getBaseBox() {
        return this.baseBox;
    }

    @Override // soot.jimple.ArrayRef
    public Value getIndex() {
        return this.indexBox.getValue();
    }

    @Override // soot.jimple.ArrayRef
    public void setIndex(Value index) {
        this.indexBox.setValue(index);
    }

    @Override // soot.jimple.ArrayRef
    public ValueBox getIndexBox() {
        return this.indexBox;
    }

    @Override // soot.Value
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> useBoxes = new ArrayList<>();
        useBoxes.addAll(this.baseBox.getValue().getUseBoxes());
        useBoxes.add(this.baseBox);
        useBoxes.addAll(this.indexBox.getValue().getUseBoxes());
        useBoxes.add(this.indexBox);
        return useBoxes;
    }

    @Override // soot.jimple.ArrayRef, soot.Value
    public Type getType() {
        Type type = this.baseBox.getValue().getType();
        if (UnknownType.v().equals(type)) {
            return UnknownType.v();
        }
        if (NullType.v().equals(type)) {
            return NullType.v();
        }
        ArrayType arrayType = type instanceof ArrayType ? (ArrayType) type : type.makeArrayType();
        if (arrayType.numDimensions == 1) {
            return arrayType.baseType;
        }
        return ArrayType.v(arrayType.baseType, arrayType.numDimensions - 1);
    }

    @Override // soot.jimple.ArrayRef, soot.util.Switchable
    public void apply(Switch sw) {
        ((RefSwitch) sw).caseArrayRef(this);
    }

    @Override // soot.jimple.ConvertToBaf
    public void convertToBaf(JimpleToBafContext context, List<Unit> out) {
        ((ConvertToBaf) getBase()).convertToBaf(context, out);
        ((ConvertToBaf) getIndex()).convertToBaf(context, out);
        Unit x = Baf.v().newArrayReadInst(getType());
        out.add(x);
        for (Tag next : context.getCurrentUnit().getTags()) {
            x.addTag(next);
        }
    }
}
