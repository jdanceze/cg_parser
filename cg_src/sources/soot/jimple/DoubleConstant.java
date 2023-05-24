package soot.jimple;

import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import soot.DoubleType;
import soot.Type;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/jimple/DoubleConstant.class */
public class DoubleConstant extends RealConstant {
    private static final long serialVersionUID = -6890604195758898783L;
    public final double value;
    public static final DoubleConstant ZERO = new DoubleConstant(Const.default_value_double);
    public static final DoubleConstant ONE = new DoubleConstant(1.0d);

    private DoubleConstant(double value) {
        this.value = value;
    }

    public static DoubleConstant v(double value) {
        if (Double.compare(value, Const.default_value_double) == 0) {
            return ZERO;
        }
        if (Double.compare(value, 1.0d) == 0) {
            return ONE;
        }
        return new DoubleConstant(value);
    }

    public boolean equals(Object c) {
        return (c instanceof DoubleConstant) && Double.compare(((DoubleConstant) c).value, this.value) == 0;
    }

    public int hashCode() {
        long v = Double.doubleToLongBits(this.value);
        return (int) (v ^ (v >>> 32));
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant add(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value + ((DoubleConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant subtract(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value - ((DoubleConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant multiply(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value * ((DoubleConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant divide(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value / ((DoubleConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant remainder(NumericConstant c) {
        assertInstanceOf(c);
        return v(this.value % ((DoubleConstant) c).value);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant equalEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) == 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant notEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) != 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public boolean isLessThan(NumericConstant c) {
        assertInstanceOf(c);
        return Double.compare(this.value, ((DoubleConstant) c).value) < 0;
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThan(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) < 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant lessThanOrEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) <= 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThan(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) > 0 ? 1 : 0);
    }

    @Override // soot.jimple.NumericConstant
    public NumericConstant greaterThanOrEqual(NumericConstant c) {
        assertInstanceOf(c);
        return IntConstant.v(Double.compare(this.value, ((DoubleConstant) c).value) >= 0 ? 1 : 0);
    }

    @Override // soot.jimple.RealConstant
    public IntConstant cmpg(RealConstant constant) {
        assertInstanceOf(constant);
        double cValue = ((DoubleConstant) constant).value;
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
        double cValue = ((DoubleConstant) constant).value;
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
    /* JADX WARN: Code restructure failed: missing block: B:14:0x0064, code lost:
        return "#" + r0;
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
            double r0 = r0.value
            java.lang.String r0 = java.lang.Double.toString(r0)
            r5 = r0
            r0 = r5
            r1 = r0
            r6 = r1
            int r0 = r0.hashCode()
            switch(r0) {
                case 78043: goto L30;
                case 237817416: goto L3c;
                case 506745205: goto L48;
                default: goto L65;
            }
        L30:
            r0 = r6
            java.lang.String r1 = "NaN"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L65
        L3c:
            r0 = r6
            java.lang.String r1 = "Infinity"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L65
        L48:
            r0 = r6
            java.lang.String r1 = "-Infinity"
            boolean r0 = r0.equals(r1)
            if (r0 != 0) goto L54
            goto L65
        L54:
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r1 = r0
            java.lang.String r2 = "#"
            r1.<init>(r2)
            r1 = r5
            java.lang.StringBuilder r0 = r0.append(r1)
            java.lang.String r0 = r0.toString()
            return r0
        L65:
            r0 = r5
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.jimple.DoubleConstant.toString():java.lang.String");
    }

    @Override // soot.Value
    public Type getType() {
        return DoubleType.v();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        ((ConstantSwitch) sw).caseDoubleConstant(this);
    }

    private void assertInstanceOf(NumericConstant constant) {
        if (!(constant instanceof DoubleConstant)) {
            throw new IllegalArgumentException("DoubleConstant expected");
        }
    }
}
