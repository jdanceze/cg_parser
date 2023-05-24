package soot.jimple;

import soot.IntType;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/IntConstant.class */
public class IntConstant extends ArithmeticConstant {
    private static final long serialVersionUID = 8622167089453261784L;
    public final int value;
    private static final int MAX_CACHE = 128;
    private static final int MIN_CACHE = -127;
    private static final int ABS_MIN_CACHE = Math.abs((int) MIN_CACHE);
    private static final IntConstant[] CACHED = new IntConstant[128 + ABS_MIN_CACHE];

    /* JADX INFO: Access modifiers changed from: protected */
    public IntConstant(int value) {
        this.value = value;
    }

    public static IntConstant v(int value) {
        if (value > MIN_CACHE && value < 128) {
            int idx = value + ABS_MIN_CACHE;
            IntConstant c = CACHED[idx];
            if (c != null) {
                return c;
            }
            IntConstant c2 = new IntConstant(value);
            CACHED[idx] = c2;
            return c2;
        }
        return new IntConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof IntConstant) && ((IntConstant) c).value == this.value;
    }

    public int hashCode() {
        return this.value;
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant add(NumericConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value + ((IntConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant subtract(NumericConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value - ((IntConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant multiply(NumericConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value * ((IntConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant divide(NumericConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value / ((IntConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant remainder(NumericConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value % ((IntConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant equalEqual(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value == ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant notEqual(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value != ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public boolean isLessThan(NumericConstant c) {
        if (c instanceof IntConstant) {
            return this.value < ((IntConstant) c).value;
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThan(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value < ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThanOrEqual(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value <= ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThan(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value > ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThanOrEqual(NumericConstant c) {
        if (c instanceof IntConstant) {
            return v(this.value >= ((IntConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("IntConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant negate() {
        return v(-this.value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant and(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value & ((IntConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant or(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value | ((IntConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant xor(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value ^ ((IntConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant shiftLeft(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value << ((IntConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant shiftRight(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value >> ((IntConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant unsignedShiftRight(ArithmeticConstant c) {
        if (!(c instanceof IntConstant)) {
            throw new IllegalArgumentException("IntConstant expected");
        }
        return v(this.value >>> ((IntConstant) c).value);
    }

    public String toString() {
        return Integer.toString(this.value);
    }

    @Override // soot.Value
    public Type getType() {
        return IntType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseIntConstant(this);
    }
}
