package javax.mail.event;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/TransportListener.class */
public interface TransportListener extends EventListener {
    void messageDelivered(TransportEvent transportEvent);

    void messageNotDelivered(TransportEvent transportEvent);

    void messagePartiallyDelivered(TransportEvent transportEvent);
}
