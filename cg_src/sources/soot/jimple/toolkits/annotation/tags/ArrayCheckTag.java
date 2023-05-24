package soot.jimple.toolkits.annotation.tags;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/tags/ArrayCheckTag.class */
public class ArrayCheckTag implements OneByteCodeTag {
    public static final String NAME = "ArrayCheckTag";
    private final boolean lowerCheck;
    private final boolean upperCheck;

    public ArrayCheckTag(boolean lower, boolean upper) {
        this.lowerCheck = lower;
        this.upperCheck = upper;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte b = 0;
        if (this.lowerCheck) {
            b = (byte) (0 | 1);
        }
        if (this.upperCheck) {
            b = (byte) (b | 2);
        }
        return new byte[]{b};
    }

    public boolean isCheckUpper() {
        return this.upperCheck;
    }

    public boolean isCheckLower() {
        return this.lowerCheck;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String toString() {
        return String.valueOf(this.lowerCheck ? "[potentially unsafe lower bound]" : "[safe lower bound]") + (this.upperCheck ? "[potentially unsafe upper bound]" : "[safe upper bound]");
    }
}
