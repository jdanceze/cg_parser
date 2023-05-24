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
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.RepeatedFieldBuilderV3;
import com.google.protobuf.SingleFieldBuilderV3;
import com.google.protobuf.UnknownFieldSet;
import com.sun.xml.bind.v2.runtime.reflect.opt.Const;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javassist.compiler.TokenId;
import org.apache.tools.ant.taskdefs.optional.vss.MSVSSConstants;
import soot.dotnet.proto.ProtoAssemblyAllTypes;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions.class */
public final class ProtoIlInstructions {
    private static final Descriptors.Descriptor internal_static_IlFunctionMsg_descriptor = getDescriptor().getMessageTypes().get(0);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlFunctionMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlFunctionMsg_descriptor, new String[]{"Body", "Variables"});
    private static final Descriptors.Descriptor internal_static_IlBlockContainerMsg_descriptor = getDescriptor().getMessageTypes().get(1);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlBlockContainerMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlBlockContainerMsg_descriptor, new String[]{"Blocks"});
    private static final Descriptors.Descriptor internal_static_IlBlock_descriptor = getDescriptor().getMessageTypes().get(2);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlBlock_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlBlock_descriptor, new String[]{"ListOfIlInstructions", "BlockName"});
    private static final Descriptors.Descriptor internal_static_IlInstructionMsg_descriptor = getDescriptor().getMessageTypes().get(3);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlInstructionMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlInstructionMsg_descriptor, new String[]{"OpCode", XmlConstants.Attributes.method, "Arguments", "ValueInstruction", "ValueConstantString", "ValueConstantInt32", "ValueConstantInt64", "ValueConstantFloat", "ValueConstantDouble", "Target", XmlConstants.Attributes.type, XmlConstants.Tags.field, "Variable", "Operator", "Sign", "Left", "Right", "TargetLabel", "ComparisonKind", "Condition", "TrueInst", "FalseInst", "Array", "ConversionKind", "InputType", "TargetType", "Argument", "ResultType", "Indices", "TryBlock", "Handlers", "FinallyBlock", "FaultBlock", "Body", "KeyInstr", "DefaultInst", "SwitchSections"});
    private static final Descriptors.Descriptor internal_static_IlVariableMsg_descriptor = getDescriptor().getMessageTypes().get(4);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlVariableMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlVariableMsg_descriptor, new String[]{XmlConstants.Attributes.type, "Name", "HasInitialValue", "VariableKind"});
    private static final Descriptors.Descriptor internal_static_IlTryCatchHandlerMsg_descriptor = getDescriptor().getMessageTypes().get(5);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlTryCatchHandlerMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlTryCatchHandlerMsg_descriptor, new String[]{"Body", "Variable", "Filter", "HasFilter"});
    private static final Descriptors.Descriptor internal_static_IlSwitchSectionMsg_descriptor = getDescriptor().getMessageTypes().get(6);
    private static final GeneratedMessageV3.FieldAccessorTable internal_static_IlSwitchSectionMsg_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_IlSwitchSectionMsg_descriptor, new String[]{MSVSSConstants.COMMAND_LABEL, "TargetInstr"});
    private static Descriptors.FileDescriptor descriptor;

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlockContainerMsgOrBuilder.class */
    public interface IlBlockContainerMsgOrBuilder extends MessageOrBuilder {
        List<IlBlock> getBlocksList();

        IlBlock getBlocks(int i);

        int getBlocksCount();

        List<? extends IlBlockOrBuilder> getBlocksOrBuilderList();

        IlBlockOrBuilder getBlocksOrBuilder(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlockOrBuilder.class */
    public interface IlBlockOrBuilder extends MessageOrBuilder {
        List<IlInstructionMsg> getListOfIlInstructionsList();

        IlInstructionMsg getListOfIlInstructions(int i);

        int getListOfIlInstructionsCount();

        List<? extends IlInstructionMsgOrBuilder> getListOfIlInstructionsOrBuilderList();

        IlInstructionMsgOrBuilder getListOfIlInstructionsOrBuilder(int i);

        String getBlockName();

        ByteString getBlockNameBytes();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlFunctionMsgOrBuilder.class */
    public interface IlFunctionMsgOrBuilder extends MessageOrBuilder {
        boolean hasBody();

        IlBlockContainerMsg getBody();

        IlBlockContainerMsgOrBuilder getBodyOrBuilder();

        List<IlVariableMsg> getVariablesList();

        IlVariableMsg getVariables(int i);

        int getVariablesCount();

        List<? extends IlVariableMsgOrBuilder> getVariablesOrBuilderList();

        IlVariableMsgOrBuilder getVariablesOrBuilder(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsgOrBuilder.class */
    public interface IlInstructionMsgOrBuilder extends MessageOrBuilder {
        int getOpCodeValue();

        IlInstructionMsg.IlOpCode getOpCode();

        boolean hasMethod();

        ProtoAssemblyAllTypes.MethodDefinition getMethod();

        ProtoAssemblyAllTypes.MethodDefinitionOrBuilder getMethodOrBuilder();

        List<IlInstructionMsg> getArgumentsList();

        IlInstructionMsg getArguments(int i);

        int getArgumentsCount();

        List<? extends IlInstructionMsgOrBuilder> getArgumentsOrBuilderList();

        IlInstructionMsgOrBuilder getArgumentsOrBuilder(int i);

        boolean hasValueInstruction();

        IlInstructionMsg getValueInstruction();

        IlInstructionMsgOrBuilder getValueInstructionOrBuilder();

        String getValueConstantString();

        ByteString getValueConstantStringBytes();

        int getValueConstantInt32();

        long getValueConstantInt64();

        float getValueConstantFloat();

        double getValueConstantDouble();

        boolean hasTarget();

        IlInstructionMsg getTarget();

        IlInstructionMsgOrBuilder getTargetOrBuilder();

        boolean hasType();

        ProtoAssemblyAllTypes.TypeDefinition getType();

        ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder();

        boolean hasField();

        ProtoAssemblyAllTypes.FieldDefinition getField();

        ProtoAssemblyAllTypes.FieldDefinitionOrBuilder getFieldOrBuilder();

        boolean hasVariable();

        IlVariableMsg getVariable();

        IlVariableMsgOrBuilder getVariableOrBuilder();

        int getOperatorValue();

        IlInstructionMsg.IlBinaryNumericOperator getOperator();

        int getSignValue();

        IlInstructionMsg.IlSign getSign();

        boolean hasLeft();

        IlInstructionMsg getLeft();

        IlInstructionMsgOrBuilder getLeftOrBuilder();

        boolean hasRight();

        IlInstructionMsg getRight();

        IlInstructionMsgOrBuilder getRightOrBuilder();

        String getTargetLabel();

        ByteString getTargetLabelBytes();

        int getComparisonKindValue();

        IlInstructionMsg.IlComparisonKind getComparisonKind();

        boolean hasCondition();

        IlInstructionMsg getCondition();

        IlInstructionMsgOrBuilder getConditionOrBuilder();

        boolean hasTrueInst();

        IlInstructionMsg getTrueInst();

        IlInstructionMsgOrBuilder getTrueInstOrBuilder();

        boolean hasFalseInst();

        IlInstructionMsg getFalseInst();

        IlInstructionMsgOrBuilder getFalseInstOrBuilder();

        boolean hasArray();

        IlInstructionMsg getArray();

        IlInstructionMsgOrBuilder getArrayOrBuilder();

        int getConversionKindValue();

        IlInstructionMsg.IlConversionKind getConversionKind();

        int getInputTypeValue();

        IlInstructionMsg.IlStackType getInputType();

        int getTargetTypeValue();

        IlInstructionMsg.IlPrimitiveType getTargetType();

        boolean hasArgument();

        IlInstructionMsg getArgument();

        IlInstructionMsgOrBuilder getArgumentOrBuilder();

        int getResultTypeValue();

        IlInstructionMsg.IlStackType getResultType();

        List<IlInstructionMsg> getIndicesList();

        IlInstructionMsg getIndices(int i);

        int getIndicesCount();

        List<? extends IlInstructionMsgOrBuilder> getIndicesOrBuilderList();

        IlInstructionMsgOrBuilder getIndicesOrBuilder(int i);

        boolean hasTryBlock();

        IlBlockContainerMsg getTryBlock();

        IlBlockContainerMsgOrBuilder getTryBlockOrBuilder();

        List<IlTryCatchHandlerMsg> getHandlersList();

        IlTryCatchHandlerMsg getHandlers(int i);

        int getHandlersCount();

        List<? extends IlTryCatchHandlerMsgOrBuilder> getHandlersOrBuilderList();

        IlTryCatchHandlerMsgOrBuilder getHandlersOrBuilder(int i);

        boolean hasFinallyBlock();

        IlBlockContainerMsg getFinallyBlock();

        IlBlockContainerMsgOrBuilder getFinallyBlockOrBuilder();

        boolean hasFaultBlock();

        IlBlockContainerMsg getFaultBlock();

        IlBlockContainerMsgOrBuilder getFaultBlockOrBuilder();

        boolean hasBody();

        IlBlockContainerMsg getBody();

        IlBlockContainerMsgOrBuilder getBodyOrBuilder();

        boolean hasKeyInstr();

        IlInstructionMsg getKeyInstr();

        IlInstructionMsgOrBuilder getKeyInstrOrBuilder();

        boolean hasDefaultInst();

        IlInstructionMsg getDefaultInst();

        IlInstructionMsgOrBuilder getDefaultInstOrBuilder();

        List<IlSwitchSectionMsg> getSwitchSectionsList();

        IlSwitchSectionMsg getSwitchSections(int i);

        int getSwitchSectionsCount();

        List<? extends IlSwitchSectionMsgOrBuilder> getSwitchSectionsOrBuilderList();

        IlSwitchSectionMsgOrBuilder getSwitchSectionsOrBuilder(int i);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlSwitchSectionMsgOrBuilder.class */
    public interface IlSwitchSectionMsgOrBuilder extends MessageOrBuilder {
        long getLabel();

        boolean hasTargetInstr();

        IlInstructionMsg getTargetInstr();

        IlInstructionMsgOrBuilder getTargetInstrOrBuilder();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlTryCatchHandlerMsgOrBuilder.class */
    public interface IlTryCatchHandlerMsgOrBuilder extends MessageOrBuilder {
        boolean hasBody();

        IlBlockContainerMsg getBody();

        IlBlockContainerMsgOrBuilder getBodyOrBuilder();

        boolean hasVariable();

        IlVariableMsg getVariable();

        IlVariableMsgOrBuilder getVariableOrBuilder();

        boolean hasFilter();

        IlBlockContainerMsg getFilter();

        IlBlockContainerMsgOrBuilder getFilterOrBuilder();

        boolean getHasFilter();
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlVariableMsgOrBuilder.class */
    public interface IlVariableMsgOrBuilder extends MessageOrBuilder {
        boolean hasType();

        ProtoAssemblyAllTypes.TypeDefinition getType();

        ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder();

        String getName();

        ByteString getNameBytes();

        boolean getHasInitialValue();

        int getVariableKindValue();

        IlVariableMsg.IlVariableKind getVariableKind();
    }

    private ProtoIlInstructions() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlFunctionMsg.class */
    public static final class IlFunctionMsg extends GeneratedMessageV3 implements IlFunctionMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int BODY_FIELD_NUMBER = 1;
        private IlBlockContainerMsg body_;
        public static final int VARIABLES_FIELD_NUMBER = 2;
        private List<IlVariableMsg> variables_;
        private byte memoizedIsInitialized;
        private static final IlFunctionMsg DEFAULT_INSTANCE = new IlFunctionMsg();
        private static final Parser<IlFunctionMsg> PARSER = new AbstractParser<IlFunctionMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsg.1
            @Override // com.google.protobuf.Parser
            public IlFunctionMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlFunctionMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlFunctionMsg(GeneratedMessageV3.Builder builder, IlFunctionMsg ilFunctionMsg) {
            this(builder);
        }

        private IlFunctionMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlFunctionMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.variables_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlFunctionMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlFunctionMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlFunctionMsg ilFunctionMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlFunctionMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                IlBlockContainerMsg.Builder subBuilder = null;
                                subBuilder = this.body_ != null ? this.body_.toBuilder() : subBuilder;
                                this.body_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.body_);
                                    this.body_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 18:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.variables_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.variables_.add((IlVariableMsg) input.readMessage(IlVariableMsg.parser(), extensionRegistry));
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
                    this.variables_ = Collections.unmodifiableList(this.variables_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoIlInstructions.internal_static_IlFunctionMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlFunctionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlFunctionMsg.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public boolean hasBody() {
            return this.body_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public IlBlockContainerMsg getBody() {
            return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
            return getBody();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public List<IlVariableMsg> getVariablesList() {
            return this.variables_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public List<? extends IlVariableMsgOrBuilder> getVariablesOrBuilderList() {
            return this.variables_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public int getVariablesCount() {
            return this.variables_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public IlVariableMsg getVariables(int index) {
            return this.variables_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
        public IlVariableMsgOrBuilder getVariablesOrBuilder(int index) {
            return this.variables_.get(index);
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
            if (this.body_ != null) {
                output.writeMessage(1, getBody());
            }
            for (int i = 0; i < this.variables_.size(); i++) {
                output.writeMessage(2, this.variables_.get(i));
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
            if (this.body_ != null) {
                size2 = 0 + CodedOutputStream.computeMessageSize(1, getBody());
            }
            for (int i = 0; i < this.variables_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(2, this.variables_.get(i));
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
            if (!(obj instanceof IlFunctionMsg)) {
                return super.equals(obj);
            }
            IlFunctionMsg other = (IlFunctionMsg) obj;
            if (hasBody() != other.hasBody()) {
                return false;
            }
            if ((hasBody() && !getBody().equals(other.getBody())) || !getVariablesList().equals(other.getVariablesList()) || !this.unknownFields.equals(other.unknownFields)) {
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
            if (hasBody()) {
                hash = (53 * ((37 * hash) + 1)) + getBody().hashCode();
            }
            if (getVariablesCount() > 0) {
                hash = (53 * ((37 * hash) + 2)) + getVariablesList().hashCode();
            }
            int hash2 = (29 * hash) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static IlFunctionMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlFunctionMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlFunctionMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlFunctionMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlFunctionMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlFunctionMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlFunctionMsg parseFrom(InputStream input) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlFunctionMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlFunctionMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlFunctionMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlFunctionMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlFunctionMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlFunctionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlFunctionMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlFunctionMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlFunctionMsgOrBuilder {
            private int bitField0_;
            private IlBlockContainerMsg body_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> bodyBuilder_;
            private List<IlVariableMsg> variables_;
            private RepeatedFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> variablesBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlFunctionMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlFunctionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlFunctionMsg.class, Builder.class);
            }

            private Builder() {
                this.variables_ = Collections.emptyList();
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
                this.variables_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (IlFunctionMsg.alwaysUseFieldBuilders) {
                    getVariablesFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                if (this.variablesBuilder_ == null) {
                    this.variables_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.variablesBuilder_.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlFunctionMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlFunctionMsg getDefaultInstanceForType() {
                return IlFunctionMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlFunctionMsg build() {
                IlFunctionMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlFunctionMsg buildPartial() {
                IlFunctionMsg result = new IlFunctionMsg(this, (IlFunctionMsg) null);
                int i = this.bitField0_;
                if (this.bodyBuilder_ == null) {
                    result.body_ = this.body_;
                } else {
                    result.body_ = this.bodyBuilder_.build();
                }
                if (this.variablesBuilder_ != null) {
                    result.variables_ = this.variablesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.variables_ = Collections.unmodifiableList(this.variables_);
                        this.bitField0_ &= -2;
                    }
                    result.variables_ = this.variables_;
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
                if (other instanceof IlFunctionMsg) {
                    return mergeFrom((IlFunctionMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlFunctionMsg other) {
                if (other == IlFunctionMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasBody()) {
                    mergeBody(other.getBody());
                }
                if (this.variablesBuilder_ == null) {
                    if (!other.variables_.isEmpty()) {
                        if (this.variables_.isEmpty()) {
                            this.variables_ = other.variables_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureVariablesIsMutable();
                            this.variables_.addAll(other.variables_);
                        }
                        onChanged();
                    }
                } else if (!other.variables_.isEmpty()) {
                    if (!this.variablesBuilder_.isEmpty()) {
                        this.variablesBuilder_.addAllMessages(other.variables_);
                    } else {
                        this.variablesBuilder_.dispose();
                        this.variablesBuilder_ = null;
                        this.variables_ = other.variables_;
                        this.bitField0_ &= -2;
                        this.variablesBuilder_ = IlFunctionMsg.alwaysUseFieldBuilders ? getVariablesFieldBuilder() : null;
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
                IlFunctionMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlFunctionMsg) IlFunctionMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlFunctionMsg ilFunctionMsg = (IlFunctionMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public boolean hasBody() {
                return (this.bodyBuilder_ == null && this.body_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public IlBlockContainerMsg getBody() {
                if (this.bodyBuilder_ == null) {
                    return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
                }
                return this.bodyBuilder_.getMessage();
            }

            public Builder setBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.body_ = value;
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setBody(IlBlockContainerMsg.Builder builderForValue) {
                if (this.bodyBuilder_ == null) {
                    this.body_ = builderForValue.build();
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (this.body_ != null) {
                        this.body_ = IlBlockContainerMsg.newBuilder(this.body_).mergeFrom(value).buildPartial();
                    } else {
                        this.body_ = value;
                    }
                    onChanged();
                } else {
                    this.bodyBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearBody() {
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                    onChanged();
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getBodyBuilder() {
                onChanged();
                return getBodyFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
                if (this.bodyBuilder_ != null) {
                    return this.bodyBuilder_.getMessageOrBuilder();
                }
                return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getBodyFieldBuilder() {
                if (this.bodyBuilder_ == null) {
                    this.bodyBuilder_ = new SingleFieldBuilderV3<>(getBody(), getParentForChildren(), isClean());
                    this.body_ = null;
                }
                return this.bodyBuilder_;
            }

            private void ensureVariablesIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.variables_ = new ArrayList(this.variables_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public List<IlVariableMsg> getVariablesList() {
                if (this.variablesBuilder_ == null) {
                    return Collections.unmodifiableList(this.variables_);
                }
                return this.variablesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public int getVariablesCount() {
                if (this.variablesBuilder_ == null) {
                    return this.variables_.size();
                }
                return this.variablesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public IlVariableMsg getVariables(int index) {
                if (this.variablesBuilder_ == null) {
                    return this.variables_.get(index);
                }
                return this.variablesBuilder_.getMessage(index);
            }

            public Builder setVariables(int index, IlVariableMsg value) {
                if (this.variablesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureVariablesIsMutable();
                    this.variables_.set(index, value);
                    onChanged();
                } else {
                    this.variablesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setVariables(int index, IlVariableMsg.Builder builderForValue) {
                if (this.variablesBuilder_ == null) {
                    ensureVariablesIsMutable();
                    this.variables_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.variablesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addVariables(IlVariableMsg value) {
                if (this.variablesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureVariablesIsMutable();
                    this.variables_.add(value);
                    onChanged();
                } else {
                    this.variablesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addVariables(int index, IlVariableMsg value) {
                if (this.variablesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureVariablesIsMutable();
                    this.variables_.add(index, value);
                    onChanged();
                } else {
                    this.variablesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addVariables(IlVariableMsg.Builder builderForValue) {
                if (this.variablesBuilder_ == null) {
                    ensureVariablesIsMutable();
                    this.variables_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.variablesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addVariables(int index, IlVariableMsg.Builder builderForValue) {
                if (this.variablesBuilder_ == null) {
                    ensureVariablesIsMutable();
                    this.variables_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.variablesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllVariables(Iterable<? extends IlVariableMsg> values) {
                if (this.variablesBuilder_ == null) {
                    ensureVariablesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.variables_);
                    onChanged();
                } else {
                    this.variablesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearVariables() {
                if (this.variablesBuilder_ == null) {
                    this.variables_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.variablesBuilder_.clear();
                }
                return this;
            }

            public Builder removeVariables(int index) {
                if (this.variablesBuilder_ == null) {
                    ensureVariablesIsMutable();
                    this.variables_.remove(index);
                    onChanged();
                } else {
                    this.variablesBuilder_.remove(index);
                }
                return this;
            }

            public IlVariableMsg.Builder getVariablesBuilder(int index) {
                return getVariablesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public IlVariableMsgOrBuilder getVariablesOrBuilder(int index) {
                if (this.variablesBuilder_ == null) {
                    return this.variables_.get(index);
                }
                return this.variablesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlFunctionMsgOrBuilder
            public List<? extends IlVariableMsgOrBuilder> getVariablesOrBuilderList() {
                if (this.variablesBuilder_ != null) {
                    return this.variablesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.variables_);
            }

            public IlVariableMsg.Builder addVariablesBuilder() {
                return getVariablesFieldBuilder().addBuilder(IlVariableMsg.getDefaultInstance());
            }

            public IlVariableMsg.Builder addVariablesBuilder(int index) {
                return getVariablesFieldBuilder().addBuilder(index, IlVariableMsg.getDefaultInstance());
            }

            public List<IlVariableMsg.Builder> getVariablesBuilderList() {
                return getVariablesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> getVariablesFieldBuilder() {
                if (this.variablesBuilder_ == null) {
                    this.variablesBuilder_ = new RepeatedFieldBuilderV3<>(this.variables_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.variables_ = null;
                }
                return this.variablesBuilder_;
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

        public static IlFunctionMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlFunctionMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlFunctionMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlFunctionMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlockContainerMsg.class */
    public static final class IlBlockContainerMsg extends GeneratedMessageV3 implements IlBlockContainerMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int BLOCKS_FIELD_NUMBER = 1;
        private List<IlBlock> blocks_;
        private byte memoizedIsInitialized;
        private static final IlBlockContainerMsg DEFAULT_INSTANCE = new IlBlockContainerMsg();
        private static final Parser<IlBlockContainerMsg> PARSER = new AbstractParser<IlBlockContainerMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsg.1
            @Override // com.google.protobuf.Parser
            public IlBlockContainerMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlBlockContainerMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlBlockContainerMsg(GeneratedMessageV3.Builder builder, IlBlockContainerMsg ilBlockContainerMsg) {
            this(builder);
        }

        private IlBlockContainerMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlBlockContainerMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.blocks_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlBlockContainerMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlBlockContainerMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlBlockContainerMsg ilBlockContainerMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlBlockContainerMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                    if ((mutable_bitField0_ & 1) == 0) {
                                        this.blocks_ = new ArrayList();
                                        mutable_bitField0_ |= 1;
                                    }
                                    this.blocks_.add((IlBlock) input.readMessage(IlBlock.parser(), extensionRegistry));
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
                if ((mutable_bitField0_ & 1) != 0) {
                    this.blocks_ = Collections.unmodifiableList(this.blocks_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoIlInstructions.internal_static_IlBlockContainerMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlBlockContainerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlBlockContainerMsg.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
        public List<IlBlock> getBlocksList() {
            return this.blocks_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
        public List<? extends IlBlockOrBuilder> getBlocksOrBuilderList() {
            return this.blocks_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
        public int getBlocksCount() {
            return this.blocks_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
        public IlBlock getBlocks(int index) {
            return this.blocks_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
        public IlBlockOrBuilder getBlocksOrBuilder(int index) {
            return this.blocks_.get(index);
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
            for (int i = 0; i < this.blocks_.size(); i++) {
                output.writeMessage(1, this.blocks_.get(i));
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
            for (int i = 0; i < this.blocks_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.blocks_.get(i));
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
            if (!(obj instanceof IlBlockContainerMsg)) {
                return super.equals(obj);
            }
            IlBlockContainerMsg other = (IlBlockContainerMsg) obj;
            if (!getBlocksList().equals(other.getBlocksList()) || !this.unknownFields.equals(other.unknownFields)) {
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
            if (getBlocksCount() > 0) {
                hash = (53 * ((37 * hash) + 1)) + getBlocksList().hashCode();
            }
            int hash2 = (29 * hash) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static IlBlockContainerMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlockContainerMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlockContainerMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlockContainerMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlockContainerMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlockContainerMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlockContainerMsg parseFrom(InputStream input) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlBlockContainerMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlBlockContainerMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlBlockContainerMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlBlockContainerMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlBlockContainerMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlockContainerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlBlockContainerMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlockContainerMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlBlockContainerMsgOrBuilder {
            private int bitField0_;
            private List<IlBlock> blocks_;
            private RepeatedFieldBuilderV3<IlBlock, IlBlock.Builder, IlBlockOrBuilder> blocksBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlBlockContainerMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlBlockContainerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlBlockContainerMsg.class, Builder.class);
            }

            private Builder() {
                this.blocks_ = Collections.emptyList();
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
                this.blocks_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (IlBlockContainerMsg.alwaysUseFieldBuilders) {
                    getBlocksFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.blocksBuilder_ == null) {
                    this.blocks_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.blocksBuilder_.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlBlockContainerMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlBlockContainerMsg getDefaultInstanceForType() {
                return IlBlockContainerMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlBlockContainerMsg build() {
                IlBlockContainerMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlBlockContainerMsg buildPartial() {
                IlBlockContainerMsg result = new IlBlockContainerMsg(this, (IlBlockContainerMsg) null);
                int i = this.bitField0_;
                if (this.blocksBuilder_ != null) {
                    result.blocks_ = this.blocksBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.blocks_ = Collections.unmodifiableList(this.blocks_);
                        this.bitField0_ &= -2;
                    }
                    result.blocks_ = this.blocks_;
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
                if (other instanceof IlBlockContainerMsg) {
                    return mergeFrom((IlBlockContainerMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlBlockContainerMsg other) {
                if (other == IlBlockContainerMsg.getDefaultInstance()) {
                    return this;
                }
                if (this.blocksBuilder_ == null) {
                    if (!other.blocks_.isEmpty()) {
                        if (this.blocks_.isEmpty()) {
                            this.blocks_ = other.blocks_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureBlocksIsMutable();
                            this.blocks_.addAll(other.blocks_);
                        }
                        onChanged();
                    }
                } else if (!other.blocks_.isEmpty()) {
                    if (!this.blocksBuilder_.isEmpty()) {
                        this.blocksBuilder_.addAllMessages(other.blocks_);
                    } else {
                        this.blocksBuilder_.dispose();
                        this.blocksBuilder_ = null;
                        this.blocks_ = other.blocks_;
                        this.bitField0_ &= -2;
                        this.blocksBuilder_ = IlBlockContainerMsg.alwaysUseFieldBuilders ? getBlocksFieldBuilder() : null;
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
                IlBlockContainerMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlBlockContainerMsg) IlBlockContainerMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlBlockContainerMsg ilBlockContainerMsg = (IlBlockContainerMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            private void ensureBlocksIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.blocks_ = new ArrayList(this.blocks_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
            public List<IlBlock> getBlocksList() {
                if (this.blocksBuilder_ == null) {
                    return Collections.unmodifiableList(this.blocks_);
                }
                return this.blocksBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
            public int getBlocksCount() {
                if (this.blocksBuilder_ == null) {
                    return this.blocks_.size();
                }
                return this.blocksBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
            public IlBlock getBlocks(int index) {
                if (this.blocksBuilder_ == null) {
                    return this.blocks_.get(index);
                }
                return this.blocksBuilder_.getMessage(index);
            }

            public Builder setBlocks(int index, IlBlock value) {
                if (this.blocksBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureBlocksIsMutable();
                    this.blocks_.set(index, value);
                    onChanged();
                } else {
                    this.blocksBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setBlocks(int index, IlBlock.Builder builderForValue) {
                if (this.blocksBuilder_ == null) {
                    ensureBlocksIsMutable();
                    this.blocks_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.blocksBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addBlocks(IlBlock value) {
                if (this.blocksBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureBlocksIsMutable();
                    this.blocks_.add(value);
                    onChanged();
                } else {
                    this.blocksBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addBlocks(int index, IlBlock value) {
                if (this.blocksBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureBlocksIsMutable();
                    this.blocks_.add(index, value);
                    onChanged();
                } else {
                    this.blocksBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addBlocks(IlBlock.Builder builderForValue) {
                if (this.blocksBuilder_ == null) {
                    ensureBlocksIsMutable();
                    this.blocks_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.blocksBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addBlocks(int index, IlBlock.Builder builderForValue) {
                if (this.blocksBuilder_ == null) {
                    ensureBlocksIsMutable();
                    this.blocks_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.blocksBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllBlocks(Iterable<? extends IlBlock> values) {
                if (this.blocksBuilder_ == null) {
                    ensureBlocksIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.blocks_);
                    onChanged();
                } else {
                    this.blocksBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearBlocks() {
                if (this.blocksBuilder_ == null) {
                    this.blocks_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.blocksBuilder_.clear();
                }
                return this;
            }

            public Builder removeBlocks(int index) {
                if (this.blocksBuilder_ == null) {
                    ensureBlocksIsMutable();
                    this.blocks_.remove(index);
                    onChanged();
                } else {
                    this.blocksBuilder_.remove(index);
                }
                return this;
            }

            public IlBlock.Builder getBlocksBuilder(int index) {
                return getBlocksFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
            public IlBlockOrBuilder getBlocksOrBuilder(int index) {
                if (this.blocksBuilder_ == null) {
                    return this.blocks_.get(index);
                }
                return this.blocksBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockContainerMsgOrBuilder
            public List<? extends IlBlockOrBuilder> getBlocksOrBuilderList() {
                if (this.blocksBuilder_ != null) {
                    return this.blocksBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.blocks_);
            }

            public IlBlock.Builder addBlocksBuilder() {
                return getBlocksFieldBuilder().addBuilder(IlBlock.getDefaultInstance());
            }

            public IlBlock.Builder addBlocksBuilder(int index) {
                return getBlocksFieldBuilder().addBuilder(index, IlBlock.getDefaultInstance());
            }

            public List<IlBlock.Builder> getBlocksBuilderList() {
                return getBlocksFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlBlock, IlBlock.Builder, IlBlockOrBuilder> getBlocksFieldBuilder() {
                if (this.blocksBuilder_ == null) {
                    this.blocksBuilder_ = new RepeatedFieldBuilderV3<>(this.blocks_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.blocks_ = null;
                }
                return this.blocksBuilder_;
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

        public static IlBlockContainerMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlBlockContainerMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlBlockContainerMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlBlockContainerMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlock.class */
    public static final class IlBlock extends GeneratedMessageV3 implements IlBlockOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int LIST_OF_IL_INSTRUCTIONS_FIELD_NUMBER = 1;
        private List<IlInstructionMsg> listOfIlInstructions_;
        public static final int BLOCK_NAME_FIELD_NUMBER = 2;
        private volatile Object blockName_;
        private byte memoizedIsInitialized;
        private static final IlBlock DEFAULT_INSTANCE = new IlBlock();
        private static final Parser<IlBlock> PARSER = new AbstractParser<IlBlock>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlBlock.1
            @Override // com.google.protobuf.Parser
            public IlBlock parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlBlock(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlBlock(GeneratedMessageV3.Builder builder, IlBlock ilBlock) {
            this(builder);
        }

        private IlBlock(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlBlock() {
            this.memoizedIsInitialized = (byte) -1;
            this.listOfIlInstructions_ = Collections.emptyList();
            this.blockName_ = "";
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlBlock();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlBlock(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlBlock ilBlock) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlBlock(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                    this.listOfIlInstructions_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.listOfIlInstructions_.add((IlInstructionMsg) input.readMessage(IlInstructionMsg.parser(), extensionRegistry));
                                break;
                            case 18:
                                String s = input.readStringRequireUtf8();
                                this.blockName_ = s;
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
                    this.listOfIlInstructions_ = Collections.unmodifiableList(this.listOfIlInstructions_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoIlInstructions.internal_static_IlBlock_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlBlock_fieldAccessorTable.ensureFieldAccessorsInitialized(IlBlock.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public List<IlInstructionMsg> getListOfIlInstructionsList() {
            return this.listOfIlInstructions_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public List<? extends IlInstructionMsgOrBuilder> getListOfIlInstructionsOrBuilderList() {
            return this.listOfIlInstructions_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public int getListOfIlInstructionsCount() {
            return this.listOfIlInstructions_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public IlInstructionMsg getListOfIlInstructions(int index) {
            return this.listOfIlInstructions_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public IlInstructionMsgOrBuilder getListOfIlInstructionsOrBuilder(int index) {
            return this.listOfIlInstructions_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public String getBlockName() {
            Object ref = this.blockName_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.blockName_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
        public ByteString getBlockNameBytes() {
            Object ref = this.blockName_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.blockName_ = b;
                return b;
            }
            return (ByteString) ref;
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
            for (int i = 0; i < this.listOfIlInstructions_.size(); i++) {
                output.writeMessage(1, this.listOfIlInstructions_.get(i));
            }
            if (!getBlockNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.blockName_);
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
            for (int i = 0; i < this.listOfIlInstructions_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(1, this.listOfIlInstructions_.get(i));
            }
            if (!getBlockNameBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(2, this.blockName_);
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
            if (!(obj instanceof IlBlock)) {
                return super.equals(obj);
            }
            IlBlock other = (IlBlock) obj;
            if (!getListOfIlInstructionsList().equals(other.getListOfIlInstructionsList()) || !getBlockName().equals(other.getBlockName()) || !this.unknownFields.equals(other.unknownFields)) {
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
            if (getListOfIlInstructionsCount() > 0) {
                hash = (53 * ((37 * hash) + 1)) + getListOfIlInstructionsList().hashCode();
            }
            int hash2 = (29 * ((53 * ((37 * hash) + 2)) + getBlockName().hashCode())) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static IlBlock parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlock parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlock parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlock parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlock parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlBlock parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlBlock parseFrom(InputStream input) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlBlock parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlBlock parseDelimitedFrom(InputStream input) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlBlock parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlBlock parseFrom(CodedInputStream input) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlBlock parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlBlock) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlBlock prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlBlock$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlBlockOrBuilder {
            private int bitField0_;
            private List<IlInstructionMsg> listOfIlInstructions_;
            private RepeatedFieldBuilderV3<IlInstructionMsg, IlInstructionMsg.Builder, IlInstructionMsgOrBuilder> listOfIlInstructionsBuilder_;
            private Object blockName_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlBlock_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlBlock_fieldAccessorTable.ensureFieldAccessorsInitialized(IlBlock.class, Builder.class);
            }

            private Builder() {
                this.listOfIlInstructions_ = Collections.emptyList();
                this.blockName_ = "";
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
                this.listOfIlInstructions_ = Collections.emptyList();
                this.blockName_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (IlBlock.alwaysUseFieldBuilders) {
                    getListOfIlInstructionsFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.listOfIlInstructionsBuilder_ == null) {
                    this.listOfIlInstructions_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.listOfIlInstructionsBuilder_.clear();
                }
                this.blockName_ = "";
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlBlock_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlBlock getDefaultInstanceForType() {
                return IlBlock.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlBlock build() {
                IlBlock result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlBlock buildPartial() {
                IlBlock result = new IlBlock(this, (IlBlock) null);
                int i = this.bitField0_;
                if (this.listOfIlInstructionsBuilder_ != null) {
                    result.listOfIlInstructions_ = this.listOfIlInstructionsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.listOfIlInstructions_ = Collections.unmodifiableList(this.listOfIlInstructions_);
                        this.bitField0_ &= -2;
                    }
                    result.listOfIlInstructions_ = this.listOfIlInstructions_;
                }
                result.blockName_ = this.blockName_;
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
                if (other instanceof IlBlock) {
                    return mergeFrom((IlBlock) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlBlock other) {
                if (other == IlBlock.getDefaultInstance()) {
                    return this;
                }
                if (this.listOfIlInstructionsBuilder_ == null) {
                    if (!other.listOfIlInstructions_.isEmpty()) {
                        if (this.listOfIlInstructions_.isEmpty()) {
                            this.listOfIlInstructions_ = other.listOfIlInstructions_;
                            this.bitField0_ &= -2;
                        } else {
                            ensureListOfIlInstructionsIsMutable();
                            this.listOfIlInstructions_.addAll(other.listOfIlInstructions_);
                        }
                        onChanged();
                    }
                } else if (!other.listOfIlInstructions_.isEmpty()) {
                    if (!this.listOfIlInstructionsBuilder_.isEmpty()) {
                        this.listOfIlInstructionsBuilder_.addAllMessages(other.listOfIlInstructions_);
                    } else {
                        this.listOfIlInstructionsBuilder_.dispose();
                        this.listOfIlInstructionsBuilder_ = null;
                        this.listOfIlInstructions_ = other.listOfIlInstructions_;
                        this.bitField0_ &= -2;
                        this.listOfIlInstructionsBuilder_ = IlBlock.alwaysUseFieldBuilders ? getListOfIlInstructionsFieldBuilder() : null;
                    }
                }
                if (!other.getBlockName().isEmpty()) {
                    this.blockName_ = other.blockName_;
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
                IlBlock parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlBlock) IlBlock.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlBlock ilBlock = (IlBlock) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            private void ensureListOfIlInstructionsIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.listOfIlInstructions_ = new ArrayList(this.listOfIlInstructions_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public List<IlInstructionMsg> getListOfIlInstructionsList() {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    return Collections.unmodifiableList(this.listOfIlInstructions_);
                }
                return this.listOfIlInstructionsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public int getListOfIlInstructionsCount() {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    return this.listOfIlInstructions_.size();
                }
                return this.listOfIlInstructionsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public IlInstructionMsg getListOfIlInstructions(int index) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    return this.listOfIlInstructions_.get(index);
                }
                return this.listOfIlInstructionsBuilder_.getMessage(index);
            }

            public Builder setListOfIlInstructions(int index, IlInstructionMsg value) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.set(index, value);
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setListOfIlInstructions(int index, IlInstructionMsg.Builder builderForValue) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addListOfIlInstructions(IlInstructionMsg value) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.add(value);
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addListOfIlInstructions(int index, IlInstructionMsg value) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.add(index, value);
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addListOfIlInstructions(IlInstructionMsg.Builder builderForValue) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addListOfIlInstructions(int index, IlInstructionMsg.Builder builderForValue) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllListOfIlInstructions(Iterable<? extends IlInstructionMsg> values) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    ensureListOfIlInstructionsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.listOfIlInstructions_);
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearListOfIlInstructions() {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    this.listOfIlInstructions_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.clear();
                }
                return this;
            }

            public Builder removeListOfIlInstructions(int index) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    ensureListOfIlInstructionsIsMutable();
                    this.listOfIlInstructions_.remove(index);
                    onChanged();
                } else {
                    this.listOfIlInstructionsBuilder_.remove(index);
                }
                return this;
            }

            public IlInstructionMsg.Builder getListOfIlInstructionsBuilder(int index) {
                return getListOfIlInstructionsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public IlInstructionMsgOrBuilder getListOfIlInstructionsOrBuilder(int index) {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    return this.listOfIlInstructions_.get(index);
                }
                return this.listOfIlInstructionsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public List<? extends IlInstructionMsgOrBuilder> getListOfIlInstructionsOrBuilderList() {
                if (this.listOfIlInstructionsBuilder_ != null) {
                    return this.listOfIlInstructionsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.listOfIlInstructions_);
            }

            public IlInstructionMsg.Builder addListOfIlInstructionsBuilder() {
                return getListOfIlInstructionsFieldBuilder().addBuilder(IlInstructionMsg.getDefaultInstance());
            }

            public IlInstructionMsg.Builder addListOfIlInstructionsBuilder(int index) {
                return getListOfIlInstructionsFieldBuilder().addBuilder(index, IlInstructionMsg.getDefaultInstance());
            }

            public List<IlInstructionMsg.Builder> getListOfIlInstructionsBuilderList() {
                return getListOfIlInstructionsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlInstructionMsg, IlInstructionMsg.Builder, IlInstructionMsgOrBuilder> getListOfIlInstructionsFieldBuilder() {
                if (this.listOfIlInstructionsBuilder_ == null) {
                    this.listOfIlInstructionsBuilder_ = new RepeatedFieldBuilderV3<>(this.listOfIlInstructions_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.listOfIlInstructions_ = null;
                }
                return this.listOfIlInstructionsBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public String getBlockName() {
                Object ref = this.blockName_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.blockName_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlBlockOrBuilder
            public ByteString getBlockNameBytes() {
                Object ref = this.blockName_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.blockName_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setBlockName(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.blockName_ = value;
                onChanged();
                return this;
            }

            public Builder clearBlockName() {
                this.blockName_ = IlBlock.getDefaultInstance().getBlockName();
                onChanged();
                return this;
            }

            public Builder setBlockNameBytes(ByteString value) {
                if (value != null) {
                    IlBlock.checkByteStringIsUtf8(value);
                    this.blockName_ = value;
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

        public static IlBlock getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlBlock> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlBlock> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlBlock getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg.class */
    public static final class IlInstructionMsg extends GeneratedMessageV3 implements IlInstructionMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int OP_CODE_FIELD_NUMBER = 1;
        private int opCode_;
        public static final int METHOD_FIELD_NUMBER = 4;
        private ProtoAssemblyAllTypes.MethodDefinition method_;
        public static final int ARGUMENTS_FIELD_NUMBER = 5;
        private List<IlInstructionMsg> arguments_;
        public static final int VALUE_INSTRUCTION_FIELD_NUMBER = 6;
        private IlInstructionMsg valueInstruction_;
        public static final int VALUE_CONSTANT_STRING_FIELD_NUMBER = 7;
        private volatile Object valueConstantString_;
        public static final int VALUE_CONSTANT_INT32_FIELD_NUMBER = 16;
        private int valueConstantInt32_;
        public static final int VALUE_CONSTANT_INT64_FIELD_NUMBER = 38;
        private long valueConstantInt64_;
        public static final int VALUE_CONSTANT_FLOAT_FIELD_NUMBER = 39;
        private float valueConstantFloat_;
        public static final int VALUE_CONSTANT_DOUBLE_FIELD_NUMBER = 40;
        private double valueConstantDouble_;
        public static final int TARGET_FIELD_NUMBER = 8;
        private IlInstructionMsg target_;
        public static final int TYPE_FIELD_NUMBER = 9;
        private ProtoAssemblyAllTypes.TypeDefinition type_;
        public static final int FIELD_FIELD_NUMBER = 10;
        private ProtoAssemblyAllTypes.FieldDefinition field_;
        public static final int VARIABLE_FIELD_NUMBER = 11;
        private IlVariableMsg variable_;
        public static final int OPERATOR_FIELD_NUMBER = 12;
        private int operator_;
        public static final int SIGN_FIELD_NUMBER = 20;
        private int sign_;
        public static final int LEFT_FIELD_NUMBER = 14;
        private IlInstructionMsg left_;
        public static final int RIGHT_FIELD_NUMBER = 15;
        private IlInstructionMsg right_;
        public static final int TARGET_LABEL_FIELD_NUMBER = 18;
        private volatile Object targetLabel_;
        public static final int COMPARISON_KIND_FIELD_NUMBER = 21;
        private int comparisonKind_;
        public static final int CONDITION_FIELD_NUMBER = 22;
        private IlInstructionMsg condition_;
        public static final int TRUE_INST_FIELD_NUMBER = 23;
        private IlInstructionMsg trueInst_;
        public static final int FALSE_INST_FIELD_NUMBER = 24;
        private IlInstructionMsg falseInst_;
        public static final int ARRAY_FIELD_NUMBER = 30;
        private IlInstructionMsg array_;
        public static final int CONVERSION_KIND_FIELD_NUMBER = 25;
        private int conversionKind_;
        public static final int INPUT_TYPE_FIELD_NUMBER = 26;
        private int inputType_;
        public static final int TARGET_TYPE_FIELD_NUMBER = 28;
        private int targetType_;
        public static final int ARGUMENT_FIELD_NUMBER = 29;
        private IlInstructionMsg argument_;
        public static final int RESULT_TYPE_FIELD_NUMBER = 27;
        private int resultType_;
        public static final int INDICES_FIELD_NUMBER = 31;
        private List<IlInstructionMsg> indices_;
        public static final int TRY_BLOCK_FIELD_NUMBER = 32;
        private IlBlockContainerMsg tryBlock_;
        public static final int HANDLERS_FIELD_NUMBER = 33;
        private List<IlTryCatchHandlerMsg> handlers_;
        public static final int FINALLY_BLOCK_FIELD_NUMBER = 34;
        private IlBlockContainerMsg finallyBlock_;
        public static final int FAULT_BLOCK_FIELD_NUMBER = 35;
        private IlBlockContainerMsg faultBlock_;
        public static final int BODY_FIELD_NUMBER = 37;
        private IlBlockContainerMsg body_;
        public static final int KEY_INSTR_FIELD_NUMBER = 41;
        private IlInstructionMsg keyInstr_;
        public static final int DEFAULT_INST_FIELD_NUMBER = 42;
        private IlInstructionMsg defaultInst_;
        public static final int SWITCH_SECTIONS_FIELD_NUMBER = 43;
        private List<IlSwitchSectionMsg> switchSections_;
        private byte memoizedIsInitialized;
        private static final IlInstructionMsg DEFAULT_INSTANCE = new IlInstructionMsg();
        private static final Parser<IlInstructionMsg> PARSER = new AbstractParser<IlInstructionMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.1
            @Override // com.google.protobuf.Parser
            public IlInstructionMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlInstructionMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlInstructionMsg(GeneratedMessageV3.Builder builder, IlInstructionMsg ilInstructionMsg) {
            this(builder);
        }

        private IlInstructionMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlInstructionMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.opCode_ = 0;
            this.arguments_ = Collections.emptyList();
            this.valueConstantString_ = "";
            this.operator_ = 0;
            this.sign_ = 0;
            this.targetLabel_ = "";
            this.comparisonKind_ = 0;
            this.conversionKind_ = 0;
            this.inputType_ = 0;
            this.targetType_ = 0;
            this.resultType_ = 0;
            this.indices_ = Collections.emptyList();
            this.handlers_ = Collections.emptyList();
            this.switchSections_ = Collections.emptyList();
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlInstructionMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlInstructionMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlInstructionMsg ilInstructionMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlInstructionMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                this.opCode_ = rawValue;
                                break;
                            case 34:
                                ProtoAssemblyAllTypes.MethodDefinition.Builder subBuilder = null;
                                subBuilder = this.method_ != null ? this.method_.toBuilder() : subBuilder;
                                this.method_ = (ProtoAssemblyAllTypes.MethodDefinition) input.readMessage(ProtoAssemblyAllTypes.MethodDefinition.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.method_);
                                    this.method_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 42:
                                if ((mutable_bitField0_ & 1) == 0) {
                                    this.arguments_ = new ArrayList();
                                    mutable_bitField0_ |= 1;
                                }
                                this.arguments_.add((IlInstructionMsg) input.readMessage(parser(), extensionRegistry));
                                break;
                            case 50:
                                Builder subBuilder2 = null;
                                subBuilder2 = this.valueInstruction_ != null ? this.valueInstruction_.toBuilder() : subBuilder2;
                                this.valueInstruction_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.valueInstruction_);
                                    this.valueInstruction_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 58:
                                String s = input.readStringRequireUtf8();
                                this.valueConstantString_ = s;
                                break;
                            case 66:
                                Builder subBuilder3 = null;
                                subBuilder3 = this.target_ != null ? this.target_.toBuilder() : subBuilder3;
                                this.target_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder3 == null) {
                                    break;
                                } else {
                                    subBuilder3.mergeFrom(this.target_);
                                    this.target_ = subBuilder3.buildPartial();
                                    break;
                                }
                            case 74:
                                ProtoAssemblyAllTypes.TypeDefinition.Builder subBuilder4 = null;
                                subBuilder4 = this.type_ != null ? this.type_.toBuilder() : subBuilder4;
                                this.type_ = (ProtoAssemblyAllTypes.TypeDefinition) input.readMessage(ProtoAssemblyAllTypes.TypeDefinition.parser(), extensionRegistry);
                                if (subBuilder4 == null) {
                                    break;
                                } else {
                                    subBuilder4.mergeFrom(this.type_);
                                    this.type_ = subBuilder4.buildPartial();
                                    break;
                                }
                            case 82:
                                ProtoAssemblyAllTypes.FieldDefinition.Builder subBuilder5 = null;
                                subBuilder5 = this.field_ != null ? this.field_.toBuilder() : subBuilder5;
                                this.field_ = (ProtoAssemblyAllTypes.FieldDefinition) input.readMessage(ProtoAssemblyAllTypes.FieldDefinition.parser(), extensionRegistry);
                                if (subBuilder5 == null) {
                                    break;
                                } else {
                                    subBuilder5.mergeFrom(this.field_);
                                    this.field_ = subBuilder5.buildPartial();
                                    break;
                                }
                            case 90:
                                IlVariableMsg.Builder subBuilder6 = null;
                                subBuilder6 = this.variable_ != null ? this.variable_.toBuilder() : subBuilder6;
                                this.variable_ = (IlVariableMsg) input.readMessage(IlVariableMsg.parser(), extensionRegistry);
                                if (subBuilder6 == null) {
                                    break;
                                } else {
                                    subBuilder6.mergeFrom(this.variable_);
                                    this.variable_ = subBuilder6.buildPartial();
                                    break;
                                }
                            case 96:
                                int rawValue2 = input.readEnum();
                                this.operator_ = rawValue2;
                                break;
                            case 114:
                                Builder subBuilder7 = null;
                                subBuilder7 = this.left_ != null ? this.left_.toBuilder() : subBuilder7;
                                this.left_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder7 == null) {
                                    break;
                                } else {
                                    subBuilder7.mergeFrom(this.left_);
                                    this.left_ = subBuilder7.buildPartial();
                                    break;
                                }
                            case 122:
                                Builder subBuilder8 = null;
                                subBuilder8 = this.right_ != null ? this.right_.toBuilder() : subBuilder8;
                                this.right_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder8 == null) {
                                    break;
                                } else {
                                    subBuilder8.mergeFrom(this.right_);
                                    this.right_ = subBuilder8.buildPartial();
                                    break;
                                }
                            case 128:
                                this.valueConstantInt32_ = input.readInt32();
                                break;
                            case 146:
                                String s2 = input.readStringRequireUtf8();
                                this.targetLabel_ = s2;
                                break;
                            case 160:
                                int rawValue3 = input.readEnum();
                                this.sign_ = rawValue3;
                                break;
                            case 168:
                                int rawValue4 = input.readEnum();
                                this.comparisonKind_ = rawValue4;
                                break;
                            case 178:
                                Builder subBuilder9 = null;
                                subBuilder9 = this.condition_ != null ? this.condition_.toBuilder() : subBuilder9;
                                this.condition_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder9 == null) {
                                    break;
                                } else {
                                    subBuilder9.mergeFrom(this.condition_);
                                    this.condition_ = subBuilder9.buildPartial();
                                    break;
                                }
                            case 186:
                                Builder subBuilder10 = null;
                                subBuilder10 = this.trueInst_ != null ? this.trueInst_.toBuilder() : subBuilder10;
                                this.trueInst_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder10 == null) {
                                    break;
                                } else {
                                    subBuilder10.mergeFrom(this.trueInst_);
                                    this.trueInst_ = subBuilder10.buildPartial();
                                    break;
                                }
                            case 194:
                                Builder subBuilder11 = null;
                                subBuilder11 = this.falseInst_ != null ? this.falseInst_.toBuilder() : subBuilder11;
                                this.falseInst_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder11 == null) {
                                    break;
                                } else {
                                    subBuilder11.mergeFrom(this.falseInst_);
                                    this.falseInst_ = subBuilder11.buildPartial();
                                    break;
                                }
                            case 200:
                                int rawValue5 = input.readEnum();
                                this.conversionKind_ = rawValue5;
                                break;
                            case 208:
                                int rawValue6 = input.readEnum();
                                this.inputType_ = rawValue6;
                                break;
                            case 216:
                                int rawValue7 = input.readEnum();
                                this.resultType_ = rawValue7;
                                break;
                            case 224:
                                int rawValue8 = input.readEnum();
                                this.targetType_ = rawValue8;
                                break;
                            case 234:
                                Builder subBuilder12 = null;
                                subBuilder12 = this.argument_ != null ? this.argument_.toBuilder() : subBuilder12;
                                this.argument_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder12 == null) {
                                    break;
                                } else {
                                    subBuilder12.mergeFrom(this.argument_);
                                    this.argument_ = subBuilder12.buildPartial();
                                    break;
                                }
                            case 242:
                                Builder subBuilder13 = null;
                                subBuilder13 = this.array_ != null ? this.array_.toBuilder() : subBuilder13;
                                this.array_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder13 == null) {
                                    break;
                                } else {
                                    subBuilder13.mergeFrom(this.array_);
                                    this.array_ = subBuilder13.buildPartial();
                                    break;
                                }
                            case 250:
                                if ((mutable_bitField0_ & 2) == 0) {
                                    this.indices_ = new ArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.indices_.add((IlInstructionMsg) input.readMessage(parser(), extensionRegistry));
                                break;
                            case 258:
                                IlBlockContainerMsg.Builder subBuilder14 = null;
                                subBuilder14 = this.tryBlock_ != null ? this.tryBlock_.toBuilder() : subBuilder14;
                                this.tryBlock_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder14 == null) {
                                    break;
                                } else {
                                    subBuilder14.mergeFrom(this.tryBlock_);
                                    this.tryBlock_ = subBuilder14.buildPartial();
                                    break;
                                }
                            case 266:
                                if ((mutable_bitField0_ & 4) == 0) {
                                    this.handlers_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                this.handlers_.add((IlTryCatchHandlerMsg) input.readMessage(IlTryCatchHandlerMsg.parser(), extensionRegistry));
                                break;
                            case 274:
                                IlBlockContainerMsg.Builder subBuilder15 = null;
                                subBuilder15 = this.finallyBlock_ != null ? this.finallyBlock_.toBuilder() : subBuilder15;
                                this.finallyBlock_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder15 == null) {
                                    break;
                                } else {
                                    subBuilder15.mergeFrom(this.finallyBlock_);
                                    this.finallyBlock_ = subBuilder15.buildPartial();
                                    break;
                                }
                            case 282:
                                IlBlockContainerMsg.Builder subBuilder16 = null;
                                subBuilder16 = this.faultBlock_ != null ? this.faultBlock_.toBuilder() : subBuilder16;
                                this.faultBlock_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder16 == null) {
                                    break;
                                } else {
                                    subBuilder16.mergeFrom(this.faultBlock_);
                                    this.faultBlock_ = subBuilder16.buildPartial();
                                    break;
                                }
                            case org.hamcrest.generator.qdox.parser.impl.Parser.GREATERTHAN /* 298 */:
                                IlBlockContainerMsg.Builder subBuilder17 = null;
                                subBuilder17 = this.body_ != null ? this.body_.toBuilder() : subBuilder17;
                                this.body_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder17 == null) {
                                    break;
                                } else {
                                    subBuilder17.mergeFrom(this.body_);
                                    this.body_ = subBuilder17.buildPartial();
                                    break;
                                }
                            case 304:
                                this.valueConstantInt64_ = input.readInt64();
                                break;
                            case 317:
                                this.valueConstantFloat_ = input.readFloat();
                                break;
                            case 321:
                                this.valueConstantDouble_ = input.readDouble();
                                break;
                            case 330:
                                Builder subBuilder18 = null;
                                subBuilder18 = this.keyInstr_ != null ? this.keyInstr_.toBuilder() : subBuilder18;
                                this.keyInstr_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder18 == null) {
                                    break;
                                } else {
                                    subBuilder18.mergeFrom(this.keyInstr_);
                                    this.keyInstr_ = subBuilder18.buildPartial();
                                    break;
                                }
                            case 338:
                                Builder subBuilder19 = null;
                                subBuilder19 = this.defaultInst_ != null ? this.defaultInst_.toBuilder() : subBuilder19;
                                this.defaultInst_ = (IlInstructionMsg) input.readMessage(parser(), extensionRegistry);
                                if (subBuilder19 == null) {
                                    break;
                                } else {
                                    subBuilder19.mergeFrom(this.defaultInst_);
                                    this.defaultInst_ = subBuilder19.buildPartial();
                                    break;
                                }
                            case TokenId.WHILE /* 346 */:
                                if ((mutable_bitField0_ & 8) == 0) {
                                    this.switchSections_ = new ArrayList();
                                    mutable_bitField0_ |= 8;
                                }
                                this.switchSections_.add((IlSwitchSectionMsg) input.readMessage(IlSwitchSectionMsg.parser(), extensionRegistry));
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
                    this.arguments_ = Collections.unmodifiableList(this.arguments_);
                }
                if ((mutable_bitField0_ & 2) != 0) {
                    this.indices_ = Collections.unmodifiableList(this.indices_);
                }
                if ((mutable_bitField0_ & 4) != 0) {
                    this.handlers_ = Collections.unmodifiableList(this.handlers_);
                }
                if ((mutable_bitField0_ & 8) != 0) {
                    this.switchSections_ = Collections.unmodifiableList(this.switchSections_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoIlInstructions.internal_static_IlInstructionMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlInstructionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlInstructionMsg.class, Builder.class);
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlOpCode.class */
        public enum IlOpCode implements ProtocolMessageEnum {
            NONE_OP(0),
            NOP(1),
            CALL(4),
            LEAVE(5),
            LDSTR(6),
            STOBJ(7),
            LDFLDA(8),
            LDC_I4(9),
            LDLOC(10),
            LDOBJ(11),
            STLOC(12),
            NEWOBJ(13),
            CALLVIRT(14),
            BINARY_NUMERIC_INSTRUCTION(15),
            BRANCH(16),
            COMP(17),
            IF_INSTRUCTION(18),
            LDSFLDA(19),
            LDNULL(20),
            LDLEN(21),
            CONV(22),
            NEWARR(23),
            LDELEMA(24),
            CASTCLASS(25),
            ISINST(26),
            BOX(27),
            UNBOXANY(28),
            UNBOX(29),
            TRY_CATCH(30),
            LDLOCA(31),
            DEFAULT_VALUE(32),
            NOT(33),
            TRY_FINALLY(34),
            TRY_FAULT(35),
            BLOCK_CONTAINER(36),
            BLOCK(37),
            TRY_CATCH_HANDLER(38),
            RETHROW(39),
            THROW(40),
            DEBUG_BREAK(41),
            CK_FINITE(42),
            CP_BLK(44),
            CP_OBJ(45),
            DUP(46),
            INIT_BLK(47),
            INIT_OBJ(48),
            LDC_I8(49),
            LDC_R4(50),
            LDC_R8(51),
            LD_FLD(52),
            LD_FTN(53),
            LD_SFLD(54),
            LD_TOKEN(55),
            LD_VIRT_FTN(56),
            LOC_ALLOC(57),
            MK_REF_ANY(58),
            NO(59),
            READONLY(60),
            REF_ANY_TYPE(61),
            REF_ANY_VAL(62),
            SIZE_OF(63),
            ST_SFLD(64),
            SWITCH(65),
            TAIL(66),
            UNALIGNED(67),
            VOLATILE(68),
            LD_MEMBER_TOKEN(69),
            LD_TYPE_TOKEN(70),
            INVALID_BRANCH(71),
            CALL_INDIRECT(72),
            UNRECOGNIZED(-1);
            
            public static final int NONE_OP_VALUE = 0;
            public static final int NOP_VALUE = 1;
            public static final int CALL_VALUE = 4;
            public static final int LEAVE_VALUE = 5;
            public static final int LDSTR_VALUE = 6;
            public static final int STOBJ_VALUE = 7;
            public static final int LDFLDA_VALUE = 8;
            public static final int LDC_I4_VALUE = 9;
            public static final int LDLOC_VALUE = 10;
            public static final int LDOBJ_VALUE = 11;
            public static final int STLOC_VALUE = 12;
            public static final int NEWOBJ_VALUE = 13;
            public static final int CALLVIRT_VALUE = 14;
            public static final int BINARY_NUMERIC_INSTRUCTION_VALUE = 15;
            public static final int BRANCH_VALUE = 16;
            public static final int COMP_VALUE = 17;
            public static final int IF_INSTRUCTION_VALUE = 18;
            public static final int LDSFLDA_VALUE = 19;
            public static final int LDNULL_VALUE = 20;
            public static final int LDLEN_VALUE = 21;
            public static final int CONV_VALUE = 22;
            public static final int NEWARR_VALUE = 23;
            public static final int LDELEMA_VALUE = 24;
            public static final int CASTCLASS_VALUE = 25;
            public static final int ISINST_VALUE = 26;
            public static final int BOX_VALUE = 27;
            public static final int UNBOXANY_VALUE = 28;
            public static final int UNBOX_VALUE = 29;
            public static final int TRY_CATCH_VALUE = 30;
            public static final int LDLOCA_VALUE = 31;
            public static final int DEFAULT_VALUE_VALUE = 32;
            public static final int NOT_VALUE = 33;
            public static final int TRY_FINALLY_VALUE = 34;
            public static final int TRY_FAULT_VALUE = 35;
            public static final int BLOCK_CONTAINER_VALUE = 36;
            public static final int BLOCK_VALUE = 37;
            public static final int TRY_CATCH_HANDLER_VALUE = 38;
            public static final int RETHROW_VALUE = 39;
            public static final int THROW_VALUE = 40;
            public static final int DEBUG_BREAK_VALUE = 41;
            public static final int CK_FINITE_VALUE = 42;
            public static final int CP_BLK_VALUE = 44;
            public static final int CP_OBJ_VALUE = 45;
            public static final int DUP_VALUE = 46;
            public static final int INIT_BLK_VALUE = 47;
            public static final int INIT_OBJ_VALUE = 48;
            public static final int LDC_I8_VALUE = 49;
            public static final int LDC_R4_VALUE = 50;
            public static final int LDC_R8_VALUE = 51;
            public static final int LD_FLD_VALUE = 52;
            public static final int LD_FTN_VALUE = 53;
            public static final int LD_SFLD_VALUE = 54;
            public static final int LD_TOKEN_VALUE = 55;
            public static final int LD_VIRT_FTN_VALUE = 56;
            public static final int LOC_ALLOC_VALUE = 57;
            public static final int MK_REF_ANY_VALUE = 58;
            public static final int NO_VALUE = 59;
            public static final int READONLY_VALUE = 60;
            public static final int REF_ANY_TYPE_VALUE = 61;
            public static final int REF_ANY_VAL_VALUE = 62;
            public static final int SIZE_OF_VALUE = 63;
            public static final int ST_SFLD_VALUE = 64;
            public static final int SWITCH_VALUE = 65;
            public static final int TAIL_VALUE = 66;
            public static final int UNALIGNED_VALUE = 67;
            public static final int VOLATILE_VALUE = 68;
            public static final int LD_MEMBER_TOKEN_VALUE = 69;
            public static final int LD_TYPE_TOKEN_VALUE = 70;
            public static final int INVALID_BRANCH_VALUE = 71;
            public static final int CALL_INDIRECT_VALUE = 72;
            private static final Internal.EnumLiteMap<IlOpCode> internalValueMap = new Internal.EnumLiteMap<IlOpCode>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlOpCode.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlOpCode findValueByNumber(int number) {
                    return IlOpCode.forNumber(number);
                }
            };
            private static final IlOpCode[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlOpCode[] valuesCustom() {
                IlOpCode[] valuesCustom = values();
                int length = valuesCustom.length;
                IlOpCode[] ilOpCodeArr = new IlOpCode[length];
                System.arraycopy(valuesCustom, 0, ilOpCodeArr, 0, length);
                return ilOpCodeArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlOpCode valueOf(int value) {
                return forNumber(value);
            }

            public static IlOpCode forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_OP;
                    case 1:
                        return NOP;
                    case 2:
                    case 3:
                    case 43:
                    default:
                        return null;
                    case 4:
                        return CALL;
                    case 5:
                        return LEAVE;
                    case 6:
                        return LDSTR;
                    case 7:
                        return STOBJ;
                    case 8:
                        return LDFLDA;
                    case 9:
                        return LDC_I4;
                    case 10:
                        return LDLOC;
                    case 11:
                        return LDOBJ;
                    case 12:
                        return STLOC;
                    case 13:
                        return NEWOBJ;
                    case 14:
                        return CALLVIRT;
                    case 15:
                        return BINARY_NUMERIC_INSTRUCTION;
                    case 16:
                        return BRANCH;
                    case 17:
                        return COMP;
                    case 18:
                        return IF_INSTRUCTION;
                    case 19:
                        return LDSFLDA;
                    case 20:
                        return LDNULL;
                    case 21:
                        return LDLEN;
                    case 22:
                        return CONV;
                    case 23:
                        return NEWARR;
                    case 24:
                        return LDELEMA;
                    case 25:
                        return CASTCLASS;
                    case 26:
                        return ISINST;
                    case 27:
                        return BOX;
                    case 28:
                        return UNBOXANY;
                    case 29:
                        return UNBOX;
                    case 30:
                        return TRY_CATCH;
                    case 31:
                        return LDLOCA;
                    case 32:
                        return DEFAULT_VALUE;
                    case 33:
                        return NOT;
                    case 34:
                        return TRY_FINALLY;
                    case 35:
                        return TRY_FAULT;
                    case 36:
                        return BLOCK_CONTAINER;
                    case 37:
                        return BLOCK;
                    case 38:
                        return TRY_CATCH_HANDLER;
                    case 39:
                        return RETHROW;
                    case 40:
                        return THROW;
                    case 41:
                        return DEBUG_BREAK;
                    case 42:
                        return CK_FINITE;
                    case 44:
                        return CP_BLK;
                    case 45:
                        return CP_OBJ;
                    case 46:
                        return DUP;
                    case 47:
                        return INIT_BLK;
                    case 48:
                        return INIT_OBJ;
                    case 49:
                        return LDC_I8;
                    case 50:
                        return LDC_R4;
                    case 51:
                        return LDC_R8;
                    case 52:
                        return LD_FLD;
                    case 53:
                        return LD_FTN;
                    case 54:
                        return LD_SFLD;
                    case 55:
                        return LD_TOKEN;
                    case 56:
                        return LD_VIRT_FTN;
                    case 57:
                        return LOC_ALLOC;
                    case 58:
                        return MK_REF_ANY;
                    case 59:
                        return NO;
                    case 60:
                        return READONLY;
                    case 61:
                        return REF_ANY_TYPE;
                    case 62:
                        return REF_ANY_VAL;
                    case 63:
                        return SIZE_OF;
                    case 64:
                        return ST_SFLD;
                    case 65:
                        return SWITCH;
                    case 66:
                        return TAIL;
                    case 67:
                        return UNALIGNED;
                    case 68:
                        return VOLATILE;
                    case 69:
                        return LD_MEMBER_TOKEN;
                    case 70:
                        return LD_TYPE_TOKEN;
                    case 71:
                        return INVALID_BRANCH;
                    case 72:
                        return CALL_INDIRECT;
                }
            }

            public static Internal.EnumLiteMap<IlOpCode> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(0);
            }

            public static IlOpCode valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlOpCode(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlBinaryNumericOperator.class */
        public enum IlBinaryNumericOperator implements ProtocolMessageEnum {
            NONE_BINARY(0),
            Add(1),
            Sub(2),
            Mul(3),
            Div(4),
            Rem(5),
            BitAnd(6),
            BitOr(7),
            BitXor(8),
            ShiftLeft(9),
            ShiftRight(10),
            UNRECOGNIZED(-1);
            
            public static final int NONE_BINARY_VALUE = 0;
            public static final int Add_VALUE = 1;
            public static final int Sub_VALUE = 2;
            public static final int Mul_VALUE = 3;
            public static final int Div_VALUE = 4;
            public static final int Rem_VALUE = 5;
            public static final int BitAnd_VALUE = 6;
            public static final int BitOr_VALUE = 7;
            public static final int BitXor_VALUE = 8;
            public static final int ShiftLeft_VALUE = 9;
            public static final int ShiftRight_VALUE = 10;
            private static final Internal.EnumLiteMap<IlBinaryNumericOperator> internalValueMap = new Internal.EnumLiteMap<IlBinaryNumericOperator>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlBinaryNumericOperator.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlBinaryNumericOperator findValueByNumber(int number) {
                    return IlBinaryNumericOperator.forNumber(number);
                }
            };
            private static final IlBinaryNumericOperator[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlBinaryNumericOperator[] valuesCustom() {
                IlBinaryNumericOperator[] valuesCustom = values();
                int length = valuesCustom.length;
                IlBinaryNumericOperator[] ilBinaryNumericOperatorArr = new IlBinaryNumericOperator[length];
                System.arraycopy(valuesCustom, 0, ilBinaryNumericOperatorArr, 0, length);
                return ilBinaryNumericOperatorArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlBinaryNumericOperator valueOf(int value) {
                return forNumber(value);
            }

            public static IlBinaryNumericOperator forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_BINARY;
                    case 1:
                        return Add;
                    case 2:
                        return Sub;
                    case 3:
                        return Mul;
                    case 4:
                        return Div;
                    case 5:
                        return Rem;
                    case 6:
                        return BitAnd;
                    case 7:
                        return BitOr;
                    case 8:
                        return BitXor;
                    case 9:
                        return ShiftLeft;
                    case 10:
                        return ShiftRight;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlBinaryNumericOperator> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(1);
            }

            public static IlBinaryNumericOperator valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlBinaryNumericOperator(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlSign.class */
        public enum IlSign implements ProtocolMessageEnum {
            NONE_SIGN(0),
            Signed(1),
            Unsigned(2),
            UNRECOGNIZED(-1);
            
            public static final int NONE_SIGN_VALUE = 0;
            public static final int Signed_VALUE = 1;
            public static final int Unsigned_VALUE = 2;
            private static final Internal.EnumLiteMap<IlSign> internalValueMap = new Internal.EnumLiteMap<IlSign>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlSign.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlSign findValueByNumber(int number) {
                    return IlSign.forNumber(number);
                }
            };
            private static final IlSign[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlSign[] valuesCustom() {
                IlSign[] valuesCustom = values();
                int length = valuesCustom.length;
                IlSign[] ilSignArr = new IlSign[length];
                System.arraycopy(valuesCustom, 0, ilSignArr, 0, length);
                return ilSignArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlSign valueOf(int value) {
                return forNumber(value);
            }

            public static IlSign forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_SIGN;
                    case 1:
                        return Signed;
                    case 2:
                        return Unsigned;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlSign> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(2);
            }

            public static IlSign valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlSign(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlComparisonKind.class */
        public enum IlComparisonKind implements ProtocolMessageEnum {
            NONE_KIND(0),
            Equality(1),
            Inequality(2),
            LessThan(3),
            LessThanOrEqual(4),
            GreaterThan(5),
            GreaterThanOrEqual(6),
            UNRECOGNIZED(-1);
            
            public static final int NONE_KIND_VALUE = 0;
            public static final int Equality_VALUE = 1;
            public static final int Inequality_VALUE = 2;
            public static final int LessThan_VALUE = 3;
            public static final int LessThanOrEqual_VALUE = 4;
            public static final int GreaterThan_VALUE = 5;
            public static final int GreaterThanOrEqual_VALUE = 6;
            private static final Internal.EnumLiteMap<IlComparisonKind> internalValueMap = new Internal.EnumLiteMap<IlComparisonKind>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlComparisonKind.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlComparisonKind findValueByNumber(int number) {
                    return IlComparisonKind.forNumber(number);
                }
            };
            private static final IlComparisonKind[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlComparisonKind[] valuesCustom() {
                IlComparisonKind[] valuesCustom = values();
                int length = valuesCustom.length;
                IlComparisonKind[] ilComparisonKindArr = new IlComparisonKind[length];
                System.arraycopy(valuesCustom, 0, ilComparisonKindArr, 0, length);
                return ilComparisonKindArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlComparisonKind valueOf(int value) {
                return forNumber(value);
            }

            public static IlComparisonKind forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_KIND;
                    case 1:
                        return Equality;
                    case 2:
                        return Inequality;
                    case 3:
                        return LessThan;
                    case 4:
                        return LessThanOrEqual;
                    case 5:
                        return GreaterThan;
                    case 6:
                        return GreaterThanOrEqual;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlComparisonKind> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(3);
            }

            public static IlComparisonKind valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlComparisonKind(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlConversionKind.class */
        public enum IlConversionKind implements ProtocolMessageEnum {
            NONE_CONVERSION(0),
            Invalid(1),
            Nop(2),
            IntToFloat(3),
            FloatToInt(4),
            FloatPrecisionChange(5),
            SignExtend(6),
            ZeroExtend(7),
            Truncate(8),
            StopGCTracking(9),
            StartGCTracking(10),
            ObjectInterior(11),
            UNRECOGNIZED(-1);
            
            public static final int NONE_CONVERSION_VALUE = 0;
            public static final int Invalid_VALUE = 1;
            public static final int Nop_VALUE = 2;
            public static final int IntToFloat_VALUE = 3;
            public static final int FloatToInt_VALUE = 4;
            public static final int FloatPrecisionChange_VALUE = 5;
            public static final int SignExtend_VALUE = 6;
            public static final int ZeroExtend_VALUE = 7;
            public static final int Truncate_VALUE = 8;
            public static final int StopGCTracking_VALUE = 9;
            public static final int StartGCTracking_VALUE = 10;
            public static final int ObjectInterior_VALUE = 11;
            private static final Internal.EnumLiteMap<IlConversionKind> internalValueMap = new Internal.EnumLiteMap<IlConversionKind>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlConversionKind.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlConversionKind findValueByNumber(int number) {
                    return IlConversionKind.forNumber(number);
                }
            };
            private static final IlConversionKind[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlConversionKind[] valuesCustom() {
                IlConversionKind[] valuesCustom = values();
                int length = valuesCustom.length;
                IlConversionKind[] ilConversionKindArr = new IlConversionKind[length];
                System.arraycopy(valuesCustom, 0, ilConversionKindArr, 0, length);
                return ilConversionKindArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlConversionKind valueOf(int value) {
                return forNumber(value);
            }

            public static IlConversionKind forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_CONVERSION;
                    case 1:
                        return Invalid;
                    case 2:
                        return Nop;
                    case 3:
                        return IntToFloat;
                    case 4:
                        return FloatToInt;
                    case 5:
                        return FloatPrecisionChange;
                    case 6:
                        return SignExtend;
                    case 7:
                        return ZeroExtend;
                    case 8:
                        return Truncate;
                    case 9:
                        return StopGCTracking;
                    case 10:
                        return StartGCTracking;
                    case 11:
                        return ObjectInterior;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlConversionKind> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(4);
            }

            public static IlConversionKind valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlConversionKind(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlStackType.class */
        public enum IlStackType implements ProtocolMessageEnum {
            NONE_STACK_TYPE(0),
            Unknown_STACK(1),
            I4_STACK(2),
            I_STACK(3),
            I8_STACK(4),
            F4(5),
            F8(6),
            O(7),
            Ref_STACK(8),
            Void(9),
            UNRECOGNIZED(-1);
            
            public static final int NONE_STACK_TYPE_VALUE = 0;
            public static final int Unknown_STACK_VALUE = 1;
            public static final int I4_STACK_VALUE = 2;
            public static final int I_STACK_VALUE = 3;
            public static final int I8_STACK_VALUE = 4;
            public static final int F4_VALUE = 5;
            public static final int F8_VALUE = 6;
            public static final int O_VALUE = 7;
            public static final int Ref_STACK_VALUE = 8;
            public static final int Void_VALUE = 9;
            private static final Internal.EnumLiteMap<IlStackType> internalValueMap = new Internal.EnumLiteMap<IlStackType>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlStackType.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlStackType findValueByNumber(int number) {
                    return IlStackType.forNumber(number);
                }
            };
            private static final IlStackType[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlStackType[] valuesCustom() {
                IlStackType[] valuesCustom = values();
                int length = valuesCustom.length;
                IlStackType[] ilStackTypeArr = new IlStackType[length];
                System.arraycopy(valuesCustom, 0, ilStackTypeArr, 0, length);
                return ilStackTypeArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlStackType valueOf(int value) {
                return forNumber(value);
            }

            public static IlStackType forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_STACK_TYPE;
                    case 1:
                        return Unknown_STACK;
                    case 2:
                        return I4_STACK;
                    case 3:
                        return I_STACK;
                    case 4:
                        return I8_STACK;
                    case 5:
                        return F4;
                    case 6:
                        return F8;
                    case 7:
                        return O;
                    case 8:
                        return Ref_STACK;
                    case 9:
                        return Void;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlStackType> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(5);
            }

            public static IlStackType valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlStackType(int value) {
                this.value = value;
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$IlPrimitiveType.class */
        public enum IlPrimitiveType implements ProtocolMessageEnum {
            NONE_PRIMITIVE_TYPE(0),
            None(1),
            I1(2),
            I2(3),
            I4(4),
            I8(5),
            R4(6),
            R8(7),
            U1(8),
            U2(9),
            U4(10),
            U8(11),
            I(12),
            U(13),
            Ref(14),
            R(15),
            Unknown(16),
            UNRECOGNIZED(-1);
            
            public static final int NONE_PRIMITIVE_TYPE_VALUE = 0;
            public static final int None_VALUE = 1;
            public static final int I1_VALUE = 2;
            public static final int I2_VALUE = 3;
            public static final int I4_VALUE = 4;
            public static final int I8_VALUE = 5;
            public static final int R4_VALUE = 6;
            public static final int R8_VALUE = 7;
            public static final int U1_VALUE = 8;
            public static final int U2_VALUE = 9;
            public static final int U4_VALUE = 10;
            public static final int U8_VALUE = 11;
            public static final int I_VALUE = 12;
            public static final int U_VALUE = 13;
            public static final int Ref_VALUE = 14;
            public static final int R_VALUE = 15;
            public static final int Unknown_VALUE = 16;
            private static final Internal.EnumLiteMap<IlPrimitiveType> internalValueMap = new Internal.EnumLiteMap<IlPrimitiveType>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsg.IlPrimitiveType.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlPrimitiveType findValueByNumber(int number) {
                    return IlPrimitiveType.forNumber(number);
                }
            };
            private static final IlPrimitiveType[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlPrimitiveType[] valuesCustom() {
                IlPrimitiveType[] valuesCustom = values();
                int length = valuesCustom.length;
                IlPrimitiveType[] ilPrimitiveTypeArr = new IlPrimitiveType[length];
                System.arraycopy(valuesCustom, 0, ilPrimitiveTypeArr, 0, length);
                return ilPrimitiveTypeArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlPrimitiveType valueOf(int value) {
                return forNumber(value);
            }

            public static IlPrimitiveType forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE_PRIMITIVE_TYPE;
                    case 1:
                        return None;
                    case 2:
                        return I1;
                    case 3:
                        return I2;
                    case 4:
                        return I4;
                    case 5:
                        return I8;
                    case 6:
                        return R4;
                    case 7:
                        return R8;
                    case 8:
                        return U1;
                    case 9:
                        return U2;
                    case 10:
                        return U4;
                    case 11:
                        return U8;
                    case 12:
                        return I;
                    case 13:
                        return U;
                    case 14:
                        return Ref;
                    case 15:
                        return R;
                    case 16:
                        return Unknown;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlPrimitiveType> internalGetValueMap() {
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
                return IlInstructionMsg.getDescriptor().getEnumTypes().get(6);
            }

            public static IlPrimitiveType valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlPrimitiveType(int value) {
                this.value = value;
            }
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getOpCodeValue() {
            return this.opCode_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlOpCode getOpCode() {
            IlOpCode result = IlOpCode.valueOf(this.opCode_);
            return result == null ? IlOpCode.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasMethod() {
            return this.method_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.MethodDefinition getMethod() {
            return this.method_ == null ? ProtoAssemblyAllTypes.MethodDefinition.getDefaultInstance() : this.method_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.MethodDefinitionOrBuilder getMethodOrBuilder() {
            return getMethod();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<IlInstructionMsg> getArgumentsList() {
            return this.arguments_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<? extends IlInstructionMsgOrBuilder> getArgumentsOrBuilderList() {
            return this.arguments_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getArgumentsCount() {
            return this.arguments_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getArguments(int index) {
            return this.arguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getArgumentsOrBuilder(int index) {
            return this.arguments_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasValueInstruction() {
            return this.valueInstruction_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getValueInstruction() {
            return this.valueInstruction_ == null ? getDefaultInstance() : this.valueInstruction_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getValueInstructionOrBuilder() {
            return getValueInstruction();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public String getValueConstantString() {
            Object ref = this.valueConstantString_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.valueConstantString_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ByteString getValueConstantStringBytes() {
            Object ref = this.valueConstantString_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.valueConstantString_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getValueConstantInt32() {
            return this.valueConstantInt32_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public long getValueConstantInt64() {
            return this.valueConstantInt64_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public float getValueConstantFloat() {
            return this.valueConstantFloat_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public double getValueConstantDouble() {
            return this.valueConstantDouble_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasTarget() {
            return this.target_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getTarget() {
            return this.target_ == null ? getDefaultInstance() : this.target_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getTargetOrBuilder() {
            return getTarget();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.TypeDefinition getType() {
            return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasField() {
            return this.field_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.FieldDefinition getField() {
            return this.field_ == null ? ProtoAssemblyAllTypes.FieldDefinition.getDefaultInstance() : this.field_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ProtoAssemblyAllTypes.FieldDefinitionOrBuilder getFieldOrBuilder() {
            return getField();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasVariable() {
            return this.variable_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlVariableMsg getVariable() {
            return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlVariableMsgOrBuilder getVariableOrBuilder() {
            return getVariable();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getOperatorValue() {
            return this.operator_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBinaryNumericOperator getOperator() {
            IlBinaryNumericOperator result = IlBinaryNumericOperator.valueOf(this.operator_);
            return result == null ? IlBinaryNumericOperator.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getSignValue() {
            return this.sign_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlSign getSign() {
            IlSign result = IlSign.valueOf(this.sign_);
            return result == null ? IlSign.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasLeft() {
            return this.left_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getLeft() {
            return this.left_ == null ? getDefaultInstance() : this.left_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getLeftOrBuilder() {
            return getLeft();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasRight() {
            return this.right_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getRight() {
            return this.right_ == null ? getDefaultInstance() : this.right_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getRightOrBuilder() {
            return getRight();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public String getTargetLabel() {
            Object ref = this.targetLabel_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            this.targetLabel_ = s;
            return s;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public ByteString getTargetLabelBytes() {
            Object ref = this.targetLabel_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.targetLabel_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getComparisonKindValue() {
            return this.comparisonKind_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlComparisonKind getComparisonKind() {
            IlComparisonKind result = IlComparisonKind.valueOf(this.comparisonKind_);
            return result == null ? IlComparisonKind.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasCondition() {
            return this.condition_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getCondition() {
            return this.condition_ == null ? getDefaultInstance() : this.condition_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getConditionOrBuilder() {
            return getCondition();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasTrueInst() {
            return this.trueInst_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getTrueInst() {
            return this.trueInst_ == null ? getDefaultInstance() : this.trueInst_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getTrueInstOrBuilder() {
            return getTrueInst();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasFalseInst() {
            return this.falseInst_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getFalseInst() {
            return this.falseInst_ == null ? getDefaultInstance() : this.falseInst_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getFalseInstOrBuilder() {
            return getFalseInst();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasArray() {
            return this.array_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getArray() {
            return this.array_ == null ? getDefaultInstance() : this.array_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getArrayOrBuilder() {
            return getArray();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getConversionKindValue() {
            return this.conversionKind_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlConversionKind getConversionKind() {
            IlConversionKind result = IlConversionKind.valueOf(this.conversionKind_);
            return result == null ? IlConversionKind.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getInputTypeValue() {
            return this.inputType_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlStackType getInputType() {
            IlStackType result = IlStackType.valueOf(this.inputType_);
            return result == null ? IlStackType.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getTargetTypeValue() {
            return this.targetType_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlPrimitiveType getTargetType() {
            IlPrimitiveType result = IlPrimitiveType.valueOf(this.targetType_);
            return result == null ? IlPrimitiveType.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasArgument() {
            return this.argument_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getArgument() {
            return this.argument_ == null ? getDefaultInstance() : this.argument_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getArgumentOrBuilder() {
            return getArgument();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getResultTypeValue() {
            return this.resultType_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlStackType getResultType() {
            IlStackType result = IlStackType.valueOf(this.resultType_);
            return result == null ? IlStackType.UNRECOGNIZED : result;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<IlInstructionMsg> getIndicesList() {
            return this.indices_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<? extends IlInstructionMsgOrBuilder> getIndicesOrBuilderList() {
            return this.indices_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getIndicesCount() {
            return this.indices_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getIndices(int index) {
            return this.indices_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getIndicesOrBuilder(int index) {
            return this.indices_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasTryBlock() {
            return this.tryBlock_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsg getTryBlock() {
            return this.tryBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.tryBlock_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getTryBlockOrBuilder() {
            return getTryBlock();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<IlTryCatchHandlerMsg> getHandlersList() {
            return this.handlers_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<? extends IlTryCatchHandlerMsgOrBuilder> getHandlersOrBuilderList() {
            return this.handlers_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getHandlersCount() {
            return this.handlers_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlTryCatchHandlerMsg getHandlers(int index) {
            return this.handlers_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlTryCatchHandlerMsgOrBuilder getHandlersOrBuilder(int index) {
            return this.handlers_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasFinallyBlock() {
            return this.finallyBlock_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsg getFinallyBlock() {
            return this.finallyBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.finallyBlock_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getFinallyBlockOrBuilder() {
            return getFinallyBlock();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasFaultBlock() {
            return this.faultBlock_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsg getFaultBlock() {
            return this.faultBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.faultBlock_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getFaultBlockOrBuilder() {
            return getFaultBlock();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasBody() {
            return this.body_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsg getBody() {
            return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
            return getBody();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasKeyInstr() {
            return this.keyInstr_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getKeyInstr() {
            return this.keyInstr_ == null ? getDefaultInstance() : this.keyInstr_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getKeyInstrOrBuilder() {
            return getKeyInstr();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public boolean hasDefaultInst() {
            return this.defaultInst_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsg getDefaultInst() {
            return this.defaultInst_ == null ? getDefaultInstance() : this.defaultInst_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlInstructionMsgOrBuilder getDefaultInstOrBuilder() {
            return getDefaultInst();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<IlSwitchSectionMsg> getSwitchSectionsList() {
            return this.switchSections_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public List<? extends IlSwitchSectionMsgOrBuilder> getSwitchSectionsOrBuilderList() {
            return this.switchSections_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public int getSwitchSectionsCount() {
            return this.switchSections_.size();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlSwitchSectionMsg getSwitchSections(int index) {
            return this.switchSections_.get(index);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
        public IlSwitchSectionMsgOrBuilder getSwitchSectionsOrBuilder(int index) {
            return this.switchSections_.get(index);
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
            if (this.opCode_ != IlOpCode.NONE_OP.getNumber()) {
                output.writeEnum(1, this.opCode_);
            }
            if (this.method_ != null) {
                output.writeMessage(4, getMethod());
            }
            for (int i = 0; i < this.arguments_.size(); i++) {
                output.writeMessage(5, this.arguments_.get(i));
            }
            if (this.valueInstruction_ != null) {
                output.writeMessage(6, getValueInstruction());
            }
            if (!getValueConstantStringBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 7, this.valueConstantString_);
            }
            if (this.target_ != null) {
                output.writeMessage(8, getTarget());
            }
            if (this.type_ != null) {
                output.writeMessage(9, getType());
            }
            if (this.field_ != null) {
                output.writeMessage(10, getField());
            }
            if (this.variable_ != null) {
                output.writeMessage(11, getVariable());
            }
            if (this.operator_ != IlBinaryNumericOperator.NONE_BINARY.getNumber()) {
                output.writeEnum(12, this.operator_);
            }
            if (this.left_ != null) {
                output.writeMessage(14, getLeft());
            }
            if (this.right_ != null) {
                output.writeMessage(15, getRight());
            }
            if (this.valueConstantInt32_ != 0) {
                output.writeInt32(16, this.valueConstantInt32_);
            }
            if (!getTargetLabelBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 18, this.targetLabel_);
            }
            if (this.sign_ != IlSign.NONE_SIGN.getNumber()) {
                output.writeEnum(20, this.sign_);
            }
            if (this.comparisonKind_ != IlComparisonKind.NONE_KIND.getNumber()) {
                output.writeEnum(21, this.comparisonKind_);
            }
            if (this.condition_ != null) {
                output.writeMessage(22, getCondition());
            }
            if (this.trueInst_ != null) {
                output.writeMessage(23, getTrueInst());
            }
            if (this.falseInst_ != null) {
                output.writeMessage(24, getFalseInst());
            }
            if (this.conversionKind_ != IlConversionKind.NONE_CONVERSION.getNumber()) {
                output.writeEnum(25, this.conversionKind_);
            }
            if (this.inputType_ != IlStackType.NONE_STACK_TYPE.getNumber()) {
                output.writeEnum(26, this.inputType_);
            }
            if (this.resultType_ != IlStackType.NONE_STACK_TYPE.getNumber()) {
                output.writeEnum(27, this.resultType_);
            }
            if (this.targetType_ != IlPrimitiveType.NONE_PRIMITIVE_TYPE.getNumber()) {
                output.writeEnum(28, this.targetType_);
            }
            if (this.argument_ != null) {
                output.writeMessage(29, getArgument());
            }
            if (this.array_ != null) {
                output.writeMessage(30, getArray());
            }
            for (int i2 = 0; i2 < this.indices_.size(); i2++) {
                output.writeMessage(31, this.indices_.get(i2));
            }
            if (this.tryBlock_ != null) {
                output.writeMessage(32, getTryBlock());
            }
            for (int i3 = 0; i3 < this.handlers_.size(); i3++) {
                output.writeMessage(33, this.handlers_.get(i3));
            }
            if (this.finallyBlock_ != null) {
                output.writeMessage(34, getFinallyBlock());
            }
            if (this.faultBlock_ != null) {
                output.writeMessage(35, getFaultBlock());
            }
            if (this.body_ != null) {
                output.writeMessage(37, getBody());
            }
            if (this.valueConstantInt64_ != 0) {
                output.writeInt64(38, this.valueConstantInt64_);
            }
            if (this.valueConstantFloat_ != 0.0f) {
                output.writeFloat(39, this.valueConstantFloat_);
            }
            if (this.valueConstantDouble_ != Const.default_value_double) {
                output.writeDouble(40, this.valueConstantDouble_);
            }
            if (this.keyInstr_ != null) {
                output.writeMessage(41, getKeyInstr());
            }
            if (this.defaultInst_ != null) {
                output.writeMessage(42, getDefaultInst());
            }
            for (int i4 = 0; i4 < this.switchSections_.size(); i4++) {
                output.writeMessage(43, this.switchSections_.get(i4));
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
            if (this.opCode_ != IlOpCode.NONE_OP.getNumber()) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.opCode_);
            }
            if (this.method_ != null) {
                size2 += CodedOutputStream.computeMessageSize(4, getMethod());
            }
            for (int i = 0; i < this.arguments_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(5, this.arguments_.get(i));
            }
            if (this.valueInstruction_ != null) {
                size2 += CodedOutputStream.computeMessageSize(6, getValueInstruction());
            }
            if (!getValueConstantStringBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(7, this.valueConstantString_);
            }
            if (this.target_ != null) {
                size2 += CodedOutputStream.computeMessageSize(8, getTarget());
            }
            if (this.type_ != null) {
                size2 += CodedOutputStream.computeMessageSize(9, getType());
            }
            if (this.field_ != null) {
                size2 += CodedOutputStream.computeMessageSize(10, getField());
            }
            if (this.variable_ != null) {
                size2 += CodedOutputStream.computeMessageSize(11, getVariable());
            }
            if (this.operator_ != IlBinaryNumericOperator.NONE_BINARY.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(12, this.operator_);
            }
            if (this.left_ != null) {
                size2 += CodedOutputStream.computeMessageSize(14, getLeft());
            }
            if (this.right_ != null) {
                size2 += CodedOutputStream.computeMessageSize(15, getRight());
            }
            if (this.valueConstantInt32_ != 0) {
                size2 += CodedOutputStream.computeInt32Size(16, this.valueConstantInt32_);
            }
            if (!getTargetLabelBytes().isEmpty()) {
                size2 += GeneratedMessageV3.computeStringSize(18, this.targetLabel_);
            }
            if (this.sign_ != IlSign.NONE_SIGN.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(20, this.sign_);
            }
            if (this.comparisonKind_ != IlComparisonKind.NONE_KIND.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(21, this.comparisonKind_);
            }
            if (this.condition_ != null) {
                size2 += CodedOutputStream.computeMessageSize(22, getCondition());
            }
            if (this.trueInst_ != null) {
                size2 += CodedOutputStream.computeMessageSize(23, getTrueInst());
            }
            if (this.falseInst_ != null) {
                size2 += CodedOutputStream.computeMessageSize(24, getFalseInst());
            }
            if (this.conversionKind_ != IlConversionKind.NONE_CONVERSION.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(25, this.conversionKind_);
            }
            if (this.inputType_ != IlStackType.NONE_STACK_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(26, this.inputType_);
            }
            if (this.resultType_ != IlStackType.NONE_STACK_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(27, this.resultType_);
            }
            if (this.targetType_ != IlPrimitiveType.NONE_PRIMITIVE_TYPE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(28, this.targetType_);
            }
            if (this.argument_ != null) {
                size2 += CodedOutputStream.computeMessageSize(29, getArgument());
            }
            if (this.array_ != null) {
                size2 += CodedOutputStream.computeMessageSize(30, getArray());
            }
            for (int i2 = 0; i2 < this.indices_.size(); i2++) {
                size2 += CodedOutputStream.computeMessageSize(31, this.indices_.get(i2));
            }
            if (this.tryBlock_ != null) {
                size2 += CodedOutputStream.computeMessageSize(32, getTryBlock());
            }
            for (int i3 = 0; i3 < this.handlers_.size(); i3++) {
                size2 += CodedOutputStream.computeMessageSize(33, this.handlers_.get(i3));
            }
            if (this.finallyBlock_ != null) {
                size2 += CodedOutputStream.computeMessageSize(34, getFinallyBlock());
            }
            if (this.faultBlock_ != null) {
                size2 += CodedOutputStream.computeMessageSize(35, getFaultBlock());
            }
            if (this.body_ != null) {
                size2 += CodedOutputStream.computeMessageSize(37, getBody());
            }
            if (this.valueConstantInt64_ != 0) {
                size2 += CodedOutputStream.computeInt64Size(38, this.valueConstantInt64_);
            }
            if (this.valueConstantFloat_ != 0.0f) {
                size2 += CodedOutputStream.computeFloatSize(39, this.valueConstantFloat_);
            }
            if (this.valueConstantDouble_ != Const.default_value_double) {
                size2 += CodedOutputStream.computeDoubleSize(40, this.valueConstantDouble_);
            }
            if (this.keyInstr_ != null) {
                size2 += CodedOutputStream.computeMessageSize(41, getKeyInstr());
            }
            if (this.defaultInst_ != null) {
                size2 += CodedOutputStream.computeMessageSize(42, getDefaultInst());
            }
            for (int i4 = 0; i4 < this.switchSections_.size(); i4++) {
                size2 += CodedOutputStream.computeMessageSize(43, this.switchSections_.get(i4));
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
            if (!(obj instanceof IlInstructionMsg)) {
                return super.equals(obj);
            }
            IlInstructionMsg other = (IlInstructionMsg) obj;
            if (this.opCode_ != other.opCode_ || hasMethod() != other.hasMethod()) {
                return false;
            }
            if ((hasMethod() && !getMethod().equals(other.getMethod())) || !getArgumentsList().equals(other.getArgumentsList()) || hasValueInstruction() != other.hasValueInstruction()) {
                return false;
            }
            if ((hasValueInstruction() && !getValueInstruction().equals(other.getValueInstruction())) || !getValueConstantString().equals(other.getValueConstantString()) || getValueConstantInt32() != other.getValueConstantInt32() || getValueConstantInt64() != other.getValueConstantInt64() || Float.floatToIntBits(getValueConstantFloat()) != Float.floatToIntBits(other.getValueConstantFloat()) || Double.doubleToLongBits(getValueConstantDouble()) != Double.doubleToLongBits(other.getValueConstantDouble()) || hasTarget() != other.hasTarget()) {
                return false;
            }
            if ((hasTarget() && !getTarget().equals(other.getTarget())) || hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || hasField() != other.hasField()) {
                return false;
            }
            if ((hasField() && !getField().equals(other.getField())) || hasVariable() != other.hasVariable()) {
                return false;
            }
            if ((hasVariable() && !getVariable().equals(other.getVariable())) || this.operator_ != other.operator_ || this.sign_ != other.sign_ || hasLeft() != other.hasLeft()) {
                return false;
            }
            if ((hasLeft() && !getLeft().equals(other.getLeft())) || hasRight() != other.hasRight()) {
                return false;
            }
            if ((hasRight() && !getRight().equals(other.getRight())) || !getTargetLabel().equals(other.getTargetLabel()) || this.comparisonKind_ != other.comparisonKind_ || hasCondition() != other.hasCondition()) {
                return false;
            }
            if ((hasCondition() && !getCondition().equals(other.getCondition())) || hasTrueInst() != other.hasTrueInst()) {
                return false;
            }
            if ((hasTrueInst() && !getTrueInst().equals(other.getTrueInst())) || hasFalseInst() != other.hasFalseInst()) {
                return false;
            }
            if ((hasFalseInst() && !getFalseInst().equals(other.getFalseInst())) || hasArray() != other.hasArray()) {
                return false;
            }
            if ((hasArray() && !getArray().equals(other.getArray())) || this.conversionKind_ != other.conversionKind_ || this.inputType_ != other.inputType_ || this.targetType_ != other.targetType_ || hasArgument() != other.hasArgument()) {
                return false;
            }
            if ((hasArgument() && !getArgument().equals(other.getArgument())) || this.resultType_ != other.resultType_ || !getIndicesList().equals(other.getIndicesList()) || hasTryBlock() != other.hasTryBlock()) {
                return false;
            }
            if ((hasTryBlock() && !getTryBlock().equals(other.getTryBlock())) || !getHandlersList().equals(other.getHandlersList()) || hasFinallyBlock() != other.hasFinallyBlock()) {
                return false;
            }
            if ((hasFinallyBlock() && !getFinallyBlock().equals(other.getFinallyBlock())) || hasFaultBlock() != other.hasFaultBlock()) {
                return false;
            }
            if ((hasFaultBlock() && !getFaultBlock().equals(other.getFaultBlock())) || hasBody() != other.hasBody()) {
                return false;
            }
            if ((hasBody() && !getBody().equals(other.getBody())) || hasKeyInstr() != other.hasKeyInstr()) {
                return false;
            }
            if ((hasKeyInstr() && !getKeyInstr().equals(other.getKeyInstr())) || hasDefaultInst() != other.hasDefaultInst()) {
                return false;
            }
            if ((hasDefaultInst() && !getDefaultInst().equals(other.getDefaultInst())) || !getSwitchSectionsList().equals(other.getSwitchSectionsList()) || !this.unknownFields.equals(other.unknownFields)) {
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
            int hash2 = (53 * ((37 * hash) + 1)) + this.opCode_;
            if (hasMethod()) {
                hash2 = (53 * ((37 * hash2) + 4)) + getMethod().hashCode();
            }
            if (getArgumentsCount() > 0) {
                hash2 = (53 * ((37 * hash2) + 5)) + getArgumentsList().hashCode();
            }
            if (hasValueInstruction()) {
                hash2 = (53 * ((37 * hash2) + 6)) + getValueInstruction().hashCode();
            }
            int hash3 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash2) + 7)) + getValueConstantString().hashCode())) + 16)) + getValueConstantInt32())) + 38)) + Internal.hashLong(getValueConstantInt64()))) + 39)) + Float.floatToIntBits(getValueConstantFloat()))) + 40)) + Internal.hashLong(Double.doubleToLongBits(getValueConstantDouble()));
            if (hasTarget()) {
                hash3 = (53 * ((37 * hash3) + 8)) + getTarget().hashCode();
            }
            if (hasType()) {
                hash3 = (53 * ((37 * hash3) + 9)) + getType().hashCode();
            }
            if (hasField()) {
                hash3 = (53 * ((37 * hash3) + 10)) + getField().hashCode();
            }
            if (hasVariable()) {
                hash3 = (53 * ((37 * hash3) + 11)) + getVariable().hashCode();
            }
            int hash4 = (53 * ((37 * ((53 * ((37 * hash3) + 12)) + this.operator_)) + 20)) + this.sign_;
            if (hasLeft()) {
                hash4 = (53 * ((37 * hash4) + 14)) + getLeft().hashCode();
            }
            if (hasRight()) {
                hash4 = (53 * ((37 * hash4) + 15)) + getRight().hashCode();
            }
            int hash5 = (53 * ((37 * ((53 * ((37 * hash4) + 18)) + getTargetLabel().hashCode())) + 21)) + this.comparisonKind_;
            if (hasCondition()) {
                hash5 = (53 * ((37 * hash5) + 22)) + getCondition().hashCode();
            }
            if (hasTrueInst()) {
                hash5 = (53 * ((37 * hash5) + 23)) + getTrueInst().hashCode();
            }
            if (hasFalseInst()) {
                hash5 = (53 * ((37 * hash5) + 24)) + getFalseInst().hashCode();
            }
            if (hasArray()) {
                hash5 = (53 * ((37 * hash5) + 30)) + getArray().hashCode();
            }
            int hash6 = (53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash5) + 25)) + this.conversionKind_)) + 26)) + this.inputType_)) + 28)) + this.targetType_;
            if (hasArgument()) {
                hash6 = (53 * ((37 * hash6) + 29)) + getArgument().hashCode();
            }
            int hash7 = (53 * ((37 * hash6) + 27)) + this.resultType_;
            if (getIndicesCount() > 0) {
                hash7 = (53 * ((37 * hash7) + 31)) + getIndicesList().hashCode();
            }
            if (hasTryBlock()) {
                hash7 = (53 * ((37 * hash7) + 32)) + getTryBlock().hashCode();
            }
            if (getHandlersCount() > 0) {
                hash7 = (53 * ((37 * hash7) + 33)) + getHandlersList().hashCode();
            }
            if (hasFinallyBlock()) {
                hash7 = (53 * ((37 * hash7) + 34)) + getFinallyBlock().hashCode();
            }
            if (hasFaultBlock()) {
                hash7 = (53 * ((37 * hash7) + 35)) + getFaultBlock().hashCode();
            }
            if (hasBody()) {
                hash7 = (53 * ((37 * hash7) + 37)) + getBody().hashCode();
            }
            if (hasKeyInstr()) {
                hash7 = (53 * ((37 * hash7) + 41)) + getKeyInstr().hashCode();
            }
            if (hasDefaultInst()) {
                hash7 = (53 * ((37 * hash7) + 42)) + getDefaultInst().hashCode();
            }
            if (getSwitchSectionsCount() > 0) {
                hash7 = (53 * ((37 * hash7) + 43)) + getSwitchSectionsList().hashCode();
            }
            int hash8 = (29 * hash7) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash8;
            return hash8;
        }

        public static IlInstructionMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlInstructionMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlInstructionMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlInstructionMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlInstructionMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlInstructionMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlInstructionMsg parseFrom(InputStream input) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlInstructionMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlInstructionMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlInstructionMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlInstructionMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlInstructionMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlInstructionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlInstructionMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlInstructionMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlInstructionMsgOrBuilder {
            private int bitField0_;
            private int opCode_;
            private ProtoAssemblyAllTypes.MethodDefinition method_;
            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.MethodDefinition, ProtoAssemblyAllTypes.MethodDefinition.Builder, ProtoAssemblyAllTypes.MethodDefinitionOrBuilder> methodBuilder_;
            private List<IlInstructionMsg> arguments_;
            private RepeatedFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> argumentsBuilder_;
            private IlInstructionMsg valueInstruction_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> valueInstructionBuilder_;
            private Object valueConstantString_;
            private int valueConstantInt32_;
            private long valueConstantInt64_;
            private float valueConstantFloat_;
            private double valueConstantDouble_;
            private IlInstructionMsg target_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> targetBuilder_;
            private ProtoAssemblyAllTypes.TypeDefinition type_;
            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.TypeDefinition, ProtoAssemblyAllTypes.TypeDefinition.Builder, ProtoAssemblyAllTypes.TypeDefinitionOrBuilder> typeBuilder_;
            private ProtoAssemblyAllTypes.FieldDefinition field_;
            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.FieldDefinition, ProtoAssemblyAllTypes.FieldDefinition.Builder, ProtoAssemblyAllTypes.FieldDefinitionOrBuilder> fieldBuilder_;
            private IlVariableMsg variable_;
            private SingleFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> variableBuilder_;
            private int operator_;
            private int sign_;
            private IlInstructionMsg left_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> leftBuilder_;
            private IlInstructionMsg right_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> rightBuilder_;
            private Object targetLabel_;
            private int comparisonKind_;
            private IlInstructionMsg condition_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> conditionBuilder_;
            private IlInstructionMsg trueInst_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> trueInstBuilder_;
            private IlInstructionMsg falseInst_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> falseInstBuilder_;
            private IlInstructionMsg array_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> arrayBuilder_;
            private int conversionKind_;
            private int inputType_;
            private int targetType_;
            private IlInstructionMsg argument_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> argumentBuilder_;
            private int resultType_;
            private List<IlInstructionMsg> indices_;
            private RepeatedFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> indicesBuilder_;
            private IlBlockContainerMsg tryBlock_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> tryBlockBuilder_;
            private List<IlTryCatchHandlerMsg> handlers_;
            private RepeatedFieldBuilderV3<IlTryCatchHandlerMsg, IlTryCatchHandlerMsg.Builder, IlTryCatchHandlerMsgOrBuilder> handlersBuilder_;
            private IlBlockContainerMsg finallyBlock_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> finallyBlockBuilder_;
            private IlBlockContainerMsg faultBlock_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> faultBlockBuilder_;
            private IlBlockContainerMsg body_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> bodyBuilder_;
            private IlInstructionMsg keyInstr_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> keyInstrBuilder_;
            private IlInstructionMsg defaultInst_;
            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> defaultInstBuilder_;
            private List<IlSwitchSectionMsg> switchSections_;
            private RepeatedFieldBuilderV3<IlSwitchSectionMsg, IlSwitchSectionMsg.Builder, IlSwitchSectionMsgOrBuilder> switchSectionsBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlInstructionMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlInstructionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlInstructionMsg.class, Builder.class);
            }

            private Builder() {
                this.opCode_ = 0;
                this.arguments_ = Collections.emptyList();
                this.valueConstantString_ = "";
                this.operator_ = 0;
                this.sign_ = 0;
                this.targetLabel_ = "";
                this.comparisonKind_ = 0;
                this.conversionKind_ = 0;
                this.inputType_ = 0;
                this.targetType_ = 0;
                this.resultType_ = 0;
                this.indices_ = Collections.emptyList();
                this.handlers_ = Collections.emptyList();
                this.switchSections_ = Collections.emptyList();
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
                this.opCode_ = 0;
                this.arguments_ = Collections.emptyList();
                this.valueConstantString_ = "";
                this.operator_ = 0;
                this.sign_ = 0;
                this.targetLabel_ = "";
                this.comparisonKind_ = 0;
                this.conversionKind_ = 0;
                this.inputType_ = 0;
                this.targetType_ = 0;
                this.resultType_ = 0;
                this.indices_ = Collections.emptyList();
                this.handlers_ = Collections.emptyList();
                this.switchSections_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (IlInstructionMsg.alwaysUseFieldBuilders) {
                    getArgumentsFieldBuilder();
                    getIndicesFieldBuilder();
                    getHandlersFieldBuilder();
                    getSwitchSectionsFieldBuilder();
                }
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.opCode_ = 0;
                if (this.methodBuilder_ == null) {
                    this.method_ = null;
                } else {
                    this.method_ = null;
                    this.methodBuilder_ = null;
                }
                if (this.argumentsBuilder_ == null) {
                    this.arguments_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                } else {
                    this.argumentsBuilder_.clear();
                }
                if (this.valueInstructionBuilder_ == null) {
                    this.valueInstruction_ = null;
                } else {
                    this.valueInstruction_ = null;
                    this.valueInstructionBuilder_ = null;
                }
                this.valueConstantString_ = "";
                this.valueConstantInt32_ = 0;
                this.valueConstantInt64_ = 0L;
                this.valueConstantFloat_ = 0.0f;
                this.valueConstantDouble_ = Const.default_value_double;
                if (this.targetBuilder_ == null) {
                    this.target_ = null;
                } else {
                    this.target_ = null;
                    this.targetBuilder_ = null;
                }
                if (this.typeBuilder_ == null) {
                    this.type_ = null;
                } else {
                    this.type_ = null;
                    this.typeBuilder_ = null;
                }
                if (this.fieldBuilder_ == null) {
                    this.field_ = null;
                } else {
                    this.field_ = null;
                    this.fieldBuilder_ = null;
                }
                if (this.variableBuilder_ == null) {
                    this.variable_ = null;
                } else {
                    this.variable_ = null;
                    this.variableBuilder_ = null;
                }
                this.operator_ = 0;
                this.sign_ = 0;
                if (this.leftBuilder_ == null) {
                    this.left_ = null;
                } else {
                    this.left_ = null;
                    this.leftBuilder_ = null;
                }
                if (this.rightBuilder_ == null) {
                    this.right_ = null;
                } else {
                    this.right_ = null;
                    this.rightBuilder_ = null;
                }
                this.targetLabel_ = "";
                this.comparisonKind_ = 0;
                if (this.conditionBuilder_ == null) {
                    this.condition_ = null;
                } else {
                    this.condition_ = null;
                    this.conditionBuilder_ = null;
                }
                if (this.trueInstBuilder_ == null) {
                    this.trueInst_ = null;
                } else {
                    this.trueInst_ = null;
                    this.trueInstBuilder_ = null;
                }
                if (this.falseInstBuilder_ == null) {
                    this.falseInst_ = null;
                } else {
                    this.falseInst_ = null;
                    this.falseInstBuilder_ = null;
                }
                if (this.arrayBuilder_ == null) {
                    this.array_ = null;
                } else {
                    this.array_ = null;
                    this.arrayBuilder_ = null;
                }
                this.conversionKind_ = 0;
                this.inputType_ = 0;
                this.targetType_ = 0;
                if (this.argumentBuilder_ == null) {
                    this.argument_ = null;
                } else {
                    this.argument_ = null;
                    this.argumentBuilder_ = null;
                }
                this.resultType_ = 0;
                if (this.indicesBuilder_ == null) {
                    this.indices_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                } else {
                    this.indicesBuilder_.clear();
                }
                if (this.tryBlockBuilder_ == null) {
                    this.tryBlock_ = null;
                } else {
                    this.tryBlock_ = null;
                    this.tryBlockBuilder_ = null;
                }
                if (this.handlersBuilder_ == null) {
                    this.handlers_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.handlersBuilder_.clear();
                }
                if (this.finallyBlockBuilder_ == null) {
                    this.finallyBlock_ = null;
                } else {
                    this.finallyBlock_ = null;
                    this.finallyBlockBuilder_ = null;
                }
                if (this.faultBlockBuilder_ == null) {
                    this.faultBlock_ = null;
                } else {
                    this.faultBlock_ = null;
                    this.faultBlockBuilder_ = null;
                }
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                if (this.keyInstrBuilder_ == null) {
                    this.keyInstr_ = null;
                } else {
                    this.keyInstr_ = null;
                    this.keyInstrBuilder_ = null;
                }
                if (this.defaultInstBuilder_ == null) {
                    this.defaultInst_ = null;
                } else {
                    this.defaultInst_ = null;
                    this.defaultInstBuilder_ = null;
                }
                if (this.switchSectionsBuilder_ == null) {
                    this.switchSections_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                } else {
                    this.switchSectionsBuilder_.clear();
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlInstructionMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlInstructionMsg getDefaultInstanceForType() {
                return IlInstructionMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlInstructionMsg build() {
                IlInstructionMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlInstructionMsg buildPartial() {
                IlInstructionMsg result = new IlInstructionMsg(this, (IlInstructionMsg) null);
                int i = this.bitField0_;
                result.opCode_ = this.opCode_;
                if (this.methodBuilder_ == null) {
                    result.method_ = this.method_;
                } else {
                    result.method_ = this.methodBuilder_.build();
                }
                if (this.argumentsBuilder_ != null) {
                    result.arguments_ = this.argumentsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 1) != 0) {
                        this.arguments_ = Collections.unmodifiableList(this.arguments_);
                        this.bitField0_ &= -2;
                    }
                    result.arguments_ = this.arguments_;
                }
                if (this.valueInstructionBuilder_ == null) {
                    result.valueInstruction_ = this.valueInstruction_;
                } else {
                    result.valueInstruction_ = this.valueInstructionBuilder_.build();
                }
                result.valueConstantString_ = this.valueConstantString_;
                result.valueConstantInt32_ = this.valueConstantInt32_;
                result.valueConstantInt64_ = this.valueConstantInt64_;
                result.valueConstantFloat_ = this.valueConstantFloat_;
                result.valueConstantDouble_ = this.valueConstantDouble_;
                if (this.targetBuilder_ == null) {
                    result.target_ = this.target_;
                } else {
                    result.target_ = this.targetBuilder_.build();
                }
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                if (this.fieldBuilder_ == null) {
                    result.field_ = this.field_;
                } else {
                    result.field_ = this.fieldBuilder_.build();
                }
                if (this.variableBuilder_ == null) {
                    result.variable_ = this.variable_;
                } else {
                    result.variable_ = this.variableBuilder_.build();
                }
                result.operator_ = this.operator_;
                result.sign_ = this.sign_;
                if (this.leftBuilder_ == null) {
                    result.left_ = this.left_;
                } else {
                    result.left_ = this.leftBuilder_.build();
                }
                if (this.rightBuilder_ == null) {
                    result.right_ = this.right_;
                } else {
                    result.right_ = this.rightBuilder_.build();
                }
                result.targetLabel_ = this.targetLabel_;
                result.comparisonKind_ = this.comparisonKind_;
                if (this.conditionBuilder_ == null) {
                    result.condition_ = this.condition_;
                } else {
                    result.condition_ = this.conditionBuilder_.build();
                }
                if (this.trueInstBuilder_ == null) {
                    result.trueInst_ = this.trueInst_;
                } else {
                    result.trueInst_ = this.trueInstBuilder_.build();
                }
                if (this.falseInstBuilder_ == null) {
                    result.falseInst_ = this.falseInst_;
                } else {
                    result.falseInst_ = this.falseInstBuilder_.build();
                }
                if (this.arrayBuilder_ == null) {
                    result.array_ = this.array_;
                } else {
                    result.array_ = this.arrayBuilder_.build();
                }
                result.conversionKind_ = this.conversionKind_;
                result.inputType_ = this.inputType_;
                result.targetType_ = this.targetType_;
                if (this.argumentBuilder_ == null) {
                    result.argument_ = this.argument_;
                } else {
                    result.argument_ = this.argumentBuilder_.build();
                }
                result.resultType_ = this.resultType_;
                if (this.indicesBuilder_ != null) {
                    result.indices_ = this.indicesBuilder_.build();
                } else {
                    if ((this.bitField0_ & 2) != 0) {
                        this.indices_ = Collections.unmodifiableList(this.indices_);
                        this.bitField0_ &= -3;
                    }
                    result.indices_ = this.indices_;
                }
                if (this.tryBlockBuilder_ == null) {
                    result.tryBlock_ = this.tryBlock_;
                } else {
                    result.tryBlock_ = this.tryBlockBuilder_.build();
                }
                if (this.handlersBuilder_ != null) {
                    result.handlers_ = this.handlersBuilder_.build();
                } else {
                    if ((this.bitField0_ & 4) != 0) {
                        this.handlers_ = Collections.unmodifiableList(this.handlers_);
                        this.bitField0_ &= -5;
                    }
                    result.handlers_ = this.handlers_;
                }
                if (this.finallyBlockBuilder_ == null) {
                    result.finallyBlock_ = this.finallyBlock_;
                } else {
                    result.finallyBlock_ = this.finallyBlockBuilder_.build();
                }
                if (this.faultBlockBuilder_ == null) {
                    result.faultBlock_ = this.faultBlock_;
                } else {
                    result.faultBlock_ = this.faultBlockBuilder_.build();
                }
                if (this.bodyBuilder_ == null) {
                    result.body_ = this.body_;
                } else {
                    result.body_ = this.bodyBuilder_.build();
                }
                if (this.keyInstrBuilder_ == null) {
                    result.keyInstr_ = this.keyInstr_;
                } else {
                    result.keyInstr_ = this.keyInstrBuilder_.build();
                }
                if (this.defaultInstBuilder_ == null) {
                    result.defaultInst_ = this.defaultInst_;
                } else {
                    result.defaultInst_ = this.defaultInstBuilder_.build();
                }
                if (this.switchSectionsBuilder_ != null) {
                    result.switchSections_ = this.switchSectionsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 8) != 0) {
                        this.switchSections_ = Collections.unmodifiableList(this.switchSections_);
                        this.bitField0_ &= -9;
                    }
                    result.switchSections_ = this.switchSections_;
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
                if (other instanceof IlInstructionMsg) {
                    return mergeFrom((IlInstructionMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlInstructionMsg other) {
                if (other != IlInstructionMsg.getDefaultInstance()) {
                    if (other.opCode_ != 0) {
                        setOpCodeValue(other.getOpCodeValue());
                    }
                    if (other.hasMethod()) {
                        mergeMethod(other.getMethod());
                    }
                    if (this.argumentsBuilder_ == null) {
                        if (!other.arguments_.isEmpty()) {
                            if (this.arguments_.isEmpty()) {
                                this.arguments_ = other.arguments_;
                                this.bitField0_ &= -2;
                            } else {
                                ensureArgumentsIsMutable();
                                this.arguments_.addAll(other.arguments_);
                            }
                            onChanged();
                        }
                    } else if (!other.arguments_.isEmpty()) {
                        if (!this.argumentsBuilder_.isEmpty()) {
                            this.argumentsBuilder_.addAllMessages(other.arguments_);
                        } else {
                            this.argumentsBuilder_.dispose();
                            this.argumentsBuilder_ = null;
                            this.arguments_ = other.arguments_;
                            this.bitField0_ &= -2;
                            this.argumentsBuilder_ = IlInstructionMsg.alwaysUseFieldBuilders ? getArgumentsFieldBuilder() : null;
                        }
                    }
                    if (other.hasValueInstruction()) {
                        mergeValueInstruction(other.getValueInstruction());
                    }
                    if (!other.getValueConstantString().isEmpty()) {
                        this.valueConstantString_ = other.valueConstantString_;
                        onChanged();
                    }
                    if (other.getValueConstantInt32() != 0) {
                        setValueConstantInt32(other.getValueConstantInt32());
                    }
                    if (other.getValueConstantInt64() != 0) {
                        setValueConstantInt64(other.getValueConstantInt64());
                    }
                    if (other.getValueConstantFloat() != 0.0f) {
                        setValueConstantFloat(other.getValueConstantFloat());
                    }
                    if (other.getValueConstantDouble() != Const.default_value_double) {
                        setValueConstantDouble(other.getValueConstantDouble());
                    }
                    if (other.hasTarget()) {
                        mergeTarget(other.getTarget());
                    }
                    if (other.hasType()) {
                        mergeType(other.getType());
                    }
                    if (other.hasField()) {
                        mergeField(other.getField());
                    }
                    if (other.hasVariable()) {
                        mergeVariable(other.getVariable());
                    }
                    if (other.operator_ != 0) {
                        setOperatorValue(other.getOperatorValue());
                    }
                    if (other.sign_ != 0) {
                        setSignValue(other.getSignValue());
                    }
                    if (other.hasLeft()) {
                        mergeLeft(other.getLeft());
                    }
                    if (other.hasRight()) {
                        mergeRight(other.getRight());
                    }
                    if (!other.getTargetLabel().isEmpty()) {
                        this.targetLabel_ = other.targetLabel_;
                        onChanged();
                    }
                    if (other.comparisonKind_ != 0) {
                        setComparisonKindValue(other.getComparisonKindValue());
                    }
                    if (other.hasCondition()) {
                        mergeCondition(other.getCondition());
                    }
                    if (other.hasTrueInst()) {
                        mergeTrueInst(other.getTrueInst());
                    }
                    if (other.hasFalseInst()) {
                        mergeFalseInst(other.getFalseInst());
                    }
                    if (other.hasArray()) {
                        mergeArray(other.getArray());
                    }
                    if (other.conversionKind_ != 0) {
                        setConversionKindValue(other.getConversionKindValue());
                    }
                    if (other.inputType_ != 0) {
                        setInputTypeValue(other.getInputTypeValue());
                    }
                    if (other.targetType_ != 0) {
                        setTargetTypeValue(other.getTargetTypeValue());
                    }
                    if (other.hasArgument()) {
                        mergeArgument(other.getArgument());
                    }
                    if (other.resultType_ != 0) {
                        setResultTypeValue(other.getResultTypeValue());
                    }
                    if (this.indicesBuilder_ == null) {
                        if (!other.indices_.isEmpty()) {
                            if (this.indices_.isEmpty()) {
                                this.indices_ = other.indices_;
                                this.bitField0_ &= -3;
                            } else {
                                ensureIndicesIsMutable();
                                this.indices_.addAll(other.indices_);
                            }
                            onChanged();
                        }
                    } else if (!other.indices_.isEmpty()) {
                        if (!this.indicesBuilder_.isEmpty()) {
                            this.indicesBuilder_.addAllMessages(other.indices_);
                        } else {
                            this.indicesBuilder_.dispose();
                            this.indicesBuilder_ = null;
                            this.indices_ = other.indices_;
                            this.bitField0_ &= -3;
                            this.indicesBuilder_ = IlInstructionMsg.alwaysUseFieldBuilders ? getIndicesFieldBuilder() : null;
                        }
                    }
                    if (other.hasTryBlock()) {
                        mergeTryBlock(other.getTryBlock());
                    }
                    if (this.handlersBuilder_ == null) {
                        if (!other.handlers_.isEmpty()) {
                            if (this.handlers_.isEmpty()) {
                                this.handlers_ = other.handlers_;
                                this.bitField0_ &= -5;
                            } else {
                                ensureHandlersIsMutable();
                                this.handlers_.addAll(other.handlers_);
                            }
                            onChanged();
                        }
                    } else if (!other.handlers_.isEmpty()) {
                        if (!this.handlersBuilder_.isEmpty()) {
                            this.handlersBuilder_.addAllMessages(other.handlers_);
                        } else {
                            this.handlersBuilder_.dispose();
                            this.handlersBuilder_ = null;
                            this.handlers_ = other.handlers_;
                            this.bitField0_ &= -5;
                            this.handlersBuilder_ = IlInstructionMsg.alwaysUseFieldBuilders ? getHandlersFieldBuilder() : null;
                        }
                    }
                    if (other.hasFinallyBlock()) {
                        mergeFinallyBlock(other.getFinallyBlock());
                    }
                    if (other.hasFaultBlock()) {
                        mergeFaultBlock(other.getFaultBlock());
                    }
                    if (other.hasBody()) {
                        mergeBody(other.getBody());
                    }
                    if (other.hasKeyInstr()) {
                        mergeKeyInstr(other.getKeyInstr());
                    }
                    if (other.hasDefaultInst()) {
                        mergeDefaultInst(other.getDefaultInst());
                    }
                    if (this.switchSectionsBuilder_ == null) {
                        if (!other.switchSections_.isEmpty()) {
                            if (this.switchSections_.isEmpty()) {
                                this.switchSections_ = other.switchSections_;
                                this.bitField0_ &= -9;
                            } else {
                                ensureSwitchSectionsIsMutable();
                                this.switchSections_.addAll(other.switchSections_);
                            }
                            onChanged();
                        }
                    } else if (!other.switchSections_.isEmpty()) {
                        if (!this.switchSectionsBuilder_.isEmpty()) {
                            this.switchSectionsBuilder_.addAllMessages(other.switchSections_);
                        } else {
                            this.switchSectionsBuilder_.dispose();
                            this.switchSectionsBuilder_ = null;
                            this.switchSections_ = other.switchSections_;
                            this.bitField0_ &= -9;
                            this.switchSectionsBuilder_ = IlInstructionMsg.alwaysUseFieldBuilders ? getSwitchSectionsFieldBuilder() : null;
                        }
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
                IlInstructionMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlInstructionMsg) IlInstructionMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlInstructionMsg ilInstructionMsg = (IlInstructionMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getOpCodeValue() {
                return this.opCode_;
            }

            public Builder setOpCodeValue(int value) {
                this.opCode_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlOpCode getOpCode() {
                IlOpCode result = IlOpCode.valueOf(this.opCode_);
                return result == null ? IlOpCode.UNRECOGNIZED : result;
            }

            public Builder setOpCode(IlOpCode value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.opCode_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearOpCode() {
                this.opCode_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasMethod() {
                return (this.methodBuilder_ == null && this.method_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.MethodDefinition getMethod() {
                if (this.methodBuilder_ == null) {
                    return this.method_ == null ? ProtoAssemblyAllTypes.MethodDefinition.getDefaultInstance() : this.method_;
                }
                return this.methodBuilder_.getMessage();
            }

            public Builder setMethod(ProtoAssemblyAllTypes.MethodDefinition value) {
                if (this.methodBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.method_ = value;
                    onChanged();
                } else {
                    this.methodBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setMethod(ProtoAssemblyAllTypes.MethodDefinition.Builder builderForValue) {
                if (this.methodBuilder_ == null) {
                    this.method_ = builderForValue.build();
                    onChanged();
                } else {
                    this.methodBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeMethod(ProtoAssemblyAllTypes.MethodDefinition value) {
                if (this.methodBuilder_ == null) {
                    if (this.method_ != null) {
                        this.method_ = ProtoAssemblyAllTypes.MethodDefinition.newBuilder(this.method_).mergeFrom(value).buildPartial();
                    } else {
                        this.method_ = value;
                    }
                    onChanged();
                } else {
                    this.methodBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearMethod() {
                if (this.methodBuilder_ == null) {
                    this.method_ = null;
                    onChanged();
                } else {
                    this.method_ = null;
                    this.methodBuilder_ = null;
                }
                return this;
            }

            public ProtoAssemblyAllTypes.MethodDefinition.Builder getMethodBuilder() {
                onChanged();
                return getMethodFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.MethodDefinitionOrBuilder getMethodOrBuilder() {
                if (this.methodBuilder_ != null) {
                    return this.methodBuilder_.getMessageOrBuilder();
                }
                return this.method_ == null ? ProtoAssemblyAllTypes.MethodDefinition.getDefaultInstance() : this.method_;
            }

            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.MethodDefinition, ProtoAssemblyAllTypes.MethodDefinition.Builder, ProtoAssemblyAllTypes.MethodDefinitionOrBuilder> getMethodFieldBuilder() {
                if (this.methodBuilder_ == null) {
                    this.methodBuilder_ = new SingleFieldBuilderV3<>(getMethod(), getParentForChildren(), isClean());
                    this.method_ = null;
                }
                return this.methodBuilder_;
            }

            private void ensureArgumentsIsMutable() {
                if ((this.bitField0_ & 1) == 0) {
                    this.arguments_ = new ArrayList(this.arguments_);
                    this.bitField0_ |= 1;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<IlInstructionMsg> getArgumentsList() {
                if (this.argumentsBuilder_ == null) {
                    return Collections.unmodifiableList(this.arguments_);
                }
                return this.argumentsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getArgumentsCount() {
                if (this.argumentsBuilder_ == null) {
                    return this.arguments_.size();
                }
                return this.argumentsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getArguments(int index) {
                if (this.argumentsBuilder_ == null) {
                    return this.arguments_.get(index);
                }
                return this.argumentsBuilder_.getMessage(index);
            }

            public Builder setArguments(int index, IlInstructionMsg value) {
                if (this.argumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureArgumentsIsMutable();
                    this.arguments_.set(index, value);
                    onChanged();
                } else {
                    this.argumentsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setArguments(int index, Builder builderForValue) {
                if (this.argumentsBuilder_ == null) {
                    ensureArgumentsIsMutable();
                    this.arguments_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.argumentsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addArguments(IlInstructionMsg value) {
                if (this.argumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureArgumentsIsMutable();
                    this.arguments_.add(value);
                    onChanged();
                } else {
                    this.argumentsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addArguments(int index, IlInstructionMsg value) {
                if (this.argumentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureArgumentsIsMutable();
                    this.arguments_.add(index, value);
                    onChanged();
                } else {
                    this.argumentsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addArguments(Builder builderForValue) {
                if (this.argumentsBuilder_ == null) {
                    ensureArgumentsIsMutable();
                    this.arguments_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.argumentsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addArguments(int index, Builder builderForValue) {
                if (this.argumentsBuilder_ == null) {
                    ensureArgumentsIsMutable();
                    this.arguments_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.argumentsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllArguments(Iterable<? extends IlInstructionMsg> values) {
                if (this.argumentsBuilder_ == null) {
                    ensureArgumentsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.arguments_);
                    onChanged();
                } else {
                    this.argumentsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearArguments() {
                if (this.argumentsBuilder_ == null) {
                    this.arguments_ = Collections.emptyList();
                    this.bitField0_ &= -2;
                    onChanged();
                } else {
                    this.argumentsBuilder_.clear();
                }
                return this;
            }

            public Builder removeArguments(int index) {
                if (this.argumentsBuilder_ == null) {
                    ensureArgumentsIsMutable();
                    this.arguments_.remove(index);
                    onChanged();
                } else {
                    this.argumentsBuilder_.remove(index);
                }
                return this;
            }

            public Builder getArgumentsBuilder(int index) {
                return getArgumentsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getArgumentsOrBuilder(int index) {
                if (this.argumentsBuilder_ == null) {
                    return this.arguments_.get(index);
                }
                return this.argumentsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<? extends IlInstructionMsgOrBuilder> getArgumentsOrBuilderList() {
                if (this.argumentsBuilder_ != null) {
                    return this.argumentsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.arguments_);
            }

            public Builder addArgumentsBuilder() {
                return getArgumentsFieldBuilder().addBuilder(IlInstructionMsg.getDefaultInstance());
            }

            public Builder addArgumentsBuilder(int index) {
                return getArgumentsFieldBuilder().addBuilder(index, IlInstructionMsg.getDefaultInstance());
            }

            public List<Builder> getArgumentsBuilderList() {
                return getArgumentsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getArgumentsFieldBuilder() {
                if (this.argumentsBuilder_ == null) {
                    this.argumentsBuilder_ = new RepeatedFieldBuilderV3<>(this.arguments_, (this.bitField0_ & 1) != 0, getParentForChildren(), isClean());
                    this.arguments_ = null;
                }
                return this.argumentsBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasValueInstruction() {
                return (this.valueInstructionBuilder_ == null && this.valueInstruction_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getValueInstruction() {
                if (this.valueInstructionBuilder_ == null) {
                    return this.valueInstruction_ == null ? IlInstructionMsg.getDefaultInstance() : this.valueInstruction_;
                }
                return this.valueInstructionBuilder_.getMessage();
            }

            public Builder setValueInstruction(IlInstructionMsg value) {
                if (this.valueInstructionBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.valueInstruction_ = value;
                    onChanged();
                } else {
                    this.valueInstructionBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setValueInstruction(Builder builderForValue) {
                if (this.valueInstructionBuilder_ == null) {
                    this.valueInstruction_ = builderForValue.build();
                    onChanged();
                } else {
                    this.valueInstructionBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeValueInstruction(IlInstructionMsg value) {
                if (this.valueInstructionBuilder_ == null) {
                    if (this.valueInstruction_ != null) {
                        this.valueInstruction_ = IlInstructionMsg.newBuilder(this.valueInstruction_).mergeFrom(value).buildPartial();
                    } else {
                        this.valueInstruction_ = value;
                    }
                    onChanged();
                } else {
                    this.valueInstructionBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearValueInstruction() {
                if (this.valueInstructionBuilder_ == null) {
                    this.valueInstruction_ = null;
                    onChanged();
                } else {
                    this.valueInstruction_ = null;
                    this.valueInstructionBuilder_ = null;
                }
                return this;
            }

            public Builder getValueInstructionBuilder() {
                onChanged();
                return getValueInstructionFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getValueInstructionOrBuilder() {
                if (this.valueInstructionBuilder_ != null) {
                    return this.valueInstructionBuilder_.getMessageOrBuilder();
                }
                return this.valueInstruction_ == null ? IlInstructionMsg.getDefaultInstance() : this.valueInstruction_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getValueInstructionFieldBuilder() {
                if (this.valueInstructionBuilder_ == null) {
                    this.valueInstructionBuilder_ = new SingleFieldBuilderV3<>(getValueInstruction(), getParentForChildren(), isClean());
                    this.valueInstruction_ = null;
                }
                return this.valueInstructionBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public String getValueConstantString() {
                Object ref = this.valueConstantString_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.valueConstantString_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ByteString getValueConstantStringBytes() {
                Object ref = this.valueConstantString_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.valueConstantString_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setValueConstantString(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.valueConstantString_ = value;
                onChanged();
                return this;
            }

            public Builder clearValueConstantString() {
                this.valueConstantString_ = IlInstructionMsg.getDefaultInstance().getValueConstantString();
                onChanged();
                return this;
            }

            public Builder setValueConstantStringBytes(ByteString value) {
                if (value != null) {
                    IlInstructionMsg.checkByteStringIsUtf8(value);
                    this.valueConstantString_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getValueConstantInt32() {
                return this.valueConstantInt32_;
            }

            public Builder setValueConstantInt32(int value) {
                this.valueConstantInt32_ = value;
                onChanged();
                return this;
            }

            public Builder clearValueConstantInt32() {
                this.valueConstantInt32_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public long getValueConstantInt64() {
                return this.valueConstantInt64_;
            }

            public Builder setValueConstantInt64(long value) {
                this.valueConstantInt64_ = value;
                onChanged();
                return this;
            }

            public Builder clearValueConstantInt64() {
                this.valueConstantInt64_ = 0L;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public float getValueConstantFloat() {
                return this.valueConstantFloat_;
            }

            public Builder setValueConstantFloat(float value) {
                this.valueConstantFloat_ = value;
                onChanged();
                return this;
            }

            public Builder clearValueConstantFloat() {
                this.valueConstantFloat_ = 0.0f;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public double getValueConstantDouble() {
                return this.valueConstantDouble_;
            }

            public Builder setValueConstantDouble(double value) {
                this.valueConstantDouble_ = value;
                onChanged();
                return this;
            }

            public Builder clearValueConstantDouble() {
                this.valueConstantDouble_ = Const.default_value_double;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasTarget() {
                return (this.targetBuilder_ == null && this.target_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getTarget() {
                if (this.targetBuilder_ == null) {
                    return this.target_ == null ? IlInstructionMsg.getDefaultInstance() : this.target_;
                }
                return this.targetBuilder_.getMessage();
            }

            public Builder setTarget(IlInstructionMsg value) {
                if (this.targetBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.target_ = value;
                    onChanged();
                } else {
                    this.targetBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setTarget(Builder builderForValue) {
                if (this.targetBuilder_ == null) {
                    this.target_ = builderForValue.build();
                    onChanged();
                } else {
                    this.targetBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeTarget(IlInstructionMsg value) {
                if (this.targetBuilder_ == null) {
                    if (this.target_ != null) {
                        this.target_ = IlInstructionMsg.newBuilder(this.target_).mergeFrom(value).buildPartial();
                    } else {
                        this.target_ = value;
                    }
                    onChanged();
                } else {
                    this.targetBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearTarget() {
                if (this.targetBuilder_ == null) {
                    this.target_ = null;
                    onChanged();
                } else {
                    this.target_ = null;
                    this.targetBuilder_ = null;
                }
                return this;
            }

            public Builder getTargetBuilder() {
                onChanged();
                return getTargetFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getTargetOrBuilder() {
                if (this.targetBuilder_ != null) {
                    return this.targetBuilder_.getMessageOrBuilder();
                }
                return this.target_ == null ? IlInstructionMsg.getDefaultInstance() : this.target_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getTargetFieldBuilder() {
                if (this.targetBuilder_ == null) {
                    this.targetBuilder_ = new SingleFieldBuilderV3<>(getTarget(), getParentForChildren(), isClean());
                    this.target_ = null;
                }
                return this.targetBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(ProtoAssemblyAllTypes.TypeDefinition value) {
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

            public Builder setType(ProtoAssemblyAllTypes.TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(ProtoAssemblyAllTypes.TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = ProtoAssemblyAllTypes.TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
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

            public ProtoAssemblyAllTypes.TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.TypeDefinition, ProtoAssemblyAllTypes.TypeDefinition.Builder, ProtoAssemblyAllTypes.TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasField() {
                return (this.fieldBuilder_ == null && this.field_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.FieldDefinition getField() {
                if (this.fieldBuilder_ == null) {
                    return this.field_ == null ? ProtoAssemblyAllTypes.FieldDefinition.getDefaultInstance() : this.field_;
                }
                return this.fieldBuilder_.getMessage();
            }

            public Builder setField(ProtoAssemblyAllTypes.FieldDefinition value) {
                if (this.fieldBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.field_ = value;
                    onChanged();
                } else {
                    this.fieldBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setField(ProtoAssemblyAllTypes.FieldDefinition.Builder builderForValue) {
                if (this.fieldBuilder_ == null) {
                    this.field_ = builderForValue.build();
                    onChanged();
                } else {
                    this.fieldBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeField(ProtoAssemblyAllTypes.FieldDefinition value) {
                if (this.fieldBuilder_ == null) {
                    if (this.field_ != null) {
                        this.field_ = ProtoAssemblyAllTypes.FieldDefinition.newBuilder(this.field_).mergeFrom(value).buildPartial();
                    } else {
                        this.field_ = value;
                    }
                    onChanged();
                } else {
                    this.fieldBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearField() {
                if (this.fieldBuilder_ == null) {
                    this.field_ = null;
                    onChanged();
                } else {
                    this.field_ = null;
                    this.fieldBuilder_ = null;
                }
                return this;
            }

            public ProtoAssemblyAllTypes.FieldDefinition.Builder getFieldBuilder() {
                onChanged();
                return getFieldFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ProtoAssemblyAllTypes.FieldDefinitionOrBuilder getFieldOrBuilder() {
                if (this.fieldBuilder_ != null) {
                    return this.fieldBuilder_.getMessageOrBuilder();
                }
                return this.field_ == null ? ProtoAssemblyAllTypes.FieldDefinition.getDefaultInstance() : this.field_;
            }

            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.FieldDefinition, ProtoAssemblyAllTypes.FieldDefinition.Builder, ProtoAssemblyAllTypes.FieldDefinitionOrBuilder> getFieldFieldBuilder() {
                if (this.fieldBuilder_ == null) {
                    this.fieldBuilder_ = new SingleFieldBuilderV3<>(getField(), getParentForChildren(), isClean());
                    this.field_ = null;
                }
                return this.fieldBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasVariable() {
                return (this.variableBuilder_ == null && this.variable_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlVariableMsg getVariable() {
                if (this.variableBuilder_ == null) {
                    return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
                }
                return this.variableBuilder_.getMessage();
            }

            public Builder setVariable(IlVariableMsg value) {
                if (this.variableBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.variable_ = value;
                    onChanged();
                } else {
                    this.variableBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setVariable(IlVariableMsg.Builder builderForValue) {
                if (this.variableBuilder_ == null) {
                    this.variable_ = builderForValue.build();
                    onChanged();
                } else {
                    this.variableBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeVariable(IlVariableMsg value) {
                if (this.variableBuilder_ == null) {
                    if (this.variable_ != null) {
                        this.variable_ = IlVariableMsg.newBuilder(this.variable_).mergeFrom(value).buildPartial();
                    } else {
                        this.variable_ = value;
                    }
                    onChanged();
                } else {
                    this.variableBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearVariable() {
                if (this.variableBuilder_ == null) {
                    this.variable_ = null;
                    onChanged();
                } else {
                    this.variable_ = null;
                    this.variableBuilder_ = null;
                }
                return this;
            }

            public IlVariableMsg.Builder getVariableBuilder() {
                onChanged();
                return getVariableFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlVariableMsgOrBuilder getVariableOrBuilder() {
                if (this.variableBuilder_ != null) {
                    return this.variableBuilder_.getMessageOrBuilder();
                }
                return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
            }

            private SingleFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> getVariableFieldBuilder() {
                if (this.variableBuilder_ == null) {
                    this.variableBuilder_ = new SingleFieldBuilderV3<>(getVariable(), getParentForChildren(), isClean());
                    this.variable_ = null;
                }
                return this.variableBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getOperatorValue() {
                return this.operator_;
            }

            public Builder setOperatorValue(int value) {
                this.operator_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBinaryNumericOperator getOperator() {
                IlBinaryNumericOperator result = IlBinaryNumericOperator.valueOf(this.operator_);
                return result == null ? IlBinaryNumericOperator.UNRECOGNIZED : result;
            }

            public Builder setOperator(IlBinaryNumericOperator value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.operator_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearOperator() {
                this.operator_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getSignValue() {
                return this.sign_;
            }

            public Builder setSignValue(int value) {
                this.sign_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlSign getSign() {
                IlSign result = IlSign.valueOf(this.sign_);
                return result == null ? IlSign.UNRECOGNIZED : result;
            }

            public Builder setSign(IlSign value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.sign_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearSign() {
                this.sign_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasLeft() {
                return (this.leftBuilder_ == null && this.left_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getLeft() {
                if (this.leftBuilder_ == null) {
                    return this.left_ == null ? IlInstructionMsg.getDefaultInstance() : this.left_;
                }
                return this.leftBuilder_.getMessage();
            }

            public Builder setLeft(IlInstructionMsg value) {
                if (this.leftBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.left_ = value;
                    onChanged();
                } else {
                    this.leftBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setLeft(Builder builderForValue) {
                if (this.leftBuilder_ == null) {
                    this.left_ = builderForValue.build();
                    onChanged();
                } else {
                    this.leftBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeLeft(IlInstructionMsg value) {
                if (this.leftBuilder_ == null) {
                    if (this.left_ != null) {
                        this.left_ = IlInstructionMsg.newBuilder(this.left_).mergeFrom(value).buildPartial();
                    } else {
                        this.left_ = value;
                    }
                    onChanged();
                } else {
                    this.leftBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearLeft() {
                if (this.leftBuilder_ == null) {
                    this.left_ = null;
                    onChanged();
                } else {
                    this.left_ = null;
                    this.leftBuilder_ = null;
                }
                return this;
            }

            public Builder getLeftBuilder() {
                onChanged();
                return getLeftFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getLeftOrBuilder() {
                if (this.leftBuilder_ != null) {
                    return this.leftBuilder_.getMessageOrBuilder();
                }
                return this.left_ == null ? IlInstructionMsg.getDefaultInstance() : this.left_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getLeftFieldBuilder() {
                if (this.leftBuilder_ == null) {
                    this.leftBuilder_ = new SingleFieldBuilderV3<>(getLeft(), getParentForChildren(), isClean());
                    this.left_ = null;
                }
                return this.leftBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasRight() {
                return (this.rightBuilder_ == null && this.right_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getRight() {
                if (this.rightBuilder_ == null) {
                    return this.right_ == null ? IlInstructionMsg.getDefaultInstance() : this.right_;
                }
                return this.rightBuilder_.getMessage();
            }

            public Builder setRight(IlInstructionMsg value) {
                if (this.rightBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.right_ = value;
                    onChanged();
                } else {
                    this.rightBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setRight(Builder builderForValue) {
                if (this.rightBuilder_ == null) {
                    this.right_ = builderForValue.build();
                    onChanged();
                } else {
                    this.rightBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeRight(IlInstructionMsg value) {
                if (this.rightBuilder_ == null) {
                    if (this.right_ != null) {
                        this.right_ = IlInstructionMsg.newBuilder(this.right_).mergeFrom(value).buildPartial();
                    } else {
                        this.right_ = value;
                    }
                    onChanged();
                } else {
                    this.rightBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearRight() {
                if (this.rightBuilder_ == null) {
                    this.right_ = null;
                    onChanged();
                } else {
                    this.right_ = null;
                    this.rightBuilder_ = null;
                }
                return this;
            }

            public Builder getRightBuilder() {
                onChanged();
                return getRightFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getRightOrBuilder() {
                if (this.rightBuilder_ != null) {
                    return this.rightBuilder_.getMessageOrBuilder();
                }
                return this.right_ == null ? IlInstructionMsg.getDefaultInstance() : this.right_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getRightFieldBuilder() {
                if (this.rightBuilder_ == null) {
                    this.rightBuilder_ = new SingleFieldBuilderV3<>(getRight(), getParentForChildren(), isClean());
                    this.right_ = null;
                }
                return this.rightBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public String getTargetLabel() {
                Object ref = this.targetLabel_;
                if (!(ref instanceof String)) {
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    this.targetLabel_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public ByteString getTargetLabelBytes() {
                Object ref = this.targetLabel_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.targetLabel_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setTargetLabel(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.targetLabel_ = value;
                onChanged();
                return this;
            }

            public Builder clearTargetLabel() {
                this.targetLabel_ = IlInstructionMsg.getDefaultInstance().getTargetLabel();
                onChanged();
                return this;
            }

            public Builder setTargetLabelBytes(ByteString value) {
                if (value != null) {
                    IlInstructionMsg.checkByteStringIsUtf8(value);
                    this.targetLabel_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getComparisonKindValue() {
                return this.comparisonKind_;
            }

            public Builder setComparisonKindValue(int value) {
                this.comparisonKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlComparisonKind getComparisonKind() {
                IlComparisonKind result = IlComparisonKind.valueOf(this.comparisonKind_);
                return result == null ? IlComparisonKind.UNRECOGNIZED : result;
            }

            public Builder setComparisonKind(IlComparisonKind value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.comparisonKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearComparisonKind() {
                this.comparisonKind_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasCondition() {
                return (this.conditionBuilder_ == null && this.condition_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getCondition() {
                if (this.conditionBuilder_ == null) {
                    return this.condition_ == null ? IlInstructionMsg.getDefaultInstance() : this.condition_;
                }
                return this.conditionBuilder_.getMessage();
            }

            public Builder setCondition(IlInstructionMsg value) {
                if (this.conditionBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.condition_ = value;
                    onChanged();
                } else {
                    this.conditionBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setCondition(Builder builderForValue) {
                if (this.conditionBuilder_ == null) {
                    this.condition_ = builderForValue.build();
                    onChanged();
                } else {
                    this.conditionBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeCondition(IlInstructionMsg value) {
                if (this.conditionBuilder_ == null) {
                    if (this.condition_ != null) {
                        this.condition_ = IlInstructionMsg.newBuilder(this.condition_).mergeFrom(value).buildPartial();
                    } else {
                        this.condition_ = value;
                    }
                    onChanged();
                } else {
                    this.conditionBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearCondition() {
                if (this.conditionBuilder_ == null) {
                    this.condition_ = null;
                    onChanged();
                } else {
                    this.condition_ = null;
                    this.conditionBuilder_ = null;
                }
                return this;
            }

            public Builder getConditionBuilder() {
                onChanged();
                return getConditionFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getConditionOrBuilder() {
                if (this.conditionBuilder_ != null) {
                    return this.conditionBuilder_.getMessageOrBuilder();
                }
                return this.condition_ == null ? IlInstructionMsg.getDefaultInstance() : this.condition_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getConditionFieldBuilder() {
                if (this.conditionBuilder_ == null) {
                    this.conditionBuilder_ = new SingleFieldBuilderV3<>(getCondition(), getParentForChildren(), isClean());
                    this.condition_ = null;
                }
                return this.conditionBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasTrueInst() {
                return (this.trueInstBuilder_ == null && this.trueInst_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getTrueInst() {
                if (this.trueInstBuilder_ == null) {
                    return this.trueInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.trueInst_;
                }
                return this.trueInstBuilder_.getMessage();
            }

            public Builder setTrueInst(IlInstructionMsg value) {
                if (this.trueInstBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.trueInst_ = value;
                    onChanged();
                } else {
                    this.trueInstBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setTrueInst(Builder builderForValue) {
                if (this.trueInstBuilder_ == null) {
                    this.trueInst_ = builderForValue.build();
                    onChanged();
                } else {
                    this.trueInstBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeTrueInst(IlInstructionMsg value) {
                if (this.trueInstBuilder_ == null) {
                    if (this.trueInst_ != null) {
                        this.trueInst_ = IlInstructionMsg.newBuilder(this.trueInst_).mergeFrom(value).buildPartial();
                    } else {
                        this.trueInst_ = value;
                    }
                    onChanged();
                } else {
                    this.trueInstBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearTrueInst() {
                if (this.trueInstBuilder_ == null) {
                    this.trueInst_ = null;
                    onChanged();
                } else {
                    this.trueInst_ = null;
                    this.trueInstBuilder_ = null;
                }
                return this;
            }

            public Builder getTrueInstBuilder() {
                onChanged();
                return getTrueInstFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getTrueInstOrBuilder() {
                if (this.trueInstBuilder_ != null) {
                    return this.trueInstBuilder_.getMessageOrBuilder();
                }
                return this.trueInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.trueInst_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getTrueInstFieldBuilder() {
                if (this.trueInstBuilder_ == null) {
                    this.trueInstBuilder_ = new SingleFieldBuilderV3<>(getTrueInst(), getParentForChildren(), isClean());
                    this.trueInst_ = null;
                }
                return this.trueInstBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasFalseInst() {
                return (this.falseInstBuilder_ == null && this.falseInst_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getFalseInst() {
                if (this.falseInstBuilder_ == null) {
                    return this.falseInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.falseInst_;
                }
                return this.falseInstBuilder_.getMessage();
            }

            public Builder setFalseInst(IlInstructionMsg value) {
                if (this.falseInstBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.falseInst_ = value;
                    onChanged();
                } else {
                    this.falseInstBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setFalseInst(Builder builderForValue) {
                if (this.falseInstBuilder_ == null) {
                    this.falseInst_ = builderForValue.build();
                    onChanged();
                } else {
                    this.falseInstBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeFalseInst(IlInstructionMsg value) {
                if (this.falseInstBuilder_ == null) {
                    if (this.falseInst_ != null) {
                        this.falseInst_ = IlInstructionMsg.newBuilder(this.falseInst_).mergeFrom(value).buildPartial();
                    } else {
                        this.falseInst_ = value;
                    }
                    onChanged();
                } else {
                    this.falseInstBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearFalseInst() {
                if (this.falseInstBuilder_ == null) {
                    this.falseInst_ = null;
                    onChanged();
                } else {
                    this.falseInst_ = null;
                    this.falseInstBuilder_ = null;
                }
                return this;
            }

            public Builder getFalseInstBuilder() {
                onChanged();
                return getFalseInstFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getFalseInstOrBuilder() {
                if (this.falseInstBuilder_ != null) {
                    return this.falseInstBuilder_.getMessageOrBuilder();
                }
                return this.falseInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.falseInst_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getFalseInstFieldBuilder() {
                if (this.falseInstBuilder_ == null) {
                    this.falseInstBuilder_ = new SingleFieldBuilderV3<>(getFalseInst(), getParentForChildren(), isClean());
                    this.falseInst_ = null;
                }
                return this.falseInstBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasArray() {
                return (this.arrayBuilder_ == null && this.array_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getArray() {
                if (this.arrayBuilder_ == null) {
                    return this.array_ == null ? IlInstructionMsg.getDefaultInstance() : this.array_;
                }
                return this.arrayBuilder_.getMessage();
            }

            public Builder setArray(IlInstructionMsg value) {
                if (this.arrayBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.array_ = value;
                    onChanged();
                } else {
                    this.arrayBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setArray(Builder builderForValue) {
                if (this.arrayBuilder_ == null) {
                    this.array_ = builderForValue.build();
                    onChanged();
                } else {
                    this.arrayBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeArray(IlInstructionMsg value) {
                if (this.arrayBuilder_ == null) {
                    if (this.array_ != null) {
                        this.array_ = IlInstructionMsg.newBuilder(this.array_).mergeFrom(value).buildPartial();
                    } else {
                        this.array_ = value;
                    }
                    onChanged();
                } else {
                    this.arrayBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearArray() {
                if (this.arrayBuilder_ == null) {
                    this.array_ = null;
                    onChanged();
                } else {
                    this.array_ = null;
                    this.arrayBuilder_ = null;
                }
                return this;
            }

            public Builder getArrayBuilder() {
                onChanged();
                return getArrayFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getArrayOrBuilder() {
                if (this.arrayBuilder_ != null) {
                    return this.arrayBuilder_.getMessageOrBuilder();
                }
                return this.array_ == null ? IlInstructionMsg.getDefaultInstance() : this.array_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getArrayFieldBuilder() {
                if (this.arrayBuilder_ == null) {
                    this.arrayBuilder_ = new SingleFieldBuilderV3<>(getArray(), getParentForChildren(), isClean());
                    this.array_ = null;
                }
                return this.arrayBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getConversionKindValue() {
                return this.conversionKind_;
            }

            public Builder setConversionKindValue(int value) {
                this.conversionKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlConversionKind getConversionKind() {
                IlConversionKind result = IlConversionKind.valueOf(this.conversionKind_);
                return result == null ? IlConversionKind.UNRECOGNIZED : result;
            }

            public Builder setConversionKind(IlConversionKind value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.conversionKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearConversionKind() {
                this.conversionKind_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getInputTypeValue() {
                return this.inputType_;
            }

            public Builder setInputTypeValue(int value) {
                this.inputType_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlStackType getInputType() {
                IlStackType result = IlStackType.valueOf(this.inputType_);
                return result == null ? IlStackType.UNRECOGNIZED : result;
            }

            public Builder setInputType(IlStackType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.inputType_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearInputType() {
                this.inputType_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getTargetTypeValue() {
                return this.targetType_;
            }

            public Builder setTargetTypeValue(int value) {
                this.targetType_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlPrimitiveType getTargetType() {
                IlPrimitiveType result = IlPrimitiveType.valueOf(this.targetType_);
                return result == null ? IlPrimitiveType.UNRECOGNIZED : result;
            }

            public Builder setTargetType(IlPrimitiveType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.targetType_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearTargetType() {
                this.targetType_ = 0;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasArgument() {
                return (this.argumentBuilder_ == null && this.argument_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getArgument() {
                if (this.argumentBuilder_ == null) {
                    return this.argument_ == null ? IlInstructionMsg.getDefaultInstance() : this.argument_;
                }
                return this.argumentBuilder_.getMessage();
            }

            public Builder setArgument(IlInstructionMsg value) {
                if (this.argumentBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.argument_ = value;
                    onChanged();
                } else {
                    this.argumentBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setArgument(Builder builderForValue) {
                if (this.argumentBuilder_ == null) {
                    this.argument_ = builderForValue.build();
                    onChanged();
                } else {
                    this.argumentBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeArgument(IlInstructionMsg value) {
                if (this.argumentBuilder_ == null) {
                    if (this.argument_ != null) {
                        this.argument_ = IlInstructionMsg.newBuilder(this.argument_).mergeFrom(value).buildPartial();
                    } else {
                        this.argument_ = value;
                    }
                    onChanged();
                } else {
                    this.argumentBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearArgument() {
                if (this.argumentBuilder_ == null) {
                    this.argument_ = null;
                    onChanged();
                } else {
                    this.argument_ = null;
                    this.argumentBuilder_ = null;
                }
                return this;
            }

            public Builder getArgumentBuilder() {
                onChanged();
                return getArgumentFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getArgumentOrBuilder() {
                if (this.argumentBuilder_ != null) {
                    return this.argumentBuilder_.getMessageOrBuilder();
                }
                return this.argument_ == null ? IlInstructionMsg.getDefaultInstance() : this.argument_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getArgumentFieldBuilder() {
                if (this.argumentBuilder_ == null) {
                    this.argumentBuilder_ = new SingleFieldBuilderV3<>(getArgument(), getParentForChildren(), isClean());
                    this.argument_ = null;
                }
                return this.argumentBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getResultTypeValue() {
                return this.resultType_;
            }

            public Builder setResultTypeValue(int value) {
                this.resultType_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlStackType getResultType() {
                IlStackType result = IlStackType.valueOf(this.resultType_);
                return result == null ? IlStackType.UNRECOGNIZED : result;
            }

            public Builder setResultType(IlStackType value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.resultType_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearResultType() {
                this.resultType_ = 0;
                onChanged();
                return this;
            }

            private void ensureIndicesIsMutable() {
                if ((this.bitField0_ & 2) == 0) {
                    this.indices_ = new ArrayList(this.indices_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<IlInstructionMsg> getIndicesList() {
                if (this.indicesBuilder_ == null) {
                    return Collections.unmodifiableList(this.indices_);
                }
                return this.indicesBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getIndicesCount() {
                if (this.indicesBuilder_ == null) {
                    return this.indices_.size();
                }
                return this.indicesBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getIndices(int index) {
                if (this.indicesBuilder_ == null) {
                    return this.indices_.get(index);
                }
                return this.indicesBuilder_.getMessage(index);
            }

            public Builder setIndices(int index, IlInstructionMsg value) {
                if (this.indicesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureIndicesIsMutable();
                    this.indices_.set(index, value);
                    onChanged();
                } else {
                    this.indicesBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setIndices(int index, Builder builderForValue) {
                if (this.indicesBuilder_ == null) {
                    ensureIndicesIsMutable();
                    this.indices_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.indicesBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addIndices(IlInstructionMsg value) {
                if (this.indicesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureIndicesIsMutable();
                    this.indices_.add(value);
                    onChanged();
                } else {
                    this.indicesBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addIndices(int index, IlInstructionMsg value) {
                if (this.indicesBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureIndicesIsMutable();
                    this.indices_.add(index, value);
                    onChanged();
                } else {
                    this.indicesBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addIndices(Builder builderForValue) {
                if (this.indicesBuilder_ == null) {
                    ensureIndicesIsMutable();
                    this.indices_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.indicesBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addIndices(int index, Builder builderForValue) {
                if (this.indicesBuilder_ == null) {
                    ensureIndicesIsMutable();
                    this.indices_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.indicesBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllIndices(Iterable<? extends IlInstructionMsg> values) {
                if (this.indicesBuilder_ == null) {
                    ensureIndicesIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.indices_);
                    onChanged();
                } else {
                    this.indicesBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearIndices() {
                if (this.indicesBuilder_ == null) {
                    this.indices_ = Collections.emptyList();
                    this.bitField0_ &= -3;
                    onChanged();
                } else {
                    this.indicesBuilder_.clear();
                }
                return this;
            }

            public Builder removeIndices(int index) {
                if (this.indicesBuilder_ == null) {
                    ensureIndicesIsMutable();
                    this.indices_.remove(index);
                    onChanged();
                } else {
                    this.indicesBuilder_.remove(index);
                }
                return this;
            }

            public Builder getIndicesBuilder(int index) {
                return getIndicesFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getIndicesOrBuilder(int index) {
                if (this.indicesBuilder_ == null) {
                    return this.indices_.get(index);
                }
                return this.indicesBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<? extends IlInstructionMsgOrBuilder> getIndicesOrBuilderList() {
                if (this.indicesBuilder_ != null) {
                    return this.indicesBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.indices_);
            }

            public Builder addIndicesBuilder() {
                return getIndicesFieldBuilder().addBuilder(IlInstructionMsg.getDefaultInstance());
            }

            public Builder addIndicesBuilder(int index) {
                return getIndicesFieldBuilder().addBuilder(index, IlInstructionMsg.getDefaultInstance());
            }

            public List<Builder> getIndicesBuilderList() {
                return getIndicesFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getIndicesFieldBuilder() {
                if (this.indicesBuilder_ == null) {
                    this.indicesBuilder_ = new RepeatedFieldBuilderV3<>(this.indices_, (this.bitField0_ & 2) != 0, getParentForChildren(), isClean());
                    this.indices_ = null;
                }
                return this.indicesBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasTryBlock() {
                return (this.tryBlockBuilder_ == null && this.tryBlock_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsg getTryBlock() {
                if (this.tryBlockBuilder_ == null) {
                    return this.tryBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.tryBlock_;
                }
                return this.tryBlockBuilder_.getMessage();
            }

            public Builder setTryBlock(IlBlockContainerMsg value) {
                if (this.tryBlockBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.tryBlock_ = value;
                    onChanged();
                } else {
                    this.tryBlockBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setTryBlock(IlBlockContainerMsg.Builder builderForValue) {
                if (this.tryBlockBuilder_ == null) {
                    this.tryBlock_ = builderForValue.build();
                    onChanged();
                } else {
                    this.tryBlockBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeTryBlock(IlBlockContainerMsg value) {
                if (this.tryBlockBuilder_ == null) {
                    if (this.tryBlock_ != null) {
                        this.tryBlock_ = IlBlockContainerMsg.newBuilder(this.tryBlock_).mergeFrom(value).buildPartial();
                    } else {
                        this.tryBlock_ = value;
                    }
                    onChanged();
                } else {
                    this.tryBlockBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearTryBlock() {
                if (this.tryBlockBuilder_ == null) {
                    this.tryBlock_ = null;
                    onChanged();
                } else {
                    this.tryBlock_ = null;
                    this.tryBlockBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getTryBlockBuilder() {
                onChanged();
                return getTryBlockFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getTryBlockOrBuilder() {
                if (this.tryBlockBuilder_ != null) {
                    return this.tryBlockBuilder_.getMessageOrBuilder();
                }
                return this.tryBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.tryBlock_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getTryBlockFieldBuilder() {
                if (this.tryBlockBuilder_ == null) {
                    this.tryBlockBuilder_ = new SingleFieldBuilderV3<>(getTryBlock(), getParentForChildren(), isClean());
                    this.tryBlock_ = null;
                }
                return this.tryBlockBuilder_;
            }

            private void ensureHandlersIsMutable() {
                if ((this.bitField0_ & 4) == 0) {
                    this.handlers_ = new ArrayList(this.handlers_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<IlTryCatchHandlerMsg> getHandlersList() {
                if (this.handlersBuilder_ == null) {
                    return Collections.unmodifiableList(this.handlers_);
                }
                return this.handlersBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getHandlersCount() {
                if (this.handlersBuilder_ == null) {
                    return this.handlers_.size();
                }
                return this.handlersBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlTryCatchHandlerMsg getHandlers(int index) {
                if (this.handlersBuilder_ == null) {
                    return this.handlers_.get(index);
                }
                return this.handlersBuilder_.getMessage(index);
            }

            public Builder setHandlers(int index, IlTryCatchHandlerMsg value) {
                if (this.handlersBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureHandlersIsMutable();
                    this.handlers_.set(index, value);
                    onChanged();
                } else {
                    this.handlersBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setHandlers(int index, IlTryCatchHandlerMsg.Builder builderForValue) {
                if (this.handlersBuilder_ == null) {
                    ensureHandlersIsMutable();
                    this.handlers_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.handlersBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addHandlers(IlTryCatchHandlerMsg value) {
                if (this.handlersBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureHandlersIsMutable();
                    this.handlers_.add(value);
                    onChanged();
                } else {
                    this.handlersBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addHandlers(int index, IlTryCatchHandlerMsg value) {
                if (this.handlersBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureHandlersIsMutable();
                    this.handlers_.add(index, value);
                    onChanged();
                } else {
                    this.handlersBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addHandlers(IlTryCatchHandlerMsg.Builder builderForValue) {
                if (this.handlersBuilder_ == null) {
                    ensureHandlersIsMutable();
                    this.handlers_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.handlersBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addHandlers(int index, IlTryCatchHandlerMsg.Builder builderForValue) {
                if (this.handlersBuilder_ == null) {
                    ensureHandlersIsMutable();
                    this.handlers_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.handlersBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllHandlers(Iterable<? extends IlTryCatchHandlerMsg> values) {
                if (this.handlersBuilder_ == null) {
                    ensureHandlersIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.handlers_);
                    onChanged();
                } else {
                    this.handlersBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearHandlers() {
                if (this.handlersBuilder_ == null) {
                    this.handlers_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    onChanged();
                } else {
                    this.handlersBuilder_.clear();
                }
                return this;
            }

            public Builder removeHandlers(int index) {
                if (this.handlersBuilder_ == null) {
                    ensureHandlersIsMutable();
                    this.handlers_.remove(index);
                    onChanged();
                } else {
                    this.handlersBuilder_.remove(index);
                }
                return this;
            }

            public IlTryCatchHandlerMsg.Builder getHandlersBuilder(int index) {
                return getHandlersFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlTryCatchHandlerMsgOrBuilder getHandlersOrBuilder(int index) {
                if (this.handlersBuilder_ == null) {
                    return this.handlers_.get(index);
                }
                return this.handlersBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<? extends IlTryCatchHandlerMsgOrBuilder> getHandlersOrBuilderList() {
                if (this.handlersBuilder_ != null) {
                    return this.handlersBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.handlers_);
            }

            public IlTryCatchHandlerMsg.Builder addHandlersBuilder() {
                return getHandlersFieldBuilder().addBuilder(IlTryCatchHandlerMsg.getDefaultInstance());
            }

            public IlTryCatchHandlerMsg.Builder addHandlersBuilder(int index) {
                return getHandlersFieldBuilder().addBuilder(index, IlTryCatchHandlerMsg.getDefaultInstance());
            }

            public List<IlTryCatchHandlerMsg.Builder> getHandlersBuilderList() {
                return getHandlersFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlTryCatchHandlerMsg, IlTryCatchHandlerMsg.Builder, IlTryCatchHandlerMsgOrBuilder> getHandlersFieldBuilder() {
                if (this.handlersBuilder_ == null) {
                    this.handlersBuilder_ = new RepeatedFieldBuilderV3<>(this.handlers_, (this.bitField0_ & 4) != 0, getParentForChildren(), isClean());
                    this.handlers_ = null;
                }
                return this.handlersBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasFinallyBlock() {
                return (this.finallyBlockBuilder_ == null && this.finallyBlock_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsg getFinallyBlock() {
                if (this.finallyBlockBuilder_ == null) {
                    return this.finallyBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.finallyBlock_;
                }
                return this.finallyBlockBuilder_.getMessage();
            }

            public Builder setFinallyBlock(IlBlockContainerMsg value) {
                if (this.finallyBlockBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.finallyBlock_ = value;
                    onChanged();
                } else {
                    this.finallyBlockBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setFinallyBlock(IlBlockContainerMsg.Builder builderForValue) {
                if (this.finallyBlockBuilder_ == null) {
                    this.finallyBlock_ = builderForValue.build();
                    onChanged();
                } else {
                    this.finallyBlockBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeFinallyBlock(IlBlockContainerMsg value) {
                if (this.finallyBlockBuilder_ == null) {
                    if (this.finallyBlock_ != null) {
                        this.finallyBlock_ = IlBlockContainerMsg.newBuilder(this.finallyBlock_).mergeFrom(value).buildPartial();
                    } else {
                        this.finallyBlock_ = value;
                    }
                    onChanged();
                } else {
                    this.finallyBlockBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearFinallyBlock() {
                if (this.finallyBlockBuilder_ == null) {
                    this.finallyBlock_ = null;
                    onChanged();
                } else {
                    this.finallyBlock_ = null;
                    this.finallyBlockBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getFinallyBlockBuilder() {
                onChanged();
                return getFinallyBlockFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getFinallyBlockOrBuilder() {
                if (this.finallyBlockBuilder_ != null) {
                    return this.finallyBlockBuilder_.getMessageOrBuilder();
                }
                return this.finallyBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.finallyBlock_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getFinallyBlockFieldBuilder() {
                if (this.finallyBlockBuilder_ == null) {
                    this.finallyBlockBuilder_ = new SingleFieldBuilderV3<>(getFinallyBlock(), getParentForChildren(), isClean());
                    this.finallyBlock_ = null;
                }
                return this.finallyBlockBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasFaultBlock() {
                return (this.faultBlockBuilder_ == null && this.faultBlock_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsg getFaultBlock() {
                if (this.faultBlockBuilder_ == null) {
                    return this.faultBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.faultBlock_;
                }
                return this.faultBlockBuilder_.getMessage();
            }

            public Builder setFaultBlock(IlBlockContainerMsg value) {
                if (this.faultBlockBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.faultBlock_ = value;
                    onChanged();
                } else {
                    this.faultBlockBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setFaultBlock(IlBlockContainerMsg.Builder builderForValue) {
                if (this.faultBlockBuilder_ == null) {
                    this.faultBlock_ = builderForValue.build();
                    onChanged();
                } else {
                    this.faultBlockBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeFaultBlock(IlBlockContainerMsg value) {
                if (this.faultBlockBuilder_ == null) {
                    if (this.faultBlock_ != null) {
                        this.faultBlock_ = IlBlockContainerMsg.newBuilder(this.faultBlock_).mergeFrom(value).buildPartial();
                    } else {
                        this.faultBlock_ = value;
                    }
                    onChanged();
                } else {
                    this.faultBlockBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearFaultBlock() {
                if (this.faultBlockBuilder_ == null) {
                    this.faultBlock_ = null;
                    onChanged();
                } else {
                    this.faultBlock_ = null;
                    this.faultBlockBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getFaultBlockBuilder() {
                onChanged();
                return getFaultBlockFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getFaultBlockOrBuilder() {
                if (this.faultBlockBuilder_ != null) {
                    return this.faultBlockBuilder_.getMessageOrBuilder();
                }
                return this.faultBlock_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.faultBlock_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getFaultBlockFieldBuilder() {
                if (this.faultBlockBuilder_ == null) {
                    this.faultBlockBuilder_ = new SingleFieldBuilderV3<>(getFaultBlock(), getParentForChildren(), isClean());
                    this.faultBlock_ = null;
                }
                return this.faultBlockBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasBody() {
                return (this.bodyBuilder_ == null && this.body_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsg getBody() {
                if (this.bodyBuilder_ == null) {
                    return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
                }
                return this.bodyBuilder_.getMessage();
            }

            public Builder setBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.body_ = value;
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setBody(IlBlockContainerMsg.Builder builderForValue) {
                if (this.bodyBuilder_ == null) {
                    this.body_ = builderForValue.build();
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (this.body_ != null) {
                        this.body_ = IlBlockContainerMsg.newBuilder(this.body_).mergeFrom(value).buildPartial();
                    } else {
                        this.body_ = value;
                    }
                    onChanged();
                } else {
                    this.bodyBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearBody() {
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                    onChanged();
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getBodyBuilder() {
                onChanged();
                return getBodyFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
                if (this.bodyBuilder_ != null) {
                    return this.bodyBuilder_.getMessageOrBuilder();
                }
                return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getBodyFieldBuilder() {
                if (this.bodyBuilder_ == null) {
                    this.bodyBuilder_ = new SingleFieldBuilderV3<>(getBody(), getParentForChildren(), isClean());
                    this.body_ = null;
                }
                return this.bodyBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasKeyInstr() {
                return (this.keyInstrBuilder_ == null && this.keyInstr_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getKeyInstr() {
                if (this.keyInstrBuilder_ == null) {
                    return this.keyInstr_ == null ? IlInstructionMsg.getDefaultInstance() : this.keyInstr_;
                }
                return this.keyInstrBuilder_.getMessage();
            }

            public Builder setKeyInstr(IlInstructionMsg value) {
                if (this.keyInstrBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.keyInstr_ = value;
                    onChanged();
                } else {
                    this.keyInstrBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setKeyInstr(Builder builderForValue) {
                if (this.keyInstrBuilder_ == null) {
                    this.keyInstr_ = builderForValue.build();
                    onChanged();
                } else {
                    this.keyInstrBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeKeyInstr(IlInstructionMsg value) {
                if (this.keyInstrBuilder_ == null) {
                    if (this.keyInstr_ != null) {
                        this.keyInstr_ = IlInstructionMsg.newBuilder(this.keyInstr_).mergeFrom(value).buildPartial();
                    } else {
                        this.keyInstr_ = value;
                    }
                    onChanged();
                } else {
                    this.keyInstrBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearKeyInstr() {
                if (this.keyInstrBuilder_ == null) {
                    this.keyInstr_ = null;
                    onChanged();
                } else {
                    this.keyInstr_ = null;
                    this.keyInstrBuilder_ = null;
                }
                return this;
            }

            public Builder getKeyInstrBuilder() {
                onChanged();
                return getKeyInstrFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getKeyInstrOrBuilder() {
                if (this.keyInstrBuilder_ != null) {
                    return this.keyInstrBuilder_.getMessageOrBuilder();
                }
                return this.keyInstr_ == null ? IlInstructionMsg.getDefaultInstance() : this.keyInstr_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getKeyInstrFieldBuilder() {
                if (this.keyInstrBuilder_ == null) {
                    this.keyInstrBuilder_ = new SingleFieldBuilderV3<>(getKeyInstr(), getParentForChildren(), isClean());
                    this.keyInstr_ = null;
                }
                return this.keyInstrBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public boolean hasDefaultInst() {
                return (this.defaultInstBuilder_ == null && this.defaultInst_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsg getDefaultInst() {
                if (this.defaultInstBuilder_ == null) {
                    return this.defaultInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.defaultInst_;
                }
                return this.defaultInstBuilder_.getMessage();
            }

            public Builder setDefaultInst(IlInstructionMsg value) {
                if (this.defaultInstBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.defaultInst_ = value;
                    onChanged();
                } else {
                    this.defaultInstBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setDefaultInst(Builder builderForValue) {
                if (this.defaultInstBuilder_ == null) {
                    this.defaultInst_ = builderForValue.build();
                    onChanged();
                } else {
                    this.defaultInstBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeDefaultInst(IlInstructionMsg value) {
                if (this.defaultInstBuilder_ == null) {
                    if (this.defaultInst_ != null) {
                        this.defaultInst_ = IlInstructionMsg.newBuilder(this.defaultInst_).mergeFrom(value).buildPartial();
                    } else {
                        this.defaultInst_ = value;
                    }
                    onChanged();
                } else {
                    this.defaultInstBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearDefaultInst() {
                if (this.defaultInstBuilder_ == null) {
                    this.defaultInst_ = null;
                    onChanged();
                } else {
                    this.defaultInst_ = null;
                    this.defaultInstBuilder_ = null;
                }
                return this;
            }

            public Builder getDefaultInstBuilder() {
                onChanged();
                return getDefaultInstFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlInstructionMsgOrBuilder getDefaultInstOrBuilder() {
                if (this.defaultInstBuilder_ != null) {
                    return this.defaultInstBuilder_.getMessageOrBuilder();
                }
                return this.defaultInst_ == null ? IlInstructionMsg.getDefaultInstance() : this.defaultInst_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, Builder, IlInstructionMsgOrBuilder> getDefaultInstFieldBuilder() {
                if (this.defaultInstBuilder_ == null) {
                    this.defaultInstBuilder_ = new SingleFieldBuilderV3<>(getDefaultInst(), getParentForChildren(), isClean());
                    this.defaultInst_ = null;
                }
                return this.defaultInstBuilder_;
            }

            private void ensureSwitchSectionsIsMutable() {
                if ((this.bitField0_ & 8) == 0) {
                    this.switchSections_ = new ArrayList(this.switchSections_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<IlSwitchSectionMsg> getSwitchSectionsList() {
                if (this.switchSectionsBuilder_ == null) {
                    return Collections.unmodifiableList(this.switchSections_);
                }
                return this.switchSectionsBuilder_.getMessageList();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public int getSwitchSectionsCount() {
                if (this.switchSectionsBuilder_ == null) {
                    return this.switchSections_.size();
                }
                return this.switchSectionsBuilder_.getCount();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlSwitchSectionMsg getSwitchSections(int index) {
                if (this.switchSectionsBuilder_ == null) {
                    return this.switchSections_.get(index);
                }
                return this.switchSectionsBuilder_.getMessage(index);
            }

            public Builder setSwitchSections(int index, IlSwitchSectionMsg value) {
                if (this.switchSectionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.set(index, value);
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setSwitchSections(int index, IlSwitchSectionMsg.Builder builderForValue) {
                if (this.switchSectionsBuilder_ == null) {
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addSwitchSections(IlSwitchSectionMsg value) {
                if (this.switchSectionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.add(value);
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addSwitchSections(int index, IlSwitchSectionMsg value) {
                if (this.switchSectionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.add(index, value);
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addSwitchSections(IlSwitchSectionMsg.Builder builderForValue) {
                if (this.switchSectionsBuilder_ == null) {
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addSwitchSections(int index, IlSwitchSectionMsg.Builder builderForValue) {
                if (this.switchSectionsBuilder_ == null) {
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllSwitchSections(Iterable<? extends IlSwitchSectionMsg> values) {
                if (this.switchSectionsBuilder_ == null) {
                    ensureSwitchSectionsIsMutable();
                    AbstractMessageLite.Builder.addAll((Iterable) values, (List) this.switchSections_);
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearSwitchSections() {
                if (this.switchSectionsBuilder_ == null) {
                    this.switchSections_ = Collections.emptyList();
                    this.bitField0_ &= -9;
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.clear();
                }
                return this;
            }

            public Builder removeSwitchSections(int index) {
                if (this.switchSectionsBuilder_ == null) {
                    ensureSwitchSectionsIsMutable();
                    this.switchSections_.remove(index);
                    onChanged();
                } else {
                    this.switchSectionsBuilder_.remove(index);
                }
                return this;
            }

            public IlSwitchSectionMsg.Builder getSwitchSectionsBuilder(int index) {
                return getSwitchSectionsFieldBuilder().getBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public IlSwitchSectionMsgOrBuilder getSwitchSectionsOrBuilder(int index) {
                if (this.switchSectionsBuilder_ == null) {
                    return this.switchSections_.get(index);
                }
                return this.switchSectionsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlInstructionMsgOrBuilder
            public List<? extends IlSwitchSectionMsgOrBuilder> getSwitchSectionsOrBuilderList() {
                if (this.switchSectionsBuilder_ != null) {
                    return this.switchSectionsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.switchSections_);
            }

            public IlSwitchSectionMsg.Builder addSwitchSectionsBuilder() {
                return getSwitchSectionsFieldBuilder().addBuilder(IlSwitchSectionMsg.getDefaultInstance());
            }

            public IlSwitchSectionMsg.Builder addSwitchSectionsBuilder(int index) {
                return getSwitchSectionsFieldBuilder().addBuilder(index, IlSwitchSectionMsg.getDefaultInstance());
            }

            public List<IlSwitchSectionMsg.Builder> getSwitchSectionsBuilderList() {
                return getSwitchSectionsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilderV3<IlSwitchSectionMsg, IlSwitchSectionMsg.Builder, IlSwitchSectionMsgOrBuilder> getSwitchSectionsFieldBuilder() {
                if (this.switchSectionsBuilder_ == null) {
                    this.switchSectionsBuilder_ = new RepeatedFieldBuilderV3<>(this.switchSections_, (this.bitField0_ & 8) != 0, getParentForChildren(), isClean());
                    this.switchSections_ = null;
                }
                return this.switchSectionsBuilder_;
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

        public static IlInstructionMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlInstructionMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlInstructionMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlInstructionMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlVariableMsg.class */
    public static final class IlVariableMsg extends GeneratedMessageV3 implements IlVariableMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int TYPE_FIELD_NUMBER = 1;
        private ProtoAssemblyAllTypes.TypeDefinition type_;
        public static final int NAME_FIELD_NUMBER = 2;
        private volatile Object name_;
        public static final int HAS_INITIAL_VALUE_FIELD_NUMBER = 3;
        private boolean hasInitialValue_;
        public static final int VARIABLE_KIND_FIELD_NUMBER = 4;
        private int variableKind_;
        private byte memoizedIsInitialized;
        private static final IlVariableMsg DEFAULT_INSTANCE = new IlVariableMsg();
        private static final Parser<IlVariableMsg> PARSER = new AbstractParser<IlVariableMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlVariableMsg.1
            @Override // com.google.protobuf.Parser
            public IlVariableMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlVariableMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlVariableMsg(GeneratedMessageV3.Builder builder, IlVariableMsg ilVariableMsg) {
            this(builder);
        }

        private IlVariableMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlVariableMsg() {
            this.memoizedIsInitialized = (byte) -1;
            this.name_ = "";
            this.variableKind_ = 0;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlVariableMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlVariableMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlVariableMsg ilVariableMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlVariableMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                    ProtoAssemblyAllTypes.TypeDefinition.Builder subBuilder = null;
                                    subBuilder = this.type_ != null ? this.type_.toBuilder() : subBuilder;
                                    this.type_ = (ProtoAssemblyAllTypes.TypeDefinition) input.readMessage(ProtoAssemblyAllTypes.TypeDefinition.parser(), extensionRegistry);
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
                                case 24:
                                    this.hasInitialValue_ = input.readBool();
                                    break;
                                case 32:
                                    int rawValue = input.readEnum();
                                    this.variableKind_ = rawValue;
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
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            }
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return ProtoIlInstructions.internal_static_IlVariableMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlVariableMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlVariableMsg.class, Builder.class);
        }

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlVariableMsg$IlVariableKind.class */
        public enum IlVariableKind implements ProtocolMessageEnum {
            NONE(0),
            LOCAL(1),
            PINNED_LOCAL(2),
            PINNED_REGION_LOCAL(3),
            USING_LOCAL(4),
            FOREACH_LOCAL(5),
            INITIALIZER_TARGET(6),
            PARAMETER(7),
            EXCEPTION_STACK_SLOT(8),
            EXCEPTION_LOCAL(9),
            STACKSLOT(10),
            NAMED_ARGUMENT(11),
            DISPLAY_CLASS_LOCAL(12),
            PATTERN_LOCAL(13),
            DECONSTRUCTION_INIT_TEMPORARY(14),
            UNRECOGNIZED(-1);
            
            public static final int NONE_VALUE = 0;
            public static final int LOCAL_VALUE = 1;
            public static final int PINNED_LOCAL_VALUE = 2;
            public static final int PINNED_REGION_LOCAL_VALUE = 3;
            public static final int USING_LOCAL_VALUE = 4;
            public static final int FOREACH_LOCAL_VALUE = 5;
            public static final int INITIALIZER_TARGET_VALUE = 6;
            public static final int PARAMETER_VALUE = 7;
            public static final int EXCEPTION_STACK_SLOT_VALUE = 8;
            public static final int EXCEPTION_LOCAL_VALUE = 9;
            public static final int STACKSLOT_VALUE = 10;
            public static final int NAMED_ARGUMENT_VALUE = 11;
            public static final int DISPLAY_CLASS_LOCAL_VALUE = 12;
            public static final int PATTERN_LOCAL_VALUE = 13;
            public static final int DECONSTRUCTION_INIT_TEMPORARY_VALUE = 14;
            private static final Internal.EnumLiteMap<IlVariableKind> internalValueMap = new Internal.EnumLiteMap<IlVariableKind>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlVariableMsg.IlVariableKind.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // com.google.protobuf.Internal.EnumLiteMap
                public IlVariableKind findValueByNumber(int number) {
                    return IlVariableKind.forNumber(number);
                }
            };
            private static final IlVariableKind[] VALUES = valuesCustom();
            private final int value;

            /* renamed from: values  reason: to resolve conflict with enum method */
            public static IlVariableKind[] valuesCustom() {
                IlVariableKind[] valuesCustom = values();
                int length = valuesCustom.length;
                IlVariableKind[] ilVariableKindArr = new IlVariableKind[length];
                System.arraycopy(valuesCustom, 0, ilVariableKindArr, 0, length);
                return ilVariableKindArr;
            }

            @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
            public final int getNumber() {
                if (this == UNRECOGNIZED) {
                    throw new IllegalArgumentException("Can't get the number of an unknown enum value.");
                }
                return this.value;
            }

            @Deprecated
            public static IlVariableKind valueOf(int value) {
                return forNumber(value);
            }

            public static IlVariableKind forNumber(int value) {
                switch (value) {
                    case 0:
                        return NONE;
                    case 1:
                        return LOCAL;
                    case 2:
                        return PINNED_LOCAL;
                    case 3:
                        return PINNED_REGION_LOCAL;
                    case 4:
                        return USING_LOCAL;
                    case 5:
                        return FOREACH_LOCAL;
                    case 6:
                        return INITIALIZER_TARGET;
                    case 7:
                        return PARAMETER;
                    case 8:
                        return EXCEPTION_STACK_SLOT;
                    case 9:
                        return EXCEPTION_LOCAL;
                    case 10:
                        return STACKSLOT;
                    case 11:
                        return NAMED_ARGUMENT;
                    case 12:
                        return DISPLAY_CLASS_LOCAL;
                    case 13:
                        return PATTERN_LOCAL;
                    case 14:
                        return DECONSTRUCTION_INIT_TEMPORARY;
                    default:
                        return null;
                }
            }

            public static Internal.EnumLiteMap<IlVariableKind> internalGetValueMap() {
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
                return IlVariableMsg.getDescriptor().getEnumTypes().get(0);
            }

            public static IlVariableKind valueOf(Descriptors.EnumValueDescriptor desc) {
                if (desc.getType() != getDescriptor()) {
                    throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                }
                if (desc.getIndex() == -1) {
                    return UNRECOGNIZED;
                }
                return VALUES[desc.getIndex()];
            }

            IlVariableKind(int value) {
                this.value = value;
            }
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public boolean hasType() {
            return this.type_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public ProtoAssemblyAllTypes.TypeDefinition getType() {
            return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder() {
            return getType();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
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

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public boolean getHasInitialValue() {
            return this.hasInitialValue_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public int getVariableKindValue() {
            return this.variableKind_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
        public IlVariableKind getVariableKind() {
            IlVariableKind result = IlVariableKind.valueOf(this.variableKind_);
            return result == null ? IlVariableKind.UNRECOGNIZED : result;
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
            if (!getNameBytes().isEmpty()) {
                GeneratedMessageV3.writeString(output, 2, this.name_);
            }
            if (this.hasInitialValue_) {
                output.writeBool(3, this.hasInitialValue_);
            }
            if (this.variableKind_ != IlVariableKind.NONE.getNumber()) {
                output.writeEnum(4, this.variableKind_);
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
            if (this.hasInitialValue_) {
                size2 += CodedOutputStream.computeBoolSize(3, this.hasInitialValue_);
            }
            if (this.variableKind_ != IlVariableKind.NONE.getNumber()) {
                size2 += CodedOutputStream.computeEnumSize(4, this.variableKind_);
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
            if (!(obj instanceof IlVariableMsg)) {
                return super.equals(obj);
            }
            IlVariableMsg other = (IlVariableMsg) obj;
            if (hasType() != other.hasType()) {
                return false;
            }
            if ((hasType() && !getType().equals(other.getType())) || !getName().equals(other.getName()) || getHasInitialValue() != other.getHasInitialValue() || this.variableKind_ != other.variableKind_ || !this.unknownFields.equals(other.unknownFields)) {
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
            int hash2 = (29 * ((53 * ((37 * ((53 * ((37 * ((53 * ((37 * hash) + 2)) + getName().hashCode())) + 3)) + Internal.hashBoolean(getHasInitialValue()))) + 4)) + this.variableKind_)) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static IlVariableMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlVariableMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlVariableMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlVariableMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlVariableMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlVariableMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlVariableMsg parseFrom(InputStream input) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlVariableMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlVariableMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlVariableMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlVariableMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlVariableMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlVariableMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlVariableMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlVariableMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlVariableMsgOrBuilder {
            private ProtoAssemblyAllTypes.TypeDefinition type_;
            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.TypeDefinition, ProtoAssemblyAllTypes.TypeDefinition.Builder, ProtoAssemblyAllTypes.TypeDefinitionOrBuilder> typeBuilder_;
            private Object name_;
            private boolean hasInitialValue_;
            private int variableKind_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlVariableMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlVariableMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlVariableMsg.class, Builder.class);
            }

            private Builder() {
                this.name_ = "";
                this.variableKind_ = 0;
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
                this.variableKind_ = 0;
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = IlVariableMsg.alwaysUseFieldBuilders;
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
                this.hasInitialValue_ = false;
                this.variableKind_ = 0;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlVariableMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlVariableMsg getDefaultInstanceForType() {
                return IlVariableMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlVariableMsg build() {
                IlVariableMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlVariableMsg buildPartial() {
                IlVariableMsg result = new IlVariableMsg(this, (IlVariableMsg) null);
                if (this.typeBuilder_ == null) {
                    result.type_ = this.type_;
                } else {
                    result.type_ = this.typeBuilder_.build();
                }
                result.name_ = this.name_;
                result.hasInitialValue_ = this.hasInitialValue_;
                result.variableKind_ = this.variableKind_;
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
                if (other instanceof IlVariableMsg) {
                    return mergeFrom((IlVariableMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlVariableMsg other) {
                if (other == IlVariableMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasType()) {
                    mergeType(other.getType());
                }
                if (!other.getName().isEmpty()) {
                    this.name_ = other.name_;
                    onChanged();
                }
                if (other.getHasInitialValue()) {
                    setHasInitialValue(other.getHasInitialValue());
                }
                if (other.variableKind_ != 0) {
                    setVariableKindValue(other.getVariableKindValue());
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
                IlVariableMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlVariableMsg) IlVariableMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlVariableMsg ilVariableMsg = (IlVariableMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public boolean hasType() {
                return (this.typeBuilder_ == null && this.type_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public ProtoAssemblyAllTypes.TypeDefinition getType() {
                if (this.typeBuilder_ == null) {
                    return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
                }
                return this.typeBuilder_.getMessage();
            }

            public Builder setType(ProtoAssemblyAllTypes.TypeDefinition value) {
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

            public Builder setType(ProtoAssemblyAllTypes.TypeDefinition.Builder builderForValue) {
                if (this.typeBuilder_ == null) {
                    this.type_ = builderForValue.build();
                    onChanged();
                } else {
                    this.typeBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeType(ProtoAssemblyAllTypes.TypeDefinition value) {
                if (this.typeBuilder_ == null) {
                    if (this.type_ != null) {
                        this.type_ = ProtoAssemblyAllTypes.TypeDefinition.newBuilder(this.type_).mergeFrom(value).buildPartial();
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

            public ProtoAssemblyAllTypes.TypeDefinition.Builder getTypeBuilder() {
                onChanged();
                return getTypeFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public ProtoAssemblyAllTypes.TypeDefinitionOrBuilder getTypeOrBuilder() {
                if (this.typeBuilder_ != null) {
                    return this.typeBuilder_.getMessageOrBuilder();
                }
                return this.type_ == null ? ProtoAssemblyAllTypes.TypeDefinition.getDefaultInstance() : this.type_;
            }

            private SingleFieldBuilderV3<ProtoAssemblyAllTypes.TypeDefinition, ProtoAssemblyAllTypes.TypeDefinition.Builder, ProtoAssemblyAllTypes.TypeDefinitionOrBuilder> getTypeFieldBuilder() {
                if (this.typeBuilder_ == null) {
                    this.typeBuilder_ = new SingleFieldBuilderV3<>(getType(), getParentForChildren(), isClean());
                    this.type_ = null;
                }
                return this.typeBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
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

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
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
                this.name_ = IlVariableMsg.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value != null) {
                    IlVariableMsg.checkByteStringIsUtf8(value);
                    this.name_ = value;
                    onChanged();
                    return this;
                }
                throw new NullPointerException();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public boolean getHasInitialValue() {
                return this.hasInitialValue_;
            }

            public Builder setHasInitialValue(boolean value) {
                this.hasInitialValue_ = value;
                onChanged();
                return this;
            }

            public Builder clearHasInitialValue() {
                this.hasInitialValue_ = false;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public int getVariableKindValue() {
                return this.variableKind_;
            }

            public Builder setVariableKindValue(int value) {
                this.variableKind_ = value;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlVariableMsgOrBuilder
            public IlVariableKind getVariableKind() {
                IlVariableKind result = IlVariableKind.valueOf(this.variableKind_);
                return result == null ? IlVariableKind.UNRECOGNIZED : result;
            }

            public Builder setVariableKind(IlVariableKind value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.variableKind_ = value.getNumber();
                onChanged();
                return this;
            }

            public Builder clearVariableKind() {
                this.variableKind_ = 0;
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

        public static IlVariableMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlVariableMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlVariableMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlVariableMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlTryCatchHandlerMsg.class */
    public static final class IlTryCatchHandlerMsg extends GeneratedMessageV3 implements IlTryCatchHandlerMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int BODY_FIELD_NUMBER = 1;
        private IlBlockContainerMsg body_;
        public static final int VARIABLE_FIELD_NUMBER = 2;
        private IlVariableMsg variable_;
        public static final int FILTER_FIELD_NUMBER = 3;
        private IlBlockContainerMsg filter_;
        public static final int HAS_FILTER_FIELD_NUMBER = 4;
        private boolean hasFilter_;
        private byte memoizedIsInitialized;
        private static final IlTryCatchHandlerMsg DEFAULT_INSTANCE = new IlTryCatchHandlerMsg();
        private static final Parser<IlTryCatchHandlerMsg> PARSER = new AbstractParser<IlTryCatchHandlerMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsg.1
            @Override // com.google.protobuf.Parser
            public IlTryCatchHandlerMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlTryCatchHandlerMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlTryCatchHandlerMsg(GeneratedMessageV3.Builder builder, IlTryCatchHandlerMsg ilTryCatchHandlerMsg) {
            this(builder);
        }

        private IlTryCatchHandlerMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlTryCatchHandlerMsg() {
            this.memoizedIsInitialized = (byte) -1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlTryCatchHandlerMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlTryCatchHandlerMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlTryCatchHandlerMsg ilTryCatchHandlerMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlTryCatchHandlerMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                            case 10:
                                IlBlockContainerMsg.Builder subBuilder = null;
                                subBuilder = this.body_ != null ? this.body_.toBuilder() : subBuilder;
                                this.body_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder == null) {
                                    break;
                                } else {
                                    subBuilder.mergeFrom(this.body_);
                                    this.body_ = subBuilder.buildPartial();
                                    break;
                                }
                            case 18:
                                IlVariableMsg.Builder subBuilder2 = null;
                                subBuilder2 = this.variable_ != null ? this.variable_.toBuilder() : subBuilder2;
                                this.variable_ = (IlVariableMsg) input.readMessage(IlVariableMsg.parser(), extensionRegistry);
                                if (subBuilder2 == null) {
                                    break;
                                } else {
                                    subBuilder2.mergeFrom(this.variable_);
                                    this.variable_ = subBuilder2.buildPartial();
                                    break;
                                }
                            case 26:
                                IlBlockContainerMsg.Builder subBuilder3 = null;
                                subBuilder3 = this.filter_ != null ? this.filter_.toBuilder() : subBuilder3;
                                this.filter_ = (IlBlockContainerMsg) input.readMessage(IlBlockContainerMsg.parser(), extensionRegistry);
                                if (subBuilder3 == null) {
                                    break;
                                } else {
                                    subBuilder3.mergeFrom(this.filter_);
                                    this.filter_ = subBuilder3.buildPartial();
                                    break;
                                }
                            case 32:
                                this.hasFilter_ = input.readBool();
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
            return ProtoIlInstructions.internal_static_IlTryCatchHandlerMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlTryCatchHandlerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlTryCatchHandlerMsg.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public boolean hasBody() {
            return this.body_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlBlockContainerMsg getBody() {
            return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
            return getBody();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public boolean hasVariable() {
            return this.variable_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlVariableMsg getVariable() {
            return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlVariableMsgOrBuilder getVariableOrBuilder() {
            return getVariable();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public boolean hasFilter() {
            return this.filter_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlBlockContainerMsg getFilter() {
            return this.filter_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.filter_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public IlBlockContainerMsgOrBuilder getFilterOrBuilder() {
            return getFilter();
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
        public boolean getHasFilter() {
            return this.hasFilter_;
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
            if (this.body_ != null) {
                output.writeMessage(1, getBody());
            }
            if (this.variable_ != null) {
                output.writeMessage(2, getVariable());
            }
            if (this.filter_ != null) {
                output.writeMessage(3, getFilter());
            }
            if (this.hasFilter_) {
                output.writeBool(4, this.hasFilter_);
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
            if (this.body_ != null) {
                size2 = 0 + CodedOutputStream.computeMessageSize(1, getBody());
            }
            if (this.variable_ != null) {
                size2 += CodedOutputStream.computeMessageSize(2, getVariable());
            }
            if (this.filter_ != null) {
                size2 += CodedOutputStream.computeMessageSize(3, getFilter());
            }
            if (this.hasFilter_) {
                size2 += CodedOutputStream.computeBoolSize(4, this.hasFilter_);
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
            if (!(obj instanceof IlTryCatchHandlerMsg)) {
                return super.equals(obj);
            }
            IlTryCatchHandlerMsg other = (IlTryCatchHandlerMsg) obj;
            if (hasBody() != other.hasBody()) {
                return false;
            }
            if ((hasBody() && !getBody().equals(other.getBody())) || hasVariable() != other.hasVariable()) {
                return false;
            }
            if ((hasVariable() && !getVariable().equals(other.getVariable())) || hasFilter() != other.hasFilter()) {
                return false;
            }
            if ((hasFilter() && !getFilter().equals(other.getFilter())) || getHasFilter() != other.getHasFilter() || !this.unknownFields.equals(other.unknownFields)) {
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
            if (hasBody()) {
                hash = (53 * ((37 * hash) + 1)) + getBody().hashCode();
            }
            if (hasVariable()) {
                hash = (53 * ((37 * hash) + 2)) + getVariable().hashCode();
            }
            if (hasFilter()) {
                hash = (53 * ((37 * hash) + 3)) + getFilter().hashCode();
            }
            int hash2 = (29 * ((53 * ((37 * hash) + 4)) + Internal.hashBoolean(getHasFilter()))) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash2;
            return hash2;
        }

        public static IlTryCatchHandlerMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlTryCatchHandlerMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlTryCatchHandlerMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlTryCatchHandlerMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlTryCatchHandlerMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlTryCatchHandlerMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlTryCatchHandlerMsg parseFrom(InputStream input) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlTryCatchHandlerMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlTryCatchHandlerMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlTryCatchHandlerMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlTryCatchHandlerMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlTryCatchHandlerMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlTryCatchHandlerMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlTryCatchHandlerMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlTryCatchHandlerMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlTryCatchHandlerMsgOrBuilder {
            private IlBlockContainerMsg body_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> bodyBuilder_;
            private IlVariableMsg variable_;
            private SingleFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> variableBuilder_;
            private IlBlockContainerMsg filter_;
            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> filterBuilder_;
            private boolean hasFilter_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlTryCatchHandlerMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlTryCatchHandlerMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlTryCatchHandlerMsg.class, Builder.class);
            }

            private Builder() {
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
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = IlTryCatchHandlerMsg.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                if (this.variableBuilder_ == null) {
                    this.variable_ = null;
                } else {
                    this.variable_ = null;
                    this.variableBuilder_ = null;
                }
                if (this.filterBuilder_ == null) {
                    this.filter_ = null;
                } else {
                    this.filter_ = null;
                    this.filterBuilder_ = null;
                }
                this.hasFilter_ = false;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlTryCatchHandlerMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlTryCatchHandlerMsg getDefaultInstanceForType() {
                return IlTryCatchHandlerMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlTryCatchHandlerMsg build() {
                IlTryCatchHandlerMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlTryCatchHandlerMsg buildPartial() {
                IlTryCatchHandlerMsg result = new IlTryCatchHandlerMsg(this, (IlTryCatchHandlerMsg) null);
                if (this.bodyBuilder_ == null) {
                    result.body_ = this.body_;
                } else {
                    result.body_ = this.bodyBuilder_.build();
                }
                if (this.variableBuilder_ == null) {
                    result.variable_ = this.variable_;
                } else {
                    result.variable_ = this.variableBuilder_.build();
                }
                if (this.filterBuilder_ == null) {
                    result.filter_ = this.filter_;
                } else {
                    result.filter_ = this.filterBuilder_.build();
                }
                result.hasFilter_ = this.hasFilter_;
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
                if (other instanceof IlTryCatchHandlerMsg) {
                    return mergeFrom((IlTryCatchHandlerMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlTryCatchHandlerMsg other) {
                if (other == IlTryCatchHandlerMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.hasBody()) {
                    mergeBody(other.getBody());
                }
                if (other.hasVariable()) {
                    mergeVariable(other.getVariable());
                }
                if (other.hasFilter()) {
                    mergeFilter(other.getFilter());
                }
                if (other.getHasFilter()) {
                    setHasFilter(other.getHasFilter());
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
                IlTryCatchHandlerMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlTryCatchHandlerMsg) IlTryCatchHandlerMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlTryCatchHandlerMsg ilTryCatchHandlerMsg = (IlTryCatchHandlerMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public boolean hasBody() {
                return (this.bodyBuilder_ == null && this.body_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlBlockContainerMsg getBody() {
                if (this.bodyBuilder_ == null) {
                    return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
                }
                return this.bodyBuilder_.getMessage();
            }

            public Builder setBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.body_ = value;
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setBody(IlBlockContainerMsg.Builder builderForValue) {
                if (this.bodyBuilder_ == null) {
                    this.body_ = builderForValue.build();
                    onChanged();
                } else {
                    this.bodyBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeBody(IlBlockContainerMsg value) {
                if (this.bodyBuilder_ == null) {
                    if (this.body_ != null) {
                        this.body_ = IlBlockContainerMsg.newBuilder(this.body_).mergeFrom(value).buildPartial();
                    } else {
                        this.body_ = value;
                    }
                    onChanged();
                } else {
                    this.bodyBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearBody() {
                if (this.bodyBuilder_ == null) {
                    this.body_ = null;
                    onChanged();
                } else {
                    this.body_ = null;
                    this.bodyBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getBodyBuilder() {
                onChanged();
                return getBodyFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getBodyOrBuilder() {
                if (this.bodyBuilder_ != null) {
                    return this.bodyBuilder_.getMessageOrBuilder();
                }
                return this.body_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.body_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getBodyFieldBuilder() {
                if (this.bodyBuilder_ == null) {
                    this.bodyBuilder_ = new SingleFieldBuilderV3<>(getBody(), getParentForChildren(), isClean());
                    this.body_ = null;
                }
                return this.bodyBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public boolean hasVariable() {
                return (this.variableBuilder_ == null && this.variable_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlVariableMsg getVariable() {
                if (this.variableBuilder_ == null) {
                    return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
                }
                return this.variableBuilder_.getMessage();
            }

            public Builder setVariable(IlVariableMsg value) {
                if (this.variableBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.variable_ = value;
                    onChanged();
                } else {
                    this.variableBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setVariable(IlVariableMsg.Builder builderForValue) {
                if (this.variableBuilder_ == null) {
                    this.variable_ = builderForValue.build();
                    onChanged();
                } else {
                    this.variableBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeVariable(IlVariableMsg value) {
                if (this.variableBuilder_ == null) {
                    if (this.variable_ != null) {
                        this.variable_ = IlVariableMsg.newBuilder(this.variable_).mergeFrom(value).buildPartial();
                    } else {
                        this.variable_ = value;
                    }
                    onChanged();
                } else {
                    this.variableBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearVariable() {
                if (this.variableBuilder_ == null) {
                    this.variable_ = null;
                    onChanged();
                } else {
                    this.variable_ = null;
                    this.variableBuilder_ = null;
                }
                return this;
            }

            public IlVariableMsg.Builder getVariableBuilder() {
                onChanged();
                return getVariableFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlVariableMsgOrBuilder getVariableOrBuilder() {
                if (this.variableBuilder_ != null) {
                    return this.variableBuilder_.getMessageOrBuilder();
                }
                return this.variable_ == null ? IlVariableMsg.getDefaultInstance() : this.variable_;
            }

            private SingleFieldBuilderV3<IlVariableMsg, IlVariableMsg.Builder, IlVariableMsgOrBuilder> getVariableFieldBuilder() {
                if (this.variableBuilder_ == null) {
                    this.variableBuilder_ = new SingleFieldBuilderV3<>(getVariable(), getParentForChildren(), isClean());
                    this.variable_ = null;
                }
                return this.variableBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public boolean hasFilter() {
                return (this.filterBuilder_ == null && this.filter_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlBlockContainerMsg getFilter() {
                if (this.filterBuilder_ == null) {
                    return this.filter_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.filter_;
                }
                return this.filterBuilder_.getMessage();
            }

            public Builder setFilter(IlBlockContainerMsg value) {
                if (this.filterBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.filter_ = value;
                    onChanged();
                } else {
                    this.filterBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setFilter(IlBlockContainerMsg.Builder builderForValue) {
                if (this.filterBuilder_ == null) {
                    this.filter_ = builderForValue.build();
                    onChanged();
                } else {
                    this.filterBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeFilter(IlBlockContainerMsg value) {
                if (this.filterBuilder_ == null) {
                    if (this.filter_ != null) {
                        this.filter_ = IlBlockContainerMsg.newBuilder(this.filter_).mergeFrom(value).buildPartial();
                    } else {
                        this.filter_ = value;
                    }
                    onChanged();
                } else {
                    this.filterBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearFilter() {
                if (this.filterBuilder_ == null) {
                    this.filter_ = null;
                    onChanged();
                } else {
                    this.filter_ = null;
                    this.filterBuilder_ = null;
                }
                return this;
            }

            public IlBlockContainerMsg.Builder getFilterBuilder() {
                onChanged();
                return getFilterFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public IlBlockContainerMsgOrBuilder getFilterOrBuilder() {
                if (this.filterBuilder_ != null) {
                    return this.filterBuilder_.getMessageOrBuilder();
                }
                return this.filter_ == null ? IlBlockContainerMsg.getDefaultInstance() : this.filter_;
            }

            private SingleFieldBuilderV3<IlBlockContainerMsg, IlBlockContainerMsg.Builder, IlBlockContainerMsgOrBuilder> getFilterFieldBuilder() {
                if (this.filterBuilder_ == null) {
                    this.filterBuilder_ = new SingleFieldBuilderV3<>(getFilter(), getParentForChildren(), isClean());
                    this.filter_ = null;
                }
                return this.filterBuilder_;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlTryCatchHandlerMsgOrBuilder
            public boolean getHasFilter() {
                return this.hasFilter_;
            }

            public Builder setHasFilter(boolean value) {
                this.hasFilter_ = value;
                onChanged();
                return this;
            }

            public Builder clearHasFilter() {
                this.hasFilter_ = false;
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

        public static IlTryCatchHandlerMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlTryCatchHandlerMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlTryCatchHandlerMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlTryCatchHandlerMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlSwitchSectionMsg.class */
    public static final class IlSwitchSectionMsg extends GeneratedMessageV3 implements IlSwitchSectionMsgOrBuilder {
        private static final long serialVersionUID = 0;
        public static final int LABEL_FIELD_NUMBER = 1;
        private long label_;
        public static final int TARGET_INSTR_FIELD_NUMBER = 2;
        private IlInstructionMsg targetInstr_;
        private byte memoizedIsInitialized;
        private static final IlSwitchSectionMsg DEFAULT_INSTANCE = new IlSwitchSectionMsg();
        private static final Parser<IlSwitchSectionMsg> PARSER = new AbstractParser<IlSwitchSectionMsg>() { // from class: soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsg.1
            @Override // com.google.protobuf.Parser
            public IlSwitchSectionMsg parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new IlSwitchSectionMsg(input, extensionRegistry, null);
            }
        };

        /* synthetic */ IlSwitchSectionMsg(GeneratedMessageV3.Builder builder, IlSwitchSectionMsg ilSwitchSectionMsg) {
            this(builder);
        }

        private IlSwitchSectionMsg(GeneratedMessageV3.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
        }

        private IlSwitchSectionMsg() {
            this.memoizedIsInitialized = (byte) -1;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessageV3
        public Object newInstance(GeneratedMessageV3.UnusedPrivateParameter unused) {
            return new IlSwitchSectionMsg();
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        /* synthetic */ IlSwitchSectionMsg(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IlSwitchSectionMsg ilSwitchSectionMsg) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        private IlSwitchSectionMsg(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
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
                                case 8:
                                    this.label_ = input.readInt64();
                                    break;
                                case 18:
                                    IlInstructionMsg.Builder subBuilder = null;
                                    subBuilder = this.targetInstr_ != null ? this.targetInstr_.toBuilder() : subBuilder;
                                    this.targetInstr_ = (IlInstructionMsg) input.readMessage(IlInstructionMsg.parser(), extensionRegistry);
                                    if (subBuilder == null) {
                                        break;
                                    } else {
                                        subBuilder.mergeFrom(this.targetInstr_);
                                        this.targetInstr_ = subBuilder.buildPartial();
                                        break;
                                    }
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
            return ProtoIlInstructions.internal_static_IlSwitchSectionMsg_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessageV3
        protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
            return ProtoIlInstructions.internal_static_IlSwitchSectionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlSwitchSectionMsg.class, Builder.class);
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
        public long getLabel() {
            return this.label_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
        public boolean hasTargetInstr() {
            return this.targetInstr_ != null;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
        public IlInstructionMsg getTargetInstr() {
            return this.targetInstr_ == null ? IlInstructionMsg.getDefaultInstance() : this.targetInstr_;
        }

        @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
        public IlInstructionMsgOrBuilder getTargetInstrOrBuilder() {
            return getTargetInstr();
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
            if (this.label_ != 0) {
                output.writeInt64(1, this.label_);
            }
            if (this.targetInstr_ != null) {
                output.writeMessage(2, getTargetInstr());
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
            if (this.label_ != 0) {
                size2 = 0 + CodedOutputStream.computeInt64Size(1, this.label_);
            }
            if (this.targetInstr_ != null) {
                size2 += CodedOutputStream.computeMessageSize(2, getTargetInstr());
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
            if (!(obj instanceof IlSwitchSectionMsg)) {
                return super.equals(obj);
            }
            IlSwitchSectionMsg other = (IlSwitchSectionMsg) obj;
            if (getLabel() != other.getLabel() || hasTargetInstr() != other.hasTargetInstr()) {
                return false;
            }
            if ((hasTargetInstr() && !getTargetInstr().equals(other.getTargetInstr())) || !this.unknownFields.equals(other.unknownFields)) {
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
            int hash2 = (53 * ((37 * hash) + 1)) + Internal.hashLong(getLabel());
            if (hasTargetInstr()) {
                hash2 = (53 * ((37 * hash2) + 2)) + getTargetInstr().hashCode();
            }
            int hash3 = (29 * hash2) + this.unknownFields.hashCode();
            this.memoizedHashCode = hash3;
            return hash3;
        }

        public static IlSwitchSectionMsg parseFrom(ByteBuffer data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlSwitchSectionMsg parseFrom(ByteBuffer data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlSwitchSectionMsg parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlSwitchSectionMsg parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlSwitchSectionMsg parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static IlSwitchSectionMsg parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static IlSwitchSectionMsg parseFrom(InputStream input) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlSwitchSectionMsg parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlSwitchSectionMsg parseDelimitedFrom(InputStream input) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input);
        }

        public static IlSwitchSectionMsg parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseDelimitedWithIOException(PARSER, input, extensionRegistry);
        }

        public static IlSwitchSectionMsg parseFrom(CodedInputStream input) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input);
        }

        public static IlSwitchSectionMsg parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return (IlSwitchSectionMsg) GeneratedMessageV3.parseWithIOException(PARSER, input, extensionRegistry);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder() {
            return DEFAULT_INSTANCE.toBuilder();
        }

        public static Builder newBuilder(IlSwitchSectionMsg prototype) {
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

        /* loaded from: gencallgraphv3.jar:soot/dotnet/proto/ProtoIlInstructions$IlSwitchSectionMsg$Builder.class */
        public static final class Builder extends GeneratedMessageV3.Builder<Builder> implements IlSwitchSectionMsgOrBuilder {
            private long label_;
            private IlInstructionMsg targetInstr_;
            private SingleFieldBuilderV3<IlInstructionMsg, IlInstructionMsg.Builder, IlInstructionMsgOrBuilder> targetInstrBuilder_;

            public static final Descriptors.Descriptor getDescriptor() {
                return ProtoIlInstructions.internal_static_IlSwitchSectionMsg_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder
            protected GeneratedMessageV3.FieldAccessorTable internalGetFieldAccessorTable() {
                return ProtoIlInstructions.internal_static_IlSwitchSectionMsg_fieldAccessorTable.ensureFieldAccessorsInitialized(IlSwitchSectionMsg.class, Builder.class);
            }

            private Builder() {
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
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = IlSwitchSectionMsg.alwaysUseFieldBuilders;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.label_ = 0L;
                if (this.targetInstrBuilder_ == null) {
                    this.targetInstr_ = null;
                } else {
                    this.targetInstr_ = null;
                    this.targetInstrBuilder_ = null;
                }
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessageV3.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return ProtoIlInstructions.internal_static_IlSwitchSectionMsg_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public IlSwitchSectionMsg getDefaultInstanceForType() {
                return IlSwitchSectionMsg.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlSwitchSectionMsg build() {
                IlSwitchSectionMsg result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public IlSwitchSectionMsg buildPartial() {
                IlSwitchSectionMsg result = new IlSwitchSectionMsg(this, (IlSwitchSectionMsg) null);
                result.label_ = this.label_;
                if (this.targetInstrBuilder_ == null) {
                    result.targetInstr_ = this.targetInstr_;
                } else {
                    result.targetInstr_ = this.targetInstrBuilder_.build();
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
                if (other instanceof IlSwitchSectionMsg) {
                    return mergeFrom((IlSwitchSectionMsg) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(IlSwitchSectionMsg other) {
                if (other == IlSwitchSectionMsg.getDefaultInstance()) {
                    return this;
                }
                if (other.getLabel() != 0) {
                    setLabel(other.getLabel());
                }
                if (other.hasTargetInstr()) {
                    mergeTargetInstr(other.getTargetInstr());
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
                IlSwitchSectionMsg parsedMessage = null;
                try {
                    try {
                        parsedMessage = (IlSwitchSectionMsg) IlSwitchSectionMsg.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        IlSwitchSectionMsg ilSwitchSectionMsg = (IlSwitchSectionMsg) e.getUnfinishedMessage();
                        throw e.unwrapIOException();
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
            public long getLabel() {
                return this.label_;
            }

            public Builder setLabel(long value) {
                this.label_ = value;
                onChanged();
                return this;
            }

            public Builder clearLabel() {
                this.label_ = 0L;
                onChanged();
                return this;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
            public boolean hasTargetInstr() {
                return (this.targetInstrBuilder_ == null && this.targetInstr_ == null) ? false : true;
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
            public IlInstructionMsg getTargetInstr() {
                if (this.targetInstrBuilder_ == null) {
                    return this.targetInstr_ == null ? IlInstructionMsg.getDefaultInstance() : this.targetInstr_;
                }
                return this.targetInstrBuilder_.getMessage();
            }

            public Builder setTargetInstr(IlInstructionMsg value) {
                if (this.targetInstrBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.targetInstr_ = value;
                    onChanged();
                } else {
                    this.targetInstrBuilder_.setMessage(value);
                }
                return this;
            }

            public Builder setTargetInstr(IlInstructionMsg.Builder builderForValue) {
                if (this.targetInstrBuilder_ == null) {
                    this.targetInstr_ = builderForValue.build();
                    onChanged();
                } else {
                    this.targetInstrBuilder_.setMessage(builderForValue.build());
                }
                return this;
            }

            public Builder mergeTargetInstr(IlInstructionMsg value) {
                if (this.targetInstrBuilder_ == null) {
                    if (this.targetInstr_ != null) {
                        this.targetInstr_ = IlInstructionMsg.newBuilder(this.targetInstr_).mergeFrom(value).buildPartial();
                    } else {
                        this.targetInstr_ = value;
                    }
                    onChanged();
                } else {
                    this.targetInstrBuilder_.mergeFrom(value);
                }
                return this;
            }

            public Builder clearTargetInstr() {
                if (this.targetInstrBuilder_ == null) {
                    this.targetInstr_ = null;
                    onChanged();
                } else {
                    this.targetInstr_ = null;
                    this.targetInstrBuilder_ = null;
                }
                return this;
            }

            public IlInstructionMsg.Builder getTargetInstrBuilder() {
                onChanged();
                return getTargetInstrFieldBuilder().getBuilder();
            }

            @Override // soot.dotnet.proto.ProtoIlInstructions.IlSwitchSectionMsgOrBuilder
            public IlInstructionMsgOrBuilder getTargetInstrOrBuilder() {
                if (this.targetInstrBuilder_ != null) {
                    return this.targetInstrBuilder_.getMessageOrBuilder();
                }
                return this.targetInstr_ == null ? IlInstructionMsg.getDefaultInstance() : this.targetInstr_;
            }

            private SingleFieldBuilderV3<IlInstructionMsg, IlInstructionMsg.Builder, IlInstructionMsgOrBuilder> getTargetInstrFieldBuilder() {
                if (this.targetInstrBuilder_ == null) {
                    this.targetInstrBuilder_ = new SingleFieldBuilderV3<>(getTargetInstr(), getParentForChildren(), isClean());
                    this.targetInstr_ = null;
                }
                return this.targetInstrBuilder_;
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

        public static IlSwitchSectionMsg getDefaultInstance() {
            return DEFAULT_INSTANCE;
        }

        public static Parser<IlSwitchSectionMsg> parser() {
            return PARSER;
        }

        @Override // com.google.protobuf.GeneratedMessageV3, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<IlSwitchSectionMsg> getParserForType() {
            return PARSER;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public IlSwitchSectionMsg getDefaultInstanceForType() {
            return DEFAULT_INSTANCE;
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0014IlInstructions.proto\u001a\u0016AssemblyAllTypes.proto\"V\n\rIlFunctionMsg\u0012\"\n\u0004body\u0018\u0001 \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012!\n\tvariables\u0018\u0002 \u0003(\u000b2\u000e.IlVariableMsg\"/\n\u0013IlBlockContainerMsg\u0012\u0018\n\u0006blocks\u0018\u0001 \u0003(\u000b2\b.IlBlock\"Q\n\u0007IlBlock\u00122\n\u0017list_of_il_instructions\u0018\u0001 \u0003(\u000b2\u0011.IlInstructionMsg\u0012\u0012\n\nblock_name\u0018\u0002 \u0001(\t\"¦\u001a\n\u0010IlInstructionMsg\u0012+\n\u0007op_code\u0018\u0001 \u0001(\u000e2\u001a.IlInstructionMsg.IlOpCode\u0012!\n\u0006method\u0018\u0004 \u0001(\u000b2\u0011.MethodDefinition\u0012$\n\targuments\u0018\u0005 \u0003(\u000b2\u0011.IlInstructionMsg\u0012,\n\u0011value_instruction\u0018\u0006 \u0001(\u000b2\u0011.IlInstructionMsg\u0012\u001d\n\u0015value_constant_string\u0018\u0007 \u0001(\t\u0012\u001c\n\u0014value_constant_int32\u0018\u0010 \u0001(\u0005\u0012\u001c\n\u0014value_constant_int64\u0018& \u0001(\u0003\u0012\u001c\n\u0014value_constant_float\u0018' \u0001(\u0002\u0012\u001d\n\u0015value_constant_double\u0018( \u0001(\u0001\u0012!\n\u0006target\u0018\b \u0001(\u000b2\u0011.IlInstructionMsg\u0012\u001d\n\u0004type\u0018\t \u0001(\u000b2\u000f.TypeDefinition\u0012\u001f\n\u0005field\u0018\n \u0001(\u000b2\u0010.FieldDefinition\u0012 \n\bvariable\u0018\u000b \u0001(\u000b2\u000e.IlVariableMsg\u0012;\n\boperator\u0018\f \u0001(\u000e2).IlInstructionMsg.IlBinaryNumericOperator\u0012&\n\u0004sign\u0018\u0014 \u0001(\u000e2\u0018.IlInstructionMsg.IlSign\u0012\u001f\n\u0004left\u0018\u000e \u0001(\u000b2\u0011.IlInstructionMsg\u0012 \n\u0005right\u0018\u000f \u0001(\u000b2\u0011.IlInstructionMsg\u0012\u0014\n\ftarget_label\u0018\u0012 \u0001(\t\u0012;\n\u000fcomparison_kind\u0018\u0015 \u0001(\u000e2\".IlInstructionMsg.IlComparisonKind\u0012$\n\tcondition\u0018\u0016 \u0001(\u000b2\u0011.IlInstructionMsg\u0012$\n\ttrue_inst\u0018\u0017 \u0001(\u000b2\u0011.IlInstructionMsg\u0012%\n\nfalse_inst\u0018\u0018 \u0001(\u000b2\u0011.IlInstructionMsg\u0012 \n\u0005array\u0018\u001e \u0001(\u000b2\u0011.IlInstructionMsg\u0012;\n\u000fconversion_kind\u0018\u0019 \u0001(\u000e2\".IlInstructionMsg.IlConversionKind\u00121\n\ninput_type\u0018\u001a \u0001(\u000e2\u001d.IlInstructionMsg.IlStackType\u00126\n\u000btarget_type\u0018\u001c \u0001(\u000e2!.IlInstructionMsg.IlPrimitiveType\u0012#\n\bargument\u0018\u001d \u0001(\u000b2\u0011.IlInstructionMsg\u00122\n\u000bresult_type\u0018\u001b \u0001(\u000e2\u001d.IlInstructionMsg.IlStackType\u0012\"\n\u0007indices\u0018\u001f \u0003(\u000b2\u0011.IlInstructionMsg\u0012'\n\ttry_block\u0018  \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012'\n\bhandlers\u0018! \u0003(\u000b2\u0015.IlTryCatchHandlerMsg\u0012+\n\rfinally_block\u0018\" \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012)\n\u000bfault_block\u0018# \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012\"\n\u0004body\u0018% \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012$\n\tkey_instr\u0018) \u0001(\u000b2\u0011.IlInstructionMsg\u0012'\n\fdefault_inst\u0018* \u0001(\u000b2\u0011.IlInstructionMsg\u0012,\n\u000fswitch_sections\u0018+ \u0003(\u000b2\u0013.IlSwitchSectionMsg\"Ë\u0007\n\bIlOpCode\u0012\u000b\n\u0007NONE_OP\u0010��\u0012\u0007\n\u0003NOP\u0010\u0001\u0012\b\n\u0004CALL\u0010\u0004\u0012\t\n\u0005LEAVE\u0010\u0005\u0012\t\n\u0005LDSTR\u0010\u0006\u0012\t\n\u0005STOBJ\u0010\u0007\u0012\n\n\u0006LDFLDA\u0010\b\u0012\n\n\u0006LDC_I4\u0010\t\u0012\t\n\u0005LDLOC\u0010\n\u0012\t\n\u0005LDOBJ\u0010\u000b\u0012\t\n\u0005STLOC\u0010\f\u0012\n\n\u0006NEWOBJ\u0010\r\u0012\f\n\bCALLVIRT\u0010\u000e\u0012\u001e\n\u001aBINARY_NUMERIC_INSTRUCTION\u0010\u000f\u0012\n\n\u0006BRANCH\u0010\u0010\u0012\b\n\u0004COMP\u0010\u0011\u0012\u0012\n\u000eIF_INSTRUCTION\u0010\u0012\u0012\u000b\n\u0007LDSFLDA\u0010\u0013\u0012\n\n\u0006LDNULL\u0010\u0014\u0012\t\n\u0005LDLEN\u0010\u0015\u0012\b\n\u0004CONV\u0010\u0016\u0012\n\n\u0006NEWARR\u0010\u0017\u0012\u000b\n\u0007LDELEMA\u0010\u0018\u0012\r\n\tCASTCLASS\u0010\u0019\u0012\n\n\u0006ISINST\u0010\u001a\u0012\u0007\n\u0003BOX\u0010\u001b\u0012\f\n\bUNBOXANY\u0010\u001c\u0012\t\n\u0005UNBOX\u0010\u001d\u0012\r\n\tTRY_CATCH\u0010\u001e\u0012\n\n\u0006LDLOCA\u0010\u001f\u0012\u0011\n\rDEFAULT_VALUE\u0010 \u0012\u0007\n\u0003NOT\u0010!\u0012\u000f\n\u000bTRY_FINALLY\u0010\"\u0012\r\n\tTRY_FAULT\u0010#\u0012\u0013\n\u000fBLOCK_CONTAINER\u0010$\u0012\t\n\u0005BLOCK\u0010%\u0012\u0015\n\u0011TRY_CATCH_HANDLER\u0010&\u0012\u000b\n\u0007RETHROW\u0010'\u0012\t\n\u0005THROW\u0010(\u0012\u000f\n\u000bDEBUG_BREAK\u0010)\u0012\r\n\tCK_FINITE\u0010*\u0012\n\n\u0006CP_BLK\u0010,\u0012\n\n\u0006CP_OBJ\u0010-\u0012\u0007\n\u0003DUP\u0010.\u0012\f\n\bINIT_BLK\u0010/\u0012\f\n\bINIT_OBJ\u00100\u0012\n\n\u0006LDC_I8\u00101\u0012\n\n\u0006LDC_R4\u00102\u0012\n\n\u0006LDC_R8\u00103\u0012\n\n\u0006LD_FLD\u00104\u0012\n\n\u0006LD_FTN\u00105\u0012\u000b\n\u0007LD_SFLD\u00106\u0012\f\n\bLD_TOKEN\u00107\u0012\u000f\n\u000bLD_VIRT_FTN\u00108\u0012\r\n\tLOC_ALLOC\u00109\u0012\u000e\n\nMK_REF_ANY\u0010:\u0012\u0006\n\u0002NO\u0010;\u0012\f\n\bREADONLY\u0010<\u0012\u0010\n\fREF_ANY_TYPE\u0010=\u0012\u000f\n\u000bREF_ANY_VAL\u0010>\u0012\u000b\n\u0007SIZE_OF\u0010?\u0012\u000b\n\u0007ST_SFLD\u0010@\u0012\n\n\u0006SWITCH\u0010A\u0012\b\n\u0004TAIL\u0010B\u0012\r\n\tUNALIGNED\u0010C\u0012\f\n\bVOLATILE\u0010D\u0012\u0013\n\u000fLD_MEMBER_TOKEN\u0010E\u0012\u0011\n\rLD_TYPE_TOKEN\u0010F\u0012\u0012\n\u000eINVALID_BRANCH\u0010G\u0012\u0011\n\rCALL_INDIRECT\u0010H\"\u0099\u0001\n\u0017IlBinaryNumericOperator\u0012\u000f\n\u000bNONE_BINARY\u0010��\u0012\u0007\n\u0003Add\u0010\u0001\u0012\u0007\n\u0003Sub\u0010\u0002\u0012\u0007\n\u0003Mul\u0010\u0003\u0012\u0007\n\u0003Div\u0010\u0004\u0012\u0007\n\u0003Rem\u0010\u0005\u0012\n\n\u0006BitAnd\u0010\u0006\u0012\t\n\u0005BitOr\u0010\u0007\u0012\n\n\u0006BitXor\u0010\b\u0012\r\n\tShiftLeft\u0010\t\u0012\u000e\n\nShiftRight\u0010\n\"1\n\u0006IlSign\u0012\r\n\tNONE_SIGN\u0010��\u0012\n\n\u0006Signed\u0010\u0001\u0012\f\n\bUnsigned\u0010\u0002\"\u008b\u0001\n\u0010IlComparisonKind\u0012\r\n\tNONE_KIND\u0010��\u0012\f\n\bEquality\u0010\u0001\u0012\u000e\n\nInequality\u0010\u0002\u0012\f\n\bLessThan\u0010\u0003\u0012\u0013\n\u000fLessThanOrEqual\u0010\u0004\u0012\u000f\n\u000bGreaterThan\u0010\u0005\u0012\u0016\n\u0012GreaterThanOrEqual\u0010\u0006\"â\u0001\n\u0010IlConversionKind\u0012\u0013\n\u000fNONE_CONVERSION\u0010��\u0012\u000b\n\u0007Invalid\u0010\u0001\u0012\u0007\n\u0003Nop\u0010\u0002\u0012\u000e\n\nIntToFloat\u0010\u0003\u0012\u000e\n\nFloatToInt\u0010\u0004\u0012\u0018\n\u0014FloatPrecisionChange\u0010\u0005\u0012\u000e\n\nSignExtend\u0010\u0006\u0012\u000e\n\nZeroExtend\u0010\u0007\u0012\f\n\bTruncate\u0010\b\u0012\u0012\n\u000eStopGCTracking\u0010\t\u0012\u0013\n\u000fStartGCTracking\u0010\n\u0012\u0012\n\u000eObjectInterior\u0010\u000b\"\u008e\u0001\n\u000bIlStackType\u0012\u0013\n\u000fNONE_STACK_TYPE\u0010��\u0012\u0011\n\rUnknown_STACK\u0010\u0001\u0012\f\n\bI4_STACK\u0010\u0002\u0012\u000b\n\u0007I_STACK\u0010\u0003\u0012\f\n\bI8_STACK\u0010\u0004\u0012\u0006\n\u0002F4\u0010\u0005\u0012\u0006\n\u0002F8\u0010\u0006\u0012\u0005\n\u0001O\u0010\u0007\u0012\r\n\tRef_STACK\u0010\b\u0012\b\n\u0004Void\u0010\t\"¯\u0001\n\u000fIlPrimitiveType\u0012\u0017\n\u0013NONE_PRIMITIVE_TYPE\u0010��\u0012\b\n\u0004None\u0010\u0001\u0012\u0006\n\u0002I1\u0010\u0002\u0012\u0006\n\u0002I2\u0010\u0003\u0012\u0006\n\u0002I4\u0010\u0004\u0012\u0006\n\u0002I8\u0010\u0005\u0012\u0006\n\u0002R4\u0010\u0006\u0012\u0006\n\u0002R8\u0010\u0007\u0012\u0006\n\u0002U1\u0010\b\u0012\u0006\n\u0002U2\u0010\t\u0012\u0006\n\u0002U4\u0010\n\u0012\u0006\n\u0002U8\u0010\u000b\u0012\u0005\n\u0001I\u0010\f\u0012\u0005\n\u0001U\u0010\r\u0012\u0007\n\u0003Ref\u0010\u000e\u0012\u0005\n\u0001R\u0010\u000f\u0012\u000b\n\u0007Unknown\u0010\u0010\"Ì\u0003\n\rIlVariableMsg\u0012\u001d\n\u0004type\u0018\u0001 \u0001(\u000b2\u000f.TypeDefinition\u0012\f\n\u0004name\u0018\u0002 \u0001(\t\u0012\u0019\n\u0011has_initial_value\u0018\u0003 \u0001(\b\u00124\n\rvariable_kind\u0018\u0004 \u0001(\u000e2\u001d.IlVariableMsg.IlVariableKind\"¼\u0002\n\u000eIlVariableKind\u0012\b\n\u0004NONE\u0010��\u0012\t\n\u0005LOCAL\u0010\u0001\u0012\u0010\n\fPINNED_LOCAL\u0010\u0002\u0012\u0017\n\u0013PINNED_REGION_LOCAL\u0010\u0003\u0012\u000f\n\u000bUSING_LOCAL\u0010\u0004\u0012\u0011\n\rFOREACH_LOCAL\u0010\u0005\u0012\u0016\n\u0012INITIALIZER_TARGET\u0010\u0006\u0012\r\n\tPARAMETER\u0010\u0007\u0012\u0018\n\u0014EXCEPTION_STACK_SLOT\u0010\b\u0012\u0013\n\u000fEXCEPTION_LOCAL\u0010\t\u0012\r\n\tSTACKSLOT\u0010\n\u0012\u0012\n\u000eNAMED_ARGUMENT\u0010\u000b\u0012\u0017\n\u0013DISPLAY_CLASS_LOCAL\u0010\f\u0012\u0011\n\rPATTERN_LOCAL\u0010\r\u0012!\n\u001dDECONSTRUCTION_INIT_TEMPORARY\u0010\u000e\"\u0096\u0001\n\u0014IlTryCatchHandlerMsg\u0012\"\n\u0004body\u0018\u0001 \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012 \n\bvariable\u0018\u0002 \u0001(\u000b2\u000e.IlVariableMsg\u0012$\n\u0006filter\u0018\u0003 \u0001(\u000b2\u0014.IlBlockContainerMsg\u0012\u0012\n\nhas_filter\u0018\u0004 \u0001(\b\"L\n\u0012IlSwitchSectionMsg\u0012\r\n\u0005label\u0018\u0001 \u0001(\u0003\u0012'\n\ftarget_instr\u0018\u0002 \u0001(\u000b2\u0011.IlInstructionMsgBQ\n\u0011soot.dotnet.protoB\u0013ProtoIlInstructionsª\u0002&Soot.Dotnet.Decompiler.Models.Protobufb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[]{ProtoAssemblyAllTypes.getDescriptor()});
        ProtoAssemblyAllTypes.getDescriptor();
    }
}
