package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/io/CharSequenceReader.class */
final class CharSequenceReader extends Reader {
    private CharSequence seq;
    private int pos;
    private int mark;

    public CharSequenceReader(CharSequence seq) {
        this.seq = (CharSequence) Preconditions.checkNotNull(seq);
    }

    private void checkOpen() throws IOException {
        if (this.seq == null) {
            throw new IOException("reader closed");
        }
    }

    private boolean hasRemaining() {
        return remaining() > 0;
    }

    private int remaining() {
        return this.seq.length() - this.pos;
    }

    @Override // java.io.Reader, java.lang.Readable
    public synchronized int read(CharBuffer target) throws IOException {
        Preconditions.checkNotNull(target);
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        int charsToRead = Math.min(target.remaining(), remaining());
        for (int i = 0; i < charsToRead; i++) {
            CharSequence charSequence = this.seq;
            int i2 = this.pos;
            this.pos = i2 + 1;
            target.put(charSequence.charAt(i2));
        }
        return charsToRead;
    }

    @Override // java.io.Reader
    public synchronized int read() throws IOException {
        checkOpen();
        if (hasRemaining()) {
            CharSequence charSequence = this.seq;
            int i = this.pos;
            this.pos = i + 1;
            return charSequence.charAt(i);
        }
        return -1;
    }

    @Override // java.io.Reader
    public synchronized int read(char[] cbuf, int off, int len) throws IOException {
        Preconditions.checkPositionIndexes(off, off + len, cbuf.length);
        checkOpen();
        if (!hasRemaining()) {
            return -1;
        }
        int charsToRead = Math.min(len, remaining());
        for (int i = 0; i < charsToRead; i++) {
            CharSequence charSequence = this.seq;
            int i2 = this.pos;
            this.pos = i2 + 1;
            cbuf[off + i] = charSequence.charAt(i2);
        }
        return charsToRead;
    }

    @Override // java.io.Reader
    public synchronized long skip(long n) throws IOException {
        Preconditions.checkArgument(n >= 0, "n (%s) may not be negative", n);
        checkOpen();
        int charsToSkip = (int) Math.min(remaining(), n);
        this.pos += charsToSkip;
        return charsToSkip;
    }

    @Override // java.io.Reader
    public synchronized boolean ready() throws IOException {
        checkOpen();
        return true;
    }

    @Override // java.io.Reader
    public boolean markSupported() {
        return true;
    }

    @Override // java.io.Reader
    public synchronized void mark(int readAheadLimit) throws IOException {
        Preconditions.checkArgument(readAheadLimit >= 0, "readAheadLimit (%s) may not be negative", readAheadLimit);
        checkOpen();
        this.mark = this.pos;
    }

    @Override // java.io.Reader
    public synchronized void reset() throws IOException {
        checkOpen();
        this.pos = this.mark;
    }

    @Override // java.io.Reader, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        this.seq = null;
    }
}
