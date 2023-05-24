package soot.jimple.toolkits.callgraph;

import java.util.Iterator;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/callgraph/Filter.class */
public class Filter implements Iterator<Edge> {
    private final EdgePredicate pred;
    private Iterator<Edge> source;
    private Edge next = null;

    public Filter(EdgePredicate pred) {
        this.pred = pred;
    }

    public Iterator<Edge> wrap(Iterator<Edge> source) {
        this.source = source;
        advance();
        return this;
    }

    private void advance() {
        while (this.source.hasNext()) {
            this.next = this.source.next();
            if (this.pred.want(this.next)) {
                return;
            }
        }
        this.next = null;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.next != null;
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // java.util.Iterator
    public Edge next() {
        Edge ret = this.next;
        advance();
        return ret;
    }

    @Override // java.util.Iterator
    public void remove() {
        throw new UnsupportedOperationException();
    }
}
