package com.sun.xml.bind.marshaller;

import com.sun.istack.FinalArrayList;
import com.sun.xml.bind.util.Which;
import com.sun.xml.bind.v2.util.XmlFactory;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/marshaller/SAX2DOMEx.class */
public class SAX2DOMEx implements ContentHandler {
    private Node node;
    private boolean isConsolidate;
    protected final Stack<Node> nodeStack;
    private final FinalArrayList<String> unprocessedNamespaces;
    protected final Document document;

    public SAX2DOMEx(Node node) {
        this(node, false);
    }

    public SAX2DOMEx(Node node, boolean isConsolidate) {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        this.node = node;
        this.isConsolidate = isConsolidate;
        this.nodeStack.push(this.node);
        if (node instanceof Document) {
            this.document = (Document) node;
        } else {
            this.document = node.getOwnerDocument();
        }
    }

    public SAX2DOMEx(DocumentBuilderFactory f) throws ParserConfigurationException {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        f.setValidating(false);
        this.document = f.newDocumentBuilder().newDocument();
        this.node = this.document;
        this.nodeStack.push(this.document);
    }

    public SAX2DOMEx() throws ParserConfigurationException {
        this.node = null;
        this.nodeStack = new Stack<>();
        this.unprocessedNamespaces = new FinalArrayList<>();
        DocumentBuilderFactory factory = XmlFactory.createDocumentBuilderFactory(false);
        factory.setValidating(false);
        this.document = factory.newDocumentBuilder().newDocument();
        this.node = this.document;
        this.nodeStack.push(this.document);
    }

    public final Element getCurrentElement() {
        return (Element) this.nodeStack.peek();
    }

    public Node getDOM() {
        return this.node;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() {
    }

    protected void namespace(Element element, String prefix, String uri) {
        String qname;
        if ("".equals(prefix) || prefix == null) {
            qname = EncodingConstants.XMLNS_NAMESPACE_PREFIX;
        } else {
            qname = "xmlns:" + prefix;
        }
        if (element.hasAttributeNS(EncodingConstants.XMLNS_NAMESPACE_NAME, qname)) {
            element.removeAttributeNS(EncodingConstants.XMLNS_NAMESPACE_NAME, qname);
        }
        element.setAttributeNS(EncodingConstants.XMLNS_NAMESPACE_NAME, qname, uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespace, String localName, String qName, Attributes attrs) {
        Node parent = this.nodeStack.peek();
        Element element = this.document.createElementNS(namespace, qName);
        if (element == null) {
            throw new AssertionError(Messages.format(Messages.DOM_IMPL_DOESNT_SUPPORT_CREATELEMENTNS, this.document.getClass().getName(), Which.which(this.document.getClass())));
        }
        for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
            String prefix = this.unprocessedNamespaces.get(i);
            String uri = this.unprocessedNamespaces.get(i + 1);
            namespace(element, prefix, uri);
        }
        this.unprocessedNamespaces.clear();
        if (attrs != null) {
            int length = attrs.getLength();
            for (int i2 = 0; i2 < length; i2++) {
                String namespaceuri = attrs.getURI(i2);
                String value = attrs.getValue(i2);
                String qname = attrs.getQName(i2);
                element.setAttributeNS(namespaceuri, qname, value);
            }
        }
        parent.appendChild(element);
        this.nodeStack.push(element);
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespace, String localName, String qName) {
        this.nodeStack.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        characters(new String(ch, start, length));
    }

    protected Text characters(String s) {
        Text text;
        Node parent = this.nodeStack.peek();
        Node lastChild = parent.getLastChild();
        if (this.isConsolidate && lastChild != null && lastChild.getNodeType() == 3) {
            text = (Text) lastChild;
            text.appendData(s);
        } else {
            text = this.document.createTextNode(s);
            parent.appendChild(text);
        }
        return text;
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        Node parent = this.nodeStack.peek();
        Node n = this.document.createProcessingInstruction(target, data);
        parent.appendChild(n);
    }

    @Override // org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.ContentHandler
    public void skippedEntity(String name) {
    }

    @Override // org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this.unprocessedNamespaces.add(prefix);
        this.unprocessedNamespaces.add(uri);
    }

    @Override // org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) {
    }
}
