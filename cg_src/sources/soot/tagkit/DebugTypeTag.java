package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/DebugTypeTag.class */
public class DebugTypeTag extends SignatureTag {
    public static final String NAME = "DebugTypeTag";

    public DebugTypeTag(String signature) {
        super(signature);
    }

    @Override // soot.tagkit.SignatureTag
    public String toString() {
        return "DebugType: " + getSignature();
    }

    @Override // soot.tagkit.SignatureTag, soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.SignatureTag
    public String getInfo() {
        return "DebugType";
    }

    @Override // soot.tagkit.SignatureTag, soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("DebugTypeTag has no value for bytecode");
    }
}
