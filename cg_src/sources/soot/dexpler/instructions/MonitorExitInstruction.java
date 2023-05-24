package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.OneRegisterInstruction;
import soot.Local;
import soot.dexpler.DexBody;
import soot.jimple.ExitMonitorStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/MonitorExitInstruction.class */
public class MonitorExitInstruction extends DexlibAbstractInstruction {
    public MonitorExitInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        int reg = ((OneRegisterInstruction) this.instruction).getRegisterA();
        Local object = body.getRegisterLocal(reg);
        ExitMonitorStmt exitMonitorStmt = Jimple.v().newExitMonitorStmt(object);
        setUnit(exitMonitorStmt);
        addTags(exitMonitorStmt);
        body.add(exitMonitorStmt);
    }
}
