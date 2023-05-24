package javax.mail.event;

import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/MessageChangedEvent.class */
public class MessageChangedEvent extends MailEvent {
    public static final int FLAGS_CHANGED = 1;
    public static final int ENVELOPE_CHANGED = 2;
    protected int type;
    protected transient Message msg;

    public MessageChangedEvent(Object source, int type, Message msg) {
        super(source);
        this.msg = msg;
        this.type = type;
    }

    public int getMessageChangeType() {
        return this.type;
    }

    public Message getMessage() {
        return this.msg;
    }

    @Override // javax.mail.event.MailEvent
    public void dispatch(Object listener) {
        ((MessageChangedListener) listener).messageChanged(this);
    }
}
