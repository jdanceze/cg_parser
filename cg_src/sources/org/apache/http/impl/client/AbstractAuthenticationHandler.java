package org.apache.http.impl.client;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.FormattedHeader;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.annotation.Immutable;
import org.apache.http.auth.AuthScheme;
import org.apache.http.auth.AuthSchemeRegistry;
import org.apache.http.auth.AuthenticationException;
import org.apache.http.auth.MalformedChallengeException;
import org.apache.http.client.AuthenticationHandler;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.CharArrayBuffer;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/AbstractAuthenticationHandler.class */
public abstract class AbstractAuthenticationHandler implements AuthenticationHandler {
    private final Log log = LogFactory.getLog(getClass());
    private static final List<String> DEFAULT_SCHEME_PRIORITY = Collections.unmodifiableList(Arrays.asList("ntlm", "digest", "basic"));

    /* JADX INFO: Access modifiers changed from: protected */
    public Map<String, Header> parseChallenges(Header[] headers) throws MalformedChallengeException {
        CharArrayBuffer buffer;
        int pos;
        Map<String, Header> map = new HashMap<>(headers.length);
        for (Header header : headers) {
            if (header instanceof FormattedHeader) {
                buffer = ((FormattedHeader) header).getBuffer();
                pos = ((FormattedHeader) header).getValuePos();
            } else {
                String s = header.getValue();
                if (s == null) {
                    throw new MalformedChallengeException("Header value is null");
                }
                buffer = new CharArrayBuffer(s.length());
                buffer.append(s);
                pos = 0;
            }
            while (pos < buffer.length() && HTTP.isWhitespace(buffer.charAt(pos))) {
                pos++;
            }
            int beginIndex = pos;
            while (pos < buffer.length() && !HTTP.isWhitespace(buffer.charAt(pos))) {
                pos++;
            }
            int endIndex = pos;
            map.put(buffer.substring(beginIndex, endIndex).toLowerCase(Locale.ENGLISH), header);
        }
        return map;
    }

    protected List<String> getAuthPreferences() {
        return DEFAULT_SCHEME_PRIORITY;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.http.client.AuthenticationHandler
    public AuthScheme selectScheme(Map<String, Header> challenges, HttpResponse response, HttpContext context) throws AuthenticationException {
        AuthSchemeRegistry registry = (AuthSchemeRegistry) context.getAttribute(ClientContext.AUTHSCHEME_REGISTRY);
        if (registry == null) {
            throw new IllegalStateException("AuthScheme registry not set in HTTP context");
        }
        Collection<String> authPrefs = (Collection) context.getAttribute(ClientContext.AUTH_SCHEME_PREF);
        if (authPrefs == null) {
            authPrefs = getAuthPreferences();
        }
        if (this.log.isDebugEnabled()) {
            this.log.debug("Authentication schemes in the order of preference: " + authPrefs);
        }
        AuthScheme authScheme = null;
        for (String id : authPrefs) {
            Header challenge = challenges.get(id.toLowerCase(Locale.ENGLISH));
            if (challenge != null) {
                if (this.log.isDebugEnabled()) {
                    this.log.debug(id + " authentication scheme selected");
                }
                try {
                    authScheme = registry.getAuthScheme(id, response.getParams());
                    break;
                } catch (IllegalStateException e) {
                    if (this.log.isWarnEnabled()) {
                        this.log.warn("Authentication scheme " + id + " not supported");
                    }
                }
            } else if (this.log.isDebugEnabled()) {
                this.log.debug("Challenge for " + id + " authentication scheme not available");
            }
        }
        if (authScheme == null) {
            throw new AuthenticationException("Unable to respond to any of these challenges: " + challenges);
        }
        return authScheme;
    }
}
