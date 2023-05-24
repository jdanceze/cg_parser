package soot.baf.internal;

import soot.Type;
import soot.baf.ArrayReadInst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BArrayReadInst.class */
public class BArrayReadInst extends AbstractOpTypeInst implements ArrayReadInst {
    public BArrayReadInst(Type opType) {
        super(opType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BArrayReadInst(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "arrayread";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseArrayReadInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public boolean containsArrayRef() {
        return true;
    }
}
