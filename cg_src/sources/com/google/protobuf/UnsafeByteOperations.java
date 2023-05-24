package com.google.protobuf;

import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/UnsafeByteOperations.class */
public final class UnsafeByteOperations {
    private UnsafeByteOperations() {
    }

    public static ByteString unsafeWrap(byte[] buffer) {
        return ByteString.wrap(buffer);
    }

    public static ByteString unsafeWrap(byte[] buffer, int offset, int length) {
        return ByteString.wrap(buffer, offset, length);
    }

    public static ByteString unsafeWrap(ByteBuffer buffer) {
        return ByteString.wrap(buffer);
    }

    public static void unsafeWriteTo(ByteString bytes, ByteOutput output) throws IOException {
        bytes.writeTo(output);
    }
}
