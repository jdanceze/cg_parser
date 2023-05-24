package soot.baf.internal;

import soot.ArrayType;
import soot.RefType;
import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.InstanceOfInst;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BInstanceOfInst.class */
public class BInstanceOfInst extends AbstractInst implements InstanceOfInst {
    protected Type checkType;

    public BInstanceOfInst(Type opType) {
        if (!(opType instanceof RefType) && !(opType instanceof ArrayType)) {
            throw new RuntimeException("invalid InstanceOfInst: " + opType);
        }
        this.checkType = opType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BInstanceOfInst(this.checkType);
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
        return Jimple.INSTANCEOF;
    }

    @Override // soot.baf.InstanceOfInst
    public Type getCheckType() {
        return this.checkType;
    }

    @Override // soot.baf.InstanceOfInst
    public void setCheckType(Type t) {
        this.checkType = t;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseInstanceOfInst(this);
    }
}
