package org.apache.http.impl.cookie;

import java.util.Locale;
import java.util.StringTokenizer;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieOrigin;
import org.apache.http.cookie.MalformedCookieException;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/cookie/NetscapeDomainHandler.class */
public class NetscapeDomainHandler extends BasicDomainHandler {
    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CookieAttributeHandler
    public void validate(Cookie cookie, CookieOrigin origin) throws MalformedCookieException {
        super.validate(cookie, origin);
        String host = origin.getHost();
        String domain = cookie.getDomain();
        if (host.contains(".")) {
            int domainParts = new StringTokenizer(domain, ".").countTokens();
            if (isSpecialDomain(domain)) {
                if (domainParts < 2) {
                    throw new MalformedCookieException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification for special domains");
                }
            } else if (domainParts < 3) {
                throw new MalformedCookieException("Domain attribute \"" + domain + "\" violates the Netscape cookie specification");
            }
        }
    }

    private static boolean isSpecialDomain(String domain) {
        String ucDomain = domain.toUpperCase(Locale.ENGLISH);
        return ucDomain.endsWith(".COM") || ucDomain.endsWith(".EDU") || ucDomain.endsWith(".NET") || ucDomain.endsWith(".GOV") || ucDomain.endsWith(".MIL") || ucDomain.endsWith(".ORG") || ucDomain.endsWith(".INT");
    }

    @Override // org.apache.http.impl.cookie.BasicDomainHandler, org.apache.http.cookie.CookieAttributeHandler
    public boolean match(Cookie cookie, CookieOrigin origin) {
        if (cookie == null) {
            throw new IllegalArgumentException("Cookie may not be null");
        }
        if (origin == null) {
            throw new IllegalArgumentException("Cookie origin may not be null");
        }
        String host = origin.getHost();
        String domain = cookie.getDomain();
        if (domain == null) {
            return false;
        }
        return host.endsWith(domain);
    }
}
