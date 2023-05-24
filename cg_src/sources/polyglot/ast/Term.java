package polyglot.ast;

import java.util.List;
import polyglot.util.SubtypeSet;
import polyglot.visit.CFGBuilder;
/* loaded from: gencallgraphv3.jar:polyglot-2006.jar:polyglot/ast/Term.class */
public interface Term extends Node {
    Term entry();

    List acceptCFG(CFGBuilder cFGBuilder, List list);

    boolean reachable();

    Term reachable(boolean z);

    SubtypeSet exceptions();

    Term exceptions(SubtypeSet subtypeSet);
}
