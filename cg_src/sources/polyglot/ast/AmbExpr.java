package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/AmbExpr.class */
public interface AmbExpr extends Expr, Ambiguous {
    String name();

    AmbExpr name(String str);
}
