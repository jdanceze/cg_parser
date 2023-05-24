package com.sun.xml.fastinfoset.stax.events;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.ProcessingInstruction;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import javax.xml.stream.util.XMLEventConsumer;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/stax/events/StAXEventAllocatorBase.class */
public class StAXEventAllocatorBase implements XMLEventAllocator {
    XMLEventFactory factory;

    public StAXEventAllocatorBase() {
        if (System.getProperty("javax.xml.stream.XMLEventFactory") == null) {
            System.setProperty("javax.xml.stream.XMLEventFactory", "com.sun.xml.fastinfoset.stax.factory.StAXEventFactory");
        }
        this.factory = XMLEventFactory.newInstance();
    }

    public XMLEventAllocator newInstance() {
        return new StAXEventAllocatorBase();
    }

    public XMLEvent allocate(XMLStreamReader streamReader) throws XMLStreamException {
        if (streamReader == null) {
            throw new XMLStreamException(CommonResourceBundle.getInstance().getString("message.nullReader"));
        }
        return getXMLEvent(streamReader);
    }

    public void allocate(XMLStreamReader streamReader, XMLEventConsumer consumer) throws XMLStreamException {
        consumer.add(getXMLEvent(streamReader));
    }

    XMLEvent getXMLEvent(XMLStreamReader reader) {
        ProcessingInstruction processingInstruction = null;
        int eventType = reader.getEventType();
        this.factory.setLocation(reader.getLocation());
        switch (eventType) {
            case 1:
                ProcessingInstruction processingInstruction2 = (StartElementEvent) this.factory.createStartElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
                addAttributes(processingInstruction2, reader);
                addNamespaces((StartElementEvent) processingInstruction2, reader);
                processingInstruction = processingInstruction2;
                break;
            case 2:
                ProcessingInstruction processingInstruction3 = (EndElementEvent) this.factory.createEndElement(reader.getPrefix(), reader.getNamespaceURI(), reader.getLocalName());
                addNamespaces((EndElementEvent) processingInstruction3, reader);
                processingInstruction = processingInstruction3;
                break;
            case 3:
                processingInstruction = this.factory.createProcessingInstruction(reader.getPITarget(), reader.getPIData());
                break;
            case 4:
                if (reader.isWhiteSpace()) {
                    processingInstruction = this.factory.createSpace(reader.getText());
                    break;
                } else {
                    processingInstruction = this.factory.createCharacters(reader.getText());
                    break;
                }
            case 5:
                processingInstruction = this.factory.createComment(reader.getText());
                break;
            case 6:
                processingInstruction = this.factory.createSpace(reader.getText());
                break;
            case 7:
                ProcessingInstruction processingInstruction4 = (StartDocumentEvent) this.factory.createStartDocument(reader.getVersion(), reader.getEncoding(), reader.isStandalone());
                if (reader.getCharacterEncodingScheme() != null) {
                    processingInstruction4.setDeclaredEncoding(true);
                } else {
                    processingInstruction4.setDeclaredEncoding(false);
                }
                processingInstruction = processingInstruction4;
                break;
            case 8:
                processingInstruction = new EndDocumentEvent();
                break;
            case 9:
                processingInstruction = this.factory.createEntityReference(reader.getLocalName(), new EntityDeclarationImpl(reader.getLocalName(), reader.getText()));
                break;
            case 10:
                processingInstruction = null;
                break;
            case 11:
                processingInstruction = this.factory.createDTD(reader.getText());
                break;
            case 12:
                processingInstruction = this.factory.createCData(reader.getText());
                break;
        }
        return processingInstruction;
    }

    protected void addAttributes(StartElementEvent event, XMLStreamReader streamReader) {
        for (int i = 0; i < streamReader.getAttributeCount(); i++) {
            AttributeBase attr = (AttributeBase) this.factory.createAttribute(streamReader.getAttributeName(i), streamReader.getAttributeValue(i));
            attr.setAttributeType(streamReader.getAttributeType(i));
            attr.setSpecified(streamReader.isAttributeSpecified(i));
            event.addAttribute(attr);
        }
    }

    protected void addNamespaces(StartElementEvent event, XMLStreamReader streamReader) {
        for (int i = 0; i < streamReader.getNamespaceCount(); i++) {
            Namespace namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i), streamReader.getNamespaceURI(i));
            event.addNamespace(namespace);
        }
    }

    protected void addNamespaces(EndElementEvent event, XMLStreamReader streamReader) {
        for (int i = 0; i < streamReader.getNamespaceCount(); i++) {
            Namespace namespace = this.factory.createNamespace(streamReader.getNamespacePrefix(i), streamReader.getNamespaceURI(i));
            event.addNamespace(namespace);
        }
    }
}
