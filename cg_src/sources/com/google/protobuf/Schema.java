package com.google.protobuf;

import com.google.protobuf.ArrayDecoders;
import java.io.IOException;
/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Schema.class */
public interface Schema<T> {
    void writeTo(T t, Writer writer) throws IOException;

    void mergeFrom(T t, Reader reader, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    void mergeFrom(T t, byte[] bArr, int i, int i2, ArrayDecoders.Registers registers) throws IOException;

    void makeImmutable(T t);

    boolean isInitialized(T t);

    T newInstance();

    boolean equals(T t, T t2);

    int hashCode(T t);

    void mergeFrom(T t, T t2);

    int getSerializedSize(T t);
}
