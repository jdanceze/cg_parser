package org.apache.http.cookie;

import java.util.List;
import org.apache.http.Header;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/CookieSpec.class */
public interface CookieSpec {
    int getVersion();

    List<Cookie> parse(Header header, CookieOrigin cookieOrigin) throws MalformedCookieException;

    void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException;

    boolean match(Cookie cookie, CookieOrigin cookieOrigin);

    List<Header> formatCookies(List<Cookie> list);

    Header getVersionHeader();
}
