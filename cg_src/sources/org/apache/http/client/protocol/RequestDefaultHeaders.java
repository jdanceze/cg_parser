package org.apache.http.client.protocol;

import java.io.IOException;
import java.util.Collection;
import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.protocol.HttpContext;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/protocol/RequestDefaultHeaders.class */
public class RequestDefaultHeaders implements HttpRequestInterceptor {
    @Override // org.apache.http.HttpRequestInterceptor
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        Collection<Header> defHeaders;
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        String method = request.getRequestLine().getMethod();
        if (!method.equalsIgnoreCase("CONNECT") && (defHeaders = (Collection) request.getParams().getParameter(ClientPNames.DEFAULT_HEADERS)) != null) {
            for (Header defHeader : defHeaders) {
                request.addHeader(defHeader);
            }
        }
    }
}
