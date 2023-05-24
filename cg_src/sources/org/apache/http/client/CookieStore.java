package org.apache.http.client;

import java.util.Date;
import java.util.List;
import org.apache.http.cookie.Cookie;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/client/CookieStore.class */
public interface CookieStore {
    void addCookie(Cookie cookie);

    List<Cookie> getCookies();

    boolean clearExpired(Date date);

    void clear();
}
