package polyglot.visit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Branch;
import polyglot.ast.Catch;
import polyglot.ast.CodeDecl;
import polyglot.ast.CompoundStmt;
import polyglot.ast.Labeled;
import polyglot.ast.Loop;
import polyglot.ast.Return;
import polyglot.ast.Stmt;
import polyglot.ast.Switch;
import polyglot.ast.Term;
import polyglot.ast.Try;
import polyglot.main.Report;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CollectionUtil;
import polyglot.util.Copy;
import polyglot.util.InternalCompilerError;
import polyglot.util.StringUtil;
import polyglot.visit.FlowGraph;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CFGBuilder.class */
public class CFGBuilder implements Copy {
    protected FlowGraph graph;
    protected TypeSystem ts;
    protected DataFlow df;
    static int counter = 0;
    protected List path_to_finally = Collections.EMPTY_LIST;
    protected CFGBuilder outer = null;
    protected Stmt innermostTarget = null;
    protected boolean skipInnermostCatches = false;
    protected boolean errorEdgesToExitNode = false;

    public CFGBuilder(TypeSystem ts, FlowGraph graph, DataFlow df) {
        this.ts = ts;
        this.graph = graph;
        this.df = df;
    }

    public TypeSystem typeSystem() {
        return this.ts;
    }

    @Override // polyglot.util.Copy
    public Object copy() {
        try {
            return (CFGBuilder) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalCompilerError("Java clone() weirdness.");
        }
    }

    public CFGBuilder push(Stmt n) {
        return push(n, false);
    }

    public CFGBuilder push(Stmt n, boolean skipInnermostCatches) {
        CFGBuilder v = (CFGBuilder) copy();
        v.outer = this;
        v.innermostTarget = n;
        v.skipInnermostCatches = skipInnermostCatches;
        return v;
    }

    public void visitBranchTarget(Branch b) {
        Block block = b;
        CFGBuilder last_visitor = this;
        CFGBuilder cFGBuilder = this;
        while (true) {
            CFGBuilder v = cFGBuilder;
            if (v != null) {
                Term c = v.innermostTarget;
                if (c instanceof Try) {
                    Try tr = (Try) c;
                    if (tr.finallyBlock() != null) {
                        last_visitor = tryFinally(v, block, last_visitor, tr.finallyBlock());
                        block = tr.finallyBlock();
                    }
                }
                if (b.label() != null) {
                    if (c instanceof Labeled) {
                        Labeled l = (Labeled) c;
                        if (l.label().equals(b.label())) {
                            if (b.kind() == Branch.BREAK) {
                                edge(last_visitor, block, l, FlowGraph.EDGE_KEY_OTHER);
                                return;
                            }
                            Stmt s = l.statement();
                            if (s instanceof Loop) {
                                Loop loop = (Loop) s;
                                edge(last_visitor, block, loop.continueTarget(), FlowGraph.EDGE_KEY_OTHER);
                                return;
                            }
                            throw new CFGBuildError("Target of continue statement must be a loop.", l.position());
                        }
                    } else {
                        continue;
                    }
                } else if (c instanceof Loop) {
                    Loop l2 = (Loop) c;
                    if (b.kind() == Branch.CONTINUE) {
                        edge(last_visitor, block, l2.continueTarget(), FlowGraph.EDGE_KEY_OTHER);
                        return;
                    } else {
                        edge(last_visitor, block, l2, FlowGraph.EDGE_KEY_OTHER);
                        return;
                    }
                } else if ((c instanceof Switch) && b.kind() == Branch.BREAK) {
                    edge(last_visitor, block, c, FlowGraph.EDGE_KEY_OTHER);
                    return;
                }
                cFGBuilder = v.outer;
            } else {
                throw new CFGBuildError("Target of branch statement not found.", b.position());
            }
        }
    }

    public void visitReturn(Return r) {
        Term last = r;
        CFGBuilder last_visitor = this;
        CFGBuilder cFGBuilder = this;
        while (true) {
            CFGBuilder v = cFGBuilder;
            if (v != null) {
                Term c = v.innermostTarget;
                if (c instanceof Try) {
                    Try tr = (Try) c;
                    if (tr.finallyBlock() != null) {
                        last_visitor = tryFinally(v, last, last_visitor, tr.finallyBlock());
                        last = tr.finallyBlock();
                    }
                }
                cFGBuilder = v.outer;
            } else {
                edge(last_visitor, last, this.graph.exitNode(), FlowGraph.EDGE_KEY_OTHER);
                return;
            }
        }
    }

