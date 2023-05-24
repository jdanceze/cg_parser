package javax.xml.soap;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPConnection.class */
public abstract class SOAPConnection {
    public abstract SOAPMessage call(SOAPMessage sOAPMessage, Object obj) throws SOAPException;

    public abstract void close() throws SOAPException;
}
