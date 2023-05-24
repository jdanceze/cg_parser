package com.google.protobuf;

import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ApiOrBuilder.class */
public interface ApiOrBuilder extends MessageOrBuilder {
    String getName();

    ByteString getNameBytes();

    List<Method> getMethodsList();

    Method getMethods(int i);

    int getMethodsCount();

    List<? extends MethodOrBuilder> getMethodsOrBuilderList();

    MethodOrBuilder getMethodsOrBuilder(int i);

    List<Option> getOptionsList();

    Option getOptions(int i);

    int getOptionsCount();

    List<? extends OptionOrBuilder> getOptionsOrBuilderList();

    OptionOrBuilder getOptionsOrBuilder(int i);

    String getVersion();

    ByteString getVersionBytes();

    boolean hasSourceContext();

    SourceContext getSourceContext();

    SourceContextOrBuilder getSourceContextOrBuilder();

    List<Mixin> getMixinsList();

    Mixin getMixins(int i);

    int getMixinsCount();

    List<? extends MixinOrBuilder> getMixinsOrBuilderList();

    MixinOrBuilder getMixinsOrBuilder(int i);

    int getSyntaxValue();

    Syntax getSyntax();
}
