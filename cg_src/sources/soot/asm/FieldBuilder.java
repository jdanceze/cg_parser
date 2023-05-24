package soot.asm;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.FieldVisitor;
import soot.SootField;
/* loaded from: gencallgraphv3.jar:soot/asm/FieldBuilder.class */
final class FieldBuilder extends FieldVisitor {
    private TagBuilder tb;
    private final SootField field;
    private final SootClassBuilder scb;

    /* JADX INFO: Access modifiers changed from: package-private */
    public FieldBuilder(SootField field, SootClassBuilder scb) {
        super(327680);
        this.field = field;
        this.scb = scb;
    }

    private TagBuilder getTagBuilder() {
        TagBuilder t = this.tb;
        if (t == null) {
            TagBuilder tagBuilder = new TagBuilder(this.field, this.scb);
            this.tb = tagBuilder;
            t = tagBuilder;
        }
        return t;
    }

    @Override // org.objectweb.asm.FieldVisitor
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        return getTagBuilder().visitAnnotation(desc, visible);
    }

    @Override // org.objectweb.asm.FieldVisitor
    public void visitAttribute(Attribute attr) {
        getTagBuilder().visitAttribute(attr);
    }
}
