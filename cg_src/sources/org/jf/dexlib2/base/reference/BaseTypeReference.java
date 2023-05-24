package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.reference.TypeReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseTypeReference.class */
public abstract class BaseTypeReference extends BaseReference implements TypeReference {
    @Override // org.jf.dexlib2.iface.reference.TypeReference
    public int hashCode() {
        return getType().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference
    public boolean equals(Object o) {
        if (o != null) {
            if (o instanceof TypeReference) {
                return getType().equals(((TypeReference) o).getType());
            }
            if (o instanceof CharSequence) {
                return getType().equals(o.toString());
            }
            return false;
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.reference.TypeReference, java.lang.Comparable
    public int compareTo(@Nonnull CharSequence o) {
        return getType().compareTo(o.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getType().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getType().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getType().subSequence(start, end);
    }

    @Override // java.lang.CharSequence
    @Nonnull
    public String toString() {
        return DexFormatter.INSTANCE.getType(this);
    }
}
