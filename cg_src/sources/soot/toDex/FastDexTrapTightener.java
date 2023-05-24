package soot.toDex;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.IdentityStmt;
import soot.jimple.ParameterRef;
import soot.jimple.ThisRef;
/* loaded from: gencallgraphv3.jar:soot/toDex/FastDexTrapTightener.class */
public class FastDexTrapTightener extends BodyTransformer {
    public FastDexTrapTightener(Singletons.Global g) {
    }

    public static FastDexTrapTightener v() {
        return G.v().soot_toDex_FastDexTrapTightener();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Trap> trapIt = b.getTraps().snapshotIterator();
        while (trapIt.hasNext()) {
            Trap t = trapIt.next();
            while (true) {
                Unit beginUnit = t.getBeginUnit();
                if (isDexInstruction(beginUnit) || t.getBeginUnit() == t.getEndUnit()) {
                    break;
                }
                t.setBeginUnit(b.getUnits().getSuccOf((UnitPatchingChain) beginUnit));
            }
            if (t.getBeginUnit() == t.getEndUnit()) {
                trapIt.remove();
            }
        }
    }

    private boolean isDexInstruction(Unit unit) {
        if (unit instanceof IdentityStmt) {
            IdentityStmt is = (IdentityStmt) unit;
            return ((is.getRightOp() instanceof ThisRef) || (is.getRightOp() instanceof ParameterRef)) ? false : true;
        }
        return true;
    }
}
