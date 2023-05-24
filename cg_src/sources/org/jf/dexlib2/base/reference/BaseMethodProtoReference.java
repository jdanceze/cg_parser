package org.jf.dexlib2.base.reference;

import com.google.common.collect.Ordering;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.reference.MethodProtoReference;
import org.jf.util.CharSequenceUtils;
import org.jf.util.CollectionUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseMethodProtoReference.class */
public abstract class BaseMethodProtoReference extends BaseReference implements MethodProtoReference {
    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    public int hashCode() {
        int hashCode = getReturnType().hashCode();
        return (hashCode * 31) + getParameterTypes().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference
    public boolean equals(@Nullable Object o) {
        if (o instanceof MethodProtoReference) {
            MethodProtoReference other = (MethodProtoReference) o;
            return getReturnType().equals(other.getReturnType()) && CharSequenceUtils.listEquals(getParameterTypes(), other.getParameterTypes());
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodProtoReference, java.lang.Comparable
    public int compareTo(@Nonnull MethodProtoReference o) {
        int res = getReturnType().compareTo(o.getReturnType());
        return res != 0 ? res : CollectionUtils.compareAsIterable(Ordering.usingToString(), getParameterTypes(), o.getParameterTypes());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getMethodProtoDescriptor(this);
    }
}
