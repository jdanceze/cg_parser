package org.apache.tools.ant.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LineOrientedOutputStream.class */
public abstract class LineOrientedOutputStream extends OutputStream {
    private static final int INITIAL_SIZE = 132;
    private static final int CR = 13;
    private static final int LF = 10;
    private ByteArrayOutputStream buffer = new ByteArrayOutputStream(132);
    private boolean skip = false;

    protected abstract void processLine(String str) throws IOException;

    @Override // java.io.OutputStream
    public final void write(int cc) throws IOException {
        byte c = (byte) cc;
        if (c == 10 || c == 13) {
            if (!this.skip) {
                processBuffer();
            }
        } else {
            this.buffer.write(cc);
        }
        this.skip = c == 13;
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void processBuffer() throws IOException {
        try {
            processLine(this.buffer.toByteArray());
        } finally {
            this.buffer.reset();
        }
    }

    protected void processLine(byte[] line) throws IOException {
        processLine(new String(line));
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.buffer.size() > 0) {
            processBuffer();
        }
        super.close();
    }

    @Override // java.io.OutputStream
    public final void write(byte[] b, int off, int len) throws IOException {
        int offset = off;
        int blockStartOffset = offset;
        int remaining = len;
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
                offset++;
                remaining--;
            }
            int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                this.buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
                write(b[offset]);
                offset++;
                remaining--;
            }
            blockStartOffset = offset;
        }
    }
}
