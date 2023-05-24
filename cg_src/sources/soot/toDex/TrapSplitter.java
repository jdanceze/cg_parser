package soot.toDex;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.G;
import soot.Singletons;
import soot.Trap;
import soot.Unit;
import soot.UnitPatchingChain;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.toolkits.scalar.UnreachableCodeEliminator;
/* loaded from: gencallgraphv3.jar:soot/toDex/TrapSplitter.class */
public class TrapSplitter extends BodyTransformer {
    public TrapSplitter(Singletons.Global g) {
    }

    public static TrapSplitter v() {
        return G.v().soot_toDex_TrapSplitter();
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:soot/toDex/TrapSplitter$TrapOverlap.class */
    public class TrapOverlap {
        private Trap t1;
        private Trap t2;
        private Unit t2Start;

        public TrapOverlap(Trap t1, Trap t2, Unit t2Start) {
            this.t1 = t1;
            this.t2 = t2;
            this.t2Start = t2Start;
        }
    }

    @Override // soot.BodyTransformer
    protected void internalTransform(Body b, String phaseName, Map<String, String> options) {
        Unit firstEndUnit;
        if (b.getTraps().size() < 2) {
            return;
        }
        Set<Unit> potentiallyUselessTrapHandlers = null;
        while (true) {
            TrapOverlap to = getNextOverlap(b);
            if (to != null) {
                if (to.t1.getBeginUnit() == to.t1.getEndUnit()) {
                    b.getTraps().remove(to.t1);
                    if (potentiallyUselessTrapHandlers == null) {
                        potentiallyUselessTrapHandlers = new HashSet<>();
                    }
                    potentiallyUselessTrapHandlers.add(to.t1.getHandlerUnit());
                } else if (to.t2.getBeginUnit() == to.t2.getEndUnit()) {
                    b.getTraps().remove(to.t2);
                    if (potentiallyUselessTrapHandlers == null) {
                        potentiallyUselessTrapHandlers = new HashSet<>();
                    }
                    potentiallyUselessTrapHandlers.add(to.t2.getHandlerUnit());
                } else if (to.t1.getBeginUnit() != to.t2Start) {
                    Trap newTrap = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), to.t2Start, to.t1.getHandlerUnit());
                    safeAddTrap(b, newTrap, to.t1);
                    to.t1.setBeginUnit(to.t2Start);
                } else if (to.t1.getBeginUnit() == to.t2.getBeginUnit()) {
                    Unit beginUnit = to.t1.getBeginUnit();
                    while (true) {
                        firstEndUnit = beginUnit;
                        if (firstEndUnit == to.t1.getEndUnit() || firstEndUnit == to.t2.getEndUnit()) {
                            break;
                        }
                        beginUnit = b.getUnits().getSuccOf((UnitPatchingChain) firstEndUnit);
                    }
                    if (firstEndUnit == to.t1.getEndUnit()) {
                        if (to.t1.getException() != to.t2.getException()) {
                            Trap newTrap2 = Jimple.v().newTrap(to.t2.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t2.getHandlerUnit());
                            safeAddTrap(b, newTrap2, to.t2);
                        } else if (to.t1.getHandlerUnit() != to.t2.getHandlerUnit()) {
                            Trap newTrap3 = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t1.getHandlerUnit());
                            safeAddTrap(b, newTrap3, to.t1);
                        }
                        to.t2.setBeginUnit(firstEndUnit);
                    } else if (firstEndUnit == to.t2.getEndUnit()) {
                        if (to.t1.getException() != to.t2.getException()) {
                            Trap newTrap22 = Jimple.v().newTrap(to.t1.getException(), to.t1.getBeginUnit(), firstEndUnit, to.t1.getHandlerUnit());
                            safeAddTrap(b, newTrap22, to.t1);
                            to.t1.setBeginUnit(firstEndUnit);
                        } else if (to.t1.getHandlerUnit() == to.t2.getHandlerUnit()) {
                            to.t1.setBeginUnit(firstEndUnit);
                        } else {
                            b.getTraps().remove(to.t2);
                            if (potentiallyUselessTrapHandlers == null) {
                                potentiallyUselessTrapHandlers = new HashSet<>();
                            }
                            potentiallyUselessTrapHandlers.add(to.t2.getHandlerUnit());
                        }
                    }
                }
            } else {
                removePotentiallyUselassTraps(b, potentiallyUselessTrapHandlers);
                return;
            }
        }
    }

    public static void removePotentiallyUselassTraps(Body b, Set<Unit> potentiallyUselessTrapHandlers) {
        if (potentiallyUselessTrapHandlers == null) {
            return;
        }
        for (Trap t : b.getTraps()) {
            potentiallyUselessTrapHandlers.remove(t.getHandlerUnit());
        }
        boolean removedUselessTrap = false;
        for (Unit uselessTrapHandler : potentiallyUselessTrapHandlers) {
            if (uselessTrapHandler instanceof IdentityStmt) {
                IdentityStmt assign = (IdentityStmt) uselessTrapHandler;
                if (assign.getRightOp() instanceof CaughtExceptionRef) {
                    b.getUnits().swapWith(assign, (IdentityStmt) Jimple.v().newAssignStmt(assign.getLeftOp(), NullConstant.v()));
                    removedUselessTrap = true;
                }
            }
        }
        if (removedUselessTrap) {
            UnreachableCodeEliminator.v().transform(b);
        }
    }

    private void safeAddTrap(Body b, Trap newTrap, Trap position) {
        if (newTrap.getBeginUnit() != newTrap.getEndUnit()) {
            if (position != null) {
                b.getTraps().insertAfter(newTrap, position);
            } else {
                b.getTraps().add(newTrap);
            }
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x007c, code lost:
        if (r12 != r0.getBeginUnit()) goto L24;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x008c, code lost:
        return new soot.toDex.TrapSplitter.TrapOverlap(r7, r0, r0, r12);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x009a, code lost:
        return new soot.toDex.TrapSplitter.TrapOverlap(r7, r0, r0, r12);
     */
    /* JADX WARN: Removed duplicated region for block: B:7:0x0048  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    private soot.toDex.TrapSplitter.TrapOverlap getNextOverlap(soot.Body r8) {
        /*
            r7 = this;
            java.util.HashMap r0 = new java.util.HashMap
            r1 = r0
            r1.<init>()
            r9 = r0
            r0 = r8
            soot.util.Chain r0 = r0.getTraps()
            java.util.Iterator r0 = r0.iterator()
            r11 = r0
            goto Le5
        L16:
            r0 = r11
            java.lang.Object r0 = r0.next()
            soot.Trap r0 = (soot.Trap) r0
            r10 = r0
            r0 = r10
            soot.Unit r0 = r0.getBeginUnit()
            r12 = r0
            goto Ld5
        L2c:
            r0 = r9
            r1 = r12
            java.lang.Object r0 = r0.get(r1)
            java.util.LinkedHashSet r0 = (java.util.LinkedHashSet) r0
            r13 = r0
            r0 = r13
            if (r0 == 0) goto Laf
            r0 = r13
            java.util.Iterator r0 = r0.iterator()
            r15 = r0
            goto L9b
        L48:
            r0 = r15
            java.lang.Object r0 = r0.next()
            soot.Trap r0 = (soot.Trap) r0
            r14 = r0
            r0 = r14
            soot.Unit r0 = r0.getEndUnit()
            r1 = r10
            soot.Unit r1 = r1.getEndUnit()
            if (r0 != r1) goto L74
            r0 = r14
            soot.SootClass r0 = r0.getException()
            r1 = r10
            soot.SootClass r1 = r1.getException()
            if (r0 != r1) goto L9b
        L74:
            r0 = r12
            r1 = r10
            soot.Unit r1 = r1.getBeginUnit()
            if (r0 != r1) goto L8d
            soot.toDex.TrapSplitter$TrapOverlap r0 = new soot.toDex.TrapSplitter$TrapOverlap
            r1 = r0
            r2 = r7
            r3 = r14
            r4 = r10
            r5 = r12
            r1.<init>(r3, r4, r5)
            return r0
        L8d:
            soot.toDex.TrapSplitter$TrapOverlap r0 = new soot.toDex.TrapSplitter$TrapOverlap
            r1 = r0
            r2 = r7
            r3 = r10
            r4 = r14
            r5 = r12
            r1.<init>(r3, r4, r5)
            return r0
        L9b:
            r0 = r15
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L48
            r0 = r13
            r1 = r10
            boolean r0 = r0.add(r1)
            goto Lca
        Laf:
            java.util.LinkedHashSet r0 = new java.util.LinkedHashSet
            r1 = r0
            r1.<init>()
            r14 = r0
            r0 = r14
            r1 = r10
            boolean r0 = r0.add(r1)
            r0 = r9
            r1 = r12
            r2 = r14
            java.lang.Object r0 = r0.put(r1, r2)
        Lca:
            r0 = r8
            soot.UnitPatchingChain r0 = r0.getUnits()
            r1 = r12
            soot.Unit r0 = r0.getSuccOf(r1)
            r12 = r0
        Ld5:
            r0 = r12
            r1 = r10
            soot.Unit r1 = r1.getEndUnit()
            if (r0 == r1) goto Le5
            r0 = r12
            if (r0 != 0) goto L2c
        Le5:
            r0 = r11
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L16
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.toDex.TrapSplitter.getNextOverlap(soot.Body):soot.toDex.TrapSplitter$TrapOverlap");
    }
}
