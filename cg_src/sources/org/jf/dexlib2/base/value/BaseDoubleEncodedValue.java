package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseDoubleEncodedValue.class */
public abstract class BaseDoubleEncodedValue implements DoubleEncodedValue {
    @Override // org.jf.dexlib2.iface.value.DoubleEncodedValue
    public int hashCode() {
        long v = Double.doubleToRawLongBits(getValue());
        return (int) (v ^ (v >>> 32));
    }

    @Override // org.jf.dexlib2.iface.value.DoubleEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof DoubleEncodedValue) && Double.doubleToRawLongBits(getValue()) == Double.doubleToRawLongBits(((DoubleEncodedValue) o).getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.DoubleEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Double.compare(getValue(), ((DoubleEncodedValue) o).getValue());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 17;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
