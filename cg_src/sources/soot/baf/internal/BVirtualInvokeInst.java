package soot.baf.internal;

import soot.SootMethodRef;
import soot.baf.InstSwitch;
import soot.baf.VirtualInvokeInst;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BVirtualInvokeInst.class */
public class BVirtualInvokeInst extends AbstractInvokeInst implements VirtualInvokeInst {
    public BVirtualInvokeInst(SootMethodRef methodRef) {
        if (methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.methodRef = methodRef;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BVirtualInvokeInst(this.methodRef);
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return super.getInMachineCount() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return super.getInCount() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public final String getName() {
        return Jimple.VIRTUALINVOKE;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseVirtualInvokeInst(this);
    }
}
