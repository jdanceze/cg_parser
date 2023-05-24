package soot.xml;
/* loaded from: gencallgraphv3.jar:soot/xml/XMLNode.class */
public class XMLNode extends XMLRoot {
    public static final int TAG_STRING_BUFFER = 4096;
    public XMLNode next;
    public XMLNode prev;
    public XMLNode parent;
    public XMLNode child;
    public XMLRoot root;

    public XMLNode(String in_name, String in_value, String[] in_attributes, String[] in_values) {
        this.next = null;
        this.prev = null;
        this.parent = null;
        this.child = null;
        this.root = null;
        this.name = in_name;
        this.value = in_value;
        this.attributes = in_attributes;
        this.values = in_values;
    }

    public XMLNode(XMLNode node) {
        this.next = null;
        this.prev = null;
        this.parent = null;
        this.child = null;
        this.root = null;
        if (node != null) {
            this.name = node.name;
            this.value = node.value;
            this.attributes = node.attributes;
            this.values = node.values;
            if (node.child != null) {
                this.child = (XMLNode) node.child.clone();
            }
            if (node.next != null) {
                this.next = (XMLNode) node.next.clone();
            }
        }
    }

    public Object clone() {
        return new XMLNode(this);
    }

    public String toPostString() {
        return toPostString("");
    }

    public String toPostString(String indent) {
        if (this.next != null) {
            return String.valueOf(toString(indent)) + this.next.toPostString(indent);
        }
        return toString(indent);
    }

    public int getNumberOfChildren() {
        int count = 0;
        XMLNode xMLNode = this.child;
        while (true) {
            XMLNode current = xMLNode;
            if (current != null) {
                count++;
                xMLNode = current.next;
            } else {
                return count;
            }
        }
    }

    public XMLNode addAttribute(String attribute, String value) {
        String[] oldAttrs = this.attributes;
        int oldAttrsLen = oldAttrs.length;
        String[] newAttrs = new String[oldAttrsLen + 1];
        System.arraycopy(oldAttrs, 0, newAttrs, 0, oldAttrsLen);
        newAttrs[oldAttrsLen] = attribute.trim();
        this.attributes = newAttrs;
        String[] oldValues = this.values;
        int oldValuesLen = oldValues.length;
        String[] newValues = new String[oldValuesLen + 1];
        System.arraycopy(oldValues, 0, newValues, 0, oldValuesLen);
        newValues[oldValuesLen] = value.trim();
        this.values = newValues;
        return this;
    }

    @Override // soot.xml.XMLRoot
    public String toString() {
        return toString("");
    }

    public String toString(String indent) {
        String endTag;
        String xmlName = eliminateSpaces(this.name);
        StringBuilder beginTag = new StringBuilder(4096);
        beginTag.append(indent);
        beginTag.append('<').append(xmlName);
        if (this.attributes != null) {
            for (int i = 0; i < this.attributes.length; i++) {
                if (this.attributes[i].length() > 0) {
                    String attributeName = eliminateSpaces(this.attributes[i].trim());
                    beginTag.append(' ').append(attributeName).append("=\"");
                    if (this.values != null) {
                        if (i < this.values.length) {
                            beginTag.append(this.values[i].trim()).append('\"');
                        } else {
                            beginTag.append(attributeName.trim()).append('\"');
                        }
                    }
                }
            }
        }
        if (this.child == null && this.value.isEmpty()) {
            beginTag.append(" />\n");
            endTag = null;
        } else {
            beginTag.append('>');
            endTag = "</" + xmlName + ">\n";
        }
        if (!this.value.isEmpty()) {
            beginTag.append(this.value);
        }
        if (this.child != null) {
            beginTag.append('\n');
            beginTag.append(this.child.toPostString(String.valueOf(indent) + "  "));
            beginTag.append(indent);
        }
        if (endTag != null) {
            beginTag.append(endTag);
        }
        return beginTag.toString();
    }

    public XMLNode insertElement(String name) {
        return insertElement(name, "", "", "");
    }

