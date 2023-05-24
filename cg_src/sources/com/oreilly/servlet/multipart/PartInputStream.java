package com.oreilly.servlet.multipart;

import java.io.FilterInputStream;
import java.io.IOException;
import javax.servlet.ServletInputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/PartInputStream.class */
public class PartInputStream extends FilterInputStream {
    private String boundary;
    private byte[] buf;
    private int count;
    private int pos;
    private boolean eof;

    /* JADX INFO: Access modifiers changed from: package-private */
    public PartInputStream(ServletInputStream in, String boundary) throws IOException {
        super(in);
        this.buf = new byte[65536];
        this.boundary = boundary;
    }

    private void fill() throws IOException {
        if (this.eof) {
            return;
        }
        if (this.count > 0) {
            if (this.count - this.pos == 2) {
                System.arraycopy(this.buf, this.pos, this.buf, 0, this.count - this.pos);
                this.count -= this.pos;
                this.pos = 0;
            } else {
                throw new IllegalStateException("fill() detected illegal buffer state");
            }
        }
        int boundaryLength = this.boundary.length();
        int maxRead = (this.buf.length - boundaryLength) - 2;
        while (this.count < maxRead) {
            int read = ((ServletInputStream) ((FilterInputStream) this).in).readLine(this.buf, this.count, this.buf.length - this.count);
            if (read == -1) {
                throw new IOException("unexpected end of part");
            }
            if (read >= boundaryLength) {
                this.eof = true;
                int i = 0;
                while (true) {
                    if (i >= boundaryLength) {
                        break;
                    } else if (this.boundary.charAt(i) == this.buf[this.count + i]) {
                        i++;
                    } else {
                        this.eof = false;
                        break;
                    }
                }
                if (this.eof) {
                    return;
                }
            }
            this.count += read;
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read() throws IOException {
        if (this.count - this.pos <= 2) {
            fill();
            if (this.count - this.pos <= 2) {
                return -1;
            }
        }
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b) throws IOException {
        return read(b, 0, b.length);
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int total = 0;
        if (len == 0) {
            return 0;
        }
        int avail = (this.count - this.pos) - 2;
        if (avail <= 0) {
            fill();
            avail = (this.count - this.pos) - 2;
            if (avail <= 0) {
                return -1;
            }
        }
        int copy = Math.min(len, avail);
        System.arraycopy(this.buf, this.pos, b, off, copy);
        this.pos += copy;
        while (true) {
            total += copy;
            if (total < len) {
                fill();
                int avail2 = (this.count - this.pos) - 2;
                if (avail2 <= 0) {
                    return total;
                }
                copy = Math.min(len - total, avail2);
                System.arraycopy(this.buf, this.pos, b, off + total, copy);
                this.pos += copy;
            } else {
                return total;
            }
        }
    }

    @Override // java.io.FilterInputStream, java.io.InputStream
    public int available() throws IOException {
        int avail = ((this.count - this.pos) - 2) + ((FilterInputStream) this).in.available();
        if (avail < 0) {
            return 0;
        }
        return avail;
    }

    @Override // java.io.FilterInputStream, java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        if (!this.eof) {
            do {
            } while (read(this.buf, 0, this.buf.length) != -1);
        }
    }
}
