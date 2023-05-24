package javax.mail.event;

import java.util.EventListener;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/MessageCountListener.class */
public interface MessageCountListener extends EventListener {
    void messagesAdded(MessageCountEvent messageCountEvent);

    void messagesRemoved(MessageCountEvent messageCountEvent);
}
