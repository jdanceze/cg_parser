package polyglot.visit;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Block;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Initializer;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.visit.DataFlow;
import polyglot.visit.FlowGraph;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ReachChecker.class */
public class ReachChecker extends DataFlow {
    public ReachChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf, true, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ReachChecker$DataFlowItem.class */
    public static class DataFlowItem extends DataFlow.Item {
        final boolean reachable;
        final boolean normalReachable;
        public static final DataFlowItem REACHABLE = new DataFlowItem(true, true);
        public static final DataFlowItem REACHABLE_EX_ONLY = new DataFlowItem(true, false);
        public static final DataFlowItem NOT_REACHABLE = new DataFlowItem(false, false);

        protected DataFlowItem(boolean reachable, boolean normalReachable) {
            this.reachable = reachable;
            this.normalReachable = normalReachable;
        }

        public String toString() {
            return new StringBuffer().append(this.reachable ? "" : "not ").append("reachable").append(this.normalReachable ? "" : " by exceptions only").toString();
        }

        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object o) {
            return (o instanceof DataFlowItem) && this.reachable == ((DataFlowItem) o).reachable && this.normalReachable == ((DataFlowItem) o).normalReachable;
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            return (this.reachable ? 5423 : 5753) + (this.normalReachable ? 31 : -2);
        }
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
        if (node == graph.entryNode()) {
            return DataFlowItem.REACHABLE;
        }
        return DataFlowItem.NOT_REACHABLE;
    }

    @Override // polyglot.visit.DataFlow
    public Map flow(DataFlow.Item in, FlowGraph graph, Term n, Set succEdgeKeys) {
        if (in == DataFlowItem.NOT_REACHABLE) {
            return itemToMap(in, succEdgeKeys);
        }
        Map m = itemToMap(DataFlowItem.REACHABLE_EX_ONLY, succEdgeKeys);
        if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_OTHER)) {
            m.put(FlowGraph.EDGE_KEY_OTHER, DataFlowItem.REACHABLE);
        }
        if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_TRUE)) {
            m.put(FlowGraph.EDGE_KEY_TRUE, DataFlowItem.REACHABLE);
        }
        if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_FALSE)) {
            m.put(FlowGraph.EDGE_KEY_FALSE, DataFlowItem.REACHABLE);
        }
        return m;
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
        throw new InternalCompilerError("Should never be called.");
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item confluence(List inItems, List itemKeys, Term node, FlowGraph graph) {
        List<Object> l = filterItemsNonException(inItems, itemKeys);
        for (Object obj : l) {
            if (obj == DataFlowItem.REACHABLE) {
                return DataFlowItem.REACHABLE;
            }
        }
        Iterator i = inItems.iterator();
        while (i.hasNext()) {
            if (((DataFlowItem) i.next()).reachable) {
                return DataFlowItem.REACHABLE_EX_ONLY;
            }
        }
        return DataFlowItem.NOT_REACHABLE;
    }

    @Override // polyglot.visit.DataFlow, polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node n) throws SemanticException {
        if (n instanceof Term) {
            n = checkReachability((Term) n);
        }
        return super.leaveCall(n);
    }

    protected Node checkReachability(Term n) throws SemanticException {
        Collection<FlowGraph.Peer> peers;
        FlowGraph g = currentFlowGraph();
        if (g != null && (peers = g.peers(n)) != null && !peers.isEmpty()) {
            boolean isInitializer = n instanceof Initializer;
            for (FlowGraph.Peer p : peers) {
                if (p.inItem() != null) {
                    DataFlowItem dfi = (DataFlowItem) p.inItem();
                    if (isInitializer && !dfi.normalReachable) {
                        throw new SemanticException("Initializers must be able to complete normally.", n.position());
                    }
                    if (dfi.reachable) {
                        return n.reachable(true);
                    }
                }
                if (p.outItems != null) {
                    for (DataFlowItem item : p.outItems.values()) {
                        if (item != null && item.reachable) {
                            return n.reachable(true);
                        }
                    }
                    continue;
                }
            }
            n = n.reachable(false);
            if (((n instanceof Block) && ((Block) n).statements().isEmpty()) || ((n instanceof Stmt) && !(n instanceof CompoundStmt))) {
                throw new SemanticException("Unreachable statement.", n.position());
            }
        }
        return n;
    }

    @Override // polyglot.visit.DataFlow
    public void post(FlowGraph graph, Term root) throws SemanticException {
        if (Report.should_report(Report.cfg, 2)) {
            dumpFlowGraph(graph, root);
        }
    }

    @Override // polyglot.visit.DataFlow
    public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
        throw new InternalCompilerError("ReachChecker.check should never be called.");
    }
}
