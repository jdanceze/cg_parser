package soot.baf.internal;

import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfNonNullInst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BIfNonNullInst.class */
public class BIfNonNullInst extends AbstractBranchInst implements IfNonNullInst {
    public BIfNonNullInst(Unit target) {
        super(Baf.v().newInstBox(target));
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BIfNonNullInst(getTarget());
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
    public String getName() {
        return "ifnonnull";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseIfNonNullInst(this);
    }
}
