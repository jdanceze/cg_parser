package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.AttributesImpl;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/StAX2SAXReader.class */
public class StAX2SAXReader {
    ContentHandler _handler;
    LexicalHandler _lexicalHandler;
    XMLStreamReader _reader;

    public StAX2SAXReader(XMLStreamReader reader, ContentHandler handler) {
        this._handler = handler;
        this._reader = reader;
    }

    public StAX2SAXReader(XMLStreamReader reader) {
        this._reader = reader;
    }

    public void setContentHandler(ContentHandler handler) {
        this._handler = handler;
    }

    public void setLexicalHandler(LexicalHandler lexicalHandler) {
        this._lexicalHandler = lexicalHandler;
    }

    public void adapt() throws XMLStreamException, SAXException {
        String qName;
        AttributesImpl attrs = new AttributesImpl();
        this._handler.startDocument();
        while (this._reader.hasNext()) {
            try {
                int event = this._reader.next();
                switch (event) {
                    case 1:
                        int nsc = this._reader.getNamespaceCount();
                        for (int i = 0; i < nsc; i++) {
                            this._handler.startPrefixMapping(this._reader.getNamespacePrefix(i), this._reader.getNamespaceURI(i));
                        }
                        attrs.clear();
                        int nat = this._reader.getAttributeCount();
                        for (int i2 = 0; i2 < nat; i2++) {
                            QName q = this._reader.getAttributeName(i2);
                            String qName2 = this._reader.getAttributePrefix(i2);
                            if (qName2 == null || qName2 == "") {
                                qName = q.getLocalPart();
                            } else {
                                qName = qName2 + ":" + q.getLocalPart();
                            }
                            attrs.addAttribute(this._reader.getAttributeNamespace(i2), q.getLocalPart(), qName, this._reader.getAttributeType(i2), this._reader.getAttributeValue(i2));
                        }
                        QName qname = this._reader.getName();
                        String prefix = qname.getPrefix();
                        String localPart = qname.getLocalPart();
                        this._handler.startElement(this._reader.getNamespaceURI(), localPart, prefix.length() > 0 ? prefix + ":" + localPart : localPart, attrs);
                        break;
                    case 2:
                        QName qname2 = this._reader.getName();
                        String prefix2 = qname2.getPrefix();
                        String localPart2 = qname2.getLocalPart();
                        this._handler.endElement(this._reader.getNamespaceURI(), localPart2, prefix2.length() > 0 ? prefix2 + ":" + localPart2 : localPart2);
                        int nsc2 = this._reader.getNamespaceCount();
                        for (int i3 = 0; i3 < nsc2; i3++) {
                            this._handler.endPrefixMapping(this._reader.getNamespacePrefix(i3));
                        }
                        break;
                    case 3:
                        this._handler.processingInstruction(this._reader.getPITarget(), this._reader.getPIData());
                        break;
                    case 4:
                        this._handler.characters(this._reader.getTextCharacters(), this._reader.getTextStart(), this._reader.getTextLength());
                        break;
                    case 5:
                        this._lexicalHandler.comment(this._reader.getTextCharacters(), this._reader.getTextStart(), this._reader.getTextLength());
                        break;
                    case 6:
                    case 7:
                    default:
                        throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.StAX2SAXReader", new Object[]{Integer.valueOf(event)}));
                    case 8:
                        break;
                }
            } catch (XMLStreamException e) {
                this._handler.endDocument();
                throw e;
            }
        }
        this._handler.endDocument();
    }
}
