package org.apache.tools.ant.util;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LazyFileOutputStream.class */
public class LazyFileOutputStream extends OutputStream {
    private OutputStream fos;
    private File file;
    private boolean append;
    private boolean alwaysCreate;
    private boolean opened;
    private boolean closed;

    public LazyFileOutputStream(String name) {
        this(name, false);
    }

    public LazyFileOutputStream(String name, boolean append) {
        this(new File(name), append);
    }

    public LazyFileOutputStream(File f) {
        this(f, false);
    }

    public LazyFileOutputStream(File file, boolean append) {
        this(file, append, false);
    }

    public LazyFileOutputStream(File file, boolean append, boolean alwaysCreate) {
        this.opened = false;
        this.closed = false;
        this.file = file;
        this.append = append;
        this.alwaysCreate = alwaysCreate;
    }

    public void open() throws IOException {
        ensureOpened();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public synchronized void close() throws IOException {
        if (this.alwaysCreate && !this.closed) {
            ensureOpened();
        }
        if (this.opened) {
            this.fos.close();
        }
        this.closed = true;
    }

    @Override // java.io.OutputStream
    public void write(byte[] b) throws IOException {
        write(b, 0, b.length);
    }

    @Override // java.io.OutputStream
    public synchronized void write(byte[] b, int offset, int len) throws IOException {
        ensureOpened();
        this.fos.write(b, offset, len);
    }

    @Override // java.io.OutputStream
    public synchronized void write(int b) throws IOException {
        ensureOpened();
        this.fos.write(b);
    }

    private synchronized void ensureOpened() throws IOException {
        if (this.closed) {
            throw new IOException(this.file + " has already been closed.");
        }
        if (!this.opened) {
            this.fos = FileUtils.newOutputStream(this.file.toPath(), this.append);
            this.opened = true;
        }
    }
}
