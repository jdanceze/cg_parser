package com.google.protobuf;
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/MessageInfo.class */
interface MessageInfo {
    ProtoSyntax getSyntax();

    boolean isMessageSetWireFormat();

    MessageLite getDefaultInstance();
}
