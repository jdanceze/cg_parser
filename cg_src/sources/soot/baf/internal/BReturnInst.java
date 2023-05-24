package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.ReturnInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BReturnInst.class */
public class BReturnInst extends AbstractOpTypeInst implements ReturnInst {
    public BReturnInst(Type opType) {
        super(opType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BReturnInst(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return AbstractJasminClass.sizeOfType(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractOpTypeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "return";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseReturnInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean fallsThrough() {
        return false;
    }
}
