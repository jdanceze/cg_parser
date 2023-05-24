package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.Internal;
import com.google.protobuf.StructuralMessageInfo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.ConcurrentHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DescriptorMessageInfoFactory.class */
public final class DescriptorMessageInfoFactory implements MessageInfoFactory {
    private static final String GET_DEFAULT_INSTANCE_METHOD_NAME = "getDefaultInstance";
    private static final DescriptorMessageInfoFactory instance = new DescriptorMessageInfoFactory();
    private static final Set<String> specialFieldNames = new HashSet(Arrays.asList("Class", "DefaultInstanceForType", "ParserForType", "SerializedSize", "AllFields", "DescriptorForType", "InitializationErrorString", "UnknownFields", "CachedSize"));
    private static IsInitializedCheckAnalyzer isInitializedCheckAnalyzer = new IsInitializedCheckAnalyzer();

    private DescriptorMessageInfoFactory() {
    }

    public static DescriptorMessageInfoFactory getInstance() {
        return instance;
    }

    @Override // com.google.protobuf.MessageInfoFactory
    public boolean isSupported(Class<?> messageType) {
        return GeneratedMessageV3.class.isAssignableFrom(messageType);
    }

    @Override // com.google.protobuf.MessageInfoFactory
    public MessageInfo messageInfoFor(Class<?> messageType) {
        if (!GeneratedMessageV3.class.isAssignableFrom(messageType)) {
            throw new IllegalArgumentException("Unsupported message type: " + messageType.getName());
        }
        return convert(messageType, descriptorForType(messageType));
    }

