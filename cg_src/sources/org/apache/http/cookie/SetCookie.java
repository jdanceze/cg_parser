package org.apache.http.cookie;

import java.util.Date;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/SetCookie.class */
public interface SetCookie extends Cookie {
    void setValue(String str);

    void setComment(String str);

    void setExpiryDate(Date date);

    void setDomain(String str);

    void setPath(String str);

    void setSecure(boolean z);

    void setVersion(int i);
}
