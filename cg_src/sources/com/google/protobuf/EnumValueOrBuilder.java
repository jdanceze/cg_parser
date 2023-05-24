package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/EnumValueOrBuilder.class */
public interface EnumValueOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    int getNumber();

    List<Option> getOptionsList();

    Option getOptions(int i);

    int getOptionsCount();

    List<? extends OptionOrBuilder> getOptionsOrBuilderList();

    OptionOrBuilder getOptionsOrBuilder(int i);
}
