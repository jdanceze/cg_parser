package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpRequestInterceptor;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/protocol/HttpRequestInterceptorList.class */
public interface HttpRequestInterceptorList {
    void addRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor);

    void addRequestInterceptor(HttpRequestInterceptor httpRequestInterceptor, int i);

    int getRequestInterceptorCount();

    HttpRequestInterceptor getRequestInterceptor(int i);

    void clearRequestInterceptors();

    void removeRequestInterceptorByClass(Class cls);

    void setInterceptors(List list);
}
