package org.apache.http.impl.client;

import java.net.URI;
import org.apache.http.annotation.NotThreadSafe;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpRequestBase;
@NotThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/HttpRedirect.class */
class HttpRedirect extends HttpRequestBase {
    private String method;

    public HttpRedirect(String method, URI uri) {
        if (method.equalsIgnoreCase(HttpHead.METHOD_NAME)) {
            this.method = HttpHead.METHOD_NAME;
        } else {
            this.method = HttpGet.METHOD_NAME;
        }
        setURI(uri);
    }

    @Override // org.apache.http.client.methods.HttpRequestBase, org.apache.http.client.methods.HttpUriRequest
    public String getMethod() {
        return this.method;
    }
}
