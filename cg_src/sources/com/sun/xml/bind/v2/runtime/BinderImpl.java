package com.sun.xml.bind.v2.runtime;

import com.sun.xml.bind.unmarshaller.InfosetScanner;
import com.sun.xml.bind.v2.runtime.AssociationMap;
import com.sun.xml.bind.v2.runtime.output.DOMOutput;
import com.sun.xml.bind.v2.runtime.unmarshaller.InterningXmlVisitor;
import com.sun.xml.bind.v2.runtime.unmarshaller.SAXConnector;
import com.sun.xml.bind.v2.runtime.unmarshaller.UnmarshallerImpl;
import javax.xml.bind.Binder;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.PropertyException;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.namespace.QName;
import javax.xml.validation.Schema;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;
/* loaded from: gencallgraphv3.jar:jaxb-runtime-2.4.0-b180830.0438.jar:com/sun/xml/bind/v2/runtime/BinderImpl.class */
public class BinderImpl<XmlNode> extends Binder<XmlNode> {
    private final JAXBContextImpl context;
    private UnmarshallerImpl unmarshaller;
    private MarshallerImpl marshaller;
    private final InfosetScanner<XmlNode> scanner;
    private final AssociationMap<XmlNode> assoc = new AssociationMap<>();

    /* JADX INFO: Access modifiers changed from: package-private */
    public BinderImpl(JAXBContextImpl _context, InfosetScanner<XmlNode> scanner) {
        this.context = _context;
        this.scanner = scanner;
    }

    private UnmarshallerImpl getUnmarshaller() {
        if (this.unmarshaller == null) {
            this.unmarshaller = new UnmarshallerImpl(this.context, this.assoc);
        }
        return this.unmarshaller;
    }

    private MarshallerImpl getMarshaller() {
        if (this.marshaller == null) {
            this.marshaller = new MarshallerImpl(this.context, this.assoc);
        }
        return this.marshaller;
    }

    @Override // javax.xml.bind.Binder
    public void marshal(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
        if (xmlNode == null || jaxbObject == null) {
            throw new IllegalArgumentException();
        }
        getMarshaller().marshal(jaxbObject, createOutput(xmlNode));
    }

    private DOMOutput createOutput(XmlNode xmlNode) {
        return new DOMOutput((Node) xmlNode, this.assoc);
    }

    @Override // javax.xml.bind.Binder
    public Object updateJAXB(XmlNode xmlNode) throws JAXBException {
        return associativeUnmarshal(xmlNode, true, null);
    }

    @Override // javax.xml.bind.Binder
    public Object unmarshal(XmlNode xmlNode) throws JAXBException {
        return associativeUnmarshal(xmlNode, false, null);
    }

    @Override // javax.xml.bind.Binder
    public <T> JAXBElement<T> unmarshal(XmlNode xmlNode, Class<T> expectedType) throws JAXBException {
        if (expectedType == null) {
            throw new IllegalArgumentException();
        }
        return (JAXBElement) associativeUnmarshal(xmlNode, true, expectedType);
    }

    @Override // javax.xml.bind.Binder
    public void setSchema(Schema schema) {
        getMarshaller().setSchema(schema);
        getUnmarshaller().setSchema(schema);
    }

    @Override // javax.xml.bind.Binder
    public Schema getSchema() {
        return getUnmarshaller().getSchema();
    }

    private Object associativeUnmarshal(XmlNode xmlNode, boolean inplace, Class expectedType) throws JAXBException {
        if (xmlNode == null) {
            throw new IllegalArgumentException();
        }
        JaxBeanInfo bi = null;
        if (expectedType != null) {
            bi = this.context.getBeanInfo(expectedType, true);
        }
        InterningXmlVisitor handler = new InterningXmlVisitor(getUnmarshaller().createUnmarshallerHandler(this.scanner, inplace, bi));
        this.scanner.setContentHandler(new SAXConnector(handler, this.scanner.getLocator()));
        try {
            this.scanner.scan(xmlNode);
            return handler.getContext().getResult();
        } catch (SAXException e) {
            throw this.unmarshaller.createUnmarshalException(e);
        }
    }

