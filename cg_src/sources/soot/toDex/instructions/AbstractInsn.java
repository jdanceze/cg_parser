package soot.toDex.instructions;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import soot.coffi.Instruction;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
import soot.toDex.SootToDexUtils;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/AbstractInsn.class */
public abstract class AbstractInsn implements Insn {
    protected Opcode opc;
    protected List<Register> regs;

    protected abstract BuilderInstruction getRealInsn0(LabelAssigner labelAssigner);

    public AbstractInsn(Opcode opc) {
        if (opc == null) {
            throw new IllegalArgumentException("opcode must not be null");
        }
        this.opc = opc;
        this.regs = new ArrayList();
    }

    @Override // soot.toDex.instructions.Insn
    public Opcode getOpcode() {
        return this.opc;
    }

    @Override // soot.toDex.instructions.Insn
    public List<Register> getRegs() {
        return this.regs;
    }

    @Override // soot.toDex.instructions.Insn
    public BitSet getIncompatibleRegs() {
        return new BitSet(0);
    }

    @Override // soot.toDex.instructions.Insn
    public boolean hasIncompatibleRegs() {
        return getIncompatibleRegs().cardinality() > 0;
    }

    @Override // soot.toDex.instructions.Insn
    public int getMinimumRegsNeeded() {
        BitSet incompatRegs = getIncompatibleRegs();
        int resultNeed = 0;
        int miscRegsNeed = 0;
        boolean hasResult = this.opc.setsRegister();
        if (hasResult && incompatRegs.get(0)) {
            resultNeed = SootToDexUtils.getDexWords(this.regs.get(0).getType());
        }
        for (int i = hasResult ? 1 : 0; i < this.regs.size(); i++) {
            if (incompatRegs.get(i)) {
                miscRegsNeed += SootToDexUtils.getDexWords(this.regs.get(i).getType());
            }
        }
        if (this.opc.name.endsWith("/2addr")) {
            return resultNeed + miscRegsNeed;
        }
        return Math.max(resultNeed, miscRegsNeed);
    }

    @Override // soot.toDex.instructions.Insn
    public BuilderInstruction getRealInsn(LabelAssigner assigner) {
        if (hasIncompatibleRegs()) {
            throw new RuntimeException("the instruction still has incompatible registers: " + getIncompatibleRegs());
        }
        return getRealInsn0(assigner);
    }

    public String toString() {
        return String.valueOf(this.opc.toString()) + Instruction.argsep + this.regs;
    }

    @Override // soot.toDex.instructions.Insn
    public int getSize() {
        return this.opc.format.size / 2;
    }
}
