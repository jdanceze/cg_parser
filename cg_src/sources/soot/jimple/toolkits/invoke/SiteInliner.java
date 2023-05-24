package soot.jimple.toolkits.invoke;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.Local;
import soot.PhaseOptions;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Trap;
import soot.TrapManager;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityRef;
import soot.jimple.IdentityStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NullConstant;
import soot.jimple.ParameterRef;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.Stmt;
import soot.jimple.ThisRef;
import soot.jimple.toolkits.scalar.LocalNameStandardizer;
import soot.tagkit.Host;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/SiteInliner.class */
public class SiteInliner {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SiteInliner.class.desiredAssertionStatus();
    }

    public String getDefaultOptions() {
        return "insert-null-checks insert-redundant-casts";
    }

    public static void inlineSites(List<List<Host>> sites) {
        inlineSites(sites, Collections.emptyMap());
    }

    public static void inlineSites(List<List<Host>> sites, Map<String, String> options) {
        for (List<Host> l : sites) {
            if (!$assertionsDisabled && l.size() != 3) {
                throw new AssertionError();
            }
            SootMethod inlinee = (SootMethod) l.get(0);
            Stmt toInline = (Stmt) l.get(1);
            SootMethod container = (SootMethod) l.get(2);
            inlineSite(inlinee, toInline, container, options);
        }
    }

    public static List<Unit> inlineSite(SootMethod inlinee, Stmt toInline, SootMethod container) {
        return inlineSite(inlinee, toInline, container, Collections.emptyMap());
    }

    /* JADX WARN: Multi-variable type inference failed */
    public static List<Unit> inlineSite(SootMethod inlinee, Stmt toInline, SootMethod container, Map<String, String> options) {
        SootClass declaringClass = inlinee.getDeclaringClass();
        if (!declaringClass.isApplicationClass() && !declaringClass.isLibraryClass()) {
            return null;
        }
        Body containerB = container.getActiveBody();
        Chain<Unit> containerUnits = containerB.getUnits();
        if ($assertionsDisabled || containerUnits.contains(toInline)) {
            InvokeExpr ie = toInline.getInvokeExpr();
            Value thisToAdd = ie instanceof InstanceInvokeExpr ? ((InstanceInvokeExpr) ie).getBase() : null;
            if (ie instanceof InstanceInvokeExpr) {
                if (PhaseOptions.getBoolean(options, "insert-redundant-casts")) {
                    Value base = ((InstanceInvokeExpr) ie).getBase();
                    SootClass localType = ((RefType) base.getType()).getSootClass();
                    if (localType.isInterface() || Scene.v().getActiveHierarchy().isClassSuperclassOf(localType, declaringClass)) {
                        Jimple jimp = Jimple.v();
                        RefType type = declaringClass.getType();
                        Local castee = jimp.newLocal("__castee", type);
                        containerB.getLocals().add(castee);
                        containerUnits.insertBefore(jimp.newAssignStmt(castee, jimp.newCastExpr(base, type)), toInline);
                        thisToAdd = castee;
                    }
                }
                if (PhaseOptions.getBoolean(options, "insert-null-checks")) {
                    Jimple jimp2 = Jimple.v();
                    if (TrapManager.isExceptionCaughtAt(Scene.v().getSootClass("java.lang.NullPointerException"), toInline, containerB)) {
                        IfStmt insertee = jimp2.newIfStmt(jimp2.newNeExpr(((InstanceInvokeExpr) ie).getBase(), NullConstant.v()), toInline);
                        containerUnits.insertBefore(insertee, (IfStmt) toInline);
                        insertee.setTarget(toInline);
                        ThrowManager.addThrowAfter(containerB.getLocals(), containerUnits, insertee);
                    } else {
                        containerUnits.insertBefore(jimp2.newIfStmt(jimp2.newEqExpr(((InstanceInvokeExpr) ie).getBase(), NullConstant.v()), ThrowManager.getNullPointerExceptionThrower(containerB)), toInline);
                    }
                }
            }
            if (inlinee.isSynchronized()) {
                if (ie instanceof InstanceInvokeExpr) {
                    SynchronizerManager.v().synchronizeStmtOn(toInline, containerB, (Local) ((InstanceInvokeExpr) ie).getBase());
                } else if (!container.getDeclaringClass().isInterface()) {
                    SynchronizerManager mgr = SynchronizerManager.v();
                    mgr.synchronizeStmtOn(toInline, containerB, mgr.addStmtsToFetchClassBefore(containerB, toInline));
                }
            }
            Body inlineeB = inlinee.getActiveBody();
            Chain<Unit> inlineeUnits = inlineeB.getUnits();
            Unit exitPoint = containerUnits.getSuccOf(toInline);
            HashMap<Local, Local> oldLocalsToNew = new HashMap<>();
            HashMap<Unit, Unit> oldUnitsToNew = new HashMap<>();
            Unit cursor = toInline;
            for (Unit u : inlineeUnits) {
                Unit currPrime = (Unit) u.clone();
                if (currPrime == null) {
                    throw new RuntimeException("getting null from clone!");
                }
                currPrime.addAllTagsOf(u);
                containerUnits.insertAfter(currPrime, cursor);
                oldUnitsToNew.put(u, currPrime);
                cursor = currPrime;
            }
            for (Local l : inlineeB.getLocals()) {
                Local lPrime = (Local) l.clone();
                if (lPrime == null) {
                    throw new RuntimeException("getting null from local clone!");
                }
                containerB.getLocals().add(lPrime);
                oldLocalsToNew.put(l, lPrime);
            }
            Iterator<Unit> it = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));
            while (it.hasNext()) {
                Unit patchee = it.next();
                for (ValueBox box : patchee.getUseAndDefBoxes()) {
                    Value value = box.getValue();
                    if (value instanceof Local) {
                        Local lPrime2 = oldLocalsToNew.get((Local) value);
                        if (lPrime2 == null) {
                            throw new RuntimeException("local has no clone!");
                        }
                        box.setValue(lPrime2);
                    }
                }
                for (UnitBox box2 : patchee.getUnitBoxes()) {
                    Unit uPrime = oldUnitsToNew.get(box2.getUnit());
                    if (uPrime == null) {
                        throw new RuntimeException("inlined stmt has no clone!");
                    }
                    box2.setUnit(uPrime);
                }
            }
            Chain<Trap> traps = containerB.getTraps();
            Trap prevTrap = null;
            for (Trap t : inlineeB.getTraps()) {
                Unit newBegin = oldUnitsToNew.get(t.getBeginUnit());
                Unit newEnd = oldUnitsToNew.get(t.getEndUnit());
                Unit newHandler = oldUnitsToNew.get(t.getHandlerUnit());
                if (newBegin == null || newEnd == null || newHandler == null) {
                    throw new RuntimeException("couldn't map trap!");
                }
                Trap trap = Jimple.v().newTrap(t.getException(), newBegin, newEnd, newHandler);
                if (prevTrap == null) {
                    traps.addFirst(trap);
                } else {
                    traps.insertAfter(trap, prevTrap);
                }
                prevTrap = trap;
            }
            ArrayList<Unit> cuCopy = new ArrayList<>();
            Iterator<Unit> it2 = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));
            while (it2.hasNext()) {
                cuCopy.add(it2.next());
            }
            Iterator<Unit> it3 = cuCopy.iterator();
            while (it3.hasNext()) {
                Unit u2 = it3.next();
                if (u2 instanceof IdentityStmt) {
                    IdentityStmt idStmt = (IdentityStmt) u2;
                    IdentityRef rhs = (IdentityRef) idStmt.getRightOp();
                    if (rhs instanceof CaughtExceptionRef) {
                        continue;
                    } else if (rhs instanceof ThisRef) {
                        if (!(ie instanceof InstanceInvokeExpr)) {
                            throw new RuntimeException("thisref with no receiver!");
                        }
                        containerUnits.swapWith(u2, Jimple.v().newAssignStmt(idStmt.getLeftOp(), thisToAdd));
                    } else if (rhs instanceof ParameterRef) {
                        ParameterRef pref = (ParameterRef) rhs;
                        containerUnits.swapWith(u2, Jimple.v().newAssignStmt(idStmt.getLeftOp(), ie.getArg(pref.getIndex())));
                    }
                } else if (u2 instanceof ReturnStmt) {
                    if (toInline instanceof InvokeStmt) {
                        containerUnits.swapWith(u2, Jimple.v().newGotoStmt(exitPoint));
                    } else if (toInline instanceof AssignStmt) {
                        Jimple jimp3 = Jimple.v();
                        AssignStmt as = jimp3.newAssignStmt(((AssignStmt) toInline).getLeftOp(), ((ReturnStmt) u2).getOp());
                        containerUnits.insertBefore(as, (AssignStmt) u2);
                        containerUnits.swapWith(u2, jimp3.newGotoStmt(exitPoint));
                    } else {
                        throw new RuntimeException("invoking stmt neither InvokeStmt nor AssignStmt!??!?!");
                    }
                } else if (u2 instanceof ReturnVoidStmt) {
                    containerUnits.swapWith(u2, Jimple.v().newGotoStmt(exitPoint));
                }
            }
            List<Unit> newStmts = new ArrayList<>();
            Iterator<Unit> i = containerUnits.iterator(containerUnits.getSuccOf(toInline), containerUnits.getPredOf(exitPoint));
            while (i.hasNext()) {
                newStmts.add(i.next());
            }
            containerUnits.remove(toInline);
            LocalNameStandardizer.v().transform(containerB, "ji.lns");
            return newStmts;
        }
        throw new AssertionError(toInline + " is not in body " + containerB);
    }
}
