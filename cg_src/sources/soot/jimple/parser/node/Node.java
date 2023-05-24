package soot.jimple.parser.node;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/jimple/parser/node/Node.class */
public abstract class Node implements Switchable, Cloneable {
    private Node parent;

    public abstract Object clone();

    /* JADX INFO: Access modifiers changed from: package-private */
    public abstract void removeChild(Node node);

    abstract void replaceChild(Node node, Node node2);

    public Node parent() {
        return this.parent;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    public void parent(Node parent) {
        this.parent = parent;
    }

    public void replaceBy(Node node) {
        this.parent.replaceChild(this, node);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String toString(Node node) {
        if (node != null) {
            return node.toString();
        }
        return "";
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public String toString(List<?> list) {
        StringBuffer s = new StringBuffer();
        Iterator<?> i = list.iterator();
        while (i.hasNext()) {
            s.append(i.next());
        }
        return s.toString();
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends Node> T cloneNode(T node) {
        if (node != null) {
            return (T) node.clone();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public <T extends Node> List<T> cloneList(List<T> list) {
        LinkedList linkedList = new LinkedList();
        for (T n : list) {
            linkedList.add((Node) n.clone());
        }
        return linkedList;
    }
}
