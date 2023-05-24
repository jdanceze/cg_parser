package org.apache.http.client;

import org.apache.http.ProtocolException;
import org.apache.http.annotation.Immutable;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/NonRepeatableRequestException.class */
public class NonRepeatableRequestException extends ProtocolException {
    private static final long serialVersionUID = 82685265288806048L;

    public NonRepeatableRequestException() {
    }

    public NonRepeatableRequestException(String message) {
        super(message);
    }

    public NonRepeatableRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
