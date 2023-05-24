package org.apache.http.auth;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.params.HttpParams;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/auth/AuthSchemeRegistry.class */
public final class AuthSchemeRegistry {
    @GuardedBy("this")
    private final Map<String, AuthSchemeFactory> registeredSchemes = new LinkedHashMap();

    public synchronized void register(String name, AuthSchemeFactory factory) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Authentication scheme factory may not be null");
        }
        this.registeredSchemes.put(name.toLowerCase(Locale.ENGLISH), factory);
    }

    public synchronized void unregister(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        this.registeredSchemes.remove(name.toLowerCase(Locale.ENGLISH));
    }

    public synchronized AuthScheme getAuthScheme(String name, HttpParams params) throws IllegalStateException {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        AuthSchemeFactory factory = this.registeredSchemes.get(name.toLowerCase(Locale.ENGLISH));
        if (factory != null) {
            return factory.newInstance(params);
        }
        throw new IllegalStateException("Unsupported authentication scheme: " + name);
    }

    public synchronized List<String> getSchemeNames() {
        return new ArrayList(this.registeredSchemes.keySet());
    }

    public synchronized void setItems(Map<String, AuthSchemeFactory> map) {
        if (map == null) {
            return;
        }
        this.registeredSchemes.clear();
        this.registeredSchemes.putAll(map);
    }
}
