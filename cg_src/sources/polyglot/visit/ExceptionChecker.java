package polyglot.visit;

import java.util.HashMap;
import java.util.Map;
import polyglot.ast.Node;
import polyglot.ast.NodeFactory;
import polyglot.frontend.Job;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.util.SubtypeSet;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/visit/ExceptionChecker.class */
public class ExceptionChecker extends ErrorHandlingVisitor {
    protected ExceptionChecker outer;
    private SubtypeSet scope;
    protected Map exceptionPositions;

    public ExceptionChecker(Job job, TypeSystem ts, NodeFactory nf) {
        super(job, ts, nf);
        this.scope = null;
        this.outer = null;
        this.exceptionPositions = new HashMap();
    }

    public ExceptionChecker pushNew() {
        ExceptionChecker ec = (ExceptionChecker) visitChildren();
        ec.scope = null;
        ec.outer = this;
        ec.exceptionPositions = new HashMap();
        return ec;
    }

    public ExceptionChecker push() {
        ExceptionChecker ec = (ExceptionChecker) visitChildren();
        ec.outer = this;
        ec.exceptionPositions = new HashMap();
        return ec;
    }

    public ExceptionChecker pop() {
        return this.outer;
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterCall(Node n) throws SemanticException {
        return n.exceptionCheckEnter(this);
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected NodeVisitor enterError(Node n) {
        return push();
    }

    @Override // polyglot.visit.ErrorHandlingVisitor
    protected Node leaveCall(Node old, Node n, NodeVisitor v) throws SemanticException {
        ExceptionChecker inner = (ExceptionChecker) v;
        if (inner.outer != this) {
            throw new InternalCompilerError("oops!");
        }
        Node n2 = n.del().exceptionCheck(inner);
        SubtypeSet t = inner.throwsSet();
        throwsSet().addAll(t);
        this.exceptionPositions.putAll(inner.exceptionPositions);
        return n2;
    }

    public void throwsException(Type t, Position pos) {
        throwsSet().add(t);
        this.exceptionPositions.put(t, pos);
    }

    public SubtypeSet throwsSet() {
        if (this.scope == null) {
            this.scope = new SubtypeSet(this.ts.Throwable());
        }
        return this.scope;
    }

    public Position exceptionPosition(Type t) {
        return (Position) this.exceptionPositions.get(t);
    }
}
