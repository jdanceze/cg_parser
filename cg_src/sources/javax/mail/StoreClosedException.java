package javax.mail;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/mail/StoreClosedException.class */
public class StoreClosedException extends MessagingException {
    private transient Store store;

    public StoreClosedException(Store store) {
        this(store, null);
    }

    public StoreClosedException(Store store, String message) {
        super(message);
        this.store = store;
    }

    public Store getStore() {
        return this.store;
    }
}
