package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.List;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominatorNode.class */
public class DominatorNode<N> {
    protected N gode;
    protected DominatorNode<N> parent;
    protected List<DominatorNode<N>> children = new ArrayList();

    /* JADX INFO: Access modifiers changed from: protected */
    public DominatorNode(N gode) {
        this.gode = gode;
    }

    public void setParent(DominatorNode<N> parent) {
        this.parent = parent;
    }

    public boolean addChild(DominatorNode<N> child) {
        if (this.children.contains(child)) {
            return false;
        }
        this.children.add(child);
        return true;
    }

    public N getGode() {
        return this.gode;
    }

    public DominatorNode<N> getParent() {
        return this.parent;
    }

    public List<DominatorNode<N>> getChildren() {
        return this.children;
    }

    public boolean isHead() {
        return this.parent == null;
    }

    public boolean isTail() {
        return this.children.isEmpty();
    }

    public String toString() {
        return this.gode.toString();
    }
}
