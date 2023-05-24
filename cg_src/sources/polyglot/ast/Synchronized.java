package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Synchronized.class */
public interface Synchronized extends CompoundStmt {
    Expr expr();

    Synchronized expr(Expr expr);

    Block body();

    Synchronized body(Block block);
}
