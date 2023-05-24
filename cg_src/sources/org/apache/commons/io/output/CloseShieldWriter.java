package org.apache.commons.io.output;

import java.io.Writer;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/output/CloseShieldWriter.class */
public class CloseShieldWriter extends ProxyWriter {
    public CloseShieldWriter(Writer out) {
        super(out);
    }

    @Override // org.apache.commons.io.output.ProxyWriter, java.io.FilterWriter, java.io.Writer, java.io.Closeable, java.lang.AutoCloseable
    public void close() {
        this.out = ClosedWriter.CLOSED_WRITER;
    }
}
