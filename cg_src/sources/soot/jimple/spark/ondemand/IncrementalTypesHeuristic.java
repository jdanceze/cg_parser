package soot.jimple.spark.ondemand;

import java.util.HashSet;
import java.util.Set;
import soot.RefType;
import soot.SootField;
import soot.jimple.spark.internal.TypeManager;
import soot.jimple.spark.ondemand.pautil.SootUtil;
import soot.jimple.spark.pag.ArrayElement;
import soot.jimple.spark.pag.SparkField;
/* loaded from: gencallgraphv3.jar:soot/jimple/spark/ondemand/IncrementalTypesHeuristic.class */
public class IncrementalTypesHeuristic implements FieldCheckHeuristic {
    private final TypeManager manager;
    private static final boolean EXCLUDE_TYPES = false;
    private static final String[] EXCLUDED_NAMES = {"ca.mcgill.sable.soot.SootMethod"};
    private Set<RefType> typesToCheck = new HashSet();
    private Set<RefType> notBothEndsTypes = new HashSet();
    private RefType newTypeOnQuery = null;

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean runNewPass() {
        if (this.newTypeOnQuery != null) {
            boolean added = this.typesToCheck.add(this.newTypeOnQuery);
            if (SootUtil.hasRecursiveField(this.newTypeOnQuery.getSootClass())) {
                this.notBothEndsTypes.add(this.newTypeOnQuery);
            }
            this.newTypeOnQuery = null;
            return added;
        }
        return false;
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validateMatchesForField(SparkField field) {
        if (field instanceof ArrayElement) {
            return true;
        }
        SootField sootField = (SootField) field;
        RefType declaringType = sootField.getDeclaringClass().getType();
        for (RefType typeToCheck : this.typesToCheck) {
            if (this.manager.castNeverFails(declaringType, typeToCheck)) {
                return true;
            }
        }
        if (this.newTypeOnQuery == null) {
            this.newTypeOnQuery = declaringType;
            return false;
        }
        return false;
    }

    public IncrementalTypesHeuristic(TypeManager manager) {
        this.manager = manager;
    }

    public String toString() {
        StringBuffer ret = new StringBuffer();
        ret.append("types ");
        ret.append(this.typesToCheck.toString());
        if (!this.notBothEndsTypes.isEmpty()) {
            ret.append(" not both ");
            ret.append(this.notBothEndsTypes.toString());
        }
        return ret.toString();
    }

    @Override // soot.jimple.spark.ondemand.FieldCheckHeuristic
    public boolean validFromBothEnds(SparkField field) {
        if (field instanceof SootField) {
            SootField sootField = (SootField) field;
            RefType declaringType = sootField.getDeclaringClass().getType();
            for (RefType type : this.notBothEndsTypes) {
                if (this.manager.castNeverFails(declaringType, type)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public boolean refineVirtualCall(SootUtil.CallSiteAndContext callSiteAndContext) {
        return true;
    }
}
