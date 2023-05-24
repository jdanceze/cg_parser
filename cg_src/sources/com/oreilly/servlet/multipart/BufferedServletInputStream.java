package com.oreilly.servlet.multipart;

import java.io.IOException;
import javax.servlet.ServletInputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/BufferedServletInputStream.class */
public class BufferedServletInputStream extends ServletInputStream {
    private ServletInputStream in;
    private byte[] buf = new byte[65536];
    private int count;
    private int pos;

    public BufferedServletInputStream(ServletInputStream in) {
        this.in = in;
    }

    private void fill() throws IOException {
        int i = this.in.read(this.buf, 0, this.buf.length);
        if (i > 0) {
            this.pos = 0;
            this.count = i;
        }
    }

    @Override // javax.servlet.ServletInputStream
    public int readLine(byte[] b, int off, int len) throws IOException {
        int total = 0;
        if (len == 0) {
            return 0;
        }
        int avail = this.count - this.pos;
        if (avail <= 0) {
            fill();
            avail = this.count - this.pos;
            if (avail <= 0) {
                return -1;
            }
        }
        int copy = Math.min(len, avail);
        int eol = findeol(this.buf, this.pos, copy);
        if (eol != -1) {
            copy = eol;
        }
        System.arraycopy(this.buf, this.pos, b, off, copy);
        this.pos += copy;
        while (true) {
            total += copy;
            if (total >= len || eol != -1) {
                break;
            }
            fill();
            int avail2 = this.count - this.pos;
            if (avail2 <= 0) {
                return total;
            }
            copy = Math.min(len - total, avail2);
            eol = findeol(this.buf, this.pos, copy);
            if (eol != -1) {
                copy = eol;
            }
            System.arraycopy(this.buf, this.pos, b, off + total, copy);
            this.pos += copy;
        }
        return total;
    }

    private static int findeol(byte[] b, int pos, int len) {
        int end = pos + len;
        int i = pos;
        while (i < end) {
            int i2 = i;
            i++;
            if (b[i2] == 10) {
                return i - pos;
            }
        }
        return -1;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        if (this.count <= this.pos) {
            fill();
            if (this.count <= this.pos) {
                return -1;
            }
        }
        byte[] bArr = this.buf;
        int i = this.pos;
        this.pos = i + 1;
        return bArr[i] & 255;
    }

    @Override // java.io.InputStream
    public int read(byte[] b, int off, int len) throws IOException {
        int i = 0;
        while (true) {
            int total = i;
            if (total < len) {
                int avail = this.count - this.pos;
                if (avail <= 0) {
                    fill();
                    avail = this.count - this.pos;
                    if (avail <= 0) {
                        if (total > 0) {
                            return total;
                        }
                        return -1;
                    }
                }
                int copy = Math.min(len - total, avail);
                System.arraycopy(this.buf, this.pos, b, off + total, copy);
                this.pos += copy;
                i = total + copy;
            } else {
                return total;
            }
        }
    }
}
