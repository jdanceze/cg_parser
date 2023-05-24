package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/LineNumberTag.class */
public class LineNumberTag implements Tag {
    public static final String NAME = "LineNumberTag";
    protected int line_number;

    public LineNumberTag(int ln) {
        this.line_number = ln;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] v = {(byte) (this.line_number / 256), (byte) (this.line_number % 256)};
        return v;
    }

    public int getLineNumber() {
        return this.line_number;
    }

    public void setLineNumber(int value) {
        this.line_number = value;
    }

    public String toString() {
        return String.valueOf(this.line_number);
    }
}
