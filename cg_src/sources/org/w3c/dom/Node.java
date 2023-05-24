package org.w3c.dom;
/* loaded from: gencallgraphv3.jar:xmlParserAPIs-2.6.2.jar:org/w3c/dom/Node.class */
public interface Node {
    public static final short ELEMENT_NODE = 1;
    public static final short ATTRIBUTE_NODE = 2;
    public static final short TEXT_NODE = 3;
    public static final short CDATA_SECTION_NODE = 4;
    public static final short ENTITY_REFERENCE_NODE = 5;
    public static final short ENTITY_NODE = 6;
    public static final short PROCESSING_INSTRUCTION_NODE = 7;
    public static final short COMMENT_NODE = 8;
    public static final short DOCUMENT_NODE = 9;
    public static final short DOCUMENT_TYPE_NODE = 10;
    public static final short DOCUMENT_FRAGMENT_NODE = 11;
    public static final short NOTATION_NODE = 12;

    String getNodeName();

    String getNodeValue() throws DOMException;

    void setNodeValue(String str) throws DOMException;

    short getNodeType();

    Node getParentNode();

    NodeList getChildNodes();

    Node getFirstChild();

    Node getLastChild();

    Node getPreviousSibling();

    Node getNextSibling();

    NamedNodeMap getAttributes();

    Document getOwnerDocument();

    Node insertBefore(Node node, Node node2) throws DOMException;

    Node replaceChild(Node node, Node node2) throws DOMException;

    Node removeChild(Node node) throws DOMException;

    Node appendChild(Node node) throws DOMException;

    boolean hasChildNodes();

    Node cloneNode(boolean z);

    void normalize();

    boolean isSupported(String str, String str2);

    String getNamespaceURI();

    String getPrefix();

    void setPrefix(String str) throws DOMException;

    String getLocalName();

    boolean hasAttributes();
}
