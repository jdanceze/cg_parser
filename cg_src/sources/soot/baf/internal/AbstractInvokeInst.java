package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.SootMethod;
import soot.SootMethodRef;
import soot.Type;
import soot.UnitPrinter;
import soot.VoidType;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractInvokeInst.class */
public abstract class AbstractInvokeInst extends AbstractInst {
    SootMethodRef methodRef;

    @Override // soot.baf.internal.AbstractInst
    public abstract String getName();

    public SootMethodRef getMethodRef() {
        return this.methodRef;
    }

    public SootMethod getMethod() {
        return this.methodRef.resolve();
    }

    public Type getType() {
        return this.methodRef.getReturnType();
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return String.valueOf(getName()) + getParameters();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public String getParameters() {
        return Instruction.argsep + this.methodRef.getSignature();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        up.methodRef(this.methodRef);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return getMethodRef().getParameterTypes().size();
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return getMethodRef().getReturnType() instanceof VoidType ? 0 : 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        int count = 0;
        for (Type t : getMethodRef().getParameterTypes()) {
            count += AbstractJasminClass.sizeOfType(t);
        }
        return count;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        Type returnType = getMethodRef().getReturnType();
        if (returnType instanceof VoidType) {
            return 0;
        }
        return AbstractJasminClass.sizeOfType(returnType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public boolean containsInvokeExpr() {
        return true;
    }
}
