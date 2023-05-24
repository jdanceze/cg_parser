package org.apache.http.impl.cookie;

import java.util.Collection;
import org.apache.http.annotation.Immutable;
import org.apache.http.cookie.CookieSpec;
import org.apache.http.cookie.CookieSpecFactory;
import org.apache.http.cookie.params.CookieSpecPNames;
import org.apache.http.params.HttpParams;
@Immutable
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/impl/cookie/BrowserCompatSpecFactory.class */
public class BrowserCompatSpecFactory implements CookieSpecFactory {
    @Override // org.apache.http.cookie.CookieSpecFactory
    public CookieSpec newInstance(HttpParams params) {
        if (params != null) {
            String[] patterns = null;
            Collection<?> param = (Collection) params.getParameter(CookieSpecPNames.DATE_PATTERNS);
            if (param != null) {
                String[] patterns2 = new String[param.size()];
                patterns = (String[]) param.toArray(patterns2);
            }
            return new BrowserCompatSpec(patterns);
        }
        return new BrowserCompatSpec();
    }
}
