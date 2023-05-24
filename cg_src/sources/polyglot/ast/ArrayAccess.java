package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/ArrayAccess.class */
public interface ArrayAccess extends Variable {
    Expr array();

    ArrayAccess array(Expr expr);

    Expr index();

    ArrayAccess index(Expr expr);
}
