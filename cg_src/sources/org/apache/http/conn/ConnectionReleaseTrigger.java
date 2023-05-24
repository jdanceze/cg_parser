package org.apache.http.conn;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/ConnectionReleaseTrigger.class */
public interface ConnectionReleaseTrigger {
    void releaseConnection() throws IOException;

    void abortConnection() throws IOException;
}
