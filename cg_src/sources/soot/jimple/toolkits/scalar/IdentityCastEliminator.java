package soot.jimple.toolkits.scalar;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Local;
import soot.Singletons;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/IdentityCastEliminator.class */
public class IdentityCastEliminator extends BodyTransformer {
    public IdentityCastEliminator(Singletons.Global g) {
    }

    public static IdentityCastEliminator v() {
        return G.v().soot_jimple_toolkits_scalar_IdentityCastEliminator();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Unit> unitIt = b.getUnits().iterator();
        while (unitIt.hasNext()) {
            Unit curUnit = unitIt.next();
            if (curUnit instanceof AssignStmt) {
                AssignStmt assignStmt = (AssignStmt) curUnit;
                Value leftOp = assignStmt.getLeftOp();
                Value rightOp = assignStmt.getRightOp();
                if ((leftOp instanceof Local) && (rightOp instanceof CastExpr)) {
                    CastExpr ce = (CastExpr) rightOp;
                    Value castOp = ce.getOp();
                    if (castOp.getType() == ce.getCastType()) {
                        if (leftOp == castOp) {
                            unitIt.remove();
                        } else {
                            assignStmt.setRightOp(castOp);
                        }
                    }
                }
            }
        }
    }
}
