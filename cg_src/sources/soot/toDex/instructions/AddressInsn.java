package soot.toDex.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import soot.toDex.LabelAssigner;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/AddressInsn.class */
public class AddressInsn extends AbstractInsn {
    private Object originalSource;

    public AddressInsn(Object originalSource) {
        super(Opcode.NOP);
        this.originalSource = originalSource;
    }

    public Object getOriginalSource() {
        return this.originalSource;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    protected BuilderInstruction getRealInsn0(LabelAssigner assigner) {
        return null;
    }

    @Override // soot.toDex.instructions.AbstractInsn, soot.toDex.instructions.Insn
    public int getSize() {
        return 0;
    }

    @Override // soot.toDex.instructions.AbstractInsn
    public String toString() {
        return "address instruction for " + this.originalSource;
    }
}
