package javax.xml.rpc.handler.soap;

import javax.xml.rpc.handler.MessageContext;
import javax.xml.soap.SOAPMessage;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/rpc/handler/soap/SOAPMessageContext.class */
public interface SOAPMessageContext extends MessageContext {
    SOAPMessage getMessage();

    void setMessage(SOAPMessage sOAPMessage);

    String[] getRoles();
}
