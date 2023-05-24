package soot.jimple.toolkits.ide.libsumm;

import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.InvokeExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.StaticInvokeExpr;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/ide/libsumm/FixedMethods.class */
public class FixedMethods {
    public static final boolean ASSUME_PACKAGES_SEALED = false;

    public static boolean isFixed(InvokeExpr ie) {
        return (ie instanceof StaticInvokeExpr) || (ie instanceof SpecialInvokeExpr) || !clientOverwriteableOverwrites(ie.getMethod());
    }

    private static boolean clientOverwriteableOverwrites(SootMethod m) {
        if (clientOverwriteable(m)) {
            return true;
        }
        SootClass c = m.getDeclaringClass();
        for (SootClass cPrime : Scene.v().getFastHierarchy().getSubclassesOf(c)) {
            SootMethod mPrime = cPrime.getMethodUnsafe(m.getSubSignature());
            if (mPrime != null && clientOverwriteable(mPrime)) {
                return true;
            }
        }
        return false;
    }

    private static boolean clientOverwriteable(SootMethod m) {
        SootClass c = m.getDeclaringClass();
        if (!c.isFinal() && !m.isFinal() && visible(m) && clientCanInstantiate(c)) {
            return true;
        }
        return false;
    }

    private static boolean clientCanInstantiate(SootClass cPrime) {
        if (cPrime.isInterface()) {
            return true;
        }
        for (SootMethod m : cPrime.getMethods()) {
            if (m.getName().equals("<init>") && visible(m)) {
                return true;
            }
        }
        return false;
    }

    private static boolean visible(SootMethod mPrime) {
        SootClass cPrime = mPrime.getDeclaringClass();
        if (cPrime.isPublic() || cPrime.isProtected() || !cPrime.isPrivate()) {
            return mPrime.isPublic() || mPrime.isProtected() || !mPrime.isPrivate();
        }
        return false;
    }
}
