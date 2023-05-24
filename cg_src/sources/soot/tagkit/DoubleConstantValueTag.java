package soot.tagkit;

import soot.jimple.DoubleConstant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/DoubleConstantValueTag.class */
public class DoubleConstantValueTag extends ConstantValueTag {
    public static final String NAME = "DoubleConstantValueTag";
    private final double value;

    public DoubleConstantValueTag(double val) {
        super(null);
        this.value = val;
    }

    public double getDoubleValue() {
        return this.value;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.ConstantValueTag
    public String toString() {
        return "ConstantValue: " + Double.toString(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public DoubleConstant getConstant() {
        return DoubleConstant.v(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public int hashCode() {
        int result = super.hashCode();
        long temp = Double.doubleToLongBits(this.value);
        return (31 * result) + ((int) (temp ^ (temp >>> 32)));
    }

    @Override // soot.tagkit.ConstantValueTag
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        DoubleConstantValueTag other = (DoubleConstantValueTag) obj;
        return Double.doubleToLongBits(this.value) == Double.doubleToLongBits(other.value);
    }
}
