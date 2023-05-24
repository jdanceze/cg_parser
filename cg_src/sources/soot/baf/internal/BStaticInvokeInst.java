package soot.baf.internal;

import soot.SootMethodRef;
import soot.VoidType;
import soot.baf.InstSwitch;
import soot.baf.StaticInvokeInst;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BStaticInvokeInst.class */
public class BStaticInvokeInst extends AbstractInvokeInst implements StaticInvokeInst {
    public BStaticInvokeInst(SootMethodRef methodRef) {
        if (!methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.methodRef = methodRef;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BStaticInvokeInst(this.methodRef);
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return this.methodRef.getParameterTypes().size();
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return this.methodRef.getReturnType() instanceof VoidType ? 0 : 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public String getName() {
        return Jimple.STATICINVOKE;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseStaticInvokeInst(this);
    }
}
