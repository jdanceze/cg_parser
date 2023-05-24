package javax.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletRequest.class */
public interface ServletRequest {
    Object getAttribute(String str);

    Enumeration getAttributeNames();

    String getCharacterEncoding();

    void setCharacterEncoding(String str) throws UnsupportedEncodingException;

    int getContentLength();

    String getContentType();

    ServletInputStream getInputStream() throws IOException;

    String getParameter(String str);

    Enumeration getParameterNames();

    String[] getParameterValues(String str);

    Map getParameterMap();

    String getProtocol();

    String getScheme();

    String getServerName();

    int getServerPort();

    BufferedReader getReader() throws IOException;

    String getRemoteAddr();

    String getRemoteHost();

    void setAttribute(String str, Object obj);

    void removeAttribute(String str);

    Locale getLocale();

    Enumeration getLocales();

    boolean isSecure();

    RequestDispatcher getRequestDispatcher(String str);

    String getRealPath(String str);

    int getRemotePort();

    String getLocalName();

    String getLocalAddr();

    int getLocalPort();
}
