package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseByteEncodedValue;
import org.jf.dexlib2.iface.value.ByteEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableByteEncodedValue.class */
public class ImmutableByteEncodedValue extends BaseByteEncodedValue implements ImmutableEncodedValue {
    protected final byte value;

    public ImmutableByteEncodedValue(byte value) {
        this.value = value;
    }

    public static ImmutableByteEncodedValue of(ByteEncodedValue byteEncodedValue) {
        if (byteEncodedValue instanceof ImmutableByteEncodedValue) {
            return (ImmutableByteEncodedValue) byteEncodedValue;
        }
        return new ImmutableByteEncodedValue(byteEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.ByteEncodedValue
    public byte getValue() {
        return this.value;
    }
}
