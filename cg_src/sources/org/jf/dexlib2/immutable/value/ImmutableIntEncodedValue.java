package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseIntEncodedValue;
import org.jf.dexlib2.iface.value.IntEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableIntEncodedValue.class */
public class ImmutableIntEncodedValue extends BaseIntEncodedValue implements ImmutableEncodedValue {
    protected final int value;

    public ImmutableIntEncodedValue(int value) {
        this.value = value;
    }

    public static ImmutableIntEncodedValue of(IntEncodedValue intEncodedValue) {
        if (intEncodedValue instanceof ImmutableIntEncodedValue) {
            return (ImmutableIntEncodedValue) intEncodedValue;
        }
        return new ImmutableIntEncodedValue(intEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.IntEncodedValue
    public int getValue() {
        return this.value;
    }
}
