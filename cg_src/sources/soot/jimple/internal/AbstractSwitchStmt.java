package soot.jimple.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Function;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.SwitchStmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/AbstractSwitchStmt.class */
public abstract class AbstractSwitchStmt extends AbstractStmt implements SwitchStmt {
    protected final ValueBox keyBox;
    protected final UnitBox defaultTargetBox;
    protected final UnitBox[] targetBoxes;
    protected final List<UnitBox> stmtBoxes;

    /* JADX INFO: Access modifiers changed from: protected */
    public AbstractSwitchStmt(ValueBox keyBox, UnitBox defaultTargetBox, UnitBox... targetBoxes) {
        this.keyBox = keyBox;
        this.defaultTargetBox = defaultTargetBox;
        this.targetBoxes = targetBoxes;
        List<UnitBox> list = new ArrayList<>();
        Collections.addAll(list, targetBoxes);
        list.add(defaultTargetBox);
        this.stmtBoxes = Collections.unmodifiableList(list);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static UnitBox[] getTargetBoxesArray(List<? extends Unit> targets, Function<Unit, UnitBox> stmtBoxWrap) {
        UnitBox[] targetBoxes = new UnitBox[targets.size()];
        ListIterator<? extends Unit> it = targets.listIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            targetBoxes[it.previousIndex()] = stmtBoxWrap.apply(u);
        }
        return targetBoxes;
    }

    @Override // soot.jimple.SwitchStmt
    public final Unit getDefaultTarget() {
        return this.defaultTargetBox.getUnit();
    }

    @Override // soot.jimple.SwitchStmt
    public final void setDefaultTarget(Unit defaultTarget) {
        this.defaultTargetBox.setUnit(defaultTarget);
    }

    @Override // soot.jimple.SwitchStmt
    public final UnitBox getDefaultTargetBox() {
        return this.defaultTargetBox;
    }

    @Override // soot.jimple.SwitchStmt
    public final Value getKey() {
        return this.keyBox.getValue();
    }

    @Override // soot.jimple.SwitchStmt
    public final void setKey(Value key) {
        this.keyBox.setValue(key);
    }

    @Override // soot.jimple.SwitchStmt
    public final ValueBox getKeyBox() {
        return this.keyBox;
    }

    @Override // soot.AbstractUnit, soot.Unit
    public final List<ValueBox> getUseBoxes() {
        List<ValueBox> list = new ArrayList<>(this.keyBox.getValue().getUseBoxes());
        list.add(this.keyBox);
        return list;
    }

    public final int getTargetCount() {
        return this.targetBoxes.length;
    }

    @Override // soot.jimple.SwitchStmt
    public final Unit getTarget(int index) {
        return this.targetBoxes[index].getUnit();
    }

    @Override // soot.jimple.SwitchStmt
    public final UnitBox getTargetBox(int index) {
        return this.targetBoxes[index];
    }

    @Override // soot.jimple.SwitchStmt
    public final void setTarget(int index, Unit target) {
        this.targetBoxes[index].setUnit(target);
    }

    @Override // soot.jimple.SwitchStmt
    public final List<Unit> getTargets() {
        UnitBox[] boxes = this.targetBoxes;
        List<Unit> targets = new ArrayList<>(boxes.length);
        for (UnitBox element : boxes) {
            targets.add(element.getUnit());
        }
        return targets;
    }

    public final void setTargets(List<? extends Unit> targets) {
        ListIterator<? extends Unit> it = targets.listIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            this.targetBoxes[it.previousIndex()].setUnit(u);
        }
    }

    public final void setTargets(Unit[] targets) {
        int e = targets.length;
        for (int i = 0; i < e; i++) {
            this.targetBoxes[i].setUnit(targets[i]);
        }
    }

    @Override // soot.AbstractUnit, soot.Unit
    public final List<UnitBox> getUnitBoxes() {
        return this.stmtBoxes;
    }

    @Override // soot.Unit
    public final boolean fallsThrough() {
        return false;
    }

    @Override // soot.Unit
    public final boolean branches() {
        return true;
    }
}
