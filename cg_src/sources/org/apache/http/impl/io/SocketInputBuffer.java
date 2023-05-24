package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/io/SocketInputBuffer.class */
public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
    private static final Class SOCKET_TIMEOUT_CLASS = SocketTimeoutExceptionClass();
    private final Socket socket;
    private boolean eof;

    private static Class SocketTimeoutExceptionClass() {
        try {
            return Class.forName("java.net.SocketTimeoutException");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean isSocketTimeoutException(InterruptedIOException e) {
        if (SOCKET_TIMEOUT_CLASS != null) {
            return SOCKET_TIMEOUT_CLASS.isInstance(e);
        }
        return true;
    }

    public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        }
        this.socket = socket;
        this.eof = false;
        buffersize = buffersize < 0 ? socket.getReceiveBufferSize() : buffersize;
        init(socket.getInputStream(), buffersize < 1024 ? 1024 : buffersize, params);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.http.impl.io.AbstractSessionInputBuffer
    public int fillBuffer() throws IOException {
        int i = super.fillBuffer();
        this.eof = i == -1;
        return i;
    }

    @Override // org.apache.http.io.SessionInputBuffer
    public boolean isDataAvailable(int timeout) throws IOException {
        boolean result = hasBufferedData();
        if (!result) {
            int oldtimeout = this.socket.getSoTimeout();
            try {
                try {
                    this.socket.setSoTimeout(timeout);
                    fillBuffer();
                    result = hasBufferedData();
                    this.socket.setSoTimeout(oldtimeout);
                } catch (InterruptedIOException e) {
                    if (!isSocketTimeoutException(e)) {
                        throw e;
                    }
                    this.socket.setSoTimeout(oldtimeout);
                }
            } catch (Throwable th) {
                this.socket.setSoTimeout(oldtimeout);
                throw th;
            }
        }
        return result;
    }

    @Override // org.apache.http.io.EofSensor
    public boolean isEof() {
        return this.eof;
    }
}
