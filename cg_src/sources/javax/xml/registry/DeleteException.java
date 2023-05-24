package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/DeleteException.class */
public class DeleteException extends RegistryException {
    public DeleteException() {
        this.cause = null;
    }

    public DeleteException(String str) {
        super(str);
        this.cause = null;
    }

    public DeleteException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public DeleteException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
