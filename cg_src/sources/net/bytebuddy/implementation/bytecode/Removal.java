package net.bytebuddy.implementation.bytecode;

import net.bytebuddy.description.type.TypeDefinition;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/Removal.class */
public enum Removal implements StackManipulation {
    ZERO(StackSize.ZERO, 0) { // from class: net.bytebuddy.implementation.bytecode.Removal.1
        @Override // net.bytebuddy.implementation.bytecode.Removal, net.bytebuddy.implementation.bytecode.StackManipulation
        public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
            return new StackManipulation.Size(0, 0);
        }
    },
    SINGLE(StackSize.SINGLE, 87),
    DOUBLE(StackSize.DOUBLE, 88);
    
    private final StackManipulation.Size size;
    private final int opcode;

    Removal(StackSize stackSize, int opcode) {
        this.size = stackSize.toDecreasingSize();
        this.opcode = opcode;
    }

    public static StackManipulation of(TypeDefinition typeDefinition) {
        switch (typeDefinition.getStackSize()) {
            case SINGLE:
                return SINGLE;
            case DOUBLE:
                return DOUBLE;
            case ZERO:
                return ZERO;
            default:
                throw new AssertionError();
        }
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public boolean isValid() {
        return true;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public StackManipulation.Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext) {
        methodVisitor.visitInsn(this.opcode);
        return this.size;
    }
}
