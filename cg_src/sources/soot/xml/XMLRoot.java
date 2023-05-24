package soot.xml;
/* loaded from: gencallgraphv3.jar:soot/xml/XMLRoot.class */
public class XMLRoot {
    public String name = "";
    public String value = "";
    public String[] attributes = {""};
    public String[] values = {""};
    protected XMLNode child = null;

    public String toString() {
        return "<?xml version=\"1.0\" ?>\n<!DOCTYPE jil SYSTEM \"http://www.sable.mcgill.ca/~flynn/jil/jil10.dtd\">\n" + this.child.toPostString();
    }

    public XMLNode addElement(String name) {
        return addElement(name, "", "", "");
    }

    public XMLNode addElement(String name, String value) {
        return addElement(name, value, "", "");
    }

    public XMLNode addElement(String name, String value, String[] attributes) {
        return addElement(name, value, attributes, (String[]) null);
    }

    public XMLNode addElement(String name, String[] attributes, String[] values) {
        return addElement(name, "", attributes, values);
    }

    public XMLNode addElement(String name, String value, String attribute, String attributeValue) {
        return addElement(name, value, new String[]{attribute}, new String[]{attributeValue});
    }

    public XMLNode addElement(String name, String value, String[] attributes, String[] values) {
        XMLNode current;
        XMLNode newnode = new XMLNode(name, value, attributes, values);
        newnode.root = this;
        if (this.child == null) {
            this.child = newnode;
            newnode.parent = null;
        } else {
            XMLNode xMLNode = this.child;
            while (true) {
                current = xMLNode;
                if (current.next == null) {
                    break;
                }
                xMLNode = current.next;
            }
            current.next = newnode;
            newnode.prev = current;
        }
        return newnode;
    }
}
