package javax.xml.bind;

import java.net.URL;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:jaxb-api-2.4.0-b180725.0427.jar:javax/xml/bind/ValidationEventLocator.class */
public interface ValidationEventLocator {
    URL getURL();

    int getOffset();

    int getLineNumber();

    int getColumnNumber();

    Object getObject();

    Node getNode();
}
