package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseTypeEncodedValue;
import org.jf.dexlib2.iface.value.TypeEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableTypeEncodedValue.class */
public class ImmutableTypeEncodedValue extends BaseTypeEncodedValue implements ImmutableEncodedValue {
    @Nonnull
    protected final String value;

    public ImmutableTypeEncodedValue(@Nonnull String value) {
        this.value = value;
    }

    public static ImmutableTypeEncodedValue of(@Nonnull TypeEncodedValue typeEncodedValue) {
        if (typeEncodedValue instanceof ImmutableTypeEncodedValue) {
            return (ImmutableTypeEncodedValue) typeEncodedValue;
        }
        return new ImmutableTypeEncodedValue(typeEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.TypeEncodedValue
    @Nonnull
    public String getValue() {
        return this.value;
    }
}
