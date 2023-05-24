package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.ast.While;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.FlowGraph;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/While_c.class */
public class While_c extends Loop_c implements While {
    protected Expr cond;
    protected Stmt body;

    public While_c(Position pos, Expr cond, Stmt body) {
        super(pos);
        this.cond = cond;
        this.body = body;
    }

    @Override // polyglot.ast.Loop
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.While
    public While cond(Expr cond) {
        While_c n = (While_c) copy();
        n.cond = cond;
        return n;
    }

    @Override // polyglot.ast.Loop
    public Stmt body() {
        return this.body;
    }

    @Override // polyglot.ast.While
    public While body(Stmt body) {
        While_c n = (While_c) copy();
        n.body = body;
        return n;
    }

    protected While_c reconstruct(Expr cond, Stmt body) {
        if (cond != this.cond || body != this.body) {
            While_c n = (While_c) copy();
            n.cond = cond;
            n.body = body;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr cond = (Expr) visitChild(this.cond, v);
        Stmt body = (Stmt) visitChild(this.body, v);
        return reconstruct(cond, body);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.equals(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("Condition of while statement must have boolean type.", this.cond.position());
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public Type childExpectedType(Expr child, AscriptionVisitor av) {
        TypeSystem ts = av.typeSystem();
        if (child == this.cond) {
            return ts.Boolean();
        }
        return child.type();
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("while (").append(this.cond).append(") ...").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("while (");
        printBlock(this.cond, w, tr);
        w.write(")");
        printSubStmt(this.body, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.cond.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (condIsConstantTrue()) {
            v.visitCFG(this.cond, this.body.entry());
        } else {
            v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
        }
        v.push(this).visitCFG(this.body, this.cond.entry());
        return succs;
    }

    @Override // polyglot.ast.Loop
    public Term continueTarget() {
        return this.cond.entry();
    }
}
