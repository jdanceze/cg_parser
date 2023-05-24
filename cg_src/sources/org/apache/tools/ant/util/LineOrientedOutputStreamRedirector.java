package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LineOrientedOutputStreamRedirector.class */
public class LineOrientedOutputStreamRedirector extends LineOrientedOutputStream {
    private OutputStream stream;

    public LineOrientedOutputStreamRedirector(OutputStream stream) {
        this.stream = stream;
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream
    protected void processLine(byte[] b) throws IOException {
        this.stream.write(b);
        this.stream.write(System.lineSeparator().getBytes());
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream
    protected void processLine(String line) throws IOException {
        this.stream.write(String.format("%s%n", line).getBytes());
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        super.close();
        this.stream.close();
    }

    @Override // org.apache.tools.ant.util.LineOrientedOutputStream, java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        super.flush();
        this.stream.flush();
    }
}
