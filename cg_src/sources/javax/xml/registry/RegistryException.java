package javax.xml.registry;

import javax.xml.registry.infomodel.Key;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/RegistryException.class */
public class RegistryException extends JAXRException {
    private Key errorObjectKey;

    public RegistryException() {
        this.cause = null;
    }

    public RegistryException(String str) {
        super(str);
        this.cause = null;
    }

    public RegistryException(String str, Throwable th) {
        super(str);
        initCause(th);
    }

    public RegistryException(Throwable th) {
        super(th.toString());
        initCause(th);
    }

    public Key getErrorObjectKey() throws JAXRException {
        return this.errorObjectKey;
    }

    public void setErrorObjectKey(Key key) throws JAXRException {
        this.errorObjectKey = key;
    }
}
