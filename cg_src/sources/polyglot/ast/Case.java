package polyglot.ast;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Case.class */
public interface Case extends SwitchElement {
    Expr expr();

    Case expr(Expr expr);

    boolean isDefault();

    long value();

    Case value(long j);
}
