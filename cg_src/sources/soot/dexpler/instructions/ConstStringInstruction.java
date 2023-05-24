package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction21c;
import org.jf.dexlib2.iface.instruction.formats.Instruction31c;
import org.jf.dexlib2.iface.reference.StringReference;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StringConstant;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/ConstStringInstruction.class */
public class ConstStringInstruction extends DexlibAbstractInstruction {
    public ConstStringInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        String s;
        int dest = ((OneRegisterInstruction) this.instruction).getRegisterA();
        if (this.instruction instanceof Instruction21c) {
            Instruction21c i = (Instruction21c) this.instruction;
            s = ((StringReference) i.getReference()).getString();
        } else if (this.instruction instanceof Instruction31c) {
            Instruction31c i2 = (Instruction31c) this.instruction;
            s = ((StringReference) i2.getReference()).getString();
        } else {
            throw new IllegalArgumentException("Expected Instruction21c or Instruction31c but got neither.");
        }
        StringConstant sc = StringConstant.v(s);
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), sc);
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
