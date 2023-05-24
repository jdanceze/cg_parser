package com.google.protobuf.util;

import com.google.protobuf.Struct;
import com.google.protobuf.Value;
/* loaded from: gencallgraphv3.jar:protobuf-java-util-3.21.2.jar:com/google/protobuf/util/Structs.class */
public final class Structs {
    public static Struct of(String k1, Value v1) {
        return Struct.newBuilder().putFields(k1, v1).build();
    }

    public static Struct of(String k1, Value v1, String k2, Value v2) {
        return Struct.newBuilder().putFields(k1, v1).putFields(k2, v2).build();
    }

    public static Struct of(String k1, Value v1, String k2, Value v2, String k3, Value v3) {
        return Struct.newBuilder().putFields(k1, v1).putFields(k2, v2).putFields(k3, v3).build();
    }

    private Structs() {
    }
}
