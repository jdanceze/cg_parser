package com.sun.xml.fastinfoset.dom;

import com.sun.xml.fastinfoset.Encoder;
import com.sun.xml.fastinfoset.EncodingConstants;
import com.sun.xml.fastinfoset.QualifiedName;
import com.sun.xml.fastinfoset.util.LocalNameQualifiedNamesMap;
import com.sun.xml.fastinfoset.util.NamespaceContextImplementation;
import java.io.IOException;
import org.jvnet.fastinfoset.FastInfosetException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/dom/DOMDocumentSerializer.class */
public class DOMDocumentSerializer extends Encoder {
    protected NamespaceContextImplementation _namespaceScopeContext = new NamespaceContextImplementation();
    protected Node[] _attributes = new Node[32];

    public final void serialize(Node n) throws IOException {
        switch (n.getNodeType()) {
            case 1:
                serializeElementAsDocument(n);
                return;
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
            default:
                return;
            case 7:
                serializeProcessingInstruction(n);
                return;
            case 8:
                serializeComment(n);
                return;
            case 9:
                serialize((Document) n);
                return;
        }
    }

    public final void serialize(Document d) throws IOException {
        reset();
        encodeHeader(false);
        encodeInitialVocabulary();
        NodeList nl = d.getChildNodes();
        for (int i = 0; i < nl.getLength(); i++) {
            Node n = nl.item(i);
            switch (n.getNodeType()) {
                case 1:
                    serializeElement(n);
                    break;
                case 7:
                    serializeProcessingInstruction(n);
                    break;
                case 8:
                    serializeComment(n);
                    break;
            }
        }
        encodeDocumentTermination();
    }

    protected final void serializeElementAsDocument(Node e) throws IOException {
        reset();
        encodeHeader(false);
        encodeInitialVocabulary();
        serializeElement(e);
        encodeDocumentTermination();
    }

    protected final void serializeElement(Node e) throws IOException {
        encodeTermination();
        int attributesSize = 0;
        this._namespaceScopeContext.pushContext();
        if (e.hasAttributes()) {
            NamedNodeMap nnm = e.getAttributes();
            for (int i = 0; i < nnm.getLength(); i++) {
                Node a = nnm.item(i);
                String namespaceURI = a.getNamespaceURI();
                if (namespaceURI != null && namespaceURI.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
                    String attrPrefix = a.getLocalName();
                    String attrNamespace = a.getNodeValue();
                    this._namespaceScopeContext.declarePrefix((attrPrefix == EncodingConstants.XMLNS_NAMESPACE_PREFIX || attrPrefix.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) ? "" : "", attrNamespace);
                } else {
                    if (attributesSize == this._attributes.length) {
                        Node[] attributes = new Node[((attributesSize * 3) / 2) + 1];
                        System.arraycopy(this._attributes, 0, attributes, 0, attributesSize);
                        this._attributes = attributes;
                    }
                    int i2 = attributesSize;
                    attributesSize++;
                    this._attributes[i2] = a;
                    String attrNamespaceURI = a.getNamespaceURI();
                    String attrPrefix2 = a.getPrefix();
                    if (attrPrefix2 != null && !this._namespaceScopeContext.getNamespaceURI(attrPrefix2).equals(attrNamespaceURI)) {
                        this._namespaceScopeContext.declarePrefix(attrPrefix2, attrNamespaceURI);
                    }
                }
            }
        }
        String elementNamespaceURI = e.getNamespaceURI();
        String elementPrefix = e.getPrefix();
        if (elementPrefix == null) {
            elementPrefix = "";
        }
        if (elementNamespaceURI != null && !this._namespaceScopeContext.getNamespaceURI(elementPrefix).equals(elementNamespaceURI)) {
            this._namespaceScopeContext.declarePrefix(elementPrefix, elementNamespaceURI);
        }
        if (!this._namespaceScopeContext.isCurrentContextEmpty()) {
            if (attributesSize > 0) {
                write(120);
            } else {
                write(56);
            }
            for (int i3 = this._namespaceScopeContext.getCurrentContextStartIndex(); i3 < this._namespaceScopeContext.getCurrentContextEndIndex(); i3++) {
                String prefix = this._namespaceScopeContext.getPrefix(i3);
                String uri = this._namespaceScopeContext.getNamespaceURI(i3);
                encodeNamespaceAttribute(prefix, uri);
            }
            write(240);
            this._b = 0;
        } else {
            this._b = attributesSize > 0 ? 64 : 0;
        }
        encodeElement(elementNamespaceURI == null ? "" : elementNamespaceURI, e.getNodeName(), e.getLocalName());
        if (attributesSize > 0) {
            for (int i4 = 0; i4 < attributesSize; i4++) {
                Node a2 = this._attributes[i4];
                this._attributes[i4] = null;
                String namespaceURI2 = a2.getNamespaceURI();
                encodeAttribute(namespaceURI2 == null ? "" : namespaceURI2, a2.getNodeName(), a2.getLocalName());
                String value = a2.getNodeValue();
                boolean addToTable = isAttributeValueLengthMatchesLimit(value.length());
                encodeNonIdentifyingStringOnFirstBit(value, this._v.attributeValue, addToTable, false);
            }
            this._b = 240;
            this._terminate = true;
        }
        if (e.hasChildNodes()) {
            NodeList nl = e.getChildNodes();
            for (int i5 = 0; i5 < nl.getLength(); i5++) {
                Node n = nl.item(i5);
                switch (n.getNodeType()) {
                    case 1:
                        serializeElement(n);
                        break;
                    case 3:
                        serializeText(n);
                        break;
                    case 4:
                        serializeCDATA(n);
                        break;
                    case 7:
                        serializeProcessingInstruction(n);
                        break;
                    case 8:
                        serializeComment(n);
                        break;
                }
            }
        }
        encodeElementTermination();
        this._namespaceScopeContext.popContext();
    }

