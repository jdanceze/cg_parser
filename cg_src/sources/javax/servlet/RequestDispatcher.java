package javax.servlet;

import java.io.IOException;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/RequestDispatcher.class */
public interface RequestDispatcher {
    void forward(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;

    void include(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException;
}
