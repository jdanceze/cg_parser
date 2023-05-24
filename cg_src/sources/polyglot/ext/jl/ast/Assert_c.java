package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Assert;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Term;
import polyglot.main.Options;
import polyglot.types.SemanticException;
import polyglot.types.Type;
import polyglot.types.TypeSystem;
import polyglot.util.CodeWriter;
import polyglot.util.ErrorQueue;
import polyglot.util.Position;
import polyglot.visit.AscriptionVisitor;
import polyglot.visit.CFGBuilder;
import polyglot.visit.NodeVisitor;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Assert_c.class */
public class Assert_c extends Stmt_c implements Assert {
    protected Expr cond;
    protected Expr errorMessage;

    public Assert_c(Position pos, Expr cond, Expr errorMessage) {
        super(pos);
        this.cond = cond;
        this.errorMessage = errorMessage;
    }

    @Override // polyglot.ast.Assert
    public Expr cond() {
        return this.cond;
    }

    @Override // polyglot.ast.Assert
    public Assert cond(Expr cond) {
        Assert_c n = (Assert_c) copy();
        n.cond = cond;
        return n;
    }

    @Override // polyglot.ast.Assert
    public Expr errorMessage() {
        return this.errorMessage;
    }

    @Override // polyglot.ast.Assert
    public Assert errorMessage(Expr errorMessage) {
        Assert_c n = (Assert_c) copy();
        n.errorMessage = errorMessage;
        return n;
    }

    protected Assert_c reconstruct(Expr cond, Expr errorMessage) {
        if (cond != this.cond || errorMessage != this.errorMessage) {
            Assert_c n = (Assert_c) copy();
            n.cond = cond;
            n.errorMessage = errorMessage;
            return n;
        }
        return this;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        TypeSystem ts = tc.typeSystem();
        if (!Options.global.assertions) {
            ErrorQueue eq = tc.errorQueue();
            eq.enqueue(0, "assert statements are disabled. Recompile with -assert and ensure the post compiler supports assert (e.g., -post \"javac -source 1.4\"). Removing the statement and continuing.", this.cond.position());
        }
        if (!ts.equals(this.cond.type(), ts.Boolean())) {
            throw new SemanticException("Condition of assert statement must have boolean type.", this.cond.position());
        }
        if (this.errorMessage != null && ts.equals(this.errorMessage.type(), ts.Void())) {
            throw new SemanticException("Error message in assert statement must have a value.", this.errorMessage.position());
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

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node visitChildren(NodeVisitor v) {
        Expr cond = (Expr) visitChild(this.cond, v);
        Expr errorMessage = (Expr) visitChild(this.errorMessage, v);
        return reconstruct(cond, errorMessage);
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append("assert ").append(this.cond.toString()).append(this.errorMessage != null ? new StringBuffer().append(": ").append(this.errorMessage.toString()).toString() : "").append(";").toString();
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write("assert ");
        print(this.cond, w, tr);
        if (this.errorMessage != null) {
            w.write(": ");
            print(this.errorMessage, w, tr);
        }
        w.write(";");
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        if (!Options.global.assertions) {
            w.write(";");
        } else {
            prettyPrint(w, tr);
        }
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.cond.entry();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        if (this.errorMessage != null) {
            v.visitCFG(this.cond, this.errorMessage.entry());
            v.visitCFG(this.errorMessage, this);
        } else {
            v.visitCFG(this.cond, this);
        }
        return succs;
    }
}
