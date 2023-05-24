package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/XmlOutputAbstractImpl.class */
public abstract class XmlOutputAbstractImpl implements XmlOutput {
    protected int[] nsUriIndex2prefixIndex;
    protected NamespaceContextImpl nsContext;
    protected XMLSerializer serializer;

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public abstract void beginStartTag(int i, String str) throws IOException, XMLStreamException;

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public abstract void attribute(int i, String str, String str2) throws IOException, XMLStreamException;

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public abstract void endStartTag() throws IOException, SAXException;

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public abstract void endTag(int i, String str) throws IOException, SAXException, XMLStreamException;

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void startDocument(XMLSerializer serializer, boolean fragment, int[] nsUriIndex2prefixIndex, NamespaceContextImpl nsContext) throws IOException, SAXException, XMLStreamException {
        this.nsUriIndex2prefixIndex = nsUriIndex2prefixIndex;
        this.nsContext = nsContext;
        this.serializer = serializer;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endDocument(boolean fragment) throws IOException, SAXException, XMLStreamException {
        this.serializer = null;
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void beginStartTag(Name name) throws IOException, XMLStreamException {
        beginStartTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void attribute(Name name, String value) throws IOException, XMLStreamException {
        short idx = name.nsUriIndex;
        if (idx == -1) {
            attribute(-1, name.localName, value);
        } else {
            attribute(this.nsUriIndex2prefixIndex[idx], name.localName, value);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.output.XmlOutput
    public void endTag(Name name) throws IOException, SAXException, XMLStreamException {
        endTag(this.nsUriIndex2prefixIndex[name.nsUriIndex], name.localName);
    }
}
