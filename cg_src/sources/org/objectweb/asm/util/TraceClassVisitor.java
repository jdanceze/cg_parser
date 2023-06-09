package org.objectweb.asm.util;

import java.io.PrintWriter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.ModuleVisitor;
import org.objectweb.asm.RecordComponentVisitor;
import org.objectweb.asm.TypePath;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceClassVisitor.class */
public final class TraceClassVisitor extends ClassVisitor {
    private final PrintWriter printWriter;
    public final Printer p;

    public TraceClassVisitor(PrintWriter printWriter) {
        this(null, printWriter);
    }

    public TraceClassVisitor(ClassVisitor classVisitor, PrintWriter printWriter) {
        this(classVisitor, new Textifier(), printWriter);
    }

    public TraceClassVisitor(ClassVisitor classVisitor, Printer printer, PrintWriter printWriter) {
        super(17432576, classVisitor);
        this.printWriter = printWriter;
        this.p = printer;
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        this.p.visit(version, access, name, signature, superName, interfaces);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitSource(String file, String debug) {
        this.p.visitSource(file, debug);
        super.visitSource(file, debug);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public ModuleVisitor visitModule(String name, int flags, String version) {
        Printer modulePrinter = this.p.visitModule(name, flags, version);
        return new TraceModuleVisitor(super.visitModule(name, flags, version), modulePrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestHost(String nestHost) {
        this.p.visitNestHost(nestHost);
        super.visitNestHost(nestHost);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitOuterClass(String owner, String name, String descriptor) {
        this.p.visitOuterClass(owner, name, descriptor);
        super.visitOuterClass(owner, name, descriptor);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitClassAnnotation(descriptor, visible);
        return new TraceAnnotationVisitor(super.visitAnnotation(descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitClassTypeAnnotation(typeRef, typePath, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitAttribute(Attribute attribute) {
        this.p.visitClassAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitNestMember(String nestMember) {
        this.p.visitNestMember(nestMember);
        super.visitNestMember(nestMember);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitPermittedSubclass(String permittedSubclass) {
        this.p.visitPermittedSubclass(permittedSubclass);
        super.visitPermittedSubclass(permittedSubclass);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitInnerClass(String name, String outerName, String innerName, int access) {
        this.p.visitInnerClass(name, outerName, innerName, access);
        super.visitInnerClass(name, outerName, innerName, access);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public RecordComponentVisitor visitRecordComponent(String name, String descriptor, String signature) {
        Printer recordComponentPrinter = this.p.visitRecordComponent(name, descriptor, signature);
        return new TraceRecordComponentVisitor(super.visitRecordComponent(name, descriptor, signature), recordComponentPrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
        Printer fieldPrinter = this.p.visitField(access, name, descriptor, signature, value);
        return new TraceFieldVisitor(super.visitField(access, name, descriptor, signature, value), fieldPrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        Printer methodPrinter = this.p.visitMethod(access, name, descriptor, signature, exceptions);
        return new TraceMethodVisitor(super.visitMethod(access, name, descriptor, signature, exceptions), methodPrinter);
    }

    @Override // org.objectweb.asm.ClassVisitor
    public void visitEnd() {
        this.p.visitClassEnd();
        if (this.printWriter != null) {
            this.p.print(this.printWriter);
            this.printWriter.flush();
        }
        super.visitEnd();
    }
}