    protected final void serializeText(Node t) throws IOException {
        String text = t.getNodeValue();
        int length = text != null ? text.length() : 0;
        if (length == 0) {
            return;
        }
        if (length < this._charBuffer.length) {
            text.getChars(0, length, this._charBuffer, 0);
            if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(this._charBuffer, 0, length)) {
                return;
            }
            encodeTermination();
            encodeCharacters(this._charBuffer, 0, length);
            return;
        }
        char[] ch = text.toCharArray();
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
            return;
        }
        encodeTermination();
        encodeCharactersNoClone(ch, 0, length);
    }

    protected final void serializeCDATA(Node t) throws IOException {
        String text = t.getNodeValue();
        int length = text != null ? text.length() : 0;
        if (length == 0) {
            return;
        }
        char[] ch = text.toCharArray();
        if (getIgnoreWhiteSpaceTextContent() && isWhiteSpace(ch, 0, length)) {
            return;
        }
        encodeTermination();
        try {
            encodeCIIBuiltInAlgorithmDataAsCDATA(ch, 0, length);
        } catch (FastInfosetException e) {
            throw new IOException("");
        }
    }

    protected final void serializeComment(Node c) throws IOException {
        if (getIgnoreComments()) {
            return;
        }
        encodeTermination();
        String comment = c.getNodeValue();
        int length = comment != null ? comment.length() : 0;
        if (length == 0) {
            encodeComment(this._charBuffer, 0, 0);
        } else if (length < this._charBuffer.length) {
            comment.getChars(0, length, this._charBuffer, 0);
            encodeComment(this._charBuffer, 0, length);
        } else {
            char[] ch = comment.toCharArray();
            encodeCommentNoClone(ch, 0, length);
        }
    }

    protected final void serializeProcessingInstruction(Node pi) throws IOException {
        if (getIgnoreProcesingInstructions()) {
            return;
        }
        encodeTermination();
        String target = pi.getNodeName();
        String data = pi.getNodeValue();
        encodeProcessingInstruction(target, data);
    }

    protected final void encodeElement(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.elementName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                if (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName)) {
                    encodeNonZeroIntegerOnThirdBit(names[i].index);
                    return;
                }
            }
        }
        if (localName != null) {
            encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
        } else {
            encodeLiteralElementQualifiedNameOnThirdBit(namespaceURI, "", qName, entry);
        }
    }

    protected final void encodeAttribute(String namespaceURI, String qName, String localName) throws IOException {
        LocalNameQualifiedNamesMap.Entry entry = this._v.attributeName.obtainEntry(qName);
        if (entry._valueIndex > 0) {
            QualifiedName[] names = entry._value;
            for (int i = 0; i < entry._valueIndex; i++) {
                if (namespaceURI == names[i].namespaceName || namespaceURI.equals(names[i].namespaceName)) {
                    encodeNonZeroIntegerOnSecondBitFirstBitZero(names[i].index);
                    return;
                }
            }
        }
        if (localName != null) {
            encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, getPrefixFromQualifiedName(qName), localName, entry);
        } else {
            encodeLiteralAttributeQualifiedNameOnSecondBit(namespaceURI, "", qName, entry);
        }
    }
}
