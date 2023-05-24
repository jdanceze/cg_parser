package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/StringTag.class */
public class StringTag implements Tag {
    public static final String NAME = "StringTag";
    private final String s;
    private final String analysisType;

    public StringTag(String s, String type) {
        this.s = s;
        this.analysisType = type;
    }

    public StringTag(String s) {
        this(s, "Unknown");
    }

    public String toString() {
        return this.s;
    }

    public String getAnalysisType() {
        return this.analysisType;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return this.s;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("StringTag has no value for bytecode");
    }
}
