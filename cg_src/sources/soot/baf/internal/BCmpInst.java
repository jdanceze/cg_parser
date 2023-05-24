package soot.baf.internal;

import soot.Type;
import soot.baf.CmpInst;
import soot.baf.InstSwitch;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BCmpInst.class */
public class BCmpInst extends AbstractOpTypeInst implements CmpInst {
    public BCmpInst(Type opType) {
        super(opType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BCmpInst(getOpType());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 4;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractOpTypeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return Jimple.CMP;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseCmpInst(this);
    }
}
