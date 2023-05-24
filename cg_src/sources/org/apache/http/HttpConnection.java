package org.apache.http;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HttpConnection.class */
public interface HttpConnection {
    void close() throws IOException;

    boolean isOpen();

    boolean isStale();

    void setSocketTimeout(int i);

    int getSocketTimeout();

    void shutdown() throws IOException;

    HttpConnectionMetrics getMetrics();
}
