package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Set;
import soot.RefType;
import soot.Scene;
import soot.SootField;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.genericutil.Util;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/InnerTypesIncrementalHeuristic.class */
public class InnerTypesIncrementalHeuristic implements FieldCheckHeuristic {
    private final TypeManager manager;
    private final int passesInDirection;
    private final Set<RefType> typesToCheck = new HashSet();
    private String newTypeOnQuery = null;
    private final Set<RefType> bothEndsTypes = new HashSet();
    private final Set<RefType> notBothEndsTypes = new HashSet();
    private int numPasses = 0;
    private boolean allNotBothEnds = false;

    public InnerTypesIncrementalHeuristic(TypeManager manager, int maxPasses) {
        this.manager = manager;
        this.passesInDirection = maxPasses / 2;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean runNewPass() {
        boolean added;
        this.numPasses++;
        if (this.numPasses == this.passesInDirection) {
            return switchToNotBothEnds();
        }
        if (this.newTypeOnQuery != null) {
            String topLevelTypeStr = Util.topLevelTypeString(this.newTypeOnQuery);
            if (Scene.v().containsType(topLevelTypeStr)) {
                RefType refType = Scene.v().getRefType(topLevelTypeStr);
                added = this.typesToCheck.add(refType);
            } else {
                added = false;
            }
            this.newTypeOnQuery = null;
            return added;
        }
        return switchToNotBothEnds();
    }

    private boolean switchToNotBothEnds() {
        if (!this.allNotBothEnds) {
            this.numPasses = 0;
            this.allNotBothEnds = true;
            this.newTypeOnQuery = null;
            this.typesToCheck.clear();
            return true;
        }
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validateMatchesForField(SparkField field) {
        RefType refType;
        if (field instanceof ArrayElement) {
            return true;
        }
        SootField sootField = (SootField) field;
        RefType declaringType = sootField.getDeclaringClass().getType();
        String declaringTypeStr = declaringType.toString();
        String topLevel = Util.topLevelTypeString(declaringTypeStr);
        if (Scene.v().containsType(topLevel)) {
            refType = Scene.v().getRefType(topLevel);
        } else {
            refType = null;
        }
        for (RefType checkedType : this.typesToCheck) {
            if (this.manager.castNeverFails(checkedType, refType)) {
                return true;
            }
        }
        if (this.newTypeOnQuery == null) {
            this.newTypeOnQuery = declaringTypeStr;
            return false;
        }
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validFromBothEnds(SparkField field) {
        if (this.allNotBothEnds) {
            return false;
        }
        if (field instanceof ArrayElement) {
            return true;
        }
        SootField sootField = (SootField) field;
        RefType declaringType = sootField.getDeclaringClass().getType();
        if (this.bothEndsTypes.contains(declaringType)) {
            return true;
        }
        if (this.notBothEndsTypes.contains(declaringType)) {
            return false;
        }
        if (SootUtil.hasRecursiveField(declaringType.getSootClass())) {
            this.notBothEndsTypes.add(declaringType);
            return false;
        }
        this.bothEndsTypes.add(declaringType);
        return true;
    }

    public String toString() {
        return this.typesToCheck.toString();
    }
}
