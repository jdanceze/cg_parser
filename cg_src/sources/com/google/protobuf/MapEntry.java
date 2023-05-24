package com.google.protobuf;

import com.google.protobuf.AbstractMessage;
import com.google.protobuf.Descriptors;
import com.google.protobuf.MapEntryLite;
import com.google.protobuf.Message;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapEntry.class */
public final class MapEntry<K, V> extends AbstractMessage {
    private final K key;
    private final V value;
    private final Metadata<K, V> metadata;
    private volatile int cachedSerializedSize;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapEntry$Metadata.class */
    public static final class Metadata<K, V> extends MapEntryLite.Metadata<K, V> {
        public final Descriptors.Descriptor descriptor;
        public final Parser<MapEntry<K, V>> parser;

        public Metadata(Descriptors.Descriptor descriptor, MapEntry<K, V> defaultInstance, WireFormat.FieldType keyType, WireFormat.FieldType valueType) {
            super(keyType, ((MapEntry) defaultInstance).key, valueType, ((MapEntry) defaultInstance).value);
            this.descriptor = descriptor;
            this.parser = new AbstractParser<MapEntry<K, V>>() { // from class: com.google.protobuf.MapEntry.Metadata.1
                @Override // com.google.protobuf.Parser
                public MapEntry<K, V> parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return new MapEntry<>(Metadata.this, input, extensionRegistry);
                }
            };
        }
    }

    private MapEntry(Descriptors.Descriptor descriptor, WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        this.cachedSerializedSize = -1;
        this.key = defaultKey;
        this.value = defaultValue;
        this.metadata = new Metadata<>(descriptor, this, keyType, valueType);
    }

    private MapEntry(Metadata metadata, K key, V value) {
        this.cachedSerializedSize = -1;
        this.key = key;
        this.value = value;
        this.metadata = metadata;
    }

    private MapEntry(Metadata<K, V> metadata, CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
        this.cachedSerializedSize = -1;
        try {
            this.metadata = metadata;
            Map.Entry<K, V> entry = MapEntryLite.parseEntry(input, metadata, extensionRegistry);
            this.key = entry.getKey();
            this.value = entry.getValue();
        } catch (InvalidProtocolBufferException e) {
            throw e.setUnfinishedMessage(this);
        } catch (IOException e2) {
            throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
        }
    }

    public static <K, V> MapEntry<K, V> newDefaultInstance(Descriptors.Descriptor descriptor, WireFormat.FieldType keyType, K defaultKey, WireFormat.FieldType valueType, V defaultValue) {
        return new MapEntry<>(descriptor, keyType, defaultKey, valueType, defaultValue);
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public int getSerializedSize() {
        if (this.cachedSerializedSize != -1) {
            return this.cachedSerializedSize;
        }
        int size = MapEntryLite.computeSerializedSize(this.metadata, this.key, this.value);
        this.cachedSerializedSize = size;
        return size;
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
    public void writeTo(CodedOutputStream output) throws IOException {
        MapEntryLite.writeTo(output, this.metadata, this.key, this.value);
    }

    @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
    public boolean isInitialized() {
        return isInitialized(this.metadata, this.value);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Parser<MapEntry<K, V>> getParserForType() {
        return this.metadata.parser;
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder<K, V> newBuilderForType() {
        return new Builder<>(this.metadata);
    }

    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
    public Builder<K, V> toBuilder() {
        return new Builder<>(this.metadata, this.key, this.value, true, true);
    }

    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
    public MapEntry<K, V> getDefaultInstanceForType() {
        return new MapEntry<>(this.metadata, this.metadata.defaultKey, this.metadata.defaultValue);
    }

    @Override // com.google.protobuf.MessageOrBuilder
    public Descriptors.Descriptor getDescriptorForType() {
        return this.metadata.descriptor;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.protobuf.MessageOrBuilder
    public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
        TreeMap<Descriptors.FieldDescriptor, Object> result = new TreeMap<>();
        for (Descriptors.FieldDescriptor field : this.metadata.descriptor.getFields()) {
            if (hasField(field)) {
                result.put(field, getField(field));
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private void checkFieldDescriptor(Descriptors.FieldDescriptor field) {
        if (field.getContainingType() != this.metadata.descriptor) {
            throw new RuntimeException("Wrong FieldDescriptor \"" + field.getFullName() + "\" used in message \"" + this.metadata.descriptor.getFullName());
        }
    }

    @Override // com.google.protobuf.MessageOrBuilder
    public boolean hasField(Descriptors.FieldDescriptor field) {
        checkFieldDescriptor(field);
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.google.protobuf.MessageOrBuilder
    public Object getField(Descriptors.FieldDescriptor field) {
        checkFieldDescriptor(field);
        Object result = field.getNumber() == 1 ? (K) getKey() : (V) getValue();
        Object result2 = result;
        if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
            result2 = field.getEnumType().findValueByNumberCreatingIfUnknown(((Integer) result).intValue());
        }
        return result2;
    }

    @Override // com.google.protobuf.MessageOrBuilder
    public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
        throw new RuntimeException("There is no repeated field in a map entry message.");
    }

    @Override // com.google.protobuf.MessageOrBuilder
    public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
        throw new RuntimeException("There is no repeated field in a map entry message.");
    }

    @Override // com.google.protobuf.MessageOrBuilder
    public UnknownFieldSet getUnknownFields() {
        return UnknownFieldSet.getDefaultInstance();
    }

    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MapEntry$Builder.class */
    public static class Builder<K, V> extends AbstractMessage.Builder<Builder<K, V>> {
        private final Metadata<K, V> metadata;
        private K key;
        private V value;
        private boolean hasKey;
        private boolean hasValue;

        private Builder(Metadata<K, V> metadata) {
            this(metadata, metadata.defaultKey, metadata.defaultValue, false, false);
        }

        private Builder(Metadata<K, V> metadata, K key, V value, boolean hasKey, boolean hasValue) {
            this.metadata = metadata;
            this.key = key;
            this.value = value;
            this.hasKey = hasKey;
            this.hasValue = hasValue;
        }

        public K getKey() {
            return this.key;
        }

        public V getValue() {
            return this.value;
        }

        public Builder<K, V> setKey(K key) {
            this.key = key;
            this.hasKey = true;
            return this;
        }

        public Builder<K, V> clearKey() {
            this.key = this.metadata.defaultKey;
            this.hasKey = false;
            return this;
        }

        public Builder<K, V> setValue(V value) {
            this.value = value;
            this.hasValue = true;
            return this;
        }

        public Builder<K, V> clearValue() {
            this.value = this.metadata.defaultValue;
            this.hasValue = false;
            return this;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public MapEntry<K, V> build() {
            MapEntry<K, V> result = buildPartial();
            if (!result.isInitialized()) {
                throw newUninitializedMessageException((Message) result);
            }
            return result;
        }

        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
        public MapEntry<K, V> buildPartial() {
            return new MapEntry<>(this.metadata, this.key, this.value);
        }

        @Override // com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
        public Descriptors.Descriptor getDescriptorForType() {
            return this.metadata.descriptor;
        }

        private void checkFieldDescriptor(Descriptors.FieldDescriptor field) {
            if (field.getContainingType() != this.metadata.descriptor) {
                throw new RuntimeException("Wrong FieldDescriptor \"" + field.getFullName() + "\" used in message \"" + this.metadata.descriptor.getFullName());
            }
        }

        @Override // com.google.protobuf.Message.Builder
        public Message.Builder newBuilderForField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            if (field.getNumber() != 2 || field.getJavaType() != Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                throw new RuntimeException("\"" + field.getFullName() + "\" is not a message value field.");
            }
            return ((Message) this.value).newBuilderForType();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.Message.Builder
        public Builder<K, V> setField(Descriptors.FieldDescriptor field, Object value) {
            V v;
            checkFieldDescriptor(field);
            if (value == 0) {
                throw new NullPointerException(field.getFullName() + " is null");
            }
            if (field.getNumber() == 1) {
                setKey(value);
            } else {
                if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
                    v = (K) Integer.valueOf(((Descriptors.EnumValueDescriptor) value).getNumber());
                } else {
                    v = value;
                    if (field.getType() == Descriptors.FieldDescriptor.Type.MESSAGE) {
                        boolean isInstance = this.metadata.defaultValue.getClass().isInstance(value);
                        v = value;
                        if (!isInstance) {
                            v = (K) ((Message) this.metadata.defaultValue).toBuilder().mergeFrom((Message) value).build();
                        }
                    }
                }
                setValue(v);
            }
            return this;
        }

        @Override // com.google.protobuf.Message.Builder
        public Builder<K, V> clearField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            if (field.getNumber() == 1) {
                clearKey();
            } else {
                clearValue();
            }
            return this;
        }

        @Override // com.google.protobuf.Message.Builder
        public Builder<K, V> setRepeatedField(Descriptors.FieldDescriptor field, int index, Object value) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        @Override // com.google.protobuf.Message.Builder
        public Builder<K, V> addRepeatedField(Descriptors.FieldDescriptor field, Object value) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        @Override // com.google.protobuf.Message.Builder
        public Builder<K, V> setUnknownFields(UnknownFieldSet unknownFields) {
            return this;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public MapEntry<K, V> getDefaultInstanceForType() {
            return new MapEntry<>(this.metadata, this.metadata.defaultKey, this.metadata.defaultValue);
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder
        public boolean isInitialized() {
            return MapEntry.isInitialized(this.metadata, this.value);
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.MessageOrBuilder
        public Map<Descriptors.FieldDescriptor, Object> getAllFields() {
            TreeMap<Descriptors.FieldDescriptor, Object> result = new TreeMap<>();
            for (Descriptors.FieldDescriptor field : this.metadata.descriptor.getFields()) {
                if (hasField(field)) {
                    result.put(field, getField(field));
                }
            }
            return Collections.unmodifiableMap(result);
        }

        @Override // com.google.protobuf.MessageOrBuilder
        public boolean hasField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            return field.getNumber() == 1 ? this.hasKey : this.hasValue;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // com.google.protobuf.MessageOrBuilder
        public Object getField(Descriptors.FieldDescriptor field) {
            checkFieldDescriptor(field);
            Object result = field.getNumber() == 1 ? (K) getKey() : (V) getValue();
            Object result2 = result;
            if (field.getType() == Descriptors.FieldDescriptor.Type.ENUM) {
                result2 = field.getEnumType().findValueByNumberCreatingIfUnknown(((Integer) result).intValue());
            }
            return result2;
        }

        @Override // com.google.protobuf.MessageOrBuilder
        public int getRepeatedFieldCount(Descriptors.FieldDescriptor field) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        @Override // com.google.protobuf.MessageOrBuilder
        public Object getRepeatedField(Descriptors.FieldDescriptor field, int index) {
            throw new RuntimeException("There is no repeated field in a map entry message.");
        }

        @Override // com.google.protobuf.MessageOrBuilder
        public UnknownFieldSet getUnknownFields() {
            return UnknownFieldSet.getDefaultInstance();
        }

        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder
        /* renamed from: clone */
        public Builder<K, V> mo572clone() {
            return new Builder<>(this.metadata, this.key, this.value, this.hasKey, this.hasValue);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <V> boolean isInitialized(Metadata metadata, V value) {
        if (metadata.valueType.getJavaType() == WireFormat.JavaType.MESSAGE) {
            return ((MessageLite) value).isInitialized();
        }
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public final Metadata<K, V> getMetadata() {
        return this.metadata;
    }
}
