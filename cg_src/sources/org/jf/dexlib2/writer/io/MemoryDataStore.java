package org.jf.dexlib2.writer.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/io/MemoryDataStore.class */
public class MemoryDataStore implements DexDataStore {
    private byte[] buf;
    private int size;

    public MemoryDataStore() {
        this(0);
    }

    public MemoryDataStore(int initialCapacity) {
        this.size = 0;
        this.buf = new byte[initialCapacity];
    }

    public byte[] getBuffer() {
        return this.buf;
    }

    public int getSize() {
        return this.size;
    }

    public byte[] getData() {
        return Arrays.copyOf(this.buf, this.size);
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    @Nonnull
    public OutputStream outputAt(final int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException();
        }
        return new OutputStream() { // from class: org.jf.dexlib2.writer.io.MemoryDataStore.1
            private int position;

            {
                this.position = offset;
            }

            @Override // java.io.OutputStream
            public void write(int b) throws IOException {
                MemoryDataStore.this.growBufferIfNeeded(this.position + 1);
                byte[] bArr = MemoryDataStore.this.buf;
                int i = this.position;
                this.position = i + 1;
                bArr[i] = (byte) b;
            }

            @Override // java.io.OutputStream
            public void write(byte[] b) throws IOException {
                MemoryDataStore.this.growBufferIfNeeded(this.position + b.length);
                System.arraycopy(b, 0, MemoryDataStore.this.buf, this.position, b.length);
                this.position += b.length;
            }

            @Override // java.io.OutputStream
            public void write(byte[] b, int off, int len) throws IOException {
                MemoryDataStore.this.growBufferIfNeeded(this.position + len);
                System.arraycopy(b, off, MemoryDataStore.this.buf, this.position, len);
                this.position += len;
            }
        };
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void growBufferIfNeeded(int minSize) {
        if (minSize > this.size) {
            if (minSize > this.buf.length) {
                int newSize = getNewBufferSize(this.buf.length, minSize);
                if (newSize < minSize) {
                    throw new IndexOutOfBoundsException();
                }
                this.buf = Arrays.copyOf(this.buf, newSize);
            }
            this.size = minSize;
        }
    }

    protected int getNewBufferSize(int currentSize, int newMinSize) {
        return Math.max(newMinSize + (newMinSize >> 2), currentSize + 262144);
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    @Nonnull
    public InputStream readAt(final int offset) {
        if (offset < 0) {
            throw new IllegalArgumentException();
        }
        return new InputStream() { // from class: org.jf.dexlib2.writer.io.MemoryDataStore.2
            private int position;
            private int mark;

            {
                this.position = offset;
                this.mark = offset;
            }

            @Override // java.io.InputStream
            public int read() throws IOException {
                if (this.position < MemoryDataStore.this.size) {
                    byte[] bArr = MemoryDataStore.this.buf;
                    int i = this.position;
                    this.position = i + 1;
                    return bArr[i];
                }
                return -1;
            }

            @Override // java.io.InputStream
            public int read(byte[] b) throws IOException {
                int readLength = Math.min(b.length, MemoryDataStore.this.size - this.position);
                if (readLength <= 0) {
                    if (this.position >= MemoryDataStore.this.size) {
                        return -1;
                    }
                    return 0;
                }
                System.arraycopy(MemoryDataStore.this.buf, this.position, b, 0, readLength);
                this.position += readLength;
                return readLength;
            }

            @Override // java.io.InputStream
            public int read(byte[] b, int off, int len) throws IOException {
                int readLength = Math.min(len, MemoryDataStore.this.size - this.position);
                if (readLength <= 0) {
                    if (this.position >= MemoryDataStore.this.size) {
                        return -1;
                    }
                    return 0;
                }
                System.arraycopy(MemoryDataStore.this.buf, this.position, b, off, readLength);
                this.position += readLength;
                return readLength;
            }

            @Override // java.io.InputStream
            public long skip(long n) throws IOException {
                int skipLength = (int) Math.max(0L, Math.min(n, MemoryDataStore.this.size - this.position));
                this.position += skipLength;
                return skipLength;
            }

            @Override // java.io.InputStream
            public int available() throws IOException {
                return Math.max(0, MemoryDataStore.this.size - this.position);
            }

            @Override // java.io.InputStream
            public void mark(int i) {
                this.mark = this.position;
            }

            @Override // java.io.InputStream
            public void reset() throws IOException {
                this.position = this.mark;
            }

            @Override // java.io.InputStream
            public boolean markSupported() {
                return true;
            }
        };
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    public void close() throws IOException {
    }
}
