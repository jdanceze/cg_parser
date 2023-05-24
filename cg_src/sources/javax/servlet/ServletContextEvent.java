package javax.servlet;

import java.util.EventObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletContextEvent.class */
public class ServletContextEvent extends EventObject {
    public ServletContextEvent(ServletContext source) {
        super(source);
    }

    public ServletContext getServletContext() {
        return (ServletContext) super.getSource();
    }
}
