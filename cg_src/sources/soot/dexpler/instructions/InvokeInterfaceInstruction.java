package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/InvokeInterfaceInstruction.class */
public class InvokeInterfaceInstruction extends MethodInvocationInstruction {
    public InvokeInterfaceInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        jimplifyInterface(body);
    }
}
