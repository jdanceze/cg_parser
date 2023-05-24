package javax.xml.soap;

import java.io.IOException;
import java.io.InputStream;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/MessageFactory.class */
public abstract class MessageFactory {
    private static final String DEFAULT_MESSAGE_FACTORY = "com.sun.xml.messaging.saaj.soap.ver1_1.SOAPMessageFactory1_1Impl";
    private static final String MESSAGE_FACTORY_PROPERTY = "javax.xml.soap.MessageFactory";

    public static MessageFactory newInstance() throws SOAPException {
        try {
            return (MessageFactory) FactoryFinder.find(MESSAGE_FACTORY_PROPERTY, DEFAULT_MESSAGE_FACTORY);
        } catch (Exception e) {
            throw new SOAPException(new StringBuffer().append("Unable to create message factory for SOAP: ").append(e.getMessage()).toString());
        }
    }

    public abstract SOAPMessage createMessage() throws SOAPException;

    public abstract SOAPMessage createMessage(MimeHeaders mimeHeaders, InputStream inputStream) throws IOException, SOAPException;
}
