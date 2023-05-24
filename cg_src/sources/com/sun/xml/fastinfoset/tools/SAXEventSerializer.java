package com.sun.xml.fastinfoset.tools;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/SAXEventSerializer.class */
public class SAXEventSerializer extends DefaultHandler implements LexicalHandler {
    private Writer _writer;
    private StringBuffer _characters;
    protected List _namespaceAttributes;
    private Stack _namespaceStack = new Stack();
    private boolean _charactersAreCDATA = false;

    public SAXEventSerializer(OutputStream s) throws IOException {
        this._writer = new OutputStreamWriter(s);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startDocument() throws SAXException {
        try {
            this._writer.write("<sax xmlns=\"http://www.sun.com/xml/sax-events\">\n");
            this._writer.write("<startDocument/>\n");
            this._writer.flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endDocument() throws SAXException {
        try {
            this._writer.write("<endDocument/>\n");
            this._writer.write("</sax>");
            this._writer.flush();
            this._writer.close();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        if (this._namespaceAttributes == null) {
            this._namespaceAttributes = new ArrayList();
        }
        String qName = prefix.length() == 0 ? EncodingConstants.XMLNS_NAMESPACE_PREFIX : EncodingConstants.XMLNS_NAMESPACE_PREFIX + prefix;
        AttributeValueHolder attribute = new AttributeValueHolder(qName, prefix, uri, null, null);
        this._namespaceAttributes.add(attribute);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endPrefixMapping(String prefix) throws SAXException {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            outputCharacters();
            if (this._namespaceAttributes != null) {
                AttributeValueHolder[] attrsHolder = (AttributeValueHolder[]) this._namespaceAttributes.toArray(new AttributeValueHolder[0]);
                quicksort(attrsHolder, 0, attrsHolder.length - 1);
                for (int i = 0; i < attrsHolder.length; i++) {
                    this._writer.write("<startPrefixMapping prefix=\"" + attrsHolder[i].localName + "\" uri=\"" + attrsHolder[i].uri + "\"/>\n");
                    this._writer.flush();
                }
                this._namespaceStack.push(attrsHolder);
                this._namespaceAttributes = null;
            } else {
                this._namespaceStack.push(null);
            }
            AttributeValueHolder[] attrsHolder2 = new AttributeValueHolder[attributes.getLength()];
            for (int i2 = 0; i2 < attributes.getLength(); i2++) {
                attrsHolder2[i2] = new AttributeValueHolder(attributes.getQName(i2), attributes.getLocalName(i2), attributes.getURI(i2), attributes.getType(i2), attributes.getValue(i2));
            }
            quicksort(attrsHolder2, 0, attrsHolder2.length - 1);
            int attributeCount = 0;
            for (AttributeValueHolder attributeValueHolder : attrsHolder2) {
                if (!attributeValueHolder.uri.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                    attributeCount++;
                }
            }
            if (attributeCount == 0) {
                this._writer.write("<startElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\"/>\n");
                return;
            }
            this._writer.write("<startElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\">\n");
            for (int i3 = 0; i3 < attrsHolder2.length; i3++) {
                if (!attrsHolder2[i3].uri.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                    this._writer.write("  <attribute qName=\"" + attrsHolder2[i3].qName + "\" localName=\"" + attrsHolder2[i3].localName + "\" uri=\"" + attrsHolder2[i3].uri + "\" value=\"" + attrsHolder2[i3].value + "\"/>\n");
                }
            }
            this._writer.write("</startElement>\n");
            this._writer.flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<endElement uri=\"" + uri + "\" localName=\"" + localName + "\" qName=\"" + qName + "\"/>\n");
            this._writer.flush();
            AttributeValueHolder[] attrsHolder = (AttributeValueHolder[]) this._namespaceStack.pop();
            if (attrsHolder != null) {
                for (int i = 0; i < attrsHolder.length; i++) {
                    this._writer.write("<endPrefixMapping prefix=\"" + attrsHolder[i].localName + "\"/>\n");
                    this._writer.flush();
                }
            }
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (length == 0) {
            return;
        }
        if (this._characters == null) {
            this._characters = new StringBuffer();
        }
        this._characters.append(ch, start, length);
    }

    private void outputCharacters() throws SAXException {
        if (this._characters == null) {
            return;
        }
        try {
            this._writer.write("<characters>" + (this._charactersAreCDATA ? "<![CDATA[" : "") + ((Object) this._characters) + (this._charactersAreCDATA ? "]]>" : "") + "</characters>\n");
            this._writer.flush();
            this._characters = null;
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void processingInstruction(String target, String data) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<processingInstruction target=\"" + target + "\" data=\"" + data + "\"/>\n");
            this._writer.flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
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
        this._charactersAreCDATA = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void endCDATA() throws SAXException {
        this._charactersAreCDATA = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public void comment(char[] ch, int start, int length) throws SAXException {
        try {
            outputCharacters();
            this._writer.write("<comment>" + new String(ch, start, length) + "</comment>\n");
            this._writer.flush();
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    private void quicksort(AttributeValueHolder[] attrs, int p, int r) {
        while (p < r) {
            int q = partition(attrs, p, r);
            quicksort(attrs, p, q);
            p = q + 1;
        }
    }

    private int partition(AttributeValueHolder[] attrs, int p, int r) {
        AttributeValueHolder x = attrs[(p + r) >>> 1];
        int i = p - 1;
        int j = r + 1;
        while (true) {
            j--;
            if (x.compareTo(attrs[j]) >= 0) {
                do {
                    i++;
                } while (x.compareTo(attrs[i]) > 0);
                if (i < j) {
                    AttributeValueHolder t = attrs[i];
                    attrs[i] = attrs[j];
                    attrs[j] = t;
                } else {
                    return j;
                }
            }
        }
    }

    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/tools/SAXEventSerializer$AttributeValueHolder.class */
    public static class AttributeValueHolder implements Comparable {
        public final String qName;
        public final String localName;
        public final String uri;
        public final String type;
        public final String value;

        public AttributeValueHolder(String qName, String localName, String uri, String type, String value) {
            this.qName = qName;
            this.localName = localName;
            this.uri = uri;
            this.type = type;
            this.value = value;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object o) {
            try {
                return this.qName.compareTo(((AttributeValueHolder) o).qName);
            } catch (Exception e) {
                throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.AttributeValueHolderExpected"));
            }
        }

        public boolean equals(Object o) {
            try {
                if (o instanceof AttributeValueHolder) {
                    if (this.qName.equals(((AttributeValueHolder) o).qName)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception e) {
                throw new RuntimeException(CommonResourceBundle.getInstance().getString("message.AttributeValueHolderExpected"));
            }
        }

        public int hashCode() {
            int hash = (97 * 7) + (this.qName != null ? this.qName.hashCode() : 0);
            return hash;
        }
    }
}
