package polyglot.visit;

import polyglot.ast.ClassDecl;
import polyglot.ast.ClassMember;
import polyglot.ast.Import;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.ast.SourceFile;
import polyglot.ast.Stmt;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ErrorHandlingVisitor.class */
public class ErrorHandlingVisitor extends HaltingVisitor {
    protected boolean error;
    protected Job job;
    protected TypeSystem ts;
    protected NodeFactory nf;

    public ErrorHandlingVisitor(Job job, TypeSystem ts, NodeFactory nf) {
        this.job = job;
        this.ts = ts;
        this.nf = nf;
    }

    public Job job() {
        return this.job;
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor begin() {
        this.error = false;
        return super.begin();
    }

    public ErrorQueue errorQueue() {
        return this.job.compiler().errorQueue();
    }

    public NodeFactory nodeFactory() {
        return this.nf;
    }

    public TypeSystem typeSystem() {
        return this.ts;
    }

    protected NodeVisitor enterCall(Node parent, Node n) throws SemanticException {
        if (Report.should_report(Report.visit, 3)) {
            Report.report(3, new StringBuffer().append("enter: ").append(parent).append(" -> ").append(n).toString());
        }
        return enterCall(n);
    }

    protected NodeVisitor enterCall(Node n) throws SemanticException {
        return this;
    }

    protected NodeVisitor enterError(Node n) {
        return this;
    }

    protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        return leaveCall(n);
    }

    protected Node leaveCall(Node n) throws SemanticException {
        return n;
    }

    protected boolean catchErrors(Node n) {
        return (n instanceof Stmt) || (n instanceof ClassMember) || (n instanceof ClassDecl) || (n instanceof Import) || (n instanceof SourceFile);
    }

    @Override // polyglot.visit.NodeVisitor
    public NodeVisitor enter(Node parent, Node n) {
        if (Report.should_report(Report.visit, 5)) {
            Report.report(5, new StringBuffer().append("enter(").append(n).append(")").toString());
        }
        if (catchErrors(n)) {
            this.error = false;
        }
        try {
            return (ErrorHandlingVisitor) enterCall(parent, n);
        } catch (SemanticException e) {
            if (e.getMessage() != null) {
                Position position = e.position();
                if (position == null) {
                    position = n.position();
                }
                errorQueue().enqueue(5, e.getMessage(), position);
            }
            if (!catchErrors(n)) {
                this.error = true;
            }
            return enterError(n);
        }
    }

    @Override // polyglot.visit.NodeVisitor
    public Node leave(Node parent, Node old, Node n, NodeVisitor v) {
        try {
            if ((v instanceof ErrorHandlingVisitor) && ((ErrorHandlingVisitor) v).error) {
                if (Report.should_report(Report.visit, 5)) {
                    Report.report(5, new StringBuffer().append("leave(").append(n).append("): error below").toString());
                }
                if (catchErrors(n)) {
                    this.error = false;
                    ((ErrorHandlingVisitor) v).error = false;
                } else {
                    this.error = true;
                }
                return n;
            }
            if (Report.should_report(Report.visit, 5)) {
                Report.report(5, new StringBuffer().append("leave(").append(n).append("): calling leaveCall").toString());
            }
            return leaveCall(old, n, v);
        } catch (SemanticException e) {
            if (e.getMessage() != null) {
                Position position = e.position();
                if (position == null) {
                    position = n.position();
                }
                errorQueue().enqueue(5, e.getMessage(), position);
            }
            if (catchErrors(n)) {
                this.error = false;
                ((ErrorHandlingVisitor) v).error = false;
            } else {
                this.error = true;
                ((ErrorHandlingVisitor) v).error = true;
            }
            return n;
        }
    }
}
