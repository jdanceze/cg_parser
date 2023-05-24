package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.MessageLite;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Message.class */
public interface Message extends MessageLite, MessageOrBuilder {

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Message$Builder.class */
    public interface Builder extends MessageLite.Builder, MessageOrBuilder {
        @Override // 
        Builder clear();

        Builder mergeFrom(Message message);

        @Override // 
        Message build();

        @Override // 
        Message buildPartial();

        @Override // 
        Builder clone();

        @Override // 
        Builder mergeFrom(CodedInputStream codedInputStream) throws IOException;

        @Override // 
        Builder mergeFrom(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        Descriptors.Descriptor getDescriptorForType();

        Builder newBuilderForField(Descriptors.FieldDescriptor fieldDescriptor);

        Builder getFieldBuilder(Descriptors.FieldDescriptor fieldDescriptor);

        Builder getRepeatedFieldBuilder(Descriptors.FieldDescriptor fieldDescriptor, int i);

        Builder setField(Descriptors.FieldDescriptor fieldDescriptor, Object obj);

        Builder clearField(Descriptors.FieldDescriptor fieldDescriptor);

        Builder clearOneof(Descriptors.OneofDescriptor oneofDescriptor);

        Builder setRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, int i, Object obj);

        Builder addRepeatedField(Descriptors.FieldDescriptor fieldDescriptor, Object obj);

        Builder setUnknownFields(UnknownFieldSet unknownFieldSet);

        Builder mergeUnknownFields(UnknownFieldSet unknownFieldSet);

        @Override // 
        Builder mergeFrom(ByteString byteString) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(ByteString byteString, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(byte[] bArr) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(byte[] bArr, int i, int i2) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(byte[] bArr, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(byte[] bArr, int i, int i2, ExtensionRegistryLite extensionRegistryLite) throws InvalidProtocolBufferException;

        @Override // 
        Builder mergeFrom(InputStream inputStream) throws IOException;

        @Override // 
        Builder mergeFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;

        @Override // com.google.protobuf.MessageLite.Builder
        boolean mergeDelimitedFrom(InputStream inputStream) throws IOException;

        @Override // com.google.protobuf.MessageLite.Builder
        boolean mergeDelimitedFrom(InputStream inputStream, ExtensionRegistryLite extensionRegistryLite) throws IOException;
    }

    @Override // 
    Parser<? extends Message> getParserForType();

    boolean equals(Object obj);

    int hashCode();

    String toString();

    @Override // 
    Builder newBuilderForType();

    @Override // 
    Builder toBuilder();
}
