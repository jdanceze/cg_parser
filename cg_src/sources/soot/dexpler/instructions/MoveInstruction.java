package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.TwoRegisterInstruction;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/MoveInstruction.class */
public class MoveInstruction extends DexlibAbstractInstruction {
    public MoveInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        int source = i.getRegisterB();
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), body.getRegisterLocal(source));
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    int movesRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        int source = i.getRegisterB();
        if (register == source) {
            return dest;
        }
        return -1;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public int movesToRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        int source = i.getRegisterB();
        if (register == dest) {
            return source;
        }
        return -1;
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean overridesRegister(int register) {
        TwoRegisterInstruction i = (TwoRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
