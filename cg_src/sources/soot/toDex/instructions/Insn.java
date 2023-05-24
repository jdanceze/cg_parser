package soot.toDex.instructions;

import java.util.BitSet;
import java.util.List;
import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.builder.BuilderInstruction;
import soot.toDex.LabelAssigner;
import soot.toDex.Register;
/* loaded from: gencallgraphv3.jar:soot/toDex/instructions/Insn.class */
public interface Insn extends Cloneable {
    Opcode getOpcode();

    List<Register> getRegs();

    BitSet getIncompatibleRegs();

    boolean hasIncompatibleRegs();

    int getMinimumRegsNeeded();

    BuilderInstruction getRealInsn(LabelAssigner labelAssigner);

    int getSize();
}
