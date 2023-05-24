package org.apache.http.protocol;

import java.util.List;
import org.apache.http.HttpResponseInterceptor;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/protocol/HttpResponseInterceptorList.class */
public interface HttpResponseInterceptorList {
    void addResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor);

    void addResponseInterceptor(HttpResponseInterceptor httpResponseInterceptor, int i);

    int getResponseInterceptorCount();

    HttpResponseInterceptor getResponseInterceptor(int i);

    void clearResponseInterceptors();

    void removeResponseInterceptorByClass(Class cls);

    void setInterceptors(List list);
}
