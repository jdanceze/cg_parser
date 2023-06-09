package org.apache.http.conn;

import java.net.ConnectException;
import org.apache.http.HttpHost;
import org.apache.http.annotation.Immutable;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/conn/HttpHostConnectException.class */
public class HttpHostConnectException extends ConnectException {
    private static final long serialVersionUID = -3194482710275220224L;
    private final HttpHost host;

    public HttpHostConnectException(HttpHost host, ConnectException cause) {
        super("Connection to " + host + " refused");
        this.host = host;
        initCause(cause);
    }

    public HttpHost getHost() {
        return this.host;
    }
}
