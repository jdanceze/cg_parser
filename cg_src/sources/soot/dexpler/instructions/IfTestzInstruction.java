package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21t;
import soot.dexpler.DexBody;
import soot.jimple.BinopExpr;
import soot.jimple.IfStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/IfTestzInstruction.class */
public class IfTestzInstruction extends ConditionalJumpInstruction {
    public IfTestzInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.ConditionalJumpInstruction
    protected IfStmt ifStatement(DexBody body) {
        Instruction21t i = (Instruction21t) this.instruction;
        BinopExpr condition = getComparisonExpr(body, i.getRegisterA());
        IfStmt jif = Jimple.v().newIfStmt(condition, this.targetInstruction.getUnit());
        addTags(jif);
        return jif;
    }
}
