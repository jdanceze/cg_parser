package org.jf.dexlib2.base.reference;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.iface.reference.StringReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/reference/BaseStringReference.class */
public abstract class BaseStringReference extends BaseReference implements StringReference {
    @Override // org.jf.dexlib2.iface.reference.StringReference
    public int hashCode() {
        return getString().hashCode();
    }

    @Override // org.jf.dexlib2.iface.reference.StringReference
    public boolean equals(@Nullable Object o) {
        if (o != null && (o instanceof StringReference)) {
            return getString().equals(((StringReference) o).getString());
        }
        return false;
    }

    @Override // org.jf.dexlib2.iface.reference.StringReference, java.lang.Comparable
    public int compareTo(@Nonnull CharSequence o) {
        return getString().compareTo(o.toString());
    }

    @Override // java.lang.CharSequence
    public int length() {
        return getString().length();
    }

    @Override // java.lang.CharSequence
    public char charAt(int index) {
        return getString().charAt(index);
    }

    @Override // java.lang.CharSequence
    public CharSequence subSequence(int start, int end) {
        return getString().subSequence(start, end);
    }

    @Override // java.lang.CharSequence
    @Nonnull
    public String toString() {
        return getString();
    }
}
