package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Assert.class */
public interface Assert extends Stmt {
    Expr cond();

    Assert cond(Expr expr);

    Expr errorMessage();

    Assert errorMessage(Expr expr);
}
