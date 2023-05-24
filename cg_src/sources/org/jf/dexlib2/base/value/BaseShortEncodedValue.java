package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Shorts;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseShortEncodedValue.class */
public abstract class BaseShortEncodedValue implements ShortEncodedValue {
    @Override // org.jf.dexlib2.iface.value.ShortEncodedValue
    public int hashCode() {
        return getValue();
    }

    @Override // org.jf.dexlib2.iface.value.ShortEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof ShortEncodedValue) && getValue() == ((ShortEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.ShortEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Shorts.compare(getValue(), ((ShortEncodedValue) o).getValue());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 2;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
