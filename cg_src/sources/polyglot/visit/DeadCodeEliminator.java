package polyglot.visit;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import polyglot.ast.Assign;
import polyglot.ast.Block;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Do;
import polyglot.ast.Empty;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.For;
import polyglot.ast.If;
import polyglot.ast.Local;
import polyglot.ast.LocalAssign;
import polyglot.ast.LocalDecl;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.ProcedureCall;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Term;
import polyglot.ast.Unary;
import polyglot.ast.While;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.LocalInstance;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.DataFlow;
import polyglot.visit.FlowGraph;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DeadCodeEliminator.class */
public class DeadCodeEliminator extends DataFlow {
    public DeadCodeEliminator(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf, false, true);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DeadCodeEliminator$DataFlowItem.class */
    public static class DataFlowItem extends DataFlow.Item {
        private Set liveVars;
        private Set liveDecls;

        protected DataFlowItem() {
            this.liveVars = new HashSet();
            this.liveDecls = new HashSet();
        }

        protected DataFlowItem(DataFlowItem dfi) {
            this.liveVars = new HashSet(dfi.liveVars);
            this.liveDecls = new HashSet(dfi.liveDecls);
        }

        public void add(LocalInstance li) {
            this.liveVars.add(li);
            this.liveDecls.add(li);
        }

        public void addAll(Set lis) {
            this.liveVars.addAll(lis);
            this.liveDecls.addAll(lis);
        }

        public void remove(LocalInstance li) {
            this.liveVars.remove(li);
        }

        public void removeAll(Set lis) {
            this.liveVars.removeAll(lis);
        }

        public void removeDecl(LocalInstance li) {
            this.liveVars.remove(li);
            this.liveDecls.remove(li);
        }

        public void union(DataFlowItem dfi) {
            this.liveVars.addAll(dfi.liveVars);
            this.liveDecls.addAll(dfi.liveDecls);
        }

        protected boolean needDecl(LocalInstance li) {
            return this.liveDecls.contains(li);
        }

        protected boolean needDef(LocalInstance li) {
            return this.liveVars.contains(li);
        }

        @Override // polyglot.visit.DataFlow.Item
        public int hashCode() {
            int result = 0;
            for (Object obj : this.liveVars) {
                result = (31 * result) + obj.hashCode();
            }
            for (Object obj2 : this.liveDecls) {
                result = (31 * result) + obj2.hashCode();
            }
            return result;
        }

        @Override // polyglot.visit.DataFlow.Item
        public boolean equals(Object o) {
            if (o instanceof DataFlowItem) {
                DataFlowItem dfi = (DataFlowItem) o;
                return this.liveVars.equals(dfi.liveVars) && this.liveDecls.equals(dfi.liveDecls);
            }
            return false;
        }

