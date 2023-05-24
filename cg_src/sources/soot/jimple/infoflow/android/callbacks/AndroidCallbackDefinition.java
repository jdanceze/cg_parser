package soot.jimple.infoflow.android.callbacks;

import soot.SootMethod;
import soot.jimple.infoflow.callbacks.CallbackDefinition;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/AndroidCallbackDefinition.class */
public class AndroidCallbackDefinition extends CallbackDefinition {
    private final CallbackType callbackType;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/callbacks/AndroidCallbackDefinition$CallbackType.class */
    public enum CallbackType {
        Widget,
        Default;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static CallbackType[] valuesCustom() {
            CallbackType[] valuesCustom = values();
            int length = valuesCustom.length;
            CallbackType[] callbackTypeArr = new CallbackType[length];
            System.arraycopy(valuesCustom, 0, callbackTypeArr, 0, length);
            return callbackTypeArr;
        }
    }

    public AndroidCallbackDefinition(SootMethod targetMethod, SootMethod parentMethod, CallbackType callbackType) {
        super(targetMethod, parentMethod);
        this.callbackType = callbackType;
    }

    public CallbackType getCallbackType() {
        return this.callbackType;
    }

    @Override // soot.jimple.infoflow.callbacks.CallbackDefinition
    public int hashCode() {
        int result = (31 * 1) + (this.callbackType == null ? 0 : this.callbackType.hashCode());
        return (31 * ((31 * result) + (this.targetMethod == null ? 0 : this.targetMethod.hashCode()))) + (this.parentMethod == null ? 0 : this.parentMethod.hashCode());
    }

    @Override // soot.jimple.infoflow.callbacks.CallbackDefinition
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AndroidCallbackDefinition other = (AndroidCallbackDefinition) obj;
        if (this.callbackType != other.callbackType) {
            return false;
        }
        if (this.targetMethod == null) {
            if (other.targetMethod != null) {
                return false;
            }
        } else if (!this.targetMethod.equals(other.targetMethod)) {
            return false;
        }
        if (this.parentMethod == null) {
            if (other.parentMethod != null) {
                return false;
            }
            return true;
        } else if (!this.parentMethod.equals(other.parentMethod)) {
            return false;
        } else {
            return true;
        }
    }

    @Override // soot.jimple.infoflow.callbacks.CallbackDefinition
    public String toString() {
        return this.targetMethod.toString();
    }
}
