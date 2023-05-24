package javax.mail.event;

import java.util.EventObject;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/MailEvent.class */
public abstract class MailEvent extends EventObject {
    public abstract void dispatch(Object obj);

    public MailEvent(Object source) {
        super(source);
    }
}
