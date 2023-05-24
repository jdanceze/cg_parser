package soot.dexpler.instructions;

import org.jf.dexlib2.Opcode;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction23x;
import soot.ArrayType;
import soot.Local;
import soot.Type;
import soot.UnknownType;
import soot.dexpler.DexBody;
import soot.dexpler.tags.ObjectOpTag;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/AputInstruction.class */
public class AputInstruction extends FieldInstruction {
    public AputInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        if (!(this.instruction instanceof Instruction23x)) {
            throw new IllegalArgumentException("Expected Instruction23x but got: " + this.instruction.getClass());
        }
        Instruction23x aPutInstr = (Instruction23x) this.instruction;
        int source = aPutInstr.getRegisterA();
        Local arrayBase = body.getRegisterLocal(aPutInstr.getRegisterB());
        Local index = body.getRegisterLocal(aPutInstr.getRegisterC());
        ArrayRef arrayRef = Jimple.v().newArrayRef(arrayBase, index);
        Local sourceValue = body.getRegisterLocal(source);
        AssignStmt assign = getAssignStmt(body, sourceValue, arrayRef);
        if (aPutInstr.getOpcode() == Opcode.APUT_OBJECT) {
            assign.addTag(new ObjectOpTag());
        }
        setUnit(assign);
        addTags(assign);
        body.add(assign);
    }

    @Override // soot.dexpler.instructions.FieldInstruction
    protected Type getTargetType(DexBody body) {
        Instruction23x aPutInstr = (Instruction23x) this.instruction;
        Type t = body.getRegisterLocal(aPutInstr.getRegisterB()).getType();
        if (t instanceof ArrayType) {
            return ((ArrayType) t).getElementType();
        }
        return UnknownType.v();
    }
}
