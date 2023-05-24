package soot.jimple.toolkits.annotation.arraycheck;

import java.util.ArrayList;
import java.util.Iterator;
import soot.toolkits.graph.HashMutableDirectedGraph;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/annotation/arraycheck/ExtendedHashMutableDirectedGraph.class */
public class ExtendedHashMutableDirectedGraph<N> extends HashMutableDirectedGraph<N> {
    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void addEdge(N from, N to) {
        if (!super.containsNode(from)) {
            super.addNode(from);
        }
        if (!super.containsNode(to)) {
            super.addNode(to);
        }
        super.addEdge(from, to);
    }

    public void addMutualEdge(N from, N to) {
        if (!super.containsNode(from)) {
            super.addNode(from);
        }
        if (!super.containsNode(to)) {
            super.addNode(to);
        }
        super.addEdge(from, to);
        super.addEdge(to, from);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public void skipNode(N node) {
        if (!super.containsNode(node)) {
            return;
        }
        ArrayList<N> origPreds = new ArrayList<>(getPredsOf(node));
        ArrayList<N> origSuccs = new ArrayList<>(getSuccsOf(node));
        Iterator<N> it = origPreds.iterator();
        while (it.hasNext()) {
            N p = it.next();
            Iterator<N> it2 = origSuccs.iterator();
            while (it2.hasNext()) {
                N s = it2.next();
                if (p != s) {
                    super.addEdge(p, s);
                }
            }
        }
        Iterator<N> it3 = origPreds.iterator();
        while (it3.hasNext()) {
            N element = it3.next();
            super.removeEdge(element, node);
        }
        Iterator<N> it4 = origSuccs.iterator();
        while (it4.hasNext()) {
            N element2 = it4.next();
            super.removeEdge(node, element2);
        }
        super.removeNode(node);
    }

    public <T extends N> void mergeWith(ExtendedHashMutableDirectedGraph<T> other) {
        for (T node : other.getNodes()) {
            for (T succ : other.getSuccsOf(node)) {
                addEdge(node, succ);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Graph:\n");
        for (N node : super.getNodes()) {
            for (N succ : super.getSuccsOf(node)) {
                sb.append(node).append("\t --- \t").append(succ).append('\n');
            }
        }
        return sb.toString();
    }
}
