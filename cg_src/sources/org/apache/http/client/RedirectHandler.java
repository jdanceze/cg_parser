package org.apache.http.client;

import java.net.URI;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolException;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/RedirectHandler.class */
public interface RedirectHandler {
    boolean isRedirectRequested(HttpResponse httpResponse, HttpContext httpContext);

    URI getLocationURI(HttpResponse httpResponse, HttpContext httpContext) throws ProtocolException;
}
