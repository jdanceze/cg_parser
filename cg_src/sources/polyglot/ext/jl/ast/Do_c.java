package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Do;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
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
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Do_c.class */
public class Do_c extends Loop_c implements Do {
    protected Stmt body;
    protected Expr cond;

    public Do_c(Position pos, Stmt body, Expr cond) {
        super(pos);
        this.body = body;
        this.cond = cond;
    }

    @Override // polyglot.ast.Loop
    public Stmt body() {
        return this.body;
    }

    @Override // polyglot.ast.Do
    public Do body(Stmt body) {
        Do_c n = (Do_c) copy();
        n.body = body;
        return n;
    }

    @Override // polyglot.ast.Loop
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.Do
    public Do cond(Expr cond) {
        Do_c n = (Do_c) copy();
        n.cond = cond;
        return n;
    }

    protected Do_c reconstruct(Stmt body, Expr cond) {
        if (body != this.body || cond != this.cond) {
            Do_c n = (Do_c) copy();
            n.body = body;
            n.cond = cond;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Stmt body = (Stmt) visitChild(this.body, v);
        Expr cond = (Expr) visitChild(this.cond, v);
        return reconstruct(body, cond);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.equals(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("Condition of do statement must have boolean type.", this.cond.position());
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
        return new StringBuffer().append("do { ... } while (").append(this.cond).append(")").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("do ");
        printSubStmt(this.body, w, tr);
        w.write("while(");
        printBlock(this.cond, w, tr);
        w.write("); ");
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.body.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.push(this).visitCFG(this.body, this.cond.entry());
        if (condIsConstantTrue()) {
            v.visitCFG(this.cond, this.body.entry());
        } else {
            v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.body.entry(), FlowGraph.EDGE_KEY_FALSE, this);
        }
        return succs;
    }

    @Override // polyglot.ast.Loop
    public Term continueTarget() {
        return this.cond.entry();
    }
}
