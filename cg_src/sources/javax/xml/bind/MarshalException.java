package javax.xml.bind;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/MarshalException.class */
public class MarshalException extends JAXBException {
    public MarshalException(String message) {
        this(message, null, null);
    }

    public MarshalException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public MarshalException(Throwable exception) {
        this(null, null, exception);
    }

    public MarshalException(String message, Throwable exception) {
        this(message, null, exception);
    }

    public MarshalException(String message, String errorCode, Throwable exception) {
        super(message, errorCode, exception);
    }
}
