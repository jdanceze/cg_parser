package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPElementFactory.class */
public class SOAPElementFactory {
    private SOAPFactory soapFactory;

    private SOAPElementFactory(SOAPFactory sOAPFactory) {
        this.soapFactory = sOAPFactory;
    }

    public SOAPElement create(Name name) throws SOAPException {
        return this.soapFactory.createElement(name);
    }

    public SOAPElement create(String str) throws SOAPException {
        return this.soapFactory.createElement(str);
    }

    public SOAPElement create(String str, String str2, String str3) throws SOAPException {
        return this.soapFactory.createElement(str, str2, str3);
    }

    public static SOAPElementFactory newInstance() throws SOAPException {
        try {
            return new SOAPElementFactory(SOAPFactory.newInstance());
        } catch (Exception e) {
            throw new SOAPException(new StringBuffer().append("Unable to create SOAP Element Factory: ").append(e.getMessage()).toString());
        }
    }
}
