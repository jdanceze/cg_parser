package com.sun.xml.fastinfoset.dom;

import com.sun.xml.fastinfoset.CommonResourceBundle;
import com.sun.xml.fastinfoset.Decoder;
import com.sun.xml.fastinfoset.DecoderStateTables;
import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.fastinfoset.QualifiedName;
import com.sun.xml.fastinfoset.algorithm.BuiltInEncodingAlgorithmFactory;
import com.sun.xml.fastinfoset.util.CharArray;
import com.sun.xml.fastinfoset.util.CharArrayString;
import com.sun.xml.fastinfoset.util.PrefixArray;
import java.io.IOException;
import java.io.InputStream;
import org.jvnet.fastinfoset.EncodingAlgorithm;
import org.jvnet.fastinfoset.EncodingAlgorithmException;
import org.jvnet.fastinfoset.FastInfosetException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/dom/DOMDocumentParser.class */
public class DOMDocumentParser extends Decoder {
    protected Document _document;
    protected Node _currentNode;
    protected Element _currentElement;
    protected int _namespaceAttributesIndex;
    protected int _namespacePrefixesIndex;
    protected Attr[] _namespaceAttributes = new Attr[16];
    protected int[] _namespacePrefixes = new int[16];

    public void parse(Document d, InputStream s) throws FastInfosetException, IOException {
        this._document = d;
        this._currentNode = d;
        this._namespaceAttributesIndex = 0;
        parse(s);
    }

    protected final void parse(InputStream s) throws FastInfosetException, IOException {
        setInputStream(s);
        parse();
    }

    protected void resetOnError() {
        this._namespacePrefixesIndex = 0;
        if (this._v == null) {
            this._prefixTable.clearCompletely();
        }
        this._duplicateAttributeVerifier.clear();
    }

    protected final void parse() throws FastInfosetException, IOException {
        try {
            reset();
            decodeHeader();
            processDII();
        } catch (IOException e) {
            resetOnError();
            throw e;
        } catch (RuntimeException e2) {
            resetOnError();
            throw new FastInfosetException(e2);
        } catch (FastInfosetException e3) {
            resetOnError();
            throw e3;
        }
    }

