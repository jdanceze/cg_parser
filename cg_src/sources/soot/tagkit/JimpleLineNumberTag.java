package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/JimpleLineNumberTag.class */
public class JimpleLineNumberTag implements Tag {
    public static final String NAME = "JimpleLineNumberTag";
    private final int startLineNumber;
    private final int endLineNumber;

    public JimpleLineNumberTag(int ln) {
        this.startLineNumber = ln;
        this.endLineNumber = ln;
    }

    public JimpleLineNumberTag(int startLn, int endLn) {
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

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        return new byte[2];
    }

    public String toString() {
        return "Jimple Line Tag: " + this.startLineNumber;
    }
}
