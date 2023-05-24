package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexJumpChainShortener.class */
public class DexJumpChainShortener extends BodyTransformer {
    public static DexJumpChainShortener v() {
        return new DexJumpChainShortener();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Unit> unitIt = b.getUnits().snapshotIterator();
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u instanceof GotoStmt) {
                GotoStmt stmt = (GotoStmt) u;
                while (stmt.getTarget() instanceof GotoStmt) {
                    GotoStmt nextTarget = (GotoStmt) stmt.getTarget();
                    stmt.setTarget(nextTarget.getTarget());
                }
            } else if (u instanceof IfStmt) {
                IfStmt stmt2 = (IfStmt) u;
                while (stmt2.getTarget() instanceof GotoStmt) {
                    GotoStmt nextTarget2 = (GotoStmt) stmt2.getTarget();
                    stmt2.setTarget(nextTarget2.getTarget());
                }
            }
        }
    }
}
