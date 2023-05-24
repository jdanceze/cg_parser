package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
import soot.Unit;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/Units.class */
public final class Units implements Iterator<Unit> {
    final Iterator<Edge> edges;

    public Units(Iterator<Edge> edges) {
        this.edges = edges;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.edges.hasNext();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Unit next() {
        Edge e = this.edges.next();
        return e.srcUnit();
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
