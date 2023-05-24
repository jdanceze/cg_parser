package com.google.protobuf;

import com.google.protobuf.MapEntryLite;
import java.io.IOException;
import java.util.List;
import java.util.Map;
/* JADX INFO: Access modifiers changed from: package-private */
@CheckReturnValue
/* loaded from: gencallgraphv3.jar:protobuf-java-3.21.7.jar:com/google/protobuf/Reader.class */
public interface Reader {
    public static final int READ_DONE = Integer.MAX_VALUE;
    public static final int TAG_UNKNOWN = 0;

    boolean shouldDiscardUnknownFields();

    int getFieldNumber() throws IOException;

    int getTag();

    boolean skipField() throws IOException;

    double readDouble() throws IOException;

    float readFloat() throws IOException;

    long readUInt64() throws IOException;

    long readInt64() throws IOException;

    int readInt32() throws IOException;

    long readFixed64() throws IOException;

    int readFixed32() throws IOException;

    boolean readBool() throws IOException;

    String readString() throws IOException;

    String readStringRequireUtf8() throws IOException;

    <T> T readMessageBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    <T> T readMessage(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    @Deprecated
    <T> T readGroup(Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    @Deprecated
    <T> T readGroupBySchemaWithCheck(Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    <T> void mergeMessageField(T t, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    <T> void mergeGroupField(T t, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    ByteString readBytes() throws IOException;

    int readUInt32() throws IOException;

    int readEnum() throws IOException;

    int readSFixed32() throws IOException;

    long readSFixed64() throws IOException;

    int readSInt32() throws IOException;

    long readSInt64() throws IOException;

    void readDoubleList(List<Double> list) throws IOException;

    void readFloatList(List<Float> list) throws IOException;

    void readUInt64List(List<Long> list) throws IOException;

    void readInt64List(List<Long> list) throws IOException;

    void readInt32List(List<Integer> list) throws IOException;

    void readFixed64List(List<Long> list) throws IOException;

    void readFixed32List(List<Integer> list) throws IOException;

    void readBoolList(List<Boolean> list) throws IOException;

    void readStringList(List<String> list) throws IOException;

    void readStringListRequireUtf8(List<String> list) throws IOException;

    <T> void readMessageList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    <T> void readMessageList(List<T> list, Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    @Deprecated
    <T> void readGroupList(List<T> list, Class<T> cls, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    @Deprecated
    <T> void readGroupList(List<T> list, Schema<T> schema, ExtensionRegistryLite extensionRegistryLite) throws IOException;

    void readBytesList(List<ByteString> list) throws IOException;

    void readUInt32List(List<Integer> list) throws IOException;

    void readEnumList(List<Integer> list) throws IOException;

    void readSFixed32List(List<Integer> list) throws IOException;

    void readSFixed64List(List<Long> list) throws IOException;

    void readSInt32List(List<Integer> list) throws IOException;

    void readSInt64List(List<Long> list) throws IOException;

    <K, V> void readMap(Map<K, V> map, MapEntryLite.Metadata<K, V> metadata, ExtensionRegistryLite extensionRegistryLite) throws IOException;
}
