package org.xmlpull.v1.builder;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlElement.class */
public interface XmlElement extends XmlContainer, XmlContained, Cloneable {
    public static final String NO_NAMESPACE = "";

    Object clone() throws CloneNotSupportedException;

    String getBaseUri();

    void setBaseUri(String str);

    XmlContainer getRoot();

    @Override // org.xmlpull.v1.builder.XmlContained
    XmlContainer getParent();

    @Override // org.xmlpull.v1.builder.XmlContained
    void setParent(XmlContainer xmlContainer);

    XmlNamespace getNamespace();

    String getNamespaceName();

    void setNamespace(XmlNamespace xmlNamespace);

    String getName();

    void setName(String str);

    Iterator attributes();

    XmlAttribute addAttribute(XmlAttribute xmlAttribute);

    XmlAttribute addAttribute(String str, String str2);

    XmlAttribute addAttribute(XmlNamespace xmlNamespace, String str, String str2);

    XmlAttribute addAttribute(String str, XmlNamespace xmlNamespace, String str2, String str3);

    XmlAttribute addAttribute(String str, XmlNamespace xmlNamespace, String str2, String str3, boolean z);

    XmlAttribute addAttribute(String str, String str2, String str3, String str4, String str5, boolean z);

    void ensureAttributeCapacity(int i);

    String getAttributeValue(String str, String str2);

    XmlAttribute attribute(String str);

    XmlAttribute attribute(XmlNamespace xmlNamespace, String str);

    XmlAttribute findAttribute(String str, String str2);

    boolean hasAttributes();

    void removeAttribute(XmlAttribute xmlAttribute);

    void removeAllAttributes();

    Iterator namespaces();

    XmlNamespace declareNamespace(String str, String str2);

    XmlNamespace declareNamespace(XmlNamespace xmlNamespace);

    void ensureNamespaceDeclarationsCapacity(int i);

    boolean hasNamespaceDeclarations();

    XmlNamespace lookupNamespaceByPrefix(String str);

    XmlNamespace lookupNamespaceByName(String str);

    XmlNamespace newNamespace(String str);

    XmlNamespace newNamespace(String str, String str2);

    void removeAllNamespaceDeclarations();

    Iterator children();

    void addChild(Object obj);

    void addChild(int i, Object obj);

    XmlElement addElement(XmlElement xmlElement);

    XmlElement addElement(int i, XmlElement xmlElement);

    XmlElement addElement(String str);

    XmlElement addElement(XmlNamespace xmlNamespace, String str);

    boolean hasChildren();

    boolean hasChild(Object obj);

    void ensureChildrenCapacity(int i);

    XmlElement findElementByName(String str);

    XmlElement findElementByName(String str, String str2);

    XmlElement findElementByName(String str, XmlElement xmlElement);

    XmlElement findElementByName(String str, String str2, XmlElement xmlElement);

    XmlElement element(int i);

    XmlElement requiredElement(XmlNamespace xmlNamespace, String str) throws XmlBuilderException;

    XmlElement element(XmlNamespace xmlNamespace, String str);

    XmlElement element(XmlNamespace xmlNamespace, String str, boolean z);

    Iterable elements(XmlNamespace xmlNamespace, String str);

    void insertChild(int i, Object obj);

    XmlElement newElement(String str);

    XmlElement newElement(XmlNamespace xmlNamespace, String str);

    XmlElement newElement(String str, String str2);

    void removeAllChildren();

    void removeChild(Object obj);

    void replaceChild(Object obj, Object obj2);

    Iterable requiredElementContent();

    String requiredTextContent();

    void replaceChildrenWithText(String str);
}
