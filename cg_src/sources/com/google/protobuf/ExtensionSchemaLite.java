package com.google.protobuf;

import com.google.protobuf.GeneratedMessageLite;
import com.google.protobuf.MessageLite;
import com.google.protobuf.WireFormat;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/ExtensionSchemaLite.class */
final class ExtensionSchemaLite extends ExtensionSchema<GeneratedMessageLite.ExtensionDescriptor> {
    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public boolean hasExtensions(MessageLite prototype) {
        return prototype instanceof GeneratedMessageLite.ExtendableMessage;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public FieldSet<GeneratedMessageLite.ExtensionDescriptor> getExtensions(Object message) {
        return ((GeneratedMessageLite.ExtendableMessage) message).extensions;
    }

    @Override // com.google.protobuf.ExtensionSchema
    void setExtensions(Object message, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) {
        ((GeneratedMessageLite.ExtendableMessage) message).extensions = extensions;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public FieldSet<GeneratedMessageLite.ExtensionDescriptor> getMutableExtensions(Object message) {
        return ((GeneratedMessageLite.ExtendableMessage) message).ensureExtensionsAreMutable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void makeImmutable(Object message) {
        getExtensions(message).makeImmutable();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v129, types: [java.lang.Object] */
    @Override // com.google.protobuf.ExtensionSchema
    public <UT, UB> UB parseExtension(Object containerMessage, Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions, UB unknownFields, UnknownFieldSchema<UT, UB> unknownFieldSchema) throws IOException {
        List<Integer> list;
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        int fieldNumber = extension.getNumber();
        if (extension.descriptor.isRepeated() && extension.descriptor.isPacked()) {
            switch (extension.getLiteType()) {
                case DOUBLE:
                    List<Integer> arrayList = new ArrayList<>();
                    reader.readDoubleList(arrayList);
                    list = arrayList;
                    break;
                case FLOAT:
                    List<Integer> arrayList2 = new ArrayList<>();
                    reader.readFloatList(arrayList2);
                    list = arrayList2;
                    break;
                case INT64:
                    List<Integer> arrayList3 = new ArrayList<>();
                    reader.readInt64List(arrayList3);
                    list = arrayList3;
                    break;
                case UINT64:
                    List<Integer> arrayList4 = new ArrayList<>();
                    reader.readUInt64List(arrayList4);
                    list = arrayList4;
                    break;
                case INT32:
                    List<Integer> list2 = new ArrayList<>();
                    reader.readInt32List(list2);
                    list = list2;
                    break;
                case FIXED64:
                    List<Integer> arrayList5 = new ArrayList<>();
                    reader.readFixed64List(arrayList5);
                    list = arrayList5;
                    break;
                case FIXED32:
                    List<Integer> list3 = new ArrayList<>();
                    reader.readFixed32List(list3);
                    list = list3;
                    break;
                case BOOL:
                    List<Integer> arrayList6 = new ArrayList<>();
                    reader.readBoolList(arrayList6);
                    list = arrayList6;
                    break;
                case UINT32:
                    List<Integer> list4 = new ArrayList<>();
                    reader.readUInt32List(list4);
                    list = list4;
                    break;
                case SFIXED32:
                    List<Integer> list5 = new ArrayList<>();
                    reader.readSFixed32List(list5);
                    list = list5;
                    break;
                case SFIXED64:
                    List<Integer> arrayList7 = new ArrayList<>();
                    reader.readSFixed64List(arrayList7);
                    list = arrayList7;
                    break;
                case SINT32:
                    List<Integer> list6 = new ArrayList<>();
                    reader.readSInt32List(list6);
                    list = list6;
                    break;
                case SINT64:
                    List<Integer> arrayList8 = new ArrayList<>();
                    reader.readSInt64List(arrayList8);
                    list = arrayList8;
                    break;
                case ENUM:
                    List<Integer> list7 = new ArrayList<>();
                    reader.readEnumList(list7);
                    unknownFields = SchemaUtil.filterUnknownEnumList(containerMessage, fieldNumber, list7, extension.descriptor.getEnumType(), unknownFields, unknownFieldSchema);
                    list = list7;
                    break;
                default:
                    throw new IllegalStateException("Type cannot be packed: " + extension.descriptor.getLiteType());
            }
            extensions.setField(extension.descriptor, list);
        } else {
            Object value = null;
            if (extension.getLiteType() == WireFormat.FieldType.ENUM) {
                int number = reader.readInt32();
                Object enumValue = extension.descriptor.getEnumType().findValueByNumber(number);
                if (enumValue == null) {
                    return (UB) SchemaUtil.storeUnknownEnum(containerMessage, fieldNumber, number, unknownFields, unknownFieldSchema);
                }
                value = Integer.valueOf(number);
            } else {
                switch (extension.getLiteType()) {
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
                        if (!extension.isRepeated()) {
                            Object oldValue = extensions.getField(extension.descriptor);
                            if (oldValue instanceof GeneratedMessageLite) {
                                Schema extSchema = Protobuf.getInstance().schemaFor((Protobuf) oldValue);
                                if (!((GeneratedMessageLite) oldValue).isMutable()) {
                                    Object newValue = extSchema.newInstance();
                                    extSchema.mergeFrom(newValue, oldValue);
                                    extensions.setField(extension.descriptor, newValue);
                                    oldValue = newValue;
                                }
                                reader.mergeGroupField(oldValue, extSchema, extensionRegistry);
                                return unknownFields;
                            }
                        }
                        value = reader.readGroup(extension.getMessageDefaultInstance().getClass(), extensionRegistry);
                        break;
                    case MESSAGE:
                        if (!extension.isRepeated()) {
                            Object oldValue2 = extensions.getField(extension.descriptor);
                            if (oldValue2 instanceof GeneratedMessageLite) {
                                Schema extSchema2 = Protobuf.getInstance().schemaFor((Protobuf) oldValue2);
                                if (!((GeneratedMessageLite) oldValue2).isMutable()) {
                                    Object newValue2 = extSchema2.newInstance();
                                    extSchema2.mergeFrom(newValue2, oldValue2);
                                    extensions.setField(extension.descriptor, newValue2);
                                    oldValue2 = newValue2;
                                }
                                reader.mergeMessageField(oldValue2, extSchema2, extensionRegistry);
                                return unknownFields;
                            }
                        }
                        value = reader.readMessage(extension.getMessageDefaultInstance().getClass(), extensionRegistry);
                        break;
                }
            }
            if (extension.isRepeated()) {
                extensions.addRepeatedField(extension.descriptor, value);
            } else {
                switch (extension.getLiteType()) {
                    case GROUP:
                    case MESSAGE:
                        Object oldValue3 = extensions.getField(extension.descriptor);
                        if (oldValue3 != null) {
                            value = Internal.mergeMessage(oldValue3, value);
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
        GeneratedMessageLite.ExtensionDescriptor descriptor = (GeneratedMessageLite.ExtensionDescriptor) extension.getKey();
        return descriptor.getNumber();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void serializeExtension(Writer writer, Map.Entry<?, ?> extension) throws IOException {
        GeneratedMessageLite.ExtensionDescriptor descriptor = (GeneratedMessageLite.ExtensionDescriptor) extension.getKey();
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
                    SchemaUtil.writeInt32List(descriptor.getNumber(), (List) extension.getValue(), writer, descriptor.isPacked());
                    return;
                case BYTES:
                    SchemaUtil.writeBytesList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case STRING:
                    SchemaUtil.writeStringList(descriptor.getNumber(), (List) extension.getValue(), writer);
                    return;
                case GROUP:
                    List<?> data = (List) extension.getValue();
                    if (data != null && !data.isEmpty()) {
                        SchemaUtil.writeGroupList(descriptor.getNumber(), (List) extension.getValue(), writer, Protobuf.getInstance().schemaFor((Class) data.get(0).getClass()));
                        return;
                    }
                    return;
                case MESSAGE:
                    List<?> data2 = (List) extension.getValue();
                    if (data2 != null && !data2.isEmpty()) {
                        SchemaUtil.writeMessageList(descriptor.getNumber(), (List) extension.getValue(), writer, Protobuf.getInstance().schemaFor((Class) data2.get(0).getClass()));
                        return;
                    }
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
                writer.writeInt32(descriptor.getNumber(), ((Integer) extension.getValue()).intValue());
                return;
            case BYTES:
                writer.writeBytes(descriptor.getNumber(), (ByteString) extension.getValue());
                return;
            case STRING:
                writer.writeString(descriptor.getNumber(), (String) extension.getValue());
                return;
            case GROUP:
                writer.writeGroup(descriptor.getNumber(), extension.getValue(), Protobuf.getInstance().schemaFor((Class) extension.getValue().getClass()));
                return;
            case MESSAGE:
                writer.writeMessage(descriptor.getNumber(), extension.getValue(), Protobuf.getInstance().schemaFor((Class) extension.getValue().getClass()));
                return;
            default:
                return;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public Object findExtensionByNumber(ExtensionRegistryLite extensionRegistry, MessageLite defaultInstance, int number) {
        return extensionRegistry.findLiteExtensionByNumber(defaultInstance, number);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void parseLengthPrefixedMessageSetItem(Reader reader, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) throws IOException {
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        Object value = reader.readMessage(extension.getMessageDefaultInstance().getClass(), extensionRegistry);
        extensions.setField(extension.descriptor, value);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.google.protobuf.ExtensionSchema
    public void parseMessageSetItem(ByteString data, Object extensionObject, ExtensionRegistryLite extensionRegistry, FieldSet<GeneratedMessageLite.ExtensionDescriptor> extensions) throws IOException {
        GeneratedMessageLite.GeneratedExtension<?, ?> extension = (GeneratedMessageLite.GeneratedExtension) extensionObject;
        MessageLite.Builder builder = extension.getMessageDefaultInstance().newBuilderForType();
        CodedInputStream input = data.newCodedInput();
        builder.mergeFrom(input, extensionRegistry);
        extensions.setField(extension.descriptor, builder.buildPartial());
        input.checkLastTagWas(0);
    }
}
