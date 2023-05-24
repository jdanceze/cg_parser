package com.google.protobuf;

import com.google.protobuf.Field;
import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/FieldOrBuilder.class */
public interface FieldOrBuilder extends MessageOrBuilder {
    int getKindValue();

    Field.Kind getKind();

    int getCardinalityValue();

    Field.Cardinality getCardinality();

    int getNumber();

    String getName();

    ByteString getNameBytes();

    String getTypeUrl();

    ByteString getTypeUrlBytes();

    int getOneofIndex();

    boolean getPacked();

    List<Option> getOptionsList();

    Option getOptions(int i);

    int getOptionsCount();

    List<? extends OptionOrBuilder> getOptionsOrBuilderList();

    OptionOrBuilder getOptionsOrBuilder(int i);

    String getJsonName();

    ByteString getJsonNameBytes();

    String getDefaultValue();

    ByteString getDefaultValueBytes();
}
