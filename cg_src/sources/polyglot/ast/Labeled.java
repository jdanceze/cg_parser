package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Labeled.class */
public interface Labeled extends CompoundStmt {
    String label();

    Labeled label(String str);

    Stmt statement();

    Labeled statement(Stmt stmt);
}
