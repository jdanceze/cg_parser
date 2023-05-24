package soot.toDex.instructions;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.builder.BuilderInstruction;
import org.jf.dexlib2.builder.Label;
import org.jf.dexlib2.builder.instruction.BuilderPackedSwitchPayload;
import soot.Unit;
import soot.jimple.Stmt;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/PackedSwitchPayload.class */
public class PackedSwitchPayload extends SwitchPayload {
    private int firstKey;

    public PackedSwitchPayload(int firstKey, List<Unit> targets) {
        super(targets);
        this.firstKey = firstKey;
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public int getSize() {
        return 4 + (this.targets.size() * 2);
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        List<Label> elements = new ArrayList<>();
        for (int i = 0; i < this.targets.size(); i++) {
            elements.add(assigner.getOrCreateLabel((Stmt) this.targets.get(i)));
        }
        return new BuilderPackedSwitchPayload(this.firstKey, elements);
    }
}
