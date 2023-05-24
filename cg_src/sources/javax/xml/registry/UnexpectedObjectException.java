package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/UnexpectedObjectException.class */
public class UnexpectedObjectException extends JAXRException {
    public UnexpectedObjectException() {
        this.cause = null;
    }

    public UnexpectedObjectException(String str) {
        super(str);
        this.cause = null;
    }

    public UnexpectedObjectException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public UnexpectedObjectException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
