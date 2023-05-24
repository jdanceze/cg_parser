package soot.tagkit;

import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/tagkit/VisibilityAnnotationTag.class */
public class VisibilityAnnotationTag implements Tag {
    public static final String NAME = "VisibilityAnnotationTag";
    private final int visibility;
    private ArrayList<AnnotationTag> annotations;

    public VisibilityAnnotationTag(int vis) {
        this.visibility = vis;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder("Visibility Annotation: level: ");
        switch (this.visibility) {
            case 0:
                sb.append("RUNTIME (runtime-visible)");
                break;
            case 1:
                sb.append("CLASS (runtime-invisible)");
                break;
            case 2:
                sb.append("SOURCE");
                break;
        }
        sb.append("\n Annotations:");
        if (this.annotations != null) {
            Iterator<AnnotationTag> it = this.annotations.iterator();
            while (it.hasNext()) {
                AnnotationTag tag = it.next();
                sb.append('\n');
                sb.append(tag.toString());
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
        return "VisibilityAnnotation";
    }

    public int getVisibility() {
        return this.visibility;
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("VisibilityAnnotationTag has no value for bytecode");
    }

    public void addAnnotation(AnnotationTag a) {
        if (this.annotations == null) {
            this.annotations = new ArrayList<>();
        }
        this.annotations.add(a);
    }

    public ArrayList<AnnotationTag> getAnnotations() {
        return this.annotations;
    }

    public boolean hasAnnotations() {
        return (this.annotations == null || this.annotations.isEmpty()) ? false : true;
    }
}
