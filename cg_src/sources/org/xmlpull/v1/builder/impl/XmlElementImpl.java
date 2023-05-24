package org.xmlpull.v1.builder.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.xmlpull.v1.builder.Iterable;
import org.xmlpull.v1.builder.XmlAttribute;
import org.xmlpull.v1.builder.XmlBuilderException;
import org.xmlpull.v1.builder.XmlCharacters;
import org.xmlpull.v1.builder.XmlContained;
import org.xmlpull.v1.builder.XmlContainer;
import org.xmlpull.v1.builder.XmlDocument;
import org.xmlpull.v1.builder.XmlElement;
import org.xmlpull.v1.builder.XmlNamespace;
/* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlElementImpl.class */
public class XmlElementImpl implements XmlElement {
    private XmlContainer parent;
    private XmlNamespace namespace;
    private String name;
    private List attrs;
    private List nsList;
    private List children;
    private static final Iterator EMPTY_ITERATOR = new EmptyIterator(null);
    private static final Iterable EMPTY_ITERABLE = new Iterable() { // from class: org.xmlpull.v1.builder.impl.XmlElementImpl.3
        @Override // org.xmlpull.v1.builder.Iterable
        public Iterator iterator() {
            return XmlElementImpl.EMPTY_ITERATOR;
        }
    };

    @Override // org.xmlpull.v1.builder.XmlElement
    public Object clone() throws CloneNotSupportedException {
        XmlElementImpl cloned = (XmlElementImpl) super.clone();
        cloned.parent = null;
        cloned.attrs = cloneList(cloned, this.attrs);
        cloned.nsList = cloneList(cloned, this.nsList);
        cloned.children = cloneList(cloned, this.children);
        if (cloned.children != null) {
            for (int i = 0; i < cloned.children.size(); i++) {
                Object member = cloned.children.get(i);
                if (member instanceof XmlContained) {
                    XmlContained contained = (XmlContained) member;
                    if (contained.getParent() == this) {
                        contained.setParent(null);
                        contained.setParent(cloned);
                    }
                }
            }
        }
        return cloned;
    }

    private List cloneList(XmlElementImpl cloned, List list) throws CloneNotSupportedException {
        Object newMember;
        if (list == null) {
            return null;
        }
        List newList = new ArrayList(list.size());
        for (int i = 0; i < list.size(); i++) {
            Object member = list.get(i);
            if ((member instanceof XmlNamespace) || (member instanceof String)) {
                newMember = member;
            } else if (member instanceof XmlElement) {
                XmlElement el = (XmlElement) member;
                newMember = el.clone();
            } else if (member instanceof XmlAttribute) {
                XmlAttribute attr = (XmlAttribute) member;
                newMember = new XmlAttributeImpl(cloned, attr.getType(), attr.getNamespace(), attr.getName(), attr.getValue(), attr.isSpecified());
            } else if (member instanceof Cloneable) {
                try {
                    newMember = member.getClass().getMethod("clone", null).invoke(member, null);
                } catch (Exception e) {
                    throw new CloneNotSupportedException(new StringBuffer().append("failed to call clone() on  ").append(member).append(e).toString());
                }
            } else {
                throw new CloneNotSupportedException();
            }
            newList.add(newMember);
        }
        return newList;
    }

