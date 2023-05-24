package com.sun.xml.txw2.output;

import com.sun.xml.txw2.TxwException;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/StaxSerializer.class */
public class StaxSerializer implements XmlSerializer {
    private final XMLStreamWriter out;

    public StaxSerializer(XMLStreamWriter writer) {
        this(writer, true);
    }

    public StaxSerializer(XMLStreamWriter writer, boolean indenting) {
        this.out = indenting ? new IndentingXMLStreamWriter(writer) : writer;
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void startDocument() {
        try {
            this.out.writeStartDocument();
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void beginStartTag(String uri, String localName, String prefix) {
        try {
            this.out.writeStartElement(prefix, localName, uri);
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void writeAttribute(String uri, String localName, String prefix, StringBuilder value) {
        try {
            this.out.writeAttribute(prefix, uri, localName, value.toString());
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void writeXmlns(String prefix, String uri) {
        try {
            if (prefix.length() == 0) {
                this.out.setDefaultNamespace(uri);
            } else {
                this.out.setPrefix(prefix, uri);
            }
            this.out.writeNamespace(prefix, uri);
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void endStartTag(String uri, String localName, String prefix) {
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void endTag() {
        try {
            this.out.writeEndElement();
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void text(StringBuilder text) {
        try {
            this.out.writeCharacters(text.toString());
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void cdata(StringBuilder text) {
        try {
            this.out.writeCData(text.toString());
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void comment(StringBuilder comment) {
        try {
            this.out.writeComment(comment.toString());
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void endDocument() {
        try {
            this.out.writeEndDocument();
            this.out.flush();
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }

    @Override // com.sun.xml.txw2.output.XmlSerializer
    public void flush() {
        try {
            this.out.flush();
        } catch (XMLStreamException e) {
            throw new TxwException((Throwable) e);
        }
    }
}
