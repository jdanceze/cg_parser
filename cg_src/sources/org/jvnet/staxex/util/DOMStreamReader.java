package org.jvnet.staxex.util;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.Location;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Text;
/* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/DOMStreamReader.class */
public class DOMStreamReader implements XMLStreamReader, NamespaceContext {
    protected Node _current;
    private Node _start;
    private NamedNodeMap _namedNodeMap;
    protected String wholeText;
    private final FinalArrayList<Attr> _currentAttributes;
    protected Scope[] scopes;
    protected int depth;
    protected int _state;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:stax-ex-1.8.jar:org/jvnet/staxex/util/DOMStreamReader$Scope.class */
    public static final class Scope {
        final Scope parent;
        final FinalArrayList<Attr> currentNamespaces = new FinalArrayList<>();
        final FinalArrayList<String> additionalNamespaces = new FinalArrayList<>();

        Scope(Scope parent) {
            this.parent = parent;
        }

        void reset() {
            this.currentNamespaces.clear();
            this.additionalNamespaces.clear();
        }

        int getNamespaceCount() {
            return this.currentNamespaces.size() + (this.additionalNamespaces.size() / 2);
        }

        String getNamespacePrefix(int index) {
            int sz = this.currentNamespaces.size();
            if (index < sz) {
                Attr attr = this.currentNamespaces.get(index);
                String result = attr.getLocalName();
                if (result == null) {
                    result = QName.valueOf(attr.getNodeName()).getLocalPart();
                }
                if (result.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                    return null;
                }
                return result;
            }
            return this.additionalNamespaces.get((index - sz) * 2);
        }

        String getNamespaceURI(int index) {
            int sz = this.currentNamespaces.size();
            if (index < sz) {
                return this.currentNamespaces.get(index).getValue();
            }
            return this.additionalNamespaces.get(((index - sz) * 2) + 1);
        }

        String getPrefix(String nsUri) {
            Scope scope = this;
            while (true) {
                Scope sp = scope;
                if (sp != null) {
                    for (int i = sp.currentNamespaces.size() - 1; i >= 0; i--) {
                        String result = DOMStreamReader.getPrefixForAttr(sp.currentNamespaces.get(i), nsUri);
                        if (result != null) {
                            return result;
                        }
                    }
                    for (int i2 = sp.additionalNamespaces.size() - 2; i2 >= 0; i2 -= 2) {
                        if (sp.additionalNamespaces.get(i2 + 1).equals(nsUri)) {
                            return sp.additionalNamespaces.get(i2);
                        }
                    }
                    scope = sp.parent;
                } else {
                    return null;
                }
            }
        }

        String getNamespaceURI(String prefix) {
            String nsDeclName = prefix.length() == 0 ? EncodingConstants.XMLNS_NAMESPACE_PREFIX : "xmlns:" + prefix;
            Scope scope = this;
            while (true) {
                Scope sp = scope;
                if (sp != null) {
                    for (int i = sp.currentNamespaces.size() - 1; i >= 0; i--) {
                        Attr a = sp.currentNamespaces.get(i);
                        if (a.getNodeName().equals(nsDeclName)) {
                            return a.getValue();
                        }
                    }
                    for (int i2 = sp.additionalNamespaces.size() - 2; i2 >= 0; i2 -= 2) {
                        if (sp.additionalNamespaces.get(i2).equals(prefix)) {
                            return sp.additionalNamespaces.get(i2 + 1);
                        }
                    }
                    scope = sp.parent;
                } else {
                    return null;
                }
            }
        }
    }

    public DOMStreamReader() {
        this._currentAttributes = new FinalArrayList<>();
        this.scopes = new Scope[8];
        this.depth = 0;
    }

    public DOMStreamReader(Node node) {
        this._currentAttributes = new FinalArrayList<>();
        this.scopes = new Scope[8];
        this.depth = 0;
        setCurrentNode(node);
    }

