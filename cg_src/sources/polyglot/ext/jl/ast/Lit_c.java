package polyglot.ext.jl.ast;

import java.util.List;
import polyglot.ast.Lit;
import polyglot.ast.Precedence;
import polyglot.ast.Term;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ext/jl/ast/Lit_c.class */
public abstract class Lit_c extends Expr_c implements Lit {
    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public abstract Object constantValue();

    public Lit_c(Position pos) {
        super(pos);
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public Precedence precedence() {
        return Precedence.LITERAL;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Expr_c, polyglot.ast.Expr
    public boolean isConstant() {
        return true;
    }
}
