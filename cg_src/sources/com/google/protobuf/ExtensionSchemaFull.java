package com.google.protobuf;

import android.provider.Contacts;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ExtensionSchemaFull.class */
final class ExtensionSchemaFull extends ExtensionSchema<Descriptors.FieldDescriptor> {
    private static final long EXTENSION_FIELD_OFFSET = getExtensionsFieldOffset();

    ExtensionSchemaFull() {
    }

    private static <T> long getExtensionsFieldOffset() {
        try {
            java.lang.reflect.Field field = GeneratedMessageV3.ExtendableMessage.class.getDeclaredField(Contacts.People.Extensions.CONTENT_DIRECTORY);
            return UnsafeUtil.objectFieldOffset(field);
        } catch (Throwable th) {
            throw new IllegalStateException("Unable to lookup extension field offset");
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public boolean hasExtensions(MessageLite prototype) {
        return prototype instanceof GeneratedMessageV3.ExtendableMessage;
    }

    @Override // com.google.protobuf.ExtensionSchema
    public FieldSet<Descriptors.FieldDescriptor> getExtensions(Object message) {
        return (FieldSet) UnsafeUtil.getObject(message, EXTENSION_FIELD_OFFSET);
    }

    @Override // com.google.protobuf.ExtensionSchema
    void setExtensions(Object message, FieldSet<Descriptors.FieldDescriptor> extensions) {
        UnsafeUtil.putObject(message, EXTENSION_FIELD_OFFSET, extensions);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public FieldSet<Descriptors.FieldDescriptor> getMutableExtensions(Object message) {
        FieldSet<Descriptors.FieldDescriptor> extensions = getExtensions(message);
        if (extensions.isImmutable()) {
            extensions = extensions.m664clone();
            setExtensions(message, extensions);
        }
        return extensions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void makeImmutable(Object message) {
        getExtensions(message).makeImmutable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v112, types: [java.lang.Object] */
    @Override // com.google.protobuf.ExtensionSchema
    public <UT, UB> UB parseExtension(Object containerMessage, Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema) throws IOException {
        List<Descriptors.EnumValueDescriptor> list;
        ExtensionRegistry.ExtensionInfo extension = (ExtensionRegistry.ExtensionInfo) extensionObject;
        int fieldNumber = extension.descriptor.getNumber();
        if (extension.descriptor.isRepeated() && extension.descriptor.isPacked()) {
            switch (extension.descriptor.getLiteType()) {
                case DOUBLE:
                    List<Descriptors.EnumValueDescriptor> arrayList = new ArrayList<>();
                    reader.readDoubleList(arrayList);
                    list = arrayList;
                    break;
                case FLOAT:
                    List<Descriptors.EnumValueDescriptor> arrayList2 = new ArrayList<>();
                    reader.readFloatList(arrayList2);
                    list = arrayList2;
                    break;
                case INT64:
                    List<Descriptors.EnumValueDescriptor> arrayList3 = new ArrayList<>();
                    reader.readInt64List(arrayList3);
                    list = arrayList3;
                    break;
                case UINT64:
                    List<Descriptors.EnumValueDescriptor> arrayList4 = new ArrayList<>();
                    reader.readUInt64List(arrayList4);
                    list = arrayList4;
                    break;
                case INT32:
                    List<Descriptors.EnumValueDescriptor> arrayList5 = new ArrayList<>();
                    reader.readInt32List(arrayList5);
                    list = arrayList5;
                    break;
                case FIXED64:
                    List<Descriptors.EnumValueDescriptor> arrayList6 = new ArrayList<>();
                    reader.readFixed64List(arrayList6);
                    list = arrayList6;
                    break;
                case FIXED32:
                    List<Descriptors.EnumValueDescriptor> arrayList7 = new ArrayList<>();
                    reader.readFixed32List(arrayList7);
                    list = arrayList7;
                    break;
                case BOOL:
                    List<Descriptors.EnumValueDescriptor> arrayList8 = new ArrayList<>();
                    reader.readBoolList(arrayList8);
                    list = arrayList8;
                    break;
                case UINT32:
                    List<Descriptors.EnumValueDescriptor> arrayList9 = new ArrayList<>();
                    reader.readUInt32List(arrayList9);
                    list = arrayList9;
                    break;
                case SFIXED32:
                    List<Descriptors.EnumValueDescriptor> arrayList10 = new ArrayList<>();
                    reader.readSFixed32List(arrayList10);
                    list = arrayList10;
                    break;
                case SFIXED64:
                    List<Descriptors.EnumValueDescriptor> arrayList11 = new ArrayList<>();
                    reader.readSFixed64List(arrayList11);
                    list = arrayList11;
                    break;
                case SINT32:
                    List<Descriptors.EnumValueDescriptor> arrayList12 = new ArrayList<>();
                    reader.readSInt32List(arrayList12);
                    list = arrayList12;
                    break;
                case SINT64:
                    List<Descriptors.EnumValueDescriptor> arrayList13 = new ArrayList<>();
                    reader.readSInt64List(arrayList13);
                    list = arrayList13;
                    break;
                case ENUM:
                    List<Integer> list2 = new ArrayList<>();
                    reader.readEnumList(list2);
                    List<Descriptors.EnumValueDescriptor> enumList = new ArrayList<>();
                    for (Integer num : list2) {
                        int number = num.intValue();
                        Descriptors.EnumValueDescriptor enumDescriptor = extension.descriptor.getEnumType().findValueByNumber(number);
                        if (enumDescriptor != null) {
                            enumList.add(enumDescriptor);
                        } else {
                            unknownFields = SchemaUtil.storeUnknownEnum(containerMessage, fieldNumber, number, unknownFields, unknownFieldSchema);
                        }
                    }
                    list = enumList;
                    break;
                default:
                    throw new IllegalStateException("Type cannot be packed: " + extension.descriptor.getLiteType());
            }
            extensions.setField(extension.descriptor, list);
        } else {
            Object value = null;
            if (extension.descriptor.getLiteType() == WireFormat.FieldType.ENUM) {
                int number2 = reader.readInt32();
                Object enumValue = extension.descriptor.getEnumType().findValueByNumber(number2);
                if (enumValue == null) {
                    return (UB) SchemaUtil.storeUnknownEnum(containerMessage, fieldNumber, number2, unknownFields, unknownFieldSchema);
                }
                value = enumValue;
            } else {
                switch (extension.descriptor.getLiteType()) {
                    case DOUBLE:
                        value = Double.valueOf(reader.readDouble());
                        break;
                    case FLOAT:
                        value = Float.valueOf(reader.readFloat());
                        break;
                    case INT64:
                        value = Long.valueOf(reader.readInt64());
                        break;
                    case UINT64:
                        value = Long.valueOf(reader.readUInt64());
                        break;
                    case INT32:
                        value = Integer.valueOf(reader.readInt32());
                        break;
                    case FIXED64:
                        value = Long.valueOf(reader.readFixed64());
                        break;
                    case FIXED32:
                        value = Integer.valueOf(reader.readFixed32());
                        break;
                    case BOOL:
                        value = Boolean.valueOf(reader.readBool());
                        break;
                    case UINT32:
                        value = Integer.valueOf(reader.readUInt32());
                        break;
                    case SFIXED32:
                        value = Integer.valueOf(reader.readSFixed32());
                        break;
                    case SFIXED64:
                        value = Long.valueOf(reader.readSFixed64());
                        break;
                    case SINT32:
                        value = Integer.valueOf(reader.readSInt32());
                        break;
                    case SINT64:
                        value = Long.valueOf(reader.readSInt64());
                        break;
                    case ENUM:
                        throw new IllegalStateException("Shouldn't reach here.");
                    case BYTES:
                        value = reader.readBytes();
                        break;
                    case STRING:
                        value = reader.readString();
                        break;
                    case GROUP:
                        value = reader.readGroup(extension.defaultInstance.getClass(), extensionRegistry);
                        break;
                    case MESSAGE:
                        value = reader.readMessage(extension.defaultInstance.getClass(), extensionRegistry);
                        break;
                }
            }
            if (extension.descriptor.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value);
            } else {
                switch (extension.descriptor.getLiteType()) {
                    case GROUP:
                    case MESSAGE:
                        Object oldValue = extensions.getField(extension.descriptor);
                        if (oldValue != null) {
                            value = Internal.mergeMessage(oldValue, value);
                            break;
                        }
                        break;
                }
                extensions.setField(extension.descriptor, value);
            }
        }
        return unknownFields;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public int extensionNumber(Map.Entry<?, ?> extension) {
        Descriptors.FieldDescriptor descriptor = (Descriptors.FieldDescriptor) extension.getKey();
        return descriptor.getNumber();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void serializeExtension(Writer writer, Map.Entry<?, ?> extension) throws IOException {
        Descriptors.FieldDescriptor descriptor = (Descriptors.FieldDescriptor) extension.getKey();
        if (descriptor.isRepeated()) {
            switch (descriptor.getLiteType()) {
                case DOUBLE:
                    SchemaUtil.writeDoubleList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FLOAT:
                    SchemaUtil.writeFloatList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case INT64:
                    SchemaUtil.writeInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case UINT64:
                    SchemaUtil.writeUInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case INT32:
                    SchemaUtil.writeInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FIXED64:
                    SchemaUtil.writeFixed64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case FIXED32:
                    SchemaUtil.writeFixed32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case BOOL:
                    SchemaUtil.writeBoolList(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case UINT32:
                    SchemaUtil.writeUInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SFIXED32:
                    SchemaUtil.writeSFixed32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SFIXED64:
                    SchemaUtil.writeSFixed64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SINT32:
                    SchemaUtil.writeSInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case SINT64:
                    SchemaUtil.writeSInt64List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case ENUM:
                    List<Descriptors.EnumValueDescriptor> enumList = (List) extension.getValue();
                    List<Integer> list = new ArrayList<>();
                    for (Descriptors.EnumValueDescriptor d : enumList) {
                        list.add(Integer.valueOf(d.getNumber()));
                    }
                    SchemaUtil.writeInt32List(descriptor.getNumber(), list, writer, descriptor.isPacked());
                    return;
                case BYTES:
                    SchemaUtil.writeBytesList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case STRING:
                    SchemaUtil.writeStringList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case GROUP:
                    SchemaUtil.writeGroupList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case MESSAGE:
                    SchemaUtil.writeMessageList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                default:
                    return;
            }
        }
        switch (descriptor.getLiteType()) {
            case DOUBLE:
                writer.writeDouble(descriptor.getNumber(), ((Double) extension.getValue()).doubleValue());
                return;
            case FLOAT:
                writer.writeFloat(descriptor.getNumber(), ((Float) extension.getValue()).floatValue());
                return;
            case INT64:
                writer.writeInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                return;
            case UINT64:
                writer.writeUInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                return;
            case INT32:
                writer.writeInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case FIXED64:
                writer.writeFixed64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                return;
            case FIXED32:
                writer.writeFixed32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case BOOL:
                writer.writeBool(descriptor.getNumber(), ((Boolean) extension.getValue()).booleanValue());
                return;
            case UINT32:
                writer.writeUInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case SFIXED32:
                writer.writeSFixed32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case SFIXED64:
                writer.writeSFixed64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                return;
            case SINT32:
                writer.writeSInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case SINT64:
                writer.writeSInt64(descriptor.getNumber(), ((Long) extension.getValue()).longValue());
                return;
            case ENUM:
                writer.writeInt32(descriptor.getNumber(), ((Descriptors.EnumValueDescriptor) extension.getValue()).getNumber());
                return;
            case BYTES:
                writer.writeBytes(descriptor.getNumber(), (ByteString) extension.getValue());
                return;
            case STRING:
                writer.writeString(descriptor.getNumber(), (String) extension.getValue());
                return;
            case GROUP:
                writer.writeGroup(descriptor.getNumber(), extension.getValue());
                return;
            case MESSAGE:
                writer.writeMessage(descriptor.getNumber(), extension.getValue());
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public Object findExtensionByNumber(ExtensionRegistryLite extensionRegistry, MessageLite defaultInstance, int number) {
        return ((ExtensionRegistry) extensionRegistry).findImmutableExtensionByNumber(((Message) defaultInstance).getDescriptorForType(), number);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void parseLengthPrefixedMessageSetItem(Reader reader, Object extension, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions) throws IOException {
        ExtensionRegistry.ExtensionInfo extensionInfo = (ExtensionRegistry.ExtensionInfo) extension;
        if (ExtensionRegistryLite.isEagerlyParseMessageSets()) {
            Object value = reader.readMessage(extensionInfo.defaultInstance.getClass(), extensionRegistry);
            extensions.setField(extensionInfo.descriptor, value);
            return;
        }
        extensions.setField(extensionInfo.descriptor, new LazyField(extensionInfo.defaultInstance, extensionRegistry, reader.readBytes()));
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void parseMessageSetItem(ByteString data, Object extension, ExtensionRegistryLite extensionRegistry, FieldSet<Descriptors.FieldDescriptor> extensions) throws IOException {
        ExtensionRegistry.ExtensionInfo extensionInfo = (ExtensionRegistry.ExtensionInfo) extension;
        Message buildPartial = extensionInfo.defaultInstance.newBuilderForType().buildPartial();
        if (ExtensionRegistryLite.isEagerlyParseMessageSets()) {
            Reader reader = BinaryReader.newInstance(ByteBuffer.wrap(data.toByteArray()), true);
            Protobuf.getInstance().mergeFrom(buildPartial, reader, extensionRegistry);
            extensions.setField(extensionInfo.descriptor, buildPartial);
            if (reader.getFieldNumber() != Integer.MAX_VALUE) {
                throw InvalidProtocolBufferException.invalidEndTag();
            }
            return;
        }
        extensions.setField(extensionInfo.descriptor, new LazyField(extensionInfo.defaultInstance, extensionRegistry, data));
    }
}
