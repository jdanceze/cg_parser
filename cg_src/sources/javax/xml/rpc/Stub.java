package javax.xml.rpc;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/Stub.class */
public interface Stub {
    public static final String USERNAME_PROPERTY = "javax.xml.rpc.security.auth.username";
    public static final String PASSWORD_PROPERTY = "javax.xml.rpc.security.auth.password";
    public static final String ENDPOINT_ADDRESS_PROPERTY = "javax.xml.rpc.service.endpoint.address";
    public static final String SESSION_MAINTAIN_PROPERTY = "javax.xml.rpc.session.maintain";

    void _setProperty(String str, Object obj);

    Object _getProperty(String str);

    Iterator _getPropertyNames();
}
