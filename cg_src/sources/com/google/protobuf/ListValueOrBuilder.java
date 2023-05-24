package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ListValueOrBuilder.class */
public interface ListValueOrBuilder extends MessageOrBuilder {
    List<Value> getValuesList();

    Value getValues(int i);

    int getValuesCount();

    List<? extends ValueOrBuilder> getValuesOrBuilderList();

    ValueOrBuilder getValuesOrBuilder(int i);
}
