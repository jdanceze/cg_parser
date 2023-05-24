package org.apache.commons.io.input;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
/* loaded from: gencallgraphv3.jar:commons-io-2.7.jar:org/apache/commons/io/input/ProxyInputStream.class */
public abstract class ProxyInputStream extends FilterInputStream {
    public ProxyInputStream(InputStream proxy) {
        super(proxy);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        try {
            beforeRead(1);
            int b = this.in.read();
            afterRead(b != -1 ? 1 : -1);
            return b;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bts) throws IOException {
        try {
            beforeRead(IOUtils.length(bts));
            int n = this.in.read(bts);
            afterRead(n);
            return n;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] bts, int off, int len) throws IOException {
        try {
            beforeRead(len);
            int n = this.in.read(bts, off, len);
            afterRead(n);
            return n;
        } catch (IOException e) {
            handleIOException(e);
            return -1;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public long skip(long ln) throws IOException {
        try {
            return this.in.skip(ln);
        } catch (IOException e) {
            handleIOException(e);
            return 0L;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        try {
            return super.available();
        } catch (IOException e) {
            handleIOException(e);
            return 0;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        IOUtils.close(this.in, e -> {
            handleIOException(e);
        });
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void mark(int readlimit) {
        this.in.mark(readlimit);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public synchronized void reset() throws IOException {
        try {
            this.in.reset();
        } catch (IOException e) {
            handleIOException(e);
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public boolean markSupported() {
        return this.in.markSupported();
    }

    protected void beforeRead(int n) throws IOException {
    }

    protected void afterRead(int n) throws IOException {
    }

    protected void handleIOException(IOException e) throws IOException {
        throw e;
    }
}
