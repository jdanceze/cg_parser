package polyglot.ext.jl.ast;

import java.util.Collections;
import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.ast.Throw;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.CollectionUtil;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Throw_c.class */
public class Throw_c extends Stmt_c implements Throw {
    protected Expr expr;

    public Throw_c(Position pos, Expr expr) {
        super(pos);
        this.expr = expr;
    }

    @Override // polyglot.ast.Throw
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Throw
    public Throw expr(Expr expr) {
        Throw_c n = (Throw_c) copy();
        n.expr = expr;
        return n;
    }

    protected Throw_c reconstruct(Expr expr) {
        if (expr != this.expr) {
            Throw_c n = (Throw_c) copy();
            n.expr = expr;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        return reconstruct(expr);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        if (!this.expr.type().isThrowable()) {
            throw new SemanticException(new StringBuffer().append("Can only throw subclasses of \"").append(tc.typeSystem().Throwable()).append("\".").toString(), this.expr.position());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Throwable();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("throw ").append(this.expr).append(";").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("throw ");
        print(this.expr, w, tr);
        w.write(";");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.expr, this);
        return Collections.EMPTY_LIST;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public List throwTypes(TypeSystem ts) {
        return CollectionUtil.list(this.expr.type(), ts.NullPointerException());
    }
}
