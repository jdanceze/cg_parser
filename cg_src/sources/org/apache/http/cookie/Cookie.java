package org.apache.http.cookie;

import java.util.Date;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/Cookie.class */
public interface Cookie {
    String getName();

    String getValue();

    String getComment();

    String getCommentURL();

    Date getExpiryDate();

    boolean isPersistent();

    String getDomain();

    String getPath();

    int[] getPorts();

    boolean isSecure();

    int getVersion();

    boolean isExpired(Date date);
}
