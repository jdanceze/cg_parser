package javax.mail.event;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/ConnectionListener.class */
public interface ConnectionListener extends EventListener {
    void opened(ConnectionEvent connectionEvent);

    void disconnected(ConnectionEvent connectionEvent);

    void closed(ConnectionEvent connectionEvent);
}
