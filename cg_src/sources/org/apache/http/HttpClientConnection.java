package org.apache.http;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HttpClientConnection.class */
public interface HttpClientConnection extends HttpConnection {
    boolean isResponseAvailable(int i) throws IOException;

    void sendRequestHeader(HttpRequest httpRequest) throws HttpException, IOException;

    void sendRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException;

    HttpResponse receiveResponseHeader() throws HttpException, IOException;

    void receiveResponseEntity(HttpResponse httpResponse) throws HttpException, IOException;

    void flush() throws IOException;
}
