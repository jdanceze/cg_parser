package soot.asm;

import java.util.ArrayList;
import java.util.List;
import org.objectweb.asm.tree.AbstractInsnNode;
import soot.Local;
import soot.Value;
import soot.ValueBox;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/asm/Operand.class */
public final class Operand {
    final AbstractInsnNode insn;
    final Value value;
    Local stack;
    private Object boxes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public Operand(AbstractInsnNode insn, Value value) {
        this.insn = insn;
        this.value = value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void removeBox(ValueBox vb) {
        if (vb == null) {
            return;
        }
        if (this.boxes == vb) {
            this.boxes = null;
        } else if (this.boxes instanceof List) {
            List<ValueBox> list = (List) this.boxes;
            list.remove(vb);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void addBox(ValueBox vb) {
        if (this.boxes instanceof List) {
            ((List) this.boxes).add(vb);
        } else if (this.boxes instanceof ValueBox) {
            ValueBox ovb = (ValueBox) this.boxes;
            List<ValueBox> list = new ArrayList<>();
            list.add(ovb);
            list.add(vb);
            this.boxes = list;
        } else {
            this.boxes = vb;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void updateBoxes() {
        Value val = stackOrValue();
        if (!(this.boxes instanceof List)) {
            if (this.boxes instanceof ValueBox) {
                ((ValueBox) this.boxes).setValue(val);
                return;
            }
            return;
        }
        for (ValueBox vb : (List) this.boxes) {
            vb.setValue(val);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public <A> A value() {
        return (A) this.value;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Value stackOrValue() {
        Local s = this.stack;
        return s == null ? this.value : s;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public boolean equivTo(Operand other) {
        if (other.value == null && this.value == null) {
            return true;
        }
        return stackOrValue().equivTo(other.stackOrValue());
    }

    public boolean equals(Object other) {
        return (other instanceof Operand) && equivTo((Operand) other);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        boolean hasStack = false;
        if (this.stack != null) {
            sb.append(this.stack.toString());
            hasStack = true;
        }
        if (this.value != null && hasStack) {
            sb.append(" - ");
            sb.append(this.value.toString());
        }
        return sb.toString();
    }
}
