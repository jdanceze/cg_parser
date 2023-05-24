package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/AutoCloseInputStream.class */
public class AutoCloseInputStream extends ProxyInputStream {
    public AutoCloseInputStream(InputStream in) {
        super(in);
    }

    @Override // org.apache.commons.io.input.ProxyInputStream, java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
        this.in = ClosedInputStream.CLOSED_INPUT_STREAM;
    }

    @Override // org.apache.commons.io.input.ProxyInputStream
    protected void afterRead(int n) throws IOException {
        if (n == -1) {
            close();
        }
    }

    protected void finalize() throws Throwable {
        close();
        super.finalize();
    }
}
