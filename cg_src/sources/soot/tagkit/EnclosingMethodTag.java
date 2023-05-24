package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/EnclosingMethodTag.class */
public class EnclosingMethodTag implements Tag {
    public static final String NAME = "EnclosingMethodTag";
    private final String enclosingClass;
    private final String enclosingMethod;
    private final String enclosingMethodSig;

    public EnclosingMethodTag(String c, String m, String s) {
        this.enclosingClass = c;
        this.enclosingMethod = m;
        this.enclosingMethodSig = s;
    }

    public String toString() {
        return "Enclosing Class: " + this.enclosingClass + " Enclosing Method: " + this.enclosingMethod + " Sig: " + this.enclosingMethodSig;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "EnclosingMethod";
    }

    public String getEnclosingClass() {
        return this.enclosingClass;
    }

    public String getEnclosingMethod() {
        return this.enclosingMethod;
    }

    public String getEnclosingMethodSig() {
        return this.enclosingMethodSig;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("EnclosingMethodTag has no value for bytecode");
    }
}
