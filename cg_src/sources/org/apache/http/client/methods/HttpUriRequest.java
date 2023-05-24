package org.apache.http.client.methods;

import java.net.URI;
import org.apache.http.HttpRequest;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/methods/HttpUriRequest.class */
public interface HttpUriRequest extends HttpRequest {
    String getMethod();

    URI getURI();

    void abort() throws UnsupportedOperationException;

    boolean isAborted();
}
