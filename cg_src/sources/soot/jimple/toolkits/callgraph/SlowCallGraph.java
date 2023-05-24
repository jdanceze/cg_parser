package soot.jimple.toolkits.callgraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import soot.MethodOrMethodContext;
import soot.Unit;
import soot.util.HashMultiMap;
import soot.util.MultiMap;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/SlowCallGraph.class */
public class SlowCallGraph extends CallGraph {
    private final Set<Edge> edges = new HashSet();
    private final MultiMap<Unit, Edge> unitMap = new HashMultiMap();
    private final MultiMap<MethodOrMethodContext, Edge> srcMap = new HashMultiMap();
    private final MultiMap<MethodOrMethodContext, Edge> tgtMap = new HashMultiMap();
    private final ChunkedQueue<Edge> stream = new ChunkedQueue<>();
    private final QueueReader<Edge> reader = this.stream.reader();

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public boolean addEdge(Edge e) {
        if (this.edges.add(e)) {
            this.stream.add(e);
            this.srcMap.put(e.getSrc(), e);
            this.tgtMap.put(e.getTgt(), e);
            this.unitMap.put(e.srcUnit(), e);
            return true;
        }
        return false;
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public boolean removeEdge(Edge e) {
        if (this.edges.remove(e)) {
            this.srcMap.remove(e.getSrc(), e);
            this.tgtMap.remove(e.getTgt(), e);
            this.unitMap.remove(e.srcUnit(), e);
            return true;
        }
        return false;
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public Iterator<MethodOrMethodContext> sourceMethods() {
        return new ArrayList(this.srcMap.keySet()).iterator();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public Iterator<Edge> edgesOutOf(Unit u) {
        return new ArrayList(this.unitMap.get(u)).iterator();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public Iterator<Edge> edgesOutOf(MethodOrMethodContext m) {
        return new ArrayList(this.srcMap.get(m)).iterator();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public Iterator<Edge> edgesInto(MethodOrMethodContext m) {
        return new ArrayList(this.tgtMap.get(m)).iterator();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public QueueReader<Edge> listener() {
        return this.reader.m3089clone();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public QueueReader<Edge> newListener() {
        return this.stream.reader();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public String toString() {
        StringBuilder out = new StringBuilder();
        QueueReader<Edge> rdr = listener();
        while (rdr.hasNext()) {
            Edge e = rdr.next();
            out.append(e.toString()).append('\n');
        }
        return out.toString();
    }

    @Override // soot.jimple.toolkits.callgraph.CallGraph
    public int size() {
        return this.edges.size();
    }
}
