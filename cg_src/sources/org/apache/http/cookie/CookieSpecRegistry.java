package org.apache.http.cookie;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.params.HttpParams;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/CookieSpecRegistry.class */
public final class CookieSpecRegistry {
    @GuardedBy("this")
    private final Map<String, CookieSpecFactory> registeredSpecs = new LinkedHashMap();

    public synchronized void register(String name, CookieSpecFactory factory) {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        if (factory == null) {
            throw new IllegalArgumentException("Cookie spec factory may not be null");
        }
        this.registeredSpecs.put(name.toLowerCase(Locale.ENGLISH), factory);
    }

    public synchronized void unregister(String id) {
        if (id == null) {
            throw new IllegalArgumentException("Id may not be null");
        }
        this.registeredSpecs.remove(id.toLowerCase(Locale.ENGLISH));
    }

    public synchronized CookieSpec getCookieSpec(String name, HttpParams params) throws IllegalStateException {
        if (name == null) {
            throw new IllegalArgumentException("Name may not be null");
        }
        CookieSpecFactory factory = this.registeredSpecs.get(name.toLowerCase(Locale.ENGLISH));
        if (factory != null) {
            return factory.newInstance(params);
        }
        throw new IllegalStateException("Unsupported cookie spec: " + name);
    }

    public synchronized CookieSpec getCookieSpec(String name) throws IllegalStateException {
        return getCookieSpec(name, null);
    }

    public synchronized List<String> getSpecNames() {
        return new ArrayList(this.registeredSpecs.keySet());
    }

    public synchronized void setItems(Map<String, CookieSpecFactory> map) {
        if (map == null) {
            return;
        }
        this.registeredSpecs.clear();
        this.registeredSpecs.putAll(map);
    }
}
