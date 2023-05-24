package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/UnsupportedCapabilityException.class */
public class UnsupportedCapabilityException extends JAXRException {
    public UnsupportedCapabilityException() {
        this.cause = null;
    }

    public UnsupportedCapabilityException(String str) {
        super(str);
        this.cause = null;
    }

    public UnsupportedCapabilityException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public UnsupportedCapabilityException(Throwable th) {
        super(th.toString());
        initCause(th);
    }
}
