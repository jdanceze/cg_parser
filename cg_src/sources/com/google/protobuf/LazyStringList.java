package com.google.protobuf;

import java.util.Collection;
import java.util.List;
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/LazyStringList.class */
public interface LazyStringList extends ProtocolStringList {
    ByteString getByteString(int i);

    Object getRaw(int i);

    byte[] getByteArray(int i);

    void add(ByteString byteString);

    void add(byte[] bArr);

    void set(int i, ByteString byteString);

    void set(int i, byte[] bArr);

    boolean addAllByteString(Collection<? extends ByteString> collection);

    boolean addAllByteArray(Collection<byte[]> collection);

    List<?> getUnderlyingElements();

    void mergeFrom(LazyStringList lazyStringList);

    List<byte[]> asByteArrayList();

    LazyStringList getUnmodifiableView();
}
