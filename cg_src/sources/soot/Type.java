package soot;

import java.io.Serializable;
import soot.util.Numberable;
import soot.util.Switch;
import soot.util.Switchable;
/* loaded from: gencallgraphv3.jar:soot/Type.class */
public abstract class Type implements Switchable, Serializable, Numberable {
    protected ArrayType arrayType;
    private int number = 0;

    public abstract String toString();

    public Type() {
        Scene.v().getTypeNumberer().add(this);
    }

    public String toQuotedString() {
        return toString();
    }

    @Deprecated
    public String getEscapedName() {
        return toQuotedString();
    }

    public static Type toMachineType(Type t) {
        if (t.equals(ShortType.v()) || t.equals(ByteType.v()) || t.equals(BooleanType.v()) || t.equals(CharType.v())) {
            return IntType.v();
        }
        return t;
    }

    public Type merge(Type other, Scene cm) {
        throw new RuntimeException("illegal type merge: " + this + " and " + other);
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }

    public void setArrayType(ArrayType at) {
        this.arrayType = at;
    }

    public ArrayType getArrayType() {
        return this.arrayType;
    }

    public ArrayType makeArrayType() {
        return ArrayType.v(this, 1);
    }

    public boolean isAllowedInFinalCode() {
        return false;
    }

    @Override // soot.util.Numberable
    public final int getNumber() {
        return this.number;
    }

    @Override // soot.util.Numberable
    public final void setNumber(int number) {
        this.number = number;
    }

    public Type getDefaultFinalType() {
        return this;
    }
}
