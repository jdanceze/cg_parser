package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.HashSet;
import java.util.Set;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ActivityEntryPointInfo.class */
public class ActivityEntryPointInfo extends ComponentEntryPointInfo {
    private SootField resultIntentField;

    public ActivityEntryPointInfo(SootMethod entryPoint) {
        super(entryPoint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setResultIntentField(SootField resultIntentField) {
        this.resultIntentField = resultIntentField;
    }

    public SootField getResultIntentField() {
        return this.resultIntentField;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public Set<SootField> getAdditionalFields() {
        Set<SootField> fields = new HashSet<>(super.getAdditionalFields());
        if (this.resultIntentField != null) {
            fields.add(this.resultIntentField);
        }
        return fields;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.resultIntentField == null ? 0 : this.resultIntentField.hashCode());
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ActivityEntryPointInfo other = (ActivityEntryPointInfo) obj;
        if (this.resultIntentField == null) {
            if (other.resultIntentField != null) {
                return false;
            }
            return true;
        } else if (!this.resultIntentField.equals(other.resultIntentField)) {
            return false;
        } else {
            return true;
        }
    }
}
