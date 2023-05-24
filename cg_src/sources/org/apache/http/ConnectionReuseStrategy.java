package org.apache.http;

import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/ConnectionReuseStrategy.class */
public interface ConnectionReuseStrategy {
    boolean keepAlive(HttpResponse httpResponse, HttpContext httpContext);
}