    public void visitGraph() {
        StringBuffer append = new StringBuffer().append(StringUtil.getShortNameComponent(this.df.getClass().getName()));
        int i = counter;
        counter = i + 1;
        String name = append.append(i).toString();
        if (Report.should_report(Report.cfg, 2)) {
            String rootName = "";
            if (this.graph.root() instanceof CodeDecl) {
                CodeDecl cd = (CodeDecl) this.graph.root();
                rootName = new StringBuffer().append(cd.codeInstance().toString()).append(" in ").append(cd.codeInstance().container().toString()).toString();
            }
            Report.report(2, new StringBuffer().append("digraph CFGBuild").append(name).append(" {").toString());
            Report.report(2, new StringBuffer().append("  label=\"CFGBuilder: ").append(name).append("\\n").append(rootName).append("\"; fontsize=20; center=true; ratio=auto; size = \"8.5,11\";").toString());
        }
        this.graph.peer(this.graph.entryNode(), Collections.EMPTY_LIST, this.df);
        this.graph.peer(this.graph.exitNode(), Collections.EMPTY_LIST, this.df);
        visitCFG(this.graph.root(), Collections.EMPTY_LIST);
        if (Report.should_report(Report.cfg, 2)) {
            Report.report(2, "}");
        }
    }

    public void visitCFGList(List elements, Term after) {
        Term prev = null;
        Iterator i = elements.iterator();
        while (i.hasNext()) {
            Term c = (Term) i.next();
            if (prev != null) {
                visitCFG(prev, c.entry());
            }
            prev = c;
        }
        if (prev != null) {
            visitCFG(prev, after);
        }
    }

    public void visitCFG(Term a, Term succ) {
        visitCFG(a, FlowGraph.EDGE_KEY_OTHER, succ);
    }

