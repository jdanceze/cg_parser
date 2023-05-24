package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SyntheticTag.class */
public class SyntheticTag implements Tag {
    public static final String NAME = "SyntheticTag";

    public String toString() {
        return "Synthetic";
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "Synthetic";
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("SyntheticTag has no value for bytecode");
    }
}
