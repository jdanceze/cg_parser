package org.jf.dexlib2.iface.instruction;

import org.jf.dexlib2.Opcode;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/iface/instruction/Instruction.class */
public interface Instruction {
    Opcode getOpcode();

    int getCodeUnits();
}
