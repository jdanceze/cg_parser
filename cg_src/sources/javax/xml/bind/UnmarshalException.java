package javax.xml.bind;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/UnmarshalException.class */
public class UnmarshalException extends JAXBException {
    public UnmarshalException(String message) {
        this(message, null, null);
    }

    public UnmarshalException(String message, String errorCode) {
        this(message, errorCode, null);
    }

    public UnmarshalException(Throwable exception) {
        this(null, null, exception);
    }

    public UnmarshalException(String message, Throwable exception) {
        this(message, null, exception);
    }

    public UnmarshalException(String message, String errorCode, Throwable exception) {
        super(message, errorCode, exception);
    }
}
