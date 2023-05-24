package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/CytronDominanceFrontier.class */
public class CytronDominanceFrontier<N> implements DominanceFrontier<N> {
    protected DominatorTree<N> dt;
    protected Map<DominatorNode<N>, List<DominatorNode<N>>> nodeToFrontier = new HashMap();

    public CytronDominanceFrontier(DominatorTree<N> dt) {
        this.dt = dt;
        for (DominatorNode<N> head : dt.getHeads()) {
            bottomUpDispatch(head);
        }
        for (N gode : dt.graph) {
            DominatorNode<N> dode = dt.fetchDode(gode);
            if (dode == null) {
                throw new RuntimeException("dode == null");
            }
            if (!isFrontierKnown(dode)) {
                throw new RuntimeException("Frontier not defined for node: " + dode);
            }
        }
    }

    @Override // soot.toolkits.graph.DominanceFrontier
    public List<DominatorNode<N>> getDominanceFrontierOf(DominatorNode<N> node) {
        List<DominatorNode<N>> frontier = this.nodeToFrontier.get(node);
        if (frontier == null) {
            throw new RuntimeException("Frontier not defined for node: " + node);
        }
        return Collections.unmodifiableList(frontier);
    }

    protected boolean isFrontierKnown(DominatorNode<N> node) {
        return this.nodeToFrontier.containsKey(node);
    }

    protected void bottomUpDispatch(DominatorNode<N> node) {
        if (isFrontierKnown(node)) {
            return;
        }
        for (DominatorNode<N> child : this.dt.getChildrenOf(node)) {
            if (!isFrontierKnown(child)) {
                bottomUpDispatch(child);
            }
        }
        processNode(node);
    }

    protected void processNode(DominatorNode<N> node) {
        HashSet<DominatorNode<N>> dominanceFrontier = new HashSet<>();
        for (DominatorNode<N> succ : this.dt.getSuccsOf(node)) {
            if (!this.dt.isImmediateDominatorOf(node, succ)) {
                dominanceFrontier.add(succ);
            }
        }
        for (DominatorNode<N> child : this.dt.getChildrenOf(node)) {
            for (DominatorNode<N> childFront : getDominanceFrontierOf(child)) {
                if (!this.dt.isImmediateDominatorOf(node, childFront)) {
                    dominanceFrontier.add(childFront);
                }
            }
        }
        this.nodeToFrontier.put(node, new ArrayList(dominanceFrontier));
    }
}
