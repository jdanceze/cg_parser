package soot.asm;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Trap;
import soot.Unit;
import soot.UnitBox;
import soot.UnitPatchingChain;
import soot.jimple.AssignStmt;
import soot.jimple.CastExpr;
import soot.jimple.GotoStmt;
import soot.jimple.Jimple;
import soot.jimple.ReturnStmt;
/* loaded from: gencallgraphv3.jar:soot/asm/CastAndReturnInliner.class */
public class CastAndReturnInliner extends BodyTransformer {
    @Override // soot.BodyTransformer
    protected void internalTransform(Body body, String phaseName, Map<String, String> options) {
        UnitPatchingChain units = body.getUnits();
        Iterator<Unit> it = units.snapshotIterator();
        while (it.hasNext()) {
            Unit u = it.next();
            if (u instanceof GotoStmt) {
                GotoStmt gtStmt = (GotoStmt) u;
                if (gtStmt.getTarget() instanceof AssignStmt) {
                    AssignStmt assign = (AssignStmt) gtStmt.getTarget();
                    if (assign.getRightOp() instanceof CastExpr) {
                        CastExpr ce = (CastExpr) assign.getRightOp();
                        Unit nextStmt = units.getSuccOf((UnitPatchingChain) assign);
                        if (nextStmt instanceof ReturnStmt) {
                            ReturnStmt retStmt = (ReturnStmt) nextStmt;
                            if (retStmt.getOp() == assign.getLeftOp()) {
                                ReturnStmt newStmt = (ReturnStmt) retStmt.clone();
                                Local a = (Local) ce.getOp();
                                for (Trap t : body.getTraps()) {
                                    for (UnitBox ubox : t.getUnitBoxes()) {
                                        if (ubox.getUnit() == gtStmt) {
                                            ubox.setUnit(newStmt);
                                        }
                                    }
                                }
                                Jimple j = Jimple.v();
                                Local n = j.newLocal(String.valueOf(a.getName()) + "_ret", ce.getCastType());
                                body.getLocals().add(n);
                                newStmt.setOp(n);
                                List<UnitBox> boxesRefGtStmt = gtStmt.getBoxesPointingToThis();
                                while (!boxesRefGtStmt.isEmpty()) {
                                    boxesRefGtStmt.get(0).setUnit(newStmt);
                                }
                                units.swapWith(gtStmt, (GotoStmt) newStmt);
                                units.insertBefore(j.newAssignStmt(n, (CastExpr) ce.clone()), (AssignStmt) newStmt);
                            }
                        }
                    }
                }
            }
        }
    }
}
