package com.sun.xml.fastinfoset.sax;

import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.fastinfoset.QualifiedName;
import com.sun.xml.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.fastinfoset.util.StringIntMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.jvnet.fastinfoset.FastInfosetException;
import org.jvnet.fastinfoset.RestrictedAlphabet;
import org.jvnet.fastinfoset.sax.EncodingAlgorithmAttributes;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/sax/SAXDocumentSerializerWithPrefixMapping.class */
public class SAXDocumentSerializerWithPrefixMapping extends SAXDocumentSerializer {
    protected Map _namespaceToPrefixMapping;
    protected Map _prefixToPrefixMapping;
    protected String _lastCheckedNamespace;
    protected String _lastCheckedPrefix;
    protected StringIntMap _declaredNamespaces;

    public SAXDocumentSerializerWithPrefixMapping(Map namespaceToPrefixMapping) {
        super(true);
        this._namespaceToPrefixMapping = new HashMap(namespaceToPrefixMapping);
        this._prefixToPrefixMapping = new HashMap();
        this._namespaceToPrefixMapping.put("", "");
        this._namespaceToPrefixMapping.put("http://www.w3.org/XML/1998/namespace", EncodingConstants.XML_NAMESPACE_PREFIX);
        this._declaredNamespaces = new StringIntMap(4);
    }

    @Override // com.sun.xml.fastinfoset.sax.SAXDocumentSerializer, org.xml.sax.helpers.DefaultHandler, org.xml.sax.ContentHandler
    public final void startPrefixMapping(String prefix, String uri) throws SAXException {
        try {
            if (!this._elementHasNamespaces) {
                encodeTermination();
                mark();
                this._elementHasNamespaces = true;
                write(56);
                this._declaredNamespaces.clear();
                this._declaredNamespaces.obtainIndex(uri);
            } else if (this._declaredNamespaces.obtainIndex(uri) != -1) {
                String p = getPrefix(uri);
                if (p != null) {
                    this._prefixToPrefixMapping.put(prefix, p);
                    return;
                }
                return;
            }
            String p2 = getPrefix(uri);
            if (p2 != null) {
                encodeNamespaceAttribute(p2, uri);
                this._prefixToPrefixMapping.put(prefix, p2);
            } else {
                putPrefix(uri, prefix);
                encodeNamespaceAttribute(prefix, uri);
            }
        } catch (IOException e) {
            throw new SAXException("startElement", e);
        }
    }

    @Override // com.sun.xml.fastinfoset.sax.SAXDocumentSerializer
    protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            if (encodeElementMapEntry(entry, namespaceURI)) {
                return;
            }
            if (this._v.elementName.isQNameFromReadOnlyMap(entry._value[0])) {
                entry = this._v.elementName.obtainDynamicEntry(localName);
                if (entry._valueIndex > 0 && encodeElementMapEntry(entry, namespaceURI)) {
                    return;
                }
            }
        }
        encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
    }

    protected boolean encodeElementMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException {
        QualifiedName[] names = entry._value;
        for (int i = 0; i < entry._valueIndex; i++) {
            if (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName)) {
                encodeNonZeroIntegerOnThirdBit(names[i].index);
                return true;
            }
        }
        return false;
    }

    @Override // com.sun.xml.fastinfoset.sax.SAXDocumentSerializer
    protected final void encodeAttributes(Attributes atts) throws IOException, FastInfosetException {
        if (atts instanceof EncodingAlgorithmAttributes) {
            EncodingAlgorithmAttributes eAtts = (EncodingAlgorithmAttributes) atts;
            for (int i = 0; i < eAtts.getLength(); i++) {
                String uri = atts.getURI(i);
                if (encodeAttribute(uri, atts.getQName(i), atts.getLocalName(i))) {
                    Object data = eAtts.getAlgorithmData(i);
                    if (data == null) {
                        String value = eAtts.getValue(i);
                        boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                        boolean mustToBeAddedToTable = eAtts.getToIndex(i);
                        String alphabet = eAtts.getAlpababet(i);
                        if (alphabet == null) {
                            if (uri == "http://www.w3.org/2001/XMLSchema-instance" || uri.equals("http://www.w3.org/2001/XMLSchema-instance")) {
                                value = convertQName(value);
                            }
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.DATE_TIME_CHARACTERS) {
                            encodeDateTimeNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
                        } else if (alphabet == RestrictedAlphabet.NUMERIC_CHARACTERS) {
                            encodeNumericNonIdentifyingStringOnFirstBit(value, addToTable, mustToBeAddedToTable);
                        } else {
                            encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, mustToBeAddedToTable);
                        }
                    } else {
                        encodeNonIdentifyingStringOnFirstBit(eAtts.getAlgorithmURI(i), eAtts.getAlgorithmIndex(i), data);
                    }
                }
            }
        } else {
            for (int i2 = 0; i2 < atts.getLength(); i2++) {
                String uri2 = atts.getURI(i2);
                if (encodeAttribute(atts.getURI(i2), atts.getQName(i2), atts.getLocalName(i2))) {
                    String value2 = atts.getValue(i2);
                    boolean addToTable2 = isAttributeValueLengthMatchesLimit(value2.length());
                    if (uri2 == "http://www.w3.org/2001/XMLSchema-instance" || uri2.equals("http://www.w3.org/2001/XMLSchema-instance")) {
                        value2 = convertQName(value2);
                    }
                    encodeNonIdentifyingStringOnFirstBit(value2, this._v.attributeValue, addToTable2, false);
                }
            }
        }
        this._b = 240;
        this._terminate = true;
    }

    private String convertQName(String qName) {
        int i = qName.indexOf(58);
        String prefix = "";
        String localName = qName;
        if (i != -1) {
            prefix = qName.substring(0, i);
            localName = qName.substring(i + 1);
        }
        String p = (String) this._prefixToPrefixMapping.get(prefix);
        if (p != null) {
            if (p.length() == 0) {
                return localName;
            }
            return p + ":" + localName;
        }
        return qName;
    }

    @Override // com.sun.xml.fastinfoset.sax.SAXDocumentSerializer
    protected final boolean encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(localName);
        if (entry._valueIndex > 0) {
            if (encodeAttributeMapEntry(entry, namespaceURI)) {
                return true;
            }
            if (this._v.attributeName.isQNameFromReadOnlyMap(entry._value[0])) {
                entry = this._v.attributeName.obtainDynamicEntry(localName);
                if (entry._valueIndex > 0 && encodeAttributeMapEntry(entry, namespaceURI)) {
                    return true;
                }
            }
        }
        return encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefix(namespaceURI), localName, entry);
    }

    protected boolean encodeAttributeMapEntry(LocalNameQualifiedNamesMap.Entry entry, String namespaceURI) throws IOException {
        QualifiedName[] names = entry._value;
        for (int i = 0; i < entry._valueIndex; i++) {
            if (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName)) {
                encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
                return true;
            }
        }
        return false;
    }

    protected final String getPrefix(String namespaceURI) {
        if (this._lastCheckedNamespace == namespaceURI) {
            return this._lastCheckedPrefix;
        }
        this._lastCheckedNamespace = namespaceURI;
        String str = (String) this._namespaceToPrefixMapping.get(namespaceURI);
        this._lastCheckedPrefix = str;
        return str;
    }

    protected final void putPrefix(String namespaceURI, String prefix) {
        this._namespaceToPrefixMapping.put(namespaceURI, prefix);
        this._lastCheckedNamespace = namespaceURI;
        this._lastCheckedPrefix = prefix;
    }
}
