package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/SourceContextProto.class */
public final class SourceContextProto {
    static final Descriptors.Descriptor internal_static_google_protobuf_SourceContext_descriptor = getDescriptor().getMessageTypes().get(0);
    static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_SourceContext_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_SourceContext_descriptor, new String[]{"FileName"});
    private static Descriptors.FileDescriptor descriptor;

    private SourceContextProto() {
    }

    public static void registerAllExtensions(ExtensionRegistryLite registry) {
    }

    public static void registerAllExtensions(ExtensionRegistry registry) {
        registerAllExtensions((ExtensionRegistryLite) registry);
    }

    public static Descriptors.FileDescriptor getDescriptor() {
        return descriptor;
    }

    static {
        String[] descriptorData = {"\n$google/protobuf/source_context.proto\u0012\u000fgoogle.protobuf\"\"\n\rSourceContext\u0012\u0011\n\tfile_name\u0018\u0001 \u0001(\tB\u008a\u0001\n\u0013com.google.protobufB\u0012SourceContextProtoP\u0001Z6google.golang.org/protobuf/types/known/sourcecontextpb¢\u0002\u0003GPBª\u0002\u001eGoogle.Protobuf.WellKnownTypesb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
