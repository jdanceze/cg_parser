package soot.javaToJimple.jj.ast;

import java.util.List;
import polyglot.ast.Expr;
import polyglot.ast.Term;
import polyglot.ext.jl.ast.Expr_c;
import polyglot.util.Position;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:soot/javaToJimple/jj/ast/JjComma_c.class */
public class JjComma_c extends Expr_c implements Expr {
    private Expr first;
    private Expr second;

    public JjComma_c(Position pos, Expr first, Expr second) {
        super(pos);
        this.first = first;
        this.second = second;
    }

    public Expr first() {
        return this.first;
    }

    public Expr second() {
        return this.second;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public List acceptCFG(CFGBuilder v, List succs) {
        return succs;
    }

    @Override // polyglot.ext.jl.ast.Term_c, polyglot.ast.Term
    public Term entry() {
        return this.first.entry();
    }
}
