package soot.tagkit;

import soot.jimple.IntConstant;
/* loaded from: gencallgraphv3.jar:soot/tagkit/IntegerConstantValueTag.class */
public class IntegerConstantValueTag extends ConstantValueTag {
    public static final String NAME = "IntegerConstantValueTag";
    private final int value;

    public IntegerConstantValueTag(int value) {
        super(new byte[]{(byte) ((value >> 24) & 255), (byte) ((value >> 16) & 255), (byte) ((value >> 8) & 255), (byte) (value & 255)});
        this.value = value;
    }

    public int getIntValue() {
        return this.value;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.ConstantValueTag
    public String toString() {
        return "ConstantValue: " + Integer.toString(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public IntConstant getConstant() {
        return IntConstant.v(this.value);
    }

    @Override // soot.tagkit.ConstantValueTag
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + this.value;
    }

    @Override // soot.tagkit.ConstantValueTag
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        IntegerConstantValueTag other = (IntegerConstantValueTag) obj;
        return this.value == other.value;
    }
}
