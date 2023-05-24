package org.apache.http.cookie;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/CookieAttributeHandler.class */
public interface CookieAttributeHandler {
    void parse(SetCookie setCookie, String str) throws MalformedCookieException;

    void validate(Cookie cookie, CookieOrigin cookieOrigin) throws MalformedCookieException;

    boolean match(Cookie cookie, CookieOrigin cookieOrigin);
}
