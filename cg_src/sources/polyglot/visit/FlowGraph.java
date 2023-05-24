package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Type;
import polyglot.util.CollectionUtil;
import polyglot.util.IdentityKey;
import polyglot.visit.DataFlow;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph.class */
public class FlowGraph {
    protected Map peerMap = new HashMap();
    protected Term root;
    protected boolean forward;
    public static final EdgeKey EDGE_KEY_TRUE = new EdgeKey("true");
    public static final EdgeKey EDGE_KEY_FALSE = new EdgeKey("false");
    public static final EdgeKey EDGE_KEY_OTHER = new EdgeKey("");

    /* JADX INFO: Access modifiers changed from: package-private */
    public FlowGraph(Term root, boolean forward) {
        this.root = root;
        this.forward = forward;
    }

    public Term startNode() {
        return this.forward ? this.root.entry() : this.root;
    }

    public Term finishNode() {
        return this.forward ? this.root : this.root.entry();
    }

    public Term entryNode() {
        return this.root.entry();
    }

    public Term exitNode() {
        return this.root;
    }

    public Term root() {
        return this.root;
    }

    public boolean forward() {
        return this.forward;
    }

    public Collection pathMaps() {
        return this.peerMap.values();
    }

    public Map pathMap(Node n) {
        return (Map) this.peerMap.get(new IdentityKey(n));
    }

    public Collection peers() {
        Collection c = new ArrayList();
        for (Map m : this.peerMap.values()) {
            for (Object obj : m.values()) {
                c.add(obj);
            }
        }
        return c;
    }

    public Peer peer(Term n, DataFlow df) {
        return peer(n, Collections.EMPTY_LIST, df);
    }

    public Collection peers(Term n) {
        IdentityKey k = new IdentityKey(n);
        Map pathMap = (Map) this.peerMap.get(k);
        if (pathMap == null) {
            return Collections.EMPTY_LIST;
        }
        return pathMap.values();
    }

    public Peer peer(Term n, List path_to_finally, DataFlow df) {
        IdentityKey k = new IdentityKey(n);
        HashMap pathMap = (Map) this.peerMap.get(k);
        if (pathMap == null) {
            pathMap = new HashMap();
            this.peerMap.put(k, pathMap);
        }
        ListKey lk = new ListKey(path_to_finally);
        Peer p = (Peer) pathMap.get(lk);
        if (p == null) {
            p = new Peer(n, path_to_finally);
            pathMap.put(lk, p);
        }
        return p;
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph$EdgeKey.class */
    public static class EdgeKey {
        protected Object o;

        protected EdgeKey(Object o) {
            this.o = o;
        }

        public int hashCode() {
            return this.o.hashCode();
        }

        public boolean equals(Object other) {
            return (other instanceof EdgeKey) && ((EdgeKey) other).o.equals(this.o);
        }

        public String toString() {
            return this.o.toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph$ExceptionEdgeKey.class */
    public static class ExceptionEdgeKey extends EdgeKey {
        public ExceptionEdgeKey(Type t) {
            super(t);
        }

        public Type type() {
            return (Type) this.o;
        }

        @Override // polyglot.visit.FlowGraph.EdgeKey
        public String toString() {
            return type().isClass() ? type().toClass().name() : type().toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph$Edge.class */
    public static class Edge {
        protected EdgeKey key;
        protected Peer target;

        /* JADX INFO: Access modifiers changed from: protected */
        public Edge(EdgeKey key, Peer target) {
            this.key = key;
            this.target = target;
        }

        public EdgeKey getKey() {
            return this.key;
        }

        public Peer getTarget() {
            return this.target;
        }

        public String toString() {
            return new StringBuffer().append("(").append(this.key).append(")").append(this.target).toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph$Peer.class */
    public static class Peer {
        protected Term node;
        protected List path_to_finally;
        protected DataFlow.Item inItem = null;
        protected Map outItems = null;
        protected List succs = new ArrayList();
        protected List preds = new ArrayList();
        private Set succEdgeKeys = null;

        public Peer(Term node, List path_to_finally) {
            this.node = node;
            this.path_to_finally = path_to_finally;
        }

        public List succs() {
            return this.succs;
        }

        public List preds() {
            return this.preds;
        }

        public Term node() {
            return this.node;
        }

        public DataFlow.Item inItem() {
            return this.inItem;
        }

        public DataFlow.Item outItem(EdgeKey key) {
            return (DataFlow.Item) this.outItems.get(key);
        }

        public String toString() {
            return new StringBuffer().append(this.node).append("[").append(hashCode()).append(": ").append(this.path_to_finally).append("]").toString();
        }

        public Set succEdgeKeys() {
            if (this.succEdgeKeys == null) {
                this.succEdgeKeys = new HashSet();
                for (Edge e : this.succs) {
                    this.succEdgeKeys.add(e.getKey());
                }
                if (this.succEdgeKeys.isEmpty()) {
                    this.succEdgeKeys.add(FlowGraph.EDGE_KEY_OTHER);
                }
            }
            return this.succEdgeKeys;
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/FlowGraph$ListKey.class */
    public static class ListKey {
        protected List list;

        ListKey(List list) {
            this.list = list;
        }

        public int hashCode() {
            return this.list.hashCode();
        }

        public boolean equals(Object other) {
            if (other instanceof ListKey) {
                ListKey k = (ListKey) other;
                return CollectionUtil.equals(this.list, k.list);
            }
            return false;
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.util.Set] */
    public String toString() {
        StringBuffer sb = new StringBuffer();
        HashSet hashSet = new HashSet(peers());
        LinkedList queue = new LinkedList(peers(startNode()));
        while (!queue.isEmpty()) {
            Peer p = (Peer) queue.removeFirst();
            hashSet.remove(p);
            sb.append(new StringBuffer().append(p.node).append(" (").append(p.node.position()).append(")\n").toString());
            for (Edge e : p.succs) {
                Peer q = e.getTarget();
                sb.append(new StringBuffer().append("    -> ").append(q.node).append(" (").append(q.node.position()).append(")\n").toString());
                if (hashSet.contains(q) && !queue.contains(q)) {
                    queue.addLast(q);
                }
            }
            if (queue.isEmpty() && !hashSet.isEmpty()) {
                sb.append("\n\n***UNREACHABLE***\n");
                queue.addAll(hashSet);
                hashSet = Collections.EMPTY_SET;
            }
        }
        return sb.toString();
    }
}
