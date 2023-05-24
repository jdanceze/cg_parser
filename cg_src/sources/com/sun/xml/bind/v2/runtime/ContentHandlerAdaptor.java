package com.sun.xml.bind.v2.runtime;

import com.sun.istack.FinalArrayList;
import com.sun.istack.SAXException2;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/ContentHandlerAdaptor.class */
public final class ContentHandlerAdaptor extends DefaultHandler {
    private final XMLSerializer serializer;
    private final FinalArrayList<String> prefixMap = new FinalArrayList<>();
    private final StringBuffer text = new StringBuffer();

    /* JADX INFO: Access modifiers changed from: package-private */
    public ContentHandlerAdaptor(XMLSerializer _serializer) {
        this.serializer = _serializer;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() {
        this.prefixMap.clear();
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) {
        this.prefixMap.add(prefix);
        this.prefixMap.add(uri);
    }

    private boolean containsPrefixMapping(String prefix, String uri) {
        for (int i = 0; i < this.prefixMap.size(); i += 2) {
            if (this.prefixMap.get(i).equals(prefix) && this.prefixMap.get(i + 1).equals(uri)) {
                return true;
            }
        }
        return false;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            flushText();
            int len = atts.getLength();
            String p = getPrefix(qName);
            if (containsPrefixMapping(p, namespaceURI)) {
                this.serializer.startElementForce(namespaceURI, localName, p, null);
            } else {
                this.serializer.startElement(namespaceURI, localName, p, null);
            }
            for (int i = 0; i < this.prefixMap.size(); i += 2) {
                this.serializer.getNamespaceContext().force(this.prefixMap.get(i + 1), this.prefixMap.get(i));
            }
            for (int i2 = 0; i2 < len; i2++) {
                String qname = atts.getQName(i2);
                if (!qname.startsWith(EncodingConstants.XMLNS_NAMESPACE_PREFIX) && atts.getURI(i2).length() != 0) {
                    String prefix = getPrefix(qname);
                    this.serializer.getNamespaceContext().declareNamespace(atts.getURI(i2), prefix, true);
                }
            }
            this.serializer.endNamespaceDecls(null);
            for (int i3 = 0; i3 < len; i3++) {
                if (!atts.getQName(i3).startsWith(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                    this.serializer.attribute(atts.getURI(i3), atts.getLocalName(i3), atts.getValue(i3));
                }
            }
            this.prefixMap.clear();
            this.serializer.endAttributes();
        } catch (XMLStreamException e) {
            throw new SAXException2((Exception) e);
        } catch (IOException e2) {
            throw new SAXException2(e2);
        }
    }

    private String getPrefix(String qname) {
        int idx = qname.indexOf(58);
        String prefix = idx == -1 ? "" : qname.substring(0, idx);
        return prefix;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            flushText();
            this.serializer.endElement();
        } catch (XMLStreamException e) {
            throw new SAXException2((Exception) e);
        } catch (IOException e2) {
            throw new SAXException2(e2);
        }
    }

    private void flushText() throws SAXException, IOException, XMLStreamException {
        if (this.text.length() != 0) {
            this.serializer.text(this.text.toString(), (String) null);
            this.text.setLength(0);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) {
        this.text.append(ch, start, length);
    }
}
