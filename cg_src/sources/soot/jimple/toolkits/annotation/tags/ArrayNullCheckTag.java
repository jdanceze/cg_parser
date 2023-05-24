package soot.jimple.toolkits.annotation.tags;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/tags/ArrayNullCheckTag.class */
public class ArrayNullCheckTag implements OneByteCodeTag {
    public static final String NAME = "ArrayNullCheckTag";
    private byte value;

    public ArrayNullCheckTag() {
        this.value = (byte) 0;
    }

    public ArrayNullCheckTag(byte v) {
        this.value = v;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[]{this.value};
    }

    public String toString() {
        return Byte.toString(this.value);
    }

    public byte accumulate(byte other) {
        byte oldv = this.value;
        this.value = (byte) (this.value | other);
        return oldv;
    }
}
