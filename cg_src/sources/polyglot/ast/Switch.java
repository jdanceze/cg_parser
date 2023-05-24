package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Switch.class */
public interface Switch extends CompoundStmt {
    Expr expr();

    Switch expr(Expr expr);

    List elements();

    Switch elements(List list);
}
