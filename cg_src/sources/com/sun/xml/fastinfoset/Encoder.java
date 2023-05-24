package com.sun.xml.fastinfoset;

import com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.fastinfoset.org.apache.xerces.util.XMLChar;
import com.sun.xml.fastinfoset.util.CharArrayIntMap;
import com.sun.xml.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.fastinfoset.util.StringIntMap;
import com.sun.xml.fastinfoset.vocab.SerializerVocabulary;
import dalvik.bytecode.Opcodes;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import org.jvnet.fastinfoset.EncodingAlgorithm;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
import org.jvnet.fastinfoset.ExternalVocabulary;
import org.jvnet.fastinfoset.FastInfosetException;
import org.jvnet.fastinfoset.FastInfosetSerializer;
import org.jvnet.fastinfoset.RestrictedAlphabet;
import org.jvnet.fastinfoset.VocabularyApplicationData;
import org.xml.sax.helpers.DefaultHandler;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/Encoder.class */
public abstract class Encoder extends DefaultHandler implements FastInfosetSerializer {
    public static final String CHARACTER_ENCODING_SCHEME_SYSTEM_PROPERTY = "com.sun.xml.fastinfoset.serializer.character-encoding-scheme";
    protected static final String _characterEncodingSchemeSystemDefault = getDefaultEncodingScheme();
    private static int[] NUMERIC_CHARACTERS_TABLE = new int[maxCharacter(RestrictedAlphabet.NUMERIC_CHARACTERS) + 1];
    private static int[] DATE_TIME_CHARACTERS_TABLE = new int[maxCharacter(RestrictedAlphabet.DATE_TIME_CHARACTERS) + 1];
    private boolean _ignoreDTD;
    private boolean _ignoreComments;
    private boolean _ignoreProcessingInstructions;
    private boolean _ignoreWhiteSpaceTextContent;
    private boolean _useLocalNameAsKeyForQualifiedNameLookup;
    private boolean _encodingStringsAsUtf8;
    private int _nonIdentifyingStringOnThirdBitCES;
    private int _nonIdentifyingStringOnFirstBitCES;
    private Map _registeredEncodingAlgorithms;
    protected SerializerVocabulary _v;
    protected VocabularyApplicationData _vData;
    private boolean _vIsInternal;
    protected boolean _terminate;
    protected int _b;
    protected OutputStream _s;
    protected char[] _charBuffer;
    protected byte[] _octetBuffer;
    protected int _octetBufferIndex;
    protected int _markIndex;
    protected int minAttributeValueSize;
    protected int maxAttributeValueSize;
    protected int attributeValueMapTotalCharactersConstraint;
    protected int minCharacterContentChunkSize;
    protected int maxCharacterContentChunkSize;
    protected int characterContentChunkMapTotalCharactersConstraint;
    private int _bitsLeftInOctet;
    private EncodingBufferOutputStream _encodingBufferOutputStream;
    private byte[] _encodingBuffer;
    private int _encodingBufferIndex;

    static /* synthetic */ int access$108(Encoder x0) {
        int i = x0._encodingBufferIndex;
        x0._encodingBufferIndex = i + 1;
        return i;
    }

    static {
        for (int i = 0; i < NUMERIC_CHARACTERS_TABLE.length; i++) {
            NUMERIC_CHARACTERS_TABLE[i] = -1;
        }
        for (int i2 = 0; i2 < DATE_TIME_CHARACTERS_TABLE.length; i2++) {
            DATE_TIME_CHARACTERS_TABLE[i2] = -1;
        }
        for (int i3 = 0; i3 < RestrictedAlphabet.NUMERIC_CHARACTERS.length(); i3++) {
            NUMERIC_CHARACTERS_TABLE[RestrictedAlphabet.NUMERIC_CHARACTERS.charAt(i3)] = i3;
        }
        for (int i4 = 0; i4 < RestrictedAlphabet.DATE_TIME_CHARACTERS.length(); i4++) {
            DATE_TIME_CHARACTERS_TABLE[RestrictedAlphabet.DATE_TIME_CHARACTERS.charAt(i4)] = i4;
        }
    }

    private static String getDefaultEncodingScheme() {
        String p = System.getProperty(CHARACTER_ENCODING_SCHEME_SYSTEM_PROPERTY, "UTF-8");
        if (p.equals(FastInfosetSerializer.UTF_16BE)) {
            return FastInfosetSerializer.UTF_16BE;
        }
        return "UTF-8";
    }

