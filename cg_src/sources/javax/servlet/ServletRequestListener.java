package javax.servlet;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/ServletRequestListener.class */
public interface ServletRequestListener extends EventListener {
    void requestDestroyed(ServletRequestEvent servletRequestEvent);

    void requestInitialized(ServletRequestEvent servletRequestEvent);
}
