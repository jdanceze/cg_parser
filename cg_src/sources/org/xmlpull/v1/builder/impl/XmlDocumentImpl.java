package org.xmlpull.v1.builder.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlComment;
import org.xmlpull.v1.builder.XmlDoctype;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
import org.xmlpull.v1.builder.XmlNotation;
import org.xmlpull.v1.builder.XmlProcessingInstruction;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlDocumentImpl.class */
public class XmlDocumentImpl implements XmlDocument {
    private List children = new ArrayList();
    private XmlElement root;
    private String version;
    private Boolean standalone;
    private String characterEncoding;

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Object clone() throws CloneNotSupportedException {
        XmlDocumentImpl cloned = (XmlDocumentImpl) super.clone();
        cloned.root = null;
        cloned.children = cloneList(cloned, this.children);
        int pos = cloned.findDocumentElement();
        if (pos >= 0) {
            cloned.root = (XmlElement) cloned.children.get(pos);
            cloned.root.setParent(cloned);
        }
        return cloned;
    }

    private List cloneList(XmlDocumentImpl cloned, List list) throws CloneNotSupportedException {
        Object newMember;
        if (list == null) {
            return null;
        }
        List newList = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            Object member = list.get(i);
            if (member instanceof XmlElement) {
                XmlElement el = (XmlElement) member;
                newMember = el.clone();
            } else if (member instanceof Cloneable) {
                try {
                    newMember = member.getClass().getMethod("clone", null).invoke(member, null);
                } catch (Exception e) {
                    throw new CloneNotSupportedException(new StringBuffer().append("failed to call clone() on  ").append(member).append(e).toString());
                }
            } else {
                throw new CloneNotSupportedException(new StringBuffer().append("could not clone ").append(member).append(" of ").append(member != null ? member.getClass().toString() : "").toString());
            }
            newList.add(newMember);
        }
        return newList;
    }

    public XmlDocumentImpl(String version, Boolean standalone, String characterEncoding) {
        this.version = version;
        this.standalone = standalone;
        this.characterEncoding = characterEncoding;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getVersion() {
        return this.version;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Boolean isStandalone() {
        return this.standalone;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getCharacterEncodingScheme() {
        return this.characterEncoding;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void setCharacterEncodingScheme(String characterEncoding) {
        this.characterEncoding = characterEncoding;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlProcessingInstruction newProcessingInstruction(String target, String content) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlProcessingInstruction addProcessingInstruction(String target, String content) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable children() {
        return new Iterable(this) { // from class: org.xmlpull.v1.builder.impl.XmlDocumentImpl.1
            private final XmlDocumentImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // org.xmlpull.v1.builder.Iterable
            public Iterator iterator() {
                return this.this$0.children.iterator();
            }
        };
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllUnparsedEntities() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void setDocumentElement(XmlElement rootElement) {
        int pos = findDocumentElement();
        if (pos >= 0) {
            this.children.set(pos, rootElement);
        } else {
            this.children.add(rootElement);
        }
        this.root = rootElement;
        rootElement.setParent(this);
    }

    private int findDocumentElement() {
        for (int i = 0; i < this.children.size(); i++) {
            Object element = this.children.get(i);
            if (element instanceof XmlElement) {
                return i;
            }
        }
        return -1;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement requiredElement(XmlNamespace n, String name) {
        XmlElement el = element(n, name);
        if (el == null) {
            throw new XmlBuilderException(new StringBuffer().append("document does not contain element with name ").append(name).append(" in namespace ").append(n.getNamespaceName()).toString());
        }
        return el;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement element(XmlNamespace n, String name) {
        return element(n, name, false);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement element(XmlNamespace namespace, String name, boolean create) {
        XmlElement e = getDocumentElement();
        if (e == null) {
            return null;
        }
        String eNamespaceName = e.getNamespace() != null ? e.getNamespace().getNamespaceName() : null;
        if (namespace != null) {
            if (name.equals(e.getName()) && eNamespaceName != null && eNamespaceName.equals(namespace.getNamespaceName())) {
                return e;
            }
        } else if (name.equals(e.getName()) && eNamespaceName == null) {
            return e;
        }
        if (create) {
            return addDocumentElement(namespace, name);
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void insertChild(int pos, Object child) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlComment addComment(String content) {
        XmlCommentImpl xmlCommentImpl = new XmlCommentImpl(this, content);
        this.children.add(xmlCommentImpl);
        return xmlCommentImpl;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlDoctype newDoctype(String systemIdentifier, String publicIdentifier) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable unparsedEntities() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllChildren() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlComment newComment(String content) {
        return new XmlCommentImpl(null, content);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllNotations() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlDoctype addDoctype(String systemIdentifier, String publicIdentifier) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void addChild(Object child) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlNotation addNotation(String name, String systemIdentifier, String publicIdentifier, String declarationBaseUri) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getBaseUri() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable notations() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement addDocumentElement(String name) {
        return addDocumentElement(null, name);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement addDocumentElement(XmlNamespace namespace, String name) {
        XmlElement el = new XmlElementImpl(namespace, name);
        if (getDocumentElement() != null) {
            throw new XmlBuilderException("document already has root element");
        }
        setDocumentElement(el);
        return el;
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public boolean isAllDeclarationsProcessed() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement getDocumentElement() {
        return this.root;
    }
}
