package soot.tagkit;

import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/tagkit/VisibilityParameterAnnotationTag.class */
public class VisibilityParameterAnnotationTag implements Tag {
    public static final String NAME = "VisibilityParameterAnnotationTag";
    private final int num_params;
    private final int kind;
    private ArrayList<VisibilityAnnotationTag> visibilityAnnotations;

    public VisibilityParameterAnnotationTag(int num, int kind) {
        this.num_params = num;
        this.kind = kind;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Visibility Param Annotation: num params: ");
        sb.append(this.num_params).append(" kind: ").append(this.kind);
        if (this.visibilityAnnotations != null) {
            Iterator<VisibilityAnnotationTag> it = this.visibilityAnnotations.iterator();
            while (it.hasNext()) {
                VisibilityAnnotationTag tag = it.next();
                sb.append('\n');
                if (tag != null) {
                    sb.append(tag.toString());
                }
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    public String getInfo() {
        return "VisibilityParameterAnnotation";
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("VisibilityParameterAnnotationTag has no value for bytecode");
    }

    public void addVisibilityAnnotation(VisibilityAnnotationTag a) {
        if (this.visibilityAnnotations == null) {
            this.visibilityAnnotations = new ArrayList<>();
        }
        this.visibilityAnnotations.add(a);
    }

    public ArrayList<VisibilityAnnotationTag> getVisibilityAnnotations() {
        return this.visibilityAnnotations;
    }

    public int getKind() {
        return this.kind;
    }

    public int getNumParams() {
        return this.num_params;
    }
}
