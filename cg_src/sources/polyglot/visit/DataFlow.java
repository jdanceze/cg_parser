package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Binary;
import polyglot.ast.CodeDecl;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.IdentityKey;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;
import polyglot.visit.FlowGraph;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow.class */
public abstract class DataFlow extends ErrorHandlingVisitor {
    protected final boolean forward;
    protected final boolean dataflowOnEntry;
    protected LinkedList flowgraphStack;
    protected static int flowCounter = 0;

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow$Item.class */
    public static abstract class Item {
        public abstract boolean equals(Object obj);

        public abstract int hashCode();
    }

    protected abstract Item createInitialItem(FlowGraph flowGraph, Term term);

    protected abstract Item confluence(List list, Term term, FlowGraph flowGraph);

    protected abstract void check(FlowGraph flowGraph, Term term, Item item, Map map) throws SemanticException;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow$FlowGraphSource.class */
    public static class FlowGraphSource {
        FlowGraph flowgraph;
        CodeDecl source;

        FlowGraphSource(FlowGraph g, CodeDecl s) {
            this.flowgraph = g;
            this.source = s;
        }

        public FlowGraph flowGraph() {
            return this.flowgraph;
        }

        public CodeDecl source() {
            return this.source;
        }
    }

    public DataFlow(Job job, TypeSystem ts, NodeFactory nf, boolean forward) {
        this(job, ts, nf, forward, false);
    }

    public DataFlow(Job job, TypeSystem ts, NodeFactory nf, boolean forward, boolean dataflowOnEntry) {
        super(job, ts, nf);
        this.forward = forward;
        this.dataflowOnEntry = dataflowOnEntry;
        if (dataflowOnEntry) {
            this.flowgraphStack = new LinkedList();
        } else {
            this.flowgraphStack = null;
        }
    }

    protected Map flow(Item in, FlowGraph graph, Term n, Set edgeKeys) {
        throw new InternalCompilerError("Unimplemented: should be implemented by subclasses if needed");
    }

    protected Map flow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
        Item inItem = safeConfluence(inItems, inItemKeys, n, graph);
        return flow(inItem, graph, n, edgeKeys);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final Map flowToBooleanFlow(List inItems, List inItemKeys, FlowGraph graph, Term n, Set edgeKeys) {
        List trueItems = new ArrayList();
        List trueItemKeys = new ArrayList();
        List falseItems = new ArrayList();
        List falseItemKeys = new ArrayList();
        List otherItems = new ArrayList();
        List otherItemKeys = new ArrayList();
        Iterator i = inItems.iterator();
        Iterator j = inItemKeys.iterator();
        while (true) {
            if (!i.hasNext() && !j.hasNext()) {
                break;
            }
            Item item = (Item) i.next();
            FlowGraph.EdgeKey key = (FlowGraph.EdgeKey) j.next();
            if (FlowGraph.EDGE_KEY_TRUE.equals(key)) {
                trueItems.add(item);
                trueItemKeys.add(key);
            } else if (FlowGraph.EDGE_KEY_FALSE.equals(key)) {
                falseItems.add(item);
                falseItemKeys.add(key);
            } else {
                otherItems.add(item);
                otherItemKeys.add(key);
            }
        }
        Item trueItem = trueItems.isEmpty() ? null : safeConfluence(trueItems, trueItemKeys, n, graph);
        Item falseItem = falseItems.isEmpty() ? null : safeConfluence(falseItems, falseItemKeys, n, graph);
        Item otherItem = otherItems.isEmpty() ? null : safeConfluence(otherItems, otherItemKeys, n, graph);
        return flow(trueItem, falseItem, otherItem, graph, n, edgeKeys);
    }

