package org.apache.http.conn;

import org.apache.http.HttpResponse;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/ConnectionKeepAliveStrategy.class */
public interface ConnectionKeepAliveStrategy {
    long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext);
}
