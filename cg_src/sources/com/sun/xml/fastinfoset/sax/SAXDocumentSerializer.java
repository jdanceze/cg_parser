package com.sun.xml.fastinfoset.sax;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.Encoder;
import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.fastinfoset.QualifiedName;
import com.sun.xml.fastinfoset.util.LocalNameQualifiedNamesMap;
import java.io.IOException;
import org.jvnet.fastinfoset.FastInfosetException;
import org.jvnet.fastinfoset.RestrictedAlphabet;
import org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import org.jvnet.fastinfoset.sax.FastInfosetWriter;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/sax/SAXDocumentSerializer.class */
public class SAXDocumentSerializer extends Encoder implements FastInfosetWriter {
    protected boolean _elementHasNamespaces;
    protected boolean _charactersAsCDATA;

    /* JADX INFO: Access modifiers changed from: protected */
    public SAXDocumentSerializer(boolean v) {
        super(v);
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    public SAXDocumentSerializer() {
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    @Override // com.sun.xml.fastinfoset.Encoder, org.jvnet.fastinfoset.FastInfosetSerializer
    public void reset() {
        super.reset();
        this._elementHasNamespaces = false;
        this._charactersAsCDATA = false;
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startDocument() throws SAXException {
        try {
            reset();
            encodeHeader(false);
            encodeInitialVocabulary();
        } catch (IOException e) {
            throw new SAXException("startDocument", e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void endDocument() throws SAXException {
        try {
            encodeDocumentTermination();
        } catch (IOException e) {
            throw new SAXException("endDocument", e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
        try {
            if (!this._elementHasNamespaces) {
                encodeTermination();
                mark();
                this._elementHasNamespaces = true;
                write(56);
            }
            encodeNamespaceAttribute(prefix, uri);
        } catch (IOException e) {
            throw new SAXException("startElement", e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
        int attributeCount = (atts == null || atts.getLength() <= 0) ? 0 : countAttributes(atts);
        try {
            if (this._elementHasNamespaces) {
                this._elementHasNamespaces = false;
                if (attributeCount > 0) {
                    byte[] bArr = this._octetBuffer;
                    int i = this._markIndex;
                    bArr[i] = (byte) (bArr[i] | 64);
                }
                resetMark();
                write(240);
                this._b = 0;
            } else {
                encodeTermination();
                this._b = 0;
                if (attributeCount > 0) {
                    this._b |= 64;
                }
            }
            encodeElement(namespaceURI, qName, localName);
            if (attributeCount > 0) {
                encodeAttributes(atts);
            }
        } catch (IOException e) {
            throw new SAXException("startElement", e);
        } catch (FastInfosetException e2) {
            throw new SAXException("startElement", e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void endElement(String namespaceURI, String localName, String qName) throws SAXException {
        try {
            encodeElementTermination();
        } catch (IOException e) {
            throw new SAXException("endElement", e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void characters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, start, length)) {
            return;
        }
        try {
            encodeTermination();
            if (!this._charactersAsCDATA) {
                encodeCharacters(ch, start, length);
            } else {
                encodeCIIBuiltInAlgorithmDataAsCDATA(ch, start, length);
            }
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
        if (getIgnoreWhiteSpaceTextContent()) {
            return;
        }
        characters(ch, start, length);
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void processingInstruction(String target, String data) throws SAXException {
        try {
            if (getIgnoreProcesingInstructions()) {
                return;
            }
            if (target.length() == 0) {
                throw new SAXException(CommonResourceBundle.getInstance().getString("message.processingInstructionTargetIsEmpty"));
            }
            encodeTermination();
            encodeProcessingInstruction(target, data);
        } catch (IOException e) {
            throw new SAXException("processingInstruction", e);
        }
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void setDocumentLocator(Locator locator) {
    }

    @Override // org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void skippedEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void comment(char[] ch, int start, int length) throws SAXException {
        try {
            if (getIgnoreComments()) {
                return;
            }
            encodeTermination();
            encodeComment(ch, start, length);
        } catch (IOException e) {
            throw new SAXException("startElement", e);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startCDATA() throws SAXException {
        this._charactersAsCDATA = true;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endCDATA() throws SAXException {
        this._charactersAsCDATA = false;
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startDTD(String name, String publicId, String systemId) throws SAXException {
        if (getIgnoreDTD()) {
            return;
        }
        try {
            encodeTermination();
            encodeDocumentTypeDeclaration(publicId, systemId);
            encodeElementTermination();
        } catch (IOException e) {
            throw new SAXException("startDTD", e);
        }
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endDTD() throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void startEntity(String name) throws SAXException {
    }

    @Override // org.xml.sax.ext.LexicalHandler
    public final void endEntity(String name) throws SAXException {
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public final void octets(String URI, int id, byte[] b, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeNonIdentifyingStringOnThirdBit(URI, id, b, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.EncodingAlgorithmContentHandler
    public final void object(String URI, int id, Object data) throws SAXException {
        try {
            encodeTermination();
            encodeNonIdentifyingStringOnThirdBit(URI, id, data);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void bytes(byte[] b, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIOctetAlgorithmData(1, b, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void shorts(short[] s, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(2, s, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void ints(int[] i, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(3, i, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void longs(long[] l, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(4, l, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void booleans(boolean[] b, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(5, b, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void floats(float[] f, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(6, f, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public final void doubles(double[] d, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(7, d, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.PrimitiveTypeContentHandler
    public void uuids(long[] msblsb, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            encodeCIIBuiltInAlgorithmData(8, msblsb, start, length);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void numericCharacters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeNumericFourBitCharacters(ch, start, length, addToTable);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void dateTimeCharacters(char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeDateTimeFourBitCharacters(ch, start, length, addToTable);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.RestrictedAlphabetContentHandler
    public void alphabetCharacters(String alphabet, char[] ch, int start, int length) throws SAXException {
        if (length <= 0) {
            return;
        }
        try {
            encodeTermination();
            boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
            encodeAlphabetCharacters(alphabet, ch, start, length, addToTable);
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    @Override // org.jvnet.fastinfoset.sax.ExtendedContentHandler
    public void characters(char[] ch, int start, int length, boolean index) throws SAXException {
        if (length <= 0) {
            return;
        }
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, start, length)) {
            return;
        }
        try {
            encodeTermination();
            if (!this._charactersAsCDATA) {
                encodeNonIdentifyingStringOnThirdBit(ch, start, length, this._v.characterContentChunk, index, true);
            } else {
                encodeCIIBuiltInAlgorithmDataAsCDATA(ch, start, length);
            }
        } catch (IOException e) {
            throw new SAXException(e);
        } catch (FastInfosetException e2) {
            throw new SAXException(e2);
        }
    }

    protected final int countAttributes(Attributes atts) {
        int count = 0;
        for (int i = 0; i < atts.getLength(); i++) {
            String uri = atts.getURI(i);
            if (uri != EncodingConstants.XMLNS_NAMESPACE_NAME && !uri.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                count++;
            }
        }
        return count;
    }

    protected void encodeAttributes(Attributes atts) throws IOException, FastInfosetException {
        if (atts instanceof EncodingAlgorithmAttributes) {
            EncodingAlgorithmAttributes eAtts = (EncodingAlgorithmAttributes) atts;
            for (int i = 0; i < eAtts.getLength(); i++) {
                if (encodeAttribute(atts.getURI(i), atts.getQName(i), atts.getLocalName(i))) {
                    Object data = eAtts.getAlgorithmData(i);
                    if (data == null) {
                        String value = eAtts.getValue(i);
                        boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                        boolean mustBeAddedToTable = eAtts.getToIndex(i);
                        String alphabet = eAtts.getAlpababet(i);
                        if (alphabet == null) {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.DATE_TIME_CHARACTERS) {
                            encodeDateTimeNonIdentifyingStringOnFirstBit(value, addToTable, mustBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.NUMERIC_CHARACTERS) {
                            encodeNumericNonIdentifyingStringOnFirstBit(value, addToTable, mustBeAddedToTable);
                        } else {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustBeAddedToTable);
                        }
                    } else {
                        encodeNonIdentifyingStringOnFirstBit(eAtts.getAlgorithmURI(i), eAtts.getAlgorithmIndex(i), data);
                    }
                }
            }
        } else {
            for (int i2 = 0; i2 < atts.getLength(); i2++) {
                if (encodeAttribute(atts.getURI(i2), atts.getQName(i2), atts.getLocalName(i2))) {
                    String value2 = atts.getValue(i2);
                    encodeNonIdentifyingStringOnFirstBit(value2, this._v.attributeValue, isAttributeValueLengthMatchesLimit(value2.length()), false);
                }
            }
        }
        this._b = 240;
        this._terminate = true;
    }

    protected void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                QualifiedName n = names[i];
                if (namespaceURI == n.namespaceName || namespaceURI.equals(n.namespaceName)) {
                    encodeNonZeroIntegerOnThirdBit(names[i].index);
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
    }

    protected boolean encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                if (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName)) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
                    return true;
                }
            }
        }
        return encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
    }
}
