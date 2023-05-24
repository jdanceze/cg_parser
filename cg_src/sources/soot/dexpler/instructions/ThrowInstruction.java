package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction11x;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.ThrowStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/ThrowInstruction.class */
public class ThrowInstruction extends DexlibAbstractInstruction {
    public ThrowInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        Instruction11x throwInstruction = (Instruction11x) this.instruction;
        ThrowStmt throwStmt = Jimple.v().newThrowStmt(body.getRegisterLocal(throwInstruction.getRegisterA()));
        setUnit(throwStmt);
        addTags(throwStmt);
        body.add(throwStmt);
    }
}
