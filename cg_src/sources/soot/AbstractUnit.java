package soot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import soot.tagkit.AbstractHost;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/AbstractUnit.class */
public abstract class AbstractUnit extends AbstractHost implements Unit {
    protected List<UnitBox> boxesPointingToThis = null;

    @Override // soot.Unit
    public abstract Object clone();

    @Override // soot.Unit
    public List<ValueBox> getUseBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Unit
    public List<ValueBox> getDefBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Unit
    public List<UnitBox> getUnitBoxes() {
        return Collections.emptyList();
    }

    @Override // soot.Unit
    public List<UnitBox> getBoxesPointingToThis() {
        List<UnitBox> ref = this.boxesPointingToThis;
        return ref == null ? Collections.emptyList() : Collections.unmodifiableList(ref);
    }

    @Override // soot.Unit
    public void addBoxPointingToThis(UnitBox b) {
        List<UnitBox> ref = this.boxesPointingToThis;
        if (ref == null) {
            List<UnitBox> arrayList = new ArrayList<>();
            ref = arrayList;
            this.boxesPointingToThis = arrayList;
        }
        ref.add(b);
    }

    @Override // soot.Unit
    public void removeBoxPointingToThis(UnitBox b) {
        List<UnitBox> ref = this.boxesPointingToThis;
        if (ref != null) {
            ref.remove(b);
        }
    }

    @Override // soot.Unit
    public void clearUnitBoxes() {
        for (UnitBox ub : getUnitBoxes()) {
            ub.setUnit(null);
        }
    }

    @Override // soot.Unit
    public List<ValueBox> getUseAndDefBoxes() {
        List<ValueBox> useBoxes = getUseBoxes();
        List<ValueBox> defBoxes = getDefBoxes();
        if (useBoxes.isEmpty()) {
            return defBoxes;
        }
        if (defBoxes.isEmpty()) {
            return useBoxes;
        }
        List<ValueBox> valueBoxes = new ArrayList<>(defBoxes.size() + useBoxes.size());
        valueBoxes.addAll(defBoxes);
        valueBoxes.addAll(useBoxes);
        return valueBoxes;
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
    }

    @Override // soot.Unit
    public void redirectJumpsToThisTo(Unit newLocation) {
        Iterator it = new ArrayList(getBoxesPointingToThis()).iterator();
        while (it.hasNext()) {
            UnitBox box = (UnitBox) it.next();
            if (box.getUnit() != this) {
                throw new RuntimeException("Something weird's happening");
            }
            if (box.isBranchTarget()) {
                box.setUnit(newLocation);
            }
        }
    }
}
