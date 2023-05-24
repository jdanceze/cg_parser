package soot.baf.internal;

import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractBranchInst.class */
public abstract class AbstractBranchInst extends AbstractInst {
    UnitBox targetBox;
    final List<UnitBox> targetBoxes;

    /* JADX INFO: Access modifiers changed from: package-private */
    public AbstractBranchInst(UnitBox targetBox) {
        this.targetBox = targetBox;
        this.targetBoxes = Collections.singletonList(targetBox);
    }

    @Override // soot.baf.internal.AbstractInst
    public String toString() {
        String target;
        Unit targetUnit = getTarget();
        if (this == targetUnit) {
            target = getName();
        } else {
            target = targetUnit.toString();
        }
        return String.valueOf(getName()) + Instruction.argsep + target;
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public void toString(UnitPrinter up) {
        up.literal(getName());
        up.literal(Instruction.argsep);
        this.targetBox.toString(up);
    }

    public Unit getTarget() {
        return this.targetBox.getUnit();
    }

    public void setTarget(Unit target) {
        this.targetBox.setUnit(target);
    }

    public UnitBox getTargetBox() {
        return this.targetBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<UnitBox> getUnitBoxes() {
        return this.targetBoxes;
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean branches() {
        return true;
    }
}
