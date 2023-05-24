package com.sun.xml.txw2;
/* loaded from: gencallgraphv3.jar:txw2-2.4.0-b180830.0438.jar:com/sun/xml/txw2/StartTag.class */
class StartTag extends Content implements NamespaceResolver {
    private String uri;
    private final String localName;
    private Attribute firstAtt;
    private Attribute lastAtt;
    private ContainerElement owner;
    private NamespaceDecl firstNs;
    private NamespaceDecl lastNs;
    final Document document;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !StartTag.class.desiredAssertionStatus();
    }

    public StartTag(ContainerElement owner, String uri, String localName) {
        this(owner.document, uri, localName);
        this.owner = owner;
    }

    public StartTag(Document document, String uri, String localName) {
        if (!$assertionsDisabled && uri == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && localName == null) {
            throw new AssertionError();
        }
        this.uri = uri;
        this.localName = localName;
        this.document = document;
        addNamespaceDecl(uri, null, false);
    }

    public void addAttribute(String nsUri, String localName, Object arg) {
        Attribute a;
        checkWritable();
        Attribute attribute = this.firstAtt;
        while (true) {
            a = attribute;
            if (a == null || a.hasName(nsUri, localName)) {
                break;
            }
            attribute = a.next;
        }
        if (a == null) {
            a = new Attribute(nsUri, localName);
            if (this.lastAtt == null) {
                if (!$assertionsDisabled && this.firstAtt != null) {
                    throw new AssertionError();
                }
                this.lastAtt = a;
                this.firstAtt = a;
            } else if (!$assertionsDisabled && this.firstAtt == null) {
                throw new AssertionError();
            } else {
                this.lastAtt.next = a;
                this.lastAtt = a;
            }
            if (nsUri.length() > 0) {
                addNamespaceDecl(nsUri, null, true);
            }
        }
        this.document.writeValue(arg, this, a.value);
    }

    public NamespaceDecl addNamespaceDecl(String uri, String prefix, boolean requirePrefix) {
        checkWritable();
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        if (uri.length() == 0) {
            if (requirePrefix) {
                throw new IllegalArgumentException("The empty namespace cannot have a non-empty prefix");
            }
            if (prefix != null && prefix.length() > 0) {
                throw new IllegalArgumentException("The empty namespace can be only bound to the empty prefix");
            }
            prefix = "";
        }
        NamespaceDecl namespaceDecl = this.firstNs;
        while (true) {
            NamespaceDecl n = namespaceDecl;
            if (n != null) {
                if (uri.equals(n.uri)) {
                    if (prefix == null) {
                        n.requirePrefix |= requirePrefix;
                        return n;
                    } else if (n.prefix == null) {
                        n.prefix = prefix;
                        n.requirePrefix |= requirePrefix;
                        return n;
                    } else if (prefix.equals(n.prefix)) {
                        n.requirePrefix |= requirePrefix;
                        return n;
                    }
                }
                if (prefix == null || n.prefix == null || !n.prefix.equals(prefix)) {
                    namespaceDecl = n.next;
                } else {
                    throw new IllegalArgumentException("Prefix '" + prefix + "' is already bound to '" + n.uri + '\'');
                }
            } else {
                NamespaceDecl ns = new NamespaceDecl(this.document.assignNewId(), uri, prefix, requirePrefix);
                if (this.lastNs == null) {
                    if (!$assertionsDisabled && this.firstNs != null) {
                        throw new AssertionError();
                    }
                    this.lastNs = ns;
                    this.firstNs = ns;
                } else if (!$assertionsDisabled && this.firstNs == null) {
                    throw new AssertionError();
                } else {
                    this.lastNs.next = ns;
                    this.lastNs = ns;
                }
                return ns;
            }
        }
    }

    private void checkWritable() {
        if (isWritten()) {
            throw new IllegalStateException("The start tag of " + this.localName + " has already been written. If you need out of order writing, see the TypedXmlWriter.block method");
        }
    }

    boolean isWritten() {
        return this.uri == null;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public boolean isReadyToCommit() {
        if (this.owner != null && this.owner.isBlocked()) {
            return false;
        }
        Content next = getNext();
        while (true) {
            Content c = next;
            if (c != null) {
                if (!c.concludesPendingStartTag()) {
                    next = c.getNext();
                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    @Override // com.sun.xml.txw2.Content
    public void written() {
        this.lastAtt = null;
        this.firstAtt = null;
        this.uri = null;
        if (this.owner != null) {
            if (!$assertionsDisabled && this.owner.startTag != this) {
                throw new AssertionError();
            }
            this.owner.startTag = null;
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public boolean concludesPendingStartTag() {
        return true;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.xml.txw2.Content
    public void accept(ContentVisitor visitor) {
        visitor.onStartTag(this.uri, this.localName, this.firstAtt, this.firstNs);
    }

    @Override // com.sun.xml.txw2.NamespaceResolver
    public String getPrefix(String nsUri) {
        NamespaceDecl ns = addNamespaceDecl(nsUri, null, false);
        if (ns.prefix != null) {
            return ns.prefix;
        }
        return ns.dummyPrefix;
    }
}
