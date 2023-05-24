package org.apache.http.client;

import java.util.Map;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.protocol.HttpContext;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/AuthenticationHandler.class */
public interface AuthenticationHandler {
    boolean isAuthenticationRequested(HttpResponse httpResponse, HttpContext httpContext);

    Map<String, Header> getChallenges(HttpResponse httpResponse, HttpContext httpContext) throws MalformedChallengeException;

    AuthScheme selectScheme(Map<String, Header> map, HttpResponse httpResponse, HttpContext httpContext) throws AuthenticationException;
}
