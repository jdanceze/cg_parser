package net.bytebuddy.implementation.bytecode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.bytebuddy.build.HashCodeAndEqualsPlugin;
import net.bytebuddy.description.method.MethodDescription;
import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/ByteCodeAppender.class */
public interface ByteCodeAppender {
    Size apply(MethodVisitor methodVisitor, Implementation.Context context, MethodDescription methodDescription);

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/ByteCodeAppender$Size.class */
    public static class Size {
        private final int operandStackSize;
        private final int localVariableSize;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.operandStackSize == ((Size) obj).operandStackSize && this.localVariableSize == ((Size) obj).localVariableSize;
        }

        public int hashCode() {
            return (((17 * 31) + this.operandStackSize) * 31) + this.localVariableSize;
        }

        public Size(int operandStackSize, int localVariableSize) {
            this.operandStackSize = operandStackSize;
            this.localVariableSize = localVariableSize;
        }

        public int getOperandStackSize() {
            return this.operandStackSize;
        }

        public int getLocalVariableSize() {
            return this.localVariableSize;
        }

        public Size merge(Size other) {
            return new Size(Math.max(this.operandStackSize, other.operandStackSize), Math.max(this.localVariableSize, other.localVariableSize));
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/ByteCodeAppender$Compound.class */
    public static class Compound implements ByteCodeAppender {
        private final List<ByteCodeAppender> byteCodeAppenders;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.byteCodeAppenders.equals(((Compound) obj).byteCodeAppenders);
        }

        public int hashCode() {
            return (17 * 31) + this.byteCodeAppenders.hashCode();
        }

        public Compound(ByteCodeAppender... byteCodeAppender) {
            this(Arrays.asList(byteCodeAppender));
        }

        public Compound(List<? extends ByteCodeAppender> byteCodeAppenders) {
            this.byteCodeAppenders = new ArrayList();
            for (ByteCodeAppender byteCodeAppender : byteCodeAppenders) {
                if (byteCodeAppender instanceof Compound) {
                    this.byteCodeAppenders.addAll(((Compound) byteCodeAppender).byteCodeAppenders);
                } else {
                    this.byteCodeAppenders.add(byteCodeAppender);
                }
            }
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            Size size = new Size(0, instrumentedMethod.getStackSize());
            for (ByteCodeAppender byteCodeAppender : this.byteCodeAppenders) {
                size = size.merge(byteCodeAppender.apply(methodVisitor, implementationContext, instrumentedMethod));
            }
            return size;
        }
    }

    @HashCodeAndEqualsPlugin.Enhance
    /* loaded from: gencallgraphv3.jar:byte-buddy-1.10.15.jar:net/bytebuddy/implementation/bytecode/ByteCodeAppender$Simple.class */
    public static class Simple implements ByteCodeAppender {
        private final StackManipulation stackManipulation;

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            return obj != null && getClass() == obj.getClass() && this.stackManipulation.equals(((Simple) obj).stackManipulation);
        }

        public int hashCode() {
            return (17 * 31) + this.stackManipulation.hashCode();
        }

        public Simple(StackManipulation... stackManipulation) {
            this(Arrays.asList(stackManipulation));
        }

        public Simple(List<? extends StackManipulation> stackManipulations) {
            this.stackManipulation = new StackManipulation.Compound(stackManipulations);
        }

        @Override // net.bytebuddy.implementation.bytecode.ByteCodeAppender
        public Size apply(MethodVisitor methodVisitor, Implementation.Context implementationContext, MethodDescription instrumentedMethod) {
            return new Size(this.stackManipulation.apply(methodVisitor, implementationContext).getMaximalSize(), instrumentedMethod.getStackSize());
        }
    }
}
