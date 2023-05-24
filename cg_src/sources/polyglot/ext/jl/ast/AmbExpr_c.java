package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.AmbExpr;
import polyglot.ast.Expr;
import polyglot.ast.Node;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.types.SemanticException;
import polyglot.util.CodeWriter;
import polyglot.util.InternalCompilerError;
import polyglot.util.Position;
import polyglot.visit.AmbiguityRemover;
import polyglot.visit.CFGBuilder;
import polyglot.visit.ExceptionChecker;
import polyglot.visit.PrettyPrinter;
import polyglot.visit.Translator;
import polyglot.visit.TypeChecker;
import soot.coffi.Instruction;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/AmbExpr_c.class */
public class AmbExpr_c extends Expr_c implements AmbExpr {
    protected String name;

    public AmbExpr_c(Position pos, String name) {
        super(pos);
        this.name = name;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ast.AmbExpr
    public String name() {
        return this.name;
    }

    @Override // polyglot.ast.AmbExpr
    public AmbExpr name(String name) {
        AmbExpr_c n = (AmbExpr_c) copy();
        n.name = name;
        return n;
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node disambiguate(AmbiguityRemover ar) throws SemanticException {
        Node n = ar.nodeFactory().disamb().disambiguate(this, ar, position(), null, this.name);
        if (n instanceof Expr) {
            return n;
        }
        throw new SemanticException(new StringBuffer().append("Could not find field or local variable \"").append(this.name).append("\".").toString(), position());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node typeCheck(TypeChecker tc) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot type check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public Node exceptionCheck(ExceptionChecker ec) throws SemanticException {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot exception check ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void prettyPrint(CodeWriter w, PrettyPrinter tr) {
        w.write(this.name);
    }

    @Override // polyglot.ext.jl.ast.Node_c, polyglot.ast.NodeOps
    public void translate(CodeWriter w, Translator tr) {
        throw new InternalCompilerError(position(), new StringBuffer().append("Cannot translate ambiguous node ").append(this).append(".").toString());
    }

    @Override // polyglot.ext.jl.ast.Node_c
    public String toString() {
        return new StringBuffer().append(this.name).append("{amb}").toString();
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ext.jl.ast.Node_c, polyglot.ast.Node
    public void dump(CodeWriter w) {
        super.dump(w);
        w.allowBreak(4, Instruction.argsep);
        w.begin(0);
        w.write(new StringBuffer().append("(name \"").append(this.name).append("\")").toString());
        w.end();
    }
}