    private static Message getDefaultInstance(Class<?> messageType) {
        try {
            java.lang.reflect.Method method = messageType.getDeclaredMethod(GET_DEFAULT_INSTANCE_METHOD_NAME, new Class[0]);
            return (Message) method.invoke(null, new Object[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to get default instance for message class " + messageType.getName(), e);
        }
    }

    private static Descriptors.Descriptor descriptorForType(Class<?> messageType) {
        return getDefaultInstance(messageType).getDescriptorForType();
    }

    private static MessageInfo convert(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        switch (messageDescriptor.getFile().getSyntax()) {
            case PROTO2:
                return convertProto2(messageType, messageDescriptor);
            case PROTO3:
                return convertProto3(messageType, messageDescriptor);
            default:
                throw new IllegalArgumentException("Unsupported syntax: " + messageDescriptor.getFile().getSyntax());
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DescriptorMessageInfoFactory$IsInitializedCheckAnalyzer.class */
    public static class IsInitializedCheckAnalyzer {
        private final Map<Descriptors.Descriptor, Boolean> resultCache = new ConcurrentHashMap();
        private int index = 0;
        private final Stack<Node> stack = new Stack<>();
        private final Map<Descriptors.Descriptor, Node> nodeCache = new HashMap();

        IsInitializedCheckAnalyzer() {
        }

        public boolean needsIsInitializedCheck(Descriptors.Descriptor descriptor) {
            Boolean cachedValue = this.resultCache.get(descriptor);
            if (cachedValue != null) {
                return cachedValue.booleanValue();
            }
            synchronized (this) {
                Boolean cachedValue2 = this.resultCache.get(descriptor);
                if (cachedValue2 != null) {
                    return cachedValue2.booleanValue();
                }
                return dfs(descriptor).component.needsIsInitializedCheck;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DescriptorMessageInfoFactory$IsInitializedCheckAnalyzer$Node.class */
        public static class Node {
            final Descriptors.Descriptor descriptor;
            final int index;
            int lowLink;
            StronglyConnectedComponent component = null;

            Node(Descriptors.Descriptor descriptor, int index) {
                this.descriptor = descriptor;
                this.index = index;
                this.lowLink = index;
            }
        }

        /* JADX INFO: Access modifiers changed from: private */
        /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DescriptorMessageInfoFactory$IsInitializedCheckAnalyzer$StronglyConnectedComponent.class */
        public static class StronglyConnectedComponent {
            final List<Descriptors.Descriptor> messages;
            boolean needsIsInitializedCheck;

            private StronglyConnectedComponent() {
                this.messages = new ArrayList();
                this.needsIsInitializedCheck = false;
            }
        }

        private Node dfs(Descriptors.Descriptor descriptor) {
            Node node;
            int i = this.index;
            this.index = i + 1;
            Node result = new Node(descriptor, i);
            this.stack.push(result);
            this.nodeCache.put(descriptor, result);
            for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                    Node child = this.nodeCache.get(field.getMessageType());
                    if (child == null) {
                        result.lowLink = Math.min(result.lowLink, dfs(field.getMessageType()).lowLink);
                    } else if (child.component == null) {
                        result.lowLink = Math.min(result.lowLink, child.lowLink);
                    }
                }
            }
            if (result.index == result.lowLink) {
                StronglyConnectedComponent component = new StronglyConnectedComponent();
                do {
                    node = this.stack.pop();
                    node.component = component;
                    component.messages.add(node.descriptor);
                } while (node != result);
                analyze(component);
            }
            return result;
        }

        private void analyze(StronglyConnectedComponent component) {
            boolean needsIsInitializedCheck = false;
            Iterator<Descriptors.Descriptor> it = component.messages.iterator();
            loop0: while (true) {
                if (!it.hasNext()) {
                    break;
                }
                Descriptors.Descriptor descriptor = it.next();
                if (descriptor.isExtendable()) {
                    needsIsInitializedCheck = true;
                    break;
                }
                for (Descriptors.FieldDescriptor field : descriptor.getFields()) {
                    if (field.isRequired()) {
                        needsIsInitializedCheck = true;
                        break loop0;
                    } else if (field.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                        Node node = this.nodeCache.get(field.getMessageType());
                        if (node.component != component && node.component.needsIsInitializedCheck) {
                            needsIsInitializedCheck = true;
                            break loop0;
                        }
                    }
                }
            }
            component.needsIsInitializedCheck = needsIsInitializedCheck;
            for (Descriptors.Descriptor descriptor2 : component.messages) {
                this.resultCache.put(descriptor2, Boolean.valueOf(component.needsIsInitializedCheck));
            }
        }
    }

    private static boolean needsIsInitializedCheck(Descriptors.Descriptor descriptor) {
        return isInitializedCheckAnalyzer.needsIsInitializedCheck(descriptor);
    }

    private static StructuralMessageInfo convertProto2(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        List<Descriptors.FieldDescriptor> fieldDescriptors = messageDescriptor.getFields();
        StructuralMessageInfo.Builder builder = StructuralMessageInfo.newBuilder(fieldDescriptors.size());
        builder.withDefaultInstance(getDefaultInstance(messageType));
        builder.withSyntax(ProtoSyntax.PROTO2);
        builder.withMessageSetWireFormat(messageDescriptor.getOptions().getMessageSetWireFormat());
        OneofState oneofState = new OneofState();
        int bitFieldIndex = 0;
        int presenceMask = 1;
        java.lang.reflect.Field bitField = null;
        for (int i = 0; i < fieldDescriptors.size(); i++) {
            final Descriptors.FieldDescriptor fd = fieldDescriptors.get(i);
            boolean enforceUtf8 = fd.getFile().getOptions().getJavaStringCheckUtf8();
            Internal.EnumVerifier enumVerifier = null;
            if (fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                enumVerifier = new Internal.EnumVerifier() { // from class: com.google.protobuf.DescriptorMessageInfoFactory.1
                    @Override // com.google.protobuf.Internal.EnumVerifier
                    public boolean isInRange(int number) {
                        return Descriptors.FieldDescriptor.this.getEnumType().findValueByNumber(number) != null;
                    }
                };
            }
            if (fd.getContainingOneof() != null) {
                builder.withField(buildOneofMember(messageType, fd, oneofState, enforceUtf8, enumVerifier));
            } else {
                java.lang.reflect.Field field = field(messageType, fd);
                int number = fd.getNumber();
                FieldType type = getFieldType(fd);
                if (fd.isMapField()) {
                    final Descriptors.FieldDescriptor valueField = fd.getMessageType().findFieldByNumber(2);
                    if (valueField.getJavaType() == Descriptors.FieldDescriptor.JavaType.ENUM) {
                        enumVerifier = new Internal.EnumVerifier() { // from class: com.google.protobuf.DescriptorMessageInfoFactory.2
                            @Override // com.google.protobuf.Internal.EnumVerifier
                            public boolean isInRange(int number2) {
                                return Descriptors.FieldDescriptor.this.getEnumType().findValueByNumber(number2) != null;
                            }
                        };
                    }
                    builder.withField(FieldInfo.forMapField(field, number, SchemaUtil.getMapDefaultEntry(messageType, fd.getName()), enumVerifier));
                } else if (fd.isRepeated()) {
                    if (enumVerifier != null) {
                        if (fd.isPacked()) {
                            builder.withField(FieldInfo.forPackedFieldWithEnumVerifier(field, number, type, enumVerifier, cachedSizeField(messageType, fd)));
                        } else {
                            builder.withField(FieldInfo.forFieldWithEnumVerifier(field, number, type, enumVerifier));
                        }
                    } else if (fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                        builder.withField(FieldInfo.forRepeatedMessageField(field, number, type, getTypeForRepeatedMessageField(messageType, fd)));
                    } else if (fd.isPacked()) {
                        builder.withField(FieldInfo.forPackedField(field, number, type, cachedSizeField(messageType, fd)));
                    } else {
                        builder.withField(FieldInfo.forField(field, number, type, enforceUtf8));
                    }
                } else {
                    if (bitField == null) {
                        bitField = bitField(messageType, bitFieldIndex);
                    }
                    if (fd.isRequired()) {
                        builder.withField(FieldInfo.forProto2RequiredField(field, number, type, bitField, presenceMask, enforceUtf8, enumVerifier));
                    } else {
                        builder.withField(FieldInfo.forProto2OptionalField(field, number, type, bitField, presenceMask, enforceUtf8, enumVerifier));
                    }
                }
            }
            presenceMask <<= 1;
            if (presenceMask == 0) {
                bitField = null;
                presenceMask = 1;
                bitFieldIndex++;
            }
        }
        List<Integer> fieldsToCheckIsInitialized = new ArrayList<>();
        for (int i2 = 0; i2 < fieldDescriptors.size(); i2++) {
            Descriptors.FieldDescriptor fd2 = fieldDescriptors.get(i2);
            if (fd2.isRequired() || (fd2.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE && needsIsInitializedCheck(fd2.getMessageType()))) {
                fieldsToCheckIsInitialized.add(Integer.valueOf(fd2.getNumber()));
            }
        }
        int[] numbers = new int[fieldsToCheckIsInitialized.size()];
        for (int i3 = 0; i3 < fieldsToCheckIsInitialized.size(); i3++) {
            numbers[i3] = fieldsToCheckIsInitialized.get(i3).intValue();
        }
        builder.withCheckInitialized(numbers);
        return builder.build();
    }

    private static StructuralMessageInfo convertProto3(Class<?> messageType, Descriptors.Descriptor messageDescriptor) {
        List<Descriptors.FieldDescriptor> fieldDescriptors = messageDescriptor.getFields();
        StructuralMessageInfo.Builder builder = StructuralMessageInfo.newBuilder(fieldDescriptors.size());
        builder.withDefaultInstance(getDefaultInstance(messageType));
        builder.withSyntax(ProtoSyntax.PROTO3);
        OneofState oneofState = new OneofState();
        for (int i = 0; i < fieldDescriptors.size(); i++) {
            Descriptors.FieldDescriptor fd = fieldDescriptors.get(i);
            if (fd.getContainingOneof() != null && !fd.getContainingOneof().isSynthetic()) {
                builder.withField(buildOneofMember(messageType, fd, oneofState, true, null));
            } else if (fd.isMapField()) {
                builder.withField(FieldInfo.forMapField(field(messageType, fd), fd.getNumber(), SchemaUtil.getMapDefaultEntry(messageType, fd.getName()), null));
            } else if (fd.isRepeated() && fd.getJavaType() == Descriptors.FieldDescriptor.JavaType.MESSAGE) {
                builder.withField(FieldInfo.forRepeatedMessageField(field(messageType, fd), fd.getNumber(), getFieldType(fd), getTypeForRepeatedMessageField(messageType, fd)));
            } else if (fd.isPacked()) {
                builder.withField(FieldInfo.forPackedField(field(messageType, fd), fd.getNumber(), getFieldType(fd), cachedSizeField(messageType, fd)));
            } else {
                builder.withField(FieldInfo.forField(field(messageType, fd), fd.getNumber(), getFieldType(fd), true));
            }
        }
        return builder.build();
    }

    private static FieldInfo buildOneofMember(Class<?> messageType, Descriptors.FieldDescriptor fd, OneofState oneofState, boolean enforceUtf8, Internal.EnumVerifier enumVerifier) {
        OneofInfo oneof = oneofState.getOneof(messageType, fd.getContainingOneof());
        FieldType type = getFieldType(fd);
        Class<?> oneofStoredType = getOneofStoredType(messageType, fd, type);
        return FieldInfo.forOneofMemberField(fd.getNumber(), type, oneof, oneofStoredType, enforceUtf8, enumVerifier);
    }

    private static Class<?> getOneofStoredType(Class<?> messageType, Descriptors.FieldDescriptor fd, FieldType type) {
        switch (type.getJavaType()) {
            case BOOLEAN:
                return Boolean.class;
            case BYTE_STRING:
                return ByteString.class;
            case DOUBLE:
                return Double.class;
            case FLOAT:
                return Float.class;
            case ENUM:
            case INT:
                return Integer.class;
            case LONG:
                return Long.class;
            case STRING:
                return String.class;
            case MESSAGE:
                return getOneofStoredTypeForMessage(messageType, fd);
            default:
                throw new IllegalArgumentException("Invalid type for oneof: " + type);
        }
    }

    private static FieldType getFieldType(Descriptors.FieldDescriptor fd) {
        switch (fd.getType()) {
            case BOOL:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.BOOL_LIST_PACKED : FieldType.BOOL_LIST;
                }
                return FieldType.BOOL;
            case BYTES:
                return fd.isRepeated() ? FieldType.BYTES_LIST : FieldType.BYTES;
            case DOUBLE:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.DOUBLE_LIST_PACKED : FieldType.DOUBLE_LIST;
                }
                return FieldType.DOUBLE;
            case ENUM:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.ENUM_LIST_PACKED : FieldType.ENUM_LIST;
                }
                return FieldType.ENUM;
            case FIXED32:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.FIXED32_LIST_PACKED : FieldType.FIXED32_LIST;
                }
                return FieldType.FIXED32;
            case FIXED64:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.FIXED64_LIST_PACKED : FieldType.FIXED64_LIST;
                }
                return FieldType.FIXED64;
            case FLOAT:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.FLOAT_LIST_PACKED : FieldType.FLOAT_LIST;
                }
                return FieldType.FLOAT;
            case GROUP:
                return fd.isRepeated() ? FieldType.GROUP_LIST : FieldType.GROUP;
            case INT32:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.INT32_LIST_PACKED : FieldType.INT32_LIST;
                }
                return FieldType.INT32;
            case INT64:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.INT64_LIST_PACKED : FieldType.INT64_LIST;
                }
                return FieldType.INT64;
            case MESSAGE:
                if (fd.isMapField()) {
                    return FieldType.MAP;
                }
                return fd.isRepeated() ? FieldType.MESSAGE_LIST : FieldType.MESSAGE;
            case SFIXED32:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.SFIXED32_LIST_PACKED : FieldType.SFIXED32_LIST;
                }
                return FieldType.SFIXED32;
            case SFIXED64:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.SFIXED64_LIST_PACKED : FieldType.SFIXED64_LIST;
                }
                return FieldType.SFIXED64;
            case SINT32:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.SINT32_LIST_PACKED : FieldType.SINT32_LIST;
                }
                return FieldType.SINT32;
            case SINT64:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.SINT64_LIST_PACKED : FieldType.SINT64_LIST;
                }
                return FieldType.SINT64;
            case STRING:
                return fd.isRepeated() ? FieldType.STRING_LIST : FieldType.STRING;
            case UINT32:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.UINT32_LIST_PACKED : FieldType.UINT32_LIST;
                }
                return FieldType.UINT32;
            case UINT64:
                if (fd.isRepeated()) {
                    return fd.isPacked() ? FieldType.UINT64_LIST_PACKED : FieldType.UINT64_LIST;
                }
                return FieldType.UINT64;
            default:
                throw new IllegalArgumentException("Unsupported field type: " + fd.getType());
        }
    }

    private static java.lang.reflect.Field bitField(Class<?> messageType, int index) {
        return field(messageType, "bitField" + index + "_");
    }

    private static java.lang.reflect.Field field(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        return field(messageType, getFieldName(fd));
    }

    private static java.lang.reflect.Field cachedSizeField(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        return field(messageType, getCachedSizeFieldName(fd));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static java.lang.reflect.Field field(Class<?> messageType, String fieldName) {
        try {
            return messageType.getDeclaredField(fieldName);
        } catch (Exception e) {
            throw new IllegalArgumentException("Unable to find field " + fieldName + " in message class " + messageType.getName());
        }
    }

    static String getFieldName(Descriptors.FieldDescriptor fd) {
        String name;
        String suffix;
        if (fd.getType() == Descriptors.FieldDescriptor.Type.GROUP) {
            name = fd.getMessageType().getName();
        } else {
            name = fd.getName();
        }
        String name2 = name;
        String upperCamelCaseName = snakeCaseToUpperCamelCase(name2);
        if (specialFieldNames.contains(upperCamelCaseName)) {
            suffix = "__";
        } else {
            suffix = "_";
        }
        return snakeCaseToLowerCamelCase(name2) + suffix;
    }

    private static String getCachedSizeFieldName(Descriptors.FieldDescriptor fd) {
        return snakeCaseToLowerCamelCase(fd.getName()) + "MemoizedSerializedSize";
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String snakeCaseToLowerCamelCase(String snakeCase) {
        return snakeCaseToCamelCase(snakeCase, false);
    }

    private static String snakeCaseToUpperCamelCase(String snakeCase) {
        return snakeCaseToCamelCase(snakeCase, true);
    }

    private static String snakeCaseToCamelCase(String snakeCase, boolean capFirst) {
        StringBuilder sb = new StringBuilder(snakeCase.length() + 1);
        boolean capNext = capFirst;
        for (int ctr = 0; ctr < snakeCase.length(); ctr++) {
            char next = snakeCase.charAt(ctr);
            if (next == '_') {
                capNext = true;
            } else if (Character.isDigit(next)) {
                sb.append(next);
                capNext = true;
            } else if (capNext) {
                sb.append(Character.toUpperCase(next));
                capNext = false;
            } else if (ctr == 0) {
                sb.append(Character.toLowerCase(next));
            } else {
                sb.append(next);
            }
        }
        return sb.toString();
    }

    private static Class<?> getOneofStoredTypeForMessage(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        try {
            String name = fd.getType() == Descriptors.FieldDescriptor.Type.GROUP ? fd.getMessageType().getName() : fd.getName();
            java.lang.reflect.Method getter = messageType.getDeclaredMethod(getterForField(name), new Class[0]);
            return getter.getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class<?> getTypeForRepeatedMessageField(Class<?> messageType, Descriptors.FieldDescriptor fd) {
        try {
            String name = fd.getType() == Descriptors.FieldDescriptor.Type.GROUP ? fd.getMessageType().getName() : fd.getName();
            java.lang.reflect.Method getter = messageType.getDeclaredMethod(getterForField(name), Integer.TYPE);
            return getter.getReturnType();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String getterForField(String snakeCase) {
        String camelCase = snakeCaseToLowerCamelCase(snakeCase);
        return "get" + Character.toUpperCase(camelCase.charAt(0)) + camelCase.substring(1, camelCase.length());
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DescriptorMessageInfoFactory$OneofState.class */
    public static final class OneofState {
        private OneofInfo[] oneofs;

        private OneofState() {
            this.oneofs = new OneofInfo[2];
        }

        OneofInfo getOneof(Class<?> messageType, Descriptors.OneofDescriptor desc) {
            int index = desc.getIndex();
            if (index >= this.oneofs.length) {
                this.oneofs = (OneofInfo[]) Arrays.copyOf(this.oneofs, index * 2);
            }
            OneofInfo info = this.oneofs[index];
            if (info == null) {
                info = newInfo(messageType, desc);
                this.oneofs[index] = info;
            }
            return info;
        }

        private static OneofInfo newInfo(Class<?> messageType, Descriptors.OneofDescriptor desc) {
            String camelCase = DescriptorMessageInfoFactory.snakeCaseToLowerCamelCase(desc.getName());
            String valueFieldName = camelCase + "_";
            String caseFieldName = camelCase + "Case_";
            return new OneofInfo(desc.getIndex(), DescriptorMessageInfoFactory.field(messageType, caseFieldName), DescriptorMessageInfoFactory.field(messageType, valueFieldName));
        }
    }
}
