package soot.toolkits.graph;

import java.util.HashMap;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/MemoryEfficientGraph.class */
public class MemoryEfficientGraph<N> extends HashMutableDirectedGraph<N> {
    HashMap<N, N> self = new HashMap<>();

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void addNode(N o) {
        super.addNode(o);
        this.self.put(o, o);
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void removeNode(N o) {
        super.removeNode(o);
        this.self.remove(o);
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void addEdge(N from, N to) {
        if (containsNode(from) && containsNode(to)) {
            super.addEdge(this.self.get(from), this.self.get(to));
        } else if (!containsNode(from)) {
            throw new RuntimeException(String.valueOf(from.toString()) + " not in graph!");
        } else {
            throw new RuntimeException(String.valueOf(to.toString()) + " not in graph!");
        }
    }

    @Override // soot.toolkits.graph.HashMutableDirectedGraph, soot.toolkits.graph.MutableDirectedGraph
    public void removeEdge(N from, N to) {
        if (containsNode(from) && containsNode(to)) {
            super.removeEdge(this.self.get(from), this.self.get(to));
        } else if (!containsNode(from)) {
            throw new RuntimeException(String.valueOf(from.toString()) + " not in graph!");
        } else {
            throw new RuntimeException(String.valueOf(to.toString()) + " not in graph!");
        }
    }
}
