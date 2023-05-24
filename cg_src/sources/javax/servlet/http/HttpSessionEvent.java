package javax.servlet.http;

import java.util.EventObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/servlet/http/HttpSessionEvent.class */
public class HttpSessionEvent extends EventObject {
    public HttpSessionEvent(HttpSession source) {
        super(source);
    }

    public HttpSession getSession() {
        return (HttpSession) super.getSource();
    }
}
