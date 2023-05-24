package org.apache.tools.ant.util;

import org.w3c.dom.CDATASection;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/DOMUtils.class */
public class DOMUtils {
    public static Document newDocument() {
        return JAXPUtils.getDocumentBuilder().newDocument();
    }

    public static Element createChildElement(Element parent, String name) {
        Document doc = parent.getOwnerDocument();
        Element e = doc.createElement(name);
        parent.appendChild(e);
        return e;
    }

    public static void appendText(Element parent, String content) {
        Document doc = parent.getOwnerDocument();
        Text t = doc.createTextNode(content);
        parent.appendChild(t);
    }

    public static void appendCDATA(Element parent, String content) {
        Document doc = parent.getOwnerDocument();
        CDATASection c = doc.createCDATASection(content);
        parent.appendChild(c);
    }

    public static void appendTextElement(Element parent, String name, String content) {
        Element e = createChildElement(parent, name);
        appendText(e, content);
    }

    public static void appendCDATAElement(Element parent, String name, String content) {
        Element e = createChildElement(parent, name);
        appendCDATA(e, content);
    }

    private DOMUtils() {
    }
}
