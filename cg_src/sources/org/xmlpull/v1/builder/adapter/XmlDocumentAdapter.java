package org.xmlpull.v1.builder.adapter;

import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlComment;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDoctype;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
import org.xmlpull.v1.builder.XmlNotation;
import org.xmlpull.v1.builder.XmlProcessingInstruction;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/adapter/XmlDocumentAdapter.class */
public class XmlDocumentAdapter implements XmlDocument {
    private XmlDocument target;

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Object clone() throws CloneNotSupportedException {
        XmlDocumentAdapter ela = (XmlDocumentAdapter) super.clone();
        ela.target = (XmlDocument) this.target.clone();
        return ela;
    }

    public XmlDocumentAdapter(XmlDocument target) {
        this.target = target;
        fixImportedChildParent(target.getDocumentElement());
    }

    private void fixImportedChildParent(Object child) {
        if (child instanceof XmlElement) {
            XmlElement childEl = (XmlElement) child;
            XmlContainer childElParent = childEl.getParent();
            if (childElParent == this.target) {
                childEl.setParent(this);
            }
        }
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable children() {
        return this.target.children();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement getDocumentElement() {
        return this.target.getDocumentElement();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement requiredElement(XmlNamespace n, String name) {
        return this.target.requiredElement(n, name);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement element(XmlNamespace n, String name) {
        return this.target.element(n, name);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement element(XmlNamespace n, String name, boolean create) {
        return this.target.element(n, name, create);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable notations() {
        return this.target.notations();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Iterable unparsedEntities() {
        return this.target.unparsedEntities();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getBaseUri() {
        return this.target.getBaseUri();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getCharacterEncodingScheme() {
        return this.target.getCharacterEncodingScheme();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void setCharacterEncodingScheme(String characterEncoding) {
        this.target.setCharacterEncodingScheme(characterEncoding);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public Boolean isStandalone() {
        return this.target.isStandalone();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public String getVersion() {
        return this.target.getVersion();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public boolean isAllDeclarationsProcessed() {
        return this.target.isAllDeclarationsProcessed();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void setDocumentElement(XmlElement rootElement) {
        this.target.setDocumentElement(rootElement);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void addChild(Object child) {
        this.target.addChild(child);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void insertChild(int pos, Object child) {
        this.target.insertChild(pos, child);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllChildren() {
        this.target.removeAllChildren();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlComment newComment(String content) {
        return this.target.newComment(content);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlComment addComment(String content) {
        return this.target.addComment(content);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlDoctype newDoctype(String systemIdentifier, String publicIdentifier) {
        return this.target.newDoctype(systemIdentifier, publicIdentifier);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlDoctype addDoctype(String systemIdentifier, String publicIdentifier) {
        return this.target.addDoctype(systemIdentifier, publicIdentifier);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement addDocumentElement(String name) {
        return this.target.addDocumentElement(name);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlElement addDocumentElement(XmlNamespace namespace, String name) {
        return this.target.addDocumentElement(namespace, name);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlProcessingInstruction newProcessingInstruction(String target, String content) {
        return this.target.newProcessingInstruction(target, content);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlProcessingInstruction addProcessingInstruction(String target, String content) {
        return this.target.addProcessingInstruction(target, content);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllUnparsedEntities() {
        this.target.removeAllUnparsedEntities();
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public XmlNotation addNotation(String name, String systemIdentifier, String publicIdentifier, String declarationBaseUri) {
        return this.target.addNotation(name, systemIdentifier, publicIdentifier, declarationBaseUri);
    }

    @Override // org.xmlpull.v1.builder.XmlDocument
    public void removeAllNotations() {
        this.target.removeAllNotations();
    }
}
