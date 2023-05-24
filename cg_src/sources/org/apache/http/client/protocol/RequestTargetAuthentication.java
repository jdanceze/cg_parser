package org.apache.http.client.protocol;

import java.io.IOException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.Credentials;
import org.apache.http.protocol.HttpContext;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/protocol/RequestTargetAuthentication.class */
public class RequestTargetAuthentication implements HttpRequestInterceptor {
    private final Log log = LogFactory.getLog(getClass());

    @Override // org.apache.http.HttpRequestInterceptor
    public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
        AuthState authState;
        AuthScheme authScheme;
        if (request == null) {
            throw new IllegalArgumentException("HTTP request may not be null");
        }
        if (context == null) {
            throw new IllegalArgumentException("HTTP context may not be null");
        }
        String method = request.getRequestLine().getMethod();
        if (method.equalsIgnoreCase("CONNECT") || request.containsHeader("Authorization") || (authState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE)) == null || (authScheme = authState.getAuthScheme()) == null) {
            return;
        }
        Credentials creds = authState.getCredentials();
        if (creds == null) {
            this.log.debug("User credentials not available");
        } else if (authState.getAuthScope() != null || !authScheme.isConnectionBased()) {
            try {
                request.addHeader(authScheme.authenticate(creds, request));
            } catch (AuthenticationException ex) {
                if (this.log.isErrorEnabled()) {
                    this.log.error("Authentication error: " + ex.getMessage());
                }
            }
        }
    }
}
