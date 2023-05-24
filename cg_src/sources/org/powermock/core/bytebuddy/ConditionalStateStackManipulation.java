package org.powermock.core.bytebuddy;

import net.bytebuddy.implementation.Implementation;
import net.bytebuddy.implementation.bytecode.StackManipulation;
import net.bytebuddy.jar.asm.Label;
import net.bytebuddy.jar.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:powermock-core-2.0.9.jar:org/powermock/core/bytebuddy/ConditionalStateStackManipulation.class */
public class ConditionalStateStackManipulation implements StackManipulation {
    private final StackManipulation condition;
    private final StackManipulation action;
    private final StackManipulation otherwise;
    private final Frame frame;

    public ConditionalStateStackManipulation(StackManipulation condition, StackManipulation action, StackManipulation otherwise, Frame frame) {
        this.condition = condition;
        this.action = action;
        this.otherwise = otherwise;
        this.frame = frame;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public boolean isValid() {
        return true;
    }

    @Override // net.bytebuddy.implementation.bytecode.StackManipulation
    public StackManipulation.Size apply(MethodVisitor mv, Implementation.Context implementationContext) {
        StackManipulation.Size size = new StackManipulation.Size(0, 0);
        Label proceed = new Label();
        Label exit = new Label();
        StackManipulation.Size size2 = size.aggregate(this.condition.apply(mv, implementationContext));
        mv.visitJumpInsn(153, proceed);
        StackManipulation.Size size3 = size2.aggregate(this.action.apply(mv, implementationContext));
        mv.visitJumpInsn(167, exit);
        mv.visitLabel(proceed);
        mv.visitFrame(0, this.frame.localSize(), this.frame.locals(), 0, null);
        StackManipulation.Size size4 = size3.aggregate(this.otherwise.apply(mv, implementationContext));
        mv.visitLabel(exit);
        mv.visitFrame(0, 0, null, 0, null);
        return size4;
    }
}
