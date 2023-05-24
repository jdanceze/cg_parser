package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.Catch;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.If;
import polyglot.ast.Local;
import polyglot.ast.LocalDecl;
import polyglot.ast.Loop;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.visit.DataFlow;
import polyglot.visit.FlowGraph;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CopyPropagator.class */
public class CopyPropagator extends DataFlow {
    public CopyPropagator(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf, true, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CopyPropagator$DataFlowItem.class */
    public static class DataFlowItem extends DataFlow.Item {
        private Map map;

        protected DataFlowItem() {
            this.map = new HashMap();
        }

        protected DataFlowItem(DataFlowItem dfi) {
            this.map = new HashMap(dfi.map.size());
            for (Map.Entry e : dfi.map.entrySet()) {
                LocalInstance li = (LocalInstance) e.getKey();
                CopyInfo ci = (CopyInfo) e.getValue();
                if (ci.from != null) {
                    add(ci.from.li, li);
                }
            }
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CopyPropagator$DataFlowItem$CopyInfo.class */
        public static class CopyInfo {
            final LocalInstance li;
            CopyInfo from;
            Set to;
            CopyInfo root;

            protected CopyInfo(LocalInstance li) {
                if (li == null) {
                    throw new InternalCompilerError("Null local instance encountered during copy propagation.");
                }
                this.li = li;
                this.from = null;
                this.to = new HashSet();
                this.root = this;
            }

            protected void setRoot(CopyInfo root) {
                List worklist = new ArrayList();
                worklist.add(this);
                while (worklist.size() > 0) {
                    CopyInfo ci = (CopyInfo) worklist.remove(worklist.size() - 1);
                    worklist.addAll(ci.to);
                    ci.root = root;
                }
            }

            public boolean equals(Object o) {
                if (o instanceof CopyInfo) {
                    CopyInfo ci = (CopyInfo) o;
                    return this.li == ci.li && (this.from != null ? !(ci.from == null || this.from.li != ci.from.li) : ci.from == null) && this.root.li == ci.root.li;
                }
                return false;
            }

            public int hashCode() {
                return this.li.hashCode() + (31 * (this.from == null ? 0 : this.from.li.hashCode() + (31 * this.root.li.hashCode())));
            }
        }

        protected void add(LocalInstance from, LocalInstance to) {
            CopyInfo ciTo;
            CopyInfo ciFrom;
            boolean newTo = !this.map.containsKey(to);
            if (newTo) {
                ciTo = new CopyInfo(to);
                this.map.put(to, ciTo);
            } else {
                ciTo = (CopyInfo) this.map.get(to);
            }
            if (this.map.containsKey(from)) {
                ciFrom = (CopyInfo) this.map.get(from);
            } else {
                ciFrom = new CopyInfo(from);
                this.map.put(from, ciFrom);
                ciFrom.root = ciFrom;
            }
            if (ciTo.from != null) {
                throw new InternalCompilerError("Error while copying dataflow item during copy propagation.");
            }
            ciFrom.to.add(ciTo);
            ciTo.from = ciFrom;
            if (newTo) {
                ciTo.root = ciFrom.root;
            } else {
                ciTo.setRoot(ciFrom.root);
            }
        }

        protected void intersect(DataFlowItem dfi) {
            boolean modified = false;
            Iterator it = this.map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                LocalInstance li = (LocalInstance) e.getKey();
                CopyInfo ci = (CopyInfo) e.getValue();
                if (!dfi.map.containsKey(li)) {
                    modified = true;
                    it.remove();
                    if (ci.from != null) {
                        ci.from.to.remove(ci);
                    }
                    for (CopyInfo toCI : ci.to) {
                        toCI.from = null;
                    }
                } else if (ci.from != null) {
                    CopyInfo otherCI = (CopyInfo) dfi.map.get(li);
                    CopyInfo otherCIfrom = (CopyInfo) dfi.map.get(ci.from.li);
                    if (otherCIfrom == null || otherCI.root != otherCIfrom.root) {
                        modified = true;
                        ci.from.to.remove(ci);
                        ci.from = null;
                    }
                }
            }
            if (modified) {
                Iterator it2 = this.map.entrySet().iterator();
                while (it2.hasNext()) {
                    CopyInfo ci2 = (CopyInfo) ((Map.Entry) it2.next()).getValue();
                    if (ci2.from == null) {
                        if (ci2.to.isEmpty()) {
                            it2.remove();
                        } else {
                            ci2.setRoot(ci2);
                        }
                    }
                }
            }
        }

        public void kill(LocalInstance var) {
            if (this.map.containsKey(var)) {
                CopyInfo ci = (CopyInfo) this.map.get(var);
                this.map.remove(var);
                if (ci.from != null) {
                    ci.from.to.remove(ci);
                }
                for (CopyInfo toCI : ci.to) {
                    toCI.from = ci.from;
                    if (ci.from == null) {
                        toCI.setRoot(toCI);
                    } else {
                        ci.from.to.add(toCI);
                    }
                }
            }
        }

        public LocalInstance getRoot(LocalInstance var) {
            if (this.map.containsKey(var)) {
                return ((CopyInfo) this.map.get(var)).root.li;
            }
            return null;
        }

        private void die() {
            throw new InternalCompilerError("Copy propagation dataflow item consistency error.");
        }

        private void consistencyCheck() {
            for (Map.Entry e : this.map.entrySet()) {
                LocalInstance li = (LocalInstance) e.getKey();
                CopyInfo ci = (CopyInfo) e.getValue();
                if (li != ci.li) {
                    die();
                }
                if (!this.map.containsKey(ci.root.li)) {
                    die();
                }
                if (this.map.get(ci.root.li) != ci.root) {
                    die();
                }
                if (ci.from == null) {
                    if (ci.root != ci) {
                        die();
                    }
                } else {
                    if (!this.map.containsKey(ci.from.li)) {
                        die();
                    }
                    if (this.map.get(ci.from.li) != ci.from) {
                        die();
                    }
                    if (ci.from.root != ci.root) {
                        die();
                    }
                    if (!ci.from.to.contains(ci)) {
                        die();
                    }
                }
                for (CopyInfo toCI : ci.to) {
                    if (!this.map.containsKey(toCI.li)) {
                        die();
                    }
                    if (this.map.get(toCI.li) != toCI) {
                        die();
                    }
                    if (toCI.root != ci.root) {
                        die();
                    }
                    if (toCI.from != ci) {
                        die();
                    }
                }
            }
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            int result = 0;
            for (Map.Entry e : this.map.entrySet()) {
                result = (31 * ((31 * result) + e.getKey().hashCode())) + e.getValue().hashCode();
            }
            return result;
        }

        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object o) {
            if (o instanceof DataFlowItem) {
                DataFlowItem dfi = (DataFlowItem) o;
                return this.map.equals(dfi.map);
            }
            return false;
        }

        public String toString() {
            String result = "";
            boolean first = true;
            for (CopyInfo ci : this.map.values()) {
                if (ci.from != null) {
                    if (!first) {
                        result = new StringBuffer().append(result).append(", ").toString();
                    }
                    if (ci.root != ci.from) {
                        result = new StringBuffer().append(result).append(ci.root.li).append(" ->* ").toString();
                    }
                    result = new StringBuffer().append(result).append(ci.from.li).append(" -> ").append(ci.li).toString();
                    first = false;
                }
            }
            return new StringBuffer().append("[").append(result).append("]").toString();
        }
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item createInitialItem(FlowGraph graph, Term node) {
        return new DataFlowItem();
    }

    @Override // polyglot.visit.DataFlow
    public DataFlow.Item confluence(List inItems, Term node, FlowGraph graph) {
        DataFlowItem result = null;
        Iterator it = inItems.iterator();
        while (it.hasNext()) {
            DataFlowItem inItem = (DataFlowItem) it.next();
            if (result == null) {
                result = new DataFlowItem(inItem);
            } else {
                result.intersect(inItem);
            }
        }
        return result;
    }

    private void killDecl(DataFlowItem dfi, Stmt stmt) {
        if (stmt instanceof LocalDecl) {
            dfi.kill(((LocalDecl) stmt).localInstance());
        }
    }

    protected DataFlowItem flow(DataFlow.Item in, FlowGraph graph, Term t) {
        DataFlowItem result = new DataFlowItem((DataFlowItem) in);
        if (t instanceof Assign) {
            Assign n = (Assign) t;
            Assign.Operator op = n.operator();
            Expr left = n.left();
            Expr right = n.right();
            if (left instanceof Local) {
                LocalInstance to = ((Local) left).localInstance();
                result.kill(to);
                if ((right instanceof Local) && op == Assign.ASSIGN) {
                    LocalInstance from = ((Local) right).localInstance();
                    result.add(from, to);
                }
            }
        } else if (t instanceof Unary) {
            Unary n2 = (Unary) t;
            Unary.Operator op2 = n2.operator();
            Expr expr = n2.expr();
            if ((expr instanceof Local) && (op2 == Unary.POST_INC || op2 == Unary.POST_DEC || op2 == Unary.PRE_INC || op2 == Unary.PRE_DEC)) {
                result.kill(((Local) expr).localInstance());
            }
        } else if (t instanceof LocalDecl) {
            LocalDecl n3 = (LocalDecl) t;
            LocalInstance to2 = n3.localInstance();
            result.kill(to2);
            if (!n3.flags().isFinal() && (n3.init() instanceof Local)) {
                LocalInstance from2 = ((Local) n3.init()).localInstance();
                result.add(from2, to2);
            }
        } else if (t instanceof Block) {
            for (Stmt stmt : ((Block) t).statements()) {
                killDecl(result, stmt);
            }
        } else if (t instanceof Loop) {
            if (t instanceof For) {
                for (Stmt stmt2 : ((For) t).inits()) {
                    killDecl(result, stmt2);
                }
            }
            killDecl(result, ((Loop) t).body());
        } else if (t instanceof Catch) {
            result.kill(((Catch) t).formal().localInstance());
        } else if (t instanceof If) {
            If n4 = (If) t;
            killDecl(result, n4.consequent());
            killDecl(result, n4.alternative());
        }
        return result;
    }

    @Override // polyglot.visit.DataFlow
    public Map flow(DataFlow.Item in, FlowGraph graph, Term t, Set succEdgeKeys) {
        return itemToMap(flow(in, graph, t), succEdgeKeys);
    }

    @Override // polyglot.visit.DataFlow
    public void post(FlowGraph graph, Term root) throws SemanticException {
        if (Report.should_report(Report.cfg, 2)) {
            dumpFlowGraph(graph, root);
        }
    }

    @Override // polyglot.visit.DataFlow
    public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
        throw new InternalCompilerError("CopyPropagator.check should never be called.");
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        LocalInstance root;
        if (n instanceof Local) {
            FlowGraph g = currentFlowGraph();
            if (g == null) {
                return n;
            }
            Local l = (Local) n;
            Collection<FlowGraph.Peer> peers = g.peers(l);
            if (peers == null || peers.isEmpty()) {
                return n;
            }
            List items = new ArrayList();
            for (FlowGraph.Peer p : peers) {
                if (p.inItem() != null) {
                    items.add(p.inItem());
                }
            }
            DataFlowItem in = (DataFlowItem) confluence(items, l, g);
            if (in != null && (root = in.getRoot(l.localInstance())) != null) {
                return l.name(root.name()).localInstance(root);
            }
            return n;
        } else if (n instanceof Unary) {
            return old;
        } else {
            if (n instanceof Assign) {
                Assign oldAssign = (Assign) old;
                Assign newAssign = (Assign) n;
                return newAssign.left(oldAssign.left());
            }
            return n;
        }
    }
}
