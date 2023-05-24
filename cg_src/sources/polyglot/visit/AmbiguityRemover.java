package polyglot.visit;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.main.Report;
import polyglot.types.ClassType;
import polyglot.types.ParsedClassType;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.Enum;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/AmbiguityRemover.class */
public class AmbiguityRemover extends ContextVisitor {
    public static final Kind SUPER = new Kind("disam-super");
    public static final Kind SIGNATURES = new Kind("disam-sigs");
    public static final Kind FIELDS = new Kind("disam-fields");
    public static final Kind ALL = new Kind("disam-all");
    protected Kind kind;

    /* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/AmbiguityRemover$Kind.class */
    public static class Kind extends Enum {
        protected Kind(String name) {
            super(name);
        }
    }

    public AmbiguityRemover(Job job, TypeSystem ts, NodeFactory nf, Kind kind) {
        super(job, ts, nf);
        this.kind = kind;
    }

    public Kind kind() {
        return this.kind;
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        if (Report.should_report(Report.visit, 2)) {
            Report.report(2, new StringBuffer().append(">> ").append(this.kind).append("::enter ").append(n).toString());
        }
        NodeVisitor v = n.del().disambiguateEnter(this);
        if (Report.should_report(Report.visit, 2)) {
            Report.report(2, new StringBuffer().append("<< ").append(this.kind).append("::enter ").append(n).append(" -> ").append(v).toString());
        }
        return v;
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        if (Report.should_report(Report.visit, 2)) {
            Report.report(2, new StringBuffer().append(">> ").append(this.kind).append("::leave ").append(n).toString());
        }
        Node m = n.del().disambiguate((AmbiguityRemover) v);
        if (Report.should_report(Report.visit, 2)) {
            Report.report(2, new StringBuffer().append("<< ").append(this.kind).append("::leave ").append(n).append(" -> ").append(m).toString());
        }
        return m;
    }

    public void addSuperDependencies(ClassType ct) {
        Set seen = new HashSet();
        Stack s = new Stack();
        s.push(ct);
        while (!s.isEmpty()) {
            Type t = (Type) s.pop();
            if (t.isClass()) {
                ClassType classt = t.toClass();
                if (!seen.contains(classt)) {
                    seen.add(classt);
                    if (classt instanceof ParsedClassType) {
                        job().extensionInfo().addDependencyToCurrentJob(((ParsedClassType) classt).fromSource());
                    }
                    for (Object obj : classt.interfaces()) {
                        s.push(obj);
                    }
                    if (classt.superType() != null) {
                        s.push(classt.superType());
                    }
                }
            }
        }
    }

    @Override // polyglot.visit.NodeVisitor
    public String toString() {
        return new StringBuffer().append(super.toString()).append("(").append(this.kind).append(")").toString();
    }
}
