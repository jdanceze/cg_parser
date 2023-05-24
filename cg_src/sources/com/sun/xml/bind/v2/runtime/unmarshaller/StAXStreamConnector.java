package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.WhiteSpaceProcessor;
import java.lang.reflect.Constructor;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/StAXStreamConnector.class */
public class StAXStreamConnector extends StAXConnector {
    private final XMLStreamReader staxStreamReader;
    protected final StringBuilder buffer;
    protected boolean textReported;
    private final Attributes attributes;
    private static final Class FI_STAX_READER_CLASS = initFIStAXReaderClass();
    private static final Constructor<? extends StAXConnector> FI_CONNECTOR_CTOR = initFastInfosetConnectorClass();
    private static final Class STAX_EX_READER_CLASS = initStAXExReader();
    private static final Constructor<? extends StAXConnector> STAX_EX_CONNECTOR_CTOR = initStAXExConnector();

    public static StAXConnector create(XMLStreamReader reader, XmlVisitor visitor) {
        Class readerClass = reader.getClass();
        if (FI_STAX_READER_CLASS != null && FI_STAX_READER_CLASS.isAssignableFrom(readerClass) && FI_CONNECTOR_CTOR != null) {
            try {
                return FI_CONNECTOR_CTOR.newInstance(reader, visitor);
            } catch (Exception e) {
            }
        }
        boolean isZephyr = readerClass.getName().equals("com.sun.xml.stream.XMLReaderImpl");
        if ((!getBoolProp(reader, "org.codehaus.stax2.internNames") || !getBoolProp(reader, "org.codehaus.stax2.internNsUris")) && !isZephyr && !checkImplementaionNameOfSjsxp(reader)) {
            visitor = new InterningXmlVisitor(visitor);
        }
        if (STAX_EX_READER_CLASS != null && STAX_EX_READER_CLASS.isAssignableFrom(readerClass)) {
            try {
                return STAX_EX_CONNECTOR_CTOR.newInstance(reader, visitor);
            } catch (Exception e2) {
            }
        }
        return new StAXStreamConnector(reader, visitor);
    }

