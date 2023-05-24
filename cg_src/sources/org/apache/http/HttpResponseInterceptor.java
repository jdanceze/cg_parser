package org.apache.http;

import java.io.IOException;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HttpResponseInterceptor.class */
public interface HttpResponseInterceptor {
    void process(HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException;
}
