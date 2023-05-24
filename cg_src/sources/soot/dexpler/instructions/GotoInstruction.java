package soot.dexpler.instructions;

import org.jf.dexlib2.iface.instruction.Instruction;
import soot.dexpler.DexBody;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/instructions/GotoInstruction.class */
public class GotoInstruction extends JumpInstruction implements DeferableInstruction {
    public GotoInstruction(Instruction instruction, int codeAdress) {
        super(instruction, codeAdress);
    }

    @Override // soot.dexpler.instructions.DexlibAbstractInstruction
    public void jimplify(DexBody body) {
        if (getTargetInstruction(body).getUnit() != null) {
            body.add(gotoStatement());
            return;
        }
        body.addDeferredJimplification(this);
        this.markerUnit = Jimple.v().newNopStmt();
        addTags(this.markerUnit);
        this.unit = this.markerUnit;
        body.add(this.markerUnit);
    }

    @Override // soot.dexpler.instructions.DeferableInstruction
    public void deferredJimplify(DexBody body) {
        body.getBody().getUnits().insertAfter(gotoStatement(), (GotoStmt) this.markerUnit);
    }

    private GotoStmt gotoStatement() {
        GotoStmt go = Jimple.v().newGotoStmt(this.targetInstruction.getUnit());
        setUnit(go);
        addTags(go);
        return go;
    }
}
