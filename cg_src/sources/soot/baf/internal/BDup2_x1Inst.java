package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup2_x1Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDup2_x1Inst.class */
public class BDup2_x1Inst extends BDupInst implements Dup2_x1Inst {
    private final Type mOp1Type;
    private final Type mOp2Type;
    private final Type mUnderType;

    public BDup2_x1Inst(Type aOp1Type, Type aOp2Type, Type aUnderType) {
        this.mOp1Type = Baf.getDescriptorTypeOf(aOp1Type);
        this.mOp2Type = Baf.getDescriptorTypeOf(aOp2Type);
        this.mUnderType = Baf.getDescriptorTypeOf(aUnderType);
    }

    @Override // soot.baf.Dup2_x1Inst
    public Type getOp1Type() {
        return this.mOp1Type;
    }

    @Override // soot.baf.Dup2_x1Inst
    public Type getOp2Type() {
        return this.mOp2Type;
    }

    @Override // soot.baf.Dup2_x1Inst
    public Type getUnder1Type() {
        return this.mUnderType;
    }

    @Override // soot.baf.DupInst
    public List<Type> getOpTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mOp1Type);
        res.add(this.mOp2Type);
        return res;
    }

    @Override // soot.baf.DupInst
    public List<Type> getUnderTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mUnderType);
        return res;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "dup2_x1";
    }

    @Override // soot.baf.internal.BDupInst, soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDup2_x1Inst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "dup2_x1." + Baf.bafDescriptorOf(this.mOp1Type) + "." + Baf.bafDescriptorOf(this.mOp2Type) + "_" + Baf.bafDescriptorOf(this.mUnderType);
    }
}
