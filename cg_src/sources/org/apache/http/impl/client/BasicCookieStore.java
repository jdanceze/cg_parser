package org.apache.http.impl.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.http.annotation.GuardedBy;
import org.apache.http.annotation.ThreadSafe;
import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.cookie.CookieIdentityComparator;
@ThreadSafe
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/client/BasicCookieStore.class */
public class BasicCookieStore implements CookieStore {
    @GuardedBy("this")
    private final ArrayList<Cookie> cookies = new ArrayList<>();
    @GuardedBy("this")
    private final Comparator<Cookie> cookieComparator = new CookieIdentityComparator();

    @Override // org.apache.http.client.CookieStore
    public synchronized void addCookie(Cookie cookie) {
        if (cookie != null) {
            Iterator<Cookie> it = this.cookies.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (this.cookieComparator.compare(cookie, it.next()) == 0) {
                    it.remove();
                    break;
                }
            }
            if (!cookie.isExpired(new Date())) {
                this.cookies.add(cookie);
            }
        }
    }

    public synchronized void addCookies(Cookie[] cookies) {
        if (cookies != null) {
            for (Cookie cooky : cookies) {
                addCookie(cooky);
            }
        }
    }

    @Override // org.apache.http.client.CookieStore
    public synchronized List<Cookie> getCookies() {
        return Collections.unmodifiableList(this.cookies);
    }

    @Override // org.apache.http.client.CookieStore
    public synchronized boolean clearExpired(Date date) {
        if (date == null) {
            return false;
        }
        boolean removed = false;
        Iterator<Cookie> it = this.cookies.iterator();
        while (it.hasNext()) {
            if (it.next().isExpired(date)) {
                it.remove();
                removed = true;
            }
        }
        return removed;
    }

    public String toString() {
        return this.cookies.toString();
    }

    @Override // org.apache.http.client.CookieStore
    public synchronized void clear() {
        this.cookies.clear();
    }
}
