package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Eval;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Eval_c.class */
public class Eval_c extends Stmt_c implements Eval {
    protected Expr expr;

    public Eval_c(Position pos, Expr expr) {
        super(pos);
        this.expr = expr;
    }

    @Override // polyglot.ast.Eval
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Eval
    public Eval expr(Expr expr) {
        Eval_c n = (Eval_c) copy();
        n.expr = expr;
        return n;
    }

    protected Eval_c reconstruct(Expr expr) {
        if (expr != this.expr) {
            Eval_c n = (Eval_c) copy();
            n.expr = expr;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Void();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        return reconstruct(expr);
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("eval(").append(this.expr.toString()).append(");").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        boolean semi = tr.appendSemicolon(true);
        print(this.expr, w, tr);
        if (semi) {
            w.write(";");
        }
        tr.appendSemicolon(semi);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.expr, this);
        return succs;
    }
}
