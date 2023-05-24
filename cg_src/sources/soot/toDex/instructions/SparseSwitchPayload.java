package soot.toDex.instructions;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.SwitchLabelElement;
import org.jf.dexlib2.builder.instruction.BuilderSparseSwitchPayload;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/SparseSwitchPayload.class */
public class SparseSwitchPayload extends SwitchPayload {
    private int[] keys;

    public SparseSwitchPayload(int[] keys, List<Unit> targets) {
        super(targets);
        this.keys = keys;
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public int getSize() {
        return 2 + (this.targets.size() * 4);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        List<SwitchLabelElement> elements = new ArrayList<>();
        for (int i = 0; i < this.keys.length; i++) {
            elements.add(new SwitchLabelElement(this.keys[i], assigner.getOrCreateLabel((Stmt) this.targets.get(i))));
        }
        return new BuilderSparseSwitchPayload(elements);
    }
}
