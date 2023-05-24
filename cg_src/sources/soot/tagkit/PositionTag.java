package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/PositionTag.class */
public class PositionTag implements Tag {
    public static final String NAME = "PositionTag";
    private final int endOffset;
    private final int startOffset;

    public PositionTag(int start, int end) {
        this.startOffset = start;
        this.endOffset = end;
    }

    public int getEndOffset() {
        return this.endOffset;
    }

    public int getStartOffset() {
        return this.startOffset;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[2];
    }

    public String toString() {
        return "Jimple pos tag: spos: " + this.startOffset + " epos: " + this.endOffset;
    }
}
