package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceRecordComponentVisitor.class */
public final class TraceRecordComponentVisitor extends RecordComponentVisitor {
    public final Printer printer;

    public TraceRecordComponentVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceRecordComponentVisitor(RecordComponentVisitor recordComponentVisitor, Printer printer) {
        super(Opcodes.ASM9, recordComponentVisitor);
        this.printer = printer;
    }

    @Override // org.objectweb.asm.RecordComponentVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Printer annotationPrinter = this.printer.visitRecordComponentAnnotation(descriptor, visible);
        return new TraceAnnotationVisitor(super.visitAnnotation(descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.RecordComponentVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        Printer annotationPrinter = this.printer.visitRecordComponentTypeAnnotation(typeRef, typePath, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.RecordComponentVisitor
    public void visitAttribute(Attribute attribute) {
        this.printer.visitRecordComponentAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.RecordComponentVisitor
    public void visitEnd() {
        this.printer.visitRecordComponentEnd();
        super.visitEnd();
    }
}
