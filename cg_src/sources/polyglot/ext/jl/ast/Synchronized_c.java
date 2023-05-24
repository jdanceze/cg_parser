package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Synchronized;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Synchronized_c.class */
public class Synchronized_c extends Stmt_c implements Synchronized {
    protected Expr expr;
    protected Block body;

    public Synchronized_c(Position pos, Expr expr, Block body) {
        super(pos);
        this.expr = expr;
        this.body = body;
    }

    @Override // polyglot.ast.Synchronized
    public Expr expr() {
        return this.expr;
    }

    @Override // polyglot.ast.Synchronized
    public Synchronized expr(Expr expr) {
        Synchronized_c n = (Synchronized_c) copy();
        n.expr = expr;
        return n;
    }

    @Override // polyglot.ast.Synchronized
    public Block body() {
        return this.body;
    }

    @Override // polyglot.ast.Synchronized
    public Synchronized body(Block body) {
        Synchronized_c n = (Synchronized_c) copy();
        n.body = body;
        return n;
    }

    protected Synchronized_c reconstruct(Expr expr, Block body) {
        if (expr != this.expr || body != this.body) {
            Synchronized_c n = (Synchronized_c) copy();
            n.expr = expr;
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr expr = (Expr) visitChild(this.expr, v);
        Block body = (Block) visitChild(this.body, v);
        return reconstruct(expr, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.isSubtype(this.expr.type(), ts.Object())) {
            throw new SemanticException(new StringBuffer().append("Cannot synchronize on an expression of type \"").append(this.expr.type()).append("\".").toString(), this.expr.position());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.expr) {
            return ts.Object();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("synchronized (").append(this.expr).append(") { ... }").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("synchronized (");
        printBlock(this.expr, w, tr);
        w.write(") ");
        printSubStmt(this.body, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.expr.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.visitCFG(this.expr, this.body.entry());
        v.visitCFG(this.body, this);
        return succs;
    }
}
