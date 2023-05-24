package org.apache.tools.ant.util;

import org.apache.tools.ant.DynamicConfiguratorNS;
import org.apache.tools.ant.DynamicElementNS;
import org.apache.tools.ant.ProjectComponent;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
/* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/XMLFragment.class */
public class XMLFragment extends ProjectComponent implements DynamicElementNS {
    private Document doc = JAXPUtils.getDocumentBuilder().newDocument();
    private DocumentFragment fragment = this.doc.createDocumentFragment();

    public DocumentFragment getFragment() {
        return this.fragment;
    }

    public void addText(String s) {
        addText(this.fragment, s);
    }

    @Override // org.apache.tools.ant.DynamicElementNS
    public Object createDynamicElement(String uri, String name, String qName) {
        Element e;
        if (uri.isEmpty()) {
            e = this.doc.createElement(name);
        } else {
            e = this.doc.createElementNS(uri, qName);
        }
        this.fragment.appendChild(e);
        return new Child(e);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void addText(Node n, String s) {
        String s2 = getProject().replaceProperties(s);
        if (s2 != null && !s2.trim().isEmpty()) {
            Text t = this.doc.createTextNode(s2.trim());
            n.appendChild(t);
        }
    }

    /* loaded from: gencallgraphv3.jar:ant-1.10.11.jar:org/apache/tools/ant/util/XMLFragment$Child.class */
    public class Child implements DynamicConfiguratorNS {
        private Element e;

        Child(Element e) {
            this.e = e;
        }

        public void addText(String s) {
            XMLFragment.this.addText(this.e, s);
        }

        @Override // org.apache.tools.ant.DynamicAttributeNS
        public void setDynamicAttribute(String uri, String name, String qName, String value) {
            if (uri.isEmpty()) {
                this.e.setAttribute(name, value);
            } else {
                this.e.setAttributeNS(uri, qName, value);
            }
        }

        @Override // org.apache.tools.ant.DynamicElementNS
        public Object createDynamicElement(String uri, String name, String qName) {
            Element e2 = uri.isEmpty() ? XMLFragment.this.doc.createElement(name) : XMLFragment.this.doc.createElementNS(uri, qName);
            this.e.appendChild(e2);
            return new Child(e2);
        }
    }
}
