package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseCharEncodedValue;
import org.jf.dexlib2.iface.value.CharEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableCharEncodedValue.class */
public class ImmutableCharEncodedValue extends BaseCharEncodedValue implements ImmutableEncodedValue {
    protected final char value;

    public ImmutableCharEncodedValue(char value) {
        this.value = value;
    }

    public static ImmutableCharEncodedValue of(CharEncodedValue charEncodedValue) {
        if (charEncodedValue instanceof ImmutableCharEncodedValue) {
            return (ImmutableCharEncodedValue) charEncodedValue;
        }
        return new ImmutableCharEncodedValue(charEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.CharEncodedValue
    public char getValue() {
        return this.value;
    }
}
