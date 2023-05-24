package polyglot.ast;

import java.util.List;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Block.class */
public interface Block extends CompoundStmt {
    List statements();

    Block statements(List list);

    Block append(Stmt stmt);

    Block prepend(Stmt stmt);
}
