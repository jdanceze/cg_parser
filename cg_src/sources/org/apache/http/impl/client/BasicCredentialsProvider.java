package org.apache.http.impl.client;

import java.util.HashMap;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.Credentials;
import org.apache.http.client.CredentialsProvider;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/BasicCredentialsProvider.class */
public class BasicCredentialsProvider implements CredentialsProvider {
    @GuardedBy("this")
    private final HashMap<AuthScope, Credentials> credMap = new HashMap<>();

    @Override // org.apache.http.client.CredentialsProvider
    public synchronized void setCredentials(AuthScope authscope, Credentials credentials) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        this.credMap.put(authscope, credentials);
    }

    private static Credentials matchCredentials(HashMap<AuthScope, Credentials> map, AuthScope authscope) {
        Credentials creds = map.get(authscope);
        if (creds == null) {
            int bestMatchFactor = -1;
            AuthScope bestMatch = null;
            for (AuthScope current : map.keySet()) {
                int factor = authscope.match(current);
                if (factor > bestMatchFactor) {
                    bestMatchFactor = factor;
                    bestMatch = current;
                }
            }
            if (bestMatch != null) {
                creds = map.get(bestMatch);
            }
        }
        return creds;
    }

    @Override // org.apache.http.client.CredentialsProvider
    public synchronized Credentials getCredentials(AuthScope authscope) {
        if (authscope == null) {
            throw new IllegalArgumentException("Authentication scope may not be null");
        }
        return matchCredentials(this.credMap, authscope);
    }

    public String toString() {
        return this.credMap.toString();
    }

    @Override // org.apache.http.client.CredentialsProvider
    public synchronized void clear() {
        this.credMap.clear();
    }
}
