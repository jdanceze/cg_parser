package soot.jimple.toolkits.pointer;

import java.util.HashSet;
import java.util.Iterator;
import soot.tagkit.Attribute;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/DependenceGraph.class */
public class DependenceGraph implements Attribute {
    public static final String NAME = "DependenceGraph";
    final HashSet<Edge> edges = new HashSet<>();

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/pointer/DependenceGraph$Edge.class */
    public class Edge {
        final short from;
        final short to;

        public Edge(short from, short to) {
            this.from = from;
            this.to = to;
        }

        public int hashCode() {
            return (this.from << 16) + this.to;
        }

        public boolean equals(Object other) {
            Edge o = (Edge) other;
            return this.from == o.from && this.to == o.to;
        }
    }

    public boolean areAdjacent(short from, short to) {
        if (from > to) {
            return areAdjacent(to, from);
        }
        if (from < 0 || to < 0) {
            return false;
        }
        if (from == to) {
            return true;
        }
        return this.edges.contains(new Edge(from, to));
    }

    public void addEdge(short from, short to) {
        if (from < 0) {
            throw new RuntimeException("from < 0");
        }
        if (to < 0) {
            throw new RuntimeException("to < 0");
        }
        if (from > to) {
            addEdge(to, from);
        } else {
            this.edges.add(new Edge(from, to));
        }
    }

    @Override // soot.tagkit.Tag
    public String getName() {
        return NAME;
    }

    @Override // soot.tagkit.Attribute
    public void setValue(byte[] v) {
        throw new RuntimeException("Not Supported");
    }

    @Override // soot.tagkit.Tag
    public byte[] getValue() {
        byte[] ret = new byte[4 * this.edges.size()];
        int i = 0;
        Iterator<Edge> it = this.edges.iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            int i2 = i;
            int i3 = i + 1;
            ret[i2] = (byte) ((e.from >> 8) & 255);
            int i4 = i3 + 1;
            ret[i3] = (byte) (e.from & 255);
            int i5 = i4 + 1;
            ret[i4] = (byte) ((e.to >> 8) & 255);
            i = i5 + 1;
            ret[i5] = (byte) (e.to & 255);
        }
        return ret;
    }

    public String toString() {
        StringBuilder buf = new StringBuilder("Dependences");
        Iterator<Edge> it = this.edges.iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            buf.append("( ").append((int) e.from).append(", ").append((int) e.to).append(" ) ");
        }
        return buf.toString();
    }
}
