package org.jf.dexlib2.base.reference;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.reference.FieldReference;
import org.jf.dexlib2.iface.reference.MethodHandleReference;
import org.jf.dexlib2.iface.reference.MethodReference;
import org.jf.dexlib2.iface.reference.Reference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseMethodHandleReference.class */
public abstract class BaseMethodHandleReference extends BaseReference implements MethodHandleReference {
    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    public int hashCode() {
        int hashCode = getMethodHandleType();
        return (hashCode * 31) + getMemberReference().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference
    public boolean equals(Object o) {
        if (o != null && (o instanceof MethodHandleReference)) {
            MethodHandleReference other = (MethodHandleReference) o;
            return getMethodHandleType() == other.getMethodHandleType() && getMemberReference().equals(other.getMemberReference());
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.reference.MethodHandleReference, java.lang.Comparable
    public int compareTo(@Nonnull MethodHandleReference o) {
        int res = Ints.compare(getMethodHandleType(), o.getMethodHandleType());
        if (res != 0) {
            return res;
        }
        Reference reference = getMemberReference();
        if (reference instanceof FieldReference) {
            if (!(o.getMemberReference() instanceof FieldReference)) {
                return -1;
            }
            return ((FieldReference) reference).compareTo((FieldReference) o.getMemberReference());
        } else if (!(o.getMemberReference() instanceof MethodReference)) {
            return 1;
        } else {
            return ((MethodReference) reference).compareTo((MethodReference) o.getMemberReference());
        }
    }

    public String toString() {
        return DexFormatter.INSTANCE.getMethodHandle(this);
    }
}
