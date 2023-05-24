package org.jf.dexlib2.base.value;

import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.EncodedValue;
import org.jf.dexlib2.iface.value.NullEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseNullEncodedValue.class */
public abstract class BaseNullEncodedValue implements NullEncodedValue {
    @Override // org.jf.dexlib2.iface.value.NullEncodedValue
    public int hashCode() {
        return 0;
    }

    @Override // org.jf.dexlib2.iface.value.NullEncodedValue
    public boolean equals(@Nullable Object o) {
        return o instanceof NullEncodedValue;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.NullEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        return Ints.compare(getValueType(), o.getValueType());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 30;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
