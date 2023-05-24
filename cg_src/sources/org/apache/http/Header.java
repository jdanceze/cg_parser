package org.apache.http;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/Header.class */
public interface Header {
    String getName();

    String getValue();

    HeaderElement[] getElements() throws ParseException;
}
