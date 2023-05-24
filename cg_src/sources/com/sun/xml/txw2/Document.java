package com.sun.xml.txw2;

import com.sun.xml.txw2.output.XmlSerializer;
import java.util.HashMap;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/Document.class */
public final class Document {
    private final XmlSerializer out;
    private NamespaceDecl activeNamespaces;
    static final char MAGIC = 0;
    static final /* synthetic */ boolean $assertionsDisabled;
    private boolean started = false;
    private Content current = null;
    private final Map<Class, DatatypeWriter> datatypeWriters = new HashMap();
    private int iota = 1;
    private final NamespaceSupport inscopeNamespace = new NamespaceSupport();
    private final ContentVisitor visitor = new ContentVisitor() { // from class: com.sun.xml.txw2.Document.1
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Document.class.desiredAssertionStatus();
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onStartDocument() {
            throw new IllegalStateException();
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onEndDocument() {
            Document.this.out.endDocument();
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onEndTag() {
            Document.this.out.endTag();
            Document.this.inscopeNamespace.popContext();
            Document.this.activeNamespaces = null;
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onPcdata(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.text(buffer);
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onCdata(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.cdata(buffer);
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onComment(StringBuilder buffer) {
            if (Document.this.activeNamespaces != null) {
                buffer = Document.this.fixPrefix(buffer);
            }
            Document.this.out.comment(buffer);
        }

        @Override // com.sun.xml.txw2.ContentVisitor
        public void onStartTag(String nsUri, String localName, Attribute attributes, NamespaceDecl namespaces) {
            NamespaceSupport namespaceSupport;
            String newPrefix;
            String uri;
            if (!$assertionsDisabled && nsUri == null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && localName == null) {
                throw new AssertionError();
            }
            Document.this.activeNamespaces = namespaces;
            if (!Document.this.started) {
                Document.this.started = true;
                Document.this.out.startDocument();
            }
            Document.this.inscopeNamespace.pushContext();
            NamespaceDecl namespaceDecl = namespaces;
            while (true) {
                NamespaceDecl ns = namespaceDecl;
                if (ns == null) {
                    break;
                }
                ns.declared = false;
                if (ns.prefix != null && ((uri = Document.this.inscopeNamespace.getURI(ns.prefix)) == null || !uri.equals(ns.uri))) {
                    Document.this.inscopeNamespace.declarePrefix(ns.prefix, ns.uri);
                    ns.declared = true;
                }
                namespaceDecl = ns.next;
            }
            NamespaceDecl namespaceDecl2 = namespaces;
            while (true) {
                NamespaceDecl ns2 = namespaceDecl2;
                if (ns2 == null) {
                    break;
                }
                if (ns2.prefix == null) {
                    if (!Document.this.inscopeNamespace.getURI("").equals(ns2.uri)) {
                        String p = Document.this.inscopeNamespace.getPrefix(ns2.uri);
                        if (p == null) {
                            do {
                                namespaceSupport = Document.this.inscopeNamespace;
                                newPrefix = Document.this.newPrefix();
                                p = newPrefix;
                            } while (namespaceSupport.getURI(newPrefix) != null);
                            ns2.declared = true;
                            Document.this.inscopeNamespace.declarePrefix(p, ns2.uri);
                        }
                        ns2.prefix = p;
                    } else {
                        ns2.prefix = "";
                    }
                }
                namespaceDecl2 = ns2.next;
            }
            if (!$assertionsDisabled && !namespaces.uri.equals(nsUri)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && namespaces.prefix == null) {
                throw new AssertionError("a prefix must have been all allocated");
            }
            Document.this.out.beginStartTag(nsUri, localName, namespaces.prefix);
            NamespaceDecl namespaceDecl3 = namespaces;
            while (true) {
                NamespaceDecl ns3 = namespaceDecl3;
                if (ns3 == null) {
                    break;
                }
                if (ns3.declared) {
                    Document.this.out.writeXmlns(ns3.prefix, ns3.uri);
                }
                namespaceDecl3 = ns3.next;
            }
            Attribute attribute = attributes;
            while (true) {
                Attribute a = attribute;
                if (a == null) {
                    Document.this.out.endStartTag(nsUri, localName, namespaces.prefix);
                    return;
                }
                String prefix = a.nsUri.length() == 0 ? "" : Document.this.inscopeNamespace.getPrefix(a.nsUri);
                Document.this.out.writeAttribute(a.nsUri, a.localName, prefix, Document.this.fixPrefix(a.value));
                attribute = a.next;
            }
        }
    };
    private final StringBuilder prefixSeed = new StringBuilder("ns");
    private int prefixIota = 0;

    static {
        $assertionsDisabled = !Document.class.desiredAssertionStatus();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public Document(XmlSerializer out) {
        this.out = out;
        for (DatatypeWriter dw : DatatypeWriter.BUILTIN) {
            this.datatypeWriters.put(dw.getType(), dw);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void flush() {
        this.out.flush();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void setFirstContent(Content c) {
        if (!$assertionsDisabled && this.current != null) {
            throw new AssertionError();
        }
        this.current = new StartDocument();
        this.current.setNext(this, c);
    }

    public void addDatatypeWriter(DatatypeWriter<?> dw) {
        this.datatypeWriters.put(dw.getType(), dw);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void run() {
        while (true) {
            Content next = this.current.getNext();
            if (next == null || !next.isReadyToCommit()) {
                return;
            }
            next.accept(this.visitor);
            next.written();
            this.current = next;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void writeValue(Object obj, NamespaceResolver nsResolver, StringBuilder buf) {
        Object[] objArr;
        if (obj == null) {
            throw new IllegalArgumentException("argument contains null");
        }
        if (obj instanceof Object[]) {
            for (Object o : (Object[]) obj) {
                writeValue(o, nsResolver, buf);
            }
        } else if (obj instanceof Iterable) {
            for (Object o2 : (Iterable) obj) {
                writeValue(o2, nsResolver, buf);
            }
        } else {
            if (buf.length() > 0) {
                buf.append(' ');
            }
            Class cls = obj.getClass();
            while (true) {
                Class c = cls;
                if (c != null) {
                    DatatypeWriter dw = this.datatypeWriters.get(c);
                    if (dw != null) {
                        dw.print(obj, nsResolver, buf);
                        return;
                    }
                    cls = c.getSuperclass();
                } else {
                    buf.append(obj);
                    return;
                }
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String newPrefix() {
        this.prefixSeed.setLength(2);
        StringBuilder sb = this.prefixSeed;
        int i = this.prefixIota + 1;
        this.prefixIota = i;
        sb.append(i);
        return this.prefixSeed.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public StringBuilder fixPrefix(StringBuilder buf) {
        NamespaceDecl ns;
        if ($assertionsDisabled || this.activeNamespaces != null) {
            int len = buf.length();
            int i = 0;
            while (i < len && buf.charAt(i) != 0) {
                i++;
            }
            if (i == len) {
                return buf;
            }
            while (i < len) {
                char uriIdx = buf.charAt(i + 1);
                NamespaceDecl namespaceDecl = this.activeNamespaces;
                while (true) {
                    ns = namespaceDecl;
                    if (ns == null || ns.uniqueId == uriIdx) {
                        break;
                    }
                    namespaceDecl = ns.next;
                }
                if (ns == null) {
                    throw new IllegalStateException("Unexpected use of prefixes " + ((Object) buf));
                }
                int length = 2;
                String prefix = ns.prefix;
                if (prefix.length() == 0) {
                    if (buf.length() <= i + 2 || buf.charAt(i + 2) != ':') {
                        throw new IllegalStateException("Unexpected use of prefixes " + ((Object) buf));
                    }
                    length = 3;
                }
                buf.replace(i, i + length, prefix);
                len += prefix.length() - length;
                while (i < len && buf.charAt(i) != 0) {
                    i++;
                }
            }
            return buf;
        }
        throw new AssertionError();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public char assignNewId() {
        int i = this.iota;
        this.iota = i + 1;
        return (char) i;
    }
}
