package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Conditional.class */
public interface Conditional extends Expr {
    Expr cond();

    Conditional cond(Expr expr);

    Expr consequent();

    Conditional consequent(Expr expr);

    Expr alternative();

    Conditional alternative(Expr expr);
}
