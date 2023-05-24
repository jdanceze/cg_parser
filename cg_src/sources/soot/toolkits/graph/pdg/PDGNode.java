package soot.toolkits.graph.pdg;

import java.util.ArrayList;
import java.util.List;
import soot.toolkits.graph.Block;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/PDGNode.class */
public class PDGNode {
    protected Type m_type;
    protected Object m_node;
    protected List<PDGNode> m_dependents = new ArrayList();
    protected List<PDGNode> m_backDependents = new ArrayList();
    protected PDGNode m_next = null;
    protected PDGNode m_prev = null;
    protected Attribute m_attrib = Attribute.NORMAL;
    protected boolean m_visited = false;

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/PDGNode$Attribute.class */
    public enum Attribute {
        NORMAL,
        ENTRY,
        CONDHEADER,
        LOOPHEADER;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Attribute[] valuesCustom() {
            Attribute[] valuesCustom = values();
            int length = valuesCustom.length;
            Attribute[] attributeArr = new Attribute[length];
            System.arraycopy(valuesCustom, 0, attributeArr, 0, length);
            return attributeArr;
        }
    }

    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/pdg/PDGNode$Type.class */
    public enum Type {
        REGION,
        CFGNODE;

        /* renamed from: values  reason: to resolve conflict with enum method */
        public static Type[] valuesCustom() {
            Type[] valuesCustom = values();
            int length = valuesCustom.length;
            Type[] typeArr = new Type[length];
            System.arraycopy(valuesCustom, 0, typeArr, 0, length);
            return typeArr;
        }
    }

    public PDGNode(Object obj, Type t) {
        this.m_node = null;
        this.m_node = obj;
        this.m_type = t;
    }

    public Type getType() {
        return this.m_type;
    }

    public void setType(Type t) {
        this.m_type = t;
    }

    public Object getNode() {
        return this.m_node;
    }

    public void setNext(PDGNode n) {
        this.m_next = n;
    }

    public PDGNode getNext() {
        return this.m_next;
    }

    public void setPrev(PDGNode n) {
        this.m_prev = n;
    }

    public PDGNode getPrev() {
        return this.m_prev;
    }

    public void setVisited(boolean v) {
        this.m_visited = v;
    }

    public boolean getVisited() {
        return this.m_visited;
    }

    public void setNode(Object obj) {
        this.m_node = obj;
    }

    public Attribute getAttrib() {
        return this.m_attrib;
    }

    public void setAttrib(Attribute a) {
        this.m_attrib = a;
    }

    public void addDependent(PDGNode node) {
        if (!this.m_dependents.contains(node)) {
            this.m_dependents.add(node);
        }
    }

    public void addBackDependent(PDGNode node) {
        this.m_backDependents.add(node);
    }

    public void removeDependent(PDGNode node) {
        this.m_dependents.remove(node);
    }

    public List<PDGNode> getDependents() {
        return this.m_dependents;
    }

    public List<PDGNode> getBackDependets() {
        return this.m_backDependents;
    }

    public String toString() {
        new String();
        String s = "Type: " + (this.m_type == Type.REGION ? "REGION: " : "CFGNODE: ");
        return String.valueOf(s) + this.m_node;
    }

    public String toShortString() {
        String s;
        new String();
        String s2 = "Type: " + (this.m_type == Type.REGION ? "REGION: " : "CFGNODE: ");
        if (this.m_type == Type.REGION) {
            s = String.valueOf(s2) + ((IRegion) this.m_node).getID();
        } else {
            s = String.valueOf(s2) + ((Block) this.m_node).toShortString();
        }
        return s;
    }
}
