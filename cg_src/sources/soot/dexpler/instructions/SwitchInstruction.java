package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OffsetInstruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Local;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.Jimple;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/SwitchInstruction.class */
public abstract class SwitchInstruction extends PseudoInstruction implements DeferableInstruction {
    protected Unit markerUnit;

    protected abstract Stmt switchStatement(DexBody dexBody, Instruction instruction, Local local);

    public SwitchInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        this.markerUnit = Jimple.v().newNopStmt();
        this.unit = this.markerUnit;
        body.add(this.markerUnit);
        body.addDeferredJimplification(this);
    }

    @Override // soot.dexpler.instructions.DeferableInstruction
    public void deferredJimplify(DexBody body) {
        int keyRegister = ((OneRegisterInstruction) this.instruction).getRegisterA();
        int offset = ((OffsetInstruction) this.instruction).getCodeOffset();
        Local key = body.getRegisterLocal(keyRegister);
        int targetAddress = this.codeAddress + offset;
        Instruction targetData = body.instructionAtAddress(targetAddress).instruction;
        Stmt stmt = switchStatement(body, targetData, key);
        body.getBody().getUnits().insertAfter(stmt, (Stmt) this.markerUnit);
    }
}
