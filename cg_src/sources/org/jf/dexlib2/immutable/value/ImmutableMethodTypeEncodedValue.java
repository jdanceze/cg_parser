package org.jf.dexlib2.immutable.value;

import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseMethodTypeEncodedValue;
import org.jf.dexlib2.iface.value.MethodTypeEncodedValue;
import org.jf.dexlib2.immutable.reference.ImmutableMethodProtoReference;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableMethodTypeEncodedValue.class */
public class ImmutableMethodTypeEncodedValue extends BaseMethodTypeEncodedValue implements ImmutableEncodedValue {
    @Nonnull
    protected final ImmutableMethodProtoReference methodProtoReference;

    public ImmutableMethodTypeEncodedValue(@Nonnull ImmutableMethodProtoReference methodProtoReference) {
        this.methodProtoReference = methodProtoReference;
    }

    @Nonnull
    public static ImmutableMethodTypeEncodedValue of(@Nonnull MethodTypeEncodedValue methodTypeEncodedValue) {
        if (methodTypeEncodedValue instanceof ImmutableMethodTypeEncodedValue) {
            return (ImmutableMethodTypeEncodedValue) methodTypeEncodedValue;
        }
        return new ImmutableMethodTypeEncodedValue(ImmutableMethodProtoReference.of(methodTypeEncodedValue.getValue()));
    }

    @Override // org.jf.dexlib2.iface.value.MethodTypeEncodedValue
    @Nonnull
    public ImmutableMethodProtoReference getValue() {
        return this.methodProtoReference;
    }
}
