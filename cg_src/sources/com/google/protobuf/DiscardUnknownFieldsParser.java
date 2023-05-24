package com.google.protobuf;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/DiscardUnknownFieldsParser.class */
public final class DiscardUnknownFieldsParser {
    public static final <T extends Message> Parser<T> wrap(final Parser<T> parser) {
        return new AbstractParser<T>() { // from class: com.google.protobuf.DiscardUnknownFieldsParser.1
            /* JADX WARN: Incorrect return type in method signature: (Lcom/google/protobuf/CodedInputStream;Lcom/google/protobuf/ExtensionRegistryLite;)TT; */
            @Override // com.google.protobuf.Parser
            public Message parsePartialFrom(CodedInputStream input, ExtensionRegistryLite extensionRegistry) throws InvalidProtocolBufferException {
                try {
                    input.discardUnknownFields();
                    Message message = (Message) Parser.this.parsePartialFrom(input, extensionRegistry);
                    input.unsetDiscardUnknownFields();
                    return message;
                } catch (Throwable th) {
                    input.unsetDiscardUnknownFields();
                    throw th;
                }
            }
        };
    }

    private DiscardUnknownFieldsParser() {
    }
}
