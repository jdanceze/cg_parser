package soot;

import com.google.common.base.Optional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/TrapManager.class */
public class TrapManager {
    public static boolean isExceptionCaughtAt(SootClass e, Unit u, Body b) {
        Hierarchy h = Scene.v().getActiveHierarchy();
        Chain<Unit> units = b.getUnits();
        for (Trap t : b.getTraps()) {
            if (h.isClassSubclassOfIncluding(e, t.getException())) {
                Iterator<Unit> it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));
                while (it.hasNext()) {
                    if (u.equals(it.next())) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }

    public static List<Trap> getTrapsAt(Unit unit, Body b) {
        Chain<Unit> units = b.getUnits();
        List<Trap> trapsList = new ArrayList<>();
        for (Trap t : b.getTraps()) {
            Iterator<Unit> it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));
            while (it.hasNext()) {
                if (unit.equals(it.next())) {
                    trapsList.add(t);
                }
            }
        }
        return trapsList;
    }

    public static Set<Unit> getTrappedUnitsOf(Body b) {
        Chain<Unit> units = b.getUnits();
        Set<Unit> trapsSet = new HashSet<>();
        for (Trap t : b.getTraps()) {
            Iterator<Unit> it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));
            while (it.hasNext()) {
                trapsSet.add(it.next());
            }
        }
        return trapsSet;
    }

    /* JADX WARN: Code restructure failed: missing block: B:28:0x00fa, code lost:
        continue;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void splitTrapsAgainst(soot.Body r4, soot.Unit r5, soot.Unit r6) {
        /*
            r0 = r4
            soot.util.Chain r0 = r0.getTraps()
            r7 = r0
            r0 = r4
            soot.UnitPatchingChain r0 = r0.getUnits()
            r8 = r0
            r0 = r7
            java.util.Iterator r0 = r0.snapshotIterator()
            r9 = r0
            goto Lfa
        L16:
            r0 = r9
            java.lang.Object r0 = r0.next()
            soot.Trap r0 = (soot.Trap) r0
            r10 = r0
            r0 = 0
            r11 = r0
            r0 = r8
            r1 = r10
            soot.Unit r1 = r1.getBeginUnit()
            r2 = r10
            soot.Unit r2 = r2.getEndUnit()
            java.util.Iterator r0 = r0.iterator(r1, r2)
            r12 = r0
            goto Lf0
        L3f:
            r0 = r12
            java.lang.Object r0 = r0.next()
            soot.Unit r0 = (soot.Unit) r0
            r13 = r0
            r0 = r5
            r1 = r13
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto L57
            r0 = 1
            r11 = r0
        L57:
            r0 = r12
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L8c
            r0 = r11
            if (r0 == 0) goto Lfa
            r0 = r10
            java.lang.Object r0 = r0.clone()
            soot.Trap r0 = (soot.Trap) r0
            r14 = r0
            r0 = r10
            r1 = r5
            r0.setBeginUnit(r1)
            r0 = r14
            r1 = r5
            r0.setEndUnit(r1)
            r0 = r7
            r1 = r14
            r2 = r10
            r0.insertAfter(r1, r2)
        L8c:
            r0 = r6
            r1 = r13
            boolean r0 = r0.equals(r1)
            if (r0 == 0) goto Lf0
            r0 = r11
            if (r0 != 0) goto La4
            java.lang.RuntimeException r0 = new java.lang.RuntimeException
            r1 = r0
            java.lang.String r2 = "inversed range?"
            r1.<init>(r2)
            throw r0
        La4:
            r0 = r10
            java.lang.Object r0 = r0.clone()
            soot.Trap r0 = (soot.Trap) r0
            r14 = r0
            r0 = r10
            java.lang.Object r0 = r0.clone()
            soot.Trap r0 = (soot.Trap) r0
            r15 = r0
            r0 = r14
            r1 = r5
            r0.setEndUnit(r1)
            r0 = r15
            r1 = r5
            r0.setBeginUnit(r1)
            r0 = r15
            r1 = r6
            r0.setEndUnit(r1)
            r0 = r10
            r1 = r6
            r0.setBeginUnit(r1)
            r0 = r7
            r1 = r14
            r2 = r10
            r0.insertAfter(r1, r2)
            r0 = r7
            r1 = r15
            r2 = r10
            r0.insertAfter(r1, r2)
        Lf0:
            r0 = r12
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L3f
        Lfa:
            r0 = r9
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L16
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: soot.TrapManager.splitTrapsAgainst(soot.Body, soot.Unit, soot.Unit):void");
    }

    public static List<RefType> getExceptionTypesOf(Unit u, Body body) {
        RefType v;
        boolean module_mode = ModuleUtil.module_mode();
        List<RefType> possibleTypes = new ArrayList<>();
        for (Trap trap : body.getTraps()) {
            if (trap.getHandlerUnit() == u) {
                if (module_mode) {
                    v = ModuleRefType.v(trap.getException().getName(), Optional.fromNullable(trap.getException().moduleName));
                } else {
                    v = RefType.v(trap.getException().getName());
                }
                RefType type = v;
                possibleTypes.add(type);
            }
        }
        return possibleTypes;
    }
}
