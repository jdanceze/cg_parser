package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/While.class */
public interface While extends Loop {
    While cond(Expr expr);

    While body(Stmt stmt);
}
