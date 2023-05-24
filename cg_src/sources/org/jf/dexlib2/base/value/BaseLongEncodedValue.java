package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseLongEncodedValue.class */
public abstract class BaseLongEncodedValue implements LongEncodedValue {
    @Override // org.jf.dexlib2.iface.value.LongEncodedValue
    public int hashCode() {
        long value = getValue();
        int hashCode = (int) value;
        return (hashCode * 31) + ((int) (value >>> 32));
    }

    @Override // org.jf.dexlib2.iface.value.LongEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof LongEncodedValue) && getValue() == ((LongEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.LongEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Longs.compare(getValue(), ((LongEncodedValue) o).getValue());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 6;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
