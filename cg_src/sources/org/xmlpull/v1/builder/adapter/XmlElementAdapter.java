package org.xmlpull.v1.builder.adapter;

import java.util.Iterator;
import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/adapter/XmlElementAdapter.class */
public class XmlElementAdapter implements XmlElement {
    private XmlElementAdapter topAdapter;
    private XmlElement target;
    private XmlContainer parent;
    static Class class$org$xmlpull$v1$builder$adapter$XmlElementAdapter;
    static Class class$org$xmlpull$v1$builder$XmlElement;

    public XmlElementAdapter(XmlElement target) {
        setTarget(target);
    }

    private void setTarget(XmlElement target) {
        this.target = target;
        if (target.getParent() != null) {
            this.parent = target.getParent();
            if (this.parent instanceof XmlDocument) {
                XmlDocument doc = (XmlDocument) this.parent;
                doc.setDocumentElement(this);
            }
            if (this.parent instanceof XmlElement) {
                XmlElement parentEl = (XmlElement) this.parent;
                parentEl.replaceChild(this, target);
            }
        }
        Iterator iter = target.children();
        while (iter.hasNext()) {
            Object child = iter.next();
            fixImportedChildParent(child);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Object clone() throws CloneNotSupportedException {
        XmlElementAdapter ela = (XmlElementAdapter) super.clone();
        ela.parent = null;
        ela.target = (XmlElement) this.target.clone();
        return ela;
    }

    public XmlElement getTarget() {
        return this.target;
    }

    public XmlElementAdapter getTopAdapter() {
        return this.topAdapter != null ? this.topAdapter : this;
    }

    public void setTopAdapter(XmlElementAdapter adapter) {
        this.topAdapter = adapter;
        if (this.target instanceof XmlElementAdapter) {
            ((XmlElementAdapter) this.target).setTopAdapter(adapter);
        }
    }

    public static XmlElementAdapter castOrWrap(XmlElement el, Class adapterClass) {
        Class cls;
        Class<?> cls2;
        Class<?> cls3;
        Class cls4;
        if (el == null) {
            throw new IllegalArgumentException("null element can not be wrapped");
        }
        if (class$org$xmlpull$v1$builder$adapter$XmlElementAdapter == null) {
            cls = class$("org.xmlpull.v1.builder.adapter.XmlElementAdapter");
            class$org$xmlpull$v1$builder$adapter$XmlElementAdapter = cls;
        } else {
            cls = class$org$xmlpull$v1$builder$adapter$XmlElementAdapter;
        }
        if (!cls.isAssignableFrom(adapterClass)) {
            StringBuffer append = new StringBuffer().append("class for cast/wrap must extend ");
            if (class$org$xmlpull$v1$builder$adapter$XmlElementAdapter == null) {
                cls4 = class$("org.xmlpull.v1.builder.adapter.XmlElementAdapter");
                class$org$xmlpull$v1$builder$adapter$XmlElementAdapter = cls4;
            } else {
                cls4 = class$org$xmlpull$v1$builder$adapter$XmlElementAdapter;
            }
            throw new IllegalArgumentException(append.append(cls4).toString());
        } else if (el instanceof XmlElementAdapter) {
            XmlElementAdapter currentAdap = (XmlElementAdapter) el;
            Class currentAdapClass = currentAdap.getClass();
            if (adapterClass.isAssignableFrom(currentAdapClass)) {
                return currentAdap;
            }
            XmlElementAdapter topAdapter = currentAdap.getTopAdapter();
            XmlElementAdapter currentAdap2 = topAdapter;
            while (currentAdap2.topAdapter != null) {
                Class currentAdapClass2 = currentAdap2.getClass();
                if (currentAdapClass2.isAssignableFrom(adapterClass)) {
                    return currentAdap2;
                }
                if (currentAdap2.target instanceof XmlElementAdapter) {
                    currentAdap2 = (XmlElementAdapter) currentAdap2.target;
                }
            }
            try {
                XmlElementAdapter xmlElementAdapter = currentAdap2;
                Class<?>[] clsArr = new Class[1];
                if (class$org$xmlpull$v1$builder$XmlElement == null) {
                    cls3 = class$("org.xmlpull.v1.builder.XmlElement");
                    class$org$xmlpull$v1$builder$XmlElement = cls3;
                } else {
                    cls3 = class$org$xmlpull$v1$builder$XmlElement;
                }
                clsArr[0] = cls3;
                xmlElementAdapter.topAdapter = (XmlElementAdapter) adapterClass.getConstructor(clsArr).newInstance(topAdapter);
                currentAdap2.topAdapter.setTopAdapter(currentAdap2.topAdapter);
                return currentAdap2.topAdapter;
            } catch (Exception e) {
                throw new XmlBuilderException(new StringBuffer().append("could not create wrapper of ").append(adapterClass).toString(), e);
            }
        } else {
            try {
                Class<?>[] clsArr2 = new Class[1];
                if (class$org$xmlpull$v1$builder$XmlElement == null) {
                    cls2 = class$("org.xmlpull.v1.builder.XmlElement");
                    class$org$xmlpull$v1$builder$XmlElement = cls2;
                } else {
                    cls2 = class$org$xmlpull$v1$builder$XmlElement;
                }
                clsArr2[0] = cls2;
                XmlElementAdapter t = (XmlElementAdapter) adapterClass.getConstructor(clsArr2).newInstance(el);
                return t;
            } catch (Exception e2) {
                throw new XmlBuilderException(new StringBuffer().append("could not wrap element ").append(el).toString(), e2);
            }
        }
    }

    static Class class$(String x0) {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError(x1.getMessage());
        }
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

    private XmlElement fixElementParent(XmlElement el) {
        el.setParent(this);
        return el;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlContainer getRoot() {
        XmlContainer root = this.target.getRoot();
        if (root == this.target) {
            root = this;
        }
        return root;
    }

    @Override // org.xmlpull.v1.builder.XmlElement, org.xmlpull.v1.builder.XmlContained
    public XmlContainer getParent() {
        return this.parent;
    }

    @Override // org.xmlpull.v1.builder.XmlElement, org.xmlpull.v1.builder.XmlContained
    public void setParent(XmlContainer parent) {
        this.parent = parent;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace newNamespace(String prefix, String namespaceName) {
        return this.target.newNamespace(prefix, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute attribute(String attributeName) {
        return this.target.attribute(attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute attribute(XmlNamespace attributeNamespaceName, String attributeName) {
        return this.target.attribute(attributeNamespaceName, attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute findAttribute(String attributeNamespaceName, String attributeName) {
        return this.target.findAttribute(attributeNamespaceName, attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator attributes() {
        return this.target.attributes();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllChildren() {
        this.target.removeAllChildren();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
        return this.target.addAttribute(attributeType, attributePrefix, attributeNamespace, attributeName, attributeValue, specified);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getAttributeValue(String attributeNamespaceName, String attributeName) {
        return this.target.getAttributeValue(attributeNamespaceName, attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
        return this.target.addAttribute(namespace, name, value);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getNamespaceName() {
        return this.target.getNamespaceName();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureChildrenCapacity(int minCapacity) {
        this.target.ensureChildrenCapacity(minCapacity);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator namespaces() {
        return this.target.namespaces();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllAttributes() {
        this.target.removeAllAttributes();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace getNamespace() {
        return this.target.getNamespace();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getBaseUri() {
        return this.target.getBaseUri();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAttribute(XmlAttribute attr) {
        this.target.removeAttribute(attr);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace declareNamespace(String prefix, String namespaceName) {
        return this.target.declareNamespace(prefix, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllNamespaceDeclarations() {
        this.target.removeAllNamespaceDeclarations();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasAttributes() {
        return this.target.hasAttributes();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value, boolean specified) {
        return this.target.addAttribute(type, namespace, name, value, specified);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace declareNamespace(XmlNamespace namespace) {
        return this.target.declareNamespace(namespace);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String name, String value) {
        return this.target.addAttribute(name, value);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasNamespaceDeclarations() {
        return this.target.hasNamespaceDeclarations();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace lookupNamespaceByName(String namespaceName) {
        XmlNamespace ns = this.target.lookupNamespaceByName(namespaceName);
        if (ns == null) {
            XmlContainer p = getParent();
            if (p instanceof XmlElement) {
                XmlElement e = (XmlElement) p;
                return e.lookupNamespaceByName(namespaceName);
            }
        }
        return ns;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
        XmlNamespace ns = this.target.lookupNamespaceByPrefix(namespacePrefix);
        if (ns == null) {
            XmlContainer p = getParent();
            if (p instanceof XmlElement) {
                XmlElement e = (XmlElement) p;
                return e.lookupNamespaceByPrefix(namespacePrefix);
            }
        }
        return ns;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace newNamespace(String namespaceName) {
        return this.target.newNamespace(namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setBaseUri(String baseUri) {
        this.target.setBaseUri(baseUri);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setNamespace(XmlNamespace namespace) {
        this.target.setNamespace(namespace);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
        this.target.ensureNamespaceDeclarationsCapacity(minCapacity);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getName() {
        return this.target.getName();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setName(String name) {
        this.target.setName(name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String type, XmlNamespace namespace, String name, String value) {
        return this.target.addAttribute(type, namespace, name, value);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureAttributeCapacity(int minCapacity) {
        this.target.ensureAttributeCapacity(minCapacity);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
        return this.target.addAttribute(attributeValueToAdd);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(int position) {
        return this.target.element(position);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement requiredElement(XmlNamespace n, String name) {
        return this.target.requiredElement(n, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(XmlNamespace n, String name) {
        return this.target.element(n, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(XmlNamespace n, String name, boolean create) {
        return this.target.element(n, name, create);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterable elements(XmlNamespace n, String name) {
        return this.target.elements(n, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
        return this.target.findElementByName(name, elementToStartLooking);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(XmlNamespace namespace, String name) {
        return this.target.newElement(namespace, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(XmlElement child) {
        return fixElementParent(this.target.addElement(child));
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(int pos, XmlElement child) {
        return fixElementParent(this.target.addElement(pos, child));
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(String name) {
        return fixElementParent(this.target.addElement(name));
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String namespaceName, String name) {
        return this.target.findElementByName(namespaceName, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void addChild(Object child) {
        this.target.addChild(child);
        fixImportedChildParent(child);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void insertChild(int pos, Object childToInsert) {
        this.target.insertChild(pos, childToInsert);
        fixImportedChildParent(childToInsert);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String name) {
        return this.target.findElementByName(name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
        return this.target.findElementByName(namespaceName, name, elementToStartLooking);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeChild(Object child) {
        this.target.removeChild(child);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator children() {
        return this.target.children();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterable requiredElementContent() {
        return this.target.requiredElementContent();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String requiredTextContent() {
        return this.target.requiredTextContent();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasChild(Object child) {
        return this.target.hasChild(child);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(String namespaceName, String name) {
        return this.target.newElement(namespaceName, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(XmlNamespace namespace, String name) {
        return fixElementParent(this.target.addElement(namespace, name));
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasChildren() {
        return this.target.hasChildren();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void addChild(int pos, Object child) {
        this.target.addChild(pos, child);
        fixImportedChildParent(child);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void replaceChild(Object newChild, Object oldChild) {
        this.target.replaceChild(newChild, oldChild);
        fixImportedChildParent(newChild);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(String name) {
        return this.target.newElement(name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void replaceChildrenWithText(String textContent) {
        this.target.replaceChildrenWithText(textContent);
    }
}
