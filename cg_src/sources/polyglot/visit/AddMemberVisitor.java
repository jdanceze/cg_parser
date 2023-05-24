package polyglot.visit;

import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.SemanticException;
import polyglot.types.TypeSystem;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/AddMemberVisitor.class */
public class AddMemberVisitor extends ContextVisitor {
    public AddMemberVisitor(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        if (Report.should_report(Report.visit, 4)) {
            Report.report(4, new StringBuffer().append(">> AddMemberVisitor::enter ").append(n).toString());
        }
        return n.del().addMembersEnter(this);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        if (Report.should_report(Report.visit, 4)) {
            Report.report(4, new StringBuffer().append("<< AddMemberVisitor::leave ").append(n).toString());
        }
        return n.del().addMembers((AddMemberVisitor) v);
    }
}
