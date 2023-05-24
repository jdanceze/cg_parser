package javax.servlet;

import java.util.Enumeration;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/FilterConfig.class */
public interface FilterConfig {
    String getFilterName();

    ServletContext getServletContext();

    String getInitParameter(String str);

    Enumeration getInitParameterNames();
}
