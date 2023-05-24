package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseShortEncodedValue;
import org.jf.dexlib2.iface.value.ShortEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableShortEncodedValue.class */
public class ImmutableShortEncodedValue extends BaseShortEncodedValue implements ImmutableEncodedValue {
    protected final short value;

    public ImmutableShortEncodedValue(short value) {
        this.value = value;
    }

    public static ImmutableShortEncodedValue of(ShortEncodedValue shortEncodedValue) {
        if (shortEncodedValue instanceof ImmutableShortEncodedValue) {
            return (ImmutableShortEncodedValue) shortEncodedValue;
        }
        return new ImmutableShortEncodedValue(shortEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.ShortEncodedValue
    public short getValue() {
        return this.value;
    }
}
