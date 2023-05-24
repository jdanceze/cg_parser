package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPConnectionFactory.class */
public abstract class SOAPConnectionFactory {
    private static final String DEFAULT_SOAP_CONNECTION_FACTORY = "com.sun.xml.messaging.saaj.client.p2p.HttpSOAPConnectionFactory";
    private static final String SF_PROPERTY = "javax.xml.soap.SOAPConnectionFactory";

    public static SOAPConnectionFactory newInstance() throws SOAPException, UnsupportedOperationException {
        try {
            return (SOAPConnectionFactory) FactoryFinder.find(SF_PROPERTY, DEFAULT_SOAP_CONNECTION_FACTORY);
        } catch (Exception e) {
            throw new SOAPException(new StringBuffer().append("Unable to create SOAP connection factory: ").append(e.getMessage()).toString());
        }
    }

    public abstract SOAPConnection createConnection() throws SOAPException;
}
