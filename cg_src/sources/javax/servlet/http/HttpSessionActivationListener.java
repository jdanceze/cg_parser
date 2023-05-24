package javax.servlet.http;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpSessionActivationListener.class */
public interface HttpSessionActivationListener extends EventListener {
    void sessionWillPassivate(HttpSessionEvent httpSessionEvent);

    void sessionDidActivate(HttpSessionEvent httpSessionEvent);
}
