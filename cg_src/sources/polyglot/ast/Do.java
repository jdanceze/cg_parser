package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Do.class */
public interface Do extends Loop {
    Do body(Stmt stmt);

    Do cond(Expr expr);
}
