package soot.jimple.spark.ondemand;

import soot.SootField;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/ManualFieldCheckHeuristic.class */
public class ManualFieldCheckHeuristic implements FieldCheckHeuristic {
    private boolean allNotBothEnds = false;
    private static final String[] importantTypes = {"java.util.Vector", "java.util.Hashtable", "java.util.Hashtable$Entry", "java.util.Hashtable$Enumerator", "java.util.LinkedList", "java.util.LinkedList$Entry", "java.util.AbstractList$Itr", "java.util.Vector$1", "java.util.ArrayList"};
    private static final String[] notBothEndsTypes = {"java.util.Hashtable$Entry", "java.util.LinkedList$Entry"};

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean runNewPass() {
        if (!this.allNotBothEnds) {
            this.allNotBothEnds = true;
            return true;
        }
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validateMatchesForField(SparkField field) {
        String[] strArr;
        if (field instanceof ArrayElement) {
            return true;
        }
        SootField sootField = (SootField) field;
        String fieldTypeStr = sootField.getDeclaringClass().getType().toString();
        for (String typeName : importantTypes) {
            if (fieldTypeStr.equals(typeName)) {
                return true;
            }
        }
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validFromBothEnds(SparkField field) {
        String[] strArr;
        if (this.allNotBothEnds) {
            return false;
        }
        if (field instanceof SootField) {
            SootField sootField = (SootField) field;
            String fieldTypeStr = sootField.getDeclaringClass().getType().toString();
            for (String typeName : notBothEndsTypes) {
                if (fieldTypeStr.equals(typeName)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public String toString() {
        return "Manual annotations";
    }
}
