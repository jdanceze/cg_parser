package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/If.class */
public interface If extends CompoundStmt {
    Expr cond();

    If cond(Expr expr);

    Stmt consequent();

    If consequent(Stmt stmt);

    Stmt alternative();

    If alternative(Stmt stmt);
}
