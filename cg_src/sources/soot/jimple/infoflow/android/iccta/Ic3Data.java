package soot.jimple.infoflow.android.iccta;

import com.google.protobuf.AbstractParser;
import com.google.protobuf.ByteString;
import com.google.protobuf.CodedInputStream;
import com.google.protobuf.CodedOutputStream;
import com.google.protobuf.Descriptors;
import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.ExtensionRegistryLite;
import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.Internal;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.LazyStringArrayList;
import com.google.protobuf.LazyStringList;
import com.google.protobuf.Message;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.Parser;
import com.google.protobuf.ProtocolMessageEnum;
import com.google.protobuf.RepeatedFieldBuilder;
import com.google.protobuf.SingleFieldBuilder;
import com.google.protobuf.UnknownFieldSet;
import com.google.protobuf.UnmodifiableLazyStringList;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectStreamException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data.class */
public final class Ic3Data {
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Attribute_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Permission_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_fieldAccessorTable;
    private static Descriptors.Descriptor internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor;
    private static GeneratedMessage.FieldAccessorTable internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_fieldAccessorTable;
    private static Descriptors.FileDescriptor descriptor;

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$ApplicationOrBuilder.class */
    public interface ApplicationOrBuilder extends MessageOrBuilder {
        boolean hasName();

        String getName();

        ByteString getNameBytes();

        boolean hasVersion();

        int getVersion();

        List<Application.Permission> getPermissionsList();

        Application.Permission getPermissions(int i);

        int getPermissionsCount();

        List<? extends Application.PermissionOrBuilder> getPermissionsOrBuilderList();

        Application.PermissionOrBuilder getPermissionsOrBuilder(int i);

        List<String> getUsedPermissionsList();

        int getUsedPermissionsCount();

        String getUsedPermissions(int i);

        ByteString getUsedPermissionsBytes(int i);

        List<Application.Component> getComponentsList();

        Application.Component getComponents(int i);

        int getComponentsCount();

        List<? extends Application.ComponentOrBuilder> getComponentsOrBuilderList();

        Application.ComponentOrBuilder getComponentsOrBuilder(int i);

        boolean hasAnalysisStart();

        long getAnalysisStart();

        boolean hasAnalysisEnd();

        long getAnalysisEnd();

        boolean hasSample();

        String getSample();

        ByteString getSampleBytes();
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$AttributeOrBuilder.class */
    public interface AttributeOrBuilder extends MessageOrBuilder {
        boolean hasKind();

        AttributeKind getKind();

        List<String> getValueList();

        int getValueCount();

        String getValue(int i);

        ByteString getValueBytes(int i);

        List<Integer> getIntValueList();

        int getIntValueCount();

        int getIntValue(int i);
    }

