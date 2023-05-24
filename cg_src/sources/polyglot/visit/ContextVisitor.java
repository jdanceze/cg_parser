package polyglot.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.Context;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ContextVisitor.class */
public class ContextVisitor extends ErrorHandlingVisitor {
    protected ContextVisitor outer;
    protected Context context;

    public ContextVisitor(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
        this.outer = null;
        this.context = null;
    }

    @Override // polyglot.visit.ErrorHandlingVisitor, polyglot.visit.NodeVisitor
    public NodeVisitor begin() {
        this.context = this.job.context();
        if (this.context == null) {
            this.context = this.ts.createContext();
        }
        this.outer = null;
        return super.begin();
    }

    public Context context() {
        return this.context;
    }

    public ContextVisitor context(Context c) {
        ContextVisitor v = (ContextVisitor) copy();
        v.context = c;
        return v;
    }

    protected Context enterScope(Node parent, Node n) {
        if (parent != null) {
            return parent.del().enterScope(n, this.context);
        }
        return n.del().enterScope(this.context);
    }

    protected void addDecls(Node n) {
        n.addDecls(this.context);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor, polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (Report.should_report(Report.visit, 5)) {
            Report.report(5, new StringBuffer().append("enter(").append(n).append(")").toString());
        }
        ContextVisitor v = this;
        Context c = enterScope(parent, n);
        if (c != this.context) {
            v = (ContextVisitor) copy();
            v.context = c;
            v.outer = this;
            v.error = false;
        }
        return v.superEnter(parent, n);
    }

    public NodeVisitor superEnter(Node parent, Node n) {
        return super.enter(parent, n);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor, polyglot.visit.NodeVisitor
    public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
        Node m = super.leave(parent, old, n, v);
        addDecls(m);
        return m;
    }
}
