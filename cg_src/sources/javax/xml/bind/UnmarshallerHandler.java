package javax.xml.bind;

import org.xml.sax.ContentHandler;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/UnmarshallerHandler.class */
public interface UnmarshallerHandler extends ContentHandler {
    Object getResult() throws JAXBException, IllegalStateException;
}
