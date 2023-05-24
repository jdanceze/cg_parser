package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/For.class */
public interface For extends Loop {
    List inits();

    For inits(List list);

    For cond(Expr expr);

    List iters();

    For iters(List list);

    For body(Stmt stmt);
}
