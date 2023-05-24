package soot.dotnet.proto;

import com.google.protobuf.AbstractMessageLite;
import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.ProtocolStringList;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes.class */
public final class ProtoAssemblyAllTypes {
    private static final Descriptors.Descriptor internal_static_AssemblyAllTypes_descriptor = getDescriptor().getMessageTypes().get(0);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_AssemblyAllTypes_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_AssemblyAllTypes_descriptor, new String[]{"ListOfTypes", "AllReferencedModuleTypes"});
    private static final Descriptors.Descriptor internal_static_TypeDefinition_descriptor = getDescriptor().getMessageTypes().get(1);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_TypeDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_TypeDefinition_descriptor, new String[]{"Accessibility", "Fullname", "Namespace", "IsAbstract", "IsReadOnly", "IsSealed", "IsStatic", "DeclaringOuterClass", "DirectBaseTypes", "TypeKind", "Methods", XmlConstants.Tags.fields, "Properties", "NestedTypes", "Attributes", "GenericTypeArguments", "ArrayDimensions", "Events", "PeToken"});
    private static final Descriptors.Descriptor internal_static_MethodDefinition_descriptor = getDescriptor().getMessageTypes().get(2);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_MethodDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_MethodDefinition_descriptor, new String[]{"Accessibility", "Name", "HasBody", "Parameter", "IsAbstract", "IsAccessor", "IsConstructor", "IsDestructor", "IsExplicitInterfaceImplementation", "IsStatic", "IsVirtual", "IsOperator", "IsExtern", "IsUnsafe", "IsSealed", "ReturnType", "Attributes", "FullName", "DeclaringType", "PeToken"});
    private static final Descriptors.Descriptor internal_static_ParameterDefinition_descriptor = getDescriptor().getMessageTypes().get(3);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_ParameterDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_ParameterDefinition_descriptor, new String[]{XmlConstants.Attributes.type, "ParameterName", "IsRef", "IsOut", "IsIn", "IsOptional"});
    private static final Descriptors.Descriptor internal_static_FieldDefinition_descriptor = getDescriptor().getMessageTypes().get(4);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_FieldDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_FieldDefinition_descriptor, new String[]{"Accessibility", "IsAbstract", "IsSealed", "IsExplicitInterfaceImplementation", "IsOverride", "IsVirtual", "IsConst", "IsReadOnly", "IsStatic", XmlConstants.Attributes.type, "TypeKind", "Name", "FullName", "DeclaringType", "Attributes", "PeToken"});
    private static final Descriptors.Descriptor internal_static_PropertyDefinition_descriptor = getDescriptor().getMessageTypes().get(5);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_PropertyDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_PropertyDefinition_descriptor, new String[]{"Accessibility", "CanGet", "CanSet", "IsAbstract", "IsSealed", "IsExplicitInterfaceImplementation", "IsOverride", "IsVirtual", "IsStatic", "IsExtern", "Getter", "Setter", XmlConstants.Attributes.type, "TypeKind", "Name", "Attributes", "PeToken"});
    private static final Descriptors.Descriptor internal_static_AttributeDefinition_descriptor = getDescriptor().getMessageTypes().get(6);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_AttributeDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_AttributeDefinition_descriptor, new String[]{"AttributeType", "Constructor", "FixedArguments", "NamedArguments"});
    private static final Descriptors.Descriptor internal_static_AttributeArgumentDefinition_descriptor = getDescriptor().getMessageTypes().get(7);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_AttributeArgumentDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_AttributeArgumentDefinition_descriptor, new String[]{XmlConstants.Attributes.type, "Name", "ValueString", "ValueInt32", "ValueInt64", "ValueDouble", "ValueFloat"});
    private static final Descriptors.Descriptor internal_static_EventDefinition_descriptor = getDescriptor().getMessageTypes().get(8);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_EventDefinition_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_EventDefinition_descriptor, new String[]{"Accessibility", "AddAccessorMethod", "CanAdd", "CanInvoke", "CanRemove", "FullName", "InvokeAccessorMethod", "Name", "RemoveAccessorMethod", "PeToken"});
    private static Descriptors.FileDescriptor descriptor;

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AssemblyAllTypesOrBuilder.class */
    public interface AssemblyAllTypesOrBuilder extends MessageOrBuilder {
        List<TypeDefinition> getListOfTypesList();

        TypeDefinition getListOfTypes(int i);

        int getListOfTypesCount();

        List<? extends TypeDefinitionOrBuilder> getListOfTypesOrBuilderList();

        TypeDefinitionOrBuilder getListOfTypesOrBuilder(int i);

        List<String> getAllReferencedModuleTypesList();

        int getAllReferencedModuleTypesCount();

        String getAllReferencedModuleTypes(int i);

        ByteString getAllReferencedModuleTypesBytes(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeArgumentDefinitionOrBuilder.class */
    public interface AttributeArgumentDefinitionOrBuilder extends MessageOrBuilder {
        boolean hasType();

        TypeDefinition getType();

        TypeDefinitionOrBuilder getTypeOrBuilder();

        String getName();

        ByteString getNameBytes();

        List<String> getValueStringList();

        int getValueStringCount();

        String getValueString(int i);

        ByteString getValueStringBytes(int i);

        List<Integer> getValueInt32List();

        int getValueInt32Count();

        int getValueInt32(int i);

        List<Long> getValueInt64List();

        int getValueInt64Count();

        long getValueInt64(int i);

        List<Double> getValueDoubleList();

        int getValueDoubleCount();

        double getValueDouble(int i);

        List<Float> getValueFloatList();

        int getValueFloatCount();

        float getValueFloat(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeDefinitionOrBuilder.class */
    public interface AttributeDefinitionOrBuilder extends MessageOrBuilder {
        boolean hasAttributeType();

        TypeDefinition getAttributeType();

        TypeDefinitionOrBuilder getAttributeTypeOrBuilder();

        String getConstructor();

        ByteString getConstructorBytes();

        List<AttributeArgumentDefinition> getFixedArgumentsList();

        AttributeArgumentDefinition getFixedArguments(int i);

        int getFixedArgumentsCount();

        List<? extends AttributeArgumentDefinitionOrBuilder> getFixedArgumentsOrBuilderList();

        AttributeArgumentDefinitionOrBuilder getFixedArgumentsOrBuilder(int i);

        List<AttributeArgumentDefinition> getNamedArgumentsList();

        AttributeArgumentDefinition getNamedArguments(int i);

        int getNamedArgumentsCount();

        List<? extends AttributeArgumentDefinitionOrBuilder> getNamedArgumentsOrBuilderList();

        AttributeArgumentDefinitionOrBuilder getNamedArgumentsOrBuilder(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$EventDefinitionOrBuilder.class */
    public interface EventDefinitionOrBuilder extends MessageOrBuilder {
        int getAccessibilityValue();

        Accessibility getAccessibility();

        boolean hasAddAccessorMethod();

        MethodDefinition getAddAccessorMethod();

        MethodDefinitionOrBuilder getAddAccessorMethodOrBuilder();

        boolean getCanAdd();

        boolean getCanInvoke();

        boolean getCanRemove();

        String getFullName();

        ByteString getFullNameBytes();

        boolean hasInvokeAccessorMethod();

        MethodDefinition getInvokeAccessorMethod();

        MethodDefinitionOrBuilder getInvokeAccessorMethodOrBuilder();

        String getName();

        ByteString getNameBytes();

        boolean hasRemoveAccessorMethod();

        MethodDefinition getRemoveAccessorMethod();

        MethodDefinitionOrBuilder getRemoveAccessorMethodOrBuilder();

        int getPeToken();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$FieldDefinitionOrBuilder.class */
    public interface FieldDefinitionOrBuilder extends MessageOrBuilder {
        int getAccessibilityValue();

        Accessibility getAccessibility();

        boolean getIsAbstract();

        boolean getIsSealed();

        boolean getIsExplicitInterfaceImplementation();

        boolean getIsOverride();

        boolean getIsVirtual();

        boolean getIsConst();

        boolean getIsReadOnly();

        boolean getIsStatic();

        boolean hasType();

        TypeDefinition getType();

        TypeDefinitionOrBuilder getTypeOrBuilder();

        int getTypeKindValue();

        TypeKindDef getTypeKind();

        String getName();

        ByteString getNameBytes();

        String getFullName();

        ByteString getFullNameBytes();

        boolean hasDeclaringType();

        TypeDefinition getDeclaringType();

        TypeDefinitionOrBuilder getDeclaringTypeOrBuilder();

        List<AttributeDefinition> getAttributesList();

        AttributeDefinition getAttributes(int i);

        int getAttributesCount();

        List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList();

        AttributeDefinitionOrBuilder getAttributesOrBuilder(int i);

        int getPeToken();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$MethodDefinitionOrBuilder.class */
    public interface MethodDefinitionOrBuilder extends MessageOrBuilder {
        int getAccessibilityValue();

        Accessibility getAccessibility();

        String getName();

        ByteString getNameBytes();

        boolean getHasBody();

        List<ParameterDefinition> getParameterList();

        ParameterDefinition getParameter(int i);

        int getParameterCount();

        List<? extends ParameterDefinitionOrBuilder> getParameterOrBuilderList();

        ParameterDefinitionOrBuilder getParameterOrBuilder(int i);

        boolean getIsAbstract();

        boolean getIsAccessor();

        boolean getIsConstructor();

        boolean getIsDestructor();

        boolean getIsExplicitInterfaceImplementation();

        boolean getIsStatic();

        boolean getIsVirtual();

        boolean getIsOperator();

        boolean getIsExtern();

        boolean getIsUnsafe();

        boolean getIsSealed();

        boolean hasReturnType();

        TypeDefinition getReturnType();

        TypeDefinitionOrBuilder getReturnTypeOrBuilder();

        List<AttributeDefinition> getAttributesList();

        AttributeDefinition getAttributes(int i);

        int getAttributesCount();

        List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList();

        AttributeDefinitionOrBuilder getAttributesOrBuilder(int i);

        String getFullName();

        ByteString getFullNameBytes();

        boolean hasDeclaringType();

        TypeDefinition getDeclaringType();

        TypeDefinitionOrBuilder getDeclaringTypeOrBuilder();

        int getPeToken();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$ParameterDefinitionOrBuilder.class */
    public interface ParameterDefinitionOrBuilder extends MessageOrBuilder {
        boolean hasType();

        TypeDefinition getType();

        TypeDefinitionOrBuilder getTypeOrBuilder();

        String getParameterName();

        ByteString getParameterNameBytes();

        boolean getIsRef();

        boolean getIsOut();

        boolean getIsIn();

        boolean getIsOptional();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$PropertyDefinitionOrBuilder.class */
    public interface PropertyDefinitionOrBuilder extends MessageOrBuilder {
        int getAccessibilityValue();

        Accessibility getAccessibility();

        boolean getCanGet();

        boolean getCanSet();

        boolean getIsAbstract();

        boolean getIsSealed();

        boolean getIsExplicitInterfaceImplementation();

        boolean getIsOverride();

        boolean getIsVirtual();

        boolean getIsStatic();

        boolean getIsExtern();

        boolean hasGetter();

        MethodDefinition getGetter();

        MethodDefinitionOrBuilder getGetterOrBuilder();

        boolean hasSetter();

        MethodDefinition getSetter();

        MethodDefinitionOrBuilder getSetterOrBuilder();

        boolean hasType();

        TypeDefinition getType();

        TypeDefinitionOrBuilder getTypeOrBuilder();

        int getTypeKindValue();

        TypeKindDef getTypeKind();

        String getName();

        ByteString getNameBytes();

        List<AttributeDefinition> getAttributesList();

        AttributeDefinition getAttributes(int i);

        int getAttributesCount();

        List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList();

        AttributeDefinitionOrBuilder getAttributesOrBuilder(int i);

        int getPeToken();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$TypeDefinitionOrBuilder.class */
    public interface TypeDefinitionOrBuilder extends MessageOrBuilder {
        int getAccessibilityValue();

        Accessibility getAccessibility();

        String getFullname();

        ByteString getFullnameBytes();

        String getNamespace();

        ByteString getNamespaceBytes();

        boolean getIsAbstract();

        boolean getIsReadOnly();

        boolean getIsSealed();

        boolean getIsStatic();

        String getDeclaringOuterClass();

        ByteString getDeclaringOuterClassBytes();

        List<TypeDefinition> getDirectBaseTypesList();

        TypeDefinition getDirectBaseTypes(int i);

        int getDirectBaseTypesCount();

        List<? extends TypeDefinitionOrBuilder> getDirectBaseTypesOrBuilderList();

        TypeDefinitionOrBuilder getDirectBaseTypesOrBuilder(int i);

        int getTypeKindValue();

        TypeKindDef getTypeKind();

        List<MethodDefinition> getMethodsList();

        MethodDefinition getMethods(int i);

        int getMethodsCount();

        List<? extends MethodDefinitionOrBuilder> getMethodsOrBuilderList();

        MethodDefinitionOrBuilder getMethodsOrBuilder(int i);

        List<FieldDefinition> getFieldsList();

        FieldDefinition getFields(int i);

        int getFieldsCount();

        List<? extends FieldDefinitionOrBuilder> getFieldsOrBuilderList();

        FieldDefinitionOrBuilder getFieldsOrBuilder(int i);

        List<PropertyDefinition> getPropertiesList();

        PropertyDefinition getProperties(int i);

        int getPropertiesCount();

        List<? extends PropertyDefinitionOrBuilder> getPropertiesOrBuilderList();

        PropertyDefinitionOrBuilder getPropertiesOrBuilder(int i);

        List<TypeDefinition> getNestedTypesList();

        TypeDefinition getNestedTypes(int i);

        int getNestedTypesCount();

        List<? extends TypeDefinitionOrBuilder> getNestedTypesOrBuilderList();

        TypeDefinitionOrBuilder getNestedTypesOrBuilder(int i);

        List<AttributeDefinition> getAttributesList();

        AttributeDefinition getAttributes(int i);

        int getAttributesCount();

        List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList();

        AttributeDefinitionOrBuilder getAttributesOrBuilder(int i);

        List<TypeDefinition> getGenericTypeArgumentsList();

        TypeDefinition getGenericTypeArguments(int i);

        int getGenericTypeArgumentsCount();

        List<? extends TypeDefinitionOrBuilder> getGenericTypeArgumentsOrBuilderList();

        TypeDefinitionOrBuilder getGenericTypeArgumentsOrBuilder(int i);

        int getArrayDimensions();

        List<EventDefinition> getEventsList();

        EventDefinition getEvents(int i);

        int getEventsCount();

        List<? extends EventDefinitionOrBuilder> getEventsOrBuilderList();

        EventDefinitionOrBuilder getEventsOrBuilder(int i);

        int getPeToken();
    }

    private ProtoAssemblyAllTypes() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$Accessibility.class */
    public enum Accessibility implements ProtocolMessageEnum {
        NONE(0),
        PRIVATE(1),
        PUBLIC(2),
        INTERNAL(3),
        PROTECTED(4),
        PROTECTED_AND_INTERNAL(5),
        PROTECTED_OR_INTERNAL(6),
        UNRECOGNIZED(-1);
        
        public static final int NONE_VALUE = 0;
        public static final int PRIVATE_VALUE = 1;
        public static final int PUBLIC_VALUE = 2;
        public static final int INTERNAL_VALUE = 3;
        public static final int PROTECTED_VALUE = 4;
        public static final int PROTECTED_AND_INTERNAL_VALUE = 5;
        public static final int PROTECTED_OR_INTERNAL_VALUE = 6;
        private static final Internal.EnumLiteMap<Accessibility> internalValueMap = new Internal.EnumLiteMap<Accessibility>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.Accessibility.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public Accessibility findValueByNumber(int number) {
                return Accessibility.forNumber(number);
            }
        };
        private static final Accessibility[] VALUES = valuesCustom();
        private final int value;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Accessibility[] valuesCustom() {
            Accessibility[] valuesCustom = values();
            int length = valuesCustom.length;
            Accessibility[] accessibilityArr = new Accessibility[length];
            System.arraycopy(valuesCustom, 0, accessibilityArr, 0, length);
            return accessibilityArr;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            }
            return this.value;
        }

        @Deprecated
        public static Accessibility valueOf(int value) {
            return forNumber(value);
        }

        public static Accessibility forNumber(int value) {
            switch (value) {
                case 0:
                    return NONE;
                case 1:
                    return PRIVATE;
                case 2:
                    return PUBLIC;
                case 3:
                    return INTERNAL;
                case 4:
                    return PROTECTED;
                case 5:
                    return PROTECTED_AND_INTERNAL;
                case 6:
                    return PROTECTED_OR_INTERNAL;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<Accessibility> internalGetValueMap() {
            return internalValueMap;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            if (this == UNRECOGNIZED) {
                throw new IllegalStateException("Can't get the descriptor of an unrecognized enum value.");
            }
            return getDescriptor().getValues().get(ordinal());
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
        }

        public static final Descriptors.EnumDescriptor getDescriptor() {
            return ProtoAssemblyAllTypes.getDescriptor().getEnumTypes().get(0);
        }

        public static Accessibility valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
            if (desc.getIndex() == -1) {
                return UNRECOGNIZED;
            }
            return VALUES[desc.getIndex()];
        }

        Accessibility(int value) {
            this.value = value;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$TypeKindDef.class */
    public enum TypeKindDef implements ProtocolMessageEnum {
        NO_TYPE(0),
        OTHER(1),
        CLASS(2),
        INTERFACE(3),
        STRUCT(4),
        DELEGATE(5),
        ENUM(6),
        VOID(7),
        UNKNOWN(8),
        NULL(9),
        NONE_TYPE(10),
        DYNAMIC(11),
        UNBOUND_TYPE_ARG(12),
        TYPE_PARAMETER(13),
        ARRAY(14),
        POINTER(15),
        BY_REF(16),
        INTERSECTION(17),
        ARG_LIST(18),
        TUPLE(19),
        MOD_OPT(20),
        MOD_REQ(21),
        N_INT(22),
        N_UINT(23),
        FUNCTION_POINTER(24),
        BY_REF_AND_ARRAY(25),
        UNRECOGNIZED(-1);
        
        public static final int NO_TYPE_VALUE = 0;
        public static final int OTHER_VALUE = 1;
        public static final int CLASS_VALUE = 2;
        public static final int INTERFACE_VALUE = 3;
        public static final int STRUCT_VALUE = 4;
        public static final int DELEGATE_VALUE = 5;
        public static final int ENUM_VALUE = 6;
        public static final int VOID_VALUE = 7;
        public static final int UNKNOWN_VALUE = 8;
        public static final int NULL_VALUE = 9;
        public static final int NONE_TYPE_VALUE = 10;
        public static final int DYNAMIC_VALUE = 11;
        public static final int UNBOUND_TYPE_ARG_VALUE = 12;
        public static final int TYPE_PARAMETER_VALUE = 13;
        public static final int ARRAY_VALUE = 14;
        public static final int POINTER_VALUE = 15;
        public static final int BY_REF_VALUE = 16;
        public static final int INTERSECTION_VALUE = 17;
        public static final int ARG_LIST_VALUE = 18;
        public static final int TUPLE_VALUE = 19;
        public static final int MOD_OPT_VALUE = 20;
        public static final int MOD_REQ_VALUE = 21;
        public static final int N_INT_VALUE = 22;
        public static final int N_UINT_VALUE = 23;
        public static final int FUNCTION_POINTER_VALUE = 24;
        public static final int BY_REF_AND_ARRAY_VALUE = 25;
        private static final Internal.EnumLiteMap<TypeKindDef> internalValueMap = new Internal.EnumLiteMap<TypeKindDef>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.TypeKindDef.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public TypeKindDef findValueByNumber(int number) {
                return TypeKindDef.forNumber(number);
            }
        };
        private static final TypeKindDef[] VALUES = valuesCustom();
        private final int value;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static TypeKindDef[] valuesCustom() {
            TypeKindDef[] valuesCustom = values();
            int length = valuesCustom.length;
            TypeKindDef[] typeKindDefArr = new TypeKindDef[length];
            System.arraycopy(valuesCustom, 0, typeKindDefArr, 0, length);
            return typeKindDefArr;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            }
            return this.value;
        }

        @Deprecated
        public static TypeKindDef valueOf(int value) {
            return forNumber(value);
        }

        public static TypeKindDef forNumber(int value) {
            switch (value) {
                case 0:
                    return NO_TYPE;
                case 1:
                    return OTHER;
                case 2:
                    return CLASS;
                case 3:
                    return INTERFACE;
                case 4:
                    return STRUCT;
                case 5:
                    return DELEGATE;
                case 6:
                    return ENUM;
                case 7:
                    return VOID;
                case 8:
                    return UNKNOWN;
                case 9:
                    return NULL;
                case 10:
                    return NONE_TYPE;
                case 11:
                    return DYNAMIC;
                case 12:
                    return UNBOUND_TYPE_ARG;
                case 13:
                    return TYPE_PARAMETER;
                case 14:
                    return ARRAY;
                case 15:
                    return POINTER;
                case 16:
                    return BY_REF;
                case 17:
                    return INTERSECTION;
                case 18:
                    return ARG_LIST;
                case 19:
                    return TUPLE;
                case 20:
                    return MOD_OPT;
                case 21:
                    return MOD_REQ;
                case 22:
                    return N_INT;
                case 23:
                    return N_UINT;
                case 24:
                    return FUNCTION_POINTER;
                case 25:
                    return BY_REF_AND_ARRAY;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<TypeKindDef> internalGetValueMap() {
            return internalValueMap;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            if (this == UNRECOGNIZED) {
                throw new IllegalStateException("Can't get the descriptor of an unrecognized enum value.");
            }
            return getDescriptor().getValues().get(ordinal());
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
        }

        public static final Descriptors.EnumDescriptor getDescriptor() {
            return ProtoAssemblyAllTypes.getDescriptor().getEnumTypes().get(1);
        }

        public static TypeKindDef valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
            if (desc.getIndex() == -1) {
                return UNRECOGNIZED;
            }
            return VALUES[desc.getIndex()];
        }

        TypeKindDef(int value) {
            this.value = value;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AssemblyAllTypes.class */
    public static final class AssemblyAllTypes extends GeneratedMessageV3 implements AssemblyAllTypesOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int LIST_OF_TYPES_FIELD_NUMBER = 1;
        private List<TypeDefinition> listOfTypes_;
        public static final int ALL_REFERENCED_MODULE_TYPES_FIELD_NUMBER = 2;
        private LazyStringList allReferencedModuleTypes_;
        private byte memoizedIsInitialized;
        private static final AssemblyAllTypes DEFAULT_INSTANCE = new AssemblyAllTypes();
        private static final Parser<AssemblyAllTypes> PARSER = new AbstractParser<AssemblyAllTypes>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypes.1
            @Override // com.google.protobuf.Parser
            public AssemblyAllTypes parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new AssemblyAllTypes(input, extensionRegistry, null);
            }
        };

        /* synthetic */ AssemblyAllTypes(GeneratedMessageV3.Builder builder, AssemblyAllTypes assemblyAllTypes) {
            this(builder);
        }

        private AssemblyAllTypes(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private AssemblyAllTypes() {
            this.memoizedIsInitialized = (byte) -1;
            this.listOfTypes_ = Collections.emptyList();
            this.allReferencedModuleTypes_ = LazyStringArrayList.EMPTY;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new AssemblyAllTypes();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ AssemblyAllTypes(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, AssemblyAllTypes assemblyAllTypes) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private AssemblyAllTypes(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 10:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.listOfTypes_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.listOfTypes_.add((TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry));
                                break;
                            case 18:
                                String s = input.readStringRequireUtf8();
                                if ((mutable_bitField0_ & 2) == 0) {
                                    this.allReferencedModuleTypes_ = new LazyStringArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.allReferencedModuleTypes_.add(s);
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.listOfTypes_ = Collections.unmodifiableList(this.listOfTypes_);
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.allReferencedModuleTypes_ = this.allReferencedModuleTypes_.getUnmodifiableView();
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_AssemblyAllTypes_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_AssemblyAllTypes_fieldAccessorTable.ensureFieldAccessorsInitialized(AssemblyAllTypes.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public List<TypeDefinition> getListOfTypesList() {
            return this.listOfTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public List<? extends TypeDefinitionOrBuilder> getListOfTypesOrBuilderList() {
            return this.listOfTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public int getListOfTypesCount() {
            return this.listOfTypes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public TypeDefinition getListOfTypes(int index) {
            return this.listOfTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public TypeDefinitionOrBuilder getListOfTypesOrBuilder(int index) {
            return this.listOfTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public ProtocolStringList getAllReferencedModuleTypesList() {
            return this.allReferencedModuleTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public int getAllReferencedModuleTypesCount() {
            return this.allReferencedModuleTypes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public String getAllReferencedModuleTypes(int index) {
            return (String) this.allReferencedModuleTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
        public ByteString getAllReferencedModuleTypesBytes(int index) {
            return this.allReferencedModuleTypes_.getByteString(index);
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
            for (int i = 0; i < this.listOfTypes_.size(); i++) {
                output.writeMessage(1, this.listOfTypes_.get(i));
            }
            for (int i2 = 0; i2 < this.allReferencedModuleTypes_.size(); i2++) {
                GeneratedMessageV3.writeString(output, 2, this.allReferencedModuleTypes_.getRaw(i2));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            for (int i = 0; i < this.listOfTypes_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.listOfTypes_.get(i));
            }
            int dataSize = 0;
            for (int i2 = 0; i2 < this.allReferencedModuleTypes_.size(); i2++) {
                dataSize += computeStringSizeNoTag(this.allReferencedModuleTypes_.getRaw(i2));
            }
            int size3 = size2 + dataSize + (1 * getAllReferencedModuleTypesList().size()) + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AssemblyAllTypes)) {
                return super.equals(obj);
            }
            AssemblyAllTypes other = (AssemblyAllTypes) obj;
            if (!getListOfTypesList().equals(other.getListOfTypesList()) || !getAllReferencedModuleTypesList().equals(other.getAllReferencedModuleTypesList()) || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            if (getListOfTypesCount() > 0) {
                hash = (53 * ((37 * hash) + 1)) + getListOfTypesList().hashCode();
            }
            if (getAllReferencedModuleTypesCount() > 0) {
                hash = (53 * ((37 * hash) + 2)) + getAllReferencedModuleTypesList().hashCode();
            }
            int hash2 = (29 * hash) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static AssemblyAllTypes parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AssemblyAllTypes parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AssemblyAllTypes parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AssemblyAllTypes parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AssemblyAllTypes parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AssemblyAllTypes parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AssemblyAllTypes parseFrom(InputStream input) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AssemblyAllTypes parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static AssemblyAllTypes parseDelimitedFrom(InputStream input) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static AssemblyAllTypes parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static AssemblyAllTypes parseFrom(CodedInputStream input) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AssemblyAllTypes parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AssemblyAllTypes) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AssemblyAllTypes prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AssemblyAllTypes$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AssemblyAllTypesOrBuilder {
            private int bitField0_;
            private List<TypeDefinition> listOfTypes_;
            private RepeatedFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> listOfTypesBuilder_;
            private LazyStringList allReferencedModuleTypes_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_AssemblyAllTypes_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_AssemblyAllTypes_fieldAccessorTable.ensureFieldAccessorsInitialized(AssemblyAllTypes.class, Builder.class);
            }

            private Builder() {
                this.listOfTypes_ = Collections.emptyList();
                this.allReferencedModuleTypes_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.listOfTypes_ = Collections.emptyList();
                this.allReferencedModuleTypes_ = LazyStringArrayList.EMPTY;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (AssemblyAllTypes.alwaysUseFieldBuilders) {
                    getListOfTypesFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.listOfTypesBuilder_ == null) {
                    this.listOfTypes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.listOfTypesBuilder_.clear();
                }
                this.allReferencedModuleTypes_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -3;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_AssemblyAllTypes_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public AssemblyAllTypes getDefaultInstanceForType() {
                return AssemblyAllTypes.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AssemblyAllTypes build() {
                AssemblyAllTypes result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AssemblyAllTypes buildPartial() {
                AssemblyAllTypes result = new AssemblyAllTypes(this, (AssemblyAllTypes) null);
                int i = this.bitField0_;
                if (this.listOfTypesBuilder_ != null) {
                    result.listOfTypes_ = this.listOfTypesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.listOfTypes_ = Collections.unmodifiableList(this.listOfTypes_);
                        this.bitField0_ &= -2;
                    }
                    result.listOfTypes_ = this.listOfTypes_;
                }
                if ((this.bitField0_ & 2) != 0) {
                    this.allReferencedModuleTypes_ = this.allReferencedModuleTypes_.getUnmodifiableView();
                    this.bitField0_ &= -3;
                }
                result.allReferencedModuleTypes_ = this.allReferencedModuleTypes_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof AssemblyAllTypes) {
                    return mergeFrom((AssemblyAllTypes) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AssemblyAllTypes other) {
                if (other == AssemblyAllTypes.getDefaultInstance()) {
                    return this;
                }
                if (this.listOfTypesBuilder_ == null) {
                    if (!other.listOfTypes_.isEmpty()) {
                        if (this.listOfTypes_.isEmpty()) {
                            this.listOfTypes_ = other.listOfTypes_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureListOfTypesIsMutable();
                            this.listOfTypes_.addAll(other.listOfTypes_);
                        }
                        onChanged();
                    }
                } else if (!other.listOfTypes_.isEmpty()) {
                    if (!this.listOfTypesBuilder_.isEmpty()) {
                        this.listOfTypesBuilder_.addAllMessages(other.listOfTypes_);
                    } else {
                        this.listOfTypesBuilder_.dispose();
                        this.listOfTypesBuilder_ = null;
                        this.listOfTypes_ = other.listOfTypes_;
                        this.bitField0_ &= -2;
                        this.listOfTypesBuilder_ = AssemblyAllTypes.alwaysUseFieldBuilders ? getListOfTypesFieldBuilder() : null;
                    }
                }
                if (!other.allReferencedModuleTypes_.isEmpty()) {
                    if (this.allReferencedModuleTypes_.isEmpty()) {
                        this.allReferencedModuleTypes_ = other.allReferencedModuleTypes_;
                        this.bitField0_ &= -3;
                    } else {
                        ensureAllReferencedModuleTypesIsMutable();
                        this.allReferencedModuleTypes_.addAll(other.allReferencedModuleTypes_);
                    }
                    onChanged();
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AssemblyAllTypes parsedMessage = null;
                try {
                    try {
                        parsedMessage = (AssemblyAllTypes) AssemblyAllTypes.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        AssemblyAllTypes assemblyAllTypes = (AssemblyAllTypes) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            private void ensureListOfTypesIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.listOfTypes_ = new ArrayList(this.listOfTypes_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public List<TypeDefinition> getListOfTypesList() {
                if (this.listOfTypesBuilder_ == null) {
                    return Collections.unmodifiableList(this.listOfTypes_);
                }
                return this.listOfTypesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public int getListOfTypesCount() {
                if (this.listOfTypesBuilder_ == null) {
                    return this.listOfTypes_.size();
                }
                return this.listOfTypesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public TypeDefinition getListOfTypes(int index) {
                if (this.listOfTypesBuilder_ == null) {
                    return this.listOfTypes_.get(index);
                }
                return this.listOfTypesBuilder_.getMessage(index);
            }

            public Builder setListOfTypes(int index, TypeDefinition value) {
                if (this.listOfTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.set(index, value);
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setListOfTypes(int index, TypeDefinition.Builder builderForValue) {
                if (this.listOfTypesBuilder_ == null) {
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addListOfTypes(TypeDefinition value) {
                if (this.listOfTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.add(value);
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addListOfTypes(int index, TypeDefinition value) {
                if (this.listOfTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.add(index, value);
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addListOfTypes(TypeDefinition.Builder builderForValue) {
                if (this.listOfTypesBuilder_ == null) {
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addListOfTypes(int index, TypeDefinition.Builder builderForValue) {
                if (this.listOfTypesBuilder_ == null) {
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllListOfTypes(Iterable<? extends TypeDefinition> values) {
                if (this.listOfTypesBuilder_ == null) {
                    ensureListOfTypesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.listOfTypes_);
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearListOfTypes() {
                if (this.listOfTypesBuilder_ == null) {
                    this.listOfTypes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.clear();
                }
                return this;
            }

            public Builder removeListOfTypes(int index) {
                if (this.listOfTypesBuilder_ == null) {
                    ensureListOfTypesIsMutable();
                    this.listOfTypes_.remove(index);
                    onChanged();
                } else {
                    this.listOfTypesBuilder_.remove(index);
                }
                return this;
            }

            public TypeDefinition.Builder getListOfTypesBuilder(int index) {
                return getListOfTypesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public TypeDefinitionOrBuilder getListOfTypesOrBuilder(int index) {
                if (this.listOfTypesBuilder_ == null) {
                    return this.listOfTypes_.get(index);
                }
                return this.listOfTypesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public List<? extends TypeDefinitionOrBuilder> getListOfTypesOrBuilderList() {
                if (this.listOfTypesBuilder_ != null) {
                    return this.listOfTypesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.listOfTypes_);
            }

            public TypeDefinition.Builder addListOfTypesBuilder() {
                return getListOfTypesFieldBuilder().addBuilder(TypeDefinition.getDefaultInstance());
            }

            public TypeDefinition.Builder addListOfTypesBuilder(int index) {
                return getListOfTypesFieldBuilder().addBuilder(index, TypeDefinition.getDefaultInstance());
            }

            public List<TypeDefinition.Builder> getListOfTypesBuilderList() {
                return getListOfTypesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getListOfTypesFieldBuilder() {
                if (this.listOfTypesBuilder_ == null) {
                    this.listOfTypesBuilder_ = new RepeatedFieldBuilderV3<>(this.listOfTypes_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.listOfTypes_ = null;
                }
                return this.listOfTypesBuilder_;
            }

            private void ensureAllReferencedModuleTypesIsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.allReferencedModuleTypes_ = new LazyStringArrayList(this.allReferencedModuleTypes_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public ProtocolStringList getAllReferencedModuleTypesList() {
                return this.allReferencedModuleTypes_.getUnmodifiableView();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public int getAllReferencedModuleTypesCount() {
                return this.allReferencedModuleTypes_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public String getAllReferencedModuleTypes(int index) {
                return (String) this.allReferencedModuleTypes_.get(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AssemblyAllTypesOrBuilder
            public ByteString getAllReferencedModuleTypesBytes(int index) {
                return this.allReferencedModuleTypes_.getByteString(index);
            }

            public Builder setAllReferencedModuleTypes(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAllReferencedModuleTypesIsMutable();
                this.allReferencedModuleTypes_.set(index, value);
                onChanged();
                return this;
            }

            public Builder addAllReferencedModuleTypes(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureAllReferencedModuleTypesIsMutable();
                this.allReferencedModuleTypes_.add(value);
                onChanged();
                return this;
            }

            public Builder addAllAllReferencedModuleTypes(Iterable<String> values) {
                ensureAllReferencedModuleTypesIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.allReferencedModuleTypes_);
                onChanged();
                return this;
            }

            public Builder clearAllReferencedModuleTypes() {
                this.allReferencedModuleTypes_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -3;
                onChanged();
                return this;
            }

            public Builder addAllReferencedModuleTypesBytes(ByteString value) {
                if (value != null) {
                    AssemblyAllTypes.checkByteStringIsUtf8(value);
                    ensureAllReferencedModuleTypesIsMutable();
                    this.allReferencedModuleTypes_.add(value);
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
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

        public static AssemblyAllTypes getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AssemblyAllTypes> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<AssemblyAllTypes> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public AssemblyAllTypes getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$TypeDefinition.class */
    public static final class TypeDefinition extends GeneratedMessageV3 implements TypeDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ACCESSIBILITY_FIELD_NUMBER = 1;
        private int accessibility_;
        public static final int FULLNAME_FIELD_NUMBER = 2;
        private volatile Object fullname_;
        public static final int NAMESPACE_FIELD_NUMBER = 3;
        private volatile Object namespace_;
        public static final int IS_ABSTRACT_FIELD_NUMBER = 4;
        private boolean isAbstract_;
        public static final int IS_READ_ONLY_FIELD_NUMBER = 5;
        private boolean isReadOnly_;
        public static final int IS_SEALED_FIELD_NUMBER = 6;
        private boolean isSealed_;
        public static final int IS_STATIC_FIELD_NUMBER = 7;
        private boolean isStatic_;
        public static final int DECLARING_OUTER_CLASS_FIELD_NUMBER = 8;
        private volatile Object declaringOuterClass_;
        public static final int DIRECT_BASE_TYPES_FIELD_NUMBER = 9;
        private List<TypeDefinition> directBaseTypes_;
        public static final int TYPE_KIND_FIELD_NUMBER = 10;
        private int typeKind_;
        public static final int METHODS_FIELD_NUMBER = 11;
        private List<MethodDefinition> methods_;
        public static final int FIELDS_FIELD_NUMBER = 12;
        private List<FieldDefinition> fields_;
        public static final int PROPERTIES_FIELD_NUMBER = 13;
        private List<PropertyDefinition> properties_;
        public static final int NESTED_TYPES_FIELD_NUMBER = 14;
        private List<TypeDefinition> nestedTypes_;
        public static final int ATTRIBUTES_FIELD_NUMBER = 15;
        private List<AttributeDefinition> attributes_;
        public static final int GENERIC_TYPE_ARGUMENTS_FIELD_NUMBER = 16;
        private List<TypeDefinition> genericTypeArguments_;
        public static final int ARRAY_DIMENSIONS_FIELD_NUMBER = 17;
        private int arrayDimensions_;
        public static final int EVENTS_FIELD_NUMBER = 18;
        private List<EventDefinition> events_;
        public static final int PE_TOKEN_FIELD_NUMBER = 19;
        private int peToken_;
        private byte memoizedIsInitialized;
        private static final TypeDefinition DEFAULT_INSTANCE = new TypeDefinition();
        private static final Parser<TypeDefinition> PARSER = new AbstractParser<TypeDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinition.1
            @Override // com.google.protobuf.Parser
            public TypeDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new TypeDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ TypeDefinition(GeneratedMessageV3.Builder builder, TypeDefinition typeDefinition) {
            this(builder);
        }

        private TypeDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private TypeDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.accessibility_ = 0;
            this.fullname_ = "";
            this.namespace_ = "";
            this.declaringOuterClass_ = "";
            this.directBaseTypes_ = Collections.emptyList();
            this.typeKind_ = 0;
            this.methods_ = Collections.emptyList();
            this.fields_ = Collections.emptyList();
            this.properties_ = Collections.emptyList();
            this.nestedTypes_ = Collections.emptyList();
            this.attributes_ = Collections.emptyList();
            this.genericTypeArguments_ = Collections.emptyList();
            this.events_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new TypeDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ TypeDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, TypeDefinition typeDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private TypeDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                this.accessibility_ = rawValue;
                                break;
                            case 18:
                                String s = input.readStringRequireUtf8();
                                this.fullname_ = s;
                                break;
                            case 26:
                                String s2 = input.readStringRequireUtf8();
                                this.namespace_ = s2;
                                break;
                            case 32:
                                this.isAbstract_ = input.readBool();
                                break;
                            case 40:
                                this.isReadOnly_ = input.readBool();
                                break;
                            case 48:
                                this.isSealed_ = input.readBool();
                                break;
                            case 56:
                                this.isStatic_ = input.readBool();
                                break;
                            case 66:
                                String s3 = input.readStringRequireUtf8();
                                this.declaringOuterClass_ = s3;
                                break;
                            case 74:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.directBaseTypes_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.directBaseTypes_.add((TypeDefinition) input.readMessage(parser(), extensionRegistry));
                                break;
                            case 80:
                                int rawValue2 = input.readEnum();
                                this.typeKind_ = rawValue2;
                                break;
                            case 90:
                                if ((mutable_bitField0_ & 2) == 0) {
                                    this.methods_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.methods_.add((MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry));
                                break;
                            case 98:
                                if ((mutable_bitField0_ & 4) == 0) {
                                    this.fields_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                this.fields_.add((FieldDefinition) input.readMessage(FieldDefinition.parser(), extensionRegistry));
                                break;
                            case 106:
                                if ((mutable_bitField0_ & 8) == 0) {
                                    this.properties_ = new ArrayList();
                                    mutable_bitField0_ |= 8;
                                }
                                this.properties_.add((PropertyDefinition) input.readMessage(PropertyDefinition.parser(), extensionRegistry));
                                break;
                            case 114:
                                if ((mutable_bitField0_ & 16) == 0) {
                                    this.nestedTypes_ = new ArrayList();
                                    mutable_bitField0_ |= 16;
                                }
                                this.nestedTypes_.add((TypeDefinition) input.readMessage(parser(), extensionRegistry));
                                break;
                            case 122:
                                if ((mutable_bitField0_ & 32) == 0) {
                                    this.attributes_ = new ArrayList();
                                    mutable_bitField0_ |= 32;
                                }
                                this.attributes_.add((AttributeDefinition) input.readMessage(AttributeDefinition.parser(), extensionRegistry));
                                break;
                            case 130:
                                if ((mutable_bitField0_ & 64) == 0) {
                                    this.genericTypeArguments_ = new ArrayList();
                                    mutable_bitField0_ |= 64;
                                }
                                this.genericTypeArguments_.add((TypeDefinition) input.readMessage(parser(), extensionRegistry));
                                break;
                            case 136:
                                this.arrayDimensions_ = input.readInt32();
                                break;
                            case 146:
                                if ((mutable_bitField0_ & 128) == 0) {
                                    this.events_ = new ArrayList();
                                    mutable_bitField0_ |= 128;
                                }
                                this.events_.add((EventDefinition) input.readMessage(EventDefinition.parser(), extensionRegistry));
                                break;
                            case 152:
                                this.peToken_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.directBaseTypes_ = Collections.unmodifiableList(this.directBaseTypes_);
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.methods_ = Collections.unmodifiableList(this.methods_);
                }
                if ((mutable_bitField0_ & 4) != 0) {
                    this.fields_ = Collections.unmodifiableList(this.fields_);
                }
                if ((mutable_bitField0_ & 8) != 0) {
                    this.properties_ = Collections.unmodifiableList(this.properties_);
                }
                if ((mutable_bitField0_ & 16) != 0) {
                    this.nestedTypes_ = Collections.unmodifiableList(this.nestedTypes_);
                }
                if ((mutable_bitField0_ & 32) != 0) {
                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                }
                if ((mutable_bitField0_ & 64) != 0) {
                    this.genericTypeArguments_ = Collections.unmodifiableList(this.genericTypeArguments_);
                }
                if ((mutable_bitField0_ & 128) != 0) {
                    this.events_ = Collections.unmodifiableList(this.events_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_TypeDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_TypeDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(TypeDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getAccessibilityValue() {
            return this.accessibility_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public Accessibility getAccessibility() {
            Accessibility result = Accessibility.valueOf(this.accessibility_);
            return result == null ? Accessibility.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public String getFullname() {
            Object ref = this.fullname_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.fullname_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public ByteString getFullnameBytes() {
            Object ref = this.fullname_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.fullname_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public String getNamespace() {
            Object ref = this.namespace_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.namespace_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public ByteString getNamespaceBytes() {
            Object ref = this.namespace_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.namespace_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public boolean getIsAbstract() {
            return this.isAbstract_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public boolean getIsReadOnly() {
            return this.isReadOnly_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public boolean getIsSealed() {
            return this.isSealed_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public boolean getIsStatic() {
            return this.isStatic_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public String getDeclaringOuterClass() {
            Object ref = this.declaringOuterClass_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.declaringOuterClass_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public ByteString getDeclaringOuterClassBytes() {
            Object ref = this.declaringOuterClass_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.declaringOuterClass_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<TypeDefinition> getDirectBaseTypesList() {
            return this.directBaseTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends TypeDefinitionOrBuilder> getDirectBaseTypesOrBuilderList() {
            return this.directBaseTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getDirectBaseTypesCount() {
            return this.directBaseTypes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinition getDirectBaseTypes(int index) {
            return this.directBaseTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinitionOrBuilder getDirectBaseTypesOrBuilder(int index) {
            return this.directBaseTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getTypeKindValue() {
            return this.typeKind_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeKindDef getTypeKind() {
            TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
            return result == null ? TypeKindDef.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<MethodDefinition> getMethodsList() {
            return this.methods_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends MethodDefinitionOrBuilder> getMethodsOrBuilderList() {
            return this.methods_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getMethodsCount() {
            return this.methods_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public MethodDefinition getMethods(int index) {
            return this.methods_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public MethodDefinitionOrBuilder getMethodsOrBuilder(int index) {
            return this.methods_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<FieldDefinition> getFieldsList() {
            return this.fields_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends FieldDefinitionOrBuilder> getFieldsOrBuilderList() {
            return this.fields_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getFieldsCount() {
            return this.fields_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public FieldDefinition getFields(int index) {
            return this.fields_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public FieldDefinitionOrBuilder getFieldsOrBuilder(int index) {
            return this.fields_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<PropertyDefinition> getPropertiesList() {
            return this.properties_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends PropertyDefinitionOrBuilder> getPropertiesOrBuilderList() {
            return this.properties_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getPropertiesCount() {
            return this.properties_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public PropertyDefinition getProperties(int index) {
            return this.properties_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public PropertyDefinitionOrBuilder getPropertiesOrBuilder(int index) {
            return this.properties_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<TypeDefinition> getNestedTypesList() {
            return this.nestedTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends TypeDefinitionOrBuilder> getNestedTypesOrBuilderList() {
            return this.nestedTypes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getNestedTypesCount() {
            return this.nestedTypes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinition getNestedTypes(int index) {
            return this.nestedTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinitionOrBuilder getNestedTypesOrBuilder(int index) {
            return this.nestedTypes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<AttributeDefinition> getAttributesList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getAttributesCount() {
            return this.attributes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public AttributeDefinition getAttributes(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<TypeDefinition> getGenericTypeArgumentsList() {
            return this.genericTypeArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends TypeDefinitionOrBuilder> getGenericTypeArgumentsOrBuilderList() {
            return this.genericTypeArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getGenericTypeArgumentsCount() {
            return this.genericTypeArguments_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinition getGenericTypeArguments(int index) {
            return this.genericTypeArguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public TypeDefinitionOrBuilder getGenericTypeArgumentsOrBuilder(int index) {
            return this.genericTypeArguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getArrayDimensions() {
            return this.arrayDimensions_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<EventDefinition> getEventsList() {
            return this.events_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public List<? extends EventDefinitionOrBuilder> getEventsOrBuilderList() {
            return this.events_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getEventsCount() {
            return this.events_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public EventDefinition getEvents(int index) {
            return this.events_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public EventDefinitionOrBuilder getEventsOrBuilder(int index) {
            return this.events_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
        public int getPeToken() {
            return this.peToken_;
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
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                output.writeEnum(1, this.accessibility_);
            }
            if (!getFullnameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.fullname_);
            }
            if (!getNamespaceBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 3, this.namespace_);
            }
            if (this.isAbstract_) {
                output.writeBool(4, this.isAbstract_);
            }
            if (this.isReadOnly_) {
                output.writeBool(5, this.isReadOnly_);
            }
            if (this.isSealed_) {
                output.writeBool(6, this.isSealed_);
            }
            if (this.isStatic_) {
                output.writeBool(7, this.isStatic_);
            }
            if (!getDeclaringOuterClassBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 8, this.declaringOuterClass_);
            }
            for (int i = 0; i < this.directBaseTypes_.size(); i++) {
                output.writeMessage(9, this.directBaseTypes_.get(i));
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                output.writeEnum(10, this.typeKind_);
            }
            for (int i2 = 0; i2 < this.methods_.size(); i2++) {
                output.writeMessage(11, this.methods_.get(i2));
            }
            for (int i3 = 0; i3 < this.fields_.size(); i3++) {
                output.writeMessage(12, this.fields_.get(i3));
            }
            for (int i4 = 0; i4 < this.properties_.size(); i4++) {
                output.writeMessage(13, this.properties_.get(i4));
            }
            for (int i5 = 0; i5 < this.nestedTypes_.size(); i5++) {
                output.writeMessage(14, this.nestedTypes_.get(i5));
            }
            for (int i6 = 0; i6 < this.attributes_.size(); i6++) {
                output.writeMessage(15, this.attributes_.get(i6));
            }
            for (int i7 = 0; i7 < this.genericTypeArguments_.size(); i7++) {
                output.writeMessage(16, this.genericTypeArguments_.get(i7));
            }
            if (this.arrayDimensions_ != 0) {
                output.writeInt32(17, this.arrayDimensions_);
            }
            for (int i8 = 0; i8 < this.events_.size(); i8++) {
                output.writeMessage(18, this.events_.get(i8));
            }
            if (this.peToken_ != 0) {
                output.writeInt32(19, this.peToken_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.accessibility_);
            }
            if (!getFullnameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.fullname_);
            }
            if (!getNamespaceBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(3, this.namespace_);
            }
            if (this.isAbstract_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.isAbstract_);
            }
            if (this.isReadOnly_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.isReadOnly_);
            }
            if (this.isSealed_) {
                size2 += CodedOutputStream.computeBoolSize(6, this.isSealed_);
            }
            if (this.isStatic_) {
                size2 += CodedOutputStream.computeBoolSize(7, this.isStatic_);
            }
            if (!getDeclaringOuterClassBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(8, this.declaringOuterClass_);
            }
            for (int i = 0; i < this.directBaseTypes_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(9, this.directBaseTypes_.get(i));
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(10, this.typeKind_);
            }
            for (int i2 = 0; i2 < this.methods_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(11, this.methods_.get(i2));
            }
            for (int i3 = 0; i3 < this.fields_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(12, this.fields_.get(i3));
            }
            for (int i4 = 0; i4 < this.properties_.size(); i4++) {
                size2 += CodedOutputStream.computeMessageSize(13, this.properties_.get(i4));
            }
            for (int i5 = 0; i5 < this.nestedTypes_.size(); i5++) {
                size2 += CodedOutputStream.computeMessageSize(14, this.nestedTypes_.get(i5));
            }
            for (int i6 = 0; i6 < this.attributes_.size(); i6++) {
                size2 += CodedOutputStream.computeMessageSize(15, this.attributes_.get(i6));
            }
            for (int i7 = 0; i7 < this.genericTypeArguments_.size(); i7++) {
                size2 += CodedOutputStream.computeMessageSize(16, this.genericTypeArguments_.get(i7));
            }
            if (this.arrayDimensions_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(17, this.arrayDimensions_);
            }
            for (int i8 = 0; i8 < this.events_.size(); i8++) {
                size2 += CodedOutputStream.computeMessageSize(18, this.events_.get(i8));
            }
            if (this.peToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(19, this.peToken_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof TypeDefinition)) {
                return super.equals(obj);
            }
            TypeDefinition other = (TypeDefinition) obj;
            if (this.accessibility_ != other.accessibility_ || !getFullname().equals(other.getFullname()) || !getNamespace().equals(other.getNamespace()) || getIsAbstract() != other.getIsAbstract() || getIsReadOnly() != other.getIsReadOnly() || getIsSealed() != other.getIsSealed() || getIsStatic() != other.getIsStatic() || !getDeclaringOuterClass().equals(other.getDeclaringOuterClass()) || !getDirectBaseTypesList().equals(other.getDirectBaseTypesList()) || this.typeKind_ != other.typeKind_ || !getMethodsList().equals(other.getMethodsList()) || !getFieldsList().equals(other.getFieldsList()) || !getPropertiesList().equals(other.getPropertiesList()) || !getNestedTypesList().equals(other.getNestedTypesList()) || !getAttributesList().equals(other.getAttributesList()) || !getGenericTypeArgumentsList().equals(other.getGenericTypeArgumentsList()) || getArrayDimensions() != other.getArrayDimensions() || !getEventsList().equals(other.getEventsList()) || getPeToken() != other.getPeToken() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            int hash2 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 1)) + this.accessibility_)) + 2)) + getFullname().hashCode())) + 3)) + getNamespace().hashCode())) + 4)) + Internal.hashBoolean(getIsAbstract()))) + 5)) + Internal.hashBoolean(getIsReadOnly()))) + 6)) + Internal.hashBoolean(getIsSealed()))) + 7)) + Internal.hashBoolean(getIsStatic()))) + 8)) + getDeclaringOuterClass().hashCode();
            if (getDirectBaseTypesCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 9)) + getDirectBaseTypesList().hashCode();
            }
            int hash3 = (53 * ((37 * hash2) + 10)) + this.typeKind_;
            if (getMethodsCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 11)) + getMethodsList().hashCode();
            }
            if (getFieldsCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 12)) + getFieldsList().hashCode();
            }
            if (getPropertiesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 13)) + getPropertiesList().hashCode();
            }
            if (getNestedTypesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 14)) + getNestedTypesList().hashCode();
            }
            if (getAttributesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 15)) + getAttributesList().hashCode();
            }
            if (getGenericTypeArgumentsCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 16)) + getGenericTypeArgumentsList().hashCode();
            }
            int hash4 = (53 * ((37 * hash3) + 17)) + getArrayDimensions();
            if (getEventsCount() > 0) {
                hash4 = (53 * ((37 * hash4) + 18)) + getEventsList().hashCode();
            }
            int hash5 = (29 * ((53 * ((37 * hash4) + 19)) + getPeToken())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash5;
            return hash5;
        }

        public static TypeDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static TypeDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static TypeDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static TypeDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static TypeDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static TypeDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static TypeDefinition parseFrom(InputStream input) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static TypeDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static TypeDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static TypeDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static TypeDefinition parseFrom(CodedInputStream input) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static TypeDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (TypeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(TypeDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$TypeDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements TypeDefinitionOrBuilder {
            private int bitField0_;
            private int accessibility_;
            private Object fullname_;
            private Object namespace_;
            private boolean isAbstract_;
            private boolean isReadOnly_;
            private boolean isSealed_;
            private boolean isStatic_;
            private Object declaringOuterClass_;
            private List<TypeDefinition> directBaseTypes_;
            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> directBaseTypesBuilder_;
            private int typeKind_;
            private List<MethodDefinition> methods_;
            private RepeatedFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> methodsBuilder_;
            private List<FieldDefinition> fields_;
            private RepeatedFieldBuilderV3<FieldDefinition, FieldDefinition.Builder, FieldDefinitionOrBuilder> fieldsBuilder_;
            private List<PropertyDefinition> properties_;
            private RepeatedFieldBuilderV3<PropertyDefinition, PropertyDefinition.Builder, PropertyDefinitionOrBuilder> propertiesBuilder_;
            private List<TypeDefinition> nestedTypes_;
            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> nestedTypesBuilder_;
            private List<AttributeDefinition> attributes_;
            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> attributesBuilder_;
            private List<TypeDefinition> genericTypeArguments_;
            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> genericTypeArgumentsBuilder_;
            private int arrayDimensions_;
            private List<EventDefinition> events_;
            private RepeatedFieldBuilderV3<EventDefinition, EventDefinition.Builder, EventDefinitionOrBuilder> eventsBuilder_;
            private int peToken_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_TypeDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_TypeDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(TypeDefinition.class, Builder.class);
            }

            private Builder() {
                this.accessibility_ = 0;
                this.fullname_ = "";
                this.namespace_ = "";
                this.declaringOuterClass_ = "";
                this.directBaseTypes_ = Collections.emptyList();
                this.typeKind_ = 0;
                this.methods_ = Collections.emptyList();
                this.fields_ = Collections.emptyList();
                this.properties_ = Collections.emptyList();
                this.nestedTypes_ = Collections.emptyList();
                this.attributes_ = Collections.emptyList();
                this.genericTypeArguments_ = Collections.emptyList();
                this.events_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.accessibility_ = 0;
                this.fullname_ = "";
                this.namespace_ = "";
                this.declaringOuterClass_ = "";
                this.directBaseTypes_ = Collections.emptyList();
                this.typeKind_ = 0;
                this.methods_ = Collections.emptyList();
                this.fields_ = Collections.emptyList();
                this.properties_ = Collections.emptyList();
                this.nestedTypes_ = Collections.emptyList();
                this.attributes_ = Collections.emptyList();
                this.genericTypeArguments_ = Collections.emptyList();
                this.events_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (TypeDefinition.alwaysUseFieldBuilders) {
                    getDirectBaseTypesFieldBuilder();
                    getMethodsFieldBuilder();
                    getFieldsFieldBuilder();
                    getPropertiesFieldBuilder();
                    getNestedTypesFieldBuilder();
                    getAttributesFieldBuilder();
                    getGenericTypeArgumentsFieldBuilder();
                    getEventsFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.accessibility_ = 0;
                this.fullname_ = "";
                this.namespace_ = "";
                this.isAbstract_ = false;
                this.isReadOnly_ = false;
                this.isSealed_ = false;
                this.isStatic_ = false;
                this.declaringOuterClass_ = "";
                if (this.directBaseTypesBuilder_ == null) {
                    this.directBaseTypes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.directBaseTypesBuilder_.clear();
                }
                this.typeKind_ = 0;
                if (this.methodsBuilder_ == null) {
                    this.methods_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.methodsBuilder_.clear();
                }
                if (this.fieldsBuilder_ == null) {
                    this.fields_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.fieldsBuilder_.clear();
                }
                if (this.propertiesBuilder_ == null) {
                    this.properties_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                } else {
                    this.propertiesBuilder_.clear();
                }
                if (this.nestedTypesBuilder_ == null) {
                    this.nestedTypes_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    this.nestedTypesBuilder_.clear();
                }
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                } else {
                    this.attributesBuilder_.clear();
                }
                if (this.genericTypeArgumentsBuilder_ == null) {
                    this.genericTypeArguments_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                } else {
                    this.genericTypeArgumentsBuilder_.clear();
                }
                this.arrayDimensions_ = 0;
                if (this.eventsBuilder_ == null) {
                    this.events_ = Collections.emptyList();
                    this.bitField0_ &= -129;
                } else {
                    this.eventsBuilder_.clear();
                }
                this.peToken_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_TypeDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public TypeDefinition getDefaultInstanceForType() {
                return TypeDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public TypeDefinition build() {
                TypeDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public TypeDefinition buildPartial() {
                TypeDefinition result = new TypeDefinition(this, (TypeDefinition) null);
                int i = this.bitField0_;
                result.accessibility_ = this.accessibility_;
                result.fullname_ = this.fullname_;
                result.namespace_ = this.namespace_;
                result.isAbstract_ = this.isAbstract_;
                result.isReadOnly_ = this.isReadOnly_;
                result.isSealed_ = this.isSealed_;
                result.isStatic_ = this.isStatic_;
                result.declaringOuterClass_ = this.declaringOuterClass_;
                if (this.directBaseTypesBuilder_ != null) {
                    result.directBaseTypes_ = this.directBaseTypesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.directBaseTypes_ = Collections.unmodifiableList(this.directBaseTypes_);
                        this.bitField0_ &= -2;
                    }
                    result.directBaseTypes_ = this.directBaseTypes_;
                }
                result.typeKind_ = this.typeKind_;
                if (this.methodsBuilder_ != null) {
                    result.methods_ = this.methodsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 2) != 0) {
                        this.methods_ = Collections.unmodifiableList(this.methods_);
                        this.bitField0_ &= -3;
                    }
                    result.methods_ = this.methods_;
                }
                if (this.fieldsBuilder_ != null) {
                    result.fields_ = this.fieldsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 4) != 0) {
                        this.fields_ = Collections.unmodifiableList(this.fields_);
                        this.bitField0_ &= -5;
                    }
                    result.fields_ = this.fields_;
                }
                if (this.propertiesBuilder_ != null) {
                    result.properties_ = this.propertiesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 8) != 0) {
                        this.properties_ = Collections.unmodifiableList(this.properties_);
                        this.bitField0_ &= -9;
                    }
                    result.properties_ = this.properties_;
                }
                if (this.nestedTypesBuilder_ != null) {
                    result.nestedTypes_ = this.nestedTypesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 16) != 0) {
                        this.nestedTypes_ = Collections.unmodifiableList(this.nestedTypes_);
                        this.bitField0_ &= -17;
                    }
                    result.nestedTypes_ = this.nestedTypes_;
                }
                if (this.attributesBuilder_ != null) {
                    result.attributes_ = this.attributesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 32) != 0) {
                        this.attributes_ = Collections.unmodifiableList(this.attributes_);
                        this.bitField0_ &= -33;
                    }
                    result.attributes_ = this.attributes_;
                }
                if (this.genericTypeArgumentsBuilder_ != null) {
                    result.genericTypeArguments_ = this.genericTypeArgumentsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 64) != 0) {
                        this.genericTypeArguments_ = Collections.unmodifiableList(this.genericTypeArguments_);
                        this.bitField0_ &= -65;
                    }
                    result.genericTypeArguments_ = this.genericTypeArguments_;
                }
                result.arrayDimensions_ = this.arrayDimensions_;
                if (this.eventsBuilder_ != null) {
                    result.events_ = this.eventsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 128) != 0) {
                        this.events_ = Collections.unmodifiableList(this.events_);
                        this.bitField0_ &= -129;
                    }
                    result.events_ = this.events_;
                }
                result.peToken_ = this.peToken_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof TypeDefinition) {
                    return mergeFrom((TypeDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(TypeDefinition other) {
                if (other != TypeDefinition.getDefaultInstance()) {
                    if (other.accessibility_ != 0) {
                        setAccessibilityValue(other.getAccessibilityValue());
                    }
                    if (!other.getFullname().isEmpty()) {
                        this.fullname_ = other.fullname_;
                        onChanged();
                    }
                    if (!other.getNamespace().isEmpty()) {
                        this.namespace_ = other.namespace_;
                        onChanged();
                    }
                    if (other.getIsAbstract()) {
                        setIsAbstract(other.getIsAbstract());
                    }
                    if (other.getIsReadOnly()) {
                        setIsReadOnly(other.getIsReadOnly());
                    }
                    if (other.getIsSealed()) {
                        setIsSealed(other.getIsSealed());
                    }
                    if (other.getIsStatic()) {
                        setIsStatic(other.getIsStatic());
                    }
                    if (!other.getDeclaringOuterClass().isEmpty()) {
                        this.declaringOuterClass_ = other.declaringOuterClass_;
                        onChanged();
                    }
                    if (this.directBaseTypesBuilder_ == null) {
                        if (!other.directBaseTypes_.isEmpty()) {
                            if (this.directBaseTypes_.isEmpty()) {
                                this.directBaseTypes_ = other.directBaseTypes_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureDirectBaseTypesIsMutable();
                                this.directBaseTypes_.addAll(other.directBaseTypes_);
                            }
                            onChanged();
                        }
                    } else if (!other.directBaseTypes_.isEmpty()) {
                        if (!this.directBaseTypesBuilder_.isEmpty()) {
                            this.directBaseTypesBuilder_.addAllMessages(other.directBaseTypes_);
                        } else {
                            this.directBaseTypesBuilder_.dispose();
                            this.directBaseTypesBuilder_ = null;
                            this.directBaseTypes_ = other.directBaseTypes_;
                            this.bitField0_ &= -2;
                            this.directBaseTypesBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getDirectBaseTypesFieldBuilder() : null;
                        }
                    }
                    if (other.typeKind_ != 0) {
                        setTypeKindValue(other.getTypeKindValue());
                    }
                    if (this.methodsBuilder_ == null) {
                        if (!other.methods_.isEmpty()) {
                            if (this.methods_.isEmpty()) {
                                this.methods_ = other.methods_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureMethodsIsMutable();
                                this.methods_.addAll(other.methods_);
                            }
                            onChanged();
                        }
                    } else if (!other.methods_.isEmpty()) {
                        if (!this.methodsBuilder_.isEmpty()) {
                            this.methodsBuilder_.addAllMessages(other.methods_);
                        } else {
                            this.methodsBuilder_.dispose();
                            this.methodsBuilder_ = null;
                            this.methods_ = other.methods_;
                            this.bitField0_ &= -3;
                            this.methodsBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getMethodsFieldBuilder() : null;
                        }
                    }
                    if (this.fieldsBuilder_ == null) {
                        if (!other.fields_.isEmpty()) {
                            if (this.fields_.isEmpty()) {
                                this.fields_ = other.fields_;
                                this.bitField0_ &= -5;
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
                            this.bitField0_ &= -5;
                            this.fieldsBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getFieldsFieldBuilder() : null;
                        }
                    }
                    if (this.propertiesBuilder_ == null) {
                        if (!other.properties_.isEmpty()) {
                            if (this.properties_.isEmpty()) {
                                this.properties_ = other.properties_;
                                this.bitField0_ &= -9;
                            } else {
                                ensurePropertiesIsMutable();
                                this.properties_.addAll(other.properties_);
                            }
                            onChanged();
                        }
                    } else if (!other.properties_.isEmpty()) {
                        if (!this.propertiesBuilder_.isEmpty()) {
                            this.propertiesBuilder_.addAllMessages(other.properties_);
                        } else {
                            this.propertiesBuilder_.dispose();
                            this.propertiesBuilder_ = null;
                            this.properties_ = other.properties_;
                            this.bitField0_ &= -9;
                            this.propertiesBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getPropertiesFieldBuilder() : null;
                        }
                    }
                    if (this.nestedTypesBuilder_ == null) {
                        if (!other.nestedTypes_.isEmpty()) {
                            if (this.nestedTypes_.isEmpty()) {
                                this.nestedTypes_ = other.nestedTypes_;
                                this.bitField0_ &= -17;
                            } else {
                                ensureNestedTypesIsMutable();
                                this.nestedTypes_.addAll(other.nestedTypes_);
                            }
                            onChanged();
                        }
                    } else if (!other.nestedTypes_.isEmpty()) {
                        if (!this.nestedTypesBuilder_.isEmpty()) {
                            this.nestedTypesBuilder_.addAllMessages(other.nestedTypes_);
                        } else {
                            this.nestedTypesBuilder_.dispose();
                            this.nestedTypesBuilder_ = null;
                            this.nestedTypes_ = other.nestedTypes_;
                            this.bitField0_ &= -17;
                            this.nestedTypesBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getNestedTypesFieldBuilder() : null;
                        }
                    }
                    if (this.attributesBuilder_ == null) {
                        if (!other.attributes_.isEmpty()) {
                            if (this.attributes_.isEmpty()) {
                                this.attributes_ = other.attributes_;
                                this.bitField0_ &= -33;
                            } else {
                                ensureAttributesIsMutable();
                                this.attributes_.addAll(other.attributes_);
                            }
                            onChanged();
                        }
                    } else if (!other.attributes_.isEmpty()) {
                        if (!this.attributesBuilder_.isEmpty()) {
                            this.attributesBuilder_.addAllMessages(other.attributes_);
                        } else {
                            this.attributesBuilder_.dispose();
                            this.attributesBuilder_ = null;
                            this.attributes_ = other.attributes_;
                            this.bitField0_ &= -33;
                            this.attributesBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getAttributesFieldBuilder() : null;
                        }
                    }
                    if (this.genericTypeArgumentsBuilder_ == null) {
                        if (!other.genericTypeArguments_.isEmpty()) {
                            if (this.genericTypeArguments_.isEmpty()) {
                                this.genericTypeArguments_ = other.genericTypeArguments_;
                                this.bitField0_ &= -65;
                            } else {
                                ensureGenericTypeArgumentsIsMutable();
                                this.genericTypeArguments_.addAll(other.genericTypeArguments_);
                            }
                            onChanged();
                        }
                    } else if (!other.genericTypeArguments_.isEmpty()) {
                        if (!this.genericTypeArgumentsBuilder_.isEmpty()) {
                            this.genericTypeArgumentsBuilder_.addAllMessages(other.genericTypeArguments_);
                        } else {
                            this.genericTypeArgumentsBuilder_.dispose();
                            this.genericTypeArgumentsBuilder_ = null;
                            this.genericTypeArguments_ = other.genericTypeArguments_;
                            this.bitField0_ &= -65;
                            this.genericTypeArgumentsBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getGenericTypeArgumentsFieldBuilder() : null;
                        }
                    }
                    if (other.getArrayDimensions() != 0) {
                        setArrayDimensions(other.getArrayDimensions());
                    }
                    if (this.eventsBuilder_ == null) {
                        if (!other.events_.isEmpty()) {
                            if (this.events_.isEmpty()) {
                                this.events_ = other.events_;
                                this.bitField0_ &= -129;
                            } else {
                                ensureEventsIsMutable();
                                this.events_.addAll(other.events_);
                            }
                            onChanged();
                        }
                    } else if (!other.events_.isEmpty()) {
                        if (!this.eventsBuilder_.isEmpty()) {
                            this.eventsBuilder_.addAllMessages(other.events_);
                        } else {
                            this.eventsBuilder_.dispose();
                            this.eventsBuilder_ = null;
                            this.events_ = other.events_;
                            this.bitField0_ &= -129;
                            this.eventsBuilder_ = TypeDefinition.alwaysUseFieldBuilders ? getEventsFieldBuilder() : null;
                        }
                    }
                    if (other.getPeToken() != 0) {
                        setPeToken(other.getPeToken());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                    return this;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                TypeDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (TypeDefinition) TypeDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        TypeDefinition typeDefinition = (TypeDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getAccessibilityValue() {
                return this.accessibility_;
            }

            public Builder setAccessibilityValue(int value) {
                this.accessibility_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public Accessibility getAccessibility() {
                Accessibility result = Accessibility.valueOf(this.accessibility_);
                return result == null ? Accessibility.UNRECOGNIZED : result;
            }

            public Builder setAccessibility(Accessibility value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.accessibility_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAccessibility() {
                this.accessibility_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public String getFullname() {
                Object ref = this.fullname_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.fullname_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public ByteString getFullnameBytes() {
                Object ref = this.fullname_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.fullname_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setFullname(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.fullname_ = value;
                onChanged();
                return this;
            }

            public Builder clearFullname() {
                this.fullname_ = TypeDefinition.getDefaultInstance().getFullname();
                onChanged();
                return this;
            }

            public Builder setFullnameBytes(ByteString value) {
                if (value != null) {
                    TypeDefinition.checkByteStringIsUtf8(value);
                    this.fullname_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public String getNamespace() {
                Object ref = this.namespace_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.namespace_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public ByteString getNamespaceBytes() {
                Object ref = this.namespace_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.namespace_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setNamespace(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.namespace_ = value;
                onChanged();
                return this;
            }

            public Builder clearNamespace() {
                this.namespace_ = TypeDefinition.getDefaultInstance().getNamespace();
                onChanged();
                return this;
            }

            public Builder setNamespaceBytes(ByteString value) {
                if (value != null) {
                    TypeDefinition.checkByteStringIsUtf8(value);
                    this.namespace_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public boolean getIsAbstract() {
                return this.isAbstract_;
            }

            public Builder setIsAbstract(boolean value) {
                this.isAbstract_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsAbstract() {
                this.isAbstract_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public boolean getIsReadOnly() {
                return this.isReadOnly_;
            }

            public Builder setIsReadOnly(boolean value) {
                this.isReadOnly_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsReadOnly() {
                this.isReadOnly_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public boolean getIsSealed() {
                return this.isSealed_;
            }

            public Builder setIsSealed(boolean value) {
                this.isSealed_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsSealed() {
                this.isSealed_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public boolean getIsStatic() {
                return this.isStatic_;
            }

            public Builder setIsStatic(boolean value) {
                this.isStatic_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsStatic() {
                this.isStatic_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public String getDeclaringOuterClass() {
                Object ref = this.declaringOuterClass_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.declaringOuterClass_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public ByteString getDeclaringOuterClassBytes() {
                Object ref = this.declaringOuterClass_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.declaringOuterClass_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setDeclaringOuterClass(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.declaringOuterClass_ = value;
                onChanged();
                return this;
            }

            public Builder clearDeclaringOuterClass() {
                this.declaringOuterClass_ = TypeDefinition.getDefaultInstance().getDeclaringOuterClass();
                onChanged();
                return this;
            }

            public Builder setDeclaringOuterClassBytes(ByteString value) {
                if (value != null) {
                    TypeDefinition.checkByteStringIsUtf8(value);
                    this.declaringOuterClass_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            private void ensureDirectBaseTypesIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.directBaseTypes_ = new ArrayList(this.directBaseTypes_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<TypeDefinition> getDirectBaseTypesList() {
                if (this.directBaseTypesBuilder_ == null) {
                    return Collections.unmodifiableList(this.directBaseTypes_);
                }
                return this.directBaseTypesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getDirectBaseTypesCount() {
                if (this.directBaseTypesBuilder_ == null) {
                    return this.directBaseTypes_.size();
                }
                return this.directBaseTypesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinition getDirectBaseTypes(int index) {
                if (this.directBaseTypesBuilder_ == null) {
                    return this.directBaseTypes_.get(index);
                }
                return this.directBaseTypesBuilder_.getMessage(index);
            }

            public Builder setDirectBaseTypes(int index, TypeDefinition value) {
                if (this.directBaseTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.set(index, value);
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setDirectBaseTypes(int index, Builder builderForValue) {
                if (this.directBaseTypesBuilder_ == null) {
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addDirectBaseTypes(TypeDefinition value) {
                if (this.directBaseTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.add(value);
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addDirectBaseTypes(int index, TypeDefinition value) {
                if (this.directBaseTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.add(index, value);
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addDirectBaseTypes(Builder builderForValue) {
                if (this.directBaseTypesBuilder_ == null) {
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addDirectBaseTypes(int index, Builder builderForValue) {
                if (this.directBaseTypesBuilder_ == null) {
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllDirectBaseTypes(Iterable<? extends TypeDefinition> values) {
                if (this.directBaseTypesBuilder_ == null) {
                    ensureDirectBaseTypesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.directBaseTypes_);
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearDirectBaseTypes() {
                if (this.directBaseTypesBuilder_ == null) {
                    this.directBaseTypes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.clear();
                }
                return this;
            }

            public Builder removeDirectBaseTypes(int index) {
                if (this.directBaseTypesBuilder_ == null) {
                    ensureDirectBaseTypesIsMutable();
                    this.directBaseTypes_.remove(index);
                    onChanged();
                } else {
                    this.directBaseTypesBuilder_.remove(index);
                }
                return this;
            }

            public Builder getDirectBaseTypesBuilder(int index) {
                return getDirectBaseTypesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinitionOrBuilder getDirectBaseTypesOrBuilder(int index) {
                if (this.directBaseTypesBuilder_ == null) {
                    return this.directBaseTypes_.get(index);
                }
                return this.directBaseTypesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends TypeDefinitionOrBuilder> getDirectBaseTypesOrBuilderList() {
                if (this.directBaseTypesBuilder_ != null) {
                    return this.directBaseTypesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.directBaseTypes_);
            }

            public Builder addDirectBaseTypesBuilder() {
                return getDirectBaseTypesFieldBuilder().addBuilder(TypeDefinition.getDefaultInstance());
            }

            public Builder addDirectBaseTypesBuilder(int index) {
                return getDirectBaseTypesFieldBuilder().addBuilder(index, TypeDefinition.getDefaultInstance());
            }

            public List<Builder> getDirectBaseTypesBuilderList() {
                return getDirectBaseTypesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> getDirectBaseTypesFieldBuilder() {
                if (this.directBaseTypesBuilder_ == null) {
                    this.directBaseTypesBuilder_ = new RepeatedFieldBuilderV3<>(this.directBaseTypes_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.directBaseTypes_ = null;
                }
                return this.directBaseTypesBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getTypeKindValue() {
                return this.typeKind_;
            }

            public Builder setTypeKindValue(int value) {
                this.typeKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeKindDef getTypeKind() {
                TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
                return result == null ? TypeKindDef.UNRECOGNIZED : result;
            }

            public Builder setTypeKind(TypeKindDef value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.typeKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearTypeKind() {
                this.typeKind_ = 0;
                onChanged();
                return this;
            }

            private void ensureMethodsIsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.methods_ = new ArrayList(this.methods_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<MethodDefinition> getMethodsList() {
                if (this.methodsBuilder_ == null) {
                    return Collections.unmodifiableList(this.methods_);
                }
                return this.methodsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getMethodsCount() {
                if (this.methodsBuilder_ == null) {
                    return this.methods_.size();
                }
                return this.methodsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public MethodDefinition getMethods(int index) {
                if (this.methodsBuilder_ == null) {
                    return this.methods_.get(index);
                }
                return this.methodsBuilder_.getMessage(index);
            }

            public Builder setMethods(int index, MethodDefinition value) {
                if (this.methodsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMethodsIsMutable();
                    this.methods_.set(index, value);
                    onChanged();
                } else {
                    this.methodsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setMethods(int index, MethodDefinition.Builder builderForValue) {
                if (this.methodsBuilder_ == null) {
                    ensureMethodsIsMutable();
                    this.methods_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.methodsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addMethods(MethodDefinition value) {
                if (this.methodsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMethodsIsMutable();
                    this.methods_.add(value);
                    onChanged();
                } else {
                    this.methodsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addMethods(int index, MethodDefinition value) {
                if (this.methodsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureMethodsIsMutable();
                    this.methods_.add(index, value);
                    onChanged();
                } else {
                    this.methodsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addMethods(MethodDefinition.Builder builderForValue) {
                if (this.methodsBuilder_ == null) {
                    ensureMethodsIsMutable();
                    this.methods_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.methodsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addMethods(int index, MethodDefinition.Builder builderForValue) {
                if (this.methodsBuilder_ == null) {
                    ensureMethodsIsMutable();
                    this.methods_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.methodsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllMethods(Iterable<? extends MethodDefinition> values) {
                if (this.methodsBuilder_ == null) {
                    ensureMethodsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.methods_);
                    onChanged();
                } else {
                    this.methodsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearMethods() {
                if (this.methodsBuilder_ == null) {
                    this.methods_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.methodsBuilder_.clear();
                }
                return this;
            }

            public Builder removeMethods(int index) {
                if (this.methodsBuilder_ == null) {
                    ensureMethodsIsMutable();
                    this.methods_.remove(index);
                    onChanged();
                } else {
                    this.methodsBuilder_.remove(index);
                }
                return this;
            }

            public MethodDefinition.Builder getMethodsBuilder(int index) {
                return getMethodsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public MethodDefinitionOrBuilder getMethodsOrBuilder(int index) {
                if (this.methodsBuilder_ == null) {
                    return this.methods_.get(index);
                }
                return this.methodsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends MethodDefinitionOrBuilder> getMethodsOrBuilderList() {
                if (this.methodsBuilder_ != null) {
                    return this.methodsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.methods_);
            }

            public MethodDefinition.Builder addMethodsBuilder() {
                return getMethodsFieldBuilder().addBuilder(MethodDefinition.getDefaultInstance());
            }

            public MethodDefinition.Builder addMethodsBuilder(int index) {
                return getMethodsFieldBuilder().addBuilder(index, MethodDefinition.getDefaultInstance());
            }

            public List<MethodDefinition.Builder> getMethodsBuilderList() {
                return getMethodsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getMethodsFieldBuilder() {
                if (this.methodsBuilder_ == null) {
                    this.methodsBuilder_ = new RepeatedFieldBuilderV3<>(this.methods_, (this.bitField0_ & 2) != 0, getParentForChildren(), isClean());
                    this.methods_ = null;
                }
                return this.methodsBuilder_;
            }

            private void ensureFieldsIsMutable() {
                if ((this.bitField0_ & 4) == 0) {
                    this.fields_ = new ArrayList(this.fields_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<FieldDefinition> getFieldsList() {
                if (this.fieldsBuilder_ == null) {
                    return Collections.unmodifiableList(this.fields_);
                }
                return this.fieldsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getFieldsCount() {
                if (this.fieldsBuilder_ == null) {
                    return this.fields_.size();
                }
                return this.fieldsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public FieldDefinition getFields(int index) {
                if (this.fieldsBuilder_ == null) {
                    return this.fields_.get(index);
                }
                return this.fieldsBuilder_.getMessage(index);
            }

            public Builder setFields(int index, FieldDefinition value) {
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

            public Builder setFields(int index, FieldDefinition.Builder builderForValue) {
                if (this.fieldsBuilder_ == null) {
                    ensureFieldsIsMutable();
                    this.fields_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.fieldsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addFields(FieldDefinition value) {
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

            public Builder addFields(int index, FieldDefinition value) {
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

            public Builder addFields(FieldDefinition.Builder builderForValue) {
                if (this.fieldsBuilder_ == null) {
                    ensureFieldsIsMutable();
                    this.fields_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.fieldsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addFields(int index, FieldDefinition.Builder builderForValue) {
                if (this.fieldsBuilder_ == null) {
                    ensureFieldsIsMutable();
                    this.fields_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.fieldsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllFields(Iterable<? extends FieldDefinition> values) {
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
                    this.bitField0_ &= -5;
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

            public FieldDefinition.Builder getFieldsBuilder(int index) {
                return getFieldsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public FieldDefinitionOrBuilder getFieldsOrBuilder(int index) {
                if (this.fieldsBuilder_ == null) {
                    return this.fields_.get(index);
                }
                return this.fieldsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends FieldDefinitionOrBuilder> getFieldsOrBuilderList() {
                if (this.fieldsBuilder_ != null) {
                    return this.fieldsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.fields_);
            }

            public FieldDefinition.Builder addFieldsBuilder() {
                return getFieldsFieldBuilder().addBuilder(FieldDefinition.getDefaultInstance());
            }

            public FieldDefinition.Builder addFieldsBuilder(int index) {
                return getFieldsFieldBuilder().addBuilder(index, FieldDefinition.getDefaultInstance());
            }

            public List<FieldDefinition.Builder> getFieldsBuilderList() {
                return getFieldsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<FieldDefinition, FieldDefinition.Builder, FieldDefinitionOrBuilder> getFieldsFieldBuilder() {
                if (this.fieldsBuilder_ == null) {
                    this.fieldsBuilder_ = new RepeatedFieldBuilderV3<>(this.fields_, (this.bitField0_ & 4) != 0, getParentForChildren(), isClean());
                    this.fields_ = null;
                }
                return this.fieldsBuilder_;
            }

            private void ensurePropertiesIsMutable() {
                if ((this.bitField0_ & 8) == 0) {
                    this.properties_ = new ArrayList(this.properties_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<PropertyDefinition> getPropertiesList() {
                if (this.propertiesBuilder_ == null) {
                    return Collections.unmodifiableList(this.properties_);
                }
                return this.propertiesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getPropertiesCount() {
                if (this.propertiesBuilder_ == null) {
                    return this.properties_.size();
                }
                return this.propertiesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public PropertyDefinition getProperties(int index) {
                if (this.propertiesBuilder_ == null) {
                    return this.properties_.get(index);
                }
                return this.propertiesBuilder_.getMessage(index);
            }

            public Builder setProperties(int index, PropertyDefinition value) {
                if (this.propertiesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePropertiesIsMutable();
                    this.properties_.set(index, value);
                    onChanged();
                } else {
                    this.propertiesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setProperties(int index, PropertyDefinition.Builder builderForValue) {
                if (this.propertiesBuilder_ == null) {
                    ensurePropertiesIsMutable();
                    this.properties_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.propertiesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addProperties(PropertyDefinition value) {
                if (this.propertiesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePropertiesIsMutable();
                    this.properties_.add(value);
                    onChanged();
                } else {
                    this.propertiesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addProperties(int index, PropertyDefinition value) {
                if (this.propertiesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePropertiesIsMutable();
                    this.properties_.add(index, value);
                    onChanged();
                } else {
                    this.propertiesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addProperties(PropertyDefinition.Builder builderForValue) {
                if (this.propertiesBuilder_ == null) {
                    ensurePropertiesIsMutable();
                    this.properties_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.propertiesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addProperties(int index, PropertyDefinition.Builder builderForValue) {
                if (this.propertiesBuilder_ == null) {
                    ensurePropertiesIsMutable();
                    this.properties_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.propertiesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllProperties(Iterable<? extends PropertyDefinition> values) {
                if (this.propertiesBuilder_ == null) {
                    ensurePropertiesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.properties_);
                    onChanged();
                } else {
                    this.propertiesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearProperties() {
                if (this.propertiesBuilder_ == null) {
                    this.properties_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                    onChanged();
                } else {
                    this.propertiesBuilder_.clear();
                }
                return this;
            }

            public Builder removeProperties(int index) {
                if (this.propertiesBuilder_ == null) {
                    ensurePropertiesIsMutable();
                    this.properties_.remove(index);
                    onChanged();
                } else {
                    this.propertiesBuilder_.remove(index);
                }
                return this;
            }

            public PropertyDefinition.Builder getPropertiesBuilder(int index) {
                return getPropertiesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public PropertyDefinitionOrBuilder getPropertiesOrBuilder(int index) {
                if (this.propertiesBuilder_ == null) {
                    return this.properties_.get(index);
                }
                return this.propertiesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends PropertyDefinitionOrBuilder> getPropertiesOrBuilderList() {
                if (this.propertiesBuilder_ != null) {
                    return this.propertiesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.properties_);
            }

            public PropertyDefinition.Builder addPropertiesBuilder() {
                return getPropertiesFieldBuilder().addBuilder(PropertyDefinition.getDefaultInstance());
            }

            public PropertyDefinition.Builder addPropertiesBuilder(int index) {
                return getPropertiesFieldBuilder().addBuilder(index, PropertyDefinition.getDefaultInstance());
            }

            public List<PropertyDefinition.Builder> getPropertiesBuilderList() {
                return getPropertiesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<PropertyDefinition, PropertyDefinition.Builder, PropertyDefinitionOrBuilder> getPropertiesFieldBuilder() {
                if (this.propertiesBuilder_ == null) {
                    this.propertiesBuilder_ = new RepeatedFieldBuilderV3<>(this.properties_, (this.bitField0_ & 8) != 0, getParentForChildren(), isClean());
                    this.properties_ = null;
                }
                return this.propertiesBuilder_;
            }

            private void ensureNestedTypesIsMutable() {
                if ((this.bitField0_ & 16) == 0) {
                    this.nestedTypes_ = new ArrayList(this.nestedTypes_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<TypeDefinition> getNestedTypesList() {
                if (this.nestedTypesBuilder_ == null) {
                    return Collections.unmodifiableList(this.nestedTypes_);
                }
                return this.nestedTypesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getNestedTypesCount() {
                if (this.nestedTypesBuilder_ == null) {
                    return this.nestedTypes_.size();
                }
                return this.nestedTypesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinition getNestedTypes(int index) {
                if (this.nestedTypesBuilder_ == null) {
                    return this.nestedTypes_.get(index);
                }
                return this.nestedTypesBuilder_.getMessage(index);
            }

            public Builder setNestedTypes(int index, TypeDefinition value) {
                if (this.nestedTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.set(index, value);
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setNestedTypes(int index, Builder builderForValue) {
                if (this.nestedTypesBuilder_ == null) {
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addNestedTypes(TypeDefinition value) {
                if (this.nestedTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.add(value);
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addNestedTypes(int index, TypeDefinition value) {
                if (this.nestedTypesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.add(index, value);
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addNestedTypes(Builder builderForValue) {
                if (this.nestedTypesBuilder_ == null) {
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addNestedTypes(int index, Builder builderForValue) {
                if (this.nestedTypesBuilder_ == null) {
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllNestedTypes(Iterable<? extends TypeDefinition> values) {
                if (this.nestedTypesBuilder_ == null) {
                    ensureNestedTypesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.nestedTypes_);
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearNestedTypes() {
                if (this.nestedTypesBuilder_ == null) {
                    this.nestedTypes_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.clear();
                }
                return this;
            }

            public Builder removeNestedTypes(int index) {
                if (this.nestedTypesBuilder_ == null) {
                    ensureNestedTypesIsMutable();
                    this.nestedTypes_.remove(index);
                    onChanged();
                } else {
                    this.nestedTypesBuilder_.remove(index);
                }
                return this;
            }

            public Builder getNestedTypesBuilder(int index) {
                return getNestedTypesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinitionOrBuilder getNestedTypesOrBuilder(int index) {
                if (this.nestedTypesBuilder_ == null) {
                    return this.nestedTypes_.get(index);
                }
                return this.nestedTypesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends TypeDefinitionOrBuilder> getNestedTypesOrBuilderList() {
                if (this.nestedTypesBuilder_ != null) {
                    return this.nestedTypesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.nestedTypes_);
            }

            public Builder addNestedTypesBuilder() {
                return getNestedTypesFieldBuilder().addBuilder(TypeDefinition.getDefaultInstance());
            }

            public Builder addNestedTypesBuilder(int index) {
                return getNestedTypesFieldBuilder().addBuilder(index, TypeDefinition.getDefaultInstance());
            }

            public List<Builder> getNestedTypesBuilderList() {
                return getNestedTypesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> getNestedTypesFieldBuilder() {
                if (this.nestedTypesBuilder_ == null) {
                    this.nestedTypesBuilder_ = new RepeatedFieldBuilderV3<>(this.nestedTypes_, (this.bitField0_ & 16) != 0, getParentForChildren(), isClean());
                    this.nestedTypes_ = null;
                }
                return this.nestedTypesBuilder_;
            }

            private void ensureAttributesIsMutable() {
                if ((this.bitField0_ & 32) == 0) {
                    this.attributes_ = new ArrayList(this.attributes_);
                    this.bitField0_ |= 32;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<AttributeDefinition> getAttributesList() {
                if (this.attributesBuilder_ == null) {
                    return Collections.unmodifiableList(this.attributes_);
                }
                return this.attributesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getAttributesCount() {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.size();
                }
                return this.attributesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public AttributeDefinition getAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessage(index);
            }

            public Builder setAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllAttributes(Iterable<? extends AttributeDefinition> values) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.attributes_);
                    onChanged();
                } else {
                    this.attributesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearAttributes() {
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -33;
                    onChanged();
                } else {
                    this.attributesBuilder_.clear();
                }
                return this;
            }

            public Builder removeAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.remove(index);
                    onChanged();
                } else {
                    this.attributesBuilder_.remove(index);
                }
                return this;
            }

            public AttributeDefinition.Builder getAttributesBuilder(int index) {
                return getAttributesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
                if (this.attributesBuilder_ != null) {
                    return this.attributesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.attributes_);
            }

            public AttributeDefinition.Builder addAttributesBuilder() {
                return getAttributesFieldBuilder().addBuilder(AttributeDefinition.getDefaultInstance());
            }

            public AttributeDefinition.Builder addAttributesBuilder(int index) {
                return getAttributesFieldBuilder().addBuilder(index, AttributeDefinition.getDefaultInstance());
            }

            public List<AttributeDefinition.Builder> getAttributesBuilderList() {
                return getAttributesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> getAttributesFieldBuilder() {
                if (this.attributesBuilder_ == null) {
                    this.attributesBuilder_ = new RepeatedFieldBuilderV3<>(this.attributes_, (this.bitField0_ & 32) != 0, getParentForChildren(), isClean());
                    this.attributes_ = null;
                }
                return this.attributesBuilder_;
            }

            private void ensureGenericTypeArgumentsIsMutable() {
                if ((this.bitField0_ & 64) == 0) {
                    this.genericTypeArguments_ = new ArrayList(this.genericTypeArguments_);
                    this.bitField0_ |= 64;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<TypeDefinition> getGenericTypeArgumentsList() {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    return Collections.unmodifiableList(this.genericTypeArguments_);
                }
                return this.genericTypeArgumentsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getGenericTypeArgumentsCount() {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    return this.genericTypeArguments_.size();
                }
                return this.genericTypeArgumentsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinition getGenericTypeArguments(int index) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    return this.genericTypeArguments_.get(index);
                }
                return this.genericTypeArgumentsBuilder_.getMessage(index);
            }

            public Builder setGenericTypeArguments(int index, TypeDefinition value) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.set(index, value);
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setGenericTypeArguments(int index, Builder builderForValue) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addGenericTypeArguments(TypeDefinition value) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.add(value);
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addGenericTypeArguments(int index, TypeDefinition value) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.add(index, value);
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addGenericTypeArguments(Builder builderForValue) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addGenericTypeArguments(int index, Builder builderForValue) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllGenericTypeArguments(Iterable<? extends TypeDefinition> values) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    ensureGenericTypeArgumentsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.genericTypeArguments_);
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearGenericTypeArguments() {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    this.genericTypeArguments_ = Collections.emptyList();
                    this.bitField0_ &= -65;
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.clear();
                }
                return this;
            }

            public Builder removeGenericTypeArguments(int index) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    ensureGenericTypeArgumentsIsMutable();
                    this.genericTypeArguments_.remove(index);
                    onChanged();
                } else {
                    this.genericTypeArgumentsBuilder_.remove(index);
                }
                return this;
            }

            public Builder getGenericTypeArgumentsBuilder(int index) {
                return getGenericTypeArgumentsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public TypeDefinitionOrBuilder getGenericTypeArgumentsOrBuilder(int index) {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    return this.genericTypeArguments_.get(index);
                }
                return this.genericTypeArgumentsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends TypeDefinitionOrBuilder> getGenericTypeArgumentsOrBuilderList() {
                if (this.genericTypeArgumentsBuilder_ != null) {
                    return this.genericTypeArgumentsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.genericTypeArguments_);
            }

            public Builder addGenericTypeArgumentsBuilder() {
                return getGenericTypeArgumentsFieldBuilder().addBuilder(TypeDefinition.getDefaultInstance());
            }

            public Builder addGenericTypeArgumentsBuilder(int index) {
                return getGenericTypeArgumentsFieldBuilder().addBuilder(index, TypeDefinition.getDefaultInstance());
            }

            public List<Builder> getGenericTypeArgumentsBuilderList() {
                return getGenericTypeArgumentsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<TypeDefinition, Builder, TypeDefinitionOrBuilder> getGenericTypeArgumentsFieldBuilder() {
                if (this.genericTypeArgumentsBuilder_ == null) {
                    this.genericTypeArgumentsBuilder_ = new RepeatedFieldBuilderV3<>(this.genericTypeArguments_, (this.bitField0_ & 64) != 0, getParentForChildren(), isClean());
                    this.genericTypeArguments_ = null;
                }
                return this.genericTypeArgumentsBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getArrayDimensions() {
                return this.arrayDimensions_;
            }

            public Builder setArrayDimensions(int value) {
                this.arrayDimensions_ = value;
                onChanged();
                return this;
            }

            public Builder clearArrayDimensions() {
                this.arrayDimensions_ = 0;
                onChanged();
                return this;
            }

            private void ensureEventsIsMutable() {
                if ((this.bitField0_ & 128) == 0) {
                    this.events_ = new ArrayList(this.events_);
                    this.bitField0_ |= 128;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<EventDefinition> getEventsList() {
                if (this.eventsBuilder_ == null) {
                    return Collections.unmodifiableList(this.events_);
                }
                return this.eventsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getEventsCount() {
                if (this.eventsBuilder_ == null) {
                    return this.events_.size();
                }
                return this.eventsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public EventDefinition getEvents(int index) {
                if (this.eventsBuilder_ == null) {
                    return this.events_.get(index);
                }
                return this.eventsBuilder_.getMessage(index);
            }

            public Builder setEvents(int index, EventDefinition value) {
                if (this.eventsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureEventsIsMutable();
                    this.events_.set(index, value);
                    onChanged();
                } else {
                    this.eventsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setEvents(int index, EventDefinition.Builder builderForValue) {
                if (this.eventsBuilder_ == null) {
                    ensureEventsIsMutable();
                    this.events_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.eventsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addEvents(EventDefinition value) {
                if (this.eventsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureEventsIsMutable();
                    this.events_.add(value);
                    onChanged();
                } else {
                    this.eventsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addEvents(int index, EventDefinition value) {
                if (this.eventsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureEventsIsMutable();
                    this.events_.add(index, value);
                    onChanged();
                } else {
                    this.eventsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addEvents(EventDefinition.Builder builderForValue) {
                if (this.eventsBuilder_ == null) {
                    ensureEventsIsMutable();
                    this.events_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.eventsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addEvents(int index, EventDefinition.Builder builderForValue) {
                if (this.eventsBuilder_ == null) {
                    ensureEventsIsMutable();
                    this.events_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.eventsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllEvents(Iterable<? extends EventDefinition> values) {
                if (this.eventsBuilder_ == null) {
                    ensureEventsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.events_);
                    onChanged();
                } else {
                    this.eventsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearEvents() {
                if (this.eventsBuilder_ == null) {
                    this.events_ = Collections.emptyList();
                    this.bitField0_ &= -129;
                    onChanged();
                } else {
                    this.eventsBuilder_.clear();
                }
                return this;
            }

            public Builder removeEvents(int index) {
                if (this.eventsBuilder_ == null) {
                    ensureEventsIsMutable();
                    this.events_.remove(index);
                    onChanged();
                } else {
                    this.eventsBuilder_.remove(index);
                }
                return this;
            }

            public EventDefinition.Builder getEventsBuilder(int index) {
                return getEventsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public EventDefinitionOrBuilder getEventsOrBuilder(int index) {
                if (this.eventsBuilder_ == null) {
                    return this.events_.get(index);
                }
                return this.eventsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public List<? extends EventDefinitionOrBuilder> getEventsOrBuilderList() {
                if (this.eventsBuilder_ != null) {
                    return this.eventsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.events_);
            }

            public EventDefinition.Builder addEventsBuilder() {
                return getEventsFieldBuilder().addBuilder(EventDefinition.getDefaultInstance());
            }

            public EventDefinition.Builder addEventsBuilder(int index) {
                return getEventsFieldBuilder().addBuilder(index, EventDefinition.getDefaultInstance());
            }

            public List<EventDefinition.Builder> getEventsBuilderList() {
                return getEventsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<EventDefinition, EventDefinition.Builder, EventDefinitionOrBuilder> getEventsFieldBuilder() {
                if (this.eventsBuilder_ == null) {
                    this.eventsBuilder_ = new RepeatedFieldBuilderV3<>(this.events_, (this.bitField0_ & 128) != 0, getParentForChildren(), isClean());
                    this.events_ = null;
                }
                return this.eventsBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.TypeDefinitionOrBuilder
            public int getPeToken() {
                return this.peToken_;
            }

            public Builder setPeToken(int value) {
                this.peToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearPeToken() {
                this.peToken_ = 0;
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

        public static TypeDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<TypeDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<TypeDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public TypeDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$MethodDefinition.class */
    public static final class MethodDefinition extends GeneratedMessageV3 implements MethodDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ACCESSIBILITY_FIELD_NUMBER = 1;
        private int accessibility_;
        public static final int NAME_FIELD_NUMBER = 2;
        private volatile Object name_;
        public static final int HAS_BODY_FIELD_NUMBER = 3;
        private boolean hasBody_;
        public static final int PARAMETER_FIELD_NUMBER = 4;
        private List<ParameterDefinition> parameter_;
        public static final int IS_ABSTRACT_FIELD_NUMBER = 5;
        private boolean isAbstract_;
        public static final int IS_ACCESSOR_FIELD_NUMBER = 6;
        private boolean isAccessor_;
        public static final int IS_CONSTRUCTOR_FIELD_NUMBER = 7;
        private boolean isConstructor_;
        public static final int IS_DESTRUCTOR_FIELD_NUMBER = 8;
        private boolean isDestructor_;
        public static final int IS_EXPLICIT_INTERFACE_IMPLEMENTATION_FIELD_NUMBER = 9;
        private boolean isExplicitInterfaceImplementation_;
        public static final int IS_STATIC_FIELD_NUMBER = 10;
        private boolean isStatic_;
        public static final int IS_VIRTUAL_FIELD_NUMBER = 11;
        private boolean isVirtual_;
        public static final int IS_OPERATOR_FIELD_NUMBER = 16;
        private boolean isOperator_;
        public static final int IS_EXTERN_FIELD_NUMBER = 17;
        private boolean isExtern_;
        public static final int IS_UNSAFE_FIELD_NUMBER = 18;
        private boolean isUnsafe_;
        public static final int IS_SEALED_FIELD_NUMBER = 19;
        private boolean isSealed_;
        public static final int RETURN_TYPE_FIELD_NUMBER = 12;
        private TypeDefinition returnType_;
        public static final int ATTRIBUTES_FIELD_NUMBER = 13;
        private List<AttributeDefinition> attributes_;
        public static final int FULL_NAME_FIELD_NUMBER = 14;
        private volatile Object fullName_;
        public static final int DECLARING_TYPE_FIELD_NUMBER = 15;
        private TypeDefinition declaringType_;
        public static final int PE_TOKEN_FIELD_NUMBER = 20;
        private int peToken_;
        private byte memoizedIsInitialized;
        private static final MethodDefinition DEFAULT_INSTANCE = new MethodDefinition();
        private static final Parser<MethodDefinition> PARSER = new AbstractParser<MethodDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinition.1
            @Override // com.google.protobuf.Parser
            public MethodDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new MethodDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ MethodDefinition(GeneratedMessageV3.Builder builder, MethodDefinition methodDefinition) {
            this(builder);
        }

        private MethodDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private MethodDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.accessibility_ = 0;
            this.name_ = "";
            this.parameter_ = Collections.emptyList();
            this.attributes_ = Collections.emptyList();
            this.fullName_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new MethodDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ MethodDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, MethodDefinition methodDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private MethodDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                this.accessibility_ = rawValue;
                                break;
                            case 18:
                                String s = input.readStringRequireUtf8();
                                this.name_ = s;
                                break;
                            case 24:
                                this.hasBody_ = input.readBool();
                                break;
                            case 34:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.parameter_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.parameter_.add((ParameterDefinition) input.readMessage(ParameterDefinition.parser(), extensionRegistry));
                                break;
                            case 40:
                                this.isAbstract_ = input.readBool();
                                break;
                            case 48:
                                this.isAccessor_ = input.readBool();
                                break;
                            case 56:
                                this.isConstructor_ = input.readBool();
                                break;
                            case 64:
                                this.isDestructor_ = input.readBool();
                                break;
                            case 72:
                                this.isExplicitInterfaceImplementation_ = input.readBool();
                                break;
                            case 80:
                                this.isStatic_ = input.readBool();
                                break;
                            case 88:
                                this.isVirtual_ = input.readBool();
                                break;
                            case 98:
                                TypeDefinition.Builder subBuilder = null;
                                subBuilder = this.returnType_ != null ? this.returnType_.toBuilder() : subBuilder;
                                this.returnType_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.returnType_);
                                    this.returnType_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 106:
                                if ((mutable_bitField0_ & 2) == 0) {
                                    this.attributes_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.attributes_.add((AttributeDefinition) input.readMessage(AttributeDefinition.parser(), extensionRegistry));
                                break;
                            case 114:
                                String s2 = input.readStringRequireUtf8();
                                this.fullName_ = s2;
                                break;
                            case 122:
                                TypeDefinition.Builder subBuilder2 = null;
                                subBuilder2 = this.declaringType_ != null ? this.declaringType_.toBuilder() : subBuilder2;
                                this.declaringType_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.declaringType_);
                                    this.declaringType_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 128:
                                this.isOperator_ = input.readBool();
                                break;
                            case 136:
                                this.isExtern_ = input.readBool();
                                break;
                            case 144:
                                this.isUnsafe_ = input.readBool();
                                break;
                            case 152:
                                this.isSealed_ = input.readBool();
                                break;
                            case 160:
                                this.peToken_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.parameter_ = Collections.unmodifiableList(this.parameter_);
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_MethodDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_MethodDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public int getAccessibilityValue() {
            return this.accessibility_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public Accessibility getAccessibility() {
            Accessibility result = Accessibility.valueOf(this.accessibility_);
            return result == null ? Accessibility.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
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

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getHasBody() {
            return this.hasBody_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public List<ParameterDefinition> getParameterList() {
            return this.parameter_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public List<? extends ParameterDefinitionOrBuilder> getParameterOrBuilderList() {
            return this.parameter_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public int getParameterCount() {
            return this.parameter_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public ParameterDefinition getParameter(int index) {
            return this.parameter_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public ParameterDefinitionOrBuilder getParameterOrBuilder(int index) {
            return this.parameter_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsAbstract() {
            return this.isAbstract_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsAccessor() {
            return this.isAccessor_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsConstructor() {
            return this.isConstructor_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsDestructor() {
            return this.isDestructor_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsExplicitInterfaceImplementation() {
            return this.isExplicitInterfaceImplementation_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsStatic() {
            return this.isStatic_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsVirtual() {
            return this.isVirtual_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsOperator() {
            return this.isOperator_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsExtern() {
            return this.isExtern_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsUnsafe() {
            return this.isUnsafe_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean getIsSealed() {
            return this.isSealed_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean hasReturnType() {
            return this.returnType_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public TypeDefinition getReturnType() {
            return this.returnType_ == null ? TypeDefinition.getDefaultInstance() : this.returnType_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public TypeDefinitionOrBuilder getReturnTypeOrBuilder() {
            return getReturnType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public List<AttributeDefinition> getAttributesList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public int getAttributesCount() {
            return this.attributes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public AttributeDefinition getAttributes(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public String getFullName() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.fullName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public ByteString getFullNameBytes() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.fullName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public boolean hasDeclaringType() {
            return this.declaringType_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public TypeDefinition getDeclaringType() {
            return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public TypeDefinitionOrBuilder getDeclaringTypeOrBuilder() {
            return getDeclaringType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
        public int getPeToken() {
            return this.peToken_;
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
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                output.writeEnum(1, this.accessibility_);
            }
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.name_);
            }
            if (this.hasBody_) {
                output.writeBool(3, this.hasBody_);
            }
            for (int i = 0; i < this.parameter_.size(); i++) {
                output.writeMessage(4, this.parameter_.get(i));
            }
            if (this.isAbstract_) {
                output.writeBool(5, this.isAbstract_);
            }
            if (this.isAccessor_) {
                output.writeBool(6, this.isAccessor_);
            }
            if (this.isConstructor_) {
                output.writeBool(7, this.isConstructor_);
            }
            if (this.isDestructor_) {
                output.writeBool(8, this.isDestructor_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                output.writeBool(9, this.isExplicitInterfaceImplementation_);
            }
            if (this.isStatic_) {
                output.writeBool(10, this.isStatic_);
            }
            if (this.isVirtual_) {
                output.writeBool(11, this.isVirtual_);
            }
            if (this.returnType_ != null) {
                output.writeMessage(12, getReturnType());
            }
            for (int i2 = 0; i2 < this.attributes_.size(); i2++) {
                output.writeMessage(13, this.attributes_.get(i2));
            }
            if (!getFullNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 14, this.fullName_);
            }
            if (this.declaringType_ != null) {
                output.writeMessage(15, getDeclaringType());
            }
            if (this.isOperator_) {
                output.writeBool(16, this.isOperator_);
            }
            if (this.isExtern_) {
                output.writeBool(17, this.isExtern_);
            }
            if (this.isUnsafe_) {
                output.writeBool(18, this.isUnsafe_);
            }
            if (this.isSealed_) {
                output.writeBool(19, this.isSealed_);
            }
            if (this.peToken_ != 0) {
                output.writeInt32(20, this.peToken_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.accessibility_);
            }
            if (!getNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.name_);
            }
            if (this.hasBody_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.hasBody_);
            }
            for (int i = 0; i < this.parameter_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.parameter_.get(i));
            }
            if (this.isAbstract_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.isAbstract_);
            }
            if (this.isAccessor_) {
                size2 += CodedOutputStream.computeBoolSize(6, this.isAccessor_);
            }
            if (this.isConstructor_) {
                size2 += CodedOutputStream.computeBoolSize(7, this.isConstructor_);
            }
            if (this.isDestructor_) {
                size2 += CodedOutputStream.computeBoolSize(8, this.isDestructor_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                size2 += CodedOutputStream.computeBoolSize(9, this.isExplicitInterfaceImplementation_);
            }
            if (this.isStatic_) {
                size2 += CodedOutputStream.computeBoolSize(10, this.isStatic_);
            }
            if (this.isVirtual_) {
                size2 += CodedOutputStream.computeBoolSize(11, this.isVirtual_);
            }
            if (this.returnType_ != null) {
                size2 += CodedOutputStream.computeMessageSize(12, getReturnType());
            }
            for (int i2 = 0; i2 < this.attributes_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(13, this.attributes_.get(i2));
            }
            if (!getFullNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(14, this.fullName_);
            }
            if (this.declaringType_ != null) {
                size2 += CodedOutputStream.computeMessageSize(15, getDeclaringType());
            }
            if (this.isOperator_) {
                size2 += CodedOutputStream.computeBoolSize(16, this.isOperator_);
            }
            if (this.isExtern_) {
                size2 += CodedOutputStream.computeBoolSize(17, this.isExtern_);
            }
            if (this.isUnsafe_) {
                size2 += CodedOutputStream.computeBoolSize(18, this.isUnsafe_);
            }
            if (this.isSealed_) {
                size2 += CodedOutputStream.computeBoolSize(19, this.isSealed_);
            }
            if (this.peToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(20, this.peToken_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof MethodDefinition)) {
                return super.equals(obj);
            }
            MethodDefinition other = (MethodDefinition) obj;
            if (this.accessibility_ != other.accessibility_ || !getName().equals(other.getName()) || getHasBody() != other.getHasBody() || !getParameterList().equals(other.getParameterList()) || getIsAbstract() != other.getIsAbstract() || getIsAccessor() != other.getIsAccessor() || getIsConstructor() != other.getIsConstructor() || getIsDestructor() != other.getIsDestructor() || getIsExplicitInterfaceImplementation() != other.getIsExplicitInterfaceImplementation() || getIsStatic() != other.getIsStatic() || getIsVirtual() != other.getIsVirtual() || getIsOperator() != other.getIsOperator() || getIsExtern() != other.getIsExtern() || getIsUnsafe() != other.getIsUnsafe() || getIsSealed() != other.getIsSealed() || hasReturnType() != other.hasReturnType()) {
                return false;
            }
            if ((hasReturnType() && !getReturnType().equals(other.getReturnType())) || !getAttributesList().equals(other.getAttributesList()) || !getFullName().equals(other.getFullName()) || hasDeclaringType() != other.hasDeclaringType()) {
                return false;
            }
            if ((hasDeclaringType() && !getDeclaringType().equals(other.getDeclaringType())) || getPeToken() != other.getPeToken() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            int hash2 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 1)) + this.accessibility_)) + 2)) + getName().hashCode())) + 3)) + Internal.hashBoolean(getHasBody());
            if (getParameterCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 4)) + getParameterList().hashCode();
            }
            int hash3 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash2) + 5)) + Internal.hashBoolean(getIsAbstract()))) + 6)) + Internal.hashBoolean(getIsAccessor()))) + 7)) + Internal.hashBoolean(getIsConstructor()))) + 8)) + Internal.hashBoolean(getIsDestructor()))) + 9)) + Internal.hashBoolean(getIsExplicitInterfaceImplementation()))) + 10)) + Internal.hashBoolean(getIsStatic()))) + 11)) + Internal.hashBoolean(getIsVirtual()))) + 16)) + Internal.hashBoolean(getIsOperator()))) + 17)) + Internal.hashBoolean(getIsExtern()))) + 18)) + Internal.hashBoolean(getIsUnsafe()))) + 19)) + Internal.hashBoolean(getIsSealed());
            if (hasReturnType()) {
                hash3 = (53 * ((37 * hash3) + 12)) + getReturnType().hashCode();
            }
            if (getAttributesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 13)) + getAttributesList().hashCode();
            }
            int hash4 = (53 * ((37 * hash3) + 14)) + getFullName().hashCode();
            if (hasDeclaringType()) {
                hash4 = (53 * ((37 * hash4) + 15)) + getDeclaringType().hashCode();
            }
            int hash5 = (29 * ((53 * ((37 * hash4) + 20)) + getPeToken())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash5;
            return hash5;
        }

        public static MethodDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static MethodDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static MethodDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static MethodDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static MethodDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static MethodDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static MethodDefinition parseFrom(InputStream input) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static MethodDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static MethodDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static MethodDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static MethodDefinition parseFrom(CodedInputStream input) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static MethodDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (MethodDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(MethodDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$MethodDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements MethodDefinitionOrBuilder {
            private int bitField0_;
            private int accessibility_;
            private Object name_;
            private boolean hasBody_;
            private List<ParameterDefinition> parameter_;
            private RepeatedFieldBuilderV3<ParameterDefinition, ParameterDefinition.Builder, ParameterDefinitionOrBuilder> parameterBuilder_;
            private boolean isAbstract_;
            private boolean isAccessor_;
            private boolean isConstructor_;
            private boolean isDestructor_;
            private boolean isExplicitInterfaceImplementation_;
            private boolean isStatic_;
            private boolean isVirtual_;
            private boolean isOperator_;
            private boolean isExtern_;
            private boolean isUnsafe_;
            private boolean isSealed_;
            private TypeDefinition returnType_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> returnTypeBuilder_;
            private List<AttributeDefinition> attributes_;
            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> attributesBuilder_;
            private Object fullName_;
            private TypeDefinition declaringType_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> declaringTypeBuilder_;
            private int peToken_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_MethodDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_MethodDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(MethodDefinition.class, Builder.class);
            }

            private Builder() {
                this.accessibility_ = 0;
                this.name_ = "";
                this.parameter_ = Collections.emptyList();
                this.attributes_ = Collections.emptyList();
                this.fullName_ = "";
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.accessibility_ = 0;
                this.name_ = "";
                this.parameter_ = Collections.emptyList();
                this.attributes_ = Collections.emptyList();
                this.fullName_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (MethodDefinition.alwaysUseFieldBuilders) {
                    getParameterFieldBuilder();
                    getAttributesFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.accessibility_ = 0;
                this.name_ = "";
                this.hasBody_ = false;
                if (this.parameterBuilder_ == null) {
                    this.parameter_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.parameterBuilder_.clear();
                }
                this.isAbstract_ = false;
                this.isAccessor_ = false;
                this.isConstructor_ = false;
                this.isDestructor_ = false;
                this.isExplicitInterfaceImplementation_ = false;
                this.isStatic_ = false;
                this.isVirtual_ = false;
                this.isOperator_ = false;
                this.isExtern_ = false;
                this.isUnsafe_ = false;
                this.isSealed_ = false;
                if (this.returnTypeBuilder_ == null) {
                    this.returnType_ = null;
                } else {
                    this.returnType_ = null;
                    this.returnTypeBuilder_ = null;
                }
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.attributesBuilder_.clear();
                }
                this.fullName_ = "";
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = null;
                } else {
                    this.declaringType_ = null;
                    this.declaringTypeBuilder_ = null;
                }
                this.peToken_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_MethodDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public MethodDefinition getDefaultInstanceForType() {
                return MethodDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public MethodDefinition build() {
                MethodDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public MethodDefinition buildPartial() {
                MethodDefinition result = new MethodDefinition(this, (MethodDefinition) null);
                int i = this.bitField0_;
                result.accessibility_ = this.accessibility_;
                result.name_ = this.name_;
                result.hasBody_ = this.hasBody_;
                if (this.parameterBuilder_ != null) {
                    result.parameter_ = this.parameterBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.parameter_ = Collections.unmodifiableList(this.parameter_);
                        this.bitField0_ &= -2;
                    }
                    result.parameter_ = this.parameter_;
                }
                result.isAbstract_ = this.isAbstract_;
                result.isAccessor_ = this.isAccessor_;
                result.isConstructor_ = this.isConstructor_;
                result.isDestructor_ = this.isDestructor_;
                result.isExplicitInterfaceImplementation_ = this.isExplicitInterfaceImplementation_;
                result.isStatic_ = this.isStatic_;
                result.isVirtual_ = this.isVirtual_;
                result.isOperator_ = this.isOperator_;
                result.isExtern_ = this.isExtern_;
                result.isUnsafe_ = this.isUnsafe_;
                result.isSealed_ = this.isSealed_;
                if (this.returnTypeBuilder_ == null) {
                    result.returnType_ = this.returnType_;
                } else {
                    result.returnType_ = this.returnTypeBuilder_.build();
                }
                if (this.attributesBuilder_ != null) {
                    result.attributes_ = this.attributesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 2) != 0) {
                        this.attributes_ = Collections.unmodifiableList(this.attributes_);
                        this.bitField0_ &= -3;
                    }
                    result.attributes_ = this.attributes_;
                }
                result.fullName_ = this.fullName_;
                if (this.declaringTypeBuilder_ == null) {
                    result.declaringType_ = this.declaringType_;
                } else {
                    result.declaringType_ = this.declaringTypeBuilder_.build();
                }
                result.peToken_ = this.peToken_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof MethodDefinition) {
                    return mergeFrom((MethodDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(MethodDefinition other) {
                if (other != MethodDefinition.getDefaultInstance()) {
                    if (other.accessibility_ != 0) {
                        setAccessibilityValue(other.getAccessibilityValue());
                    }
                    if (!other.getName().isEmpty()) {
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (other.getHasBody()) {
                        setHasBody(other.getHasBody());
                    }
                    if (this.parameterBuilder_ == null) {
                        if (!other.parameter_.isEmpty()) {
                            if (this.parameter_.isEmpty()) {
                                this.parameter_ = other.parameter_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureParameterIsMutable();
                                this.parameter_.addAll(other.parameter_);
                            }
                            onChanged();
                        }
                    } else if (!other.parameter_.isEmpty()) {
                        if (!this.parameterBuilder_.isEmpty()) {
                            this.parameterBuilder_.addAllMessages(other.parameter_);
                        } else {
                            this.parameterBuilder_.dispose();
                            this.parameterBuilder_ = null;
                            this.parameter_ = other.parameter_;
                            this.bitField0_ &= -2;
                            this.parameterBuilder_ = MethodDefinition.alwaysUseFieldBuilders ? getParameterFieldBuilder() : null;
                        }
                    }
                    if (other.getIsAbstract()) {
                        setIsAbstract(other.getIsAbstract());
                    }
                    if (other.getIsAccessor()) {
                        setIsAccessor(other.getIsAccessor());
                    }
                    if (other.getIsConstructor()) {
                        setIsConstructor(other.getIsConstructor());
                    }
                    if (other.getIsDestructor()) {
                        setIsDestructor(other.getIsDestructor());
                    }
                    if (other.getIsExplicitInterfaceImplementation()) {
                        setIsExplicitInterfaceImplementation(other.getIsExplicitInterfaceImplementation());
                    }
                    if (other.getIsStatic()) {
                        setIsStatic(other.getIsStatic());
                    }
                    if (other.getIsVirtual()) {
                        setIsVirtual(other.getIsVirtual());
                    }
                    if (other.getIsOperator()) {
                        setIsOperator(other.getIsOperator());
                    }
                    if (other.getIsExtern()) {
                        setIsExtern(other.getIsExtern());
                    }
                    if (other.getIsUnsafe()) {
                        setIsUnsafe(other.getIsUnsafe());
                    }
                    if (other.getIsSealed()) {
                        setIsSealed(other.getIsSealed());
                    }
                    if (other.hasReturnType()) {
                        mergeReturnType(other.getReturnType());
                    }
                    if (this.attributesBuilder_ == null) {
                        if (!other.attributes_.isEmpty()) {
                            if (this.attributes_.isEmpty()) {
                                this.attributes_ = other.attributes_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureAttributesIsMutable();
                                this.attributes_.addAll(other.attributes_);
                            }
                            onChanged();
                        }
                    } else if (!other.attributes_.isEmpty()) {
                        if (!this.attributesBuilder_.isEmpty()) {
                            this.attributesBuilder_.addAllMessages(other.attributes_);
                        } else {
                            this.attributesBuilder_.dispose();
                            this.attributesBuilder_ = null;
                            this.attributes_ = other.attributes_;
                            this.bitField0_ &= -3;
                            this.attributesBuilder_ = MethodDefinition.alwaysUseFieldBuilders ? getAttributesFieldBuilder() : null;
                        }
                    }
                    if (!other.getFullName().isEmpty()) {
                        this.fullName_ = other.fullName_;
                        onChanged();
                    }
                    if (other.hasDeclaringType()) {
                        mergeDeclaringType(other.getDeclaringType());
                    }
                    if (other.getPeToken() != 0) {
                        setPeToken(other.getPeToken());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                    return this;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                MethodDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (MethodDefinition) MethodDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        MethodDefinition methodDefinition = (MethodDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public int getAccessibilityValue() {
                return this.accessibility_;
            }

            public Builder setAccessibilityValue(int value) {
                this.accessibility_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public Accessibility getAccessibility() {
                Accessibility result = Accessibility.valueOf(this.accessibility_);
                return result == null ? Accessibility.UNRECOGNIZED : result;
            }

            public Builder setAccessibility(Accessibility value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.accessibility_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAccessibility() {
                this.accessibility_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
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

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
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
                this.name_ = MethodDefinition.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    MethodDefinition.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getHasBody() {
                return this.hasBody_;
            }

            public Builder setHasBody(boolean value) {
                this.hasBody_ = value;
                onChanged();
                return this;
            }

            public Builder clearHasBody() {
                this.hasBody_ = false;
                onChanged();
                return this;
            }

            private void ensureParameterIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.parameter_ = new ArrayList(this.parameter_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public List<ParameterDefinition> getParameterList() {
                if (this.parameterBuilder_ == null) {
                    return Collections.unmodifiableList(this.parameter_);
                }
                return this.parameterBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public int getParameterCount() {
                if (this.parameterBuilder_ == null) {
                    return this.parameter_.size();
                }
                return this.parameterBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public ParameterDefinition getParameter(int index) {
                if (this.parameterBuilder_ == null) {
                    return this.parameter_.get(index);
                }
                return this.parameterBuilder_.getMessage(index);
            }

            public Builder setParameter(int index, ParameterDefinition value) {
                if (this.parameterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureParameterIsMutable();
                    this.parameter_.set(index, value);
                    onChanged();
                } else {
                    this.parameterBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setParameter(int index, ParameterDefinition.Builder builderForValue) {
                if (this.parameterBuilder_ == null) {
                    ensureParameterIsMutable();
                    this.parameter_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.parameterBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addParameter(ParameterDefinition value) {
                if (this.parameterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureParameterIsMutable();
                    this.parameter_.add(value);
                    onChanged();
                } else {
                    this.parameterBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addParameter(int index, ParameterDefinition value) {
                if (this.parameterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureParameterIsMutable();
                    this.parameter_.add(index, value);
                    onChanged();
                } else {
                    this.parameterBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addParameter(ParameterDefinition.Builder builderForValue) {
                if (this.parameterBuilder_ == null) {
                    ensureParameterIsMutable();
                    this.parameter_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.parameterBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addParameter(int index, ParameterDefinition.Builder builderForValue) {
                if (this.parameterBuilder_ == null) {
                    ensureParameterIsMutable();
                    this.parameter_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.parameterBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllParameter(Iterable<? extends ParameterDefinition> values) {
                if (this.parameterBuilder_ == null) {
                    ensureParameterIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.parameter_);
                    onChanged();
                } else {
                    this.parameterBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearParameter() {
                if (this.parameterBuilder_ == null) {
                    this.parameter_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.parameterBuilder_.clear();
                }
                return this;
            }

            public Builder removeParameter(int index) {
                if (this.parameterBuilder_ == null) {
                    ensureParameterIsMutable();
                    this.parameter_.remove(index);
                    onChanged();
                } else {
                    this.parameterBuilder_.remove(index);
                }
                return this;
            }

            public ParameterDefinition.Builder getParameterBuilder(int index) {
                return getParameterFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public ParameterDefinitionOrBuilder getParameterOrBuilder(int index) {
                if (this.parameterBuilder_ == null) {
                    return this.parameter_.get(index);
                }
                return this.parameterBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public List<? extends ParameterDefinitionOrBuilder> getParameterOrBuilderList() {
                if (this.parameterBuilder_ != null) {
                    return this.parameterBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.parameter_);
            }

            public ParameterDefinition.Builder addParameterBuilder() {
                return getParameterFieldBuilder().addBuilder(ParameterDefinition.getDefaultInstance());
            }

            public ParameterDefinition.Builder addParameterBuilder(int index) {
                return getParameterFieldBuilder().addBuilder(index, ParameterDefinition.getDefaultInstance());
            }

            public List<ParameterDefinition.Builder> getParameterBuilderList() {
                return getParameterFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<ParameterDefinition, ParameterDefinition.Builder, ParameterDefinitionOrBuilder> getParameterFieldBuilder() {
                if (this.parameterBuilder_ == null) {
                    this.parameterBuilder_ = new RepeatedFieldBuilderV3<>(this.parameter_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.parameter_ = null;
                }
                return this.parameterBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsAbstract() {
                return this.isAbstract_;
            }

            public Builder setIsAbstract(boolean value) {
                this.isAbstract_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsAbstract() {
                this.isAbstract_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsAccessor() {
                return this.isAccessor_;
            }

            public Builder setIsAccessor(boolean value) {
                this.isAccessor_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsAccessor() {
                this.isAccessor_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsConstructor() {
                return this.isConstructor_;
            }

            public Builder setIsConstructor(boolean value) {
                this.isConstructor_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsConstructor() {
                this.isConstructor_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsDestructor() {
                return this.isDestructor_;
            }

            public Builder setIsDestructor(boolean value) {
                this.isDestructor_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsDestructor() {
                this.isDestructor_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsExplicitInterfaceImplementation() {
                return this.isExplicitInterfaceImplementation_;
            }

            public Builder setIsExplicitInterfaceImplementation(boolean value) {
                this.isExplicitInterfaceImplementation_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsExplicitInterfaceImplementation() {
                this.isExplicitInterfaceImplementation_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsStatic() {
                return this.isStatic_;
            }

            public Builder setIsStatic(boolean value) {
                this.isStatic_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsStatic() {
                this.isStatic_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsVirtual() {
                return this.isVirtual_;
            }

            public Builder setIsVirtual(boolean value) {
                this.isVirtual_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsVirtual() {
                this.isVirtual_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsOperator() {
                return this.isOperator_;
            }

            public Builder setIsOperator(boolean value) {
                this.isOperator_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsOperator() {
                this.isOperator_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsExtern() {
                return this.isExtern_;
            }

            public Builder setIsExtern(boolean value) {
                this.isExtern_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsExtern() {
                this.isExtern_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsUnsafe() {
                return this.isUnsafe_;
            }

            public Builder setIsUnsafe(boolean value) {
                this.isUnsafe_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsUnsafe() {
                this.isUnsafe_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean getIsSealed() {
                return this.isSealed_;
            }

            public Builder setIsSealed(boolean value) {
                this.isSealed_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsSealed() {
                this.isSealed_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean hasReturnType() {
                return (this.returnTypeBuilder_ == null && this.returnType_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public TypeDefinition getReturnType() {
                if (this.returnTypeBuilder_ == null) {
                    return this.returnType_ == null ? TypeDefinition.getDefaultInstance() : this.returnType_;
                }
                return this.returnTypeBuilder_.getMessage();
            }

            public Builder setReturnType(TypeDefinition value) {
                if (this.returnTypeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.returnType_ = value;
                    onChanged();
                } else {
                    this.returnTypeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setReturnType(TypeDefinition.Builder builderForValue) {
                if (this.returnTypeBuilder_ == null) {
                    this.returnType_ = builderForValue.build();
                    onChanged();
                } else {
                    this.returnTypeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeReturnType(TypeDefinition value) {
                if (this.returnTypeBuilder_ == null) {
                    if (this.returnType_ != null) {
                        this.returnType_ = TypeDefinition.newBuilder(this.returnType_).mergeFrom(value).buildPartial();
                    } else {
                        this.returnType_ = value;
                    }
                    onChanged();
                } else {
                    this.returnTypeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearReturnType() {
                if (this.returnTypeBuilder_ == null) {
                    this.returnType_ = null;
                    onChanged();
                } else {
                    this.returnType_ = null;
                    this.returnTypeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getReturnTypeBuilder() {
                onChanged();
                return getReturnTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public TypeDefinitionOrBuilder getReturnTypeOrBuilder() {
                if (this.returnTypeBuilder_ != null) {
                    return this.returnTypeBuilder_.getMessageOrBuilder();
                }
                return this.returnType_ == null ? TypeDefinition.getDefaultInstance() : this.returnType_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getReturnTypeFieldBuilder() {
                if (this.returnTypeBuilder_ == null) {
                    this.returnTypeBuilder_ = new SingleFieldBuilderV3<>(getReturnType(), getParentForChildren(), isClean());
                    this.returnType_ = null;
                }
                return this.returnTypeBuilder_;
            }

            private void ensureAttributesIsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.attributes_ = new ArrayList(this.attributes_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public List<AttributeDefinition> getAttributesList() {
                if (this.attributesBuilder_ == null) {
                    return Collections.unmodifiableList(this.attributes_);
                }
                return this.attributesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public int getAttributesCount() {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.size();
                }
                return this.attributesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public AttributeDefinition getAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessage(index);
            }

            public Builder setAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllAttributes(Iterable<? extends AttributeDefinition> values) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.attributes_);
                    onChanged();
                } else {
                    this.attributesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearAttributes() {
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.attributesBuilder_.clear();
                }
                return this;
            }

            public Builder removeAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.remove(index);
                    onChanged();
                } else {
                    this.attributesBuilder_.remove(index);
                }
                return this;
            }

            public AttributeDefinition.Builder getAttributesBuilder(int index) {
                return getAttributesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
                if (this.attributesBuilder_ != null) {
                    return this.attributesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.attributes_);
            }

            public AttributeDefinition.Builder addAttributesBuilder() {
                return getAttributesFieldBuilder().addBuilder(AttributeDefinition.getDefaultInstance());
            }

            public AttributeDefinition.Builder addAttributesBuilder(int index) {
                return getAttributesFieldBuilder().addBuilder(index, AttributeDefinition.getDefaultInstance());
            }

            public List<AttributeDefinition.Builder> getAttributesBuilderList() {
                return getAttributesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> getAttributesFieldBuilder() {
                if (this.attributesBuilder_ == null) {
                    this.attributesBuilder_ = new RepeatedFieldBuilderV3<>(this.attributes_, (this.bitField0_ & 2) != 0, getParentForChildren(), isClean());
                    this.attributes_ = null;
                }
                return this.attributesBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public String getFullName() {
                Object ref = this.fullName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.fullName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public ByteString getFullNameBytes() {
                Object ref = this.fullName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.fullName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setFullName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.fullName_ = value;
                onChanged();
                return this;
            }

            public Builder clearFullName() {
                this.fullName_ = MethodDefinition.getDefaultInstance().getFullName();
                onChanged();
                return this;
            }

            public Builder setFullNameBytes(ByteString value) {
                if (value != null) {
                    MethodDefinition.checkByteStringIsUtf8(value);
                    this.fullName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public boolean hasDeclaringType() {
                return (this.declaringTypeBuilder_ == null && this.declaringType_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public TypeDefinition getDeclaringType() {
                if (this.declaringTypeBuilder_ == null) {
                    return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
                }
                return this.declaringTypeBuilder_.getMessage();
            }

            public Builder setDeclaringType(TypeDefinition value) {
                if (this.declaringTypeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.declaringType_ = value;
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setDeclaringType(TypeDefinition.Builder builderForValue) {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = builderForValue.build();
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeDeclaringType(TypeDefinition value) {
                if (this.declaringTypeBuilder_ == null) {
                    if (this.declaringType_ != null) {
                        this.declaringType_ = TypeDefinition.newBuilder(this.declaringType_).mergeFrom(value).buildPartial();
                    } else {
                        this.declaringType_ = value;
                    }
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearDeclaringType() {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = null;
                    onChanged();
                } else {
                    this.declaringType_ = null;
                    this.declaringTypeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getDeclaringTypeBuilder() {
                onChanged();
                return getDeclaringTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public TypeDefinitionOrBuilder getDeclaringTypeOrBuilder() {
                if (this.declaringTypeBuilder_ != null) {
                    return this.declaringTypeBuilder_.getMessageOrBuilder();
                }
                return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getDeclaringTypeFieldBuilder() {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringTypeBuilder_ = new SingleFieldBuilderV3<>(getDeclaringType(), getParentForChildren(), isClean());
                    this.declaringType_ = null;
                }
                return this.declaringTypeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.MethodDefinitionOrBuilder
            public int getPeToken() {
                return this.peToken_;
            }

            public Builder setPeToken(int value) {
                this.peToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearPeToken() {
                this.peToken_ = 0;
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

        public static MethodDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<MethodDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<MethodDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public MethodDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$ParameterDefinition.class */
    public static final class ParameterDefinition extends GeneratedMessageV3 implements ParameterDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int TYPE_FIELD_NUMBER = 1;
        private TypeDefinition type_;
        public static final int PARAMETER_NAME_FIELD_NUMBER = 2;
        private volatile Object parameterName_;
        public static final int IS_REF_FIELD_NUMBER = 3;
        private boolean isRef_;
        public static final int IS_OUT_FIELD_NUMBER = 4;
        private boolean isOut_;
        public static final int IS_IN_FIELD_NUMBER = 5;
        private boolean isIn_;
        public static final int IS_OPTIONAL_FIELD_NUMBER = 6;
        private boolean isOptional_;
        private byte memoizedIsInitialized;
        private static final ParameterDefinition DEFAULT_INSTANCE = new ParameterDefinition();
        private static final Parser<ParameterDefinition> PARSER = new AbstractParser<ParameterDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinition.1
            @Override // com.google.protobuf.Parser
            public ParameterDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new ParameterDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ ParameterDefinition(GeneratedMessageV3.Builder builder, ParameterDefinition parameterDefinition) {
            this(builder);
        }

        private ParameterDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private ParameterDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.parameterName_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new ParameterDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ ParameterDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, ParameterDefinition parameterDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private ParameterDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        try {
                            int tag = input.readTag();
                            switch (tag) {
                                case 0:
                                    done = true;
                                    break;
                                case 10:
                                    TypeDefinition.Builder subBuilder = null;
                                    subBuilder = this.type_ != null ? this.type_.toBuilder() : subBuilder;
                                    this.type_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                    if (subBuilder == null) {
                                        break;
                                    } else {
                                        subBuilder.mergeFrom(this.type_);
                                        this.type_ = subBuilder.buildPartial();
                                        break;
                                    }
                                case 18:
                                    String s = input.readStringRequireUtf8();
                                    this.parameterName_ = s;
                                    break;
                                case 24:
                                    this.isRef_ = input.readBool();
                                    break;
                                case 32:
                                    this.isOut_ = input.readBool();
                                    break;
                                case 40:
                                    this.isIn_ = input.readBool();
                                    break;
                                case 48:
                                    this.isOptional_ = input.readBool();
                                    break;
                                default:
                                    if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                        done = true;
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } catch (IOException e) {
                            throw new InvalidProtocolBufferException(e).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                }
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_ParameterDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_ParameterDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(ParameterDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public TypeDefinition getType() {
            return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public String getParameterName() {
            Object ref = this.parameterName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.parameterName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public ByteString getParameterNameBytes() {
            Object ref = this.parameterName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.parameterName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public boolean getIsRef() {
            return this.isRef_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public boolean getIsOut() {
            return this.isOut_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public boolean getIsIn() {
            return this.isIn_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
        public boolean getIsOptional() {
            return this.isOptional_;
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
            if (this.type_ != null) {
                output.writeMessage(1, getType());
            }
            if (!getParameterNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.parameterName_);
            }
            if (this.isRef_) {
                output.writeBool(3, this.isRef_);
            }
            if (this.isOut_) {
                output.writeBool(4, this.isOut_);
            }
            if (this.isIn_) {
                output.writeBool(5, this.isIn_);
            }
            if (this.isOptional_) {
                output.writeBool(6, this.isOptional_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.type_ != null) {
                size2 = 0 + CodedOutputStream.computeMessageSize(1, getType());
            }
            if (!getParameterNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.parameterName_);
            }
            if (this.isRef_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.isRef_);
            }
            if (this.isOut_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.isOut_);
            }
            if (this.isIn_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.isIn_);
            }
            if (this.isOptional_) {
                size2 += CodedOutputStream.computeBoolSize(6, this.isOptional_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof ParameterDefinition)) {
                return super.equals(obj);
            }
            ParameterDefinition other = (ParameterDefinition) obj;
            if (hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || !getParameterName().equals(other.getParameterName()) || getIsRef() != other.getIsRef() || getIsOut() != other.getIsOut() || getIsIn() != other.getIsIn() || getIsOptional() != other.getIsOptional() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            if (hasType()) {
                hash = (53 * ((37 * hash) + 1)) + getType().hashCode();
            }
            int hash2 = (29 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 2)) + getParameterName().hashCode())) + 3)) + Internal.hashBoolean(getIsRef()))) + 4)) + Internal.hashBoolean(getIsOut()))) + 5)) + Internal.hashBoolean(getIsIn()))) + 6)) + Internal.hashBoolean(getIsOptional()))) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static ParameterDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ParameterDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ParameterDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ParameterDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ParameterDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static ParameterDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static ParameterDefinition parseFrom(InputStream input) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static ParameterDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static ParameterDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static ParameterDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static ParameterDefinition parseFrom(CodedInputStream input) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static ParameterDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (ParameterDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(ParameterDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$ParameterDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements ParameterDefinitionOrBuilder {
            private TypeDefinition type_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> typeBuilder_;
            private Object parameterName_;
            private boolean isRef_;
            private boolean isOut_;
            private boolean isIn_;
            private boolean isOptional_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_ParameterDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_ParameterDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(ParameterDefinition.class, Builder.class);
            }

            private Builder() {
                this.parameterName_ = "";
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.parameterName_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = ParameterDefinition.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                this.parameterName_ = "";
                this.isRef_ = false;
                this.isOut_ = false;
                this.isIn_ = false;
                this.isOptional_ = false;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_ParameterDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public ParameterDefinition getDefaultInstanceForType() {
                return ParameterDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public ParameterDefinition build() {
                ParameterDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public ParameterDefinition buildPartial() {
                ParameterDefinition result = new ParameterDefinition(this, (ParameterDefinition) null);
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                result.parameterName_ = this.parameterName_;
                result.isRef_ = this.isRef_;
                result.isOut_ = this.isOut_;
                result.isIn_ = this.isIn_;
                result.isOptional_ = this.isOptional_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof ParameterDefinition) {
                    return mergeFrom((ParameterDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(ParameterDefinition other) {
                if (other == ParameterDefinition.getDefaultInstance()) {
                    return this;
                }
                if (other.hasType()) {
                    mergeType(other.getType());
                }
                if (!other.getParameterName().isEmpty()) {
                    this.parameterName_ = other.parameterName_;
                    onChanged();
                }
                if (other.getIsRef()) {
                    setIsRef(other.getIsRef());
                }
                if (other.getIsOut()) {
                    setIsOut(other.getIsOut());
                }
                if (other.getIsIn()) {
                    setIsIn(other.getIsIn());
                }
                if (other.getIsOptional()) {
                    setIsOptional(other.getIsOptional());
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                ParameterDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (ParameterDefinition) ParameterDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        ParameterDefinition parameterDefinition = (ParameterDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.type_ = value;
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setType(TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
                    } else {
                        this.type_ = value;
                    }
                    onChanged();
                } else {
                    this.typeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearType() {
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                    onChanged();
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public String getParameterName() {
                Object ref = this.parameterName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.parameterName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public ByteString getParameterNameBytes() {
                Object ref = this.parameterName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.parameterName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setParameterName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.parameterName_ = value;
                onChanged();
                return this;
            }

            public Builder clearParameterName() {
                this.parameterName_ = ParameterDefinition.getDefaultInstance().getParameterName();
                onChanged();
                return this;
            }

            public Builder setParameterNameBytes(ByteString value) {
                if (value != null) {
                    ParameterDefinition.checkByteStringIsUtf8(value);
                    this.parameterName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public boolean getIsRef() {
                return this.isRef_;
            }

            public Builder setIsRef(boolean value) {
                this.isRef_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsRef() {
                this.isRef_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public boolean getIsOut() {
                return this.isOut_;
            }

            public Builder setIsOut(boolean value) {
                this.isOut_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsOut() {
                this.isOut_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public boolean getIsIn() {
                return this.isIn_;
            }

            public Builder setIsIn(boolean value) {
                this.isIn_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsIn() {
                this.isIn_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.ParameterDefinitionOrBuilder
            public boolean getIsOptional() {
                return this.isOptional_;
            }

            public Builder setIsOptional(boolean value) {
                this.isOptional_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsOptional() {
                this.isOptional_ = false;
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

        public static ParameterDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<ParameterDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<ParameterDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public ParameterDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$FieldDefinition.class */
    public static final class FieldDefinition extends GeneratedMessageV3 implements FieldDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ACCESSIBILITY_FIELD_NUMBER = 1;
        private int accessibility_;
        public static final int IS_ABSTRACT_FIELD_NUMBER = 2;
        private boolean isAbstract_;
        public static final int IS_SEALED_FIELD_NUMBER = 3;
        private boolean isSealed_;
        public static final int IS_EXPLICIT_INTERFACE_IMPLEMENTATION_FIELD_NUMBER = 4;
        private boolean isExplicitInterfaceImplementation_;
        public static final int IS_OVERRIDE_FIELD_NUMBER = 5;
        private boolean isOverride_;
        public static final int IS_VIRTUAL_FIELD_NUMBER = 6;
        private boolean isVirtual_;
        public static final int IS_CONST_FIELD_NUMBER = 7;
        private boolean isConst_;
        public static final int IS_READ_ONLY_FIELD_NUMBER = 8;
        private boolean isReadOnly_;
        public static final int IS_STATIC_FIELD_NUMBER = 9;
        private boolean isStatic_;
        public static final int TYPE_FIELD_NUMBER = 10;
        private TypeDefinition type_;
        public static final int TYPE_KIND_FIELD_NUMBER = 14;
        private int typeKind_;
        public static final int NAME_FIELD_NUMBER = 11;
        private volatile Object name_;
        public static final int FULL_NAME_FIELD_NUMBER = 12;
        private volatile Object fullName_;
        public static final int DECLARING_TYPE_FIELD_NUMBER = 13;
        private TypeDefinition declaringType_;
        public static final int ATTRIBUTES_FIELD_NUMBER = 15;
        private List<AttributeDefinition> attributes_;
        public static final int PE_TOKEN_FIELD_NUMBER = 16;
        private int peToken_;
        private byte memoizedIsInitialized;
        private static final FieldDefinition DEFAULT_INSTANCE = new FieldDefinition();
        private static final Parser<FieldDefinition> PARSER = new AbstractParser<FieldDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinition.1
            @Override // com.google.protobuf.Parser
            public FieldDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new FieldDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ FieldDefinition(GeneratedMessageV3.Builder builder, FieldDefinition fieldDefinition) {
            this(builder);
        }

        private FieldDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private FieldDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.accessibility_ = 0;
            this.typeKind_ = 0;
            this.name_ = "";
            this.fullName_ = "";
            this.attributes_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new FieldDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ FieldDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, FieldDefinition fieldDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private FieldDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                this.accessibility_ = rawValue;
                                break;
                            case 16:
                                this.isAbstract_ = input.readBool();
                                break;
                            case 24:
                                this.isSealed_ = input.readBool();
                                break;
                            case 32:
                                this.isExplicitInterfaceImplementation_ = input.readBool();
                                break;
                            case 40:
                                this.isOverride_ = input.readBool();
                                break;
                            case 48:
                                this.isVirtual_ = input.readBool();
                                break;
                            case 56:
                                this.isConst_ = input.readBool();
                                break;
                            case 64:
                                this.isReadOnly_ = input.readBool();
                                break;
                            case 72:
                                this.isStatic_ = input.readBool();
                                break;
                            case 82:
                                TypeDefinition.Builder subBuilder = null;
                                subBuilder = this.type_ != null ? this.type_.toBuilder() : subBuilder;
                                this.type_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.type_);
                                    this.type_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 90:
                                String s = input.readStringRequireUtf8();
                                this.name_ = s;
                                break;
                            case 98:
                                String s2 = input.readStringRequireUtf8();
                                this.fullName_ = s2;
                                break;
                            case 106:
                                TypeDefinition.Builder subBuilder2 = null;
                                subBuilder2 = this.declaringType_ != null ? this.declaringType_.toBuilder() : subBuilder2;
                                this.declaringType_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.declaringType_);
                                    this.declaringType_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 112:
                                int rawValue2 = input.readEnum();
                                this.typeKind_ = rawValue2;
                                break;
                            case 122:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.attributes_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.attributes_.add((AttributeDefinition) input.readMessage(AttributeDefinition.parser(), extensionRegistry));
                                break;
                            case 128:
                                this.peToken_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_FieldDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_FieldDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public int getAccessibilityValue() {
            return this.accessibility_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public Accessibility getAccessibility() {
            Accessibility result = Accessibility.valueOf(this.accessibility_);
            return result == null ? Accessibility.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsAbstract() {
            return this.isAbstract_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsSealed() {
            return this.isSealed_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsExplicitInterfaceImplementation() {
            return this.isExplicitInterfaceImplementation_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsOverride() {
            return this.isOverride_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsVirtual() {
            return this.isVirtual_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsConst() {
            return this.isConst_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsReadOnly() {
            return this.isReadOnly_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean getIsStatic() {
            return this.isStatic_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public TypeDefinition getType() {
            return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public int getTypeKindValue() {
            return this.typeKind_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public TypeKindDef getTypeKind() {
            TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
            return result == null ? TypeKindDef.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
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

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public String getFullName() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.fullName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public ByteString getFullNameBytes() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.fullName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public boolean hasDeclaringType() {
            return this.declaringType_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public TypeDefinition getDeclaringType() {
            return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public TypeDefinitionOrBuilder getDeclaringTypeOrBuilder() {
            return getDeclaringType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public List<AttributeDefinition> getAttributesList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public int getAttributesCount() {
            return this.attributes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public AttributeDefinition getAttributes(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
        public int getPeToken() {
            return this.peToken_;
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
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                output.writeEnum(1, this.accessibility_);
            }
            if (this.isAbstract_) {
                output.writeBool(2, this.isAbstract_);
            }
            if (this.isSealed_) {
                output.writeBool(3, this.isSealed_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                output.writeBool(4, this.isExplicitInterfaceImplementation_);
            }
            if (this.isOverride_) {
                output.writeBool(5, this.isOverride_);
            }
            if (this.isVirtual_) {
                output.writeBool(6, this.isVirtual_);
            }
            if (this.isConst_) {
                output.writeBool(7, this.isConst_);
            }
            if (this.isReadOnly_) {
                output.writeBool(8, this.isReadOnly_);
            }
            if (this.isStatic_) {
                output.writeBool(9, this.isStatic_);
            }
            if (this.type_ != null) {
                output.writeMessage(10, getType());
            }
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 11, this.name_);
            }
            if (!getFullNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 12, this.fullName_);
            }
            if (this.declaringType_ != null) {
                output.writeMessage(13, getDeclaringType());
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                output.writeEnum(14, this.typeKind_);
            }
            for (int i = 0; i < this.attributes_.size(); i++) {
                output.writeMessage(15, this.attributes_.get(i));
            }
            if (this.peToken_ != 0) {
                output.writeInt32(16, this.peToken_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.accessibility_);
            }
            if (this.isAbstract_) {
                size2 += CodedOutputStream.computeBoolSize(2, this.isAbstract_);
            }
            if (this.isSealed_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.isSealed_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.isExplicitInterfaceImplementation_);
            }
            if (this.isOverride_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.isOverride_);
            }
            if (this.isVirtual_) {
                size2 += CodedOutputStream.computeBoolSize(6, this.isVirtual_);
            }
            if (this.isConst_) {
                size2 += CodedOutputStream.computeBoolSize(7, this.isConst_);
            }
            if (this.isReadOnly_) {
                size2 += CodedOutputStream.computeBoolSize(8, this.isReadOnly_);
            }
            if (this.isStatic_) {
                size2 += CodedOutputStream.computeBoolSize(9, this.isStatic_);
            }
            if (this.type_ != null) {
                size2 += CodedOutputStream.computeMessageSize(10, getType());
            }
            if (!getNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(11, this.name_);
            }
            if (!getFullNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(12, this.fullName_);
            }
            if (this.declaringType_ != null) {
                size2 += CodedOutputStream.computeMessageSize(13, getDeclaringType());
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(14, this.typeKind_);
            }
            for (int i = 0; i < this.attributes_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(15, this.attributes_.get(i));
            }
            if (this.peToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(16, this.peToken_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof FieldDefinition)) {
                return super.equals(obj);
            }
            FieldDefinition other = (FieldDefinition) obj;
            if (this.accessibility_ != other.accessibility_ || getIsAbstract() != other.getIsAbstract() || getIsSealed() != other.getIsSealed() || getIsExplicitInterfaceImplementation() != other.getIsExplicitInterfaceImplementation() || getIsOverride() != other.getIsOverride() || getIsVirtual() != other.getIsVirtual() || getIsConst() != other.getIsConst() || getIsReadOnly() != other.getIsReadOnly() || getIsStatic() != other.getIsStatic() || hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || this.typeKind_ != other.typeKind_ || !getName().equals(other.getName()) || !getFullName().equals(other.getFullName()) || hasDeclaringType() != other.hasDeclaringType()) {
                return false;
            }
            if ((hasDeclaringType() && !getDeclaringType().equals(other.getDeclaringType())) || !getAttributesList().equals(other.getAttributesList()) || getPeToken() != other.getPeToken() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            int hash2 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 1)) + this.accessibility_)) + 2)) + Internal.hashBoolean(getIsAbstract()))) + 3)) + Internal.hashBoolean(getIsSealed()))) + 4)) + Internal.hashBoolean(getIsExplicitInterfaceImplementation()))) + 5)) + Internal.hashBoolean(getIsOverride()))) + 6)) + Internal.hashBoolean(getIsVirtual()))) + 7)) + Internal.hashBoolean(getIsConst()))) + 8)) + Internal.hashBoolean(getIsReadOnly()))) + 9)) + Internal.hashBoolean(getIsStatic());
            if (hasType()) {
                hash2 = (53 * ((37 * hash2) + 10)) + getType().hashCode();
            }
            int hash3 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash2) + 14)) + this.typeKind_)) + 11)) + getName().hashCode())) + 12)) + getFullName().hashCode();
            if (hasDeclaringType()) {
                hash3 = (53 * ((37 * hash3) + 13)) + getDeclaringType().hashCode();
            }
            if (getAttributesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 15)) + getAttributesList().hashCode();
            }
            int hash4 = (29 * ((53 * ((37 * hash3) + 16)) + getPeToken())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash4;
            return hash4;
        }

        public static FieldDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FieldDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FieldDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FieldDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FieldDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static FieldDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static FieldDefinition parseFrom(InputStream input) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static FieldDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static FieldDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static FieldDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static FieldDefinition parseFrom(CodedInputStream input) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static FieldDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (FieldDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(FieldDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$FieldDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements FieldDefinitionOrBuilder {
            private int bitField0_;
            private int accessibility_;
            private boolean isAbstract_;
            private boolean isSealed_;
            private boolean isExplicitInterfaceImplementation_;
            private boolean isOverride_;
            private boolean isVirtual_;
            private boolean isConst_;
            private boolean isReadOnly_;
            private boolean isStatic_;
            private TypeDefinition type_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> typeBuilder_;
            private int typeKind_;
            private Object name_;
            private Object fullName_;
            private TypeDefinition declaringType_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> declaringTypeBuilder_;
            private List<AttributeDefinition> attributes_;
            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> attributesBuilder_;
            private int peToken_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_FieldDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_FieldDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(FieldDefinition.class, Builder.class);
            }

            private Builder() {
                this.accessibility_ = 0;
                this.typeKind_ = 0;
                this.name_ = "";
                this.fullName_ = "";
                this.attributes_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.accessibility_ = 0;
                this.typeKind_ = 0;
                this.name_ = "";
                this.fullName_ = "";
                this.attributes_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (FieldDefinition.alwaysUseFieldBuilders) {
                    getAttributesFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.accessibility_ = 0;
                this.isAbstract_ = false;
                this.isSealed_ = false;
                this.isExplicitInterfaceImplementation_ = false;
                this.isOverride_ = false;
                this.isVirtual_ = false;
                this.isConst_ = false;
                this.isReadOnly_ = false;
                this.isStatic_ = false;
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                this.typeKind_ = 0;
                this.name_ = "";
                this.fullName_ = "";
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = null;
                } else {
                    this.declaringType_ = null;
                    this.declaringTypeBuilder_ = null;
                }
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.attributesBuilder_.clear();
                }
                this.peToken_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_FieldDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public FieldDefinition getDefaultInstanceForType() {
                return FieldDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FieldDefinition build() {
                FieldDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public FieldDefinition buildPartial() {
                FieldDefinition result = new FieldDefinition(this, (FieldDefinition) null);
                int i = this.bitField0_;
                result.accessibility_ = this.accessibility_;
                result.isAbstract_ = this.isAbstract_;
                result.isSealed_ = this.isSealed_;
                result.isExplicitInterfaceImplementation_ = this.isExplicitInterfaceImplementation_;
                result.isOverride_ = this.isOverride_;
                result.isVirtual_ = this.isVirtual_;
                result.isConst_ = this.isConst_;
                result.isReadOnly_ = this.isReadOnly_;
                result.isStatic_ = this.isStatic_;
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                result.typeKind_ = this.typeKind_;
                result.name_ = this.name_;
                result.fullName_ = this.fullName_;
                if (this.declaringTypeBuilder_ == null) {
                    result.declaringType_ = this.declaringType_;
                } else {
                    result.declaringType_ = this.declaringTypeBuilder_.build();
                }
                if (this.attributesBuilder_ != null) {
                    result.attributes_ = this.attributesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.attributes_ = Collections.unmodifiableList(this.attributes_);
                        this.bitField0_ &= -2;
                    }
                    result.attributes_ = this.attributes_;
                }
                result.peToken_ = this.peToken_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof FieldDefinition) {
                    return mergeFrom((FieldDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(FieldDefinition other) {
                if (other != FieldDefinition.getDefaultInstance()) {
                    if (other.accessibility_ != 0) {
                        setAccessibilityValue(other.getAccessibilityValue());
                    }
                    if (other.getIsAbstract()) {
                        setIsAbstract(other.getIsAbstract());
                    }
                    if (other.getIsSealed()) {
                        setIsSealed(other.getIsSealed());
                    }
                    if (other.getIsExplicitInterfaceImplementation()) {
                        setIsExplicitInterfaceImplementation(other.getIsExplicitInterfaceImplementation());
                    }
                    if (other.getIsOverride()) {
                        setIsOverride(other.getIsOverride());
                    }
                    if (other.getIsVirtual()) {
                        setIsVirtual(other.getIsVirtual());
                    }
                    if (other.getIsConst()) {
                        setIsConst(other.getIsConst());
                    }
                    if (other.getIsReadOnly()) {
                        setIsReadOnly(other.getIsReadOnly());
                    }
                    if (other.getIsStatic()) {
                        setIsStatic(other.getIsStatic());
                    }
                    if (other.hasType()) {
                        mergeType(other.getType());
                    }
                    if (other.typeKind_ != 0) {
                        setTypeKindValue(other.getTypeKindValue());
                    }
                    if (!other.getName().isEmpty()) {
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (!other.getFullName().isEmpty()) {
                        this.fullName_ = other.fullName_;
                        onChanged();
                    }
                    if (other.hasDeclaringType()) {
                        mergeDeclaringType(other.getDeclaringType());
                    }
                    if (this.attributesBuilder_ == null) {
                        if (!other.attributes_.isEmpty()) {
                            if (this.attributes_.isEmpty()) {
                                this.attributes_ = other.attributes_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureAttributesIsMutable();
                                this.attributes_.addAll(other.attributes_);
                            }
                            onChanged();
                        }
                    } else if (!other.attributes_.isEmpty()) {
                        if (!this.attributesBuilder_.isEmpty()) {
                            this.attributesBuilder_.addAllMessages(other.attributes_);
                        } else {
                            this.attributesBuilder_.dispose();
                            this.attributesBuilder_ = null;
                            this.attributes_ = other.attributes_;
                            this.bitField0_ &= -2;
                            this.attributesBuilder_ = FieldDefinition.alwaysUseFieldBuilders ? getAttributesFieldBuilder() : null;
                        }
                    }
                    if (other.getPeToken() != 0) {
                        setPeToken(other.getPeToken());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                    return this;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                FieldDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (FieldDefinition) FieldDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        FieldDefinition fieldDefinition = (FieldDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public int getAccessibilityValue() {
                return this.accessibility_;
            }

            public Builder setAccessibilityValue(int value) {
                this.accessibility_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public Accessibility getAccessibility() {
                Accessibility result = Accessibility.valueOf(this.accessibility_);
                return result == null ? Accessibility.UNRECOGNIZED : result;
            }

            public Builder setAccessibility(Accessibility value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.accessibility_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAccessibility() {
                this.accessibility_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsAbstract() {
                return this.isAbstract_;
            }

            public Builder setIsAbstract(boolean value) {
                this.isAbstract_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsAbstract() {
                this.isAbstract_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsSealed() {
                return this.isSealed_;
            }

            public Builder setIsSealed(boolean value) {
                this.isSealed_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsSealed() {
                this.isSealed_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsExplicitInterfaceImplementation() {
                return this.isExplicitInterfaceImplementation_;
            }

            public Builder setIsExplicitInterfaceImplementation(boolean value) {
                this.isExplicitInterfaceImplementation_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsExplicitInterfaceImplementation() {
                this.isExplicitInterfaceImplementation_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsOverride() {
                return this.isOverride_;
            }

            public Builder setIsOverride(boolean value) {
                this.isOverride_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsOverride() {
                this.isOverride_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsVirtual() {
                return this.isVirtual_;
            }

            public Builder setIsVirtual(boolean value) {
                this.isVirtual_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsVirtual() {
                this.isVirtual_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsConst() {
                return this.isConst_;
            }

            public Builder setIsConst(boolean value) {
                this.isConst_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsConst() {
                this.isConst_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsReadOnly() {
                return this.isReadOnly_;
            }

            public Builder setIsReadOnly(boolean value) {
                this.isReadOnly_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsReadOnly() {
                this.isReadOnly_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean getIsStatic() {
                return this.isStatic_;
            }

            public Builder setIsStatic(boolean value) {
                this.isStatic_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsStatic() {
                this.isStatic_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.type_ = value;
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setType(TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
                    } else {
                        this.type_ = value;
                    }
                    onChanged();
                } else {
                    this.typeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearType() {
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                    onChanged();
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public int getTypeKindValue() {
                return this.typeKind_;
            }

            public Builder setTypeKindValue(int value) {
                this.typeKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public TypeKindDef getTypeKind() {
                TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
                return result == null ? TypeKindDef.UNRECOGNIZED : result;
            }

            public Builder setTypeKind(TypeKindDef value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.typeKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearTypeKind() {
                this.typeKind_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
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

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
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
                this.name_ = FieldDefinition.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    FieldDefinition.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public String getFullName() {
                Object ref = this.fullName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.fullName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public ByteString getFullNameBytes() {
                Object ref = this.fullName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.fullName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setFullName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.fullName_ = value;
                onChanged();
                return this;
            }

            public Builder clearFullName() {
                this.fullName_ = FieldDefinition.getDefaultInstance().getFullName();
                onChanged();
                return this;
            }

            public Builder setFullNameBytes(ByteString value) {
                if (value != null) {
                    FieldDefinition.checkByteStringIsUtf8(value);
                    this.fullName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public boolean hasDeclaringType() {
                return (this.declaringTypeBuilder_ == null && this.declaringType_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public TypeDefinition getDeclaringType() {
                if (this.declaringTypeBuilder_ == null) {
                    return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
                }
                return this.declaringTypeBuilder_.getMessage();
            }

            public Builder setDeclaringType(TypeDefinition value) {
                if (this.declaringTypeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.declaringType_ = value;
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setDeclaringType(TypeDefinition.Builder builderForValue) {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = builderForValue.build();
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeDeclaringType(TypeDefinition value) {
                if (this.declaringTypeBuilder_ == null) {
                    if (this.declaringType_ != null) {
                        this.declaringType_ = TypeDefinition.newBuilder(this.declaringType_).mergeFrom(value).buildPartial();
                    } else {
                        this.declaringType_ = value;
                    }
                    onChanged();
                } else {
                    this.declaringTypeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearDeclaringType() {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringType_ = null;
                    onChanged();
                } else {
                    this.declaringType_ = null;
                    this.declaringTypeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getDeclaringTypeBuilder() {
                onChanged();
                return getDeclaringTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public TypeDefinitionOrBuilder getDeclaringTypeOrBuilder() {
                if (this.declaringTypeBuilder_ != null) {
                    return this.declaringTypeBuilder_.getMessageOrBuilder();
                }
                return this.declaringType_ == null ? TypeDefinition.getDefaultInstance() : this.declaringType_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getDeclaringTypeFieldBuilder() {
                if (this.declaringTypeBuilder_ == null) {
                    this.declaringTypeBuilder_ = new SingleFieldBuilderV3<>(getDeclaringType(), getParentForChildren(), isClean());
                    this.declaringType_ = null;
                }
                return this.declaringTypeBuilder_;
            }

            private void ensureAttributesIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.attributes_ = new ArrayList(this.attributes_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public List<AttributeDefinition> getAttributesList() {
                if (this.attributesBuilder_ == null) {
                    return Collections.unmodifiableList(this.attributes_);
                }
                return this.attributesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public int getAttributesCount() {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.size();
                }
                return this.attributesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public AttributeDefinition getAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessage(index);
            }

            public Builder setAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllAttributes(Iterable<? extends AttributeDefinition> values) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.attributes_);
                    onChanged();
                } else {
                    this.attributesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearAttributes() {
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.attributesBuilder_.clear();
                }
                return this;
            }

            public Builder removeAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.remove(index);
                    onChanged();
                } else {
                    this.attributesBuilder_.remove(index);
                }
                return this;
            }

            public AttributeDefinition.Builder getAttributesBuilder(int index) {
                return getAttributesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
                if (this.attributesBuilder_ != null) {
                    return this.attributesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.attributes_);
            }

            public AttributeDefinition.Builder addAttributesBuilder() {
                return getAttributesFieldBuilder().addBuilder(AttributeDefinition.getDefaultInstance());
            }

            public AttributeDefinition.Builder addAttributesBuilder(int index) {
                return getAttributesFieldBuilder().addBuilder(index, AttributeDefinition.getDefaultInstance());
            }

            public List<AttributeDefinition.Builder> getAttributesBuilderList() {
                return getAttributesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> getAttributesFieldBuilder() {
                if (this.attributesBuilder_ == null) {
                    this.attributesBuilder_ = new RepeatedFieldBuilderV3<>(this.attributes_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.attributes_ = null;
                }
                return this.attributesBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.FieldDefinitionOrBuilder
            public int getPeToken() {
                return this.peToken_;
            }

            public Builder setPeToken(int value) {
                this.peToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearPeToken() {
                this.peToken_ = 0;
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

        public static FieldDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<FieldDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<FieldDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public FieldDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$PropertyDefinition.class */
    public static final class PropertyDefinition extends GeneratedMessageV3 implements PropertyDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ACCESSIBILITY_FIELD_NUMBER = 1;
        private int accessibility_;
        public static final int CAN_GET_FIELD_NUMBER = 2;
        private boolean canGet_;
        public static final int CAN_SET_FIELD_NUMBER = 3;
        private boolean canSet_;
        public static final int IS_ABSTRACT_FIELD_NUMBER = 4;
        private boolean isAbstract_;
        public static final int IS_SEALED_FIELD_NUMBER = 5;
        private boolean isSealed_;
        public static final int IS_EXPLICIT_INTERFACE_IMPLEMENTATION_FIELD_NUMBER = 6;
        private boolean isExplicitInterfaceImplementation_;
        public static final int IS_OVERRIDE_FIELD_NUMBER = 7;
        private boolean isOverride_;
        public static final int IS_VIRTUAL_FIELD_NUMBER = 8;
        private boolean isVirtual_;
        public static final int IS_STATIC_FIELD_NUMBER = 9;
        private boolean isStatic_;
        public static final int IS_EXTERN_FIELD_NUMBER = 15;
        private boolean isExtern_;
        public static final int GETTER_FIELD_NUMBER = 10;
        private MethodDefinition getter_;
        public static final int SETTER_FIELD_NUMBER = 11;
        private MethodDefinition setter_;
        public static final int TYPE_FIELD_NUMBER = 12;
        private TypeDefinition type_;
        public static final int TYPE_KIND_FIELD_NUMBER = 14;
        private int typeKind_;
        public static final int NAME_FIELD_NUMBER = 13;
        private volatile Object name_;
        public static final int ATTRIBUTES_FIELD_NUMBER = 16;
        private List<AttributeDefinition> attributes_;
        public static final int PE_TOKEN_FIELD_NUMBER = 17;
        private int peToken_;
        private byte memoizedIsInitialized;
        private static final PropertyDefinition DEFAULT_INSTANCE = new PropertyDefinition();
        private static final Parser<PropertyDefinition> PARSER = new AbstractParser<PropertyDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinition.1
            @Override // com.google.protobuf.Parser
            public PropertyDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new PropertyDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ PropertyDefinition(GeneratedMessageV3.Builder builder, PropertyDefinition propertyDefinition) {
            this(builder);
        }

        private PropertyDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private PropertyDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.accessibility_ = 0;
            this.typeKind_ = 0;
            this.name_ = "";
            this.attributes_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new PropertyDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ PropertyDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, PropertyDefinition propertyDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private PropertyDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                this.accessibility_ = rawValue;
                                break;
                            case 16:
                                this.canGet_ = input.readBool();
                                break;
                            case 24:
                                this.canSet_ = input.readBool();
                                break;
                            case 32:
                                this.isAbstract_ = input.readBool();
                                break;
                            case 40:
                                this.isSealed_ = input.readBool();
                                break;
                            case 48:
                                this.isExplicitInterfaceImplementation_ = input.readBool();
                                break;
                            case 56:
                                this.isOverride_ = input.readBool();
                                break;
                            case 64:
                                this.isVirtual_ = input.readBool();
                                break;
                            case 72:
                                this.isStatic_ = input.readBool();
                                break;
                            case 82:
                                MethodDefinition.Builder subBuilder = null;
                                subBuilder = this.getter_ != null ? this.getter_.toBuilder() : subBuilder;
                                this.getter_ = (MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.getter_);
                                    this.getter_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 90:
                                MethodDefinition.Builder subBuilder2 = null;
                                subBuilder2 = this.setter_ != null ? this.setter_.toBuilder() : subBuilder2;
                                this.setter_ = (MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.setter_);
                                    this.setter_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 98:
                                TypeDefinition.Builder subBuilder3 = null;
                                subBuilder3 = this.type_ != null ? this.type_.toBuilder() : subBuilder3;
                                this.type_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder3 == null) {
                                    break;
                                } else {
                                    subBuilder3.mergeFrom(this.type_);
                                    this.type_ = subBuilder3.buildPartial();
                                    break;
                                }
                            case 106:
                                String s = input.readStringRequireUtf8();
                                this.name_ = s;
                                break;
                            case 112:
                                int rawValue2 = input.readEnum();
                                this.typeKind_ = rawValue2;
                                break;
                            case 120:
                                this.isExtern_ = input.readBool();
                                break;
                            case 130:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.attributes_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.attributes_.add((AttributeDefinition) input.readMessage(AttributeDefinition.parser(), extensionRegistry));
                                break;
                            case 136:
                                this.peToken_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_PropertyDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_PropertyDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(PropertyDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public int getAccessibilityValue() {
            return this.accessibility_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public Accessibility getAccessibility() {
            Accessibility result = Accessibility.valueOf(this.accessibility_);
            return result == null ? Accessibility.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getCanGet() {
            return this.canGet_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getCanSet() {
            return this.canSet_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsAbstract() {
            return this.isAbstract_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsSealed() {
            return this.isSealed_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsExplicitInterfaceImplementation() {
            return this.isExplicitInterfaceImplementation_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsOverride() {
            return this.isOverride_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsVirtual() {
            return this.isVirtual_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsStatic() {
            return this.isStatic_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean getIsExtern() {
            return this.isExtern_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean hasGetter() {
            return this.getter_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public MethodDefinition getGetter() {
            return this.getter_ == null ? MethodDefinition.getDefaultInstance() : this.getter_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public MethodDefinitionOrBuilder getGetterOrBuilder() {
            return getGetter();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean hasSetter() {
            return this.setter_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public MethodDefinition getSetter() {
            return this.setter_ == null ? MethodDefinition.getDefaultInstance() : this.setter_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public MethodDefinitionOrBuilder getSetterOrBuilder() {
            return getSetter();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public TypeDefinition getType() {
            return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public int getTypeKindValue() {
            return this.typeKind_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public TypeKindDef getTypeKind() {
            TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
            return result == null ? TypeKindDef.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
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

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public List<AttributeDefinition> getAttributesList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
            return this.attributes_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public int getAttributesCount() {
            return this.attributes_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public AttributeDefinition getAttributes(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
            return this.attributes_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
        public int getPeToken() {
            return this.peToken_;
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
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                output.writeEnum(1, this.accessibility_);
            }
            if (this.canGet_) {
                output.writeBool(2, this.canGet_);
            }
            if (this.canSet_) {
                output.writeBool(3, this.canSet_);
            }
            if (this.isAbstract_) {
                output.writeBool(4, this.isAbstract_);
            }
            if (this.isSealed_) {
                output.writeBool(5, this.isSealed_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                output.writeBool(6, this.isExplicitInterfaceImplementation_);
            }
            if (this.isOverride_) {
                output.writeBool(7, this.isOverride_);
            }
            if (this.isVirtual_) {
                output.writeBool(8, this.isVirtual_);
            }
            if (this.isStatic_) {
                output.writeBool(9, this.isStatic_);
            }
            if (this.getter_ != null) {
                output.writeMessage(10, getGetter());
            }
            if (this.setter_ != null) {
                output.writeMessage(11, getSetter());
            }
            if (this.type_ != null) {
                output.writeMessage(12, getType());
            }
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 13, this.name_);
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                output.writeEnum(14, this.typeKind_);
            }
            if (this.isExtern_) {
                output.writeBool(15, this.isExtern_);
            }
            for (int i = 0; i < this.attributes_.size(); i++) {
                output.writeMessage(16, this.attributes_.get(i));
            }
            if (this.peToken_ != 0) {
                output.writeInt32(17, this.peToken_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.accessibility_);
            }
            if (this.canGet_) {
                size2 += CodedOutputStream.computeBoolSize(2, this.canGet_);
            }
            if (this.canSet_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.canSet_);
            }
            if (this.isAbstract_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.isAbstract_);
            }
            if (this.isSealed_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.isSealed_);
            }
            if (this.isExplicitInterfaceImplementation_) {
                size2 += CodedOutputStream.computeBoolSize(6, this.isExplicitInterfaceImplementation_);
            }
            if (this.isOverride_) {
                size2 += CodedOutputStream.computeBoolSize(7, this.isOverride_);
            }
            if (this.isVirtual_) {
                size2 += CodedOutputStream.computeBoolSize(8, this.isVirtual_);
            }
            if (this.isStatic_) {
                size2 += CodedOutputStream.computeBoolSize(9, this.isStatic_);
            }
            if (this.getter_ != null) {
                size2 += CodedOutputStream.computeMessageSize(10, getGetter());
            }
            if (this.setter_ != null) {
                size2 += CodedOutputStream.computeMessageSize(11, getSetter());
            }
            if (this.type_ != null) {
                size2 += CodedOutputStream.computeMessageSize(12, getType());
            }
            if (!getNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(13, this.name_);
            }
            if (this.typeKind_ != TypeKindDef.NO_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(14, this.typeKind_);
            }
            if (this.isExtern_) {
                size2 += CodedOutputStream.computeBoolSize(15, this.isExtern_);
            }
            for (int i = 0; i < this.attributes_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(16, this.attributes_.get(i));
            }
            if (this.peToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(17, this.peToken_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof PropertyDefinition)) {
                return super.equals(obj);
            }
            PropertyDefinition other = (PropertyDefinition) obj;
            if (this.accessibility_ != other.accessibility_ || getCanGet() != other.getCanGet() || getCanSet() != other.getCanSet() || getIsAbstract() != other.getIsAbstract() || getIsSealed() != other.getIsSealed() || getIsExplicitInterfaceImplementation() != other.getIsExplicitInterfaceImplementation() || getIsOverride() != other.getIsOverride() || getIsVirtual() != other.getIsVirtual() || getIsStatic() != other.getIsStatic() || getIsExtern() != other.getIsExtern() || hasGetter() != other.hasGetter()) {
                return false;
            }
            if ((hasGetter() && !getGetter().equals(other.getGetter())) || hasSetter() != other.hasSetter()) {
                return false;
            }
            if ((hasSetter() && !getSetter().equals(other.getSetter())) || hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || this.typeKind_ != other.typeKind_ || !getName().equals(other.getName()) || !getAttributesList().equals(other.getAttributesList()) || getPeToken() != other.getPeToken() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            int hash2 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 1)) + this.accessibility_)) + 2)) + Internal.hashBoolean(getCanGet()))) + 3)) + Internal.hashBoolean(getCanSet()))) + 4)) + Internal.hashBoolean(getIsAbstract()))) + 5)) + Internal.hashBoolean(getIsSealed()))) + 6)) + Internal.hashBoolean(getIsExplicitInterfaceImplementation()))) + 7)) + Internal.hashBoolean(getIsOverride()))) + 8)) + Internal.hashBoolean(getIsVirtual()))) + 9)) + Internal.hashBoolean(getIsStatic()))) + 15)) + Internal.hashBoolean(getIsExtern());
            if (hasGetter()) {
                hash2 = (53 * ((37 * hash2) + 10)) + getGetter().hashCode();
            }
            if (hasSetter()) {
                hash2 = (53 * ((37 * hash2) + 11)) + getSetter().hashCode();
            }
            if (hasType()) {
                hash2 = (53 * ((37 * hash2) + 12)) + getType().hashCode();
            }
            int hash3 = (53 * ((37 * ((53 * ((37 * hash2) + 14)) + this.typeKind_)) + 13)) + getName().hashCode();
            if (getAttributesCount() > 0) {
                hash3 = (53 * ((37 * hash3) + 16)) + getAttributesList().hashCode();
            }
            int hash4 = (29 * ((53 * ((37 * hash3) + 17)) + getPeToken())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash4;
            return hash4;
        }

        public static PropertyDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PropertyDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PropertyDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PropertyDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PropertyDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static PropertyDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static PropertyDefinition parseFrom(InputStream input) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static PropertyDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static PropertyDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static PropertyDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static PropertyDefinition parseFrom(CodedInputStream input) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static PropertyDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (PropertyDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(PropertyDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$PropertyDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements PropertyDefinitionOrBuilder {
            private int bitField0_;
            private int accessibility_;
            private boolean canGet_;
            private boolean canSet_;
            private boolean isAbstract_;
            private boolean isSealed_;
            private boolean isExplicitInterfaceImplementation_;
            private boolean isOverride_;
            private boolean isVirtual_;
            private boolean isStatic_;
            private boolean isExtern_;
            private MethodDefinition getter_;
            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getterBuilder_;
            private MethodDefinition setter_;
            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> setterBuilder_;
            private TypeDefinition type_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> typeBuilder_;
            private int typeKind_;
            private Object name_;
            private List<AttributeDefinition> attributes_;
            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> attributesBuilder_;
            private int peToken_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_PropertyDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_PropertyDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(PropertyDefinition.class, Builder.class);
            }

            private Builder() {
                this.accessibility_ = 0;
                this.typeKind_ = 0;
                this.name_ = "";
                this.attributes_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.accessibility_ = 0;
                this.typeKind_ = 0;
                this.name_ = "";
                this.attributes_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (PropertyDefinition.alwaysUseFieldBuilders) {
                    getAttributesFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.accessibility_ = 0;
                this.canGet_ = false;
                this.canSet_ = false;
                this.isAbstract_ = false;
                this.isSealed_ = false;
                this.isExplicitInterfaceImplementation_ = false;
                this.isOverride_ = false;
                this.isVirtual_ = false;
                this.isStatic_ = false;
                this.isExtern_ = false;
                if (this.getterBuilder_ == null) {
                    this.getter_ = null;
                } else {
                    this.getter_ = null;
                    this.getterBuilder_ = null;
                }
                if (this.setterBuilder_ == null) {
                    this.setter_ = null;
                } else {
                    this.setter_ = null;
                    this.setterBuilder_ = null;
                }
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                this.typeKind_ = 0;
                this.name_ = "";
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.attributesBuilder_.clear();
                }
                this.peToken_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_PropertyDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public PropertyDefinition getDefaultInstanceForType() {
                return PropertyDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PropertyDefinition build() {
                PropertyDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public PropertyDefinition buildPartial() {
                PropertyDefinition result = new PropertyDefinition(this, (PropertyDefinition) null);
                int i = this.bitField0_;
                result.accessibility_ = this.accessibility_;
                result.canGet_ = this.canGet_;
                result.canSet_ = this.canSet_;
                result.isAbstract_ = this.isAbstract_;
                result.isSealed_ = this.isSealed_;
                result.isExplicitInterfaceImplementation_ = this.isExplicitInterfaceImplementation_;
                result.isOverride_ = this.isOverride_;
                result.isVirtual_ = this.isVirtual_;
                result.isStatic_ = this.isStatic_;
                result.isExtern_ = this.isExtern_;
                if (this.getterBuilder_ == null) {
                    result.getter_ = this.getter_;
                } else {
                    result.getter_ = this.getterBuilder_.build();
                }
                if (this.setterBuilder_ == null) {
                    result.setter_ = this.setter_;
                } else {
                    result.setter_ = this.setterBuilder_.build();
                }
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                result.typeKind_ = this.typeKind_;
                result.name_ = this.name_;
                if (this.attributesBuilder_ != null) {
                    result.attributes_ = this.attributesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.attributes_ = Collections.unmodifiableList(this.attributes_);
                        this.bitField0_ &= -2;
                    }
                    result.attributes_ = this.attributes_;
                }
                result.peToken_ = this.peToken_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof PropertyDefinition) {
                    return mergeFrom((PropertyDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(PropertyDefinition other) {
                if (other != PropertyDefinition.getDefaultInstance()) {
                    if (other.accessibility_ != 0) {
                        setAccessibilityValue(other.getAccessibilityValue());
                    }
                    if (other.getCanGet()) {
                        setCanGet(other.getCanGet());
                    }
                    if (other.getCanSet()) {
                        setCanSet(other.getCanSet());
                    }
                    if (other.getIsAbstract()) {
                        setIsAbstract(other.getIsAbstract());
                    }
                    if (other.getIsSealed()) {
                        setIsSealed(other.getIsSealed());
                    }
                    if (other.getIsExplicitInterfaceImplementation()) {
                        setIsExplicitInterfaceImplementation(other.getIsExplicitInterfaceImplementation());
                    }
                    if (other.getIsOverride()) {
                        setIsOverride(other.getIsOverride());
                    }
                    if (other.getIsVirtual()) {
                        setIsVirtual(other.getIsVirtual());
                    }
                    if (other.getIsStatic()) {
                        setIsStatic(other.getIsStatic());
                    }
                    if (other.getIsExtern()) {
                        setIsExtern(other.getIsExtern());
                    }
                    if (other.hasGetter()) {
                        mergeGetter(other.getGetter());
                    }
                    if (other.hasSetter()) {
                        mergeSetter(other.getSetter());
                    }
                    if (other.hasType()) {
                        mergeType(other.getType());
                    }
                    if (other.typeKind_ != 0) {
                        setTypeKindValue(other.getTypeKindValue());
                    }
                    if (!other.getName().isEmpty()) {
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (this.attributesBuilder_ == null) {
                        if (!other.attributes_.isEmpty()) {
                            if (this.attributes_.isEmpty()) {
                                this.attributes_ = other.attributes_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureAttributesIsMutable();
                                this.attributes_.addAll(other.attributes_);
                            }
                            onChanged();
                        }
                    } else if (!other.attributes_.isEmpty()) {
                        if (!this.attributesBuilder_.isEmpty()) {
                            this.attributesBuilder_.addAllMessages(other.attributes_);
                        } else {
                            this.attributesBuilder_.dispose();
                            this.attributesBuilder_ = null;
                            this.attributes_ = other.attributes_;
                            this.bitField0_ &= -2;
                            this.attributesBuilder_ = PropertyDefinition.alwaysUseFieldBuilders ? getAttributesFieldBuilder() : null;
                        }
                    }
                    if (other.getPeToken() != 0) {
                        setPeToken(other.getPeToken());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                    return this;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                PropertyDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (PropertyDefinition) PropertyDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        PropertyDefinition propertyDefinition = (PropertyDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public int getAccessibilityValue() {
                return this.accessibility_;
            }

            public Builder setAccessibilityValue(int value) {
                this.accessibility_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public Accessibility getAccessibility() {
                Accessibility result = Accessibility.valueOf(this.accessibility_);
                return result == null ? Accessibility.UNRECOGNIZED : result;
            }

            public Builder setAccessibility(Accessibility value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.accessibility_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAccessibility() {
                this.accessibility_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getCanGet() {
                return this.canGet_;
            }

            public Builder setCanGet(boolean value) {
                this.canGet_ = value;
                onChanged();
                return this;
            }

            public Builder clearCanGet() {
                this.canGet_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getCanSet() {
                return this.canSet_;
            }

            public Builder setCanSet(boolean value) {
                this.canSet_ = value;
                onChanged();
                return this;
            }

            public Builder clearCanSet() {
                this.canSet_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsAbstract() {
                return this.isAbstract_;
            }

            public Builder setIsAbstract(boolean value) {
                this.isAbstract_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsAbstract() {
                this.isAbstract_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsSealed() {
                return this.isSealed_;
            }

            public Builder setIsSealed(boolean value) {
                this.isSealed_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsSealed() {
                this.isSealed_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsExplicitInterfaceImplementation() {
                return this.isExplicitInterfaceImplementation_;
            }

            public Builder setIsExplicitInterfaceImplementation(boolean value) {
                this.isExplicitInterfaceImplementation_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsExplicitInterfaceImplementation() {
                this.isExplicitInterfaceImplementation_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsOverride() {
                return this.isOverride_;
            }

            public Builder setIsOverride(boolean value) {
                this.isOverride_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsOverride() {
                this.isOverride_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsVirtual() {
                return this.isVirtual_;
            }

            public Builder setIsVirtual(boolean value) {
                this.isVirtual_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsVirtual() {
                this.isVirtual_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsStatic() {
                return this.isStatic_;
            }

            public Builder setIsStatic(boolean value) {
                this.isStatic_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsStatic() {
                this.isStatic_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean getIsExtern() {
                return this.isExtern_;
            }

            public Builder setIsExtern(boolean value) {
                this.isExtern_ = value;
                onChanged();
                return this;
            }

            public Builder clearIsExtern() {
                this.isExtern_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean hasGetter() {
                return (this.getterBuilder_ == null && this.getter_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public MethodDefinition getGetter() {
                if (this.getterBuilder_ == null) {
                    return this.getter_ == null ? MethodDefinition.getDefaultInstance() : this.getter_;
                }
                return this.getterBuilder_.getMessage();
            }

            public Builder setGetter(MethodDefinition value) {
                if (this.getterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.getter_ = value;
                    onChanged();
                } else {
                    this.getterBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setGetter(MethodDefinition.Builder builderForValue) {
                if (this.getterBuilder_ == null) {
                    this.getter_ = builderForValue.build();
                    onChanged();
                } else {
                    this.getterBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeGetter(MethodDefinition value) {
                if (this.getterBuilder_ == null) {
                    if (this.getter_ != null) {
                        this.getter_ = MethodDefinition.newBuilder(this.getter_).mergeFrom(value).buildPartial();
                    } else {
                        this.getter_ = value;
                    }
                    onChanged();
                } else {
                    this.getterBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearGetter() {
                if (this.getterBuilder_ == null) {
                    this.getter_ = null;
                    onChanged();
                } else {
                    this.getter_ = null;
                    this.getterBuilder_ = null;
                }
                return this;
            }

            public MethodDefinition.Builder getGetterBuilder() {
                onChanged();
                return getGetterFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public MethodDefinitionOrBuilder getGetterOrBuilder() {
                if (this.getterBuilder_ != null) {
                    return this.getterBuilder_.getMessageOrBuilder();
                }
                return this.getter_ == null ? MethodDefinition.getDefaultInstance() : this.getter_;
            }

            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getGetterFieldBuilder() {
                if (this.getterBuilder_ == null) {
                    this.getterBuilder_ = new SingleFieldBuilderV3<>(getGetter(), getParentForChildren(), isClean());
                    this.getter_ = null;
                }
                return this.getterBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean hasSetter() {
                return (this.setterBuilder_ == null && this.setter_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public MethodDefinition getSetter() {
                if (this.setterBuilder_ == null) {
                    return this.setter_ == null ? MethodDefinition.getDefaultInstance() : this.setter_;
                }
                return this.setterBuilder_.getMessage();
            }

            public Builder setSetter(MethodDefinition value) {
                if (this.setterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.setter_ = value;
                    onChanged();
                } else {
                    this.setterBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setSetter(MethodDefinition.Builder builderForValue) {
                if (this.setterBuilder_ == null) {
                    this.setter_ = builderForValue.build();
                    onChanged();
                } else {
                    this.setterBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeSetter(MethodDefinition value) {
                if (this.setterBuilder_ == null) {
                    if (this.setter_ != null) {
                        this.setter_ = MethodDefinition.newBuilder(this.setter_).mergeFrom(value).buildPartial();
                    } else {
                        this.setter_ = value;
                    }
                    onChanged();
                } else {
                    this.setterBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearSetter() {
                if (this.setterBuilder_ == null) {
                    this.setter_ = null;
                    onChanged();
                } else {
                    this.setter_ = null;
                    this.setterBuilder_ = null;
                }
                return this;
            }

            public MethodDefinition.Builder getSetterBuilder() {
                onChanged();
                return getSetterFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public MethodDefinitionOrBuilder getSetterOrBuilder() {
                if (this.setterBuilder_ != null) {
                    return this.setterBuilder_.getMessageOrBuilder();
                }
                return this.setter_ == null ? MethodDefinition.getDefaultInstance() : this.setter_;
            }

            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getSetterFieldBuilder() {
                if (this.setterBuilder_ == null) {
                    this.setterBuilder_ = new SingleFieldBuilderV3<>(getSetter(), getParentForChildren(), isClean());
                    this.setter_ = null;
                }
                return this.setterBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.type_ = value;
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setType(TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
                    } else {
                        this.type_ = value;
                    }
                    onChanged();
                } else {
                    this.typeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearType() {
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                    onChanged();
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public int getTypeKindValue() {
                return this.typeKind_;
            }

            public Builder setTypeKindValue(int value) {
                this.typeKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public TypeKindDef getTypeKind() {
                TypeKindDef result = TypeKindDef.valueOf(this.typeKind_);
                return result == null ? TypeKindDef.UNRECOGNIZED : result;
            }

            public Builder setTypeKind(TypeKindDef value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.typeKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearTypeKind() {
                this.typeKind_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
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

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
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
                this.name_ = PropertyDefinition.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    PropertyDefinition.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            private void ensureAttributesIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.attributes_ = new ArrayList(this.attributes_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public List<AttributeDefinition> getAttributesList() {
                if (this.attributesBuilder_ == null) {
                    return Collections.unmodifiableList(this.attributes_);
                }
                return this.attributesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public int getAttributesCount() {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.size();
                }
                return this.attributesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public AttributeDefinition getAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessage(index);
            }

            public Builder setAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition value) {
                if (this.attributesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, value);
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addAttributes(AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addAttributes(int index, AttributeDefinition.Builder builderForValue) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.attributesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllAttributes(Iterable<? extends AttributeDefinition> values) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.attributes_);
                    onChanged();
                } else {
                    this.attributesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearAttributes() {
                if (this.attributesBuilder_ == null) {
                    this.attributes_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.attributesBuilder_.clear();
                }
                return this;
            }

            public Builder removeAttributes(int index) {
                if (this.attributesBuilder_ == null) {
                    ensureAttributesIsMutable();
                    this.attributes_.remove(index);
                    onChanged();
                } else {
                    this.attributesBuilder_.remove(index);
                }
                return this;
            }

            public AttributeDefinition.Builder getAttributesBuilder(int index) {
                return getAttributesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public AttributeDefinitionOrBuilder getAttributesOrBuilder(int index) {
                if (this.attributesBuilder_ == null) {
                    return this.attributes_.get(index);
                }
                return this.attributesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public List<? extends AttributeDefinitionOrBuilder> getAttributesOrBuilderList() {
                if (this.attributesBuilder_ != null) {
                    return this.attributesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.attributes_);
            }

            public AttributeDefinition.Builder addAttributesBuilder() {
                return getAttributesFieldBuilder().addBuilder(AttributeDefinition.getDefaultInstance());
            }

            public AttributeDefinition.Builder addAttributesBuilder(int index) {
                return getAttributesFieldBuilder().addBuilder(index, AttributeDefinition.getDefaultInstance());
            }

            public List<AttributeDefinition.Builder> getAttributesBuilderList() {
                return getAttributesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeDefinition, AttributeDefinition.Builder, AttributeDefinitionOrBuilder> getAttributesFieldBuilder() {
                if (this.attributesBuilder_ == null) {
                    this.attributesBuilder_ = new RepeatedFieldBuilderV3<>(this.attributes_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.attributes_ = null;
                }
                return this.attributesBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.PropertyDefinitionOrBuilder
            public int getPeToken() {
                return this.peToken_;
            }

            public Builder setPeToken(int value) {
                this.peToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearPeToken() {
                this.peToken_ = 0;
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

        public static PropertyDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<PropertyDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<PropertyDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public PropertyDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeDefinition.class */
    public static final class AttributeDefinition extends GeneratedMessageV3 implements AttributeDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ATTRIBUTE_TYPE_FIELD_NUMBER = 1;
        private TypeDefinition attributeType_;
        public static final int CONSTRUCTOR_FIELD_NUMBER = 2;
        private volatile Object constructor_;
        public static final int FIXED_ARGUMENTS_FIELD_NUMBER = 3;
        private List<AttributeArgumentDefinition> fixedArguments_;
        public static final int NAMED_ARGUMENTS_FIELD_NUMBER = 4;
        private List<AttributeArgumentDefinition> namedArguments_;
        private byte memoizedIsInitialized;
        private static final AttributeDefinition DEFAULT_INSTANCE = new AttributeDefinition();
        private static final Parser<AttributeDefinition> PARSER = new AbstractParser<AttributeDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinition.1
            @Override // com.google.protobuf.Parser
            public AttributeDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new AttributeDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ AttributeDefinition(GeneratedMessageV3.Builder builder, AttributeDefinition attributeDefinition) {
            this(builder);
        }

        private AttributeDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private AttributeDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.constructor_ = "";
            this.fixedArguments_ = Collections.emptyList();
            this.namedArguments_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new AttributeDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ AttributeDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, AttributeDefinition attributeDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private AttributeDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 10:
                                TypeDefinition.Builder subBuilder = null;
                                subBuilder = this.attributeType_ != null ? this.attributeType_.toBuilder() : subBuilder;
                                this.attributeType_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.attributeType_);
                                    this.attributeType_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 18:
                                String s = input.readStringRequireUtf8();
                                this.constructor_ = s;
                                break;
                            case 26:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.fixedArguments_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.fixedArguments_.add((AttributeArgumentDefinition) input.readMessage(AttributeArgumentDefinition.parser(), extensionRegistry));
                                break;
                            case 34:
                                if ((mutable_bitField0_ & 2) == 0) {
                                    this.namedArguments_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.namedArguments_.add((AttributeArgumentDefinition) input.readMessage(AttributeArgumentDefinition.parser(), extensionRegistry));
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.fixedArguments_ = Collections.unmodifiableList(this.fixedArguments_);
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.namedArguments_ = Collections.unmodifiableList(this.namedArguments_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_AttributeDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_AttributeDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(AttributeDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public boolean hasAttributeType() {
            return this.attributeType_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public TypeDefinition getAttributeType() {
            return this.attributeType_ == null ? TypeDefinition.getDefaultInstance() : this.attributeType_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public TypeDefinitionOrBuilder getAttributeTypeOrBuilder() {
            return getAttributeType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public String getConstructor() {
            Object ref = this.constructor_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.constructor_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public ByteString getConstructorBytes() {
            Object ref = this.constructor_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.constructor_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public List<AttributeArgumentDefinition> getFixedArgumentsList() {
            return this.fixedArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public List<? extends AttributeArgumentDefinitionOrBuilder> getFixedArgumentsOrBuilderList() {
            return this.fixedArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public int getFixedArgumentsCount() {
            return this.fixedArguments_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public AttributeArgumentDefinition getFixedArguments(int index) {
            return this.fixedArguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public AttributeArgumentDefinitionOrBuilder getFixedArgumentsOrBuilder(int index) {
            return this.fixedArguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public List<AttributeArgumentDefinition> getNamedArgumentsList() {
            return this.namedArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public List<? extends AttributeArgumentDefinitionOrBuilder> getNamedArgumentsOrBuilderList() {
            return this.namedArguments_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public int getNamedArgumentsCount() {
            return this.namedArguments_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public AttributeArgumentDefinition getNamedArguments(int index) {
            return this.namedArguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
        public AttributeArgumentDefinitionOrBuilder getNamedArgumentsOrBuilder(int index) {
            return this.namedArguments_.get(index);
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
            if (this.attributeType_ != null) {
                output.writeMessage(1, getAttributeType());
            }
            if (!getConstructorBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.constructor_);
            }
            for (int i = 0; i < this.fixedArguments_.size(); i++) {
                output.writeMessage(3, this.fixedArguments_.get(i));
            }
            for (int i2 = 0; i2 < this.namedArguments_.size(); i2++) {
                output.writeMessage(4, this.namedArguments_.get(i2));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.attributeType_ != null) {
                size2 = 0 + CodedOutputStream.computeMessageSize(1, getAttributeType());
            }
            if (!getConstructorBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.constructor_);
            }
            for (int i = 0; i < this.fixedArguments_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.fixedArguments_.get(i));
            }
            for (int i2 = 0; i2 < this.namedArguments_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(4, this.namedArguments_.get(i2));
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AttributeDefinition)) {
                return super.equals(obj);
            }
            AttributeDefinition other = (AttributeDefinition) obj;
            if (hasAttributeType() != other.hasAttributeType()) {
                return false;
            }
            if ((hasAttributeType() && !getAttributeType().equals(other.getAttributeType())) || !getConstructor().equals(other.getConstructor()) || !getFixedArgumentsList().equals(other.getFixedArgumentsList()) || !getNamedArgumentsList().equals(other.getNamedArgumentsList()) || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            if (hasAttributeType()) {
                hash = (53 * ((37 * hash) + 1)) + getAttributeType().hashCode();
            }
            int hash2 = (53 * ((37 * hash) + 2)) + getConstructor().hashCode();
            if (getFixedArgumentsCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 3)) + getFixedArgumentsList().hashCode();
            }
            if (getNamedArgumentsCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 4)) + getNamedArgumentsList().hashCode();
            }
            int hash3 = (29 * hash2) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash3;
            return hash3;
        }

        public static AttributeDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeDefinition parseFrom(InputStream input) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AttributeDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static AttributeDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static AttributeDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static AttributeDefinition parseFrom(CodedInputStream input) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AttributeDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AttributeDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AttributeDefinitionOrBuilder {
            private int bitField0_;
            private TypeDefinition attributeType_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> attributeTypeBuilder_;
            private Object constructor_;
            private List<AttributeArgumentDefinition> fixedArguments_;
            private RepeatedFieldBuilderV3<AttributeArgumentDefinition, AttributeArgumentDefinition.Builder, AttributeArgumentDefinitionOrBuilder> fixedArgumentsBuilder_;
            private List<AttributeArgumentDefinition> namedArguments_;
            private RepeatedFieldBuilderV3<AttributeArgumentDefinition, AttributeArgumentDefinition.Builder, AttributeArgumentDefinitionOrBuilder> namedArgumentsBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_AttributeDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_AttributeDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(AttributeDefinition.class, Builder.class);
            }

            private Builder() {
                this.constructor_ = "";
                this.fixedArguments_ = Collections.emptyList();
                this.namedArguments_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.constructor_ = "";
                this.fixedArguments_ = Collections.emptyList();
                this.namedArguments_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (AttributeDefinition.alwaysUseFieldBuilders) {
                    getFixedArgumentsFieldBuilder();
                    getNamedArgumentsFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.attributeTypeBuilder_ == null) {
                    this.attributeType_ = null;
                } else {
                    this.attributeType_ = null;
                    this.attributeTypeBuilder_ = null;
                }
                this.constructor_ = "";
                if (this.fixedArgumentsBuilder_ == null) {
                    this.fixedArguments_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.fixedArgumentsBuilder_.clear();
                }
                if (this.namedArgumentsBuilder_ == null) {
                    this.namedArguments_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.namedArgumentsBuilder_.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_AttributeDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public AttributeDefinition getDefaultInstanceForType() {
                return AttributeDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AttributeDefinition build() {
                AttributeDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AttributeDefinition buildPartial() {
                AttributeDefinition result = new AttributeDefinition(this, (AttributeDefinition) null);
                int i = this.bitField0_;
                if (this.attributeTypeBuilder_ == null) {
                    result.attributeType_ = this.attributeType_;
                } else {
                    result.attributeType_ = this.attributeTypeBuilder_.build();
                }
                result.constructor_ = this.constructor_;
                if (this.fixedArgumentsBuilder_ != null) {
                    result.fixedArguments_ = this.fixedArgumentsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.fixedArguments_ = Collections.unmodifiableList(this.fixedArguments_);
                        this.bitField0_ &= -2;
                    }
                    result.fixedArguments_ = this.fixedArguments_;
                }
                if (this.namedArgumentsBuilder_ != null) {
                    result.namedArguments_ = this.namedArgumentsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 2) != 0) {
                        this.namedArguments_ = Collections.unmodifiableList(this.namedArguments_);
                        this.bitField0_ &= -3;
                    }
                    result.namedArguments_ = this.namedArguments_;
                }
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof AttributeDefinition) {
                    return mergeFrom((AttributeDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AttributeDefinition other) {
                if (other == AttributeDefinition.getDefaultInstance()) {
                    return this;
                }
                if (other.hasAttributeType()) {
                    mergeAttributeType(other.getAttributeType());
                }
                if (!other.getConstructor().isEmpty()) {
                    this.constructor_ = other.constructor_;
                    onChanged();
                }
                if (this.fixedArgumentsBuilder_ == null) {
                    if (!other.fixedArguments_.isEmpty()) {
                        if (this.fixedArguments_.isEmpty()) {
                            this.fixedArguments_ = other.fixedArguments_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureFixedArgumentsIsMutable();
                            this.fixedArguments_.addAll(other.fixedArguments_);
                        }
                        onChanged();
                    }
                } else if (!other.fixedArguments_.isEmpty()) {
                    if (!this.fixedArgumentsBuilder_.isEmpty()) {
                        this.fixedArgumentsBuilder_.addAllMessages(other.fixedArguments_);
                    } else {
                        this.fixedArgumentsBuilder_.dispose();
                        this.fixedArgumentsBuilder_ = null;
                        this.fixedArguments_ = other.fixedArguments_;
                        this.bitField0_ &= -2;
                        this.fixedArgumentsBuilder_ = AttributeDefinition.alwaysUseFieldBuilders ? getFixedArgumentsFieldBuilder() : null;
                    }
                }
                if (this.namedArgumentsBuilder_ == null) {
                    if (!other.namedArguments_.isEmpty()) {
                        if (this.namedArguments_.isEmpty()) {
                            this.namedArguments_ = other.namedArguments_;
                            this.bitField0_ &= -3;
                        } else {
                            ensureNamedArgumentsIsMutable();
                            this.namedArguments_.addAll(other.namedArguments_);
                        }
                        onChanged();
                    }
                } else if (!other.namedArguments_.isEmpty()) {
                    if (!this.namedArgumentsBuilder_.isEmpty()) {
                        this.namedArgumentsBuilder_.addAllMessages(other.namedArguments_);
                    } else {
                        this.namedArgumentsBuilder_.dispose();
                        this.namedArgumentsBuilder_ = null;
                        this.namedArguments_ = other.namedArguments_;
                        this.bitField0_ &= -3;
                        this.namedArgumentsBuilder_ = AttributeDefinition.alwaysUseFieldBuilders ? getNamedArgumentsFieldBuilder() : null;
                    }
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AttributeDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (AttributeDefinition) AttributeDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        AttributeDefinition attributeDefinition = (AttributeDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public boolean hasAttributeType() {
                return (this.attributeTypeBuilder_ == null && this.attributeType_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public TypeDefinition getAttributeType() {
                if (this.attributeTypeBuilder_ == null) {
                    return this.attributeType_ == null ? TypeDefinition.getDefaultInstance() : this.attributeType_;
                }
                return this.attributeTypeBuilder_.getMessage();
            }

            public Builder setAttributeType(TypeDefinition value) {
                if (this.attributeTypeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.attributeType_ = value;
                    onChanged();
                } else {
                    this.attributeTypeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setAttributeType(TypeDefinition.Builder builderForValue) {
                if (this.attributeTypeBuilder_ == null) {
                    this.attributeType_ = builderForValue.build();
                    onChanged();
                } else {
                    this.attributeTypeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeAttributeType(TypeDefinition value) {
                if (this.attributeTypeBuilder_ == null) {
                    if (this.attributeType_ != null) {
                        this.attributeType_ = TypeDefinition.newBuilder(this.attributeType_).mergeFrom(value).buildPartial();
                    } else {
                        this.attributeType_ = value;
                    }
                    onChanged();
                } else {
                    this.attributeTypeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearAttributeType() {
                if (this.attributeTypeBuilder_ == null) {
                    this.attributeType_ = null;
                    onChanged();
                } else {
                    this.attributeType_ = null;
                    this.attributeTypeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getAttributeTypeBuilder() {
                onChanged();
                return getAttributeTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public TypeDefinitionOrBuilder getAttributeTypeOrBuilder() {
                if (this.attributeTypeBuilder_ != null) {
                    return this.attributeTypeBuilder_.getMessageOrBuilder();
                }
                return this.attributeType_ == null ? TypeDefinition.getDefaultInstance() : this.attributeType_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getAttributeTypeFieldBuilder() {
                if (this.attributeTypeBuilder_ == null) {
                    this.attributeTypeBuilder_ = new SingleFieldBuilderV3<>(getAttributeType(), getParentForChildren(), isClean());
                    this.attributeType_ = null;
                }
                return this.attributeTypeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public String getConstructor() {
                Object ref = this.constructor_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.constructor_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public ByteString getConstructorBytes() {
                Object ref = this.constructor_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.constructor_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setConstructor(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.constructor_ = value;
                onChanged();
                return this;
            }

            public Builder clearConstructor() {
                this.constructor_ = AttributeDefinition.getDefaultInstance().getConstructor();
                onChanged();
                return this;
            }

            public Builder setConstructorBytes(ByteString value) {
                if (value != null) {
                    AttributeDefinition.checkByteStringIsUtf8(value);
                    this.constructor_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            private void ensureFixedArgumentsIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.fixedArguments_ = new ArrayList(this.fixedArguments_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public List<AttributeArgumentDefinition> getFixedArgumentsList() {
                if (this.fixedArgumentsBuilder_ == null) {
                    return Collections.unmodifiableList(this.fixedArguments_);
                }
                return this.fixedArgumentsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public int getFixedArgumentsCount() {
                if (this.fixedArgumentsBuilder_ == null) {
                    return this.fixedArguments_.size();
                }
                return this.fixedArgumentsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public AttributeArgumentDefinition getFixedArguments(int index) {
                if (this.fixedArgumentsBuilder_ == null) {
                    return this.fixedArguments_.get(index);
                }
                return this.fixedArgumentsBuilder_.getMessage(index);
            }

            public Builder setFixedArguments(int index, AttributeArgumentDefinition value) {
                if (this.fixedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.set(index, value);
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setFixedArguments(int index, AttributeArgumentDefinition.Builder builderForValue) {
                if (this.fixedArgumentsBuilder_ == null) {
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addFixedArguments(AttributeArgumentDefinition value) {
                if (this.fixedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.add(value);
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addFixedArguments(int index, AttributeArgumentDefinition value) {
                if (this.fixedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.add(index, value);
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addFixedArguments(AttributeArgumentDefinition.Builder builderForValue) {
                if (this.fixedArgumentsBuilder_ == null) {
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addFixedArguments(int index, AttributeArgumentDefinition.Builder builderForValue) {
                if (this.fixedArgumentsBuilder_ == null) {
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllFixedArguments(Iterable<? extends AttributeArgumentDefinition> values) {
                if (this.fixedArgumentsBuilder_ == null) {
                    ensureFixedArgumentsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.fixedArguments_);
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearFixedArguments() {
                if (this.fixedArgumentsBuilder_ == null) {
                    this.fixedArguments_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.clear();
                }
                return this;
            }

            public Builder removeFixedArguments(int index) {
                if (this.fixedArgumentsBuilder_ == null) {
                    ensureFixedArgumentsIsMutable();
                    this.fixedArguments_.remove(index);
                    onChanged();
                } else {
                    this.fixedArgumentsBuilder_.remove(index);
                }
                return this;
            }

            public AttributeArgumentDefinition.Builder getFixedArgumentsBuilder(int index) {
                return getFixedArgumentsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public AttributeArgumentDefinitionOrBuilder getFixedArgumentsOrBuilder(int index) {
                if (this.fixedArgumentsBuilder_ == null) {
                    return this.fixedArguments_.get(index);
                }
                return this.fixedArgumentsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public List<? extends AttributeArgumentDefinitionOrBuilder> getFixedArgumentsOrBuilderList() {
                if (this.fixedArgumentsBuilder_ != null) {
                    return this.fixedArgumentsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.fixedArguments_);
            }

            public AttributeArgumentDefinition.Builder addFixedArgumentsBuilder() {
                return getFixedArgumentsFieldBuilder().addBuilder(AttributeArgumentDefinition.getDefaultInstance());
            }

            public AttributeArgumentDefinition.Builder addFixedArgumentsBuilder(int index) {
                return getFixedArgumentsFieldBuilder().addBuilder(index, AttributeArgumentDefinition.getDefaultInstance());
            }

            public List<AttributeArgumentDefinition.Builder> getFixedArgumentsBuilderList() {
                return getFixedArgumentsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeArgumentDefinition, AttributeArgumentDefinition.Builder, AttributeArgumentDefinitionOrBuilder> getFixedArgumentsFieldBuilder() {
                if (this.fixedArgumentsBuilder_ == null) {
                    this.fixedArgumentsBuilder_ = new RepeatedFieldBuilderV3<>(this.fixedArguments_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.fixedArguments_ = null;
                }
                return this.fixedArgumentsBuilder_;
            }

            private void ensureNamedArgumentsIsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.namedArguments_ = new ArrayList(this.namedArguments_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public List<AttributeArgumentDefinition> getNamedArgumentsList() {
                if (this.namedArgumentsBuilder_ == null) {
                    return Collections.unmodifiableList(this.namedArguments_);
                }
                return this.namedArgumentsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public int getNamedArgumentsCount() {
                if (this.namedArgumentsBuilder_ == null) {
                    return this.namedArguments_.size();
                }
                return this.namedArgumentsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public AttributeArgumentDefinition getNamedArguments(int index) {
                if (this.namedArgumentsBuilder_ == null) {
                    return this.namedArguments_.get(index);
                }
                return this.namedArgumentsBuilder_.getMessage(index);
            }

            public Builder setNamedArguments(int index, AttributeArgumentDefinition value) {
                if (this.namedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.set(index, value);
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setNamedArguments(int index, AttributeArgumentDefinition.Builder builderForValue) {
                if (this.namedArgumentsBuilder_ == null) {
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addNamedArguments(AttributeArgumentDefinition value) {
                if (this.namedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.add(value);
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addNamedArguments(int index, AttributeArgumentDefinition value) {
                if (this.namedArgumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.add(index, value);
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addNamedArguments(AttributeArgumentDefinition.Builder builderForValue) {
                if (this.namedArgumentsBuilder_ == null) {
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addNamedArguments(int index, AttributeArgumentDefinition.Builder builderForValue) {
                if (this.namedArgumentsBuilder_ == null) {
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllNamedArguments(Iterable<? extends AttributeArgumentDefinition> values) {
                if (this.namedArgumentsBuilder_ == null) {
                    ensureNamedArgumentsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.namedArguments_);
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearNamedArguments() {
                if (this.namedArgumentsBuilder_ == null) {
                    this.namedArguments_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.clear();
                }
                return this;
            }

            public Builder removeNamedArguments(int index) {
                if (this.namedArgumentsBuilder_ == null) {
                    ensureNamedArgumentsIsMutable();
                    this.namedArguments_.remove(index);
                    onChanged();
                } else {
                    this.namedArgumentsBuilder_.remove(index);
                }
                return this;
            }

            public AttributeArgumentDefinition.Builder getNamedArgumentsBuilder(int index) {
                return getNamedArgumentsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public AttributeArgumentDefinitionOrBuilder getNamedArgumentsOrBuilder(int index) {
                if (this.namedArgumentsBuilder_ == null) {
                    return this.namedArguments_.get(index);
                }
                return this.namedArgumentsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeDefinitionOrBuilder
            public List<? extends AttributeArgumentDefinitionOrBuilder> getNamedArgumentsOrBuilderList() {
                if (this.namedArgumentsBuilder_ != null) {
                    return this.namedArgumentsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.namedArguments_);
            }

            public AttributeArgumentDefinition.Builder addNamedArgumentsBuilder() {
                return getNamedArgumentsFieldBuilder().addBuilder(AttributeArgumentDefinition.getDefaultInstance());
            }

            public AttributeArgumentDefinition.Builder addNamedArgumentsBuilder(int index) {
                return getNamedArgumentsFieldBuilder().addBuilder(index, AttributeArgumentDefinition.getDefaultInstance());
            }

            public List<AttributeArgumentDefinition.Builder> getNamedArgumentsBuilderList() {
                return getNamedArgumentsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<AttributeArgumentDefinition, AttributeArgumentDefinition.Builder, AttributeArgumentDefinitionOrBuilder> getNamedArgumentsFieldBuilder() {
                if (this.namedArgumentsBuilder_ == null) {
                    this.namedArgumentsBuilder_ = new RepeatedFieldBuilderV3<>(this.namedArguments_, (this.bitField0_ & 2) != 0, getParentForChildren(), isClean());
                    this.namedArguments_ = null;
                }
                return this.namedArgumentsBuilder_;
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

        public static AttributeDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AttributeDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<AttributeDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public AttributeDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeArgumentDefinition.class */
    public static final class AttributeArgumentDefinition extends GeneratedMessageV3 implements AttributeArgumentDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int TYPE_FIELD_NUMBER = 1;
        private TypeDefinition type_;
        public static final int NAME_FIELD_NUMBER = 2;
        private volatile Object name_;
        public static final int VALUE_STRING_FIELD_NUMBER = 3;
        private LazyStringList valueString_;
        public static final int VALUE_INT32_FIELD_NUMBER = 4;
        private Internal.IntList valueInt32_;
        private int valueInt32MemoizedSerializedSize;
        public static final int VALUE_INT64_FIELD_NUMBER = 5;
        private Internal.LongList valueInt64_;
        private int valueInt64MemoizedSerializedSize;
        public static final int VALUE_DOUBLE_FIELD_NUMBER = 6;
        private Internal.DoubleList valueDouble_;
        private int valueDoubleMemoizedSerializedSize;
        public static final int VALUE_FLOAT_FIELD_NUMBER = 7;
        private Internal.FloatList valueFloat_;
        private int valueFloatMemoizedSerializedSize;
        private byte memoizedIsInitialized;
        private static final AttributeArgumentDefinition DEFAULT_INSTANCE = new AttributeArgumentDefinition();
        private static final Parser<AttributeArgumentDefinition> PARSER = new AbstractParser<AttributeArgumentDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinition.1
            @Override // com.google.protobuf.Parser
            public AttributeArgumentDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new AttributeArgumentDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ AttributeArgumentDefinition(GeneratedMessageV3.Builder builder, AttributeArgumentDefinition attributeArgumentDefinition) {
            this(builder);
        }

        private AttributeArgumentDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.valueInt32MemoizedSerializedSize = -1;
            this.valueInt64MemoizedSerializedSize = -1;
            this.valueDoubleMemoizedSerializedSize = -1;
            this.valueFloatMemoizedSerializedSize = -1;
            this.memoizedIsInitialized = (byte) -1;
        }

        private AttributeArgumentDefinition() {
            this.valueInt32MemoizedSerializedSize = -1;
            this.valueInt64MemoizedSerializedSize = -1;
            this.valueDoubleMemoizedSerializedSize = -1;
            this.valueFloatMemoizedSerializedSize = -1;
            this.memoizedIsInitialized = (byte) -1;
            this.name_ = "";
            this.valueString_ = LazyStringArrayList.EMPTY;
            this.valueInt32_ = emptyIntList();
            this.valueInt64_ = emptyLongList();
            this.valueDouble_ = emptyDoubleList();
            this.valueFloat_ = emptyFloatList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new AttributeArgumentDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ AttributeArgumentDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, AttributeArgumentDefinition attributeArgumentDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private AttributeArgumentDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            int mutable_bitField0_ = 0;
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        try {
                            int tag = input.readTag();
                            switch (tag) {
                                case 0:
                                    done = true;
                                    break;
                                case 10:
                                    TypeDefinition.Builder subBuilder = null;
                                    subBuilder = this.type_ != null ? this.type_.toBuilder() : subBuilder;
                                    this.type_ = (TypeDefinition) input.readMessage(TypeDefinition.parser(), extensionRegistry);
                                    if (subBuilder == null) {
                                        break;
                                    } else {
                                        subBuilder.mergeFrom(this.type_);
                                        this.type_ = subBuilder.buildPartial();
                                        break;
                                    }
                                case 18:
                                    String s = input.readStringRequireUtf8();
                                    this.name_ = s;
                                    break;
                                case 26:
                                    String s2 = input.readStringRequireUtf8();
                                    if ((mutable_bitField0_ & 1) == 0) {
                                        this.valueString_ = new LazyStringArrayList();
                                        mutable_bitField0_ |= 1;
                                    }
                                    this.valueString_.add(s2);
                                    break;
                                case 32:
                                    if ((mutable_bitField0_ & 2) == 0) {
                                        this.valueInt32_ = newIntList();
                                        mutable_bitField0_ |= 2;
                                    }
                                    this.valueInt32_.addInt(input.readInt32());
                                    break;
                                case 34:
                                    int length = input.readRawVarint32();
                                    int limit = input.pushLimit(length);
                                    if ((mutable_bitField0_ & 2) == 0 && input.getBytesUntilLimit() > 0) {
                                        this.valueInt32_ = newIntList();
                                        mutable_bitField0_ |= 2;
                                    }
                                    while (input.getBytesUntilLimit() > 0) {
                                        this.valueInt32_.addInt(input.readInt32());
                                    }
                                    input.popLimit(limit);
                                    break;
                                case 40:
                                    if ((mutable_bitField0_ & 4) == 0) {
                                        this.valueInt64_ = newLongList();
                                        mutable_bitField0_ |= 4;
                                    }
                                    this.valueInt64_.addLong(input.readInt64());
                                    break;
                                case 42:
                                    int length2 = input.readRawVarint32();
                                    int limit2 = input.pushLimit(length2);
                                    if ((mutable_bitField0_ & 4) == 0 && input.getBytesUntilLimit() > 0) {
                                        this.valueInt64_ = newLongList();
                                        mutable_bitField0_ |= 4;
                                    }
                                    while (input.getBytesUntilLimit() > 0) {
                                        this.valueInt64_.addLong(input.readInt64());
                                    }
                                    input.popLimit(limit2);
                                    break;
                                case 49:
                                    if ((mutable_bitField0_ & 8) == 0) {
                                        this.valueDouble_ = newDoubleList();
                                        mutable_bitField0_ |= 8;
                                    }
                                    this.valueDouble_.addDouble(input.readDouble());
                                    break;
                                case 50:
                                    int length3 = input.readRawVarint32();
                                    int limit3 = input.pushLimit(length3);
                                    if ((mutable_bitField0_ & 8) == 0 && input.getBytesUntilLimit() > 0) {
                                        this.valueDouble_ = newDoubleList();
                                        mutable_bitField0_ |= 8;
                                    }
                                    while (input.getBytesUntilLimit() > 0) {
                                        this.valueDouble_.addDouble(input.readDouble());
                                    }
                                    input.popLimit(limit3);
                                    break;
                                case 58:
                                    int length4 = input.readRawVarint32();
                                    int limit4 = input.pushLimit(length4);
                                    if ((mutable_bitField0_ & 16) == 0 && input.getBytesUntilLimit() > 0) {
                                        this.valueFloat_ = newFloatList();
                                        mutable_bitField0_ |= 16;
                                    }
                                    while (input.getBytesUntilLimit() > 0) {
                                        this.valueFloat_.addFloat(input.readFloat());
                                    }
                                    input.popLimit(limit4);
                                    break;
                                case 61:
                                    if ((mutable_bitField0_ & 16) == 0) {
                                        this.valueFloat_ = newFloatList();
                                        mutable_bitField0_ |= 16;
                                    }
                                    this.valueFloat_.addFloat(input.readFloat());
                                    break;
                                default:
                                    if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                        done = true;
                                        break;
                                    } else {
                                        break;
                                    }
                            }
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        }
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                if ((mutable_bitField0_ & 1) != 0) {
                    this.valueString_ = this.valueString_.getUnmodifiableView();
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.valueInt32_.makeImmutable();
                }
                if ((mutable_bitField0_ & 4) != 0) {
                    this.valueInt64_.makeImmutable();
                }
                if ((mutable_bitField0_ & 8) != 0) {
                    this.valueDouble_.makeImmutable();
                }
                if ((mutable_bitField0_ & 16) != 0) {
                    this.valueFloat_.makeImmutable();
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_AttributeArgumentDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_AttributeArgumentDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(AttributeArgumentDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public TypeDefinition getType() {
            return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
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

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public ProtocolStringList getValueStringList() {
            return this.valueString_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueStringCount() {
            return this.valueString_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public String getValueString(int index) {
            return (String) this.valueString_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public ByteString getValueStringBytes(int index) {
            return this.valueString_.getByteString(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public List<Integer> getValueInt32List() {
            return this.valueInt32_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueInt32Count() {
            return this.valueInt32_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueInt32(int index) {
            return this.valueInt32_.getInt(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public List<Long> getValueInt64List() {
            return this.valueInt64_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueInt64Count() {
            return this.valueInt64_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public long getValueInt64(int index) {
            return this.valueInt64_.getLong(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public List<Double> getValueDoubleList() {
            return this.valueDouble_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueDoubleCount() {
            return this.valueDouble_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public double getValueDouble(int index) {
            return this.valueDouble_.getDouble(index);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public List<Float> getValueFloatList() {
            return this.valueFloat_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public int getValueFloatCount() {
            return this.valueFloat_.size();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
        public float getValueFloat(int index) {
            return this.valueFloat_.getFloat(index);
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
            getSerializedSize();
            if (this.type_ != null) {
                output.writeMessage(1, getType());
            }
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.name_);
            }
            for (int i = 0; i < this.valueString_.size(); i++) {
                GeneratedMessageV3.writeString(output, 3, this.valueString_.getRaw(i));
            }
            if (getValueInt32List().size() > 0) {
                output.writeUInt32NoTag(34);
                output.writeUInt32NoTag(this.valueInt32MemoizedSerializedSize);
            }
            for (int i2 = 0; i2 < this.valueInt32_.size(); i2++) {
                output.writeInt32NoTag(this.valueInt32_.getInt(i2));
            }
            if (getValueInt64List().size() > 0) {
                output.writeUInt32NoTag(42);
                output.writeUInt32NoTag(this.valueInt64MemoizedSerializedSize);
            }
            for (int i3 = 0; i3 < this.valueInt64_.size(); i3++) {
                output.writeInt64NoTag(this.valueInt64_.getLong(i3));
            }
            if (getValueDoubleList().size() > 0) {
                output.writeUInt32NoTag(50);
                output.writeUInt32NoTag(this.valueDoubleMemoizedSerializedSize);
            }
            for (int i4 = 0; i4 < this.valueDouble_.size(); i4++) {
                output.writeDoubleNoTag(this.valueDouble_.getDouble(i4));
            }
            if (getValueFloatList().size() > 0) {
                output.writeUInt32NoTag(58);
                output.writeUInt32NoTag(this.valueFloatMemoizedSerializedSize);
            }
            for (int i5 = 0; i5 < this.valueFloat_.size(); i5++) {
                output.writeFloatNoTag(this.valueFloat_.getFloat(i5));
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.type_ != null) {
                size2 = 0 + CodedOutputStream.computeMessageSize(1, getType());
            }
            if (!getNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.name_);
            }
            int dataSize = 0;
            for (int i = 0; i < this.valueString_.size(); i++) {
                dataSize += computeStringSizeNoTag(this.valueString_.getRaw(i));
            }
            int size3 = size2 + dataSize + (1 * getValueStringList().size());
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.valueInt32_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.valueInt32_.getInt(i2));
            }
            int size4 = size3 + dataSize2;
            if (!getValueInt32List().isEmpty()) {
                size4 = size4 + 1 + CodedOutputStream.computeInt32SizeNoTag(dataSize2);
            }
            this.valueInt32MemoizedSerializedSize = dataSize2;
            int dataSize3 = 0;
            for (int i3 = 0; i3 < this.valueInt64_.size(); i3++) {
                dataSize3 += CodedOutputStream.computeInt64SizeNoTag(this.valueInt64_.getLong(i3));
            }
            int size5 = size4 + dataSize3;
            if (!getValueInt64List().isEmpty()) {
                size5 = size5 + 1 + CodedOutputStream.computeInt32SizeNoTag(dataSize3);
            }
            this.valueInt64MemoizedSerializedSize = dataSize3;
            int dataSize4 = 8 * getValueDoubleList().size();
            int size6 = size5 + dataSize4;
            if (!getValueDoubleList().isEmpty()) {
                size6 = size6 + 1 + CodedOutputStream.computeInt32SizeNoTag(dataSize4);
            }
            this.valueDoubleMemoizedSerializedSize = dataSize4;
            int dataSize5 = 4 * getValueFloatList().size();
            int size7 = size6 + dataSize5;
            if (!getValueFloatList().isEmpty()) {
                size7 = size7 + 1 + CodedOutputStream.computeInt32SizeNoTag(dataSize5);
            }
            this.valueFloatMemoizedSerializedSize = dataSize5;
            int size8 = size7 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size8;
            return size8;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof AttributeArgumentDefinition)) {
                return super.equals(obj);
            }
            AttributeArgumentDefinition other = (AttributeArgumentDefinition) obj;
            if (hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || !getName().equals(other.getName()) || !getValueStringList().equals(other.getValueStringList()) || !getValueInt32List().equals(other.getValueInt32List()) || !getValueInt64List().equals(other.getValueInt64List()) || !getValueDoubleList().equals(other.getValueDoubleList()) || !getValueFloatList().equals(other.getValueFloatList()) || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            if (hasType()) {
                hash = (53 * ((37 * hash) + 1)) + getType().hashCode();
            }
            int hash2 = (53 * ((37 * hash) + 2)) + getName().hashCode();
            if (getValueStringCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 3)) + getValueStringList().hashCode();
            }
            if (getValueInt32Count() > 0) {
                hash2 = (53 * ((37 * hash2) + 4)) + getValueInt32List().hashCode();
            }
            if (getValueInt64Count() > 0) {
                hash2 = (53 * ((37 * hash2) + 5)) + getValueInt64List().hashCode();
            }
            if (getValueDoubleCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 6)) + getValueDoubleList().hashCode();
            }
            if (getValueFloatCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 7)) + getValueFloatList().hashCode();
            }
            int hash3 = (29 * hash2) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash3;
            return hash3;
        }

        public static AttributeArgumentDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeArgumentDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeArgumentDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeArgumentDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeArgumentDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AttributeArgumentDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AttributeArgumentDefinition parseFrom(InputStream input) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AttributeArgumentDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static AttributeArgumentDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static AttributeArgumentDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static AttributeArgumentDefinition parseFrom(CodedInputStream input) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AttributeArgumentDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AttributeArgumentDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AttributeArgumentDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$AttributeArgumentDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AttributeArgumentDefinitionOrBuilder {
            private int bitField0_;
            private TypeDefinition type_;
            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> typeBuilder_;
            private Object name_;
            private LazyStringList valueString_;
            private Internal.IntList valueInt32_;
            private Internal.LongList valueInt64_;
            private Internal.DoubleList valueDouble_;
            private Internal.FloatList valueFloat_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_AttributeArgumentDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_AttributeArgumentDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(AttributeArgumentDefinition.class, Builder.class);
            }

            private Builder() {
                this.name_ = "";
                this.valueString_ = LazyStringArrayList.EMPTY;
                this.valueInt32_ = AttributeArgumentDefinition.emptyIntList();
                this.valueInt64_ = AttributeArgumentDefinition.emptyLongList();
                this.valueDouble_ = AttributeArgumentDefinition.emptyDoubleList();
                this.valueFloat_ = AttributeArgumentDefinition.emptyFloatList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.name_ = "";
                this.valueString_ = LazyStringArrayList.EMPTY;
                this.valueInt32_ = AttributeArgumentDefinition.emptyIntList();
                this.valueInt64_ = AttributeArgumentDefinition.emptyLongList();
                this.valueDouble_ = AttributeArgumentDefinition.emptyDoubleList();
                this.valueFloat_ = AttributeArgumentDefinition.emptyFloatList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = AttributeArgumentDefinition.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                this.name_ = "";
                this.valueString_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                this.valueInt32_ = AttributeArgumentDefinition.emptyIntList();
                this.bitField0_ &= -3;
                this.valueInt64_ = AttributeArgumentDefinition.emptyLongList();
                this.bitField0_ &= -5;
                this.valueDouble_ = AttributeArgumentDefinition.emptyDoubleList();
                this.bitField0_ &= -9;
                this.valueFloat_ = AttributeArgumentDefinition.emptyFloatList();
                this.bitField0_ &= -17;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_AttributeArgumentDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public AttributeArgumentDefinition getDefaultInstanceForType() {
                return AttributeArgumentDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AttributeArgumentDefinition build() {
                AttributeArgumentDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AttributeArgumentDefinition buildPartial() {
                AttributeArgumentDefinition result = new AttributeArgumentDefinition(this, (AttributeArgumentDefinition) null);
                int i = this.bitField0_;
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                result.name_ = this.name_;
                if ((this.bitField0_ & 1) != 0) {
                    this.valueString_ = this.valueString_.getUnmodifiableView();
                    this.bitField0_ &= -2;
                }
                result.valueString_ = this.valueString_;
                if ((this.bitField0_ & 2) != 0) {
                    this.valueInt32_.makeImmutable();
                    this.bitField0_ &= -3;
                }
                result.valueInt32_ = this.valueInt32_;
                if ((this.bitField0_ & 4) != 0) {
                    this.valueInt64_.makeImmutable();
                    this.bitField0_ &= -5;
                }
                result.valueInt64_ = this.valueInt64_;
                if ((this.bitField0_ & 8) != 0) {
                    this.valueDouble_.makeImmutable();
                    this.bitField0_ &= -9;
                }
                result.valueDouble_ = this.valueDouble_;
                if ((this.bitField0_ & 16) != 0) {
                    this.valueFloat_.makeImmutable();
                    this.bitField0_ &= -17;
                }
                result.valueFloat_ = this.valueFloat_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof AttributeArgumentDefinition) {
                    return mergeFrom((AttributeArgumentDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AttributeArgumentDefinition other) {
                if (other == AttributeArgumentDefinition.getDefaultInstance()) {
                    return this;
                }
                if (other.hasType()) {
                    mergeType(other.getType());
                }
                if (!other.getName().isEmpty()) {
                    this.name_ = other.name_;
                    onChanged();
                }
                if (!other.valueString_.isEmpty()) {
                    if (this.valueString_.isEmpty()) {
                        this.valueString_ = other.valueString_;
                        this.bitField0_ &= -2;
                    } else {
                        ensureValueStringIsMutable();
                        this.valueString_.addAll(other.valueString_);
                    }
                    onChanged();
                }
                if (!other.valueInt32_.isEmpty()) {
                    if (this.valueInt32_.isEmpty()) {
                        this.valueInt32_ = other.valueInt32_;
                        this.bitField0_ &= -3;
                    } else {
                        ensureValueInt32IsMutable();
                        this.valueInt32_.addAll(other.valueInt32_);
                    }
                    onChanged();
                }
                if (!other.valueInt64_.isEmpty()) {
                    if (this.valueInt64_.isEmpty()) {
                        this.valueInt64_ = other.valueInt64_;
                        this.bitField0_ &= -5;
                    } else {
                        ensureValueInt64IsMutable();
                        this.valueInt64_.addAll(other.valueInt64_);
                    }
                    onChanged();
                }
                if (!other.valueDouble_.isEmpty()) {
                    if (this.valueDouble_.isEmpty()) {
                        this.valueDouble_ = other.valueDouble_;
                        this.bitField0_ &= -9;
                    } else {
                        ensureValueDoubleIsMutable();
                        this.valueDouble_.addAll(other.valueDouble_);
                    }
                    onChanged();
                }
                if (!other.valueFloat_.isEmpty()) {
                    if (this.valueFloat_.isEmpty()) {
                        this.valueFloat_ = other.valueFloat_;
                        this.bitField0_ &= -17;
                    } else {
                        ensureValueFloatIsMutable();
                        this.valueFloat_.addAll(other.valueFloat_);
                    }
                    onChanged();
                }
                mergeUnknownFields(other.unknownFields);
                onChanged();
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                AttributeArgumentDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (AttributeArgumentDefinition) AttributeArgumentDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        AttributeArgumentDefinition attributeArgumentDefinition = (AttributeArgumentDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.type_ = value;
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setType(TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
                    } else {
                        this.type_ = value;
                    }
                    onChanged();
                } else {
                    this.typeBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearType() {
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                    onChanged();
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                return this;
            }

            public TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<TypeDefinition, TypeDefinition.Builder, TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
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

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
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
                this.name_ = AttributeArgumentDefinition.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    AttributeArgumentDefinition.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            private void ensureValueStringIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.valueString_ = new LazyStringArrayList(this.valueString_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public ProtocolStringList getValueStringList() {
                return this.valueString_.getUnmodifiableView();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueStringCount() {
                return this.valueString_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public String getValueString(int index) {
                return (String) this.valueString_.get(index);
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public ByteString getValueStringBytes(int index) {
                return this.valueString_.getByteString(index);
            }

            public Builder setValueString(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueStringIsMutable();
                this.valueString_.set(index, value);
                onChanged();
                return this;
            }

            public Builder addValueString(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueStringIsMutable();
                this.valueString_.add(value);
                onChanged();
                return this;
            }

            public Builder addAllValueString(Iterable<String> values) {
                ensureValueStringIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.valueString_);
                onChanged();
                return this;
            }

            public Builder clearValueString() {
                this.valueString_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -2;
                onChanged();
                return this;
            }

            public Builder addValueStringBytes(ByteString value) {
                if (value != null) {
                    AttributeArgumentDefinition.checkByteStringIsUtf8(value);
                    ensureValueStringIsMutable();
                    this.valueString_.add(value);
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            private void ensureValueInt32IsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.valueInt32_ = AttributeArgumentDefinition.mutableCopy(this.valueInt32_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public List<Integer> getValueInt32List() {
                return (this.bitField0_ & 2) != 0 ? Collections.unmodifiableList(this.valueInt32_) : this.valueInt32_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueInt32Count() {
                return this.valueInt32_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueInt32(int index) {
                return this.valueInt32_.getInt(index);
            }

            public Builder setValueInt32(int index, int value) {
                ensureValueInt32IsMutable();
                this.valueInt32_.setInt(index, value);
                onChanged();
                return this;
            }

            public Builder addValueInt32(int value) {
                ensureValueInt32IsMutable();
                this.valueInt32_.addInt(value);
                onChanged();
                return this;
            }

            public Builder addAllValueInt32(Iterable<? extends Integer> values) {
                ensureValueInt32IsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.valueInt32_);
                onChanged();
                return this;
            }

            public Builder clearValueInt32() {
                this.valueInt32_ = AttributeArgumentDefinition.emptyIntList();
                this.bitField0_ &= -3;
                onChanged();
                return this;
            }

            private void ensureValueInt64IsMutable() {
                if ((this.bitField0_ & 4) == 0) {
                    this.valueInt64_ = AttributeArgumentDefinition.mutableCopy(this.valueInt64_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public List<Long> getValueInt64List() {
                return (this.bitField0_ & 4) != 0 ? Collections.unmodifiableList(this.valueInt64_) : this.valueInt64_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueInt64Count() {
                return this.valueInt64_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public long getValueInt64(int index) {
                return this.valueInt64_.getLong(index);
            }

            public Builder setValueInt64(int index, long value) {
                ensureValueInt64IsMutable();
                this.valueInt64_.setLong(index, value);
                onChanged();
                return this;
            }

            public Builder addValueInt64(long value) {
                ensureValueInt64IsMutable();
                this.valueInt64_.addLong(value);
                onChanged();
                return this;
            }

            public Builder addAllValueInt64(Iterable<? extends Long> values) {
                ensureValueInt64IsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.valueInt64_);
                onChanged();
                return this;
            }

            public Builder clearValueInt64() {
                this.valueInt64_ = AttributeArgumentDefinition.emptyLongList();
                this.bitField0_ &= -5;
                onChanged();
                return this;
            }

            private void ensureValueDoubleIsMutable() {
                if ((this.bitField0_ & 8) == 0) {
                    this.valueDouble_ = AttributeArgumentDefinition.mutableCopy(this.valueDouble_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public List<Double> getValueDoubleList() {
                return (this.bitField0_ & 8) != 0 ? Collections.unmodifiableList(this.valueDouble_) : this.valueDouble_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueDoubleCount() {
                return this.valueDouble_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public double getValueDouble(int index) {
                return this.valueDouble_.getDouble(index);
            }

            public Builder setValueDouble(int index, double value) {
                ensureValueDoubleIsMutable();
                this.valueDouble_.setDouble(index, value);
                onChanged();
                return this;
            }

            public Builder addValueDouble(double value) {
                ensureValueDoubleIsMutable();
                this.valueDouble_.addDouble(value);
                onChanged();
                return this;
            }

            public Builder addAllValueDouble(Iterable<? extends Double> values) {
                ensureValueDoubleIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.valueDouble_);
                onChanged();
                return this;
            }

            public Builder clearValueDouble() {
                this.valueDouble_ = AttributeArgumentDefinition.emptyDoubleList();
                this.bitField0_ &= -9;
                onChanged();
                return this;
            }

            private void ensureValueFloatIsMutable() {
                if ((this.bitField0_ & 16) == 0) {
                    this.valueFloat_ = AttributeArgumentDefinition.mutableCopy(this.valueFloat_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public List<Float> getValueFloatList() {
                return (this.bitField0_ & 16) != 0 ? Collections.unmodifiableList(this.valueFloat_) : this.valueFloat_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public int getValueFloatCount() {
                return this.valueFloat_.size();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.AttributeArgumentDefinitionOrBuilder
            public float getValueFloat(int index) {
                return this.valueFloat_.getFloat(index);
            }

            public Builder setValueFloat(int index, float value) {
                ensureValueFloatIsMutable();
                this.valueFloat_.setFloat(index, value);
                onChanged();
                return this;
            }

            public Builder addValueFloat(float value) {
                ensureValueFloatIsMutable();
                this.valueFloat_.addFloat(value);
                onChanged();
                return this;
            }

            public Builder addAllValueFloat(Iterable<? extends Float> values) {
                ensureValueFloatIsMutable();
                AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.valueFloat_);
                onChanged();
                return this;
            }

            public Builder clearValueFloat() {
                this.valueFloat_ = AttributeArgumentDefinition.emptyFloatList();
                this.bitField0_ &= -17;
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

        public static AttributeArgumentDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AttributeArgumentDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<AttributeArgumentDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public AttributeArgumentDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$EventDefinition.class */
    public static final class EventDefinition extends GeneratedMessageV3 implements EventDefinitionOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ACCESSIBILITY_FIELD_NUMBER = 1;
        private int accessibility_;
        public static final int ADD_ACCESSOR_METHOD_FIELD_NUMBER = 2;
        private MethodDefinition addAccessorMethod_;
        public static final int CAN_ADD_FIELD_NUMBER = 3;
        private boolean canAdd_;
        public static final int CAN_INVOKE_FIELD_NUMBER = 4;
        private boolean canInvoke_;
        public static final int CAN_REMOVE_FIELD_NUMBER = 5;
        private boolean canRemove_;
        public static final int FULL_NAME_FIELD_NUMBER = 6;
        private volatile Object fullName_;
        public static final int INVOKE_ACCESSOR_METHOD_FIELD_NUMBER = 7;
        private MethodDefinition invokeAccessorMethod_;
        public static final int NAME_FIELD_NUMBER = 8;
        private volatile Object name_;
        public static final int REMOVE_ACCESSOR_METHOD_FIELD_NUMBER = 9;
        private MethodDefinition removeAccessorMethod_;
        public static final int PE_TOKEN_FIELD_NUMBER = 10;
        private int peToken_;
        private byte memoizedIsInitialized;
        private static final EventDefinition DEFAULT_INSTANCE = new EventDefinition();
        private static final Parser<EventDefinition> PARSER = new AbstractParser<EventDefinition>() { // from class: soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinition.1
            @Override // com.google.protobuf.Parser
            public EventDefinition parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new EventDefinition(input, extensionRegistry, null);
            }
        };

        /* synthetic */ EventDefinition(GeneratedMessageV3.Builder builder, EventDefinition eventDefinition) {
            this(builder);
        }

        private EventDefinition(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private EventDefinition() {
            this.memoizedIsInitialized = (byte) -1;
            this.accessibility_ = 0;
            this.fullName_ = "";
            this.name_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new EventDefinition();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ EventDefinition(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, EventDefinition eventDefinition) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private EventDefinition(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this();
            if (extensionRegistry == null) {
                throw new NullPointerException();
            }
            UnknownFieldSet.Builder unknownFields = UnknownFieldSet.newBuilder();
            try {
                boolean done = false;
                while (!done) {
                    try {
                        int tag = input.readTag();
                        switch (tag) {
                            case 0:
                                done = true;
                                break;
                            case 8:
                                int rawValue = input.readEnum();
                                this.accessibility_ = rawValue;
                                break;
                            case 18:
                                MethodDefinition.Builder subBuilder = null;
                                subBuilder = this.addAccessorMethod_ != null ? this.addAccessorMethod_.toBuilder() : subBuilder;
                                this.addAccessorMethod_ = (MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.addAccessorMethod_);
                                    this.addAccessorMethod_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 24:
                                this.canAdd_ = input.readBool();
                                break;
                            case 32:
                                this.canInvoke_ = input.readBool();
                                break;
                            case 40:
                                this.canRemove_ = input.readBool();
                                break;
                            case 50:
                                String s = input.readStringRequireUtf8();
                                this.fullName_ = s;
                                break;
                            case 58:
                                MethodDefinition.Builder subBuilder2 = null;
                                subBuilder2 = this.invokeAccessorMethod_ != null ? this.invokeAccessorMethod_.toBuilder() : subBuilder2;
                                this.invokeAccessorMethod_ = (MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.invokeAccessorMethod_);
                                    this.invokeAccessorMethod_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 66:
                                String s2 = input.readStringRequireUtf8();
                                this.name_ = s2;
                                break;
                            case 74:
                                MethodDefinition.Builder subBuilder3 = null;
                                subBuilder3 = this.removeAccessorMethod_ != null ? this.removeAccessorMethod_.toBuilder() : subBuilder3;
                                this.removeAccessorMethod_ = (MethodDefinition) input.readMessage(MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder3 == null) {
                                    break;
                                } else {
                                    subBuilder3.mergeFrom(this.removeAccessorMethod_);
                                    this.removeAccessorMethod_ = subBuilder3.buildPartial();
                                    break;
                                }
                            case 80:
                                this.peToken_ = input.readInt32();
                                break;
                            default:
                                if (!parseUnknownField(input, unknownFields, extensionRegistry, tag)) {
                                    done = true;
                                    break;
                                } else {
                                    break;
                                }
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw e.setUnfinishedMessage(this);
                    } catch (IOException e2) {
                        throw new InvalidProtocolBufferException(e2).setUnfinishedMessage(this);
                    }
                }
            } finally {
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoAssemblyAllTypes.internal_static_EventDefinition_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoAssemblyAllTypes.internal_static_EventDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(EventDefinition.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public int getAccessibilityValue() {
            return this.accessibility_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public Accessibility getAccessibility() {
            Accessibility result = Accessibility.valueOf(this.accessibility_);
            return result == null ? Accessibility.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean hasAddAccessorMethod() {
            return this.addAccessorMethod_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinition getAddAccessorMethod() {
            return this.addAccessorMethod_ == null ? MethodDefinition.getDefaultInstance() : this.addAccessorMethod_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinitionOrBuilder getAddAccessorMethodOrBuilder() {
            return getAddAccessorMethod();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean getCanAdd() {
            return this.canAdd_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean getCanInvoke() {
            return this.canInvoke_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean getCanRemove() {
            return this.canRemove_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public String getFullName() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.fullName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public ByteString getFullNameBytes() {
            Object ref = this.fullName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.fullName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean hasInvokeAccessorMethod() {
            return this.invokeAccessorMethod_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinition getInvokeAccessorMethod() {
            return this.invokeAccessorMethod_ == null ? MethodDefinition.getDefaultInstance() : this.invokeAccessorMethod_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinitionOrBuilder getInvokeAccessorMethodOrBuilder() {
            return getInvokeAccessorMethod();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
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

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public boolean hasRemoveAccessorMethod() {
            return this.removeAccessorMethod_ != null;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinition getRemoveAccessorMethod() {
            return this.removeAccessorMethod_ == null ? MethodDefinition.getDefaultInstance() : this.removeAccessorMethod_;
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public MethodDefinitionOrBuilder getRemoveAccessorMethodOrBuilder() {
            return getRemoveAccessorMethod();
        }

        @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
        public int getPeToken() {
            return this.peToken_;
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
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                output.writeEnum(1, this.accessibility_);
            }
            if (this.addAccessorMethod_ != null) {
                output.writeMessage(2, getAddAccessorMethod());
            }
            if (this.canAdd_) {
                output.writeBool(3, this.canAdd_);
            }
            if (this.canInvoke_) {
                output.writeBool(4, this.canInvoke_);
            }
            if (this.canRemove_) {
                output.writeBool(5, this.canRemove_);
            }
            if (!getFullNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 6, this.fullName_);
            }
            if (this.invokeAccessorMethod_ != null) {
                output.writeMessage(7, getInvokeAccessorMethod());
            }
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 8, this.name_);
            }
            if (this.removeAccessorMethod_ != null) {
                output.writeMessage(9, getRemoveAccessorMethod());
            }
            if (this.peToken_ != 0) {
                output.writeInt32(10, this.peToken_);
            }
            this.unknownFields.writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if (this.accessibility_ != Accessibility.NONE.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.accessibility_);
            }
            if (this.addAccessorMethod_ != null) {
                size2 += CodedOutputStream.computeMessageSize(2, getAddAccessorMethod());
            }
            if (this.canAdd_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.canAdd_);
            }
            if (this.canInvoke_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.canInvoke_);
            }
            if (this.canRemove_) {
                size2 += CodedOutputStream.computeBoolSize(5, this.canRemove_);
            }
            if (!getFullNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(6, this.fullName_);
            }
            if (this.invokeAccessorMethod_ != null) {
                size2 += CodedOutputStream.computeMessageSize(7, getInvokeAccessorMethod());
            }
            if (!getNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(8, this.name_);
            }
            if (this.removeAccessorMethod_ != null) {
                size2 += CodedOutputStream.computeMessageSize(9, getRemoveAccessorMethod());
            }
            if (this.peToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(10, this.peToken_);
            }
            int size3 = size2 + this.unknownFields.getSerializedSize();
            this.memoizedSize = size3;
            return size3;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof EventDefinition)) {
                return super.equals(obj);
            }
            EventDefinition other = (EventDefinition) obj;
            if (this.accessibility_ != other.accessibility_ || hasAddAccessorMethod() != other.hasAddAccessorMethod()) {
                return false;
            }
            if ((hasAddAccessorMethod() && !getAddAccessorMethod().equals(other.getAddAccessorMethod())) || getCanAdd() != other.getCanAdd() || getCanInvoke() != other.getCanInvoke() || getCanRemove() != other.getCanRemove() || !getFullName().equals(other.getFullName()) || hasInvokeAccessorMethod() != other.hasInvokeAccessorMethod()) {
                return false;
            }
            if ((hasInvokeAccessorMethod() && !getInvokeAccessorMethod().equals(other.getInvokeAccessorMethod())) || !getName().equals(other.getName()) || hasRemoveAccessorMethod() != other.hasRemoveAccessorMethod()) {
                return false;
            }
            if ((hasRemoveAccessorMethod() && !getRemoveAccessorMethod().equals(other.getRemoveAccessorMethod())) || getPeToken() != other.getPeToken() || !this.unknownFields.equals(other.unknownFields)) {
                return false;
            }
            return true;
        }

        @Override // com.google.protobuf.AbstractMessage, com.google.protobuf.Message
        public int hashCode() {
            if (this.memoizedHashCode != 0) {
                return this.memoizedHashCode;
            }
            int hash = (19 * 41) + getDescriptor().hashCode();
            int hash2 = (53 * ((37 * hash) + 1)) + this.accessibility_;
            if (hasAddAccessorMethod()) {
                hash2 = (53 * ((37 * hash2) + 2)) + getAddAccessorMethod().hashCode();
            }
            int hash3 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash2) + 3)) + Internal.hashBoolean(getCanAdd()))) + 4)) + Internal.hashBoolean(getCanInvoke()))) + 5)) + Internal.hashBoolean(getCanRemove()))) + 6)) + getFullName().hashCode();
            if (hasInvokeAccessorMethod()) {
                hash3 = (53 * ((37 * hash3) + 7)) + getInvokeAccessorMethod().hashCode();
            }
            int hash4 = (53 * ((37 * hash3) + 8)) + getName().hashCode();
            if (hasRemoveAccessorMethod()) {
                hash4 = (53 * ((37 * hash4) + 9)) + getRemoveAccessorMethod().hashCode();
            }
            int hash5 = (29 * ((53 * ((37 * hash4) + 10)) + getPeToken())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash5;
            return hash5;
        }

        public static EventDefinition parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static EventDefinition parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static EventDefinition parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static EventDefinition parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static EventDefinition parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static EventDefinition parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static EventDefinition parseFrom(InputStream input) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static EventDefinition parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static EventDefinition parseDelimitedFrom(InputStream input) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static EventDefinition parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static EventDefinition parseFrom(CodedInputStream input) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static EventDefinition parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (EventDefinition) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(EventDefinition prototype) {
            return DEFAULT_INSTANCE.toBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return this == DEFAULT_INSTANCE ? new Builder((Builder) null) : new Builder((Builder) null).mergeFrom(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Builder newBuilderForType(GeneratedMessageV3.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoAssemblyAllTypes$EventDefinition$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements EventDefinitionOrBuilder {
            private int accessibility_;
            private MethodDefinition addAccessorMethod_;
            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> addAccessorMethodBuilder_;
            private boolean canAdd_;
            private boolean canInvoke_;
            private boolean canRemove_;
            private Object fullName_;
            private MethodDefinition invokeAccessorMethod_;
            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> invokeAccessorMethodBuilder_;
            private Object name_;
            private MethodDefinition removeAccessorMethod_;
            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> removeAccessorMethodBuilder_;
            private int peToken_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoAssemblyAllTypes.internal_static_EventDefinition_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoAssemblyAllTypes.internal_static_EventDefinition_fieldAccessorTable.ensureFieldAccessorsInitialized(EventDefinition.class, Builder.class);
            }

            private Builder() {
                this.accessibility_ = 0;
                this.fullName_ = "";
                this.name_ = "";
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(Builder builder) {
                this();
            }

            /* synthetic */ Builder(GeneratedMessageV3.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessageV3.BuilderParent parent) {
                super(parent);
                this.accessibility_ = 0;
                this.fullName_ = "";
                this.name_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = EventDefinition.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.accessibility_ = 0;
                if (this.addAccessorMethodBuilder_ == null) {
                    this.addAccessorMethod_ = null;
                } else {
                    this.addAccessorMethod_ = null;
                    this.addAccessorMethodBuilder_ = null;
                }
                this.canAdd_ = false;
                this.canInvoke_ = false;
                this.canRemove_ = false;
                this.fullName_ = "";
                if (this.invokeAccessorMethodBuilder_ == null) {
                    this.invokeAccessorMethod_ = null;
                } else {
                    this.invokeAccessorMethod_ = null;
                    this.invokeAccessorMethodBuilder_ = null;
                }
                this.name_ = "";
                if (this.removeAccessorMethodBuilder_ == null) {
                    this.removeAccessorMethod_ = null;
                } else {
                    this.removeAccessorMethod_ = null;
                    this.removeAccessorMethodBuilder_ = null;
                }
                this.peToken_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoAssemblyAllTypes.internal_static_EventDefinition_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public EventDefinition getDefaultInstanceForType() {
                return EventDefinition.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public EventDefinition build() {
                EventDefinition result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public EventDefinition buildPartial() {
                EventDefinition result = new EventDefinition(this, (EventDefinition) null);
                result.accessibility_ = this.accessibility_;
                if (this.addAccessorMethodBuilder_ == null) {
                    result.addAccessorMethod_ = this.addAccessorMethod_;
                } else {
                    result.addAccessorMethod_ = this.addAccessorMethodBuilder_.build();
                }
                result.canAdd_ = this.canAdd_;
                result.canInvoke_ = this.canInvoke_;
                result.canRemove_ = this.canRemove_;
                result.fullName_ = this.fullName_;
                if (this.invokeAccessorMethodBuilder_ == null) {
                    result.invokeAccessorMethod_ = this.invokeAccessorMethod_;
                } else {
                    result.invokeAccessorMethod_ = this.invokeAccessorMethodBuilder_.build();
                }
                result.name_ = this.name_;
                if (this.removeAccessorMethodBuilder_ == null) {
                    result.removeAccessorMethod_ = this.removeAccessorMethod_;
                } else {
                    result.removeAccessorMethod_ = this.removeAccessorMethodBuilder_.build();
                }
                result.peToken_ = this.peToken_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
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
                if (other instanceof EventDefinition) {
                    return mergeFrom((EventDefinition) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(EventDefinition other) {
                if (other != EventDefinition.getDefaultInstance()) {
                    if (other.accessibility_ != 0) {
                        setAccessibilityValue(other.getAccessibilityValue());
                    }
                    if (other.hasAddAccessorMethod()) {
                        mergeAddAccessorMethod(other.getAddAccessorMethod());
                    }
                    if (other.getCanAdd()) {
                        setCanAdd(other.getCanAdd());
                    }
                    if (other.getCanInvoke()) {
                        setCanInvoke(other.getCanInvoke());
                    }
                    if (other.getCanRemove()) {
                        setCanRemove(other.getCanRemove());
                    }
                    if (!other.getFullName().isEmpty()) {
                        this.fullName_ = other.fullName_;
                        onChanged();
                    }
                    if (other.hasInvokeAccessorMethod()) {
                        mergeInvokeAccessorMethod(other.getInvokeAccessorMethod());
                    }
                    if (!other.getName().isEmpty()) {
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (other.hasRemoveAccessorMethod()) {
                        mergeRemoveAccessorMethod(other.getRemoveAccessorMethod());
                    }
                    if (other.getPeToken() != 0) {
                        setPeToken(other.getPeToken());
                    }
                    mergeUnknownFields(other.unknownFields);
                    onChanged();
                    return this;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                EventDefinition parsedMessage = null;
                try {
                    try {
                        parsedMessage = (EventDefinition) EventDefinition.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        EventDefinition eventDefinition = (EventDefinition) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public int getAccessibilityValue() {
                return this.accessibility_;
            }

            public Builder setAccessibilityValue(int value) {
                this.accessibility_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public Accessibility getAccessibility() {
                Accessibility result = Accessibility.valueOf(this.accessibility_);
                return result == null ? Accessibility.UNRECOGNIZED : result;
            }

            public Builder setAccessibility(Accessibility value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.accessibility_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAccessibility() {
                this.accessibility_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean hasAddAccessorMethod() {
                return (this.addAccessorMethodBuilder_ == null && this.addAccessorMethod_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinition getAddAccessorMethod() {
                if (this.addAccessorMethodBuilder_ == null) {
                    return this.addAccessorMethod_ == null ? MethodDefinition.getDefaultInstance() : this.addAccessorMethod_;
                }
                return this.addAccessorMethodBuilder_.getMessage();
            }

            public Builder setAddAccessorMethod(MethodDefinition value) {
                if (this.addAccessorMethodBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.addAccessorMethod_ = value;
                    onChanged();
                } else {
                    this.addAccessorMethodBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setAddAccessorMethod(MethodDefinition.Builder builderForValue) {
                if (this.addAccessorMethodBuilder_ == null) {
                    this.addAccessorMethod_ = builderForValue.build();
                    onChanged();
                } else {
                    this.addAccessorMethodBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeAddAccessorMethod(MethodDefinition value) {
                if (this.addAccessorMethodBuilder_ == null) {
                    if (this.addAccessorMethod_ != null) {
                        this.addAccessorMethod_ = MethodDefinition.newBuilder(this.addAccessorMethod_).mergeFrom(value).buildPartial();
                    } else {
                        this.addAccessorMethod_ = value;
                    }
                    onChanged();
                } else {
                    this.addAccessorMethodBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearAddAccessorMethod() {
                if (this.addAccessorMethodBuilder_ == null) {
                    this.addAccessorMethod_ = null;
                    onChanged();
                } else {
                    this.addAccessorMethod_ = null;
                    this.addAccessorMethodBuilder_ = null;
                }
                return this;
            }

            public MethodDefinition.Builder getAddAccessorMethodBuilder() {
                onChanged();
                return getAddAccessorMethodFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinitionOrBuilder getAddAccessorMethodOrBuilder() {
                if (this.addAccessorMethodBuilder_ != null) {
                    return this.addAccessorMethodBuilder_.getMessageOrBuilder();
                }
                return this.addAccessorMethod_ == null ? MethodDefinition.getDefaultInstance() : this.addAccessorMethod_;
            }

            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getAddAccessorMethodFieldBuilder() {
                if (this.addAccessorMethodBuilder_ == null) {
                    this.addAccessorMethodBuilder_ = new SingleFieldBuilderV3<>(getAddAccessorMethod(), getParentForChildren(), isClean());
                    this.addAccessorMethod_ = null;
                }
                return this.addAccessorMethodBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean getCanAdd() {
                return this.canAdd_;
            }

            public Builder setCanAdd(boolean value) {
                this.canAdd_ = value;
                onChanged();
                return this;
            }

            public Builder clearCanAdd() {
                this.canAdd_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean getCanInvoke() {
                return this.canInvoke_;
            }

            public Builder setCanInvoke(boolean value) {
                this.canInvoke_ = value;
                onChanged();
                return this;
            }

            public Builder clearCanInvoke() {
                this.canInvoke_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean getCanRemove() {
                return this.canRemove_;
            }

            public Builder setCanRemove(boolean value) {
                this.canRemove_ = value;
                onChanged();
                return this;
            }

            public Builder clearCanRemove() {
                this.canRemove_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public String getFullName() {
                Object ref = this.fullName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.fullName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public ByteString getFullNameBytes() {
                Object ref = this.fullName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.fullName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setFullName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.fullName_ = value;
                onChanged();
                return this;
            }

            public Builder clearFullName() {
                this.fullName_ = EventDefinition.getDefaultInstance().getFullName();
                onChanged();
                return this;
            }

            public Builder setFullNameBytes(ByteString value) {
                if (value != null) {
                    EventDefinition.checkByteStringIsUtf8(value);
                    this.fullName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean hasInvokeAccessorMethod() {
                return (this.invokeAccessorMethodBuilder_ == null && this.invokeAccessorMethod_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinition getInvokeAccessorMethod() {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    if (this.invokeAccessorMethod_ == null) {
                        return MethodDefinition.getDefaultInstance();
                    }
                    return this.invokeAccessorMethod_;
                }
                return this.invokeAccessorMethodBuilder_.getMessage();
            }

            public Builder setInvokeAccessorMethod(MethodDefinition value) {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.invokeAccessorMethod_ = value;
                    onChanged();
                } else {
                    this.invokeAccessorMethodBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setInvokeAccessorMethod(MethodDefinition.Builder builderForValue) {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    this.invokeAccessorMethod_ = builderForValue.build();
                    onChanged();
                } else {
                    this.invokeAccessorMethodBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeInvokeAccessorMethod(MethodDefinition value) {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    if (this.invokeAccessorMethod_ != null) {
                        this.invokeAccessorMethod_ = MethodDefinition.newBuilder(this.invokeAccessorMethod_).mergeFrom(value).buildPartial();
                    } else {
                        this.invokeAccessorMethod_ = value;
                    }
                    onChanged();
                } else {
                    this.invokeAccessorMethodBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearInvokeAccessorMethod() {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    this.invokeAccessorMethod_ = null;
                    onChanged();
                } else {
                    this.invokeAccessorMethod_ = null;
                    this.invokeAccessorMethodBuilder_ = null;
                }
                return this;
            }

            public MethodDefinition.Builder getInvokeAccessorMethodBuilder() {
                onChanged();
                return getInvokeAccessorMethodFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinitionOrBuilder getInvokeAccessorMethodOrBuilder() {
                if (this.invokeAccessorMethodBuilder_ != null) {
                    return this.invokeAccessorMethodBuilder_.getMessageOrBuilder();
                }
                if (this.invokeAccessorMethod_ == null) {
                    return MethodDefinition.getDefaultInstance();
                }
                return this.invokeAccessorMethod_;
            }

            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getInvokeAccessorMethodFieldBuilder() {
                if (this.invokeAccessorMethodBuilder_ == null) {
                    this.invokeAccessorMethodBuilder_ = new SingleFieldBuilderV3<>(getInvokeAccessorMethod(), getParentForChildren(), isClean());
                    this.invokeAccessorMethod_ = null;
                }
                return this.invokeAccessorMethodBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
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

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
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
                this.name_ = EventDefinition.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    EventDefinition.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public boolean hasRemoveAccessorMethod() {
                return (this.removeAccessorMethodBuilder_ == null && this.removeAccessorMethod_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinition getRemoveAccessorMethod() {
                if (this.removeAccessorMethodBuilder_ == null) {
                    if (this.removeAccessorMethod_ == null) {
                        return MethodDefinition.getDefaultInstance();
                    }
                    return this.removeAccessorMethod_;
                }
                return this.removeAccessorMethodBuilder_.getMessage();
            }

            public Builder setRemoveAccessorMethod(MethodDefinition value) {
                if (this.removeAccessorMethodBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.removeAccessorMethod_ = value;
                    onChanged();
                } else {
                    this.removeAccessorMethodBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setRemoveAccessorMethod(MethodDefinition.Builder builderForValue) {
                if (this.removeAccessorMethodBuilder_ == null) {
                    this.removeAccessorMethod_ = builderForValue.build();
                    onChanged();
                } else {
                    this.removeAccessorMethodBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeRemoveAccessorMethod(MethodDefinition value) {
                if (this.removeAccessorMethodBuilder_ == null) {
                    if (this.removeAccessorMethod_ != null) {
                        this.removeAccessorMethod_ = MethodDefinition.newBuilder(this.removeAccessorMethod_).mergeFrom(value).buildPartial();
                    } else {
                        this.removeAccessorMethod_ = value;
                    }
                    onChanged();
                } else {
                    this.removeAccessorMethodBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearRemoveAccessorMethod() {
                if (this.removeAccessorMethodBuilder_ == null) {
                    this.removeAccessorMethod_ = null;
                    onChanged();
                } else {
                    this.removeAccessorMethod_ = null;
                    this.removeAccessorMethodBuilder_ = null;
                }
                return this;
            }

            public MethodDefinition.Builder getRemoveAccessorMethodBuilder() {
                onChanged();
                return getRemoveAccessorMethodFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public MethodDefinitionOrBuilder getRemoveAccessorMethodOrBuilder() {
                if (this.removeAccessorMethodBuilder_ != null) {
                    return this.removeAccessorMethodBuilder_.getMessageOrBuilder();
                }
                if (this.removeAccessorMethod_ == null) {
                    return MethodDefinition.getDefaultInstance();
                }
                return this.removeAccessorMethod_;
            }

            private SingleFieldBuilderV3<MethodDefinition, MethodDefinition.Builder, MethodDefinitionOrBuilder> getRemoveAccessorMethodFieldBuilder() {
                if (this.removeAccessorMethodBuilder_ == null) {
                    this.removeAccessorMethodBuilder_ = new SingleFieldBuilderV3<>(getRemoveAccessorMethod(), getParentForChildren(), isClean());
                    this.removeAccessorMethod_ = null;
                }
                return this.removeAccessorMethodBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoAssemblyAllTypes.EventDefinitionOrBuilder
            public int getPeToken() {
                return this.peToken_;
            }

            public Builder setPeToken(int value) {
                this.peToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearPeToken() {
                this.peToken_ = 0;
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

        public static EventDefinition getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<EventDefinition> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<EventDefinition> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public EventDefinition getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0016AssemblyAllTypes.proto\"_\n\u0010AssemblyAllTypes\u0012&\n\rlist_of_types\u0018\u0001 \u0003(\u000b2\u000f.TypeDefinition\u0012#\n\u001ball_referenced_module_types\u0018\u0002 \u0003(\t\"Ø\u0004\n\u000eTypeDefinition\u0012%\n\raccessibility\u0018\u0001 \u0001(\u000e2\u000e.Accessibility\u0012\u0010\n\bfullname\u0018\u0002 \u0001(\t\u0012\u0011\n\tnamespace\u0018\u0003 \u0001(\t\u0012\u0013\n\u000bis_abstract\u0018\u0004 \u0001(\b\u0012\u0014\n\fis_read_only\u0018\u0005 \u0001(\b\u0012\u0011\n\tis_sealed\u0018\u0006 \u0001(\b\u0012\u0011\n\tis_static\u0018\u0007 \u0001(\b\u0012\u001d\n\u0015declaring_outer_class\u0018\b \u0001(\t\u0012*\n\u0011direct_base_types\u0018\t \u0003(\u000b2\u000f.TypeDefinition\u0012\u001f\n\ttype_kind\u0018\n \u0001(\u000e2\f.TypeKindDef\u0012\"\n\u0007methods\u0018\u000b \u0003(\u000b2\u0011.MethodDefinition\u0012 \n\u0006fields\u0018\f \u0003(\u000b2\u0010.FieldDefinition\u0012'\n\nproperties\u0018\r \u0003(\u000b2\u0013.PropertyDefinition\u0012%\n\fnested_types\u0018\u000e \u0003(\u000b2\u000f.TypeDefinition\u0012(\n\nattributes\u0018\u000f \u0003(\u000b2\u0014.AttributeDefinition\u0012/\n\u0016generic_type_arguments\u0018\u0010 \u0003(\u000b2\u000f.TypeDefinition\u0012\u0018\n\u0010array_dimensions\u0018\u0011 \u0001(\u0005\u0012 \n\u0006events\u0018\u0012 \u0003(\u000b2\u0010.EventDefinition\u0012\u0010\n\bpe_token\u0018\u0013 \u0001(\u0005\"\u009c\u0004\n\u0010MethodDefinition\u0012%\n\raccessibility\u0018\u0001 \u0001(\u000e2\u000e.Accessibility\u0012\f\n\u0004name\u0018\u0002 \u0001(\t\u0012\u0010\n\bhas_body\u0018\u0003 \u0001(\b\u0012'\n\tparameter\u0018\u0004 \u0003(\u000b2\u0014.ParameterDefinition\u0012\u0013\n\u000bis_abstract\u0018\u0005 \u0001(\b\u0012\u0013\n\u000bis_accessor\u0018\u0006 \u0001(\b\u0012\u0016\n\u000eis_constructor\u0018\u0007 \u0001(\b\u0012\u0015\n\ris_destructor\u0018\b \u0001(\b\u0012,\n$is_explicit_interface_implementation\u0018\t \u0001(\b\u0012\u0011\n\tis_static\u0018\n \u0001(\b\u0012\u0012\n\nis_virtual\u0018\u000b \u0001(\b\u0012\u0013\n\u000bis_operator\u0018\u0010 \u0001(\b\u0012\u0011\n\tis_extern\u0018\u0011 \u0001(\b\u0012\u0011\n\tis_unsafe\u0018\u0012 \u0001(\b\u0012\u0011\n\tis_sealed\u0018\u0013 \u0001(\b\u0012$\n\u000breturn_type\u0018\f \u0001(\u000b2\u000f.TypeDefinition\u0012(\n\nattributes\u0018\r \u0003(\u000b2\u0014.AttributeDefinition\u0012\u0011\n\tfull_name\u0018\u000e \u0001(\t\u0012'\n\u000edeclaring_type\u0018\u000f \u0001(\u000b2\u000f.TypeDefinition\u0012\u0010\n\bpe_token\u0018\u0014 \u0001(\u0005\"\u0090\u0001\n\u0013ParameterDefinition\u0012\u001d\n\u0004type\u0018\u0001 \u0001(\u000b2\u000f.TypeDefinition\u0012\u0016\n\u000eparameter_name\u0018\u0002 \u0001(\t\u0012\u000e\n\u0006is_ref\u0018\u0003 \u0001(\b\u0012\u000e\n\u0006is_out\u0018\u0004 \u0001(\b\u0012\r\n\u0005is_in\u0018\u0005 \u0001(\b\u0012\u0013\n\u000bis_optional\u0018\u0006 \u0001(\b\"¸\u0003\n\u000fFieldDefinition\u0012%\n\raccessibility\u0018\u0001 \u0001(\u000e2\u000e.Accessibility\u0012\u0013\n\u000bis_abstract\u0018\u0002 \u0001(\b\u0012\u0011\n\tis_sealed\u0018\u0003 \u0001(\b\u0012,\n$is_explicit_interface_implementation\u0018\u0004 \u0001(\b\u0012\u0013\n\u000bis_override\u0018\u0005 \u0001(\b\u0012\u0012\n\nis_virtual\u0018\u0006 \u0001(\b\u0012\u0010\n\bis_const\u0018\u0007 \u0001(\b\u0012\u0014\n\fis_read_only\u0018\b \u0001(\b\u0012\u0011\n\tis_static\u0018\t \u0001(\b\u0012\u001d\n\u0004type\u0018\n \u0001(\u000b2\u000f.TypeDefinition\u0012\u001f\n\ttype_kind\u0018\u000e \u0001(\u000e2\f.TypeKindDef\u0012\f\n\u0004name\u0018\u000b \u0001(\t\u0012\u0011\n\tfull_name\u0018\f \u0001(\t\u0012'\n\u000edeclaring_type\u0018\r \u0001(\u000b2\u000f.TypeDefinition\u0012(\n\nattributes\u0018\u000f \u0003(\u000b2\u0014.AttributeDefinition\u0012\u0010\n\bpe_token\u0018\u0010 \u0001(\u0005\"Ò\u0003\n\u0012PropertyDefinition\u0012%\n\raccessibility\u0018\u0001 \u0001(\u000e2\u000e.Accessibility\u0012\u000f\n\u0007can_get\u0018\u0002 \u0001(\b\u0012\u000f\n\u0007can_set\u0018\u0003 \u0001(\b\u0012\u0013\n\u000bis_abstract\u0018\u0004 \u0001(\b\u0012\u0011\n\tis_sealed\u0018\u0005 \u0001(\b\u0012,\n$is_explicit_interface_implementation\u0018\u0006 \u0001(\b\u0012\u0013\n\u000bis_override\u0018\u0007 \u0001(\b\u0012\u0012\n\nis_virtual\u0018\b \u0001(\b\u0012\u0011\n\tis_static\u0018\t \u0001(\b\u0012\u0011\n\tis_extern\u0018\u000f \u0001(\b\u0012!\n\u0006getter\u0018\n \u0001(\u000b2\u0011.MethodDefinition\u0012!\n\u0006setter\u0018\u000b \u0001(\u000b2\u0011.MethodDefinition\u0012\u001d\n\u0004type\u0018\f \u0001(\u000b2\u000f.TypeDefinition\u0012\u001f\n\ttype_kind\u0018\u000e \u0001(\u000e2\f.TypeKindDef\u0012\f\n\u0004name\u0018\r \u0001(\t\u0012(\n\nattributes\u0018\u0010 \u0003(\u000b2\u0014.AttributeDefinition\u0012\u0010\n\bpe_token\u0018\u0011 \u0001(\u0005\"Á\u0001\n\u0013AttributeDefinition\u0012'\n\u000eattribute_type\u0018\u0001 \u0001(\u000b2\u000f.TypeDefinition\u0012\u0013\n\u000bconstructor\u0018\u0002 \u0001(\t\u00125\n\u000ffixed_arguments\u0018\u0003 \u0003(\u000b2\u001c.AttributeArgumentDefinition\u00125\n\u000fnamed_arguments\u0018\u0004 \u0003(\u000b2\u001c.AttributeArgumentDefinition\"µ\u0001\n\u001bAttributeArgumentDefinition\u0012\u001d\n\u0004type\u0018\u0001 \u0001(\u000b2\u000f.TypeDefinition\u0012\f\n\u0004name\u0018\u0002 \u0001(\t\u0012\u0014\n\fvalue_string\u0018\u0003 \u0003(\t\u0012\u0013\n\u000bvalue_int32\u0018\u0004 \u0003(\u0005\u0012\u0013\n\u000bvalue_int64\u0018\u0005 \u0003(\u0003\u0012\u0014\n\fvalue_double\u0018\u0006 \u0003(\u0001\u0012\u0013\n\u000bvalue_float\u0018\u0007 \u0003(\u0002\"º\u0002\n\u000fEventDefinition\u0012%\n\raccessibility\u0018\u0001 \u0001(\u000e2\u000e.Accessibility\u0012.\n\u0013add_accessor_method\u0018\u0002 \u0001(\u000b2\u0011.MethodDefinition\u0012\u000f\n\u0007can_add\u0018\u0003 \u0001(\b\u0012\u0012\n\ncan_invoke\u0018\u0004 \u0001(\b\u0012\u0012\n\ncan_remove\u0018\u0005 \u0001(\b\u0012\u0011\n\tfull_name\u0018\u0006 \u0001(\t\u00121\n\u0016invoke_accessor_method\u0018\u0007 \u0001(\u000b2\u0011.MethodDefinition\u0012\f\n\u0004name\u0018\b \u0001(\t\u00121\n\u0016remove_accessor_method\u0018\t \u0001(\u000b2\u0011.MethodDefinition\u0012\u0010\n\bpe_token\u0018\n \u0001(\u0005*\u0086\u0001\n\rAccessibility\u0012\b\n\u0004NONE\u0010��\u0012\u000b\n\u0007PRIVATE\u0010\u0001\u0012\n\n\u0006PUBLIC\u0010\u0002\u0012\f\n\bINTERNAL\u0010\u0003\u0012\r\n\tPROTECTED\u0010\u0004\u0012\u001a\n\u0016PROTECTED_AND_INTERNAL\u0010\u0005\u0012\u0019\n\u0015PROTECTED_OR_INTERNAL\u0010\u0006*ö\u0002\n\u000bTypeKindDef\u0012\u000b\n\u0007NO_TYPE\u0010��\u0012\t\n\u0005OTHER\u0010\u0001\u0012\t\n\u0005CLASS\u0010\u0002\u0012\r\n\tINTERFACE\u0010\u0003\u0012\n\n\u0006STRUCT\u0010\u0004\u0012\f\n\bDELEGATE\u0010\u0005\u0012\b\n\u0004ENUM\u0010\u0006\u0012\b\n\u0004VOID\u0010\u0007\u0012\u000b\n\u0007UNKNOWN\u0010\b\u0012\b\n\u0004NULL\u0010\t\u0012\r\n\tNONE_TYPE\u0010\n\u0012\u000b\n\u0007DYNAMIC\u0010\u000b\u0012\u0014\n\u0010UNBOUND_TYPE_ARG\u0010\f\u0012\u0012\n\u000eTYPE_PARAMETER\u0010\r\u0012\t\n\u0005ARRAY\u0010\u000e\u0012\u000b\n\u0007POINTER\u0010\u000f\u0012\n\n\u0006BY_REF\u0010\u0010\u0012\u0010\n\fINTERSECTION\u0010\u0011\u0012\f\n\bARG_LIST\u0010\u0012\u0012\t\n\u0005TUPLE\u0010\u0013\u0012\u000b\n\u0007MOD_OPT\u0010\u0014\u0012\u000b\n\u0007MOD_REQ\u0010\u0015\u0012\t\n\u0005N_INT\u0010\u0016\u0012\n\n\u0006N_UINT\u0010\u0017\u0012\u0014\n\u0010FUNCTION_POINTER\u0010\u0018\u0012\u0014\n\u0010BY_REF_AND_ARRAY\u0010\u0019BS\n\u0011soot.dotnet.protoB\u0015ProtoAssemblyAllTypesª\u0002&Soot.Dotnet.Decompiler.Models.Protobufb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
