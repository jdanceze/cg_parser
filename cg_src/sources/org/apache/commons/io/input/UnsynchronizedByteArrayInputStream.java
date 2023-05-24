package org.apache.commons.io.input;

import java.io.InputStream;
import java.util.Objects;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/UnsynchronizedByteArrayInputStream.class */
public class UnsynchronizedByteArrayInputStream extends InputStream {
    public static final int END_OF_STREAM = -1;
    private final byte[] data;
    private final int eod;
    private int offset;
    private int markedOffset;

    public UnsynchronizedByteArrayInputStream(byte[] data) {
        Objects.requireNonNull(data);
        this.data = data;
        this.offset = 0;
        this.eod = data.length;
        this.markedOffset = this.offset;
    }

    public UnsynchronizedByteArrayInputStream(byte[] data, int offset) {
        Objects.requireNonNull(data);
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        this.data = data;
        this.offset = Math.min(offset, data.length > 0 ? data.length : offset);
        this.eod = data.length;
        this.markedOffset = this.offset;
    }

    public UnsynchronizedByteArrayInputStream(byte[] data, int offset, int length) {
        Objects.requireNonNull(data);
        if (offset < 0) {
            throw new IllegalArgumentException("offset cannot be negative");
        }
        if (length < 0) {
            throw new IllegalArgumentException("length cannot be negative");
        }
        this.data = data;
        this.offset = Math.min(offset, data.length > 0 ? data.length : offset);
        this.eod = Math.min(this.offset + length, data.length);
        this.markedOffset = this.offset;
    }

    @Override // java.io.InputStream
    public int available() {
        if (this.offset < this.eod) {
            return this.eod - this.offset;
        }
        return 0;
    }

    @Override // java.io.InputStream
    public int read() {
        if (this.offset < this.eod) {
            byte[] bArr = this.data;
            int i = this.offset;
            this.offset = i + 1;
            return bArr[i] & 255;
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(byte[] b) {
        Objects.requireNonNull(b);
        return read(b, 0, b.length);
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) {
        Objects.requireNonNull(b);
        if (off < 0 || len < 0 || off + len > b.length) {
            throw new IndexOutOfBoundsException();
        }
        if (this.offset >= this.eod) {
            return -1;
        }
        int actualLen = this.eod - this.offset;
        if (len < actualLen) {
            actualLen = len;
        }
        if (actualLen <= 0) {
            return 0;
        }
        System.arraycopy(this.data, this.offset, b, off, actualLen);
        this.offset += actualLen;
        return actualLen;
    }

    @Override // java.io.InputStream
    public long skip(long n) {
        if (n < 0) {
            throw new IllegalArgumentException("Skipping backward is not supported");
        }
        long actualSkip = this.eod - this.offset;
        if (n < actualSkip) {
            actualSkip = n;
        }
        this.offset = (int) (this.offset + actualSkip);
        return actualSkip;
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.InputStream
    public void mark(int readlimit) {
        this.markedOffset = this.offset;
    }

    @Override // java.io.InputStream
    public void reset() {
        this.offset = this.markedOffset;
    }
}
