package soot.jimple.toolkits.typing;

import soot.Body;
import soot.Unit;
import soot.jimple.IdentityStmt;
import soot.jimple.Stmt;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/typing/Util.class */
public class Util {
    /* JADX WARN: Multi-variable type inference failed */
    public static Unit findLastIdentityUnit(Body b, Stmt s) {
        Chain<Unit> units = b.getUnits();
        Unit u2 = s;
        Unit unit = u2;
        while (true) {
            Unit u1 = unit;
            if (u1 instanceof IdentityStmt) {
                u2 = u1;
                unit = units.getSuccOf(u1);
            } else {
                return u2;
            }
        }
    }

    public static Unit findFirstNonIdentityUnit(Body b, Stmt s) {
        Chain<Unit> units = b.getUnits();
        Unit unit = s;
        while (true) {
            Unit u1 = unit;
            if (u1 instanceof IdentityStmt) {
                unit = units.getSuccOf(u1);
            } else {
                return u1;
            }
        }
    }

    private Util() {
    }
}
