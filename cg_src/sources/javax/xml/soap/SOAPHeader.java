package javax.xml.soap;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPHeader.class */
public interface SOAPHeader extends SOAPElement {
    SOAPHeaderElement addHeaderElement(Name name) throws SOAPException;

    Iterator examineMustUnderstandHeaderElements(String str);

    Iterator examineHeaderElements(String str);

    Iterator extractHeaderElements(String str);

    Iterator examineAllHeaderElements();

    Iterator extractAllHeaderElements();
}
