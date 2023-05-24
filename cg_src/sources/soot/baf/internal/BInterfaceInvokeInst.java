package soot.baf.internal;

import soot.SootMethodRef;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.InterfaceInvokeInst;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BInterfaceInvokeInst.class */
public class BInterfaceInvokeInst extends AbstractInvokeInst implements InterfaceInvokeInst {
    int argCount;

    public BInterfaceInvokeInst(SootMethodRef methodRef, int argCount) {
        if (methodRef.isStatic()) {
            throw new RuntimeException("wrong static-ness");
        }
        this.methodRef = methodRef;
        this.argCount = argCount;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BInterfaceInvokeInst(this.methodRef, getArgCount());
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return this.methodRef.getParameterTypes().size() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return super.getInMachineCount() + 1;
    }

    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public final String getName() {
        return Jimple.INTERFACEINVOKE;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public final String getParameters() {
        return String.valueOf(super.getParameters()) + Instruction.argsep + this.argCount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInvokeInst, soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        super.getParameters(up);
        up.literal(Instruction.argsep);
        up.literal(Integer.toString(this.argCount));
    }

    @Override // soot.baf.InterfaceInvokeInst
    public int getArgCount() {
        return this.argCount;
    }

    @Override // soot.baf.InterfaceInvokeInst
    public void setArgCount(int x) {
        this.argCount = x;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseInterfaceInvokeInst(this);
    }
}
