package javax.xml.soap;

import java.util.Iterator;
import org.w3c.dom.Element;
/* loaded from: gencallgraphv3.jar:j2ee.jar:javax/xml/soap/SOAPElement.class */
public interface SOAPElement extends Node, Element {
    SOAPElement addChildElement(Name name) throws SOAPException;

    SOAPElement addChildElement(String str) throws SOAPException;

    SOAPElement addChildElement(String str, String str2) throws SOAPException;

    SOAPElement addChildElement(String str, String str2, String str3) throws SOAPException;

    SOAPElement addChildElement(SOAPElement sOAPElement) throws SOAPException;

    void removeContents();

    SOAPElement addTextNode(String str) throws SOAPException;

    SOAPElement addAttribute(Name name, String str) throws SOAPException;

    SOAPElement addNamespaceDeclaration(String str, String str2) throws SOAPException;

    String getAttributeValue(Name name);

    Iterator getAllAttributes();

    String getNamespaceURI(String str);

    Iterator getNamespacePrefixes();

    Iterator getVisibleNamespacePrefixes();

    Name getElementName();

    boolean removeAttribute(Name name);

    boolean removeNamespaceDeclaration(String str);

    Iterator getChildElements();

    Iterator getChildElements(Name name);

    void setEncodingStyle(String str) throws SOAPException;

    String getEncodingStyle();
}
