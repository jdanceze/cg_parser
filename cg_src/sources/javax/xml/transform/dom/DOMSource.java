package javax.xml.transform.dom;

import javax.xml.transform.Source;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/dom/DOMSource.class */
public class DOMSource implements Source {
    public static final String FEATURE = "http://javax.xml.transform.dom.DOMSource/feature";
    private Node node;
    String baseID;

    public DOMSource() {
    }

    public DOMSource(Node node) {
        setNode(node);
    }

    public DOMSource(Node node, String str) {
        setNode(node);
        setSystemId(str);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }

    @Override // javax.xml.transform.Source
    public void setSystemId(String str) {
        this.baseID = str;
    }

    @Override // javax.xml.transform.Source
    public String getSystemId() {
        return this.baseID;
    }
}
