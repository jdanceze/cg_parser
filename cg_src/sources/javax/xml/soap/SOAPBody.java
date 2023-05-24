package javax.xml.soap;

import java.util.Locale;
import org.w3c.dom.Document;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPBody.class */
public interface SOAPBody extends SOAPElement {
    SOAPFault addFault() throws SOAPException;

    SOAPFault addFault(Name name, String str, Locale locale) throws SOAPException;

    SOAPFault addFault(Name name, String str) throws SOAPException;

    boolean hasFault();

    SOAPFault getFault();

    SOAPBodyElement addBodyElement(Name name) throws SOAPException;

    SOAPBodyElement addDocument(Document document) throws SOAPException;
}
