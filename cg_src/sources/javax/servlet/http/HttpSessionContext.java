package javax.servlet.http;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpSessionContext.class */
public interface HttpSessionContext {
    HttpSession getSession(String str);

    Enumeration getIds();
}
