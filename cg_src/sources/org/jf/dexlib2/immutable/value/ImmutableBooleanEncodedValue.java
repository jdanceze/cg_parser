package org.jf.dexlib2.immutable.value;

import org.jf.dexlib2.base.value.BaseBooleanEncodedValue;
import org.jf.dexlib2.iface.value.BooleanEncodedValue;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/immutable/value/ImmutableBooleanEncodedValue.class */
public class ImmutableBooleanEncodedValue extends BaseBooleanEncodedValue implements ImmutableEncodedValue {
    public static final ImmutableBooleanEncodedValue TRUE_VALUE = new ImmutableBooleanEncodedValue(true);
    public static final ImmutableBooleanEncodedValue FALSE_VALUE = new ImmutableBooleanEncodedValue(false);
    protected final boolean value;

    private ImmutableBooleanEncodedValue(boolean value) {
        this.value = value;
    }

    public static ImmutableBooleanEncodedValue forBoolean(boolean value) {
        return value ? TRUE_VALUE : FALSE_VALUE;
    }

    public static ImmutableBooleanEncodedValue of(BooleanEncodedValue booleanEncodedValue) {
        return forBoolean(booleanEncodedValue.getValue());
    }

    @Override // org.jf.dexlib2.iface.value.BooleanEncodedValue
    public boolean getValue() {
        return this.value;
    }
}
