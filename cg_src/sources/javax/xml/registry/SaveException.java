package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/SaveException.class */
public class SaveException extends RegistryException {
    public SaveException() {
        this.cause = null;
    }

    public SaveException(String str) {
        super(str);
        this.cause = null;
    }

    public SaveException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public SaveException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
