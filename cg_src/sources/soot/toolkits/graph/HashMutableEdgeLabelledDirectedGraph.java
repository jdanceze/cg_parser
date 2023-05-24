package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/HashMutableEdgeLabelledDirectedGraph.class */
public class HashMutableEdgeLabelledDirectedGraph<N, L> implements MutableEdgeLabelledDirectedGraph<N, L> {
    private static final Logger logger = LoggerFactory.getLogger(HashMutableEdgeLabelledDirectedGraph.class);
    protected final Map<N, List<N>> nodeToPreds;
    protected final Map<N, List<N>> nodeToSuccs;
    protected final Map<DGEdge<N>, List<L>> edgeToLabels;
    protected final Map<L, List<DGEdge<N>>> labelToEdges;
    protected final Set<N> heads;
    protected final Set<N> tails;

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public /* bridge */ /* synthetic */ DirectedGraph getEdgesForLabel(Object obj) {
        return getEdgesForLabel((HashMutableEdgeLabelledDirectedGraph<N, L>) obj);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/toolkits/graph/HashMutableEdgeLabelledDirectedGraph$DGEdge.class */
    public static class DGEdge<N> {
        final N from;
        final N to;

        public DGEdge(N from, N to) {
            this.from = from;
            this.to = to;
        }

        public N from() {
            return this.from;
        }

        public N to() {
            return this.to;
        }

        public boolean equals(Object o) {
            if (o instanceof DGEdge) {
                DGEdge<?> other = (DGEdge) o;
                return this.from.equals(other.from) && this.to.equals(other.to);
            }
            return false;
        }

        public int hashCode() {
            return Arrays.hashCode(new Object[]{this.from, this.to});
        }
    }

    private static <T> List<T> getCopy(Collection<? extends T> c) {
        return Collections.unmodifiableList(new ArrayList(c));
    }

    public HashMutableEdgeLabelledDirectedGraph() {
        this.nodeToPreds = new HashMap();
        this.nodeToSuccs = new HashMap();
        this.edgeToLabels = new HashMap();
        this.labelToEdges = new HashMap();
        this.heads = new HashSet();
        this.tails = new HashSet();
    }

    public HashMutableEdgeLabelledDirectedGraph(HashMutableEdgeLabelledDirectedGraph<N, L> orig) {
        this.nodeToPreds = deepCopy(orig.nodeToPreds);
        this.nodeToSuccs = deepCopy(orig.nodeToSuccs);
        this.edgeToLabels = deepCopy(orig.edgeToLabels);
        this.labelToEdges = deepCopy(orig.labelToEdges);
        this.heads = new HashSet(orig.heads);
        this.tails = new HashSet(orig.tails);
    }

    private static <A, B> Map<A, List<B>> deepCopy(Map<A, List<B>> in) {
        HashMap<A, List<B>> retVal = new HashMap<>((Map<? extends A, ? extends List<B>>) in);
        for (Map.Entry<A, List<B>> e : retVal.entrySet()) {
            e.setValue(new ArrayList<>(e.getValue()));
        }
        return retVal;
    }

    /* renamed from: clone */
    public HashMutableEdgeLabelledDirectedGraph<N, L> m3032clone() {
        return new HashMutableEdgeLabelledDirectedGraph<>(this);
    }

    public void clearAll() {
        this.nodeToPreds.clear();
        this.nodeToSuccs.clear();
        this.edgeToLabels.clear();
        this.labelToEdges.clear();
        this.heads.clear();
        this.tails.clear();
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getHeads() {
        return getCopy(this.heads);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getTails() {
        return getCopy(this.tails);
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getPredsOf(N s) {
        List<N> preds = this.nodeToPreds.get(s);
        if (preds != null) {
            return Collections.unmodifiableList(preds);
        }
        throw new RuntimeException(s + " not in graph!");
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getSuccsOf(N s) {
        List<N> succs = this.nodeToSuccs.get(s);
        if (succs != null) {
            return Collections.unmodifiableList(succs);
        }
        throw new RuntimeException(s + " not in graph!");
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public int size() {
        return this.nodeToPreds.keySet().size();
    }

    @Override // soot.toolkits.graph.DirectedGraph, java.lang.Iterable
    public Iterator<N> iterator() {
        return this.nodeToPreds.keySet().iterator();
    }

    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void addEdge(N from, N to, L label) {
        if (from == null || to == null) {
            throw new RuntimeException("edge with null endpoint");
        }
        if (label == null) {
            throw new RuntimeException("edge with null label");
        }
        if (containsEdge(from, to, label)) {
            return;
        }
        List<N> succsList = this.nodeToSuccs.get(from);
        if (succsList == null) {
            throw new RuntimeException(from + " not in graph!");
        }
        List<N> predsList = this.nodeToPreds.get(to);
        if (predsList == null) {
            throw new RuntimeException(to + " not in graph!");
        }
        this.heads.remove(to);
        this.tails.remove(from);
        if (!succsList.contains(to)) {
            succsList.add(to);
        }
        if (!predsList.contains(from)) {
            predsList.add(from);
        }
        DGEdge<N> edge = new DGEdge<>(from, to);
        List<L> labels = this.edgeToLabels.get(edge);
        if (labels == null) {
            Map<DGEdge<N>, List<L>> map = this.edgeToLabels;
            List<L> arrayList = new ArrayList<>();
            labels = arrayList;
            map.put(edge, arrayList);
        }
        List<DGEdge<N>> edges = this.labelToEdges.get(label);
        if (edges == null) {
            Map<L, List<DGEdge<N>>> map2 = this.labelToEdges;
            List<DGEdge<N>> arrayList2 = new ArrayList<>();
            edges = arrayList2;
            map2.put(label, arrayList2);
        }
        labels.add(label);
        edges.add(edge);
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public List<L> getLabelsForEdges(N from, N to) {
        DGEdge<N> edge = new DGEdge<>(from, to);
        return this.edgeToLabels.get(edge);
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public MutableDirectedGraph<N> getEdgesForLabel(L label) {
        List<DGEdge<N>> edges = this.labelToEdges.get(label);
        MutableDirectedGraph<N> ret = new HashMutableDirectedGraph<>();
        if (edges == null) {
            return ret;
        }
        for (DGEdge<N> edge : edges) {
            N from = edge.from();
            if (!ret.containsNode(from)) {
                ret.addNode(from);
            }
            N to = edge.to();
            if (!ret.containsNode(to)) {
                ret.addNode(to);
            }
            ret.addEdge(from, to);
        }
        return ret;
    }

    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void removeEdge(N from, N to, L label) {
        DGEdge<N> edge = new DGEdge<>(from, to);
        List<L> labels = this.edgeToLabels.get(edge);
        if (labels == null || !labels.contains(label)) {
            return;
        }
        List<DGEdge<N>> edges = this.labelToEdges.get(label);
        if (edges == null) {
            throw new RuntimeException("label " + label + " not in graph!");
        }
        labels.remove(label);
        edges.remove(edge);
        if (labels.isEmpty()) {
            this.edgeToLabels.remove(edge);
            List<N> succsList = this.nodeToSuccs.get(from);
            if (succsList == null) {
                throw new RuntimeException(from + " not in graph!");
            }
            List<N> predsList = this.nodeToPreds.get(to);
            if (predsList == null) {
                throw new RuntimeException(to + " not in graph!");
            }
            succsList.remove(to);
            predsList.remove(from);
            if (succsList.isEmpty()) {
                this.tails.add(from);
            }
            if (predsList.isEmpty()) {
                this.heads.add(to);
            }
        }
        if (edges.isEmpty()) {
            this.labelToEdges.remove(label);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void removeAllEdges(N from, N to) {
        DGEdge<N> edge = new DGEdge<>(from, to);
        List<L> labels = this.edgeToLabels.get(edge);
        if (labels == null || labels.isEmpty()) {
            return;
        }
        for (Object obj : getCopy(labels)) {
            removeEdge(from, to, obj);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void removeAllEdges(L label) {
        List<DGEdge<N>> edges = this.labelToEdges.get(label);
        if (edges == null || edges.isEmpty()) {
            return;
        }
        for (DGEdge<N> edge : getCopy(edges)) {
            removeEdge(edge.from(), edge.to(), label);
        }
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public boolean containsEdge(N from, N to, L label) {
        List<L> labels = this.edgeToLabels.get(new DGEdge(from, to));
        return labels != null && labels.contains(label);
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public boolean containsAnyEdge(N from, N to) {
        List<L> labels = this.edgeToLabels.get(new DGEdge(from, to));
        return (labels == null || labels.isEmpty()) ? false : true;
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public boolean containsAnyEdge(L label) {
        List<DGEdge<N>> edges = this.labelToEdges.get(label);
        return (edges == null || edges.isEmpty()) ? false : true;
    }

    @Override // soot.toolkits.graph.EdgeLabelledDirectedGraph
    public boolean containsNode(N node) {
        return this.nodeToPreds.keySet().contains(node);
    }

    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void addNode(N node) {
        if (containsNode(node)) {
            throw new RuntimeException("Node already in graph");
        }
        this.nodeToSuccs.put(node, new ArrayList());
        this.nodeToPreds.put(node, new ArrayList());
        this.heads.add(node);
        this.tails.add(node);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.graph.MutableEdgeLabelledDirectedGraph
    public void removeNode(N node) {
        Iterator it = new ArrayList(this.nodeToSuccs.get(node)).iterator();
        while (it.hasNext()) {
            removeAllEdges(node, it.next());
        }
        this.nodeToSuccs.remove(node);
        Iterator it2 = new ArrayList(this.nodeToPreds.get(node)).iterator();
        while (it2.hasNext()) {
            removeAllEdges(it2.next(), node);
        }
        this.nodeToPreds.remove(node);
        this.heads.remove(node);
        this.tails.remove(node);
    }

    public void printGraph() {
        Iterator<N> it = iterator();
        while (it.hasNext()) {
            N node = it.next();
            logger.debug("Node = " + node);
            logger.debug("Preds:");
            for (N pred : getPredsOf(node)) {
                DGEdge<N> edge = new DGEdge<>(pred, node);
                List<L> labels = this.edgeToLabels.get(edge);
                logger.debug("     " + pred + " [" + labels + "]");
            }
            logger.debug("Succs:");
            for (N succ : getSuccsOf(node)) {
                DGEdge<N> edge2 = new DGEdge<>(node, succ);
                List<L> labels2 = this.edgeToLabels.get(edge2);
                logger.debug("     " + succ + " [" + labels2 + "]");
            }
        }
    }
}
