package org.jf.dexlib2.immutable.value;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import javax.annotation.Nonnull;
import org.jf.dexlib2.base.value.BaseArrayEncodedValue;
import org.jf.dexlib2.iface.value.ArrayEncodedValue;
import org.jf.dexlib2.iface.value.EncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableArrayEncodedValue.class */
public class ImmutableArrayEncodedValue extends BaseArrayEncodedValue implements ImmutableEncodedValue {
    @Nonnull
    protected final ImmutableList<? extends ImmutableEncodedValue> value;

    public ImmutableArrayEncodedValue(@Nonnull Collection<? extends EncodedValue> value) {
        this.value = ImmutableEncodedValueFactory.immutableListOf(value);
    }

    public ImmutableArrayEncodedValue(@Nonnull ImmutableList<ImmutableEncodedValue> value) {
        this.value = value;
    }

    public static ImmutableArrayEncodedValue of(@Nonnull ArrayEncodedValue arrayEncodedValue) {
        if (arrayEncodedValue instanceof ImmutableArrayEncodedValue) {
            return (ImmutableArrayEncodedValue) arrayEncodedValue;
        }
        return new ImmutableArrayEncodedValue(arrayEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.ArrayEncodedValue
    @Nonnull
    public ImmutableList<? extends ImmutableEncodedValue> getValue() {
        return this.value;
    }
}
