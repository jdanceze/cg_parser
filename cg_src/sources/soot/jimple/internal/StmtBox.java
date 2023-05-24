package soot.jimple.internal;

import soot.AbstractUnitBox;
import soot.Unit;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/StmtBox.class */
public class StmtBox extends AbstractUnitBox {
    public StmtBox(Stmt s) {
        setUnit(s);
    }

    @Override // soot.UnitBox
    public boolean canContainUnit(Unit u) {
        return u == null || (u instanceof Stmt);
    }
}
