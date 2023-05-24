package com.oreilly.servlet.multipart;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:cos.jar:com/oreilly/servlet/multipart/MacBinaryDecoderOutputStream.class */
public class MacBinaryDecoderOutputStream extends FilterOutputStream {
    private int bytesFiltered;
    private int dataForkLength;

    public MacBinaryDecoderOutputStream(OutputStream out) {
        super(out);
        this.bytesFiltered = 0;
        this.dataForkLength = 0;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(int b) throws IOException {
        if (this.bytesFiltered <= 86 && this.bytesFiltered >= 83) {
            int leftShift = (86 - this.bytesFiltered) * 8;
            this.dataForkLength |= (b & 255) << leftShift;
        } else if (this.bytesFiltered < 128 + this.dataForkLength && this.bytesFiltered >= 128) {
            ((FilterOutputStream) this).out.write(b);
        }
        this.bytesFiltered++;
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.FilterOutputStream, java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        if (this.bytesFiltered >= 128 + this.dataForkLength) {
            this.bytesFiltered += len;
        } else if (this.bytesFiltered >= 128 && this.bytesFiltered + len <= 128 + this.dataForkLength) {
            ((FilterOutputStream) this).out.write(b, off, len);
            this.bytesFiltered += len;
        } else {
            for (int i = 0; i < len; i++) {
                write(b[off + i]);
            }
        }
    }
}
