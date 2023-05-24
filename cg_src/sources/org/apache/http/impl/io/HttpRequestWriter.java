package org.apache.http.impl.io;

import java.io.IOException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpRequest;
import org.apache.http.io.SessionOutputBuffer;
import org.apache.http.message.LineFormatter;
import org.apache.http.params.HttpParams;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/io/HttpRequestWriter.class */
public class HttpRequestWriter extends AbstractMessageWriter {
    public HttpRequestWriter(SessionOutputBuffer buffer, LineFormatter formatter, HttpParams params) {
        super(buffer, formatter, params);
    }

    @Override // org.apache.http.impl.io.AbstractMessageWriter
    protected void writeHeadLine(HttpMessage message) throws IOException {
        this.lineFormatter.formatRequestLine(this.lineBuf, ((HttpRequest) message).getRequestLine());
        this.sessionBuffer.writeLine(this.lineBuf);
    }
}
