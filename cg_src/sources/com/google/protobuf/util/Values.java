package com.google.protobuf.util;

import com.google.protobuf.ListValue;
import com.google.protobuf.NullValue;
import com.google.protobuf.Struct;
import com.google.protobuf.Value;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Values.class */
public final class Values {
    private static final Value NULL_VALUE = Value.newBuilder().setNullValue(NullValue.NULL_VALUE).build();

    public static Value ofNull() {
        return NULL_VALUE;
    }

    public static Value of(boolean value) {
        return Value.newBuilder().setBoolValue(value).build();
    }

    public static Value of(double value) {
        return Value.newBuilder().setNumberValue(value).build();
    }

    public static Value of(String value) {
        return Value.newBuilder().setStringValue(value).build();
    }

    public static Value of(Struct value) {
        return Value.newBuilder().setStructValue(value).build();
    }

    public static Value of(ListValue value) {
        return Value.newBuilder().setListValue(value).build();
    }

    public static Value of(Iterable<Value> values) {
        Value.Builder valueBuilder = Value.newBuilder();
        ListValue.Builder listValue = valueBuilder.getListValueBuilder();
        listValue.addAllValues(values);
        return valueBuilder.build();
    }

    private Values() {
    }
}
