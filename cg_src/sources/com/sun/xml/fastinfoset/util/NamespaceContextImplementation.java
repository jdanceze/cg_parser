package com.sun.xml.fastinfoset.util;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.namespace.NamespaceContext;
/* loaded from: gencallgraphv3.jar:FastInfoset-1.2.15.jar:com/sun/xml/fastinfoset/util/NamespaceContextImplementation.class */
public final class NamespaceContextImplementation implements NamespaceContext {
    private static int DEFAULT_SIZE = 8;
    private int namespacePosition;
    private int contextPosition;
    private int currentContext;
    private String[] prefixes = new String[DEFAULT_SIZE];
    private String[] namespaceURIs = new String[DEFAULT_SIZE];
    private int[] contexts = new int[DEFAULT_SIZE];

    public NamespaceContextImplementation() {
        this.prefixes[0] = EncodingConstants.XML_NAMESPACE_PREFIX;
        this.namespaceURIs[0] = "http://www.w3.org/XML/1998/namespace";
        this.prefixes[1] = EncodingConstants.XMLNS_NAMESPACE_PREFIX;
        this.namespaceURIs[1] = EncodingConstants.XMLNS_NAMESPACE_NAME;
        this.namespacePosition = 2;
        this.currentContext = 2;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getNamespaceURI(String prefix) {
        if (prefix == null) {
            throw new IllegalArgumentException();
        }
        for (int i = this.namespacePosition - 1; i >= 0; i--) {
            String declaredPrefix = this.prefixes[i];
            if (declaredPrefix.equals(prefix)) {
                return this.namespaceURIs[i];
            }
        }
        return "";
    }

    @Override // javax.xml.namespace.NamespaceContext
    public String getPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        for (int i = this.namespacePosition - 1; i >= 0; i--) {
            String declaredNamespaceURI = this.namespaceURIs[i];
            if (declaredNamespaceURI.equals(namespaceURI)) {
                String declaredPrefix = this.prefixes[i];
                boolean isOutOfScope = false;
                int j = i + 1;
                while (true) {
                    if (j < this.namespacePosition) {
                        if (!declaredPrefix.equals(this.prefixes[j])) {
                            j++;
                        } else {
                            isOutOfScope = true;
                            break;
                        }
                    } else {
                        break;
                    }
                }
                if (!isOutOfScope) {
                    return declaredPrefix;
                }
            }
        }
        return null;
    }

    public String getNonDefaultPrefix(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        int i = this.namespacePosition - 1;
        while (i >= 0) {
            String declaredNamespaceURI = this.namespaceURIs[i];
            if (!declaredNamespaceURI.equals(namespaceURI) || this.prefixes[i].length() <= 0) {
                i--;
            } else {
                String declaredPrefix = this.prefixes[i];
                while (true) {
                    i++;
                    if (i < this.namespacePosition) {
                        if (declaredPrefix.equals(this.prefixes[i])) {
                            return null;
                        }
                    } else {
                        return declaredPrefix;
                    }
                }
            }
        }
        return null;
    }

    @Override // javax.xml.namespace.NamespaceContext
    public Iterator getPrefixes(String namespaceURI) {
        if (namespaceURI == null) {
            throw new IllegalArgumentException();
        }
        List l = new ArrayList();
        for (int i = this.namespacePosition - 1; i >= 0; i--) {
            String declaredNamespaceURI = this.namespaceURIs[i];
            if (declaredNamespaceURI.equals(namespaceURI)) {
                String declaredPrefix = this.prefixes[i];
                int j = i + 1;
                while (true) {
                    if (j < this.namespacePosition) {
                        if (declaredPrefix.equals(this.prefixes[j])) {
                            break;
                        }
                        j++;
                    } else {
                        l.add(declaredPrefix);
                        break;
                    }
                }
            }
        }
        return l.iterator();
    }

    public String getPrefix(int index) {
        return this.prefixes[index];
    }

    public String getNamespaceURI(int index) {
        return this.namespaceURIs[index];
    }

    public int getCurrentContextStartIndex() {
        return this.currentContext;
    }

    public int getCurrentContextEndIndex() {
        return this.namespacePosition;
    }

    public boolean isCurrentContextEmpty() {
        return this.currentContext == this.namespacePosition;
    }

    public void declarePrefix(String prefix, String namespaceURI) {
        String prefix2 = prefix.intern();
        String namespaceURI2 = namespaceURI.intern();
        if (prefix2 == EncodingConstants.XML_NAMESPACE_PREFIX || prefix2 == EncodingConstants.XMLNS_NAMESPACE_PREFIX) {
            return;
        }
        for (int i = this.currentContext; i < this.namespacePosition; i++) {
            String declaredPrefix = this.prefixes[i];
            if (declaredPrefix == prefix2) {
                this.prefixes[i] = prefix2;
                this.namespaceURIs[i] = namespaceURI2;
                return;
            }
        }
        if (this.namespacePosition == this.namespaceURIs.length) {
            resizeNamespaces();
        }
        this.prefixes[this.namespacePosition] = prefix2;
        String[] strArr = this.namespaceURIs;
        int i2 = this.namespacePosition;
        this.namespacePosition = i2 + 1;
        strArr[i2] = namespaceURI2;
    }

    private void resizeNamespaces() {
        int newLength = ((this.namespaceURIs.length * 3) / 2) + 1;
        String[] newPrefixes = new String[newLength];
        System.arraycopy(this.prefixes, 0, newPrefixes, 0, this.prefixes.length);
        this.prefixes = newPrefixes;
        String[] newNamespaceURIs = new String[newLength];
        System.arraycopy(this.namespaceURIs, 0, newNamespaceURIs, 0, this.namespaceURIs.length);
        this.namespaceURIs = newNamespaceURIs;
    }

    public void pushContext() {
        if (this.contextPosition == this.contexts.length) {
            resizeContexts();
        }
        int[] iArr = this.contexts;
        int i = this.contextPosition;
        this.contextPosition = i + 1;
        int i2 = this.namespacePosition;
        this.currentContext = i2;
        iArr[i] = i2;
    }

    private void resizeContexts() {
        int[] newContexts = new int[((this.contexts.length * 3) / 2) + 1];
        System.arraycopy(this.contexts, 0, newContexts, 0, this.contexts.length);
        this.contexts = newContexts;
    }

    public void popContext() {
        if (this.contextPosition > 0) {
            int[] iArr = this.contexts;
            int i = this.contextPosition - 1;
            this.contextPosition = i;
            int i2 = iArr[i];
            this.currentContext = i2;
            this.namespacePosition = i2;
        }
    }

    public void reset() {
        this.namespacePosition = 2;
        this.currentContext = 2;
    }
}
