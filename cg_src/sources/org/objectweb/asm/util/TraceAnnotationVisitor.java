package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceAnnotationVisitor.class */
public final class TraceAnnotationVisitor extends AnnotationVisitor {
    private final Printer printer;

    public TraceAnnotationVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceAnnotationVisitor(AnnotationVisitor annotationVisitor, Printer printer) {
        super(Opcodes.ASM9, annotationVisitor);
        this.printer = printer;
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visit(String name, Object value) {
        this.printer.visit(name, value);
        super.visit(name, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnum(String name, String descriptor, String value) {
        this.printer.visitEnum(name, descriptor, value);
        super.visitEnum(name, descriptor, value);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitAnnotation(String name, String descriptor) {
        Printer annotationPrinter = this.printer.visitAnnotation(name, descriptor);
        return new TraceAnnotationVisitor(super.visitAnnotation(name, descriptor), annotationPrinter);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public AnnotationVisitor visitArray(String name) {
        Printer arrayPrinter = this.printer.visitArray(name);
        return new TraceAnnotationVisitor(super.visitArray(name), arrayPrinter);
    }

    @Override // org.objectweb.asm.AnnotationVisitor
    public void visitEnd() {
        this.printer.visitAnnotationEnd();
        super.visitEnd();
    }
}
