package soot.dexpler;

import java.util.Iterator;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.Value;
import soot.ValueBox;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.Stmt;
import soot.options.Options;
import soot.toolkits.scalar.UnusedLocalEliminator;
/* loaded from: gencallgraphv3.jar:soot/dexpler/DexArrayInitReducer.class */
public class DexArrayInitReducer extends BodyTransformer {
    private static final Logger logger = LoggerFactory.getLogger(DexArrayInitReducer.class);

    public static DexArrayInitReducer v() {
        return new DexArrayInitReducer();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        if (!b.getTraps().isEmpty()) {
            return;
        }
        Unit u1 = null;
        Unit u2 = null;
        Iterator<Unit> uIt = b.getUnits().snapshotIterator();
        while (uIt.hasNext()) {
            Unit u = uIt.next();
            if (!(u instanceof AssignStmt) || !((Stmt) u).getBoxesPointingToThis().isEmpty()) {
                u1 = null;
                u2 = null;
            } else {
                AssignStmt assignStmt = (AssignStmt) u;
                if (assignStmt.getLeftOp() instanceof ArrayRef) {
                    if (u1 != null && u2 != null && u2.getBoxesPointingToThis().isEmpty() && assignStmt.getBoxesPointingToThis().isEmpty()) {
                        ArrayRef arrayRef = (ArrayRef) assignStmt.getLeftOp();
                        Value u1val = u1.getDefBoxes().get(0).getValue();
                        Value u2val = u2.getDefBoxes().get(0).getValue();
                        if (arrayRef.getIndex() == u1val) {
                            arrayRef.setIndex(((AssignStmt) u1).getRightOp());
                        } else if (arrayRef.getIndex() == u2val) {
                            arrayRef.setIndex(((AssignStmt) u2).getRightOp());
                        }
                        if (assignStmt.getRightOp() == u1val) {
                            assignStmt.setRightOp(((AssignStmt) u1).getRightOp());
                        } else if (assignStmt.getRightOp() == u2val) {
                            assignStmt.setRightOp(((AssignStmt) u2).getRightOp());
                        }
                        Iterator<Unit> checkIt = b.getUnits().iterator((UnitPatchingChain) u);
                        boolean foundU1 = false;
                        boolean foundU2 = false;
                        boolean doneU1 = false;
                        boolean doneU2 = false;
                        while (true) {
                            if ((doneU1 && doneU2) || ((foundU1 && foundU2) || !checkIt.hasNext())) {
                                break;
                            }
                            Unit checkU = checkIt.next();
                            for (ValueBox vb : checkU.getUseBoxes()) {
                                if (!doneU1 && vb.getValue() == u1val) {
                                    foundU1 = true;
                                }
                                if (!doneU2 && vb.getValue() == u2val) {
                                    foundU2 = true;
                                }
                            }
                            for (ValueBox vb2 : checkU.getDefBoxes()) {
                                if (vb2.getValue() == u1val) {
                                    doneU1 = true;
                                } else if (vb2.getValue() == u2val) {
                                    doneU2 = true;
                                }
                            }
                            if (checkU.branches()) {
                                foundU1 = true;
                                foundU2 = true;
                                break;
                            }
                        }
                        if (!foundU1 && (u1val instanceof Local)) {
                            b.getUnits().remove(u1);
                            if (Options.v().verbose()) {
                                logger.debug("[" + b.getMethod().getName() + "]    remove 1 " + u1);
                            }
                        }
                        if (!foundU2 && (u2val instanceof Local)) {
                            b.getUnits().remove(u2);
                            if (Options.v().verbose()) {
                                logger.debug("[" + b.getMethod().getName() + "]    remove 2 " + u2);
                            }
                        }
                        u1 = null;
                        u2 = null;
                    } else {
                        u1 = null;
                        u2 = null;
                    }
                }
                if (!(assignStmt.getRightOp() instanceof Constant)) {
                    u1 = null;
                    u2 = null;
                } else if (u1 == null) {
                    u1 = assignStmt;
                } else if (u2 == null) {
                    u2 = assignStmt;
                    if (u1 != null) {
                        Value op1 = ((AssignStmt) u1).getLeftOp();
                        if (op1 == ((AssignStmt) u2).getLeftOp()) {
                            u1 = u2;
                            u2 = null;
                        }
                    }
                } else {
                    u1 = u2;
                    u2 = assignStmt;
                }
            }
        }
        UnusedLocalEliminator.v().transform(b);
    }
}
