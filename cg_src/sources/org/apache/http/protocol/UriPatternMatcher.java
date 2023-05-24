package org.apache.http.protocol;

import java.util.HashMap;
import java.util.Map;
import net.bytebuddy.description.type.TypeDescription;
/* loaded from: gencallgraphv3.jar:httpcore-4.0.1.jar:org/apache/http/protocol/UriPatternMatcher.class */
public class UriPatternMatcher {
    private final Map map = new HashMap();

    public void register(String pattern, Object obj) {
        if (pattern == null) {
            throw new IllegalArgumentException("URI request pattern may not be null");
        }
        this.map.put(pattern, obj);
    }

    public void unregister(String pattern) {
        if (pattern == null) {
            return;
        }
        this.map.remove(pattern);
    }

    public void setHandlers(Map map) {
        if (map == null) {
            throw new IllegalArgumentException("Map of handlers may not be null");
        }
        this.map.clear();
        this.map.putAll(map);
    }

    public Object lookup(String requestURI) {
        if (requestURI == null) {
            throw new IllegalArgumentException("Request URI may not be null");
        }
        int index = requestURI.indexOf(TypeDescription.Generic.OfWildcardType.SYMBOL);
        if (index != -1) {
            requestURI = requestURI.substring(0, index);
        }
        Object handler = this.map.get(requestURI);
        if (handler == null) {
            String bestMatch = null;
            for (String pattern : this.map.keySet()) {
                if (matchUriRequestPattern(pattern, requestURI) && (bestMatch == null || bestMatch.length() < pattern.length() || (bestMatch.length() == pattern.length() && pattern.endsWith("*")))) {
                    handler = this.map.get(pattern);
                    bestMatch = pattern;
                }
            }
        }
        return handler;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public boolean matchUriRequestPattern(String pattern, String requestUri) {
        if (pattern.equals("*")) {
            return true;
        }
        return (pattern.endsWith("*") && requestUri.startsWith(pattern.substring(0, pattern.length() - 1))) || (pattern.startsWith("*") && requestUri.endsWith(pattern.substring(1, pattern.length())));
    }
}
