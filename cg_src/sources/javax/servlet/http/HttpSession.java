package javax.servlet.http;

import java.util.Enumeration;
import javax.servlet.ServletContext;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpSession.class */
public interface HttpSession {
    long getCreationTime();

    String getId();

    long getLastAccessedTime();

    ServletContext getServletContext();

    void setMaxInactiveInterval(int i);

    int getMaxInactiveInterval();

    HttpSessionContext getSessionContext();

    Object getAttribute(String str);

    Object getValue(String str);

    Enumeration getAttributeNames();

    String[] getValueNames();

    void setAttribute(String str, Object obj);

    void putValue(String str, Object obj);

    void removeAttribute(String str);

    void removeValue(String str);

    void invalidate();

    boolean isNew();
}
