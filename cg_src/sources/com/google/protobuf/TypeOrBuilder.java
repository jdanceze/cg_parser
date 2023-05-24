package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/TypeOrBuilder.class */
public interface TypeOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    List<Field> getFieldsList();

    Field getFields(int i);

    int getFieldsCount();

    List<? extends FieldOrBuilder> getFieldsOrBuilderList();

    FieldOrBuilder getFieldsOrBuilder(int i);

    List<String> getOneofsList();

    int getOneofsCount();

    String getOneofs(int i);

    ByteString getOneofsBytes(int i);

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
