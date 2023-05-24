package com.oreilly.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
/* compiled from: CacheHttpServlet.java */
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/CacheServletOutputStream.class */
class CacheServletOutputStream extends ServletOutputStream {
    ServletOutputStream delegate;
    ByteArrayOutputStream cache = new ByteArrayOutputStream(4096);

    /* JADX INFO: Access modifiers changed from: package-private */
    public CacheServletOutputStream(ServletOutputStream out) {
        this.delegate = out;
    }

    public ByteArrayOutputStream getBuffer() {
        return this.cache;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.delegate.write(b);
        this.cache.write(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        this.delegate.write(b);
        this.cache.write(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] buf, int offset, int len) throws IOException {
        this.delegate.write(buf, offset, len);
        this.cache.write(buf, offset, len);
    }
}
