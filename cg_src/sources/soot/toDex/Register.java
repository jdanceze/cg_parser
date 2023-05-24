package soot.toDex;

import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/toDex/Register.class */
public class Register implements Cloneable {
    public static final int MAX_REG_NUM_UNCONSTRAINED = 65535;
    public static final int MAX_REG_NUM_SHORT = 255;
    public static final int MAX_REG_NUM_BYTE = 15;
    public static final Register EMPTY_REGISTER = new Register(IntType.v(), 0);
    private final Type type;
    private int number;

    private static boolean fitsInto(int regNumber, int maxNumber, boolean isWide) {
        return isWide ? regNumber >= 0 && regNumber < maxNumber : regNumber >= 0 && regNumber <= maxNumber;
    }

    public static boolean fitsUnconstrained(int regNumber, boolean isWide) {
        return fitsInto(regNumber, 65535, isWide);
    }

    public static boolean fitsShort(int regNumber, boolean isWide) {
        return fitsInto(regNumber, 255, isWide);
    }

    public static boolean fitsByte(int regNumber, boolean isWide) {
        return fitsInto(regNumber, 15, isWide);
    }

    public Register(Type type, int number) {
        this.type = type;
        this.number = number;
    }

    public boolean isEmptyReg() {
        return this == EMPTY_REGISTER;
    }

    public boolean isWide() {
        return SootToDexUtils.isWide(this.type);
    }

    public boolean isObject() {
        return SootToDexUtils.isObject(this.type);
    }

    public boolean isFloat() {
        return this.type instanceof FloatType;
    }

    public boolean isDouble() {
        return this.type instanceof DoubleType;
    }

    public Type getType() {
        return this.type;
    }

    public String getTypeString() {
        return this.type.toString();
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        if (isEmptyReg()) {
            return;
        }
        this.number = number;
    }

    private boolean fitsInto(int maxNumber) {
        if (isEmptyReg()) {
            return true;
        }
        return fitsInto(this.number, maxNumber, isWide());
    }

    public boolean fitsUnconstrained() {
        return fitsInto(65535);
    }

    public boolean fitsShort() {
        return fitsInto(255);
    }

    public boolean fitsByte() {
        return fitsInto(15);
    }

    /* renamed from: clone */
    public Register m3017clone() {
        return new Register(this.type, this.number);
    }

    public String toString() {
        if (isEmptyReg()) {
            return "the empty reg";
        }
        return "reg(" + this.number + "):" + this.type.toString();
    }

    public int hashCode() {
        int result = (31 * 1) + this.number;
        return (31 * result) + (this.type == null ? 0 : this.type.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Register other = (Register) obj;
        if (this.number != other.number) {
            return false;
        }
        if (this.type == null) {
            if (other.type != null) {
                return false;
            }
            return true;
        } else if (!this.type.equals(other.type)) {
            return false;
        } else {
            return true;
        }
    }
}
