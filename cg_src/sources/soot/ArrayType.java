package soot;

import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/ArrayType.class */
public class ArrayType extends RefLikeType {
    public final Type baseType;
    public final int numDimensions;

    private ArrayType(Type baseType, int numDimensions) {
        if (!(baseType instanceof PrimType) && !(baseType instanceof RefType) && !(baseType instanceof NullType)) {
            throw new RuntimeException("oops, base type must be PrimType or RefType but not '" + baseType + "'");
        }
        if (numDimensions < 1) {
            throw new RuntimeException("attempt to create array with " + numDimensions + " dimensions");
        }
        this.baseType = baseType;
        this.numDimensions = numDimensions;
    }

    public static ArrayType v(Type baseType, int numDimensions) {
        if (numDimensions < 0) {
            throw new RuntimeException("Invalid number of array dimensions: " + numDimensions);
        }
        Type elementType = baseType;
        while (numDimensions > 0) {
            ArrayType ret = elementType.getArrayType();
            if (ret == null) {
                ret = new ArrayType(baseType, (numDimensions - numDimensions) + 1);
                elementType.setArrayType(ret);
            }
            elementType = ret;
            numDimensions--;
        }
        return (ArrayType) elementType;
    }

    public boolean equals(Object t) {
        return t == this;
    }

    public void toString(UnitPrinter up) {
        up.type(this.baseType);
        for (int i = 0; i < this.numDimensions; i++) {
            up.literal("[]");
        }
    }

    @Override // soot.Type
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.baseType.toString());
        for (int i = 0; i < this.numDimensions; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    @Override // soot.Type
    public String toQuotedString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(this.baseType.toQuotedString());
        for (int i = 0; i < this.numDimensions; i++) {
            buffer.append("[]");
        }
        return buffer.toString();
    }

    public int hashCode() {
        return this.baseType.hashCode() + (1127088961 * this.numDimensions);
    }

    @Override // soot.Type, soot.util.Switchable
    public void apply(Switch sw) {
        ((TypeSwitch) sw).caseArrayType(this);
    }

    @Override // soot.RefLikeType
    public Type getArrayElementType() {
        return getElementType();
    }

    public Type getElementType() {
        if (this.numDimensions > 1) {
            return v(this.baseType, this.numDimensions - 1);
        }
        return this.baseType;
    }

    @Override // soot.Type
    public ArrayType makeArrayType() {
        return v(this.baseType, this.numDimensions + 1);
    }

    @Override // soot.Type
    public boolean isAllowedInFinalCode() {
        return true;
    }
}
