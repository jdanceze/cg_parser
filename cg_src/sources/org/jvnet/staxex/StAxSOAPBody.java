package org.jvnet.staxex;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/StAxSOAPBody.class */
public interface StAxSOAPBody {

    /* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/StAxSOAPBody$Payload.class */
    public interface Payload {
        QName getPayloadQName();

        XMLStreamReader readPayload() throws XMLStreamException;

        void writePayloadTo(XMLStreamWriter xMLStreamWriter) throws XMLStreamException;

        String getPayloadAttributeValue(String str) throws XMLStreamException;

        String getPayloadAttributeValue(QName qName) throws XMLStreamException;

        void materialize() throws XMLStreamException;
    }

    void setPayload(Payload payload) throws XMLStreamException;

    Payload getPayload() throws XMLStreamException;

    boolean hasStaxPayload();
}
