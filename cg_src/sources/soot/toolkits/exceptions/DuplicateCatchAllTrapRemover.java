package soot.toolkits.exceptions;

import java.util.Iterator;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Scene;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/toolkits/exceptions/DuplicateCatchAllTrapRemover.class */
public class DuplicateCatchAllTrapRemover extends BodyTransformer {
    public DuplicateCatchAllTrapRemover(Singletons.Global g) {
    }

    public static DuplicateCatchAllTrapRemover v() {
        return G.v().soot_toolkits_exceptions_DuplicateCatchAllTrapRemover();
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Iterator<Trap> t1It = b.getTraps().snapshotIterator();
        while (t1It.hasNext()) {
            Trap t1 = t1It.next();
            if (t1.getException().getName().equals(Scene.v().getBaseExceptionType().toString())) {
                Iterator<Trap> t2It = b.getTraps().snapshotIterator();
                while (t2It.hasNext()) {
                    Trap t2 = t2It.next();
                    if (t1 != t2 && t1.getBeginUnit() == t2.getBeginUnit() && t1.getEndUnit() == t2.getEndUnit() && t2.getException().getName().equals(Scene.v().getBaseExceptionType().toString())) {
                        Iterator<Trap> it = b.getTraps().iterator();
                        while (true) {
                            if (it.hasNext()) {
                                Trap t3 = it.next();
                                if (t3 != t1 && t3 != t2 && t3.getException().getName().equals(Scene.v().getBaseExceptionType().toString())) {
                                    if (trapCoversUnit(b, t3, t1.getHandlerUnit()) && t3.getHandlerUnit() == t2.getHandlerUnit()) {
                                        b.getTraps().remove(t2);
                                        break;
                                    } else if (trapCoversUnit(b, t3, t2.getHandlerUnit()) && t3.getHandlerUnit() == t1.getHandlerUnit()) {
                                        b.getTraps().remove(t1);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean trapCoversUnit(Body b, Trap trap, Unit unit) {
        Iterator<Unit> unitIt = b.getUnits().iterator(trap.getBeginUnit(), trap.getEndUnit());
        while (unitIt.hasNext()) {
            Unit u = unitIt.next();
            if (u == unit) {
                return true;
            }
        }
        return false;
    }
}
