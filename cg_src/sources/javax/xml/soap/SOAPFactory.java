package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPFactory.class */
public abstract class SOAPFactory {
    private static final String SOAP_FACTORY_PROPERTY = "javax.xml.soap.SOAPFactory";
    private static final String DEFAULT_SOAP_FACTORY = "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPFactory1_1Impl";

    public abstract SOAPElement createElement(Name name) throws SOAPException;

    public abstract SOAPElement createElement(String str) throws SOAPException;

    public abstract SOAPElement createElement(String str, String str2, String str3) throws SOAPException;

    public abstract Detail createDetail() throws SOAPException;

    public abstract Name createName(String str, String str2, String str3) throws SOAPException;

    public abstract Name createName(String str) throws SOAPException;

    public static SOAPFactory newInstance() throws SOAPException {
        try {
            return (SOAPFactory) FactoryFinder.find(SOAP_FACTORY_PROPERTY, DEFAULT_SOAP_FACTORY);
        } catch (Exception e) {
            throw new SOAPException(new StringBuffer().append("Unable to create SOAP Factory: ").append(e.getMessage()).toString());
        }
    }
}