    private static int maxCharacter(String alphabet) {
        int c = 0;
        for (int i = 0; i < alphabet.length(); i++) {
            if (c < alphabet.charAt(i)) {
                c = alphabet.charAt(i);
            }
        }
        return c;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Encoder() {
        this._encodingStringsAsUtf8 = true;
        this._registeredEncodingAlgorithms = new HashMap();
        this._terminate = false;
        this._charBuffer = new char[512];
        this._octetBuffer = new byte[1024];
        this._markIndex = -1;
        this.minAttributeValueSize = 0;
        this.maxAttributeValueSize = 32;
        this.attributeValueMapTotalCharactersConstraint = 1073741823;
        this.minCharacterContentChunkSize = 0;
        this.maxCharacterContentChunkSize = 32;
        this.characterContentChunkMapTotalCharactersConstraint = 1073741823;
        this._encodingBufferOutputStream = new EncodingBufferOutputStream();
        this._encodingBuffer = new byte[512];
        setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Encoder(boolean useLocalNameAsKeyForQualifiedNameLookup) {
        this._encodingStringsAsUtf8 = true;
        this._registeredEncodingAlgorithms = new HashMap();
        this._terminate = false;
        this._charBuffer = new char[512];
        this._octetBuffer = new byte[1024];
        this._markIndex = -1;
        this.minAttributeValueSize = 0;
        this.maxAttributeValueSize = 32;
        this.attributeValueMapTotalCharactersConstraint = 1073741823;
        this.minCharacterContentChunkSize = 0;
        this.maxCharacterContentChunkSize = 32;
        this.characterContentChunkMapTotalCharactersConstraint = 1073741823;
        this._encodingBufferOutputStream = new EncodingBufferOutputStream();
        this._encodingBuffer = new byte[512];
        setCharacterEncodingScheme(_characterEncodingSchemeSystemDefault);
        this._useLocalNameAsKeyForQualifiedNameLookup = useLocalNameAsKeyForQualifiedNameLookup;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreDTD(boolean ignoreDTD) {
        this._ignoreDTD = ignoreDTD;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreDTD() {
        return this._ignoreDTD;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreComments(boolean ignoreComments) {
        this._ignoreComments = ignoreComments;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreComments() {
        return this._ignoreComments;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreProcesingInstructions(boolean ignoreProcesingInstructions) {
        this._ignoreProcessingInstructions = ignoreProcesingInstructions;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreProcesingInstructions() {
        return this._ignoreProcessingInstructions;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final void setIgnoreWhiteSpaceTextContent(boolean ignoreWhiteSpaceTextContent) {
        this._ignoreWhiteSpaceTextContent = ignoreWhiteSpaceTextContent;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public final boolean getIgnoreWhiteSpaceTextContent() {
        return this._ignoreWhiteSpaceTextContent;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setCharacterEncodingScheme(String characterEncodingScheme) {
        if (characterEncodingScheme.equals(FastInfosetSerializer.UTF_16BE)) {
            this._encodingStringsAsUtf8 = false;
            this._nonIdentifyingStringOnThirdBitCES = 132;
            this._nonIdentifyingStringOnFirstBitCES = 16;
            return;
        }
        this._encodingStringsAsUtf8 = true;
        this._nonIdentifyingStringOnThirdBitCES = 128;
        this._nonIdentifyingStringOnFirstBitCES = 0;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public String getCharacterEncodingScheme() {
        return this._encodingStringsAsUtf8 ? "UTF-8" : FastInfosetSerializer.UTF_16BE;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setRegisteredEncodingAlgorithms(Map algorithms) {
        this._registeredEncodingAlgorithms = algorithms;
        if (this._registeredEncodingAlgorithms == null) {
            this._registeredEncodingAlgorithms = new HashMap();
        }
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public Map getRegisteredEncodingAlgorithms() {
        return this._registeredEncodingAlgorithms;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMinCharacterContentChunkSize() {
        return this.minCharacterContentChunkSize;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMinCharacterContentChunkSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.minCharacterContentChunkSize = size;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMaxCharacterContentChunkSize() {
        return this.maxCharacterContentChunkSize;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMaxCharacterContentChunkSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.maxCharacterContentChunkSize = size;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getCharacterContentChunkMapMemoryLimit() {
        return this.characterContentChunkMapTotalCharactersConstraint * 2;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setCharacterContentChunkMapMemoryLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.characterContentChunkMapTotalCharactersConstraint = size / 2;
    }

    public boolean isCharacterContentChunkLengthMatchesLimit(int length) {
        return length >= this.minCharacterContentChunkSize && length < this.maxCharacterContentChunkSize;
    }

    public boolean canAddCharacterContentToTable(int length, CharArrayIntMap map) {
        return map.getTotalCharacterCount() + length < this.characterContentChunkMapTotalCharactersConstraint;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMinAttributeValueSize() {
        return this.minAttributeValueSize;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMinAttributeValueSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.minAttributeValueSize = size;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getMaxAttributeValueSize() {
        return this.maxAttributeValueSize;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setMaxAttributeValueSize(int size) {
        if (size < 0) {
            size = 0;
        }
        this.maxAttributeValueSize = size;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setAttributeValueMapMemoryLimit(int size) {
        if (size < 0) {
            size = 0;
        }
        this.attributeValueMapTotalCharactersConstraint = size / 2;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public int getAttributeValueMapMemoryLimit() {
        return this.attributeValueMapTotalCharactersConstraint * 2;
    }

    public boolean isAttributeValueLengthMatchesLimit(int length) {
        return length >= this.minAttributeValueSize && length < this.maxAttributeValueSize;
    }

    public boolean canAddAttributeToTable(int length) {
        return this._v.attributeValue.getTotalCharacterCount() + length < this.attributeValueMapTotalCharactersConstraint;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setExternalVocabulary(ExternalVocabulary v) {
        this._v = new SerializerVocabulary();
        SerializerVocabulary ev = new SerializerVocabulary(v.vocabulary, this._useLocalNameAsKeyForQualifiedNameLookup);
        this._v.setExternalVocabulary(v.URI, ev, false);
        this._vIsInternal = true;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setVocabularyApplicationData(VocabularyApplicationData data) {
        this._vData = data;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public VocabularyApplicationData getVocabularyApplicationData() {
        return this._vData;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void reset() {
        this._terminate = false;
    }

    @Override // org.jvnet.fastinfoset.FastInfosetSerializer
    public void setOutputStream(OutputStream s) {
        this._octetBufferIndex = 0;
        this._markIndex = -1;
        this._s = s;
    }

    public void setVocabulary(SerializerVocabulary vocabulary) {
        this._v = vocabulary;
        this._vIsInternal = false;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeHeader(boolean encodeXmlDecl) throws IOException {
        if (encodeXmlDecl) {
            this._s.write(EncodingConstants.XML_DECLARATION_VALUES[0]);
        }
        this._s.write(EncodingConstants.BINARY_HEADER);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeInitialVocabulary() throws IOException {
        if (this._v == null) {
            this._v = new SerializerVocabulary();
            this._vIsInternal = true;
        } else if (this._vIsInternal) {
            this._v.clear();
            if (this._vData != null) {
                this._vData.clear();
            }
        }
        if (!this._v.hasInitialVocabulary() && !this._v.hasExternalVocabulary()) {
            write(0);
        } else if (!this._v.hasInitialVocabulary()) {
            if (this._v.hasExternalVocabulary()) {
                this._b = 32;
                write(this._b);
                this._b = 16;
                write(this._b);
                write(0);
                encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
            }
        } else {
            this._b = 32;
            write(this._b);
            SerializerVocabulary initialVocabulary = this._v.getReadOnlyVocabulary();
            if (initialVocabulary.hasExternalVocabulary()) {
                this._b = 16;
                write(this._b);
                write(0);
            }
            if (initialVocabulary.hasExternalVocabulary()) {
                encodeNonEmptyOctetStringOnSecondBit(this._v.getExternalVocabularyURI());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeDocumentTermination() throws IOException {
        encodeElementTermination();
        encodeTermination();
        _flush();
        this._s.flush();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeElementTermination() throws IOException {
        this._terminate = true;
        switch (this._b) {
            case 240:
                this._b = 255;
                return;
            case 255:
                write(255);
                break;
        }
        this._b = 240;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeTermination() throws IOException {
        if (this._terminate) {
            write(this._b);
            this._b = 0;
            this._terminate = false;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNamespaceAttribute(String prefix, String uri) throws IOException {
        this._b = 204;
        if (prefix.length() > 0) {
            this._b |= 2;
        }
        if (uri.length() > 0) {
            this._b |= 1;
        }
        write(this._b);
        if (prefix.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(prefix, this._v.prefix);
        }
        if (uri.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(uri, this._v.namespaceName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCharacters(char[] ch, int offset, int length) throws IOException {
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCharactersNoClone(char[] ch, int offset, int length) throws IOException {
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnThirdBit(ch, offset, length, this._v.characterContentChunk, addToTable, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNumericFourBitCharacters(char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        encodeFourBitCharacters(0, NUMERIC_CHARACTERS_TABLE, ch, offset, length, addToTable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeDateTimeFourBitCharacters(char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        encodeFourBitCharacters(1, DATE_TIME_CHARACTERS_TABLE, ch, offset, length, addToTable);
    }

    protected final void encodeFourBitCharacters(int id, int[] table, char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        int i;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
            if (canAddCharacterContentToTable) {
                i = this._v.characterContentChunk.obtainIndex(ch, offset, length, true);
            } else {
                i = this._v.characterContentChunk.get(ch, offset, length);
            }
            int index = i;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 152;
            } else {
                this._b = 136;
            }
        } else {
            this._b = 136;
        }
        write(this._b);
        this._b = id << 2;
        encodeNonEmptyFourBitCharacterStringOnSeventhBit(table, ch, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeAlphabetCharacters(String alphabet, char[] ch, int offset, int length, boolean addToTable) throws FastInfosetException, IOException {
        int i;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, this._v.characterContentChunk);
            if (canAddCharacterContentToTable) {
                i = this._v.characterContentChunk.obtainIndex(ch, offset, length, true);
            } else {
                i = this._v.characterContentChunk.get(ch, offset, length);
            }
            int index = i;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 152;
            } else {
                this._b = 136;
            }
        } else {
            this._b = 136;
        }
        int id = this._v.restrictedAlphabet.get(alphabet);
        if (id == -1) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.restrictedAlphabetNotPresent"));
        }
        int id2 = id + 32;
        this._b |= (id2 & 192) >> 6;
        write(this._b);
        this._b = (id2 & 63) << 2;
        encodeNonEmptyNBitCharacterStringOnSeventhBit(alphabet, ch, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeProcessingInstruction(String target, String data) throws IOException {
        write(225);
        encodeIdentifyingNonEmptyStringOnFirstBit(target, this._v.otherNCName);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(data.length());
        encodeNonIdentifyingStringOnFirstBit(data, this._v.otherString, addToTable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeDocumentTypeDeclaration(String systemId, String publicId) throws IOException {
        this._b = 196;
        if (systemId != null && systemId.length() > 0) {
            this._b |= 2;
        }
        if (publicId != null && publicId.length() > 0) {
            this._b |= 1;
        }
        write(this._b);
        if (systemId != null && systemId.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(systemId, this._v.otherURI);
        }
        if (publicId != null && publicId.length() > 0) {
            encodeIdentifyingNonEmptyStringOnFirstBit(publicId, this._v.otherURI);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeComment(char[] ch, int offset, int length) throws IOException {
        write(226);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCommentNoClone(char[] ch, int offset, int length) throws IOException {
        write(226);
        boolean addToTable = isCharacterContentChunkLengthMatchesLimit(length);
        encodeNonIdentifyingStringOnFirstBit(ch, offset, length, this._v.otherString, addToTable, false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                if ((prefix == names[i].prefix || prefix.equals(names[i].prefix)) && (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName))) {
                    encodeNonZeroIntegerOnThirdBit(names[i].index);
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, prefix, localName, entry);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeLiteralElementQualifiedNameOnThirdBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry) throws IOException {
        QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.elementName.getNextIndex());
        entry.addQualifiedName(name);
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (namespaceURI.length() > 0) {
            namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
            if (namespaceURIIndex == -1) {
                throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[]{namespaceURI}));
            }
            if (prefix.length() > 0) {
                prefixIndex = this._v.prefix.get(prefix);
                if (prefixIndex == -1) {
                    throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[]{prefix}));
                }
            }
        }
        int localNameIndex = this._v.localName.obtainIndex(localName);
        this._b |= 60;
        if (namespaceURIIndex >= 0) {
            this._b |= 1;
            if (prefixIndex >= 0) {
                this._b |= 2;
            }
        }
        write(this._b);
        if (namespaceURIIndex >= 0) {
            if (prefixIndex >= 0) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
            }
            encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
        }
        if (localNameIndex >= 0) {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
        } else {
            encodeNonEmptyOctetStringOnSecondBit(localName);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                if ((prefix == names[i].prefix || prefix.equals(names[i].prefix)) && (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName))) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
                    return;
                }
            }
        }
        encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, prefix, localName, entry);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean encodeLiteralAttributeQualifiedNameOnSecondBit(String namespaceURI, String prefix, String localName, LocalNameQualifiedNamesMap.Entry entry) throws IOException {
        int namespaceURIIndex = -1;
        int prefixIndex = -1;
        if (namespaceURI.length() > 0) {
            namespaceURIIndex = this._v.namespaceName.get(namespaceURI);
            if (namespaceURIIndex == -1) {
                if (namespaceURI == EncodingConstants.XMLNS_NAMESPACE_NAME || namespaceURI.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                    return false;
                }
                throw new IOException(CommonResourceBundle.getInstance().getString("message.namespaceURINotIndexed", new Object[]{namespaceURI}));
            } else if (prefix.length() > 0) {
                prefixIndex = this._v.prefix.get(prefix);
                if (prefixIndex == -1) {
                    throw new IOException(CommonResourceBundle.getInstance().getString("message.prefixNotIndexed", new Object[]{prefix}));
                }
            }
        }
        int localNameIndex = this._v.localName.obtainIndex(localName);
        QualifiedName name = new QualifiedName(prefix, namespaceURI, localName, "", this._v.attributeName.getNextIndex());
        entry.addQualifiedName(name);
        this._b = 120;
        if (namespaceURI.length() > 0) {
            this._b |= 1;
            if (prefix.length() > 0) {
                this._b |= 2;
            }
        }
        write(this._b);
        if (namespaceURIIndex >= 0) {
            if (prefixIndex >= 0) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(prefixIndex);
            }
            encodeNonZeroIntegerOnSecondBitFirstBitOne(namespaceURIIndex);
        } else if (namespaceURI != "") {
            encodeNonEmptyOctetStringOnSecondBit(EncodingConstants.XML_NAMESPACE_PREFIX);
            encodeNonEmptyOctetStringOnSecondBit("http://www.w3.org/XML/1998/namespace");
        }
        if (localNameIndex >= 0) {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(localNameIndex);
            return true;
        }
        encodeNonEmptyOctetStringOnSecondBit(localName);
        return true;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonIdentifyingStringOnFirstBit(String s, StringIntMap map, boolean addToTable, boolean mustBeAddedToTable) throws IOException {
        int i;
        if (s == null || s.length() == 0) {
            write(255);
        } else if (addToTable || mustBeAddedToTable) {
            boolean canAddAttributeToTable = mustBeAddedToTable || canAddAttributeToTable(s.length());
            if (canAddAttributeToTable) {
                i = map.obtainIndex(s);
            } else {
                i = map.get(s);
            }
            int index = i;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
            } else if (canAddAttributeToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s);
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s);
            }
        } else {
            this._b = this._nonIdentifyingStringOnFirstBitCES;
            encodeNonEmptyCharacterStringOnFifthBit(s);
        }
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(String s, CharArrayIntMap map, boolean addToTable) throws IOException {
        int i;
        if (s == null || s.length() == 0) {
            write(255);
        } else if (addToTable) {
            char[] ch = s.toCharArray();
            int length = s.length();
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                i = map.obtainIndex(ch, 0, length, false);
            } else {
                i = map.get(ch, 0, length);
            }
            int index = i;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
            } else if (canAddCharacterContentToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, 0, length);
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(s);
            }
        } else {
            this._b = this._nonIdentifyingStringOnFirstBitCES;
            encodeNonEmptyCharacterStringOnFifthBit(s);
        }
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone) throws IOException {
        int i;
        if (length == 0) {
            write(255);
        } else if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                i = map.obtainIndex(ch, offset, length, clone);
            } else {
                i = map.get(ch, offset, length);
            }
            int index = i;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
            } else if (canAddCharacterContentToTable) {
                this._b = 64 | this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
            } else {
                this._b = this._nonIdentifyingStringOnFirstBitCES;
                encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
            }
        } else {
            this._b = this._nonIdentifyingStringOnFirstBitCES;
            encodeNonEmptyCharacterStringOnFifthBit(ch, offset, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNumericNonIdentifyingStringOnFirstBit(String s, boolean addToTable, boolean mustBeAddedToTable) throws IOException, FastInfosetException {
        encodeNonIdentifyingStringOnFirstBit(0, NUMERIC_CHARACTERS_TABLE, s, addToTable, mustBeAddedToTable);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeDateTimeNonIdentifyingStringOnFirstBit(String s, boolean addToTable, boolean mustBeAddedToTable) throws IOException, FastInfosetException {
        encodeNonIdentifyingStringOnFirstBit(1, DATE_TIME_CHARACTERS_TABLE, s, addToTable, mustBeAddedToTable);
    }

    protected final void encodeNonIdentifyingStringOnFirstBit(int id, int[] table, String s, boolean addToTable, boolean mustBeAddedToTable) throws IOException, FastInfosetException {
        int i;
        if (s == null || s.length() == 0) {
            write(255);
            return;
        }
        if (addToTable || mustBeAddedToTable) {
            boolean canAddAttributeToTable = mustBeAddedToTable || canAddAttributeToTable(s.length());
            if (canAddAttributeToTable) {
                i = this._v.attributeValue.obtainIndex(s);
            } else {
                i = this._v.attributeValue.get(s);
            }
            int index = i;
            if (index != -1) {
                encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
                return;
            } else if (canAddAttributeToTable) {
                this._b = 96;
            } else {
                this._b = 32;
            }
        } else {
            this._b = 32;
        }
        write(this._b | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        int length = s.length();
        int octetPairLength = length / 2;
        int octetSingleLength = length % 2;
        encodeNonZeroOctetStringLengthOnFifthBit(octetPairLength + octetSingleLength);
        encodeNonEmptyFourBitCharacterString(table, s.toCharArray(), 0, octetPairLength, octetSingleLength);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonIdentifyingStringOnFirstBit(String URI, int id, Object data) throws FastInfosetException, IOException {
        int length;
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            int id3 = id2 + 32;
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                encodeAIIObjectAlgorithmData(id3, data, ea);
            } else if (data instanceof byte[]) {
                byte[] d = (byte[]) data;
                encodeAIIOctetAlgorithmData(id3, d, 0, d.length);
            } else {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
        } else if (id > 9) {
            if (id >= 32) {
                if (data instanceof byte[]) {
                    byte[] d2 = (byte[]) data;
                    encodeAIIOctetAlgorithmData(id, d2, 0, d2.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        } else {
            switch (id) {
                case 0:
                case 1:
                    length = ((byte[]) data).length;
                    break;
                case 2:
                    length = ((short[]) data).length;
                    break;
                case 3:
                    length = ((int[]) data).length;
                    break;
                case 4:
                case 8:
                    length = ((long[]) data).length;
                    break;
                case 5:
                    length = ((boolean[]) data).length;
                    break;
                case 6:
                    length = ((float[]) data).length;
                    break;
                case 7:
                    length = ((double[]) data).length;
                    break;
                case 9:
                    throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
                default:
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[]{Integer.valueOf(id)}));
            }
            encodeAIIBuiltInAlgorithmData(id, data, 0, length);
        }
    }

    protected final void encodeAIIOctetAlgorithmData(int id, byte[] d, int offset, int length) throws IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        encodeNonZeroOctetStringLengthOnFifthBit(length);
        write(d, offset, length);
    }

    protected final void encodeAIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea) throws FastInfosetException, IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        this._encodingBufferOutputStream.reset();
        ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
        encodeNonZeroOctetStringLengthOnFifthBit(this._encodingBufferIndex);
        write(this._encodingBuffer, this._encodingBufferIndex);
    }

    protected final void encodeAIIBuiltInAlgorithmData(int id, Object data, int offset, int length) throws IOException {
        write(48 | ((id & 240) >> 4));
        this._b = (id & 15) << 4;
        int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
        encodeNonZeroOctetStringLengthOnFifthBit(octetLength);
        ensureSize(octetLength);
        BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
        this._octetBufferIndex += octetLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonIdentifyingStringOnThirdBit(char[] ch, int offset, int length, CharArrayIntMap map, boolean addToTable, boolean clone) throws IOException {
        int i;
        if (addToTable) {
            boolean canAddCharacterContentToTable = canAddCharacterContentToTable(length, map);
            if (canAddCharacterContentToTable) {
                i = map.obtainIndex(ch, offset, length, clone);
            } else {
                i = map.get(ch, offset, length);
            }
            int index = i;
            if (index != -1) {
                this._b = 160;
                encodeNonZeroIntegerOnFourthBit(index);
                return;
            } else if (canAddCharacterContentToTable) {
                this._b = 16 | this._nonIdentifyingStringOnThirdBitCES;
                encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
                return;
            } else {
                this._b = this._nonIdentifyingStringOnThirdBitCES;
                encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
                return;
            }
        }
        this._b = this._nonIdentifyingStringOnThirdBitCES;
        encodeNonEmptyCharacterStringOnSeventhBit(ch, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, Object data) throws FastInfosetException, IOException {
        int length;
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            int id3 = id2 + 32;
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                encodeCIIObjectAlgorithmData(id3, data, ea);
            } else if (data instanceof byte[]) {
                byte[] d = (byte[]) data;
                encodeCIIOctetAlgorithmData(id3, d, 0, d.length);
            } else {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
        } else if (id > 9) {
            if (id >= 32) {
                if (data instanceof byte[]) {
                    byte[] d2 = (byte[]) data;
                    encodeCIIOctetAlgorithmData(id, d2, 0, d2.length);
                    return;
                }
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.nullEncodingAlgorithmURI"));
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.identifiers10to31Reserved"));
        } else {
            switch (id) {
                case 0:
                case 1:
                    length = ((byte[]) data).length;
                    break;
                case 2:
                    length = ((short[]) data).length;
                    break;
                case 3:
                    length = ((int[]) data).length;
                    break;
                case 4:
                case 8:
                    length = ((long[]) data).length;
                    break;
                case 5:
                    length = ((boolean[]) data).length;
                    break;
                case 6:
                    length = ((float[]) data).length;
                    break;
                case 7:
                    length = ((double[]) data).length;
                    break;
                case 9:
                    throw new UnsupportedOperationException(CommonResourceBundle.getInstance().getString("message.CDATA"));
                default:
                    throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.UnsupportedBuiltInAlgorithm", new Object[]{Integer.valueOf(id)}));
            }
            encodeCIIBuiltInAlgorithmData(id, data, 0, length);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonIdentifyingStringOnThirdBit(String URI, int id, byte[] d, int offset, int length) throws FastInfosetException, IOException {
        if (URI != null) {
            int id2 = this._v.encodingAlgorithm.get(URI);
            if (id2 == -1) {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.EncodingAlgorithmURI", new Object[]{URI}));
            }
            id = id2 + 32;
        }
        encodeCIIOctetAlgorithmData(id, d, offset, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCIIOctetAlgorithmData(int id, byte[] d, int offset, int length) throws IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        encodeNonZeroOctetStringLengthOnSenventhBit(length);
        write(d, offset, length);
    }

    protected final void encodeCIIObjectAlgorithmData(int id, Object data, EncodingAlgorithm ea) throws FastInfosetException, IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        this._encodingBufferOutputStream.reset();
        ea.encodeToOutputStream(data, this._encodingBufferOutputStream);
        encodeNonZeroOctetStringLengthOnSenventhBit(this._encodingBufferIndex);
        write(this._encodingBuffer, this._encodingBufferIndex);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCIIBuiltInAlgorithmData(int id, Object data, int offset, int length) throws FastInfosetException, IOException {
        write(140 | ((id & 192) >> 6));
        this._b = (id & 63) << 2;
        int octetLength = BuiltInEncodingAlgorithmFactory.getAlgorithm(id).getOctetLengthFromPrimitiveLength(length);
        encodeNonZeroOctetStringLengthOnSenventhBit(octetLength);
        ensureSize(octetLength);
        BuiltInEncodingAlgorithmFactory.getAlgorithm(id).encodeToBytes(data, offset, length, this._octetBuffer, this._octetBufferIndex);
        this._octetBufferIndex += octetLength;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeCIIBuiltInAlgorithmDataAsCDATA(char[] ch, int offset, int length) throws FastInfosetException, IOException {
        write(140);
        this._b = 36;
        int length2 = encodeUTF8String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnSenventhBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeIdentifyingNonEmptyStringOnFirstBit(String s, StringIntMap map) throws IOException {
        int index = map.obtainIndex(s);
        if (index == -1) {
            encodeNonEmptyOctetStringOnSecondBit(s);
        } else {
            encodeNonZeroIntegerOnSecondBitFirstBitOne(index);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonEmptyOctetStringOnSecondBit(String s) throws IOException {
        int length = encodeUTF8String(s);
        encodeNonZeroOctetStringLengthOnSecondBit(length);
        write(this._encodingBuffer, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonZeroOctetStringLengthOnSecondBit(int length) throws IOException {
        if (length < 65) {
            write(length - 1);
        } else if (length < 321) {
            write(64);
            write(length - 65);
        } else {
            write(96);
            int length2 = length - 321;
            write(length2 >>> 24);
            write((length2 >> 16) & 255);
            write((length2 >> 8) & 255);
            write(length2 & 255);
        }
    }

    protected final void encodeNonEmptyCharacterStringOnFifthBit(String s) throws IOException {
        int length = this._encodingStringsAsUtf8 ? encodeUTF8String(s) : encodeUtf16String(s);
        encodeNonZeroOctetStringLengthOnFifthBit(length);
        write(this._encodingBuffer, length);
    }

    protected final void encodeNonEmptyCharacterStringOnFifthBit(char[] ch, int offset, int length) throws IOException {
        int length2 = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnFifthBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonZeroOctetStringLengthOnFifthBit(int length) throws IOException {
        if (length < 9) {
            write(this._b | (length - 1));
        } else if (length < 265) {
            write(this._b | 8);
            write(length - 9);
        } else {
            write(this._b | 12);
            int length2 = length - 265;
            write(length2 >>> 24);
            write((length2 >> 16) & 255);
            write((length2 >> 8) & 255);
            write(length2 & 255);
        }
    }

    protected final void encodeNonEmptyCharacterStringOnSeventhBit(char[] ch, int offset, int length) throws IOException {
        int length2 = this._encodingStringsAsUtf8 ? encodeUTF8String(ch, offset, length) : encodeUtf16String(ch, offset, length);
        encodeNonZeroOctetStringLengthOnSenventhBit(length2);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonEmptyFourBitCharacterStringOnSeventhBit(int[] table, char[] ch, int offset, int length) throws FastInfosetException, IOException {
        int octetPairLength = length / 2;
        int octetSingleLength = length % 2;
        encodeNonZeroOctetStringLengthOnSenventhBit(octetPairLength + octetSingleLength);
        encodeNonEmptyFourBitCharacterString(table, ch, offset, octetPairLength, octetSingleLength);
    }

    protected final void encodeNonEmptyFourBitCharacterString(int[] table, char[] ch, int offset, int octetPairLength, int octetSingleLength) throws FastInfosetException, IOException {
        ensureSize(octetPairLength + octetSingleLength);
        for (int i = 0; i < octetPairLength; i++) {
            int i2 = offset;
            int offset2 = offset + 1;
            offset = offset2 + 1;
            int v = (table[ch[i2]] << 4) | table[ch[offset2]];
            if (v < 0) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            byte[] bArr = this._octetBuffer;
            int i3 = this._octetBufferIndex;
            this._octetBufferIndex = i3 + 1;
            bArr[i3] = (byte) v;
        }
        if (octetSingleLength == 1) {
            int v2 = (table[ch[offset]] << 4) | 15;
            if (v2 < 0) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            byte[] bArr2 = this._octetBuffer;
            int i4 = this._octetBufferIndex;
            this._octetBufferIndex = i4 + 1;
            bArr2[i4] = (byte) v2;
        }
    }

    protected final void encodeNonEmptyNBitCharacterStringOnSeventhBit(String alphabet, char[] ch, int offset, int length) throws FastInfosetException, IOException {
        int bitsPerCharacter = 1;
        while ((1 << bitsPerCharacter) <= alphabet.length()) {
            bitsPerCharacter++;
        }
        int bits = length * bitsPerCharacter;
        int octets = bits / 8;
        int bitsOfLastOctet = bits % 8;
        int totalOctets = octets + (bitsOfLastOctet > 0 ? 1 : 0);
        encodeNonZeroOctetStringLengthOnSenventhBit(totalOctets);
        resetBits();
        ensureSize(totalOctets);
        for (int i = 0; i < length; i++) {
            char c = ch[offset + i];
            int v = 0;
            while (v < alphabet.length() && c != alphabet.charAt(v)) {
                v++;
            }
            if (v == alphabet.length()) {
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.characterOutofAlphabetRange"));
            }
            writeBits(bitsPerCharacter, v);
        }
        if (bitsOfLastOctet > 0) {
            this._b |= (1 << (8 - bitsOfLastOctet)) - 1;
            write(this._b);
        }
    }

    private final void resetBits() {
        this._bitsLeftInOctet = 8;
        this._b = 0;
    }

    private final void writeBits(int bits, int v) throws IOException {
        while (bits > 0) {
            bits--;
            int bit = (v & (1 << bits)) > 0 ? 1 : 0;
            int i = this._b;
            int i2 = this._bitsLeftInOctet - 1;
            this._bitsLeftInOctet = i2;
            this._b = i | (bit << i2);
            if (this._bitsLeftInOctet == 0) {
                write(this._b);
                this._bitsLeftInOctet = 8;
                this._b = 0;
            }
        }
    }

    protected final void encodeNonZeroOctetStringLengthOnSenventhBit(int length) throws IOException {
        if (length < 3) {
            write(this._b | (length - 1));
        } else if (length < 259) {
            write(this._b | 2);
            write(length - 3);
        } else {
            write(this._b | 3);
            int length2 = length - 259;
            write(length2 >>> 24);
            write((length2 >> 16) & 255);
            write((length2 >> 8) & 255);
            write(length2 & 255);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonZeroIntegerOnSecondBitFirstBitOne(int i) throws IOException {
        if (i < 64) {
            write(128 | i);
        } else if (i < 8256) {
            int i2 = i - 64;
            this._b = 192 | (i2 >> 8);
            write(this._b);
            write(i2 & 255);
        } else if (i < 1048576) {
            int i3 = i - 8256;
            this._b = 224 | (i3 >> 16);
            write(this._b);
            write((i3 >> 8) & 255);
            write(i3 & 255);
        } else {
            throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[]{1048576}));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonZeroIntegerOnSecondBitFirstBitZero(int i) throws IOException {
        if (i < 64) {
            write(i);
        } else if (i < 8256) {
            int i2 = i - 64;
            this._b = 64 | (i2 >> 8);
            write(this._b);
            write(i2 & 255);
        } else {
            int i3 = i - 8256;
            this._b = 96 | (i3 >> 16);
            write(this._b);
            write((i3 >> 8) & 255);
            write(i3 & 255);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void encodeNonZeroIntegerOnThirdBit(int i) throws IOException {
        if (i < 32) {
            write(this._b | i);
        } else if (i < 2080) {
            int i2 = i - 32;
            this._b |= 32 | (i2 >> 8);
            write(this._b);
            write(i2 & 255);
        } else if (i < 526368) {
            int i3 = i - 2080;
            this._b |= 40 | (i3 >> 16);
            write(this._b);
            write((i3 >> 8) & 255);
            write(i3 & 255);
        } else {
            int i4 = i - EncodingConstants.INTEGER_3RD_BIT_LARGE_LIMIT;
            this._b |= 48;
            write(this._b);
            write(i4 >> 16);
            write((i4 >> 8) & 255);
            write(i4 & 255);
        }
    }

    protected final void encodeNonZeroIntegerOnFourthBit(int i) throws IOException {
        if (i < 16) {
            write(this._b | i);
        } else if (i < 1040) {
            int i2 = i - 16;
            this._b |= 16 | (i2 >> 8);
            write(this._b);
            write(i2 & 255);
        } else if (i < 263184) {
            int i3 = i - 1040;
            this._b |= 20 | (i3 >> 16);
            write(this._b);
            write((i3 >> 8) & 255);
            write(i3 & 255);
        } else {
            int i4 = i - EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT;
            this._b |= 24;
            write(this._b);
            write(i4 >> 16);
            write((i4 >> 8) & 255);
            write(i4 & 255);
        }
    }

    protected final void encodeNonEmptyUTF8StringAsOctetString(int b, String s, int[] constants) throws IOException {
        char[] ch = s.toCharArray();
        encodeNonEmptyUTF8StringAsOctetString(b, ch, 0, ch.length, constants);
    }

    protected final void encodeNonEmptyUTF8StringAsOctetString(int b, char[] ch, int offset, int length, int[] constants) throws IOException {
        int length2 = encodeUTF8String(ch, offset, length);
        encodeNonZeroOctetStringLength(b, length2, constants);
        write(this._encodingBuffer, length2);
    }

    protected final void encodeNonZeroOctetStringLength(int b, int length, int[] constants) throws IOException {
        if (length < constants[0]) {
            write(b | (length - 1));
        } else if (length < constants[1]) {
            write(b | constants[2]);
            write(length - constants[0]);
        } else {
            write(b | constants[3]);
            int length2 = length - constants[1];
            write(length2 >>> 24);
            write((length2 >> 16) & 255);
            write((length2 >> 8) & 255);
            write(length2 & 255);
        }
    }

    protected final void encodeNonZeroInteger(int b, int i, int[] constants) throws IOException {
        if (i < constants[0]) {
            write(b | i);
        } else if (i < constants[1]) {
            int i2 = i - constants[0];
            write(b | constants[3] | (i2 >> 8));
            write(i2 & 255);
        } else if (i < constants[2]) {
            int i3 = i - constants[1];
            write(b | constants[4] | (i3 >> 16));
            write((i3 >> 8) & 255);
            write(i3 & 255);
        } else if (i < 1048576) {
            int i4 = i - constants[2];
            write(b | constants[5]);
            write(i4 >> 16);
            write((i4 >> 8) & 255);
            write(i4 & 255);
        } else {
            throw new IOException(CommonResourceBundle.getInstance().getString("message.integerMaxSize", new Object[]{1048576}));
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void mark() {
        this._markIndex = this._octetBufferIndex;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void resetMark() {
        this._markIndex = -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final boolean hasMark() {
        return this._markIndex != -1;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void write(int i) throws IOException {
        if (this._octetBufferIndex < this._octetBuffer.length) {
            byte[] bArr = this._octetBuffer;
            int i2 = this._octetBufferIndex;
            this._octetBufferIndex = i2 + 1;
            bArr[i2] = (byte) i;
        } else if (this._markIndex == -1) {
            this._s.write(this._octetBuffer);
            this._octetBufferIndex = 1;
            this._octetBuffer[0] = (byte) i;
        } else {
            resize((this._octetBuffer.length * 3) / 2);
            byte[] bArr2 = this._octetBuffer;
            int i3 = this._octetBufferIndex;
            this._octetBufferIndex = i3 + 1;
            bArr2[i3] = (byte) i;
        }
    }

    protected final void write(byte[] b, int length) throws IOException {
        write(b, 0, length);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final void write(byte[] b, int offset, int length) throws IOException {
        if (this._octetBufferIndex + length < this._octetBuffer.length) {
            System.arraycopy(b, offset, this._octetBuffer, this._octetBufferIndex, length);
            this._octetBufferIndex += length;
        } else if (this._markIndex == -1) {
            this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
            this._s.write(b, offset, length);
            this._octetBufferIndex = 0;
        } else {
            resize((((this._octetBuffer.length + length) * 3) / 2) + 1);
            System.arraycopy(b, offset, this._octetBuffer, this._octetBufferIndex, length);
            this._octetBufferIndex += length;
        }
    }

    private void ensureSize(int length) {
        if (this._octetBufferIndex + length > this._octetBuffer.length) {
            resize((((this._octetBufferIndex + length) * 3) / 2) + 1);
        }
    }

    private void resize(int length) {
        byte[] b = new byte[length];
        System.arraycopy(this._octetBuffer, 0, b, 0, this._octetBufferIndex);
        this._octetBuffer = b;
    }

    private void _flush() throws IOException {
        if (this._octetBufferIndex > 0) {
            this._s.write(this._octetBuffer, 0, this._octetBufferIndex);
            this._octetBufferIndex = 0;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/Encoder$EncodingBufferOutputStream.class */
    public class EncodingBufferOutputStream extends OutputStream {
        private EncodingBufferOutputStream() {
        }

        @Override // java.io.OutputStream
        public void write(int b) throws IOException {
            if (Encoder.this._encodingBufferIndex < Encoder.this._encodingBuffer.length) {
                Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = (byte) b;
                return;
            }
            byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, Encoder.this._encodingBufferIndex)];
            System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
            Encoder.this._encodingBuffer = newbuf;
            Encoder.this._encodingBuffer[Encoder.access$108(Encoder.this)] = (byte) b;
        }

        @Override // java.io.OutputStream
        public void write(byte[] b, int off, int len) throws IOException {
            if (off < 0 || off > b.length || len < 0 || off + len > b.length || off + len < 0) {
                throw new IndexOutOfBoundsException();
            }
            if (len != 0) {
                int newoffset = Encoder.this._encodingBufferIndex + len;
                if (newoffset > Encoder.this._encodingBuffer.length) {
                    byte[] newbuf = new byte[Math.max(Encoder.this._encodingBuffer.length << 1, newoffset)];
                    System.arraycopy(Encoder.this._encodingBuffer, 0, newbuf, 0, Encoder.this._encodingBufferIndex);
                    Encoder.this._encodingBuffer = newbuf;
                }
                System.arraycopy(b, off, Encoder.this._encodingBuffer, Encoder.this._encodingBufferIndex, len);
                Encoder.this._encodingBufferIndex = newoffset;
            }
        }

        public int getLength() {
            return Encoder.this._encodingBufferIndex;
        }

        public void reset() {
            Encoder.this._encodingBufferIndex = 0;
        }
    }

    protected final int encodeUTF8String(String s) throws IOException {
        int length = s.length();
        if (length < this._charBuffer.length) {
            s.getChars(0, length, this._charBuffer, 0);
            return encodeUTF8String(this._charBuffer, 0, length);
        }
        char[] ch = s.toCharArray();
        return encodeUTF8String(ch, 0, length);
    }

    private void ensureEncodingBufferSizeForUtf8String(int length) {
        int newLength = 4 * length;
        if (this._encodingBuffer.length < newLength) {
            this._encodingBuffer = new byte[newLength];
        }
    }

    protected final int encodeUTF8String(char[] ch, int offset, int length) throws IOException {
        int bpos = 0;
        ensureEncodingBufferSizeForUtf8String(length);
        int end = offset + length;
        while (end != offset) {
            int i = offset;
            offset++;
            char c = ch[i];
            if (c < 128) {
                int i2 = bpos;
                bpos++;
                this._encodingBuffer[i2] = (byte) c;
            } else if (c < 2048) {
                int i3 = bpos;
                int bpos2 = bpos + 1;
                this._encodingBuffer[i3] = (byte) (192 | (c >> 6));
                bpos = bpos2 + 1;
                this._encodingBuffer[bpos2] = (byte) (128 | (c & '?'));
            } else if (c <= 65535) {
                if (!XMLChar.isHighSurrogate(c) && !XMLChar.isLowSurrogate(c)) {
                    int i4 = bpos;
                    int bpos3 = bpos + 1;
                    this._encodingBuffer[i4] = (byte) (224 | (c >> '\f'));
                    int bpos4 = bpos3 + 1;
                    this._encodingBuffer[bpos3] = (byte) (128 | ((c >> 6) & 63));
                    bpos = bpos4 + 1;
                    this._encodingBuffer[bpos4] = (byte) (128 | (c & '?'));
                } else {
                    encodeCharacterAsUtf8FourByte(c, ch, offset, end, bpos);
                    bpos += 4;
                    offset++;
                }
            }
        }
        return bpos;
    }

    private void encodeCharacterAsUtf8FourByte(int c, char[] ch, int chpos, int chend, int bpos) throws IOException {
        if (chpos == chend) {
            throw new IOException("");
        }
        char d = ch[chpos];
        if (!XMLChar.isLowSurrogate(d)) {
            throw new IOException("");
        }
        int uc = (((c & Opcodes.OP_NEW_INSTANCE_JUMBO) << 10) | (d & 1023)) + 65536;
        if (uc < 0 || uc >= 2097152) {
            throw new IOException("");
        }
        int bpos2 = bpos + 1;
        this._encodingBuffer[bpos] = (byte) (240 | (uc >> 18));
        int bpos3 = bpos2 + 1;
        this._encodingBuffer[bpos2] = (byte) (128 | ((uc >> 12) & 63));
        int bpos4 = bpos3 + 1;
        this._encodingBuffer[bpos3] = (byte) (128 | ((uc >> 6) & 63));
        int i = bpos4 + 1;
        this._encodingBuffer[bpos4] = (byte) (128 | (uc & 63));
    }

    protected final int encodeUtf16String(String s) throws IOException {
        int length = s.length();
        if (length < this._charBuffer.length) {
            s.getChars(0, length, this._charBuffer, 0);
            return encodeUtf16String(this._charBuffer, 0, length);
        }
        char[] ch = s.toCharArray();
        return encodeUtf16String(ch, 0, length);
    }

    private void ensureEncodingBufferSizeForUtf16String(int length) {
        int newLength = 2 * length;
        if (this._encodingBuffer.length < newLength) {
            this._encodingBuffer = new byte[newLength];
        }
    }

    protected final int encodeUtf16String(char[] ch, int offset, int length) throws IOException {
        int byteLength = 0;
        ensureEncodingBufferSizeForUtf16String(length);
        int n = offset + length;
        for (int i = offset; i < n; i++) {
            char c = ch[i];
            int i2 = byteLength;
            int byteLength2 = byteLength + 1;
            this._encodingBuffer[i2] = (byte) (c >> '\b');
            byteLength = byteLength2 + 1;
            this._encodingBuffer[byteLength2] = (byte) (c & 255);
        }
        return byteLength;
    }

    public static String getPrefixFromQualifiedName(String qName) {
        int i = qName.indexOf(58);
        String prefix = "";
        if (i != -1) {
            prefix = qName.substring(0, i);
        }
        return prefix;
    }

    public static boolean isWhiteSpace(char[] ch, int start, int length) {
        if (XMLChar.isSpace(ch[start])) {
            int end = start + length;
            do {
                start++;
                if (start >= end) {
                    break;
                }
            } while (XMLChar.isSpace(ch[start]));
            return start == end;
        }
        return false;
    }

    public static boolean isWhiteSpace(String s) {
        if (XMLChar.isSpace(s.charAt(0))) {
            int end = s.length();
            int start = 1;
            while (start < end) {
                int i = start;
                start++;
                if (!XMLChar.isSpace(s.charAt(i))) {
                    break;
                }
            }
            return start == end;
        }
        return false;
    }
}
