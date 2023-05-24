package soot.baf.internal;

import soot.AbstractTrap;
import soot.SootClass;
import soot.Unit;
import soot.baf.Baf;
/* loaded from: gencallgraphv3.jar:soot/baf/internal/BTrap.class */
public class BTrap extends AbstractTrap {
    public BTrap(SootClass exception, Unit beginStmt, Unit endStmt, Unit handlerStmt) {
        super(exception, Baf.v().newInstBox(beginStmt), Baf.v().newInstBox(endStmt), Baf.v().newInstBox(handlerStmt));
    }

    @Override // soot.AbstractTrap, soot.Trap
    public Object clone() {
        return new BTrap(this.exception, getBeginUnit(), getEndUnit(), getHandlerUnit());
    }
}
