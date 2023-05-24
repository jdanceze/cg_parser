package soot.jimple.spark.ondemand;

import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/ManualAndInnerHeuristic.class */
public class ManualAndInnerHeuristic implements FieldCheckHeuristic {
    final ManualFieldCheckHeuristic manual = new ManualFieldCheckHeuristic();
    final InnerTypesIncrementalHeuristic inner;

    public ManualAndInnerHeuristic(TypeManager tm, int maxPasses) {
        this.inner = new InnerTypesIncrementalHeuristic(tm, maxPasses);
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean runNewPass() {
        return this.inner.runNewPass();
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validateMatchesForField(SparkField field) {
        return this.manual.validateMatchesForField(field) || this.inner.validateMatchesForField(field);
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validFromBothEnds(SparkField field) {
        return this.inner.validFromBothEnds(field);
    }
}
