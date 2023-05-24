package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.formats.Instruction35c;
import org.jf.dexlib2.iface.reference.TypeReference;
import soot.ArrayType;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.dexpler.DexType;
import soot.dexpler.Util;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NewArrayExpr;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/FilledNewArrayInstruction.class */
public class FilledNewArrayInstruction extends FilledArrayInstruction {
    public FilledNewArrayInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        if (!(this.instruction instanceof Instruction35c)) {
            throw new IllegalArgumentException("Expected Instruction35c but got: " + this.instruction.getClass());
        }
        Instruction35c filledNewArrayInstr = (Instruction35c) this.instruction;
        int[] regs = {filledNewArrayInstr.getRegisterC(), filledNewArrayInstr.getRegisterD(), filledNewArrayInstr.getRegisterE(), filledNewArrayInstr.getRegisterF(), filledNewArrayInstr.getRegisterG()};
        int usedRegister = filledNewArrayInstr.getRegisterCount();
        Type t = DexType.toSoot((TypeReference) filledNewArrayInstr.getReference());
        Type arrayType = ((ArrayType) t).getElementType();
        NewArrayExpr arrayExpr = Jimple.v().newNewArrayExpr(arrayType, IntConstant.v(usedRegister));
        Local arrayLocal = body.getStoreResultLocal();
        AssignStmt assign = Jimple.v().newAssignStmt(arrayLocal, arrayExpr);
        body.add(assign);
        for (int i = 0; i < usedRegister; i++) {
            ArrayRef arrayRef = Jimple.v().newArrayRef(arrayLocal, IntConstant.v(i));
            AssignStmt assign2 = Jimple.v().newAssignStmt(arrayRef, body.getRegisterLocal(regs[i]));
            addTags(assign2);
            body.add(assign2);
        }
        setUnit(assign);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean isUsedAsFloatingPoint(DexBody body, int register) {
        Instruction35c i = (Instruction35c) this.instruction;
        Type arrayType = DexType.toSoot((TypeReference) i.getReference());
        return isRegisterUsed(register) && Util.isFloatLike(arrayType);
    }

    private boolean isRegisterUsed(int register) {
        Instruction35c i = (Instruction35c) this.instruction;
        return register == i.getRegisterD() || register == i.getRegisterE() || register == i.getRegisterF() || register == i.getRegisterG() || register == i.getRegisterC();
    }
}
