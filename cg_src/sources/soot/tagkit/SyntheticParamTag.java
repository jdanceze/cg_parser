package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SyntheticParamTag.class */
public class SyntheticParamTag implements Tag {
    public static final String NAME = "SyntheticParamTag";

    public String toString() {
        return "SyntheticParam";
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "SyntheticParam";
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("SyntheticParamTag has no value for bytecode");
    }
}
