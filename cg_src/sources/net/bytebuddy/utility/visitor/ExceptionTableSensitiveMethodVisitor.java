package net.bytebuddy.utility.visitor;

import net.bytebuddy.jar.asm.Handle;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/utility/visitor/ExceptionTableSensitiveMethodVisitor.class */
public abstract class ExceptionTableSensitiveMethodVisitor extends MethodVisitor {
    private boolean trigger;

    protected abstract void onAfterExceptionTable();

    /* JADX INFO: Access modifiers changed from: protected */
    public ExceptionTableSensitiveMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
        this.trigger = true;
    }

    private void considerEndOfExceptionTable() {
        if (this.trigger) {
            this.trigger = false;
            onAfterExceptionTable();
        }
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitFrame(int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
        considerEndOfExceptionTable();
        onVisitFrame(type, localVariableLength, localVariable, stackSize, stack);
    }

    protected void onVisitFrame(int type, int localVariableLength, Object[] localVariable, int stackSize, Object[] stack) {
        super.visitFrame(type, localVariableLength, localVariable, stackSize, stack);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitLabel(Label label) {
        considerEndOfExceptionTable();
        onVisitLabel(label);
    }

    protected void onVisitLabel(Label label) {
        super.visitLabel(label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitIntInsn(int opcode, int operand) {
        considerEndOfExceptionTable();
        onVisitIntInsn(opcode, operand);
    }

    protected void onVisitIntInsn(int opcode, int operand) {
        super.visitIntInsn(opcode, operand);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitVarInsn(int opcode, int offset) {
        considerEndOfExceptionTable();
        onVisitVarInsn(opcode, offset);
    }

    protected void onVisitVarInsn(int opcode, int offset) {
        super.visitVarInsn(opcode, offset);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitTypeInsn(int opcode, String type) {
        considerEndOfExceptionTable();
        onVisitTypeInsn(opcode, type);
    }

    protected void onVisitTypeInsn(int opcode, String type) {
        super.visitTypeInsn(opcode, type);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
        considerEndOfExceptionTable();
        onVisitFieldInsn(opcode, owner, name, descriptor);
    }

    protected void onVisitFieldInsn(int opcode, String owner, String name, String descriptor) {
        super.visitFieldInsn(opcode, owner, name, descriptor);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitMethodInsn(int opcode, String owner, String name, String descriptor) {
        considerEndOfExceptionTable();
        onVisitMethodInsn(opcode, owner, name, descriptor);
    }

    @Deprecated
    protected void onVisitMethodInsn(int opcode, String owner, String name, String descriptor) {
        super.visitMethodInsn(opcode, owner, name, descriptor);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        considerEndOfExceptionTable();
        onVisitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    protected void onVisitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
        super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitInvokeDynamicInsn(String name, String descriptor, Handle handle, Object... argument) {
        considerEndOfExceptionTable();
        onVisitInvokeDynamicInsn(name, descriptor, handle, argument);
    }

    protected void onVisitInvokeDynamicInsn(String name, String descriptor, Handle handle, Object... argument) {
        super.visitInvokeDynamicInsn(name, descriptor, handle, argument);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitJumpInsn(int opcode, Label label) {
        considerEndOfExceptionTable();
        onVisitJumpInsn(opcode, label);
    }

    protected void onVisitJumpInsn(int opcode, Label label) {
        super.visitJumpInsn(opcode, label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitLdcInsn(Object constant) {
        considerEndOfExceptionTable();
        onVisitLdcInsn(constant);
    }

    protected void onVisitLdcInsn(Object constant) {
        super.visitLdcInsn(constant);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitIincInsn(int offset, int increment) {
        considerEndOfExceptionTable();
        onVisitIincInsn(offset, increment);
    }

    protected void onVisitIincInsn(int offset, int increment) {
        super.visitIincInsn(offset, increment);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitTableSwitchInsn(int minimum, int maximum, Label defaultTarget, Label... label) {
        considerEndOfExceptionTable();
        onVisitTableSwitchInsn(minimum, maximum, defaultTarget, label);
    }

    protected void onVisitTableSwitchInsn(int minimum, int maximum, Label defaultTarget, Label... label) {
        super.visitTableSwitchInsn(minimum, maximum, defaultTarget, label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitLookupSwitchInsn(Label dflt, int[] key, Label[] label) {
        considerEndOfExceptionTable();
        onVisitLookupSwitchInsn(dflt, key, label);
    }

    protected void onVisitLookupSwitchInsn(Label defaultTarget, int[] key, Label[] label) {
        super.visitLookupSwitchInsn(defaultTarget, key, label);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitMultiANewArrayInsn(String descriptor, int dimensions) {
        considerEndOfExceptionTable();
        onVisitMultiANewArrayInsn(descriptor, dimensions);
    }

    protected void onVisitMultiANewArrayInsn(String descriptor, int dimensions) {
        super.visitMultiANewArrayInsn(descriptor, dimensions);
    }

    @Override // net.bytebuddy.jar.asm.MethodVisitor
    public final void visitInsn(int opcode) {
        considerEndOfExceptionTable();
        onVisitInsn(opcode);
    }

    protected void onVisitInsn(int opcode) {
        super.visitInsn(opcode);
    }
}
