package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.Descriptors;
import com.google.protobuf.Field;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Option;
import com.google.protobuf.SourceContext;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Type.class */
public final class Type extends GeneratedMessageV3 implements TypeOrBuilder {
    private static final long serialVersionUID = 0;
    public static final int NAME_FIELD_NUMBER = 1;
    private volatile Object name_;
    public static final int FIELDS_FIELD_NUMBER = 2;
    private List<Field> fields_;
    public static final int ONEOFS_FIELD_NUMBER = 3;
    private LazyStringList oneofs_;
    public static final int OPTIONS_FIELD_NUMBER = 4;
    private List<Option> options_;
    public static final int SOURCE_CONTEXT_FIELD_NUMBER = 5;
    private SourceContext sourceContext_;
    public static final int SYNTAX_FIELD_NUMBER = 6;
    private int syntax_;
    private byte memoizedIsInitialized;
    private static final Type DEFAULT_INSTANCE = new Type();
    private static final Parser<Type> PARSER = new AbstractParser<Type>() { // from class: com.google.protobuf.Type.1
        @Override // com.google.protobuf.Parser
        public Type parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            Builder builder = Type.newBuilder();
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

    private Type(GeneratedMessageV3.Builder<?> builder) {
        super(builder);
        this.memoizedIsInitialized = (byte) -1;
    }

    private Type() {
        this.memoizedIsInitialized = (byte) -1;
        this.name_ = "";
        this.fields_ = Collections.emptyList();
        this.oneofs_ = LazyStringArrayList.EMPTY;
        this.options_ = Collections.emptyList();
        this.syntax_ = 0;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.google.protobuf.GeneratedMessageV3
    public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
        return new Type();
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
    public final UnknownFieldSet getUnknownFields() {
        return this.unknownFields;
    }

    public static final Descriptors.Descriptor getDescriptor() {
        return TypeProto.internal_static_google_protobuf_Type_descriptor;
    }

    @Override // com.google.protobuf.GeneratedMessageV3
    protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
        return TypeProto.internal_static_google_protobuf_Type_fieldAccessorTable.ensureFieldAccessorsInitialized(Type.class, Builder.class);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public String getName() {
        Object ref = this.name_;
        if (ref instanceof String) {
            return (String) ref;
        }
        ByteString bs = (ByteString) ref;
        String s = bs.toStringUtf8();
        this.name_ = s;
        return s;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public ByteString getNameBytes() {
        Object ref = this.name_;
        if (ref instanceof String) {
            ByteString b = ByteString.copyFromUtf8((String) ref);
            this.name_ = b;
            return b;
        }
        return (ByteString) ref;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public List<Field> getFieldsList() {
        return this.fields_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public List<? extends FieldOrBuilder> getFieldsOrBuilderList() {
        return this.fields_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public int getFieldsCount() {
        return this.fields_.size();
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public Field getFields(int index) {
        return this.fields_.get(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public FieldOrBuilder getFieldsOrBuilder(int index) {
        return this.fields_.get(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public ProtocolStringList getOneofsList() {
        return this.oneofs_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public int getOneofsCount() {
        return this.oneofs_.size();
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public String getOneofs(int index) {
        return (String) this.oneofs_.get(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public ByteString getOneofsBytes(int index) {
        return this.oneofs_.getByteString(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public List<Option> getOptionsList() {
        return this.options_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
        return this.options_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public int getOptionsCount() {
        return this.options_.size();
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public Option getOptions(int index) {
        return this.options_.get(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public OptionOrBuilder getOptionsOrBuilder(int index) {
        return this.options_.get(index);
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public boolean hasSourceContext() {
        return this.sourceContext_ != null;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public SourceContext getSourceContext() {
        return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public SourceContextOrBuilder getSourceContextOrBuilder() {
        return getSourceContext();
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public int getSyntaxValue() {
        return this.syntax_;
    }

    @Override // com.google.protobuf.TypeOrBuilder
    public Syntax getSyntax() {
        Syntax result = Syntax.valueOf(this.syntax_);
        return result == null ? Syntax.UNRECOGNIZED : result;
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
        if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
            GeneratedMessageV3.writeString(output, 1, this.name_);
        }
        for (int i = 0; i < this.fields_.size(); i++) {
            output.writeMessage(2, this.fields_.get(i));
        }
        for (int i2 = 0; i2 < this.oneofs_.size(); i2++) {
            GeneratedMessageV3.writeString(output, 3, this.oneofs_.getRaw(i2));
        }
        for (int i3 = 0; i3 < this.options_.size(); i3++) {
            output.writeMessage(4, this.options_.get(i3));
        }
        if (this.sourceContext_ != null) {
            output.writeMessage(5, getSourceContext());
        }
        if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
            output.writeEnum(6, this.syntax_);
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
        if (!GeneratedMessageV3.isStringEmpty(this.name_)) {
            size2 = 0 + GeneratedMessageV3.computeStringSize(1, this.name_);
        }
        for (int i = 0; i < this.fields_.size(); i++) {
            size2 += CodedOutputStream.computeMessageSize(2, this.fields_.get(i));
        }
        int dataSize = 0;
        for (int i2 = 0; i2 < this.oneofs_.size(); i2++) {
            dataSize += computeStringSizeNoTag(this.oneofs_.getRaw(i2));
        }
        int size3 = size2 + dataSize + (1 * getOneofsList().size());
        for (int i3 = 0; i3 < this.options_.size(); i3++) {
            size3 += CodedOutputStream.computeMessageSize(4, this.options_.get(i3));
        }
        if (this.sourceContext_ != null) {
            size3 += CodedOutputStream.computeMessageSize(5, getSourceContext());
        }
        if (this.syntax_ != Syntax.SYNTAX_PROTO2.getNumber()) {
            size3 += CodedOutputStream.computeEnumSize(6, this.syntax_);
        }
        int size4 = size3 + getUnknownFields().getSerializedSize();
        this.memoizedSize = size4;
        return size4;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Type)) {
            return super.equals(obj);
        }
        Type other = (Type) obj;
        if (getName().equals(other.getName()) && getFieldsList().equals(other.getFieldsList()) && getOneofsList().equals(other.getOneofsList()) && getOptionsList().equals(other.getOptionsList()) && hasSourceContext() == other.hasSourceContext()) {
            return (!hasSourceContext() || getSourceContext().equals(other.getSourceContext())) && this.syntax_ == other.syntax_ && getUnknownFields().equals(other.getUnknownFields());
        }
        return false;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
    public int hashCode() {
        if (this.memoizedHashCode != 0) {
            return this.memoizedHashCode;
        }
        int hash = (19 * 41) + getDescriptor().hashCode();
        int hash2 = (53 * ((37 * hash) + 1)) + getName().hashCode();
        if (getFieldsCount() > 0) {
            hash2 = (53 * ((37 * hash2) + 2)) + getFieldsList().hashCode();
        }
        if (getOneofsCount() > 0) {
            hash2 = (53 * ((37 * hash2) + 3)) + getOneofsList().hashCode();
        }
        if (getOptionsCount() > 0) {
            hash2 = (53 * ((37 * hash2) + 4)) + getOptionsList().hashCode();
        }
        if (hasSourceContext()) {
            hash2 = (53 * ((37 * hash2) + 5)) + getSourceContext().hashCode();
        }
        int hash3 = (29 * ((53 * ((37 * hash2) + 6)) + this.syntax_)) + getUnknownFields().hashCode();
        this.memoizedHashCode = hash3;
        return hash3;
    }

    public static Type parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Type parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Type parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Type parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Type parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data);
    }

    public static Type parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        return PARSER.parseFrom(data, extensionRegistry);
    }

    public static Type parseFrom(InputStream input) throws IOException {
        return (Type) GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static Type parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (Type) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }

    public static Type parseDelimitedFrom(InputStream input) throws IOException {
        return (Type) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
    }

    public static Type parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (Type) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
    }

    public static Type parseFrom(CodedInputStream input) throws IOException {
        return (Type) GeneratedMessageV3.parseWithIOException(PARSER, input);
    }

    public static Type parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
        return (Type) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder newBuilderForType() {
        return newBuilder();
    }

    public static Builder newBuilder() {
        return DEFAULT_INSTANCE.toBuilder();
    }

    public static Builder newBuilder(Type prototype) {
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

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Type$Builder.class */
    public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements TypeOrBuilder {
        private int bitField0_;
        private Object name_;
        private List<Field> fields_;
        private RepeatedFieldBuilderV3<Field, Field.Builder, FieldOrBuilder> fieldsBuilder_;
        private LazyStringList oneofs_;
        private List<Option> options_;
        private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> optionsBuilder_;
        private SourceContext sourceContext_;
        private SingleFieldBuilderV3<SourceContext, SourceContext.Builder, SourceContextOrBuilder> sourceContextBuilder_;
        private int syntax_;

        public static final Descriptors.Descriptor getDescriptor() {
            return TypeProto.internal_static_google_protobuf_Type_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return TypeProto.internal_static_google_protobuf_Type_fieldAccessorTable.ensureFieldAccessorsInitialized(Type.class, Builder.class);
        }

        private Builder() {
            this.name_ = "";
            this.fields_ = Collections.emptyList();
            this.oneofs_ = LazyStringArrayList.EMPTY;
            this.options_ = Collections.emptyList();
            this.syntax_ = 0;
        }

        private Builder(GeneratedMessageV3.BuilderParent parent) {
            super(parent);
            this.name_ = "";
            this.fields_ = Collections.emptyList();
            this.oneofs_ = LazyStringArrayList.EMPTY;
            this.options_ = Collections.emptyList();
            this.syntax_ = 0;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clear() {
            super.clear();
            this.name_ = "";
            if (this.fieldsBuilder_ == null) {
                this.fields_ = Collections.emptyList();
            } else {
                this.fields_ = null;
                this.fieldsBuilder_.clear();
            }
            this.bitField0_ &= -2;
            this.oneofs_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -3;
            if (this.optionsBuilder_ == null) {
                this.options_ = Collections.emptyList();
            } else {
                this.options_ = null;
                this.optionsBuilder_.clear();
            }
            this.bitField0_ &= -5;
            if (this.sourceContextBuilder_ == null) {
                this.sourceContext_ = null;
            } else {
                this.sourceContext_ = null;
                this.sourceContextBuilder_ = null;
            }
            this.syntax_ = 0;
            return this;
        }

        @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
        public Descriptors.Descriptor getDescriptorForType() {
            return TypeProto.internal_static_google_protobuf_Type_descriptor;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Type getDefaultInstanceForType() {
            return Type.getDefaultInstance();
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Type build() {
            Type result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException((Message) result);
            }
            return result;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Type buildPartial() {
            Type result = new Type(this);
            int i = this.bitField0_;
            result.name_ = this.name_;
            if (this.fieldsBuilder_ != null) {
                result.fields_ = this.fieldsBuilder_.build();
            } else {
                if ((this.bitField0_ & 1) != 0) {
                    this.fields_ = Collections.unmodifiableList(this.fields_);
                    this.bitField0_ &= -2;
                }
                result.fields_ = this.fields_;
            }
            if ((this.bitField0_ & 2) != 0) {
                this.oneofs_ = this.oneofs_.getUnmodifiableView();
                this.bitField0_ &= -3;
            }
            result.oneofs_ = this.oneofs_;
            if (this.optionsBuilder_ != null) {
                result.options_ = this.optionsBuilder_.build();
            } else {
                if ((this.bitField0_ & 4) != 0) {
                    this.options_ = Collections.unmodifiableList(this.options_);
                    this.bitField0_ &= -5;
                }
                result.options_ = this.options_;
            }
            if (this.sourceContextBuilder_ == null) {
                result.sourceContext_ = this.sourceContext_;
            } else {
                result.sourceContext_ = this.sourceContextBuilder_.build();
            }
            result.syntax_ = this.syntax_;
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
            if (other instanceof Type) {
                return mergeFrom((Type) other);
            }
            super.mergeFrom(other);
            return this;
        }

        public Builder mergeFrom(Type other) {
            if (other == Type.getDefaultInstance()) {
                return this;
            }
            if (!other.getName().isEmpty()) {
                this.name_ = other.name_;
                onChanged();
            }
            if (this.fieldsBuilder_ == null) {
                if (!other.fields_.isEmpty()) {
                    if (this.fields_.isEmpty()) {
                        this.fields_ = other.fields_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureFieldsIsMutable();
                        this.fields_.addAll(other.fields_);
                    }
                    onChanged();
                }
            } else if (!other.fields_.isEmpty()) {
                if (!this.fieldsBuilder_.isEmpty()) {
                    this.fieldsBuilder_.addAllMessages(other.fields_);
                } else {
                    this.fieldsBuilder_.dispose();
                    this.fieldsBuilder_ = null;
                    this.fields_ = other.fields_;
                    this.bitField0_ &= -2;
                    this.fieldsBuilder_ = GeneratedMessageV3.alwaysUseFieldBuilders ? getFieldsFieldBuilder() : null;
                }
            }
            if (!other.oneofs_.isEmpty()) {
                if (this.oneofs_.isEmpty()) {
                    this.oneofs_ = other.oneofs_;
                    this.bitField0_ &= -3;
                } else {
                    ensureOneofsIsMutable();
                    this.oneofs_.addAll(other.oneofs_);
                }
                onChanged();
            }
            if (this.optionsBuilder_ == null) {
                if (!other.options_.isEmpty()) {
                    if (this.options_.isEmpty()) {
                        this.options_ = other.options_;
                        this.bitField0_ &= -5;
                    } else {
                        ensureOptionsIsMutable();
                        this.options_.addAll(other.options_);
                    }
                    onChanged();
                }
            } else if (!other.options_.isEmpty()) {
                if (!this.optionsBuilder_.isEmpty()) {
                    this.optionsBuilder_.addAllMessages(other.options_);
                } else {
                    this.optionsBuilder_.dispose();
                    this.optionsBuilder_ = null;
                    this.options_ = other.options_;
                    this.bitField0_ &= -5;
                    this.optionsBuilder_ = GeneratedMessageV3.alwaysUseFieldBuilders ? getOptionsFieldBuilder() : null;
                }
            }
            if (other.hasSourceContext()) {
                mergeSourceContext(other.getSourceContext());
            }
            if (other.syntax_ != 0) {
                setSyntaxValue(other.getSyntaxValue());
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
                            case 10:
                                this.name_ = input.readStringRequireUtf8();
                                break;
                            case 18:
                                Field m = (Field) input.readMessage(Field.parser(), extensionRegistry);
                                if (this.fieldsBuilder_ == null) {
                                    ensureFieldsIsMutable();
                                    this.fields_.add(m);
                                    break;
                                } else {
                                    this.fieldsBuilder_.addMessage(m);
                                    break;
                                }
                            case 26:
                                String s = input.readStringRequireUtf8();
                                ensureOneofsIsMutable();
                                this.oneofs_.add(s);
                                break;
                            case 34:
                                Option m2 = (Option) input.readMessage(Option.parser(), extensionRegistry);
                                if (this.optionsBuilder_ == null) {
                                    ensureOptionsIsMutable();
                                    this.options_.add(m2);
                                    break;
                                } else {
                                    this.optionsBuilder_.addMessage(m2);
                                    break;
                                }
                            case 42:
                                input.readMessage(getSourceContextFieldBuilder().getBuilder(), extensionRegistry);
                                break;
                            case 48:
                                this.syntax_ = input.readEnum();
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

        @Override // com.google.protobuf.TypeOrBuilder
        public String getName() {
            Object ref = this.name_;
            if (!(ref instanceof String)) {
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                this.name_ = s;
                return s;
            }
            return (String) ref;
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        public Builder setName(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.name_ = value;
            onChanged();
            return this;
        }

        public Builder clearName() {
            this.name_ = Type.getDefaultInstance().getName();
            onChanged();
            return this;
        }

        public Builder setNameBytes(ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            AbstractMessageLite.checkByteStringIsUtf8(value);
            this.name_ = value;
            onChanged();
            return this;
        }

        private void ensureFieldsIsMutable() {
            if ((this.bitField0_ & 1) == 0) {
                this.fields_ = new ArrayList(this.fields_);
                this.bitField0_ |= 1;
            }
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public List<Field> getFieldsList() {
            if (this.fieldsBuilder_ == null) {
                return Collections.unmodifiableList(this.fields_);
            }
            return this.fieldsBuilder_.getMessageList();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public int getFieldsCount() {
            if (this.fieldsBuilder_ == null) {
                return this.fields_.size();
            }
            return this.fieldsBuilder_.getCount();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public Field getFields(int index) {
            if (this.fieldsBuilder_ == null) {
                return this.fields_.get(index);
            }
            return this.fieldsBuilder_.getMessage(index);
        }

        public Builder setFields(int index, Field value) {
            if (this.fieldsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureFieldsIsMutable();
                this.fields_.set(index, value);
                onChanged();
            } else {
                this.fieldsBuilder_.setMessage(index, value);
            }
            return this;
        }

        public Builder setFields(int index, Field.Builder builderForValue) {
            if (this.fieldsBuilder_ == null) {
                ensureFieldsIsMutable();
                this.fields_.set(index, builderForValue.build());
                onChanged();
            } else {
                this.fieldsBuilder_.setMessage(index, builderForValue.build());
            }
            return this;
        }

        public Builder addFields(Field value) {
            if (this.fieldsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureFieldsIsMutable();
                this.fields_.add(value);
                onChanged();
            } else {
                this.fieldsBuilder_.addMessage(value);
            }
            return this;
        }

        public Builder addFields(int index, Field value) {
            if (this.fieldsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureFieldsIsMutable();
                this.fields_.add(index, value);
                onChanged();
            } else {
                this.fieldsBuilder_.addMessage(index, value);
            }
            return this;
        }

        public Builder addFields(Field.Builder builderForValue) {
            if (this.fieldsBuilder_ == null) {
                ensureFieldsIsMutable();
                this.fields_.add(builderForValue.build());
                onChanged();
            } else {
                this.fieldsBuilder_.addMessage(builderForValue.build());
            }
            return this;
        }

        public Builder addFields(int index, Field.Builder builderForValue) {
            if (this.fieldsBuilder_ == null) {
                ensureFieldsIsMutable();
                this.fields_.add(index, builderForValue.build());
                onChanged();
            } else {
                this.fieldsBuilder_.addMessage(index, builderForValue.build());
            }
            return this;
        }

        public Builder addAllFields(Iterable<? extends Field> values) {
            if (this.fieldsBuilder_ == null) {
                ensureFieldsIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.fields_);
                onChanged();
            } else {
                this.fieldsBuilder_.addAllMessages(values);
            }
            return this;
        }

        public Builder clearFields() {
            if (this.fieldsBuilder_ == null) {
                this.fields_ = Collections.emptyList();
                this.bitField0_ &= -2;
                onChanged();
            } else {
                this.fieldsBuilder_.clear();
            }
            return this;
        }

        public Builder removeFields(int index) {
            if (this.fieldsBuilder_ == null) {
                ensureFieldsIsMutable();
                this.fields_.remove(index);
                onChanged();
            } else {
                this.fieldsBuilder_.remove(index);
            }
            return this;
        }

        public Field.Builder getFieldsBuilder(int index) {
            return getFieldsFieldBuilder().getBuilder(index);
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public FieldOrBuilder getFieldsOrBuilder(int index) {
            if (this.fieldsBuilder_ == null) {
                return this.fields_.get(index);
            }
            return this.fieldsBuilder_.getMessageOrBuilder(index);
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public List<? extends FieldOrBuilder> getFieldsOrBuilderList() {
            if (this.fieldsBuilder_ != null) {
                return this.fieldsBuilder_.getMessageOrBuilderList();
            }
            return Collections.unmodifiableList(this.fields_);
        }

        public Field.Builder addFieldsBuilder() {
            return getFieldsFieldBuilder().addBuilder(Field.getDefaultInstance());
        }

        public Field.Builder addFieldsBuilder(int index) {
            return getFieldsFieldBuilder().addBuilder(index, Field.getDefaultInstance());
        }

        public List<Field.Builder> getFieldsBuilderList() {
            return getFieldsFieldBuilder().getBuilderList();
        }

        private RepeatedFieldBuilderV3<Field, Field.Builder, FieldOrBuilder> getFieldsFieldBuilder() {
            if (this.fieldsBuilder_ == null) {
                this.fieldsBuilder_ = new RepeatedFieldBuilderV3<>(this.fields_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                this.fields_ = null;
            }
            return this.fieldsBuilder_;
        }

        private void ensureOneofsIsMutable() {
            if ((this.bitField0_ & 2) == 0) {
                this.oneofs_ = new LazyStringArrayList(this.oneofs_);
                this.bitField0_ |= 2;
            }
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public ProtocolStringList getOneofsList() {
            return this.oneofs_.getUnmodifiableView();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public int getOneofsCount() {
            return this.oneofs_.size();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public String getOneofs(int index) {
            return (String) this.oneofs_.get(index);
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public ByteString getOneofsBytes(int index) {
            return this.oneofs_.getByteString(index);
        }

        public Builder setOneofs(int index, String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensureOneofsIsMutable();
            this.oneofs_.set(index, value);
            onChanged();
            return this;
        }

        public Builder addOneofs(String value) {
            if (value == null) {
                throw new NullPointerException();
            }
            ensureOneofsIsMutable();
            this.oneofs_.add(value);
            onChanged();
            return this;
        }

        public Builder addAllOneofs(Iterable<String> values) {
            ensureOneofsIsMutable();
            AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.oneofs_);
            onChanged();
            return this;
        }

        public Builder clearOneofs() {
            this.oneofs_ = LazyStringArrayList.EMPTY;
            this.bitField0_ &= -3;
            onChanged();
            return this;
        }

        public Builder addOneofsBytes(ByteString value) {
            if (value == null) {
                throw new NullPointerException();
            }
            AbstractMessageLite.checkByteStringIsUtf8(value);
            ensureOneofsIsMutable();
            this.oneofs_.add(value);
            onChanged();
            return this;
        }

        private void ensureOptionsIsMutable() {
            if ((this.bitField0_ & 4) == 0) {
                this.options_ = new ArrayList(this.options_);
                this.bitField0_ |= 4;
            }
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public List<Option> getOptionsList() {
            if (this.optionsBuilder_ == null) {
                return Collections.unmodifiableList(this.options_);
            }
            return this.optionsBuilder_.getMessageList();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public int getOptionsCount() {
            if (this.optionsBuilder_ == null) {
                return this.options_.size();
            }
            return this.optionsBuilder_.getCount();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public Option getOptions(int index) {
            if (this.optionsBuilder_ == null) {
                return this.options_.get(index);
            }
            return this.optionsBuilder_.getMessage(index);
        }

        public Builder setOptions(int index, Option value) {
            if (this.optionsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureOptionsIsMutable();
                this.options_.set(index, value);
                onChanged();
            } else {
                this.optionsBuilder_.setMessage(index, value);
            }
            return this;
        }

        public Builder setOptions(int index, Option.Builder builderForValue) {
            if (this.optionsBuilder_ == null) {
                ensureOptionsIsMutable();
                this.options_.set(index, builderForValue.build());
                onChanged();
            } else {
                this.optionsBuilder_.setMessage(index, builderForValue.build());
            }
            return this;
        }

        public Builder addOptions(Option value) {
            if (this.optionsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureOptionsIsMutable();
                this.options_.add(value);
                onChanged();
            } else {
                this.optionsBuilder_.addMessage(value);
            }
            return this;
        }

        public Builder addOptions(int index, Option value) {
            if (this.optionsBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureOptionsIsMutable();
                this.options_.add(index, value);
                onChanged();
            } else {
                this.optionsBuilder_.addMessage(index, value);
            }
            return this;
        }

        public Builder addOptions(Option.Builder builderForValue) {
            if (this.optionsBuilder_ == null) {
                ensureOptionsIsMutable();
                this.options_.add(builderForValue.build());
                onChanged();
            } else {
                this.optionsBuilder_.addMessage(builderForValue.build());
            }
            return this;
        }

        public Builder addOptions(int index, Option.Builder builderForValue) {
            if (this.optionsBuilder_ == null) {
                ensureOptionsIsMutable();
                this.options_.add(index, builderForValue.build());
                onChanged();
            } else {
                this.optionsBuilder_.addMessage(index, builderForValue.build());
            }
            return this;
        }

        public Builder addAllOptions(Iterable<? extends Option> values) {
            if (this.optionsBuilder_ == null) {
                ensureOptionsIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.options_);
                onChanged();
            } else {
                this.optionsBuilder_.addAllMessages(values);
            }
            return this;
        }

        public Builder clearOptions() {
            if (this.optionsBuilder_ == null) {
                this.options_ = Collections.emptyList();
                this.bitField0_ &= -5;
                onChanged();
            } else {
                this.optionsBuilder_.clear();
            }
            return this;
        }

        public Builder removeOptions(int index) {
            if (this.optionsBuilder_ == null) {
                ensureOptionsIsMutable();
                this.options_.remove(index);
                onChanged();
            } else {
                this.optionsBuilder_.remove(index);
            }
            return this;
        }

        public Option.Builder getOptionsBuilder(int index) {
            return getOptionsFieldBuilder().getBuilder(index);
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public OptionOrBuilder getOptionsOrBuilder(int index) {
            if (this.optionsBuilder_ == null) {
                return this.options_.get(index);
            }
            return this.optionsBuilder_.getMessageOrBuilder(index);
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public List<? extends OptionOrBuilder> getOptionsOrBuilderList() {
            if (this.optionsBuilder_ != null) {
                return this.optionsBuilder_.getMessageOrBuilderList();
            }
            return Collections.unmodifiableList(this.options_);
        }

        public Option.Builder addOptionsBuilder() {
            return getOptionsFieldBuilder().addBuilder(Option.getDefaultInstance());
        }

        public Option.Builder addOptionsBuilder(int index) {
            return getOptionsFieldBuilder().addBuilder(index, Option.getDefaultInstance());
        }

        public List<Option.Builder> getOptionsBuilderList() {
            return getOptionsFieldBuilder().getBuilderList();
        }

        private RepeatedFieldBuilderV3<Option, Option.Builder, OptionOrBuilder> getOptionsFieldBuilder() {
            if (this.optionsBuilder_ == null) {
                this.optionsBuilder_ = new RepeatedFieldBuilderV3<>(this.options_, (this.bitField0_ & 4) != 0, getParentForChildren(), isClean());
                this.options_ = null;
            }
            return this.optionsBuilder_;
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public boolean hasSourceContext() {
            return (this.sourceContextBuilder_ == null && this.sourceContext_ == null) ? false : true;
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public SourceContext getSourceContext() {
            if (this.sourceContextBuilder_ == null) {
                return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
            }
            return this.sourceContextBuilder_.getMessage();
        }

        public Builder setSourceContext(SourceContext value) {
            if (this.sourceContextBuilder_ == null) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.sourceContext_ = value;
                onChanged();
            } else {
                this.sourceContextBuilder_.setMessage(value);
            }
            return this;
        }

        public Builder setSourceContext(SourceContext.Builder builderForValue) {
            if (this.sourceContextBuilder_ == null) {
                this.sourceContext_ = builderForValue.build();
                onChanged();
            } else {
                this.sourceContextBuilder_.setMessage(builderForValue.build());
            }
            return this;
        }

        public Builder mergeSourceContext(SourceContext value) {
            if (this.sourceContextBuilder_ == null) {
                if (this.sourceContext_ != null) {
                    this.sourceContext_ = SourceContext.newBuilder(this.sourceContext_).mergeFrom(value).buildPartial();
                } else {
                    this.sourceContext_ = value;
                }
                onChanged();
            } else {
                this.sourceContextBuilder_.mergeFrom(value);
            }
            return this;
        }

        public Builder clearSourceContext() {
            if (this.sourceContextBuilder_ == null) {
                this.sourceContext_ = null;
                onChanged();
            } else {
                this.sourceContext_ = null;
                this.sourceContextBuilder_ = null;
            }
            return this;
        }

        public SourceContext.Builder getSourceContextBuilder() {
            onChanged();
            return getSourceContextFieldBuilder().getBuilder();
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public SourceContextOrBuilder getSourceContextOrBuilder() {
            if (this.sourceContextBuilder_ != null) {
                return this.sourceContextBuilder_.getMessageOrBuilder();
            }
            return this.sourceContext_ == null ? SourceContext.getDefaultInstance() : this.sourceContext_;
        }

        private SingleFieldBuilderV3<SourceContext, SourceContext.Builder, SourceContextOrBuilder> getSourceContextFieldBuilder() {
            if (this.sourceContextBuilder_ == null) {
                this.sourceContextBuilder_ = new SingleFieldBuilderV3<>(getSourceContext(), getParentForChildren(), isClean());
                this.sourceContext_ = null;
            }
            return this.sourceContextBuilder_;
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public int getSyntaxValue() {
            return this.syntax_;
        }

        public Builder setSyntaxValue(int value) {
            this.syntax_ = value;
            onChanged();
            return this;
        }

        @Override // com.google.protobuf.TypeOrBuilder
        public Syntax getSyntax() {
            Syntax result = Syntax.valueOf(this.syntax_);
            return result == null ? Syntax.UNRECOGNIZED : result;
        }

        public Builder setSyntax(Syntax value) {
            if (value == null) {
                throw new NullPointerException();
            }
            this.syntax_ = value.getNumber();
            onChanged();
            return this;
        }

        public Builder clearSyntax() {
            this.syntax_ = 0;
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

    public static Type getDefaultInstance() {
        return DEFAULT_INSTANCE;
    }

    public static Parser<Type> parser() {
        return PARSER;
    }

    @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Parser<Type> getParserForType() {
        return PARSER;
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public Type getDefaultInstanceForType() {
        return DEFAULT_INSTANCE;
    }
}
