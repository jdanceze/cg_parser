package com.google.protobuf;

import com.google.protobuf.Descriptors;
import com.google.protobuf.GeneratedMessageV3;
import soot.jimple.infoflow.results.xml.XmlConstants;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/AnyProto.class */
public final class AnyProto {
    static final Descriptors.Descriptor internal_static_google_protobuf_Any_descriptor = getDescriptor().getMessageTypes().get(0);
    static final GeneratedMessageV3.FieldAccessorTable internal_static_google_protobuf_Any_fieldAccessorTable = new GeneratedMessageV3.FieldAccessorTable(internal_static_google_protobuf_Any_descriptor, new String[]{"TypeUrl", XmlConstants.Attributes.value});
    private static Descriptors.FileDescriptor descriptor;

    private AnyProto() {
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
        String[] descriptorData = {"\n\u0019google/protobuf/any.proto\u0012\u000fgoogle.protobuf\"&\n\u0003Any\u0012\u0010\n\btype_url\u0018\u0001 \u0001(\t\u0012\r\n\u0005value\u0018\u0002 \u0001(\fBv\n\u0013com.google.protobufB\bAnyProtoP\u0001Z,google.golang.org/protobuf/types/known/anypb¢\u0002\u0003GPBª\u0002\u001eGoogle.Protobuf.WellKnownTypesb\u0006proto3"};
        descriptor = Descriptors.FileDescriptor.internalBuildGeneratedFileFrom(descriptorData, new Descriptors.FileDescriptor[0]);
    }
}
