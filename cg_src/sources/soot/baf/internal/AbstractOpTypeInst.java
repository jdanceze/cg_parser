package soot.baf.internal;

import soot.AbstractJasminClass;
import soot.Type;
import soot.UnitPrinter;
import soot.baf.Baf;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractOpTypeInst.class */
public abstract class AbstractOpTypeInst extends AbstractInst {
    protected Type opType;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractOpTypeInst(Type opType) {
        setOpType(opType);
    }

    public Type getOpType() {
        return this.opType;
    }

    public void setOpType(Type t) {
        this.opType = Baf.getDescriptorTypeOf(t);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return String.valueOf(getName()) + "." + Baf.bafDescriptorOf(this.opType) + getParameters();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(getName());
        up.literal(".");
        up.literal(Baf.bafDescriptorOf(this.opType));
        getParameters(up);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return AbstractJasminClass.sizeOfType(getOpType());
    }
}
