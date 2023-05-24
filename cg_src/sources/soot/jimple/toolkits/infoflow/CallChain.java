package soot.jimple.toolkits.infoflow;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import soot.SootMethod;
import soot.jimple.toolkits.callgraph.Edge;
/* loaded from: gencallgraphv3.jar:soot/jimple/toolkits/infoflow/CallChain.class */
public class CallChain {
    Edge edge;
    CallChain next;

    public CallChain(Edge edge, CallChain next) {
        this.edge = edge;
        if (next != null && next.edge == null && next.next == null) {
            this.next = null;
        } else {
            this.next = next;
        }
    }

    public List<Edge> getEdges() {
        List<Edge> ret = new LinkedList<>();
        if (this.edge != null) {
            ret.add(this.edge);
        }
        CallChain callChain = this.next;
        while (true) {
            CallChain current = callChain;
            if (current != null) {
                ret.add(current.edge);
                callChain = current.next;
            } else {
                return ret;
            }
        }
    }

    public int size() {
        return 1 + (this.next == null ? 0 : this.next.size());
    }

    public Iterator<Edge> iterator() {
        return getEdges().iterator();
    }

    public boolean contains(Edge e) {
        if (this.edge != e) {
            return this.next != null && this.next.contains(e);
        }
        return true;
    }

    public boolean containsMethod(SootMethod sm) {
        if (this.edge == null || this.edge.tgt() != sm) {
            return this.next != null && this.next.containsMethod(sm);
        }
        return true;
    }

    public CallChain cloneAndExtend(CallChain extension) {
        if (this.next == null) {
            return new CallChain(this.edge, extension);
        }
        return new CallChain(this.edge, this.next.cloneAndExtend(extension));
    }

    public Object clone() {
        if (this.next == null) {
            return new CallChain(this.edge, null);
        }
        return new CallChain(this.edge, (CallChain) this.next.clone());
    }

    public boolean equals(Object o) {
        if (o instanceof CallChain) {
            CallChain other = (CallChain) o;
            if (this.edge == other.edge) {
                if (this.next == null && other.next == null) {
                    return true;
                }
                if (this.next != null && other.next != null && this.next.equals(other.next)) {
                    return true;
                }
                return false;
            }
            return false;
        }
        return false;
    }
}
