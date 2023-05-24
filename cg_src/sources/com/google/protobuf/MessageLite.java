package com.google.protobuf;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MessageLite.class */
public interface MessageLite extends MessageLiteOrBuilder {

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MessageLite$Builder.class */
    public interface Builder extends MessageLiteOrBuilder, Cloneable {
        Builder clear();

        MessageLite build();

        MessageLite buildPartial();

        Builder clone();

        Builder mergeFrom(CodedInputStream codedInputStream) throws IOException;

        Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        Builder mergeFrom(ByteString byteString) throws InvalidProtocolBufferException;

        Builder mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        Builder mergeFrom(byte[] bArr) throws InvalidProtocolBufferException;

        Builder mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        Builder mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        Builder mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        Builder mergeFrom(InputStream inputStream) throws IOException;

        Builder mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        Builder mergeFrom(MessageLite messageLite);

        boolean mergeDelimitedFrom(InputStream inputStream) throws IOException;

        boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;
    }

    void writeTo(CodedOutputStream codedOutputStream) throws IOException;

    int getSerializedSize();

    Parser<? extends MessageLite> getParserForType();

    ByteString toByteString();

    byte[] toByteArray();

    void writeTo(OutputStream outputStream) throws IOException;

    void writeDelimitedTo(OutputStream outputStream) throws IOException;

    Builder newBuilderForType();

    Builder toBuilder();
}
