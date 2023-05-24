package com.google.protobuf;

import com.google.protobuf.Value;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ValueOrBuilder.class */
public interface ValueOrBuilder extends MessageOrBuilder {
    boolean hasNullValue();

    int getNullValueValue();

    NullValue getNullValue();

    boolean hasNumberValue();

    double getNumberValue();

    boolean hasStringValue();

    String getStringValue();

    ByteString getStringValueBytes();

    boolean hasBoolValue();

    boolean getBoolValue();

    boolean hasStructValue();

    Struct getStructValue();

    StructOrBuilder getStructValueOrBuilder();

    boolean hasListValue();

    ListValue getListValue();

    ListValueOrBuilder getListValueOrBuilder();

    Value.KindCase getKindCase();
}
