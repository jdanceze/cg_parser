package org.apache.http.io;

import java.io.IOException;
import org.apache.http.util.CharArrayBuffer;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/io/SessionInputBuffer.class */
public interface SessionInputBuffer {
    int read(byte[] bArr, int i, int i2) throws IOException;

    int read(byte[] bArr) throws IOException;

    int read() throws IOException;

    int readLine(CharArrayBuffer charArrayBuffer) throws IOException;

    String readLine() throws IOException;

    boolean isDataAvailable(int i) throws IOException;

    HttpTransportMetrics getMetrics();
}
