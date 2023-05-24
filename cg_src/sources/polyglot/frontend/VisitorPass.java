package polyglot.frontend;

import polyglot.ast.Node;
import polyglot.frontend.Pass;
import polyglot.main.Report;
import polyglot.util.ErrorQueue;
import polyglot.util.InternalCompilerError;
import polyglot.visit.NodeVisitor;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/frontend/VisitorPass.class */
public class VisitorPass extends AbstractPass {
    Job job;
    NodeVisitor v;

    public VisitorPass(Pass.ID id, Job job) {
        this(id, job, null);
    }

    public VisitorPass(Pass.ID id, Job job, NodeVisitor v) {
        super(id);
        this.job = job;
        this.v = v;
    }

    public void visitor(NodeVisitor v) {
        this.v = v;
    }

    public NodeVisitor visitor() {
        return this.v;
    }

    @Override // polyglot.frontend.AbstractPass, polyglot.frontend.Pass
    public boolean run() {
        Node ast = this.job.ast();
        if (ast == null) {
            throw new InternalCompilerError("Null AST: did the parser run?");
        }
        NodeVisitor v_ = this.v.begin();
        if (v_ != null) {
            ErrorQueue q = this.job.compiler().errorQueue();
            int nErrsBefore = q.errorCount();
            if (Report.should_report(Report.frontend, 3)) {
                Report.report(3, new StringBuffer().append("Running ").append(v_).append(" on ").append(ast).toString());
            }
            Node ast2 = ast.visit(v_);
            v_.finish(ast2);
            int nErrsAfter = q.errorCount();
            this.job.ast(ast2);
            return nErrsBefore == nErrsAfter;
        }
        return false;
    }

    @Override // polyglot.frontend.AbstractPass
    public String toString() {
        return this.id.toString();
    }
}
