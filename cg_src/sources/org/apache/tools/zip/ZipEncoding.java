package org.apache.tools.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/ZipEncoding.class */
public interface ZipEncoding {
    boolean canEncode(String str);

    ByteBuffer encode(String str) throws IOException;

    String decode(byte[] bArr) throws IOException;
}
