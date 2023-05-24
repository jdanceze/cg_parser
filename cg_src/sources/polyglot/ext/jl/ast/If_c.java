package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Block;
import polyglot.ast.Expr;
import polyglot.ast.If;
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
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/If_c.class */
public class If_c extends Stmt_c implements If {
    protected Expr cond;
    protected Stmt consequent;
    protected Stmt alternative;

    public If_c(Position pos, Expr cond, Stmt consequent, Stmt alternative) {
        super(pos);
        this.cond = cond;
        this.consequent = consequent;
        this.alternative = alternative;
    }

    @Override // polyglot.ast.If
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.If
    public If cond(Expr cond) {
        If_c n = (If_c) copy();
        n.cond = cond;
        return n;
    }

    @Override // polyglot.ast.If
    public Stmt consequent() {
        return this.consequent;
    }

    @Override // polyglot.ast.If
    public If consequent(Stmt consequent) {
        If_c n = (If_c) copy();
        n.consequent = consequent;
        return n;
    }

    @Override // polyglot.ast.If
    public Stmt alternative() {
        return this.alternative;
    }

    @Override // polyglot.ast.If
    public If alternative(Stmt alternative) {
        If_c n = (If_c) copy();
        n.alternative = alternative;
        return n;
    }

    protected If_c reconstruct(Expr cond, Stmt consequent, Stmt alternative) {
        if (cond != this.cond || consequent != this.consequent || alternative != this.alternative) {
            If_c n = (If_c) copy();
            n.cond = cond;
            n.consequent = consequent;
            n.alternative = alternative;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr cond = (Expr) visitChild(this.cond, v);
        Stmt consequent = (Stmt) visitChild(this.consequent, v);
        Stmt alternative = (Stmt) visitChild(this.alternative, v);
        return reconstruct(cond, consequent, alternative);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!ts.equals(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("Condition of if statement must have boolean type.", this.cond.position());
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
        return new StringBuffer().append("if (").append(this.cond).append(") ").append(this.consequent).append(this.alternative != null ? new StringBuffer().append(" else ").append(this.alternative).toString() : "").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("if (");
        printBlock(this.cond, w, tr);
        w.write(")");
        printSubStmt(this.consequent, w, tr);
        if (this.alternative != null) {
            if (this.consequent instanceof Block) {
                w.write(Instruction.argsep);
            } else {
                w.allowBreak(0, Instruction.argsep);
            }
            w.write("else");
            printSubStmt(this.alternative, w, tr);
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.cond.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.alternative == null) {
            v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this);
            v.visitCFG(this.consequent, this);
        } else {
            v.visitCFG(this.cond, FlowGraph.EDGE_KEY_TRUE, this.consequent.entry(), FlowGraph.EDGE_KEY_FALSE, this.alternative.entry());
            v.visitCFG(this.consequent, this);
            v.visitCFG(this.alternative, this);
        }
        return succs;
    }
}
