package org.objectweb.asm.tree;

import java.util.Map;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
/* loaded from: gencallgraphv3.jar:asm-tree-9.4.jar:org/objectweb/asm/tree/LabelNode.class */
public class LabelNode extends AbstractInsnNode {
    private Label value;

    public LabelNode() {
        super(-1);
    }

    public LabelNode(Label label) {
        super(-1);
        this.value = label;
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public int getType() {
        return 8;
    }

    public Label getLabel() {
        if (this.value == null) {
            this.value = new Label();
        }
        return this.value;
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public void accept(MethodVisitor methodVisitor) {
        methodVisitor.visitLabel(getLabel());
    }

    @Override // org.objectweb.asm.tree.AbstractInsnNode
    public AbstractInsnNode clone(Map<LabelNode, LabelNode> clonedLabels) {
        return clonedLabels.get(this);
    }

    public void resetLabel() {
        this.value = null;
    }
}
