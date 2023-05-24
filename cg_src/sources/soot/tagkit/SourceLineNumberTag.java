package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SourceLineNumberTag.class */
public class SourceLineNumberTag implements Tag {
    public static final String NAME = "SourceLineNumberTag";
    protected int startLineNumber;
    protected int endLineNumber;

    public SourceLineNumberTag(int ln) {
        this.startLineNumber = ln;
        this.endLineNumber = ln;
    }

    public SourceLineNumberTag(int startLn, int endLn) {
        this.startLineNumber = startLn;
        this.endLineNumber = endLn;
    }

    public int getLineNumber() {
        return this.startLineNumber;
    }

    public int getStartLineNumber() {
        return this.startLineNumber;
    }

    public int getEndLineNumber() {
        return this.endLineNumber;
    }

    public void setLineNumber(int value) {
        this.startLineNumber = value;
        this.endLineNumber = value;
    }

    public void setStartLineNumber(int value) {
        this.startLineNumber = value;
    }

    public void setEndLineNumber(int value) {
        this.endLineNumber = value;
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
        return String.valueOf(this.startLineNumber);
    }
}
