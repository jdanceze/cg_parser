package soot.tagkit;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/tagkit/VisibilityLocalVariableAnnotationTag.class */
public class VisibilityLocalVariableAnnotationTag extends VisibilityParameterAnnotationTag {
    public static final String NAME = "VisibilityLocalVariableAnnotationTag";

    public VisibilityLocalVariableAnnotationTag(int num, int kind) {
        super(num, kind);
    }

    @Override // soot.tagkit.VisibilityParameterAnnotationTag
    public String toString() {
        int num_var = getVisibilityAnnotations() != null ? getVisibilityAnnotations().size() : 0;
        StringBuilder sb = new StringBuilder("Visibility LocalVariable Annotation: num Annotation: ");
        sb.append(num_var).append(" kind: ").append(getKind());
        if (num_var > 0) {
            Iterator<VisibilityAnnotationTag> it = getVisibilityAnnotations().iterator();
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

    @Override // soot.tagkit.VisibilityParameterAnnotationTag, soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.VisibilityParameterAnnotationTag
    public String getInfo() {
        return "VisibilityLocalVariableAnnotation";
    }

    @Override // soot.tagkit.VisibilityParameterAnnotationTag, soot.tagkit.Tag
    public byte[] getValue() {
        throw new RuntimeException("VisibilityLocalVariableAnnotationTag has no value for bytecode");
    }
}
