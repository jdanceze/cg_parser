package soot.dexpler;

import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Scene;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexTrapStackFixer.class */
public class DexTrapStackFixer extends BodyTransformer {
    public static DexTrapStackFixer v() {
        return new DexTrapStackFixer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        for (Trap t : b.getTraps()) {
            if (!isCaughtExceptionRef(t.getHandlerUnit())) {
                Local l = Scene.v().createLocalGenerator(b).generateLocal(t.getException().getType());
                IdentityStmt newIdentityStmt = Jimple.v().newIdentityStmt(l, Jimple.v().newCaughtExceptionRef());
                b.getUnits().add((UnitPatchingChain) newIdentityStmt);
                b.getUnits().add((UnitPatchingChain) Jimple.v().newGotoStmt(t.getHandlerUnit()));
                t.setHandlerUnit(newIdentityStmt);
            }
        }
    }

    private boolean isCaughtExceptionRef(Unit handlerUnit) {
        if (!(handlerUnit instanceof IdentityStmt)) {
            return false;
        }
        IdentityStmt stmt = (IdentityStmt) handlerUnit;
        return stmt.getRightOp() instanceof CaughtExceptionRef;
    }
}
