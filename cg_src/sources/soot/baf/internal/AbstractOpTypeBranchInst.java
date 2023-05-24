package soot.baf.internal;

import soot.Type;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractOpTypeBranchInst.class */
public abstract class AbstractOpTypeBranchInst extends AbstractBranchInst {
    protected Type opType;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractOpTypeBranchInst(Type opType, UnitBox targetBox) {
        super(targetBox);
        setOpType(opType);
    }

    public Type getOpType() {
        return this.opType;
    }

    public void setOpType(Type t) {
        this.opType = Baf.getDescriptorTypeOf(t);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractBranchInst, soot.baf.internal.AbstractInst
    public String toString() {
        return String.valueOf(getName()) + "." + Baf.bafDescriptorOf(this.opType) + Instruction.argsep + getTarget();
    }

    @Override // soot.baf.internal.AbstractBranchInst, soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(getName());
        up.literal(".");
        up.literal(Baf.bafDescriptorOf(this.opType));
        up.literal(Instruction.argsep);
        this.targetBox.toString(up);
    }
}
