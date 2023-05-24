package org.apache.tools.ant;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.WeakHashMap;
import org.apache.tools.ant.util.FileUtils;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/DemuxOutputStream.class */
public class DemuxOutputStream extends OutputStream {
    private static final int MAX_SIZE = 1024;
    private static final int INITIAL_SIZE = 132;
    private static final int CR = 13;
    private static final int LF = 10;
    private WeakHashMap<Thread, BufferInfo> buffers = new WeakHashMap<>();
    private Project project;
    private boolean isErrorStream;

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/DemuxOutputStream$BufferInfo.class */
    public static class BufferInfo {
        private ByteArrayOutputStream buffer;
        private boolean crSeen;

        private BufferInfo() {
            this.crSeen = false;
        }
    }

    public DemuxOutputStream(Project project, boolean isErrorStream) {
        this.project = project;
        this.isErrorStream = isErrorStream;
    }

    private BufferInfo getBufferInfo() {
        Thread current = Thread.currentThread();
        return this.buffers.computeIfAbsent(current, x -> {
            BufferInfo bufferInfo = new BufferInfo();
            bufferInfo.buffer = new ByteArrayOutputStream(132);
            bufferInfo.crSeen = false;
            return bufferInfo;
        });
    }

    private void resetBufferInfo() {
        Thread current = Thread.currentThread();
        BufferInfo bufferInfo = this.buffers.get(current);
        FileUtils.close((OutputStream) bufferInfo.buffer);
        bufferInfo.buffer = new ByteArrayOutputStream();
        bufferInfo.crSeen = false;
    }

    private void removeBuffer() {
        Thread current = Thread.currentThread();
        this.buffers.remove(current);
    }

    @Override // java.io.OutputStream
    public void write(int cc) throws IOException {
        byte c = (byte) cc;
        BufferInfo bufferInfo = getBufferInfo();
        if (c == 10) {
            bufferInfo.buffer.write(cc);
            processBuffer(bufferInfo.buffer);
        } else {
            if (bufferInfo.crSeen) {
                processBuffer(bufferInfo.buffer);
            }
            bufferInfo.buffer.write(cc);
        }
        bufferInfo.crSeen = c == 13;
        if (!bufferInfo.crSeen && bufferInfo.buffer.size() > 1024) {
            processBuffer(bufferInfo.buffer);
        }
    }

    protected void processBuffer(ByteArrayOutputStream buffer) {
        String output = buffer.toString();
        this.project.demuxOutput(output, this.isErrorStream);
        resetBufferInfo();
    }

    protected void processFlush(ByteArrayOutputStream buffer) {
        String output = buffer.toString();
        this.project.demuxFlush(output, this.isErrorStream);
        resetBufferInfo();
    }

    @Override // java.io.OutputStream, java.io.Closeable, java.lang.AutoCloseable
    public void close() throws IOException {
        flush();
        removeBuffer();
    }

    @Override // java.io.OutputStream, java.io.Flushable
    public void flush() throws IOException {
        BufferInfo bufferInfo = getBufferInfo();
        if (bufferInfo.buffer.size() > 0) {
            processFlush(bufferInfo.buffer);
        }
    }

    @Override // java.io.OutputStream
    public void write(byte[] b, int off, int len) throws IOException {
        int offset = off;
        int blockStartOffset = offset;
        int remaining = len;
        BufferInfo bufferInfo = getBufferInfo();
        while (remaining > 0) {
            while (remaining > 0 && b[offset] != 10 && b[offset] != 13) {
                offset++;
                remaining--;
            }
            int blockLength = offset - blockStartOffset;
            if (blockLength > 0) {
                bufferInfo.buffer.write(b, blockStartOffset, blockLength);
            }
            while (remaining > 0 && (b[offset] == 10 || b[offset] == 13)) {
                write(b[offset]);
                offset++;
                remaining--;
            }
            blockStartOffset = offset;
        }
    }
}