    private static boolean checkImplementaionNameOfSjsxp(XMLStreamReader reader) {
        try {
            Object name = reader.getProperty("http://java.sun.com/xml/stream/properties/implementation-name");
            if (name != null) {
                if (name.equals("sjsxp")) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    private static boolean getBoolProp(XMLStreamReader r, String n) {
        try {
            Object o = r.getProperty(n);
            if (o instanceof Boolean) {
                return ((Boolean) o).booleanValue();
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public StAXStreamConnector(XMLStreamReader staxStreamReader, XmlVisitor visitor) {
        super(visitor);
        this.buffer = new StringBuilder();
        this.textReported = false;
        this.attributes = new Attributes() { // from class: com.sun.xml.bind.v2.runtime.unmarshaller.StAXStreamConnector.1
            @Override // org.xml.sax.Attributes
            public int getLength() {
                return StAXStreamConnector.this.staxStreamReader.getAttributeCount();
            }

            @Override // org.xml.sax.Attributes
            public String getURI(int index) {
                String uri = StAXStreamConnector.this.staxStreamReader.getAttributeNamespace(index);
                return uri == null ? "" : uri;
            }

            @Override // org.xml.sax.Attributes
            public String getLocalName(int index) {
                return StAXStreamConnector.this.staxStreamReader.getAttributeLocalName(index);
            }

            @Override // org.xml.sax.Attributes
            public String getQName(int index) {
                String prefix = StAXStreamConnector.this.staxStreamReader.getAttributePrefix(index);
                if (prefix == null || prefix.length() == 0) {
                    return getLocalName(index);
                }
                return prefix + ':' + getLocalName(index);
            }

            @Override // org.xml.sax.Attributes
            public String getType(int index) {
                return StAXStreamConnector.this.staxStreamReader.getAttributeType(index);
            }

            @Override // org.xml.sax.Attributes
            public String getValue(int index) {
                return StAXStreamConnector.this.staxStreamReader.getAttributeValue(index);
            }

            @Override // org.xml.sax.Attributes
            public int getIndex(String uri, String localName) {
                for (int i = getLength() - 1; i >= 0; i--) {
                    if (localName.equals(getLocalName(i)) && uri.equals(getURI(i))) {
                        return i;
                    }
                }
                return -1;
            }

            @Override // org.xml.sax.Attributes
            public int getIndex(String qName) {
                for (int i = getLength() - 1; i >= 0; i--) {
                    if (qName.equals(getQName(i))) {
                        return i;
                    }
                }
                return -1;
            }

            @Override // org.xml.sax.Attributes
            public String getType(String uri, String localName) {
                int index = getIndex(uri, localName);
                if (index < 0) {
                    return null;
                }
                return getType(index);
            }

            @Override // org.xml.sax.Attributes
            public String getType(String qName) {
                int index = getIndex(qName);
                if (index < 0) {
                    return null;
                }
                return getType(index);
            }

            @Override // org.xml.sax.Attributes
            public String getValue(String uri, String localName) {
                int index = getIndex(uri, localName);
                if (index < 0) {
                    return null;
                }
                return getValue(index);
            }

            @Override // org.xml.sax.Attributes
            public String getValue(String qName) {
                int index = getIndex(qName);
                if (index < 0) {
                    return null;
                }
                return getValue(index);
            }
        };
        this.staxStreamReader = staxStreamReader;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    public void bridge() throws XMLStreamException {
        try {
            int depth = 0;
            int event = this.staxStreamReader.getEventType();
            if (event == 7) {
                while (!this.staxStreamReader.isStartElement()) {
                    event = this.staxStreamReader.next();
                }
            }
            if (event != 1) {
                throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
            }
            handleStartDocument(this.staxStreamReader.getNamespaceContext());
            while (true) {
                switch (event) {
                    case 1:
                        handleStartElement();
                        depth++;
                        break;
                    case 2:
                        depth--;
                        handleEndElement();
                        if (depth != 0) {
                            break;
                        } else {
                            this.staxStreamReader.next();
                            handleEndDocument();
                            return;
                        }
                    case 4:
                    case 6:
                    case 12:
                        handleCharacters();
                        break;
                }
                event = this.staxStreamReader.next();
            }
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    protected Location getCurrentLocation() {
        return this.staxStreamReader.getLocation();
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    protected String getCurrentQName() {
        return getQName(this.staxStreamReader.getPrefix(), this.staxStreamReader.getLocalName());
    }

    private void handleEndElement() throws SAXException {
        processText(false);
        this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI());
        this.tagName.local = this.staxStreamReader.getLocalName();
        this.visitor.endElement(this.tagName);
        int nsCount = this.staxStreamReader.getNamespaceCount();
        for (int i = nsCount - 1; i >= 0; i--) {
            this.visitor.endPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)));
        }
    }

    private void handleStartElement() throws SAXException {
        processText(true);
        int nsCount = this.staxStreamReader.getNamespaceCount();
        for (int i = 0; i < nsCount; i++) {
            this.visitor.startPrefixMapping(fixNull(this.staxStreamReader.getNamespacePrefix(i)), fixNull(this.staxStreamReader.getNamespaceURI(i)));
        }
        this.tagName.uri = fixNull(this.staxStreamReader.getNamespaceURI());
        this.tagName.local = this.staxStreamReader.getLocalName();
        this.tagName.atts = this.attributes;
        this.visitor.startElement(this.tagName);
    }

    protected void handleCharacters() throws XMLStreamException, SAXException {
        if (this.predictor.expectText()) {
            this.buffer.append(this.staxStreamReader.getTextCharacters(), this.staxStreamReader.getTextStart(), this.staxStreamReader.getTextLength());
        }
    }

    private void processText(boolean ignorable) throws SAXException {
        if (this.predictor.expectText() && (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer) || this.context.getCurrentState().isMixed())) {
            if (this.textReported) {
                this.textReported = false;
            } else {
                this.visitor.text(this.buffer);
            }
        }
        this.buffer.setLength(0);
    }

    private static Class initFIStAXReaderClass() {
        try {
            Class<?> fisr = Class.forName("org.jvnet.fastinfoset.stax.FastInfosetStreamReader");
            Class<?> sdp = Class.forName("com.sun.xml.fastinfoset.stax.StAXDocumentParser");
            if (fisr.isAssignableFrom(sdp)) {
                return sdp;
            }
            return null;
        } catch (Throwable th) {
            return null;
        }
    }

    private static Constructor<? extends StAXConnector> initFastInfosetConnectorClass() {
        try {
            if (FI_STAX_READER_CLASS == null) {
                return null;
            }
            Class c = Class.forName("com.sun.xml.bind.v2.runtime.unmarshaller.FastInfosetConnector");
            return c.getConstructor(FI_STAX_READER_CLASS, XmlVisitor.class);
        } catch (Throwable th) {
            return null;
        }
    }

    private static Class initStAXExReader() {
        try {
            return Class.forName("org.jvnet.staxex.XMLStreamReaderEx");
        } catch (Throwable th) {
            return null;
        }
    }

    private static Constructor<? extends StAXConnector> initStAXExConnector() {
        try {
            Class c = Class.forName("com.sun.xml.bind.v2.runtime.unmarshaller.StAXExConnector");
            return c.getConstructor(STAX_EX_READER_CLASS, XmlVisitor.class);
        } catch (Throwable th) {
            return null;
        }
    }
}
