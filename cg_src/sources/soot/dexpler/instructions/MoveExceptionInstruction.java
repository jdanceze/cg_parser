package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Body;
import soot.Local;
import soot.Type;
import soot.dexpler.DexBody;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/MoveExceptionInstruction.class */
public class MoveExceptionInstruction extends DexlibAbstractInstruction implements RetypeableInstruction {
    protected Type realType;
    protected IdentityStmt stmtToRetype;

    public MoveExceptionInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int dest = ((OneRegisterInstruction) this.instruction).getRegisterA();
        Local l = body.getRegisterLocal(dest);
        this.stmtToRetype = Jimple.v().newIdentityStmt(l, Jimple.v().newCaughtExceptionRef());
        setUnit(this.stmtToRetype);
        addTags(this.stmtToRetype);
        body.add(this.stmtToRetype);
    }

    @Override // soot.dexpler.instructions.RetypeableInstruction
    public void setRealType(DexBody body, Type t) {
        this.realType = t;
        body.addRetype(this);
    }

    @Override // soot.dexpler.instructions.RetypeableInstruction
    public void retype(Body body) {
        if (this.realType == null) {
            throw new RuntimeException("Real type of this instruction has not been set or was already retyped: " + this);
        }
        if (body.getUnits().contains(this.stmtToRetype)) {
            Local l = (Local) this.stmtToRetype.getLeftOp();
            l.setType(this.realType);
            this.realType = null;
        }
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    boolean overridesRegister(int register) {
        OneRegisterInstruction i = (OneRegisterInstruction) this.instruction;
        int dest = i.getRegisterA();
        return register == dest;
    }
}
