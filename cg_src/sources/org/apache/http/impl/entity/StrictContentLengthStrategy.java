package org.apache.http.impl.entity;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpVersion;
import org.apache.http.ProtocolException;
import org.apache.http.entity.ContentLengthStrategy;
import org.apache.http.protocol.HTTP;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/impl/entity/StrictContentLengthStrategy.class */
public class StrictContentLengthStrategy implements ContentLengthStrategy {
    @Override // org.apache.http.entity.ContentLengthStrategy
    public long determineLength(HttpMessage message) throws HttpException {
        if (message == null) {
            throw new IllegalArgumentException("HTTP message may not be null");
        }
        Header transferEncodingHeader = message.getFirstHeader("Transfer-Encoding");
        Header contentLengthHeader = message.getFirstHeader("Content-Length");
        if (transferEncodingHeader != null) {
            String s = transferEncodingHeader.getValue();
            if (HTTP.CHUNK_CODING.equalsIgnoreCase(s)) {
                if (message.getProtocolVersion().lessEquals(HttpVersion.HTTP_1_0)) {
                    throw new ProtocolException(new StringBuffer().append("Chunked transfer encoding not allowed for ").append(message.getProtocolVersion()).toString());
                }
                return -2L;
            } else if (HTTP.IDENTITY_CODING.equalsIgnoreCase(s)) {
                return -1L;
            } else {
                throw new ProtocolException(new StringBuffer().append("Unsupported transfer encoding: ").append(s).toString());
            }
        } else if (contentLengthHeader != null) {
            String s2 = contentLengthHeader.getValue();
            try {
                long len = Long.parseLong(s2);
                return len;
            } catch (NumberFormatException e) {
                throw new ProtocolException(new StringBuffer().append("Invalid content length: ").append(s2).toString());
            }
        } else {
            return -1L;
        }
    }
}
