package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Labeled;
import polyglot.ast.Node;
import polyglot.ast.Stmt;
import polyglot.ast.Term;
import polyglot.util.CodeWriter;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Labeled_c.class */
public class Labeled_c extends Stmt_c implements Labeled {
    protected String label;
    protected Stmt statement;

    public Labeled_c(Position pos, String label, Stmt statement) {
        super(pos);
        this.label = label;
        this.statement = statement;
    }

    @Override // polyglot.ast.Labeled
    public String label() {
        return this.label;
    }

    @Override // polyglot.ast.Labeled
    public Labeled label(String label) {
        Labeled_c n = (Labeled_c) copy();
        n.label = label;
        return n;
    }

    @Override // polyglot.ast.Labeled
    public Stmt statement() {
        return this.statement;
    }

    @Override // polyglot.ast.Labeled
    public Labeled statement(Stmt statement) {
        Labeled_c n = (Labeled_c) copy();
        n.statement = statement;
        return n;
    }

    protected Labeled_c reconstruct(Stmt statement) {
        if (statement != this.statement) {
            Labeled_c n = (Labeled_c) copy();
            n.statement = statement;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Stmt statement = (Stmt) visitChild(this.statement, v);
        return reconstruct(statement);
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.label).append(": ").append(this.statement).toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(new StringBuffer().append(this.label).append(": ").toString());
        print(this.statement, w, tr);
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.statement.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        v.push(this).visitCFG(this.statement, this);
        return succs;
    }
}
