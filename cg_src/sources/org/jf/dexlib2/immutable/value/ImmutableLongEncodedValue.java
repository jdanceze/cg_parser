package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseLongEncodedValue;
import org.jf.dexlib2.iface.value.LongEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableLongEncodedValue.class */
public class ImmutableLongEncodedValue extends BaseLongEncodedValue implements ImmutableEncodedValue {
    protected final long value;

    public ImmutableLongEncodedValue(long value) {
        this.value = value;
    }

    public static ImmutableLongEncodedValue of(LongEncodedValue longEncodedValue) {
        if (longEncodedValue instanceof ImmutableLongEncodedValue) {
            return (ImmutableLongEncodedValue) longEncodedValue;
        }
        return new ImmutableLongEncodedValue(longEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.LongEncodedValue
    public long getValue() {
        return this.value;
    }
}
