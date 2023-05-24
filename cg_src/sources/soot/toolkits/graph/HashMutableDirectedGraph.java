package soot.toolkits.graph;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/* loaded from: gencallgraphv3.jar:soot/toolkits/graph/HashMutableDirectedGraph.class */
public class HashMutableDirectedGraph<N> implements MutableDirectedGraph<N> {
    private static final Logger logger = LoggerFactory.getLogger(HashMutableDirectedGraph.class);
    protected final Map<N, Set<N>> nodeToPreds;
    protected final Map<N, Set<N>> nodeToSuccs;
    protected final Set<N> heads;
    protected final Set<N> tails;

    private static <T> List<T> getCopy(Collection<? extends T> c) {
        return Collections.unmodifiableList(new ArrayList(c));
    }

    private static <A, B> Map<A, Set<B>> deepCopy(Map<A, Set<B>> in) {
        HashMap<A, Set<B>> retVal = new HashMap<>((Map<? extends A, ? extends Set<B>>) in);
        for (Map.Entry<A, Set<B>> e : retVal.entrySet()) {
            e.setValue(new LinkedHashSet<>(e.getValue()));
        }
        return retVal;
    }

    public HashMutableDirectedGraph() {
        this.nodeToPreds = new HashMap();
        this.nodeToSuccs = new HashMap();
        this.heads = new HashSet();
        this.tails = new HashSet();
    }

    public HashMutableDirectedGraph(HashMutableDirectedGraph<N> orig) {
        this.nodeToPreds = deepCopy(orig.nodeToPreds);
        this.nodeToSuccs = deepCopy(orig.nodeToSuccs);
        this.heads = new HashSet(orig.heads);
        this.tails = new HashSet(orig.tails);
    }

    public Object clone() {
        return new HashMutableDirectedGraph(this);
    }

    public void clearAll() {
        this.nodeToPreds.clear();
        this.nodeToSuccs.clear();
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
        Set<N> preds = this.nodeToPreds.get(s);
        if (preds != null) {
            return getCopy(preds);
        }
        throw new RuntimeException(s + " not in graph!");
    }

    public Set<N> getPredsOfAsSet(N s) {
        Set<N> preds = this.nodeToPreds.get(s);
        if (preds != null) {
            return Collections.unmodifiableSet(preds);
        }
        throw new RuntimeException(s + " not in graph!");
    }

    @Override // soot.toolkits.graph.DirectedGraph
    public List<N> getSuccsOf(N s) {
        Set<N> succs = this.nodeToSuccs.get(s);
        if (succs != null) {
            return getCopy(succs);
        }
        throw new RuntimeException(s + " not in graph!");
    }

    public Set<N> getSuccsOfAsSet(N s) {
        Set<N> succs = this.nodeToSuccs.get(s);
        if (succs != null) {
            return Collections.unmodifiableSet(succs);
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

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public void addEdge(N from, N to) {
        if (from == null || to == null) {
            throw new RuntimeException("edge with null endpoint");
        }
        if (containsEdge(from, to)) {
            return;
        }
        Set<N> succsList = this.nodeToSuccs.get(from);
        if (succsList == null) {
            throw new RuntimeException(from + " not in graph!");
        }
        Set<N> predsList = this.nodeToPreds.get(to);
        if (predsList == null) {
            throw new RuntimeException(to + " not in graph!");
        }
        this.heads.remove(to);
        this.tails.remove(from);
        succsList.add(to);
        predsList.add(from);
    }

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public void removeEdge(N from, N to) {
        Set<N> succs = this.nodeToSuccs.get(from);
        if (succs == null || !succs.contains(to)) {
            return;
        }
        Set<N> preds = this.nodeToPreds.get(to);
        if (preds == null) {
            throw new RuntimeException(to + " not in graph!");
        }
        succs.remove(to);
        preds.remove(from);
        if (succs.isEmpty()) {
            this.tails.add(from);
        }
        if (preds.isEmpty()) {
            this.heads.add(to);
        }
    }

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public boolean containsEdge(N from, N to) {
        Set<N> succs = this.nodeToSuccs.get(from);
        return succs != null && succs.contains(to);
    }

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public boolean containsNode(N node) {
        return this.nodeToPreds.keySet().contains(node);
    }

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public List<N> getNodes() {
        return getCopy(this.nodeToPreds.keySet());
    }

    @Override // soot.toolkits.graph.MutableDirectedGraph
    public void addNode(N node) {
        if (containsNode(node)) {
            throw new RuntimeException("Node already in graph");
        }
        this.nodeToSuccs.put(node, new LinkedHashSet());
        this.nodeToPreds.put(node, new LinkedHashSet());
        this.heads.add(node);
        this.tails.add(node);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // soot.toolkits.graph.MutableDirectedGraph
    public void removeNode(N node) {
        Iterator it = new ArrayList(this.nodeToSuccs.get(node)).iterator();
        while (it.hasNext()) {
            removeEdge(node, it.next());
        }
        this.nodeToSuccs.remove(node);
        Iterator it2 = new ArrayList(this.nodeToPreds.get(node)).iterator();
        while (it2.hasNext()) {
            removeEdge(it2.next(), node);
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
            for (N p : getPredsOf(node)) {
                logger.debug("     ");
                logger.debug(new StringBuilder().append(p).toString());
            }
            logger.debug("Succs:");
            for (N s : getSuccsOf(node)) {
                logger.debug("     ");
                logger.debug(new StringBuilder().append(s).toString());
            }
        }
    }
}