    private Ic3Data() {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$AttributeKind.class */
    public enum AttributeKind implements ProtocolMessageEnum {
        ACTION(0, 0),
        CATEGORY(1, 1),
        PACKAGE(2, 2),
        CLASS(3, 3),
        TYPE(4, 4),
        URI(5, 5),
        SCHEME(6, 6),
        EXTRA(7, 7),
        AUTHORITY(8, 8),
        HOST(9, 9),
        PATH(10, 10),
        PORT(11, 11),
        SSP(12, 12),
        QUERY(13, 13),
        FLAG(14, 14),
        PRIORITY(15, 15);
        
        public static final int ACTION_VALUE = 0;
        public static final int CATEGORY_VALUE = 1;
        public static final int PACKAGE_VALUE = 2;
        public static final int CLASS_VALUE = 3;
        public static final int TYPE_VALUE = 4;
        public static final int URI_VALUE = 5;
        public static final int SCHEME_VALUE = 6;
        public static final int EXTRA_VALUE = 7;
        public static final int AUTHORITY_VALUE = 8;
        public static final int HOST_VALUE = 9;
        public static final int PATH_VALUE = 10;
        public static final int PORT_VALUE = 11;
        public static final int SSP_VALUE = 12;
        public static final int QUERY_VALUE = 13;
        public static final int FLAG_VALUE = 14;
        public static final int PRIORITY_VALUE = 15;
        private static Internal.EnumLiteMap<AttributeKind> internalValueMap = new Internal.EnumLiteMap<AttributeKind>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.AttributeKind.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.google.protobuf.Internal.EnumLiteMap
            public AttributeKind findValueByNumber(int number) {
                return AttributeKind.valueOf(number);
            }
        };
        private static final AttributeKind[] VALUES = valuesCustom();
        private final int index;
        private final int value;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static AttributeKind[] valuesCustom() {
            AttributeKind[] valuesCustom = values();
            int length = valuesCustom.length;
            AttributeKind[] attributeKindArr = new AttributeKind[length];
            System.arraycopy(valuesCustom, 0, attributeKindArr, 0, length);
            return attributeKindArr;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
        public final int getNumber() {
            return this.value;
        }

        public static AttributeKind valueOf(int value) {
            switch (value) {
                case 0:
                    return ACTION;
                case 1:
                    return CATEGORY;
                case 2:
                    return PACKAGE;
                case 3:
                    return CLASS;
                case 4:
                    return TYPE;
                case 5:
                    return URI;
                case 6:
                    return SCHEME;
                case 7:
                    return EXTRA;
                case 8:
                    return AUTHORITY;
                case 9:
                    return HOST;
                case 10:
                    return PATH;
                case 11:
                    return PORT;
                case 12:
                    return SSP;
                case 13:
                    return QUERY;
                case 14:
                    return FLAG;
                case 15:
                    return PRIORITY;
                default:
                    return null;
            }
        }

        public static Internal.EnumLiteMap<AttributeKind> internalGetValueMap() {
            return internalValueMap;
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumValueDescriptor getValueDescriptor() {
            return getDescriptor().getValues().get(this.index);
        }

        @Override // com.google.protobuf.ProtocolMessageEnum
        public final Descriptors.EnumDescriptor getDescriptorForType() {
            return getDescriptor();
        }

        public static final Descriptors.EnumDescriptor getDescriptor() {
            return Ic3Data.getDescriptor().getEnumTypes().get(0);
        }

        public static AttributeKind valueOf(Descriptors.EnumValueDescriptor desc) {
            if (desc.getType() != getDescriptor()) {
                throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
            }
            return VALUES[desc.getIndex()];
        }

        AttributeKind(int index, int value) {
            this.index = index;
            this.value = value;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Attribute.class */
    public static final class Attribute extends GeneratedMessage implements AttributeOrBuilder {
        private final UnknownFieldSet unknownFields;
        private int bitField0_;
        public static final int KIND_FIELD_NUMBER = 1;
        private AttributeKind kind_;
        public static final int VALUE_FIELD_NUMBER = 2;
        private LazyStringList value_;
        public static final int INT_VALUE_FIELD_NUMBER = 3;
        private List<Integer> intValue_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private static final long serialVersionUID = 0;
        public static Parser<Attribute> PARSER = new AbstractParser<Attribute>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Attribute.1
            @Override // com.google.protobuf.Parser
            public Attribute parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Attribute(input, extensionRegistry, null);
            }
        };
        private static final Attribute defaultInstance = new Attribute(true);

        /* synthetic */ Attribute(GeneratedMessage.Builder builder, Attribute attribute) {
            this(builder);
        }

        private Attribute(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Attribute(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static Attribute getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Attribute getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Attribute(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
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
                                AttributeKind value = AttributeKind.valueOf(rawValue);
                                if (value == null) {
                                    unknownFields.mergeVarintField(1, rawValue);
                                    break;
                                } else {
                                    this.bitField0_ |= 1;
                                    this.kind_ = value;
                                    break;
                                }
                            case 18:
                                if ((mutable_bitField0_ & 2) != 2) {
                                    this.value_ = new LazyStringArrayList();
                                    mutable_bitField0_ |= 2;
                                }
                                this.value_.add(input.readBytes());
                                break;
                            case 24:
                                if ((mutable_bitField0_ & 4) != 4) {
                                    this.intValue_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                this.intValue_.add(Integer.valueOf(input.readInt32()));
                                break;
                            case 26:
                                int length = input.readRawVarint32();
                                int limit = input.pushLimit(length);
                                if ((mutable_bitField0_ & 4) != 4 && input.getBytesUntilLimit() > 0) {
                                    this.intValue_ = new ArrayList();
                                    mutable_bitField0_ |= 4;
                                }
                                while (input.getBytesUntilLimit() > 0) {
                                    this.intValue_.add(Integer.valueOf(input.readInt32()));
                                }
                                input.popLimit(limit);
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
                        throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                    }
                }
                if ((mutable_bitField0_ & 2) == 2) {
                    this.value_ = new UnmodifiableLazyStringList(this.value_);
                }
                if ((mutable_bitField0_ & 4) == 4) {
                    this.intValue_ = Collections.unmodifiableList(this.intValue_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            } catch (Throwable th) {
                if ((mutable_bitField0_ & 2) == 2) {
                    this.value_ = new UnmodifiableLazyStringList(this.value_);
                }
                if ((mutable_bitField0_ & 4) == 4) {
                    this.intValue_ = Collections.unmodifiableList(this.intValue_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
                throw th;
            }
        }

        /* synthetic */ Attribute(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Attribute attribute) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessage
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_fieldAccessorTable.ensureFieldAccessorsInitialized(Attribute.class, Builder.class);
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Attribute> getParserForType() {
            return PARSER;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public boolean hasKind() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public AttributeKind getKind() {
            return this.kind_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public List<String> getValueList() {
            return this.value_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public int getValueCount() {
            return this.value_.size();
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public String getValue(int index) {
            return (String) this.value_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public ByteString getValueBytes(int index) {
            return this.value_.getByteString(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public List<Integer> getIntValueList() {
            return this.intValue_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public int getIntValueCount() {
            return this.intValue_.size();
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
        public int getIntValue(int index) {
            return this.intValue_.get(index).intValue();
        }

        private void initFields() {
            this.kind_ = AttributeKind.ACTION;
            this.value_ = LazyStringArrayList.EMPTY;
            this.intValue_ = Collections.emptyList();
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeEnum(1, this.kind_.getNumber());
            }
            for (int i = 0; i < this.value_.size(); i++) {
                output.writeBytes(2, this.value_.getByteString(i));
            }
            for (int i2 = 0; i2 < this.intValue_.size(); i2++) {
                output.writeInt32(3, this.intValue_.get(i2).intValue());
            }
            getUnknownFields().writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                size2 = 0 + CodedOutputStream.computeEnumSize(1, this.kind_.getNumber());
            }
            int dataSize = 0;
            for (int i = 0; i < this.value_.size(); i++) {
                dataSize += CodedOutputStream.computeBytesSizeNoTag(this.value_.getByteString(i));
            }
            int size3 = size2 + dataSize + (1 * getValueList().size());
            int dataSize2 = 0;
            for (int i2 = 0; i2 < this.intValue_.size(); i2++) {
                dataSize2 += CodedOutputStream.computeInt32SizeNoTag(this.intValue_.get(i2).intValue());
            }
            int size4 = size3 + dataSize2 + (1 * getIntValueList().size()) + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        @Override // com.google.protobuf.GeneratedMessage
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static Attribute parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Attribute parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Attribute parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Attribute parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Attribute parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Attribute parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Attribute parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Attribute parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Attribute parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Attribute parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Attribute prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return newBuilder(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessage
        public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Attribute$Builder.class */
        public static final class Builder extends GeneratedMessage.Builder<Builder> implements AttributeOrBuilder {
            private int bitField0_;
            private AttributeKind kind_;
            private LazyStringList value_;
            private List<Integer> intValue_;

            public static final Descriptors.Descriptor getDescriptor() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_fieldAccessorTable.ensureFieldAccessorsInitialized(Attribute.class, Builder.class);
            }

            private Builder() {
                this.kind_ = AttributeKind.ACTION;
                this.value_ = LazyStringArrayList.EMPTY;
                this.intValue_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.kind_ = AttributeKind.ACTION;
                this.value_ = LazyStringArrayList.EMPTY;
                this.intValue_ = Collections.emptyList();
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                boolean unused = Attribute.alwaysUseFieldBuilders;
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.kind_ = AttributeKind.ACTION;
                this.bitField0_ &= -2;
                this.value_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -3;
                this.intValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Attribute getDefaultInstanceForType() {
                return Attribute.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Attribute build() {
                Attribute result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Attribute buildPartial() {
                Attribute result = new Attribute(this, (Attribute) null);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.kind_ = this.kind_;
                if ((this.bitField0_ & 2) == 2) {
                    this.value_ = new UnmodifiableLazyStringList(this.value_);
                    this.bitField0_ &= -3;
                }
                result.value_ = this.value_;
                if ((this.bitField0_ & 4) == 4) {
                    this.intValue_ = Collections.unmodifiableList(this.intValue_);
                    this.bitField0_ &= -5;
                }
                result.intValue_ = this.intValue_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof Attribute) {
                    return mergeFrom((Attribute) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(Attribute other) {
                if (other == Attribute.getDefaultInstance()) {
                    return this;
                }
                if (other.hasKind()) {
                    setKind(other.getKind());
                }
                if (!other.value_.isEmpty()) {
                    if (this.value_.isEmpty()) {
                        this.value_ = other.value_;
                        this.bitField0_ &= -3;
                    } else {
                        ensureValueIsMutable();
                        this.value_.addAll(other.value_);
                    }
                    onChanged();
                }
                if (!other.intValue_.isEmpty()) {
                    if (this.intValue_.isEmpty()) {
                        this.intValue_ = other.intValue_;
                        this.bitField0_ &= -5;
                    } else {
                        ensureIntValueIsMutable();
                        this.intValue_.addAll(other.intValue_);
                    }
                    onChanged();
                }
                mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Attribute parsedMessage = null;
                try {
                    try {
                        parsedMessage = Attribute.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        Attribute attribute = (Attribute) e.getUnfinishedMessage();
                        throw e;
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public boolean hasKind() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public AttributeKind getKind() {
                return this.kind_;
            }

            public Builder setKind(AttributeKind value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.kind_ = value;
                onChanged();
                return this;
            }

            public Builder clearKind() {
                this.bitField0_ &= -2;
                this.kind_ = AttributeKind.ACTION;
                onChanged();
                return this;
            }

            private void ensureValueIsMutable() {
                if ((this.bitField0_ & 2) != 2) {
                    this.value_ = new LazyStringArrayList(this.value_);
                    this.bitField0_ |= 2;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public List<String> getValueList() {
                return Collections.unmodifiableList(this.value_);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public int getValueCount() {
                return this.value_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public String getValue(int index) {
                return (String) this.value_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public ByteString getValueBytes(int index) {
                return this.value_.getByteString(index);
            }

            public Builder setValue(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.set(index, value);
                onChanged();
                return this;
            }

            public Builder addValue(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(value);
                onChanged();
                return this;
            }

            public Builder addAllValue(Iterable<String> values) {
                ensureValueIsMutable();
                GeneratedMessage.Builder.addAll((Iterable) values, (List) this.value_);
                onChanged();
                return this;
            }

            public Builder clearValue() {
                this.value_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -3;
                onChanged();
                return this;
            }

            public Builder addValueBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureValueIsMutable();
                this.value_.add(value);
                onChanged();
                return this;
            }

            private void ensureIntValueIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.intValue_ = new ArrayList(this.intValue_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public List<Integer> getIntValueList() {
                return Collections.unmodifiableList(this.intValue_);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public int getIntValueCount() {
                return this.intValue_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.AttributeOrBuilder
            public int getIntValue(int index) {
                return this.intValue_.get(index).intValue();
            }

            public Builder setIntValue(int index, int value) {
                ensureIntValueIsMutable();
                this.intValue_.set(index, Integer.valueOf(value));
                onChanged();
                return this;
            }

            public Builder addIntValue(int value) {
                ensureIntValueIsMutable();
                this.intValue_.add(Integer.valueOf(value));
                onChanged();
                return this;
            }

            public Builder addAllIntValue(Iterable<? extends Integer> values) {
                ensureIntValueIsMutable();
                GeneratedMessage.Builder.addAll((Iterable) values, (List) this.intValue_);
                onChanged();
                return this;
            }

            public Builder clearIntValue() {
                this.intValue_ = Collections.emptyList();
                this.bitField0_ &= -5;
                onChanged();
                return this;
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application.class */
    public static final class Application extends GeneratedMessage implements ApplicationOrBuilder {
        private final UnknownFieldSet unknownFields;
        private int bitField0_;
        public static final int NAME_FIELD_NUMBER = 1;
        private Object name_;
        public static final int VERSION_FIELD_NUMBER = 2;
        private int version_;
        public static final int PERMISSIONS_FIELD_NUMBER = 3;
        private List<Permission> permissions_;
        public static final int USED_PERMISSIONS_FIELD_NUMBER = 4;
        private LazyStringList usedPermissions_;
        public static final int COMPONENTS_FIELD_NUMBER = 5;
        private List<Component> components_;
        public static final int ANALYSIS_START_FIELD_NUMBER = 6;
        private long analysisStart_;
        public static final int ANALYSIS_END_FIELD_NUMBER = 7;
        private long analysisEnd_;
        public static final int SAMPLE_FIELD_NUMBER = 8;
        private Object sample_;
        private byte memoizedIsInitialized;
        private int memoizedSerializedSize;
        private static final long serialVersionUID = 0;
        public static Parser<Application> PARSER = new AbstractParser<Application>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.1
            @Override // com.google.protobuf.Parser
            public Application parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return new Application(input, extensionRegistry, null);
            }
        };
        private static final Application defaultInstance = new Application(true);

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$ComponentOrBuilder.class */
        public interface ComponentOrBuilder extends MessageOrBuilder {
            boolean hasName();

            String getName();

            ByteString getNameBytes();

            boolean hasKind();

            Component.ComponentKind getKind();

            boolean hasExported();

            boolean getExported();

            boolean hasPermission();

            String getPermission();

            ByteString getPermissionBytes();

            boolean hasMissing();

            int getMissing();

            List<Component.Extra> getExtrasList();

            Component.Extra getExtras(int i);

            int getExtrasCount();

            List<? extends Component.ExtraOrBuilder> getExtrasOrBuilderList();

            Component.ExtraOrBuilder getExtrasOrBuilder(int i);

            boolean hasAliasTarget();

            String getAliasTarget();

            ByteString getAliasTargetBytes();

            boolean hasGrantUriPermissions();

            boolean getGrantUriPermissions();

            boolean hasReadPermission();

            String getReadPermission();

            ByteString getReadPermissionBytes();

            boolean hasWritePermission();

            String getWritePermission();

            ByteString getWritePermissionBytes();

            List<String> getAuthoritiesList();

            int getAuthoritiesCount();

            String getAuthorities(int i);

            ByteString getAuthoritiesBytes(int i);

            List<Component.IntentFilter> getIntentFiltersList();

            Component.IntentFilter getIntentFilters(int i);

            int getIntentFiltersCount();

            List<? extends Component.IntentFilterOrBuilder> getIntentFiltersOrBuilderList();

            Component.IntentFilterOrBuilder getIntentFiltersOrBuilder(int i);

            List<Component.ExitPoint> getExitPointsList();

            Component.ExitPoint getExitPoints(int i);

            int getExitPointsCount();

            List<? extends Component.ExitPointOrBuilder> getExitPointsOrBuilderList();

            Component.ExitPointOrBuilder getExitPointsOrBuilder(int i);

            boolean hasRegistrationInstruction();

            Component.Instruction getRegistrationInstruction();

            Component.InstructionOrBuilder getRegistrationInstructionOrBuilder();
        }

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$PermissionOrBuilder.class */
        public interface PermissionOrBuilder extends MessageOrBuilder {
            boolean hasName();

            String getName();

            ByteString getNameBytes();

            boolean hasLevel();

            Permission.Level getLevel();
        }

        /* synthetic */ Application(GeneratedMessage.Builder builder, Application application) {
            this(builder);
        }

        private Application(GeneratedMessage.Builder<?> builder) {
            super(builder);
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = builder.getUnknownFields();
        }

        private Application(boolean noInit) {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            this.unknownFields = UnknownFieldSet.getDefaultInstance();
        }

        public static Application getDefaultInstance() {
            return defaultInstance;
        }

        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
        public Application getDefaultInstanceForType() {
            return defaultInstance;
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
        public final UnknownFieldSet getUnknownFields() {
            return this.unknownFields;
        }

        private Application(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            this.memoizedIsInitialized = (byte) -1;
            this.memoizedSerializedSize = -1;
            initFields();
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
                                    this.bitField0_ |= 1;
                                    this.name_ = input.readBytes();
                                    break;
                                case 16:
                                    this.bitField0_ |= 2;
                                    this.version_ = input.readUInt32();
                                    break;
                                case 26:
                                    if ((mutable_bitField0_ & 4) != 4) {
                                        this.permissions_ = new ArrayList();
                                        mutable_bitField0_ |= 4;
                                    }
                                    this.permissions_.add((Permission) input.readMessage(Permission.PARSER, extensionRegistry));
                                    break;
                                case 34:
                                    if ((mutable_bitField0_ & 8) != 8) {
                                        this.usedPermissions_ = new LazyStringArrayList();
                                        mutable_bitField0_ |= 8;
                                    }
                                    this.usedPermissions_.add(input.readBytes());
                                    break;
                                case 42:
                                    if ((mutable_bitField0_ & 16) != 16) {
                                        this.components_ = new ArrayList();
                                        mutable_bitField0_ |= 16;
                                    }
                                    this.components_.add((Component) input.readMessage(Component.PARSER, extensionRegistry));
                                    break;
                                case 48:
                                    this.bitField0_ |= 4;
                                    this.analysisStart_ = input.readInt64();
                                    break;
                                case 56:
                                    this.bitField0_ |= 8;
                                    this.analysisEnd_ = input.readInt64();
                                    break;
                                case 66:
                                    this.bitField0_ |= 16;
                                    this.sample_ = input.readBytes();
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
                            throw new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
                        }
                    } catch (InvalidProtocolBufferException e2) {
                        throw e2.setUnfinishedMessage(this);
                    }
                }
                if ((mutable_bitField0_ & 4) == 4) {
                    this.permissions_ = Collections.unmodifiableList(this.permissions_);
                }
                if ((mutable_bitField0_ & 8) == 8) {
                    this.usedPermissions_ = new UnmodifiableLazyStringList(this.usedPermissions_);
                }
                if ((mutable_bitField0_ & 16) == 16) {
                    this.components_ = Collections.unmodifiableList(this.components_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
            } catch (Throwable th) {
                if ((mutable_bitField0_ & 4) == 4) {
                    this.permissions_ = Collections.unmodifiableList(this.permissions_);
                }
                if ((mutable_bitField0_ & 8) == 8) {
                    this.usedPermissions_ = new UnmodifiableLazyStringList(this.usedPermissions_);
                }
                if ((mutable_bitField0_ & 16) == 16) {
                    this.components_ = Collections.unmodifiableList(this.components_);
                }
                this.unknownFields = unknownFields.build();
                makeExtensionsImmutable();
                throw th;
            }
        }

        /* synthetic */ Application(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Application application) throws InvalidProtocolBufferException {
            this(codedInputStream, extensionRegistryLite);
        }

        public static final Descriptors.Descriptor getDescriptor() {
            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor;
        }

        @Override // com.google.protobuf.GeneratedMessage
        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_fieldAccessorTable.ensureFieldAccessorsInitialized(Application.class, Builder.class);
        }

        static {
            defaultInstance.initFields();
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Parser<Application> getParserForType() {
            return PARSER;
        }

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Permission.class */
        public static final class Permission extends GeneratedMessage implements PermissionOrBuilder {
            private final UnknownFieldSet unknownFields;
            private int bitField0_;
            public static final int NAME_FIELD_NUMBER = 1;
            private Object name_;
            public static final int LEVEL_FIELD_NUMBER = 2;
            private Level level_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private static final long serialVersionUID = 0;
            public static Parser<Permission> PARSER = new AbstractParser<Permission>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Permission.1
                @Override // com.google.protobuf.Parser
                public Permission parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return new Permission(input, extensionRegistry, null);
                }
            };
            private static final Permission defaultInstance = new Permission(true);

            /* synthetic */ Permission(GeneratedMessage.Builder builder, Permission permission) {
                this(builder);
            }

            private Permission(GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = builder.getUnknownFields();
            }

            private Permission(boolean noInit) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = UnknownFieldSet.getDefaultInstance();
            }

            public static Permission getDefaultInstance() {
                return defaultInstance;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Permission getDefaultInstanceForType() {
                return defaultInstance;
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
            public final UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            private Permission(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                initFields();
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
                                    this.bitField0_ |= 1;
                                    this.name_ = input.readBytes();
                                    break;
                                case 16:
                                    int rawValue = input.readEnum();
                                    Level value = Level.valueOf(rawValue);
                                    if (value == null) {
                                        unknownFields.mergeVarintField(2, rawValue);
                                        break;
                                    } else {
                                        this.bitField0_ |= 2;
                                        this.level_ = value;
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
                        } catch (InvalidProtocolBufferException e) {
                            throw e.setUnfinishedMessage(this);
                        } catch (IOException e2) {
                            throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                        }
                    }
                } finally {
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                }
            }

            /* synthetic */ Permission(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Permission permission) throws InvalidProtocolBufferException {
                this(codedInputStream, extensionRegistryLite);
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessage
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_fieldAccessorTable.ensureFieldAccessorsInitialized(Permission.class, Builder.class);
            }

            static {
                defaultInstance.initFields();
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Parser<Permission> getParserForType() {
                return PARSER;
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Permission$Level.class */
            public enum Level implements ProtocolMessageEnum {
                NORMAL(0, 0),
                DANGEROUS(1, 1),
                SIGNATURE(2, 2),
                SIGNATURE_OR_SYSTEM(3, 3);
                
                public static final int NORMAL_VALUE = 0;
                public static final int DANGEROUS_VALUE = 1;
                public static final int SIGNATURE_VALUE = 2;
                public static final int SIGNATURE_OR_SYSTEM_VALUE = 3;
                private static Internal.EnumLiteMap<Level> internalValueMap = new Internal.EnumLiteMap<Level>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Permission.Level.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.google.protobuf.Internal.EnumLiteMap
                    public Level findValueByNumber(int number) {
                        return Level.valueOf(number);
                    }
                };
                private static final Level[] VALUES = valuesCustom();
                private final int index;
                private final int value;

                /* renamed from: values  reason: to resolve conflict with enum method */
                public static Level[] valuesCustom() {
                    Level[] valuesCustom = values();
                    int length = valuesCustom.length;
                    Level[] levelArr = new Level[length];
                    System.arraycopy(valuesCustom, 0, levelArr, 0, length);
                    return levelArr;
                }

                @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
                public final int getNumber() {
                    return this.value;
                }

                public static Level valueOf(int value) {
                    switch (value) {
                        case 0:
                            return NORMAL;
                        case 1:
                            return DANGEROUS;
                        case 2:
                            return SIGNATURE;
                        case 3:
                            return SIGNATURE_OR_SYSTEM;
                        default:
                            return null;
                    }
                }

                public static Internal.EnumLiteMap<Level> internalGetValueMap() {
                    return internalValueMap;
                }

                @Override // com.google.protobuf.ProtocolMessageEnum
                public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                    return getDescriptor().getValues().get(this.index);
                }

                @Override // com.google.protobuf.ProtocolMessageEnum
                public final Descriptors.EnumDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public static final Descriptors.EnumDescriptor getDescriptor() {
                    return Permission.getDescriptor().getEnumTypes().get(0);
                }

                public static Level valueOf(Descriptors.EnumValueDescriptor desc) {
                    if (desc.getType() != getDescriptor()) {
                        throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                    }
                    return VALUES[desc.getIndex()];
                }

                Level(int index, int value) {
                    this.index = index;
                    this.value = value;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
            public String getName() {
                Object ref = this.name_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.name_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
            public ByteString getNameBytes() {
                Object ref = this.name_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.name_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
            public boolean hasLevel() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
            public Level getLevel() {
                return this.level_;
            }

            private void initFields() {
                this.name_ = "";
                this.level_ = Level.NORMAL;
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                byte isInitialized = this.memoizedIsInitialized;
                if (isInitialized != -1) {
                    return isInitialized == 1;
                }
                this.memoizedIsInitialized = (byte) 1;
                return true;
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
            public void writeTo(CodedOutputStream output) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    output.writeBytes(1, getNameBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    output.writeEnum(2, this.level_.getNumber());
                }
                getUnknownFields().writeTo(output);
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
            public int getSerializedSize() {
                int size = this.memoizedSerializedSize;
                if (size != -1) {
                    return size;
                }
                int size2 = 0;
                if ((this.bitField0_ & 1) == 1) {
                    size2 = 0 + CodedOutputStream.computeBytesSize(1, getNameBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    size2 += CodedOutputStream.computeEnumSize(2, this.level_.getNumber());
                }
                int size3 = size2 + getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = size3;
                return size3;
            }

            @Override // com.google.protobuf.GeneratedMessage
            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public static Permission parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static Permission parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static Permission parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static Permission parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static Permission parseFrom(InputStream input) throws IOException {
                return PARSER.parseFrom(input);
            }

            public static Permission parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Permission parseDelimitedFrom(InputStream input) throws IOException {
                return PARSER.parseDelimitedFrom(input);
            }

            public static Permission parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseDelimitedFrom(input, extensionRegistry);
            }

            public static Permission parseFrom(CodedInputStream input) throws IOException {
                return PARSER.parseFrom(input);
            }

            public static Permission parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(Permission prototype) {
                return newBuilder().mergeFrom(prototype);
            }

            @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Builder toBuilder() {
                return newBuilder(this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.protobuf.GeneratedMessage
            public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                Builder builder = new Builder(parent, null);
                return builder;
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Permission$Builder.class */
            public static final class Builder extends GeneratedMessage.Builder<Builder> implements PermissionOrBuilder {
                private int bitField0_;
                private Object name_;
                private Level level_;

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_fieldAccessorTable.ensureFieldAccessorsInitialized(Permission.class, Builder.class);
                }

                private Builder() {
                    this.name_ = "";
                    this.level_ = Level.NORMAL;
                    maybeForceBuilderInitialization();
                }

                /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                    this(builderParent);
                }

                private Builder(GeneratedMessage.BuilderParent parent) {
                    super(parent);
                    this.name_ = "";
                    this.level_ = Level.NORMAL;
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    boolean unused = Permission.alwaysUseFieldBuilders;
                }

                /* JADX INFO: Access modifiers changed from: private */
                public static Builder create() {
                    return new Builder();
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder clear() {
                    super.clear();
                    this.name_ = "";
                    this.bitField0_ &= -2;
                    this.level_ = Level.NORMAL;
                    this.bitField0_ &= -3;
                    return this;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                public Descriptors.Descriptor getDescriptorForType() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public Permission getDefaultInstanceForType() {
                    return Permission.getDefaultInstance();
                }

                @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Permission build() {
                    Permission result = buildPartial();
                    if (!result.isInitialized()) {
                        throw newUninitializedMessageException((Message) result);
                    }
                    return result;
                }

                @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Permission buildPartial() {
                    Permission result = new Permission(this, (Permission) null);
                    int from_bitField0_ = this.bitField0_;
                    int to_bitField0_ = 0;
                    if ((from_bitField0_ & 1) == 1) {
                        to_bitField0_ = 0 | 1;
                    }
                    result.name_ = this.name_;
                    if ((from_bitField0_ & 2) == 2) {
                        to_bitField0_ |= 2;
                    }
                    result.level_ = this.level_;
                    result.bitField0_ = to_bitField0_;
                    onBuilt();
                    return result;
                }

                @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                public Builder mergeFrom(Message other) {
                    if (other instanceof Permission) {
                        return mergeFrom((Permission) other);
                    }
                    super.mergeFrom(other);
                    return this;
                }

                public Builder mergeFrom(Permission other) {
                    if (other == Permission.getDefaultInstance()) {
                        return this;
                    }
                    if (other.hasName()) {
                        this.bitField0_ |= 1;
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (other.hasLevel()) {
                        setLevel(other.getLevel());
                    }
                    mergeUnknownFields(other.getUnknownFields());
                    return this;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    return true;
                }

                @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    Permission parsedMessage = null;
                    try {
                        try {
                            parsedMessage = Permission.PARSER.parsePartialFrom(input, extensionRegistry);
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            return this;
                        } catch (InvalidProtocolBufferException e) {
                            Permission permission = (Permission) e.getUnfinishedMessage();
                            throw e;
                        }
                    } catch (Throwable th) {
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        throw th;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
                public boolean hasName() {
                    return (this.bitField0_ & 1) == 1;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
                public String getName() {
                    Object ref = this.name_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.name_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
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
                    this.bitField0_ |= 1;
                    this.name_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearName() {
                    this.bitField0_ &= -2;
                    this.name_ = Permission.getDefaultInstance().getName();
                    onChanged();
                    return this;
                }

                public Builder setNameBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 1;
                    this.name_ = value;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
                public boolean hasLevel() {
                    return (this.bitField0_ & 2) == 2;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.PermissionOrBuilder
                public Level getLevel() {
                    return this.level_;
                }

                public Builder setLevel(Level value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 2;
                    this.level_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearLevel() {
                    this.bitField0_ &= -3;
                    this.level_ = Level.NORMAL;
                    onChanged();
                    return this;
                }
            }
        }

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component.class */
        public static final class Component extends GeneratedMessage implements ComponentOrBuilder {
            private final UnknownFieldSet unknownFields;
            private int bitField0_;
            public static final int NAME_FIELD_NUMBER = 1;
            private Object name_;
            public static final int KIND_FIELD_NUMBER = 2;
            private ComponentKind kind_;
            public static final int EXPORTED_FIELD_NUMBER = 3;
            private boolean exported_;
            public static final int PERMISSION_FIELD_NUMBER = 4;
            private Object permission_;
            public static final int MISSING_FIELD_NUMBER = 5;
            private int missing_;
            public static final int EXTRAS_FIELD_NUMBER = 6;
            private List<Extra> extras_;
            public static final int ALIAS_TARGET_FIELD_NUMBER = 7;
            private Object aliasTarget_;
            public static final int GRANT_URI_PERMISSIONS_FIELD_NUMBER = 8;
            private boolean grantUriPermissions_;
            public static final int READ_PERMISSION_FIELD_NUMBER = 9;
            private Object readPermission_;
            public static final int WRITE_PERMISSION_FIELD_NUMBER = 10;
            private Object writePermission_;
            public static final int AUTHORITIES_FIELD_NUMBER = 11;
            private LazyStringList authorities_;
            public static final int INTENT_FILTERS_FIELD_NUMBER = 12;
            private List<IntentFilter> intentFilters_;
            public static final int EXIT_POINTS_FIELD_NUMBER = 13;
            private List<ExitPoint> exitPoints_;
            public static final int REGISTRATION_INSTRUCTION_FIELD_NUMBER = 14;
            private Instruction registrationInstruction_;
            private byte memoizedIsInitialized;
            private int memoizedSerializedSize;
            private App app;
            private static final long serialVersionUID = 0;
            public static Parser<Component> PARSER = new AbstractParser<Component>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.1
                @Override // com.google.protobuf.Parser
                public Component parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return new Component(input, extensionRegistry, null);
                }
            };
            private static final Component defaultInstance = new Component(true);

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPointOrBuilder.class */
            public interface ExitPointOrBuilder extends MessageOrBuilder {
                boolean hasInstruction();

                Instruction getInstruction();

                InstructionOrBuilder getInstructionOrBuilder();

                boolean hasKind();

                ComponentKind getKind();

                boolean hasMissing();

                int getMissing();

                List<ExitPoint.Intent> getIntentsList();

                ExitPoint.Intent getIntents(int i);

                int getIntentsCount();

                List<? extends ExitPoint.IntentOrBuilder> getIntentsOrBuilderList();

                ExitPoint.IntentOrBuilder getIntentsOrBuilder(int i);

                List<ExitPoint.Uri> getUrisList();

                ExitPoint.Uri getUris(int i);

                int getUrisCount();

                List<? extends ExitPoint.UriOrBuilder> getUrisOrBuilderList();

                ExitPoint.UriOrBuilder getUrisOrBuilder(int i);
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExtraOrBuilder.class */
            public interface ExtraOrBuilder extends MessageOrBuilder {
                boolean hasExtra();

                String getExtra();

                ByteString getExtraBytes();

                boolean hasInstruction();

                Instruction getInstruction();

                InstructionOrBuilder getInstructionOrBuilder();
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$InstructionOrBuilder.class */
            public interface InstructionOrBuilder extends MessageOrBuilder {
                boolean hasStatement();

                String getStatement();

                ByteString getStatementBytes();

                boolean hasClassName();

                String getClassName();

                ByteString getClassNameBytes();

                boolean hasMethod();

                String getMethod();

                ByteString getMethodBytes();

                boolean hasId();

                int getId();
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$IntentFilterOrBuilder.class */
            public interface IntentFilterOrBuilder extends MessageOrBuilder {
                List<Attribute> getAttributesList();

                Attribute getAttributes(int i);

                int getAttributesCount();

                List<? extends AttributeOrBuilder> getAttributesOrBuilderList();

                AttributeOrBuilder getAttributesOrBuilder(int i);
            }

            /* synthetic */ Component(GeneratedMessage.Builder builder, Component component) {
                this(builder);
            }

            private Component(GeneratedMessage.Builder<?> builder) {
                super(builder);
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = builder.getUnknownFields();
            }

            private Component(boolean noInit) {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                this.unknownFields = UnknownFieldSet.getDefaultInstance();
            }

            public static Component getDefaultInstance() {
                return defaultInstance;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Component getDefaultInstanceForType() {
                return defaultInstance;
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
            public final UnknownFieldSet getUnknownFields() {
                return this.unknownFields;
            }

            private Component(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                this.memoizedIsInitialized = (byte) -1;
                this.memoizedSerializedSize = -1;
                initFields();
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
                                    this.bitField0_ |= 1;
                                    this.name_ = input.readBytes();
                                    break;
                                case 16:
                                    int rawValue = input.readEnum();
                                    ComponentKind value = ComponentKind.valueOf(rawValue);
                                    if (value == null) {
                                        unknownFields.mergeVarintField(2, rawValue);
                                        break;
                                    } else {
                                        this.bitField0_ |= 2;
                                        this.kind_ = value;
                                        break;
                                    }
                                case 24:
                                    this.bitField0_ |= 4;
                                    this.exported_ = input.readBool();
                                    break;
                                case 34:
                                    this.bitField0_ |= 8;
                                    this.permission_ = input.readBytes();
                                    break;
                                case 40:
                                    this.bitField0_ |= 16;
                                    this.missing_ = input.readUInt32();
                                    break;
                                case 50:
                                    if ((mutable_bitField0_ & 32) != 32) {
                                        this.extras_ = new ArrayList();
                                        mutable_bitField0_ |= 32;
                                    }
                                    this.extras_.add((Extra) input.readMessage(Extra.PARSER, extensionRegistry));
                                    break;
                                case 58:
                                    this.bitField0_ |= 32;
                                    this.aliasTarget_ = input.readBytes();
                                    break;
                                case 64:
                                    this.bitField0_ |= 64;
                                    this.grantUriPermissions_ = input.readBool();
                                    break;
                                case 74:
                                    this.bitField0_ |= 128;
                                    this.readPermission_ = input.readBytes();
                                    break;
                                case 82:
                                    this.bitField0_ |= 256;
                                    this.writePermission_ = input.readBytes();
                                    break;
                                case 90:
                                    if ((mutable_bitField0_ & 1024) != 1024) {
                                        this.authorities_ = new LazyStringArrayList();
                                        mutable_bitField0_ |= 1024;
                                    }
                                    this.authorities_.add(input.readBytes());
                                    break;
                                case 98:
                                    if ((mutable_bitField0_ & 2048) != 2048) {
                                        this.intentFilters_ = new ArrayList();
                                        mutable_bitField0_ |= 2048;
                                    }
                                    this.intentFilters_.add((IntentFilter) input.readMessage(IntentFilter.PARSER, extensionRegistry));
                                    break;
                                case 106:
                                    if ((mutable_bitField0_ & 4096) != 4096) {
                                        this.exitPoints_ = new ArrayList();
                                        mutable_bitField0_ |= 4096;
                                    }
                                    this.exitPoints_.add((ExitPoint) input.readMessage(ExitPoint.PARSER, extensionRegistry));
                                    break;
                                case 114:
                                    Instruction.Builder subBuilder = null;
                                    subBuilder = (this.bitField0_ & 512) == 512 ? this.registrationInstruction_.toBuilder() : subBuilder;
                                    this.registrationInstruction_ = (Instruction) input.readMessage(Instruction.PARSER, extensionRegistry);
                                    if (subBuilder != null) {
                                        subBuilder.mergeFrom(this.registrationInstruction_);
                                        this.registrationInstruction_ = subBuilder.buildPartial();
                                    }
                                    this.bitField0_ |= 512;
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
                            throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                        }
                    }
                    if ((mutable_bitField0_ & 32) == 32) {
                        this.extras_ = Collections.unmodifiableList(this.extras_);
                    }
                    if ((mutable_bitField0_ & 1024) == 1024) {
                        this.authorities_ = new UnmodifiableLazyStringList(this.authorities_);
                    }
                    if ((mutable_bitField0_ & 2048) == 2048) {
                        this.intentFilters_ = Collections.unmodifiableList(this.intentFilters_);
                    }
                    if ((mutable_bitField0_ & 4096) == 4096) {
                        this.exitPoints_ = Collections.unmodifiableList(this.exitPoints_);
                    }
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                } catch (Throwable th) {
                    if ((mutable_bitField0_ & 32) == 32) {
                        this.extras_ = Collections.unmodifiableList(this.extras_);
                    }
                    if ((mutable_bitField0_ & 1024) == 1024) {
                        this.authorities_ = new UnmodifiableLazyStringList(this.authorities_);
                    }
                    if ((mutable_bitField0_ & 2048) == 2048) {
                        this.intentFilters_ = Collections.unmodifiableList(this.intentFilters_);
                    }
                    if ((mutable_bitField0_ & 4096) == 4096) {
                        this.exitPoints_ = Collections.unmodifiableList(this.exitPoints_);
                    }
                    this.unknownFields = unknownFields.build();
                    makeExtensionsImmutable();
                    throw th;
                }
            }

            /* synthetic */ Component(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Component component) throws InvalidProtocolBufferException {
                this(codedInputStream, extensionRegistryLite);
            }

            public static final Descriptors.Descriptor getDescriptor() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessage
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_fieldAccessorTable.ensureFieldAccessorsInitialized(Component.class, Builder.class);
            }

            static {
                defaultInstance.initFields();
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Parser<Component> getParserForType() {
                return PARSER;
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ComponentKind.class */
            public enum ComponentKind implements ProtocolMessageEnum {
                ACTIVITY(0, 0),
                SERVICE(1, 1),
                RECEIVER(2, 2),
                DYNAMIC_RECEIVER(3, 3),
                PROVIDER(4, 4);
                
                public static final int ACTIVITY_VALUE = 0;
                public static final int SERVICE_VALUE = 1;
                public static final int RECEIVER_VALUE = 2;
                public static final int DYNAMIC_RECEIVER_VALUE = 3;
                public static final int PROVIDER_VALUE = 4;
                private static Internal.EnumLiteMap<ComponentKind> internalValueMap = new Internal.EnumLiteMap<ComponentKind>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ComponentKind.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // com.google.protobuf.Internal.EnumLiteMap
                    public ComponentKind findValueByNumber(int number) {
                        return ComponentKind.valueOf(number);
                    }
                };
                private static final ComponentKind[] VALUES = valuesCustom();
                private final int index;
                private final int value;

                /* renamed from: values  reason: to resolve conflict with enum method */
                public static ComponentKind[] valuesCustom() {
                    ComponentKind[] valuesCustom = values();
                    int length = valuesCustom.length;
                    ComponentKind[] componentKindArr = new ComponentKind[length];
                    System.arraycopy(valuesCustom, 0, componentKindArr, 0, length);
                    return componentKindArr;
                }

                @Override // com.google.protobuf.ProtocolMessageEnum, com.google.protobuf.Internal.EnumLite
                public final int getNumber() {
                    return this.value;
                }

                public static ComponentKind valueOf(int value) {
                    switch (value) {
                        case 0:
                            return ACTIVITY;
                        case 1:
                            return SERVICE;
                        case 2:
                            return RECEIVER;
                        case 3:
                            return DYNAMIC_RECEIVER;
                        case 4:
                            return PROVIDER;
                        default:
                            return null;
                    }
                }

                public static Internal.EnumLiteMap<ComponentKind> internalGetValueMap() {
                    return internalValueMap;
                }

                @Override // com.google.protobuf.ProtocolMessageEnum
                public final Descriptors.EnumValueDescriptor getValueDescriptor() {
                    return getDescriptor().getValues().get(this.index);
                }

                @Override // com.google.protobuf.ProtocolMessageEnum
                public final Descriptors.EnumDescriptor getDescriptorForType() {
                    return getDescriptor();
                }

                public static final Descriptors.EnumDescriptor getDescriptor() {
                    return Component.getDescriptor().getEnumTypes().get(0);
                }

                public static ComponentKind valueOf(Descriptors.EnumValueDescriptor desc) {
                    if (desc.getType() != getDescriptor()) {
                        throw new IllegalArgumentException("EnumValueDescriptor is not for this type.");
                    }
                    return VALUES[desc.getIndex()];
                }

                ComponentKind(int index, int value) {
                    this.index = index;
                    this.value = value;
                }
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$Extra.class */
            public static final class Extra extends GeneratedMessage implements ExtraOrBuilder {
                private final UnknownFieldSet unknownFields;
                private int bitField0_;
                public static final int EXTRA_FIELD_NUMBER = 1;
                private Object extra_;
                public static final int INSTRUCTION_FIELD_NUMBER = 2;
                private Instruction instruction_;
                private byte memoizedIsInitialized;
                private int memoizedSerializedSize;
                private static final long serialVersionUID = 0;
                public static Parser<Extra> PARSER = new AbstractParser<Extra>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.Extra.1
                    @Override // com.google.protobuf.Parser
                    public Extra parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return new Extra(input, extensionRegistry, null);
                    }
                };
                private static final Extra defaultInstance = new Extra(true);

                /* synthetic */ Extra(GeneratedMessage.Builder builder, Extra extra) {
                    this(builder);
                }

                private Extra(GeneratedMessage.Builder<?> builder) {
                    super(builder);
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = builder.getUnknownFields();
                }

                private Extra(boolean noInit) {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = UnknownFieldSet.getDefaultInstance();
                }

                public static Extra getDefaultInstance() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public Extra getDefaultInstanceForType() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                public final UnknownFieldSet getUnknownFields() {
                    return this.unknownFields;
                }

                private Extra(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    initFields();
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
                                            this.bitField0_ |= 1;
                                            this.extra_ = input.readBytes();
                                            break;
                                        case 18:
                                            Instruction.Builder subBuilder = null;
                                            subBuilder = (this.bitField0_ & 2) == 2 ? this.instruction_.toBuilder() : subBuilder;
                                            this.instruction_ = (Instruction) input.readMessage(Instruction.PARSER, extensionRegistry);
                                            if (subBuilder != null) {
                                                subBuilder.mergeFrom(this.instruction_);
                                                this.instruction_ = subBuilder.buildPartial();
                                            }
                                            this.bitField0_ |= 2;
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
                                throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                            }
                        }
                    } finally {
                        this.unknownFields = unknownFields.build();
                        makeExtensionsImmutable();
                    }
                }

                /* synthetic */ Extra(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Extra extra) throws InvalidProtocolBufferException {
                    this(codedInputStream, extensionRegistryLite);
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_fieldAccessorTable.ensureFieldAccessorsInitialized(Extra.class, Builder.class);
                }

                static {
                    defaultInstance.initFields();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Parser<Extra> getParserForType() {
                    return PARSER;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public boolean hasExtra() {
                    return (this.bitField0_ & 1) == 1;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public String getExtra() {
                    Object ref = this.extra_;
                    if (ref instanceof String) {
                        return (String) ref;
                    }
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.extra_ = s;
                    }
                    return s;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public ByteString getExtraBytes() {
                    Object ref = this.extra_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.extra_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public boolean hasInstruction() {
                    return (this.bitField0_ & 2) == 2;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public Instruction getInstruction() {
                    return this.instruction_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                public InstructionOrBuilder getInstructionOrBuilder() {
                    return this.instruction_;
                }

                private void initFields() {
                    this.extra_ = "";
                    this.instruction_ = Instruction.getDefaultInstance();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    byte isInitialized = this.memoizedIsInitialized;
                    if (isInitialized != -1) {
                        return isInitialized == 1;
                    }
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public void writeTo(CodedOutputStream output) throws IOException {
                    getSerializedSize();
                    if ((this.bitField0_ & 1) == 1) {
                        output.writeBytes(1, getExtraBytes());
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        output.writeMessage(2, this.instruction_);
                    }
                    getUnknownFields().writeTo(output);
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public int getSerializedSize() {
                    int size = this.memoizedSerializedSize;
                    if (size != -1) {
                        return size;
                    }
                    int size2 = 0;
                    if ((this.bitField0_ & 1) == 1) {
                        size2 = 0 + CodedOutputStream.computeBytesSize(1, getExtraBytes());
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        size2 += CodedOutputStream.computeMessageSize(2, this.instruction_);
                    }
                    int size3 = size2 + getUnknownFields().getSerializedSize();
                    this.memoizedSerializedSize = size3;
                    return size3;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected Object writeReplace() throws ObjectStreamException {
                    return super.writeReplace();
                }

                public static Extra parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static Extra parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static Extra parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static Extra parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static Extra parseFrom(InputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static Extra parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Extra parseDelimitedFrom(InputStream input) throws IOException {
                    return PARSER.parseDelimitedFrom(input);
                }

                public static Extra parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseDelimitedFrom(input, extensionRegistry);
                }

                public static Extra parseFrom(CodedInputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static Extra parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Builder newBuilder() {
                    return Builder.create();
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder newBuilderForType() {
                    return newBuilder();
                }

                public static Builder newBuilder(Extra prototype) {
                    return newBuilder().mergeFrom(prototype);
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder toBuilder() {
                    return newBuilder(this);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.protobuf.GeneratedMessage
                public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                    Builder builder = new Builder(parent, null);
                    return builder;
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$Extra$Builder.class */
                public static final class Builder extends GeneratedMessage.Builder<Builder> implements ExtraOrBuilder {
                    private int bitField0_;
                    private Object extra_;
                    private Instruction instruction_;
                    private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> instructionBuilder_;

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_fieldAccessorTable.ensureFieldAccessorsInitialized(Extra.class, Builder.class);
                    }

                    private Builder() {
                        this.extra_ = "";
                        this.instruction_ = Instruction.getDefaultInstance();
                        maybeForceBuilderInitialization();
                    }

                    /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                        this(builderParent);
                    }

                    private Builder(GeneratedMessage.BuilderParent parent) {
                        super(parent);
                        this.extra_ = "";
                        this.instruction_ = Instruction.getDefaultInstance();
                        maybeForceBuilderInitialization();
                    }

                    private void maybeForceBuilderInitialization() {
                        if (Extra.alwaysUseFieldBuilders) {
                            getInstructionFieldBuilder();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public static Builder create() {
                        return new Builder();
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clear() {
                        super.clear();
                        this.extra_ = "";
                        this.bitField0_ &= -2;
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = Instruction.getDefaultInstance();
                        } else {
                            this.instructionBuilder_.clear();
                        }
                        this.bitField0_ &= -3;
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clone() {
                        return create().mergeFrom(buildPartial());
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                    public Descriptors.Descriptor getDescriptorForType() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public Extra getDefaultInstanceForType() {
                        return Extra.getDefaultInstance();
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Extra build() {
                        Extra result = buildPartial();
                        if (!result.isInitialized()) {
                            throw newUninitializedMessageException((Message) result);
                        }
                        return result;
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Extra buildPartial() {
                        Extra result = new Extra(this, (Extra) null);
                        int from_bitField0_ = this.bitField0_;
                        int to_bitField0_ = 0;
                        if ((from_bitField0_ & 1) == 1) {
                            to_bitField0_ = 0 | 1;
                        }
                        result.extra_ = this.extra_;
                        if ((from_bitField0_ & 2) == 2) {
                            to_bitField0_ |= 2;
                        }
                        if (this.instructionBuilder_ == null) {
                            result.instruction_ = this.instruction_;
                        } else {
                            result.instruction_ = this.instructionBuilder_.build();
                        }
                        result.bitField0_ = to_bitField0_;
                        onBuilt();
                        return result;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(Message other) {
                        if (other instanceof Extra) {
                            return mergeFrom((Extra) other);
                        }
                        super.mergeFrom(other);
                        return this;
                    }

                    public Builder mergeFrom(Extra other) {
                        if (other == Extra.getDefaultInstance()) {
                            return this;
                        }
                        if (other.hasExtra()) {
                            this.bitField0_ |= 1;
                            this.extra_ = other.extra_;
                            onChanged();
                        }
                        if (other.hasInstruction()) {
                            mergeInstruction(other.getInstruction());
                        }
                        mergeUnknownFields(other.getUnknownFields());
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        return true;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        Extra parsedMessage = null;
                        try {
                            try {
                                parsedMessage = Extra.PARSER.parsePartialFrom(input, extensionRegistry);
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                return this;
                            } catch (InvalidProtocolBufferException e) {
                                Extra extra = (Extra) e.getUnfinishedMessage();
                                throw e;
                            }
                        } catch (Throwable th) {
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            throw th;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public boolean hasExtra() {
                        return (this.bitField0_ & 1) == 1;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public String getExtra() {
                        Object ref = this.extra_;
                        if (!(ref instanceof String)) {
                            String s = ((ByteString) ref).toStringUtf8();
                            this.extra_ = s;
                            return s;
                        }
                        return (String) ref;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public ByteString getExtraBytes() {
                        Object ref = this.extra_;
                        if (ref instanceof String) {
                            ByteString b = ByteString.copyFromUtf8((String) ref);
                            this.extra_ = b;
                            return b;
                        }
                        return (ByteString) ref;
                    }

                    public Builder setExtra(String value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 1;
                        this.extra_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearExtra() {
                        this.bitField0_ &= -2;
                        this.extra_ = Extra.getDefaultInstance().getExtra();
                        onChanged();
                        return this;
                    }

                    public Builder setExtraBytes(ByteString value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 1;
                        this.extra_ = value;
                        onChanged();
                        return this;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public boolean hasInstruction() {
                        return (this.bitField0_ & 2) == 2;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public Instruction getInstruction() {
                        if (this.instructionBuilder_ == null) {
                            return this.instruction_;
                        }
                        return this.instructionBuilder_.getMessage();
                    }

                    public Builder setInstruction(Instruction value) {
                        if (this.instructionBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            this.instruction_ = value;
                            onChanged();
                        } else {
                            this.instructionBuilder_.setMessage(value);
                        }
                        this.bitField0_ |= 2;
                        return this;
                    }

                    public Builder setInstruction(Instruction.Builder builderForValue) {
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = builderForValue.build();
                            onChanged();
                        } else {
                            this.instructionBuilder_.setMessage(builderForValue.build());
                        }
                        this.bitField0_ |= 2;
                        return this;
                    }

                    public Builder mergeInstruction(Instruction value) {
                        if (this.instructionBuilder_ == null) {
                            if ((this.bitField0_ & 2) == 2 && this.instruction_ != Instruction.getDefaultInstance()) {
                                this.instruction_ = Instruction.newBuilder(this.instruction_).mergeFrom(value).buildPartial();
                            } else {
                                this.instruction_ = value;
                            }
                            onChanged();
                        } else {
                            this.instructionBuilder_.mergeFrom(value);
                        }
                        this.bitField0_ |= 2;
                        return this;
                    }

                    public Builder clearInstruction() {
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = Instruction.getDefaultInstance();
                            onChanged();
                        } else {
                            this.instructionBuilder_.clear();
                        }
                        this.bitField0_ &= -3;
                        return this;
                    }

                    public Instruction.Builder getInstructionBuilder() {
                        this.bitField0_ |= 2;
                        onChanged();
                        return getInstructionFieldBuilder().getBuilder();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExtraOrBuilder
                    public InstructionOrBuilder getInstructionOrBuilder() {
                        if (this.instructionBuilder_ != null) {
                            return this.instructionBuilder_.getMessageOrBuilder();
                        }
                        return this.instruction_;
                    }

                    private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> getInstructionFieldBuilder() {
                        if (this.instructionBuilder_ == null) {
                            this.instructionBuilder_ = new SingleFieldBuilder<>(this.instruction_, getParentForChildren(), isClean());
                            this.instruction_ = null;
                        }
                        return this.instructionBuilder_;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$IntentFilter.class */
            public static final class IntentFilter extends GeneratedMessage implements IntentFilterOrBuilder {
                private final UnknownFieldSet unknownFields;
                public static final int ATTRIBUTES_FIELD_NUMBER = 1;
                private List<Attribute> attributes_;
                private byte memoizedIsInitialized;
                private int memoizedSerializedSize;
                private static final long serialVersionUID = 0;
                public static Parser<IntentFilter> PARSER = new AbstractParser<IntentFilter>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilter.1
                    @Override // com.google.protobuf.Parser
                    public IntentFilter parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return new IntentFilter(input, extensionRegistry, null);
                    }
                };
                private static final IntentFilter defaultInstance = new IntentFilter(true);

                /* synthetic */ IntentFilter(GeneratedMessage.Builder builder, IntentFilter intentFilter) {
                    this(builder);
                }

                private IntentFilter(GeneratedMessage.Builder<?> builder) {
                    super(builder);
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = builder.getUnknownFields();
                }

                private IntentFilter(boolean noInit) {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = UnknownFieldSet.getDefaultInstance();
                }

                public static IntentFilter getDefaultInstance() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public IntentFilter getDefaultInstanceForType() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                public final UnknownFieldSet getUnknownFields() {
                    return this.unknownFields;
                }

                private IntentFilter(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    initFields();
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
                                        if ((mutable_bitField0_ & 1) != 1) {
                                            this.attributes_ = new ArrayList();
                                            mutable_bitField0_ |= 1;
                                        }
                                        this.attributes_.add((Attribute) input.readMessage(Attribute.PARSER, extensionRegistry));
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
                                throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                            }
                        }
                    } finally {
                        if ((mutable_bitField0_ & 1) == 1) {
                            this.attributes_ = Collections.unmodifiableList(this.attributes_);
                        }
                        this.unknownFields = unknownFields.build();
                        makeExtensionsImmutable();
                    }
                }

                /* synthetic */ IntentFilter(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, IntentFilter intentFilter) throws InvalidProtocolBufferException {
                    this(codedInputStream, extensionRegistryLite);
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_fieldAccessorTable.ensureFieldAccessorsInitialized(IntentFilter.class, Builder.class);
                }

                static {
                    defaultInstance.initFields();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Parser<IntentFilter> getParserForType() {
                    return PARSER;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                public List<Attribute> getAttributesList() {
                    return this.attributes_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                    return this.attributes_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                public int getAttributesCount() {
                    return this.attributes_.size();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                public Attribute getAttributes(int index) {
                    return this.attributes_.get(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                public AttributeOrBuilder getAttributesOrBuilder(int index) {
                    return this.attributes_.get(index);
                }

                private void initFields() {
                    this.attributes_ = Collections.emptyList();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    byte isInitialized = this.memoizedIsInitialized;
                    if (isInitialized != -1) {
                        return isInitialized == 1;
                    }
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public void writeTo(CodedOutputStream output) throws IOException {
                    getSerializedSize();
                    for (int i = 0; i < this.attributes_.size(); i++) {
                        output.writeMessage(1, this.attributes_.get(i));
                    }
                    getUnknownFields().writeTo(output);
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public int getSerializedSize() {
                    int size = this.memoizedSerializedSize;
                    if (size != -1) {
                        return size;
                    }
                    int size2 = 0;
                    for (int i = 0; i < this.attributes_.size(); i++) {
                        size2 += CodedOutputStream.computeMessageSize(1, this.attributes_.get(i));
                    }
                    int size3 = size2 + getUnknownFields().getSerializedSize();
                    this.memoizedSerializedSize = size3;
                    return size3;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected Object writeReplace() throws ObjectStreamException {
                    return super.writeReplace();
                }

                public static IntentFilter parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static IntentFilter parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static IntentFilter parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static IntentFilter parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static IntentFilter parseFrom(InputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static IntentFilter parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static IntentFilter parseDelimitedFrom(InputStream input) throws IOException {
                    return PARSER.parseDelimitedFrom(input);
                }

                public static IntentFilter parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseDelimitedFrom(input, extensionRegistry);
                }

                public static IntentFilter parseFrom(CodedInputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static IntentFilter parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Builder newBuilder() {
                    return Builder.create();
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder newBuilderForType() {
                    return newBuilder();
                }

                public static Builder newBuilder(IntentFilter prototype) {
                    return newBuilder().mergeFrom(prototype);
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder toBuilder() {
                    return newBuilder(this);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.protobuf.GeneratedMessage
                public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                    Builder builder = new Builder(parent, null);
                    return builder;
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$IntentFilter$Builder.class */
                public static final class Builder extends GeneratedMessage.Builder<Builder> implements IntentFilterOrBuilder {
                    private int bitField0_;
                    private List<Attribute> attributes_;
                    private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> attributesBuilder_;

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_fieldAccessorTable.ensureFieldAccessorsInitialized(IntentFilter.class, Builder.class);
                    }

                    private Builder() {
                        this.attributes_ = Collections.emptyList();
                        maybeForceBuilderInitialization();
                    }

                    /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                        this(builderParent);
                    }

                    private Builder(GeneratedMessage.BuilderParent parent) {
                        super(parent);
                        this.attributes_ = Collections.emptyList();
                        maybeForceBuilderInitialization();
                    }

                    private void maybeForceBuilderInitialization() {
                        if (IntentFilter.alwaysUseFieldBuilders) {
                            getAttributesFieldBuilder();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public static Builder create() {
                        return new Builder();
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clear() {
                        super.clear();
                        if (this.attributesBuilder_ == null) {
                            this.attributes_ = Collections.emptyList();
                            this.bitField0_ &= -2;
                        } else {
                            this.attributesBuilder_.clear();
                        }
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clone() {
                        return create().mergeFrom(buildPartial());
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                    public Descriptors.Descriptor getDescriptorForType() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public IntentFilter getDefaultInstanceForType() {
                        return IntentFilter.getDefaultInstance();
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public IntentFilter build() {
                        IntentFilter result = buildPartial();
                        if (!result.isInitialized()) {
                            throw newUninitializedMessageException((Message) result);
                        }
                        return result;
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public IntentFilter buildPartial() {
                        IntentFilter result = new IntentFilter(this, (IntentFilter) null);
                        int i = this.bitField0_;
                        if (this.attributesBuilder_ != null) {
                            result.attributes_ = this.attributesBuilder_.build();
                        } else {
                            if ((this.bitField0_ & 1) == 1) {
                                this.attributes_ = Collections.unmodifiableList(this.attributes_);
                                this.bitField0_ &= -2;
                            }
                            result.attributes_ = this.attributes_;
                        }
                        onBuilt();
                        return result;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(Message other) {
                        if (other instanceof IntentFilter) {
                            return mergeFrom((IntentFilter) other);
                        }
                        super.mergeFrom(other);
                        return this;
                    }

                    public Builder mergeFrom(IntentFilter other) {
                        RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> repeatedFieldBuilder;
                        if (other == IntentFilter.getDefaultInstance()) {
                            return this;
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
                                if (IntentFilter.alwaysUseFieldBuilders) {
                                    repeatedFieldBuilder = getAttributesFieldBuilder();
                                } else {
                                    repeatedFieldBuilder = null;
                                }
                                this.attributesBuilder_ = repeatedFieldBuilder;
                            }
                        }
                        mergeUnknownFields(other.getUnknownFields());
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        return true;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        IntentFilter parsedMessage = null;
                        try {
                            try {
                                parsedMessage = IntentFilter.PARSER.parsePartialFrom(input, extensionRegistry);
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                return this;
                            } catch (InvalidProtocolBufferException e) {
                                IntentFilter intentFilter = (IntentFilter) e.getUnfinishedMessage();
                                throw e;
                            }
                        } catch (Throwable th) {
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            throw th;
                        }
                    }

                    private void ensureAttributesIsMutable() {
                        if ((this.bitField0_ & 1) != 1) {
                            this.attributes_ = new ArrayList(this.attributes_);
                            this.bitField0_ |= 1;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                    public List<Attribute> getAttributesList() {
                        if (this.attributesBuilder_ == null) {
                            return Collections.unmodifiableList(this.attributes_);
                        }
                        return this.attributesBuilder_.getMessageList();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                    public int getAttributesCount() {
                        if (this.attributesBuilder_ == null) {
                            return this.attributes_.size();
                        }
                        return this.attributesBuilder_.getCount();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                    public Attribute getAttributes(int index) {
                        if (this.attributesBuilder_ == null) {
                            return this.attributes_.get(index);
                        }
                        return this.attributesBuilder_.getMessage(index);
                    }

                    public Builder setAttributes(int index, Attribute value) {
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

                    public Builder setAttributes(int index, Attribute.Builder builderForValue) {
                        if (this.attributesBuilder_ == null) {
                            ensureAttributesIsMutable();
                            this.attributes_.set(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.attributesBuilder_.setMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addAttributes(Attribute value) {
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

                    public Builder addAttributes(int index, Attribute value) {
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

                    public Builder addAttributes(Attribute.Builder builderForValue) {
                        if (this.attributesBuilder_ == null) {
                            ensureAttributesIsMutable();
                            this.attributes_.add(builderForValue.build());
                            onChanged();
                        } else {
                            this.attributesBuilder_.addMessage(builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addAttributes(int index, Attribute.Builder builderForValue) {
                        if (this.attributesBuilder_ == null) {
                            ensureAttributesIsMutable();
                            this.attributes_.add(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.attributesBuilder_.addMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addAllAttributes(Iterable<? extends Attribute> values) {
                        if (this.attributesBuilder_ == null) {
                            ensureAttributesIsMutable();
                            GeneratedMessage.Builder.addAll((Iterable) values, (List) this.attributes_);
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

                    public Attribute.Builder getAttributesBuilder(int index) {
                        return getAttributesFieldBuilder().getBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                    public AttributeOrBuilder getAttributesOrBuilder(int index) {
                        if (this.attributesBuilder_ == null) {
                            return this.attributes_.get(index);
                        }
                        return this.attributesBuilder_.getMessageOrBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.IntentFilterOrBuilder
                    public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                        if (this.attributesBuilder_ != null) {
                            return this.attributesBuilder_.getMessageOrBuilderList();
                        }
                        return Collections.unmodifiableList(this.attributes_);
                    }

                    public Attribute.Builder addAttributesBuilder() {
                        return getAttributesFieldBuilder().addBuilder(Attribute.getDefaultInstance());
                    }

                    public Attribute.Builder addAttributesBuilder(int index) {
                        return getAttributesFieldBuilder().addBuilder(index, Attribute.getDefaultInstance());
                    }

                    public List<Attribute.Builder> getAttributesBuilderList() {
                        return getAttributesFieldBuilder().getBuilderList();
                    }

                    private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> getAttributesFieldBuilder() {
                        if (this.attributesBuilder_ == null) {
                            this.attributesBuilder_ = new RepeatedFieldBuilder<>(this.attributes_, (this.bitField0_ & 1) == 1, getParentForChildren(), isClean());
                            this.attributes_ = null;
                        }
                        return this.attributesBuilder_;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$Instruction.class */
            public static final class Instruction extends GeneratedMessage implements InstructionOrBuilder {
                private final UnknownFieldSet unknownFields;
                private int bitField0_;
                public static final int STATEMENT_FIELD_NUMBER = 1;
                private Object statement_;
                public static final int CLASS_NAME_FIELD_NUMBER = 2;
                private Object className_;
                public static final int METHOD_FIELD_NUMBER = 3;
                private Object method_;
                public static final int ID_FIELD_NUMBER = 4;
                private int id_;
                private byte memoizedIsInitialized;
                private int memoizedSerializedSize;
                private static final long serialVersionUID = 0;
                public static Parser<Instruction> PARSER = new AbstractParser<Instruction>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.Instruction.1
                    @Override // com.google.protobuf.Parser
                    public Instruction parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return new Instruction(input, extensionRegistry, null);
                    }
                };
                private static final Instruction defaultInstance = new Instruction(true);

                /* synthetic */ Instruction(GeneratedMessage.Builder builder, Instruction instruction) {
                    this(builder);
                }

                private Instruction(GeneratedMessage.Builder<?> builder) {
                    super(builder);
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = builder.getUnknownFields();
                }

                private Instruction(boolean noInit) {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = UnknownFieldSet.getDefaultInstance();
                }

                public static Instruction getDefaultInstance() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public Instruction getDefaultInstanceForType() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                public final UnknownFieldSet getUnknownFields() {
                    return this.unknownFields;
                }

                private Instruction(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    initFields();
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
                                            this.bitField0_ |= 1;
                                            this.statement_ = input.readBytes();
                                            break;
                                        case 18:
                                            this.bitField0_ |= 2;
                                            this.className_ = input.readBytes();
                                            break;
                                        case 26:
                                            this.bitField0_ |= 4;
                                            this.method_ = input.readBytes();
                                            break;
                                        case 32:
                                            this.bitField0_ |= 8;
                                            this.id_ = input.readUInt32();
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
                                    throw new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
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

                /* synthetic */ Instruction(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Instruction instruction) throws InvalidProtocolBufferException {
                    this(codedInputStream, extensionRegistryLite);
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_fieldAccessorTable.ensureFieldAccessorsInitialized(Instruction.class, Builder.class);
                }

                static {
                    defaultInstance.initFields();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Parser<Instruction> getParserForType() {
                    return PARSER;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public boolean hasStatement() {
                    return (this.bitField0_ & 1) == 1;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public String getStatement() {
                    Object ref = this.statement_;
                    if (ref instanceof String) {
                        return (String) ref;
                    }
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.statement_ = s;
                    }
                    return s;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public ByteString getStatementBytes() {
                    Object ref = this.statement_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.statement_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public boolean hasClassName() {
                    return (this.bitField0_ & 2) == 2;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public String getClassName() {
                    Object ref = this.className_;
                    if (ref instanceof String) {
                        return (String) ref;
                    }
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.className_ = s;
                    }
                    return s;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public ByteString getClassNameBytes() {
                    Object ref = this.className_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.className_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public boolean hasMethod() {
                    return (this.bitField0_ & 4) == 4;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public String getMethod() {
                    Object ref = this.method_;
                    if (ref instanceof String) {
                        return (String) ref;
                    }
                    ByteString bs = (ByteString) ref;
                    String s = bs.toStringUtf8();
                    if (bs.isValidUtf8()) {
                        this.method_ = s;
                    }
                    return s;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public ByteString getMethodBytes() {
                    Object ref = this.method_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.method_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public boolean hasId() {
                    return (this.bitField0_ & 8) == 8;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                public int getId() {
                    return this.id_;
                }

                private void initFields() {
                    this.statement_ = "";
                    this.className_ = "";
                    this.method_ = "";
                    this.id_ = 0;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    byte isInitialized = this.memoizedIsInitialized;
                    if (isInitialized != -1) {
                        return isInitialized == 1;
                    }
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public void writeTo(CodedOutputStream output) throws IOException {
                    getSerializedSize();
                    if ((this.bitField0_ & 1) == 1) {
                        output.writeBytes(1, getStatementBytes());
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        output.writeBytes(2, getClassNameBytes());
                    }
                    if ((this.bitField0_ & 4) == 4) {
                        output.writeBytes(3, getMethodBytes());
                    }
                    if ((this.bitField0_ & 8) == 8) {
                        output.writeUInt32(4, this.id_);
                    }
                    getUnknownFields().writeTo(output);
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public int getSerializedSize() {
                    int size = this.memoizedSerializedSize;
                    if (size != -1) {
                        return size;
                    }
                    int size2 = 0;
                    if ((this.bitField0_ & 1) == 1) {
                        size2 = 0 + CodedOutputStream.computeBytesSize(1, getStatementBytes());
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        size2 += CodedOutputStream.computeBytesSize(2, getClassNameBytes());
                    }
                    if ((this.bitField0_ & 4) == 4) {
                        size2 += CodedOutputStream.computeBytesSize(3, getMethodBytes());
                    }
                    if ((this.bitField0_ & 8) == 8) {
                        size2 += CodedOutputStream.computeUInt32Size(4, this.id_);
                    }
                    int size3 = size2 + getUnknownFields().getSerializedSize();
                    this.memoizedSerializedSize = size3;
                    return size3;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected Object writeReplace() throws ObjectStreamException {
                    return super.writeReplace();
                }

                public static Instruction parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static Instruction parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static Instruction parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static Instruction parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static Instruction parseFrom(InputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static Instruction parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Instruction parseDelimitedFrom(InputStream input) throws IOException {
                    return PARSER.parseDelimitedFrom(input);
                }

                public static Instruction parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseDelimitedFrom(input, extensionRegistry);
                }

                public static Instruction parseFrom(CodedInputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static Instruction parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Builder newBuilder() {
                    return Builder.create();
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder newBuilderForType() {
                    return newBuilder();
                }

                public static Builder newBuilder(Instruction prototype) {
                    return newBuilder().mergeFrom(prototype);
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder toBuilder() {
                    return newBuilder(this);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.protobuf.GeneratedMessage
                public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                    Builder builder = new Builder(parent, null);
                    return builder;
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$Instruction$Builder.class */
                public static final class Builder extends GeneratedMessage.Builder<Builder> implements InstructionOrBuilder {
                    private int bitField0_;
                    private Object statement_;
                    private Object className_;
                    private Object method_;
                    private int id_;

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_fieldAccessorTable.ensureFieldAccessorsInitialized(Instruction.class, Builder.class);
                    }

                    private Builder() {
                        this.statement_ = "";
                        this.className_ = "";
                        this.method_ = "";
                        maybeForceBuilderInitialization();
                    }

                    /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                        this(builderParent);
                    }

                    private Builder(GeneratedMessage.BuilderParent parent) {
                        super(parent);
                        this.statement_ = "";
                        this.className_ = "";
                        this.method_ = "";
                        maybeForceBuilderInitialization();
                    }

                    private void maybeForceBuilderInitialization() {
                        boolean unused = Instruction.alwaysUseFieldBuilders;
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public static Builder create() {
                        return new Builder();
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clear() {
                        super.clear();
                        this.statement_ = "";
                        this.bitField0_ &= -2;
                        this.className_ = "";
                        this.bitField0_ &= -3;
                        this.method_ = "";
                        this.bitField0_ &= -5;
                        this.id_ = 0;
                        this.bitField0_ &= -9;
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clone() {
                        return create().mergeFrom(buildPartial());
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                    public Descriptors.Descriptor getDescriptorForType() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public Instruction getDefaultInstanceForType() {
                        return Instruction.getDefaultInstance();
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Instruction build() {
                        Instruction result = buildPartial();
                        if (!result.isInitialized()) {
                            throw newUninitializedMessageException((Message) result);
                        }
                        return result;
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Instruction buildPartial() {
                        Instruction result = new Instruction(this, (Instruction) null);
                        int from_bitField0_ = this.bitField0_;
                        int to_bitField0_ = 0;
                        if ((from_bitField0_ & 1) == 1) {
                            to_bitField0_ = 0 | 1;
                        }
                        result.statement_ = this.statement_;
                        if ((from_bitField0_ & 2) == 2) {
                            to_bitField0_ |= 2;
                        }
                        result.className_ = this.className_;
                        if ((from_bitField0_ & 4) == 4) {
                            to_bitField0_ |= 4;
                        }
                        result.method_ = this.method_;
                        if ((from_bitField0_ & 8) == 8) {
                            to_bitField0_ |= 8;
                        }
                        result.id_ = this.id_;
                        result.bitField0_ = to_bitField0_;
                        onBuilt();
                        return result;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(Message other) {
                        if (other instanceof Instruction) {
                            return mergeFrom((Instruction) other);
                        }
                        super.mergeFrom(other);
                        return this;
                    }

                    public Builder mergeFrom(Instruction other) {
                        if (other == Instruction.getDefaultInstance()) {
                            return this;
                        }
                        if (other.hasStatement()) {
                            this.bitField0_ |= 1;
                            this.statement_ = other.statement_;
                            onChanged();
                        }
                        if (other.hasClassName()) {
                            this.bitField0_ |= 2;
                            this.className_ = other.className_;
                            onChanged();
                        }
                        if (other.hasMethod()) {
                            this.bitField0_ |= 4;
                            this.method_ = other.method_;
                            onChanged();
                        }
                        if (other.hasId()) {
                            setId(other.getId());
                        }
                        mergeUnknownFields(other.getUnknownFields());
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        return true;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        Instruction parsedMessage = null;
                        try {
                            try {
                                parsedMessage = Instruction.PARSER.parsePartialFrom(input, extensionRegistry);
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                return this;
                            } catch (InvalidProtocolBufferException e) {
                                Instruction instruction = (Instruction) e.getUnfinishedMessage();
                                throw e;
                            }
                        } catch (Throwable th) {
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            throw th;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public boolean hasStatement() {
                        return (this.bitField0_ & 1) == 1;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public String getStatement() {
                        Object ref = this.statement_;
                        if (!(ref instanceof String)) {
                            String s = ((ByteString) ref).toStringUtf8();
                            this.statement_ = s;
                            return s;
                        }
                        return (String) ref;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public ByteString getStatementBytes() {
                        Object ref = this.statement_;
                        if (ref instanceof String) {
                            ByteString b = ByteString.copyFromUtf8((String) ref);
                            this.statement_ = b;
                            return b;
                        }
                        return (ByteString) ref;
                    }

                    public Builder setStatement(String value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 1;
                        this.statement_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearStatement() {
                        this.bitField0_ &= -2;
                        this.statement_ = Instruction.getDefaultInstance().getStatement();
                        onChanged();
                        return this;
                    }

                    public Builder setStatementBytes(ByteString value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 1;
                        this.statement_ = value;
                        onChanged();
                        return this;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public boolean hasClassName() {
                        return (this.bitField0_ & 2) == 2;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public String getClassName() {
                        Object ref = this.className_;
                        if (!(ref instanceof String)) {
                            String s = ((ByteString) ref).toStringUtf8();
                            this.className_ = s;
                            return s;
                        }
                        return (String) ref;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public ByteString getClassNameBytes() {
                        Object ref = this.className_;
                        if (ref instanceof String) {
                            ByteString b = ByteString.copyFromUtf8((String) ref);
                            this.className_ = b;
                            return b;
                        }
                        return (ByteString) ref;
                    }

                    public Builder setClassName(String value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 2;
                        this.className_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearClassName() {
                        this.bitField0_ &= -3;
                        this.className_ = Instruction.getDefaultInstance().getClassName();
                        onChanged();
                        return this;
                    }

                    public Builder setClassNameBytes(ByteString value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 2;
                        this.className_ = value;
                        onChanged();
                        return this;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public boolean hasMethod() {
                        return (this.bitField0_ & 4) == 4;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public String getMethod() {
                        Object ref = this.method_;
                        if (!(ref instanceof String)) {
                            String s = ((ByteString) ref).toStringUtf8();
                            this.method_ = s;
                            return s;
                        }
                        return (String) ref;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public ByteString getMethodBytes() {
                        Object ref = this.method_;
                        if (ref instanceof String) {
                            ByteString b = ByteString.copyFromUtf8((String) ref);
                            this.method_ = b;
                            return b;
                        }
                        return (ByteString) ref;
                    }

                    public Builder setMethod(String value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 4;
                        this.method_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearMethod() {
                        this.bitField0_ &= -5;
                        this.method_ = Instruction.getDefaultInstance().getMethod();
                        onChanged();
                        return this;
                    }

                    public Builder setMethodBytes(ByteString value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 4;
                        this.method_ = value;
                        onChanged();
                        return this;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public boolean hasId() {
                        return (this.bitField0_ & 8) == 8;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.InstructionOrBuilder
                    public int getId() {
                        return this.id_;
                    }

                    public Builder setId(int value) {
                        this.bitField0_ |= 8;
                        this.id_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearId() {
                        this.bitField0_ &= -9;
                        this.id_ = 0;
                        onChanged();
                        return this;
                    }
                }
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint.class */
            public static final class ExitPoint extends GeneratedMessage implements ExitPointOrBuilder {
                private final UnknownFieldSet unknownFields;
                private int bitField0_;
                public static final int INSTRUCTION_FIELD_NUMBER = 1;
                private Instruction instruction_;
                public static final int KIND_FIELD_NUMBER = 2;
                private ComponentKind kind_;
                public static final int MISSING_FIELD_NUMBER = 3;
                private int missing_;
                public static final int INTENTS_FIELD_NUMBER = 4;
                private List<Intent> intents_;
                public static final int URIS_FIELD_NUMBER = 5;
                private List<Uri> uris_;
                private byte memoizedIsInitialized;
                private int memoizedSerializedSize;
                private static final long serialVersionUID = 0;
                public static Parser<ExitPoint> PARSER = new AbstractParser<ExitPoint>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.1
                    @Override // com.google.protobuf.Parser
                    public ExitPoint parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return new ExitPoint(input, extensionRegistry, null);
                    }
                };
                private static final ExitPoint defaultInstance = new ExitPoint(true);

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$IntentOrBuilder.class */
                public interface IntentOrBuilder extends MessageOrBuilder {
                    List<Attribute> getAttributesList();

                    Attribute getAttributes(int i);

                    int getAttributesCount();

                    List<? extends AttributeOrBuilder> getAttributesOrBuilderList();

                    AttributeOrBuilder getAttributesOrBuilder(int i);

                    boolean hasPermission();

                    String getPermission();

                    ByteString getPermissionBytes();
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$UriOrBuilder.class */
                public interface UriOrBuilder extends MessageOrBuilder {
                    List<Attribute> getAttributesList();

                    Attribute getAttributes(int i);

                    int getAttributesCount();

                    List<? extends AttributeOrBuilder> getAttributesOrBuilderList();

                    AttributeOrBuilder getAttributesOrBuilder(int i);
                }

                /* synthetic */ ExitPoint(GeneratedMessage.Builder builder, ExitPoint exitPoint) {
                    this(builder);
                }

                private ExitPoint(GeneratedMessage.Builder<?> builder) {
                    super(builder);
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = builder.getUnknownFields();
                }

                private ExitPoint(boolean noInit) {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    this.unknownFields = UnknownFieldSet.getDefaultInstance();
                }

                public static ExitPoint getDefaultInstance() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public ExitPoint getDefaultInstanceForType() {
                    return defaultInstance;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                public final UnknownFieldSet getUnknownFields() {
                    return this.unknownFields;
                }

                private ExitPoint(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    this.memoizedIsInitialized = (byte) -1;
                    this.memoizedSerializedSize = -1;
                    initFields();
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
                                        Instruction.Builder subBuilder = null;
                                        subBuilder = (this.bitField0_ & 1) == 1 ? this.instruction_.toBuilder() : subBuilder;
                                        this.instruction_ = (Instruction) input.readMessage(Instruction.PARSER, extensionRegistry);
                                        if (subBuilder != null) {
                                            subBuilder.mergeFrom(this.instruction_);
                                            this.instruction_ = subBuilder.buildPartial();
                                        }
                                        this.bitField0_ |= 1;
                                        break;
                                    case 16:
                                        int rawValue = input.readEnum();
                                        ComponentKind value = ComponentKind.valueOf(rawValue);
                                        if (value == null) {
                                            unknownFields.mergeVarintField(2, rawValue);
                                            break;
                                        } else {
                                            this.bitField0_ |= 2;
                                            this.kind_ = value;
                                            break;
                                        }
                                    case 24:
                                        this.bitField0_ |= 4;
                                        this.missing_ = input.readUInt32();
                                        break;
                                    case 34:
                                        if ((mutable_bitField0_ & 8) != 8) {
                                            this.intents_ = new ArrayList();
                                            mutable_bitField0_ |= 8;
                                        }
                                        this.intents_.add((Intent) input.readMessage(Intent.PARSER, extensionRegistry));
                                        break;
                                    case 42:
                                        if ((mutable_bitField0_ & 16) != 16) {
                                            this.uris_ = new ArrayList();
                                            mutable_bitField0_ |= 16;
                                        }
                                        this.uris_.add((Uri) input.readMessage(Uri.PARSER, extensionRegistry));
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
                                throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                            }
                        }
                        if ((mutable_bitField0_ & 8) == 8) {
                            this.intents_ = Collections.unmodifiableList(this.intents_);
                        }
                        if ((mutable_bitField0_ & 16) == 16) {
                            this.uris_ = Collections.unmodifiableList(this.uris_);
                        }
                        this.unknownFields = unknownFields.build();
                        makeExtensionsImmutable();
                    } catch (Throwable th) {
                        if ((mutable_bitField0_ & 8) == 8) {
                            this.intents_ = Collections.unmodifiableList(this.intents_);
                        }
                        if ((mutable_bitField0_ & 16) == 16) {
                            this.uris_ = Collections.unmodifiableList(this.uris_);
                        }
                        this.unknownFields = unknownFields.build();
                        makeExtensionsImmutable();
                        throw th;
                    }
                }

                /* synthetic */ ExitPoint(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, ExitPoint exitPoint) throws InvalidProtocolBufferException {
                    this(codedInputStream, extensionRegistryLite);
                }

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_fieldAccessorTable.ensureFieldAccessorsInitialized(ExitPoint.class, Builder.class);
                }

                static {
                    defaultInstance.initFields();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Parser<ExitPoint> getParserForType() {
                    return PARSER;
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$Intent.class */
                public static final class Intent extends GeneratedMessage implements IntentOrBuilder {
                    private final UnknownFieldSet unknownFields;
                    private int bitField0_;
                    public static final int ATTRIBUTES_FIELD_NUMBER = 1;
                    private List<Attribute> attributes_;
                    public static final int PERMISSION_FIELD_NUMBER = 2;
                    private Object permission_;
                    private byte memoizedIsInitialized;
                    private int memoizedSerializedSize;
                    private static final long serialVersionUID = 0;
                    public static Parser<Intent> PARSER = new AbstractParser<Intent>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.Intent.1
                        @Override // com.google.protobuf.Parser
                        public Intent parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                            return new Intent(input, extensionRegistry, null);
                        }
                    };
                    private static final Intent defaultInstance = new Intent(true);

                    /* synthetic */ Intent(GeneratedMessage.Builder builder, Intent intent) {
                        this(builder);
                    }

                    private Intent(GeneratedMessage.Builder<?> builder) {
                        super(builder);
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        this.unknownFields = builder.getUnknownFields();
                    }

                    private Intent(boolean noInit) {
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        this.unknownFields = UnknownFieldSet.getDefaultInstance();
                    }

                    public static Intent getDefaultInstance() {
                        return defaultInstance;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public Intent getDefaultInstanceForType() {
                        return defaultInstance;
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                    public final UnknownFieldSet getUnknownFields() {
                        return this.unknownFields;
                    }

                    private Intent(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        initFields();
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
                                                if ((mutable_bitField0_ & 1) != 1) {
                                                    this.attributes_ = new ArrayList();
                                                    mutable_bitField0_ |= 1;
                                                }
                                                this.attributes_.add((Attribute) input.readMessage(Attribute.PARSER, extensionRegistry));
                                                break;
                                            case 18:
                                                this.bitField0_ |= 1;
                                                this.permission_ = input.readBytes();
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
                                        throw new InvalidProtocolBufferException(e.getMessage()).setUnfinishedMessage(this);
                                    }
                                } catch (InvalidProtocolBufferException e2) {
                                    throw e2.setUnfinishedMessage(this);
                                }
                            }
                        } finally {
                            if ((mutable_bitField0_ & 1) == 1) {
                                this.attributes_ = Collections.unmodifiableList(this.attributes_);
                            }
                            this.unknownFields = unknownFields.build();
                            makeExtensionsImmutable();
                        }
                    }

                    /* synthetic */ Intent(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Intent intent) throws InvalidProtocolBufferException {
                        this(codedInputStream, extensionRegistryLite);
                    }

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_fieldAccessorTable.ensureFieldAccessorsInitialized(Intent.class, Builder.class);
                    }

                    static {
                        defaultInstance.initFields();
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Parser<Intent> getParserForType() {
                        return PARSER;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public List<Attribute> getAttributesList() {
                        return this.attributes_;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                        return this.attributes_;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public int getAttributesCount() {
                        return this.attributes_.size();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public Attribute getAttributes(int index) {
                        return this.attributes_.get(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public AttributeOrBuilder getAttributesOrBuilder(int index) {
                        return this.attributes_.get(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public boolean hasPermission() {
                        return (this.bitField0_ & 1) == 1;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public String getPermission() {
                        Object ref = this.permission_;
                        if (ref instanceof String) {
                            return (String) ref;
                        }
                        ByteString bs = (ByteString) ref;
                        String s = bs.toStringUtf8();
                        if (bs.isValidUtf8()) {
                            this.permission_ = s;
                        }
                        return s;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                    public ByteString getPermissionBytes() {
                        Object ref = this.permission_;
                        if (ref instanceof String) {
                            ByteString b = ByteString.copyFromUtf8((String) ref);
                            this.permission_ = b;
                            return b;
                        }
                        return (ByteString) ref;
                    }

                    private void initFields() {
                        this.attributes_ = Collections.emptyList();
                        this.permission_ = "";
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        byte isInitialized = this.memoizedIsInitialized;
                        if (isInitialized != -1) {
                            return isInitialized == 1;
                        }
                        this.memoizedIsInitialized = (byte) 1;
                        return true;
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                    public void writeTo(CodedOutputStream output) throws IOException {
                        getSerializedSize();
                        for (int i = 0; i < this.attributes_.size(); i++) {
                            output.writeMessage(1, this.attributes_.get(i));
                        }
                        if ((this.bitField0_ & 1) == 1) {
                            output.writeBytes(2, getPermissionBytes());
                        }
                        getUnknownFields().writeTo(output);
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                    public int getSerializedSize() {
                        int size = this.memoizedSerializedSize;
                        if (size != -1) {
                            return size;
                        }
                        int size2 = 0;
                        for (int i = 0; i < this.attributes_.size(); i++) {
                            size2 += CodedOutputStream.computeMessageSize(1, this.attributes_.get(i));
                        }
                        if ((this.bitField0_ & 1) == 1) {
                            size2 += CodedOutputStream.computeBytesSize(2, getPermissionBytes());
                        }
                        int size3 = size2 + getUnknownFields().getSerializedSize();
                        this.memoizedSerializedSize = size3;
                        return size3;
                    }

                    @Override // com.google.protobuf.GeneratedMessage
                    protected Object writeReplace() throws ObjectStreamException {
                        return super.writeReplace();
                    }

                    public static Intent parseFrom(ByteString data) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data);
                    }

                    public static Intent parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data, extensionRegistry);
                    }

                    public static Intent parseFrom(byte[] data) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data);
                    }

                    public static Intent parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data, extensionRegistry);
                    }

                    public static Intent parseFrom(InputStream input) throws IOException {
                        return PARSER.parseFrom(input);
                    }

                    public static Intent parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseFrom(input, extensionRegistry);
                    }

                    public static Intent parseDelimitedFrom(InputStream input) throws IOException {
                        return PARSER.parseDelimitedFrom(input);
                    }

                    public static Intent parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseDelimitedFrom(input, extensionRegistry);
                    }

                    public static Intent parseFrom(CodedInputStream input) throws IOException {
                        return PARSER.parseFrom(input);
                    }

                    public static Intent parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseFrom(input, extensionRegistry);
                    }

                    public static Builder newBuilder() {
                        return Builder.create();
                    }

                    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Builder newBuilderForType() {
                        return newBuilder();
                    }

                    public static Builder newBuilder(Intent prototype) {
                        return newBuilder().mergeFrom(prototype);
                    }

                    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Builder toBuilder() {
                        return newBuilder(this);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // com.google.protobuf.GeneratedMessage
                    public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                        Builder builder = new Builder(parent, null);
                        return builder;
                    }

                    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$Intent$Builder.class */
                    public static final class Builder extends GeneratedMessage.Builder<Builder> implements IntentOrBuilder {
                        private int bitField0_;
                        private List<Attribute> attributes_;
                        private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> attributesBuilder_;
                        private Object permission_;

                        public static final Descriptors.Descriptor getDescriptor() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder
                        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_fieldAccessorTable.ensureFieldAccessorsInitialized(Intent.class, Builder.class);
                        }

                        private Builder() {
                            this.attributes_ = Collections.emptyList();
                            this.permission_ = "";
                            maybeForceBuilderInitialization();
                        }

                        /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                            this(builderParent);
                        }

                        private Builder(GeneratedMessage.BuilderParent parent) {
                            super(parent);
                            this.attributes_ = Collections.emptyList();
                            this.permission_ = "";
                            maybeForceBuilderInitialization();
                        }

                        private void maybeForceBuilderInitialization() {
                            if (Intent.alwaysUseFieldBuilders) {
                                getAttributesFieldBuilder();
                            }
                        }

                        /* JADX INFO: Access modifiers changed from: private */
                        public static Builder create() {
                            return new Builder();
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder clear() {
                            super.clear();
                            if (this.attributesBuilder_ == null) {
                                this.attributes_ = Collections.emptyList();
                                this.bitField0_ &= -2;
                            } else {
                                this.attributesBuilder_.clear();
                            }
                            this.permission_ = "";
                            this.bitField0_ &= -3;
                            return this;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder clone() {
                            return create().mergeFrom(buildPartial());
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                        public Descriptors.Descriptor getDescriptorForType() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor;
                        }

                        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                        public Intent getDefaultInstanceForType() {
                            return Intent.getDefaultInstance();
                        }

                        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Intent build() {
                            Intent result = buildPartial();
                            if (!result.isInitialized()) {
                                throw newUninitializedMessageException((Message) result);
                            }
                            return result;
                        }

                        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Intent buildPartial() {
                            Intent result = new Intent(this, (Intent) null);
                            int from_bitField0_ = this.bitField0_;
                            int to_bitField0_ = 0;
                            if (this.attributesBuilder_ != null) {
                                result.attributes_ = this.attributesBuilder_.build();
                            } else {
                                if ((this.bitField0_ & 1) == 1) {
                                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                                    this.bitField0_ &= -2;
                                }
                                result.attributes_ = this.attributes_;
                            }
                            if ((from_bitField0_ & 2) == 2) {
                                to_bitField0_ = 0 | 1;
                            }
                            result.permission_ = this.permission_;
                            result.bitField0_ = to_bitField0_;
                            onBuilt();
                            return result;
                        }

                        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                        public Builder mergeFrom(Message other) {
                            if (other instanceof Intent) {
                                return mergeFrom((Intent) other);
                            }
                            super.mergeFrom(other);
                            return this;
                        }

                        public Builder mergeFrom(Intent other) {
                            RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> repeatedFieldBuilder;
                            if (other == Intent.getDefaultInstance()) {
                                return this;
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
                                    if (Intent.alwaysUseFieldBuilders) {
                                        repeatedFieldBuilder = getAttributesFieldBuilder();
                                    } else {
                                        repeatedFieldBuilder = null;
                                    }
                                    this.attributesBuilder_ = repeatedFieldBuilder;
                                }
                            }
                            if (other.hasPermission()) {
                                this.bitField0_ |= 2;
                                this.permission_ = other.permission_;
                                onChanged();
                            }
                            mergeUnknownFields(other.getUnknownFields());
                            return this;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                        public final boolean isInitialized() {
                            return true;
                        }

                        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                            Intent parsedMessage = null;
                            try {
                                try {
                                    parsedMessage = Intent.PARSER.parsePartialFrom(input, extensionRegistry);
                                    if (parsedMessage != null) {
                                        mergeFrom(parsedMessage);
                                    }
                                    return this;
                                } catch (InvalidProtocolBufferException e) {
                                    Intent intent = (Intent) e.getUnfinishedMessage();
                                    throw e;
                                }
                            } catch (Throwable th) {
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                throw th;
                            }
                        }

                        private void ensureAttributesIsMutable() {
                            if ((this.bitField0_ & 1) != 1) {
                                this.attributes_ = new ArrayList(this.attributes_);
                                this.bitField0_ |= 1;
                            }
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public List<Attribute> getAttributesList() {
                            if (this.attributesBuilder_ == null) {
                                return Collections.unmodifiableList(this.attributes_);
                            }
                            return this.attributesBuilder_.getMessageList();
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public int getAttributesCount() {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.size();
                            }
                            return this.attributesBuilder_.getCount();
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public Attribute getAttributes(int index) {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.get(index);
                            }
                            return this.attributesBuilder_.getMessage(index);
                        }

                        public Builder setAttributes(int index, Attribute value) {
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

                        public Builder setAttributes(int index, Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.set(index, builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.setMessage(index, builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAttributes(Attribute value) {
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

                        public Builder addAttributes(int index, Attribute value) {
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

                        public Builder addAttributes(Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.add(builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.addMessage(builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAttributes(int index, Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.add(index, builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.addMessage(index, builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAllAttributes(Iterable<? extends Attribute> values) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                GeneratedMessage.Builder.addAll((Iterable) values, (List) this.attributes_);
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

                        public Attribute.Builder getAttributesBuilder(int index) {
                            return getAttributesFieldBuilder().getBuilder(index);
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public AttributeOrBuilder getAttributesOrBuilder(int index) {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.get(index);
                            }
                            return this.attributesBuilder_.getMessageOrBuilder(index);
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                            if (this.attributesBuilder_ != null) {
                                return this.attributesBuilder_.getMessageOrBuilderList();
                            }
                            return Collections.unmodifiableList(this.attributes_);
                        }

                        public Attribute.Builder addAttributesBuilder() {
                            return getAttributesFieldBuilder().addBuilder(Attribute.getDefaultInstance());
                        }

                        public Attribute.Builder addAttributesBuilder(int index) {
                            return getAttributesFieldBuilder().addBuilder(index, Attribute.getDefaultInstance());
                        }

                        public List<Attribute.Builder> getAttributesBuilderList() {
                            return getAttributesFieldBuilder().getBuilderList();
                        }

                        private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> getAttributesFieldBuilder() {
                            if (this.attributesBuilder_ == null) {
                                this.attributesBuilder_ = new RepeatedFieldBuilder<>(this.attributes_, (this.bitField0_ & 1) == 1, getParentForChildren(), isClean());
                                this.attributes_ = null;
                            }
                            return this.attributesBuilder_;
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public boolean hasPermission() {
                            return (this.bitField0_ & 2) == 2;
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public String getPermission() {
                            Object ref = this.permission_;
                            if (!(ref instanceof String)) {
                                String s = ((ByteString) ref).toStringUtf8();
                                this.permission_ = s;
                                return s;
                            }
                            return (String) ref;
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.IntentOrBuilder
                        public ByteString getPermissionBytes() {
                            Object ref = this.permission_;
                            if (ref instanceof String) {
                                ByteString b = ByteString.copyFromUtf8((String) ref);
                                this.permission_ = b;
                                return b;
                            }
                            return (ByteString) ref;
                        }

                        public Builder setPermission(String value) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            this.bitField0_ |= 2;
                            this.permission_ = value;
                            onChanged();
                            return this;
                        }

                        public Builder clearPermission() {
                            this.bitField0_ &= -3;
                            this.permission_ = Intent.getDefaultInstance().getPermission();
                            onChanged();
                            return this;
                        }

                        public Builder setPermissionBytes(ByteString value) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            this.bitField0_ |= 2;
                            this.permission_ = value;
                            onChanged();
                            return this;
                        }
                    }
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$Uri.class */
                public static final class Uri extends GeneratedMessage implements UriOrBuilder {
                    private final UnknownFieldSet unknownFields;
                    public static final int ATTRIBUTES_FIELD_NUMBER = 1;
                    private List<Attribute> attributes_;
                    private byte memoizedIsInitialized;
                    private int memoizedSerializedSize;
                    private static final long serialVersionUID = 0;
                    public static Parser<Uri> PARSER = new AbstractParser<Uri>() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.Uri.1
                        @Override // com.google.protobuf.Parser
                        public Uri parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                            return new Uri(input, extensionRegistry, null);
                        }
                    };
                    private static final Uri defaultInstance = new Uri(true);

                    /* synthetic */ Uri(GeneratedMessage.Builder builder, Uri uri) {
                        this(builder);
                    }

                    private Uri(GeneratedMessage.Builder<?> builder) {
                        super(builder);
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        this.unknownFields = builder.getUnknownFields();
                    }

                    private Uri(boolean noInit) {
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        this.unknownFields = UnknownFieldSet.getDefaultInstance();
                    }

                    public static Uri getDefaultInstance() {
                        return defaultInstance;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public Uri getDefaultInstanceForType() {
                        return defaultInstance;
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageOrBuilder
                    public final UnknownFieldSet getUnknownFields() {
                        return this.unknownFields;
                    }

                    private Uri(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        this.memoizedIsInitialized = (byte) -1;
                        this.memoizedSerializedSize = -1;
                        initFields();
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
                                            if ((mutable_bitField0_ & 1) != 1) {
                                                this.attributes_ = new ArrayList();
                                                mutable_bitField0_ |= 1;
                                            }
                                            this.attributes_.add((Attribute) input.readMessage(Attribute.PARSER, extensionRegistry));
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
                                    throw new InvalidProtocolBufferException(e2.getMessage()).setUnfinishedMessage(this);
                                }
                            }
                        } finally {
                            if ((mutable_bitField0_ & 1) == 1) {
                                this.attributes_ = Collections.unmodifiableList(this.attributes_);
                            }
                            this.unknownFields = unknownFields.build();
                            makeExtensionsImmutable();
                        }
                    }

                    /* synthetic */ Uri(CodedInputStream codedInputStream, ExtensionRegistryLite extensionRegistryLite, Uri uri) throws InvalidProtocolBufferException {
                        this(codedInputStream, extensionRegistryLite);
                    }

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_fieldAccessorTable.ensureFieldAccessorsInitialized(Uri.class, Builder.class);
                    }

                    static {
                        defaultInstance.initFields();
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Parser<Uri> getParserForType() {
                        return PARSER;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                    public List<Attribute> getAttributesList() {
                        return this.attributes_;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                    public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                        return this.attributes_;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                    public int getAttributesCount() {
                        return this.attributes_.size();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                    public Attribute getAttributes(int index) {
                        return this.attributes_.get(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                    public AttributeOrBuilder getAttributesOrBuilder(int index) {
                        return this.attributes_.get(index);
                    }

                    private void initFields() {
                        this.attributes_ = Collections.emptyList();
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        byte isInitialized = this.memoizedIsInitialized;
                        if (isInitialized != -1) {
                            return isInitialized == 1;
                        }
                        this.memoizedIsInitialized = (byte) 1;
                        return true;
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                    public void writeTo(CodedOutputStream output) throws IOException {
                        getSerializedSize();
                        for (int i = 0; i < this.attributes_.size(); i++) {
                            output.writeMessage(1, this.attributes_.get(i));
                        }
                        getUnknownFields().writeTo(output);
                    }

                    @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                    public int getSerializedSize() {
                        int size = this.memoizedSerializedSize;
                        if (size != -1) {
                            return size;
                        }
                        int size2 = 0;
                        for (int i = 0; i < this.attributes_.size(); i++) {
                            size2 += CodedOutputStream.computeMessageSize(1, this.attributes_.get(i));
                        }
                        int size3 = size2 + getUnknownFields().getSerializedSize();
                        this.memoizedSerializedSize = size3;
                        return size3;
                    }

                    @Override // com.google.protobuf.GeneratedMessage
                    protected Object writeReplace() throws ObjectStreamException {
                        return super.writeReplace();
                    }

                    public static Uri parseFrom(ByteString data) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data);
                    }

                    public static Uri parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data, extensionRegistry);
                    }

                    public static Uri parseFrom(byte[] data) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data);
                    }

                    public static Uri parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                        return PARSER.parseFrom(data, extensionRegistry);
                    }

                    public static Uri parseFrom(InputStream input) throws IOException {
                        return PARSER.parseFrom(input);
                    }

                    public static Uri parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseFrom(input, extensionRegistry);
                    }

                    public static Uri parseDelimitedFrom(InputStream input) throws IOException {
                        return PARSER.parseDelimitedFrom(input);
                    }

                    public static Uri parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseDelimitedFrom(input, extensionRegistry);
                    }

                    public static Uri parseFrom(CodedInputStream input) throws IOException {
                        return PARSER.parseFrom(input);
                    }

                    public static Uri parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        return PARSER.parseFrom(input, extensionRegistry);
                    }

                    public static Builder newBuilder() {
                        return Builder.create();
                    }

                    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Builder newBuilderForType() {
                        return newBuilder();
                    }

                    public static Builder newBuilder(Uri prototype) {
                        return newBuilder().mergeFrom(prototype);
                    }

                    @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                    public Builder toBuilder() {
                        return newBuilder(this);
                    }

                    /* JADX INFO: Access modifiers changed from: protected */
                    @Override // com.google.protobuf.GeneratedMessage
                    public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                        Builder builder = new Builder(parent, null);
                        return builder;
                    }

                    /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$Uri$Builder.class */
                    public static final class Builder extends GeneratedMessage.Builder<Builder> implements UriOrBuilder {
                        private int bitField0_;
                        private List<Attribute> attributes_;
                        private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> attributesBuilder_;

                        public static final Descriptors.Descriptor getDescriptor() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder
                        protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_fieldAccessorTable.ensureFieldAccessorsInitialized(Uri.class, Builder.class);
                        }

                        private Builder() {
                            this.attributes_ = Collections.emptyList();
                            maybeForceBuilderInitialization();
                        }

                        /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                            this(builderParent);
                        }

                        private Builder(GeneratedMessage.BuilderParent parent) {
                            super(parent);
                            this.attributes_ = Collections.emptyList();
                            maybeForceBuilderInitialization();
                        }

                        private void maybeForceBuilderInitialization() {
                            if (Uri.alwaysUseFieldBuilders) {
                                getAttributesFieldBuilder();
                            }
                        }

                        /* JADX INFO: Access modifiers changed from: private */
                        public static Builder create() {
                            return new Builder();
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder clear() {
                            super.clear();
                            if (this.attributesBuilder_ == null) {
                                this.attributes_ = Collections.emptyList();
                                this.bitField0_ &= -2;
                            } else {
                                this.attributesBuilder_.clear();
                            }
                            return this;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder clone() {
                            return create().mergeFrom(buildPartial());
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                        public Descriptors.Descriptor getDescriptorForType() {
                            return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor;
                        }

                        @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                        public Uri getDefaultInstanceForType() {
                            return Uri.getDefaultInstance();
                        }

                        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Uri build() {
                            Uri result = buildPartial();
                            if (!result.isInitialized()) {
                                throw newUninitializedMessageException((Message) result);
                            }
                            return result;
                        }

                        @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Uri buildPartial() {
                            Uri result = new Uri(this, (Uri) null);
                            int i = this.bitField0_;
                            if (this.attributesBuilder_ != null) {
                                result.attributes_ = this.attributesBuilder_.build();
                            } else {
                                if ((this.bitField0_ & 1) == 1) {
                                    this.attributes_ = Collections.unmodifiableList(this.attributes_);
                                    this.bitField0_ &= -2;
                                }
                                result.attributes_ = this.attributes_;
                            }
                            onBuilt();
                            return result;
                        }

                        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                        public Builder mergeFrom(Message other) {
                            if (other instanceof Uri) {
                                return mergeFrom((Uri) other);
                            }
                            super.mergeFrom(other);
                            return this;
                        }

                        public Builder mergeFrom(Uri other) {
                            RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> repeatedFieldBuilder;
                            if (other == Uri.getDefaultInstance()) {
                                return this;
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
                                    if (Uri.alwaysUseFieldBuilders) {
                                        repeatedFieldBuilder = getAttributesFieldBuilder();
                                    } else {
                                        repeatedFieldBuilder = null;
                                    }
                                    this.attributesBuilder_ = repeatedFieldBuilder;
                                }
                            }
                            mergeUnknownFields(other.getUnknownFields());
                            return this;
                        }

                        @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                        public final boolean isInitialized() {
                            return true;
                        }

                        @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                        public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                            Uri parsedMessage = null;
                            try {
                                try {
                                    parsedMessage = Uri.PARSER.parsePartialFrom(input, extensionRegistry);
                                    if (parsedMessage != null) {
                                        mergeFrom(parsedMessage);
                                    }
                                    return this;
                                } catch (InvalidProtocolBufferException e) {
                                    Uri uri = (Uri) e.getUnfinishedMessage();
                                    throw e;
                                }
                            } catch (Throwable th) {
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                throw th;
                            }
                        }

                        private void ensureAttributesIsMutable() {
                            if ((this.bitField0_ & 1) != 1) {
                                this.attributes_ = new ArrayList(this.attributes_);
                                this.bitField0_ |= 1;
                            }
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                        public List<Attribute> getAttributesList() {
                            if (this.attributesBuilder_ == null) {
                                return Collections.unmodifiableList(this.attributes_);
                            }
                            return this.attributesBuilder_.getMessageList();
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                        public int getAttributesCount() {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.size();
                            }
                            return this.attributesBuilder_.getCount();
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                        public Attribute getAttributes(int index) {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.get(index);
                            }
                            return this.attributesBuilder_.getMessage(index);
                        }

                        public Builder setAttributes(int index, Attribute value) {
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

                        public Builder setAttributes(int index, Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.set(index, builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.setMessage(index, builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAttributes(Attribute value) {
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

                        public Builder addAttributes(int index, Attribute value) {
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

                        public Builder addAttributes(Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.add(builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.addMessage(builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAttributes(int index, Attribute.Builder builderForValue) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                this.attributes_.add(index, builderForValue.build());
                                onChanged();
                            } else {
                                this.attributesBuilder_.addMessage(index, builderForValue.build());
                            }
                            return this;
                        }

                        public Builder addAllAttributes(Iterable<? extends Attribute> values) {
                            if (this.attributesBuilder_ == null) {
                                ensureAttributesIsMutable();
                                GeneratedMessage.Builder.addAll((Iterable) values, (List) this.attributes_);
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

                        public Attribute.Builder getAttributesBuilder(int index) {
                            return getAttributesFieldBuilder().getBuilder(index);
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                        public AttributeOrBuilder getAttributesOrBuilder(int index) {
                            if (this.attributesBuilder_ == null) {
                                return this.attributes_.get(index);
                            }
                            return this.attributesBuilder_.getMessageOrBuilder(index);
                        }

                        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPoint.UriOrBuilder
                        public List<? extends AttributeOrBuilder> getAttributesOrBuilderList() {
                            if (this.attributesBuilder_ != null) {
                                return this.attributesBuilder_.getMessageOrBuilderList();
                            }
                            return Collections.unmodifiableList(this.attributes_);
                        }

                        public Attribute.Builder addAttributesBuilder() {
                            return getAttributesFieldBuilder().addBuilder(Attribute.getDefaultInstance());
                        }

                        public Attribute.Builder addAttributesBuilder(int index) {
                            return getAttributesFieldBuilder().addBuilder(index, Attribute.getDefaultInstance());
                        }

                        public List<Attribute.Builder> getAttributesBuilderList() {
                            return getAttributesFieldBuilder().getBuilderList();
                        }

                        private RepeatedFieldBuilder<Attribute, Attribute.Builder, AttributeOrBuilder> getAttributesFieldBuilder() {
                            if (this.attributesBuilder_ == null) {
                                this.attributesBuilder_ = new RepeatedFieldBuilder<>(this.attributes_, (this.bitField0_ & 1) == 1, getParentForChildren(), isClean());
                                this.attributes_ = null;
                            }
                            return this.attributesBuilder_;
                        }
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public boolean hasInstruction() {
                    return (this.bitField0_ & 1) == 1;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public Instruction getInstruction() {
                    return this.instruction_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public InstructionOrBuilder getInstructionOrBuilder() {
                    return this.instruction_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public boolean hasKind() {
                    return (this.bitField0_ & 2) == 2;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public ComponentKind getKind() {
                    return this.kind_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public boolean hasMissing() {
                    return (this.bitField0_ & 4) == 4;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public int getMissing() {
                    return this.missing_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public List<Intent> getIntentsList() {
                    return this.intents_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public List<? extends IntentOrBuilder> getIntentsOrBuilderList() {
                    return this.intents_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public int getIntentsCount() {
                    return this.intents_.size();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public Intent getIntents(int index) {
                    return this.intents_.get(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public IntentOrBuilder getIntentsOrBuilder(int index) {
                    return this.intents_.get(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public List<Uri> getUrisList() {
                    return this.uris_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public List<? extends UriOrBuilder> getUrisOrBuilderList() {
                    return this.uris_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public int getUrisCount() {
                    return this.uris_.size();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public Uri getUris(int index) {
                    return this.uris_.get(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                public UriOrBuilder getUrisOrBuilder(int index) {
                    return this.uris_.get(index);
                }

                private void initFields() {
                    this.instruction_ = Instruction.getDefaultInstance();
                    this.kind_ = ComponentKind.ACTIVITY;
                    this.missing_ = 0;
                    this.intents_ = Collections.emptyList();
                    this.uris_ = Collections.emptyList();
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    byte isInitialized = this.memoizedIsInitialized;
                    if (isInitialized != -1) {
                        return isInitialized == 1;
                    }
                    this.memoizedIsInitialized = (byte) 1;
                    return true;
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public void writeTo(CodedOutputStream output) throws IOException {
                    getSerializedSize();
                    if ((this.bitField0_ & 1) == 1) {
                        output.writeMessage(1, this.instruction_);
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        output.writeEnum(2, this.kind_.getNumber());
                    }
                    if ((this.bitField0_ & 4) == 4) {
                        output.writeUInt32(3, this.missing_);
                    }
                    for (int i = 0; i < this.intents_.size(); i++) {
                        output.writeMessage(4, this.intents_.get(i));
                    }
                    for (int i2 = 0; i2 < this.uris_.size(); i2++) {
                        output.writeMessage(5, this.uris_.get(i2));
                    }
                    getUnknownFields().writeTo(output);
                }

                @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
                public int getSerializedSize() {
                    int size = this.memoizedSerializedSize;
                    if (size != -1) {
                        return size;
                    }
                    int size2 = 0;
                    if ((this.bitField0_ & 1) == 1) {
                        size2 = 0 + CodedOutputStream.computeMessageSize(1, this.instruction_);
                    }
                    if ((this.bitField0_ & 2) == 2) {
                        size2 += CodedOutputStream.computeEnumSize(2, this.kind_.getNumber());
                    }
                    if ((this.bitField0_ & 4) == 4) {
                        size2 += CodedOutputStream.computeUInt32Size(3, this.missing_);
                    }
                    for (int i = 0; i < this.intents_.size(); i++) {
                        size2 += CodedOutputStream.computeMessageSize(4, this.intents_.get(i));
                    }
                    for (int i2 = 0; i2 < this.uris_.size(); i2++) {
                        size2 += CodedOutputStream.computeMessageSize(5, this.uris_.get(i2));
                    }
                    int size3 = size2 + getUnknownFields().getSerializedSize();
                    this.memoizedSerializedSize = size3;
                    return size3;
                }

                @Override // com.google.protobuf.GeneratedMessage
                protected Object writeReplace() throws ObjectStreamException {
                    return super.writeReplace();
                }

                public static ExitPoint parseFrom(ByteString data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static ExitPoint parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static ExitPoint parseFrom(byte[] data) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data);
                }

                public static ExitPoint parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                    return PARSER.parseFrom(data, extensionRegistry);
                }

                public static ExitPoint parseFrom(InputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static ExitPoint parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static ExitPoint parseDelimitedFrom(InputStream input) throws IOException {
                    return PARSER.parseDelimitedFrom(input);
                }

                public static ExitPoint parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseDelimitedFrom(input, extensionRegistry);
                }

                public static ExitPoint parseFrom(CodedInputStream input) throws IOException {
                    return PARSER.parseFrom(input);
                }

                public static ExitPoint parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    return PARSER.parseFrom(input, extensionRegistry);
                }

                public static Builder newBuilder() {
                    return Builder.create();
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder newBuilderForType() {
                    return newBuilder();
                }

                public static Builder newBuilder(ExitPoint prototype) {
                    return newBuilder().mergeFrom(prototype);
                }

                @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
                public Builder toBuilder() {
                    return newBuilder(this);
                }

                /* JADX INFO: Access modifiers changed from: protected */
                @Override // com.google.protobuf.GeneratedMessage
                public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                    Builder builder = new Builder(parent, null);
                    return builder;
                }

                /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$ExitPoint$Builder.class */
                public static final class Builder extends GeneratedMessage.Builder<Builder> implements ExitPointOrBuilder {
                    private int bitField0_;
                    private Instruction instruction_;
                    private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> instructionBuilder_;
                    private ComponentKind kind_;
                    private int missing_;
                    private List<Intent> intents_;
                    private RepeatedFieldBuilder<Intent, Intent.Builder, IntentOrBuilder> intentsBuilder_;
                    private List<Uri> uris_;
                    private RepeatedFieldBuilder<Uri, Uri.Builder, UriOrBuilder> urisBuilder_;

                    public static final Descriptors.Descriptor getDescriptor() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder
                    protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_fieldAccessorTable.ensureFieldAccessorsInitialized(ExitPoint.class, Builder.class);
                    }

                    private Builder() {
                        this.instruction_ = Instruction.getDefaultInstance();
                        this.kind_ = ComponentKind.ACTIVITY;
                        this.intents_ = Collections.emptyList();
                        this.uris_ = Collections.emptyList();
                        maybeForceBuilderInitialization();
                    }

                    /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                        this(builderParent);
                    }

                    private Builder(GeneratedMessage.BuilderParent parent) {
                        super(parent);
                        this.instruction_ = Instruction.getDefaultInstance();
                        this.kind_ = ComponentKind.ACTIVITY;
                        this.intents_ = Collections.emptyList();
                        this.uris_ = Collections.emptyList();
                        maybeForceBuilderInitialization();
                    }

                    private void maybeForceBuilderInitialization() {
                        if (ExitPoint.alwaysUseFieldBuilders) {
                            getInstructionFieldBuilder();
                            getIntentsFieldBuilder();
                            getUrisFieldBuilder();
                        }
                    }

                    /* JADX INFO: Access modifiers changed from: private */
                    public static Builder create() {
                        return new Builder();
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clear() {
                        super.clear();
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = Instruction.getDefaultInstance();
                        } else {
                            this.instructionBuilder_.clear();
                        }
                        this.bitField0_ &= -2;
                        this.kind_ = ComponentKind.ACTIVITY;
                        this.bitField0_ &= -3;
                        this.missing_ = 0;
                        this.bitField0_ &= -5;
                        if (this.intentsBuilder_ == null) {
                            this.intents_ = Collections.emptyList();
                            this.bitField0_ &= -9;
                        } else {
                            this.intentsBuilder_.clear();
                        }
                        if (this.urisBuilder_ == null) {
                            this.uris_ = Collections.emptyList();
                            this.bitField0_ &= -17;
                        } else {
                            this.urisBuilder_.clear();
                        }
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder clone() {
                        return create().mergeFrom(buildPartial());
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                    public Descriptors.Descriptor getDescriptorForType() {
                        return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor;
                    }

                    @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                    public ExitPoint getDefaultInstanceForType() {
                        return ExitPoint.getDefaultInstance();
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public ExitPoint build() {
                        ExitPoint result = buildPartial();
                        if (!result.isInitialized()) {
                            throw newUninitializedMessageException((Message) result);
                        }
                        return result;
                    }

                    @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public ExitPoint buildPartial() {
                        ExitPoint result = new ExitPoint(this, (ExitPoint) null);
                        int from_bitField0_ = this.bitField0_;
                        int to_bitField0_ = 0;
                        if ((from_bitField0_ & 1) == 1) {
                            to_bitField0_ = 0 | 1;
                        }
                        if (this.instructionBuilder_ == null) {
                            result.instruction_ = this.instruction_;
                        } else {
                            result.instruction_ = this.instructionBuilder_.build();
                        }
                        if ((from_bitField0_ & 2) == 2) {
                            to_bitField0_ |= 2;
                        }
                        result.kind_ = this.kind_;
                        if ((from_bitField0_ & 4) == 4) {
                            to_bitField0_ |= 4;
                        }
                        result.missing_ = this.missing_;
                        if (this.intentsBuilder_ != null) {
                            result.intents_ = this.intentsBuilder_.build();
                        } else {
                            if ((this.bitField0_ & 8) == 8) {
                                this.intents_ = Collections.unmodifiableList(this.intents_);
                                this.bitField0_ &= -9;
                            }
                            result.intents_ = this.intents_;
                        }
                        if (this.urisBuilder_ != null) {
                            result.uris_ = this.urisBuilder_.build();
                        } else {
                            if ((this.bitField0_ & 16) == 16) {
                                this.uris_ = Collections.unmodifiableList(this.uris_);
                                this.bitField0_ &= -17;
                            }
                            result.uris_ = this.uris_;
                        }
                        result.bitField0_ = to_bitField0_;
                        onBuilt();
                        return result;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(Message other) {
                        if (other instanceof ExitPoint) {
                            return mergeFrom((ExitPoint) other);
                        }
                        super.mergeFrom(other);
                        return this;
                    }

                    public Builder mergeFrom(ExitPoint other) {
                        RepeatedFieldBuilder<Intent, Intent.Builder, IntentOrBuilder> repeatedFieldBuilder;
                        RepeatedFieldBuilder<Uri, Uri.Builder, UriOrBuilder> repeatedFieldBuilder2;
                        if (other == ExitPoint.getDefaultInstance()) {
                            return this;
                        }
                        if (other.hasInstruction()) {
                            mergeInstruction(other.getInstruction());
                        }
                        if (other.hasKind()) {
                            setKind(other.getKind());
                        }
                        if (other.hasMissing()) {
                            setMissing(other.getMissing());
                        }
                        if (this.intentsBuilder_ == null) {
                            if (!other.intents_.isEmpty()) {
                                if (this.intents_.isEmpty()) {
                                    this.intents_ = other.intents_;
                                    this.bitField0_ &= -9;
                                } else {
                                    ensureIntentsIsMutable();
                                    this.intents_.addAll(other.intents_);
                                }
                                onChanged();
                            }
                        } else if (!other.intents_.isEmpty()) {
                            if (!this.intentsBuilder_.isEmpty()) {
                                this.intentsBuilder_.addAllMessages(other.intents_);
                            } else {
                                this.intentsBuilder_.dispose();
                                this.intentsBuilder_ = null;
                                this.intents_ = other.intents_;
                                this.bitField0_ &= -9;
                                if (ExitPoint.alwaysUseFieldBuilders) {
                                    repeatedFieldBuilder = getIntentsFieldBuilder();
                                } else {
                                    repeatedFieldBuilder = null;
                                }
                                this.intentsBuilder_ = repeatedFieldBuilder;
                            }
                        }
                        if (this.urisBuilder_ == null) {
                            if (!other.uris_.isEmpty()) {
                                if (this.uris_.isEmpty()) {
                                    this.uris_ = other.uris_;
                                    this.bitField0_ &= -17;
                                } else {
                                    ensureUrisIsMutable();
                                    this.uris_.addAll(other.uris_);
                                }
                                onChanged();
                            }
                        } else if (!other.uris_.isEmpty()) {
                            if (!this.urisBuilder_.isEmpty()) {
                                this.urisBuilder_.addAllMessages(other.uris_);
                            } else {
                                this.urisBuilder_.dispose();
                                this.urisBuilder_ = null;
                                this.uris_ = other.uris_;
                                this.bitField0_ &= -17;
                                if (ExitPoint.alwaysUseFieldBuilders) {
                                    repeatedFieldBuilder2 = getUrisFieldBuilder();
                                } else {
                                    repeatedFieldBuilder2 = null;
                                }
                                this.urisBuilder_ = repeatedFieldBuilder2;
                            }
                        }
                        mergeUnknownFields(other.getUnknownFields());
                        return this;
                    }

                    @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                    public final boolean isInitialized() {
                        return true;
                    }

                    @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                    public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                        ExitPoint parsedMessage = null;
                        try {
                            try {
                                parsedMessage = ExitPoint.PARSER.parsePartialFrom(input, extensionRegistry);
                                if (parsedMessage != null) {
                                    mergeFrom(parsedMessage);
                                }
                                return this;
                            } catch (InvalidProtocolBufferException e) {
                                ExitPoint exitPoint = (ExitPoint) e.getUnfinishedMessage();
                                throw e;
                            }
                        } catch (Throwable th) {
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            throw th;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public boolean hasInstruction() {
                        return (this.bitField0_ & 1) == 1;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public Instruction getInstruction() {
                        if (this.instructionBuilder_ == null) {
                            return this.instruction_;
                        }
                        return this.instructionBuilder_.getMessage();
                    }

                    public Builder setInstruction(Instruction value) {
                        if (this.instructionBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            this.instruction_ = value;
                            onChanged();
                        } else {
                            this.instructionBuilder_.setMessage(value);
                        }
                        this.bitField0_ |= 1;
                        return this;
                    }

                    public Builder setInstruction(Instruction.Builder builderForValue) {
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = builderForValue.build();
                            onChanged();
                        } else {
                            this.instructionBuilder_.setMessage(builderForValue.build());
                        }
                        this.bitField0_ |= 1;
                        return this;
                    }

                    public Builder mergeInstruction(Instruction value) {
                        if (this.instructionBuilder_ == null) {
                            if ((this.bitField0_ & 1) == 1 && this.instruction_ != Instruction.getDefaultInstance()) {
                                this.instruction_ = Instruction.newBuilder(this.instruction_).mergeFrom(value).buildPartial();
                            } else {
                                this.instruction_ = value;
                            }
                            onChanged();
                        } else {
                            this.instructionBuilder_.mergeFrom(value);
                        }
                        this.bitField0_ |= 1;
                        return this;
                    }

                    public Builder clearInstruction() {
                        if (this.instructionBuilder_ == null) {
                            this.instruction_ = Instruction.getDefaultInstance();
                            onChanged();
                        } else {
                            this.instructionBuilder_.clear();
                        }
                        this.bitField0_ &= -2;
                        return this;
                    }

                    public Instruction.Builder getInstructionBuilder() {
                        this.bitField0_ |= 1;
                        onChanged();
                        return getInstructionFieldBuilder().getBuilder();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public InstructionOrBuilder getInstructionOrBuilder() {
                        if (this.instructionBuilder_ != null) {
                            return this.instructionBuilder_.getMessageOrBuilder();
                        }
                        return this.instruction_;
                    }

                    private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> getInstructionFieldBuilder() {
                        if (this.instructionBuilder_ == null) {
                            this.instructionBuilder_ = new SingleFieldBuilder<>(this.instruction_, getParentForChildren(), isClean());
                            this.instruction_ = null;
                        }
                        return this.instructionBuilder_;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public boolean hasKind() {
                        return (this.bitField0_ & 2) == 2;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public ComponentKind getKind() {
                        return this.kind_;
                    }

                    public Builder setKind(ComponentKind value) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.bitField0_ |= 2;
                        this.kind_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearKind() {
                        this.bitField0_ &= -3;
                        this.kind_ = ComponentKind.ACTIVITY;
                        onChanged();
                        return this;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public boolean hasMissing() {
                        return (this.bitField0_ & 4) == 4;
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public int getMissing() {
                        return this.missing_;
                    }

                    public Builder setMissing(int value) {
                        this.bitField0_ |= 4;
                        this.missing_ = value;
                        onChanged();
                        return this;
                    }

                    public Builder clearMissing() {
                        this.bitField0_ &= -5;
                        this.missing_ = 0;
                        onChanged();
                        return this;
                    }

                    private void ensureIntentsIsMutable() {
                        if ((this.bitField0_ & 8) != 8) {
                            this.intents_ = new ArrayList(this.intents_);
                            this.bitField0_ |= 8;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public List<Intent> getIntentsList() {
                        if (this.intentsBuilder_ == null) {
                            return Collections.unmodifiableList(this.intents_);
                        }
                        return this.intentsBuilder_.getMessageList();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public int getIntentsCount() {
                        if (this.intentsBuilder_ == null) {
                            return this.intents_.size();
                        }
                        return this.intentsBuilder_.getCount();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public Intent getIntents(int index) {
                        if (this.intentsBuilder_ == null) {
                            return this.intents_.get(index);
                        }
                        return this.intentsBuilder_.getMessage(index);
                    }

                    public Builder setIntents(int index, Intent value) {
                        if (this.intentsBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureIntentsIsMutable();
                            this.intents_.set(index, value);
                            onChanged();
                        } else {
                            this.intentsBuilder_.setMessage(index, value);
                        }
                        return this;
                    }

                    public Builder setIntents(int index, Intent.Builder builderForValue) {
                        if (this.intentsBuilder_ == null) {
                            ensureIntentsIsMutable();
                            this.intents_.set(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.intentsBuilder_.setMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addIntents(Intent value) {
                        if (this.intentsBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureIntentsIsMutable();
                            this.intents_.add(value);
                            onChanged();
                        } else {
                            this.intentsBuilder_.addMessage(value);
                        }
                        return this;
                    }

                    public Builder addIntents(int index, Intent value) {
                        if (this.intentsBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureIntentsIsMutable();
                            this.intents_.add(index, value);
                            onChanged();
                        } else {
                            this.intentsBuilder_.addMessage(index, value);
                        }
                        return this;
                    }

                    public Builder addIntents(Intent.Builder builderForValue) {
                        if (this.intentsBuilder_ == null) {
                            ensureIntentsIsMutable();
                            this.intents_.add(builderForValue.build());
                            onChanged();
                        } else {
                            this.intentsBuilder_.addMessage(builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addIntents(int index, Intent.Builder builderForValue) {
                        if (this.intentsBuilder_ == null) {
                            ensureIntentsIsMutable();
                            this.intents_.add(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.intentsBuilder_.addMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addAllIntents(Iterable<? extends Intent> values) {
                        if (this.intentsBuilder_ == null) {
                            ensureIntentsIsMutable();
                            GeneratedMessage.Builder.addAll((Iterable) values, (List) this.intents_);
                            onChanged();
                        } else {
                            this.intentsBuilder_.addAllMessages(values);
                        }
                        return this;
                    }

                    public Builder clearIntents() {
                        if (this.intentsBuilder_ == null) {
                            this.intents_ = Collections.emptyList();
                            this.bitField0_ &= -9;
                            onChanged();
                        } else {
                            this.intentsBuilder_.clear();
                        }
                        return this;
                    }

                    public Builder removeIntents(int index) {
                        if (this.intentsBuilder_ == null) {
                            ensureIntentsIsMutable();
                            this.intents_.remove(index);
                            onChanged();
                        } else {
                            this.intentsBuilder_.remove(index);
                        }
                        return this;
                    }

                    public Intent.Builder getIntentsBuilder(int index) {
                        return getIntentsFieldBuilder().getBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public IntentOrBuilder getIntentsOrBuilder(int index) {
                        if (this.intentsBuilder_ == null) {
                            return this.intents_.get(index);
                        }
                        return this.intentsBuilder_.getMessageOrBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public List<? extends IntentOrBuilder> getIntentsOrBuilderList() {
                        if (this.intentsBuilder_ != null) {
                            return this.intentsBuilder_.getMessageOrBuilderList();
                        }
                        return Collections.unmodifiableList(this.intents_);
                    }

                    public Intent.Builder addIntentsBuilder() {
                        return getIntentsFieldBuilder().addBuilder(Intent.getDefaultInstance());
                    }

                    public Intent.Builder addIntentsBuilder(int index) {
                        return getIntentsFieldBuilder().addBuilder(index, Intent.getDefaultInstance());
                    }

                    public List<Intent.Builder> getIntentsBuilderList() {
                        return getIntentsFieldBuilder().getBuilderList();
                    }

                    private RepeatedFieldBuilder<Intent, Intent.Builder, IntentOrBuilder> getIntentsFieldBuilder() {
                        if (this.intentsBuilder_ == null) {
                            this.intentsBuilder_ = new RepeatedFieldBuilder<>(this.intents_, (this.bitField0_ & 8) == 8, getParentForChildren(), isClean());
                            this.intents_ = null;
                        }
                        return this.intentsBuilder_;
                    }

                    private void ensureUrisIsMutable() {
                        if ((this.bitField0_ & 16) != 16) {
                            this.uris_ = new ArrayList(this.uris_);
                            this.bitField0_ |= 16;
                        }
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public List<Uri> getUrisList() {
                        if (this.urisBuilder_ == null) {
                            return Collections.unmodifiableList(this.uris_);
                        }
                        return this.urisBuilder_.getMessageList();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public int getUrisCount() {
                        if (this.urisBuilder_ == null) {
                            return this.uris_.size();
                        }
                        return this.urisBuilder_.getCount();
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public Uri getUris(int index) {
                        if (this.urisBuilder_ == null) {
                            return this.uris_.get(index);
                        }
                        return this.urisBuilder_.getMessage(index);
                    }

                    public Builder setUris(int index, Uri value) {
                        if (this.urisBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureUrisIsMutable();
                            this.uris_.set(index, value);
                            onChanged();
                        } else {
                            this.urisBuilder_.setMessage(index, value);
                        }
                        return this;
                    }

                    public Builder setUris(int index, Uri.Builder builderForValue) {
                        if (this.urisBuilder_ == null) {
                            ensureUrisIsMutable();
                            this.uris_.set(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.urisBuilder_.setMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addUris(Uri value) {
                        if (this.urisBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureUrisIsMutable();
                            this.uris_.add(value);
                            onChanged();
                        } else {
                            this.urisBuilder_.addMessage(value);
                        }
                        return this;
                    }

                    public Builder addUris(int index, Uri value) {
                        if (this.urisBuilder_ == null) {
                            if (value == null) {
                                throw new NullPointerException();
                            }
                            ensureUrisIsMutable();
                            this.uris_.add(index, value);
                            onChanged();
                        } else {
                            this.urisBuilder_.addMessage(index, value);
                        }
                        return this;
                    }

                    public Builder addUris(Uri.Builder builderForValue) {
                        if (this.urisBuilder_ == null) {
                            ensureUrisIsMutable();
                            this.uris_.add(builderForValue.build());
                            onChanged();
                        } else {
                            this.urisBuilder_.addMessage(builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addUris(int index, Uri.Builder builderForValue) {
                        if (this.urisBuilder_ == null) {
                            ensureUrisIsMutable();
                            this.uris_.add(index, builderForValue.build());
                            onChanged();
                        } else {
                            this.urisBuilder_.addMessage(index, builderForValue.build());
                        }
                        return this;
                    }

                    public Builder addAllUris(Iterable<? extends Uri> values) {
                        if (this.urisBuilder_ == null) {
                            ensureUrisIsMutable();
                            GeneratedMessage.Builder.addAll((Iterable) values, (List) this.uris_);
                            onChanged();
                        } else {
                            this.urisBuilder_.addAllMessages(values);
                        }
                        return this;
                    }

                    public Builder clearUris() {
                        if (this.urisBuilder_ == null) {
                            this.uris_ = Collections.emptyList();
                            this.bitField0_ &= -17;
                            onChanged();
                        } else {
                            this.urisBuilder_.clear();
                        }
                        return this;
                    }

                    public Builder removeUris(int index) {
                        if (this.urisBuilder_ == null) {
                            ensureUrisIsMutable();
                            this.uris_.remove(index);
                            onChanged();
                        } else {
                            this.urisBuilder_.remove(index);
                        }
                        return this;
                    }

                    public Uri.Builder getUrisBuilder(int index) {
                        return getUrisFieldBuilder().getBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public UriOrBuilder getUrisOrBuilder(int index) {
                        if (this.urisBuilder_ == null) {
                            return this.uris_.get(index);
                        }
                        return this.urisBuilder_.getMessageOrBuilder(index);
                    }

                    @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.Component.ExitPointOrBuilder
                    public List<? extends UriOrBuilder> getUrisOrBuilderList() {
                        if (this.urisBuilder_ != null) {
                            return this.urisBuilder_.getMessageOrBuilderList();
                        }
                        return Collections.unmodifiableList(this.uris_);
                    }

                    public Uri.Builder addUrisBuilder() {
                        return getUrisFieldBuilder().addBuilder(Uri.getDefaultInstance());
                    }

                    public Uri.Builder addUrisBuilder(int index) {
                        return getUrisFieldBuilder().addBuilder(index, Uri.getDefaultInstance());
                    }

                    public List<Uri.Builder> getUrisBuilderList() {
                        return getUrisFieldBuilder().getBuilderList();
                    }

                    private RepeatedFieldBuilder<Uri, Uri.Builder, UriOrBuilder> getUrisFieldBuilder() {
                        if (this.urisBuilder_ == null) {
                            this.urisBuilder_ = new RepeatedFieldBuilder<>(this.uris_, (this.bitField0_ & 16) == 16, getParentForChildren(), isClean());
                            this.uris_ = null;
                        }
                        return this.urisBuilder_;
                    }
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getName() {
                Object ref = this.name_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.name_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getNameBytes() {
                Object ref = this.name_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.name_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasKind() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ComponentKind getKind() {
                return this.kind_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasExported() {
                return (this.bitField0_ & 4) == 4;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean getExported() {
                return this.exported_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasPermission() {
                return (this.bitField0_ & 8) == 8;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getPermission() {
                Object ref = this.permission_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.permission_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getPermissionBytes() {
                Object ref = this.permission_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.permission_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasMissing() {
                return (this.bitField0_ & 16) == 16;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public int getMissing() {
                return this.missing_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<Extra> getExtrasList() {
                return this.extras_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<? extends ExtraOrBuilder> getExtrasOrBuilderList() {
                return this.extras_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public int getExtrasCount() {
                return this.extras_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public Extra getExtras(int index) {
                return this.extras_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ExtraOrBuilder getExtrasOrBuilder(int index) {
                return this.extras_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasAliasTarget() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getAliasTarget() {
                Object ref = this.aliasTarget_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.aliasTarget_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getAliasTargetBytes() {
                Object ref = this.aliasTarget_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.aliasTarget_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasGrantUriPermissions() {
                return (this.bitField0_ & 64) == 64;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean getGrantUriPermissions() {
                return this.grantUriPermissions_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasReadPermission() {
                return (this.bitField0_ & 128) == 128;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getReadPermission() {
                Object ref = this.readPermission_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.readPermission_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getReadPermissionBytes() {
                Object ref = this.readPermission_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.readPermission_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasWritePermission() {
                return (this.bitField0_ & 256) == 256;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getWritePermission() {
                Object ref = this.writePermission_;
                if (ref instanceof String) {
                    return (String) ref;
                }
                ByteString bs = (ByteString) ref;
                String s = bs.toStringUtf8();
                if (bs.isValidUtf8()) {
                    this.writePermission_ = s;
                }
                return s;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getWritePermissionBytes() {
                Object ref = this.writePermission_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.writePermission_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<String> getAuthoritiesList() {
                return this.authorities_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public int getAuthoritiesCount() {
                return this.authorities_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public String getAuthorities(int index) {
                return (String) this.authorities_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ByteString getAuthoritiesBytes(int index) {
                return this.authorities_.getByteString(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<IntentFilter> getIntentFiltersList() {
                return this.intentFilters_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<? extends IntentFilterOrBuilder> getIntentFiltersOrBuilderList() {
                return this.intentFilters_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public int getIntentFiltersCount() {
                return this.intentFilters_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public IntentFilter getIntentFilters(int index) {
                return this.intentFilters_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public IntentFilterOrBuilder getIntentFiltersOrBuilder(int index) {
                return this.intentFilters_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<ExitPoint> getExitPointsList() {
                return this.exitPoints_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public List<? extends ExitPointOrBuilder> getExitPointsOrBuilderList() {
                return this.exitPoints_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public int getExitPointsCount() {
                return this.exitPoints_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ExitPoint getExitPoints(int index) {
                return this.exitPoints_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public ExitPointOrBuilder getExitPointsOrBuilder(int index) {
                return this.exitPoints_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public boolean hasRegistrationInstruction() {
                return (this.bitField0_ & 512) == 512;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public Instruction getRegistrationInstruction() {
                return this.registrationInstruction_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
            public InstructionOrBuilder getRegistrationInstructionOrBuilder() {
                return this.registrationInstruction_;
            }

            private void initFields() {
                this.name_ = "";
                this.kind_ = ComponentKind.ACTIVITY;
                this.exported_ = false;
                this.permission_ = "";
                this.missing_ = 0;
                this.extras_ = Collections.emptyList();
                this.aliasTarget_ = "";
                this.grantUriPermissions_ = false;
                this.readPermission_ = "";
                this.writePermission_ = "";
                this.authorities_ = LazyStringArrayList.EMPTY;
                this.intentFilters_ = Collections.emptyList();
                this.exitPoints_ = Collections.emptyList();
                this.registrationInstruction_ = Instruction.getDefaultInstance();
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                byte isInitialized = this.memoizedIsInitialized;
                if (isInitialized != -1) {
                    return isInitialized == 1;
                }
                this.memoizedIsInitialized = (byte) 1;
                return true;
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
            public void writeTo(CodedOutputStream output) throws IOException {
                getSerializedSize();
                if ((this.bitField0_ & 1) == 1) {
                    output.writeBytes(1, getNameBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    output.writeEnum(2, this.kind_.getNumber());
                }
                if ((this.bitField0_ & 4) == 4) {
                    output.writeBool(3, this.exported_);
                }
                if ((this.bitField0_ & 8) == 8) {
                    output.writeBytes(4, getPermissionBytes());
                }
                if ((this.bitField0_ & 16) == 16) {
                    output.writeUInt32(5, this.missing_);
                }
                for (int i = 0; i < this.extras_.size(); i++) {
                    output.writeMessage(6, this.extras_.get(i));
                }
                if ((this.bitField0_ & 32) == 32) {
                    output.writeBytes(7, getAliasTargetBytes());
                }
                if ((this.bitField0_ & 64) == 64) {
                    output.writeBool(8, this.grantUriPermissions_);
                }
                if ((this.bitField0_ & 128) == 128) {
                    output.writeBytes(9, getReadPermissionBytes());
                }
                if ((this.bitField0_ & 256) == 256) {
                    output.writeBytes(10, getWritePermissionBytes());
                }
                for (int i2 = 0; i2 < this.authorities_.size(); i2++) {
                    output.writeBytes(11, this.authorities_.getByteString(i2));
                }
                for (int i3 = 0; i3 < this.intentFilters_.size(); i3++) {
                    output.writeMessage(12, this.intentFilters_.get(i3));
                }
                for (int i4 = 0; i4 < this.exitPoints_.size(); i4++) {
                    output.writeMessage(13, this.exitPoints_.get(i4));
                }
                if ((this.bitField0_ & 512) == 512) {
                    output.writeMessage(14, this.registrationInstruction_);
                }
                getUnknownFields().writeTo(output);
            }

            @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
            public int getSerializedSize() {
                int size = this.memoizedSerializedSize;
                if (size != -1) {
                    return size;
                }
                int size2 = 0;
                if ((this.bitField0_ & 1) == 1) {
                    size2 = 0 + CodedOutputStream.computeBytesSize(1, getNameBytes());
                }
                if ((this.bitField0_ & 2) == 2) {
                    size2 += CodedOutputStream.computeEnumSize(2, this.kind_.getNumber());
                }
                if ((this.bitField0_ & 4) == 4) {
                    size2 += CodedOutputStream.computeBoolSize(3, this.exported_);
                }
                if ((this.bitField0_ & 8) == 8) {
                    size2 += CodedOutputStream.computeBytesSize(4, getPermissionBytes());
                }
                if ((this.bitField0_ & 16) == 16) {
                    size2 += CodedOutputStream.computeUInt32Size(5, this.missing_);
                }
                for (int i = 0; i < this.extras_.size(); i++) {
                    size2 += CodedOutputStream.computeMessageSize(6, this.extras_.get(i));
                }
                if ((this.bitField0_ & 32) == 32) {
                    size2 += CodedOutputStream.computeBytesSize(7, getAliasTargetBytes());
                }
                if ((this.bitField0_ & 64) == 64) {
                    size2 += CodedOutputStream.computeBoolSize(8, this.grantUriPermissions_);
                }
                if ((this.bitField0_ & 128) == 128) {
                    size2 += CodedOutputStream.computeBytesSize(9, getReadPermissionBytes());
                }
                if ((this.bitField0_ & 256) == 256) {
                    size2 += CodedOutputStream.computeBytesSize(10, getWritePermissionBytes());
                }
                int dataSize = 0;
                for (int i2 = 0; i2 < this.authorities_.size(); i2++) {
                    dataSize += CodedOutputStream.computeBytesSizeNoTag(this.authorities_.getByteString(i2));
                }
                int size3 = size2 + dataSize + (1 * getAuthoritiesList().size());
                for (int i3 = 0; i3 < this.intentFilters_.size(); i3++) {
                    size3 += CodedOutputStream.computeMessageSize(12, this.intentFilters_.get(i3));
                }
                for (int i4 = 0; i4 < this.exitPoints_.size(); i4++) {
                    size3 += CodedOutputStream.computeMessageSize(13, this.exitPoints_.get(i4));
                }
                if ((this.bitField0_ & 512) == 512) {
                    size3 += CodedOutputStream.computeMessageSize(14, this.registrationInstruction_);
                }
                int size4 = size3 + getUnknownFields().getSerializedSize();
                this.memoizedSerializedSize = size4;
                return size4;
            }

            @Override // com.google.protobuf.GeneratedMessage
            protected Object writeReplace() throws ObjectStreamException {
                return super.writeReplace();
            }

            public static Component parseFrom(ByteString data) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static Component parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static Component parseFrom(byte[] data) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data);
            }

            public static Component parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                return PARSER.parseFrom(data, extensionRegistry);
            }

            public static Component parseFrom(InputStream input) throws IOException {
                return PARSER.parseFrom(input);
            }

            public static Component parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Component parseDelimitedFrom(InputStream input) throws IOException {
                return PARSER.parseDelimitedFrom(input);
            }

            public static Component parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseDelimitedFrom(input, extensionRegistry);
            }

            public static Component parseFrom(CodedInputStream input) throws IOException {
                return PARSER.parseFrom(input);
            }

            public static Component parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                return PARSER.parseFrom(input, extensionRegistry);
            }

            public static Builder newBuilder() {
                return Builder.create();
            }

            @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Builder newBuilderForType() {
                return newBuilder();
            }

            public static Builder newBuilder(Component prototype) {
                return newBuilder().mergeFrom(prototype);
            }

            @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
            public Builder toBuilder() {
                return newBuilder(this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // com.google.protobuf.GeneratedMessage
            public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
                Builder builder = new Builder(parent, null);
                return builder;
            }

            /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Component$Builder.class */
            public static final class Builder extends GeneratedMessage.Builder<Builder> implements ComponentOrBuilder {
                private int bitField0_;
                private Object name_;
                private ComponentKind kind_;
                private boolean exported_;
                private Object permission_;
                private int missing_;
                private List<Extra> extras_;
                private RepeatedFieldBuilder<Extra, Extra.Builder, ExtraOrBuilder> extrasBuilder_;
                private Object aliasTarget_;
                private boolean grantUriPermissions_;
                private Object readPermission_;
                private Object writePermission_;
                private LazyStringList authorities_;
                private List<IntentFilter> intentFilters_;
                private RepeatedFieldBuilder<IntentFilter, IntentFilter.Builder, IntentFilterOrBuilder> intentFiltersBuilder_;
                private List<ExitPoint> exitPoints_;
                private RepeatedFieldBuilder<ExitPoint, ExitPoint.Builder, ExitPointOrBuilder> exitPointsBuilder_;
                private Instruction registrationInstruction_;
                private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> registrationInstructionBuilder_;

                public static final Descriptors.Descriptor getDescriptor() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder
                protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_fieldAccessorTable.ensureFieldAccessorsInitialized(Component.class, Builder.class);
                }

                private Builder() {
                    this.name_ = "";
                    this.kind_ = ComponentKind.ACTIVITY;
                    this.permission_ = "";
                    this.extras_ = Collections.emptyList();
                    this.aliasTarget_ = "";
                    this.readPermission_ = "";
                    this.writePermission_ = "";
                    this.authorities_ = LazyStringArrayList.EMPTY;
                    this.intentFilters_ = Collections.emptyList();
                    this.exitPoints_ = Collections.emptyList();
                    this.registrationInstruction_ = Instruction.getDefaultInstance();
                    maybeForceBuilderInitialization();
                }

                /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                    this(builderParent);
                }

                private Builder(GeneratedMessage.BuilderParent parent) {
                    super(parent);
                    this.name_ = "";
                    this.kind_ = ComponentKind.ACTIVITY;
                    this.permission_ = "";
                    this.extras_ = Collections.emptyList();
                    this.aliasTarget_ = "";
                    this.readPermission_ = "";
                    this.writePermission_ = "";
                    this.authorities_ = LazyStringArrayList.EMPTY;
                    this.intentFilters_ = Collections.emptyList();
                    this.exitPoints_ = Collections.emptyList();
                    this.registrationInstruction_ = Instruction.getDefaultInstance();
                    maybeForceBuilderInitialization();
                }

                private void maybeForceBuilderInitialization() {
                    if (Component.alwaysUseFieldBuilders) {
                        getExtrasFieldBuilder();
                        getIntentFiltersFieldBuilder();
                        getExitPointsFieldBuilder();
                        getRegistrationInstructionFieldBuilder();
                    }
                }

                /* JADX INFO: Access modifiers changed from: private */
                public static Builder create() {
                    return new Builder();
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder clear() {
                    super.clear();
                    this.name_ = "";
                    this.bitField0_ &= -2;
                    this.kind_ = ComponentKind.ACTIVITY;
                    this.bitField0_ &= -3;
                    this.exported_ = false;
                    this.bitField0_ &= -5;
                    this.permission_ = "";
                    this.bitField0_ &= -9;
                    this.missing_ = 0;
                    this.bitField0_ &= -17;
                    if (this.extrasBuilder_ == null) {
                        this.extras_ = Collections.emptyList();
                        this.bitField0_ &= -33;
                    } else {
                        this.extrasBuilder_.clear();
                    }
                    this.aliasTarget_ = "";
                    this.bitField0_ &= -65;
                    this.grantUriPermissions_ = false;
                    this.bitField0_ &= -129;
                    this.readPermission_ = "";
                    this.bitField0_ &= -257;
                    this.writePermission_ = "";
                    this.bitField0_ &= -513;
                    this.authorities_ = LazyStringArrayList.EMPTY;
                    this.bitField0_ &= -1025;
                    if (this.intentFiltersBuilder_ == null) {
                        this.intentFilters_ = Collections.emptyList();
                        this.bitField0_ &= -2049;
                    } else {
                        this.intentFiltersBuilder_.clear();
                    }
                    if (this.exitPointsBuilder_ == null) {
                        this.exitPoints_ = Collections.emptyList();
                        this.bitField0_ &= -4097;
                    } else {
                        this.exitPointsBuilder_.clear();
                    }
                    if (this.registrationInstructionBuilder_ == null) {
                        this.registrationInstruction_ = Instruction.getDefaultInstance();
                    } else {
                        this.registrationInstructionBuilder_.clear();
                    }
                    this.bitField0_ &= -8193;
                    return this;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder clone() {
                    return create().mergeFrom(buildPartial());
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
                public Descriptors.Descriptor getDescriptorForType() {
                    return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor;
                }

                @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
                public Component getDefaultInstanceForType() {
                    return Component.getDefaultInstance();
                }

                @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Component build() {
                    Component result = buildPartial();
                    if (!result.isInitialized()) {
                        throw newUninitializedMessageException((Message) result);
                    }
                    return result;
                }

                @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Component buildPartial() {
                    Component result = new Component(this, (Component) null);
                    int from_bitField0_ = this.bitField0_;
                    int to_bitField0_ = 0;
                    if ((from_bitField0_ & 1) == 1) {
                        to_bitField0_ = 0 | 1;
                    }
                    result.name_ = this.name_;
                    if ((from_bitField0_ & 2) == 2) {
                        to_bitField0_ |= 2;
                    }
                    result.kind_ = this.kind_;
                    if ((from_bitField0_ & 4) == 4) {
                        to_bitField0_ |= 4;
                    }
                    result.exported_ = this.exported_;
                    if ((from_bitField0_ & 8) == 8) {
                        to_bitField0_ |= 8;
                    }
                    result.permission_ = this.permission_;
                    if ((from_bitField0_ & 16) == 16) {
                        to_bitField0_ |= 16;
                    }
                    result.missing_ = this.missing_;
                    if (this.extrasBuilder_ != null) {
                        result.extras_ = this.extrasBuilder_.build();
                    } else {
                        if ((this.bitField0_ & 32) == 32) {
                            this.extras_ = Collections.unmodifiableList(this.extras_);
                            this.bitField0_ &= -33;
                        }
                        result.extras_ = this.extras_;
                    }
                    if ((from_bitField0_ & 64) == 64) {
                        to_bitField0_ |= 32;
                    }
                    result.aliasTarget_ = this.aliasTarget_;
                    if ((from_bitField0_ & 128) == 128) {
                        to_bitField0_ |= 64;
                    }
                    result.grantUriPermissions_ = this.grantUriPermissions_;
                    if ((from_bitField0_ & 256) == 256) {
                        to_bitField0_ |= 128;
                    }
                    result.readPermission_ = this.readPermission_;
                    if ((from_bitField0_ & 512) == 512) {
                        to_bitField0_ |= 256;
                    }
                    result.writePermission_ = this.writePermission_;
                    if ((this.bitField0_ & 1024) == 1024) {
                        this.authorities_ = new UnmodifiableLazyStringList(this.authorities_);
                        this.bitField0_ &= -1025;
                    }
                    result.authorities_ = this.authorities_;
                    if (this.intentFiltersBuilder_ != null) {
                        result.intentFilters_ = this.intentFiltersBuilder_.build();
                    } else {
                        if ((this.bitField0_ & 2048) == 2048) {
                            this.intentFilters_ = Collections.unmodifiableList(this.intentFilters_);
                            this.bitField0_ &= -2049;
                        }
                        result.intentFilters_ = this.intentFilters_;
                    }
                    if (this.exitPointsBuilder_ != null) {
                        result.exitPoints_ = this.exitPointsBuilder_.build();
                    } else {
                        if ((this.bitField0_ & 4096) == 4096) {
                            this.exitPoints_ = Collections.unmodifiableList(this.exitPoints_);
                            this.bitField0_ &= -4097;
                        }
                        result.exitPoints_ = this.exitPoints_;
                    }
                    if ((from_bitField0_ & 8192) == 8192) {
                        to_bitField0_ |= 512;
                    }
                    if (this.registrationInstructionBuilder_ == null) {
                        result.registrationInstruction_ = this.registrationInstruction_;
                    } else {
                        result.registrationInstruction_ = this.registrationInstructionBuilder_.build();
                    }
                    result.bitField0_ = to_bitField0_;
                    onBuilt();
                    return result;
                }

                @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
                public Builder mergeFrom(Message other) {
                    if (other instanceof Component) {
                        return mergeFrom((Component) other);
                    }
                    super.mergeFrom(other);
                    return this;
                }

                public Builder mergeFrom(Component other) {
                    RepeatedFieldBuilder<Extra, Extra.Builder, ExtraOrBuilder> repeatedFieldBuilder;
                    RepeatedFieldBuilder<IntentFilter, IntentFilter.Builder, IntentFilterOrBuilder> repeatedFieldBuilder2;
                    RepeatedFieldBuilder<ExitPoint, ExitPoint.Builder, ExitPointOrBuilder> repeatedFieldBuilder3;
                    if (other == Component.getDefaultInstance()) {
                        return this;
                    }
                    if (other.hasName()) {
                        this.bitField0_ |= 1;
                        this.name_ = other.name_;
                        onChanged();
                    }
                    if (other.hasKind()) {
                        setKind(other.getKind());
                    }
                    if (other.hasExported()) {
                        setExported(other.getExported());
                    }
                    if (other.hasPermission()) {
                        this.bitField0_ |= 8;
                        this.permission_ = other.permission_;
                        onChanged();
                    }
                    if (other.hasMissing()) {
                        setMissing(other.getMissing());
                    }
                    if (this.extrasBuilder_ == null) {
                        if (!other.extras_.isEmpty()) {
                            if (this.extras_.isEmpty()) {
                                this.extras_ = other.extras_;
                                this.bitField0_ &= -33;
                            } else {
                                ensureExtrasIsMutable();
                                this.extras_.addAll(other.extras_);
                            }
                            onChanged();
                        }
                    } else if (!other.extras_.isEmpty()) {
                        if (!this.extrasBuilder_.isEmpty()) {
                            this.extrasBuilder_.addAllMessages(other.extras_);
                        } else {
                            this.extrasBuilder_.dispose();
                            this.extrasBuilder_ = null;
                            this.extras_ = other.extras_;
                            this.bitField0_ &= -33;
                            if (Component.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder = getExtrasFieldBuilder();
                            } else {
                                repeatedFieldBuilder = null;
                            }
                            this.extrasBuilder_ = repeatedFieldBuilder;
                        }
                    }
                    if (other.hasAliasTarget()) {
                        this.bitField0_ |= 64;
                        this.aliasTarget_ = other.aliasTarget_;
                        onChanged();
                    }
                    if (other.hasGrantUriPermissions()) {
                        setGrantUriPermissions(other.getGrantUriPermissions());
                    }
                    if (other.hasReadPermission()) {
                        this.bitField0_ |= 256;
                        this.readPermission_ = other.readPermission_;
                        onChanged();
                    }
                    if (other.hasWritePermission()) {
                        this.bitField0_ |= 512;
                        this.writePermission_ = other.writePermission_;
                        onChanged();
                    }
                    if (!other.authorities_.isEmpty()) {
                        if (this.authorities_.isEmpty()) {
                            this.authorities_ = other.authorities_;
                            this.bitField0_ &= -1025;
                        } else {
                            ensureAuthoritiesIsMutable();
                            this.authorities_.addAll(other.authorities_);
                        }
                        onChanged();
                    }
                    if (this.intentFiltersBuilder_ == null) {
                        if (!other.intentFilters_.isEmpty()) {
                            if (this.intentFilters_.isEmpty()) {
                                this.intentFilters_ = other.intentFilters_;
                                this.bitField0_ &= -2049;
                            } else {
                                ensureIntentFiltersIsMutable();
                                this.intentFilters_.addAll(other.intentFilters_);
                            }
                            onChanged();
                        }
                    } else if (!other.intentFilters_.isEmpty()) {
                        if (!this.intentFiltersBuilder_.isEmpty()) {
                            this.intentFiltersBuilder_.addAllMessages(other.intentFilters_);
                        } else {
                            this.intentFiltersBuilder_.dispose();
                            this.intentFiltersBuilder_ = null;
                            this.intentFilters_ = other.intentFilters_;
                            this.bitField0_ &= -2049;
                            if (Component.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder2 = getIntentFiltersFieldBuilder();
                            } else {
                                repeatedFieldBuilder2 = null;
                            }
                            this.intentFiltersBuilder_ = repeatedFieldBuilder2;
                        }
                    }
                    if (this.exitPointsBuilder_ == null) {
                        if (!other.exitPoints_.isEmpty()) {
                            if (this.exitPoints_.isEmpty()) {
                                this.exitPoints_ = other.exitPoints_;
                                this.bitField0_ &= -4097;
                            } else {
                                ensureExitPointsIsMutable();
                                this.exitPoints_.addAll(other.exitPoints_);
                            }
                            onChanged();
                        }
                    } else if (!other.exitPoints_.isEmpty()) {
                        if (!this.exitPointsBuilder_.isEmpty()) {
                            this.exitPointsBuilder_.addAllMessages(other.exitPoints_);
                        } else {
                            this.exitPointsBuilder_.dispose();
                            this.exitPointsBuilder_ = null;
                            this.exitPoints_ = other.exitPoints_;
                            this.bitField0_ &= -4097;
                            if (Component.alwaysUseFieldBuilders) {
                                repeatedFieldBuilder3 = getExitPointsFieldBuilder();
                            } else {
                                repeatedFieldBuilder3 = null;
                            }
                            this.exitPointsBuilder_ = repeatedFieldBuilder3;
                        }
                    }
                    if (other.hasRegistrationInstruction()) {
                        mergeRegistrationInstruction(other.getRegistrationInstruction());
                    }
                    mergeUnknownFields(other.getUnknownFields());
                    return this;
                }

                @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
                public final boolean isInitialized() {
                    return true;
                }

                @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
                public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                    Component parsedMessage = null;
                    try {
                        try {
                            parsedMessage = Component.PARSER.parsePartialFrom(input, extensionRegistry);
                            if (parsedMessage != null) {
                                mergeFrom(parsedMessage);
                            }
                            return this;
                        } catch (InvalidProtocolBufferException e) {
                            Component component = (Component) e.getUnfinishedMessage();
                            throw e;
                        }
                    } catch (Throwable th) {
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        throw th;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasName() {
                    return (this.bitField0_ & 1) == 1;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getName() {
                    Object ref = this.name_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.name_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
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
                    this.bitField0_ |= 1;
                    this.name_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearName() {
                    this.bitField0_ &= -2;
                    this.name_ = Component.getDefaultInstance().getName();
                    onChanged();
                    return this;
                }

                public Builder setNameBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 1;
                    this.name_ = value;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasKind() {
                    return (this.bitField0_ & 2) == 2;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ComponentKind getKind() {
                    return this.kind_;
                }

                public Builder setKind(ComponentKind value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 2;
                    this.kind_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearKind() {
                    this.bitField0_ &= -3;
                    this.kind_ = ComponentKind.ACTIVITY;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasExported() {
                    return (this.bitField0_ & 4) == 4;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean getExported() {
                    return this.exported_;
                }

                public Builder setExported(boolean value) {
                    this.bitField0_ |= 4;
                    this.exported_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearExported() {
                    this.bitField0_ &= -5;
                    this.exported_ = false;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasPermission() {
                    return (this.bitField0_ & 8) == 8;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getPermission() {
                    Object ref = this.permission_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.permission_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ByteString getPermissionBytes() {
                    Object ref = this.permission_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.permission_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                public Builder setPermission(String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 8;
                    this.permission_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearPermission() {
                    this.bitField0_ &= -9;
                    this.permission_ = Component.getDefaultInstance().getPermission();
                    onChanged();
                    return this;
                }

                public Builder setPermissionBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 8;
                    this.permission_ = value;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasMissing() {
                    return (this.bitField0_ & 16) == 16;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public int getMissing() {
                    return this.missing_;
                }

                public Builder setMissing(int value) {
                    this.bitField0_ |= 16;
                    this.missing_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearMissing() {
                    this.bitField0_ &= -17;
                    this.missing_ = 0;
                    onChanged();
                    return this;
                }

                private void ensureExtrasIsMutable() {
                    if ((this.bitField0_ & 32) != 32) {
                        this.extras_ = new ArrayList(this.extras_);
                        this.bitField0_ |= 32;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<Extra> getExtrasList() {
                    if (this.extrasBuilder_ == null) {
                        return Collections.unmodifiableList(this.extras_);
                    }
                    return this.extrasBuilder_.getMessageList();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public int getExtrasCount() {
                    if (this.extrasBuilder_ == null) {
                        return this.extras_.size();
                    }
                    return this.extrasBuilder_.getCount();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public Extra getExtras(int index) {
                    if (this.extrasBuilder_ == null) {
                        return this.extras_.get(index);
                    }
                    return this.extrasBuilder_.getMessage(index);
                }

                public Builder setExtras(int index, Extra value) {
                    if (this.extrasBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExtrasIsMutable();
                        this.extras_.set(index, value);
                        onChanged();
                    } else {
                        this.extrasBuilder_.setMessage(index, value);
                    }
                    return this;
                }

                public Builder setExtras(int index, Extra.Builder builderForValue) {
                    if (this.extrasBuilder_ == null) {
                        ensureExtrasIsMutable();
                        this.extras_.set(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.extrasBuilder_.setMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addExtras(Extra value) {
                    if (this.extrasBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExtrasIsMutable();
                        this.extras_.add(value);
                        onChanged();
                    } else {
                        this.extrasBuilder_.addMessage(value);
                    }
                    return this;
                }

                public Builder addExtras(int index, Extra value) {
                    if (this.extrasBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExtrasIsMutable();
                        this.extras_.add(index, value);
                        onChanged();
                    } else {
                        this.extrasBuilder_.addMessage(index, value);
                    }
                    return this;
                }

                public Builder addExtras(Extra.Builder builderForValue) {
                    if (this.extrasBuilder_ == null) {
                        ensureExtrasIsMutable();
                        this.extras_.add(builderForValue.build());
                        onChanged();
                    } else {
                        this.extrasBuilder_.addMessage(builderForValue.build());
                    }
                    return this;
                }

                public Builder addExtras(int index, Extra.Builder builderForValue) {
                    if (this.extrasBuilder_ == null) {
                        ensureExtrasIsMutable();
                        this.extras_.add(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.extrasBuilder_.addMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addAllExtras(Iterable<? extends Extra> values) {
                    if (this.extrasBuilder_ == null) {
                        ensureExtrasIsMutable();
                        GeneratedMessage.Builder.addAll((Iterable) values, (List) this.extras_);
                        onChanged();
                    } else {
                        this.extrasBuilder_.addAllMessages(values);
                    }
                    return this;
                }

                public Builder clearExtras() {
                    if (this.extrasBuilder_ == null) {
                        this.extras_ = Collections.emptyList();
                        this.bitField0_ &= -33;
                        onChanged();
                    } else {
                        this.extrasBuilder_.clear();
                    }
                    return this;
                }

                public Builder removeExtras(int index) {
                    if (this.extrasBuilder_ == null) {
                        ensureExtrasIsMutable();
                        this.extras_.remove(index);
                        onChanged();
                    } else {
                        this.extrasBuilder_.remove(index);
                    }
                    return this;
                }

                public Extra.Builder getExtrasBuilder(int index) {
                    return getExtrasFieldBuilder().getBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ExtraOrBuilder getExtrasOrBuilder(int index) {
                    if (this.extrasBuilder_ == null) {
                        return this.extras_.get(index);
                    }
                    return this.extrasBuilder_.getMessageOrBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<? extends ExtraOrBuilder> getExtrasOrBuilderList() {
                    if (this.extrasBuilder_ != null) {
                        return this.extrasBuilder_.getMessageOrBuilderList();
                    }
                    return Collections.unmodifiableList(this.extras_);
                }

                public Extra.Builder addExtrasBuilder() {
                    return getExtrasFieldBuilder().addBuilder(Extra.getDefaultInstance());
                }

                public Extra.Builder addExtrasBuilder(int index) {
                    return getExtrasFieldBuilder().addBuilder(index, Extra.getDefaultInstance());
                }

                public List<Extra.Builder> getExtrasBuilderList() {
                    return getExtrasFieldBuilder().getBuilderList();
                }

                private RepeatedFieldBuilder<Extra, Extra.Builder, ExtraOrBuilder> getExtrasFieldBuilder() {
                    if (this.extrasBuilder_ == null) {
                        this.extrasBuilder_ = new RepeatedFieldBuilder<>(this.extras_, (this.bitField0_ & 32) == 32, getParentForChildren(), isClean());
                        this.extras_ = null;
                    }
                    return this.extrasBuilder_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasAliasTarget() {
                    return (this.bitField0_ & 64) == 64;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getAliasTarget() {
                    Object ref = this.aliasTarget_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.aliasTarget_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ByteString getAliasTargetBytes() {
                    Object ref = this.aliasTarget_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.aliasTarget_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                public Builder setAliasTarget(String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 64;
                    this.aliasTarget_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearAliasTarget() {
                    this.bitField0_ &= -65;
                    this.aliasTarget_ = Component.getDefaultInstance().getAliasTarget();
                    onChanged();
                    return this;
                }

                public Builder setAliasTargetBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 64;
                    this.aliasTarget_ = value;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasGrantUriPermissions() {
                    return (this.bitField0_ & 128) == 128;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean getGrantUriPermissions() {
                    return this.grantUriPermissions_;
                }

                public Builder setGrantUriPermissions(boolean value) {
                    this.bitField0_ |= 128;
                    this.grantUriPermissions_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearGrantUriPermissions() {
                    this.bitField0_ &= -129;
                    this.grantUriPermissions_ = false;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasReadPermission() {
                    return (this.bitField0_ & 256) == 256;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getReadPermission() {
                    Object ref = this.readPermission_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.readPermission_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ByteString getReadPermissionBytes() {
                    Object ref = this.readPermission_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.readPermission_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                public Builder setReadPermission(String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 256;
                    this.readPermission_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearReadPermission() {
                    this.bitField0_ &= -257;
                    this.readPermission_ = Component.getDefaultInstance().getReadPermission();
                    onChanged();
                    return this;
                }

                public Builder setReadPermissionBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 256;
                    this.readPermission_ = value;
                    onChanged();
                    return this;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasWritePermission() {
                    return (this.bitField0_ & 512) == 512;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getWritePermission() {
                    Object ref = this.writePermission_;
                    if (!(ref instanceof String)) {
                        String s = ((ByteString) ref).toStringUtf8();
                        this.writePermission_ = s;
                        return s;
                    }
                    return (String) ref;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ByteString getWritePermissionBytes() {
                    Object ref = this.writePermission_;
                    if (ref instanceof String) {
                        ByteString b = ByteString.copyFromUtf8((String) ref);
                        this.writePermission_ = b;
                        return b;
                    }
                    return (ByteString) ref;
                }

                public Builder setWritePermission(String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 512;
                    this.writePermission_ = value;
                    onChanged();
                    return this;
                }

                public Builder clearWritePermission() {
                    this.bitField0_ &= -513;
                    this.writePermission_ = Component.getDefaultInstance().getWritePermission();
                    onChanged();
                    return this;
                }

                public Builder setWritePermissionBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    this.bitField0_ |= 512;
                    this.writePermission_ = value;
                    onChanged();
                    return this;
                }

                private void ensureAuthoritiesIsMutable() {
                    if ((this.bitField0_ & 1024) != 1024) {
                        this.authorities_ = new LazyStringArrayList(this.authorities_);
                        this.bitField0_ |= 1024;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<String> getAuthoritiesList() {
                    return Collections.unmodifiableList(this.authorities_);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public int getAuthoritiesCount() {
                    return this.authorities_.size();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public String getAuthorities(int index) {
                    return (String) this.authorities_.get(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ByteString getAuthoritiesBytes(int index) {
                    return this.authorities_.getByteString(index);
                }

                public Builder setAuthorities(int index, String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuthoritiesIsMutable();
                    this.authorities_.set(index, value);
                    onChanged();
                    return this;
                }

                public Builder addAuthorities(String value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuthoritiesIsMutable();
                    this.authorities_.add(value);
                    onChanged();
                    return this;
                }

                public Builder addAllAuthorities(Iterable<String> values) {
                    ensureAuthoritiesIsMutable();
                    GeneratedMessage.Builder.addAll((Iterable) values, (List) this.authorities_);
                    onChanged();
                    return this;
                }

                public Builder clearAuthorities() {
                    this.authorities_ = LazyStringArrayList.EMPTY;
                    this.bitField0_ &= -1025;
                    onChanged();
                    return this;
                }

                public Builder addAuthoritiesBytes(ByteString value) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureAuthoritiesIsMutable();
                    this.authorities_.add(value);
                    onChanged();
                    return this;
                }

                private void ensureIntentFiltersIsMutable() {
                    if ((this.bitField0_ & 2048) != 2048) {
                        this.intentFilters_ = new ArrayList(this.intentFilters_);
                        this.bitField0_ |= 2048;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<IntentFilter> getIntentFiltersList() {
                    if (this.intentFiltersBuilder_ == null) {
                        return Collections.unmodifiableList(this.intentFilters_);
                    }
                    return this.intentFiltersBuilder_.getMessageList();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public int getIntentFiltersCount() {
                    if (this.intentFiltersBuilder_ == null) {
                        return this.intentFilters_.size();
                    }
                    return this.intentFiltersBuilder_.getCount();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public IntentFilter getIntentFilters(int index) {
                    if (this.intentFiltersBuilder_ == null) {
                        return this.intentFilters_.get(index);
                    }
                    return this.intentFiltersBuilder_.getMessage(index);
                }

                public Builder setIntentFilters(int index, IntentFilter value) {
                    if (this.intentFiltersBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.set(index, value);
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.setMessage(index, value);
                    }
                    return this;
                }

                public Builder setIntentFilters(int index, IntentFilter.Builder builderForValue) {
                    if (this.intentFiltersBuilder_ == null) {
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.set(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.setMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addIntentFilters(IntentFilter value) {
                    if (this.intentFiltersBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.add(value);
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.addMessage(value);
                    }
                    return this;
                }

                public Builder addIntentFilters(int index, IntentFilter value) {
                    if (this.intentFiltersBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.add(index, value);
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.addMessage(index, value);
                    }
                    return this;
                }

                public Builder addIntentFilters(IntentFilter.Builder builderForValue) {
                    if (this.intentFiltersBuilder_ == null) {
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.add(builderForValue.build());
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.addMessage(builderForValue.build());
                    }
                    return this;
                }

                public Builder addIntentFilters(int index, IntentFilter.Builder builderForValue) {
                    if (this.intentFiltersBuilder_ == null) {
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.add(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.addMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addAllIntentFilters(Iterable<? extends IntentFilter> values) {
                    if (this.intentFiltersBuilder_ == null) {
                        ensureIntentFiltersIsMutable();
                        GeneratedMessage.Builder.addAll((Iterable) values, (List) this.intentFilters_);
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.addAllMessages(values);
                    }
                    return this;
                }

                public Builder clearIntentFilters() {
                    if (this.intentFiltersBuilder_ == null) {
                        this.intentFilters_ = Collections.emptyList();
                        this.bitField0_ &= -2049;
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.clear();
                    }
                    return this;
                }

                public Builder removeIntentFilters(int index) {
                    if (this.intentFiltersBuilder_ == null) {
                        ensureIntentFiltersIsMutable();
                        this.intentFilters_.remove(index);
                        onChanged();
                    } else {
                        this.intentFiltersBuilder_.remove(index);
                    }
                    return this;
                }

                public IntentFilter.Builder getIntentFiltersBuilder(int index) {
                    return getIntentFiltersFieldBuilder().getBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public IntentFilterOrBuilder getIntentFiltersOrBuilder(int index) {
                    if (this.intentFiltersBuilder_ == null) {
                        return this.intentFilters_.get(index);
                    }
                    return this.intentFiltersBuilder_.getMessageOrBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<? extends IntentFilterOrBuilder> getIntentFiltersOrBuilderList() {
                    if (this.intentFiltersBuilder_ != null) {
                        return this.intentFiltersBuilder_.getMessageOrBuilderList();
                    }
                    return Collections.unmodifiableList(this.intentFilters_);
                }

                public IntentFilter.Builder addIntentFiltersBuilder() {
                    return getIntentFiltersFieldBuilder().addBuilder(IntentFilter.getDefaultInstance());
                }

                public IntentFilter.Builder addIntentFiltersBuilder(int index) {
                    return getIntentFiltersFieldBuilder().addBuilder(index, IntentFilter.getDefaultInstance());
                }

                public List<IntentFilter.Builder> getIntentFiltersBuilderList() {
                    return getIntentFiltersFieldBuilder().getBuilderList();
                }

                private RepeatedFieldBuilder<IntentFilter, IntentFilter.Builder, IntentFilterOrBuilder> getIntentFiltersFieldBuilder() {
                    if (this.intentFiltersBuilder_ == null) {
                        this.intentFiltersBuilder_ = new RepeatedFieldBuilder<>(this.intentFilters_, (this.bitField0_ & 2048) == 2048, getParentForChildren(), isClean());
                        this.intentFilters_ = null;
                    }
                    return this.intentFiltersBuilder_;
                }

                private void ensureExitPointsIsMutable() {
                    if ((this.bitField0_ & 4096) != 4096) {
                        this.exitPoints_ = new ArrayList(this.exitPoints_);
                        this.bitField0_ |= 4096;
                    }
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<ExitPoint> getExitPointsList() {
                    if (this.exitPointsBuilder_ == null) {
                        return Collections.unmodifiableList(this.exitPoints_);
                    }
                    return this.exitPointsBuilder_.getMessageList();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public int getExitPointsCount() {
                    if (this.exitPointsBuilder_ == null) {
                        return this.exitPoints_.size();
                    }
                    return this.exitPointsBuilder_.getCount();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ExitPoint getExitPoints(int index) {
                    if (this.exitPointsBuilder_ == null) {
                        return this.exitPoints_.get(index);
                    }
                    return this.exitPointsBuilder_.getMessage(index);
                }

                public Builder setExitPoints(int index, ExitPoint value) {
                    if (this.exitPointsBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExitPointsIsMutable();
                        this.exitPoints_.set(index, value);
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.setMessage(index, value);
                    }
                    return this;
                }

                public Builder setExitPoints(int index, ExitPoint.Builder builderForValue) {
                    if (this.exitPointsBuilder_ == null) {
                        ensureExitPointsIsMutable();
                        this.exitPoints_.set(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.setMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addExitPoints(ExitPoint value) {
                    if (this.exitPointsBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExitPointsIsMutable();
                        this.exitPoints_.add(value);
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.addMessage(value);
                    }
                    return this;
                }

                public Builder addExitPoints(int index, ExitPoint value) {
                    if (this.exitPointsBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        ensureExitPointsIsMutable();
                        this.exitPoints_.add(index, value);
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.addMessage(index, value);
                    }
                    return this;
                }

                public Builder addExitPoints(ExitPoint.Builder builderForValue) {
                    if (this.exitPointsBuilder_ == null) {
                        ensureExitPointsIsMutable();
                        this.exitPoints_.add(builderForValue.build());
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.addMessage(builderForValue.build());
                    }
                    return this;
                }

                public Builder addExitPoints(int index, ExitPoint.Builder builderForValue) {
                    if (this.exitPointsBuilder_ == null) {
                        ensureExitPointsIsMutable();
                        this.exitPoints_.add(index, builderForValue.build());
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.addMessage(index, builderForValue.build());
                    }
                    return this;
                }

                public Builder addAllExitPoints(Iterable<? extends ExitPoint> values) {
                    if (this.exitPointsBuilder_ == null) {
                        ensureExitPointsIsMutable();
                        GeneratedMessage.Builder.addAll((Iterable) values, (List) this.exitPoints_);
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.addAllMessages(values);
                    }
                    return this;
                }

                public Builder clearExitPoints() {
                    if (this.exitPointsBuilder_ == null) {
                        this.exitPoints_ = Collections.emptyList();
                        this.bitField0_ &= -4097;
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.clear();
                    }
                    return this;
                }

                public Builder removeExitPoints(int index) {
                    if (this.exitPointsBuilder_ == null) {
                        ensureExitPointsIsMutable();
                        this.exitPoints_.remove(index);
                        onChanged();
                    } else {
                        this.exitPointsBuilder_.remove(index);
                    }
                    return this;
                }

                public ExitPoint.Builder getExitPointsBuilder(int index) {
                    return getExitPointsFieldBuilder().getBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public ExitPointOrBuilder getExitPointsOrBuilder(int index) {
                    if (this.exitPointsBuilder_ == null) {
                        return this.exitPoints_.get(index);
                    }
                    return this.exitPointsBuilder_.getMessageOrBuilder(index);
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public List<? extends ExitPointOrBuilder> getExitPointsOrBuilderList() {
                    if (this.exitPointsBuilder_ != null) {
                        return this.exitPointsBuilder_.getMessageOrBuilderList();
                    }
                    return Collections.unmodifiableList(this.exitPoints_);
                }

                public ExitPoint.Builder addExitPointsBuilder() {
                    return getExitPointsFieldBuilder().addBuilder(ExitPoint.getDefaultInstance());
                }

                public ExitPoint.Builder addExitPointsBuilder(int index) {
                    return getExitPointsFieldBuilder().addBuilder(index, ExitPoint.getDefaultInstance());
                }

                public List<ExitPoint.Builder> getExitPointsBuilderList() {
                    return getExitPointsFieldBuilder().getBuilderList();
                }

                private RepeatedFieldBuilder<ExitPoint, ExitPoint.Builder, ExitPointOrBuilder> getExitPointsFieldBuilder() {
                    if (this.exitPointsBuilder_ == null) {
                        this.exitPointsBuilder_ = new RepeatedFieldBuilder<>(this.exitPoints_, (this.bitField0_ & 4096) == 4096, getParentForChildren(), isClean());
                        this.exitPoints_ = null;
                    }
                    return this.exitPointsBuilder_;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public boolean hasRegistrationInstruction() {
                    return (this.bitField0_ & 8192) == 8192;
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public Instruction getRegistrationInstruction() {
                    if (this.registrationInstructionBuilder_ == null) {
                        return this.registrationInstruction_;
                    }
                    return this.registrationInstructionBuilder_.getMessage();
                }

                public Builder setRegistrationInstruction(Instruction value) {
                    if (this.registrationInstructionBuilder_ == null) {
                        if (value == null) {
                            throw new NullPointerException();
                        }
                        this.registrationInstruction_ = value;
                        onChanged();
                    } else {
                        this.registrationInstructionBuilder_.setMessage(value);
                    }
                    this.bitField0_ |= 8192;
                    return this;
                }

                public Builder setRegistrationInstruction(Instruction.Builder builderForValue) {
                    if (this.registrationInstructionBuilder_ == null) {
                        this.registrationInstruction_ = builderForValue.build();
                        onChanged();
                    } else {
                        this.registrationInstructionBuilder_.setMessage(builderForValue.build());
                    }
                    this.bitField0_ |= 8192;
                    return this;
                }

                public Builder mergeRegistrationInstruction(Instruction value) {
                    if (this.registrationInstructionBuilder_ == null) {
                        if ((this.bitField0_ & 8192) == 8192 && this.registrationInstruction_ != Instruction.getDefaultInstance()) {
                            this.registrationInstruction_ = Instruction.newBuilder(this.registrationInstruction_).mergeFrom(value).buildPartial();
                        } else {
                            this.registrationInstruction_ = value;
                        }
                        onChanged();
                    } else {
                        this.registrationInstructionBuilder_.mergeFrom(value);
                    }
                    this.bitField0_ |= 8192;
                    return this;
                }

                public Builder clearRegistrationInstruction() {
                    if (this.registrationInstructionBuilder_ == null) {
                        this.registrationInstruction_ = Instruction.getDefaultInstance();
                        onChanged();
                    } else {
                        this.registrationInstructionBuilder_.clear();
                    }
                    this.bitField0_ &= -8193;
                    return this;
                }

                public Instruction.Builder getRegistrationInstructionBuilder() {
                    this.bitField0_ |= 8192;
                    onChanged();
                    return getRegistrationInstructionFieldBuilder().getBuilder();
                }

                @Override // soot.jimple.infoflow.android.iccta.Ic3Data.Application.ComponentOrBuilder
                public InstructionOrBuilder getRegistrationInstructionOrBuilder() {
                    if (this.registrationInstructionBuilder_ != null) {
                        return this.registrationInstructionBuilder_.getMessageOrBuilder();
                    }
                    return this.registrationInstruction_;
                }

                private SingleFieldBuilder<Instruction, Instruction.Builder, InstructionOrBuilder> getRegistrationInstructionFieldBuilder() {
                    if (this.registrationInstructionBuilder_ == null) {
                        this.registrationInstructionBuilder_ = new SingleFieldBuilder<>(this.registrationInstruction_, getParentForChildren(), isClean());
                        this.registrationInstruction_ = null;
                    }
                    return this.registrationInstructionBuilder_;
                }
            }

            public App getApp() {
                return this.app;
            }

            public void setApp(App app) {
                this.app = app;
            }
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public boolean hasName() {
            return (this.bitField0_ & 1) == 1;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public String getName() {
            Object ref = this.name_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.name_ = s;
            }
            return s;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public ByteString getNameBytes() {
            Object ref = this.name_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.name_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public boolean hasVersion() {
            return (this.bitField0_ & 2) == 2;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public int getVersion() {
            return this.version_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public List<Permission> getPermissionsList() {
            return this.permissions_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public List<? extends PermissionOrBuilder> getPermissionsOrBuilderList() {
            return this.permissions_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public int getPermissionsCount() {
            return this.permissions_.size();
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public Permission getPermissions(int index) {
            return this.permissions_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public PermissionOrBuilder getPermissionsOrBuilder(int index) {
            return this.permissions_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public List<String> getUsedPermissionsList() {
            return this.usedPermissions_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public int getUsedPermissionsCount() {
            return this.usedPermissions_.size();
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public String getUsedPermissions(int index) {
            return (String) this.usedPermissions_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public ByteString getUsedPermissionsBytes(int index) {
            return this.usedPermissions_.getByteString(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public List<Component> getComponentsList() {
            return this.components_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public List<? extends ComponentOrBuilder> getComponentsOrBuilderList() {
            return this.components_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public int getComponentsCount() {
            return this.components_.size();
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public Component getComponents(int index) {
            return this.components_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public ComponentOrBuilder getComponentsOrBuilder(int index) {
            return this.components_.get(index);
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public boolean hasAnalysisStart() {
            return (this.bitField0_ & 4) == 4;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public long getAnalysisStart() {
            return this.analysisStart_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public boolean hasAnalysisEnd() {
            return (this.bitField0_ & 8) == 8;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public long getAnalysisEnd() {
            return this.analysisEnd_;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public boolean hasSample() {
            return (this.bitField0_ & 16) == 16;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public String getSample() {
            Object ref = this.sample_;
            if (ref instanceof String) {
                return (String) ref;
            }
            ByteString bs = (ByteString) ref;
            String s = bs.toStringUtf8();
            if (bs.isValidUtf8()) {
                this.sample_ = s;
            }
            return s;
        }

        @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
        public ByteString getSampleBytes() {
            Object ref = this.sample_;
            if (ref instanceof String) {
                ByteString b = ByteString.copyFromUtf8((String) ref);
                this.sample_ = b;
                return b;
            }
            return (ByteString) ref;
        }

        private void initFields() {
            this.name_ = "";
            this.version_ = 0;
            this.permissions_ = Collections.emptyList();
            this.usedPermissions_ = LazyStringArrayList.EMPTY;
            this.components_ = Collections.emptyList();
            this.analysisStart_ = 0L;
            this.analysisEnd_ = 0L;
            this.sample_ = "";
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLiteOrBuilder
        public final boolean isInitialized() {
            byte isInitialized = this.memoizedIsInitialized;
            if (isInitialized != -1) {
                return isInitialized == 1;
            }
            this.memoizedIsInitialized = (byte) 1;
            return true;
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public void writeTo(CodedOutputStream output) throws IOException {
            getSerializedSize();
            if ((this.bitField0_ & 1) == 1) {
                output.writeBytes(1, getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                output.writeUInt32(2, this.version_);
            }
            for (int i = 0; i < this.permissions_.size(); i++) {
                output.writeMessage(3, this.permissions_.get(i));
            }
            for (int i2 = 0; i2 < this.usedPermissions_.size(); i2++) {
                output.writeBytes(4, this.usedPermissions_.getByteString(i2));
            }
            for (int i3 = 0; i3 < this.components_.size(); i3++) {
                output.writeMessage(5, this.components_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                output.writeInt64(6, this.analysisStart_);
            }
            if ((this.bitField0_ & 8) == 8) {
                output.writeInt64(7, this.analysisEnd_);
            }
            if ((this.bitField0_ & 16) == 16) {
                output.writeBytes(8, getSampleBytes());
            }
            getUnknownFields().writeTo(output);
        }

        @Override // com.google.protobuf.GeneratedMessage, com.google.protobuf.AbstractMessage, com.google.protobuf.MessageLite
        public int getSerializedSize() {
            int size = this.memoizedSerializedSize;
            if (size != -1) {
                return size;
            }
            int size2 = 0;
            if ((this.bitField0_ & 1) == 1) {
                size2 = 0 + CodedOutputStream.computeBytesSize(1, getNameBytes());
            }
            if ((this.bitField0_ & 2) == 2) {
                size2 += CodedOutputStream.computeUInt32Size(2, this.version_);
            }
            for (int i = 0; i < this.permissions_.size(); i++) {
                size2 += CodedOutputStream.computeMessageSize(3, this.permissions_.get(i));
            }
            int dataSize = 0;
            for (int i2 = 0; i2 < this.usedPermissions_.size(); i2++) {
                dataSize += CodedOutputStream.computeBytesSizeNoTag(this.usedPermissions_.getByteString(i2));
            }
            int size3 = size2 + dataSize + (1 * getUsedPermissionsList().size());
            for (int i3 = 0; i3 < this.components_.size(); i3++) {
                size3 += CodedOutputStream.computeMessageSize(5, this.components_.get(i3));
            }
            if ((this.bitField0_ & 4) == 4) {
                size3 += CodedOutputStream.computeInt64Size(6, this.analysisStart_);
            }
            if ((this.bitField0_ & 8) == 8) {
                size3 += CodedOutputStream.computeInt64Size(7, this.analysisEnd_);
            }
            if ((this.bitField0_ & 16) == 16) {
                size3 += CodedOutputStream.computeBytesSize(8, getSampleBytes());
            }
            int size4 = size3 + getUnknownFields().getSerializedSize();
            this.memoizedSerializedSize = size4;
            return size4;
        }

        @Override // com.google.protobuf.GeneratedMessage
        protected Object writeReplace() throws ObjectStreamException {
            return super.writeReplace();
        }

        public static Application parseFrom(ByteString data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Application parseFrom(ByteString data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Application parseFrom(byte[] data) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data);
        }

        public static Application parseFrom(byte[] data, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
            return PARSER.parseFrom(data, extensionRegistry);
        }

        public static Application parseFrom(InputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Application parseFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Application parseDelimitedFrom(InputStream input) throws IOException {
            return PARSER.parseDelimitedFrom(input);
        }

        public static Application parseDelimitedFrom(InputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseDelimitedFrom(input, extensionRegistry);
        }

        public static Application parseFrom(CodedInputStream input) throws IOException {
            return PARSER.parseFrom(input);
        }

        public static Application parseFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
            return PARSER.parseFrom(input, extensionRegistry);
        }

        public static Builder newBuilder() {
            return Builder.create();
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder newBuilderForType() {
            return newBuilder();
        }

        public static Builder newBuilder(Application prototype) {
            return newBuilder().mergeFrom(prototype);
        }

        @Override // com.google.protobuf.MessageLite, com.google.protobuf.Message
        public Builder toBuilder() {
            return newBuilder(this);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // com.google.protobuf.GeneratedMessage
        public Builder newBuilderForType(GeneratedMessage.BuilderParent parent) {
            Builder builder = new Builder(parent, null);
            return builder;
        }

        /* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/iccta/Ic3Data$Application$Builder.class */
        public static final class Builder extends GeneratedMessage.Builder<Builder> implements ApplicationOrBuilder {
            private int bitField0_;
            private Object name_;
            private int version_;
            private List<Permission> permissions_;
            private RepeatedFieldBuilder<Permission, Permission.Builder, PermissionOrBuilder> permissionsBuilder_;
            private LazyStringList usedPermissions_;
            private List<Component> components_;
            private RepeatedFieldBuilder<Component, Component.Builder, ComponentOrBuilder> componentsBuilder_;
            private long analysisStart_;
            private long analysisEnd_;
            private Object sample_;

            public static final Descriptors.Descriptor getDescriptor() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder
            protected GeneratedMessage.FieldAccessorTable internalGetFieldAccessorTable() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_fieldAccessorTable.ensureFieldAccessorsInitialized(Application.class, Builder.class);
            }

            private Builder() {
                this.name_ = "";
                this.permissions_ = Collections.emptyList();
                this.usedPermissions_ = LazyStringArrayList.EMPTY;
                this.components_ = Collections.emptyList();
                this.sample_ = "";
                maybeForceBuilderInitialization();
            }

            /* synthetic */ Builder(GeneratedMessage.BuilderParent builderParent, Builder builder) {
                this(builderParent);
            }

            private Builder(GeneratedMessage.BuilderParent parent) {
                super(parent);
                this.name_ = "";
                this.permissions_ = Collections.emptyList();
                this.usedPermissions_ = LazyStringArrayList.EMPTY;
                this.components_ = Collections.emptyList();
                this.sample_ = "";
                maybeForceBuilderInitialization();
            }

            private void maybeForceBuilderInitialization() {
                if (Application.alwaysUseFieldBuilders) {
                    getPermissionsFieldBuilder();
                    getComponentsFieldBuilder();
                }
            }

            /* JADX INFO: Access modifiers changed from: private */
            public static Builder create() {
                return new Builder();
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clear() {
                super.clear();
                this.name_ = "";
                this.bitField0_ &= -2;
                this.version_ = 0;
                this.bitField0_ &= -3;
                if (this.permissionsBuilder_ == null) {
                    this.permissions_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                } else {
                    this.permissionsBuilder_.clear();
                }
                this.usedPermissions_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -9;
                if (this.componentsBuilder_ == null) {
                    this.components_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                } else {
                    this.componentsBuilder_.clear();
                }
                this.analysisStart_ = 0L;
                this.bitField0_ &= -33;
                this.analysisEnd_ = 0L;
                this.bitField0_ &= -65;
                this.sample_ = "";
                this.bitField0_ &= -129;
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder clone() {
                return create().mergeFrom(buildPartial());
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.Message.Builder, com.google.protobuf.MessageOrBuilder
            public Descriptors.Descriptor getDescriptorForType() {
                return Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor;
            }

            @Override // com.google.protobuf.MessageLiteOrBuilder, com.google.protobuf.MessageOrBuilder
            public Application getDefaultInstanceForType() {
                return Application.getDefaultInstance();
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Application build() {
                Application result = buildPartial();
                if (!result.isInitialized()) {
                    throw newUninitializedMessageException((Message) result);
                }
                return result;
            }

            @Override // com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Application buildPartial() {
                Application result = new Application(this, (Application) null);
                int from_bitField0_ = this.bitField0_;
                int to_bitField0_ = 0;
                if ((from_bitField0_ & 1) == 1) {
                    to_bitField0_ = 0 | 1;
                }
                result.name_ = this.name_;
                if ((from_bitField0_ & 2) == 2) {
                    to_bitField0_ |= 2;
                }
                result.version_ = this.version_;
                if (this.permissionsBuilder_ != null) {
                    result.permissions_ = this.permissionsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 4) == 4) {
                        this.permissions_ = Collections.unmodifiableList(this.permissions_);
                        this.bitField0_ &= -5;
                    }
                    result.permissions_ = this.permissions_;
                }
                if ((this.bitField0_ & 8) == 8) {
                    this.usedPermissions_ = new UnmodifiableLazyStringList(this.usedPermissions_);
                    this.bitField0_ &= -9;
                }
                result.usedPermissions_ = this.usedPermissions_;
                if (this.componentsBuilder_ != null) {
                    result.components_ = this.componentsBuilder_.build();
                } else {
                    if ((this.bitField0_ & 16) == 16) {
                        this.components_ = Collections.unmodifiableList(this.components_);
                        this.bitField0_ &= -17;
                    }
                    result.components_ = this.components_;
                }
                if ((from_bitField0_ & 32) == 32) {
                    to_bitField0_ |= 4;
                }
                result.analysisStart_ = this.analysisStart_;
                if ((from_bitField0_ & 64) == 64) {
                    to_bitField0_ |= 8;
                }
                result.analysisEnd_ = this.analysisEnd_;
                if ((from_bitField0_ & 128) == 128) {
                    to_bitField0_ |= 16;
                }
                result.sample_ = this.sample_;
                result.bitField0_ = to_bitField0_;
                onBuilt();
                return result;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(Message other) {
                if (other instanceof Application) {
                    return mergeFrom((Application) other);
                }
                super.mergeFrom(other);
                return this;
            }

            public Builder mergeFrom(Application other) {
                RepeatedFieldBuilder<Permission, Permission.Builder, PermissionOrBuilder> repeatedFieldBuilder;
                RepeatedFieldBuilder<Component, Component.Builder, ComponentOrBuilder> repeatedFieldBuilder2;
                if (other == Application.getDefaultInstance()) {
                    return this;
                }
                if (other.hasName()) {
                    this.bitField0_ |= 1;
                    this.name_ = other.name_;
                    onChanged();
                }
                if (other.hasVersion()) {
                    setVersion(other.getVersion());
                }
                if (this.permissionsBuilder_ == null) {
                    if (!other.permissions_.isEmpty()) {
                        if (this.permissions_.isEmpty()) {
                            this.permissions_ = other.permissions_;
                            this.bitField0_ &= -5;
                        } else {
                            ensurePermissionsIsMutable();
                            this.permissions_.addAll(other.permissions_);
                        }
                        onChanged();
                    }
                } else if (!other.permissions_.isEmpty()) {
                    if (!this.permissionsBuilder_.isEmpty()) {
                        this.permissionsBuilder_.addAllMessages(other.permissions_);
                    } else {
                        this.permissionsBuilder_.dispose();
                        this.permissionsBuilder_ = null;
                        this.permissions_ = other.permissions_;
                        this.bitField0_ &= -5;
                        if (Application.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder = getPermissionsFieldBuilder();
                        } else {
                            repeatedFieldBuilder = null;
                        }
                        this.permissionsBuilder_ = repeatedFieldBuilder;
                    }
                }
                if (!other.usedPermissions_.isEmpty()) {
                    if (this.usedPermissions_.isEmpty()) {
                        this.usedPermissions_ = other.usedPermissions_;
                        this.bitField0_ &= -9;
                    } else {
                        ensureUsedPermissionsIsMutable();
                        this.usedPermissions_.addAll(other.usedPermissions_);
                    }
                    onChanged();
                }
                if (this.componentsBuilder_ == null) {
                    if (!other.components_.isEmpty()) {
                        if (this.components_.isEmpty()) {
                            this.components_ = other.components_;
                            this.bitField0_ &= -17;
                        } else {
                            ensureComponentsIsMutable();
                            this.components_.addAll(other.components_);
                        }
                        onChanged();
                    }
                } else if (!other.components_.isEmpty()) {
                    if (!this.componentsBuilder_.isEmpty()) {
                        this.componentsBuilder_.addAllMessages(other.components_);
                    } else {
                        this.componentsBuilder_.dispose();
                        this.componentsBuilder_ = null;
                        this.components_ = other.components_;
                        this.bitField0_ &= -17;
                        if (Application.alwaysUseFieldBuilders) {
                            repeatedFieldBuilder2 = getComponentsFieldBuilder();
                        } else {
                            repeatedFieldBuilder2 = null;
                        }
                        this.componentsBuilder_ = repeatedFieldBuilder2;
                    }
                }
                if (other.hasAnalysisStart()) {
                    setAnalysisStart(other.getAnalysisStart());
                }
                if (other.hasAnalysisEnd()) {
                    setAnalysisEnd(other.getAnalysisEnd());
                }
                if (other.hasSample()) {
                    this.bitField0_ |= 128;
                    this.sample_ = other.sample_;
                    onChanged();
                }
                mergeUnknownFields(other.getUnknownFields());
                return this;
            }

            @Override // com.google.protobuf.GeneratedMessage.Builder, com.google.protobuf.MessageLiteOrBuilder
            public final boolean isInitialized() {
                return true;
            }

            @Override // com.google.protobuf.AbstractMessage.Builder, com.google.protobuf.AbstractMessageLite.Builder, com.google.protobuf.MessageLite.Builder, com.google.protobuf.Message.Builder
            public Builder mergeFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws IOException {
                Application parsedMessage = null;
                try {
                    try {
                        parsedMessage = Application.PARSER.parsePartialFrom(input, extensionRegistry);
                        if (parsedMessage != null) {
                            mergeFrom(parsedMessage);
                        }
                        return this;
                    } catch (InvalidProtocolBufferException e) {
                        Application application = (Application) e.getUnfinishedMessage();
                        throw e;
                    }
                } catch (Throwable th) {
                    if (parsedMessage != null) {
                        mergeFrom(parsedMessage);
                    }
                    throw th;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public boolean hasName() {
                return (this.bitField0_ & 1) == 1;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public String getName() {
                Object ref = this.name_;
                if (!(ref instanceof String)) {
                    String s = ((ByteString) ref).toStringUtf8();
                    this.name_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
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
                this.bitField0_ |= 1;
                this.name_ = value;
                onChanged();
                return this;
            }

            public Builder clearName() {
                this.bitField0_ &= -2;
                this.name_ = Application.getDefaultInstance().getName();
                onChanged();
                return this;
            }

            public Builder setNameBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 1;
                this.name_ = value;
                onChanged();
                return this;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public boolean hasVersion() {
                return (this.bitField0_ & 2) == 2;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public int getVersion() {
                return this.version_;
            }

            public Builder setVersion(int value) {
                this.bitField0_ |= 2;
                this.version_ = value;
                onChanged();
                return this;
            }

            public Builder clearVersion() {
                this.bitField0_ &= -3;
                this.version_ = 0;
                onChanged();
                return this;
            }

            private void ensurePermissionsIsMutable() {
                if ((this.bitField0_ & 4) != 4) {
                    this.permissions_ = new ArrayList(this.permissions_);
                    this.bitField0_ |= 4;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public List<Permission> getPermissionsList() {
                if (this.permissionsBuilder_ == null) {
                    return Collections.unmodifiableList(this.permissions_);
                }
                return this.permissionsBuilder_.getMessageList();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public int getPermissionsCount() {
                if (this.permissionsBuilder_ == null) {
                    return this.permissions_.size();
                }
                return this.permissionsBuilder_.getCount();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public Permission getPermissions(int index) {
                if (this.permissionsBuilder_ == null) {
                    return this.permissions_.get(index);
                }
                return this.permissionsBuilder_.getMessage(index);
            }

            public Builder setPermissions(int index, Permission value) {
                if (this.permissionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePermissionsIsMutable();
                    this.permissions_.set(index, value);
                    onChanged();
                } else {
                    this.permissionsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setPermissions(int index, Permission.Builder builderForValue) {
                if (this.permissionsBuilder_ == null) {
                    ensurePermissionsIsMutable();
                    this.permissions_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.permissionsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addPermissions(Permission value) {
                if (this.permissionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePermissionsIsMutable();
                    this.permissions_.add(value);
                    onChanged();
                } else {
                    this.permissionsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addPermissions(int index, Permission value) {
                if (this.permissionsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensurePermissionsIsMutable();
                    this.permissions_.add(index, value);
                    onChanged();
                } else {
                    this.permissionsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addPermissions(Permission.Builder builderForValue) {
                if (this.permissionsBuilder_ == null) {
                    ensurePermissionsIsMutable();
                    this.permissions_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.permissionsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addPermissions(int index, Permission.Builder builderForValue) {
                if (this.permissionsBuilder_ == null) {
                    ensurePermissionsIsMutable();
                    this.permissions_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.permissionsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllPermissions(Iterable<? extends Permission> values) {
                if (this.permissionsBuilder_ == null) {
                    ensurePermissionsIsMutable();
                    GeneratedMessage.Builder.addAll((Iterable) values, (List) this.permissions_);
                    onChanged();
                } else {
                    this.permissionsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearPermissions() {
                if (this.permissionsBuilder_ == null) {
                    this.permissions_ = Collections.emptyList();
                    this.bitField0_ &= -5;
                    onChanged();
                } else {
                    this.permissionsBuilder_.clear();
                }
                return this;
            }

            public Builder removePermissions(int index) {
                if (this.permissionsBuilder_ == null) {
                    ensurePermissionsIsMutable();
                    this.permissions_.remove(index);
                    onChanged();
                } else {
                    this.permissionsBuilder_.remove(index);
                }
                return this;
            }

            public Permission.Builder getPermissionsBuilder(int index) {
                return getPermissionsFieldBuilder().getBuilder(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public PermissionOrBuilder getPermissionsOrBuilder(int index) {
                if (this.permissionsBuilder_ == null) {
                    return this.permissions_.get(index);
                }
                return this.permissionsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public List<? extends PermissionOrBuilder> getPermissionsOrBuilderList() {
                if (this.permissionsBuilder_ != null) {
                    return this.permissionsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.permissions_);
            }

            public Permission.Builder addPermissionsBuilder() {
                return getPermissionsFieldBuilder().addBuilder(Permission.getDefaultInstance());
            }

            public Permission.Builder addPermissionsBuilder(int index) {
                return getPermissionsFieldBuilder().addBuilder(index, Permission.getDefaultInstance());
            }

            public List<Permission.Builder> getPermissionsBuilderList() {
                return getPermissionsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilder<Permission, Permission.Builder, PermissionOrBuilder> getPermissionsFieldBuilder() {
                if (this.permissionsBuilder_ == null) {
                    this.permissionsBuilder_ = new RepeatedFieldBuilder<>(this.permissions_, (this.bitField0_ & 4) == 4, getParentForChildren(), isClean());
                    this.permissions_ = null;
                }
                return this.permissionsBuilder_;
            }

            private void ensureUsedPermissionsIsMutable() {
                if ((this.bitField0_ & 8) != 8) {
                    this.usedPermissions_ = new LazyStringArrayList(this.usedPermissions_);
                    this.bitField0_ |= 8;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public List<String> getUsedPermissionsList() {
                return Collections.unmodifiableList(this.usedPermissions_);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public int getUsedPermissionsCount() {
                return this.usedPermissions_.size();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public String getUsedPermissions(int index) {
                return (String) this.usedPermissions_.get(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public ByteString getUsedPermissionsBytes(int index) {
                return this.usedPermissions_.getByteString(index);
            }

            public Builder setUsedPermissions(int index, String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsedPermissionsIsMutable();
                this.usedPermissions_.set(index, value);
                onChanged();
                return this;
            }

            public Builder addUsedPermissions(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsedPermissionsIsMutable();
                this.usedPermissions_.add(value);
                onChanged();
                return this;
            }

            public Builder addAllUsedPermissions(Iterable<String> values) {
                ensureUsedPermissionsIsMutable();
                GeneratedMessage.Builder.addAll((Iterable) values, (List) this.usedPermissions_);
                onChanged();
                return this;
            }

            public Builder clearUsedPermissions() {
                this.usedPermissions_ = LazyStringArrayList.EMPTY;
                this.bitField0_ &= -9;
                onChanged();
                return this;
            }

            public Builder addUsedPermissionsBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                ensureUsedPermissionsIsMutable();
                this.usedPermissions_.add(value);
                onChanged();
                return this;
            }

            private void ensureComponentsIsMutable() {
                if ((this.bitField0_ & 16) != 16) {
                    this.components_ = new ArrayList(this.components_);
                    this.bitField0_ |= 16;
                }
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public List<Component> getComponentsList() {
                if (this.componentsBuilder_ == null) {
                    return Collections.unmodifiableList(this.components_);
                }
                return this.componentsBuilder_.getMessageList();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public int getComponentsCount() {
                if (this.componentsBuilder_ == null) {
                    return this.components_.size();
                }
                return this.componentsBuilder_.getCount();
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public Component getComponents(int index) {
                if (this.componentsBuilder_ == null) {
                    return this.components_.get(index);
                }
                return this.componentsBuilder_.getMessage(index);
            }

            public Builder setComponents(int index, Component value) {
                if (this.componentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureComponentsIsMutable();
                    this.components_.set(index, value);
                    onChanged();
                } else {
                    this.componentsBuilder_.setMessage(index, value);
                }
                return this;
            }

            public Builder setComponents(int index, Component.Builder builderForValue) {
                if (this.componentsBuilder_ == null) {
                    ensureComponentsIsMutable();
                    this.components_.set(index, builderForValue.build());
                    onChanged();
                } else {
                    this.componentsBuilder_.setMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addComponents(Component value) {
                if (this.componentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureComponentsIsMutable();
                    this.components_.add(value);
                    onChanged();
                } else {
                    this.componentsBuilder_.addMessage(value);
                }
                return this;
            }

            public Builder addComponents(int index, Component value) {
                if (this.componentsBuilder_ == null) {
                    if (value == null) {
                        throw new NullPointerException();
                    }
                    ensureComponentsIsMutable();
                    this.components_.add(index, value);
                    onChanged();
                } else {
                    this.componentsBuilder_.addMessage(index, value);
                }
                return this;
            }

            public Builder addComponents(Component.Builder builderForValue) {
                if (this.componentsBuilder_ == null) {
                    ensureComponentsIsMutable();
                    this.components_.add(builderForValue.build());
                    onChanged();
                } else {
                    this.componentsBuilder_.addMessage(builderForValue.build());
                }
                return this;
            }

            public Builder addComponents(int index, Component.Builder builderForValue) {
                if (this.componentsBuilder_ == null) {
                    ensureComponentsIsMutable();
                    this.components_.add(index, builderForValue.build());
                    onChanged();
                } else {
                    this.componentsBuilder_.addMessage(index, builderForValue.build());
                }
                return this;
            }

            public Builder addAllComponents(Iterable<? extends Component> values) {
                if (this.componentsBuilder_ == null) {
                    ensureComponentsIsMutable();
                    GeneratedMessage.Builder.addAll((Iterable) values, (List) this.components_);
                    onChanged();
                } else {
                    this.componentsBuilder_.addAllMessages(values);
                }
                return this;
            }

            public Builder clearComponents() {
                if (this.componentsBuilder_ == null) {
                    this.components_ = Collections.emptyList();
                    this.bitField0_ &= -17;
                    onChanged();
                } else {
                    this.componentsBuilder_.clear();
                }
                return this;
            }

            public Builder removeComponents(int index) {
                if (this.componentsBuilder_ == null) {
                    ensureComponentsIsMutable();
                    this.components_.remove(index);
                    onChanged();
                } else {
                    this.componentsBuilder_.remove(index);
                }
                return this;
            }

            public Component.Builder getComponentsBuilder(int index) {
                return getComponentsFieldBuilder().getBuilder(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public ComponentOrBuilder getComponentsOrBuilder(int index) {
                if (this.componentsBuilder_ == null) {
                    return this.components_.get(index);
                }
                return this.componentsBuilder_.getMessageOrBuilder(index);
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public List<? extends ComponentOrBuilder> getComponentsOrBuilderList() {
                if (this.componentsBuilder_ != null) {
                    return this.componentsBuilder_.getMessageOrBuilderList();
                }
                return Collections.unmodifiableList(this.components_);
            }

            public Component.Builder addComponentsBuilder() {
                return getComponentsFieldBuilder().addBuilder(Component.getDefaultInstance());
            }

            public Component.Builder addComponentsBuilder(int index) {
                return getComponentsFieldBuilder().addBuilder(index, Component.getDefaultInstance());
            }

            public List<Component.Builder> getComponentsBuilderList() {
                return getComponentsFieldBuilder().getBuilderList();
            }

            private RepeatedFieldBuilder<Component, Component.Builder, ComponentOrBuilder> getComponentsFieldBuilder() {
                if (this.componentsBuilder_ == null) {
                    this.componentsBuilder_ = new RepeatedFieldBuilder<>(this.components_, (this.bitField0_ & 16) == 16, getParentForChildren(), isClean());
                    this.components_ = null;
                }
                return this.componentsBuilder_;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public boolean hasAnalysisStart() {
                return (this.bitField0_ & 32) == 32;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public long getAnalysisStart() {
                return this.analysisStart_;
            }

            public Builder setAnalysisStart(long value) {
                this.bitField0_ |= 32;
                this.analysisStart_ = value;
                onChanged();
                return this;
            }

            public Builder clearAnalysisStart() {
                this.bitField0_ &= -33;
                this.analysisStart_ = 0L;
                onChanged();
                return this;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public boolean hasAnalysisEnd() {
                return (this.bitField0_ & 64) == 64;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public long getAnalysisEnd() {
                return this.analysisEnd_;
            }

            public Builder setAnalysisEnd(long value) {
                this.bitField0_ |= 64;
                this.analysisEnd_ = value;
                onChanged();
                return this;
            }

            public Builder clearAnalysisEnd() {
                this.bitField0_ &= -65;
                this.analysisEnd_ = 0L;
                onChanged();
                return this;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public boolean hasSample() {
                return (this.bitField0_ & 128) == 128;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public String getSample() {
                Object ref = this.sample_;
                if (!(ref instanceof String)) {
                    String s = ((ByteString) ref).toStringUtf8();
                    this.sample_ = s;
                    return s;
                }
                return (String) ref;
            }

            @Override // soot.jimple.infoflow.android.iccta.Ic3Data.ApplicationOrBuilder
            public ByteString getSampleBytes() {
                Object ref = this.sample_;
                if (ref instanceof String) {
                    ByteString b = ByteString.copyFromUtf8((String) ref);
                    this.sample_ = b;
                    return b;
                }
                return (ByteString) ref;
            }

            public Builder setSample(String value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.sample_ = value;
                onChanged();
                return this;
            }

            public Builder clearSample() {
                this.bitField0_ &= -129;
                this.sample_ = Application.getDefaultInstance().getSample();
                onChanged();
                return this;
            }

            public Builder setSampleBytes(ByteString value) {
                if (value == null) {
                    throw new NullPointerException();
                }
                this.bitField0_ |= 128;
                this.sample_ = value;
                onChanged();
                return this;
            }
        }
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n\u0017protobuf/ic3_data.proto\u0012\u0014edu.psu.cse.siis.ic3\"`\n\tAttribute\u00121\n\u0004kind\u0018\u0001 \u0001(\u000e2#.edu.psu.cse.siis.ic3.AttributeKind\u0012\r\n\u0005value\u0018\u0002 \u0003(\t\u0012\u0011\n\tint_value\u0018\u0003 \u0003(\u0005\"Á\u000e\n\u000bApplication\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012\u000f\n\u0007version\u0018\u0002 \u0001(\r\u0012A\n\u000bpermissions\u0018\u0003 \u0003(\u000b2,.edu.psu.cse.siis.ic3.Application.Permission\u0012\u0018\n\u0010used_permissions\u0018\u0004 \u0003(\t\u0012?\n\ncomponents\u0018\u0005 \u0003(\u000b2+.edu.psu.cse.siis.ic3.Application.Component\u0012\u0016\n\u000eanalysis_start\u0018\u0006 \u0001(\u0003\u0012\u0014\n\fanalysis_end\u0018\u0007 \u0001(\u0003\u0012\u000e\n\u0006", "sample\u0018\b \u0001(\t\u001a©\u0001\n\nPermission\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012A\n\u0005level\u0018\u0002 \u0001(\u000e22.edu.psu.cse.siis.ic3.Application.Permission.Level\"J\n\u0005Level\u0012\n\n\u0006NORMAL\u0010��\u0012\r\n\tDANGEROUS\u0010\u0001\u0012\r\n\tSIGNATURE\u0010\u0002\u0012\u0017\n\u0013SIGNATURE_OR_SYSTEM\u0010\u0003\u001a\u008a\u000b\n\tComponent\u0012\f\n\u0004name\u0018\u0001 \u0001(\t\u0012G\n\u0004kind\u0018\u0002 \u0001(\u000e29.edu.psu.cse.siis.ic3.Application.Component.ComponentKind\u0012\u0010\n\bexported\u0018\u0003 \u0001(\b\u0012\u0012\n\npermission\u0018\u0004 \u0001(\t\u0012\u000f\n\u0007missing\u0018\u0005 \u0001(\r\u0012A\n\u0006extras\u0018\u0006 \u0003(\u000b21.edu.psu.cse.siis.ic3.Application.Componen", "t.Extra\u0012\u0014\n\falias_target\u0018\u0007 \u0001(\t\u0012\u001d\n\u0015grant_uri_permissions\u0018\b \u0001(\b\u0012\u0017\n\u000fread_permission\u0018\t \u0001(\t\u0012\u0018\n\u0010write_permission\u0018\n \u0001(\t\u0012\u0013\n\u000bauthorities\u0018\u000b \u0003(\t\u0012P\n\u000eintent_filters\u0018\f \u0003(\u000b28.edu.psu.cse.siis.ic3.Application.Component.IntentFilter\u0012J\n\u000bexit_points\u0018\r \u0003(\u000b25.edu.psu.cse.siis.ic3.Application.Component.ExitPoint\u0012Y\n\u0018registration_instruction\u0018\u000e \u0001(\u000b27.edu.psu.cse.siis.ic3.Application.Component.Instruction\u001ad\n\u0005Extra\u0012\r\n\u0005extra\u0018", "\u0001 \u0001(\t\u0012L\n\u000binstruction\u0018\u0002 \u0001(\u000b27.edu.psu.cse.siis.ic3.Application.Component.Instruction\u001aC\n\fIntentFilter\u00123\n\nattributes\u0018\u0001 \u0003(\u000b2\u001f.edu.psu.cse.siis.ic3.Attribute\u001aP\n\u000bInstruction\u0012\u0011\n\tstatement\u0018\u0001 \u0001(\t\u0012\u0012\n\nclass_name\u0018\u0002 \u0001(\t\u0012\u000e\n\u0006method\u0018\u0003 \u0001(\t\u0012\n\n\u0002id\u0018\u0004 \u0001(\r\u001aÚ\u0003\n\tExitPoint\u0012L\n\u000binstruction\u0018\u0001 \u0001(\u000b27.edu.psu.cse.siis.ic3.Application.Component.Instruction\u0012G\n\u0004kind\u0018\u0002 \u0001(\u000e29.edu.psu.cse.siis.ic3.Application.Component.ComponentKind\u0012", "\u000f\n\u0007missing\u0018\u0003 \u0001(\r\u0012M\n\u0007intents\u0018\u0004 \u0003(\u000b2<.edu.psu.cse.siis.ic3.Application.Component.ExitPoint.Intent\u0012G\n\u0004uris\u0018\u0005 \u0003(\u000b29.edu.psu.cse.siis.ic3.Application.Component.ExitPoint.Uri\u001aQ\n\u0006Intent\u00123\n\nattributes\u0018\u0001 \u0003(\u000b2\u001f.edu.psu.cse.siis.ic3.Attribute\u0012\u0012\n\npermission\u0018\u0002 \u0001(\t\u001a:\n\u0003Uri\u00123\n\nattributes\u0018\u0001 \u0003(\u000b2\u001f.edu.psu.cse.siis.ic3.Attribute\"\\\n\rComponentKind\u0012\f\n\bACTIVITY\u0010��\u0012\u000b\n\u0007SERVICE\u0010\u0001\u0012\f\n\bRECEIVER\u0010\u0002\u0012\u0014\n\u0010DYNAMIC_RECEIVER\u0010\u0003\u0012\f\n\bPROVI", "DER\u0010\u0004*Ä\u0001\n\rAttributeKind\u0012\n\n\u0006ACTION\u0010��\u0012\f\n\bCATEGORY\u0010\u0001\u0012\u000b\n\u0007PACKAGE\u0010\u0002\u0012\t\n\u0005CLASS\u0010\u0003\u0012\b\n\u0004TYPE\u0010\u0004\u0012\u0007\n\u0003URI\u0010\u0005\u0012\n\n\u0006SCHEME\u0010\u0006\u0012\t\n\u0005EXTRA\u0010\u0007\u0012\r\n\tAUTHORITY\u0010\b\u0012\b\n\u0004HOST\u0010\t\u0012\b\n\u0004PATH\u0010\n\u0012\b\n\u0004PORT\u0010\u000b\u0012\u0007\n\u0003SSP\u0010\f\u0012\t\n\u0005QUERY\u0010\r\u0012\b\n\u0004FLAG\u0010\u000e\u0012\f\n\bPRIORITY\u0010\u000f"};
        Descriptors.FileDescriptor.InternalDescriptorAssigner assigner = new Descriptors.FileDescriptor.InternalDescriptorAssigner() { // from class: soot.jimple.infoflow.android.iccta.Ic3Data.1
            @Override // com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner
            public ExtensionRegistry assignDescriptors(Descriptors.FileDescriptor root) {
                Ic3Data.descriptor = root;
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor = Ic3Data.getDescriptor().getMessageTypes().get(0);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Attribute_descriptor, new String[]{"Kind", XmlConstants.Attributes.value, "IntValue"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor = Ic3Data.getDescriptor().getMessageTypes().get(1);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor, new String[]{"Name", "Version", "Permissions", "UsedPermissions", "Components", "AnalysisStart", "AnalysisEnd", "Sample"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor.getNestedTypes().get(0);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Permission_descriptor, new String[]{"Name", "Level"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_descriptor.getNestedTypes().get(1);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor, new String[]{"Name", "Kind", "Exported", "Permission", "Missing", "Extras", "AliasTarget", "GrantUriPermissions", "ReadPermission", "WritePermission", "Authorities", "IntentFilters", "ExitPoints", "RegistrationInstruction"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor.getNestedTypes().get(0);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Extra_descriptor, new String[]{"Extra", "Instruction"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor.getNestedTypes().get(1);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_IntentFilter_descriptor, new String[]{"Attributes"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor.getNestedTypes().get(2);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_Instruction_descriptor, new String[]{XmlConstants.Attributes.statement, "ClassName", XmlConstants.Attributes.method, "Id"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_descriptor.getNestedTypes().get(3);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor, new String[]{"Instruction", "Kind", "Missing", "Intents", "Uris"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor.getNestedTypes().get(0);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Intent_descriptor, new String[]{"Attributes", "Permission"});
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor = Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_descriptor.getNestedTypes().get(1);
                Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_fieldAccessorTable = new GeneratedMessage.FieldAccessorTable(Ic3Data.internal_static_edu_psu_cse_siis_ic3_Application_Component_ExitPoint_Uri_descriptor, new String[]{"Attributes"});
                return null;
            }
        };
        Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0], assigner);
    }
}
