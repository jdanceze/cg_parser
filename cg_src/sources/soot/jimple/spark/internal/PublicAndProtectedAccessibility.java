package soot.jimple.spark.internal;

import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/internal/PublicAndProtectedAccessibility.class */
public class PublicAndProtectedAccessibility implements ClientAccessibilityOracle {
    public PublicAndProtectedAccessibility(Singletons.Global g) {
    }

    public static PublicAndProtectedAccessibility v() {
        return G.v().soot_jimple_spark_internal_PublicAndProtectedAccessibility();
    }

    @Override // soot.jimple.spark.internal.ClientAccessibilityOracle
    public boolean isAccessible(SootMethod method) {
        return method.isPublic() || method.isProtected();
    }

    @Override // soot.jimple.spark.internal.ClientAccessibilityOracle
    public boolean isAccessible(SootField field) {
        return field.isPublic() || field.isProtected();
    }
}
