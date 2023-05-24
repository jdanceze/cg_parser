package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.MulInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BMulInst.class */
public class BMulInst extends AbstractOpTypeInst implements MulInst {
    public BMulInst(Type opType) {
        super(opType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BMulInst(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 2 * AbstractJasminClass.sizeOfType(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractOpTypeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1 * AbstractJasminClass.sizeOfType(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "mul";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseMulInst(this);
    }
}
