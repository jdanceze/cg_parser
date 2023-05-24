package org.objectweb.asm.util;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.TypePath;
/* loaded from: gencallgraphv3.jar:asm-util-9.4.jar:org/objectweb/asm/util/TraceMethodVisitor.class */
public final class TraceMethodVisitor extends MethodVisitor {
    public final Printer p;

    public TraceMethodVisitor(Printer printer) {
        this(null, printer);
    }

    public TraceMethodVisitor(MethodVisitor methodVisitor, Printer printer) {
        super(Opcodes.ASM9, methodVisitor);
        this.p = printer;
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitParameter(String name, int access) {
        this.p.visitParameter(name, access);
        super.visitParameter(name, access);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitMethodAnnotation(descriptor, visible);
        return new TraceAnnotationVisitor(super.visitAnnotation(descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTypeAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitMethodTypeAnnotation(typeRef, typePath, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitTypeAnnotation(typeRef, typePath, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitAttribute(Attribute attribute) {
        this.p.visitMethodAttribute(attribute);
        super.visitAttribute(attribute);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitAnnotationDefault() {
        Printer annotationPrinter = this.p.visitAnnotationDefault();
        return new TraceAnnotationVisitor(super.visitAnnotationDefault(), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitAnnotableParameterCount(int parameterCount, boolean visible) {
        this.p.visitAnnotableParameterCount(parameterCount, visible);
        super.visitAnnotableParameterCount(parameterCount, visible);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitParameterAnnotation(int parameter, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitParameterAnnotation(parameter, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitParameterAnnotation(parameter, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitCode() {
        this.p.visitCode();
        super.visitCode();
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFrame(int type, int numLocal, Object[] local, int numStack, Object[] stack) {
        this.p.visitFrame(type, numLocal, local, numStack, stack);
        super.visitFrame(type, numLocal, local, numStack, stack);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInsn(int opcode) {
        this.p.visitInsn(opcode);
        super.visitInsn(opcode);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIntInsn(int opcode, int operand) {
        this.p.visitIntInsn(opcode, operand);
        super.visitIntInsn(opcode, operand);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitVarInsn(int opcode, int varIndex) {
        this.p.visitVarInsn(opcode, varIndex);
        super.visitVarInsn(opcode, varIndex);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTypeInsn(int opcode, String type) {
        this.p.visitTypeInsn(opcode, type);
        super.visitTypeInsn(opcode, type);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        this.p.visitFieldInsn(opcode, owner, name, descriptor);
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        if (this.p.api < 327680) {
            if (isInterface != (opcode == 185)) {
                throw new IllegalArgumentException("INVOKESPECIAL/STATIC on interfaces require ASM5");
            }
            this.p.visitMethodInsn(opcode, owner, name, descriptor);
        } else {
            this.p.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
        if (this.mv != null) {
            this.mv.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
        }
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitInvokeDynamicInsn(String name, String descriptor, Handle bootstrapMethodHandle, Object... bootstrapMethodArguments) {
        this.p.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
        super.visitInvokeDynamicInsn(name, descriptor, bootstrapMethodHandle, bootstrapMethodArguments);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitJumpInsn(int opcode, Label label) {
        this.p.visitJumpInsn(opcode, label);
        super.visitJumpInsn(opcode, label);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLabel(Label label) {
        this.p.visitLabel(label);
        super.visitLabel(label);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLdcInsn(Object value) {
        this.p.visitLdcInsn(value);
        super.visitLdcInsn(value);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitIincInsn(int varIndex, int increment) {
        this.p.visitIincInsn(varIndex, increment);
        super.visitIincInsn(varIndex, increment);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        this.p.visitTableSwitchInsn(min, max, dflt, labels);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        this.p.visitLookupSwitchInsn(dflt, keys, labels);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMultiANewArrayInsn(String descriptor, int numDimensions) {
        this.p.visitMultiANewArrayInsn(descriptor, numDimensions);
        super.visitMultiANewArrayInsn(descriptor, numDimensions);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitInsnAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitInsnAnnotation(typeRef, typePath, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitInsnAnnotation(typeRef, typePath, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitTryCatchBlock(Label start, Label end, Label handler, String type) {
        this.p.visitTryCatchBlock(start, end, handler, type);
        super.visitTryCatchBlock(start, end, handler, type);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitTryCatchAnnotation(int typeRef, TypePath typePath, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitTryCatchAnnotation(typeRef, typePath, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLocalVariable(String name, String descriptor, String signature, Label start, Label end, int index) {
        this.p.visitLocalVariable(name, descriptor, signature, start, end, index);
        super.visitLocalVariable(name, descriptor, signature, start, end, index);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public AnnotationVisitor visitLocalVariableAnnotation(int typeRef, TypePath typePath, Label[] start, Label[] end, int[] index, String descriptor, boolean visible) {
        Printer annotationPrinter = this.p.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible);
        return new TraceAnnotationVisitor(super.visitLocalVariableAnnotation(typeRef, typePath, start, end, index, descriptor, visible), annotationPrinter);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitLineNumber(int line, Label start) {
        this.p.visitLineNumber(line, start);
        super.visitLineNumber(line, start);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitMaxs(int maxStack, int maxLocals) {
        this.p.visitMaxs(maxStack, maxLocals);
        super.visitMaxs(maxStack, maxLocals);
    }

    @Override // org.objectweb.asm.MethodVisitor
    public void visitEnd() {
        this.p.visitMethodEnd();
        super.visitEnd();
    }
}
