package soot.dexpler.instructions;

import java.util.ArrayList;
import java.util.List;
import org.jf.dexlib2.iface.instruction.Instruction;
import org.jf.dexlib2.iface.instruction.SwitchElement;
import org.jf.dexlib2.iface.instruction.formats.SparseSwitchPayload;
import soot.Local;
import soot.Unit;
import soot.dexpler.DexBody;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/SparseSwitchInstruction.class */
public class SparseSwitchInstruction extends SwitchInstruction {
    public SparseSwitchInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.SwitchInstruction
    protected Stmt switchStatement(DexBody body, Instruction targetData, Local key) {
        SparseSwitchPayload i = (SparseSwitchPayload) targetData;
        List<? extends SwitchElement> seList = i.getSwitchElements();
        int defaultTargetAddress = this.codeAddress + this.instruction.getCodeUnits();
        Unit defaultTarget = body.instructionAtAddress(defaultTargetAddress).getUnit();
        List<IntConstant> lookupValues = new ArrayList<>();
        List<Unit> targets = new ArrayList<>();
        for (SwitchElement se : seList) {
            lookupValues.add(IntConstant.v(se.getKey()));
            int offset = se.getOffset();
            targets.add(body.instructionAtAddress(this.codeAddress + offset).getUnit());
        }
        LookupSwitchStmt switchStmt = Jimple.v().newLookupSwitchStmt(key, lookupValues, targets, defaultTarget);
        setUnit(switchStmt);
        addTags(switchStmt);
        return switchStmt;
    }

    @Override // soot.dexpler.instructions.PseudoInstruction
    public void computeDataOffsets(DexBody body) {
    }
}
