package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/InvalidRequestException.class */
public class InvalidRequestException extends JAXRException {
    public InvalidRequestException() {
        this.cause = null;
    }

    public InvalidRequestException(String str) {
        super(str);
        this.cause = null;
    }

    public InvalidRequestException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public InvalidRequestException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
