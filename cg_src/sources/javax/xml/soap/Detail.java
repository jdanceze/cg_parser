package javax.xml.soap;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/Detail.class */
public interface Detail extends SOAPFaultElement {
    DetailEntry addDetailEntry(Name name) throws SOAPException;

    Iterator getDetailEntries();
}
