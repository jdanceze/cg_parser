package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexReturnPacker.class */
public class DexReturnPacker extends BodyTransformer {
    public static DexReturnPacker v() {
        return new DexReturnPacker();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Unit lastUnit = null;
        Iterator<Unit> unitIt = b.getUnits().iterator();
        while (unitIt.hasNext()) {
            Unit curUnit = unitIt.next();
            if ((curUnit instanceof ReturnStmt) || (curUnit instanceof ReturnVoidStmt)) {
                if (lastUnit != null && isEqual(lastUnit, curUnit)) {
                    curUnit.redirectJumpsToThisTo(lastUnit);
                    unitIt.remove();
                } else {
                    lastUnit = curUnit;
                }
            } else {
                lastUnit = null;
            }
        }
    }

    private boolean isEqual(Unit unit1, Unit unit2) {
        if (unit1 == unit2 || unit1.equals(unit2)) {
            return true;
        }
        if (unit1.getClass() == unit2.getClass()) {
            if (unit1 instanceof ReturnVoidStmt) {
                return true;
            }
            return (unit1 instanceof ReturnStmt) && ((ReturnStmt) unit1).getOp() == ((ReturnStmt) unit2).getOp();
        }
        return false;
    }
}
