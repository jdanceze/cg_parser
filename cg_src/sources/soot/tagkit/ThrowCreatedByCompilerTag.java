package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/ThrowCreatedByCompilerTag.class */
public class ThrowCreatedByCompilerTag implements Tag {
    public static final String NAME = "ThrowCreatedByCompilerTag";

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("ThrowCreatedByCompilerTag has no value for bytecode");
    }
}
