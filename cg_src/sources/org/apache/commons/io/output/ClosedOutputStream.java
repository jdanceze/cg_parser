package org.apache.commons.io.output;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/output/ClosedOutputStream.class */
public class ClosedOutputStream extends OutputStream {
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = new ClosedOutputStream();

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        throw new IOException("write(" + b + ") failed: stream is closed");
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        throw new IOException("flush() failed: stream is closed");
    }
}
