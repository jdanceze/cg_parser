package org.apache.http;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/HttpServerConnection.class */
public interface HttpServerConnection extends HttpConnection {
    HttpRequest receiveRequestHeader() throws HttpException, IOException;

    void receiveRequestEntity(HttpEntityEnclosingRequest httpEntityEnclosingRequest) throws HttpException, IOException;

    void sendResponseHeader(HttpResponse httpResponse) throws HttpException, IOException;

    void sendResponseEntity(HttpResponse httpResponse) throws HttpException, IOException;

    void flush() throws IOException;
}
