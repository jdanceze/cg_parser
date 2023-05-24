package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/MoveResultInstruction.class */
public class MoveResultInstruction extends DexlibAbstractInstruction {
    public MoveResultInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int dest = ((OneRegisterInstruction) this.instruction).getRegisterA();
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), body.getStoreResultLocal());
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean overridesRegister(int register) {
        OneRegisterInstruction i = (OneRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
