package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Loop.class */
public interface Loop extends CompoundStmt {
    Expr cond();

    boolean condIsConstant();

    boolean condIsConstantTrue();

    Stmt body();

    Term continueTarget();
}
