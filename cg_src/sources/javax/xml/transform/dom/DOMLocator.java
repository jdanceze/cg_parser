package javax.xml.transform.dom;

import javax.xml.transform.SourceLocator;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/dom/DOMLocator.class */
public interface DOMLocator extends SourceLocator {
    Node getOriginatingNode();
}
