package soot.tagkit;
/* loaded from: gencallgraphv3.jar:soot/tagkit/AnnotationDefaultTag.class */
public class AnnotationDefaultTag implements Tag {
    public static final String NAME = "AnnotationDefaultTag";
    private final AnnotationElem defaultVal;

    public AnnotationDefaultTag(AnnotationElem def) {
        this.defaultVal = def;
    }

    public String toString() {
        return "Annotation Default: " + this.defaultVal;
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "AnnotationDefault";
    }

    public AnnotationElem getDefaultVal() {
        return this.defaultVal;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("AnnotationDefaultTag has no value for bytecode");
    }
}
