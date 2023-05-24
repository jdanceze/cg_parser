package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.IncInst;
import soot.baf.InstSwitch;
import soot.coffi.Instruction;
import soot.jimple.Constant;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BIncInst.class */
public class BIncInst extends AbstractInst implements IncInst {
    final ValueBox localBox;
    final ValueBox defLocalBox;
    final List<ValueBox> useBoxes;
    final List<ValueBox> mDefBoxes;
    Constant mConstant;

    public BIncInst(Local local, Constant constant) {
        this.mConstant = constant;
        this.localBox = new BafLocalBox(local);
        this.useBoxes = Collections.singletonList(this.localBox);
        this.defLocalBox = new BafLocalBox(local);
        this.mDefBoxes = Collections.singletonList(this.defLocalBox);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BIncInst(getLocal(), getConstant());
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
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.baf.IncInst
    public Constant getConstant() {
        return this.mConstant;
    }

    @Override // soot.baf.IncInst
    public void setConstant(Constant aConstant) {
        this.mConstant = aConstant;
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "inc.i";
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // soot.baf.internal.AbstractInst
    public final String getParameters() {
        return Instruction.argsep + this.localBox.getValue().toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // soot.baf.internal.AbstractInst
    public void getParameters(UnitPrinter up) {
        up.literal(Instruction.argsep);
        this.localBox.toString(up);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseIncInst(this);
    }

    @Override // soot.baf.IncInst
    public void setLocal(Local l) {
        this.localBox.setValue(l);
    }

    @Override // soot.baf.IncInst
    public Local getLocal() {
        return (Local) this.localBox.getValue();
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        return this.useBoxes;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getDefBoxes() {
        return this.mDefBoxes;
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return "inc.i " + getLocal() + Instruction.argsep + getConstant();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal("inc.i ");
        this.localBox.toString(up);
        up.literal(Instruction.argsep);
        up.constant(this.mConstant);
    }
}
