package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Local;
import soot.Type;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.baf.InstSwitch;
import soot.baf.LoadInst;
import soot.coffi.Instruction;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BLoadInst.class */
public class BLoadInst extends AbstractOpTypeInst implements LoadInst {
    ValueBox localBox;
    List<ValueBox> useBoxes;

    public BLoadInst(Type opType, Local local) {
        super(opType);
        this.localBox = new BafLocalBox(local);
        this.useBoxes = Collections.singletonList(this.localBox);
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BLoadInst(getOpType(), getLocal());
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

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return "load";
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
        ((InstSwitch) sw).caseLoadInst(this);
    }

    @Override // soot.baf.LoadInst
    public void setLocal(Local l) {
        this.localBox.setValue(l);
    }

    @Override // soot.baf.LoadInst
    public Local getLocal() {
        return (Local) this.localBox.getValue();
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        return this.useBoxes;
    }
}
