package soot.tagkit;

import soot.jimple.FloatConstant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/FloatConstantValueTag.class */
public class FloatConstantValueTag extends ConstantValueTag {
    public static final String NAME = "FloatConstantValueTag";
    private final float value;

    public FloatConstantValueTag(float value) {
        super(null);
        this.value = value;
    }

    public float getFloatValue() {
        return this.value;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.ConstantValueTag
    public String toString() {
        return "ConstantValue: " + Float.toString(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public FloatConstant getConstant() {
        return FloatConstant.v(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + Float.floatToIntBits(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        FloatConstantValueTag other = (FloatConstantValueTag) obj;
        return Float.floatToIntBits(this.value) == Float.floatToIntBits(other.value);
    }
}
