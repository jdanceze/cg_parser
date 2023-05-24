package soot.jimple.toolkits.scalar;

import soot.BodyTransformer;
import soot.RefType;
import soot.SootClass;
import soot.Type;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/scalar/AbstractStaticnessCorrector.class */
public abstract class AbstractStaticnessCorrector extends BodyTransformer {
    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isClassLoaded(SootClass sc) {
        return sc.resolvingLevel() >= 2;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static boolean isTypeLoaded(Type tp) {
        if (tp instanceof RefType) {
            RefType rt = (RefType) tp;
            if (rt.hasSootClass()) {
                return isClassLoaded(rt.getSootClass());
            }
            return false;
        }
        return false;
    }
}
