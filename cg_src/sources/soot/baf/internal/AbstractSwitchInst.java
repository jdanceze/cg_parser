package soot.baf.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.baf.Baf;
import soot.baf.SwitchInst;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/AbstractSwitchInst.class */
public abstract class AbstractSwitchInst extends AbstractInst implements SwitchInst {
    UnitBox defaultTargetBox;
    UnitBox[] targetBoxes;
    List<UnitBox> unitBoxes;

    public AbstractSwitchInst(Unit defaultTarget, List<? extends Unit> targets) {
        this.defaultTargetBox = Baf.v().newInstBox(defaultTarget);
        int numTargets = targets.size();
        UnitBox[] tgts = new UnitBox[numTargets];
        for (int i = 0; i < numTargets; i++) {
            tgts[i] = Baf.v().newInstBox(targets.get(i));
        }
        this.targetBoxes = tgts;
        List<UnitBox> unitBoxes = new ArrayList<>(numTargets + 1);
        for (UnitBox element : tgts) {
            unitBoxes.add(element);
        }
        unitBoxes.add(this.defaultTargetBox);
        this.unitBoxes = Collections.unmodifiableList(unitBoxes);
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getInMachineCount() {
        return 1;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutCount() {
        return 0;
    }

    @Override // soot.baf.internal.AbstractInst, soot.baf.Inst
    public int getOutMachineCount() {
        return 0;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public List<UnitBox> getUnitBoxes() {
        return this.unitBoxes;
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean fallsThrough() {
        return false;
    }

    @Override // soot.baf.internal.AbstractInst, soot.Unit
    public boolean branches() {
        return true;
    }

    @Override // soot.baf.SwitchInst
    public Unit getDefaultTarget() {
        return this.defaultTargetBox.getUnit();
    }

    @Override // soot.baf.SwitchInst
    public void setDefaultTarget(Unit defaultTarget) {
        this.defaultTargetBox.setUnit(defaultTarget);
    }

    @Override // soot.baf.SwitchInst
    public UnitBox getDefaultTargetBox() {
        return this.defaultTargetBox;
    }

    @Override // soot.baf.SwitchInst
    public int getTargetCount() {
        return this.targetBoxes.length;
    }

    @Override // soot.baf.SwitchInst
    public Unit getTarget(int index) {
        return this.targetBoxes[index].getUnit();
    }

    @Override // soot.baf.SwitchInst
    public void setTarget(int index, Unit target) {
        this.targetBoxes[index].setUnit(target);
    }

    @Override // soot.baf.SwitchInst
    public void setTargets(List<Unit> targets) {
        UnitBox[] targetBoxes = this.targetBoxes;
        for (int i = 0; i < targets.size(); i++) {
            targetBoxes[i].setUnit(targets.get(i));
        }
    }

    @Override // soot.baf.SwitchInst
    public UnitBox getTargetBox(int index) {
        return this.targetBoxes[index];
    }

    @Override // soot.baf.SwitchInst
    public List<Unit> getTargets() {
        UnitBox[] unitBoxArr;
        List<Unit> targets = new ArrayList<>();
        for (UnitBox element : this.targetBoxes) {
            targets.add(element.getUnit());
        }
        return targets;
    }
}
