package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/EnumOrBuilder.class */
public interface EnumOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    List<EnumValue> getEnumvalueList();

    EnumValue getEnumvalue(int i);

    int getEnumvalueCount();

    List<? extends EnumValueOrBuilder> getEnumvalueOrBuilderList();

    EnumValueOrBuilder getEnumvalueOrBuilder(int i);

    List<Option> getOptionsList();

    Option getOptions(int i);

    int getOptionsCount();

    List<? extends OptionOrBuilder> getOptionsOrBuilderList();

    OptionOrBuilder getOptionsOrBuilder(int i);

    boolean hasSourceContext();

    SourceContext getSourceContext();

    SourceContextOrBuilder getSourceContextOrBuilder();

    int getSyntaxValue();

    Syntax getSyntax();
}
