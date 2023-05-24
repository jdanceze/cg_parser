package org.apache.http.impl.auth;

import org.apache.http.annotation.Immutable;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/auth/UnsupportedDigestAlgorithmException.class */
public class UnsupportedDigestAlgorithmException extends RuntimeException {
    private static final long serialVersionUID = 319558534317118022L;

    public UnsupportedDigestAlgorithmException() {
    }

    public UnsupportedDigestAlgorithmException(String message) {
        super(message);
    }

    public UnsupportedDigestAlgorithmException(String message, Throwable cause) {
        super(message, cause);
    }
}