    protected Map flow(Item trueItem, Item falseItem, Item otherItem, FlowGraph graph, Term n, Set edgeKeys) {
        throw new InternalCompilerError("Unimplemented: should be implemented by subclasses if needed");
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Map flowBooleanConditions(Item trueItem, Item falseItem, Item otherItem, FlowGraph graph, Expr n, Set edgeKeys) {
        if (!n.type().isBoolean() || (!(n instanceof Binary) && !(n instanceof Unary))) {
            throw new InternalCompilerError("This method only takes binary or unary operators of boolean type");
        }
        if (trueItem == null || falseItem == null) {
            throw new IllegalArgumentException("The trueItem and falseItem for flowBooleanConditions must be non-null.");
        }
        if (n instanceof Unary) {
            Unary u = (Unary) n;
            if (u.operator() == Unary.NOT) {
                return itemsToMap(falseItem, trueItem, otherItem, edgeKeys);
            }
            return null;
        }
        Binary b = (Binary) n;
        if (b.operator() == Binary.COND_AND) {
            return itemsToMap(trueItem, falseItem, otherItem, edgeKeys);
        }
        if (b.operator() == Binary.COND_OR) {
            return itemsToMap(trueItem, falseItem, otherItem, edgeKeys);
        }
        if (b.operator() == Binary.BIT_AND) {
            Item bitANDFalse = safeConfluence(trueItem, FlowGraph.EDGE_KEY_TRUE, falseItem, FlowGraph.EDGE_KEY_FALSE, n, graph);
            return itemsToMap(trueItem, bitANDFalse, otherItem, edgeKeys);
        } else if (b.operator() == Binary.BIT_OR) {
            Item bitORTrue = safeConfluence(trueItem, FlowGraph.EDGE_KEY_TRUE, falseItem, FlowGraph.EDGE_KEY_FALSE, n, graph);
            return itemsToMap(bitORTrue, falseItem, otherItem, edgeKeys);
        } else {
            return null;
        }
    }

    protected Item confluence(List items, List itemKeys, Term node, FlowGraph graph) {
        return confluence(items, node, graph);
    }

    protected Item safeConfluence(List items, List itemKeys, Term node, FlowGraph graph) {
        if (items.isEmpty()) {
            return createInitialItem(graph, node);
        }
        if (items.size() == 1) {
            return (Item) items.get(0);
        }
        return confluence(items, itemKeys, node, graph);
    }

    protected Item safeConfluence(Item item1, FlowGraph.EdgeKey key1, Item item2, FlowGraph.EdgeKey key2, Term node, FlowGraph graph) {
        return safeConfluence(item1, key1, item2, key2, null, null, node, graph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public Item safeConfluence(Item item1, FlowGraph.EdgeKey key1, Item item2, FlowGraph.EdgeKey key2, Item item3, FlowGraph.EdgeKey key3, Term node, FlowGraph graph) {
        List items = new ArrayList(3);
        List itemKeys = new ArrayList(3);
        if (item1 != null) {
            items.add(item1);
            itemKeys.add(key1);
        }
        if (item2 != null) {
            items.add(item2);
            itemKeys.add(key2);
        }
        if (item3 != null) {
            items.add(item3);
            itemKeys.add(key3);
        }
        return safeConfluence(items, itemKeys, node, graph);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dataflow(CodeDecl cd) throws SemanticException {
        FlowGraph g;
        if (cd.body() != null && (g = initGraph(cd, cd)) != null) {
            CFGBuilder v = createCFGBuilder(this.ts, g);
            try {
                v.visitGraph();
                dataflow(g);
                post(g, cd);
                if (this.dataflowOnEntry) {
                    this.flowgraphStack.addFirst(new FlowGraphSource(g, cd));
                }
            } catch (CFGBuildError e) {
                throw new SemanticException(e.message(), e.position());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow$Frame.class */
    public static class Frame {
        FlowGraph.Peer peer;
        Iterator edges;

        Frame(FlowGraph.Peer p, boolean forward) {
            this.peer = p;
            if (!forward) {
                this.edges = p.preds().iterator();
            } else {
                this.edges = p.succs().iterator();
            }
        }
    }

    protected LinkedList findSCCs(FlowGraph graph) {
        Collection peers = graph.peers();
        Object[] sorted = new FlowGraph.Peer[peers.size()];
        Collection<FlowGraph.Peer> start = graph.peers(graph.startNode());
        int n = 0;
        LinkedList stack = new LinkedList();
        Set reachable = new HashSet();
        for (FlowGraph.Peer peer : start) {
            if (!reachable.contains(peer)) {
                reachable.add(peer);
                stack.addFirst(new Frame(peer, true));
                while (stack.size() != 0) {
                    Frame top = (Frame) stack.getFirst();
                    if (top.edges.hasNext()) {
                        FlowGraph.Edge e = (FlowGraph.Edge) top.edges.next();
                        FlowGraph.Peer q = e.getTarget();
                        if (!reachable.contains(q)) {
                            reachable.add(q);
                            stack.addFirst(new Frame(q, true));
                        }
                    } else {
                        stack.removeFirst();
                        int i = n;
                        n++;
                        sorted[i] = top.peer;
                    }
                }
            }
        }
        FlowGraph.Peer[] by_scc = new FlowGraph.Peer[n];
        int[] scc_head = new int[n];
        Set visited = new HashSet();
        int head = 0;
        for (int i2 = n - 1; i2 >= 0; i2--) {
            if (!visited.contains(sorted[i2])) {
                Set SCC = new HashSet();
                visited.add(sorted[i2]);
                stack.add(new Frame(sorted[i2], false));
                while (stack.size() != 0) {
                    Frame top2 = (Frame) stack.getFirst();
                    if (top2.edges.hasNext()) {
                        FlowGraph.Edge e2 = (FlowGraph.Edge) top2.edges.next();
                        FlowGraph.Peer q2 = e2.getTarget();
                        if (reachable.contains(q2) && !visited.contains(q2)) {
                            visited.add(q2);
                            Frame f = new Frame(q2, false);
                            stack.addFirst(f);
                        }
                    } else {
                        stack.removeFirst();
                        SCC.add(top2.peer);
                    }
                }
                stack.add(new Frame(sorted[i2], true));
                Set revisited = new HashSet();
                revisited.add(sorted[i2]);
                int scc_size = SCC.size();
                int nsorted = 0;
                while (stack.size() != 0) {
                    Frame top3 = (Frame) stack.getFirst();
                    if (top3.edges.hasNext()) {
                        FlowGraph.Edge e3 = (FlowGraph.Edge) top3.edges.next();
                        FlowGraph.Peer q3 = e3.getTarget();
                        if (SCC.contains(q3) && !revisited.contains(q3)) {
                            revisited.add(q3);
                            Frame f2 = new Frame(q3, true);
                            stack.addFirst(f2);
                        }
                    } else {
                        stack.removeFirst();
                        int n3 = ((head + scc_size) - nsorted) - 1;
                        scc_head[n3] = -2;
                        by_scc[n3] = top3.peer;
                        nsorted++;
                    }
                }
                scc_head[(head + scc_size) - 1] = head;
                scc_head[head] = -1;
                head += scc_size;
            }
        }
        if (Report.should_report(Report.dataflow, 2)) {
            for (int j = 0; j < n; j++) {
                switch (scc_head[j]) {
                    case -2:
                        Report.report(2, new StringBuffer().append(j).append("       : ").append(by_scc[j]).toString());
                        break;
                    case -1:
                        Report.report(2, new StringBuffer().append(j).append("[HEAD] : ").append(by_scc[j]).toString());
                        break;
                    default:
                        Report.report(2, new StringBuffer().append(j).append(" ->").append(scc_head[j]).append(" : ").append(by_scc[j]).toString());
                        break;
                }
                for (FlowGraph.Edge edge : by_scc[j].succs()) {
                    Report.report(3, new StringBuffer().append("     successor: ").append(edge.getTarget()).toString());
                }
            }
        }
        LinkedList ret = new LinkedList();
        ret.addFirst(scc_head);
        ret.addFirst(by_scc);
        return ret;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dataflow(FlowGraph graph) {
        if (Report.should_report(Report.dataflow, 1)) {
            Report.report(1, "Finding strongly connected components");
        }
        LinkedList pair = findSCCs(graph);
        FlowGraph.Peer[] by_scc = (FlowGraph.Peer[]) pair.getFirst();
        int[] scc_head = (int[]) pair.getLast();
        int npeers = by_scc.length;
        if (Report.should_report(Report.dataflow, 1)) {
            Report.report(1, "Iterating dataflow equations");
        }
        int current = 0;
        boolean change = false;
        while (current < npeers) {
            FlowGraph.Peer p = by_scc[current];
            if (scc_head[current] == -1) {
                change = false;
            }
            List inItems = new ArrayList(p.preds.size());
            List inItemKeys = new ArrayList(p.preds.size());
            for (FlowGraph.Edge e : p.preds) {
                FlowGraph.Peer o = e.getTarget();
                if (o.outItems != null) {
                    if (!o.outItems.keySet().contains(e.getKey())) {
                        throw new InternalCompilerError(new StringBuffer().append("There should have an out Item with edge key ").append(e.getKey()).append("; instead there were only ").append(o.outItems.keySet()).toString());
                    }
                    Item it = (Item) o.outItems.get(e.getKey());
                    if (it != null) {
                        inItems.add(it);
                        inItemKeys.add(e.getKey());
                    }
                }
            }
            Map oldOutItems = p.outItems;
            p.inItem = safeConfluence(inItems, inItemKeys, p.node, graph);
            p.outItems = flow(inItems, inItemKeys, graph, p.node, p.succEdgeKeys());
            if (!p.succEdgeKeys().equals(p.outItems.keySet())) {
                throw new InternalCompilerError(new StringBuffer().append("The flow only defined outputs for ").append(p.outItems.keySet()).append("; needs to ").append("define outputs for all of: ").append(p.succEdgeKeys()).toString());
            }
            if (oldOutItems != p.outItems && (oldOutItems == null || !oldOutItems.equals(p.outItems))) {
                change = true;
            }
            if (change && scc_head[current] >= 0) {
                current = scc_head[current];
            } else {
                current++;
            }
        }
        if (Report.should_report(Report.dataflow, 1)) {
            Report.report(1, "Done.");
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FlowGraph initGraph(CodeDecl code, Term root) {
        return new FlowGraph(root, this.forward);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public CFGBuilder createCFGBuilder(TypeSystem ts, FlowGraph g) {
        return new CFGBuilder(ts, g, this);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // polyglot.visit.ErrorHandlingVisitor
    public NodeVisitor enterCall(Node n) throws SemanticException {
        if (this.dataflowOnEntry && (n instanceof CodeDecl)) {
            dataflow((CodeDecl) n);
        }
        return this;
    }

    @Override // polyglot.visit.ErrorHandlingVisitor, polyglot.visit.NodeVisitor
    public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
        Object o;
        if (old != n && this.dataflowOnEntry && currentFlowGraph() != null && (o = currentFlowGraph().peerMap.get(new IdentityKey(old))) != null) {
            currentFlowGraph().peerMap.put(new IdentityKey(n), o);
        }
        return super.leave(parent, old, n, v);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node n) throws SemanticException {
        if (n instanceof CodeDecl) {
            if (!this.dataflowOnEntry) {
                dataflow((CodeDecl) n);
            } else if (this.dataflowOnEntry && !this.flowgraphStack.isEmpty()) {
                FlowGraphSource fgs = (FlowGraphSource) this.flowgraphStack.getFirst();
                if (fgs.source.equals(n)) {
                    this.flowgraphStack.removeFirst();
                }
            }
        }
        return n;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void post(FlowGraph graph, Term root) throws SemanticException {
        if (Report.should_report(Report.cfg, 2)) {
            dumpFlowGraph(graph, root);
        }
        Set uncheckedPeers = new HashSet(graph.peers());
        LinkedList peersToCheck = new LinkedList(graph.peers(graph.startNode()));
        while (!peersToCheck.isEmpty()) {
            FlowGraph.Peer p = (FlowGraph.Peer) peersToCheck.removeFirst();
            uncheckedPeers.remove(p);
            check(graph, p.node, p.inItem, p.outItems);
            for (FlowGraph.Edge edge : p.succs) {
                FlowGraph.Peer q = edge.getTarget();
                if (uncheckedPeers.contains(q) && !peersToCheck.contains(q)) {
                    peersToCheck.addLast(q);
                }
            }
            if (peersToCheck.isEmpty() && !uncheckedPeers.isEmpty()) {
                Iterator i = uncheckedPeers.iterator();
                peersToCheck.add(i.next());
                i.remove();
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public FlowGraph currentFlowGraph() {
        if (!this.dataflowOnEntry) {
            throw new InternalCompilerError("currentFlowGraph() cannot be called when dataflow is not performed on entry");
        }
        if (this.flowgraphStack.isEmpty()) {
            return null;
        }
        return ((FlowGraphSource) this.flowgraphStack.getFirst()).flowgraph;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public static final Map itemToMap(Item i, Set edgeKeys) {
        Map m = new HashMap();
        for (Object o : edgeKeys) {
            m.put(o, i);
        }
        return m;
    }

    protected static final Map itemsToMap(Item trueItem, Item falseItem, Item remainingItem, Set edgeKeys) {
        Map m = new HashMap();
        Iterator iter = edgeKeys.iterator();
        while (iter.hasNext()) {
            FlowGraph.EdgeKey k = (FlowGraph.EdgeKey) iter.next();
            if (FlowGraph.EDGE_KEY_TRUE.equals(k)) {
                m.put(k, trueItem);
            } else if (FlowGraph.EDGE_KEY_FALSE.equals(k)) {
                m.put(k, falseItem);
            } else {
                m.put(k, remainingItem);
            }
        }
        return m;
    }

    protected final List filterItemsNonError(List items, List itemKeys) {
        List filtered = new ArrayList(items.size());
        Iterator i = items.iterator();
        Iterator j = itemKeys.iterator();
        while (i.hasNext() && j.hasNext()) {
            Item item = (Item) i.next();
            FlowGraph.EdgeKey key = (FlowGraph.EdgeKey) j.next();
            if (!(key instanceof FlowGraph.ExceptionEdgeKey) || !((FlowGraph.ExceptionEdgeKey) key).type().isSubtype(this.ts.Error())) {
                filtered.add(item);
            }
        }
        if (i.hasNext() || j.hasNext()) {
            throw new InternalCompilerError("item and item key lists have different sizes.");
        }
        return filtered;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public final List filterItemsNonException(List items, List itemKeys) {
        List filtered = new ArrayList(items.size());
        Iterator i = items.iterator();
        Iterator j = itemKeys.iterator();
        while (i.hasNext() && j.hasNext()) {
            Item item = (Item) i.next();
            FlowGraph.EdgeKey key = (FlowGraph.EdgeKey) j.next();
            if (!(key instanceof FlowGraph.ExceptionEdgeKey)) {
                filtered.add(item);
            }
        }
        if (i.hasNext() || j.hasNext()) {
            throw new InternalCompilerError("item and item key lists have different sizes.");
        }
        return filtered;
    }

    protected final List filterItemsExceptionSubclass(List items, List itemKeys, Type excType) {
        List filtered = new ArrayList(items.size());
        Iterator i = items.iterator();
        Iterator j = itemKeys.iterator();
        while (i.hasNext() && j.hasNext()) {
            Item item = (Item) i.next();
            FlowGraph.EdgeKey key = (FlowGraph.EdgeKey) j.next();
            if (key instanceof FlowGraph.ExceptionEdgeKey) {
                FlowGraph.ExceptionEdgeKey eek = (FlowGraph.ExceptionEdgeKey) key;
                if (eek.type().isImplicitCastValid(excType)) {
                    filtered.add(item);
                }
            }
        }
        if (i.hasNext() || j.hasNext()) {
            throw new InternalCompilerError("item and item key lists have different sizes.");
        }
        return filtered;
    }

    protected final List filterItems(List items, List itemKeys, FlowGraph.EdgeKey filterEdgeKey) {
        List filtered = new ArrayList(items.size());
        Iterator i = items.iterator();
        Iterator j = itemKeys.iterator();
        while (i.hasNext() && j.hasNext()) {
            Item item = (Item) i.next();
            FlowGraph.EdgeKey key = (FlowGraph.EdgeKey) j.next();
            if (filterEdgeKey.equals(key)) {
                filtered.add(item);
            }
        }
        if (i.hasNext() || j.hasNext()) {
            throw new InternalCompilerError("item and item key lists have different sizes.");
        }
        return filtered;
    }

    protected static final boolean hasTrueFalseBranches(Set edgeKeys) {
        return edgeKeys.contains(FlowGraph.EDGE_KEY_FALSE) && edgeKeys.contains(FlowGraph.EDGE_KEY_TRUE);
    }

    protected static Map constructItemsFromCondition(Expr booleanCond, Item startingItem, Set succEdgeKeys, ConditionNavigator navigator) {
        if (!booleanCond.type().isBoolean()) {
            throw new IllegalArgumentException("booleanCond must be a boolean expression");
        }
        if (!hasTrueFalseBranches(succEdgeKeys)) {
            throw new IllegalArgumentException("succEdgeKeys does not have true and false branches.");
        }
        BoolItem results = navigator.navigate(booleanCond, startingItem);
        Map m = new HashMap();
        m.put(FlowGraph.EDGE_KEY_TRUE, results.trueItem);
        m.put(FlowGraph.EDGE_KEY_FALSE, results.falseItem);
        Iterator iter = succEdgeKeys.iterator();
        while (iter.hasNext()) {
            FlowGraph.EdgeKey e = (FlowGraph.EdgeKey) iter.next();
            if (!FlowGraph.EDGE_KEY_TRUE.equals(e) && !FlowGraph.EDGE_KEY_FALSE.equals(e)) {
                m.put(e, startingItem);
            }
        }
        return m;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow$BoolItem.class */
    public static class BoolItem {
        Item trueItem;
        Item falseItem;

        public BoolItem(Item trueItem, Item falseItem) {
            this.trueItem = trueItem;
            this.falseItem = falseItem;
        }

        public Item trueItem() {
            return this.trueItem;
        }

        public Item falseItem() {
            return this.falseItem;
        }

        public String toString() {
            return new StringBuffer().append("[ true: ").append(this.trueItem).append("; false: ").append(this.falseItem).append(" ]").toString();
        }
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DataFlow$ConditionNavigator.class */
    protected static abstract class ConditionNavigator {
        public abstract Item combine(Item item, Item item2);

        public abstract BoolItem handleExpression(Expr expr, Item item);

        protected ConditionNavigator() {
        }

        public BoolItem navigate(Expr expr, Item startingItem) {
            if (expr.type().isBoolean()) {
                if (expr instanceof Binary) {
                    Binary b = (Binary) expr;
                    if (Binary.COND_AND.equals(b.operator()) || Binary.BIT_AND.equals(b.operator())) {
                        BoolItem leftRes = navigate(b.left(), startingItem);
                        Item rightResStart = startingItem;
                        if (Binary.COND_AND.equals(b.operator())) {
                            rightResStart = leftRes.trueItem;
                        }
                        BoolItem rightRes = navigate(b.right(), rightResStart);
                        return andResults(leftRes, rightRes, startingItem);
                    } else if (Binary.COND_OR.equals(b.operator()) || Binary.BIT_OR.equals(b.operator())) {
                        BoolItem leftRes2 = navigate(b.left(), startingItem);
                        Item rightResStart2 = startingItem;
                        if (Binary.COND_OR.equals(b.operator())) {
                            rightResStart2 = leftRes2.falseItem;
                        }
                        BoolItem rightRes2 = navigate(b.right(), rightResStart2);
                        return orResults(leftRes2, rightRes2, startingItem);
                    }
                } else if (expr instanceof Unary) {
                    Unary u = (Unary) expr;
                    if (Unary.NOT.equals(u.operator())) {
                        BoolItem res = navigate(u.expr(), startingItem);
                        return notResult(res);
                    }
                }
            }
            return handleExpression(expr, startingItem);
        }

        public BoolItem andResults(BoolItem left, BoolItem right, Item startingItem) {
            return new BoolItem(combine(left.trueItem, right.trueItem), startingItem);
        }

        public BoolItem orResults(BoolItem left, BoolItem right, Item startingItem) {
            return new BoolItem(startingItem, combine(left.falseItem, right.falseItem));
        }

        public BoolItem notResult(BoolItem results) {
            return new BoolItem(results.falseItem, results.trueItem);
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    public void dumpFlowGraph(FlowGraph graph, Term root) {
        String label;
        StringBuffer append = new StringBuffer().append(StringUtil.getShortNameComponent(getClass().getName()));
        int i = flowCounter;
        flowCounter = i + 1;
        String name = append.append(i).toString();
        String rootName = "";
        if (graph.root() instanceof CodeDecl) {
            CodeDecl cd = (CodeDecl) graph.root();
            rootName = new StringBuffer().append(cd.codeInstance().toString()).append(" in ").append(cd.codeInstance().container().toString()).toString();
        }
        Report.report(2, new StringBuffer().append("digraph DataFlow").append(name).append(" {").toString());
        Report.report(2, new StringBuffer().append("  label=\"Dataflow: ").append(name).append("\\n").append(rootName).append("\"; fontsize=20; center=true; ratio=auto; size = \"8.5,11\";").toString());
        for (FlowGraph.Peer p : graph.peers()) {
            Report.report(2, new StringBuffer().append(p.hashCode()).append(" [ label = \"").append(StringUtil.escape(p.node.toString())).append("\\n(").append(StringUtil.escape(StringUtil.getShortNameComponent(p.node.getClass().getName()))).append(")\" ];").toString());
            for (FlowGraph.Edge q : p.succs) {
                Report.report(2, new StringBuffer().append(q.getTarget().hashCode()).append(" [ label = \"").append(StringUtil.escape(q.getTarget().node.toString())).append(" (").append(StringUtil.escape(StringUtil.getShortNameComponent(q.getTarget().node.getClass().getName()))).append(")\" ];").toString());
                String label2 = q.getKey().toString();
                if (p.outItems != null) {
                    label = new StringBuffer().append(label2).append("\\n").append(p.outItems.get(q.getKey())).toString();
                } else {
                    label = new StringBuffer().append(label2).append("\\n[no dataflow available]").toString();
                }
                Report.report(2, new StringBuffer().append(p.hashCode()).append(" -> ").append(q.getTarget().hashCode()).append(" [label=\"").append(label).append("\"];").toString());
            }
        }
        Report.report(2, "}");
    }
}
