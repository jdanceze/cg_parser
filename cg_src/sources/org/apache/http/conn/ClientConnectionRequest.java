package org.apache.http.conn;

import java.util.concurrent.TimeUnit;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/ClientConnectionRequest.class */
public interface ClientConnectionRequest {
    ManagedClientConnection getConnection(long j, TimeUnit timeUnit) throws InterruptedException, ConnectionPoolTimeoutException;

    void abortRequest();
}