    public void setCurrentNode(Node node) {
        this.scopes[0] = new Scope(null);
        this.depth = 0;
        this._current = node;
        this._start = node;
        this._state = 7;
    }

    public void close() throws XMLStreamException {
    }

    protected void splitAttributes() {
        this._currentAttributes.clear();
        Scope scope = allocateScope();
        this._namedNodeMap = this._current.getAttributes();
        if (this._namedNodeMap != null) {
            int n = this._namedNodeMap.getLength();
            for (int i = 0; i < n; i++) {
                Attr attr = (Attr) this._namedNodeMap.item(i);
                String attrName = attr.getNodeName();
                if (attrName.startsWith("xmlns:") || attrName.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                    scope.currentNamespaces.add(attr);
                } else {
                    this._currentAttributes.add(attr);
                }
            }
        }
        ensureNs(this._current);
        for (int i2 = this._currentAttributes.size() - 1; i2 >= 0; i2--) {
            Attr a = this._currentAttributes.get(i2);
            if (fixNull(a.getNamespaceURI()).length() > 0) {
                ensureNs(a);
            }
        }
    }

    private void ensureNs(Node n) {
        String prefix = fixNull(n.getPrefix());
        String uri = fixNull(n.getNamespaceURI());
        Scope scope = this.scopes[this.depth];
        String currentUri = scope.getNamespaceURI(prefix);
        if (prefix.length() == 0) {
            if (fixNull(currentUri).equals(uri)) {
                return;
            }
        } else if (currentUri != null && currentUri.equals(uri)) {
            return;
        }
        if (prefix.equals(EncodingConstants.XML_NAMESPACE_PREFIX) || prefix.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
            return;
        }
        scope.additionalNamespaces.add(prefix);
        scope.additionalNamespaces.add(uri);
    }

    private Scope allocateScope() {
        int length = this.scopes.length;
        int i = this.depth + 1;
        this.depth = i;
        if (length == i) {
            Scope[] newBuf = new Scope[this.scopes.length * 2];
            System.arraycopy(this.scopes, 0, newBuf, 0, this.scopes.length);
            this.scopes = newBuf;
        }
        Scope scope = this.scopes[this.depth];
        if (scope == null) {
            Scope[] scopeArr = this.scopes;
            int i2 = this.depth;
            Scope scope2 = new Scope(this.scopes[this.depth - 1]);
            scopeArr[i2] = scope2;
            scope = scope2;
        } else {
            scope.reset();
        }
        return scope;
    }

