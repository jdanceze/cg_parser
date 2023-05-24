package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Eval.class */
public interface Eval extends ForInit, ForUpdate {
    Expr expr();

    Eval expr(Expr expr);
}
