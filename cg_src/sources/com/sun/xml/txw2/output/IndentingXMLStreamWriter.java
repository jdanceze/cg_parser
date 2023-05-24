package com.sun.xml.txw2.output;

import java.util.Stack;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/output/IndentingXMLStreamWriter.class */
public class IndentingXMLStreamWriter extends DelegatingXMLStreamWriter {
    private static final Object SEEN_NOTHING = new Object();
    private static final Object SEEN_ELEMENT = new Object();
    private static final Object SEEN_DATA = new Object();
    private Object state;
    private Stack<Object> stateStack;
    private String indentStep;
    private int depth;

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ Object getProperty(String str) throws IllegalArgumentException {
        return super.getProperty(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ NamespaceContext getNamespaceContext() {
        return super.getNamespaceContext();
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void setNamespaceContext(NamespaceContext namespaceContext) throws XMLStreamException {
        super.setNamespaceContext(namespaceContext);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void setDefaultNamespace(String str) throws XMLStreamException {
        super.setDefaultNamespace(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void setPrefix(String str, String str2) throws XMLStreamException {
        super.setPrefix(str, str2);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ String getPrefix(String str) throws XMLStreamException {
        return super.getPrefix(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeEntityRef(String str) throws XMLStreamException {
        super.writeEntityRef(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeDTD(String str) throws XMLStreamException {
        super.writeDTD(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeProcessingInstruction(String str, String str2) throws XMLStreamException {
        super.writeProcessingInstruction(str, str2);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeProcessingInstruction(String str) throws XMLStreamException {
        super.writeProcessingInstruction(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeComment(String str) throws XMLStreamException {
        super.writeComment(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeDefaultNamespace(String str) throws XMLStreamException {
        super.writeDefaultNamespace(str);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeNamespace(String str, String str2) throws XMLStreamException {
        super.writeNamespace(str, str2);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeAttribute(String str, String str2, String str3) throws XMLStreamException {
        super.writeAttribute(str, str2, str3);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeAttribute(String str, String str2, String str3, String str4) throws XMLStreamException {
        super.writeAttribute(str, str2, str3, str4);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeAttribute(String str, String str2) throws XMLStreamException {
        super.writeAttribute(str, str2);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void flush() throws XMLStreamException {
        super.flush();
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void close() throws XMLStreamException {
        super.close();
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public /* bridge */ /* synthetic */ void writeEndDocument() throws XMLStreamException {
        super.writeEndDocument();
    }

    public IndentingXMLStreamWriter(XMLStreamWriter writer) {
        super(writer);
        this.state = SEEN_NOTHING;
        this.stateStack = new Stack<>();
        this.indentStep = "  ";
        this.depth = 0;
    }

    public int getIndentStep() {
        return this.indentStep.length();
    }

    public void setIndentStep(int indentStep) {
        StringBuilder s = new StringBuilder();
        while (indentStep > 0) {
            s.append(' ');
            indentStep--;
        }
        setIndentStep(s.toString());
    }

    public void setIndentStep(String s) {
        this.indentStep = s;
    }

    private void onStartElement() throws XMLStreamException {
        this.stateStack.push(SEEN_ELEMENT);
        this.state = SEEN_NOTHING;
        if (this.depth > 0) {
            super.writeCharacters("\n");
        }
        doIndent();
        this.depth++;
    }

    private void onEndElement() throws XMLStreamException {
        this.depth--;
        if (this.state == SEEN_ELEMENT) {
            super.writeCharacters("\n");
            doIndent();
        }
        this.state = this.stateStack.pop();
    }

    private void onEmptyElement() throws XMLStreamException {
        this.state = SEEN_ELEMENT;
        if (this.depth > 0) {
            super.writeCharacters("\n");
        }
        doIndent();
    }

    private void doIndent() throws XMLStreamException {
        if (this.depth > 0) {
            for (int i = 0; i < this.depth; i++) {
                super.writeCharacters(this.indentStep);
            }
        }
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartDocument() throws XMLStreamException {
        super.writeStartDocument();
        super.writeCharacters("\n");
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartDocument(String version) throws XMLStreamException {
        super.writeStartDocument(version);
        super.writeCharacters("\n");
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        super.writeStartDocument(encoding, version);
        super.writeCharacters("\n");
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartElement(String localName) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(localName);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartElement(String namespaceURI, String localName) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(namespaceURI, localName);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        onStartElement();
        super.writeStartElement(prefix, localName, namespaceURI);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeEmptyElement(String namespaceURI, String localName) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(namespaceURI, localName);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeEmptyElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(prefix, localName, namespaceURI);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeEmptyElement(String localName) throws XMLStreamException {
        onEmptyElement();
        super.writeEmptyElement(localName);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeEndElement() throws XMLStreamException {
        onEndElement();
        super.writeEndElement();
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeCharacters(String text) throws XMLStreamException {
        this.state = SEEN_DATA;
        super.writeCharacters(text);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeCharacters(char[] text, int start, int len) throws XMLStreamException {
        this.state = SEEN_DATA;
        super.writeCharacters(text, start, len);
    }

    @Override // com.sun.xml.txw2.output.DelegatingXMLStreamWriter
    public void writeCData(String data) throws XMLStreamException {
        this.state = SEEN_DATA;
        super.writeCData(data);
    }
}