    protected final void processDII() throws FastInfosetException, IOException {
        this._b = read();
        if (this._b > 0) {
            processDIIOptionalProperties();
        }
        boolean firstElementHasOccured = false;
        boolean documentTypeDeclarationOccured = false;
        while (true) {
            if (!this._terminate || !firstElementHasOccured) {
                this._b = read();
                switch (DecoderStateTables.DII(this._b)) {
                    case 0:
                        processEII(this._elementNameTable._array[this._b], false);
                        firstElementHasOccured = true;
                        continue;
                    case 1:
                        processEII(this._elementNameTable._array[this._b & 31], true);
                        firstElementHasOccured = true;
                        continue;
                    case 2:
                        processEII(decodeEIIIndexMedium(), (this._b & 64) > 0);
                        firstElementHasOccured = true;
                        continue;
                    case 3:
                        processEII(decodeEIIIndexLarge(), (this._b & 64) > 0);
                        firstElementHasOccured = true;
                        continue;
                    case 4:
                        processEIIWithNamespaces();
                        firstElementHasOccured = true;
                        continue;
                    case 5:
                        QualifiedName qn = processLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, (this._b & 64) > 0);
                        firstElementHasOccured = true;
                        continue;
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11:
                    case 12:
                    case 13:
                    case 14:
                    case 15:
                    case 16:
                    case 17:
                    case 21:
                    default:
                        throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
                    case 18:
                        processCommentII();
                        continue;
                    case 19:
                        processProcessingII();
                        continue;
                    case 20:
                        if (documentTypeDeclarationOccured) {
                            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.secondOccurenceOfDTDII"));
                        }
                        documentTypeDeclarationOccured = true;
                        String decodeIdentifyingNonEmptyStringOnFirstBit = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
                        String decodeIdentifyingNonEmptyStringOnFirstBit2 = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
                        this._b = read();
                        while (this._b == 225) {
                            switch (decodeNonIdentifyingStringOnFirstBit()) {
                                case 0:
                                    if (!this._addToTable) {
                                        break;
                                    } else {
                                        this._v.otherString.add(new CharArray(this._charBuffer, 0, this._charBufferLength, true));
                                        break;
                                    }
                                case 2:
                                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
                            }
                            this._b = read();
                        }
                        if ((this._b & 240) != 240) {
                            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.processingInstructionIIsNotTerminatedCorrectly"));
                        }
                        if (this._b == 255) {
                            this._terminate = true;
                        }
                        this._notations.clear();
                        this._unparsedEntities.clear();
                        continue;
                        break;
                    case 22:
                        break;
                    case 23:
                        this._doubleTerminate = true;
                        break;
                }
                this._terminate = true;
            } else {
                while (!this._terminate) {
                    this._b = read();
                    switch (DecoderStateTables.DII(this._b)) {
                        case 18:
                            processCommentII();
                            continue;
                        case 19:
                            processProcessingII();
                            continue;
                        case 20:
                        case 21:
                        default:
                            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingDII"));
                        case 22:
                            break;
                        case 23:
                            this._doubleTerminate = true;
                            break;
                    }
                    this._terminate = true;
                }
                return;
            }
        }
    }

    protected final void processDIIOptionalProperties() throws FastInfosetException, IOException {
        if (this._b == 32) {
            decodeInitialVocabulary();
            return;
        }
        if ((this._b & 64) > 0) {
            decodeAdditionalData();
        }
        if ((this._b & 32) > 0) {
            decodeInitialVocabulary();
        }
        if ((this._b & 16) > 0) {
            decodeNotations();
        }
        if ((this._b & 8) > 0) {
            decodeUnparsedEntities();
        }
        if ((this._b & 4) > 0) {
            decodeCharacterEncodingScheme();
        }
        if ((this._b & 2) > 0) {
            read();
        }
        if ((this._b & 1) > 0) {
            decodeVersion();
        }
    }

    protected final void processEII(QualifiedName name, boolean hasAttributes) throws FastInfosetException, IOException {
        if (this._prefixTable._currentInScope[name.prefixIndex] != name.namespaceNameIndex) {
            throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qnameOfEIINotInScope"));
        }
        Node parentCurrentNode = this._currentNode;
        Element createElement = createElement(name.namespaceName, name.qName, name.localName);
        this._currentElement = createElement;
        this._currentNode = createElement;
        if (this._namespaceAttributesIndex > 0) {
            for (int i = 0; i < this._namespaceAttributesIndex; i++) {
                this._currentElement.setAttributeNode(this._namespaceAttributes[i]);
                this._namespaceAttributes[i] = null;
            }
            this._namespaceAttributesIndex = 0;
        }
        if (hasAttributes) {
            processAIIs();
        }
        parentCurrentNode.appendChild(this._currentElement);
        while (!this._terminate) {
            this._b = read();
            switch (DecoderStateTables.EII(this._b)) {
                case 0:
                    processEII(this._elementNameTable._array[this._b], false);
                    continue;
                case 1:
                    processEII(this._elementNameTable._array[this._b & 31], true);
                    continue;
                case 2:
                    processEII(decodeEIIIndexMedium(), (this._b & 64) > 0);
                    continue;
                case 3:
                    processEII(decodeEIIIndexLarge(), (this._b & 64) > 0);
                    continue;
                case 4:
                    processEIIWithNamespaces();
                    continue;
                case 5:
                    QualifiedName qn = processLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                    this._elementNameTable.add(qn);
                    processEII(qn, (this._b & 64) > 0);
                    continue;
                case 6:
                    this._octetBufferLength = (this._b & 1) + 1;
                    appendOrCreateTextData(processUtf8CharacterString());
                    continue;
                case 7:
                    this._octetBufferLength = read() + 3;
                    appendOrCreateTextData(processUtf8CharacterString());
                    continue;
                case 8:
                    this._octetBufferLength = (read() << 24) | (read() << 16) | (read() << 8) | read();
                    this._octetBufferLength += 259;
                    appendOrCreateTextData(processUtf8CharacterString());
                    continue;
                case 9:
                    this._octetBufferLength = (this._b & 1) + 1;
                    String v = decodeUtf16StringAsString();
                    if ((this._b & 16) > 0) {
                        this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                    }
                    appendOrCreateTextData(v);
                    continue;
                case 10:
                    this._octetBufferLength = read() + 3;
                    String v2 = decodeUtf16StringAsString();
                    if ((this._b & 16) > 0) {
                        this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                    }
                    appendOrCreateTextData(v2);
                    continue;
                case 11:
                    this._octetBufferLength = (read() << 24) | (read() << 16) | (read() << 8) | read();
                    this._octetBufferLength += 259;
                    String v3 = decodeUtf16StringAsString();
                    if ((this._b & 16) > 0) {
                        this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                    }
                    appendOrCreateTextData(v3);
                    continue;
                case 12:
                    boolean addToTable = (this._b & 16) > 0;
                    this._identifier = (this._b & 2) << 6;
                    this._b = read();
                    this._identifier |= (this._b & 252) >> 2;
                    decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                    String v4 = decodeRestrictedAlphabetAsString();
                    if (addToTable) {
                        this._characterContentChunkTable.add(this._charBuffer, this._charBufferLength);
                    }
                    appendOrCreateTextData(v4);
                    continue;
                case 13:
                    boolean addToTable2 = (this._b & 16) > 0;
                    this._identifier = (this._b & 2) << 6;
                    this._b = read();
                    this._identifier |= (this._b & 252) >> 2;
                    decodeOctetsOnSeventhBitOfNonIdentifyingStringOnThirdBit(this._b);
                    String s = convertEncodingAlgorithmDataToCharacters(false);
                    if (addToTable2) {
                        this._characterContentChunkTable.add(s.toCharArray(), s.length());
                    }
                    appendOrCreateTextData(s);
                    continue;
                case 14:
                    String s2 = this._characterContentChunkTable.getString(this._b & 15);
                    appendOrCreateTextData(s2);
                    continue;
                case 15:
                    int index = (((this._b & 3) << 8) | read()) + 16;
                    String s3 = this._characterContentChunkTable.getString(index);
                    appendOrCreateTextData(s3);
                    continue;
                case 16:
                    int index2 = ((this._b & 3) << 16) | (read() << 8) | read();
                    String s4 = this._characterContentChunkTable.getString(index2 + 1040);
                    appendOrCreateTextData(s4);
                    continue;
                case 17:
                    int index3 = (read() << 16) | (read() << 8) | read();
                    String s5 = this._characterContentChunkTable.getString(index3 + EncodingConstants.INTEGER_4TH_BIT_LARGE_LIMIT);
                    appendOrCreateTextData(s5);
                    continue;
                case 18:
                    processCommentII();
                    continue;
                case 19:
                    processProcessingII();
                    continue;
                case 20:
                default:
                    throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEII"));
                case 21:
                    decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
                    String decodeIdentifyingNonEmptyStringOnFirstBit = (this._b & 2) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
                    String decodeIdentifyingNonEmptyStringOnFirstBit2 = (this._b & 1) > 0 ? decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherURI) : null;
                    continue;
                case 22:
                    break;
                case 23:
                    this._doubleTerminate = true;
                    break;
            }
            this._terminate = true;
        }
        this._terminate = this._doubleTerminate;
        this._doubleTerminate = false;
        this._currentNode = parentCurrentNode;
    }

    private void appendOrCreateTextData(String textData) {
        Node lastChild = this._currentNode.getLastChild();
        if (lastChild instanceof Text) {
            ((Text) lastChild).appendData(textData);
        } else {
            this._currentNode.appendChild(this._document.createTextNode(textData));
        }
    }

    private final String processUtf8CharacterString() throws FastInfosetException, IOException {
        if ((this._b & 16) > 0) {
            this._characterContentChunkTable.ensureSize(this._octetBufferLength);
            int charactersOffset = this._characterContentChunkTable._arrayIndex;
            decodeUtf8StringAsCharBuffer(this._characterContentChunkTable._array, charactersOffset);
            this._characterContentChunkTable.add(this._charBufferLength);
            return this._characterContentChunkTable.getString(this._characterContentChunkTable._cachedIndex);
        }
        decodeUtf8StringAsCharBuffer();
        return new String(this._charBuffer, 0, this._charBufferLength);
    }

    protected final void processEIIWithNamespaces() throws FastInfosetException, IOException {
        boolean hasAttributes = (this._b & 64) > 0;
        PrefixArray prefixArray = this._prefixTable;
        int i = prefixArray._declarationId + 1;
        prefixArray._declarationId = i;
        if (i == Integer.MAX_VALUE) {
            this._prefixTable.clearDeclarationIds();
        }
        Attr a = null;
        int start = this._namespacePrefixesIndex;
        int read = read();
        while (true) {
            int b = read;
            if ((b & 252) == 204) {
                if (this._namespaceAttributesIndex == this._namespaceAttributes.length) {
                    Attr[] newNamespaceAttributes = new Attr[((this._namespaceAttributesIndex * 3) / 2) + 1];
                    System.arraycopy(this._namespaceAttributes, 0, newNamespaceAttributes, 0, this._namespaceAttributesIndex);
                    this._namespaceAttributes = newNamespaceAttributes;
                }
                if (this._namespacePrefixesIndex == this._namespacePrefixes.length) {
                    int[] namespaceAIIs = new int[((this._namespacePrefixesIndex * 3) / 2) + 1];
                    System.arraycopy(this._namespacePrefixes, 0, namespaceAIIs, 0, this._namespacePrefixesIndex);
                    this._namespacePrefixes = namespaceAIIs;
                }
                switch (b & 3) {
                    case 0:
                        a = createAttribute(EncodingConstants.XMLNS_NAMESPACE_NAME, EncodingConstants.XMLNS_NAMESPACE_PREFIX, EncodingConstants.XMLNS_NAMESPACE_PREFIX);
                        a.setValue("");
                        int[] iArr = this._namespacePrefixes;
                        int i2 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i2 + 1;
                        iArr[i2] = -1;
                        this._namespaceNameIndex = -1;
                        this._prefixIndex = -1;
                        break;
                    case 1:
                        a = createAttribute(EncodingConstants.XMLNS_NAMESPACE_NAME, EncodingConstants.XMLNS_NAMESPACE_PREFIX, EncodingConstants.XMLNS_NAMESPACE_PREFIX);
                        a.setValue(decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(false));
                        int[] iArr2 = this._namespacePrefixes;
                        int i3 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i3 + 1;
                        iArr2[i3] = -1;
                        this._prefixIndex = -1;
                        break;
                    case 2:
                        String prefix = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(false);
                        a = createAttribute(EncodingConstants.XMLNS_NAMESPACE_NAME, createQualifiedNameString(prefix), prefix);
                        a.setValue("");
                        this._namespaceNameIndex = -1;
                        int[] iArr3 = this._namespacePrefixes;
                        int i4 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i4 + 1;
                        iArr3[i4] = this._prefixIndex;
                        break;
                    case 3:
                        String prefix2 = decodeIdentifyingNonEmptyStringOnFirstBitAsPrefix(true);
                        a = createAttribute(EncodingConstants.XMLNS_NAMESPACE_NAME, createQualifiedNameString(prefix2), prefix2);
                        a.setValue(decodeIdentifyingNonEmptyStringOnFirstBitAsNamespaceName(true));
                        int[] iArr4 = this._namespacePrefixes;
                        int i5 = this._namespacePrefixesIndex;
                        this._namespacePrefixesIndex = i5 + 1;
                        iArr4[i5] = this._prefixIndex;
                        break;
                }
                this._prefixTable.pushScope(this._prefixIndex, this._namespaceNameIndex);
                Attr[] attrArr = this._namespaceAttributes;
                int i6 = this._namespaceAttributesIndex;
                this._namespaceAttributesIndex = i6 + 1;
                attrArr[i6] = a;
                read = read();
            } else if (b != 240) {
                throw new IOException(CommonResourceBundle.getInstance().getString("message.EIInamespaceNameNotTerminatedCorrectly"));
            } else {
                int end = this._namespacePrefixesIndex;
                this._b = read();
                switch (DecoderStateTables.EII(this._b)) {
                    case 0:
                        processEII(this._elementNameTable._array[this._b], hasAttributes);
                        break;
                    case 1:
                    case 4:
                    default:
                        throw new IOException(CommonResourceBundle.getInstance().getString("message.IllegalStateDecodingEIIAfterAIIs"));
                    case 2:
                        processEII(decodeEIIIndexMedium(), hasAttributes);
                        break;
                    case 3:
                        processEII(decodeEIIIndexLarge(), hasAttributes);
                        break;
                    case 5:
                        QualifiedName qn = processLiteralQualifiedName(this._b & 3, this._elementNameTable.getNext());
                        this._elementNameTable.add(qn);
                        processEII(qn, hasAttributes);
                        break;
                }
                for (int i7 = start; i7 < end; i7++) {
                    this._prefixTable.popScope(this._namespacePrefixes[i7]);
                }
                this._namespacePrefixesIndex = start;
                return;
            }
        }
    }

    protected final QualifiedName processLiteralQualifiedName(int state, QualifiedName q) throws FastInfosetException, IOException {
        if (q == null) {
            q = new QualifiedName();
        }
        switch (state) {
            case 0:
                return q.set((String) null, (String) null, decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, (char[]) null);
            case 1:
                return q.set((String) null, decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, (char[]) null);
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
            case 3:
                return q.set(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
        }
    }

    protected final QualifiedName processLiteralQualifiedName(int state) throws FastInfosetException, IOException {
        switch (state) {
            case 0:
                return new QualifiedName((String) null, (String) null, decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, -1, this._identifier, (char[]) null);
            case 1:
                return new QualifiedName((String) null, decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(false), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), -1, this._namespaceNameIndex, this._identifier, (char[]) null);
            case 2:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.qNameMissingNamespaceName"));
            case 3:
                return new QualifiedName(decodeIdentifyingNonEmptyStringIndexOnFirstBitAsPrefix(true), decodeIdentifyingNonEmptyStringIndexOnFirstBitAsNamespaceName(true), decodeIdentifyingNonEmptyStringOnFirstBit(this._v.localName), this._prefixIndex, this._namespaceNameIndex, this._identifier, this._charBuffer);
            default:
                throw new FastInfosetException(CommonResourceBundle.getInstance().getString("message.decodingEII"));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:17:0x00e1  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x016c  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x01ae  */
    /* JADX WARN: Removed duplicated region for block: B:39:0x01f1  */
    /* JADX WARN: Removed duplicated region for block: B:47:0x0251  */
    /* JADX WARN: Removed duplicated region for block: B:55:0x0293  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x02d6  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x0336  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0392  */
    /* JADX WARN: Removed duplicated region for block: B:87:0x03ef  */
    /* JADX WARN: Removed duplicated region for block: B:88:0x0413  */
    /* JADX WARN: Removed duplicated region for block: B:89:0x0446  */
    /* JADX WARN: Removed duplicated region for block: B:90:0x0482  */
    /* JADX WARN: Removed duplicated region for block: B:99:0x049a A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    protected final void processAIIs() throws org.jvnet.fastinfoset.FastInfosetException, java.io.IOException {
        /*
            Method dump skipped, instructions count: 1229
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.fastinfoset.dom.DOMDocumentParser.processAIIs():void");
    }

    protected final void processCommentII() throws FastInfosetException, IOException {
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                String s = new String(this._charBuffer, 0, this._charBufferLength);
                if (this._addToTable) {
                    this._v.otherString.add(new CharArrayString(s, false));
                }
                this._currentNode.appendChild(this._document.createComment(s));
                return;
            case 1:
                this._currentNode.appendChild(this._document.createComment(this._v.otherString.get(this._integer).toString()));
                return;
            case 2:
                throw new IOException(CommonResourceBundle.getInstance().getString("message.commentIIAlgorithmNotSupported"));
            case 3:
                this._currentNode.appendChild(this._document.createComment(""));
                return;
            default:
                return;
        }
    }

    protected final void processProcessingII() throws FastInfosetException, IOException {
        String target = decodeIdentifyingNonEmptyStringOnFirstBit(this._v.otherNCName);
        switch (decodeNonIdentifyingStringOnFirstBit()) {
            case 0:
                String data = new String(this._charBuffer, 0, this._charBufferLength);
                if (this._addToTable) {
                    this._v.otherString.add(new CharArrayString(data, false));
                }
                this._currentNode.appendChild(this._document.createProcessingInstruction(target, data));
                return;
            case 1:
                this._currentNode.appendChild(this._document.createProcessingInstruction(target, this._v.otherString.get(this._integer).toString()));
                return;
            case 2:
                throw new IOException(CommonResourceBundle.getInstance().getString("message.processingIIWithEncodingAlgorithm"));
            case 3:
                this._currentNode.appendChild(this._document.createProcessingInstruction(target, ""));
                return;
            default:
                return;
        }
    }

    protected Element createElement(String namespaceName, String qName, String localName) {
        return this._document.createElementNS(namespaceName, qName);
    }

    protected Attr createAttribute(String namespaceName, String qName, String localName) {
        return this._document.createAttributeNS(namespaceName, qName);
    }

    protected String convertEncodingAlgorithmDataToCharacters(boolean isAttributeValue) throws FastInfosetException, IOException {
        StringBuffer buffer = new StringBuffer();
        if (this._identifier < 9) {
            Object array = BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
            BuiltInEncodingAlgorithmFactory.getAlgorithm(this._identifier).convertToCharacters(array, buffer);
        } else if (this._identifier == 9) {
            if (!isAttributeValue) {
                this._octetBufferOffset -= this._octetBufferLength;
                return decodeUtf8StringAsString();
            }
            throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.CDATAAlgorithmNotSupported"));
        } else if (this._identifier >= 32) {
            String URI = this._v.encodingAlgorithm.get(this._identifier - 32);
            EncodingAlgorithm ea = (EncodingAlgorithm) this._registeredEncodingAlgorithms.get(URI);
            if (ea != null) {
                Object data = ea.decodeFromBytes(this._octetBuffer, this._octetBufferStart, this._octetBufferLength);
                ea.convertToCharacters(data, buffer);
            } else {
                throw new EncodingAlgorithmException(CommonResourceBundle.getInstance().getString("message.algorithmDataCannotBeReported"));
            }
        }
        return buffer.toString();
    }
}
