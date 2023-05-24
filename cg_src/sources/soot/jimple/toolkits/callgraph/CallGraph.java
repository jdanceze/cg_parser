package soot.jimple.toolkits.callgraph;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import soot.Kind;
import soot.MethodOrMethodContext;
import soot.SootMethod;
import soot.Unit;
import soot.jimple.Stmt;
import soot.util.queue.ChunkedQueue;
import soot.util.queue.QueueReader;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraph.class */
public class CallGraph implements Iterable<Edge> {
    protected Set<Edge> edges = new LinkedHashSet();
    protected ChunkedQueue<Edge> stream = new ChunkedQueue<>();
    protected QueueReader<Edge> reader = this.stream.reader();
    protected Map<MethodOrMethodContext, Edge> srcMethodToEdge = new LinkedHashMap();
    protected Map<Unit, Edge> srcUnitToEdge = new LinkedHashMap();
    protected Map<MethodOrMethodContext, Edge> tgtToEdge = new LinkedHashMap();
    protected Edge dummy = new Edge(null, null, null, Kind.INVALID);

    public boolean addEdge(Edge e) {
        if (!this.edges.add(e)) {
            return false;
        }
        this.stream.add(e);
        Edge position = this.srcUnitToEdge.get(e.srcUnit());
        if (position == null) {
            this.srcUnitToEdge.put(e.srcUnit(), e);
            position = this.dummy;
        }
        e.insertAfterByUnit(position);
        Edge position2 = this.srcMethodToEdge.get(e.getSrc());
        if (position2 == null) {
            this.srcMethodToEdge.put(e.getSrc(), e);
            position2 = this.dummy;
        }
        e.insertAfterBySrc(position2);
        Edge position3 = this.tgtToEdge.get(e.getTgt());
        if (position3 == null) {
            this.tgtToEdge.put(e.getTgt(), e);
            position3 = this.dummy;
        }
        e.insertAfterByTgt(position3);
        return true;
    }

    public boolean removeAllEdgesOutOf(Unit u) {
        boolean hasRemoved = false;
        Set<Edge> edgesToRemove = new HashSet<>();
        QueueReader<Edge> edgeRdr = listener();
        while (edgeRdr.hasNext()) {
            Edge e = edgeRdr.next();
            if (e.srcUnit() == u) {
                e.remove();
                removeEdge(e, false);
                edgesToRemove.add(e);
                hasRemoved = true;
            }
        }
        if (hasRemoved) {
            this.reader.remove(edgesToRemove);
        }
        return hasRemoved;
    }

    public boolean swapEdgesOutOf(Stmt out, Stmt in) {
        boolean hasSwapped = false;
        Iterator<Edge> edgeRdr = edgesOutOf(out);
        while (edgeRdr.hasNext()) {
            Edge e = edgeRdr.next();
            MethodOrMethodContext src = e.getSrc();
            MethodOrMethodContext tgt = e.getTgt();
            removeEdge(e);
            e.remove();
            addEdge(new Edge(src, in, tgt));
            hasSwapped = true;
        }
        return hasSwapped;
    }

    public boolean removeEdge(Edge e) {
        return removeEdge(e, true);
    }

    public boolean removeEdge(Edge e, boolean removeInEdgeList) {
        if (!this.edges.remove(e)) {
            return false;
        }
        e.remove();
        if (this.srcUnitToEdge.get(e.srcUnit()) == e) {
            if (e.nextByUnit().srcUnit() == e.srcUnit()) {
                this.srcUnitToEdge.put(e.srcUnit(), e.nextByUnit());
            } else {
                this.srcUnitToEdge.remove(e.srcUnit());
            }
        }
        if (this.srcMethodToEdge.get(e.getSrc()) == e) {
            if (e.nextBySrc().getSrc() == e.getSrc()) {
                this.srcMethodToEdge.put(e.getSrc(), e.nextBySrc());
            } else {
                this.srcMethodToEdge.remove(e.getSrc());
            }
        }
        if (this.tgtToEdge.get(e.getTgt()) == e) {
            if (e.nextByTgt().getTgt() == e.getTgt()) {
                this.tgtToEdge.put(e.getTgt(), e.nextByTgt());
            } else {
                this.tgtToEdge.remove(e.getTgt());
            }
        }
        if (removeInEdgeList) {
            this.reader.remove((QueueReader<Edge>) e);
            return true;
        }
        return true;
    }

