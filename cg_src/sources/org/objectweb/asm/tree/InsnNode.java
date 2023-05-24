package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:asm-tree-9.4.jar:org/objectweb/asm/tree/InsnNode.class */
public class InsnNode extends AbstractInsnNode {
    public InsnNode(int opcode) {
        super(opcode);
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 0;
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitInsn(this.opcode);
        acceptAnnotations(methodVisitor);
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return new InsnNode(this.opcode).cloneAnnotations(this);
    }
}
