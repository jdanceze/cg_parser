package javax.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Set;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletContext.class */
public interface ServletContext {
    ServletContext getContext(String str);

    int getMajorVersion();

    int getMinorVersion();

    String getMimeType(String str);

    Set getResourcePaths(String str);

    URL getResource(String str) throws MalformedURLException;

    InputStream getResourceAsStream(String str);

    RequestDispatcher getRequestDispatcher(String str);

    RequestDispatcher getNamedDispatcher(String str);

    Servlet getServlet(String str) throws ServletException;

    Enumeration getServlets();

    Enumeration getServletNames();

    void log(String str);

    void log(Exception exc, String str);

    void log(String str, Throwable th);

    String getRealPath(String str);

    String getServerInfo();

    String getInitParameter(String str);

    Enumeration getInitParameterNames();

    Object getAttribute(String str);

    Enumeration getAttributeNames();

    void setAttribute(String str, Object obj);

    void removeAttribute(String str);

    String getServletContextName();
}
