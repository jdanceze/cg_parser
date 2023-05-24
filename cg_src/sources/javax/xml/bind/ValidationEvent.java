package javax.xml.bind;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/ValidationEvent.class */
public interface ValidationEvent {
    public static final int WARNING = 0;
    public static final int ERROR = 1;
    public static final int FATAL_ERROR = 2;

    int getSeverity();

    String getMessage();

    Throwable getLinkedException();

    ValidationEventLocator getLocator();
}
