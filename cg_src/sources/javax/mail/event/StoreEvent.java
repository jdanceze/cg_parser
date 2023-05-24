package javax.mail.event;

import javax.mail.Store;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/event/StoreEvent.class */
public class StoreEvent extends MailEvent {
    public static final int ALERT = 1;
    public static final int NOTICE = 2;
    protected int type;
    protected String message;

    public StoreEvent(Store store, int type, String message) {
        super(store);
        this.type = type;
        this.message = message;
    }

    public int getMessageType() {
        return this.type;
    }

    public String getMessage() {
        return this.message;
    }

    @Override // javax.mail.event.MailEvent
    public void dispatch(Object listener) {
        ((StoreListener) listener).notification(this);
    }
}
