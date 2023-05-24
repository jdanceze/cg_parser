package org.xmlpull.v1.builder;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/XmlDocument.class */
public interface XmlDocument extends XmlContainer, Cloneable {
    Object clone() throws CloneNotSupportedException;

    Iterable children();

    XmlElement getDocumentElement();

    XmlElement requiredElement(XmlNamespace xmlNamespace, String str);

    XmlElement element(XmlNamespace xmlNamespace, String str);

    XmlElement element(XmlNamespace xmlNamespace, String str, boolean z);

    Iterable notations();

    Iterable unparsedEntities();

    String getBaseUri();

    String getCharacterEncodingScheme();

    void setCharacterEncodingScheme(String str);

    Boolean isStandalone();

    String getVersion();

    boolean isAllDeclarationsProcessed();

    void setDocumentElement(XmlElement xmlElement);

    void addChild(Object obj);

    void insertChild(int i, Object obj);

    void removeAllChildren();

    XmlComment newComment(String str);

    XmlComment addComment(String str);

    XmlDoctype newDoctype(String str, String str2);

    XmlDoctype addDoctype(String str, String str2);

    XmlElement addDocumentElement(String str);

    XmlElement addDocumentElement(XmlNamespace xmlNamespace, String str);

    XmlProcessingInstruction newProcessingInstruction(String str, String str2);

    XmlProcessingInstruction addProcessingInstruction(String str, String str2);

    void removeAllUnparsedEntities();

    XmlNotation addNotation(String str, String str2, String str3, String str4);

    void removeAllNotations();
}
