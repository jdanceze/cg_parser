package com.sun.xml.bind.v2.runtime.unmarshaller;

import com.sun.xml.bind.WhiteSpaceProcessor;
import com.sun.xml.fastinfoset.stax.StAXDocumentParser;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/FastInfosetConnector.class */
public final class FastInfosetConnector extends StAXConnector {
    private final StAXDocumentParser fastInfosetStreamReader;
    private boolean textReported;
    private final Base64Data base64Data;
    private final StringBuilder buffer;
    private final CharSequenceImpl charArray;

    public FastInfosetConnector(StAXDocumentParser fastInfosetStreamReader, XmlVisitor visitor) {
        super(visitor);
        this.base64Data = new Base64Data();
        this.buffer = new StringBuilder();
        this.charArray = new CharSequenceImpl();
        fastInfosetStreamReader.setStringInterning(true);
        this.fastInfosetStreamReader = fastInfosetStreamReader;
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    public void bridge() throws XMLStreamException {
        try {
            int depth = 0;
            int event = this.fastInfosetStreamReader.getEventType();
            if (event == 7) {
                while (!this.fastInfosetStreamReader.isStartElement()) {
                    event = this.fastInfosetStreamReader.next();
                }
            }
            if (event != 1) {
                throw new IllegalStateException("The current event is not START_ELEMENT\n but " + event);
            }
            handleStartDocument(this.fastInfosetStreamReader.getNamespaceContext());
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
                            this.fastInfosetStreamReader.next();
                            handleEndDocument();
                            return;
                        }
                    case 4:
                    case 6:
                    case 12:
                        if (!this.predictor.expectText()) {
                            break;
                        } else {
                            int event2 = this.fastInfosetStreamReader.peekNext();
                            if (event2 == 2) {
                                processNonIgnorableText();
                                break;
                            } else if (event2 == 1) {
                                processIgnorableText();
                                break;
                            } else {
                                handleFragmentedCharacters();
                                break;
                            }
                        }
                }
                event = this.fastInfosetStreamReader.next();
            }
        } catch (SAXException e) {
            throw new XMLStreamException(e);
        }
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    protected Location getCurrentLocation() {
        return this.fastInfosetStreamReader.getLocation();
    }

    @Override // com.sun.xml.bind.v2.runtime.unmarshaller.StAXConnector
    protected String getCurrentQName() {
        return this.fastInfosetStreamReader.getNameString();
    }

    private void handleStartElement() throws SAXException {
        processUnreportedText();
        for (int i = 0; i < this.fastInfosetStreamReader.accessNamespaceCount(); i++) {
            this.visitor.startPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i), this.fastInfosetStreamReader.getNamespaceURI(i));
        }
        this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
        this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
        this.tagName.atts = this.fastInfosetStreamReader.getAttributesHolder();
        this.visitor.startElement(this.tagName);
    }

    private void handleFragmentedCharacters() throws XMLStreamException, SAXException {
        this.buffer.setLength(0);
        this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
        while (true) {
            switch (this.fastInfosetStreamReader.peekNext()) {
                case 1:
                    processBufferedText(true);
                    return;
                case 2:
                    processBufferedText(false);
                    return;
                case 3:
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                default:
                    this.fastInfosetStreamReader.next();
                    break;
                case 4:
                case 6:
                case 12:
                    this.fastInfosetStreamReader.next();
                    this.buffer.append(this.fastInfosetStreamReader.getTextCharacters(), this.fastInfosetStreamReader.getTextStart(), this.fastInfosetStreamReader.getTextLength());
                    break;
            }
        }
    }

    private void handleEndElement() throws SAXException {
        processUnreportedText();
        this.tagName.uri = this.fastInfosetStreamReader.accessNamespaceURI();
        this.tagName.local = this.fastInfosetStreamReader.accessLocalName();
        this.visitor.endElement(this.tagName);
        for (int i = this.fastInfosetStreamReader.accessNamespaceCount() - 1; i >= 0; i--) {
            this.visitor.endPrefixMapping(this.fastInfosetStreamReader.getNamespacePrefix(i));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/unmarshaller/FastInfosetConnector$CharSequenceImpl.class */
    public final class CharSequenceImpl implements CharSequence {
        char[] ch;
        int start;
        int length;

        CharSequenceImpl() {
        }

        CharSequenceImpl(char[] ch, int start, int length) {
            this.ch = ch;
            this.start = start;
            this.length = length;
        }

        public void set() {
            this.ch = FastInfosetConnector.this.fastInfosetStreamReader.getTextCharacters();
            this.start = FastInfosetConnector.this.fastInfosetStreamReader.getTextStart();
            this.length = FastInfosetConnector.this.fastInfosetStreamReader.getTextLength();
        }

        @Override // java.lang.CharSequence
        public final int length() {
            return this.length;
        }

        @Override // java.lang.CharSequence
        public final char charAt(int index) {
            return this.ch[this.start + index];
        }

        @Override // java.lang.CharSequence
        public final CharSequence subSequence(int start, int end) {
            return new CharSequenceImpl(this.ch, this.start + start, end - start);
        }

        @Override // java.lang.CharSequence
        public String toString() {
            return new String(this.ch, this.start, this.length);
        }
    }

    private void processNonIgnorableText() throws SAXException {
        this.textReported = true;
        boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
        if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1) {
            this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
            this.visitor.text(this.base64Data);
            return;
        }
        if (isTextAlgorithmAplied) {
            this.fastInfosetStreamReader.getText();
        }
        this.charArray.set();
        this.visitor.text(this.charArray);
    }

    private void processIgnorableText() throws SAXException {
        boolean isTextAlgorithmAplied = this.fastInfosetStreamReader.getTextAlgorithmBytes() != null;
        if (isTextAlgorithmAplied && this.fastInfosetStreamReader.getTextAlgorithmIndex() == 1) {
            this.base64Data.set(this.fastInfosetStreamReader.getTextAlgorithmBytesClone(), null);
            this.visitor.text(this.base64Data);
            this.textReported = true;
            return;
        }
        if (isTextAlgorithmAplied) {
            this.fastInfosetStreamReader.getText();
        }
        this.charArray.set();
        if (!WhiteSpaceProcessor.isWhiteSpace(this.charArray)) {
            this.visitor.text(this.charArray);
            this.textReported = true;
        }
    }

    private void processBufferedText(boolean ignorable) throws SAXException {
        if (!ignorable || !WhiteSpaceProcessor.isWhiteSpace(this.buffer)) {
            this.visitor.text(this.buffer);
            this.textReported = true;
        }
    }

    private void processUnreportedText() throws SAXException {
        if (!this.textReported && this.predictor.expectText()) {
            this.visitor.text("");
        }
        this.textReported = false;
    }
}
