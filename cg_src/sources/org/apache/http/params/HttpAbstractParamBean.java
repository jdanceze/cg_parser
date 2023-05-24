package org.apache.http.params;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/params/HttpAbstractParamBean.class */
public abstract class HttpAbstractParamBean {
    protected final HttpParams params;

    public HttpAbstractParamBean(HttpParams params) {
        if (params == null) {
            throw new IllegalArgumentException("HTTP parameters may not be null");
        }
        this.params = params;
    }
}
