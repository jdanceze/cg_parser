package org.apache.tools.ant;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.tools.ant.IntrospectionHelper;
import org.apache.tools.ant.attribute.EnableAttribute;
import org.apache.tools.ant.taskdefs.MacroDef;
import org.apache.tools.ant.taskdefs.MacroInstance;
import org.xml.sax.AttributeList;
import org.xml.sax.helpers.AttributeListImpl;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/RuntimeConfigurable.class */
public class RuntimeConfigurable implements Serializable {
    private static final long serialVersionUID = 1;
    @Deprecated
    private transient AttributeList attributes;
    private String elementTag = null;
    private List<RuntimeConfigurable> children = null;
    private transient Object wrappedObject = null;
    private transient boolean namespacedAttribute = false;
    private LinkedHashMap<String, Object> attributeMap = null;
    private StringBuffer characters = null;
    private boolean proxyConfigured = false;
    private String polyType = null;
    private String id = null;

    public RuntimeConfigurable(Object proxy, String elementTag) {
        setProxy(proxy);
        setElementTag(elementTag);
        if (proxy instanceof Task) {
            ((Task) proxy).setRuntimeConfigurableWrapper(this);
        }
    }

    public synchronized void setProxy(Object proxy) {
        this.wrappedObject = proxy;
        this.proxyConfigured = false;
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/RuntimeConfigurable$EnableAttributeConsumer.class */
    private static class EnableAttributeConsumer {
        private EnableAttributeConsumer() {
        }

        public void add(EnableAttribute b) {
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/RuntimeConfigurable$AttributeComponentInformation.class */
    public static class AttributeComponentInformation {
        String componentName;
        boolean restricted;

        private AttributeComponentInformation(String componentName, boolean restricted) {
            this.componentName = componentName;
            this.restricted = restricted;
        }

        public String getComponentName() {
            return this.componentName;
        }

        public boolean isRestricted() {
            return this.restricted;
        }
    }

    private AttributeComponentInformation isRestrictedAttribute(String name, ComponentHelper componentHelper) {
        if (!name.contains(":")) {
            return new AttributeComponentInformation(null, false);
        }
        String componentName = attrToComponent(name);
        String ns = ProjectHelper.extractUriFromComponentName(componentName);
        if (componentHelper.getRestrictedDefinitions(ProjectHelper.nsToComponentName(ns)) == null) {
            return new AttributeComponentInformation(null, false);
        }
        return new AttributeComponentInformation(componentName, true);
    }

    public boolean isEnabled(UnknownElement owner) {
        if (!this.namespacedAttribute) {
            return true;
        }
        ComponentHelper componentHelper = ComponentHelper.getComponentHelper(owner.getProject());
        IntrospectionHelper ih = IntrospectionHelper.getHelper(owner.getProject(), EnableAttributeConsumer.class);
        for (Map.Entry<String, Object> entry : this.attributeMap.entrySet()) {
            AttributeComponentInformation attributeComponentInformation = isRestrictedAttribute(entry.getKey(), componentHelper);
            if (attributeComponentInformation.isRestricted()) {
                String value = (String) entry.getValue();
                try {
                    EnableAttribute enable = (EnableAttribute) ih.createElement(owner.getProject(), new EnableAttributeConsumer(), attributeComponentInformation.getComponentName());
                    if (enable != null && !enable.isEnabled(owner, owner.getProject().replaceProperties(value))) {
                        return false;
                    }
                } catch (BuildException e) {
                    throw new BuildException("Unsupported attribute " + attributeComponentInformation.getComponentName());
                }
            }
        }
        return true;
    }

    private String attrToComponent(String a) {
        int p1 = a.lastIndexOf(58);
        int p2 = a.lastIndexOf(58, p1 - 1);
        return a.substring(0, p2) + a.substring(p1);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized void setCreator(IntrospectionHelper.Creator creator) {
    }

    public synchronized Object getProxy() {
        return this.wrappedObject;
    }

    public synchronized String getId() {
        return this.id;
    }

    public synchronized String getPolyType() {
        return this.polyType;
    }

    public synchronized void setPolyType(String polyType) {
        this.polyType = polyType;
    }

    @Deprecated
    public synchronized void setAttributes(AttributeList attributes) {
        this.attributes = new AttributeListImpl(attributes);
        for (int i = 0; i < attributes.getLength(); i++) {
            setAttribute(attributes.getName(i), attributes.getValue(i));
        }
    }

    public synchronized void setAttribute(String name, String value) {
        if (name.contains(":")) {
            this.namespacedAttribute = true;
        }
        setAttribute(name, (Object) value);
    }

    public synchronized void setAttribute(String name, Object value) {
        if (name.equalsIgnoreCase(ProjectHelper.ANT_TYPE)) {
            this.polyType = value == null ? null : value.toString();
            return;
        }
        if (this.attributeMap == null) {
            this.attributeMap = new LinkedHashMap<>();
        }
        if ("refid".equalsIgnoreCase(name) && !this.attributeMap.isEmpty()) {
            LinkedHashMap<String, Object> newAttributeMap = new LinkedHashMap<>();
            newAttributeMap.put(name, value);
            newAttributeMap.putAll(this.attributeMap);
            this.attributeMap = newAttributeMap;
        } else {
            this.attributeMap.put(name, value);
        }
        if ("id".equals(name)) {
            this.id = value == null ? null : value.toString();
        }
    }

    public synchronized void removeAttribute(String name) {
        this.attributeMap.remove(name);
    }

    public synchronized Hashtable<String, Object> getAttributeMap() {
        return new Hashtable<>(this.attributeMap == null ? Collections.emptyMap() : this.attributeMap);
    }

    @Deprecated
    public synchronized AttributeList getAttributes() {
        return this.attributes;
    }

    public synchronized void addChild(RuntimeConfigurable child) {
        this.children = this.children == null ? new ArrayList<>() : this.children;
        this.children.add(child);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public synchronized RuntimeConfigurable getChild(int index) {
        return this.children.get(index);
    }

    public synchronized Enumeration<RuntimeConfigurable> getChildren() {
        return this.children == null ? Collections.emptyEnumeration() : Collections.enumeration(this.children);
    }

    public synchronized void addText(String data) {
        if (data.isEmpty()) {
            return;
        }
        this.characters = this.characters == null ? new StringBuffer(data) : this.characters.append(data);
    }

    public synchronized void addText(char[] buf, int start, int count) {
        if (count == 0) {
            return;
        }
        this.characters = (this.characters == null ? new StringBuffer(count) : this.characters).append(buf, start, count);
    }

    public synchronized StringBuffer getText() {
        return this.characters == null ? new StringBuffer(0) : this.characters;
    }

    public synchronized void setElementTag(String elementTag) {
        this.elementTag = elementTag;
    }

    public synchronized String getElementTag() {
        return this.elementTag;
    }

    public void maybeConfigure(Project p) throws BuildException {
        maybeConfigure(p, true);
    }

    public synchronized void maybeConfigure(Project p, boolean configureChildren) throws BuildException {
        Object attrValue;
        if (this.proxyConfigured) {
            return;
        }
        Object target = this.wrappedObject instanceof TypeAdapter ? ((TypeAdapter) this.wrappedObject).getProxy() : this.wrappedObject;
        IntrospectionHelper ih = IntrospectionHelper.getHelper(p, target.getClass());
        ComponentHelper componentHelper = ComponentHelper.getComponentHelper(p);
        if (this.attributeMap != null) {
            for (Map.Entry<String, Object> entry : this.attributeMap.entrySet()) {
                String name = entry.getKey();
                AttributeComponentInformation attributeComponentInformation = isRestrictedAttribute(name, componentHelper);
                if (!attributeComponentInformation.isRestricted()) {
                    Object value = entry.getValue();
                    if (value instanceof Evaluable) {
                        attrValue = ((Evaluable) value).eval();
                    } else {
                        attrValue = PropertyHelper.getPropertyHelper(p).parseProperties(value.toString());
                    }
                    if (target instanceof MacroInstance) {
                        for (MacroDef.Attribute attr : ((MacroInstance) target).getMacroDef().getAttributes()) {
                            if (attr.getName().equals(name)) {
                                if (!attr.isDoubleExpanding()) {
                                    attrValue = value;
                                }
                            }
                        }
                    }
                    try {
                        ih.setAttribute(p, target, name, attrValue);
                    } catch (UnsupportedAttributeException be) {
                        if (!"id".equals(name)) {
                            if (getElementTag() == null) {
                                throw be;
                            }
                            throw new BuildException(getElementTag() + " doesn't support the \"" + be.getAttribute() + "\" attribute", be);
                        }
                    } catch (BuildException be2) {
                        if (!"id".equals(name)) {
                            throw be2;
                        }
                    }
                }
            }
        }
        if (this.characters != null) {
            ProjectHelper.addText(p, this.wrappedObject, this.characters.substring(0));
        }
        if (this.id != null) {
            p.addReference(this.id, this.wrappedObject);
        }
        this.proxyConfigured = true;
    }

    public void reconfigure(Project p) {
        this.proxyConfigured = false;
        maybeConfigure(p);
    }

    public void applyPreSet(RuntimeConfigurable r) {
        if (r.attributeMap != null) {
            for (String name : r.attributeMap.keySet()) {
                if (this.attributeMap == null || this.attributeMap.get(name) == null) {
                    setAttribute(name, (String) r.attributeMap.get(name));
                }
            }
        }
        this.polyType = this.polyType == null ? r.polyType : this.polyType;
        if (r.children != null) {
            List<RuntimeConfigurable> newChildren = new ArrayList<>(r.children);
            if (this.children != null) {
                newChildren.addAll(this.children);
            }
            this.children = newChildren;
        }
        if (r.characters != null) {
            if (this.characters == null || this.characters.toString().trim().isEmpty()) {
                this.characters = new StringBuffer(r.characters.toString());
            }
        }
    }
}
