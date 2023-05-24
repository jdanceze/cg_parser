package org.apache.http.client.methods;

import java.io.IOException;
import org.apache.http.conn.ClientConnectionRequest;
import org.apache.http.conn.ConnectionReleaseTrigger;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/methods/AbortableHttpRequest.class */
public interface AbortableHttpRequest {
    void setConnectionRequest(ClientConnectionRequest clientConnectionRequest) throws IOException;

    void setReleaseTrigger(ConnectionReleaseTrigger connectionReleaseTrigger) throws IOException;

    void abort();
}
