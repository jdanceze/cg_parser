package soot.jimple.spark.ondemand;

import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/FieldCheckHeuristic.class */
public interface FieldCheckHeuristic {
    boolean runNewPass();

    boolean validateMatchesForField(SparkField sparkField);

    boolean validFromBothEnds(SparkField sparkField);
}
