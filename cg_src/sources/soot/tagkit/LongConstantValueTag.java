package soot.tagkit;

import soot.jimple.LongConstant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/LongConstantValueTag.class */
public class LongConstantValueTag extends ConstantValueTag {
    public static final String NAME = "LongConstantValueTag";
    private final long value;

    public LongConstantValueTag(long value) {
        super(new byte[]{(byte) ((value >> 56) & 255), (byte) ((value >> 48) & 255), (byte) ((value >> 40) & 255), (byte) ((value >> 32) & 255), (byte) ((value >> 24) & 255), (byte) ((value >> 16) & 255), (byte) ((value >> 8) & 255), (byte) (value & 255)});
        this.value = value;
    }

    public long getLongValue() {
        return this.value;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.ConstantValueTag
    public String toString() {
        return "ConstantValue: " + Long.toString(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public LongConstant getConstant() {
        return LongConstant.v(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + ((int) (this.value ^ (this.value >>> 32)));
    }

    @Override // soot.tagkit.ConstantValueTag
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        LongConstantValueTag other = (LongConstantValueTag) obj;
        return this.value == other.value;
    }
}
