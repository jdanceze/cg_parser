package org.apache.http.client;

import java.io.IOException;
import org.apache.http.HttpResponse;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/ResponseHandler.class */
public interface ResponseHandler<T> {
    T handleResponse(HttpResponse httpResponse) throws ClientProtocolException, IOException;
}
