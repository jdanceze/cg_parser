package org.apache.tools.ant.util;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/KeepAliveOutputStream.class */
public class KeepAliveOutputStream extends FilterOutputStream {
    public KeepAliveOutputStream(OutputStream out) {
        super(out);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
    }

    public static PrintStream wrapSystemOut() {
        return wrap(System.out);
    }

    public static PrintStream wrapSystemErr() {
        return wrap(System.err);
    }

    private static PrintStream wrap(PrintStream ps) {
        return new PrintStream(new KeepAliveOutputStream(ps));
    }
}
