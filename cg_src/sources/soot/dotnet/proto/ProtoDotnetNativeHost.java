package soot.dotnet.proto;

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
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.UnknownFieldSet;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost.class */
public final class ProtoDotnetNativeHost {
    private static final Descriptors.Descriptor internal_static_AnalyzerParamsMsg_descriptor = getDescriptor().getMessageTypes().get(0);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_AnalyzerParamsMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_AnalyzerParamsMsg_descriptor, new String[]{"AnalyzerMethodCall", "AssemblyFileAbsolutePath", "TypeReflectionName", "MethodName", "MethodNameSuffix", "MethodPeToken", "PropertyName", "PropertyIsSetter", "EventName", "EventAccessorType", "DebugMode"});
    private static Descriptors.FileDescriptor descriptor;

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost$AnalyzerParamsMsgOrBuilder.class */
    public interface AnalyzerParamsMsgOrBuilder extends MessageOrBuilder {
        int getAnalyzerMethodCallValue();

        AnalyzerMethodCall getAnalyzerMethodCall();

        String getAssemblyFileAbsolutePath();

        ByteString getAssemblyFileAbsolutePathBytes();

        String getTypeReflectionName();

        ByteString getTypeReflectionNameBytes();

        String getMethodName();

        ByteString getMethodNameBytes();

        String getMethodNameSuffix();

        ByteString getMethodNameSuffixBytes();

        int getMethodPeToken();

        String getPropertyName();

        ByteString getPropertyNameBytes();

        boolean getPropertyIsSetter();

        String getEventName();

        ByteString getEventNameBytes();

        int getEventAccessorTypeValue();

        EventAccessorType getEventAccessorType();

        boolean getDebugMode();
    }

    private ProtoDotnetNativeHost() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost$AnalyzerMethodCall.class */
    public enum AnalyzerMethodCall implements ProtocolMessageEnum {
        NO_CALL(0),
        GET_ALL_TYPES(1),
        GET_METHOD_BODY(2),
        GET_METHOD_BODY_OF_PROPERTY(3),
        GET_METHOD_BODY_OF_EVENT(4),
        GET_TYPE_DEF(5),
        UNRECOGNIZED(-1);
        
        public static final int NO_CALL_VALUE = 0;
        public static final int GET_ALL_TYPES_VALUE = 1;
        public static final int GET_METHOD_BODY_VALUE = 2;
        public static final int GET_METHOD_BODY_OF_PROPERTY_VALUE = 3;
        public static final int GET_METHOD_BODY_OF_EVENT_VALUE = 4;
        public static final int GET_TYPE_DEF_VALUE = 5;
        private static final Internal.EnumLiteMap<AnalyzerMethodCall> internalValueMap = new Internal.EnumLiteMap<AnalyzerMethodCall>() { // from class: soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerMethodCall.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public AnalyzerMethodCall findValueByNumber(int number) {
                return AnalyzerMethodCall.forNumber(number);
            }
        };
        private static final AnalyzerMethodCall[] VALUES = valuesCustom();
        private final int value;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static AnalyzerMethodCall[] valuesCustom() {
            AnalyzerMethodCall[] valuesCustom = values();
            int length = valuesCustom.length;
            AnalyzerMethodCall[] analyzerMethodCallArr = new AnalyzerMethodCall[length];
            System.arraycopy(valuesCustom, 0, analyzerMethodCallArr, 0, length);
            return analyzerMethodCallArr;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            }
            return this.value;
        }

        @Deprecated
        public static AnalyzerMethodCall valueOf(int value) {
            return forNumber(value);
        }

        public static AnalyzerMethodCall forNumber(int value) {
            switch (value) {
                case 0:
                    return NO_CALL;
                case 1:
                    return GET_ALL_TYPES;
                case 2:
                    return GET_METHOD_BODY;
                case 3:
                    return GET_METHOD_BODY_OF_PROPERTY;
                case 4:
                    return GET_METHOD_BODY_OF_EVENT;
                case 5:
                    return GET_TYPE_DEF;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<AnalyzerMethodCall> internalGetValueMap() {
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
            return ProtoDotnetNativeHost.getDescriptor().getEnumTypes().get(0);
        }

        public static AnalyzerMethodCall valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
            if (desc.getIndex() == -1) {
                return UNRECOGNIZED;
            }
            return VALUES[desc.getIndex()];
        }

        AnalyzerMethodCall(int value) {
            this.value = value;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost$EventAccessorType.class */
    public enum EventAccessorType implements ProtocolMessageEnum {
        NO_EVENT_METHOD(0),
        ADD_ACCESSOR(1),
        INVOKE_ACCESSOR(2),
        REMOVE_ACCESSOR(3),
        UNRECOGNIZED(-1);
        
        public static final int NO_EVENT_METHOD_VALUE = 0;
        public static final int ADD_ACCESSOR_VALUE = 1;
        public static final int INVOKE_ACCESSOR_VALUE = 2;
        public static final int REMOVE_ACCESSOR_VALUE = 3;
        private static final Internal.EnumLiteMap<EventAccessorType> internalValueMap = new Internal.EnumLiteMap<EventAccessorType>() { // from class: soot.dotnet.proto.ProtoDotnetNativeHost.EventAccessorType.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public EventAccessorType findValueByNumber(int number) {
                return EventAccessorType.forNumber(number);
            }
        };
        private static final EventAccessorType[] VALUES = valuesCustom();
        private final int value;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static EventAccessorType[] valuesCustom() {
            EventAccessorType[] valuesCustom = values();
            int length = valuesCustom.length;
            EventAccessorType[] eventAccessorTypeArr = new EventAccessorType[length];
            System.arraycopy(valuesCustom, 0, eventAccessorTypeArr, 0, length);
            return eventAccessorTypeArr;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            if (this == UNRECOGNIZED) {
                throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
            }
            return this.value;
        }

        @Deprecated
        public static EventAccessorType valueOf(int value) {
            return forNumber(value);
        }

        public static EventAccessorType forNumber(int value) {
            switch (value) {
                case 0:
                    return NO_EVENT_METHOD;
                case 1:
                    return ADD_ACCESSOR;
                case 2:
                    return INVOKE_ACCESSOR;
                case 3:
                    return REMOVE_ACCESSOR;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<EventAccessorType> internalGetValueMap() {
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
            return ProtoDotnetNativeHost.getDescriptor().getEnumTypes().get(1);
        }

        public static EventAccessorType valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
            if (desc.getIndex() == -1) {
                return UNRECOGNIZED;
            }
            return VALUES[desc.getIndex()];
        }

        EventAccessorType(int value) {
            this.value = value;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost$AnalyzerParamsMsg.class */
    public static final class AnalyzerParamsMsg extends GeneratedMessageV3 implements AnalyzerParamsMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int ANALYZER_METHOD_CALL_FIELD_NUMBER = 1;
        private int analyzerMethodCall_;
        public static final int ASSEMBLY_FILE_ABSOLUTE_PATH_FIELD_NUMBER = 2;
        private volatile Object assemblyFileAbsolutePath_;
        public static final int TYPE_REFLECTION_NAME_FIELD_NUMBER = 3;
        private volatile Object typeReflectionName_;
        public static final int METHOD_NAME_FIELD_NUMBER = 4;
        private volatile Object methodName_;
        public static final int METHOD_NAME_SUFFIX_FIELD_NUMBER = 11;
        private volatile Object methodNameSuffix_;
        public static final int METHOD_PE_TOKEN_FIELD_NUMBER = 12;
        private int methodPeToken_;
        public static final int PROPERTY_NAME_FIELD_NUMBER = 6;
        private volatile Object propertyName_;
        public static final int PROPERTY_IS_SETTER_FIELD_NUMBER = 7;
        private boolean propertyIsSetter_;
        public static final int EVENT_NAME_FIELD_NUMBER = 8;
        private volatile Object eventName_;
        public static final int EVENT_ACCESSOR_TYPE_FIELD_NUMBER = 9;
        private int eventAccessorType_;
        public static final int DEBUG_MODE_FIELD_NUMBER = 10;
        private boolean debugMode_;
        private byte memoizedIsInitialized;
        private static final AnalyzerParamsMsg DEFAULT_INSTANCE = new AnalyzerParamsMsg();
        private static final Parser<AnalyzerParamsMsg> PARSER = new AbstractParser<AnalyzerParamsMsg>() { // from class: soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsg.1
            @Override // com.google.protobuf.Parser
            public AnalyzerParamsMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new AnalyzerParamsMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ AnalyzerParamsMsg(GeneratedMessageV3.Builder builder, AnalyzerParamsMsg analyzerParamsMsg) {
            this(builder);
        }

        private AnalyzerParamsMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private AnalyzerParamsMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.analyzerMethodCall_ = 0;
            this.assemblyFileAbsolutePath_ = "";
            this.typeReflectionName_ = "";
            this.methodName_ = "";
            this.methodNameSuffix_ = "";
            this.propertyName_ = "";
            this.eventName_ = "";
            this.eventAccessorType_ = 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new AnalyzerParamsMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ AnalyzerParamsMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, AnalyzerParamsMsg analyzerParamsMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private AnalyzerParamsMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                this.analyzerMethodCall_ = rawValue;
                                break;
                            case 18:
                                String s = input.readStringRequireUtf8();
                                this.assemblyFileAbsolutePath_ = s;
                                break;
                            case 26:
                                String s2 = input.readStringRequireUtf8();
                                this.typeReflectionName_ = s2;
                                break;
                            case 34:
                                String s3 = input.readStringRequireUtf8();
                                this.methodName_ = s3;
                                break;
                            case 50:
                                String s4 = input.readStringRequireUtf8();
                                this.propertyName_ = s4;
                                break;
                            case 56:
                                this.propertyIsSetter_ = input.readBool();
                                break;
                            case 66:
                                String s5 = input.readStringRequireUtf8();
                                this.eventName_ = s5;
                                break;
                            case 72:
                                int rawValue2 = input.readEnum();
                                this.eventAccessorType_ = rawValue2;
                                break;
                            case 80:
                                this.debugMode_ = input.readBool();
                                break;
                            case 90:
                                String s6 = input.readStringRequireUtf8();
                                this.methodNameSuffix_ = s6;
                                break;
                            case 96:
                                this.methodPeToken_ = input.readInt32();
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
            return ProtoDotnetNativeHost.internal_static_AnalyzerParamsMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoDotnetNativeHost.internal_static_AnalyzerParamsMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AnalyzerParamsMsg.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public int getAnalyzerMethodCallValue() {
            return this.analyzerMethodCall_;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public AnalyzerMethodCall getAnalyzerMethodCall() {
            AnalyzerMethodCall result = AnalyzerMethodCall.valueOf(this.analyzerMethodCall_);
            return result == null ? AnalyzerMethodCall.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getAssemblyFileAbsolutePath() {
            Object ref = this.assemblyFileAbsolutePath_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.assemblyFileAbsolutePath_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getAssemblyFileAbsolutePathBytes() {
            Object ref = this.assemblyFileAbsolutePath_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.assemblyFileAbsolutePath_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getTypeReflectionName() {
            Object ref = this.typeReflectionName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.typeReflectionName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getTypeReflectionNameBytes() {
            Object ref = this.typeReflectionName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.typeReflectionName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getMethodName() {
            Object ref = this.methodName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.methodName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getMethodNameBytes() {
            Object ref = this.methodName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.methodName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getMethodNameSuffix() {
            Object ref = this.methodNameSuffix_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.methodNameSuffix_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getMethodNameSuffixBytes() {
            Object ref = this.methodNameSuffix_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.methodNameSuffix_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public int getMethodPeToken() {
            return this.methodPeToken_;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getPropertyName() {
            Object ref = this.propertyName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.propertyName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getPropertyNameBytes() {
            Object ref = this.propertyName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.propertyName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public boolean getPropertyIsSetter() {
            return this.propertyIsSetter_;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public String getEventName() {
            Object ref = this.eventName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.eventName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public ByteString getEventNameBytes() {
            Object ref = this.eventName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.eventName_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public int getEventAccessorTypeValue() {
            return this.eventAccessorType_;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public EventAccessorType getEventAccessorType() {
            EventAccessorType result = EventAccessorType.valueOf(this.eventAccessorType_);
            return result == null ? EventAccessorType.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
        public boolean getDebugMode() {
            return this.debugMode_;
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
            if (this.analyzerMethodCall_ != AnalyzerMethodCall.NO_CALL.getNumber()) {
                output.writeEnum(1, this.analyzerMethodCall_);
            }
            if (!getAssemblyFileAbsolutePathBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.assemblyFileAbsolutePath_);
            }
            if (!getTypeReflectionNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 3, this.typeReflectionName_);
            }
            if (!getMethodNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 4, this.methodName_);
            }
            if (!getPropertyNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 6, this.propertyName_);
            }
            if (this.propertyIsSetter_) {
                output.writeBool(7, this.propertyIsSetter_);
            }
            if (!getEventNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 8, this.eventName_);
            }
            if (this.eventAccessorType_ != EventAccessorType.NO_EVENT_METHOD.getNumber()) {
                output.writeEnum(9, this.eventAccessorType_);
            }
            if (this.debugMode_) {
                output.writeBool(10, this.debugMode_);
            }
            if (!getMethodNameSuffixBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 11, this.methodNameSuffix_);
            }
            if (this.methodPeToken_ != 0) {
                output.writeInt32(12, this.methodPeToken_);
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
            if (this.analyzerMethodCall_ != AnalyzerMethodCall.NO_CALL.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.analyzerMethodCall_);
            }
            if (!getAssemblyFileAbsolutePathBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.assemblyFileAbsolutePath_);
            }
            if (!getTypeReflectionNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(3, this.typeReflectionName_);
            }
            if (!getMethodNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(4, this.methodName_);
            }
            if (!getPropertyNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(6, this.propertyName_);
            }
            if (this.propertyIsSetter_) {
                size2 += CodedOutputStream.computeBoolSize(7, this.propertyIsSetter_);
            }
            if (!getEventNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(8, this.eventName_);
            }
            if (this.eventAccessorType_ != EventAccessorType.NO_EVENT_METHOD.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(9, this.eventAccessorType_);
            }
            if (this.debugMode_) {
                size2 += CodedOutputStream.computeBoolSize(10, this.debugMode_);
            }
            if (!getMethodNameSuffixBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(11, this.methodNameSuffix_);
            }
            if (this.methodPeToken_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(12, this.methodPeToken_);
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
            if (!(obj instanceof AnalyzerParamsMsg)) {
                return super.equals(obj);
            }
            AnalyzerParamsMsg other = (AnalyzerParamsMsg) obj;
            if (this.analyzerMethodCall_ != other.analyzerMethodCall_ || !getAssemblyFileAbsolutePath().equals(other.getAssemblyFileAbsolutePath()) || !getTypeReflectionName().equals(other.getTypeReflectionName()) || !getMethodName().equals(other.getMethodName()) || !getMethodNameSuffix().equals(other.getMethodNameSuffix()) || getMethodPeToken() != other.getMethodPeToken() || !getPropertyName().equals(other.getPropertyName()) || getPropertyIsSetter() != other.getPropertyIsSetter() || !getEventName().equals(other.getEventName()) || this.eventAccessorType_ != other.eventAccessorType_ || getDebugMode() != other.getDebugMode() || !this.unknownFields.equals(other.unknownFields)) {
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
            int hash2 = (29 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 1)) + this.analyzerMethodCall_)) + 2)) + getAssemblyFileAbsolutePath().hashCode())) + 3)) + getTypeReflectionName().hashCode())) + 4)) + getMethodName().hashCode())) + 11)) + getMethodNameSuffix().hashCode())) + 12)) + getMethodPeToken())) + 6)) + getPropertyName().hashCode())) + 7)) + Internal.hashBoolean(getPropertyIsSetter()))) + 8)) + getEventName().hashCode())) + 9)) + this.eventAccessorType_)) + 10)) + Internal.hashBoolean(getDebugMode()))) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static AnalyzerParamsMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AnalyzerParamsMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AnalyzerParamsMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AnalyzerParamsMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AnalyzerParamsMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static AnalyzerParamsMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static AnalyzerParamsMsg parseFrom(InputStream input) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AnalyzerParamsMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static AnalyzerParamsMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static AnalyzerParamsMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static AnalyzerParamsMsg parseFrom(CodedInputStream input) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static AnalyzerParamsMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (AnalyzerParamsMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(AnalyzerParamsMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoDotnetNativeHost$AnalyzerParamsMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements AnalyzerParamsMsgOrBuilder {
            private int analyzerMethodCall_;
            private Object assemblyFileAbsolutePath_;
            private Object typeReflectionName_;
            private Object methodName_;
            private Object methodNameSuffix_;
            private int methodPeToken_;
            private Object propertyName_;
            private boolean propertyIsSetter_;
            private Object eventName_;
            private int eventAccessorType_;
            private boolean debugMode_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoDotnetNativeHost.internal_static_AnalyzerParamsMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoDotnetNativeHost.internal_static_AnalyzerParamsMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(AnalyzerParamsMsg.class, Builder.class);
            }

            private Builder() {
                this.analyzerMethodCall_ = 0;
                this.assemblyFileAbsolutePath_ = "";
                this.typeReflectionName_ = "";
                this.methodName_ = "";
                this.methodNameSuffix_ = "";
                this.propertyName_ = "";
                this.eventName_ = "";
                this.eventAccessorType_ = 0;
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
                this.analyzerMethodCall_ = 0;
                this.assemblyFileAbsolutePath_ = "";
                this.typeReflectionName_ = "";
                this.methodName_ = "";
                this.methodNameSuffix_ = "";
                this.propertyName_ = "";
                this.eventName_ = "";
                this.eventAccessorType_ = 0;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = AnalyzerParamsMsg.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.analyzerMethodCall_ = 0;
                this.assemblyFileAbsolutePath_ = "";
                this.typeReflectionName_ = "";
                this.methodName_ = "";
                this.methodNameSuffix_ = "";
                this.methodPeToken_ = 0;
                this.propertyName_ = "";
                this.propertyIsSetter_ = false;
                this.eventName_ = "";
                this.eventAccessorType_ = 0;
                this.debugMode_ = false;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoDotnetNativeHost.internal_static_AnalyzerParamsMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public AnalyzerParamsMsg getDefaultInstanceForType() {
                return AnalyzerParamsMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AnalyzerParamsMsg build() {
                AnalyzerParamsMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public AnalyzerParamsMsg buildPartial() {
                AnalyzerParamsMsg result = new AnalyzerParamsMsg(this, (AnalyzerParamsMsg) null);
                result.analyzerMethodCall_ = this.analyzerMethodCall_;
                result.assemblyFileAbsolutePath_ = this.assemblyFileAbsolutePath_;
                result.typeReflectionName_ = this.typeReflectionName_;
                result.methodName_ = this.methodName_;
                result.methodNameSuffix_ = this.methodNameSuffix_;
                result.methodPeToken_ = this.methodPeToken_;
                result.propertyName_ = this.propertyName_;
                result.propertyIsSetter_ = this.propertyIsSetter_;
                result.eventName_ = this.eventName_;
                result.eventAccessorType_ = this.eventAccessorType_;
                result.debugMode_ = this.debugMode_;
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
                if (other instanceof AnalyzerParamsMsg) {
                    return mergeFrom((AnalyzerParamsMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(AnalyzerParamsMsg other) {
                if (other != AnalyzerParamsMsg.getDefaultInstance()) {
                    if (other.analyzerMethodCall_ != 0) {
                        setAnalyzerMethodCallValue(other.getAnalyzerMethodCallValue());
                    }
                    if (!other.getAssemblyFileAbsolutePath().isEmpty()) {
                        this.assemblyFileAbsolutePath_ = other.assemblyFileAbsolutePath_;
                        onChanged();
                    }
                    if (!other.getTypeReflectionName().isEmpty()) {
                        this.typeReflectionName_ = other.typeReflectionName_;
                        onChanged();
                    }
                    if (!other.getMethodName().isEmpty()) {
                        this.methodName_ = other.methodName_;
                        onChanged();
                    }
                    if (!other.getMethodNameSuffix().isEmpty()) {
                        this.methodNameSuffix_ = other.methodNameSuffix_;
                        onChanged();
                    }
                    if (other.getMethodPeToken() != 0) {
                        setMethodPeToken(other.getMethodPeToken());
                    }
                    if (!other.getPropertyName().isEmpty()) {
                        this.propertyName_ = other.propertyName_;
                        onChanged();
                    }
                    if (other.getPropertyIsSetter()) {
                        setPropertyIsSetter(other.getPropertyIsSetter());
                    }
                    if (!other.getEventName().isEmpty()) {
                        this.eventName_ = other.eventName_;
                        onChanged();
                    }
                    if (other.eventAccessorType_ != 0) {
                        setEventAccessorTypeValue(other.getEventAccessorTypeValue());
                    }
                    if (other.getDebugMode()) {
                        setDebugMode(other.getDebugMode());
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
                AnalyzerParamsMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (AnalyzerParamsMsg) AnalyzerParamsMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        AnalyzerParamsMsg analyzerParamsMsg = (AnalyzerParamsMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public int getAnalyzerMethodCallValue() {
                return this.analyzerMethodCall_;
            }

            public Builder setAnalyzerMethodCallValue(int value) {
                this.analyzerMethodCall_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public AnalyzerMethodCall getAnalyzerMethodCall() {
                AnalyzerMethodCall result = AnalyzerMethodCall.valueOf(this.analyzerMethodCall_);
                return result == null ? AnalyzerMethodCall.UNRECOGNIZED : result;
            }

            public Builder setAnalyzerMethodCall(AnalyzerMethodCall value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.analyzerMethodCall_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearAnalyzerMethodCall() {
                this.analyzerMethodCall_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getAssemblyFileAbsolutePath() {
                Object ref = this.assemblyFileAbsolutePath_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.assemblyFileAbsolutePath_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getAssemblyFileAbsolutePathBytes() {
                Object ref = this.assemblyFileAbsolutePath_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.assemblyFileAbsolutePath_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setAssemblyFileAbsolutePath(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.assemblyFileAbsolutePath_ = value;
                onChanged();
                return this;
            }

            public Builder clearAssemblyFileAbsolutePath() {
                this.assemblyFileAbsolutePath_ = AnalyzerParamsMsg.getDefaultInstance().getAssemblyFileAbsolutePath();
                onChanged();
                return this;
            }

            public Builder setAssemblyFileAbsolutePathBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.assemblyFileAbsolutePath_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getTypeReflectionName() {
                Object ref = this.typeReflectionName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.typeReflectionName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getTypeReflectionNameBytes() {
                Object ref = this.typeReflectionName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.typeReflectionName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setTypeReflectionName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.typeReflectionName_ = value;
                onChanged();
                return this;
            }

            public Builder clearTypeReflectionName() {
                this.typeReflectionName_ = AnalyzerParamsMsg.getDefaultInstance().getTypeReflectionName();
                onChanged();
                return this;
            }

            public Builder setTypeReflectionNameBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.typeReflectionName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getMethodName() {
                Object ref = this.methodName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.methodName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getMethodNameBytes() {
                Object ref = this.methodName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.methodName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setMethodName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.methodName_ = value;
                onChanged();
                return this;
            }

            public Builder clearMethodName() {
                this.methodName_ = AnalyzerParamsMsg.getDefaultInstance().getMethodName();
                onChanged();
                return this;
            }

            public Builder setMethodNameBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.methodName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getMethodNameSuffix() {
                Object ref = this.methodNameSuffix_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.methodNameSuffix_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getMethodNameSuffixBytes() {
                Object ref = this.methodNameSuffix_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.methodNameSuffix_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setMethodNameSuffix(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.methodNameSuffix_ = value;
                onChanged();
                return this;
            }

            public Builder clearMethodNameSuffix() {
                this.methodNameSuffix_ = AnalyzerParamsMsg.getDefaultInstance().getMethodNameSuffix();
                onChanged();
                return this;
            }

            public Builder setMethodNameSuffixBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.methodNameSuffix_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public int getMethodPeToken() {
                return this.methodPeToken_;
            }

            public Builder setMethodPeToken(int value) {
                this.methodPeToken_ = value;
                onChanged();
                return this;
            }

            public Builder clearMethodPeToken() {
                this.methodPeToken_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getPropertyName() {
                Object ref = this.propertyName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.propertyName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getPropertyNameBytes() {
                Object ref = this.propertyName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.propertyName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setPropertyName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.propertyName_ = value;
                onChanged();
                return this;
            }

            public Builder clearPropertyName() {
                this.propertyName_ = AnalyzerParamsMsg.getDefaultInstance().getPropertyName();
                onChanged();
                return this;
            }

            public Builder setPropertyNameBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.propertyName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public boolean getPropertyIsSetter() {
                return this.propertyIsSetter_;
            }

            public Builder setPropertyIsSetter(boolean value) {
                this.propertyIsSetter_ = value;
                onChanged();
                return this;
            }

            public Builder clearPropertyIsSetter() {
                this.propertyIsSetter_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public String getEventName() {
                Object ref = this.eventName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.eventName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public ByteString getEventNameBytes() {
                Object ref = this.eventName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.eventName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setEventName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.eventName_ = value;
                onChanged();
                return this;
            }

            public Builder clearEventName() {
                this.eventName_ = AnalyzerParamsMsg.getDefaultInstance().getEventName();
                onChanged();
                return this;
            }

            public Builder setEventNameBytes(ByteString value) {
                if (value != null) {
                    AnalyzerParamsMsg.checkByteStringIsUtf8(value);
                    this.eventName_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public int getEventAccessorTypeValue() {
                return this.eventAccessorType_;
            }

            public Builder setEventAccessorTypeValue(int value) {
                this.eventAccessorType_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public EventAccessorType getEventAccessorType() {
                EventAccessorType result = EventAccessorType.valueOf(this.eventAccessorType_);
                return result == null ? EventAccessorType.UNRECOGNIZED : result;
            }

            public Builder setEventAccessorType(EventAccessorType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.eventAccessorType_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearEventAccessorType() {
                this.eventAccessorType_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoDotnetNativeHost.AnalyzerParamsMsgOrBuilder
            public boolean getDebugMode() {
                return this.debugMode_;
            }

            public Builder setDebugMode(boolean value) {
                this.debugMode_ = value;
                onChanged();
                return this;
            }

            public Builder clearDebugMode() {
                this.debugMode_ = false;
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

        public static AnalyzerParamsMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<AnalyzerParamsMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<AnalyzerParamsMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public AnalyzerParamsMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0016DotnetNativeHost.proto\"ß\u0002\n\u0011AnalyzerParamsMsg\u00121\n\u0014analyzer_method_call\u0018\u0001 \u0001(\u000e2\u0013.AnalyzerMethodCall\u0012#\n\u001bassembly_file_absolute_path\u0018\u0002 \u0001(\t\u0012\u001c\n\u0014type_reflection_name\u0018\u0003 \u0001(\t\u0012\u0013\n\u000bmethod_name\u0018\u0004 \u0001(\t\u0012\u001a\n\u0012method_name_suffix\u0018\u000b \u0001(\t\u0012\u0017\n\u000fmethod_pe_token\u0018\f \u0001(\u0005\u0012\u0015\n\rproperty_name\u0018\u0006 \u0001(\t\u0012\u001a\n\u0012property_is_setter\u0018\u0007 \u0001(\b\u0012\u0012\n\nevent_name\u0018\b \u0001(\t\u0012/\n\u0013event_accessor_type\u0018\t \u0001(\u000e2\u0012.EventAccessorType\u0012\u0012\n\ndebug_mode\u0018\n \u0001(\b*\u009a\u0001\n\u0012AnalyzerMethodCall\u0012\u000b\n\u0007NO_CALL\u0010��\u0012\u0011\n\rGET_ALL_TYPES\u0010\u0001\u0012\u0013\n\u000fGET_METHOD_BODY\u0010\u0002\u0012\u001f\n\u001bGET_METHOD_BODY_OF_PROPERTY\u0010\u0003\u0012\u001c\n\u0018GET_METHOD_BODY_OF_EVENT\u0010\u0004\u0012\u0010\n\fGET_TYPE_DEF\u0010\u0005*d\n\u0011EventAccessorType\u0012\u0013\n\u000fNO_EVENT_METHOD\u0010��\u0012\u0010\n\fADD_ACCESSOR\u0010\u0001\u0012\u0013\n\u000fINVOKE_ACCESSOR\u0010\u0002\u0012\u0013\n\u000fREMOVE_ACCESSOR\u0010\u0003BS\n\u0011soot.dotnet.protoB\u0015ProtoDotnetNativeHostª\u0002&Soot.Dotnet.Decompiler.Models.Protobufb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
