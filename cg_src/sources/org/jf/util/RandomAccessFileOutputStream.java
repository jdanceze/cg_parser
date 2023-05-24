package org.jf.util;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/util/RandomAccessFileOutputStream.class */
public class RandomAccessFileOutputStream extends OutputStream {
    private int filePosition;
    @Nonnull
    private final RandomAccessFile raf;

    public RandomAccessFileOutputStream(@Nonnull RandomAccessFile raf, int startFilePosition) {
        this.filePosition = startFilePosition;
        this.raf = raf;
    }

    @Override // java.io.OutputStream
    public void write(int b) throws IOException {
        this.raf.seek(this.filePosition);
        this.filePosition++;
        this.raf.write(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        this.raf.seek(this.filePosition);
        this.filePosition += b.length;
        this.raf.write(b);
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        this.raf.seek(this.filePosition);
        this.filePosition += len;
        this.raf.write(b, off, len);
    }
}
