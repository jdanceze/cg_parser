package org.apache.http.auth;

import java.security.Principal;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/auth/Credentials.class */
public interface Credentials {
    Principal getUserPrincipal();

    String getPassword();
}