    public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey, Term succ) {
        visitCFG(a, CollectionUtil.list(new EdgeKeyTermPair(edgeKey, succ)));
    }

    public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey1, Term succ1, FlowGraph.EdgeKey edgeKey2, Term succ2) {
        visitCFG(a, CollectionUtil.list(new EdgeKeyTermPair(edgeKey1, succ1), new EdgeKeyTermPair(edgeKey2, succ2)));
    }

    public void visitCFG(Term a, FlowGraph.EdgeKey edgeKey, List succ) {
        List l = new ArrayList(2 * succ.size());
        Iterator iter = succ.iterator();
        while (iter.hasNext()) {
            l.add(new EdgeKeyTermPair(edgeKey, (Term) iter.next()));
        }
        visitCFG(a, l);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/CFGBuilder$EdgeKeyTermPair.class */
    public static class EdgeKeyTermPair {
        FlowGraph.EdgeKey edgeKey;
        Term term;

        public EdgeKeyTermPair(FlowGraph.EdgeKey edgeKey, Term term) {
            this.edgeKey = edgeKey;
            this.term = term;
        }

        public String toString() {
            return new StringBuffer().append("{edgeKey=").append(this.edgeKey).append(",term=").append(this.term).append("}").toString();
        }
    }

    protected void visitCFG(Term a, List succs) {
        if (Report.should_report(Report.cfg, 2)) {
            Report.report(2, new StringBuffer().append("// node ").append(a).append(" -> ").append(succs).toString());
        }
        for (EdgeKeyTermPair pair : a.acceptCFG(this, succs)) {
            edge(a, pair.term, pair.edgeKey);
        }
        visitThrow(a);
    }

    public void visitThrow(Term a) {
        for (Type type : a.del().throwTypes(this.ts)) {
            visitThrow(a, type);
        }
        if (((a instanceof Stmt) && !(a instanceof CompoundStmt)) || ((a instanceof Block) && ((Block) a).statements().isEmpty())) {
            visitThrow(a, this.ts.Error());
        }
    }

    public void visitThrow(Term t, Type type) {
        Term last = t;
        CFGBuilder last_visitor = this;
        CFGBuilder cFGBuilder = this;
        while (true) {
            CFGBuilder v = cFGBuilder;
            if (v != null) {
                Term c = v.innermostTarget;
                if (c instanceof Try) {
                    Try tr = (Try) c;
                    if (!v.skipInnermostCatches) {
                        boolean definiteCatch = false;
                        for (Catch cb : tr.catchBlocks()) {
                            if (type.isImplicitCastValid(cb.catchType())) {
                                edge(last_visitor, last, cb.entry(), new FlowGraph.ExceptionEdgeKey(type));
                                definiteCatch = true;
                            } else if (cb.catchType().isImplicitCastValid(type)) {
                                edge(last_visitor, last, cb.entry(), new FlowGraph.ExceptionEdgeKey(cb.catchType()));
                            }
                        }
                        if (definiteCatch) {
                            return;
                        }
                    }
                    if (tr.finallyBlock() != null) {
                        last_visitor = tryFinally(v, last, last_visitor, tr.finallyBlock());
                        last = tr.finallyBlock();
                    }
                }
                cFGBuilder = v.outer;
            } else if (this.errorEdgesToExitNode || !type.isSubtype(this.ts.Error())) {
                edge(last_visitor, last, this.graph.exitNode(), new FlowGraph.ExceptionEdgeKey(type));
                return;
            } else {
                return;
            }
        }
    }

    protected static CFGBuilder tryFinally(CFGBuilder v, Term last, CFGBuilder last_visitor, Block finallyBlock) {
        CFGBuilder v_ = v.outer.enterFinally(last);
        v_.edge(last_visitor, last, finallyBlock.entry(), FlowGraph.EDGE_KEY_OTHER);
        v_.visitCFG(finallyBlock, Collections.EMPTY_LIST);
        return v_;
    }

    protected CFGBuilder enterFinally(Term from) {
        CFGBuilder v = (CFGBuilder) copy();
        v.path_to_finally = new ArrayList(this.path_to_finally.size() + 1);
        v.path_to_finally.addAll(this.path_to_finally);
        v.path_to_finally.add(from);
        return v;
    }

    public void edge(Term p, Term q) {
        edge(this, p, q, FlowGraph.EDGE_KEY_OTHER);
    }

    public void edge(Term p, Term q, FlowGraph.EdgeKey edgeKey) {
        edge(this, p, q, edgeKey);
    }

    public void edge(CFGBuilder p_visitor, Term p, Term q, FlowGraph.EdgeKey edgeKey) {
        if (Report.should_report(Report.cfg, 2)) {
            Report.report(2, new StringBuffer().append("//     edge ").append(p).append(" -> ").append(q).toString());
        }
        FlowGraph.Peer pp = this.graph.peer(p, p_visitor.path_to_finally, this.df);
        FlowGraph.Peer pq = this.graph.peer(q, this.path_to_finally, this.df);
        if (Report.should_report(Report.cfg, 3)) {
            Report.report(2, new StringBuffer().append(pp.hashCode()).append(" [ label = \"").append(StringUtil.escape(pp.toString())).append("\" ];").toString());
            Report.report(2, new StringBuffer().append(pq.hashCode()).append(" [ label = \"").append(StringUtil.escape(pq.toString())).append("\" ];").toString());
        } else if (Report.should_report(Report.cfg, 2)) {
            Report.report(2, new StringBuffer().append(pp.hashCode()).append(" [ label = \"").append(StringUtil.escape(pp.node.toString())).append("\" ];").toString());
            Report.report(2, new StringBuffer().append(pq.hashCode()).append(" [ label = \"").append(StringUtil.escape(pq.node.toString())).append("\" ];").toString());
        }
        if (this.graph.forward()) {
            if (Report.should_report(Report.cfg, 2)) {
                Report.report(2, new StringBuffer().append(pp.hashCode()).append(" -> ").append(pq.hashCode()).append(" [label=\"").append(edgeKey).append("\"];").toString());
            }
            pp.succs.add(new FlowGraph.Edge(edgeKey, pq));
            pq.preds.add(new FlowGraph.Edge(edgeKey, pp));
            return;
        }
        if (Report.should_report(Report.cfg, 2)) {
            Report.report(2, new StringBuffer().append(pq.hashCode()).append(" -> ").append(pp.hashCode()).append(" [label=\"").append(edgeKey).append("\"];").toString());
        }
        pq.succs.add(new FlowGraph.Edge(edgeKey, pp));
        pp.preds.add(new FlowGraph.Edge(edgeKey, pq));
    }
}
