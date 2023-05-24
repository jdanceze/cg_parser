package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.dexpler.DexBody;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/SgetInstruction.class */
public class SgetInstruction extends FieldInstruction {
    public SgetInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int dest = ((OneRegisterInstruction) this.instruction).getRegisterA();
        FieldReference f = (FieldReference) ((ReferenceInstruction) this.instruction).getReference();
        StaticFieldRef r = Jimple.v().newStaticFieldRef(getStaticSootFieldRef(f));
        AssignStmt assign = Jimple.v().newAssignStmt(body.getRegisterLocal(dest), r);
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
