package soot.jimple.toolkits.invoke;

import java.util.Iterator;
import soot.Hierarchy;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.jimple.AssignStmt;
import soot.jimple.FieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.Stmt;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/invoke/InlinerSafetyManager.class */
public class InlinerSafetyManager {
    private static final boolean PRINT_FAILURE_REASONS = true;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InlinerSafetyManager.class.desiredAssertionStatus();
    }

    public static boolean checkSpecialInlineRestrictions(SootMethod container, SootMethod target, String options) {
        boolean accessors = "accessors".equals(options);
        Iterator<Unit> it = target.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt st = (Stmt) u;
            if (st.containsInvokeExpr()) {
                InvokeExpr ie1 = st.getInvokeExpr();
                if (ie1 instanceof SpecialInvokeExpr) {
                    SootClass containerDeclaringClass = container.getDeclaringClass();
                    if (specialInvokePerformsLookupIn(ie1, containerDeclaringClass) || specialInvokePerformsLookupIn(ie1, target.getDeclaringClass())) {
                        return false;
                    }
                    SootMethod specialTarget = ie1.getMethod();
                    if (specialTarget.isPrivate() && specialTarget.getDeclaringClass() != containerDeclaringClass && !accessors) {
                        return false;
                    }
                } else {
                    continue;
                }
            }
        }
        return true;
    }

    public static boolean checkAccessRestrictions(SootMethod container, SootMethod target, String modifierOptions) {
        Iterator<Unit> it = target.getActiveBody().getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt st = (Stmt) u;
            if (st.containsInvokeExpr() && !AccessManager.ensureAccess(container, st.getInvokeExpr().getMethod(), modifierOptions)) {
                return false;
            }
            if (st instanceof AssignStmt) {
                Value lhs = ((AssignStmt) st).getLeftOp();
                Value rhs = ((AssignStmt) st).getRightOp();
                if (!(lhs instanceof FieldRef) || AccessManager.ensureAccess(container, ((FieldRef) lhs).getField(), modifierOptions)) {
                    if ((rhs instanceof FieldRef) && !AccessManager.ensureAccess(container, ((FieldRef) rhs).getField(), modifierOptions)) {
                        return false;
                    }
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean ensureInlinability(SootMethod target, Stmt toInline, SootMethod container, String modifierOptions) {
        if (!canSafelyInlineInto(target, toInline, container)) {
            System.out.println("[InlinerSafetyManager] failed canSafelyInlineInto checks");
            return false;
        } else if (!AccessManager.ensureAccess(container, target, modifierOptions)) {
            System.out.println("[InlinerSafetyManager] failed AccessManager.ensureAccess checks");
            return false;
        } else if (!checkSpecialInlineRestrictions(container, target, modifierOptions)) {
            System.out.println("[InlinerSafetyManager] failed checkSpecialInlineRestrictions checks");
            return false;
        } else if (!checkAccessRestrictions(container, target, modifierOptions)) {
            System.out.println("[InlinerSafetyManager] failed checkAccessRestrictions checks");
            return false;
        } else {
            return true;
        }
    }

    private static boolean canSafelyInlineInto(SootMethod inlinee, Stmt toInline, SootMethod container) {
        if ("<init>".equals(inlinee.getName())) {
            System.out.println("[InlinerSafetyManager] cannot inline constructors");
            return false;
        } else if (inlinee.getSignature().equals(container.getSignature())) {
            System.out.println("[InlinerSafetyManager] cannot inline method into itself");
            return false;
        } else if (inlinee.isNative() || inlinee.isAbstract()) {
            System.out.println("[InlinerSafetyManager] cannot inline native or abstract methods");
            return false;
        } else {
            InvokeExpr ie = toInline.getInvokeExpr();
            if (ie instanceof InstanceInvokeExpr) {
                Type baseTy = ((InstanceInvokeExpr) ie).getBase().getType();
                if ((baseTy instanceof RefType) && invokeThrowsAccessErrorIn(((RefType) baseTy).getSootClass(), inlinee, container)) {
                    System.out.println("[InlinerSafetyManager] cannot inline away IllegalAccessErrors");
                    return false;
                }
            }
            if (ie instanceof SpecialInvokeExpr) {
                if (specialInvokePerformsLookupIn(ie, inlinee.getDeclaringClass()) || specialInvokePerformsLookupIn(ie, container.getDeclaringClass())) {
                    System.out.println("[InlinerSafetyManager] cannot inline if changes semantics of invokespecial");
                    return false;
                }
                return true;
            }
            return true;
        }
    }

    private static boolean invokeThrowsAccessErrorIn(SootClass base, SootMethod inlinee, SootMethod container) {
        SootClass inlineeClass = inlinee.getDeclaringClass();
        SootClass containerClass = container.getDeclaringClass();
        if (inlinee.isPrivate() && !inlineeClass.getName().equals(containerClass.getName())) {
            return true;
        }
        if (!inlinee.isPrivate() && !inlinee.isProtected() && !inlinee.isPublic() && !inlineeClass.getPackageName().equals(containerClass.getPackageName())) {
            return true;
        }
        if (inlinee.isProtected()) {
            Hierarchy h = Scene.v().getActiveHierarchy();
            if (!h.isClassSuperclassOfIncluding(inlineeClass, containerClass)) {
                if (base == null || !h.isClassSuperclassOfIncluding(base, containerClass)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }

    static boolean specialInvokePerformsLookupIn(InvokeExpr ie, SootClass containerClass) {
        if ($assertionsDisabled || (ie instanceof SpecialInvokeExpr)) {
            SootMethod m = ie.getMethod();
            if ("<init>".equals(m.getName()) || m.isPrivate()) {
                return false;
            }
            Hierarchy h = Scene.v().getActiveHierarchy();
            return h.isClassSuperclassOf(m.getDeclaringClass(), containerClass);
        }
        throw new AssertionError();
    }

    private InlinerSafetyManager() {
    }
}
