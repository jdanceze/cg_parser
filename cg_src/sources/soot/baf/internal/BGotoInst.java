package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.GotoInst;
import soot.baf.InstSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BGotoInst.class */
public class BGotoInst extends AbstractBranchInst implements GotoInst {
    public BGotoInst(Unit target) {
        super(Baf.v().newInstBox(target));
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BGotoInst(getTarget());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractBranchInst, soot.baf.internal.AbstractInst, soot.Unit
    public boolean branches() {
        return true;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.GOTO;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseGotoInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean fallsThrough() {
        return false;
    }
}
