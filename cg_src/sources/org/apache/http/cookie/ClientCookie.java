package org.apache.http.cookie;
/* loaded from: gencallgraphv3.jar:httpclient-4.0.1.jar:org/apache/http/cookie/ClientCookie.class */
public interface ClientCookie extends Cookie {
    public static final String VERSION_ATTR = "version";
    public static final String PATH_ATTR = "path";
    public static final String DOMAIN_ATTR = "domain";
    public static final String MAX_AGE_ATTR = "max-age";
    public static final String SECURE_ATTR = "secure";
    public static final String COMMENT_ATTR = "comment";
    public static final String EXPIRES_ATTR = "expires";
    public static final String PORT_ATTR = "port";
    public static final String COMMENTURL_ATTR = "commenturl";
    public static final String DISCARD_ATTR = "discard";

    String getAttribute(String str);

    boolean containsAttribute(String str);
}
