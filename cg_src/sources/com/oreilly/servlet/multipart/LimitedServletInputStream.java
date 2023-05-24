package com.oreilly.servlet.multipart;

import java.io.IOException;
import javax.servlet.ServletInputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/LimitedServletInputStream.class */
public class LimitedServletInputStream extends ServletInputStream {
    private ServletInputStream in;
    private int totalExpected;
    private int totalRead = 0;

    public LimitedServletInputStream(ServletInputStream in, int totalExpected) {
        this.in = in;
        this.totalExpected = totalExpected;
    }

    @Override // javax.servlet.ServletInputStream
    public int readLine(byte[] b, int off, int len) throws IOException {
        int left = this.totalExpected - this.totalRead;
        if (left <= 0) {
            return -1;
        }
        int result = this.in.readLine(b, off, Math.min(left, len));
        if (result > 0) {
            this.totalRead += result;
        }
        return result;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.totalRead >= this.totalExpected) {
            return -1;
        }
        int result = this.in.read();
        if (result != -1) {
            this.totalRead++;
        }
        return result;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int left = this.totalExpected - this.totalRead;
        if (left <= 0) {
            return -1;
        }
        int result = this.in.read(b, off, Math.min(left, len));
        if (result > 0) {
            this.totalRead += result;
        }
        return result;
    }
}
