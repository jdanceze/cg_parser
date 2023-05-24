package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.MethodOrMethodContext;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/Targets.class */
public final class Targets implements Iterator<MethodOrMethodContext> {
    final Iterator<Edge> edges;

    public Targets(Iterator<Edge> edges) {
        this.edges = edges;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.edges.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public MethodOrMethodContext next() {
        Edge e = this.edges.next();
        return e.getTgt();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
