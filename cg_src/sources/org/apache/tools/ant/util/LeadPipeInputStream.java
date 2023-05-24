package org.apache.tools.ant.util;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import org.apache.tools.ant.ProjectComponent;
import org.apache.tools.ant.Task;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/LeadPipeInputStream.class */
public class LeadPipeInputStream extends PipedInputStream {
    private static final int BYTE_MASK = 255;
    private ProjectComponent managingPc;

    public LeadPipeInputStream() {
    }

    public LeadPipeInputStream(int size) {
        setBufferSize(size);
    }

    public LeadPipeInputStream(PipedOutputStream src) throws IOException {
        super(src);
    }

    public LeadPipeInputStream(PipedOutputStream src, int size) throws IOException {
        super(src);
        setBufferSize(size);
    }

    @Override // java.io.PipedInputStream, java.io.InputStream
    public synchronized int read() throws IOException {
        int result = -1;
        try {
            result = super.read();
        } catch (IOException eyeOhEx) {
            String msg = eyeOhEx.getMessage();
            if ("write end dead".equalsIgnoreCase(msg) || "pipe broken".equalsIgnoreCase(msg)) {
                if (((PipedInputStream) this).in > 0 && ((PipedInputStream) this).out < ((PipedInputStream) this).buffer.length && ((PipedInputStream) this).out > ((PipedInputStream) this).in) {
                    byte[] bArr = ((PipedInputStream) this).buffer;
                    int i = ((PipedInputStream) this).out;
                    ((PipedInputStream) this).out = i + 1;
                    result = bArr[i] & 255;
                }
            } else {
                log("error at LeadPipeInputStream.read():  " + msg, 2);
            }
        }
        return result;
    }

    public synchronized void setBufferSize(int size) {
        if (size > this.buffer.length) {
            byte[] newBuffer = new byte[size];
            if (this.in >= 0) {
                if (this.in > this.out) {
                    System.arraycopy(this.buffer, this.out, newBuffer, this.out, this.in - this.out);
                } else {
                    int outlen = this.buffer.length - this.out;
                    System.arraycopy(this.buffer, this.out, newBuffer, 0, outlen);
                    System.arraycopy(this.buffer, 0, newBuffer, outlen, this.in);
                    this.in += outlen;
                    this.out = 0;
                }
            }
            this.buffer = newBuffer;
        }
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
}