    public XMLNode insertElement(String name, String value) {
        return insertElement(name, value, "", "");
    }

    public XMLNode insertElement(String name, String value, String[] attributes) {
        return insertElement(name, value, attributes, (String[]) null);
    }

    public XMLNode insertElement(String name, String[] attributes, String[] values) {
        return insertElement(name, "", attributes, values);
    }

    public XMLNode insertElement(String name, String value, String attribute, String attributeValue) {
        return insertElement(name, value, new String[]{attribute}, new String[]{attributeValue});
    }

    public XMLNode insertElement(String name, String value, String[] attributes, String[] values) {
        XMLNode newnode = new XMLNode(name, value, attributes, values);
        if (this.parent != null) {
            if (this.parent.child.equals(this)) {
                this.parent.child = newnode;
            }
        } else if (this.prev == null) {
            this.root.child = newnode;
        }
        newnode.child = null;
        newnode.parent = this.parent;
        newnode.prev = this.prev;
        if (newnode.prev != null) {
            newnode.prev.next = newnode;
        }
        this.prev = newnode;
        newnode.next = this;
        return newnode;
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name) {
        return addElement(name, "", "", "");
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name, String value) {
        return addElement(name, value, "", "");
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name, String value, String[] attributes) {
        return addElement(name, value, attributes, (String[]) null);
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name, String[] attributes, String[] values) {
        return addElement(name, "", attributes, values);
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name, String value, String attribute, String attributeValue) {
        return addElement(name, value, new String[]{attribute}, new String[]{attributeValue});
    }

    @Override // soot.xml.XMLRoot
    public XMLNode addElement(String name, String value, String[] attributes, String[] values) {
        XMLNode newnode = new XMLNode(name, value, attributes, values);
        return addElement(newnode);
    }

    public XMLNode addElement(XMLNode node) {
        XMLNode xMLNode = this;
        while (true) {
            XMLNode current = xMLNode;
            if (current.next != null) {
                xMLNode = current.next;
            } else {
                current.next = node;
                node.prev = current;
                return node;
            }
        }
    }

    public XMLNode addChildren(XMLNode children) {
        XMLNode current;
        XMLNode xMLNode = children;
        while (true) {
            XMLNode current2 = xMLNode;
            if (current2 == null) {
                break;
            }
            current2.parent = this;
            xMLNode = current2.next;
        }
        if (this.child == null) {
            this.child = children;
        } else {
            XMLNode xMLNode2 = this.child;
            while (true) {
                current = xMLNode2;
                if (current.next == null) {
                    break;
                }
                xMLNode2 = current.next;
            }
            current.next = children;
        }
        return this;
    }

    public XMLNode addChild(String name) {
        return addChild(name, "", "", "");
    }

    public XMLNode addChild(String name, String value) {
        return addChild(name, value, "", "");
    }

    public XMLNode addChild(String name, String value, String[] attributes) {
        return addChild(name, value, attributes, (String[]) null);
    }

    public XMLNode addChild(String name, String[] attributes, String[] values) {
        return addChild(name, "", attributes, values);
    }

    public XMLNode addChild(String name, String value, String attribute, String attributeValue) {
        return addChild(name, value, new String[]{attribute}, new String[]{attributeValue});
    }

    public XMLNode addChild(String name, String value, String[] attributes, String[] values) {
        XMLNode newnode = new XMLNode(name, value, attributes, values);
        return addChild(newnode);
    }

    public XMLNode addChild(XMLNode node) {
        XMLNode current;
        if (this.child == null) {
            this.child = node;
            node.parent = this;
        } else {
            XMLNode xMLNode = this.child;
            while (true) {
                current = xMLNode;
                if (current.next == null) {
                    break;
                }
                xMLNode = current.next;
            }
            current.next = node;
            node.prev = current;
            node.parent = this;
        }
        return node;
    }

    private String eliminateSpaces(String str) {
        return str.trim().replace(' ', '_');
    }
}
