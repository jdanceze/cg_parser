package com.sun.xml.bind.unmarshaller;

import com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.Enumeration;
import javax.xml.bind.ValidationEventLocator;
import javax.xml.bind.helpers.ValidationEventLocatorImpl;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.NamespaceSupport;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/unmarshaller/DOMScanner.class */
public class DOMScanner implements LocatorEx, InfosetScanner {
    private Node currentNode = null;
    private final AttributesImpl atts = new AttributesImpl();
    private ContentHandler receiver = null;
    private Locator locator = this;

    public void setLocator(Locator loc) {
        this.locator = loc;
    }

    @Override // com.sun.xml.bind.unmarshaller.InfosetScanner
    public void scan(Object node) throws SAXException {
        if (node instanceof Document) {
            scan((Document) node);
        } else {
            scan((Element) node);
        }
    }

    public void scan(Document doc) throws SAXException {
        scan(doc.getDocumentElement());
    }

    public void scan(Element e) throws SAXException {
        setCurrentLocation(e);
        this.receiver.setDocumentLocator(this.locator);
        this.receiver.startDocument();
        NamespaceSupport nss = new NamespaceSupport();
        buildNamespaceSupport(nss, e.getParentNode());
        Enumeration en = nss.getPrefixes();
        while (en.hasMoreElements()) {
            String prefix = (String) en.nextElement();
            this.receiver.startPrefixMapping(prefix, nss.getURI(prefix));
        }
        visit(e);
        Enumeration en2 = nss.getPrefixes();
        while (en2.hasMoreElements()) {
            this.receiver.endPrefixMapping((String) en2.nextElement());
        }
        setCurrentLocation(e);
        this.receiver.endDocument();
    }

    public void parse(Element e, ContentHandler handler) throws SAXException {
        this.receiver = handler;
        setCurrentLocation(e);
        this.receiver.startDocument();
        this.receiver.setDocumentLocator(this.locator);
        visit(e);
        setCurrentLocation(e);
        this.receiver.endDocument();
    }

    public void parseWithContext(Element e, ContentHandler handler) throws SAXException {
        setContentHandler(handler);
        scan(e);
    }

    private void buildNamespaceSupport(NamespaceSupport nss, Node node) {
        if (node == null || node.getNodeType() != 1) {
            return;
        }
        buildNamespaceSupport(nss, node.getParentNode());
        nss.pushContext();
        NamedNodeMap atts = node.getAttributes();
        for (int i = 0; i < atts.getLength(); i++) {
            Attr a = (Attr) atts.item(i);
            if (EncodingConstants.XMLNS_NAMESPACE_PREFIX.equals(a.getPrefix())) {
                nss.declarePrefix(a.getLocalName(), a.getValue());
            } else if (EncodingConstants.XMLNS_NAMESPACE_PREFIX.equals(a.getName())) {
                nss.declarePrefix("", a.getValue());
            }
        }
    }

    public void visit(Element e) throws SAXException {
        setCurrentLocation(e);
        NamedNodeMap attributes = e.getAttributes();
        this.atts.clear();
        int len = attributes == null ? 0 : attributes.getLength();
        for (int i = len - 1; i >= 0; i--) {
            Attr a = (Attr) attributes.item(i);
            String name = a.getName();
            if (name.startsWith(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                if (name.length() == 5) {
                    this.receiver.startPrefixMapping("", a.getValue());
                } else {
                    String localName = a.getLocalName();
                    if (localName == null) {
                        localName = name.substring(6);
                    }
                    this.receiver.startPrefixMapping(localName, a.getValue());
                }
            } else {
                String uri = a.getNamespaceURI();
                if (uri == null) {
                    uri = "";
                }
                String local = a.getLocalName();
                if (local == null) {
                    local = a.getName();
                }
                this.atts.addAttribute(uri, local, a.getName(), "CDATA", a.getValue());
            }
        }
        String uri2 = e.getNamespaceURI();
        if (uri2 == null) {
            uri2 = "";
        }
        String local2 = e.getLocalName();
        String qname = e.getTagName();
        if (local2 == null) {
            local2 = qname;
        }
        this.receiver.startElement(uri2, local2, qname, this.atts);
        NodeList children = e.getChildNodes();
        int clen = children.getLength();
        for (int i2 = 0; i2 < clen; i2++) {
            visit(children.item(i2));
        }
        setCurrentLocation(e);
        this.receiver.endElement(uri2, local2, qname);
        for (int i3 = len - 1; i3 >= 0; i3--) {
            Attr a2 = (Attr) attributes.item(i3);
            String name2 = a2.getName();
            if (name2.startsWith(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                if (name2.length() == 5) {
                    this.receiver.endPrefixMapping("");
                } else {
                    this.receiver.endPrefixMapping(a2.getLocalName());
                }
            }
        }
    }

    private void visit(Node n) throws SAXException {
        setCurrentLocation(n);
        switch (n.getNodeType()) {
            case 1:
                visit((Element) n);
                return;
            case 2:
            case 6:
            default:
                return;
            case 3:
            case 4:
                String value = n.getNodeValue();
                this.receiver.characters(value.toCharArray(), 0, value.length());
                return;
            case 5:
                this.receiver.skippedEntity(n.getNodeName());
                return;
            case 7:
                ProcessingInstruction pi = (ProcessingInstruction) n;
                this.receiver.processingInstruction(pi.getTarget(), pi.getData());
                return;
        }
    }

    private void setCurrentLocation(Node currNode) {
        this.currentNode = currNode;
    }

    public Node getCurrentLocation() {
        return this.currentNode;
    }

    @Override // com.sun.xml.bind.unmarshaller.InfosetScanner
    public Object getCurrentElement() {
        return this.currentNode;
    }

    @Override // com.sun.xml.bind.unmarshaller.InfosetScanner
    public LocatorEx getLocator() {
        return this;
    }

    @Override // com.sun.xml.bind.unmarshaller.InfosetScanner
    public void setContentHandler(ContentHandler handler) {
        this.receiver = handler;
    }

    @Override // com.sun.xml.bind.unmarshaller.InfosetScanner
    public ContentHandler getContentHandler() {
        return this.receiver;
    }

    @Override // org.xml.sax.Locator
    public String getPublicId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public String getSystemId() {
        return null;
    }

    @Override // org.xml.sax.Locator
    public int getLineNumber() {
        return -1;
    }

    @Override // org.xml.sax.Locator
    public int getColumnNumber() {
        return -1;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.LocatorEx
    public ValidationEventLocator getLocation() {
        return new ValidationEventLocatorImpl(getCurrentLocation());
    }
}
