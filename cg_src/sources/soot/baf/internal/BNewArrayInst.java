package soot.baf.internal;

import soot.Type;
import soot.baf.InstSwitch;
import soot.baf.NewArrayInst;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BNewArrayInst.class */
public class BNewArrayInst extends AbstractInst implements NewArrayInst {
    protected Type baseType;

    public BNewArrayInst(Type opType) {
        this.baseType = opType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BNewArrayInst(this.baseType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return Jimple.NEWARRAY;
    }

    @Override // soot.baf.NewArrayInst
    public Type getBaseType() {
        return this.baseType;
    }

    @Override // soot.baf.NewArrayInst
    public void setBaseType(Type type) {
        this.baseType = type;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseNewArrayInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public boolean containsNewExpr() {
        return true;
    }
}
