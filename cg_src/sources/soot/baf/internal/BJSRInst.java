package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.JSRInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BJSRInst.class */
public class BJSRInst extends AbstractBranchInst implements JSRInst {
    public BJSRInst(Unit target) {
        super(Baf.v().newInstBox(target));
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BJSRInst(getTarget());
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
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return "jsr";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseJSRInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean fallsThrough() {
        return false;
    }
}
