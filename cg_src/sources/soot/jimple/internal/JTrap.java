package soot.jimple.internal;

import soot.AbstractTrap;
import soot.SootClass;
import soot.Unit;
import soot.UnitBox;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/jimple/internal/JTrap.class */
public class JTrap extends AbstractTrap {
    public JTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
        super(exception, Jimple.v().newStmtBox(beginStmt), Jimple.v().newStmtBox(endStmt), Jimple.v().newStmtBox(handlerStmt));
    }

    public JTrap(SootClass exception, UnitBox beginStmt, UnitBox endStmt, UnitBox handlerStmt) {
        super(exception, beginStmt, endStmt, handlerStmt);
    }

    @Override // soot.AbstractTrap, soot.Trap
    public Object clone() {
        return new JTrap(this.exception, getBeginUnit(), getEndUnit(), getHandlerUnit());
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("Trap :");
        buf.append("\nbegin  : ").append(getBeginUnit());
        buf.append("\nend    : ").append(getEndUnit());
        buf.append("\nhandler: ").append(getHandlerUnit());
        return buf.toString();
    }
}