    XmlElementImpl(String name) {
        this.name = name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlElementImpl(XmlNamespace namespace, String name) {
        this.namespace = namespace;
        this.name = name;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public XmlElementImpl(String namespaceName, String name) {
        if (namespaceName != null) {
            this.namespace = new XmlNamespaceImpl(null, namespaceName);
        }
        this.name = name;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlContainer getRoot() {
        XmlContainer root;
        XmlContainer xmlContainer = this;
        while (true) {
            root = xmlContainer;
            if (!(root instanceof XmlElement)) {
                break;
            }
            XmlElement el = (XmlElement) root;
            if (el.getParent() == null) {
                break;
            }
            xmlContainer = el.getParent();
        }
        return root;
    }

    @Override // org.xmlpull.v1.builder.XmlElement, org.xmlpull.v1.builder.XmlContained
    public XmlContainer getParent() {
        return this.parent;
    }

    @Override // org.xmlpull.v1.builder.XmlElement, org.xmlpull.v1.builder.XmlContained
    public void setParent(XmlContainer parent) {
        if (parent != null && (parent instanceof XmlDocument)) {
            XmlDocument doc = (XmlDocument) parent;
            if (doc.getDocumentElement() != this) {
                throw new XmlBuilderException("this element must be root document element to have document set as parent but already different element is set as root document element");
            }
        }
        this.parent = parent;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace getNamespace() {
        return this.namespace;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getNamespaceName() {
        if (this.namespace != null) {
            return this.namespace.getNamespaceName();
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setNamespace(XmlNamespace namespace) {
        this.namespace = namespace;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getName() {
        return this.name;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        return new StringBuffer().append("name[").append(this.name).append("]").append(this.namespace != null ? new StringBuffer().append(" namespace[").append(this.namespace.getNamespaceName()).append("]").toString() : "").toString();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getBaseUri() {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void setBaseUri(String baseUri) {
        throw new XmlBuilderException("not implemented");
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator attributes() {
        if (this.attrs == null) {
            return EMPTY_ITERATOR;
        }
        return this.attrs.iterator();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(XmlAttribute attributeValueToAdd) {
        if (this.attrs == null) {
            ensureAttributeCapacity(5);
        }
        this.attrs.add(attributeValueToAdd);
        return attributeValueToAdd;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(XmlNamespace namespace, String name, String value) {
        return addAttribute("CDATA", namespace, name, value, false);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String name, String value) {
        return addAttribute("CDATA", null, name, value, false);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value) {
        return addAttribute(attributeType, namespace, name, value, false);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String attributeType, XmlNamespace namespace, String name, String value, boolean specified) {
        XmlAttribute a = new XmlAttributeImpl(this, attributeType, namespace, name, value, specified);
        return addAttribute(a);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute addAttribute(String attributeType, String attributePrefix, String attributeNamespace, String attributeName, String attributeValue, boolean specified) {
        XmlNamespace n = newNamespace(attributePrefix, attributeNamespace);
        return addAttribute(attributeType, n, attributeName, attributeValue, specified);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureAttributeCapacity(int minCapacity) {
        if (this.attrs == null) {
            this.attrs = new ArrayList(minCapacity);
        } else {
            ((ArrayList) this.attrs).ensureCapacity(minCapacity);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String getAttributeValue(String attributeNamespaceName, String attributeName) {
        XmlAttribute xat = findAttribute(attributeNamespaceName, attributeName);
        if (xat != null) {
            return xat.getValue();
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasAttributes() {
        return this.attrs != null && this.attrs.size() > 0;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute attribute(String attributeName) {
        return attribute(null, attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute attribute(XmlNamespace attributeNamespace, String attributeName) {
        return findAttribute(attributeNamespace != null ? attributeNamespace.getNamespaceName() : null, attributeName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlAttribute findAttribute(String attributeNamespace, String attributeName) {
        if (attributeName == null) {
            throw new IllegalArgumentException("attribute name ca not ber null");
        }
        if (this.attrs == null) {
            return null;
        }
        int length = this.attrs.size();
        for (int i = 0; i < length; i++) {
            XmlAttribute a = (XmlAttribute) this.attrs.get(i);
            String aName = a.getName();
            if (aName == attributeName || attributeName.equals(aName)) {
                if (attributeNamespace != null) {
                    String aNamespace = a.getNamespaceName();
                    if (attributeNamespace.equals(aNamespace)) {
                        return a;
                    }
                    if (attributeNamespace == "" && aNamespace == null) {
                        return a;
                    }
                } else if (a.getNamespace() == null) {
                    return a;
                } else {
                    if (a.getNamespace().getNamespaceName() == "") {
                        return a;
                    }
                }
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllAttributes() {
        this.attrs = null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAttribute(XmlAttribute attr) {
        if (this.attrs == null) {
            throw new XmlBuilderException("this element has no attributes to remove");
        }
        for (int i = 0; i < this.attrs.size(); i++) {
            if (this.attrs.get(i).equals(attr)) {
                this.attrs.remove(i);
                return;
            }
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace declareNamespace(String prefix, String namespaceName) {
        if (prefix == null) {
            throw new XmlBuilderException("namespace added to element must have not null prefix");
        }
        XmlNamespace n = newNamespace(prefix, namespaceName);
        return declareNamespace(n);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace declareNamespace(XmlNamespace n) {
        if (n.getPrefix() == null) {
            throw new XmlBuilderException("namespace added to element must have not null prefix");
        }
        if (this.nsList == null) {
            ensureNamespaceDeclarationsCapacity(5);
        }
        this.nsList.add(n);
        return n;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasNamespaceDeclarations() {
        return this.nsList != null && this.nsList.size() > 0;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace lookupNamespaceByPrefix(String namespacePrefix) {
        if (namespacePrefix == null) {
            throw new IllegalArgumentException("namespace prefix can not be null");
        }
        if (hasNamespaceDeclarations()) {
            int length = this.nsList.size();
            for (int i = 0; i < length; i++) {
                XmlNamespace n = (XmlNamespace) this.nsList.get(i);
                if (namespacePrefix.equals(n.getPrefix())) {
                    return n;
                }
            }
        }
        if (this.parent != null && (this.parent instanceof XmlElement)) {
            return ((XmlElement) this.parent).lookupNamespaceByPrefix(namespacePrefix);
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace lookupNamespaceByName(String namespaceName) {
        if (namespaceName == null) {
            throw new IllegalArgumentException("namespace name can not ber null");
        }
        if (hasNamespaceDeclarations()) {
            int length = this.nsList.size();
            for (int i = 0; i < length; i++) {
                XmlNamespace n = (XmlNamespace) this.nsList.get(i);
                if (namespaceName.equals(n.getNamespaceName())) {
                    return n;
                }
            }
        }
        if (this.parent != null && (this.parent instanceof XmlElement)) {
            return ((XmlElement) this.parent).lookupNamespaceByName(namespaceName);
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator namespaces() {
        if (this.nsList == null) {
            return EMPTY_ITERATOR;
        }
        return this.nsList.iterator();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace newNamespace(String namespaceName) {
        return newNamespace(null, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlNamespace newNamespace(String prefix, String namespaceName) {
        return new XmlNamespaceImpl(prefix, namespaceName);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureNamespaceDeclarationsCapacity(int minCapacity) {
        if (this.nsList == null) {
            this.nsList = new ArrayList(minCapacity);
        } else {
            ((ArrayList) this.nsList).ensureCapacity(minCapacity);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllNamespaceDeclarations() {
        this.nsList = null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void addChild(Object child) {
        if (child == null) {
            throw new NullPointerException();
        }
        if (this.children == null) {
            ensureChildrenCapacity(1);
        }
        this.children.add(child);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void addChild(int index, Object child) {
        if (this.children == null) {
            ensureChildrenCapacity(1);
        }
        this.children.add(index, child);
    }

    private void checkChildParent(Object child) {
        if (child instanceof XmlContainer) {
            if (!(child instanceof XmlElement)) {
                if (child instanceof XmlDocument) {
                    throw new XmlBuilderException("docuemet can not be stored as element child");
                }
                return;
            }
            XmlElement elChild = (XmlElement) child;
            XmlContainer childParent = elChild.getParent();
            if (childParent != null && childParent != this.parent) {
                throw new XmlBuilderException("child must have no parent to be added to this node");
            }
        }
    }

    private void setChildParent(Object child) {
        if (child instanceof XmlElement) {
            XmlElement elChild = (XmlElement) child;
            elChild.setParent(this);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(XmlElement element) {
        checkChildParent(element);
        addChild(element);
        setChildParent(element);
        return element;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(int pos, XmlElement element) {
        checkChildParent(element);
        addChild(pos, element);
        setChildParent(element);
        return element;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(XmlNamespace namespace, String name) {
        XmlElement el = newElement(namespace, name);
        addChild(el);
        setChildParent(el);
        return el;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement addElement(String name) {
        return addElement((XmlNamespace) null, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterator children() {
        if (this.children == null) {
            return EMPTY_ITERATOR;
        }
        return this.children.iterator();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterable requiredElementContent() {
        if (this.children == null) {
            return EMPTY_ITERABLE;
        }
        return new Iterable(this) { // from class: org.xmlpull.v1.builder.impl.XmlElementImpl.1
            private final XmlElementImpl this$0;

            {
                this.this$0 = this;
            }

            @Override // org.xmlpull.v1.builder.Iterable
            public Iterator iterator() {
                return new RequiredElementContentIterator(this.this$0.children.iterator());
            }
        };
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public String requiredTextContent() {
        if (this.children == null || this.children.size() == 0) {
            return "";
        }
        if (this.children.size() == 1) {
            Object child = this.children.get(0);
            if (child instanceof String) {
                return child.toString();
            }
            if (child instanceof XmlCharacters) {
                return ((XmlCharacters) child).getText();
            }
            throw new XmlBuilderException(new StringBuffer().append("expected text content and not ").append(child != null ? child.getClass() : null).append(" with '").append(child).append("'").toString());
        }
        Iterator i = children();
        StringBuffer buf = new StringBuffer();
        while (i.hasNext()) {
            Object child2 = i.next();
            if (child2 instanceof String) {
                buf.append(child2.toString());
            } else if (child2 instanceof XmlCharacters) {
                buf.append(((XmlCharacters) child2).getText());
            } else {
                throw new XmlBuilderException(new StringBuffer().append("expected text content and not ").append(child2.getClass()).append(" with '").append(child2).append("'").toString());
            }
        }
        return buf.toString();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void ensureChildrenCapacity(int minCapacity) {
        if (this.children == null) {
            this.children = new ArrayList(minCapacity);
        } else {
            ((ArrayList) this.children).ensureCapacity(minCapacity);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(int position) {
        if (this.children == null) {
            return null;
        }
        int length = this.children.size();
        int count = 0;
        if (position >= 0 && position < length + 1) {
            for (int pos = 0; pos < length; pos++) {
                Object child = this.children.get(pos);
                if (child instanceof XmlElement) {
                    count++;
                    if (count == position) {
                        return (XmlElement) child;
                    }
                }
            }
            throw new IndexOutOfBoundsException(new StringBuffer().append("position ").append(position).append(" too big as only ").append(count).append(" element(s) available").toString());
        }
        throw new IndexOutOfBoundsException(new StringBuffer().append("position ").append(position).append(" bigger or equal to ").append(length).append(" children").toString());
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement requiredElement(XmlNamespace n, String name) throws XmlBuilderException {
        XmlElement el = element(n, name);
        if (el == null) {
            throw new XmlBuilderException(new StringBuffer().append("could not find element with name ").append(name).append(" in namespace ").append(n != null ? n.getNamespaceName() : null).toString());
        }
        return el;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(XmlNamespace n, String name) {
        return element(n, name, false);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement element(XmlNamespace n, String name, boolean create) {
        XmlElement e = n != null ? findElementByName(n.getNamespaceName(), name) : findElementByName(name);
        if (e != null) {
            return e;
        }
        if (create) {
            return addElement(n, name);
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public Iterable elements(XmlNamespace n, String name) {
        return new Iterable(this, n, name) { // from class: org.xmlpull.v1.builder.impl.XmlElementImpl.2
            private final XmlNamespace val$n;
            private final String val$name;
            private final XmlElementImpl this$0;

            {
                this.this$0 = this;
                this.val$n = n;
                this.val$name = name;
            }

            @Override // org.xmlpull.v1.builder.Iterable
            public Iterator iterator() {
                return new ElementsSimpleIterator(this.this$0, this.val$n, this.val$name, this.this$0.children());
            }
        };
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String name) {
        if (this.children == null) {
            return null;
        }
        int length = this.children.size();
        for (int i = 0; i < length; i++) {
            Object child = this.children.get(i);
            if (child instanceof XmlElement) {
                XmlElement childEl = (XmlElement) child;
                if (name.equals(childEl.getName())) {
                    return childEl;
                }
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String namespaceName, String name, XmlElement elementToStartLooking) {
        throw new UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String name, XmlElement elementToStartLooking) {
        throw new UnsupportedOperationException();
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement findElementByName(String namespaceName, String name) {
        if (this.children == null) {
            return null;
        }
        int length = this.children.size();
        for (int i = 0; i < length; i++) {
            Object child = this.children.get(i);
            if (child instanceof XmlElement) {
                XmlElement childEl = (XmlElement) child;
                XmlNamespace namespace = childEl.getNamespace();
                if (namespace != null) {
                    if (name.equals(childEl.getName()) && namespaceName.equals(namespace.getNamespaceName())) {
                        return childEl;
                    }
                } else if (name.equals(childEl.getName()) && namespaceName == null) {
                    return childEl;
                }
            }
        }
        return null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasChild(Object child) {
        if (this.children == null) {
            return false;
        }
        for (int i = 0; i < this.children.size(); i++) {
            if (this.children.get(i) == child) {
                return true;
            }
        }
        return false;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public boolean hasChildren() {
        return this.children != null && this.children.size() > 0;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void insertChild(int pos, Object childToInsert) {
        if (this.children == null) {
            ensureChildrenCapacity(1);
        }
        this.children.add(pos, childToInsert);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(String name) {
        return newElement((XmlNamespace) null, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(String namespace, String name) {
        return new XmlElementImpl(namespace, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public XmlElement newElement(XmlNamespace namespace, String name) {
        return new XmlElementImpl(namespace, name);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void replaceChild(Object newChild, Object oldChild) {
        if (newChild == null) {
            throw new IllegalArgumentException("new child to replace can not be null");
        }
        if (oldChild == null) {
            throw new IllegalArgumentException("old child to replace can not be null");
        }
        if (!hasChildren()) {
            throw new XmlBuilderException("no children available for replacement");
        }
        int pos = this.children.indexOf(oldChild);
        if (pos == -1) {
            throw new XmlBuilderException("could not find child to replace");
        }
        this.children.set(pos, newChild);
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeAllChildren() {
        this.children = null;
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void removeChild(Object child) {
        if (child == null) {
            throw new IllegalArgumentException("child to remove can not be null");
        }
        if (!hasChildren()) {
            throw new XmlBuilderException("no children to remove");
        }
        int pos = this.children.indexOf(child);
        if (pos != -1) {
            this.children.remove(pos);
        }
    }

    @Override // org.xmlpull.v1.builder.XmlElement
    public void replaceChildrenWithText(String textContent) {
        removeAllChildren();
        addChild(textContent);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final boolean isWhiteSpace(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            if (txt.charAt(i) != ' ' && txt.charAt(i) != '\n' && txt.charAt(i) != '\t' && txt.charAt(i) != '\r') {
                return false;
            }
        }
        return true;
    }

    /* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlElementImpl$ElementsSimpleIterator.class */
    private class ElementsSimpleIterator implements Iterator {
        private Iterator children;
        private XmlElement currentEl;
        private XmlNamespace n;
        private String name;
        private final XmlElementImpl this$0;

        ElementsSimpleIterator(XmlElementImpl xmlElementImpl, XmlNamespace n, String name, Iterator children) {
            this.this$0 = xmlElementImpl;
            this.children = children;
            this.n = n;
            this.name = name;
            findNextEl();
        }

        private void findNextEl() {
            this.currentEl = null;
            while (this.children.hasNext()) {
                Object child = this.children.next();
                if (child instanceof XmlElement) {
                    XmlElement el = (XmlElement) child;
                    if (this.name == null || el.getName() == this.name || this.name.equals(el.getName())) {
                        if (this.n == null || el.getNamespace() == this.n || this.n.equals(el.getNamespace())) {
                            this.currentEl = el;
                            return;
                        }
                    }
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.currentEl != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.currentEl == null) {
                throw new XmlBuilderException("this iterator has no content and next() is not allowed");
            }
            XmlElement el = this.currentEl;
            findNextEl();
            return el;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new XmlBuilderException("this element iterator does nto support remove()");
        }
    }

    /* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlElementImpl$RequiredElementContentIterator.class */
    private static class RequiredElementContentIterator implements Iterator {
        private Iterator children;
        private XmlElement currentEl;

        RequiredElementContentIterator(Iterator children) {
            this.children = children;
            findNextEl();
        }

        private void findNextEl() {
            this.currentEl = null;
            while (this.children.hasNext()) {
                Object child = this.children.next();
                if (child instanceof XmlElement) {
                    this.currentEl = (XmlElement) child;
                    return;
                } else if (child instanceof String) {
                    String s = child.toString();
                    if (false == XmlElementImpl.isWhiteSpace(s)) {
                        throw new XmlBuilderException("only whitespace string children allowed for non mixed element content");
                    }
                } else if (child instanceof XmlCharacters) {
                    XmlCharacters xc = (XmlCharacters) child;
                    if (!Boolean.TRUE.equals(xc.isWhitespaceContent()) || false == XmlElementImpl.isWhiteSpace(xc.getText())) {
                        throw new XmlBuilderException("only whitespace characters children allowed for non mixed element content");
                    }
                } else {
                    throw new XmlBuilderException(new StringBuffer().append("only whitespace characters and element children allowed for non mixed element content and not ").append(child.getClass()).toString());
                }
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.currentEl != null;
        }

        @Override // java.util.Iterator
        public Object next() {
            if (this.currentEl == null) {
                throw new XmlBuilderException("this iterator has no content and next() is not allowed");
            }
            XmlElement el = this.currentEl;
            findNextEl();
            return el;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new XmlBuilderException("this iterator does nto support remove()");
        }
    }

    /* loaded from: gencallgraphv3.jar:xpp3-1.1.4c.jar:org/xmlpull/v1/builder/impl/XmlElementImpl$EmptyIterator.class */
    private static class EmptyIterator implements Iterator {
        private EmptyIterator() {
        }

        EmptyIterator(AnonymousClass1 x0) {
            this();
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return false;
        }

        @Override // java.util.Iterator
        public Object next() {
            throw new XmlBuilderException("this iterator has no content and next() is not allowed");
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new XmlBuilderException("this iterator has no content and remove() is not allowed");
        }
    }
}
