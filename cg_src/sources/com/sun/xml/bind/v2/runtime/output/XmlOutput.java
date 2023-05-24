package com.sun.xml.bind.v2.runtime.output;

import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import java.io.IOException;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/XmlOutput.class */
public interface XmlOutput {
    void startDocument(XMLSerializer xMLSerializer, boolean z, int[] iArr, NamespaceContextImpl namespaceContextImpl) throws IOException, SAXException, XMLStreamException;

    void endDocument(boolean z) throws IOException, SAXException, XMLStreamException;

    void beginStartTag(Name name) throws IOException, XMLStreamException;

    void beginStartTag(int i, String str) throws IOException, XMLStreamException;

    void attribute(Name name, String str) throws IOException, XMLStreamException;

    void attribute(int i, String str, String str2) throws IOException, XMLStreamException;

    void endStartTag() throws IOException, SAXException;

    void endTag(Name name) throws IOException, SAXException, XMLStreamException;

    void endTag(int i, String str) throws IOException, SAXException, XMLStreamException;

    void text(String str, boolean z) throws IOException, SAXException, XMLStreamException;

    void text(Pcdata pcdata, boolean z) throws IOException, SAXException, XMLStreamException;
}
