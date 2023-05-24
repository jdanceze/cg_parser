package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import soot.Unit;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/JumpInstruction.class */
public abstract class JumpInstruction extends DexlibAbstractInstruction {
    protected DexlibAbstractInstruction targetInstruction;
    protected Unit markerUnit;

    public JumpInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DexlibAbstractInstruction getTargetInstruction(DexBody body) {
        int offset = ((OffsetInstruction) this.instruction).getCodeOffset();
        int targetAddress = this.codeAddress + offset;
        this.targetInstruction = body.instructionAtAddress(targetAddress);
        return this.targetInstruction;
    }
}
