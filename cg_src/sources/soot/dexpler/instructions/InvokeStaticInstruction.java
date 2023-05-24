package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/InvokeStaticInstruction.class */
public class InvokeStaticInstruction extends MethodInvocationInstruction {
    public InvokeStaticInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        jimplifyStatic(body);
    }

    @Override // soot.dexpler.instructions.MethodInvocationInstruction, soot.dexpler.instructions.DexlibAbstractInstruction
    boolean isUsedAsFloatingPoint(DexBody body, int register) {
        return isUsedAsFloatingPoint(body, register, true);
    }
}
