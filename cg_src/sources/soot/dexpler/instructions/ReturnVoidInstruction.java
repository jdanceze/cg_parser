package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.ReturnVoidStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/ReturnVoidInstruction.class */
public class ReturnVoidInstruction extends DexlibAbstractInstruction {
    public ReturnVoidInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        ReturnVoidStmt returnStmt = Jimple.v().newReturnVoidStmt();
        setUnit(returnStmt);
        addTags(returnStmt);
        body.add(returnStmt);
    }
}
