package org.xmlpull.v1.builder.impl;

import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlNamespace;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlNamespaceImpl.class */
public class XmlNamespaceImpl implements XmlNamespace {
    private String namespaceName;
    private String prefix;

    XmlNamespaceImpl(String namespaceName) {
        if (namespaceName == null) {
            throw new XmlBuilderException("namespace name can not be null");
        }
        this.namespaceName = namespaceName;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlNamespaceImpl(String prefix, String namespaceName) {
        this.prefix = prefix;
        if (namespaceName == null) {
            throw new XmlBuilderException("namespace name can not be null");
        }
        if (prefix != null && prefix.indexOf(58) != -1) {
            throw new XmlBuilderException(new StringBuffer().append("prefix '").append(prefix).append("' for namespace '").append(namespaceName).append("' can not contain colon (:)").toString());
        }
        this.namespaceName = namespaceName;
    }

    @Override // org.xmlpull.v1.builder.XmlNamespace
    public String getPrefix() {
        return this.prefix;
    }

    @Override // org.xmlpull.v1.builder.XmlNamespace
    public String getNamespaceName() {
        return this.namespaceName;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (other != null && (other instanceof XmlNamespace)) {
            XmlNamespace otherNamespace = (XmlNamespace) other;
            return getNamespaceName().equals(otherNamespace.getNamespaceName());
        }
        return false;
    }

    public String toString() {
        return new StringBuffer().append("{prefix='").append(this.prefix).append("',namespaceName='").append(this.namespaceName).append("'}").toString();
    }
}
