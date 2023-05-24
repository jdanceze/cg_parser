package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Cast.class */
public interface Cast extends Expr {
    TypeNode castType();

    Cast castType(TypeNode typeNode);

    Expr expr();

    Cast expr(Expr expr);
}
