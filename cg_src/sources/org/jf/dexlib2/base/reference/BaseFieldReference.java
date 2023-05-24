package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.reference.FieldReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseFieldReference.class */
public abstract class BaseFieldReference extends BaseReference implements FieldReference {
    @Override // org.jf.dexlib2.iface.reference.FieldReference
    public int hashCode() {
        int hashCode = getDefiningClass().hashCode();
        return (((hashCode * 31) + getName().hashCode()) * 31) + getType().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference
    public boolean equals(@Nullable Object o) {
        if (o instanceof FieldReference) {
            FieldReference other = (FieldReference) o;
            return getDefiningClass().equals(other.getDefiningClass()) && getName().equals(other.getName()) && getType().equals(other.getType());
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.reference.FieldReference, java.lang.Comparable
    public int compareTo(@Nonnull FieldReference o) {
        int res = getDefiningClass().compareTo(o.getDefiningClass());
        if (res != 0) {
            return res;
        }
        int res2 = getName().compareTo(o.getName());
        return res2 != 0 ? res2 : getType().compareTo(o.getType());
    }

    public String toString() {
        return DexFormatter.INSTANCE.getFieldDescriptor(this);
    }
}
