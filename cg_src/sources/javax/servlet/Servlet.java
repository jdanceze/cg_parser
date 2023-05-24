package javax.servlet;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/Servlet.class */
public interface Servlet {
    void init(ServletConfig servletConfig) throws ServletException;

    ServletConfig getServletConfig();

    void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;

    String getServletInfo();

    void destroy();
}
