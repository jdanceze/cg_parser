package soot.jimple.toolkits.annotation.tags;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/tags/NullCheckTag.class */
public class NullCheckTag implements OneByteCodeTag {
    public static final String NAME = "NullCheckTag";
    private final byte value;

    public NullCheckTag(boolean needCheck) {
        if (needCheck) {
            this.value = (byte) 4;
        } else {
            this.value = (byte) 0;
        }
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[]{this.value};
    }

    public boolean needCheck() {
        return this.value != 0;
    }

    public String toString() {
        return this.value == 0 ? "[not null]" : "[unknown]";
    }
}
