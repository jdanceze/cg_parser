package soot.jimple.toolkits.invoke;

import java.util.Iterator;
import java.util.Set;
import soot.Body;
import soot.Hierarchy;
import soot.Local;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.Trap;
import soot.TrapManager;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.JimpleBody;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.shimple.ShimpleBody;
import soot.util.Chain;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/ThrowManager.class */
public class ThrowManager {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ThrowManager.class.desiredAssertionStatus();
    }

    public static Stmt getNullPointerExceptionThrower(JimpleBody b) {
        return getNullPointerExceptionThrower((Body) b);
    }

    public static Stmt getNullPointerExceptionThrower(ShimpleBody b) {
        return getNullPointerExceptionThrower((Body) b);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Stmt getNullPointerExceptionThrower(Body b) {
        if ($assertionsDisabled || (b instanceof JimpleBody) || (b instanceof ShimpleBody)) {
            Set<Unit> trappedUnits = TrapManager.getTrappedUnitsOf(b);
            Chain<Unit> units = b.getUnits();
            Unit first = units.getFirst();
            Stmt last = (Stmt) units.getLast();
            Stmt stmt = last;
            while (true) {
                Stmt s = stmt;
                if (s == first) {
                    break;
                }
                if (!trappedUnits.contains(s) && (s instanceof ThrowStmt)) {
                    Value throwee = ((ThrowStmt) s).getOp();
                    if (throwee instanceof Constant) {
                        continue;
                    } else if (s == first) {
                        break;
                    } else {
                        Stmt prosInvoke = (Stmt) units.getPredOf(s);
                        if (!(prosInvoke instanceof InvokeStmt)) {
                            continue;
                        } else if (prosInvoke == first) {
                            break;
                        } else {
                            Stmt prosNew = (Stmt) units.getPredOf(prosInvoke);
                            if (prosNew instanceof AssignStmt) {
                                InvokeExpr ie = ((InvokeStmt) prosInvoke).getInvokeExpr();
                                if ((ie instanceof SpecialInvokeExpr) && ((SpecialInvokeExpr) ie).getBase() == throwee && "<init>".equals(ie.getMethodRef().name())) {
                                    Value ro = ((AssignStmt) prosNew).getRightOp();
                                    if (((AssignStmt) prosNew).getLeftOp() == throwee && (ro instanceof NewExpr) && ((NewExpr) ro).getBaseType().equals(RefType.v("java.lang.NullPointerException"))) {
                                        return prosNew;
                                    }
                                }
                            } else {
                                continue;
                            }
                        }
                    }
                }
                stmt = (Stmt) units.getPredOf(s);
            }
            return addThrowAfter(b.getLocals(), units, last);
        }
        throw new AssertionError();
    }

    static Stmt addThrowAfter(JimpleBody b, Stmt target) {
        return addThrowAfter(b.getLocals(), b.getUnits(), target);
    }

    static Stmt addThrowAfter(ShimpleBody b, Stmt target) {
        return addThrowAfter(b.getLocals(), b.getUnits(), target);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public static Stmt addThrowAfter(Chain<Local> locals, Chain<Unit> units, Stmt target) {
        boolean canAddI;
        int i = 0;
        do {
            canAddI = true;
            String name = "__throwee" + i;
            for (Local l : locals) {
                if (name.equals(l.getName())) {
                    canAddI = false;
                }
            }
            if (!canAddI) {
                i++;
            }
        } while (!canAddI);
        Jimple jimp = Jimple.v();
        Local l2 = jimp.newLocal("__throwee" + i, RefType.v("java.lang.NullPointerException"));
        locals.add(l2);
        Stmt newStmt = jimp.newAssignStmt(l2, jimp.newNewExpr(RefType.v("java.lang.NullPointerException")));
        Stmt invStmt = jimp.newInvokeStmt(jimp.newSpecialInvokeExpr(l2, Scene.v().getMethod("<java.lang.NullPointerException: void <init>()>").makeRef()));
        Stmt throwStmt = jimp.newThrowStmt(l2);
        units.insertAfter(newStmt, target);
        units.insertAfter(invStmt, newStmt);
        units.insertAfter(throwStmt, invStmt);
        return newStmt;
    }

    static boolean isExceptionCaughtAt(SootClass e, Stmt stmt, Body b) {
        Hierarchy h = new Hierarchy();
        for (Trap t : b.getTraps()) {
            if (h.isClassSubclassOfIncluding(e, t.getException())) {
                Iterator<Unit> it = b.getUnits().iterator(t.getBeginUnit(), t.getEndUnit());
                while (it.hasNext()) {
                    if (stmt.equals(it.next())) {
                        return true;
                    }
                }
                continue;
            }
        }
        return false;
    }
}
