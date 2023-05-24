package org.apache.http.client;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/CredentialsProvider.class */
public interface CredentialsProvider {
    void setCredentials(AuthScope authScope, Credentials credentials);

    Credentials getCredentials(AuthScope authScope);

    void clear();
}
