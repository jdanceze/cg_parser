package soot.jimple.infoflow.util;

import java.util.Iterator;
import soot.Body;
import soot.RefType;
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Unit;
import soot.jimple.Constant;
import soot.jimple.InvokeExpr;
import soot.jimple.Stmt;
import soot.jimple.StringConstant;
import soot.jimple.infoflow.data.AccessPath;
import soot.jimple.infoflow.data.AccessPathFragment;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/util/SystemClassHandler.class */
public class SystemClassHandler {
    private static SystemClassHandler instance;
    private boolean excludeSystemComponents = true;

    public static SystemClassHandler v() {
        if (instance == null) {
            instance = new SystemClassHandler();
        }
        return instance;
    }

    public static void setInstance(SystemClassHandler instance2) {
        instance = instance2;
    }

    public boolean isClassInSystemPackage(SootClass clazz) {
        return clazz != null && isClassInSystemPackage(clazz.getName());
    }

    public boolean isClassInSystemPackage(String className) {
        return (className.startsWith("android.") || className.startsWith("java.") || className.startsWith("javax.") || className.startsWith("sun.") || className.startsWith("org.omg.") || className.startsWith("org.w3c.dom.") || className.startsWith("com.google.") || className.startsWith("com.android.")) && this.excludeSystemComponents;
    }

    public boolean isClassInSystemPackage(Type type) {
        if (type instanceof RefType) {
            return isClassInSystemPackage(((RefType) type).getSootClass().getName());
        }
        return false;
    }

    public boolean isTaintVisible(AccessPath taintedPath, SootMethod method) {
        AccessPathFragment[] fragments;
        if (taintedPath == null || !taintedPath.isInstanceFieldRef() || !isClassInSystemPackage(method.getDeclaringClass().getName())) {
            return true;
        }
        boolean hasSystemType = taintedPath.getBaseType() != null && isClassInSystemPackage(taintedPath.getBaseType());
        for (AccessPathFragment fragment : taintedPath.getFragments()) {
            boolean curFieldIsSystem = isClassInSystemPackage(fragment.getFieldType()) || isClassInSystemPackage(fragment.getField().getDeclaringClass().getType());
            if (curFieldIsSystem) {
                hasSystemType = true;
            } else if (hasSystemType) {
                return false;
            }
        }
        return true;
    }

    public void setExcludeSystemComponents(boolean excludeSystemComponents) {
        this.excludeSystemComponents = excludeSystemComponents;
    }

    public boolean isStubImplementation(Body body) {
        Constant stubConst = StringConstant.v("Stub!");
        Iterator<Unit> it = body.getUnits().iterator();
        while (it.hasNext()) {
            Unit u = it.next();
            Stmt stmt = (Stmt) u;
            if (stmt.containsInvokeExpr()) {
                InvokeExpr iexpr = stmt.getInvokeExpr();
                SootMethod targetMethod = iexpr.getMethod();
                if (targetMethod.isConstructor() && targetMethod.getDeclaringClass().getName().equals("java.lang.RuntimeException") && iexpr.getArgCount() > 0 && iexpr.getArg(0).equals(stubConst)) {
                    return true;
                }
            }
        }
        return false;
    }
}
