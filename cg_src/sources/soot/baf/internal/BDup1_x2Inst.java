package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup1_x2Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDup1_x2Inst.class */
public class BDup1_x2Inst extends BDupInst implements Dup1_x2Inst {
    private final Type mOpType;
    private final Type mUnder1Type;
    private final Type mUnder2Type;

    public BDup1_x2Inst(Type aOpType, Type aUnder1Type, Type aUnder2Type) {
        this.mOpType = Baf.getDescriptorTypeOf(aOpType);
        this.mUnder1Type = Baf.getDescriptorTypeOf(aUnder1Type);
        this.mUnder2Type = Baf.getDescriptorTypeOf(aUnder2Type);
    }

    @Override // soot.baf.Dup1_x2Inst
    public Type getOp1Type() {
        return this.mOpType;
    }

    @Override // soot.baf.Dup1_x2Inst
    public Type getUnder1Type() {
        return this.mUnder1Type;
    }

    @Override // soot.baf.Dup1_x2Inst
    public Type getUnder2Type() {
        return this.mUnder2Type;
    }

    @Override // soot.baf.DupInst
    public List<Type> getOpTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mOpType);
        return res;
    }

    @Override // soot.baf.DupInst
    public List<Type> getUnderTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mUnder1Type);
        res.add(this.mUnder2Type);
        return res;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "dup1_x2";
    }

    @Override // soot.baf.internal.BDupInst, soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDup1_x2Inst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "dup1_x2." + Baf.bafDescriptorOf(this.mOpType) + "_" + Baf.bafDescriptorOf(this.mUnder1Type) + "." + Baf.bafDescriptorOf(this.mUnder2Type);
    }
}
