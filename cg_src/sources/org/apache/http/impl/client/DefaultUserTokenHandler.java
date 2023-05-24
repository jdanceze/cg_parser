package org.apache.http.impl.client;

import java.security.Principal;
import javax.net.ssl.SSLSession;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthState;
import org.apache.http.auth.Credentials;
import org.apache.http.client.UserTokenHandler;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.ManagedClientConnection;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/DefaultUserTokenHandler.class */
public class DefaultUserTokenHandler implements UserTokenHandler {
    @Override // org.apache.http.client.UserTokenHandler
    public Object getUserToken(HttpContext context) {
        SSLSession sslsession;
        Principal userPrincipal = null;
        AuthState targetAuthState = (AuthState) context.getAttribute(ClientContext.TARGET_AUTH_STATE);
        if (targetAuthState != null) {
            userPrincipal = getAuthPrincipal(targetAuthState);
            if (userPrincipal == null) {
                AuthState proxyAuthState = (AuthState) context.getAttribute(ClientContext.PROXY_AUTH_STATE);
                userPrincipal = getAuthPrincipal(proxyAuthState);
            }
        }
        if (userPrincipal == null) {
            ManagedClientConnection conn = (ManagedClientConnection) context.getAttribute(ExecutionContext.HTTP_CONNECTION);
            if (conn.isOpen() && (sslsession = conn.getSSLSession()) != null) {
                userPrincipal = sslsession.getLocalPrincipal();
            }
        }
        return userPrincipal;
    }

    private static Principal getAuthPrincipal(AuthState authState) {
        Credentials creds;
        AuthScheme scheme = authState.getAuthScheme();
        if (scheme != null && scheme.isComplete() && scheme.isConnectionBased() && (creds = authState.getCredentials()) != null) {
            return creds.getUserPrincipal();
        }
        return null;
    }
}
