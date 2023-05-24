package org.jf.dexlib2.base.value;

import com.google.common.primitives.Booleans;
import com.google.common.primitives.Ints;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jf.dexlib2.formatter.DexFormatter;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/base/value/BaseBooleanEncodedValue.class */
public abstract class BaseBooleanEncodedValue implements BooleanEncodedValue {
    @Override // org.jf.dexlib2.iface.value.BooleanEncodedValue
    public int hashCode() {
        return getValue() ? 1 : 0;
    }

    @Override // org.jf.dexlib2.iface.value.BooleanEncodedValue
    public boolean equals(@Nullable Object o) {
        return (o instanceof BooleanEncodedValue) && getValue() == ((BooleanEncodedValue) o).getValue();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // org.jf.dexlib2.iface.value.BooleanEncodedValue, java.lang.Comparable
    public int compareTo(@Nonnull EncodedValue o) {
        int res = Ints.compare(getValueType(), o.getValueType());
        return res != 0 ? res : Booleans.compare(getValue(), ((BooleanEncodedValue) o).getValue());
    }

    @Override // org.jf.dexlib2.iface.value.EncodedValue
    public int getValueType() {
        return 31;
    }

    public String toString() {
        return DexFormatter.INSTANCE.getEncodedValue(this);
    }
}
