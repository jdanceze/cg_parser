package org.objectweb.asm.commons;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
/* loaded from: gencallgraphv3.jar:asm-commons-9.4.jar:org/objectweb/asm/commons/AnnotationRemapper.class */
public class AnnotationRemapper extends AnnotationVisitor {
    protected final String descriptor;
    protected final Remapper remapper;

    @Deprecated
    public AnnotationRemapper(AnnotationVisitor annotationVisitor, Remapper remapper) {
        this((String) null, annotationVisitor, remapper);
    }

    public AnnotationRemapper(String descriptor, AnnotationVisitor annotationVisitor, Remapper remapper) {
        this(Opcodes.ASM9, descriptor, annotationVisitor, remapper);
    }

    @Deprecated
    protected AnnotationRemapper(int api, AnnotationVisitor annotationVisitor, Remapper remapper) {
        this(api, null, annotationVisitor, remapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public AnnotationRemapper(int api, String descriptor, AnnotationVisitor annotationVisitor, Remapper remapper) {
        super(api, annotationVisitor);
        this.descriptor = descriptor;
        this.remapper = remapper;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        super.visit(mapAnnotationAttributeName(name), this.remapper.mapValue(value));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String descriptor, String value) {
        super.visitEnum(mapAnnotationAttributeName(name), this.remapper.mapDesc(descriptor), value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        AnnotationVisitor annotationVisitor = super.visitAnnotation(mapAnnotationAttributeName(name), this.remapper.mapDesc(descriptor));
        if (annotationVisitor == null) {
            return null;
        }
        if (annotationVisitor == this.av) {
            return this;
        }
        return createAnnotationRemapper(descriptor, annotationVisitor);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        AnnotationVisitor annotationVisitor = super.visitArray(mapAnnotationAttributeName(name));
        if (annotationVisitor == null) {
            return null;
        }
        if (annotationVisitor == this.av) {
            return this;
        }
        return createAnnotationRemapper(null, annotationVisitor);
    }

    @Deprecated
    protected AnnotationVisitor createAnnotationRemapper(AnnotationVisitor annotationVisitor) {
        return new AnnotationRemapper(this.api, null, annotationVisitor, this.remapper);
    }

    protected AnnotationVisitor createAnnotationRemapper(String descriptor, AnnotationVisitor annotationVisitor) {
        return new AnnotationRemapper(this.api, descriptor, annotationVisitor, this.remapper).orDeprecatedValue(createAnnotationRemapper(annotationVisitor));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final AnnotationVisitor orDeprecatedValue(AnnotationVisitor deprecatedAnnotationVisitor) {
        if (deprecatedAnnotationVisitor.getClass() == getClass()) {
            AnnotationRemapper deprecatedAnnotationRemapper = (AnnotationRemapper) deprecatedAnnotationVisitor;
            if (deprecatedAnnotationRemapper.api == this.api && deprecatedAnnotationRemapper.av == this.av && deprecatedAnnotationRemapper.remapper == this.remapper) {
                return this;
            }
        }
        return deprecatedAnnotationVisitor;
    }

    private String mapAnnotationAttributeName(String name) {
        if (this.descriptor == null) {
            return name;
        }
        return this.remapper.mapAnnotationAttributeName(this.descriptor, name);
    }
}
