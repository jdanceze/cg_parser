package org.apache.tools.ant.util;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/ConcatFileInputStream.class */
public class ConcatFileInputStream extends InputStream {
    private static final int EOF = -1;
    private int currentIndex = -1;
    private boolean eof = false;
    private File[] file;
    private InputStream currentStream;
    private ProjectComponent managingPc;

    public ConcatFileInputStream(File[] file) throws IOException {
        this.file = file;
    }

    @Override // java.io.InputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        closeCurrent();
        this.eof = true;
    }

    @Override // java.io.InputStream
    public int read() throws IOException {
        int result = readCurrent();
        if (result == -1 && !this.eof) {
            int i = this.currentIndex + 1;
            this.currentIndex = i;
            openFile(i);
            result = readCurrent();
        }
        return result;
    }

    public void setManagingTask(Task task) {
        setManagingComponent(task);
    }

    public void setManagingComponent(ProjectComponent pc) {
        this.managingPc = pc;
    }

    public void log(String message, int loglevel) {
        if (this.managingPc != null) {
            this.managingPc.log(message, loglevel);
        } else if (loglevel > 1) {
            System.out.println(message);
        } else {
            System.err.println(message);
        }
    }

    private int readCurrent() throws IOException {
        if (this.eof || this.currentStream == null) {
            return -1;
        }
        return this.currentStream.read();
    }

    private void openFile(int index) throws IOException {
        closeCurrent();
        if (this.file != null && index < this.file.length) {
            log("Opening " + this.file[index], 3);
            try {
                this.currentStream = new BufferedInputStream(Files.newInputStream(this.file[index].toPath(), new OpenOption[0]));
                return;
            } catch (IOException eyeOhEx) {
                log("Failed to open " + this.file[index], 0);
                throw eyeOhEx;
            }
        }
        this.eof = true;
    }

    private void closeCurrent() {
        FileUtils.close(this.currentStream);
        this.currentStream = null;
    }
}
