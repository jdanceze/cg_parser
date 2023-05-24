package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/BytecodeOffsetTag.class */
public class BytecodeOffsetTag implements Tag {
    public static final String NAME = "BytecodeOffsetTag";
    protected final int offset;

    public BytecodeOffsetTag(int offset) {
        this.offset = offset;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] v = {(byte) ((this.offset >> 24) % 256), (byte) ((this.offset >> 16) % 256), (byte) ((this.offset >> 8) % 256), (byte) (this.offset % 256)};
        return v;
    }

    public int getBytecodeOffset() {
        return this.offset;
    }

    public String toString() {
        return Integer.toString(this.offset);
    }
}