        public String toString() {
            return new StringBuffer().append("<vars=").append(this.liveVars).append(" ; decls=").append(this.liveDecls).append(">").toString();
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
                result.union(inItem);
            }
        }
        return result;
    }

    @Override // polyglot.visit.DataFlow
    public Map flow(DataFlow.Item in, FlowGraph graph, Term t, Set succEdgeKeys) {
        return itemToMap(flow(in, graph, t), succEdgeKeys);
    }

    protected DataFlowItem flow(DataFlow.Item in, FlowGraph graph, Term t) {
        DataFlowItem result = new DataFlowItem((DataFlowItem) in);
        Set[] du = null;
        if (t instanceof LocalDecl) {
            LocalDecl n = (LocalDecl) t;
            LocalInstance to = n.localInstance();
            result.removeDecl(to);
            du = getDefUse(n.init());
        } else if ((t instanceof Stmt) && !(t instanceof CompoundStmt)) {
            du = getDefUse((Stmt) t);
        } else if (t instanceof CompoundStmt) {
            if (t instanceof If) {
                du = getDefUse(((If) t).cond());
            } else if (t instanceof Switch) {
                du = getDefUse(((Switch) t).expr());
            } else if (t instanceof Do) {
                du = getDefUse(((Do) t).cond());
            } else if (t instanceof For) {
                du = getDefUse(((For) t).cond());
            } else if (t instanceof While) {
                du = getDefUse(((While) t).cond());
            }
        }
        if (du != null) {
            result.removeAll(du[0]);
            result.addAll(du[1]);
        }
        return result;
    }

    @Override // polyglot.visit.DataFlow
    public void post(FlowGraph graph, Term root) throws SemanticException {
        if (Report.should_report(Report.cfg, 2)) {
            dumpFlowGraph(graph, root);
        }
    }

    @Override // polyglot.visit.DataFlow
    public void check(FlowGraph graph, Term n, DataFlow.Item inItem, Map outItems) throws SemanticException {
        throw new InternalCompilerError("DeadCodeEliminator.check should never be called.");
    }

    private DataFlowItem getItem(Term n) {
        Collection<FlowGraph.Peer> peers;
        FlowGraph g = currentFlowGraph();
        if (g == null || (peers = g.peers(n)) == null || peers.isEmpty()) {
            return null;
        }
        List items = new ArrayList();
        for (FlowGraph.Peer p : peers) {
            if (p.inItem() != null) {
                items.add(p.inItem());
            }
        }
        return (DataFlowItem) confluence(items, n, g);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    public Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        Local local;
        if (n instanceof LocalDecl) {
            LocalDecl ld = (LocalDecl) n;
            DataFlowItem in = getItem(ld);
            return (in == null || in.needDecl(ld.localInstance())) ? n : getEffects(ld.init());
        } else if (n instanceof Eval) {
            Eval eval = (Eval) n;
            Expr expr = eval.expr();
            Expr right = null;
            if (expr instanceof Assign) {
                Assign assign = (Assign) expr;
                Expr left = assign.left();
                right = assign.right();
                if (!(left instanceof Local)) {
                    return n;
                }
                local = (Local) left;
            } else if (expr instanceof Unary) {
                Unary unary = (Unary) expr;
                Expr expr2 = unary.expr();
                if (!(expr2 instanceof Local)) {
                    return n;
                }
                local = (Local) expr2;
            } else {
                return n;
            }
            DataFlowItem in2 = getItem(eval);
            if (in2 == null || in2.needDef(local.localInstance())) {
                return n;
            }
            if (right != null) {
                return getEffects(right);
            }
            return this.nf.Empty(Position.COMPILER_GENERATED);
        } else if (n instanceof Block) {
            Block b = (Block) n;
            List stmts = new ArrayList(b.statements());
            Iterator it = stmts.iterator();
            while (it.hasNext()) {
                if (it.next() instanceof Empty) {
                    it.remove();
                }
            }
            return b.statements(stmts);
        } else {
            return n;
        }
    }

    protected Set[] getDefUse(Node n) {
        Set def = new HashSet();
        Set use = new HashSet();
        if (n != null) {
            n.visit(createDefUseFinder(def, use));
        }
        return new Set[]{def, use};
    }

    protected NodeVisitor createDefUseFinder(Set def, Set use) {
        return new DefUseFinder(def, use);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/DeadCodeEliminator$DefUseFinder.class */
    public static class DefUseFinder extends HaltingVisitor {
        protected Set def;
        protected Set use;

        public DefUseFinder(Set def, Set use) {
            this.def = def;
            this.use = use;
        }

        @Override // polyglot.visit.NodeVisitor
        public NodeVisitor enter(Node n) {
            if (n instanceof LocalAssign) {
                return bypass(((Assign) n).left());
            }
            return super.enter(n);
        }

        @Override // polyglot.visit.NodeVisitor
        public Node leave(Node old, Node n, NodeVisitor v) {
            if (n instanceof Local) {
                this.use.add(((Local) n).localInstance());
            } else if (n instanceof Assign) {
                Expr left = ((Assign) n).left();
                if (left instanceof Local) {
                    this.def.add(((Local) left).localInstance());
                }
            }
            return n;
        }
    }

    protected Stmt getEffects(Expr expr) {
        Stmt empty = this.nf.Empty(Position.COMPILER_GENERATED);
        if (expr == null) {
            return empty;
        }
        List result = new LinkedList();
        Position pos = Position.COMPILER_GENERATED;
        NodeVisitor v = new HaltingVisitor(this, result, pos) { // from class: polyglot.visit.DeadCodeEliminator.1
            private final List val$result;
            private final Position val$pos;
            private final DeadCodeEliminator this$0;

            @Override // polyglot.visit.NodeVisitor
            public NodeVisitor enter(Node n) {
                Unary.Operator op;
                if ((n instanceof Assign) || (n instanceof ProcedureCall)) {
                    return bypassChildren(n);
                }
                if ((n instanceof Unary) && ((op = ((Unary) n).operator()) == Unary.POST_INC || op == Unary.POST_DEC || op == Unary.PRE_INC || op == Unary.PRE_INC)) {
                    return bypassChildren(n);
                }
                return this;
            }

            {
                this.this$0 = this;
                this.val$result = result;
                this.val$pos = pos;
            }

            @Override // polyglot.visit.NodeVisitor
            public Node leave(Node old, Node n, NodeVisitor v2) {
                Unary.Operator op;
                if ((n instanceof Assign) || (n instanceof ProcedureCall)) {
                    this.val$result.add(this.this$0.nf.Eval(this.val$pos, (Expr) n));
                } else if ((n instanceof Unary) && ((op = ((Unary) n).operator()) == Unary.POST_INC || op == Unary.POST_DEC || op == Unary.PRE_INC || op == Unary.PRE_INC)) {
                    this.val$result.add(this.this$0.nf.Eval(this.val$pos, (Expr) n));
                }
                return n;
            }
        };
        expr.visit(v);
        return result.isEmpty() ? empty : result.size() == 1 ? (Stmt) result.get(0) : this.nf.Block(Position.COMPILER_GENERATED, result);
    }
}
