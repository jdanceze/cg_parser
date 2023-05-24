package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.FloatEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseFloatEncodedValue.class */
public abstract class BaseFloatEncodedValue implements FloatEncodedValue {
    @Override // org.jf.dexlib2.iface.value.FloatEncodedValue
    public int hashCode() {
        return Float.floatToRawIntBits(getValue());
    }

    @Override // org.jf.dexlib2.iface.value.FloatEncodedValue
    public boolean equals(@Nullable Object o) {
        return o != null && (o instanceof FloatEncodedValue) && Float.floatToRawIntBits(getValue()) == Float.floatToRawIntBits(((FloatEncodedValue) o).getValue());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.FloatEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Float.compare(getValue(), ((FloatEncodedValue) o).getValue());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 16;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
