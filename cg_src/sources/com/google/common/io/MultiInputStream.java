package com.google.common.io;

import com.google.common.annotations.GwtIncompatible;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
@GwtIncompatible
/* loaded from: gencallgraphv3.jar:guava-27.1-android.jar:com/google/common/io/MultiInputStream.class */
final class MultiInputStream extends InputStream {
    private Iterator<? extends ByteSource> it;
    @NullableDecl
    private InputStream in;

    public MultiInputStream(Iterator<? extends ByteSource> it) throws IOException {
        this.it = (Iterator) Preconditions.checkNotNull(it);
        advance();
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (this.in != null) {
            try {
                this.in.close();
            } finally {
                this.in = null;
            }
        }
    }

    private void advance() throws IOException {
        close();
        if (this.it.hasNext()) {
            this.in = this.it.next().openStream();
        }
    }

    @Override // java.io.InputStream
    public int available() throws IOException {
        if (this.in == null) {
            return 0;
        }
        return this.in.available();
    }

    @Override // java.io.InputStream
    public boolean markSupported() {
        return false;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        while (this.in != null) {
            int result = this.in.read();
            if (result != -1) {
                return result;
            }
            advance();
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read(@NullableDecl byte[] b, int off, int len) throws IOException {
        while (this.in != null) {
            int result = this.in.read(b, off, len);
            if (result != -1) {
                return result;
            }
            advance();
        }
        return -1;
    }

    @Override // java.io.InputStream
    public long skip(long n) throws IOException {
        if (this.in == null || n <= 0) {
            return 0L;
        }
        long result = this.in.skip(n);
        if (result != 0) {
            return result;
        }
        if (read() == -1) {
            return 0L;
        }
        return 1 + this.in.skip(n - 1);
    }
}
