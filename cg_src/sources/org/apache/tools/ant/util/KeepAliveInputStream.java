package org.apache.tools.ant.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/KeepAliveInputStream.class */
public class KeepAliveInputStream extends FilterInputStream {
    public KeepAliveInputStream(InputStream in) {
        super(in);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public static InputStream wrapSystemIn() {
        return new KeepAliveInputStream(System.in);
    }
}
