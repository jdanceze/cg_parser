package javax.mail.event;

import javax.mail.Folder;
import javax.mail.Message;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/MessageCountEvent.class */
public class MessageCountEvent extends MailEvent {
    public static final int ADDED = 1;
    public static final int REMOVED = 2;
    protected int type;
    protected boolean removed;
    protected transient Message[] msgs;

    public MessageCountEvent(Folder folder, int type, boolean removed, Message[] msgs) {
        super(folder);
        this.type = type;
        this.removed = removed;
        this.msgs = msgs;
    }

    public int getType() {
        return this.type;
    }

    public boolean isRemoved() {
        return this.removed;
    }

    public Message[] getMessages() {
        return this.msgs;
    }

    @Override // javax.mail.event.MailEvent
    public void dispatch(Object listener) {
        if (this.type == 1) {
            ((MessageCountListener) listener).messagesAdded(this);
        } else {
            ((MessageCountListener) listener).messagesRemoved(this);
        }
    }
}
