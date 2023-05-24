package net.bytebuddy.implementation.bytecode;

import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/Addition.class */
public enum Addition implements StackManipulation {
    INTEGER(96, StackSize.SINGLE),
    LONG(97, StackSize.DOUBLE),
    FLOAT(98, StackSize.SINGLE),
    DOUBLE(99, StackSize.DOUBLE);
    
    private final int opcode;
    private final StackSize stackSize;

    Addition(int opcode, StackSize stackSize) {
        this.opcode = opcode;
        this.stackSize = stackSize;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public boolean isValid() {
        return true;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        methodVisitor.visitInsn(this.opcode);
        return this.stackSize.toDecreasingSize();
    }
}
