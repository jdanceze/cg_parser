package soot.jimple;

import soot.FloatType;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/FloatConstant.class */
public class FloatConstant extends RealConstant {
    private static final long serialVersionUID = 8670501761494749605L;
    public static final FloatConstant ZERO = new FloatConstant(0.0f);
    public static final FloatConstant ONE = new FloatConstant(1.0f);
    public final float value;

    protected FloatConstant(float value) {
        this.value = value;
    }

    public static FloatConstant v(float value) {
        if (Float.compare(value, 0.0f) == 0) {
            return ZERO;
        }
        if (Float.compare(value, 1.0f) == 0) {
            return ONE;
        }
        return new FloatConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof FloatConstant) && Float.compare(((FloatConstant) c).value, this.value) == 0;
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant add(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value + ((FloatConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant subtract(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value - ((FloatConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant multiply(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value * ((FloatConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant divide(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value / ((FloatConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant remainder(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value % ((FloatConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant equalEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) == 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant notEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) != 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public boolean isLessThan(NumericConstant c) {
        assertInstanceOf(c);
        return Float.compare(this.value, ((FloatConstant) c).value) < 0;
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThan(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) < 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThanOrEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) <= 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThan(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) > 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThanOrEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Float.compare(this.value, ((FloatConstant) c).value) >= 0 ? 1 : 0);
    }

    @Override // soot.jimple.RealConstant
    public IntConstant cmpg(RealConstant constant) {
        assertInstanceOf(constant);
        float cValue = ((FloatConstant) constant).value;
        if (this.value < cValue) {
            return IntConstant.v(-1);
        }
        if (this.value == cValue) {
            return IntConstant.v(0);
        }
        return IntConstant.v(1);
    }

    @Override // soot.jimple.RealConstant
    public IntConstant cmpl(RealConstant constant) {
        assertInstanceOf(constant);
        float cValue = ((FloatConstant) constant).value;
        if (this.value > cValue) {
            return IntConstant.v(1);
        }
        if (this.value == cValue) {
            return IntConstant.v(0);
        }
        return IntConstant.v(-1);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant negate() {
        return v(-this.value);
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x004e, code lost:
        if (r0.equals("-Infinity") == false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0069, code lost:
        return "#" + r0 + "F";
     */
    /* JADX WARN: Code restructure failed: missing block: B:5:0x0036, code lost:
        if (r0.equals("NaN") == false) goto L12;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0042, code lost:
        if (r0.equals("Infinity") == false) goto L12;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public java.lang.String toString() {
        /*
            r4 = this;
            r0 = r4
            float r0 = r0.value
            java.lang.String r0 = java.lang.Float.toString(r0)
            r5 = r0
            r0 = r5
            r1 = r0
            r6 = r1
            int r0 = r0.hashCode()
            switch(r0) {
                case 78043: goto L30;
                case 237817416: goto L3c;
                case 506745205: goto L48;
                default: goto L6a;
            }
        L30:
            r0 = r6
            java.lang.String r1 = "NaN"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L6a
        L3c:
            r0 = r6
            java.lang.String r1 = "Infinity"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L6a
        L48:
            r0 = r6
            java.lang.String r1 = "-Infinity"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L6a
        L54:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            java.lang.String r2 = "#"
            r1.<init>(r2)
            r1 = r5
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r1 = "F"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        L6a:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            r2 = r5
            java.lang.String r2 = java.lang.String.valueOf(r2)
            r1.<init>(r2)
            java.lang.String r1 = "F"
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.FloatConstant.toString():java.lang.String");
    }

    @Override // soot.Value
    public Type getType() {
        return FloatType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseFloatConstant(this);
    }

    private void assertInstanceOf(NumericConstant constant) {
        if (!(constant instanceof FloatConstant)) {
            throw new IllegalArgumentException("FloatConstant expected");
        }
    }
}
