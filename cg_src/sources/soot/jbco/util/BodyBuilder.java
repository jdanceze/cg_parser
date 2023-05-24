package soot.jbco.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import soot.Local;
import soot.PatchingChain;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Unit;
import soot.baf.IfCmpEqInst;
import soot.baf.IfCmpGeInst;
import soot.baf.IfCmpGtInst;
import soot.baf.IfCmpLeInst;
import soot.baf.IfCmpLtInst;
import soot.baf.IfCmpNeInst;
import soot.baf.IfEqInst;
import soot.baf.IfGeInst;
import soot.baf.IfGtInst;
import soot.baf.IfLeInst;
import soot.baf.IfLtInst;
import soot.baf.IfNeInst;
import soot.baf.IfNonNullInst;
import soot.baf.IfNullInst;
import soot.jimple.Jimple;
import soot.jimple.ThisRef;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jbco/util/BodyBuilder.class */
public class BodyBuilder {
    public static boolean bodiesHaveBeenBuilt = false;
    public static boolean namesHaveBeenRetrieved = false;
    public static List<String> nameList = new ArrayList();

    public static void retrieveAllBodies() {
        if (bodiesHaveBeenBuilt) {
            return;
        }
        for (SootClass c : Scene.v().getApplicationClasses()) {
            for (SootMethod m : c.getMethods()) {
                if (m.isConcrete() && !m.hasActiveBody()) {
                    m.retrieveActiveBody();
                }
            }
        }
        bodiesHaveBeenBuilt = true;
    }

    public static void retrieveAllNames() {
        if (namesHaveBeenRetrieved) {
            return;
        }
        for (SootClass c : Scene.v().getApplicationClasses()) {
            nameList.add(c.getName());
            for (SootMethod m : c.getMethods()) {
                nameList.add(m.getName());
            }
            for (SootField m2 : c.getFields()) {
                nameList.add(m2.getName());
            }
        }
        namesHaveBeenRetrieved = true;
    }

    public static Local buildThisLocal(PatchingChain<Unit> units, ThisRef tr, Collection<Local> locals) {
        Local ths = Jimple.v().newLocal("ths", tr.getType());
        locals.add(ths);
        units.add((PatchingChain<Unit>) Jimple.v().newIdentityStmt(ths, Jimple.v().newThisRef((RefType) tr.getType())));
        return ths;
    }

    public static List<Local> buildParameterLocals(PatchingChain<Unit> units, Collection<Local> locals, List<Type> paramTypes) {
        List<Local> args = new ArrayList<>();
        for (int k = 0; k < paramTypes.size(); k++) {
            Type type = paramTypes.get(k);
            Local loc = Jimple.v().newLocal("l" + k, type);
            locals.add(loc);
            units.add((PatchingChain<Unit>) Jimple.v().newIdentityStmt(loc, Jimple.v().newParameterRef(type, k)));
            args.add(loc);
        }
        return args;
    }

    public static void updateTraps(Unit oldu, Unit newu, Chain<Trap> traps) {
        Trap succOf;
        int size = traps.size();
        if (size == 0) {
            return;
        }
        Trap t = traps.getFirst();
        do {
            if (t.getBeginUnit() == oldu) {
                t.setBeginUnit(newu);
            }
            if (t.getEndUnit() == oldu) {
                t.setEndUnit(newu);
            }
            if (t.getHandlerUnit() == oldu) {
                t.setHandlerUnit(newu);
            }
            size--;
            if (size <= 0) {
                return;
            }
            succOf = traps.getSuccOf(t);
            t = succOf;
        } while (succOf != null);
    }

    public static boolean isExceptionCaughtAt(Chain<Unit> units, Unit u, Iterator<Trap> trapsIt) {
        while (trapsIt.hasNext()) {
            Trap t = trapsIt.next();
            Iterator<Unit> it = units.iterator(t.getBeginUnit(), units.getPredOf(t.getEndUnit()));
            while (it.hasNext()) {
                if (u.equals(it.next())) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int getIntegerNine() {
        int r1 = Rand.getInt(8388606) * 256;
        int r2 = Rand.getInt(28) * 9;
        if (r2 > 126) {
            r2 += 4;
        }
        return r1 + r2;
    }

    public static boolean isBafIf(Unit u) {
        if ((u instanceof IfCmpEqInst) || (u instanceof IfCmpGeInst) || (u instanceof IfCmpGtInst) || (u instanceof IfCmpLeInst) || (u instanceof IfCmpLtInst) || (u instanceof IfCmpNeInst) || (u instanceof IfEqInst) || (u instanceof IfGeInst) || (u instanceof IfGtInst) || (u instanceof IfLeInst) || (u instanceof IfLtInst) || (u instanceof IfNeInst) || (u instanceof IfNonNullInst) || (u instanceof IfNullInst)) {
            return true;
        }
        return false;
    }
}
