package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodEncodedValue;
import org.jf.dexlib2.iface.value.MethodEncodedValue;
import org.jf.dexlib2.immutable.reference.ImmutableMethodReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableMethodEncodedValue.class */
public class ImmutableMethodEncodedValue extends BaseMethodEncodedValue implements ImmutableEncodedValue {
    @Nonnull
    protected final ImmutableMethodReference value;

    public ImmutableMethodEncodedValue(@Nonnull ImmutableMethodReference value) {
        this.value = value;
    }

    public static ImmutableMethodEncodedValue of(@Nonnull MethodEncodedValue methodEncodedValue) {
        if (methodEncodedValue instanceof ImmutableMethodEncodedValue) {
            return (ImmutableMethodEncodedValue) methodEncodedValue;
        }
        return new ImmutableMethodEncodedValue(ImmutableMethodReference.of(methodEncodedValue.getValue()));
    }

    @Override // org.jf.dexlib2.iface.value.MethodEncodedValue
    @Nonnull
    public ImmutableMethodReference getValue() {
        return this.value;
    }
}
