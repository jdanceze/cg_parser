package soot.asm;

import java.util.List;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPrinter;
import soot.ValueBox;
import soot.tagkit.Host;
import soot.tagkit.Tag;
import soot.util.Switch;
/* loaded from: gencallgraphv3.jar:soot/asm/UnitContainer.class */
class UnitContainer implements Unit {
    final Unit[] units;

    /* JADX INFO: Access modifiers changed from: package-private */
    public UnitContainer(Unit... units) {
        this.units = units;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Unit getFirstUnit() {
        Unit unit = this.units[0];
        while (true) {
            Unit ret = unit;
            if (ret instanceof UnitContainer) {
                unit = ((UnitContainer) ret).units[0];
            } else {
                return ret;
            }
        }
    }

    @Override // soot.Unit
    public Object clone() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.util.Switchable
    public void apply(Switch sw) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public List<Tag> getTags() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public Tag getTag(String aName) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public void addTag(Tag t) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public void removeTag(String name) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public boolean hasTag(String aName) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public void removeAllTags() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public void addAllTagsOf(Host h) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public List<ValueBox> getUseBoxes() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public List<ValueBox> getDefBoxes() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public List<UnitBox> getUnitBoxes() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public List<UnitBox> getBoxesPointingToThis() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public void addBoxPointingToThis(UnitBox b) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public void removeBoxPointingToThis(UnitBox b) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public void clearUnitBoxes() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public List<ValueBox> getUseAndDefBoxes() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public boolean fallsThrough() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public boolean branches() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public void toString(UnitPrinter up) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.Unit
    public void redirectJumpsToThisTo(Unit newLocation) {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public int getJavaSourceStartLineNumber() {
        throw new UnsupportedOperationException();
    }

    @Override // soot.tagkit.Host
    public int getJavaSourceStartColumnNumber() {
        throw new UnsupportedOperationException();
    }
}
