package com.sun.xml.txw2.output;

import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.txw2.TxwException;
import java.util.ArrayList;
import java.util.Stack;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
/* compiled from: DomSerializer.java */
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/Dom2SaxAdapter.class */
class Dom2SaxAdapter implements ContentHandler, LexicalHandler {
    private final Node _node;
    private boolean inCDATA;
    private final Document _document;
    private final Stack _nodeStk = new Stack();
    private ArrayList unprocessedNamespaces = new ArrayList();

    public final Element getCurrentElement() {
        return (Element) this._nodeStk.peek();
    }

    public Dom2SaxAdapter(Node node) {
        this._node = node;
        this._nodeStk.push(this._node);
        if (node instanceof Document) {
            this._document = (Document) node;
        } else {
            this._document = node.getOwnerDocument();
        }
    }

    public Dom2SaxAdapter() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        factory.setValidating(false);
        this._document = factory.newDocumentBuilder().newDocument();
        this._node = this._document;
        this._nodeStk.push(this._document);
    }

    public Node getDOM() {
        return this._node;
    }

    @Override // org.xml.sax.ContentHandler
    public void startDocument() {
    }

    @Override // org.xml.sax.ContentHandler
    public void endDocument() {
    }

    @Override // org.xml.sax.ContentHandler
    public void startElement(String namespace, String localName, String qName, Attributes attrs) {
        String qname;
        Element element = this._document.createElementNS(namespace, qName);
        if (element == null) {
            throw new TxwException("Your DOM provider doesn't support the createElementNS method properly");
        }
        for (int i = 0; i < this.unprocessedNamespaces.size(); i += 2) {
            String prefix = (String) this.unprocessedNamespaces.get(i + 0);
            String uri = (String) this.unprocessedNamespaces.get(i + 1);
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
        this.unprocessedNamespaces.clear();
        int length = attrs.getLength();
        for (int i2 = 0; i2 < length; i2++) {
            String namespaceuri = attrs.getURI(i2);
            String value = attrs.getValue(i2);
            String qname2 = attrs.getQName(i2);
            element.setAttributeNS(namespaceuri, qname2, value);
        }
        getParent().appendChild(element);
        this._nodeStk.push(element);
    }

    private final Node getParent() {
        return (Node) this._nodeStk.peek();
    }

    @Override // org.xml.sax.ContentHandler
    public void endElement(String namespace, String localName, String qName) {
        this._nodeStk.pop();
    }

    @Override // org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        Node text;
        if (this.inCDATA) {
            text = this._document.createCDATASection(new String(ch, start, length));
        } else {
            text = this._document.createTextNode(new String(ch, start, length));
        }
        getParent().appendChild(text);
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        getParent().appendChild(this._document.createComment(new String(ch, start, length)));
    }

    @Override // org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) {
    }

    @Override // org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        Node node = this._document.createProcessingInstruction(target, data);
        getParent().appendChild(node);
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

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
        this.inCDATA = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this.inCDATA = false;
    }
}
