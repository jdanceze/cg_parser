package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/SignatureTag.class */
public class SignatureTag implements Tag {
    public static final String NAME = "SignatureTag";
    private final String signature;

    public SignatureTag(String signature) {
        this.signature = signature;
    }

    public String getSignature() {
        return this.signature;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("SignatureTag has no value for bytecode");
    }

    public String getInfo() {
        return "Signature";
    }

    public String toString() {
        return "Signature: " + this.signature;
    }
}
