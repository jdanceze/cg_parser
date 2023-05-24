package com.google.protobuf;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.ByteString;
import com.google.protobuf.MessageLite;
import com.google.protobuf.Writer;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnknownFieldSet.class */
public final class UnknownFieldSet implements MessageLite {
    private final TreeMap<Integer, Field> fields;
    private static final UnknownFieldSet defaultInstance = new UnknownFieldSet(new TreeMap());
    private static final Parser PARSER = new Parser();

    private UnknownFieldSet(TreeMap<Integer, Field> fields) {
        this.fields = fields;
    }

    public static Builder newBuilder() {
        return Builder.create();
    }

    public static Builder newBuilder(UnknownFieldSet copyFrom) {
        return newBuilder().mergeFrom(copyFrom);
    }

    public static UnknownFieldSet getDefaultInstance() {
        return defaultInstance;
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public UnknownFieldSet getDefaultInstanceForType() {
        return defaultInstance;
    }

    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        return (other instanceof UnknownFieldSet) && this.fields.equals(((UnknownFieldSet) other).fields);
    }

    public int hashCode() {
        if (this.fields.isEmpty()) {
            return 0;
        }
        return this.fields.hashCode();
    }

    public Map<Integer, Field> asMap() {
        return (Map) this.fields.clone();
    }

    public boolean hasField(int number) {
        return this.fields.containsKey(Integer.valueOf(number));
    }

    public Field getField(int number) {
        Field result = this.fields.get(Integer.valueOf(number));
        return result == null ? Field.getDefaultInstance() : result;
    }

    @Override // com.google.protobuf.MessageLite
    public void writeTo(CodedOutputStream output) throws IOException {
        for (Map.Entry<Integer, Field> entry : this.fields.entrySet()) {
            Field field = entry.getValue();
            field.writeTo(entry.getKey().intValue(), output);
        }
    }

    public String toString() {
        return TextFormat.printer().printToString(this);
    }

    @Override // com.google.protobuf.MessageLite
    public ByteString toByteString() {
        try {
            ByteString.CodedBuilder out = ByteString.newCodedBuilder(getSerializedSize());
            writeTo(out.getCodedOutput());
            return out.build();
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a ByteString threw an IOException (should never happen).", e);
        }
    }

    @Override // com.google.protobuf.MessageLite
    public byte[] toByteArray() {
        try {
            byte[] result = new byte[getSerializedSize()];
            CodedOutputStream output = CodedOutputStream.newInstance(result);
            writeTo(output);
            output.checkNoSpaceLeft();
            return result;
        } catch (IOException e) {
            throw new RuntimeException("Serializing to a byte array threw an IOException (should never happen).", e);
        }
    }

