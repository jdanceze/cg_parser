package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseDoubleEncodedValue;
import org.jf.dexlib2.iface.value.DoubleEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableDoubleEncodedValue.class */
public class ImmutableDoubleEncodedValue extends BaseDoubleEncodedValue implements ImmutableEncodedValue {
    protected final double value;

    public ImmutableDoubleEncodedValue(double value) {
        this.value = value;
    }

    public static ImmutableDoubleEncodedValue of(DoubleEncodedValue doubleEncodedValue) {
        if (doubleEncodedValue instanceof ImmutableDoubleEncodedValue) {
            return (ImmutableDoubleEncodedValue) doubleEncodedValue;
        }
        return new ImmutableDoubleEncodedValue(doubleEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.DoubleEncodedValue
    public double getValue() {
        return this.value;
    }
}
