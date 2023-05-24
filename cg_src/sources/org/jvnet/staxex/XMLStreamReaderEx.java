package org.jvnet.staxex;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/XMLStreamReaderEx.class */
public interface XMLStreamReaderEx extends XMLStreamReader {
    CharSequence getPCDATA() throws XMLStreamException;

    NamespaceContextEx getNamespaceContext();

    String getElementTextTrim() throws XMLStreamException;
}
