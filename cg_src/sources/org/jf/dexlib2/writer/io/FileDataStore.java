package org.jf.dexlib2.writer.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import javax.annotation.Nonnull;
import org.jf.util.RandomAccessFileInputStream;
import org.jf.util.RandomAccessFileOutputStream;
/* loaded from: gencallgraphv3.jar:dexlib2-2.5.2.jar:org/jf/dexlib2/writer/io/FileDataStore.class */
public class FileDataStore implements DexDataStore {
    private final RandomAccessFile raf;

    public FileDataStore(@Nonnull File file) throws FileNotFoundException, IOException {
        this.raf = new RandomAccessFile(file, "rw");
        this.raf.setLength(0L);
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    @Nonnull
    public OutputStream outputAt(int offset) {
        return new RandomAccessFileOutputStream(this.raf, offset);
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    @Nonnull
    public InputStream readAt(int offset) {
        return new RandomAccessFileInputStream(this.raf, offset);
    }

    @Override // org.jf.dexlib2.writer.io.DexDataStore
    public void close() throws IOException {
        this.raf.close();
    }
}
