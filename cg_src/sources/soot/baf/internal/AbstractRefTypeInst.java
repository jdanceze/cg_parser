package soot.baf.internal;

import soot.RefType;
import soot.Type;
import soot.UnitPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractRefTypeInst.class */
public abstract class AbstractRefTypeInst extends AbstractInst {
    Type opType;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractRefTypeInst(RefType opType) {
        this.opType = opType;
    }

    public Type getOpType() {
        return this.opType;
    }

    public void setOpType(Type t) {
        this.opType = t;
    }

    public RefType getBaseType() {
        return (RefType) this.opType;
    }

    public void setBaseType(RefType type) {
        this.opType = type;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public String getParameters() {
        return Instruction.argsep + this.opType.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        up.type(this.opType);
    }
}
