package org.jf.dexlib2.base.reference;

import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.reference.CallSiteReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseCallSiteReference.class */
public abstract class BaseCallSiteReference extends BaseReference implements CallSiteReference {
    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    public int hashCode() {
        int hashCode = getName().hashCode();
        return (((((((hashCode * 31) + getMethodHandle().hashCode()) * 31) + getMethodName().hashCode()) * 31) + getMethodProto().hashCode()) * 31) + getExtraArguments().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.CallSiteReference
    public boolean equals(Object o) {
        if (o != null && (o instanceof CallSiteReference)) {
            CallSiteReference other = (CallSiteReference) o;
            return getMethodHandle().equals(other.getMethodHandle()) && getMethodName().equals(other.getMethodName()) && getMethodProto().equals(other.getMethodProto()) && getExtraArguments().equals(other.getExtraArguments());
        }
        return false;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getCallSite(this);
    }
}
