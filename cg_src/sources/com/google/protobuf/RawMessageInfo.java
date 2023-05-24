package com.google.protobuf;

import dalvik.bytecode.Opcodes;
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/RawMessageInfo.class */
final class RawMessageInfo implements MessageInfo {
    private final MessageLite defaultInstance;
    private final String info;
    private final Object[] objects;
    private final int flags;

    /* JADX INFO: Access modifiers changed from: package-private */
    public RawMessageInfo(MessageLite defaultInstance, String info, Object[] objects) {
        this.defaultInstance = defaultInstance;
        this.info = info;
        this.objects = objects;
        int position = 0 + 1;
        int value = info.charAt(0);
        if (value < 55296) {
            this.flags = value;
            return;
        }
        int result = value & Opcodes.OP_SPUT_BYTE_JUMBO;
        int shift = 13;
        while (true) {
            int i = position;
            position++;
            int value2 = info.charAt(i);
            if (value2 >= 55296) {
                result |= (value2 & Opcodes.OP_SPUT_BYTE_JUMBO) << shift;
                shift += 13;
            } else {
                this.flags = result | (value2 << shift);
                return;
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public String getStringInfo() {
        return this.info;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Object[] getObjects() {
        return this.objects;
    }

    @Override // com.google.protobuf.MessageInfo
    public MessageLite getDefaultInstance() {
        return this.defaultInstance;
    }

    @Override // com.google.protobuf.MessageInfo
    public ProtoSyntax getSyntax() {
        return (this.flags & 1) == 1 ? ProtoSyntax.PROTO2 : ProtoSyntax.PROTO3;
    }

    @Override // com.google.protobuf.MessageInfo
    public boolean isMessageSetWireFormat() {
        return (this.flags & 2) == 2;
    }
}
