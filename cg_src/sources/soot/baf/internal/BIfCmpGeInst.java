package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.Unit;
import soot.baf.Baf;
import soot.baf.IfCmpGeInst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BIfCmpGeInst.class */
public class BIfCmpGeInst extends AbstractOpTypeBranchInst implements IfCmpGeInst {
    public BIfCmpGeInst(Type opType, Unit target) {
        super(opType, Baf.v().newInstBox(target));
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BIfCmpGeInst(getOpType(), getTarget());
    }

    @Override // soot.baf.internal.AbstractOpTypeBranchInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 2 * AbstractJasminClass.sizeOfType(getOpType());
    }

    @Override // soot.baf.internal.AbstractOpTypeBranchInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return "ifcmpge";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseIfCmpGeInst(this);
    }
}
