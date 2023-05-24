package javax.servlet;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletRequestAttributeListener.class */
public interface ServletRequestAttributeListener extends EventListener {
    void attributeAdded(ServletRequestAttributeEvent servletRequestAttributeEvent);

    void attributeRemoved(ServletRequestAttributeEvent servletRequestAttributeEvent);

    void attributeReplaced(ServletRequestAttributeEvent servletRequestAttributeEvent);
}
