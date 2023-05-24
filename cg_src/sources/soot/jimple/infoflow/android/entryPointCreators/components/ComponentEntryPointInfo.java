package soot.jimple.infoflow.android.entryPointCreators.components;

import java.util.Collections;
import java.util.Set;
import soot.SootField;
import soot.SootMethod;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/entryPointCreators/components/ComponentEntryPointInfo.class */
public class ComponentEntryPointInfo {
    private final SootMethod entryPoint;
    private SootField intentField;

    public ComponentEntryPointInfo(SootMethod entryPoint) {
        this.entryPoint = entryPoint;
    }

    public SootMethod getEntryPoint() {
        return this.entryPoint;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setIntentField(SootField intentField) {
        this.intentField = intentField;
    }

    public SootField getIntentField() {
        return this.intentField;
    }

    public Set<SootField> getAdditionalFields() {
        if (this.intentField == null) {
            return Collections.emptySet();
        }
        return Collections.singleton(this.intentField);
    }

    public int hashCode() {
        int result = (31 * 1) + (this.entryPoint == null ? 0 : this.entryPoint.hashCode());
        return (31 * result) + (this.intentField == null ? 0 : this.intentField.hashCode());
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        ComponentEntryPointInfo other = (ComponentEntryPointInfo) obj;
        if (this.entryPoint == null) {
            if (other.entryPoint != null) {
                return false;
            }
        } else if (!this.entryPoint.equals(other.entryPoint)) {
            return false;
        }
        if (this.intentField == null) {
            if (other.intentField != null) {
                return false;
            }
            return true;
        } else if (!this.intentField.equals(other.intentField)) {
            return false;
        } else {
            return true;
        }
    }
}