    @Override // com.google.protobuf.MessageLite
    public void writeTo(OutputStream output) throws IOException {
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output);
        writeTo(codedOutput);
        codedOutput.flush();
    }

    @Override // com.google.protobuf.MessageLite
    public void writeDelimitedTo(OutputStream output) throws IOException {
        CodedOutputStream codedOutput = CodedOutputStream.newInstance(output);
        codedOutput.writeUInt32NoTag(getSerializedSize());
        writeTo(codedOutput);
        codedOutput.flush();
    }

    @Override // com.google.protobuf.MessageLite
    public int getSerializedSize() {
        int result = 0;
        if (!this.fields.isEmpty()) {
            for (Map.Entry<Integer, Field> entry : this.fields.entrySet()) {
                result += entry.getValue().getSerializedSize(entry.getKey().intValue());
            }
        }
        return result;
    }

    public void writeAsMessageSetTo(CodedOutputStream output) throws IOException {
        for (Map.Entry<Integer, Field> entry : this.fields.entrySet()) {
            entry.getValue().writeAsMessageSetExtensionTo(entry.getKey().intValue(), output);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeTo(Writer writer) throws IOException {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            for (Map.Entry<Integer, Field> entry : this.fields.descendingMap().entrySet()) {
                entry.getValue().writeTo(entry.getKey().intValue(), writer);
            }
            return;
        }
        for (Map.Entry<Integer, Field> entry2 : this.fields.entrySet()) {
            entry2.getValue().writeTo(entry2.getKey().intValue(), writer);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeAsMessageSetTo(Writer writer) throws IOException {
        if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
            for (Map.Entry<Integer, Field> entry : this.fields.descendingMap().entrySet()) {
                entry.getValue().writeAsMessageSetExtensionTo(entry.getKey().intValue(), writer);
            }
            return;
        }
        for (Map.Entry<Integer, Field> entry2 : this.fields.entrySet()) {
            entry2.getValue().writeAsMessageSetExtensionTo(entry2.getKey().intValue(), writer);
        }
    }

    public int getSerializedSizeAsMessageSet() {
        int result = 0;
        for (Map.Entry<Integer, Field> entry : this.fields.entrySet()) {
            result += entry.getValue().getSerializedSizeAsMessageSetExtension(entry.getKey().intValue());
        }
        return result;
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder
    public boolean isInitialized() {
        return true;
    }

    public static UnknownFieldSet parseFrom(CodedInputStream input) throws IOException {
        return newBuilder().mergeFrom(input).build();
    }

    public static UnknownFieldSet parseFrom(ByteString data) throws InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).build();
    }

    public static UnknownFieldSet parseFrom(byte[] data) throws InvalidProtocolBufferException {
        return newBuilder().mergeFrom(data).build();
    }

    public static UnknownFieldSet parseFrom(InputStream input) throws IOException {
        return newBuilder().mergeFrom(input).build();
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder newBuilderForType() {
        return newBuilder();
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder toBuilder() {
        return newBuilder().mergeFrom(this);
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnknownFieldSet$Builder.class */
    public static final class Builder implements MessageLite.Builder {
        private TreeMap<Integer, Field.Builder> fieldBuilders = new TreeMap<>();

        private Builder() {
        }

        /* JADX INFO: Access modifiers changed from: private */
        public static Builder create() {
            return new Builder();
        }

        private Field.Builder getFieldBuilder(int number) {
            if (number == 0) {
                return null;
            }
            Field.Builder builder = this.fieldBuilders.get(Integer.valueOf(number));
            if (builder == null) {
                builder = Field.newBuilder();
                this.fieldBuilders.put(Integer.valueOf(number), builder);
            }
            return builder;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UnknownFieldSet build() {
            UnknownFieldSet result;
            if (this.fieldBuilders.isEmpty()) {
                result = UnknownFieldSet.getDefaultInstance();
            } else {
                TreeMap<Integer, Field> fields = new TreeMap<>();
                for (Map.Entry<Integer, Field.Builder> entry : this.fieldBuilders.entrySet()) {
                    fields.put(entry.getKey(), entry.getValue().build());
                }
                result = new UnknownFieldSet(fields);
            }
            return result;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public UnknownFieldSet buildPartial() {
            return build();
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clone() {
            Builder clone = UnknownFieldSet.newBuilder();
            for (Map.Entry<Integer, Field.Builder> entry : this.fieldBuilders.entrySet()) {
                Integer key = entry.getKey();
                Field.Builder value = entry.getValue();
                clone.fieldBuilders.put(key, value.m737clone());
            }
            return clone;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public UnknownFieldSet getDefaultInstanceForType() {
            return UnknownFieldSet.getDefaultInstance();
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder clear() {
            this.fieldBuilders = new TreeMap<>();
            return this;
        }

        public Builder clearField(int number) {
            if (number <= 0) {
                throw new IllegalArgumentException(number + " is not a valid field number.");
            }
            if (this.fieldBuilders.containsKey(Integer.valueOf(number))) {
                this.fieldBuilders.remove(Integer.valueOf(number));
            }
            return this;
        }

        public Builder mergeFrom(UnknownFieldSet other) {
            if (other != UnknownFieldSet.getDefaultInstance()) {
                for (Map.Entry<Integer, Field> entry : other.fields.entrySet()) {
                    mergeField(entry.getKey().intValue(), entry.getValue());
                }
            }
            return this;
        }

        public Builder mergeField(int number, Field field) {
            if (number <= 0) {
                throw new IllegalArgumentException(number + " is not a valid field number.");
            }
            if (hasField(number)) {
                getFieldBuilder(number).mergeFrom(field);
            } else {
                addField(number, field);
            }
            return this;
        }

        public Builder mergeVarintField(int number, int value) {
            if (number <= 0) {
                throw new IllegalArgumentException(number + " is not a valid field number.");
            }
            getFieldBuilder(number).addVarint(value);
            return this;
        }

        public Builder mergeLengthDelimitedField(int number, ByteString value) {
            if (number <= 0) {
                throw new IllegalArgumentException(number + " is not a valid field number.");
            }
            getFieldBuilder(number).addLengthDelimited(value);
            return this;
        }

        public boolean hasField(int number) {
            return this.fieldBuilders.containsKey(Integer.valueOf(number));
        }

        public Builder addField(int number, Field field) {
            if (number <= 0) {
                throw new IllegalArgumentException(number + " is not a valid field number.");
            }
            this.fieldBuilders.put(Integer.valueOf(number), Field.newBuilder(field));
            return this;
        }

        public Map<Integer, Field> asMap() {
            TreeMap<Integer, Field> fields = new TreeMap<>();
            for (Map.Entry<Integer, Field.Builder> entry : this.fieldBuilders.entrySet()) {
                fields.put(entry.getKey(), entry.getValue().build());
            }
            return Collections.unmodifiableMap(fields);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(CodedInputStream input) throws IOException {
            int tag;
            do {
                tag = input.readTag();
                if (tag == 0) {
                    break;
                }
            } while (mergeFieldFrom(tag, input));
            return this;
        }

        public boolean mergeFieldFrom(int tag, CodedInputStream input) throws IOException {
            int number = WireFormat.getTagFieldNumber(tag);
            switch (WireFormat.getTagWireType(tag)) {
                case 0:
                    getFieldBuilder(number).addVarint(input.readInt64());
                    return true;
                case 1:
                    getFieldBuilder(number).addFixed64(input.readFixed64());
                    return true;
                case 2:
                    getFieldBuilder(number).addLengthDelimited(input.readBytes());
                    return true;
                case 3:
                    Builder subBuilder = UnknownFieldSet.newBuilder();
                    input.readGroup(number, subBuilder, ExtensionRegistry.getEmptyRegistry());
                    getFieldBuilder(number).addGroup(subBuilder.build());
                    return true;
                case 4:
                    return false;
                case 5:
                    getFieldBuilder(number).addFixed32(input.readFixed32());
                    return true;
                default:
                    throw InvalidProtocolBufferException.invalidWireType();
            }
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(ByteString data) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = data.newCodedInput();
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (IOException e2) {
                throw new RuntimeException("Reading from a ByteString threw an IOException (should never happen).", e2);
            }
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(byte[] data) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = CodedInputStream.newInstance(data);
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (IOException e2) {
                throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
            }
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(InputStream input) throws IOException {
            CodedInputStream codedInput = CodedInputStream.newInstance(input);
            mergeFrom(codedInput);
            codedInput.checkLastTagWas(0);
            return this;
        }

        @Override // com.google.protobuf.MessageLite.Builder
        public boolean mergeDelimitedFrom(InputStream input) throws IOException {
            int firstByte = input.read();
            if (firstByte == -1) {
                return false;
            }
            int size = CodedInputStream.readRawVarint32(firstByte, input);
            InputStream limitedInput = new AbstractMessageLite.Builder.LimitedInputStream(input, size);
            mergeFrom(limitedInput);
            return true;
        }

        @Override // com.google.protobuf.MessageLite.Builder
        public boolean mergeDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return mergeDelimitedFrom(input);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return mergeFrom(input);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return mergeFrom(data);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(byte[] data, int off, int len) throws InvalidProtocolBufferException {
            try {
                CodedInputStream input = CodedInputStream.newInstance(data, off, len);
                mergeFrom(input);
                input.checkLastTagWas(0);
                return this;
            } catch (InvalidProtocolBufferException e) {
                throw e;
            } catch (IOException e2) {
                throw new RuntimeException("Reading from a byte array threw an IOException (should never happen).", e2);
            }
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return mergeFrom(data);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(byte[] data, int off, int len, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return mergeFrom(data, off, len);
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public Builder mergeFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return mergeFrom(input);
        }

        @Override // com.google.protobuf.MessageLite.Builder
        public Builder mergeFrom(MessageLite m) {
            if (m instanceof UnknownFieldSet) {
                return mergeFrom((UnknownFieldSet) m);
            }
            throw new IllegalArgumentException("mergeFrom(MessageLite) can only merge messages of the same type.");
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder
        public boolean isInitialized() {
            return true;
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnknownFieldSet$Field.class */
    public static final class Field {
        private static final Field fieldDefaultInstance = newBuilder().build();
        private List<Long> varint;
        private List<Integer> fixed32;
        private List<Long> fixed64;
        private List<ByteString> lengthDelimited;
        private List<UnknownFieldSet> group;

        private Field() {
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        public static Builder newBuilder(Field copyFrom) {
            return newBuilder().mergeFrom(copyFrom);
        }

        public static Field getDefaultInstance() {
            return fieldDefaultInstance;
        }

        public List<Long> getVarintList() {
            return this.varint;
        }

        public List<Integer> getFixed32List() {
            return this.fixed32;
        }

        public List<Long> getFixed64List() {
            return this.fixed64;
        }

        public List<ByteString> getLengthDelimitedList() {
            return this.lengthDelimited;
        }

        public List<UnknownFieldSet> getGroupList() {
            return this.group;
        }

        public boolean equals(Object other) {
            if (this == other) {
                return true;
            }
            if (!(other instanceof Field)) {
                return false;
            }
            return Arrays.equals(getIdentityArray(), ((Field) other).getIdentityArray());
        }

        public int hashCode() {
            return Arrays.hashCode(getIdentityArray());
        }

        private Object[] getIdentityArray() {
            return new Object[]{this.varint, this.fixed32, this.fixed64, this.lengthDelimited, this.group};
        }

        public ByteString toByteString(int fieldNumber) {
            try {
                ByteString.CodedBuilder out = ByteString.newCodedBuilder(getSerializedSize(fieldNumber));
                writeTo(fieldNumber, out.getCodedOutput());
                return out.build();
            } catch (IOException e) {
                throw new RuntimeException("Serializing to a ByteString should never fail with an IOException", e);
            }
        }

        public void writeTo(int fieldNumber, CodedOutputStream output) throws IOException {
            for (Long l : this.varint) {
                long value = l.longValue();
                output.writeUInt64(fieldNumber, value);
            }
            for (Integer num : this.fixed32) {
                int value2 = num.intValue();
                output.writeFixed32(fieldNumber, value2);
            }
            for (Long l2 : this.fixed64) {
                long value3 = l2.longValue();
                output.writeFixed64(fieldNumber, value3);
            }
            for (ByteString value4 : this.lengthDelimited) {
                output.writeBytes(fieldNumber, value4);
            }
            for (UnknownFieldSet value5 : this.group) {
                output.writeGroup(fieldNumber, value5);
            }
        }

        public int getSerializedSize(int fieldNumber) {
            int result = 0;
            for (Long l : this.varint) {
                long value = l.longValue();
                result += CodedOutputStream.computeUInt64Size(fieldNumber, value);
            }
            for (Integer num : this.fixed32) {
                int value2 = num.intValue();
                result += CodedOutputStream.computeFixed32Size(fieldNumber, value2);
            }
            for (Long l2 : this.fixed64) {
                long value3 = l2.longValue();
                result += CodedOutputStream.computeFixed64Size(fieldNumber, value3);
            }
            for (ByteString value4 : this.lengthDelimited) {
                result += CodedOutputStream.computeBytesSize(fieldNumber, value4);
            }
            for (UnknownFieldSet value5 : this.group) {
                result += CodedOutputStream.computeGroupSize(fieldNumber, value5);
            }
            return result;
        }

        public void writeAsMessageSetExtensionTo(int fieldNumber, CodedOutputStream output) throws IOException {
            for (ByteString value : this.lengthDelimited) {
                output.writeRawMessageSetExtension(fieldNumber, value);
            }
        }

        void writeTo(int fieldNumber, Writer writer) throws IOException {
            writer.writeInt64List(fieldNumber, this.varint, false);
            writer.writeFixed32List(fieldNumber, this.fixed32, false);
            writer.writeFixed64List(fieldNumber, this.fixed64, false);
            writer.writeBytesList(fieldNumber, this.lengthDelimited);
            if (writer.fieldOrder() == Writer.FieldOrder.ASCENDING) {
                for (int i = 0; i < this.group.size(); i++) {
                    writer.writeStartGroup(fieldNumber);
                    this.group.get(i).writeTo(writer);
                    writer.writeEndGroup(fieldNumber);
                }
                return;
            }
            for (int i2 = this.group.size() - 1; i2 >= 0; i2--) {
                writer.writeEndGroup(fieldNumber);
                this.group.get(i2).writeTo(writer);
                writer.writeStartGroup(fieldNumber);
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        public void writeAsMessageSetExtensionTo(int fieldNumber, Writer writer) throws IOException {
            if (writer.fieldOrder() == Writer.FieldOrder.DESCENDING) {
                ListIterator<ByteString> iter = this.lengthDelimited.listIterator(this.lengthDelimited.size());
                while (iter.hasPrevious()) {
                    writer.writeMessageSetItem(fieldNumber, iter.previous());
                }
                return;
            }
            for (ByteString value : this.lengthDelimited) {
                writer.writeMessageSetItem(fieldNumber, value);
            }
        }

        public int getSerializedSizeAsMessageSetExtension(int fieldNumber) {
            int result = 0;
            for (ByteString value : this.lengthDelimited) {
                result += CodedOutputStream.computeRawMessageSetExtensionSize(fieldNumber, value);
            }
            return result;
        }

        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnknownFieldSet$Field$Builder.class */
        public static final class Builder {
            private Field result = new Field();

            private Builder() {
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                Builder builder = new Builder();
                return builder;
            }

            /* renamed from: clone */
            public Builder m737clone() {
                Field copy = new Field();
                if (this.result.varint == null) {
                    copy.varint = null;
                } else {
                    copy.varint = new ArrayList(this.result.varint);
                }
                if (this.result.fixed32 == null) {
                    copy.fixed32 = null;
                } else {
                    copy.fixed32 = new ArrayList(this.result.fixed32);
                }
                if (this.result.fixed64 == null) {
                    copy.fixed64 = null;
                } else {
                    copy.fixed64 = new ArrayList(this.result.fixed64);
                }
                if (this.result.lengthDelimited == null) {
                    copy.lengthDelimited = null;
                } else {
                    copy.lengthDelimited = new ArrayList(this.result.lengthDelimited);
                }
                if (this.result.group == null) {
                    copy.group = null;
                } else {
                    copy.group = new ArrayList(this.result.group);
                }
                Builder clone = new Builder();
                clone.result = copy;
                return clone;
            }

            public Field build() {
                Field built = new Field();
                if (this.result.varint == null) {
                    built.varint = Collections.emptyList();
                } else {
                    built.varint = Collections.unmodifiableList(new ArrayList(this.result.varint));
                }
                if (this.result.fixed32 == null) {
                    built.fixed32 = Collections.emptyList();
                } else {
                    built.fixed32 = Collections.unmodifiableList(new ArrayList(this.result.fixed32));
                }
                if (this.result.fixed64 == null) {
                    built.fixed64 = Collections.emptyList();
                } else {
                    built.fixed64 = Collections.unmodifiableList(new ArrayList(this.result.fixed64));
                }
                if (this.result.lengthDelimited == null) {
                    built.lengthDelimited = Collections.emptyList();
                } else {
                    built.lengthDelimited = Collections.unmodifiableList(new ArrayList(this.result.lengthDelimited));
                }
                if (this.result.group == null) {
                    built.group = Collections.emptyList();
                } else {
                    built.group = Collections.unmodifiableList(new ArrayList(this.result.group));
                }
                return built;
            }

            public Builder clear() {
                this.result = new Field();
                return this;
            }

            public Builder mergeFrom(Field other) {
                if (!other.varint.isEmpty()) {
                    if (this.result.varint == null) {
                        this.result.varint = new ArrayList();
                    }
                    this.result.varint.addAll(other.varint);
                }
                if (!other.fixed32.isEmpty()) {
                    if (this.result.fixed32 == null) {
                        this.result.fixed32 = new ArrayList();
                    }
                    this.result.fixed32.addAll(other.fixed32);
                }
                if (!other.fixed64.isEmpty()) {
                    if (this.result.fixed64 == null) {
                        this.result.fixed64 = new ArrayList();
                    }
                    this.result.fixed64.addAll(other.fixed64);
                }
                if (!other.lengthDelimited.isEmpty()) {
                    if (this.result.lengthDelimited == null) {
                        this.result.lengthDelimited = new ArrayList();
                    }
                    this.result.lengthDelimited.addAll(other.lengthDelimited);
                }
                if (!other.group.isEmpty()) {
                    if (this.result.group == null) {
                        this.result.group = new ArrayList();
                    }
                    this.result.group.addAll(other.group);
                }
                return this;
            }

            public Builder addVarint(long value) {
                if (this.result.varint == null) {
                    this.result.varint = new ArrayList();
                }
                this.result.varint.add(Long.valueOf(value));
                return this;
            }

            public Builder addFixed32(int value) {
                if (this.result.fixed32 == null) {
                    this.result.fixed32 = new ArrayList();
                }
                this.result.fixed32.add(Integer.valueOf(value));
                return this;
            }

            public Builder addFixed64(long value) {
                if (this.result.fixed64 == null) {
                    this.result.fixed64 = new ArrayList();
                }
                this.result.fixed64.add(Long.valueOf(value));
                return this;
            }

            public Builder addLengthDelimited(ByteString value) {
                if (this.result.lengthDelimited == null) {
                    this.result.lengthDelimited = new ArrayList();
                }
                this.result.lengthDelimited.add(value);
                return this;
            }

            public Builder addGroup(UnknownFieldSet value) {
                if (this.result.group == null) {
                    this.result.group = new ArrayList();
                }
                this.result.group.add(value);
                return this;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnknownFieldSet$Parser.class */
    public static final class Parser extends AbstractParser<UnknownFieldSet> {
        @Override // com.google.protobuf.Parser
        public UnknownFieldSet parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            Builder builder = UnknownFieldSet.newBuilder();
            try {
                builder.mergeFrom(input);
                return builder.buildPartial();
            } catch (InvalidProtocolBufferException e) {
                throw e.setUnfinishedMessage(builder.buildPartial());
            } catch (IOException e2) {
                throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(builder.buildPartial());
            }
        }
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public final Parser getParserForType() {
        return PARSER;
    }
}
