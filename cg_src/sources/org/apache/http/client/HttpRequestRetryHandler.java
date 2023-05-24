package org.apache.http.client;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/HttpRequestRetryHandler.class */
public interface HttpRequestRetryHandler {
    boolean retryRequest(IOException iOException, int i, HttpContext httpContext);
}
