package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MethodOrBuilder.class */
public interface MethodOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    String getRequestTypeUrl();

    ByteString getRequestTypeUrlBytes();

    boolean getRequestStreaming();

    String getResponseTypeUrl();

    ByteString getResponseTypeUrlBytes();

    boolean getResponseStreaming();

    List<Option> getOptionsList();

    Option getOptions(int i);

    int getOptionsCount();

    List<? extends OptionOrBuilder> getOptionsOrBuilderList();

    OptionOrBuilder getOptionsOrBuilder(int i);

    int getSyntaxValue();

    Syntax getSyntax();
}
