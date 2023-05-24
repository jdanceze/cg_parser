package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseStringEncodedValue;
import org.jf.dexlib2.iface.value.StringEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableStringEncodedValue.class */
public class ImmutableStringEncodedValue extends BaseStringEncodedValue implements ImmutableEncodedValue {
    @Nonnull
    protected final String value;

    public ImmutableStringEncodedValue(@Nonnull String value) {
        this.value = value;
    }

    public static ImmutableStringEncodedValue of(@Nonnull StringEncodedValue stringEncodedValue) {
        if (stringEncodedValue instanceof ImmutableStringEncodedValue) {
            return (ImmutableStringEncodedValue) stringEncodedValue;
        }
        return new ImmutableStringEncodedValue(stringEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.StringEncodedValue
    @Nonnull
    public String getValue() {
        return this.value;
    }
}
