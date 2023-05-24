package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.baf.Baf;
import soot.baf.Dup2_x2Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDup2_x2Inst.class */
public class BDup2_x2Inst extends BDupInst implements Dup2_x2Inst {
    private final Type mOp1Type;
    private final Type mOp2Type;
    private final Type mUnder1Type;
    private final Type mUnder2Type;

    public BDup2_x2Inst(Type aOp1Type, Type aOp2Type, Type aUnder1Type, Type aUnder2Type) {
        this.mOp1Type = Baf.getDescriptorTypeOf(aOp1Type);
        this.mOp2Type = Baf.getDescriptorTypeOf(aOp2Type);
        this.mUnder1Type = Baf.getDescriptorTypeOf(aUnder1Type);
        this.mUnder2Type = Baf.getDescriptorTypeOf(aUnder2Type);
    }

    @Override // soot.baf.Dup2_x2Inst
    public Type getOp1Type() {
        return this.mOp1Type;
    }

    @Override // soot.baf.Dup2_x2Inst
    public Type getOp2Type() {
        return this.mOp2Type;
    }

    @Override // soot.baf.Dup2_x2Inst
    public Type getUnder1Type() {
        return this.mUnder1Type;
    }

    @Override // soot.baf.Dup2_x2Inst
    public Type getUnder2Type() {
        return this.mUnder2Type;
    }

    @Override // soot.baf.DupInst
    public List<Type> getOpTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mOp1Type);
        if (this.mOp2Type != null) {
            res.add(this.mOp2Type);
        }
        return res;
    }

    @Override // soot.baf.DupInst
    public List<Type> getUnderTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mUnder1Type);
        if (this.mUnder2Type != null) {
            res.add(this.mUnder2Type);
        }
        return res;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "dup2_x2";
    }

    @Override // soot.baf.internal.BDupInst, soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDup2_x2Inst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        String optypes = Baf.bafDescriptorOf(this.mOp1Type);
        if (this.mOp2Type != null) {
            optypes = String.valueOf(optypes) + "." + Baf.bafDescriptorOf(this.mOp2Type);
        }
        String undertypes = Baf.bafDescriptorOf(this.mUnder1Type);
        if (this.mUnder2Type != null) {
            optypes = String.valueOf(optypes) + "." + Baf.bafDescriptorOf(this.mUnder2Type);
        }
        return "dup2_x2." + optypes + "_" + undertypes;
    }
}
