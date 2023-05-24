package soot.baf.internal;

import soot.SootMethodRef;
import soot.baf.InstSwitch;
import soot.baf.SpecialInvokeInst;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BSpecialInvokeInst.class */
public class BSpecialInvokeInst extends AbstractInvokeInst implements SpecialInvokeInst {
    public BSpecialInvokeInst(SootMethodRef methodRef) {
        if (methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.methodRef = methodRef;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BSpecialInvokeInst(this.methodRef);
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return super.getInCount() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return super.getInMachineCount() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.SPECIALINVOKE;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseSpecialInvokeInst(this);
    }
}
