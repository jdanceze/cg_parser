package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UInt64Value.class */
public final class UInt64Value extends GeneratedMessageV3 implements UInt64ValueOrBuilder {
    private static final long serialVersionUID = 0;
    public static final int VALUE_FIELD_NUMBER = 1;
    private long value_;
    private byte memoizedIsInitialized;
    private static final UInt64Value DEFAULT_INSTANCE = new UInt64Value();
    private static final Parser<UInt64Value> PARSER = new AbstractParser<UInt64Value>() { // from class: com.google.protobuf.UInt64Value.1
        @Override // com.google.protobuf.Parser
        public UInt64Value parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            Builder builder = UInt64Value.newBuilder();
            try {
                builder.mergeFrom(input, extensionRegistry);
                return builder.buildPartial();
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (UninitializedMessageException e2) {
                throw e2.asInvalidProtocolBufferException().setUnfinishedMessage(builder.buildPartial());
            } catch (IOException e3) {
                throw new InvalidProtocolBufferException(e3).setUnfinishedMessage(builder.buildPartial());
            }
        }
    };

    private UInt64Value(GeneratedMessageV3.Builder<?> builder) {
        super(builder);
        this.memoizedIsInitialized = (byte) -1;
    }

    private UInt64Value() {
        this.memoizedIsInitialized = (byte) -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
        return new UInt64Value();
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
    public final UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    public static final Descriptors.Descriptor getDescriptor() {
        return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return WrappersProto.internal_static_google_protobuf_UInt64Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt64Value.class, Builder.class);
    }

    @Override // com.google.protobuf.UInt64ValueOrBuilder
    public long getValue() {
        return this.value_;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
    public final boolean isInitialized() {
        byte isInitialized = this.memoizedIsInitialized;
        if (isInitialized == 1) {
            return true;
        }
        if (isInitialized == 0) {
            return false;
        }
        this.memoizedIsInitialized = (byte) 1;
        return true;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public void writeTo(CodedOutputStream output) throws IOException {
        if (this.value_ != 0) {
            output.writeUInt64(1, this.value_);
        }
        getUnknownFields().writeTo(output);
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public int getSerializedSize() {
        int size = this.memoizedSize;
        if (size != -1) {
            return size;
        }
        int size2 = 0;
        if (this.value_ != 0) {
            size2 = 0 + CodedOutputStream.computeUInt64Size(1, this.value_);
        }
        int size3 = size2 + getUnknownFields().getSerializedSize();
        this.memoizedSize = size3;
        return size3;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof UInt64Value)) {
            return super.equals(obj);
        }
        UInt64Value other = (UInt64Value) obj;
        return getValue() == other.getValue() && getUnknownFields().equals(other.getUnknownFields());
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public int hashCode() {
        if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
        }
        int hash = (19 * 41) + getDescriptor().hashCode();
        int hash2 = (29 * ((53 * ((37 * hash) + 1)) + Internal.hashLong(getValue()))) + getUnknownFields().hashCode();
        this.memoizedHashCode = hash2;
        return hash2;
    }

    public static UInt64Value parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static UInt64Value parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static UInt64Value parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static UInt64Value parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static UInt64Value parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static UInt64Value parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static UInt64Value parseFrom(InputStream input) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static UInt64Value parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static UInt64Value parseDelimitedFrom(InputStream input) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static UInt64Value parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static UInt64Value parseFrom(CodedInputStream input) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static UInt64Value parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (UInt64Value) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder newBuilderForType() {
        return newBuilder();
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(UInt64Value prototype) {
        return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder toBuilder() {
        return this == DEFAULT_INSTANCE ? new Builder() : new Builder().mergeFrom(this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
        Builder builder = new Builder(parent);
        return builder;
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UInt64Value$Builder.class */
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements UInt64ValueOrBuilder {
        private long value_;

        public static final Descriptors.Descriptor getDescriptor() {
            return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return WrappersProto.internal_static_google_protobuf_UInt64Value_fieldAccessorTable.ensureFieldAccessorsInitialized(UInt64Value.class, Builder.class);
        }

        private Builder() {
        }

        private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clear() {
            super.clear();
            this.value_ = 0L;
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
        public Descriptors.Descriptor getDescriptorForType() {
            return WrappersProto.internal_static_google_protobuf_UInt64Value_descriptor;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public UInt64Value getDefaultInstanceForType() {
            return UInt64Value.getDefaultInstance();
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UInt64Value build() {
            UInt64Value result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException((Message) result);
            }
            return result;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UInt64Value buildPartial() {
            UInt64Value result = new UInt64Value(this);
            result.value_ = this.value_;
            onBuilt();
            return result;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
        /* renamed from: clone */
        public Builder mo572clone() {
            return (Builder) super.mo572clone();
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder setField(Descriptors.FieldDescriptor field, Object value) {
            return (Builder) super.setField(field, value);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder clearField(Descriptors.FieldDescriptor field) {
            return (Builder) super.clearField(field);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public Builder clearOneof(Descriptors.OneofDescriptor oneof) {
            return (Builder) super.clearOneof(oneof);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            return (Builder) super.setRepeatedField(field, index, value);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public Builder addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            return (Builder) super.addRepeatedField(field, value);
        }

        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(Message other) {
            if (other instanceof UInt64Value) {
                return mergeFrom((UInt64Value) other);
            }
            super.mergeFrom(other);
            return this;
        }

        public Builder mergeFrom(UInt64Value other) {
            if (other == UInt64Value.getDefaultInstance()) {
                return this;
            }
            if (other.getValue() != 0) {
                setValue(other.getValue());
            }
            mergeUnknownFields(other.getUnknownFields());
            onChanged();
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            try {
                if (extensionRegistry == null) {
                    throw new NullPointerException();
                }
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                this.value_ = input.readUInt64();
                                break;
                            default:
                                if (!super.parseUnknownField(input, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.unwrapIOException();
                    }
                }
                return this;
            } finally {
                onChanged();
            }
        }

        @Override // com.google.protobuf.UInt64ValueOrBuilder
        public long getValue() {
            return this.value_;
        }

        public Builder setValue(long value) {
            this.value_ = value;
            onChanged();
            return this;
        }

        public Builder clearValue() {
            this.value_ = 0L;
            onChanged();
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder
        public final Builder setUnknownFields(UnknownFieldSet unknownFields) {
            return (Builder) super.setUnknownFields(unknownFields);
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
        public final Builder mergeUnknownFields(UnknownFieldSet unknownFields) {
            return (Builder) super.mergeUnknownFields(unknownFields);
        }
    }

    public static UInt64Value getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static UInt64Value of(long value) {
        return newBuilder().setValue(value).build();
    }

    public static Parser<UInt64Value> parser() {
        return PARSER;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Parser<UInt64Value> getParserForType() {
        return PARSER;
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public UInt64Value getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }
}
