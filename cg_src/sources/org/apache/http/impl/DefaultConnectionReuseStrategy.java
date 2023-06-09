package org.apache.http.impl;

import org.apache.http.ConnectionReuseStrategy;
import org.apache.http.HeaderIterator;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.ParseException;
import org.apache.http.ProtocolVersion;
import org.apache.http.TokenIterator;
import org.apache.http.message.BasicTokenIterator;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/DefaultConnectionReuseStrategy.class */
public class DefaultConnectionReuseStrategy implements ConnectionReuseStrategy {
    @Override // org.apache.http.ConnectionReuseStrategy
    public boolean keepAlive(HttpResponse response, HttpContext context) {
        if (response == null) {
            throw new IllegalArgumentException("HTTP response may not be null.");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null.");
        }
        HttpConnection conn = (HttpConnection) context.getAttribute(ExecutionContext.HTTP_CONNECTION);
        if (conn != null && !conn.isOpen()) {
            return false;
        }
        HttpEntity entity = response.getEntity();
        ProtocolVersion ver = response.getStatusLine().getProtocolVersion();
        if (entity != null && entity.getContentLength() < 0 && (!entity.isChunked() || ver.lessEquals(HttpVersion.HTTP_1_0))) {
            return false;
        }
        HeaderIterator hit = response.headerIterator("Connection");
        if (!hit.hasNext()) {
            hit = response.headerIterator("Proxy-Connection");
        }
        if (hit.hasNext()) {
            try {
                TokenIterator ti = createTokenIterator(hit);
                boolean keepalive = false;
                while (ti.hasNext()) {
                    String token = ti.nextToken();
                    if (HTTP.CONN_CLOSE.equalsIgnoreCase(token)) {
                        return false;
                    }
                    if (HTTP.CONN_KEEP_ALIVE.equalsIgnoreCase(token)) {
                        keepalive = true;
                    }
                }
                if (keepalive) {
                    return true;
                }
            } catch (ParseException e) {
                return false;
            }
        }
        return !ver.lessEquals(HttpVersion.HTTP_1_0);
    }

    protected TokenIterator createTokenIterator(HeaderIterator hit) {
        return new BasicTokenIterator(hit);
    }
}
