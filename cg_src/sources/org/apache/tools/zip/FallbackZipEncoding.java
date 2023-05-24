package org.apache.tools.zip;

import java.io.IOException;
import java.nio.ByteBuffer;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/zip/FallbackZipEncoding.class */
class FallbackZipEncoding implements ZipEncoding {
    private final String charset;

    public FallbackZipEncoding() {
        this.charset = null;
    }

    public FallbackZipEncoding(String charset) {
        this.charset = charset;
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public boolean canEncode(String name) {
        return true;
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public ByteBuffer encode(String name) throws IOException {
        if (this.charset == null) {
            return ByteBuffer.wrap(name.getBytes());
        }
        return ByteBuffer.wrap(name.getBytes(this.charset));
    }

    @Override // org.apache.tools.zip.ZipEncoding
    public String decode(byte[] data) throws IOException {
        if (this.charset == null) {
            return new String(data);
        }
        return new String(data, this.charset);
    }
}
