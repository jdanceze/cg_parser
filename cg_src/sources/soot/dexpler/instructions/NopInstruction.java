package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.NopStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/NopInstruction.class */
public class NopInstruction extends DexlibAbstractInstruction {
    public NopInstruction(Instruction instruction, int codeAddress) {
        super(instruction, codeAddress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        NopStmt nop = Jimple.v().newNopStmt();
        setUnit(nop);
        addTags(nop);
        body.add(nop);
    }
}
