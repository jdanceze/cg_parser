package org.apache.http;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/RequestLine.class */
public interface RequestLine {
    String getMethod();

    ProtocolVersion getProtocolVersion();

    String getUri();
}
