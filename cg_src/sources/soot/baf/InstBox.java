package soot.baf;

import soot.AbstractUnitBox;
import soot.Unit;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:soot/baf/InstBox.class */
public class InstBox extends AbstractUnitBox {
    /* JADX INFO: Access modifiers changed from: package-private */
    public InstBox(Inst s) {
        setUnit(s);
    }

    @Override // soot.UnitBox
    public boolean canContainUnit(Unit u) {
        return u == null || (u instanceof Inst);
    }
}
