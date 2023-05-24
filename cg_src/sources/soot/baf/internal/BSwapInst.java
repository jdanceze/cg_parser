package soot.baf.internal;

import soot.DoubleType;
import soot.LongType;
import soot.Type;
import soot.baf.Baf;
import soot.baf.InstSwitch;
import soot.baf.SwapInst;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BSwapInst.class */
public class BSwapInst extends AbstractInst implements SwapInst {
    protected Type mFromType;
    protected Type mToType;

    public BSwapInst(Type fromType, Type toType) {
        if ((fromType instanceof LongType) || (fromType instanceof DoubleType)) {
            throw new RuntimeException("fromType is LongType or DoubleType !");
        }
        if ((toType instanceof LongType) || (toType instanceof DoubleType)) {
            throw new RuntimeException("toType is LongType or DoubleType !");
        }
        this.mFromType = Baf.getDescriptorTypeOf(fromType);
        this.mToType = Baf.getDescriptorTypeOf(toType);
    }

    @Override // soot.baf.SwapInst
    public Type getFromType() {
        return this.mFromType;
    }

    @Override // soot.baf.SwapInst
    public void setFromType(Type fromType) {
        this.mFromType = fromType;
    }

    @Override // soot.baf.SwapInst
    public Type getToType() {
        return this.mToType;
    }

    @Override // soot.baf.SwapInst
    public void setToType(Type toType) {
        this.mToType = toType;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 2;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 2;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseSwapInst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "swap." + Baf.bafDescriptorOf(this.mFromType) + Baf.bafDescriptorOf(this.mToType);
    }

    @Override // soot.baf.internal.AbstractInst
    public String getName() {
        return "swap";
    }
}
