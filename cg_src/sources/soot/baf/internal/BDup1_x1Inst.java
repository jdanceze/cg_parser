package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup1_x1Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDup1_x1Inst.class */
public class BDup1_x1Inst extends BDupInst implements Dup1_x1Inst {
    private final Type mOpType;
    private final Type mUnderType;

    public BDup1_x1Inst(Type aOpType, Type aUnderType) {
        this.mOpType = Baf.getDescriptorTypeOf(aOpType);
        this.mUnderType = Baf.getDescriptorTypeOf(aUnderType);
    }

    @Override // soot.baf.Dup1_x1Inst
    public Type getOp1Type() {
        return this.mOpType;
    }

    @Override // soot.baf.Dup1_x1Inst
    public Type getUnder1Type() {
        return this.mUnderType;
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
        res.add(this.mUnderType);
        return res;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "dup1_x1";
    }

    @Override // soot.baf.internal.BDupInst, soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDup1_x1Inst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "dup1_x1." + Baf.bafDescriptorOf(this.mOpType) + "_" + Baf.bafDescriptorOf(this.mUnderType);
    }
}
