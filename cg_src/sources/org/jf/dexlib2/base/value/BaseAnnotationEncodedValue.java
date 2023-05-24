package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.AnnotationEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.util.CollectionUtils;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseAnnotationEncodedValue.class */
public abstract class BaseAnnotationEncodedValue implements AnnotationEncodedValue {
    @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue
    public int hashCode() {
        int hashCode = getType().hashCode();
        return (hashCode * 31) + getElements().hashCode();
    }

    @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue
    public boolean equals(@Nullable Object o) {
        if (o instanceof AnnotationEncodedValue) {
            AnnotationEncodedValue other = (AnnotationEncodedValue) o;
            return getType().equals(other.getType()) && getElements().equals(other.getElements());
        }
        return false;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.AnnotationEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        if (res != 0) {
            return res;
        }
        AnnotationEncodedValue other = (AnnotationEncodedValue) o;
        int res2 = getType().compareTo(other.getType());
        return res2 != 0 ? res2 : CollectionUtils.compareAsSet(getElements(), other.getElements());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 29;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
