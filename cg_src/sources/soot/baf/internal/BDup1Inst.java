package soot.baf.internal;

import java.util.ArrayList;
import java.util.List;
import soot.Type;
import soot.UnitPrinter;
import soot.baf.Baf;
import soot.baf.Dup1Inst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BDup1Inst.class */
public class BDup1Inst extends BDupInst implements Dup1Inst {
    private final Type mOpType;

    public BDup1Inst(Type aOpType) {
        this.mOpType = Baf.getDescriptorTypeOf(aOpType);
    }

    @Override // soot.baf.Dup1Inst
    public Type getOp1Type() {
        return this.mOpType;
    }

    @Override // soot.baf.DupInst
    public List<Type> getOpTypes() {
        List<Type> res = new ArrayList<>();
        res.add(this.mOpType);
        return res;
    }

    @Override // soot.baf.DupInst
    public List<Type> getUnderTypes() {
        return new ArrayList();
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "dup1";
    }

    @Override // soot.baf.internal.BDupInst, soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseDup1Inst(this);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "dup1." + Baf.bafDescriptorOf(this.mOpType);
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("dup1.");
        up.literal(Baf.bafDescriptorOf(this.mOpType));
    }
}
