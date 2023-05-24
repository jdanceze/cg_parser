package soot.jimple.infoflow.android.axml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:soot/jimple/infoflow/android/axml/AXmlNode.class */
public class AXmlNode extends AXmlElement {
    protected String tag;
    protected String text;
    protected AXmlNode parent;
    ArrayList<AXmlNode> children;
    Map<String, AXmlAttribute<?>> attributes;

    public AXmlNode(String tag, String ns, AXmlNode parent) {
        this(tag, ns, parent, null);
    }

    public AXmlNode(String tag, String ns, AXmlNode parent, String text) {
        this(tag, ns, parent, true, text);
    }

    public AXmlNode(String tag, String ns, AXmlNode parent, boolean added, String text) {
        super(ns, added);
        this.parent = null;
        this.children = null;
        this.attributes = null;
        this.tag = tag;
        this.parent = parent;
        if (parent != null) {
            parent.addChild(this);
        }
        this.text = text;
    }

    public String getTag() {
        return this.tag;
    }

    public boolean addSiblingBefore(AXmlNode sibling) {
        if (this.parent != null) {
            this.parent.addChild(sibling, this.parent.getChildren().indexOf(this));
            return true;
        }
        return false;
    }

    public boolean addSiblingAfter(AXmlNode sibling) {
        if (this.parent != null) {
            this.parent.addChild(sibling, this.parent.getChildren().indexOf(this) + 1);
            return true;
        }
        return false;
    }

    public AXmlNode addChild(AXmlNode child) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
        return this;
    }

    public AXmlNode addChild(AXmlNode child, int index) {
        if (this.children == null) {
            this.children = new ArrayList<>();
        }
        this.children.add(index, child);
        return this;
    }

    public List<AXmlNode> getChildren() {
        if (this.children == null) {
            return Collections.emptyList();
        }
        return new ArrayList(this.children);
    }

    public List<AXmlNode> getChildrenWithTag(String tag) {
        if (this.children == null) {
            return Collections.emptyList();
        }
        ArrayList<AXmlNode> children = new ArrayList<>();
        Iterator<AXmlNode> it = this.children.iterator();
        while (it.hasNext()) {
            AXmlNode child = it.next();
            if (child.getTag().equals(tag)) {
                children.add(child);
            }
        }
        return children;
    }

    public AXmlAttribute<?> removeAttribute(String key) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.remove(key);
    }

    public Map<String, AXmlAttribute<?>> getAttributes() {
        if (this.attributes == null) {
            return Collections.emptyMap();
        }
        return new HashMap(this.attributes);
    }

    public boolean hasAttribute(String name) {
        if (this.attributes == null) {
            return false;
        }
        return this.attributes.containsKey(name);
    }

    public AXmlAttribute<?> getAttribute(String name) {
        if (this.attributes == null) {
            return null;
        }
        return this.attributes.get(name);
    }

    public void addAttribute(AXmlAttribute<?> attr) {
        if (attr == null) {
            throw new NullPointerException("AXmlAttribute is null");
        }
        if (this.attributes == null) {
            this.attributes = new HashMap();
        }
        this.attributes.put(attr.getName(), attr);
    }

    public void addAttribute(String key, String ns, String value) {
        addAttribute(new AXmlAttribute<>(key, value, ns));
    }

    public AXmlNode getParent() {
        return this.parent;
    }

    public void setParent(AXmlNode parent) {
        this.parent = parent;
    }

    public String toString() {
        String attributes = "";
        if (this.attributes != null) {
            for (AXmlAttribute<?> attrNode : this.attributes.values()) {
                attributes = String.valueOf(attributes) + Instruction.argsep + attrNode;
            }
        }
        return "<" + this.tag + attributes + ">";
    }

    public void removeChild(AXmlNode child) {
        this.children.remove(child);
    }

    public String getText() {
        return this.text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
