package org.xmlpull.v1.builder.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.Iterator;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlCharacters;
import org.xmlpull.v1.builder.XmlComment;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlInfosetBuilder;
import org.xmlpull.v1.builder.XmlNamespace;
import org.xmlpull.v1.builder.XmlSerializable;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlInfosetBuilderImpl.class */
public class XmlInfosetBuilderImpl extends XmlInfosetBuilder {
    private static final String PROPERTY_XMLDECL_STANDALONE = "http://xmlpull.org/v1/doc/properties.html#xmldecl-standalone";
    private static final String PROPERTY_XMLDECL_VERSION = "http://xmlpull.org/v1/doc/properties.html#xmldecl-version";

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlDocument newDocument(String version, Boolean standalone, String characterEncoding) {
        return new XmlDocumentImpl(version, standalone, characterEncoding);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlElement newFragment(String elementName) {
        return new XmlElementImpl((XmlNamespace) null, elementName);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlElement newFragment(String elementNamespaceName, String elementName) {
        return new XmlElementImpl(elementNamespaceName, elementName);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlElement newFragment(XmlNamespace elementNamespace, String elementName) {
        return new XmlElementImpl(elementNamespace, elementName);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlNamespace newNamespace(String namespaceName) {
        return new XmlNamespaceImpl(null, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlNamespace newNamespace(String prefix, String namespaceName) {
        return new XmlNamespaceImpl(prefix, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlDocument parse(XmlPullParser pp) {
        XmlDocument doc = parseDocumentStart(pp);
        XmlElement root = parseFragment(pp);
        doc.setDocumentElement(root);
        return doc;
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public Object parseItem(XmlPullParser pp) {
        try {
            int eventType = pp.getEventType();
            if (eventType == 2) {
                return parseStartTag(pp);
            }
            if (eventType == 4) {
                return pp.getText();
            }
            if (eventType == 0) {
                return parseDocumentStart(pp);
            }
            throw new XmlBuilderException(new StringBuffer().append("currently unsupported event type ").append(XmlPullParser.TYPES[eventType]).append(pp.getPositionDescription()).toString());
        } catch (XmlPullParserException e) {
            throw new XmlBuilderException("could not parse XML item", e);
        }
    }

    private XmlDocument parseDocumentStart(XmlPullParser pp) {
        try {
            if (pp.getEventType() != 0) {
                throw new XmlBuilderException(new StringBuffer().append("parser must be positioned on beginning of document and not ").append(pp.getPositionDescription()).toString());
            }
            pp.next();
            String xmlDeclVersion = (String) pp.getProperty(PROPERTY_XMLDECL_VERSION);
            Boolean xmlDeclStandalone = (Boolean) pp.getProperty(PROPERTY_XMLDECL_STANDALONE);
            String characterEncoding = pp.getInputEncoding();
            XmlDocument doc = new XmlDocumentImpl(xmlDeclVersion, xmlDeclStandalone, characterEncoding);
            return doc;
        } catch (IOException e) {
            throw new XmlBuilderException("could not read XML document prolog", e);
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException("could not parse XML document prolog", e2);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlElement parseFragment(XmlPullParser pp) {
        try {
            int depth = pp.getDepth();
            int eventType = pp.getEventType();
            if (eventType != 2) {
                throw new XmlBuilderException(new StringBuffer().append("expected parser to be on start tag and not ").append(XmlPullParser.TYPES[eventType]).append(pp.getPositionDescription()).toString());
            }
            XmlElement curElem = parseStartTag(pp);
            while (true) {
                int eventType2 = pp.next();
                if (eventType2 == 2) {
                    XmlElement child = parseStartTag(pp);
                    curElem.addElement(child);
                    curElem = child;
                } else if (eventType2 == 3) {
                    XmlContainer parent = curElem.getParent();
                    if (parent == null) {
                        break;
                    }
                    curElem = (XmlElement) parent;
                } else if (eventType2 == 4) {
                    curElem.addChild(pp.getText());
                }
            }
            if (pp.getDepth() != depth) {
                throw new XmlBuilderException(new StringBuffer().append("unbalanced input").append(pp.getPositionDescription()).toString());
            }
            return curElem;
        } catch (IOException e) {
            throw new XmlBuilderException("could not read XML tree content", e);
        } catch (XmlPullParserException e2) {
            throw new XmlBuilderException("could not build tree from XML", e2);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlElement parseStartTag(XmlPullParser pp) {
        try {
            if (pp.getEventType() != 2) {
                throw new XmlBuilderException(new StringBuffer().append("parser must be on START_TAG and not ").append(pp.getPositionDescription()).toString());
            }
            String elNsPrefix = pp.getPrefix();
            XmlNamespace elementNs = new XmlNamespaceImpl(elNsPrefix, pp.getNamespace());
            XmlElement el = new XmlElementImpl(elementNs, pp.getName());
            for (int i = pp.getNamespaceCount(pp.getDepth() - 1); i < pp.getNamespaceCount(pp.getDepth()); i++) {
                String prefix = pp.getNamespacePrefix(i);
                el.declareNamespace(prefix == null ? "" : prefix, pp.getNamespaceUri(i));
            }
            for (int i2 = 0; i2 < pp.getAttributeCount(); i2++) {
                el.addAttribute(pp.getAttributeType(i2), pp.getAttributePrefix(i2), pp.getAttributeNamespace(i2), pp.getAttributeName(i2), pp.getAttributeValue(i2), !pp.isAttributeDefault(i2));
            }
            return el;
        } catch (XmlPullParserException e) {
            throw new XmlBuilderException("could not parse XML start tag", e);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public XmlDocument parseLocation(String locationUrl) {
        try {
            URL url = new URL(locationUrl);
            try {
                return parseInputStream(url.openStream());
            } catch (IOException e) {
                throw new XmlBuilderException(new StringBuffer().append("could not open connection to URL ").append(locationUrl).toString(), e);
            }
        } catch (MalformedURLException e2) {
            throw new XmlBuilderException(new StringBuffer().append("could not parse URL ").append(locationUrl).toString(), e2);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public void serialize(Object item, XmlSerializer serializer) {
        if (item instanceof Collection) {
            Collection<Object> c = (Collection) item;
            for (Object obj : c) {
                serialize(obj, serializer);
            }
        } else if (item instanceof XmlContainer) {
            serializeContainer((XmlContainer) item, serializer);
        } else {
            serializeItem(item, serializer);
        }
    }

    private void serializeContainer(XmlContainer node, XmlSerializer serializer) {
        if (node instanceof XmlSerializable) {
            try {
                ((XmlSerializable) node).serialize(serializer);
            } catch (IOException e) {
                throw new XmlBuilderException(new StringBuffer().append("could not serialize node ").append(node).append(": ").append(e).toString(), e);
            }
        } else if (node instanceof XmlDocument) {
            serializeDocument((XmlDocument) node, serializer);
        } else if (node instanceof XmlElement) {
            serializeFragment((XmlElement) node, serializer);
        } else {
            throw new IllegalArgumentException(new StringBuffer().append("could not serialzie unknown XML container ").append(node.getClass()).toString());
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public void serializeItem(Object item, XmlSerializer ser) {
        try {
            if (item instanceof XmlSerializable) {
                try {
                    ((XmlSerializable) item).serialize(ser);
                } catch (IOException e) {
                    throw new XmlBuilderException(new StringBuffer().append("could not serialize item ").append(item).append(": ").append(e).toString(), e);
                }
            } else if (item instanceof String) {
                ser.text(item.toString());
            } else if (item instanceof XmlCharacters) {
                ser.text(((XmlCharacters) item).getText());
            } else if (item instanceof XmlComment) {
                ser.comment(((XmlComment) item).getContent());
            } else {
                throw new IllegalArgumentException(new StringBuffer().append("could not serialize ").append(item != null ? item.getClass() : item).toString());
            }
        } catch (IOException e2) {
            throw new XmlBuilderException("serializing XML start tag failed", e2);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public void serializeStartTag(XmlElement el, XmlSerializer ser) {
        try {
            XmlNamespace elNamespace = el.getNamespace();
            String elPrefix = elNamespace != null ? elNamespace.getPrefix() : "";
            if (elPrefix == null) {
                elPrefix = "";
            }
            String nToDeclare = null;
            if (el.hasNamespaceDeclarations()) {
                Iterator iter = el.namespaces();
                while (iter.hasNext()) {
                    XmlNamespace n = (XmlNamespace) iter.next();
                    String nPrefix = n.getPrefix();
                    if (!elPrefix.equals(nPrefix)) {
                        ser.setPrefix(nPrefix, n.getNamespaceName());
                    } else {
                        nToDeclare = n.getNamespaceName();
                    }
                }
            }
            if (nToDeclare != null) {
                ser.setPrefix(elPrefix, nToDeclare);
            } else if (elNamespace != null) {
                String namespaceName = elNamespace.getNamespaceName();
                if (namespaceName == null) {
                    namespaceName = "";
                }
                String serPrefix = null;
                if (namespaceName.length() > 0) {
                    ser.getPrefix(namespaceName, false);
                }
                if (0 == 0) {
                    serPrefix = "";
                }
                if (serPrefix != elPrefix && !serPrefix.equals(elPrefix)) {
                    ser.setPrefix(elPrefix, namespaceName);
                }
            }
            ser.startTag(el.getNamespaceName(), el.getName());
            if (el.hasAttributes()) {
                Iterator iter2 = el.attributes();
                while (iter2.hasNext()) {
                    XmlAttribute a = (XmlAttribute) iter2.next();
                    if (a instanceof XmlSerializable) {
                        ((XmlSerializable) a).serialize(ser);
                    } else {
                        ser.attribute(a.getNamespaceName(), a.getName(), a.getValue());
                    }
                }
            }
        } catch (IOException e) {
            throw new XmlBuilderException("serializing XML start tag failed", e);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlInfosetBuilder
    public void serializeEndTag(XmlElement el, XmlSerializer ser) {
        try {
            ser.endTag(el.getNamespaceName(), el.getName());
        } catch (IOException e) {
            throw new XmlBuilderException("serializing XML end tag failed", e);
        }
    }

    private void serializeDocument(XmlDocument doc, XmlSerializer ser) {
        try {
            ser.startDocument(doc.getCharacterEncodingScheme(), doc.isStandalone());
            if (doc.getDocumentElement() != null) {
                serializeFragment(doc.getDocumentElement(), ser);
                try {
                    ser.endDocument();
                    return;
                } catch (IOException e) {
                    throw new XmlBuilderException("serializing XML document end failed", e);
                }
            }
            throw new XmlBuilderException(new StringBuffer().append("could not serialize document without root element ").append(doc).append(": ").toString());
        } catch (IOException e2) {
            throw new XmlBuilderException("serializing XML document start failed", e2);
        }
    }

    private void serializeFragment(XmlElement el, XmlSerializer ser) {
        serializeStartTag(el, ser);
        if (el.hasChildren()) {
            Iterator iter = el.children();
            while (iter.hasNext()) {
                Object child = iter.next();
                if (child instanceof XmlSerializable) {
                    try {
                        ((XmlSerializable) child).serialize(ser);
                    } catch (IOException e) {
                        throw new XmlBuilderException(new StringBuffer().append("could not serialize item ").append(child).append(": ").append(e).toString(), e);
                    }
                } else if (child instanceof XmlElement) {
                    serializeFragment((XmlElement) child, ser);
                } else {
                    serializeItem(child, ser);
                }
            }
        }
        serializeEndTag(el, ser);
    }
}
