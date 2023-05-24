package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.UnitPrinter;
import soot.Value;
import soot.ValueBox;
import soot.baf.Baf;
import soot.baf.IdentityInst;
import soot.baf.InstSwitch;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BIdentityInst.class */
public class BIdentityInst extends AbstractInst implements IdentityInst {
    ValueBox leftBox;
    ValueBox rightBox;
    List<ValueBox> defBoxes;

    protected BIdentityInst(ValueBox localBox, ValueBox identityValueBox) {
        this.leftBox = localBox;
        this.rightBox = identityValueBox;
        this.defBoxes = Collections.singletonList(localBox);
    }

    public BIdentityInst(Value local, Value identityValue) {
        this(Baf.v().newLocalBox(local), Baf.v().newIdentityRefBox(identityValue));
    }

    @Override // soot.baf.internal.AbstractInst, soot.AbstractUnit, soot.Unit
    public Object clone() {
        return new BIdentityInst(getLeftOp(), getRightOp());
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

    @Override // soot.IdentityUnit
    public Value getLeftOp() {
        return this.leftBox.getValue();
    }

    @Override // soot.IdentityUnit
    public Value getRightOp() {
        return this.rightBox.getValue();
    }

    @Override // soot.IdentityUnit
    public ValueBox getLeftOpBox() {
        return this.leftBox;
    }

    @Override // soot.IdentityUnit
    public ValueBox getRightOpBox() {
        return this.rightBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getDefBoxes() {
        return this.defBoxes;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>();
        list.addAll(this.rightBox.getValue().getUseBoxes());
        list.add(this.rightBox);
        list.addAll(this.leftBox.getValue().getUseBoxes());
        return list;
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        return String.valueOf(this.leftBox.getValue().toString()) + " := " + this.rightBox.getValue().toString();
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        this.leftBox.toString(up);
        up.literal(" := ");
        this.rightBox.toString(up);
    }

    @Override // soot.baf.internal.AbstractInst
    public final String getName() {
        return ":=";
    }

    @Override // soot.baf.IdentityInst
    public void setLeftOp(Value local) {
        this.leftBox.setValue(local);
    }

    @Override // soot.baf.IdentityInst
    public void setRightOp(Value identityRef) {
        this.rightBox.setValue(identityRef);
    }

    @Override // soot.AbstractUnit, soot.util.Switchable
    public void apply(Switch sw) {
        ((InstSwitch) sw).caseIdentityInst(this);
    }
}
