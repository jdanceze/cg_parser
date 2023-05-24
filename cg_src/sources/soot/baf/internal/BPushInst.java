package soot.baf.internal;

import soot.UnitPrinter;
import soot.baf.InstSwitch;
import soot.baf.PushInst;
import soot.coffi.Instruction;
import soot.jimple.Constant;
import soot.jimple.DoubleConstant;
import soot.jimple.LongConstant;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BPushInst.class */
public class BPushInst extends AbstractInst implements PushInst {
    private Constant constant;

    public BPushInst(Constant c) {
        this.constant = c;
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BPushInst(getConstant());
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "push";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public final String getParameters() {
        return Instruction.argsep + this.constant.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        up.constant(this.constant);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        if ((this.constant instanceof LongConstant) || (this.constant instanceof DoubleConstant)) {
            return 2;
        }
        return 1;
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).casePushInst(this);
    }

    @Override // soot.baf.PushInst
    public Constant getConstant() {
        return this.constant;
    }

    @Override // soot.baf.PushInst
    public void setConstant(Constant c) {
        this.constant = c;
    }
}
