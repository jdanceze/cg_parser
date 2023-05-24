package org.apache.http;

import org.apache.http.params.HttpParams;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HttpMessage.class */
public interface HttpMessage {
    ProtocolVersion getProtocolVersion();

    boolean containsHeader(String str);

    Header[] getHeaders(String str);

    Header getFirstHeader(String str);

    Header getLastHeader(String str);

    Header[] getAllHeaders();

    void addHeader(Header header);

    void addHeader(String str, String str2);

    void setHeader(Header header);

    void setHeader(String str, String str2);

    void setHeaders(Header[] headerArr);

    void removeHeader(Header header);

    void removeHeaders(String str);

    HeaderIterator headerIterator();

    HeaderIterator headerIterator(String str);

    HttpParams getParams();

    void setParams(HttpParams httpParams);
}
