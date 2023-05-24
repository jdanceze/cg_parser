package org.jf.dexlib2.analysis;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/analysis/UnresolvedOdexInstruction.class */
public class UnresolvedOdexInstruction implements Instruction {
    public final Instruction originalInstruction;
    public final int objectRegisterNum;

    public UnresolvedOdexInstruction(Instruction originalInstruction, int objectRegisterNumber) {
        this.originalInstruction = originalInstruction;
        this.objectRegisterNum = objectRegisterNumber;
    }

    @Override // org.jf.dexlib2.iface.instruction.Instruction
    public Opcode getOpcode() {
        return this.originalInstruction.getOpcode();
    }

    @Override // org.jf.dexlib2.iface.instruction.Instruction
    public int getCodeUnits() {
        return this.originalInstruction.getCodeUnits();
    }
}
