package soot.dexpler;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexReturnInliner.class */
public class DexReturnInliner extends DexTransformer {
    public static DexReturnInliner v() {
        return new DexReturnInliner();
    }

    private boolean isInstanceofReturn(Unit u) {
        if ((u instanceof ReturnStmt) || (u instanceof ReturnVoidStmt)) {
            return true;
        }
        return false;
    }

    private boolean isInstanceofFlowChange(Unit u) {
        if ((u instanceof GotoStmt) || isInstanceofReturn(u)) {
            return true;
        }
        return false;
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        boolean mayBeMore;
        Set<Unit> duplicateIfTargets = getFallThroughReturns(body);
        Iterator<Unit> it = body.getUnits().snapshotIterator();
        Unit last = null;
        do {
            mayBeMore = false;
            while (it.hasNext()) {
                Unit u = it.next();
                if (u instanceof GotoStmt) {
                    GotoStmt gtStmt = (GotoStmt) u;
                    if (isInstanceofReturn(gtStmt.getTarget())) {
                        Stmt stmt = (Stmt) gtStmt.getTarget().clone();
                        for (Trap t : body.getTraps()) {
                            for (UnitBox ubox : t.getUnitBoxes()) {
                                if (ubox.getUnit() == u) {
                                    ubox.setUnit(stmt);
                                }
                            }
                        }
                        while (!u.getBoxesPointingToThis().isEmpty()) {
                            u.getBoxesPointingToThis().get(0).setUnit(stmt);
                        }
                        stmt.addAllTagsOf(u);
                        body.getUnits().swapWith(u, (Unit) stmt);
                        mayBeMore = true;
                    }
                } else if (u instanceof IfStmt) {
                    IfStmt ifstmt = (IfStmt) u;
                    Unit t2 = ifstmt.getTarget();
                    if (isInstanceofReturn(t2)) {
                        if (duplicateIfTargets == null) {
                            duplicateIfTargets = new HashSet<>();
                        }
                        if (!duplicateIfTargets.add(t2)) {
                            Unit newTarget = (Unit) t2.clone();
                            body.getUnits().addLast((UnitPatchingChain) newTarget);
                            ifstmt.setTarget(newTarget);
                        }
                    }
                } else if (isInstanceofReturn(u) && last != null) {
                    u.removeAllTags();
                    u.addAllTagsOf(last);
                }
                last = u;
            }
        } while (mayBeMore);
    }

    private Set<Unit> getFallThroughReturns(Body body) {
        Set<Unit> fallThroughReturns = null;
        Unit lastUnit = null;
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (lastUnit != null && isInstanceofReturn(u) && !isInstanceofFlowChange(lastUnit)) {
                if (fallThroughReturns == null) {
                    fallThroughReturns = new HashSet<>();
                }
                fallThroughReturns.add(u);
            }
            lastUnit = u;
        }
        return fallThroughReturns;
    }
}
