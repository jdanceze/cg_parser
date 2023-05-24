package javax.xml.transform.dom;

import javax.xml.transform.Result;
import org.w3c.dom.Node;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:javax/xml/transform/dom/DOMResult.class */
public class DOMResult implements Result {
    public static final String FEATURE = "http://javax.xml.transform.dom.DOMResult/feature";
    private Node node;
    private String systemId;

    public DOMResult() {
    }

    public DOMResult(Node node) {
        setNode(node);
    }

    public DOMResult(Node node, String str) {
        setNode(node);
        setSystemId(str);
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public Node getNode() {
        return this.node;
    }

    @Override // javax.xml.transform.Result
    public void setSystemId(String str) {
        this.systemId = str;
    }

    @Override // javax.xml.transform.Result
    public String getSystemId() {
        return this.systemId;
    }
}
