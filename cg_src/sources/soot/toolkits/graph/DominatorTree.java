package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/DominatorTree.class */
public class DominatorTree<N> implements Iterable<DominatorNode<N>> {
    private static final Logger logger = LoggerFactory.getLogger(DominatorTree.class);
    protected final DominatorsFinder<N> dominators;
    protected final DirectedGraph<N> graph;
    protected final List<DominatorNode<N>> heads = new ArrayList();
    protected final List<DominatorNode<N>> tails = new ArrayList();
    protected final Map<N, DominatorNode<N>> godeToDode = new HashMap();

    public DominatorTree(DominatorsFinder<N> dominators) {
        this.dominators = dominators;
        this.graph = dominators.getGraph();
        buildTree();
    }

    public DirectedGraph<N> getGraph() {
        return this.dominators.getGraph();
    }

    public List<DominatorNode<N>> getHeads() {
        return Collections.unmodifiableList(this.heads);
    }

    public DominatorNode<N> getHead() {
        if (this.heads.isEmpty()) {
            return null;
        }
        return this.heads.get(0);
    }

    public List<DominatorNode<N>> getTails() {
        return Collections.unmodifiableList(this.tails);
    }

    public DominatorNode<N> getParentOf(DominatorNode<N> node) {
        return node.getParent();
    }

    public List<DominatorNode<N>> getChildrenOf(DominatorNode<N> node) {
        return Collections.unmodifiableList(node.getChildren());
    }

    public List<DominatorNode<N>> getPredsOf(DominatorNode<N> node) {
        List<DominatorNode<N>> predNodes = new ArrayList<>();
        for (N pred : this.graph.getPredsOf(node.getGode())) {
            predNodes.add(getDode(pred));
        }
        return predNodes;
    }

    public List<DominatorNode<N>> getSuccsOf(DominatorNode<N> node) {
        List<DominatorNode<N>> succNodes = new ArrayList<>();
        for (N succ : this.graph.getSuccsOf(node.getGode())) {
            succNodes.add(getDode(succ));
        }
        return succNodes;
    }

    public boolean isImmediateDominatorOf(DominatorNode<N> idom, DominatorNode<N> node) {
        return node.getParent() == idom;
    }

    public boolean isDominatorOf(DominatorNode<N> dom, DominatorNode<N> node) {
        return this.dominators.isDominatedBy(node.getGode(), dom.getGode());
    }

    public DominatorNode<N> getDode(N gode) {
        DominatorNode<N> dode = this.godeToDode.get(gode);
        if (dode == null) {
            throw new RuntimeException("Assertion failed: Dominator tree does not have a corresponding dode for gode (" + gode + ")");
        }
        return dode;
    }

    @Override // java.lang.Iterable
    public Iterator<DominatorNode<N>> iterator() {
        return this.godeToDode.values().iterator();
    }

    public int size() {
        return this.godeToDode.size();
    }

    protected void buildTree() {
        for (N gode : this.graph) {
            DominatorNode<N> dode = fetchDode(gode);
            DominatorNode<N> parent = fetchParent(gode);
            if (parent == null) {
                this.heads.add(dode);
            } else {
                parent.addChild(dode);
                dode.setParent(parent);
            }
        }
        Iterator<DominatorNode<N>> it = iterator();
        while (it.hasNext()) {
            DominatorNode<N> dode2 = it.next();
            if (dode2.isTail()) {
                this.tails.add(dode2);
            }
        }
        if (this.heads instanceof ArrayList) {
            ((ArrayList) this.heads).trimToSize();
        }
        if (this.tails instanceof ArrayList) {
            ((ArrayList) this.tails).trimToSize();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public DominatorNode<N> fetchDode(N gode) {
        DominatorNode<N> dode = this.godeToDode.get(gode);
        if (dode == null) {
            dode = new DominatorNode<>(gode);
            this.godeToDode.put(gode, dode);
        }
        return dode;
    }

    protected DominatorNode<N> fetchParent(N gode) {
        N immediateDominator = this.dominators.getImmediateDominator(gode);
        if (immediateDominator == null) {
            return null;
        }
        return fetchDode(immediateDominator);
    }
}
