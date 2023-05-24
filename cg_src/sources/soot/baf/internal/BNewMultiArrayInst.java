package soot.baf.internal;

import soot.ArrayType;
import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.NewMultiArrayInst;
import soot.coffi.Instruction;
import soot.jimple.Jimple;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BNewMultiArrayInst.class */
public class BNewMultiArrayInst extends AbstractInst implements NewMultiArrayInst {
    int dimensionCount;
    ArrayType baseType;

    public BNewMultiArrayInst(ArrayType opType, int dimensionCount) {
        this.dimensionCount = dimensionCount;
        this.baseType = opType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BNewMultiArrayInst(getBaseType(), getDimensionCount());
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return this.dimensionCount;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return this.dimensionCount;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return Jimple.NEWMULTIARRAY;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public final String getParameters() {
        return Instruction.argsep + this.dimensionCount;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        up.literal(Integer.toString(this.dimensionCount));
    }

    @Override // soot.baf.NewMultiArrayInst
    public ArrayType getBaseType() {
        return this.baseType;
    }

    @Override // soot.baf.NewMultiArrayInst
    public void setBaseType(ArrayType type) {
        this.baseType = type;
    }

    @Override // soot.baf.NewMultiArrayInst
    public int getDimensionCount() {
        return this.dimensionCount;
    }

    @Override // soot.baf.NewMultiArrayInst
    public void setDimensionCount(int x) {
        this.dimensionCount = x;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseNewMultiArrayInst(this);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public boolean containsNewExpr() {
        return true;
    }
}
