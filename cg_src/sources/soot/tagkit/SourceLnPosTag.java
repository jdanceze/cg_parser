package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SourceLnPosTag.class */
public class SourceLnPosTag implements Tag {
    public static final String NAME = "SourceLnPosTag";
    private final int startLn;
    private final int endLn;
    private final int startPos;
    private final int endPos;

    public SourceLnPosTag(int sline, int eline, int spos, int epos) {
        this.startLn = sline;
        this.endLn = eline;
        this.startPos = spos;
        this.endPos = epos;
    }

    public int startLn() {
        return this.startLn;
    }

    public int endLn() {
        return this.endLn;
    }

    public int startPos() {
        return this.startPos;
    }

    public int endPos() {
        return this.endPos;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] v = {(byte) (this.startLn / 256), (byte) (this.startLn % 256)};
        return v;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Source Line Pos Tag: ");
        sb.append("sline: ").append(this.startLn);
        sb.append(" eline: ").append(this.endLn);
        sb.append(" spos: ").append(this.startPos);
        sb.append(" epos: ").append(this.endPos);
        return sb.toString();
    }
}
