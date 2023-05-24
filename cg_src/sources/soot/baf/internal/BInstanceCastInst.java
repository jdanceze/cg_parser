package soot.baf.internal;

import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.InstanceCastInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BInstanceCastInst.class */
public class BInstanceCastInst extends AbstractInst implements InstanceCastInst {
    protected Type castType;

    public BInstanceCastInst(Type opType) {
        if (!(opType instanceof RefType) && !(opType instanceof ArrayType)) {
            throw new RuntimeException("invalid InstanceCastInst: " + opType);
        }
        this.castType = opType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BInstanceCastInst(this.castType);
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
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "checkcast";
    }

    @Override // soot.baf.InstanceCastInst
    public Type getCastType() {
        return this.castType;
    }

    @Override // soot.baf.InstanceCastInst
    public void setCastType(Type t) {
        this.castType = t;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseInstanceCastInst(this);
    }
}
