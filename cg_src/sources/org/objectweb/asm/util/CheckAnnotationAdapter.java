package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/CheckAnnotationAdapter.class */
public class CheckAnnotationAdapter extends AnnotationVisitor {
    private final boolean useNamedValue;
    private boolean visitEndCalled;

    public CheckAnnotationAdapter(AnnotationVisitor annotationVisitor) {
        this(annotationVisitor, true);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public CheckAnnotationAdapter(AnnotationVisitor annotationVisitor, boolean useNamedValues) {
        super(Opcodes.ASM9, annotationVisitor);
        this.useNamedValue = useNamedValues;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        checkVisitEndNotCalled();
        checkName(name);
        if (!(value instanceof Byte) && !(value instanceof Boolean) && !(value instanceof Character) && !(value instanceof Short) && !(value instanceof Integer) && !(value instanceof Long) && !(value instanceof Float) && !(value instanceof Double) && !(value instanceof String) && !(value instanceof Type) && !(value instanceof byte[]) && !(value instanceof boolean[]) && !(value instanceof char[]) && !(value instanceof short[]) && !(value instanceof int[]) && !(value instanceof long[]) && !(value instanceof float[]) && !(value instanceof double[])) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        if ((value instanceof Type) && ((Type) value).getSort() == 11) {
            throw new IllegalArgumentException("Invalid annotation value");
        }
        super.visit(name, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String descriptor, String value) {
        checkVisitEndNotCalled();
        checkName(name);
        CheckMethodAdapter.checkDescriptor(49, descriptor, false);
        if (value == null) {
            throw new IllegalArgumentException("Invalid enum value");
        }
        super.visitEnum(name, descriptor, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        checkVisitEndNotCalled();
        checkName(name);
        CheckMethodAdapter.checkDescriptor(49, descriptor, false);
        return new CheckAnnotationAdapter(super.visitAnnotation(name, descriptor));
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        checkVisitEndNotCalled();
        checkName(name);
        return new CheckAnnotationAdapter(super.visitArray(name), false);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        checkVisitEndNotCalled();
        this.visitEndCalled = true;
        super.visitEnd();
    }

    private void checkName(String name) {
        if (this.useNamedValue && name == null) {
            throw new IllegalArgumentException("Annotation value name must not be null");
        }
    }

    private void checkVisitEndNotCalled() {
        if (this.visitEndCalled) {
            throw new IllegalStateException("Cannot call a visit method after visitEnd has been called");
        }
    }
}
