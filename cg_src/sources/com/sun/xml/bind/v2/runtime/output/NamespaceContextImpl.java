package com.sun.xml.bind.v2.runtime.output;

import com.sun.istack.NotNull;
import com.sun.istack.Nullable;
import com.sun.xml.bind.marshaller.NamespacePrefixMapper;
import com.sun.xml.bind.v2.WellKnownNamespace;
import com.sun.xml.bind.v2.runtime.Name;
import com.sun.xml.bind.v2.runtime.NamespaceContext2;
import com.sun.xml.bind.v2.runtime.XMLSerializer;
import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import javax.xml.rpc.NamespaceConstants;
import javax.xml.stream.XMLStreamException;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/NamespaceContextImpl.class */
public final class NamespaceContextImpl implements NamespaceContext2 {
    private final XMLSerializer owner;
    private int size;
    private Element current;
    private final Element top;
    public boolean collectionMode;
    private static final NamespacePrefixMapper defaultNamespacePrefixMapper;
    static final /* synthetic */ boolean $assertionsDisabled;
    private String[] prefixes = new String[4];
    private String[] nsUris = new String[4];
    private NamespacePrefixMapper prefixMapper = defaultNamespacePrefixMapper;

    static {
        $assertionsDisabled = !NamespaceContextImpl.class.desiredAssertionStatus();
        defaultNamespacePrefixMapper = new NamespacePrefixMapper() { // from class: com.sun.xml.bind.v2.runtime.output.NamespaceContextImpl.1
            @Override // com.sun.xml.bind.marshaller.NamespacePrefixMapper
            public String getPreferredPrefix(String namespaceUri, String suggestion, boolean requirePrefix) {
                if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema-instance")) {
                    return NamespaceConstants.NSPREFIX_SCHEMA_XSI;
                }
                if (namespaceUri.equals("http://www.w3.org/2001/XMLSchema")) {
                    return "xs";
                }
                if (namespaceUri.equals(WellKnownNamespace.XML_MIME_URI)) {
                    return "xmime";
                }
                return suggestion;
            }
        };
    }

    public NamespaceContextImpl(XMLSerializer owner) {
        this.owner = owner;
        Element element = new Element(this, null);
        this.top = element;
        this.current = element;
        put("http://www.w3.org/XML/1998/namespace", EncodingConstants.XML_NAMESPACE_PREFIX);
    }

    public void setPrefixMapper(NamespacePrefixMapper mapper) {
        if (mapper == null) {
            mapper = defaultNamespacePrefixMapper;
        }
        this.prefixMapper = mapper;
    }

    public NamespacePrefixMapper getPrefixMapper() {
        return this.prefixMapper;
    }

    public void reset() {
        this.current = this.top;
        this.size = 1;
        this.collectionMode = false;
    }

    public int declareNsUri(String uri, String preferedPrefix, boolean requirePrefix) {
        String preferedPrefix2 = this.prefixMapper.getPreferredPrefix(uri, preferedPrefix, requirePrefix);
        if (uri.length() == 0) {
            for (int i = this.size - 1; i >= 0; i--) {
                if (this.nsUris[i].length() == 0) {
                    return i;
                }
                if (this.prefixes[i].length() == 0) {
                    if ($assertionsDisabled || (this.current.defaultPrefixIndex == -1 && this.current.oldDefaultNamespaceUriIndex == -1)) {
                        String oldUri = this.nsUris[i];
                        String[] knownURIs = this.owner.nameList.namespaceURIs;
                        if (this.current.baseIndex <= i) {
                            this.nsUris[i] = "";
                            int subst = put(oldUri, null);
                            int j = knownURIs.length - 1;
                            while (true) {
                                if (j >= 0) {
                                    if (knownURIs[j].equals(oldUri)) {
                                        this.owner.knownUri2prefixIndexMap[j] = subst;
                                        break;
                                    } else {
                                        j--;
                                    }
                                } else {
                                    break;
                                }
                            }
                            if (this.current.elementLocalName != null) {
                                this.current.setTagName(subst, this.current.elementLocalName, this.current.getOuterPeer());
                            }
                            return i;
                        }
                        int j2 = knownURIs.length - 1;
                        while (true) {
                            if (j2 >= 0) {
                                if (knownURIs[j2].equals(oldUri)) {
                                    this.current.defaultPrefixIndex = i;
                                    this.current.oldDefaultNamespaceUriIndex = j2;
                                    this.owner.knownUri2prefixIndexMap[j2] = this.size;
                                    break;
                                } else {
                                    j2--;
                                }
                            } else {
                                break;
                            }
                        }
                        if (this.current.elementLocalName != null) {
                            this.current.setTagName(this.size, this.current.elementLocalName, this.current.getOuterPeer());
                        }
                        put(this.nsUris[i], null);
                        return put("", "");
                    } else {
                        throw new AssertionError();
                    }
                }
            }
            return put("", "");
        }
        for (int i2 = this.size - 1; i2 >= 0; i2--) {
            String p = this.prefixes[i2];
            if (this.nsUris[i2].equals(uri) && (!requirePrefix || p.length() > 0)) {
                return i2;
            }
            if (p.equals(preferedPrefix2)) {
                preferedPrefix2 = null;
            }
        }
        if (preferedPrefix2 == null && requirePrefix) {
            preferedPrefix2 = makeUniquePrefix();
        }
        return put(uri, preferedPrefix2);
    }

    @Override // com.sun.xml.bind.v2.runtime.NamespaceContext2
    public int force(@NotNull String uri, @NotNull String prefix) {
        int i = this.size - 1;
        while (true) {
            if (i < 0) {
                break;
            } else if (!this.prefixes[i].equals(prefix)) {
                i--;
            } else if (this.nsUris[i].equals(uri)) {
                return i;
            }
        }
        return put(uri, prefix);
    }

    public int put(@NotNull String uri, @Nullable String prefix) {
        if (this.size == this.nsUris.length) {
            String[] u = new String[this.nsUris.length * 2];
            String[] p = new String[this.prefixes.length * 2];
            System.arraycopy(this.nsUris, 0, u, 0, this.nsUris.length);
            System.arraycopy(this.prefixes, 0, p, 0, this.prefixes.length);
            this.nsUris = u;
            this.prefixes = p;
        }
        if (prefix == null) {
            if (this.size == 1) {
                prefix = "";
            } else {
                prefix = makeUniquePrefix();
            }
        }
        this.nsUris[this.size] = uri;
        this.prefixes[this.size] = prefix;
        int i = this.size;
        this.size = i + 1;
        return i;
    }

    private String makeUniquePrefix() {
        String sb = new StringBuilder(5).append("ns").append(this.size).toString();
        while (true) {
            String prefix = sb;
            if (getNamespaceURI(prefix) != null) {
                sb = prefix + '_';
            } else {
                return prefix;
            }
        }
    }

    public Element getCurrent() {
        return this.current;
    }

    public int getPrefixIndex(String uri) {
        for (int i = this.size - 1; i >= 0; i--) {
            if (this.nsUris[i].equals(uri)) {
                return i;
            }
        }
        throw new IllegalStateException();
    }

    public String getPrefix(int prefixIndex) {
        return this.prefixes[prefixIndex];
    }

    public String getNamespaceURI(int prefixIndex) {
        return this.nsUris[prefixIndex];
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        for (int i = this.size - 1; i >= 0; i--) {
            if (this.prefixes[i].equals(prefix)) {
                return this.nsUris[i];
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String uri) {
        if (this.collectionMode) {
            return declareNamespace(uri, null, false);
        }
        for (int i = this.size - 1; i >= 0; i--) {
            if (this.nsUris[i].equals(uri)) {
                return this.prefixes[i];
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator<String> getPrefixes(String uri) {
        String prefix = getPrefix(uri);
        if (prefix == null) {
            return Collections.emptySet().iterator();
        }
        return Collections.singleton(uri).iterator();
    }

    @Override // com.sun.xml.bind.v2.runtime.NamespaceContext2
    public String declareNamespace(String namespaceUri, String preferedPrefix, boolean requirePrefix) {
        int idx = declareNsUri(namespaceUri, preferedPrefix, requirePrefix);
        return getPrefix(idx);
    }

    public int count() {
        return this.size;
    }

    /* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/output/NamespaceContextImpl$Element.class */
    public final class Element {
        public final NamespaceContextImpl context;
        private final Element prev;
        private Element next;
        private int oldDefaultNamespaceUriIndex;
        private int defaultPrefixIndex;
        private int baseIndex;
        private final int depth;
        private int elementNamePrefix;
        private String elementLocalName;
        private Name elementName;
        private Object outerPeer;
        private Object innerPeer;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !NamespaceContextImpl.class.desiredAssertionStatus();
        }

        private Element(NamespaceContextImpl context, Element prev) {
            this.context = context;
            this.prev = prev;
            this.depth = prev == null ? 0 : prev.depth + 1;
        }

        public boolean isRootElement() {
            return this.depth == 1;
        }

        public Element push() {
            if (this.next == null) {
                this.next = new Element(this.context, this);
            }
            this.next.onPushed();
            return this.next;
        }

        public Element pop() {
            if (this.oldDefaultNamespaceUriIndex >= 0) {
                this.context.owner.knownUri2prefixIndexMap[this.oldDefaultNamespaceUriIndex] = this.defaultPrefixIndex;
            }
            this.context.size = this.baseIndex;
            this.context.current = this.prev;
            this.innerPeer = null;
            this.outerPeer = null;
            return this.prev;
        }

        private void onPushed() {
            this.defaultPrefixIndex = -1;
            this.oldDefaultNamespaceUriIndex = -1;
            this.baseIndex = this.context.size;
            this.context.current = this;
        }

        public void setTagName(int prefix, String localName, Object outerPeer) {
            if (!$assertionsDisabled && localName == null) {
                throw new AssertionError();
            }
            this.elementNamePrefix = prefix;
            this.elementLocalName = localName;
            this.elementName = null;
            this.outerPeer = outerPeer;
        }

        public void setTagName(Name tagName, Object outerPeer) {
            if (!$assertionsDisabled && tagName == null) {
                throw new AssertionError();
            }
            this.elementName = tagName;
            this.outerPeer = outerPeer;
        }

        public void startElement(XmlOutput out, Object innerPeer) throws IOException, XMLStreamException {
            this.innerPeer = innerPeer;
            if (this.elementName != null) {
                out.beginStartTag(this.elementName);
            } else {
                out.beginStartTag(this.elementNamePrefix, this.elementLocalName);
            }
        }

        public void endElement(XmlOutput out) throws IOException, SAXException, XMLStreamException {
            if (this.elementName != null) {
                out.endTag(this.elementName);
                this.elementName = null;
                return;
            }
            out.endTag(this.elementNamePrefix, this.elementLocalName);
        }

        public final int count() {
            return this.context.size - this.baseIndex;
        }

        public final String getPrefix(int idx) {
            return this.context.prefixes[this.baseIndex + idx];
        }

        public final String getNsUri(int idx) {
            return this.context.nsUris[this.baseIndex + idx];
        }

        public int getBase() {
            return this.baseIndex;
        }

        public Object getOuterPeer() {
            return this.outerPeer;
        }

        public Object getInnerPeer() {
            return this.innerPeer;
        }

        public Element getParent() {
            return this.prev;
        }
    }
}
