package soot.jimple.spark.ondemand;

import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/EverythingHeuristic.class */
public class EverythingHeuristic implements FieldCheckHeuristic {
    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean runNewPass() {
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validateMatchesForField(SparkField field) {
        return true;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validFromBothEnds(SparkField field) {
        return false;
    }
}
