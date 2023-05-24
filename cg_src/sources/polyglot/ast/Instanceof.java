package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Instanceof.class */
public interface Instanceof extends Expr {
    Expr expr();

    Instanceof expr(Expr expr);

    TypeNode compareType();

    Instanceof compareType(TypeNode typeNode);
}
