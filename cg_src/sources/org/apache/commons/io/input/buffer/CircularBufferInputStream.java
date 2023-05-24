package org.apache.commons.io.input.buffer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/buffer/CircularBufferInputStream.class */
public class CircularBufferInputStream extends InputStream {
    protected final InputStream in;
    protected final CircularByteBuffer buffer;
    protected final int bufferSize;
    private boolean eofSeen;

    public CircularBufferInputStream(InputStream inputStream, int bufferSize) {
        if (bufferSize <= 0) {
            throw new IllegalArgumentException("Invalid bufferSize: " + bufferSize);
        }
        this.in = (InputStream) Objects.requireNonNull(inputStream, "inputStream");
        this.buffer = new CircularByteBuffer(bufferSize);
        this.bufferSize = bufferSize;
        this.eofSeen = false;
    }

    public CircularBufferInputStream(InputStream inputStream) {
        this(inputStream, 8192);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void fillBuffer() throws IOException {
        if (this.eofSeen) {
            return;
        }
        int space = this.buffer.getSpace();
        byte[] buf = new byte[space];
        while (space > 0) {
            int res = this.in.read(buf, 0, space);
            if (res == -1) {
                this.eofSeen = true;
                return;
            } else if (res > 0) {
                this.buffer.add(buf, 0, res);
                space -= res;
            }
        }
    }

    protected boolean haveBytes(int count) throws IOException {
        if (this.buffer.getCurrentNumberOfBytes() < count) {
            fillBuffer();
        }
        return this.buffer.hasBytes();
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (!haveBytes(1)) {
            return -1;
        }
        return this.buffer.read();
    }

    @Override // java.io.InputStream
    public int read(byte[] buffer) throws IOException {
        return read(buffer, 0, buffer.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] targetBuffer, int offset, int length) throws IOException {
        Objects.requireNonNull(targetBuffer, "Buffer");
        if (offset < 0) {
            throw new IllegalArgumentException("Offset must not be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("Length must not be negative");
        }
        if (!haveBytes(length)) {
            return -1;
        }
        int result = Math.min(length, this.buffer.getCurrentNumberOfBytes());
        for (int i = 0; i < result; i++) {
            targetBuffer[offset + i] = this.buffer.read();
        }
        return result;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        this.in.close();
        this.eofSeen = true;
        this.buffer.clear();
    }
}
