package polyglot.visit;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.CodeDecl;
import polyglot.ast.MethodDecl;
import polyglot.ast.NodeFactory;
import polyglot.ast.Return;
import polyglot.ast.Term;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.visit.DataFlow;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ExitChecker.class */
public class ExitChecker extends DataFlow {
    protected CodeDecl code;

    public ExitChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf, false);
    }

    @Override // polyglot.visit.DataFlow
    protected FlowGraph initGraph(CodeDecl code, Term root) {
        this.code = code;
        if (code instanceof MethodDecl) {
            MethodDecl d = (MethodDecl) code;
            if (!d.methodInstance().returnType().isVoid()) {
                return super.initGraph(code, root);
            }
            return null;
        }
        return null;
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
        return DataFlowItem.EXITS;
    }

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ExitChecker$DataFlowItem.class */
    protected static class DataFlowItem extends DataFlow.Item {
        final boolean exits;
        public static final DataFlowItem EXITS = new DataFlowItem(true);
        public static final DataFlowItem DOES_NOT_EXIT = new DataFlowItem(false);

        protected DataFlowItem(boolean exits) {
            this.exits = exits;
        }

        public String toString() {
            return new StringBuffer().append("exits=").append(this.exits).toString();
        }

        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object o) {
            return (o instanceof DataFlowItem) && this.exits == ((DataFlowItem) o).exits;
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            return this.exits ? 5235 : 8673;
        }
    }

    @Override // polyglot.visit.DataFlow
    public Map flow(DataFlow.Item in, FlowGraph graph, Term n, Set succEdgeKeys) {
        if (n instanceof Return) {
            return itemToMap(DataFlowItem.EXITS, succEdgeKeys);
        }
        if (n == graph.exitNode()) {
            Map m = itemToMap(DataFlowItem.EXITS, succEdgeKeys);
            if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_OTHER)) {
                m.put(FlowGraph.EDGE_KEY_OTHER, DataFlowItem.DOES_NOT_EXIT);
            }
            if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_TRUE)) {
                m.put(FlowGraph.EDGE_KEY_TRUE, DataFlowItem.DOES_NOT_EXIT);
            }
            if (succEdgeKeys.contains(FlowGraph.EDGE_KEY_FALSE)) {
                m.put(FlowGraph.EDGE_KEY_FALSE, DataFlowItem.DOES_NOT_EXIT);
            }
            return m;
        }
        return itemToMap(in, succEdgeKeys);
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
        Iterator i = inItems.iterator();
        while (i.hasNext()) {
            if (!((DataFlowItem) i.next()).exits) {
                return DataFlowItem.DOES_NOT_EXIT;
            }
        }
        return DataFlowItem.EXITS;
    }

    @Override // polyglot.visit.DataFlow
    public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
        DataFlowItem outItem;
        if (n == graph.entryNode() && outItems != null && !outItems.isEmpty() && (outItem = (DataFlowItem) outItems.values().iterator().next()) != null && !outItem.exits) {
            throw new SemanticException("Missing return statement.", this.code.position());
        }
    }
}