    public int getAttributeCount() {
        if (this._state == 1) {
            return this._currentAttributes.size();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeCount() called in illegal state");
    }

    public String getAttributeLocalName(int index) {
        if (this._state == 1) {
            String localName = this._currentAttributes.get(index).getLocalName();
            return localName != null ? localName : QName.valueOf(this._currentAttributes.get(index).getNodeName()).getLocalPart();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeLocalName() called in illegal state");
    }

    public QName getAttributeName(int index) {
        if (this._state == 1) {
            Node attr = this._currentAttributes.get(index);
            String localName = attr.getLocalName();
            if (localName != null) {
                String prefix = attr.getPrefix();
                String uri = attr.getNamespaceURI();
                return new QName(fixNull(uri), localName, fixNull(prefix));
            }
            return QName.valueOf(attr.getNodeName());
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeName() called in illegal state");
    }

    public String getAttributeNamespace(int index) {
        if (this._state == 1) {
            String uri = this._currentAttributes.get(index).getNamespaceURI();
            return fixNull(uri);
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeNamespace() called in illegal state");
    }

    public String getAttributePrefix(int index) {
        if (this._state == 1) {
            String prefix = this._currentAttributes.get(index).getPrefix();
            return fixNull(prefix);
        }
        throw new IllegalStateException("DOMStreamReader: getAttributePrefix() called in illegal state");
    }

    public String getAttributeType(int index) {
        if (this._state == 1) {
            return "CDATA";
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeType() called in illegal state");
    }

    public String getAttributeValue(int index) {
        if (this._state == 1) {
            return this._currentAttributes.get(index).getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
    }

    public String getAttributeValue(String namespaceURI, String localName) {
        Node attr;
        if (this._state == 1) {
            if (this._namedNodeMap == null || (attr = this._namedNodeMap.getNamedItemNS(namespaceURI, localName)) == null) {
                return null;
            }
            return attr.getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
    }

    public String getCharacterEncodingScheme() {
        return null;
    }

    public String getElementText() throws XMLStreamException {
        throw new RuntimeException("DOMStreamReader: getElementText() not implemented");
    }

    public String getEncoding() {
        return null;
    }

    public int getEventType() {
        return this._state;
    }

    public String getLocalName() {
        if (this._state == 1 || this._state == 2) {
            String localName = this._current.getLocalName();
            return localName != null ? localName : QName.valueOf(this._current.getNodeName()).getLocalPart();
        } else if (this._state == 9) {
            return this._current.getNodeName();
        } else {
            throw new IllegalStateException("DOMStreamReader: getAttributeValue() called in illegal state");
        }
    }

    public Location getLocation() {
        return DummyLocation.INSTANCE;
    }

    public QName getName() {
        if (this._state == 1 || this._state == 2) {
            String localName = this._current.getLocalName();
            if (localName != null) {
                String prefix = this._current.getPrefix();
                String uri = this._current.getNamespaceURI();
                return new QName(fixNull(uri), localName, fixNull(prefix));
            }
            return QName.valueOf(this._current.getNodeName());
        }
        throw new IllegalStateException("DOMStreamReader: getName() called in illegal state");
    }

    public NamespaceContext getNamespaceContext() {
        return this;
    }

    private Scope getCheckedScope() {
        if (this._state == 1 || this._state == 2) {
            return this.scopes[this.depth];
        }
        throw new IllegalStateException("DOMStreamReader: neither on START_ELEMENT nor END_ELEMENT");
    }

    public int getNamespaceCount() {
        return getCheckedScope().getNamespaceCount();
    }

    public String getNamespacePrefix(int index) {
        return getCheckedScope().getNamespacePrefix(index);
    }

    public String getNamespaceURI(int index) {
        return getCheckedScope().getNamespaceURI(index);
    }

    public String getNamespaceURI() {
        if (this._state == 1 || this._state == 2) {
            String uri = this._current.getNamespaceURI();
            return fixNull(uri);
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException("DOMStreamReader: getNamespaceURI(String) call with a null prefix");
        }
        if (prefix.equals(EncodingConstants.XML_NAMESPACE_PREFIX)) {
            return "http://www.w3.org/XML/1998/namespace";
        }
        if (prefix.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
            return EncodingConstants.XMLNS_NAMESPACE_NAME;
        }
        String nsUri = this.scopes[this.depth].getNamespaceURI(prefix);
        if (nsUri != null) {
            return nsUri;
        }
        String nsDeclName = prefix.length() == 0 ? EncodingConstants.XMLNS_NAMESPACE_PREFIX : "xmlns:" + prefix;
        for (Node node = findRootElement(); node.getNodeType() != 9; node = node.getParentNode()) {
            NamedNodeMap namedNodeMap = node.getAttributes();
            Attr attr = (Attr) namedNodeMap.getNamedItem(nsDeclName);
            if (attr != null) {
                return attr.getValue();
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String nsUri) {
        if (nsUri == null) {
            throw new IllegalArgumentException("DOMStreamReader: getPrefix(String) call with a null namespace URI");
        }
        if (nsUri.equals("http://www.w3.org/XML/1998/namespace")) {
            return EncodingConstants.XML_NAMESPACE_PREFIX;
        }
        if (nsUri.equals(EncodingConstants.XMLNS_NAMESPACE_NAME)) {
            return EncodingConstants.XMLNS_NAMESPACE_PREFIX;
        }
        String prefix = this.scopes[this.depth].getPrefix(nsUri);
        if (prefix != null) {
            return prefix;
        }
        Node findRootElement = findRootElement();
        while (true) {
            Node node = findRootElement;
            if (node.getNodeType() != 9) {
                NamedNodeMap namedNodeMap = node.getAttributes();
                for (int i = namedNodeMap.getLength() - 1; i >= 0; i--) {
                    Attr attr = (Attr) namedNodeMap.item(i);
                    String prefix2 = getPrefixForAttr(attr, nsUri);
                    if (prefix2 != null) {
                        return prefix2;
                    }
                }
                findRootElement = node.getParentNode();
            } else {
                return null;
            }
        }
    }

    private Node findRootElement() {
        Node node;
        Node node2 = this._start;
        while (true) {
            node = node2;
            int type = node.getNodeType();
            if (type == 9 || type == 1) {
                break;
            }
            node2 = node.getParentNode();
        }
        return node;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getPrefixForAttr(Attr attr, String nsUri) {
        String attrName = attr.getNodeName();
        if ((attrName.startsWith("xmlns:") || attrName.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) && attr.getValue().equals(nsUri)) {
            if (attrName.equals(EncodingConstants.XMLNS_NAMESPACE_PREFIX)) {
                return "";
            }
            String localName = attr.getLocalName();
            return localName != null ? localName : QName.valueOf(attrName).getLocalPart();
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String nsUri) {
        String prefix = getPrefix(nsUri);
        return prefix == null ? Collections.emptyList().iterator() : Collections.singletonList(prefix).iterator();
    }

    public String getPIData() {
        if (this._state == 3) {
            return ((ProcessingInstruction) this._current).getData();
        }
        return null;
    }

    public String getPITarget() {
        if (this._state == 3) {
            return ((ProcessingInstruction) this._current).getTarget();
        }
        return null;
    }

    public String getPrefix() {
        if (this._state == 1 || this._state == 2) {
            String prefix = this._current.getPrefix();
            return fixNull(prefix);
        }
        return null;
    }

    public Object getProperty(String str) throws IllegalArgumentException {
        return null;
    }

    public String getText() {
        if (this._state == 4) {
            return this.wholeText;
        }
        if (this._state == 12 || this._state == 5 || this._state == 9) {
            return this._current.getNodeValue();
        }
        throw new IllegalStateException("DOMStreamReader: getTextLength() called in illegal state");
    }

    public char[] getTextCharacters() {
        return getText().toCharArray();
    }

    public int getTextCharacters(int sourceStart, char[] target, int targetStart, int targetLength) throws XMLStreamException {
        String text = getText();
        int copiedSize = Math.min(targetLength, text.length() - sourceStart);
        text.getChars(sourceStart, sourceStart + copiedSize, target, targetStart);
        return copiedSize;
    }

    public int getTextLength() {
        return getText().length();
    }

    public int getTextStart() {
        if (this._state == 4 || this._state == 12 || this._state == 5 || this._state == 9) {
            return 0;
        }
        throw new IllegalStateException("DOMStreamReader: getTextStart() called in illegal state");
    }

    public String getVersion() {
        return null;
    }

    public boolean hasName() {
        return this._state == 1 || this._state == 2;
    }

    public boolean hasNext() throws XMLStreamException {
        return this._state != 8;
    }

    public boolean hasText() {
        return (this._state == 4 || this._state == 12 || this._state == 5 || this._state == 9) && getText().trim().length() > 0;
    }

    public boolean isAttributeSpecified(int param) {
        return false;
    }

    public boolean isCharacters() {
        return this._state == 4;
    }

    public boolean isEndElement() {
        return this._state == 2;
    }

    public boolean isStandalone() {
        return true;
    }

    public boolean isStartElement() {
        return this._state == 1;
    }

    public boolean isWhiteSpace() {
        return (this._state == 4 || this._state == 12) && getText().trim().length() == 0;
    }

    private static int mapNodeTypeToState(int nodetype) {
        switch (nodetype) {
            case 1:
                return 1;
            case 2:
            case 9:
            case 10:
            case 11:
            default:
                throw new RuntimeException("DOMStreamReader: Unexpected node type");
            case 3:
                return 4;
            case 4:
                return 12;
            case 5:
                return 9;
            case 6:
                return 15;
            case 7:
                return 3;
            case 8:
                return 5;
            case 12:
                return 14;
        }
    }

    public int next() throws XMLStreamException {
        while (true) {
            int r = _next();
            switch (r) {
                case 1:
                    splitAttributes();
                    return 1;
                case 4:
                    Node prev = this._current.getPreviousSibling();
                    if (prev == null || prev.getNodeType() != 3) {
                        Text t = (Text) this._current;
                        this.wholeText = t.getWholeText();
                        if (this.wholeText.length() != 0) {
                            return 4;
                        }
                    }
                    break;
                default:
                    return r;
            }
        }
    }

    protected int _next() throws XMLStreamException {
        switch (this._state) {
            case 1:
                Node child = this._current.getFirstChild();
                if (child == null) {
                    this._state = 2;
                    return 2;
                }
                this._current = child;
                int mapNodeTypeToState = mapNodeTypeToState(this._current.getNodeType());
                this._state = mapNodeTypeToState;
                return mapNodeTypeToState;
            case 2:
            case 3:
            case 4:
            case 5:
            case 9:
            case 12:
                if (this._state == 2) {
                    this.depth--;
                }
                if (this._current == this._start) {
                    this._state = 8;
                    return 8;
                }
                Node sibling = this._current.getNextSibling();
                if (sibling == null) {
                    this._current = this._current.getParentNode();
                    this._state = (this._current == null || this._current.getNodeType() == 9) ? 8 : 2;
                    return this._state;
                }
                this._current = sibling;
                int mapNodeTypeToState2 = mapNodeTypeToState(this._current.getNodeType());
                this._state = mapNodeTypeToState2;
                return mapNodeTypeToState2;
            case 6:
            case 10:
            case 11:
            case 13:
            default:
                throw new RuntimeException("DOMStreamReader: Unexpected internal state");
            case 7:
                if (this._current.getNodeType() == 1) {
                    this._state = 1;
                    return 1;
                }
                Node child2 = this._current.getFirstChild();
                if (child2 == null) {
                    this._state = 8;
                    return 8;
                }
                this._current = child2;
                int mapNodeTypeToState3 = mapNodeTypeToState(this._current.getNodeType());
                this._state = mapNodeTypeToState3;
                return mapNodeTypeToState3;
            case 8:
                throw new IllegalStateException("DOMStreamReader: Calling next() at END_DOCUMENT");
        }
    }

    public int nextTag() throws XMLStreamException {
        int eventType;
        int next = next();
        while (true) {
            eventType = next;
            if ((eventType != 4 || !isWhiteSpace()) && ((eventType != 12 || !isWhiteSpace()) && eventType != 6 && eventType != 3 && eventType != 5)) {
                break;
            }
            next = next();
        }
        if (eventType != 1 && eventType != 2) {
            throw new XMLStreamException("DOMStreamReader: Expected start or end tag");
        }
        return eventType;
    }

    public void require(int type, String namespaceURI, String localName) throws XMLStreamException {
        if (type != this._state) {
            throw new XMLStreamException("DOMStreamReader: Required event type not found");
        }
        if (namespaceURI != null && !namespaceURI.equals(getNamespaceURI())) {
            throw new XMLStreamException("DOMStreamReader: Required namespaceURI not found");
        }
        if (localName != null && !localName.equals(getLocalName())) {
            throw new XMLStreamException("DOMStreamReader: Required localName not found");
        }
    }

    public boolean standaloneSet() {
        return true;
    }

    private static String fixNull(String s) {
        return s == null ? "" : s;
    }
}
