package soot.baf.internal;

import soot.baf.InstSwitch;
import soot.baf.ThrowInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BThrowInst.class */
public class BThrowInst extends AbstractInst implements ThrowInst {
    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BThrowInst();
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 1;
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
    public final String getName() {
        return "athrow";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseThrowInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean fallsThrough() {
        return false;
    }
}
