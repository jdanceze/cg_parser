package soot.jimple;

import soot.LongType;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/LongConstant.class */
public class LongConstant extends ArithmeticConstant {
    private static final long serialVersionUID = 1008501511477295944L;
    public final long value;
    public static final LongConstant ZERO = new LongConstant(0);
    public static final LongConstant ONE = new LongConstant(1);

    private LongConstant(long value) {
        this.value = value;
    }

    public static LongConstant v(long value) {
        if (value == 0) {
            return ZERO;
        }
        if (value == 1) {
            return ONE;
        }
        return new LongConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof LongConstant) && ((LongConstant) c).value == this.value;
    }

    public int hashCode() {
        return (int) (this.value ^ (this.value >>> 32));
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant add(NumericConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value + ((LongConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant subtract(NumericConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value - ((LongConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant multiply(NumericConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value * ((LongConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant divide(NumericConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value / ((LongConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant remainder(NumericConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value % ((LongConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant equalEqual(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value == ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant notEqual(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value != ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public boolean isLessThan(NumericConstant c) {
        if (c instanceof LongConstant) {
            return this.value < ((LongConstant) c).value;
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThan(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value < ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThanOrEqual(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value <= ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThan(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value > ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThanOrEqual(NumericConstant c) {
        if (c instanceof LongConstant) {
            return IntConstant.v(this.value >= ((LongConstant) c).value ? 1 : 0);
        }
        throw new IllegalArgumentException("LongConstant expected");
    }

    public IntConstant cmp(LongConstant c) {
        if (this.value > c.value) {
            return IntConstant.v(1);
        }
        if (this.value == c.value) {
            return IntConstant.v(0);
        }
        return IntConstant.v(-1);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant negate() {
        return v(-this.value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant and(ArithmeticConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value & ((LongConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant or(ArithmeticConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value | ((LongConstant) c).value);
    }

    @Override // soot.jimple.ArithmeticConstant
    public ArithmeticConstant xor(ArithmeticConstant c) {
        if (!(c instanceof LongConstant)) {
            throw new IllegalArgumentException("LongConstant expected");
        }
        return v(this.value ^ ((LongConstant) c).value);
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
        return String.valueOf(Long.toString(this.value)) + "L";
    }

    @Override // soot.Value
    public Type getType() {
        return LongType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseLongConstant(this);
    }
}