    public boolean removeEdges(Collection<Edge> edges) {
        if (!this.edges.removeAll(edges)) {
            return false;
        }
        for (Edge e : edges) {
            removeEdge(e, false);
        }
        this.reader.remove(edges);
        return true;
    }

    public boolean isEntryMethod(SootMethod method) {
        return !this.tgtToEdge.containsKey(method);
    }

    public Edge findEdge(Unit u, SootMethod callee) {
        Edge e = this.srcUnitToEdge.get(u);
        if (e != null) {
            while (e.srcUnit() == u && e.kind() != Kind.INVALID) {
                if (e.tgt() == callee) {
                    return e;
                }
                e = e.nextByUnit();
            }
            return null;
        }
        return null;
    }

    public Iterator<MethodOrMethodContext> sourceMethods() {
        return this.srcMethodToEdge.keySet().iterator();
    }

    public Iterator<Edge> edgesOutOf(Unit u) {
        return new TargetsOfUnitIterator(u);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraph$TargetsOfUnitIterator.class */
    public class TargetsOfUnitIterator implements Iterator<Edge> {
        private final Unit u;
        private Edge position;

        TargetsOfUnitIterator(Unit u) {
            this.u = u;
            if (u == null) {
                throw new RuntimeException();
            }
            this.position = CallGraph.this.srcUnitToEdge.get(u);
            if (this.position == null) {
                this.position = CallGraph.this.dummy;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.position.srcUnit() == this.u && this.position.kind() != Kind.INVALID;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Edge next() {
            Edge ret = this.position;
            this.position = this.position.nextByUnit();
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Edge> edgesOutOf(MethodOrMethodContext m) {
        return new TargetsOfMethodIterator(m);
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraph$TargetsOfMethodIterator.class */
    class TargetsOfMethodIterator implements Iterator<Edge> {
        private final MethodOrMethodContext m;
        private Edge position;

        TargetsOfMethodIterator(MethodOrMethodContext m) {
            this.m = m;
            if (m == null) {
                throw new RuntimeException();
            }
            this.position = CallGraph.this.srcMethodToEdge.get(m);
            if (this.position == null) {
                this.position = CallGraph.this.dummy;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.position.getSrc() == this.m && this.position.kind() != Kind.INVALID;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Edge next() {
            Edge ret = this.position;
            this.position = this.position.nextBySrc();
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public Iterator<Edge> edgesInto(MethodOrMethodContext m) {
        return new CallersOfMethodIterator(m);
    }

    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/CallGraph$CallersOfMethodIterator.class */
    class CallersOfMethodIterator implements Iterator<Edge> {
        private final MethodOrMethodContext m;
        private Edge position;

        CallersOfMethodIterator(MethodOrMethodContext m) {
            this.m = m;
            if (m == null) {
                throw new RuntimeException();
            }
            this.position = CallGraph.this.tgtToEdge.get(m);
            if (this.position == null) {
                this.position = CallGraph.this.dummy;
            }
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.position.getTgt() == this.m && this.position.kind() != Kind.INVALID;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public Edge next() {
            Edge ret = this.position;
            this.position = this.position.nextByTgt();
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public QueueReader<Edge> listener() {
        return this.reader.m3089clone();
    }

    public QueueReader<Edge> newListener() {
        return this.stream.reader();
    }

    public String toString() {
        StringBuilder out = new StringBuilder();
        QueueReader<Edge> reader = listener();
        while (reader.hasNext()) {
            Edge e = reader.next();
            out.append(e.toString()).append('\n');
        }
        return out.toString();
    }

    public int size() {
        return this.edges.size();
    }

    @Override // java.lang.Iterable
    public Iterator<Edge> iterator() {
        return this.edges.iterator();
    }
}