    @Override // javax.xml.bind.Binder
    public XmlNode getXMLNode(Object jaxbObject) {
        if (jaxbObject == null) {
            throw new IllegalArgumentException();
        }
        AssociationMap.Entry<XmlNode> e = this.assoc.byPeer(jaxbObject);
        if (e == null) {
            return null;
        }
        return e.element();
    }

    @Override // javax.xml.bind.Binder
    public Object getJAXBNode(XmlNode xmlNode) {
        if (xmlNode == null) {
            throw new IllegalArgumentException();
        }
        AssociationMap.Entry e = this.assoc.byElement(xmlNode);
        if (e == null) {
            return null;
        }
        return e.outer() != null ? e.outer() : e.inner();
    }

    @Override // javax.xml.bind.Binder
    public XmlNode updateXML(Object jaxbObject) throws JAXBException {
        return updateXML(jaxbObject, getXMLNode(jaxbObject));
    }

    /* JADX WARN: Type inference failed for: r0v19, types: [XmlNode, org.w3c.dom.Node] */
    @Override // javax.xml.bind.Binder
    public XmlNode updateXML(Object jaxbObject, XmlNode xmlNode) throws JAXBException {
        if (jaxbObject == null || xmlNode == null) {
            throw new IllegalArgumentException();
        }
        Element e = (Element) xmlNode;
        Node ns = e.getNextSibling();
        Node p = e.getParentNode();
        p.removeChild(e);
        JaxBeanInfo bi = this.context.getBeanInfo(jaxbObject, true);
        if (!bi.isElement()) {
            jaxbObject = new JAXBElement(new QName(e.getNamespaceURI(), e.getLocalName()), bi.jaxbType, jaxbObject);
        }
        getMarshaller().marshal(jaxbObject, p);
        ?? r0 = (XmlNode) p.getLastChild();
        p.removeChild(r0);
        p.insertBefore(r0, ns);
        return r0;
    }

    @Override // javax.xml.bind.Binder
    public void setEventHandler(ValidationEventHandler handler) throws JAXBException {
        getUnmarshaller().setEventHandler(handler);
        getMarshaller().setEventHandler(handler);
    }

    @Override // javax.xml.bind.Binder
    public ValidationEventHandler getEventHandler() {
        return getUnmarshaller().getEventHandler();
    }

    @Override // javax.xml.bind.Binder
    public Object getProperty(String name) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
        }
        if (excludeProperty(name)) {
            throw new PropertyException(name);
        }
        try {
            Object prop = getMarshaller().getProperty(name);
            return prop;
        } catch (PropertyException e) {
            try {
                Object prop2 = getUnmarshaller().getProperty(name);
                return prop2;
            } catch (PropertyException p) {
                p.setStackTrace(Thread.currentThread().getStackTrace());
                throw p;
            }
        }
    }

    @Override // javax.xml.bind.Binder
    public void setProperty(String name, Object value) throws PropertyException {
        if (name == null) {
            throw new IllegalArgumentException(Messages.NULL_PROPERTY_NAME.format(new Object[0]));
        }
        if (excludeProperty(name)) {
            throw new PropertyException(name, value);
        }
        try {
            getMarshaller().setProperty(name, value);
        } catch (PropertyException e) {
            try {
                getUnmarshaller().setProperty(name, value);
            } catch (PropertyException p) {
                p.setStackTrace(Thread.currentThread().getStackTrace());
                throw p;
            }
        }
    }

    private boolean excludeProperty(String name) {
        return name.equals("com.sun.xml.bind.characterEscapeHandler") || name.equals("com.sun.xml.bind.xmlDeclaration") || name.equals("com.sun.xml.bind.xmlHeaders");
    }
}
