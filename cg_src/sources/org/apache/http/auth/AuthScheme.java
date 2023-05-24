package org.apache.http.auth;

import org.apache.http.Header;
import org.apache.http.HttpRequest;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/auth/AuthScheme.class */
public interface AuthScheme {
    void processChallenge(Header header) throws MalformedChallengeException;

    String getSchemeName();

    String getParameter(String str);

    String getRealm();

    boolean isConnectionBased();

    boolean isComplete();

    Header authenticate(Credentials credentials, HttpRequest httpRequest) throws AuthenticationException;
}
