package com.google.protobuf;

import com.google.protobuf.GeneratedMessageV3;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/NewInstanceSchemaFull.class */
final class NewInstanceSchemaFull implements NewInstanceSchema {
    NewInstanceSchemaFull() {
    }

    @Override // com.google.protobuf.NewInstanceSchema
    public Object newInstance(Object defaultInstance) {
        return ((GeneratedMessageV3) defaultInstance).newInstance(GeneratedMessageV3.UnusedPrivateParameter.INSTANCE);
    }
}
