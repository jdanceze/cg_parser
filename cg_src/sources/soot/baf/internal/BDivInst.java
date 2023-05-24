package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.baf.DivInst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDivInst.class */
public class BDivInst extends AbstractOpTypeInst implements DivInst {
    public BDivInst(Type opType) {
        super(opType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BDivInst(getOpType());
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
        return "div";
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDivInst(this);
    }
}
