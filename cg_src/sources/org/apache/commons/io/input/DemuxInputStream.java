package org.apache.commons.io.input;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/DemuxInputStream.class */
public class DemuxInputStream extends InputStream {
    private final InheritableThreadLocal<InputStream> inputStream = new InheritableThreadLocal<>();

    public InputStream bindStream(InputStream input) {
        InputStream oldValue = this.inputStream.get();
        this.inputStream.set(input);
        return oldValue;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOUtils.close(this.inputStream.get());
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        InputStream input = this.inputStream.get();
        if (null != input) {
            return input.read();
        }
        return -1;
    }
}
