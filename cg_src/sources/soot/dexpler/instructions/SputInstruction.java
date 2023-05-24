package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import org.jf.dexlib2.iface.instruction.ReferenceInstruction;
import org.jf.dexlib2.iface.reference.FieldReference;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticFieldRef;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/SputInstruction.class */
public class SputInstruction extends FieldInstruction {
    public SputInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int source = ((OneRegisterInstruction) this.instruction).getRegisterA();
        FieldReference f = (FieldReference) ((ReferenceInstruction) this.instruction).getReference();
        StaticFieldRef instanceField = Jimple.v().newStaticFieldRef(getStaticSootFieldRef(f));
        Local sourceValue = body.getRegisterLocal(source);
        AssignStmt assign = getAssignStmt(body, sourceValue, instanceField);
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    @Override // soot.dexpler.instructions.FieldInstruction
    protected Type getTargetType(DexBody body) {
        FieldReference f = (FieldReference) ((ReferenceInstruction) this.instruction).getReference();
        return DexType.toSoot(f.getType());
    }
}
