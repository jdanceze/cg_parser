package org.apache.http.protocol;

import java.util.Map;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/protocol/HttpRequestHandlerRegistry.class */
public class HttpRequestHandlerRegistry implements HttpRequestHandlerResolver {
    private final UriPatternMatcher matcher = new UriPatternMatcher();

    public void register(String pattern, HttpRequestHandler handler) {
        if (pattern == null) {
            throw new IllegalArgumentException("URI request pattern may not be null");
        }
        if (handler == null) {
            throw new IllegalArgumentException("Request handler may not be null");
        }
        this.matcher.register(pattern, handler);
    }

    public void unregister(String pattern) {
        this.matcher.unregister(pattern);
    }

    public void setHandlers(Map map) {
        this.matcher.setHandlers(map);
    }

    @Override // org.apache.http.protocol.HttpRequestHandlerResolver
    public HttpRequestHandler lookup(String requestURI) {
        return (HttpRequestHandler) this.matcher.lookup(requestURI);
    }

    protected boolean matchUriRequestPattern(String pattern, String requestUri) {
        return this.matcher.matchUriRequestPattern(pattern, requestUri);
    }
}
