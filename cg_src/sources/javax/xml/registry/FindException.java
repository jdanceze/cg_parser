package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/FindException.class */
public class FindException extends RegistryException {
    public FindException() {
        this.cause = null;
    }

    public FindException(String str) {
        super(str);
        this.cause = null;
    }

    public FindException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public FindException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
