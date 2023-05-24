package javax.servlet;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletConfig.class */
public interface ServletConfig {
    String getServletName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration getInitParameterNames();
}
