package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.HashSet;
import java.util.Set;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ServiceEntryPointInfo.class */
public class ServiceEntryPointInfo extends ComponentEntryPointInfo {
    private SootField binderField;

    public ServiceEntryPointInfo(SootMethod entryPoint) {
        super(entryPoint);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setBinderField(SootField binderField) {
        this.binderField = binderField;
    }

    public SootField getBinderField() {
        return this.binderField;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public Set<SootField> getAdditionalFields() {
        Set<SootField> fields = new HashSet<>(super.getAdditionalFields());
        if (this.binderField != null) {
            fields.add(this.binderField);
        }
        return fields;
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public int hashCode() {
        int result = super.hashCode();
        return (31 * result) + (this.binderField == null ? 0 : this.binderField.hashCode());
    }

    @Override // soot.jimple.infoflow.android.entryPointCreators.components.ComponentEntryPointInfo
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj) || getClass() != obj.getClass()) {
            return false;
        }
        ServiceEntryPointInfo other = (ServiceEntryPointInfo) obj;
        if (this.binderField == null) {
            if (other.binderField != null) {
                return false;
            }
            return true;
        } else if (!this.binderField.equals(other.binderField)) {
            return false;
        } else {
            return true;
        }
    }
}
