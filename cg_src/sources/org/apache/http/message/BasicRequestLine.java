package org.apache.http.message;

import org.apache.http.ProtocolVersion;
import org.apache.http.RequestLine;
import org.apache.http.util.CharArrayBuffer;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/message/BasicRequestLine.class */
public class BasicRequestLine implements RequestLine, Cloneable {
    private final ProtocolVersion protoversion;
    private final String method;
    private final String uri;

    public BasicRequestLine(String method, String uri, ProtocolVersion version) {
        if (method == null) {
            throw new IllegalArgumentException("Method must not be null.");
        }
        if (uri == null) {
            throw new IllegalArgumentException("URI must not be null.");
        }
        if (version == null) {
            throw new IllegalArgumentException("Protocol version must not be null.");
        }
        this.method = method;
        this.uri = uri;
        this.protoversion = version;
    }

    @Override // org.apache.http.RequestLine
    public String getMethod() {
        return this.method;
    }

    @Override // org.apache.http.RequestLine
    public ProtocolVersion getProtocolVersion() {
        return this.protoversion;
    }

    @Override // org.apache.http.RequestLine
    public String getUri() {
        return this.uri;
    }

    public String toString() {
        return BasicLineFormatter.DEFAULT.formatRequestLine((CharArrayBuffer) null, this).toString();
    }

    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
