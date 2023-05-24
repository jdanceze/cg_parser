package org.jvnet.staxex;

import java.io.OutputStream;
import javax.activation.DataHandler;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/XMLStreamWriterEx.class */
public interface XMLStreamWriterEx extends XMLStreamWriter {
    void writeBinary(byte[] bArr, int i, int i2, String str) throws XMLStreamException;

    void writeBinary(DataHandler dataHandler) throws XMLStreamException;

    OutputStream writeBinary(String str) throws XMLStreamException;

    void writePCDATA(CharSequence charSequence) throws XMLStreamException;

    NamespaceContextEx getNamespaceContext();
}
