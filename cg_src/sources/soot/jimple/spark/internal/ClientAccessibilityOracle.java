package soot.jimple.spark.internal;

import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/internal/ClientAccessibilityOracle.class */
public interface ClientAccessibilityOracle {
    boolean isAccessible(SootMethod sootMethod);

    boolean isAccessible(SootField sootField);
}
