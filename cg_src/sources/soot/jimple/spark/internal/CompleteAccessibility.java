package soot.jimple.spark.internal;

import soot.G;
import soot.Singletons;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/internal/CompleteAccessibility.class */
public class CompleteAccessibility implements ClientAccessibilityOracle {
    public CompleteAccessibility(Singletons.Global g) {
    }

    public static CompleteAccessibility v() {
        return G.v().soot_jimple_spark_internal_CompleteAccessibility();
    }

    @Override // soot.jimple.spark.internal.ClientAccessibilityOracle
    public boolean isAccessible(SootMethod method) {
        return true;
    }

    @Override // soot.jimple.spark.internal.ClientAccessibilityOracle
    public boolean isAccessible(SootField field) {
        return true;
    }
}
