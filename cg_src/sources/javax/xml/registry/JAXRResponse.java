package javax.xml.registry;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/registry/JAXRResponse.class */
public interface JAXRResponse {
    public static final int STATUS_SUCCESS = 0;
    public static final int STATUS_WARNING = 1;
    public static final int STATUS_FAILURE = 2;
    public static final int STATUS_UNAVAILABLE = 3;

    String getRequestId() throws JAXRException;

    int getStatus() throws JAXRException;

    boolean isAvailable() throws JAXRException;
}
