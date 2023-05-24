package net.bytebuddy.jar.asm.commons;

import net.bytebuddy.jar.asm.AnnotationVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/jar/asm/commons/AnnotationRemapper.class */
public class AnnotationRemapper extends AnnotationVisitor {
    protected final Remapper remapper;

    public AnnotationRemapper(AnnotationVisitor annotationVisitor, Remapper remapper) {
        this(524288, annotationVisitor, remapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AnnotationRemapper(int api, AnnotationVisitor annotationVisitor, Remapper remapper) {
        super(api, annotationVisitor);
        this.remapper = remapper;
    }

    @Override // net.bytebuddy.jar.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        super.visit(name, this.remapper.mapValue(value));
    }

    @Override // net.bytebuddy.jar.asm.AnnotationVisitor
    public void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(name, this.remapper.mapDesc(descriptor), value);
    }

    @Override // net.bytebuddy.jar.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(name, this.remapper.mapDesc(descriptor));
        if (annotationVisitor == null) {
            return null;
        }
        return annotationVisitor == this.av ? this : createAnnotationRemapper(annotationVisitor);
    }

    @Override // net.bytebuddy.jar.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor annotationVisitor = super.visitArray(name);
        if (annotationVisitor == null) {
            return null;
        }
        return annotationVisitor == this.av ? this : createAnnotationRemapper(annotationVisitor);
    }

    protected AnnotationVisitor createAnnotationRemapper(AnnotationVisitor annotationVisitor) {
        return new AnnotationRemapper(this.api, annotationVisitor, this.remapper);
    }
}
