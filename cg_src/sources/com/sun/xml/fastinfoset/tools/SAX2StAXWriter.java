package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.QualifiedName;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/SAX2StAXWriter.class */
public class SAX2StAXWriter extends DefaultHandler implements LexicalHandler {
    private static final Logger logger = Logger.getLogger(SAX2StAXWriter.class.getName());
    XMLStreamWriter _writer;
    ArrayList _namespaces = new ArrayList();

    public SAX2StAXWriter(XMLStreamWriter writer) {
        this._writer = writer;
    }

    public XMLStreamWriter getWriter() {
        return this._writer;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        try {
            this._writer.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            this._writer.writeEndDocument();
            this._writer.flush();
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        try {
            this._writer.writeCharacters(ch, start, length);
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        try {
            int k = qName.indexOf(58);
            String prefix = k > 0 ? qName.substring(0, k) : "";
            this._writer.writeStartElement(prefix, localName, namespaceURI);
            int length = this._namespaces.size();
            for (int i = 0; i < length; i++) {
                QualifiedName nsh = (QualifiedName) this._namespaces.get(i);
                this._writer.writeNamespace(nsh.prefix, nsh.namespaceName);
            }
            this._namespaces.clear();
            int length2 = atts.getLength();
            for (int i2 = 0; i2 < length2; i2++) {
                this._writer.writeAttribute(atts.getURI(i2), atts.getLocalName(i2), atts.getValue(i2));
            }
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            this._writer.writeEndElement();
        } catch (XMLStreamException e) {
            logger.log(Level.FINE, "Exception on endElement", (Throwable) e);
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        this._namespaces.add(new QualifiedName(prefix, uri));
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            this._writer.writeProcessingInstruction(target, data);
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            this._writer.writeComment(new String(ch, start, length));
        } catch (XMLStreamException e) {
            throw new SAXException((Exception) e);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startCDATA() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startDTD(String name, String publicId, String systemId) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void startEntity(String name) throws SAXException {
    }
}
