package org.xmlpull.v1.dom2_builder;

import com.sun.xml.fastinfoset.EncodingConstants;
import java.io.IOException;
import java.io.Reader;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/dom2_builder/DOM2XmlPullBuilder.class */
public class DOM2XmlPullBuilder {

    /* JADX INFO: Access modifiers changed from: package-private */
    /* renamed from: org.xmlpull.v1.dom2_builder.DOM2XmlPullBuilder$1  reason: invalid class name */
    /* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/dom2_builder/DOM2XmlPullBuilder$1.class */
    public static class AnonymousClass1 {
    }

    protected Document newDoc() throws XmlPullParserException {
        try {
            DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = domFactory.newDocumentBuilder();
            builder.getDOMImplementation();
            return builder.newDocument();
        } catch (FactoryConfigurationError ex) {
            throw new XmlPullParserException(new StringBuffer().append("could not configure factory JAXP DocumentBuilderFactory: ").append(ex).toString(), null, ex);
        } catch (ParserConfigurationException ex2) {
            throw new XmlPullParserException(new StringBuffer().append("could not configure parser JAXP DocumentBuilderFactory: ").append(ex2).toString(), null, ex2);
        }
    }

    protected XmlPullParser newParser() throws XmlPullParserException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        return factory.newPullParser();
    }

    public Element parse(Reader reader) throws XmlPullParserException, IOException {
        Document docFactory = newDoc();
        return parse(reader, docFactory);
    }

    public Element parse(Reader reader, Document docFactory) throws XmlPullParserException, IOException {
        XmlPullParser pp = newParser();
        pp.setFeature("http://xmlpull.org/v1/doc/features.html#process-namespaces", true);
        pp.setInput(reader);
        pp.next();
        return parse(pp, docFactory);
    }

    public Element parse(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
        Element root = parseSubTree(pp, docFactory);
        return root;
    }

    public Element parseSubTree(XmlPullParser pp) throws XmlPullParserException, IOException {
        Document doc = newDoc();
        Element root = parseSubTree(pp, doc);
        return root;
    }

    public Element parseSubTree(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
        BuildProcess process = new BuildProcess(null);
        return process.parseSubTree(pp, docFactory);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/dom2_builder/DOM2XmlPullBuilder$BuildProcess.class */
    public static class BuildProcess {
        private XmlPullParser pp;
        private Document docFactory;
        private boolean scanNamespaces;

        BuildProcess(AnonymousClass1 x0) {
            this();
        }

        private BuildProcess() {
            this.scanNamespaces = true;
        }

        public Element parseSubTree(XmlPullParser pp, Document docFactory) throws XmlPullParserException, IOException {
            this.pp = pp;
            this.docFactory = docFactory;
            return parseSubTree();
        }

        private Element parseSubTree() throws XmlPullParserException, IOException {
            this.pp.require(2, null, null);
            String name = this.pp.getName();
            String ns = this.pp.getNamespace();
            String prefix = this.pp.getPrefix();
            String qname = prefix != null ? new StringBuffer().append(prefix).append(":").append(name).toString() : name;
            Element parent = this.docFactory.createElementNS(ns, qname);
            declareNamespaces(this.pp, parent);
            for (int i = 0; i < this.pp.getAttributeCount(); i++) {
                String attrNs = this.pp.getAttributeNamespace(i);
                String attrName = this.pp.getAttributeName(i);
                String attrValue = this.pp.getAttributeValue(i);
                if (attrNs == null || attrNs.length() == 0) {
                    parent.setAttribute(attrName, attrValue);
                } else {
                    String attrPrefix = this.pp.getAttributePrefix(i);
                    String attrQname = attrPrefix != null ? new StringBuffer().append(attrPrefix).append(":").append(attrName).toString() : attrName;
                    parent.setAttributeNS(attrNs, attrQname, attrValue);
                }
            }
            while (this.pp.next() != 3) {
                if (this.pp.getEventType() == 2) {
                    Element el = parseSubTree(this.pp, this.docFactory);
                    parent.appendChild(el);
                } else if (this.pp.getEventType() == 4) {
                    String text = this.pp.getText();
                    Text textEl = this.docFactory.createTextNode(text);
                    parent.appendChild(textEl);
                } else {
                    throw new XmlPullParserException(new StringBuffer().append("unexpected event ").append(XmlPullParser.TYPES[this.pp.getEventType()]).toString(), this.pp, null);
                }
            }
            this.pp.require(3, ns, name);
            return parent;
        }

        private void declareNamespaces(XmlPullParser pp, Element parent) throws DOMException, XmlPullParserException {
            if (this.scanNamespaces) {
                this.scanNamespaces = false;
                int top = pp.getNamespaceCount(pp.getDepth()) - 1;
                for (int i = top; i >= pp.getNamespaceCount(0); i--) {
                    String prefix = pp.getNamespacePrefix(i);
                    int j = top;
                    while (true) {
                        if (j > i) {
                            String prefixJ = pp.getNamespacePrefix(j);
                            if ((prefix == null || !prefix.equals(prefixJ)) && (prefix == null || prefix != prefixJ)) {
                                j--;
                            }
                        } else {
                            declareOneNamespace(pp, i, parent);
                            break;
                        }
                    }
                }
                return;
            }
            for (int i2 = pp.getNamespaceCount(pp.getDepth() - 1); i2 < pp.getNamespaceCount(pp.getDepth()); i2++) {
                declareOneNamespace(pp, i2, parent);
            }
        }

        private void declareOneNamespace(XmlPullParser pp, int i, Element parent) throws DOMException, XmlPullParserException {
            String xmlnsPrefix = pp.getNamespacePrefix(i);
            String xmlnsUri = pp.getNamespaceUri(i);
            String xmlnsDecl = xmlnsPrefix != null ? new StringBuffer().append("xmlns:").append(xmlnsPrefix).toString() : EncodingConstants.XMLNS_NAMESPACE_PREFIX;
            parent.setAttributeNS(EncodingConstants.XMLNS_NAMESPACE_NAME, xmlnsDecl, xmlnsUri);
        }
    }

    private static void assertEquals(String expected, String s) {
        if ((expected != null && !expected.equals(s)) || (expected == null && s == null)) {
            throw new RuntimeException(new StringBuffer().append("expected '").append(expected).append("' but got '").append(s).append("'").toString());
        }
    }

    private static void assertNotNull(Object o) {
        if (o == null) {
            throw new RuntimeException("expected no null value");
        }
    }

    public static void main(String[] args) throws Exception {
    }
}
